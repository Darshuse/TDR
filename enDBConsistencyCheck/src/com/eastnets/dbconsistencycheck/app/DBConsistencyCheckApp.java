package com.eastnets.dbconsistencycheck.app;

import java.text.SimpleDateFormat;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.eastnets.application.BaseApp;
import com.eastnets.config.DBType;
import com.eastnets.dbconsistencycheck.endec.ConnectionSettings;
import com.eastnets.dbconsistencycheck.endec.EnEcfParser;
import com.eastnets.dbconsistencycheck.loadervalidators.CommandParamValidator;


public class DBConsistencyCheckApp extends BaseApp {

	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger("DBConsistencyCheck");

	private static  DBConsistencyCheckConfig 	appConfigBean;
	private static  CommandParamValidator 		validator;
	private static 	String 						ecfFile; 
	static 	SimpleDateFormat 					dateFormat ;

	final static DBConsistencyCheckApp app = new DBConsistencyCheckApp();
	final static String dbCheckHeader = "Eastnets DB Consistency Check App - 1.0.0 build 6";


	public static SimpleDateFormat getDateFormat(String format) {
		return dateFormat = new SimpleDateFormat(format);
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		DBConsistencyCheckApp.dateFormat = dateFormat;
	}

	private static ReportInfoBean infoBean;
	
	
	public ReportInfoBean getInfoBean() {
		return infoBean;
	}

	public void setInfoBean(ReportInfoBean infoBean) {
		DBConsistencyCheckApp.infoBean = infoBean;
	}

	public static void main(String[] args) {
		try{
			boolean result;
			String checkParam;

			// write the header
			System.out.println(dbCheckHeader);
			System.out.println(String.format("%0" + (dbCheckHeader.length()) + "d", 0).replace("0", "-"));

			appConfigBean = new DBConsistencyCheckConfig();

			// load args params
			result = loadParameters(args, appConfigBean);
			if (!result) 
				return;
			

			validator = new CommandParamValidator();

			// check database connection parameter
			checkParam = validator.checkDBConnection(appConfigBean);
			if (!checkParam.equals("VALID")) {
				app.displayUsage(checkParam);
				return;
			}

			// initialize services
			app.init(appConfigBean);
			app.log.debug("Initializing databases consistency check service done ");

			// display passed db arguments
			app.displayDBArguments();

			// check mandatory parameter
			checkParam = validator.checkMandatoryParam(appConfigBean);
			if (!checkParam.equals("VALID")) {
				app.displayUsage(checkParam);
				return;
			}
			
			// display passed app arguments
			app.displayAppArguments();

			// check mandatory Mail parameters
			if (!(appConfigBean.getMailHost().trim().isEmpty()) && appConfigBean.getMailHost() != null ){

				checkParam = validator
						.checkEmailMandatoryParams(appConfigBean);

				if (!checkParam.equals("VALID")) {
					app.displayUsage(checkParam);
					return;
				}
				// display passed Mail arguments
				app.displayMailArguments();
			}
			
			infoBean = new ReportInfoBean();
			

			// start with new thread
			ProcessDBCheckThread dbCheckProcess = new ProcessDBCheckThread(app, appConfigBean, infoBean);
			Thread thread1 = new Thread(dbCheckProcess, "Thread 1");
			thread1.start();

			if (appConfigBean.getReportTime() != null){
				// start another thread
				SendReportThread sendReport = new SendReportThread(app, appConfigBean, infoBean);

				Thread thread2 = new Thread(sendReport, "Thread 2");
				thread2.start();				
			}

		}catch(Exception e){
			app.log.error(e.getMessage());
		}
	}

