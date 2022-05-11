package com.eastnets.enGpiLoader.main;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eastnets.application.BaseApp;
import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.enGpiLoader.config.AppConfigBean;
import com.eastnets.enGpiLoader.source.ExternalDBSource;
import com.eastnets.enGpiLoader.source.MessageSource;
import com.eastnets.enGpiLoader.source.MqSource;
import com.eastnets.enGpiLoader.source.SaaSource;
import com.eastnets.enGpiLoader.utility.DataSource;
import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.enGpiLoader.utility.XMLDataSouceHelper;
import com.eastnets.enGpiLoader.utility.XMLMailConfigHelper;

/**
 * @author MKassab
 * 
 */

public class Main extends BaseApp implements Runnable { 

	private static final String COPYRIGHT = "Copyright 1999-2018 EastNets"; 
	private static final long serialVersionUID = 3388764241142597051L; 
	private static final String APP_NAME = "GPI Notifiers Service 1.0.1"; 
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	private static final String ENABLE_Verbose = "-v";
	protected ClassPathXmlApplicationContext ctx;
	private static XMLDataSouceHelper xmlDataSouceHelper;
	private String configFile;
	static Main main;
	DataSourceParser dataSourceParser = null;
	private static final String XML_CONIFG = "-configfile";
	static {
		main = new Main();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	} 
	private static AppConfigBean appConfigBean;

	public static AppConfigBean getAppConfigBean() {
		if (appConfigBean == null) {
			appConfigBean = new AppConfigBean();
		}
		return appConfigBean;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(APP_NAME);
		System.out.println(COPYRIGHT);
		try {
			if (main.loadParamter(args)) {
				LOGGER.info("parameters loaded successfully");
			} else { 
				return;
			}
		} catch (Exception e) {
			main.displayUsage("" + e.getMessage());
			return;
		} 
		Thread test = new Thread(main);
		test.start();

	}



	private boolean loadParamter(String[] args) throws Exception {
		AppConfigBean configBean = getAppConfigBean(); 
		configBean.setDatabaseType(DBType.ORACLE);
		configBean.setPortNumber("1522");

		for (int i = 0; i < args.length; i++) { 
			String value = args[i]; 
			if (XML_CONIFG.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					String confPath = args[i];
					configBean.setDbConfigFilePath(confPath);   
				} else {
					displayUsage(value);
					return false;
				}
			}
			else if (ENABLE_Verbose.equals(value)) {
				if (i < args.length) {
					appConfigBean.setEnableDebugMode(true); 
					enableDebugLoggerMode();
				}
			} 
			else{
				displayUsage(value);
				return false;	
			}

		} 
		try {

			dataSourceParser = new DataSourceParser(configBean.getDbConfigFilePath());
		} catch (Exception e) {
			displayUsage("Invalid data");
			return false;
		}  
		XMLDataSouceHelper dataSouceHelper=dataSourceParser.getXmlDataSource();
		XMLMailConfigHelper  xmlMailConfigHelper=dataSourceParser.getXmlMailConfigHelper(); 
			if(!dataSouceHelper.getUsername().isEmpty()){
				configBean.setUsername(dataSouceHelper.getUsername());
			} 

			if(!dataSouceHelper.getPassword().isEmpty()){
				configBean.setPassword(dataSouceHelper.getPassword());
			} 

			if(!dataSouceHelper.getIp().isEmpty()){
				configBean.setServerName(dataSouceHelper.getIp());
			} 

			if(!dataSouceHelper.getDbType().isEmpty()){
				configBean.setDatabaseType(dataSouceHelper.getDbType().toLowerCase().equals("mssql") ? DBType.MSSQL : DBType.ORACLE);
				if (dataSouceHelper.getDbType().toLowerCase().equals("mssql") && configBean.getPortNumber().equals("1521"))
					configBean.setPortNumber("1433");
			}  
			if(!dataSouceHelper.getDbName().isEmpty()){
				configBean.setDatabaseName(dataSouceHelper.getDbName());
			}  
			if(dataSouceHelper.getServiceName() != null &&!dataSouceHelper.getServiceName().isEmpty()){
				configBean.setDbServiceName(dataSouceHelper.getServiceName());
			}  
			if(dataSouceHelper.getInstansName() != null &&!dataSouceHelper.getInstansName().isEmpty()){
				configBean.setInstanceName(dataSouceHelper.getInstansName());
			} 

			if(!dataSouceHelper.getPort().isEmpty()){
				configBean.setPortNumber(dataSouceHelper.getPort());
			}  
			configBean.setPartitioned(dataSouceHelper.isPartitioned());	 

