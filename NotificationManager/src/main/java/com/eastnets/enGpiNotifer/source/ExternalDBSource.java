package com.eastnets.enGpiNotifer.source;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiNotifer.Beans.DBViewBean;
import com.eastnets.enGpiNotifer.Beans.GbiHistoryBean;
import com.eastnets.enGpiNotifer.DAO.ExternalDAO;
import com.eastnets.enGpiNotifer.DAO.LoaderDAO;
import com.eastnets.enGpiNotifer.JMS.JMSBrowse;
import com.eastnets.enGpiNotifer.Notifaction.Notifaction;
import com.eastnets.enGpiNotifer.builder.GpiQueryBulder;
import com.eastnets.enGpiNotifer.config.AppConfigBean;
import com.eastnets.enGpiNotifer.parser.GenerateMTTypes;
import com.eastnets.enGpiNotifer.parser.MTType;
import com.eastnets.enGpiNotifer.parser.RjeMessageParser;
import com.eastnets.enGpiNotifer.service.GpiService;
import com.eastnets.enGpiNotifer.utility.DataSourceParser;
import com.eastnets.enGpiNotifer.utility.PermissionHandler;
import com.eastnets.enGpiNotifer.utility.XMLDataSouceHelper;
import com.eastnets.enGpiNotifer.utility.XMLNotifierConfigHelper;
import com.eastnets.service.ServiceLocator;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import oracle.jdbc.pool.OracleDataSource;

public class ExternalDBSource implements MessageSource {

	private OracleDataSource datasource;
	private JtdsDataSource jtdsDataSource;
	private AppConfigBean appConfigBean = null;
	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private JMSBrowse jmsBrowse;
	List<NotifierMessage> gpiMessegesPendenig = null;
	List<NotifierMessage> resultMessages = null;
	private Map<String, MTType> mtTypesMap = null;

	private static final Logger LOGGER = Logger.getLogger(MqSource.class);
	SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Map<String, GbiHistoryBean> historyMap = new HashMap<>();
	private RjeMessageParser rjeMessageParser;
	String curuntDate = "";
	private Connection conn;
	private ExternalDAO externalDAO;
	String query = "";
	private Notifaction notifaction;
	private PermissionHandler permissionHandler;