	//================================================================================================
	public static boolean loadParameters(String[] args, DBConsistencyCheckConfig appConfigBean) {		
		try {

			app.log.info("Passing user parameters into application");

			if (args.length<=0){
				app.log.error(" Missing or invalid parameters");
				app.displayUsage("");
				return false;
			}

			appConfigBean.setDatabaseType(DBType.ORACLE);

			for (int i = 0; i < args.length; i++) {

				String value = args[i];

				// database user name
				if ("-u".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setUsername(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// database password
				else if ("-p".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setPassword(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// database ip
				else if ("-ip".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setServerName(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// database type
				else if ("-dbtype".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						if (args[i].equalsIgnoreCase("mssql"))
							appConfigBean.setDatabaseType(DBType.MSSQL);
						else
							appConfigBean.setDatabaseType(DBType.ORACLE);
					} else {
						app.displayUsage(value);
						return false;
					}
				}

				// database port
				else if ("-port".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setPortNumber(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// database name
				else if ("-dbname".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setDatabaseName(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				}
				// instance name
				else if ("-instancename".equalsIgnoreCase(value)) {
					if (i++ < args.length)
						appConfigBean.setInstanceName(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// service name
				else if ("-servicename".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setDbServiceName(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// partitioned
				else if ("-partitioned".equalsIgnoreCase(value)) 
					appConfigBean.setPartitioned(true);

				// SAA ip address
				else if ("-SAA_IP".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setSAAServer(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 
				// SAXS component port
				else if ("-SAXS_PORT".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setSAAPort(Integer.parseInt(args[i])+1);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// report file path
				else if ("-RptFile".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setReportPath(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// mail ip address
				else if ("-mail".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailHost(args[i]);
					else {
						app.displayUsage(value);
						return false;
					}			
				} 

				// mail server port
				else if ("-mailport".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setMailPort(args[i]);
					} else {
						app.displayUsage(value);
						return false;
					}					
				} 

				// mail server user name
				else if ("-mailuser".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailUsername(args[i]);
					else {
						app.displayUsage(value);
						return false;
					} 
				}	

				// mail server password
				else if ("-mailpassword".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailPassword(args[i]);
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// from mail address
				else if ("-mailfrom".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailFrom(args[i]);
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// to mail address
				else if ("-mailto".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailTo(args[i]);
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// cc mail address
				else if ("-mailcc".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMailCc(args[i]);
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// mail server no authentication
				else if ("-mailNoAuth".equalsIgnoreCase(value)) {
					appConfigBean.setMailAuthenticationRequired(false);
					app.log.info("Disables mail server authentication.");
				}

				// alliance id
				else if ("-I".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setAid(Integer.parseInt(args[i]));
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// verbose mode
				else if ("-v".equalsIgnoreCase(value)) {
					appConfigBean.setVerbose(true);
					app.log.setLevel(Level.DEBUG);
					app.log.info("Debug started......");

				}

				// Encrypted  file
				else if ("-ecf".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						ecfFile = args[i];
						app.log.info("Starting to parse Connection setting on ECF ...");
						ConnectionSettings cs = EnEcfParser.parseECF(ecfFile);

						if (cs.getServerName() != null) 
							appConfigBean.setServerName(cs.getServerName());

						if (cs.getUserName() != null) 
							appConfigBean.setUsername(cs.getUserName());

						if (cs.getPassword() != null) 
							appConfigBean.setPassword(cs.getPassword());

						if (cs.getPortNumber() != null) 
							appConfigBean.setPortNumber(cs.getPortNumber()
									.toString());

						if (cs.getServiceName() != null) 
							appConfigBean.setDatabaseName(cs.getServiceName());

						app.log.info("parsing Connection setting on ECF completed");
					} else {
						app.displayUsage(value);
						return false;
					}

				} 


				// frequency	
				else if ("-frequency".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setFrequency(Integer.parseInt(args[i]));

						if (appConfigBean.getFrequency() > 24){
							app.log.error("The frequency param, should be less than or equal 24");
							app.displayUsage("");
							return false;
						}

						if (appConfigBean.getFrequency() <= 0){
							app.log.error("The frequency param, should be greater than 0");
							app.displayUsage("");
							return false;
						}
					} else {
						app.displayUsage(value);
						return false;
					} 

				}

				// day number
				else if ("-d".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setDayNum(Integer.parseInt(args[i]));
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// active time
				/*else if ("-ActiveTime".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setActiveTime((args[i]));
					else {
						app.displayUsage(value);
						return false;
					} 

				}*/
				
				else if ("-SleepPeriod".equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						
						appConfigBean.setSleepPeriod(Integer.parseInt(args[i]));
						
						if (appConfigBean.getSleepPeriod() > 23){
							app.log.error("The sleep period param, should be less than or equal 23");
							app.displayUsage("");
							return false;
						}

						if (appConfigBean.getFrequency() < 0){
							app.log.error("The sleep period param, should be greater than or equal 0");
							app.displayUsage("");
							return false;
						}
					}
						
					else {
						app.displayUsage(value);
						return false;
					} 

				}

				// sleep time
				else if ("-SleepTime".equalsIgnoreCase(value)) {
					if (i++ < args.length)
						appConfigBean.setSleepTime((getDateFormat("kk:mm:ss").parse(args[i])));						
					else {
						app.displayUsage(value);
						return false;
					} 

				}

				// end of day report
				else if ("-EodRrt".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setReportTime((getDateFormat("kk:mm:ss").parse(args[i])));
					else {
						app.displayUsage(value);
						return false;
					} 
				}

				// mode
				else if ("-mode".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setMode(Integer.parseInt(args[i]));
					else {
						app.displayUsage(value);
						return false;
					}
				} 

				// force update
				else if ("-forceUpdate".equalsIgnoreCase(value)) 
					appConfigBean.setForceUpdate(true);
				
				
				// report frequency
				else if ("-RptFrequency".equalsIgnoreCase(value)) {
					if (i++ < args.length) 
						appConfigBean.setReportFrequency(Integer.parseInt(args[i]));
					else {
						app.displayUsage(value);
						return false;
					}
				} 
				
			}

			// default values
			if (appConfigBean.getServerName().isEmpty() == true)
				appConfigBean.setServerName("localhost");

			return true;

		} catch (Exception e) {
			app.log.error("Failed to pass user parameters into application, reason : "+ e.getMessage());
			app.displayUsage("");
			return false;
		}		
	}
	//================================================================================================

	public void displayUsage(String value) {
		System.out.printf("%s : missing or invalid parameter \n", value);
		System.out.println("Usage : ");
		System.out.println("  -u <user name>\t\t: user name for database connection.");
		System.out.println("  -p <password>\t\t\t: password for databasae connection.");
		System.out.println("  -ip <server name>\t\t: The IP address or server name for database connection, default value is localhost.");
		System.out.println("  -dbtype <oracle| mssql>\t: oracle or mssql database, default value is oracle.");
		System.out.println("  -port <port_number>\t\t: database port, default value is 1521 for oracle and 1433 for mssql.");
		System.out.println("  -dbname <database_name>\t: database name, default value is SIDEDB for mssql.");
		System.out.println("  -servicename\t\t\t: service name, for oracle database type only");
		System.out.println("  -instancename\t\t\t: instance name, for mssql database type only");
		System.out.println("  -partitioned\t\t\t: partition database for oracle database type , default value is false");
		System.out.println("  -SAA_IP <IP address>\t\t: IP address of the SAA SAXS component.");
		System.out.println("  -SAXS_PORT <port number>\t: Port on which the SAA SAXS component listens.");
		System.out.println("  -RptFile <path/filename>\t\t: Defines the report file path + name.");
		System.out.println("  {-mail <IP address>}\t\t: Mail server IP address.");
		System.out.println("  {-mailport <port number>}\t: Mail server Port Number.");
		System.out.println("  {-mailuser <user name>}\t: Mail server user name.");
		System.out.println("  {-mailpassword <password>}\t: Mail server password (should be encrypted)");
		System.out.println("  {-mailfrom <email address>}\t: Specifies the email sender address");
		System.out.println("  {-mailto <email address>}\t: Specifies the email receiver address");
		System.out.println("  {-mailcc <email address>}\t: Specifies the email carbon copy address");
		System.out.println("  {-mailNoAuth}\t\t\t: Disables mail server authentication.");
		System.out.println("  -I <ID>\t\t\t: SWIFT Alliance Access ID. The default is 0.");
		System.out.println("  {-v}\t\t\t\t: Verbose mode.");
		System.out.println("  {-ecf} <encfilename.ecf>\t: Encrypted File used when encryption is needed. The file contains the username, password, and server name used to login to the database.");
		System.out.println("  -frequency <number in hours>\t: The number of hours between two checks, min = 1, max = 24");
		System.out.println("  -d <number>\t\t\t: The number of days back from today to be checked. Example: -d 5");
		//System.out.println("  -ActiveTime <HH24:MI:SS>\t: Active cycle Start time.");
		System.out.println("  -SleepTime <HH24:MI:SS>\t: End time of the cycle");
		System.out.println("  -SleepPeriod <number>\t: the number of sleep period to sleep the target process after sleep time, min = 0, max = 23");
		System.out.println("  {-EodRrt <HH24:MI:SS>}\t: End of day report time, if not specified, the report will not be sent. Example: 17:00:00");
		System.out.println("  {-RptFrequency <number>}\t: Number of notification to send email");
		//System.out.println("  {-forceUpdate} \t: to insert record in ldRequestUpdate table to get update for message.");
	}

	//================================================================================================

	public void displayDBArguments() {
		System.out.println("Passed arguments:");
		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));
		System.out.println("Database server Type\t\t: " + (appConfigBean.getDatabaseType() == DBType.ORACLE ? "ORACLE" : "MSSQL"));

		if ( ecfFile == null ){
			System.out.println("Database server name\t\t: " + appConfigBean.getServerName());

			if (appConfigBean.getDatabaseType() == DBType.ORACLE && appConfigBean.getDbServiceName().isEmpty())
				System.out.println("Database global name\t\t: " + appConfigBean.getDatabaseName());	
			else if (!appConfigBean.getDatabaseName().isEmpty())
				System.out.println("Database service name\t\t: " + appConfigBean.getDbServiceName());

			if ((appConfigBean.getDatabaseType() == DBType.MSSQL) && !appConfigBean.getInstanceName().isEmpty())
				System.out.println("Database instance name\t\t: " + appConfigBean.getInstanceName());
			

			System.out.println("Database server port\t\t: " + appConfigBean.getPortNumber());
			System.out.println("Database user name\t\t: " + appConfigBean.getUsername());
			System.out.println("Database user password\t\t: "
					+ String.format("%0" + (appConfigBean.getPassword().length()) + "d", 0).replace("0", "*"));
		} 

		else 
			System.out.println("Encrypted connection file\t\t: " + ecfFile);

		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));

	}

	//================================================================================================

	public void displayAppArguments() {
		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));
		System.out.println("SAA IP Address\t\t: "+ appConfigBean.getSAAServer());
		System.out.println("SAXS Port\t\t: "+ (appConfigBean.getSAAPort()));
		System.out.println("Alliance Access ID\t: "+ (appConfigBean.getAid()));
		System.out.println("Frequency\t\t: "+ (appConfigBean.getFrequency()));
		//System.out.println("Active Time\t\t: "+ (appConfigBean.getActiveTime()));
		System.out.println("Sleep Period\t\t: "+ (appConfigBean.getSleepPeriod()));
		System.out.println("Sleep Time\t\t: "+ ((getDateFormat("kk:mm:ss").format(appConfigBean.getSleepTime().getTime()))));
		System.out.println("Verbose Mode\t\t: "+ (appConfigBean.isVerbose()? "ON" : "OFF"));
		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));
	}

	//================================================================================================

	public void displayMailArguments() {
		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));
		System.out.println("Mail Server\t\t: "+ (appConfigBean.getMailHost()));
		System.out.println("Mail UserName\t\t: "+ (appConfigBean.getMailUsername()));
		System.out.println("Mail From\t\t: "+ (appConfigBean.getMailFrom()));
		System.out.println("Mail To\t\t\t: "+ (appConfigBean.getMailTo()));
		System.out.println("Mail CC\t\t\t: "+ (appConfigBean.getMailCc()));
		System.out.println(String.format("%0" + (20) + "d", 0).replace("0", "-"));
	}
	//================================================================================================

}