		if(!xmlMailConfigHelper.getMailHost().isEmpty()){
			configBean.setMailHost(xmlMailConfigHelper.getMailHost());
		}
		if(!xmlMailConfigHelper.getMailPort().isEmpty()){  
			configBean.setMailPort(xmlMailConfigHelper.getMailPort()); 
		}
		if(!xmlMailConfigHelper.getMailUsername().isEmpty()){
			configBean.setMailUsername(xmlMailConfigHelper.getMailUsername()); 
		}
		if(!xmlMailConfigHelper.getMailPassword().isEmpty()){
			configBean.setMailPassword(xmlMailConfigHelper.getMailPassword()); 
		}

		if(!xmlMailConfigHelper.getMailTo().isEmpty()){
			configBean.setMailTo(xmlMailConfigHelper.getMailTo()); 
		}

		if(!xmlMailConfigHelper.getMailSubjec().isEmpty()){
			configBean.setMailSubject(xmlMailConfigHelper.getMailSubjec()); 
		} 

		if(!xmlMailConfigHelper.getMailFrom().isEmpty()){
			configBean.setMailFrom(xmlMailConfigHelper.getMailFrom()); 
		}




		Main.appConfigBean = configBean;
		main.init(appConfigBean);
		return true;
	}
	private void initMQProperties(Properties prop) {
		try {
			DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
			XMLDataSouceHelper xmlDataSource = dataSourceParser.getXmlDataSource();

			if (xmlDataSource == null) {
				throw new IllegalStateException(
						"Missing configuration in sample_connection for mqreader, unable to set MQ properties from sample_connection.xml");
			}
			try {
				prop.setProperty("IbmMqIP", xmlDataSource.getIbmMqIp());
				prop.setProperty("IbmMqUsername", xmlDataSource.getIbmMqUsername());
				prop.setProperty("IbmMqPassword", xmlDataSource.getIbmMqPassword());
				prop.setProperty("IbmMqChannel", xmlDataSource.getIbmChannel());
				prop.setProperty("IbmQueueManager", xmlDataSource.getIbmQueueManager());
				prop.setProperty("IbmMqPort", xmlDataSource.getIbmMqPort());
				prop.setProperty("IbmInputQueueName", xmlDataSource.getIbmInputQueueName());
				prop.setProperty("IbmErrorQueueName", xmlDataSource.getIbmErrorQueueName());
				
			} catch (NullPointerException e) {
				// optional in-case of Apache active MQ connector
				LOGGER.debug("IBM MQ Properties are not set");
			}
		} catch (Exception e) {
			LOGGER.error("Failed to set MQ Properties " + e.getMessage());
		}
	}


	protected void init(AppConfigBean configBean) throws PortNumberRangeException {

		Properties prop; 
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		prop = BaseApp.createPropertiesConfig(configBean);
		//prop.setProperty("aid", configBean.getAid());
		prop.setProperty("dbConfigFilePath", configBean.getDbConfigFilePath());
		setConfigFile(configBean.getDbConfigFilePath());
		prop.setProperty("enableDebugMode", configBean.getEnableDebugMode().toString()); 
        initMQProperties(prop);
		configurer.setProperties(prop);
		try {
			ctx = new ClassPathXmlApplicationContext();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		ctx.addBeanFactoryPostProcessor(configurer);
		ctx.setConfigLocation("classpath:/LoaderSpringConfig/LoaderServices.xml");
		ctx.refresh();
		if(appConfigBean.getEnableDebugMode()){
			enableDebugLoggerMode();
		}

		try {
			AppConfigBean bean = (AppConfigBean) ctx.getBean("appConfigBean");
			BeanUtils.copyProperties(bean, configBean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void enableDebugLoggerMode() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}



	public void displayUsage(String value) {
		System.out.printf("%s: missing or invalid paramter\n", value);
		System.out.println("Usage :");
		System.out.println("  " + XML_CONIFG + "\t\t: XML Configration Path"); 
	}

	@Override
	public void run() {
		MessageSource messageSource=null;
		DataSource dataSource=dataSourceParser.getXmlDataSource().getDataSource();
		if(dataSource.equals(DataSource.SAA)){
			messageSource=(SaaSource) ctx.getBean("saaSource");	
		}else if (dataSource.equals(DataSource.MQ)){
			messageSource=(MqSource) ctx.getBean("mqSource");		
		}else if (dataSource.equals(DataSource.EXT_DB)){
			System.out.println("Hola");
			messageSource=(ExternalDBSource)ctx.getBean("extDB");
		}
		try {
			new ServiceLauncher().startGpiUpdateStatusJob( dataSourceParser,messageSource);
		}   catch (Exception e) { 
			e.printStackTrace();
		}
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public static void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		Main.xmlDataSouceHelper = xmlDataSouceHelper;
	}

}
