package com.eastnets.enReportingLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.eastnets.application.BaseApp;
import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.reportingserver.encdec.ConnectionSettings;
import com.eastnets.reportingserver.encdec.EnEcfParser;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;

/**
 * @author MKassab
 * 
 */
public class Main extends BaseApp implements Runnable {

	private static final String KEY_AID = "-aid";

	private static final String KEY_DBNAME = "-dbname";

	private static final String KEY_PORT = "-port";
	private static final String KEY_DBTYPE = "-dbtype";

	private static final String KEY_ECF = "-ecf";

	private static final String KEY_IP = "-ip";

	private static final String KEY_PASS = "-p";

	private static final String KEY_USER = "-u";

	private static final String KEY_DB_CONIFG = "-configfile";

	private static final String KEY_CLUSTER_TCP_CONIFG = "-clusterTcpConfigfile";

	private static final String COPYRIGHT = "Copyright 1999-2021 EastNets";

	private static final long serialVersionUID = 3388764241142597051L;

	private static final String APP_NAME = "MQConnector_3_2_0_10";

	private static final String LOG_FILE = "-logfile";

	private static final String ENABLE_DEBUG = "-debug";

	private static final String SERVICE_NAME = "-servicename";
	private static final String INSTANCE_NAME = "-instancename";

	private static final String CLUSTERING_ENABLED = "-clusteringEnabled";

	private static final String CLUSTER_NAME = "-clustername";

	private static final Logger LOGGER;

	protected ClassPathXmlApplicationContext ctx;
	private static XMLDataSouceHelper xmlDataSouceHelper;
	private String configFile;
	protected ServiceLauncher lancher;
	LoaderDAO loaderDAO;

	static Main main;
	static {
		LOGGER = Logger.getLogger(Main.class);
		main = new Main();
	}

	private static AppConfigBean appConfigBean;

	public static AppConfigBean getAppConfigBean() {
		if (appConfigBean == null) {
			appConfigBean = new AppConfigBean();
		}
		return appConfigBean;
	}

	public static boolean isDbReader() {
		if (xmlDataSouceHelper.isApacheMqIntegrationEnabled() || xmlDataSouceHelper.isIbmMQIntegrationEnabled()) {
			return false;
		}
		return true;
	}

