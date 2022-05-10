package com.eastnets.watchdog.service;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.eastnets.watchdog.checker.Checker;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.dumper.Dumper;
import com.eastnets.watchdog.mail.MailService;
import com.eastnets.watchdog.reader.DBReader;
import com.eastnets.watchdog.reader.Reader;
import com.eastnets.watchdog.utility.ProductChecker;

@Service
public class ServiceInitiator {
	private static final Logger LOGGER = Logger.getLogger(ServiceInitiator.class);

	@Autowired
	WatchDogRepositoryService watchDogService;
	ApplicationContext applicationContext;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Autowired
	ProductChecker productChecker;

	private Reader reader;
	private Dumper dumper;

	public void startApplication(ApplicationContext applicationContext) {

		if (watchdogConfiguration.getDebug()) {
			enableDebugLoggerMode();
		}
		// Here we must check on argument
		LOGGER.debug("check all WatchDog Argumants ... ");
		if (!argumentValidator()) {
			System.exit(0);
		}

		LOGGER.debug("start all WatchDog service ");

		// Here must check on user Roal (user must have SIDE_LOADER)
		boolean checkUserRoals = productChecker.sCheckRoals();
		if (!checkUserRoals) {
			LOGGER.error("The User is not authorized to run WatchDog ....");
			System.exit(1);
			return;
		}

		// Here must check on user itself
		boolean checkUser = productChecker.checkUser(watchdogConfiguration.getDbUsername());
		if (!checkUser) {
			LOGGER.error("Check user failed....");
			System.exit(1);
			return;
		}

		LOGGER.info("WatchDog Roles is checked successfuly...");
		this.applicationContext = applicationContext;

		startCheckerService();
		startReaderService();
		startDumperService();
		startMailService();
	}

	private boolean argumentValidator() {
		if (watchdogConfiguration.getDbType() == null || watchdogConfiguration.getDbType().isEmpty()) {
			displayUsage(watchdogConfiguration.getDbType());
			return false;
		}

		return true;

	}

	private void enableDebugLoggerMode() {

		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}

	private void displayUsage(String value) {

		System.out.printf("%s: missing or invalid paramter%n", value);
		System.out.println("Usage : \n");
		System.out.println("  " + "dbUsername" + "\t\t: user name for database connection");
		System.out.println("  " + "password" + "\t\t: password for databasae connection");
		System.out.println("  " + "serviceName" + "\t\t: The IP address or server name for database connection");
		System.out.println("  " + "dbType" + "\t: oracle or mssql database");
		System.out.println("  " + "port" + "\t\t: database port");
		System.out.println("  " + "dbName" + "\t: database name");
		System.out.println("  " + "serviceName" + "\t: database service name");
		System.out.println("  " + "instanceName" + "\t: instance name");
		System.out.println("  " + "ecf" + "\t\t: Encrypted Connection File to connect to database");
		System.out.println("  " + "mailServer" + "\t\t: Mail server address");
		System.out.println("  " + "mailPort" + "\t\t: Mail server port");
		System.out.println("  " + "mailFrom" + "\t\t: Mail to send from");
		System.out.println("  " + "mailTo" + "\t\t: Mail to send notifications to");
		System.out.println("  " + "mailPassword" + "\t\t: Mail password");
		System.out.println("  " + "mailSubject" + "\t\t: Mail subject	");
		System.out.println("  " + "mode" + "\t\t: Watchdog server mode {A : Messages and events and email notifications, E: Events Only, M: Messages Only, N: Email Notifiations Only}");
		System.out.println("  " + "delay" + "\t\t: Delay between checks");
		System.out.println("  " + "debug" + "\t\t: Run in debug mode ( more logs to be printed ) ");
	}

	private void startMailService() {
		Thread mailService = new Thread(getMailServiceBean());
		if (watchdogConfiguration.getMode() == 'A' || watchdogConfiguration.getMode() == 'N') {
			mailService.start();
		}
	}

	private void startDumperService() {
		LOGGER.debug("Starting Dumper Service");
		Thread messageDumper = new Thread(getDumperBean("messageDumper"));
		Thread eventDumper = new Thread(getDumperBean("eventDumper"));
		switch (watchdogConfiguration.getMode()) {

		case 'A':
			LOGGER.trace("Starting Messages and Events Dumpers");
			messageDumper.start();
			eventDumper.start();
			break;

		case 'E':

			LOGGER.trace("Starting Event Dumper");
			eventDumper.start();
			break;

		case 'M':
			LOGGER.trace("Starting Message Dumper");
			messageDumper.start();
			break;

		default:
			LOGGER.error("Wrong mode - Dumper service won't be started");
			break;
		}
	}

	private Runnable getDumperBean(String dumperBean) {
		LOGGER.trace("Getting Dumper Bean");
		return (Dumper) applicationContext.getBean(dumperBean);
	}

	private void startCheckerService() {
		LOGGER.debug("Starting Checker Service");
		Thread messageChecker = new Thread(getCheckerBean("messageChecker"));
		Thread eventChecker = new Thread(getCheckerBean("eventChecker"));
		switch (watchdogConfiguration.getMode()) {

		case 'A':
			LOGGER.trace("Starting Message and Event checkers");
			messageChecker.start();
			eventChecker.start();
			break;

		case 'E':
			LOGGER.trace("Starting Event checker");
			eventChecker.start();
			break;

		case 'M':
			LOGGER.trace("Starting Message checker");
			messageChecker.start();
			break;

		default:
			LOGGER.error("Wrong mode - Checker service won't be started");
			break;

		}
	}

	private void startReaderService() {
		LOGGER.debug("Starting Reader Service");
		Thread readerThread = new Thread(getReaderBean());
		readerThread.start();
	}

	private MailService getMailServiceBean() {
		LOGGER.trace("Getting Mail Service Bean");
		return (MailService) applicationContext.getBean("mailService");
	}

	public Reader getReaderBean() {
		LOGGER.trace("Getting Reader Bean");
		LOGGER.trace("Getting DB Reader Bean");
		return (DBReader) applicationContext.getBean("DBReader");
	}

	private Checker getCheckerBean(String checkerBean) {
		LOGGER.trace("Getting Checker Bean");
		return (Checker) applicationContext.getBean(checkerBean);
	}

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public Dumper getDumper() {
		return dumper;
	}

	public void setDumper(Dumper dumper) {
		this.dumper = dumper;
	}

}
