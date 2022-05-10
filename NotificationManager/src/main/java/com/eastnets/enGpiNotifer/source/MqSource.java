package com.eastnets.enGpiNotifer.source;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiNotifer.Beans.GbiHistoryBean;
import com.eastnets.enGpiNotifer.DAO.LoaderDAO;
import com.eastnets.enGpiNotifer.JMS.JMSBrowse;
import com.eastnets.enGpiNotifer.Notifaction.Notifaction;
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

public class MqSource implements MessageSource {
	private AppConfigBean appConfigBean = null;
	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private JMSBrowse jmsBrowse;
	List<NotifierMessage> gpiMessegesPendenig = null;
	List<NotifierMessage> resultMessages = null;
	private Map<String, MTType> mtTypesMap = null;
	private static final DateFormat dateF = new SimpleDateFormat("yyMMddS");
	private static final Logger LOGGER = Logger.getLogger(MqSource.class);
	SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Map<String, GbiHistoryBean> historyMap = new HashMap<>();
	private RjeMessageParser rjeMessageParser;
	String curuntDate = "";
	private PermissionHandler permissionHandler;
	private Notifaction notifaction;

	@Override
	public void startSourceMonotoring(DataSourceParser dataSourceParser) throws Exception {
		permissionHandler.checkNotifationMangerLic(dataSourceParser.getxMLNotifierConfigHelper().isEnabelUC());
		permissionHandler.checkNotifationMangerUserRoals(dataSourceParser.getXmlDataSource().getUsername());
		LOGGER.info("Start Notification Manager Job ....");
		List<NotifierMessage> gpiMessegesPendenig = null;
		mtTypesMap = GenerateMTTypes.getMtTypesMap();
		rjeMessageParser = new RjeMessageParser(mtTypesMap, serviceLocator);
		// This commit for test

		while (true) {
			try {
				// jmsBrowse.sendByteMesage("");
				gpiMessegesPendenig = getPendingJmsMessage(jmsBrowse.browse(), dataSourceParser);
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
				Thread.sleep((sleepPeriod) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private Set<String> getOnlyMQIDMessage(Map<String, String> messageMap) {
		Set<String> mesgKeySet = new HashSet<String>();
		for (String key : messageMap.keySet()) {
			String[] dataWithid = key.split(",");
			String id = dataWithid[1];
			mesgKeySet.add(id);
		}
		return mesgKeySet;

	}

	private List<NotifierMessage> getPendingJmsMessage(Map<String, String> messageMap, DataSourceParser dataSourceParser) {
		List<NotifierMessage> notifierMessages = new ArrayList<NotifierMessage>();
		XMLNotifierConfigHelper notifierConfigHelper = dataSourceParser.getxMLNotifierConfigHelper();
		XMLDataSouceHelper dataSouceHelper = dataSourceParser.getXmlDataSource();
		historyMap = loaderDAO.getHistoryMap(DataSource.MQ, notifierConfigHelper, getOnlyMQIDMessage(messageMap));
		if (historyMap == null)
			return null;
		for (String key : messageMap.keySet()) {
			NotifierMessage notifierMessage = new NotifierMessage();
			String[] dataWithid = key.split(",");
			String insertionTime = dataWithid[0];
			String id = dataWithid[1];
			if (!GpiService.isMsgExceededDuration(insertionTime, formater.format(new Date()), notifierConfigHelper.getDurationTime())) {
				continue;
			}
			GbiHistoryBean gbiHistoryBean = (historyMap.isEmpty()) ? null : historyMap.get(id);
			if (gbiHistoryBean == null) { // new message
				notifierMessage.setDataSource(DataSource.MQ);
				notifierMessage.setQueueName(dataSouceHelper.getIbmInputQueueName());
				notifierMessage.setReasonCode(notifierConfigHelper.getReasonCode());
				notifierMessage.setSettlementMethod(notifierConfigHelper.getSettlementMethod());
				notifierMessage.setStatusCode(notifierConfigHelper.getStatusCode());
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setMessageText(messageMap.get(key));
				notifierMessage.setMesgID(id);
				notifierMessage.setConfirmAtempt(0);
				notifierMessage.setMailAtempt(0);
				notifierMessage.setConfirmAttemptsDate(null);
				notifierMessage.setMailAttemptsDate(null);
				notifierMessages.add(notifierMessage);
			} else {
				notifierMessage.setDataSource(DataSource.MQ);
				notifierMessage.setQueueName(dataSouceHelper.getIbmInputQueueName());
				notifierMessage.setReasonCode(notifierConfigHelper.getReasonCode());
				notifierMessage.setSettlementMethod(notifierConfigHelper.getSettlementMethod());
				notifierMessage.setStatusCode(notifierConfigHelper.getStatusCode());
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setMessageText(messageMap.get(key));
				notifierMessage.setMesgID(id);
				notifierMessage.setConfirmAtempt(gbiHistoryBean.getConfirmAtempt());
				notifierMessage.setMailAtempt(gbiHistoryBean.getMailAtempt());
				notifierMessage.setConfirmAttemptsDate(gbiHistoryBean.getConfirmAttemptsDate());
				notifierMessage.setMailAttemptsDate(gbiHistoryBean.getMailAttemptsDate());
				notifierMessages.add(notifierMessage);
			}
		}

		return notifierMessages;

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
