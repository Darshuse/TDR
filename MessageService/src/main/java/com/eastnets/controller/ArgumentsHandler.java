package com.eastnets.controller;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eastnets.config.CommonServiceConfiguration;
import com.eastnets.config.DBConfiguration;
import com.eastnets.utility.Util;
import com.eastnets.config.DBProvider;

public class ArgumentsHandler {

	protected ClassPathXmlApplicationContext ctx;
	private CommonServiceConfiguration configBean;
	private DBConfiguration dbConfiguration;
	private static final Logger LOGGER = LogManager.getLogger(ArgumentsHandler.class);

	private static final String DB_SERVER_NAME = "-dbServerName";
	private static final String DB_TYPE = "-dbType";
	private static final String DB_PORT = "-dbPort";
	private static final String DB_USERNAME = "-dbUserName";
	private static final String DB_PASSWORD = "-dbPassword";
	private static final String DB_SERVICE_NAME = "-dbServiceName";
	private static final String DB_INSTANCE_NAME = "-dbInstanceName";
	private static final String DB_NAME = "-dbName";
	private static final String ECF = "-ecf";
	private static final String MQ_SERVER_NAME = "-mqServerName";
	private static final String MQ_PORT = "-mqPort";
	private static final String MQ_CHANNEL_NAME = "-mqChannelName";
	private static final String MAIL_SERVER = "-mailServerName";
	private static final String MAIL_PORT = "-mailPort";
	private static final String MAIL_FROM = "-mailFrom";
	private static final String MAIL_TO = "-mailTo";
	private static final String MAIL_PASSWORD = "-mailPassword";
	private static final String MODE = "-mode";
	private static final String MAX_THREAD_COUNT = "-maxThreadCount";
	private static final String DEBUG = "-debug";
	private static final String HELP = "-help";
	private static final String XML_CONFIG_FILE = "-xmlConfigFile";

	public ArgumentsHandler() {

		LOGGER.debug("Argument handler constructor");

		configBean = CommonServiceConfiguration.getInstance();
		dbConfiguration = configBean.getDbConfig();
	}

	public void createPropertyFile(CommonServiceConfiguration configBean) {
		LOGGER.trace("Creating propertis file");
		Properties prop = null;
		OutputStream output = null;
		prop = createPropertiesConfig(configBean);
		try {
			LOGGER.trace("Creating new properties file to store the properties in");
			output = new FileOutputStream("src/main/resources/commonService.properties");
			prop.store(output, null);
			LOGGER.trace("properties file created successfully");
		} catch (Exception e) {
			LOGGER.error("context", e);
		}

	}

	public CommonServiceConfiguration readCommandArgs(String[] args) {

		LOGGER.debug("Reading command arguments");

		try {

			for (int index = 0; index < args.length; index++) {
				String param = args[index];

				LOGGER.trace("index: " + index);
				LOGGER.trace("param: " + param);

				if (DB_SERVER_NAME.equalsIgnoreCase(param)) {
					// dbServerName
					index++;
					dbConfiguration.setServerName(args[index]);
				} else if (DB_TYPE.equalsIgnoreCase(param)) {
					// ORACLE/MSSQL
					index++;
					dbConfiguration.setDbType(args[index]);
					if (dbConfiguration.getDbType().equalsIgnoreCase("Oracle")) {
						dbConfiguration.setDbProvider(DBProvider.ORACLE);
					} else if (dbConfiguration.getDbType().equalsIgnoreCase("MSSQL")) {
						dbConfiguration.setDbProvider(DBProvider.MSSQL);
					}
				} else if (DB_PORT.equalsIgnoreCase(param)) {
					// Password
					index++;
					dbConfiguration.setPort(Integer.parseInt(args[index]));
				} else if (DB_USERNAME.equalsIgnoreCase(param)) {
					// User name
					index++;
					dbConfiguration.setUsername(args[index]);
				} else if (DB_PASSWORD.equalsIgnoreCase(param)) {
					// DB Password
					index++;
					dbConfiguration.setPassword(args[index]);
				} else if (DB_SERVICE_NAME.equalsIgnoreCase(param)) {
					// database service name (SID)
					index++;
					dbConfiguration.setServiceName(args[index]);
				} else if (DB_INSTANCE_NAME.equalsIgnoreCase(param)) {
					// Instance name
					index++;
					dbConfiguration.setInstanceName(args[index]);
				} else if (DB_NAME.equalsIgnoreCase(param)) {
					// instance name
					index++;
					dbConfiguration.setDbName(args[index]);
				} else if (HELP.equalsIgnoreCase(param)) {
					// Help message
					displayUsage("");
					return null;
				}
			}

			// Create Database URL based on the entries of DB configuration -
			// This URL will be used in Spring beans.
			dbConfiguration.setDbUrl(Util.getDatabaseURL(dbConfiguration));

		} catch (Exception e) {
			LOGGER.error("context", e);
		}

		return configBean;
	}

