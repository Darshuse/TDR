package com.eastnets.resilience.toolmonitor;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.encdec.ConnectionSettings;


public class ClientCommandLineArgumentsParser {

	private ConnectionSettings connectionSettings= new ConnectionSettings();
	private DatabaseType dbType = DatabaseType.ORACLE;
	private Integer timeout;
	private Integer port;
	private String instanceName= "";
	private String settingsFile= null;
	private String listenAddress= null;

	public void displayArguments() {
		/*System.out.println("Passed arguments:");
		System.out.println("Database Type      : " + (dbType == DatabaseType.ORACLE ? "Oracle" : "Sql Server"));
		System.out.println("Database Server    : " + defaultString(connectionSettings.getServerName()));
		System.out.println("Database name      : " + defaultString(connectionSettings.getServiceName()));
		if ( dbType != DatabaseType.ORACLE  ){
			System.out.println("Database instance  : " + defaultString(instanceName));
		}
		
		System.out.println("Database port      : " + defaultString(connectionSettings.getPortNumber()));
		System.out.println("Database user name : " + defaultString(connectionSettings.getUserName()));
		System.out.println("Database password  : " 
				+ String.format("%0" + (connectionSettings.getPassword().length()) + "d", 0).replace("0", "*"));*/
	}

	/*private String defaultString(String string) {
		if ( string == null ){
			return "";
		}
		return string;
	}
	private String defaultString(Integer val) {
		if ( val == null ){
			return "";
		}
		return val.toString();
	}*/

	public void displayUsage() {
		System.out.println(Globals.getDateString() + "Usage:");
		System.out.println(Globals.getDateString() + "-settingsFile <file name>   : XML file that contains settings for the enCSM and the handlers.");
	}
	
	public boolean parseAndValidate(String[] args) {
		
		String param = "";
		for (int index = 0; index < args.length; index++) {
			param = args[index];
			
			if ("-settingsFile".equalsIgnoreCase(param)) {
				// instance name
				settingsFile= args[++index];
			}	
		}
		if ( settingsFile == null || settingsFile.trim().isEmpty() )
		{
			
			String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();
			if ( path.startsWith("/") && path.indexOf(":") == 2 ){
				path= path.substring(1);
			}
			settingsFile= path + "/enCSMConfig.xml";  
			
		}
		
		if ( !parseSettingsFile() ){
			System.out.println(Globals.getDateString() + "Error: The settings file is not valid.");
			return false;
		}	
		
		return true;
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

	public DatabaseType getDbType() {
		return dbType;
	}

	public String getDbUser() {
		return connectionSettings.getUserName();
	}

	public Integer getTimeout() {
		return timeout;
	}
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getSettingsFile() {
		return settingsFile;
	}

	public void setSettingsFile(String settingsFile) {
		this.settingsFile = settingsFile;
	}
	
	private boolean parseSettingsFile( ){
		try {
			File fXmlFile = new File( settingsFile );
			
			if ( !fXmlFile.exists() || !fXmlFile.isFile() ){
				System.out.println(Globals.getDateString() + "The File: \"" + settingsFile + "\" does not exist.");
				return false;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();		
			Document doc = dBuilder.parse(fXmlFile);
			
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			
			//Fill Database settings 
			Element  database = (Element)doc.getElementsByTagName("Database").item(0);	
			if ( database == null ){
				return false;
			}
			
			String databaseType= getSubElementValue( database, "Type");
			dbType= DatabaseType.ORACLE;
			if (databaseType != null && ( databaseType.equalsIgnoreCase("mssql") || databaseType.equalsIgnoreCase("sqlserver") )){
				dbType= DatabaseType.MSSQL;
			}
			
			connectionSettings.setServerName( getSubElementValue( database, "Server") ) ;
			if ( connectionSettings.getServerName() == null || connectionSettings.getServerName().isEmpty() ){
				System.out.println(Globals.getDateString() + "the tag <Server> is required.");
				return false;
			}
			String tmp = getSubElementValue( database, "Port") ;
			if ( tmp != null && !tmp.isEmpty()){
				connectionSettings.setPortNumber( Integer.parseInt( tmp ) ) ;
			}else{
				connectionSettings.setPortNumber(  dbType == DatabaseType.MSSQL ? 1433 : 1521 );
			}
			connectionSettings.setUserName( getSubElementValue( database, "UserName") ) ;
			if ( connectionSettings.getUserName() == null || connectionSettings.getUserName().isEmpty() ){
				System.out.println(Globals.getDateString() + "the tag <UserName> is required.");
				return false;
			}
			
			String password = getSubElementValue( database, "Password");
			if ( password == null || password.isEmpty() ){
				System.out.println(Globals.getDateString() + "the tag <Password> is required.");
				return false;
			}
			try{
				password= AESEncryptDecrypt.decrypt(password);//if the password is encrypted, decrypt it 
			}catch(Exception ex){}
			connectionSettings.setPassword( password ) ;
			connectionSettings.setServiceName( getSubElementValue( database, "DatabaseName") ) ;
			instanceName=  getSubElementValue( database, "InstanceName");
			
			//get general settings
			
			tmp  =  getElementValue( doc, "Timeout" );
			
			timeout = 10;//default
			if ( tmp != null && !tmp.isEmpty()  ){
				timeout= Integer.parseInt(tmp);
			}
			
			tmp  =  getElementValue( doc, "Port" );			
			port = 6100;//default
			if ( tmp != null && !tmp.isEmpty()  ){
				port= Integer.parseInt(tmp);
			}
			
			tmp  =  getElementValue( doc, "ListenAddress" );			
			if ( tmp != null && !tmp.isEmpty()  ){
				listenAddress= tmp;
			}
			
			NodeList nList = doc.getElementsByTagName("HandleClass");
			for (int i = 0; i < nList.getLength(); i++) {
				Element node= (Element)nList.item(i);
				String fullClassID = getSubElementValue( node, "Class" );				
				
				Element config= null;
				
				NodeList configList = node.getElementsByTagName("Config");
				if ( configList.getLength() > 0 ){
					config= (Element)configList.item(0);
				}
				
				if ( !fullClassID.isEmpty() ){
					NotificationManager.getInstance().registerHandler(fullClassID, config);
				}
			}
			return nList.getLength() > 0;
			
		} catch (Exception ex) {
			System.out.println(Globals.getDateString() + "Error : parsing settings File failed ('"  + ex.getMessage() +"')");
		}
		return false;
	}

	private String getSubElementValue(Element parentElement, String elementName) {
		NodeList list = parentElement.getElementsByTagName(elementName);
		if ( list.getLength() > 0  ){
			return list.item(0).getTextContent();
		}		
		return null;
	}

	private String getElementValue(Document parent, String elementName) {
		NodeList list = parent.getElementsByTagName(elementName);
		if ( list.getLength() > 0  ){
			return list.item(0).getTextContent();
		}		
		return null;
	}

	public String getListenAddress() {
		return listenAddress;
	}

	public void setListenAddress(String listenAddress) {
		this.listenAddress = listenAddress;
	}
}



enum DatabaseType {
	ORACLE, MSSQL;
}