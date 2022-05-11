package com.eastnets.resilience.filepayloadcommunication.client;
import com.eastnets.encdec.ConnectionSettings;
import com.eastnets.encdec.EnEcfParser;


public class ClientCommandLineArgumentsParser {

	private Integer aid = null;
	/*private String dbServer = null;
	private Integer dbPort = null;
	private String dbName = null;
	private String dbUser = null;
	private String dbPassword = null;*/
	private ConnectionSettings connectionSettings= new ConnectionSettings();
	private DatabaseType dbType = DatabaseType.ORACLE;
	private Integer timeout = 30;
	private Double requestedResetTimeHours = 12.0;
	private String ecfFile = null;
	private String instanceName= "";

	public void displayArguments() {
		System.out.println("Passed arguments:");
		System.out.println("Database server Type          : " + (dbType == DatabaseType.ORACLE ? "Oracle" : "Sql Server"));
		System.out.println("Alliance ID                   : " + aid);
		if ( ecfFile == null ){
			System.out.println("Database server name          : " + connectionSettings.getServerName());
			System.out.println("Database server service name  : " + connectionSettings.getServiceName());
			if ( dbType != DatabaseType.ORACLE  ){
				System.out.println("Database server instance name : " + instanceName);
			}
			
			System.out.println("Database server port          : " + connectionSettings.getPortNumber());
			System.out.println("Database server user name     : " + connectionSettings.getUserName());
			System.out.println("Database server user password : "
					+ String.format("%0" + (connectionSettings.getPassword().length()) + "d", 0).replace("0", "*"));
		} else {
			System.out.println("Encrypted connection file     : " + ecfFile);
		}
		
		// System.out.println("Server timeout                : " + timeout );
		//System.out.println();
	}

	public void displayUsage() {
		System.out.println("Usage:");
		System.out.println("-aid <aid>                  : Alliance ID to connect for");
		System.out.println(" { (-ip <server name>       : Database server name or IP");
		System.out.println("    -u <user name>          : Database server user name");
		System.out.println("    -p <password>           : Database server user password");
		System.out.println("    -port <port number>     : Database server port");
		System.out.println("    -dbname <database name> : Database server name");
		System.out.println("    -dbservicename <database service name>  : Database service name");
		System.out.println("   ) OR ( ");
		System.out.println("    -ecf <ECF file> ) }     : Encrypted database Connection File");
		System.out.println("[-dbtype] <(ORACLE|MSSQL)>  : Database server type, default is ORACLE");
		System.out.println("[-instance] <instance name> : Database server instance, only for MSSQL database");
		// System.out.println("[-Timeout  <timeout>]           : Server timeout, default 30 ");
		//System.out.println();
	}

	public Integer getAid() {
		return aid;
	}

	public String getDbName() {
		return connectionSettings.getServiceName();
	}

	public String getDbPassword() {
		return connectionSettings.getPassword();
	}

	public Integer getDbPort() {
		if ( connectionSettings.getPortNumber() == null  ){
			if (dbType == DatabaseType.ORACLE){
				connectionSettings.setPortNumber( 1521 );
			}
			else{
				connectionSettings.setPortNumber( 1433 );
			}
		}
		return connectionSettings.getPortNumber();
	}

	public String getDbServer() {
		return connectionSettings.getServerName();
	}
	// to handle db service name (oracle db type
	public String getDbServiceName (){
		return connectionSettings.getDbServiceName();
	}

	public DatabaseType getDbType() {
		return dbType;
	}

	public String getDbUser() {
		return connectionSettings.getUserName();
	}

	public Double getRequestedResetTimeHours() {
		return requestedResetTimeHours;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public boolean parseAndValidate(String[] args) {
		String param = "";
		try {
			for (int index = 0; index < args.length; index++) {
				param = args[index];

				if ("-aid".equalsIgnoreCase(param) || "-id".equalsIgnoreCase(param)) {
					// aid
					aid= Integer.parseInt(args[++index]);
					
				} else if ("-ip".equalsIgnoreCase(param)) {
					// database server
					connectionSettings.setServerName(args[++index]);
					
				} else if ("-u".equalsIgnoreCase(param)) {
					// database server user
					connectionSettings.setUserName(args[++index]);
					
				} else if ("-p".equalsIgnoreCase(param)) {
					// database server user password
					connectionSettings.setPassword(args[++index]);
					
				} else if ("-port".equalsIgnoreCase(param)) {
					// database port
					connectionSettings.setPortNumber(Integer.parseInt(args[++index]));
					
				} else if ("-dbname".equalsIgnoreCase(param)) {
					// database instance name
					connectionSettings.setServiceName(args[++index]);
				} 
				
				// to handle db service name (oracle db type
				else if ("-dbservicename".equalsIgnoreCase(param)) {
					// database instance name
					connectionSettings.setDbServiceName(args[++index]);
										
				} else if ("-dbtype".equalsIgnoreCase(param)) {
					// database type
					dbType= DatabaseType.ORACLE;
					
					if (args[++index].equalsIgnoreCase("MSSQL")) {
						dbType= DatabaseType.MSSQL;
					}
					
				} else if ("-ecf".equalsIgnoreCase(param)) {
					// connection timeout
					ecfFile = args[++index];
					ConnectionSettings cs= EnEcfParser.parseECF(ecfFile);
						
					if ( cs.getServerName() != null  ){
						connectionSettings.setServerName(cs.getServerName()) ;
					}
					if ( cs.getPassword() != null  ){
						connectionSettings.setPassword(cs.getPassword() ) ;
					}
					if ( cs.getPortNumber() != null  ){
						connectionSettings.setPortNumber(cs.getPortNumber() ) ;
					}
					if ( cs.getServiceName() != null  ){
						connectionSettings.setServiceName(cs.getServiceName() ) ;
					}
					if ( cs.getUserName() != null  ){
						connectionSettings.setUserName(cs.getUserName() ) ;
					}
					
				}  else if ("-timeout".equalsIgnoreCase(param)) {
					// connection timeout
					timeout= Integer.parseInt(args[++index]);
					
				} else if ("-reset".equalsIgnoreCase(param)) {
					// requested flag reset time in hours
					requestedResetTimeHours= Double.parseDouble(args[++index]);
					
				} else if ("-instance".equalsIgnoreCase(param)) {
					// instance name
					instanceName= args[++index];
					
				} else {
					System.out.println("Unhandled commandline argument :" + param);
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception reading parameter \"" + param + "\": " + ex.getMessage());
			return false;
		}
		if (aid == null && connectionSettings.getServerName() == null && connectionSettings.getUserName() == null && connectionSettings.getPassword() == null && connectionSettings.getServiceName() == null ) {
			
			return false;// just display the usage
		}

		if (aid == null) {
			System.out.println("Error: The Parameter -aid is missing.");
			return false;
		}
		if (connectionSettings.getServerName() == null) {
			System.out.println("Error: The Parameter -ip is missing.");
			return false;
		}
		if (connectionSettings.getUserName() == null) {
			System.out.println("Error: The Parameter -u is missing.");
			return false;
		}
		if (connectionSettings.getPassword() == null) {
			System.out.println("Error: The Parameter -p is missing.");
			return false;
		}
		// to handle db service name (oracle db type
		if (connectionSettings.getServiceName() == null && connectionSettings.getDbServiceName() == null ) {
			System.out.println("Error: The Parameters -dbname and -dbservicename are missing.");
			return false;
		}

		return true;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
}

enum DatabaseType {
	ORACLE, MSSQL;
}