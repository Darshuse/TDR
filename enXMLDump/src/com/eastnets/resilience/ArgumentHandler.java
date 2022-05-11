package com.eastnets.resilience;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

import com.eastnets.resilience.EnXmlDumpConfig.DBType;

public class ArgumentHandler {
	
	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'd', '3', 'b', 'e', '4', '6', '4', '1', '2', '9', 'b', '7',
			'd', '8', 'a', '7' };	
	
	//================================================================================================
		public static EnXmlDumpConfig getGlobalConfig(String[] args) {
			if ( args.length < 3 ) {
				displayUsage();
				return null;
			}
			
			EnXmlDumpConfig configuration = new EnXmlDumpConfig();
			try{
				for (int index = 0; index < args.length; index++) {
					String param = args[index];
		
					if ("-aid".equalsIgnoreCase(param)) {
						// allianceId
						configuration.setAid(Integer.parseInt(args[++index]));
					} else if ("-dbtype".equalsIgnoreCase(param)) {
						// ORACLE/MSSQL
						configuration.setDBType( DBType.MSQL );
						if ( args[++index].toUpperCase().equalsIgnoreCase("oracle") ){
							configuration.setDBType( DBType.ORACLE );
						}				
					} else if ("-u".equalsIgnoreCase(param)) {
						// User name
						configuration.setUser(args[++index]);
					} else if ("-p".equalsIgnoreCase(param)) {
						// Password
						configuration.setPassword(args[++index]);
					} else if ("-dbname".equalsIgnoreCase(param) || "-sid".equalsIgnoreCase(param)) {
						// SID/database name
						configuration.setDatabaseName(args[++index]);
					} else if ("-port".equalsIgnoreCase(param)) {
						// database port
						configuration.setPort(Integer.parseInt(args[++index]));
					} else if ("-s".equalsIgnoreCase(param)) {
						// database server IP/name
						configuration.setServer(args[++index]);
					} else if ("-v".equalsIgnoreCase(param)) {
						// verbose
						configuration.setVerbose(true);
					}else if ("-ecf".equalsIgnoreCase(param)) {
						// ECF
						parseECF(args[++index], configuration);
					} else if ("-h".equalsIgnoreCase(param)) {
						// Help message
						displayUsage();
						System.exit(1);
					}else if ("-from".equalsIgnoreCase(param)) {
						// From date
						SimpleDateFormat ft= new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
						try{
						configuration.setFromDate( ft.parse( args[++index] + " " + args[++index]  ));
						}catch( Exception ex ){
							EnLogger.logE("Invalid from date.");
							return null;
						}
					}else if ("-to".equalsIgnoreCase(param)) {
						// To date
						SimpleDateFormat ft= new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
						try{
						configuration.setToDate( ft.parse( args[++index] + " " + args[++index] ));
						}catch( Exception ex ){
							EnLogger.logE("Invalid to date.");
							return null;
						}
					}else if ("-file".equalsIgnoreCase(param)) {
						// file path
						configuration.setFilePath( args[++index] );
					}else if ("-f".equalsIgnoreCase(param)) {
						// format the  xml file
						configuration.setFormattedXml( true );
					}else if ("-log".equalsIgnoreCase(param)) {
						//enable logging 
						configuration.setLogInfo(true);
					}
					else if ("-swiftnet".equalsIgnoreCase(param)) {
						//enable swift net only
						configuration.setSwiftNet(true);
					}
					else if ("-acked".equalsIgnoreCase(param)) {
						//enable acked only
						configuration.setAcked(true);
					}
				}
			}
			catch( Exception ex ){
				return null;
			}
			// double check configuration
			boolean validArgs= checkConfiguration(configuration);
			if ( !validArgs ){
				displayUsage();
				return null;
			}

			return configuration;
		}

		//================================================================================================
		private static boolean checkConfiguration(EnXmlDumpConfig configuration) {
			if ( configuration.getServer() == null  ){
				EnLogger.logE("Database server name/IP is mandatory.");
				return false;
			}
			if ( configuration.getDatabaseName() == null  ){
				EnLogger.logE("Database name is mandatory.");
				return false;
			}
			if ( configuration.getPort() == null  ){
				EnLogger.logE("Database port is mandatory.");
				return false;
			}
			if ( configuration.getUser() == null  ){
				EnLogger.logE("Database user is mandatory.");
				return false;
			}
			if ( configuration.getPassword() == null  ){
				EnLogger.logE("Database password is mandatory.");
				return false;
			}
			if ( configuration.getAid() == null  ){
				EnLogger.logE("Alliance ID is mandatory.");
				return false;
			}
			if( configuration.getFromDate() != null && configuration.getToDate() != null 
					&& configuration.getFromDate().compareTo(configuration.getToDate()) > 0 ){
				EnLogger.logE("To date must be earlier than from date");
				return false;
			}
			if( configuration.getFilePath() == null ){
				EnLogger.logE("Output file path must be provided.");
				return false;
			}
			if( new File( configuration.getFilePath() ).exists() ){
				System.out.println("Target file already exists, Are you sure you want to overwrite the file (Y/*)?");
				Scanner keyboard = new Scanner(System.in);
				String val = keyboard.next();
				keyboard.close();
				if ( !val.equalsIgnoreCase("y") ) {
					System.exit(0);
					return false;
				}
			}
			return true;
		}

		//================================================================================================
		private static void parseECF(String filePath, EnXmlDumpConfig configuration) throws Exception {
			try {

				File ecfFile = new File(filePath);
				BufferedReader reader = new BufferedReader(new FileReader(ecfFile));

				String encryptedValue = reader.readLine();
				reader.close();

				Key key = new SecretKeySpec(keyValue, ALGORITHM);

				Cipher c = Cipher.getInstance(ALGORITHM);
				c.init(Cipher.DECRYPT_MODE, key);
				byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
				byte[] decValue = c.doFinal(decordedValue);
				String decryptedValue = new String(decValue);

				String[] args = decryptedValue.split("\t");

				configuration.setUser(args[0]);
				configuration.setPassword(args[1]);
				configuration.setServer(args[2]);

			} catch (Exception ex) {
				EnLogger.logE("ECF File Provided is not valid.");
				throw ex;
			}

		}

		//================================================================================================
		private static void displayUsage() {
			StringBuilder display = new StringBuilder();

			display.append( "enXmlDump tool 1.0.0 beta\r\n");
			display.append("\n\n");
			
			display.append(" { ( -u <user name> -p <password> : User name and password to connect to database.\n");
			display.append("     [-s <ip/server name>]        : The ip address or server name of the database server.\n");
			display.append("     [-port <db port>] )          : Database port.\n");
			display.append("   OR -ecf <ECF file> }           : Passs the Encrypted Connection File to connect to database.\n");
			display.append(" -dbname <SID/database name>      : Database name to connect to .\n");		    
			display.append(" -file <output file>              : Output file name.\n");
			display.append(" [-dbtype <oracle|mssql>]         : Oracle or MS SQL database, default is oracle.\n");
			display.append(" [-aid <alliance id>]             : AID to export messages from.\n");
			display.append(" [-from <date>]                   : Date of the oldest message to be exported( YYYY-MM-DD HH:MI:SS ).\n");
			display.append(" [-to <date>]                     : Date of the latest message to be exported( YYYY-MM-DD HH:MI:SS ).\n");
		    display.append(" [-swiftnet]                      : Export Messages sent to SWIFT only.\n");
		    display.append(" [-f]                             : Format the exported XML file.\n");
		    display.append(" [-log]                           : Write output log file.\n");
			display.append(" [-h]                             : Display this help menu and exit.\n");

			System.out.println(display.toString());			
		}
}
