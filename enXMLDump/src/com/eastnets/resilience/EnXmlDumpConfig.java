package com.eastnets.resilience;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class EnXmlDumpConfig {
	public enum DBType{ ORACLE, MSQL };
	
	//logging 
	private boolean verbose= false;
		
	//database connection 
	private DBType dbType= DBType.ORACLE;
	private String server;
	private Integer port;
	private String databaseName;
	private String user;
	private String password;
	
	//Application Settings 
	private Integer aid;
	private Date fromDate;
	private Date toDate;
	private String filePath;
	
	private boolean logInfo= false;
	private boolean formattedXml= false;
	

	private boolean swiftNet= false;
	private boolean acked= false;
	
	public void printLog(){

		EnLogger.printLn("Passed arguments:");
		if ( dbType == DBType.MSQL ){
			EnLogger.printLn("Database: Microsoft SQL Server.");
		}else{
			EnLogger.printLn("Database: Oracle.");
		}
		EnLogger.printLn("Server: " + server );
		EnLogger.printLn("Database: " + databaseName );
		EnLogger.printLn("Port: " + port );
		EnLogger.printLn("User: " + user );
		EnLogger.printLn("Password: *****" );
		
		EnLogger.printLn("Alliance ID: " + aid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if ( fromDate != null ){
			EnLogger.printLn("Messages From: " + sdf.format(fromDate));
		}
		if ( toDate != null ){
			EnLogger.printLn("Messages To: " + sdf.format(toDate));
		}

		EnLogger.printLn("Output file: " + filePath );
		if ( logInfo ){
			System.out.println("Log file : " + filePath +".log");
		}
	}
	
	EnXmlDumpConfig(){
		setVerbose( false );
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DBType getDBType() {
		return dbType;
	}
	public void setDBType(DBType dbType) {
		this.dbType = dbType;
	}
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public boolean getVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
		if ( verbose ){
			EnLogger.setLevel(Level.ALL);
		} else {
			EnLogger.setLevel(Level.SEVERE);
		}
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isFormattedXml() {
		return formattedXml;
	}

	public void setFormattedXml(boolean formattedXml) {
		this.formattedXml = formattedXml;
	}

	public boolean isLogInfo() {
		return logInfo;
	}

	public void setLogInfo(boolean logInfo) {
		this.logInfo = logInfo;
	}

	public boolean isSwiftNet() {
		return swiftNet;
	}

	public void setSwiftNet(boolean swiftNet) {
		this.swiftNet = swiftNet;
	}

	public boolean isAcked() {
		return acked;
	}

	public void setAcked(boolean acked) {
		this.acked = acked;
	}

}
