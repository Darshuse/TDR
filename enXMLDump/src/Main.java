import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastnets.resilience.ApplicationMonitor;
import com.eastnets.resilience.ArgumentHandler;
import com.eastnets.resilience.EnLogger;
import com.eastnets.resilience.EnXmlDumpConfig;
import com.eastnets.resilience.EnXmlDumpConfig.DBType;
import com.eastnets.resilience.ObjectCreator;
import com.eastnets.resilience.XmlHandler;

public class Main {

	// add comment

	// ================================================================================================
	public static void main(String[] args) {

		// getGlobalConfig will fill the command line arguments into the EnXmlDumpConfig object,
		// after that it validates the arguments, when ever anything wrong happens it will log an error and return null
		EnXmlDumpConfig globalConfig = ArgumentHandler.getGlobalConfig(args);
		if (globalConfig == null) {
			// we log the error when it happens so just die
			return;
		}

		Connection connection = getConnection(globalConfig);
		if (connection == null) {
			// we log the error when it happens so just die
			return;
		}

		if (globalConfig.isLogInfo()) {
			try {
				EnLogger.setBufferWriter(new BufferedWriter(new FileWriter(globalConfig.getFilePath() + ".log")));
			} catch (IOException e) {
			}
		}
		// EnLogger.printLn( "Exporting Messages to : " + globalConfig.getFilePath() );

		globalConfig.printLog();

		System.out.println("");
		System.out.println("Database connected successfully.");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		EnLogger.print("");
		EnLogger.print("");
		EnLogger.printLn("Started: " + sdf.format(new Date()));

		// generate the output file
		XmlHandler xmlHandler = new XmlHandler();
		xmlHandler.ProcessMessages(globalConfig, connection, ObjectCreator.getQueryProvider(connection, globalConfig.getDBType()));
		EnLogger.print("");
		EnLogger.print("");
		EnLogger.printLn("Stop: " + sdf.format(new Date()));
		EnLogger.printLn(String.format("\r\nProcessed: %d messages, %d instances, %d interventions, %d appendices", ApplicationMonitor.processedMessages, ApplicationMonitor.processedInstances, ApplicationMonitor.processedInterventions,
				ApplicationMonitor.processedAppendices));

		EnLogger.closeFile();
	}

	// ================================================================================================
	private static Connection getConnection(EnXmlDumpConfig globalConfig) {
		if (globalConfig.getDBType() == DBType.MSQL) {
			String url = String.format("jdbc:sqlserver://%s:%d;DatabaseName=%s;user=%s;Password=%s", globalConfig.getServer(), globalConfig.getPort(), globalConfig.getDatabaseName(), globalConfig.getUser(), globalConfig.getPassword());
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				return DriverManager.getConnection(url);
			} catch (ClassNotFoundException e) {
				EnLogger.logE("Sql Server Driver not found, if reinstalling the tool did not fix the issue, please contat EastNets support.");
			} catch (SQLException e) {
				EnLogger.logE("Database Error: " + e.getMessage());
			}
		} else {
			try {
				String url = String.format("jdbc:oracle:thin:@%s:%d/%s", globalConfig.getServer(), globalConfig.getPort(), globalConfig.getDatabaseName());
				Class.forName("oracle.jdbc.OracleDriver");
				return DriverManager.getConnection(url, globalConfig.getUser(), globalConfig.getPassword());
			} catch (ClassNotFoundException e) {
				EnLogger.logE("Oracle Driver not found, if reinstalling the tool did not fix the issue, please contat EastNets support.");
			} catch (SQLException e) {
				EnLogger.logE("Database Error: " + e.getMessage());
			}
		}

		return null;
	}

}
