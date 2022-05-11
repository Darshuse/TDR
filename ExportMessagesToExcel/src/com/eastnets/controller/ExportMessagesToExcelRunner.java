package com.eastnets.controller;


import java.util.List;
import java.util.Map;
import com.eastnets.beans.DBType;
import com.eastnets.beans.MessageField;
import com.eastnets.beans.MessageKey;
import com.eastnets.beans.SearchCriteria;
import com.eastnets.beans.ServerInfo;


public class ExportMessagesToExcelRunner {

	private SearchCriteria searchCriteriaObj = new SearchCriteria();
	private ServerInfo serverInfoObj = new ServerInfo();
		
	
	public void displayUsage(String value) throws Exception {
		
		System.out.printf("%s: missing or invalid paramter\n", value);
		System.out.println("Export Messages To Excel");
		System.out.println("Copyright 1999-2013 EastNets" );
		System.out.println("Usage : \n");
		System.out.println(  "  -U\t\t: (SQLServer) user name for database connection");
		System.out.println(  "  -P\t\t: (SQLServer) password for database connection");
		System.out.println(  "  -S\t\t: (SQLServer) server name for database connection");
		System.out.println(  "  -dbport\t\t: (SQLServer) database port number");
		System.out.println(  "  -C\t\t: (Oracle) connection string <User>/<Password>@<Server>:<Port>");
		System.out.println(  "  -db\t\t: schema name");
		System.out.println(  " " );
		System.out.println(  "  -dFrom\t\t: from date");
		System.out.println(  "  -dTo\t\t: to date");
		System.out.println(  "  -rpath\t: the directory path for report generated");
		System.out.println(  "  -xownlt\t: xOwnLt");
		
	}//displayUsage
	
	
	public boolean loadParameters(String[] args) throws Exception {
		
		try {
			for (int i=0; i<args.length;i++){
				
				String value = args[i];
				
				if("-U".equals(value)){
					
					if(i++ < args.length){
						serverInfoObj.setUsername(args[i]);
						serverInfoObj.setDatabaseType(DBType.MSSQL.toString()) ;
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-P".equals(value)){
					
					if(i++ < args.length){
						serverInfoObj.setPassword(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-S".equals(value)){
					
					if(i++ < args.length){
						serverInfoObj.setServerName(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-dbport".equals(value)){
					
					if(i++ < args.length){
						serverInfoObj.setPortNumber(args[i]) ;
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-C".equals(value)){
					
					if(i++ < args.length){
						
						serverInfoObj.setUsername(args[i].substring(0,args[i].indexOf("/")));
						serverInfoObj.setPassword(args[i].substring(args[i].indexOf("/") + 1 , args[i].indexOf("@")));
						
						if(args[i].indexOf(":") != -1) {
							serverInfoObj.setServerName(args[i].substring(args[i].indexOf("@") + 1 , args[i].indexOf(":")));
							serverInfoObj.setPortNumber(args[i].substring(args[i].indexOf(":") + 1));
						} else {
							serverInfoObj.setServerName(args[i].substring(args[i].indexOf("@") + 1));
							serverInfoObj.setPortNumber("1521");
						}
						
						serverInfoObj.setDatabaseType(DBType.ORACLE.toString()) ;
						
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-db".equals(value)){
					
					if(i++ < args.length){
						serverInfoObj.setDatabaseName(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-rpath".equals(value)){
					
					if(i++ < args.length){
						searchCriteriaObj.setReportDirectoryPath(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-dFrom".equals(value)){
					
					if(i++ < args.length){
						searchCriteriaObj.setFromDate(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-dTo".equals(value)){
					
					if(i++ < args.length){
						searchCriteriaObj.setToDate(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else if("-xownlt".equals(value)){
					
					if(i++ < args.length){
						searchCriteriaObj.setxOwnLt(args[i]);
					}else{
						displayUsage(value);
						return false;
					}
				}else{
					displayUsage(value);
					return false;
				}
			}
		} catch(Exception ex) {
			displayUsage("");
			return false;
		}
		
		return true;

	}//loadParameters
	
	
	public static void main(String[] args) {
		
		try {
			
			ExportMessagesToExcelRunner runnerObj = new ExportMessagesToExcelRunner();
			runnerObj.loadParameters(args);
		
			MessagesCollector msgCollector = new MessagesCollector();
			Map<MessageKey, List<MessageField>> messages = msgCollector.getMessages(runnerObj.searchCriteriaObj);
			
			System.out.println(messages.size());
			
			MessagesExporter messageExporter = new MessagesExporter();
			messageExporter.exportMessages(messages, runnerObj.searchCriteriaObj);
												
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
