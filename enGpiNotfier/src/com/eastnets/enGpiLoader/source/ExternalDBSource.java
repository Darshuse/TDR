package com.eastnets.enGpiLoader.source;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.Beans.DBViewBean;
import com.eastnets.enGpiLoader.Beans.GbiHistoryBean;
import com.eastnets.enGpiLoader.DAO.ExternalDAO;
import com.eastnets.enGpiLoader.DAO.LoaderDAO;
import com.eastnets.enGpiLoader.config.AppConfigBean;
import com.eastnets.enGpiLoader.jms.JMSBrowse;
import com.eastnets.enGpiLoader.notification.Notifaction;
import com.eastnets.enGpiLoader.parser.GenerateMTTypes;
import com.eastnets.enGpiLoader.parser.MTType;
import com.eastnets.enGpiLoader.parser.RjeMessageParser;
import com.eastnets.enGpiLoader.service.GpiService;
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.enGpiLoader.utility.XMLDataSouceHelper;
import com.eastnets.enGpiLoader.utility.XMLNotifierConfigHelper;
import com.eastnets.service.ServiceLocator;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import oracle.jdbc.pool.OracleDataSource;

public class ExternalDBSource implements MessageSource{

	private OracleDataSource datasource;
	private JtdsDataSource jtdsDataSource;
	private AppConfigBean appConfigBean = null;
	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private JMSBrowse jmsBrowse;
	List<NotifierMessage> gpiMessegesPendenig =null;
	List<NotifierMessage> resultMessages=null;
	private Map<String, MTType> mtTypesMap = null ;


	private static final Logger LOGGER = Logger.getLogger(MqSource.class);
	SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Map<String,GbiHistoryBean> historyMap=new HashMap<>();
	private RjeMessageParser rjeMessageParser;
	String curuntDate = "";
	private Connection conn;
	private ExternalDAO externalDAO;


	@Override
	public void startSourceMonotoring(DataSourceParser dataSourceParser) throws Exception {
		initDataSource(dataSourceParser);
		externalDAO=new ExternalDAO(conn);

		List<NotifierMessage> gpiMessegesPendenig =null; 
		mtTypesMap=GenerateMTTypes.getMtTypesMap();
		rjeMessageParser=new RjeMessageParser(mtTypesMap, serviceLocator);
		while (true) { 
			try {
				gpiMessegesPendenig = getPendingJmsMessage(externalDAO.getPendingExternalPendingMsg(dataSourceParser),dataSourceParser);
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while getPendingGpiMesg() SQL Exception ", null);
				e.printStackTrace(); 
			}  

			if(gpiMessegesPendenig != null  && !gpiMessegesPendenig.isEmpty()){ 
				LOGGER.debug("Start notification cycle for pending messages    ...");  
				new Notifaction().notify(dataSourceParser,gpiMessegesPendenig,serviceLocator,loaderDAO,appConfigBean,rjeMessageParser); 
				LOGGER.debug("End notification cycle for pending Messages    ...");

			}else{ 
				LOGGER.debug("No message pending in queue ..."); 

			}

			try {  
				Long sleepPeriod=dataSourceParser.getxMLNotifierConfigHelper().getSleepPeriod();
				LOGGER.info("Notifier  will sleep for " + sleepPeriod + " Seconds");
				Thread.sleep((sleepPeriod) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}
	private List<NotifierMessage> getPendingJmsMessage(Map<String,DBViewBean> messageMap,DataSourceParser dataSourceParser){
		List<NotifierMessage> notifierMessages=new ArrayList<NotifierMessage>();
		XMLNotifierConfigHelper notifierConfigHelper=dataSourceParser.getxMLNotifierConfigHelper();
		XMLDataSouceHelper dataSouceHelper =dataSourceParser.getXmlDataSource();
		historyMap=loaderDAO.getHistoryMap(DataSource.MQ,notifierConfigHelper);
		for(String key:messageMap.keySet()){
			NotifierMessage notifierMessage=new NotifierMessage(); 
			DBViewBean msgBean=messageMap.get(key); 
			String insertionTime=msgBean.getMesgCreaDateTimeStr();
			String id=msgBean.getSeq(); 
			String mesgText=msgBean.getMesgText();

			if(!GpiService.isMsgExceededDuration(insertionTime,formater.format(new java.util.Date()),notifierConfigHelper.getDurationTime())){
				continue;
			} 
			GbiHistoryBean gbiHistoryBean=(historyMap.isEmpty()) ?null:historyMap.get(id);
			if(gbiHistoryBean == null){ // new message
				notifierMessage.setDataSource(DataSource.MQ);
				notifierMessage.setQueueName(dataSouceHelper.getIbmInputQueueName());
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setMessageText(mesgText);
				notifierMessage.setMesgID(id);
				notifierMessage.setConfirmAtempt(0);
				notifierMessage.setMailAtempt(0);
				notifierMessage.setConfirmAttemptsDate(null);
				notifierMessage.setMailAttemptsDate(null);
				notifierMessages.add(notifierMessage);
			}else{
				notifierMessage.setDataSource(DataSource.MQ);
				notifierMessage.setQueueName(dataSouceHelper.getIbmInputQueueName());
				notifierMessage.setInsertionMessageDataTime(insertionTime);
				notifierMessage.setMessageText(mesgText);
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


	public void initDataSource(DataSourceParser dataSourceParser) throws Exception {

		XMLDataSouceHelper dataSouceHelper =dataSourceParser.getXmlDataSource();


		if(dataSouceHelper.getDbType().equalsIgnoreCase("oracle")){ 
			datasource = new OracleDataSource();
			datasource.setDriverType("thin");
			datasource.setServerName(dataSouceHelper.getIpView());
			if(dataSouceHelper.getDbName() != null && !dataSouceHelper.getDbName().isEmpty()) {
				datasource.setDatabaseName(dataSouceHelper.getDbNameView());
			} else {
				// then the service name selected
				datasource.setServiceName(dataSouceHelper.getServiceNameView());
			}

			datasource.setPortNumber(Integer.parseInt(dataSouceHelper.getPortView()));
			datasource.setUser(dataSouceHelper.getUsernameView());
			datasource.setPassword(dataSouceHelper.getPasswordView());

		}


		if(dataSouceHelper.getDbType().equalsIgnoreCase("mssql")){

			jtdsDataSource = new JtdsDataSource();

			jtdsDataSource.setServerName(dataSouceHelper.getIpView());
			jtdsDataSource.setDatabaseName(dataSouceHelper.getDbNameView());
			jtdsDataSource.setPortNumber(Integer.parseInt(dataSouceHelper.getPortView()));
			jtdsDataSource.setUser(dataSouceHelper.getUsernameView());
			jtdsDataSource.setPassword(dataSouceHelper.getPasswordView());

		} 
		Connection conn =  dataSourceParser.getXmlDataSource().getDbTypeView().equalsIgnoreCase("oracle") ? datasource.getConnection() : jtdsDataSource.getConnection(); 
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

}
