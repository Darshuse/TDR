package com.eastnets.notifier.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.util.Log4jConfigurer;


/**
 * this class aims to initialize the log4j Logger class 
 * @author SAlababneh
 *
 */
public class LoggingConfig {

	private static Logger logger ;
	
	private static String logFilePath;
	
	MethodInvokingFactoryBean factory;
	
	public String getLogFilePath() {
		return logFilePath;
	}


	public void setLogFilePath(String logFilePath) {
		LoggingConfig.logFilePath = logFilePath;
	}

	public static void init() {
		
		/*manually parse the ApplicationConfig.properties file and get the logfile property,
		 * this was made because the logger was called before we initialize and set the log file path.
		 * the problem : 
		 * 		initializing spring calls the Logger, and as we used to initialize the logger within/after the initialization of spring,
		 * 		there were some exceptions appearing on AIX because we have not set the system property 'LogFilePath' yet ( as its been initialized in spring
		 * 		, so this was made as a workaround to fix it .
		 * the was made for the TFS 27881.
		 * */ 
		
		final String path = System.getenv("EN_REPORTING_CONFIG_HOME");
		if ( path == null ){
			System.out.println("error : the environment variable 'EN_REPORTING_CONFIG_HOME' not set.");
			return;
		}
		String loggerPath= null;
		
		File file= new File(path+ "/ApplicationConfig.properties");
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
			   if ( line.trim().startsWith("logfile") ){
				   if ( line.indexOf("=") > 0  ){
					   line= line.substring( line.indexOf("=") + 1 );
					   
					   //remove the comment if found
					   if ( line.indexOf("#") > 0  ){
						   line= line.substring(0, line.indexOf("#"));
					   }
					   loggerPath=line.trim();
					   //will not stop cause spring use the last value specified to the property 
					   //so we will keep iterating and set the value of loggerPath to the last instance of the 'logfile' property
				   }
			   }
			}
			br.close();
		}catch( Exception ex){
			System.out.println("Reading ApplicationConfig.properties failed with the exception : " + ex );
			System.out.println("ApplicationConfig.properties path is : " + path+ "/ApplicationConfig.properties" );
		}
		
		if ( loggerPath != null ){
			logFilePath = loggerPath;
			initDone();
		}
	}

	public static String initDone()
	{
		try{
			String fp = logFilePath + "/enNotifier.log";
			fp = fp.replace("\\", "/");
			int tooManyLoops = 0;
			while ( fp.contains("//") ){
				fp = fp.replace("//", "/");
				tooManyLoops++;
				if ( tooManyLoops > 100 ){
					break;
				}
			}
			//System.out.println("Initializing logging on : " + fp );
			
			System.setProperty("LogFilePath", defaultString(logFilePath));		
			
			Log4jConfigurer.initLogging("classpath:log4j.properties");
			
			setLogger(Logger.getLogger(LoggingConfig.class));
		}catch(Exception e ){
			System.out.println("The Exception : " + e);
		}
		return "";
	}
	

	private static String defaultString(String string) {
		return defaultString( string, "" );
	}
	private static String defaultString(String string, String defaultValue ) {
		if ( string == null )
			return defaultValue;
		return string;
	}


	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		LoggingConfig.logger = logger;
	}


}
