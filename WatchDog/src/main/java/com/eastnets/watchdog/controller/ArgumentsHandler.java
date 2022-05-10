package com.eastnets.watchdog.controller;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eastnets.encdec.ConnectionSettings;
import com.eastnets.encdec.EnEcfParser;
import com.eastnets.watchdog.config.DBConfiguration;
import com.eastnets.watchdog.config.DBProvider;
import com.eastnets.watchdog.config.MQConfiguration;
import com.eastnets.watchdog.config.MailConfiguration;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.utility.Util;

public class ArgumentsHandler {

	protected ClassPathXmlApplicationContext ctx;
	private WatchdogConfiguration configBean;
	private DBConfiguration dbConfiguration;
	private MQConfiguration mqConfiguration;
	MailConfiguration mailConfiguration;

	private static final Logger LOGGER = Logger.getLogger(ArgumentsHandler.class);

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
	private static final String MAIL_SUBJECT = "-mailSubject";
	private static final String MODE = "-mode";
	private static final String MAX_THREAD_COUNT = "-maxThreadCount";
	private static final String DEBUG = "-debug";
	private static final String DELAY = "-delay";
	private static final String BULK_SIZE = "-bulkSize";
	private static final String HELP = "-help";
	private static final String XML_CONFIG_FILE = "-xmlConfigFile";

	public ArgumentsHandler() {
		/*
		 * LOGGER.debug("Argument handler constructor");
		 * 
		 * configBean = WatchdogConfiguration.getInstance(); dbConfiguration = configBean.getDbConfig(); mqConfiguration = configBean.getMqConfig(); mailConfiguration = configBean.getMailConfig();
		 */
	}

	public void createPropertyFile(WatchdogConfiguration configBean) {

	}

	public WatchdogConfiguration readCommandArgs(String[] args) {

		LOGGER.debug("Reading command arguments");

		try {

			for (int index = 0; index < args.length; index++) {
				String param = args[index];

				LOGGER.trace("index: " + index);
				LOGGER.trace("param: " + param);

				if (XML_CONFIG_FILE.equalsIgnoreCase(param)) {
					LOGGER.debug("xml config file provided");
					XMLConfigParser xmlParser = new XMLConfigParser();
					LOGGER.info("Reading configuration from xml file");
					if (index > 0) {
						LOGGER.info("Ignoring all other arguments");
					}

					return xmlParser.parseConfigFile(args[++index]);

				} else if (DB_SERVER_NAME.equalsIgnoreCase(param)) {
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
				} else if (ECF.equalsIgnoreCase(param)) {
					// ECF
					index++;
					dbConfiguration.setEcf(args[index]);
					ConnectionSettings cs = EnEcfParser.parseECF(args[index]);
					if (cs.getServerName() != null) {
						dbConfiguration.setServerName(cs.getServerName());
					}
					if (cs.getPassword() != null) {
						dbConfiguration.setPassword(cs.getPassword());
					}
					if (cs.getPortNumber() != null) {
						dbConfiguration.setPort(cs.getPortNumber());
					}
					if (cs.getServiceName() != null) {
						dbConfiguration.setServiceName(cs.getServiceName());
					}
					if (cs.getUserName() != null) {
						dbConfiguration.setUsername(cs.getUserName());
					}
				} else if (MQ_SERVER_NAME.equalsIgnoreCase(param)) {
					// MQ server name
					index++;
					mqConfiguration.setServerName(args[index]);
				} else if (MQ_PORT.equalsIgnoreCase(param)) {
					// MQ Port
					index++;
					mqConfiguration.setPort(Integer.parseInt(args[index]));
				} else if (MQ_CHANNEL_NAME.equalsIgnoreCase(param)) {
					// MQ channel name
					index++;
					mqConfiguration.setChannelName(args[index]);
				} else if (MAIL_SERVER.equalsIgnoreCase(param)) {
					// Mail Server
					index++;
					mailConfiguration.setMailServer(args[index]);
				} else if (MAIL_PORT.equalsIgnoreCase(param)) {
					// Mail port
					index++;
					mailConfiguration.setPort(Integer.parseInt(args[index]));
				} else if (MAIL_FROM.equalsIgnoreCase(param)) {
					// Mail address to send from
					index++;
					mailConfiguration.setMailFrom(args[index]);
				} else if (MAIL_TO.equalsIgnoreCase(param)) {
					// Mail to send to
					index++;
					mailConfiguration.setMailTo(args[index]);
				} else if (MAIL_PASSWORD.equalsIgnoreCase(param)) {
					// Mail to send to
					index++;
					mailConfiguration.setPassword(args[index]);
				} else if (MAIL_SUBJECT.equalsIgnoreCase(param)) {
					// Mail Subject
					index++;
					mailConfiguration.setMailSubject(args[index]);
				} else if (MODE.equalsIgnoreCase(param)) {
					// MODE( EVENTS , MESSAGES , Notifications )
					index++;
					char mode;
					switch (args[index].charAt(0)) {
					case 'A':
						mode = 'A';
						break;
					case 'E':
						mode = 'E';
						break;
					case 'M':
						mode = 'M';
						break;
					case 'N':
						mode = 'N';
						break;
					default:
						mode = 'Z';
					}
					configBean.setMode(mode);
				} else if (MAX_THREAD_COUNT.equalsIgnoreCase(param)) {
					// debug
					index++;
					configBean.setMaxThreadCount(Integer.parseInt(args[index]));
				} else if (DEBUG.equalsIgnoreCase(param)) {
					configBean.setDebug(true);
				} else if (DELAY.equalsIgnoreCase(param)) {
					// Delay
					index++;
					configBean.setDelay(Integer.parseInt(args[index]));
				} else if (BULK_SIZE.equalsIgnoreCase(param)) {
					// bulk size
					index++;
					Integer bulkSize = Integer.parseInt(args[index]);

					// This condition to set the maximum to 5000.
					if (bulkSize > 5000) {
						bulkSize = 5000;
					}

					configBean.setBulkSize(bulkSize);
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
		System.out.println("  " + XML_CONFIG_FILE + "\t\t: xml file to read configurations from. If added other command arguments will be ignored");
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
		System.out.println("  " + MODE + "\t\t: Watchdog server mode {A : Messages and events and email notifications, E: Events Only, M: Messages Only, N: Email Notifiations Only}");
		System.out.println("  " + DELAY + "\t\t: Delay between checks");
		System.out.println("  " + MAX_THREAD_COUNT + "\t\t: Maximum number of running threads. Default is 1");
		System.out.println("  " + DEBUG + "\t\t: Run in debug mode ( more logs to be printed ) ");

	}

	public static boolean validateArgs(WatchdogConfiguration config) {

		return config != null;
	}
}
