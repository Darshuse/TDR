package com.eastnets.reportingserver;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.reportingserver.encdec.ConnectionSettings;
import com.eastnets.reportingserver.encdec.EnEcfParser;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.reportingserver.reportGenerators.DatabaseReportGenerator;
import com.eastnets.reportingserver.reportGenerators.FileReportGenerator;
import com.eastnets.reportingserver.reportGenerators.MailReportGenerator;
import com.eastnets.reportingserver.reportGenerators.PrinterReportGenerator;
import com.eastnets.reportingserver.reportGenerators.TextControlReportGenerator;
import com.eastnets.reportingserver.reportvalidators.CommandParamValidator;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.reporting.ReportingService;

public class ReportingServerApp extends BaseApp implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4458224637062118428L;
	public static ReportingServerApp reportingServerApp;
	public static String what = "Reporting Generator Server 3.2.0 Build 9";
	static Logger log;
	static {
		log = Logger.getLogger(ReportingServerApp.class.getName());
		reportingServerApp = new ReportingServerApp();

	}

	private static AppConfigBean appConfigBean = null;

	public static AppConfigBean getAppConfigBean() {

		if (appConfigBean == null) {
			appConfigBean = new AppConfigBean();
		}
		return appConfigBean;
	}

	public ReportingServerApp() {
		getAppConfigBean().setSchemaName("side");
		getAppConfigBean().setTableSpace("sidedb");
	}

	public void displayUsage(String value) throws Exception {

		System.out.printf("%s: missing or invalid parameter\n", value);
		System.out.println("Usage : \n");
		System.out.println("  -U\t\t: user name for database connection");
		System.out.println("  -P\t\t: password for databasae connection");
		System.out.println("  -IP\t\t: The IP address or server name for database connection, default is localhost.");
		System.out.println("  -ecf\t\t: passs the Encrypted Connection File to connect to database");
		System.out.println("  -dbtype\t: oracle or mssql database, default is oracle");
		System.out.println("  -port\t\t: database port, default 1521 for oracle and 1433 for mssql");
		System.out.println("  -dbname\t: database name");
		System.out.println("  -servicename\t: ORACLE service name");
		System.out.println("  -instancename : MSSQL Instance Name ");

		System.out.println("  -tnsEnabled \t: another option to connect to oracle DB using tns ora (optional)");
		System.out.println("  -tnsPath \t: if tns enabled the tns ora path should be provided (optional if tns connection way not used)");

		System.out.println("  -rtemplatepath: reports templates directory path");
		System.out.println("  -rpath\t: reports directory path");
		System.out.println("  -r\t\t: file report name or #<report ID>, and -r represent report id only if the -s parameter is used");
		System.out.println("  -s\t\t: parameters set name");
		System.out.println("  -l\t\t: display all reports names and parameters set");
		System.out.println("  -f\t\t: output format");
		System.out.println("			EXCEL	: Microsoft Excel file.");
		System.out.println("			WORD	: Microsoft Word file.");
		System.out.println("			PDF	: Portable Document Format file.");
		System.out.println("  -d\t\t: destination ( see -n option )");
		System.out.println("			PRINT : send report to printer. Option by default.");
		System.out.println("			FILE  : send report to file.");
		System.out.println("			EMAIL : send report to email.\n");
		System.out.println(" ");
		System.out.println("				SMTP MODE :");
		System.out.println("				-----------");
		System.out.println("			-smtp    : smtp server (mandatory)");
		System.out.println("			-Mport   : smtp port server (optional; 25 by default)");
		System.out.println("			-MU      : smtp server username optional if use -noMailAuthentication");
		System.out.println("			-MP      : smtp server password optional if use -noMailAuthentication");
		System.out.println("-noMailAuthentication : if your email doesn't need authentication");
		System.out.println("			-from    : sender name, formatted for your server (mandatory)");
		System.out.println("			-to      : recipients names separated by ';' (mandatory)");
		System.out.println("			-cc      : recipients names separated by ';' (optional)");
		System.out.println("			-subject : subject description (optional)");
		System.out.println("			-body    : message description (optional)");

		System.out.println(" ");
		System.out.println("  -p\t\t: printer name, can be use with -d PRINT only.");
		System.out.println("      \t\t  if no printer is specified, default printer is selected.");
		System.out.println("  -n\t\t: output file name (mandatory)");
		System.out.println("  -c\t\t: copies number. Default is 1");
		System.out.println("  -reportMaxFetchSize\t\t: maximum number of records per report. Default is 1000");
		System.out.println("  -debug\t: Enable debugging to include more details in the log file. This is used in problem troubleshooting.");
		System.out.println("  -schedule\t: Enable schedule Mode.");
		System.out.println("  -maxAttachSize\t: Max Attachment size in MB. Default is 2M.");

		System.out.println("  -reportLogoPath\t: absolute file name of the logo to be added to the reports.");

	}

	public boolean loadParameters(String[] args) throws Exception {
		log.debug("Passing user parameter into application...");
		try {
			AppConfigBean configBean = getAppConfigBean();
			CommandParamValidator validator = new CommandParamValidator();

			// default config values for database connection
			configBean.setDatabaseType(DBType.ORACLE);
			configBean.setPortNumber("1521");
			configBean.setDatabaseName("");
			for (int i = 0; i < args.length; i++) {
				String value = args[i];
				if ("-U".equals(value)) {

					if (i++ < args.length) {
						configBean.setUsername(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-P".equals(value)) {

					if (i++ < args.length) {
						configBean.setPassword(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-IP".equals(value)) {

					if (i++ < args.length) {
						configBean.setServerName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-ecf".equals(value)) {

					if (i++ < args.length) {
						log.debug("Starting to parse Connection setting on ECF ...");
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
						log.debug("parsing Connection setting on ECF completed");

					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-dbname".equals(value)) {

					if (i++ < args.length) {
						configBean.setDatabaseName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-servicename".equals(value)) {

					if (i++ < args.length) {
						configBean.setDbServiceName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-dbtype".equals(value)) {

					if (i++ < args.length) {
						configBean.setDatabaseType(args[i].toLowerCase().equals("mssql") ? DBType.MSSQL : DBType.ORACLE);
						if (args[i].toLowerCase().equals("mssql") && configBean.getPortNumber().equals("1521"))
							configBean.setPortNumber("1433");
					} else {
						displayUsage(value);
						return false;
					}

				} else if ("-instancename".equals(value)) { /* Add instanceName Argument */

					if (i++ < args.length) {
						configBean.setInstanceName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-port".equals(value)) {

					if (i++ < args.length) {
						configBean.setPortNumber(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-noMailAuthentication".equals(value)) {
					configBean.setMailAuthenticationRequired(false);
				} else if ("-rtemplatepath".equals(value)) {

					if (i++ < args.length) {
						configBean.setReportsDirectoryPath(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-rpath".equals(value)) {

					if (i++ < args.length) {
						configBean.setOutpuReportPath(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-r".equals(value)) {

					if (i++ < args.length) {
						configBean.setReportId(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if ("-s".equals(value)) {

					if (i++ < args.length) {
						configBean.setCriteriaName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-l".equals(value)) {

					configBean.setShowAllCriteriasName(true);
				} else if ("-f".equals(value)) {

					if (i++ < args.length) {
						if (configBean.getReportTypes() == null)
							configBean.setReportTypes(args[i]);
						else {
							displayUsage("Multiple " + value + ", One format should be determined at each run");
							return false;
						}
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-d".equals(value)) {

					if (i++ < args.length) {

						if (validator.checkDestinationValue(args[i])) {
							configBean.setDestenationTypes(args[i]);
						} else {
							displayUsage(value + " must be ( PRINT | EMAIL | FILE ) ");
							return false;
						}

					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-p".equals(value)) {

					if (i++ < args.length) {
						configBean.setPrinterName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-n".equals(value)) {

					if (i++ < args.length) {
						configBean.setOutpuReportName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-c".equals(value)) {

					if (i++ < args.length) {
						configBean.setCopiesCount(Integer.parseInt(args[i].trim()));
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-smtp".equals(value)) {

					if (i++ < args.length) {
						configBean.setMailHost(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-Mport".equals(value)) {

					if (i++ < args.length) {
						configBean.setMailPort(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-MU".equals(value)) {

					if (i++ < args.length) {
						configBean.setMailUsername(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-MP".equals(value)) {

					if (i++ < args.length) {
						configBean.setMailPassword(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-from".equals(value)) {

					if (i++ < args.length) {
						configBean.setMailFrom(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-to".equals(value)) {

					if (i++ < args.length) {
						if (isValidEmails(args[i], validator))
							configBean.setMailTo(args[i]);
						else
							return false;
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-cc".equals(value)) {

					if (i++ < args.length) {
						if (isValidEmails(args[i], validator))
							configBean.setMailCc(args[i].replaceAll(";", ","));
						else
							return false;
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-reportMaxFetchSize".equals(value)) {
					if (i++ < args.length) {
						if (!args[i].matches("\\d+")) {
							displayUsage(value);
							return false;
						}
						configBean.setReportMaxFetchSize(Integer.parseInt(args[i]));
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-debug".equals(value)) {
					configBean.setDebug(true);
					log.setLevel(Level.DEBUG);
					log.info("debug started......");
				} else if ("-schedule".equals(value)) {
					configBean.setScheduleMode(true);
					log.info("Schedule Mode Started......");
				} else if ("-maxAttachSize".equals(value)) {
					if (i++ < args.length) {
						configBean.setMaxAttachSize(Integer.parseInt(args[i]));
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-subject".equals(value)) {

					if (i++ < args.length) {

						if (!args[i].isEmpty() && !args[i].startsWith("-")) {
							configBean.setMailSubject(args[i]);
						}
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-body".equals(value)) {

					if (i++ < args.length) {
						if (!args[i].isEmpty() && !args[i].startsWith("-"))
							configBean.setMailBody(args[i]);
					} else {
						displayUsage(value);
						return false;
					}
				} else if ("-tnsEnabled".equals(value)) {

					if (i++ < args.length) {
						if (!args[i].isEmpty() && !args[i].startsWith("-"))
							configBean.setTnsEnabled(Boolean.parseBoolean(args[i]));
					}
				} else if ("-tnsPath".equals(value)) {

					if (i++ < args.length) {
						if (!args[i].isEmpty() && !args[i].startsWith("-"))
							configBean.setTnsPath(args[i]);
					}
				} else if ("-reportLogoPath".equals(value)) {
					if (i++ < args.length) {
						if (!args[i].isEmpty() && !args[i].startsWith("-"))
							configBean.setReportLogoPath(args[i]);
					}
				}

				else {
					displayUsage(value);
					return false;
				}
			}
		} catch (Exception ex) {
			displayUsage("");
			return false;
		}
		log.debug("Passing parameter into application successfully");
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			System.out.println(what);
			System.out.println("Copyright 1999-2021 EastNets");
			System.out.println();

			if (reportingServerApp.loadParameters(args) == false) {
				return;
			}

			AppConfigBean configBean = getAppConfigBean();
			CommandParamValidator validator = new CommandParamValidator();

			String connectionMissingChar = validator.checkDBConnection(configBean);

			/**
			 * TNS connection way feature enabled , set service name equals to server name , since no server details rather than service name
			 */
			if (connectionMissingChar.equals("VALID") && configBean.getTnsEnabled() && (configBean.getDbServiceName() == null || configBean.getDbServiceName().isEmpty())) {
				configBean.setDbServiceName(configBean.getServerName());
			}

			if (!connectionMissingChar.equals("VALID")) {
				reportingServerApp.displayUsage(connectionMissingChar);
				log.error("DB params in not valid");
				return;
			}
			if (configBean.isScheduleMode()) {
				@SuppressWarnings("unused") // to overcome warning
				ReportingServerSchedule reportingServerSchedule = null;
				reportingServerSchedule = new ReportingServerSchedule(configBean);
			} else {

				if (configBean.isShowAllCriteriasName()) {
					log.debug("getting ready to print all Criterias Names.. ");
					reportingServerApp.init(configBean);
					reportingServerApp.printAllCriteriasName();
					log.debug("print all Criterias Names successfully Done");

					return;
				}

				if (getAppConfigBean().getDestenationTypes() == null) {
					configBean.setDestenationTypes("");
					log.error("Destenation Types is not set ");
				}

				if (getAppConfigBean().getReportTypes() == null) {
					log.error("Report Type is not defined .");
					reportingServerApp.displayUsage("-f");
					return;
				}

				String reportMissingChar = validator.checkReportMandatoryParam(configBean);

				if (!reportMissingChar.equals("VALID")) {
					log.error("one or more Mandatory params were not set ");
					reportingServerApp.displayUsage(reportMissingChar);
					return;
				}
				log.debug("Mandatory params successfully passed validation ");
				String additionalParam = validator.checkAdditionalParam(configBean);

				if (!additionalParam.equals("VALID")) {
					log.error("one or more Additional params were not set ");
					reportingServerApp.displayUsage(additionalParam);
					return;
				}
				log.debug("Additional params successfully passed validation ");

				String emailMissingChar = validator.checkEmailMandatoryParams(configBean);

				if (!emailMissingChar.equals("VALID")) {
					log.error("one or more Email params were not set ");
					reportingServerApp.displayUsage(emailMissingChar);
					return;
				}
				log.debug("Email params successfully passed validation");

				if (validator.checkFileName(configBean)) {
					log.error("File name validation Failed ");
					reportingServerApp.displayUsage("-n is mandatory and must not contains one of the following character ?\\/:\"*<>|");
					return;
				}
				log.debug("Assigning parameter to the application...");
				reportingServerApp.init(configBean);
				log.debug("Assigning parameter to the application successfully done");

				Thread thread = new Thread(reportingServerApp);
				thread.start();
			}

		} catch (Exception ex) {
			if (ex.getMessage().contains("JDBC Connection")) {
				log.info("The connection to the database could not be established");
			} else {
				log.error("Error", ex);
			}
		}
	}

	@Override
	public void run() {

		try {
			log.info("Process Start");
			ConfigBean cnfgBean = reportingServerApp.getConfigBean();
			log.debug("Preparing to initialize the report generator... ");
			ReportGenerator reportGenerator = initReportGenerator(cnfgBean);

			log.debug("Preparing to initialize the report service... ");
			ServiceLocator serviceLocator = this.getServiceLocater();
			ReportingService reportingService = serviceLocator.getReportingService();
			log.debug("Initializing report and the report service Done ");

			log.debug("Get the User Name...");
			String userName = ReportingServerApp.getAppConfigBean().getUsername();
			log.debug("User Name : " + userName);
			User user = null;
			if (serviceLocator.getAdminService().checkRole("SIDE_REPORT")) {
				user = serviceLocator.getAdminService().getUserWithoutRoles(userName);
				if (user == null) {
					reportingServerApp.displayUsage("Provided user is not a reporting users.");
					log.error("This User is not a Reporting User ");
					return;
				} else if (user.getApprovalStatus().getId() != 0) {
					log.error(user.getApprovalStatus().getDescription());
					return;
				}
			} else {
				log.error("Provided user does not have reporting role. ");
				return;
			}

			if (getAppConfigBean().getCriteriaName() != null && getAppConfigBean().getCriteriaName().length() > 0 && getAppConfigBean().getReportId() != null && getAppConfigBean().getReportId().length() > 0) {
				Long reportId = null;

				try {
					log.debug("Passing the report Id to the application...");
					reportId = new Long(getAppConfigBean().getReportId());
				} catch (NumberFormatException e) {
					reportingServerApp.displayUsage("-r must be report id");
					log.error("Error in the report Id  ");
					return;
				}

				log.info("Criteria Name: " + getAppConfigBean().getCriteriaName() + " Report Id: " + reportId);

				log.debug("Getting ready to pass the report criteria to the Report...");
				ReportSet reportSet = new ReportSet();
				reportSet = reportingService.getReportSet(userName, reportId, getAppConfigBean().getCriteriaName());

				if (reportSet != null && reportSet.getId() != null) {
					log.debug("Getting ready to generate the report...");
					reportGenerator.generateReport(reportSet.getId(), user, reportSet.getName());
					log.info("Report  Generated Successfully ");
				} else {
					log.error("Criteria not found");
				}
			} else if (getAppConfigBean().getReportId() != null && getAppConfigBean().getReportId().length() > 0) {

				Report report = null;

				try {
					log.debug("Getting ready to find the report by Id ...");
					Long reportId = new Long(getAppConfigBean().getReportId());
					log.debug("Getting ready to generate the report ...");
					report = reportingService.getReport(userName, reportId);
				} catch (NumberFormatException e) {
					report = reportingService.getReport(userName, getAppConfigBean().getReportId());
				}
				log.debug("Report generated successfully ");
				if (report == null) {
					log.error("Report Not Found");
					return;
				}

				log.info("Report Name: " + report.getName());
				log.debug("Getting ready to get the Report Sets...");
				List<ReportSet> reportSets = reportingService.getReportSets(userName, report);
				if (reportSets == null || reportSets.isEmpty()) {
					return;
				}

				for (ReportSet reportSet : reportSets) {

					log.info("Criteria Name: " + reportSet.getName());
					reportGenerator.generateReport(reportSet.getId(), user, reportSet.getName());
				} // for

			}
		} catch (Exception ex) {
			log.error("Error", ex);
		}
	}

	public ReportGenerator initReportGenerator(ConfigBean cnfgBean) throws Exception {

		ReportGenerator reportGenerator = null;

		if (getAppConfigBean().isScheduleMode()) {
			if (getAppConfigBean().getDestenationTypes() == null) {
				reportGenerator = new DatabaseReportGenerator();
			}
			if (getAppConfigBean().getDestenationTypes().name().equalsIgnoreCase(ConfigBean.DestenationTypes.custom.name())) {
				reportGenerator = new TextControlReportGenerator();
			} else {
				reportGenerator = new DatabaseReportGenerator();
			}

		} else if (getAppConfigBean().getDestenationTypes().name().equalsIgnoreCase(ConfigBean.DestenationTypes.file.name()))
			reportGenerator = new FileReportGenerator();
		else if (getAppConfigBean().getDestenationTypes().name().equalsIgnoreCase(ConfigBean.DestenationTypes.email.name()))
			reportGenerator = new MailReportGenerator();
		else if (getAppConfigBean().getDestenationTypes().name().equalsIgnoreCase(ConfigBean.DestenationTypes.printer.name()))
			reportGenerator = new PrinterReportGenerator();

		return reportGenerator;
	}

	private void printAllCriteriasName() throws Exception {

		ServiceLocator serviceLocator = this.getServiceLocater();
		ReportingService reportingService = serviceLocator.getReportingService();
		String userName = ReportingServerApp.getAppConfigBean().getUsername();

		List<Report> reports = reportingService.getReports(userName);

		if (reports == null || reports.isEmpty()) {
			return;
		}

		for (Report report : reports) {
			String reportId = String.format("%-10s%-10s%-10s", "Report Id :", report.getId(), " || ");
			log.info(reportId + "Report Name: " + report.getName());
			List<ReportSet> reportSets = reportingService.getReportSets(userName, report);

			if (reportSets == null || reportSets.isEmpty()) {
				continue;
			}

			for (ReportSet reportSet : reportSets) {
				String cirteriaName = String.format("%-4s%-10s%-10s", "\t\t Criteria Name:", "||", reportSet.getName());
				log.info(cirteriaName);
			} // for
		} // for

	}

	private boolean isValidEmails(String emails, CommandParamValidator validator) throws Exception {

		boolean valid = true;

		String[] emailsList = emails.split("(;|,)");

		for (int i = 0; i < emailsList.length; ++i) {
			if (!validator.isValidEmail(emailsList[i].trim())) {
				valid = false;
				displayUsage(emailsList[i] + " is not a valid email");
				break;
			}
		}

		return valid;
	}
}
