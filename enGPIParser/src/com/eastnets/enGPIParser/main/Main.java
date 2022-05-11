package com.eastnets.enGPIParser.main;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eastnets.application.BaseApp;
import com.eastnets.config.DBType;
import com.eastnets.enGPIParser.GPIConfig.AppConfigBean;
import com.eastnets.enGPIParser.service.GPIService;
import com.eastnets.encdec.ConnectionSettings;
import com.eastnets.encdec.EnEcfParser;

public class Main extends BaseApp implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DBNAME = "-dbname";
	private static final String DB_TYPE = "-dbtype";
	private static final String IP = "-ip";
	private static final String DBPORT = "-port";
	private static final String INSTANCENAME = "-instancename";
	private static final String SERVICENAME = "-servicename";
	private static final String USER = "-u";
	private static final String PASSWORD = "-p";
	private static final String ECF = "-ecf";
	private static final String AID = "-aid";
	private static final String SLEEPPERIOD = "-sleep";
	private static final String UC_ENABLE = "-UC";
	private static final String APP_NAME = "gpi Parser Service 8.0";
	private static final String VERBOSE = "-v";
	private static final String COPYRIGHT = "Copyright 1999-2021 EastNets";
	private static final String FROM_DATE = "-fromDate";
	private static final String TO_DATE = "-toDate";
	private static final String BATCH_SIZE = "-batchSize";

	private static final Logger LOGGER;

	protected ClassPathXmlApplicationContext ctx;

	static Main main;
	static {
		main = new Main();
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		LOGGER = Logger.getLogger(Main.class);
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
				LOGGER.info("paramters loaded successfully");
				if (appConfigBean.isVerbose()) {
					Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
					Configurator.setRootLevel(Level.DEBUG);
				}
			} else {
				LOGGER.info("Parameters failed to start");
				return;
			}
		} catch (Exception e) {
			main.displayUsage("" + e.getMessage());
			return;
		}

		Thread test = new Thread(main);
		test.start();

	}

	public void init(AppConfigBean configBean) {

		Properties prop;

		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		prop = BaseApp.createPropertiesConfig(configBean);
		prop.setProperty("ucEnabel", String.valueOf(configBean.isUcEnabel()));
		prop.setProperty("batchSize", String.valueOf(configBean.getBatchSize()));
		prop.setProperty("fromDate", configBean.getFromDate());
		prop.setProperty("toDate", configBean.getToDate());
		configurer.setProperties(prop);

		ctx = new ClassPathXmlApplicationContext();
		ctx.addBeanFactoryPostProcessor(configurer);
		ctx.setConfigLocation("classpath:/LoaderServices.xml");
		ctx.refresh();
	}

	private boolean loadParamter(String[] args) throws Exception {

		AppConfigBean configBean = getAppConfigBean();
		configBean.setDatabaseType(DBType.ORACLE);
		configBean.setPortNumber("1522");
		for (int i = 0; i < args.length; i++) {
			String value = args[i];
			if (USER.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					String username = args[i];
					configBean.setUsername(username);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (PASSWORD.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setPassword(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (IP.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setServerName(args[i]);
				} else {
					displayUsage(value);
					return false;
				}

			} else if (DB_TYPE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setDatabaseType(args[i].toLowerCase().equals("mssql") ? DBType.MSSQL : DBType.ORACLE);
					configBean.setDbType(args[i]);
					if (args[i].toLowerCase().equals("mssql") && configBean.getPortNumber().equals("1521"))
						configBean.setPortNumber("1433");
				} else {
					displayUsage(value);
					return false;
				}

			} else if (VERBOSE.equalsIgnoreCase(value)) {
				configBean.setVerbose(true);
			} else if (UC_ENABLE.equalsIgnoreCase(value)) {
				configBean.setUcEnabel(true);
			} else if (DBNAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setDatabaseName(args[i]);
				} else {
					return false;
				}
			} else if (SERVICENAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {

					configBean.setDbServiceName(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (INSTANCENAME.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setInstanceName(args[i]);
				}

			} else if (ECF.equalsIgnoreCase(value)) {

				if (i++ < args.length) {
					LOGGER.debug("Starting to parse Connection setting on ECF ...");
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
					LOGGER.debug("parsing Connection setting on ECF completed");

				} else {
					displayUsage(value);
					return false;
				}
			} else if (DBPORT.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setPortNumber(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else if (FROM_DATE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setFromDate(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			}

			else if (TO_DATE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setToDate(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			}

			else if (BATCH_SIZE.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setBatchSize(Integer.parseInt(args[i]));
				} else {
					displayUsage(value);
					return false;
				}
			}

			else if (SLEEPPERIOD.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					configBean.setSleepPeriod(Long.parseLong(args[i]));
				} else {
					displayUsage(value);
					return false;
				}
			} else if (AID.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					Properties log4jProperties = new Properties();
					log4jProperties.setProperty("", value);
					configBean.setsAAAid(args[i]);
				} else {
					displayUsage(value);
					return false;
				}
			} else {
				displayUsage("");
				return false;
			}
		}

		if (configBean.getUsername().isEmpty() || configBean.getPassword().isEmpty() || configBean.getsAAAid().isEmpty() || configBean.getDatabaseName().isEmpty()) {
			displayUsage("");
			return false;
		}
		Main.appConfigBean = configBean;
		main.init(appConfigBean);
		return true;

	}

	public void run() {

		GPIService gpiService = (GPIService) ctx.getBean("GPIService");
		try {

			doParsing(gpiService);
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void doParsing(GPIService gpiService) throws InterruptedException {

		int messagesParsedNumber = 0;

		long waitPeriod = getAppConfigBean().getSleepPeriod();
		while (true) {

			messagesParsedNumber = gpiService.startGPIParsing();

			if (messagesParsedNumber == 0) {
				LOGGER.debug("No Messages Parsed, Scanner will sleep for " + waitPeriod + " Seconds");
				Thread.sleep((waitPeriod) * 1000);
			}

		}

	}

	public void displayUsage(String value) {
		System.out.printf("%s: missing or invalid paramter\n", value);
		System.out.println("Usage : \n");
		System.out.println("  " + USER + "\t\t: user name for database connection");
		System.out.println("  " + PASSWORD + "\t\t: password for databasae connection");
		System.out.println("  " + IP + "\t\t: The IP address or server name for database connection, default is localhost.");
		System.out.println("  " + DB_TYPE + "\t: oracle or mssql database, default is oracle");
		System.out.println("  " + DBPORT + "\t\t: database port, default 1521 for oracle and 1433 for mssql");
		System.out.println("  " + DBNAME + "\t: database name");
		System.out.println("  " + SERVICENAME + "\t: service name");
		System.out.println("  " + INSTANCENAME + "\t: instance name");
		System.out.println("  " + AID + "\t\t: aid Id");
		System.out.println("  " + SLEEPPERIOD + "\t: sleep period");
		System.out.println("  " + ECF + "\t\t: Encrypted Connection File to connect to database");
		System.out.println("  " + UC_ENABLE + "\t\t: To Enable Universal Confirmations");
		System.out.println("  " + FROM_DATE + "\t\t: From Date");
		System.out.println("  " + TO_DATE + "\t\t: To Date");
		System.out.println("  " + BATCH_SIZE + "\t\t: Batch size per cycle");
		System.out.println("  " + VERBOSE + "\t\t: VERBOSE MODE");
	}

}