	private void displayUsage(String value) {

		System.out.printf("%s: missing or invalid paramter%n", value);
		System.out.println("Usage : \n");
		System.out.println("  " + XML_CONFIG_FILE
				+ "\t\t: xml file to read configurations from. If added other command arguments will be ignored");
		System.out.println("  " + DB_USERNAME + "\t\t: user name for database connection");
		System.out.println("  " + DB_PASSWORD + "\t\t: password for databasae connection");
		System.out.println("  " + DB_SERVER_NAME + "\t\t: The IP address or server name for database connection");
		System.out.println("  " + DB_TYPE + "\t: oracle or mssql database");
		System.out.println("  " + DB_PORT + "\t\t: database port");
		System.out.println("  " + DB_NAME + "\t: database name");
		System.out.println("  " + DB_SERVICE_NAME + "\t: database service name");
		System.out.println("  " + DB_INSTANCE_NAME + "\t: instance name");
		System.out.println("  " + ECF + "\t\t: Encrypted Connection File to connect to database");
		System.out.println("  " + MQ_SERVER_NAME + "\t\t: The IP address or server name for MQ server");
		System.out.println("  " + MQ_PORT + "\t\t: MQ port");
		System.out.println("  " + MQ_CHANNEL_NAME + "\t\t: MQ Channel name");
		System.out.println("  " + MAIL_SERVER + "\t\t: Mail server address");
		System.out.println("  " + MAIL_PORT + "\t\t: Mail server port");
		System.out.println("  " + MAIL_FROM + "\t\t: Mail to send from");
		System.out.println("  " + MAIL_TO + "\t\t: Mail to send notifications to");
		System.out.println("  " + MAIL_PASSWORD + "\t\t: Mail password");
		System.out.println("  " + MAIL_PASSWORD + "\t\t: Mail subject	");
		System.out.println("  " + MODE
				+ "\t\t: Watchdog server mode {A : Messages and events and email notifications, E: Events Only, M: Messages Only, N: Email Notifiations Only}");
		System.out.println("  " + MAX_THREAD_COUNT + "\t\t: Maximum number of running threads. Default is 1");
		System.out.println("  " + DEBUG + "\t\t: Run in debug mode ( more logs to be printed ) ");

	}

	public static Properties createPropertiesConfig(CommonServiceConfiguration configBean) {
		LOGGER.debug("Argument handler create properties out of config bean");
		Properties properties = new Properties();
		properties.setProperty("dbServerName", configBean.getDbConfig().getServerName());
		properties.setProperty("dbPort", configBean.getDbConfig().getPort().toString());
		properties.setProperty("dbType", configBean.getDbConfig().getDbType());
		properties.setProperty("dbServiceName", configBean.getDbConfig().getServiceName());
		properties.setProperty("dbInstanceName", configBean.getDbConfig().getInstanceName());
		properties.setProperty("dbName", configBean.getDbConfig().getDbName());
		properties.setProperty("dbUserName", configBean.getDbConfig().getUsername());
		properties.setProperty("dbPassword", configBean.getDbConfig().getPassword());
		properties.setProperty("dbDriverClassName", configBean.getDbConfig().getDbProvider().getDriver());
		properties.setProperty("JPADBVendor", configBean.getDbConfig().getDbProvider().getProvider());
		properties.setProperty("dbEcf", configBean.getDbConfig().getEcf());
		properties.setProperty("dbUrl", configBean.getDbConfig().getDbUrl());
		properties.setProperty("spring.mail.port", "true");
		properties.setProperty("spring.mail.properties.mail.smtp.starttls.enable", "true");
		properties.setProperty("databaseUrl", Util.getDatabaseURL(configBean.getDbConfig()));
		LOGGER.trace("setting properties done");
		return properties;

	}

	public static boolean validateArgs(CommonServiceConfiguration config) {

		return config != null;
	}
}