	protected void init(AppConfigBean configBean) throws PortNumberRangeException {

		Properties prop;

		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		prop = BaseApp.createPropertiesConfig(configBean);
		prop.setProperty("aid", configBean.getSAAAid());
		prop.setProperty("dbConfigFilePath", configBean.getDbConfigFilePath());
		setConfigFile(configBean.getDbConfigFilePath());
		prop.setProperty("enableDebugMode", configBean.getEnableDebugMode().toString());
		if (configBean.isClusteringEnabled() != null) {
			prop.setProperty("clusteringEnabled", String.valueOf(configBean.isClusteringEnabled()));
		}
		if (configBean.getClustername() != null) {
			prop.setProperty("clustername", configBean.getClustername());
		}
		if (configBean.getClusterTcpConfigFilePath() != null) {
			prop.setProperty("clusterTcpConfigfile", configBean.getClusterTcpConfigFilePath());
		}

		initMQProperties(prop);
		configurer.setProperties(prop);
		ctx = new ClassPathXmlApplicationContext();
		if (prop.getProperty("mqVendor") == null) {
			try {
				DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
				XMLDataSouceHelper xmlDataSource = dataSourceParser.getXmlDataSource();
				prop.setProperty("mqVendor", "ibm");
				// prop.setProperty("mqBulkSize", "1000");
				// prop.setProperty("apacheMqIp", "localhost");
				// prop.setProperty("apacheMqPort", "61616");
				// prop.setProperty("apacheInputQueueName", "jmsInputQueue");
				// prop.setProperty("apacheErrorQueueName", "jmsErrorQueue");
				/////////////////////

				prop.setProperty("IbmMqIP", "192.168.100.151");
				prop.setProperty("IbmMqUsername", "");
				prop.setProperty("IbmMqPassword", "");
				prop.setProperty("IbmMqChannel", "con123");
				prop.setProperty("IbmQueueManager", "QM");
				prop.setProperty("IbmMqPort", "1414");
				prop.setProperty("IbmInputQueueName", "devQueue");
				prop.setProperty("IbmErrorQueueName", "devQueueEroor");

				// For enable ssl
				prop.setProperty("sslKeyStoreType", "");
				prop.setProperty("sslKeyStore", "");
				prop.setProperty("sslKeyStorePassword", "");
				prop.setProperty("sslTrustStoreType", "");
				prop.setProperty("sslTrustStore", "");
				prop.setProperty("sslTrustStorePassword", "");
				prop.setProperty("sslCipherSuite", "");
				prop.setProperty("sslPeerName", "");
				prop.setProperty("SSLCipherSpec", "");
				prop.setProperty("useIBMCipherMappings", "");
				prop.setProperty("noAuthentication", "true");
				prop.setProperty("enableFIPS", "true");
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		setSpringEnvProps(prop.getProperty("mqVendor"));
		ctx.addBeanFactoryPostProcessor(configurer);
		ctx.setConfigLocation("classpath:/LoaderSpringConfig/LoaderServices.xml");
		ctx.refresh();
		DefaultMessageListenerContainer dmlc = (DefaultMessageListenerContainer) ctx.getBean("jmsContainer");
		dmlc.stop();

		try {
			AppConfigBean bean = (AppConfigBean) ctx.getBean("appConfigBean");
			BeanUtils.copyProperties(bean, configBean);
			lancher = (ServiceLauncher) ctx.getBean("serviceLauncher");
		} catch (IllegalAccessException e) {
			e.printStackTrace();

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void setSpringEnvProps(String vendor) {
		MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mqVendor", vendor);
		MapPropertySource propertySource1 = new MapPropertySource("map", map);
		sources.addFirst(propertySource1);
	}

	private void initMQProperties(Properties prop) {
		try {
			DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
			XMLDataSouceHelper xmlDataSource = dataSourceParser.getXmlDataSource();

			if (xmlDataSource == null) {
				throw new IllegalStateException("Missing configuration in sample_connection for mqreader, unable to set MQ properties from sample_connection.xml");
			}

			// These properties are mandatory in both Apache MQ and IBM MQ
			// connectors
			prop.setProperty("mqVendor", xmlDataSource.getMqVendor());
			prop.setProperty("mqBulkSize", String.valueOf(xmlDataSource.getMqBulkSize()));

			try {
				prop.setProperty("IbmMqIP", xmlDataSource.getIbmMqIp());
				if (xmlDataSource.getIbmMqUsername() != null) {
					prop.setProperty("IbmMqUsername", xmlDataSource.getIbmMqUsername());
					prop.setProperty("IbmMqPassword", xmlDataSource.getIbmMqPassword());
				}

				prop.setProperty("IbmMqChannel", xmlDataSource.getIbmChannel());
				prop.setProperty("IbmQueueManager", xmlDataSource.getIbmQueueManager());
				prop.setProperty("IbmMqPort", xmlDataSource.getIbmMqPort());
				prop.setProperty("IbmInputQueueName", xmlDataSource.getIbmInputQueueName());
				prop.setProperty("IbmErrorQueueName", xmlDataSource.getIbmErrorQueueName());

				// For enable ssl
				prop.setProperty("sslKeyStoreType", xmlDataSource.getSslKeyStoreType());
				prop.setProperty("sslKeyStore", xmlDataSource.getSslKeyStore());
				prop.setProperty("sslKeyStorePassword", xmlDataSource.getSslKeyStorePassword());
				prop.setProperty("sslTrustStoreType", xmlDataSource.getSslTrustStoreType());
				prop.setProperty("sslTrustStore", xmlDataSource.getSslTrustStore());
				prop.setProperty("sslTrustStorePassword", xmlDataSource.getSslTrustStorePassword());
				prop.setProperty("sslCipherSuite", xmlDataSource.getSslCipherSuite());
				prop.setProperty("sslPeerName", xmlDataSource.getSslPeerName());
				prop.setProperty("SSLCipherSpec", xmlDataSource.getSSLCipherSpec());
				prop.setProperty("useIBMCipherMappings", xmlDataSource.getUseIBMCipherMappings());
				prop.setProperty("noAuthentication", String.valueOf(xmlDataSource.isNoAuthentication()));
				prop.setProperty("enableFIPS", String.valueOf(xmlDataSource.isEnableFIPS()));

			} catch (NullPointerException e) {
				// optional in-case of Apache active MQ connector
				LOGGER.debug("IBM MQ Properties are not set");
			}

			try {
				prop.setProperty("apacheMqIp", xmlDataSource.getApacheMqIp());
				prop.setProperty("apacheMqPort", xmlDataSource.getApacheMqPort());
				prop.setProperty("apacheInputQueueName", xmlDataSource.getApacheInputQueueName());
				prop.setProperty("apacheErrorQueueName", xmlDataSource.getApacheErrorQueueName());
			} catch (NullPointerException e) {
				// optional in-case of IBM Websphere MQ connector
				LOGGER.debug("Apache MQ Properties are not set");
			}
		} catch (Exception e) {
			LOGGER.error("Failed to set MQ Properties " + e.getMessage());
		}
	}

	private String empatyIfNull(String value) {
		if (value == null)
			return "";

		return value;

	}

	private void configLogFile(String fullFileLocation) {
		PatternLayout patternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");

		try {
			RollingFileAppender appender = new RollingFileAppender(patternLayout, fullFileLocation);
			appender.setMaxFileSize("10MB");
			appender.setMaxBackupIndex(10);
			LogManager.getRootLogger().addAppender(appender);
		} catch (IOException e) {
			LogManager.getRootLogger().error(e);
		}
	}

	private void enableDebugLoggerMode() {
		Configurator.setLevel("com.eastnets", Level.DEBUG);
	}

	public static void main(String[] args) {
		System.out.println(APP_NAME);
		System.out.println(COPYRIGHT);
		System.out.println();
		/* System.getProperty("file.separator"); */
		try {
			if (main.loadParamter(args)) {
				System.out.println("paramters loaded successfully");
			} else {
				main.displayUsage("");
				return;
			}
		} catch (Exception e) {
			main.displayUsage("" + e.getMessage());
			return;
		}
		Thread test = new Thread(main);
		test.start();
	}

	// put all config in configBean abd send it to init
	// no Spring Yet
	// Call init
	private boolean loadParamter(String[] args) throws Exception {
		AppConfigBean configBean = getAppConfigBean();
		DataSourceParser dataSourceParser = null;
		// default config values for database connection
		configBean.setDatabaseType(DBType.ORACLE);
		configBean.setPortNumber("1522");

		for (int i = 0; i < args.length; i++) {
			String value = args[i];
			if (KEY_USER.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					String username = args[i];
					configBean.setUsername(username);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (KEY_PASS.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setPassword(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (KEY_IP.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setServerName(args[i]);
				} else {
					displayUsage(value);
					return false;
				}

			} else if (KEY_DBTYPE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setDatabaseType(args[i].toLowerCase().equals("mssql") ? DBType.MSSQL : DBType.ORACLE);
					if (args[i].toLowerCase().equals("mssql") && configBean.getPortNumber().equals("1521"))
						configBean.setPortNumber("1433");
				} else {
					displayUsage(value);
					return false;
				}
			} else if (KEY_DBNAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setDatabaseName(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (SERVICE_NAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {

					configBean.setDbServiceName(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (INSTANCE_NAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setInstanceName(args[i]);
				}
			} else if (CLUSTERING_ENABLED.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setClusteringEnabled(Boolean.valueOf((args[i]).trim()));
				} /*
					 * else { displayUsage(value); return false; }
					 */
			} else if (CLUSTER_NAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setClustername((args[i]));
				} /*
					 * else { displayUsage(value); return false; }
					 */
			} else if (KEY_DB_CONIFG.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setDbConfigFilePath(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (KEY_CLUSTER_TCP_CONIFG.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setClusterTcpConfigFilePath(args[i]);
				} /*
					 * else { displayUsage(value); return false; }
					 */
			} else if (KEY_PORT.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setPortNumber(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (KEY_ECF.equalsIgnoreCase(value)) {

				if (i++ < args.length) {
					ConnectionSettings cs = EnEcfParser.parseECF(args[i]);
					if (cs.getServerName() != null) {
						configBean.setServerName(cs.getServerName());
					}
					if (cs.getUserName() != null) {
						configBean.setUsername(cs.getUserName());
					}
					if (cs.getPassword() != null) {
						configBean.setPassword(cs.getPassword());
					}
					if (cs.getPortNumber() != null) {
						configBean.setPortNumber(cs.getPortNumber().toString());
					}
					if (cs.getServiceName() != null) {
						configBean.setDatabaseName(cs.getServiceName());
					}
				} /*
					 * else { displayUsage(value); return false; }
					 */
			} else if (KEY_AID.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					Properties log4jProperties = new Properties();
					log4jProperties.setProperty("", value);
					configBean.setSAAAid(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (LOG_FILE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configLogFile(args[i]);
				}
			} else if (ENABLE_DEBUG.equals(value)) {
				if (i < args.length) {
					appConfigBean.setEnableDebugMode(true);
					enableDebugLoggerMode();
				}
			} else {
				displayUsage("");
				return false;
			}

		}
		if (configBean.getUsername() == null || configBean.getPassword() == null || configBean.getDbConfigFilePath() == null) {
			throw new RuntimeException("Error");
		}
		try {
			dataSourceParser = new DataSourceParser(appConfigBean.getDbConfigFilePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlDataSouceHelper = dataSourceParser.getXmlDataSource();

		Main.appConfigBean = configBean;
		main.init(appConfigBean);
		return true;
	}

	public void run() {
		ServiceLauncher lancher = (ServiceLauncher) ctx.getBean("serviceLauncher");
		// lancher.setAppConfigBean(appConfig);
		lancher.launch();

	}

	public void displayUsage(String value) {
		System.out.printf("%s: missing or invalid paramter\n", value);
		System.out.println("Usage : \n");
		System.out.println("  " + KEY_USER + "\t\t: user name for database connection");
		System.out.println("  " + KEY_PASS + "\t\t: password for databasae connection");
		System.out.println("  " + KEY_IP + "\t\t: The IP address or server name for database connection, default is localhost.");
		System.out.println("  " + KEY_ECF + "\t\t: passs the Encrypted Connection File to connect to database");
		System.out.println("  " + KEY_DBTYPE + "\t: oracle or mssql database, default is oracle");
		System.out.println("  " + KEY_PORT + "\t\t: database port, default 1521 for oracle and 1433 for mssql");
		System.out.println("  " + KEY_DBNAME + "\t: database name");
		System.out.println("  " + SERVICE_NAME + "\t: service name");
		System.out.println("  " + KEY_AID + "\t\t: aid Id.");
		System.out.println("  " + KEY_DB_CONIFG + "\t: config file path.");
		System.out.println("  " + CLUSTERING_ENABLED + "\t: enable clustering mode.");
		System.out.println("  " + CLUSTER_NAME + "\t: identifies the target cluster name that this loader will join.");
		System.out.println("  " + KEY_CLUSTER_TCP_CONIFG + "\t: cluster tcp config file path.");
		System.out.println("  " + ENABLE_DEBUG + "\t: enable debugging.");
		System.out.println("  " + LOG_FILE + "\t:  to specify the location of the log file and it's name");
		// System.out.println(" " + ENABLE_DEBUG + "\t\t: for more logging
		// details");
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

}