	@Override
	public void startSourceMonotoring(DataSourceParser dataSourceParser) throws Exception {
		permissionHandler.checkNotifationMangerLic(dataSourceParser.getxMLNotifierConfigHelper().isEnabelUC());
		permissionHandler.checkNotifationMangerUserRoals(dataSourceParser.getXmlDataSource().getUsername());
		LOGGER.info("Start Notification Manager Job ....");
		initDataSource(dataSourceParser);
		externalDAO = new ExternalDAO(conn);
		query = GpiQueryBulder.buildQueryForExtirnalDB(dataSourceParser);
		List<NotifierMessage> gpiMessegesPendenig = null;
		mtTypesMap = GenerateMTTypes.getMtTypesMap();
		rjeMessageParser = new RjeMessageParser(mtTypesMap, serviceLocator);
		while (true) {
			try {
				gpiMessegesPendenig = getPendingViewMessage(externalDAO.getPendingExternalPendingMsg("", query, dataSourceParser), dataSourceParser);
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while getPendingGpiMesg() SQL Exception ", null);
				e.printStackTrace();
			}
			if (gpiMessegesPendenig != null && !gpiMessegesPendenig.isEmpty()) {
				LOGGER.debug("Start notification cycle for pending messages    ...");
				notifaction.notify(dataSourceParser, gpiMessegesPendenig, rjeMessageParser);
				LOGGER.debug("End notification cycle for pending Messages    ...");
			} else {
				LOGGER.debug("No message pending in queue ...");
			}
			try {
				Long sleepPeriod = dataSourceParser.getxMLNotifierConfigHelper().getSleepPeriod();
				LOGGER.info("Notifier  will sleep for " + sleepPeriod + " Seconds");
				// Re connect after close a connection in case oracle DB & SQL SERVER
				initDataSource(dataSourceParser);
				externalDAO = new ExternalDAO(conn);
				Thread.sleep((sleepPeriod) * 1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private List<NotifierMessage> getPendingViewMessage(Map<String, DBViewBean> messageMap, DataSourceParser dataSourceParser) {
		List<NotifierMessage> notifierMessages = new ArrayList<NotifierMessage>();
		XMLNotifierConfigHelper notifierConfigHelper = dataSourceParser.getxMLNotifierConfigHelper();
		historyMap = loaderDAO.getHistoryMap(DataSource.EXT_DB, notifierConfigHelper, messageMap.keySet());
		if (historyMap == null)
			return null;

		messageMap.forEach((key, dbViewBean) -> {
			NotifierMessage notifierMessage = new NotifierMessage();
			String insertionTime = dbViewBean.getMesgCreaDateTimeStr();
			String id = dbViewBean.getSeq().toString();
			String mesgText = dbViewBean.getMesgText();
			// String queueName=dbViewBean.getQueueName();
			if (!GpiService.isMsgExceededDuration(insertionTime, formater.format(new java.util.Date()), notifierConfigHelper.getDurationTime())) {
				return;
			}
			GbiHistoryBean gbiHistoryBean = (historyMap.isEmpty()) ? null : historyMap.get(id);
			if (gbiHistoryBean == null) { // new message
				notifierMessage.setDataSource(DataSource.EXT_DB);
				// notifierMessage.setQueueName(queueName);
				notifierMessage.setReasonCode(notifierConfigHelper.getReasonCode());
				notifierMessage.setSettlementMethod(notifierConfigHelper.getSettlementMethod());
				notifierMessage.setStatusCode(notifierConfigHelper.getStatusCode());
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setMessageText(mesgText);
				notifierMessage.setMesgID(id);
				notifierMessage.setConfirmAtempt(0);
				notifierMessage.setMailAtempt(0);
				notifierMessage.setConfirmAttemptsDate(null);
				notifierMessage.setMailAttemptsDate(null);
				notifierMessages.add(notifierMessage);
			} else {
				notifierMessage.setDataSource(DataSource.EXT_DB);
				// notifierMessage.setQueueName(queueName);
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setReasonCode(notifierConfigHelper.getReasonCode());
				notifierMessage.setSettlementMethod(notifierConfigHelper.getSettlementMethod());
				notifierMessage.setStatusCode(notifierConfigHelper.getStatusCode());
				notifierMessage.setMessageText(mesgText);
				notifierMessage.setMesgID(id);
				notifierMessage.setConfirmAtempt(gbiHistoryBean.getConfirmAtempt());
				notifierMessage.setMailAtempt(gbiHistoryBean.getMailAtempt());
				notifierMessage.setConfirmAttemptsDate(gbiHistoryBean.getConfirmAttemptsDate());
				notifierMessage.setMailAttemptsDate(gbiHistoryBean.getMailAttemptsDate());
				notifierMessages.add(notifierMessage);
			}
		});
		return notifierMessages;

	}

	public void initDataSource(DataSourceParser dataSourceParser) throws Exception {

		XMLDataSouceHelper dataSouceHelper = dataSourceParser.getXmlDataSource();
		if (dataSouceHelper.getDbTypeView().equalsIgnoreCase("oracle")) {
			datasource = new OracleDataSource();
			datasource.setDriverType("thin");

			if (dataSouceHelper.getTnsEnabled() != true) {
				datasource.setServerName(dataSouceHelper.getIpView());
				datasource.setPortNumber(Integer.parseInt(dataSouceHelper.getPortView()));
				if (dataSouceHelper.getDbNameView() != null && !dataSouceHelper.getDbNameView().isEmpty()) {
					datasource.setDatabaseName(dataSouceHelper.getDbNameView());
				} else {
					// then the service name selected
					datasource.setServiceName(dataSouceHelper.getServiceNameView());
				}
			} else {
				System.setProperty("oracle.net.tns_admin", dataSouceHelper.getTnsPath());
				datasource.setTNSEntryName(dataSouceHelper.getServiceNameView());

			}

			datasource.setUser(dataSouceHelper.getUsernameView());
			datasource.setPassword(dataSouceHelper.getPasswordView());
			// here olso must enable tns-nams

		}
		if (dataSouceHelper.getDbTypeView().equalsIgnoreCase("mssql")) {
			jtdsDataSource = new JtdsDataSource();
			jtdsDataSource.setServerName(dataSouceHelper.getIpView());
			jtdsDataSource.setDatabaseName(dataSouceHelper.getDbNameView());
			jtdsDataSource.setPortNumber(Integer.parseInt(dataSouceHelper.getPortView()));
			jtdsDataSource.setUser(dataSouceHelper.getUsernameView());
			jtdsDataSource.setPassword(dataSouceHelper.getPasswordView());

		}
		Connection conn = dataSourceParser.getXmlDataSource().getDbTypeView().equalsIgnoreCase("oracle") ? datasource.getConnection() : jtdsDataSource.getConnection();
		setConn(conn);
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public JMSBrowse getJmsBrowse() {
		return jmsBrowse;
	}

	public void setJmsBrowse(JMSBrowse jmsBrowse) {
		this.jmsBrowse = jmsBrowse;
	}

	public RjeMessageParser getRjeMessageParser() {
		return rjeMessageParser;
	}

	public void setRjeMessageParser(RjeMessageParser rjeMessageParser) {
		this.rjeMessageParser = rjeMessageParser;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public ExternalDAO getExternalDAO() {
		return externalDAO;
	}

	public void setExternalDAO(ExternalDAO externalDAO) {
		this.externalDAO = externalDAO;
	}

	public Notifaction getNotifaction() {
		return notifaction;
	}

	public void setNotifaction(Notifaction notifaction) {
		this.notifaction = notifaction;
	}

	public PermissionHandler getPermissionHandler() {
		return permissionHandler;
	}

	public void setPermissionHandler(PermissionHandler permissionHandler) {
		this.permissionHandler = permissionHandler;
	}

}
