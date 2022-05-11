package com.eastnets.service.loader.helper;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.enReportingLoader.ServiceLauncher;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.util.LoaderUtils;


public class DatabaseMessageReader implements MessageReader {

	private static String whereStmt = "";
	private static  String fromTable = " ";
	private StringBuffer colums;
	private OracleDataSource datasource;
	private XMLDataSouceHelper xmlDataSouceHelper;
	private JtdsDataSource jtdsDataSource;
	private boolean oracleDBType;
	private Connection lockCon;
	private BigDecimal lastProcessMesg;
	private AppConfigBean appConfigBean;
	private String normalSql = null;
	private String restoreSQL= null;
	private Map<String,XMLQueryHelper> xmlQueryHelperMap;
	private LoaderDAO loaderDAO;
	private static final Logger LOGGER = Logger.getLogger(DatabaseMessageReader.class);
	private XMLDataSouceHelper xmlDataSource;
	private enum QueryTypes {
		NORMAL_PROCESS_QUERY("normal-fetch-process"),RESTORE_QUERY("restore-query");
		private String queryName;

		QueryTypes(String queryName) {
			this.queryName = queryName;
		}

		public String getQueryName() {
			return this.queryName;
		}
	}

	@Override
	public void init()   { 
		LOGGER.info("Start init DatabaseMessageReader");
		lastProcessMesg = getLastLoaderConnection().getLastProcessedSequenceNo();
		try {  
			initDataSource();
		} catch (Exception e) {  
			String errMessage="Failed";
			if(e.getMessage() != null){
				errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
			} 
			if(Main.isDbReader()){
				
				loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

			}else{
				loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

			}
			e.printStackTrace();
		}
	}

	private LoaderConnection getLastLoaderConnection() {
		return loaderDAO.getLastLoaderConnection();
	}





	@Override
	public List<LoaderMessage> readMessages(Connection lockCone,String aid) throws Exception {  
		XMLQueryHelper normalQuery = this.xmlQueryHelperMap.get(QueryTypes.NORMAL_PROCESS_QUERY.getQueryName());
		LOGGER.debug("Read Messages from :: " + fromTable + " :: Bulk Size :: " + this.xmlDataSource.getBulkSize());
		List<LoaderMessage> mesgList = new ArrayList<LoaderMessage>();

		Connection conn =  oracleDBType ? datasource.getConnection() : jtdsDataSource.getConnection(); 
		PreparedStatement stat = conn.prepareStatement(normalSql);

		if(oracleDBType){ 
			stat.setBigDecimal(1, lastProcessMesg);// start record
			stat.setInt(2, this.xmlDataSource.getBulkSize());
		}else{
			stat.setBigDecimal(2, lastProcessMesg);// start record
			stat.setInt(1, this.xmlDataSource.getBulkSize());
		} 
		
		//System.out.println(normalSql);
		//System.out.println(lastProcessMesg);
		//System.out.println(this.xmlDataSource.getBulkSize());
 
		ResultSet res = stat.executeQuery();

		while (res.next()) {
			LoaderMessage loadermesg = new LoaderMessage();

			Clob rowdata = res.getClob(normalQuery.getTxtCol());
			Clob rowHistory = null;
			if (normalQuery.getHistoryCol() != null) {
				rowHistory = res.getClob(normalQuery.getHistoryCol());
				
			} 
			//id msgtype id datataime unite Text
			if (rowdata != null) {
				loadermesg.setRowData(LoaderUtils.convertClob2String(rowdata));
			}
			
			
			if(rowHistory != null){
				loadermesg.setRowHistory(LoaderUtils.convertClob2String(rowHistory));
			}
	 
			loadermesg.setMesgType(res.getString(normalQuery.getMesgType()));
			loadermesg.setMessageSequenceNo(res.getBigDecimal(normalQuery.getSeqNumb()));
			/*
			 * The following columns optional; so if the column name not null
			 * that means it must be passed to the above query
			 */
			if (normalQuery.getUnitName() != null) {
				loadermesg.getMesgProperties().put("unitname", res.getString(normalQuery.getUnitName()));
			}

			if (normalQuery.getCreationDate() != null) {
				loadermesg.getMesgProperties().put("creationdate", res.getTimestamp(normalQuery.getCreationDate()));
			}

			if (normalQuery.getCreationOperator() != null) {
				loadermesg.getMesgProperties().put("creationoperator", res.getString(normalQuery.getCreationOperator()));
			}

			mesgList.add(loadermesg);
			lastProcessMesg = loadermesg.getMessageSequenceNo();
			LOGGER.debug("Current Message ID :: " + loadermesg.getMessageSequenceNo()+" ,Message Type:: "+loadermesg.getMesgType());
		}
		if(mesgList.size() != 0){
			mesgList.get(mesgList.size()-1).setLockCon(lockCone); 
		}
		res.close();
		conn.close();
		return mesgList;
	}




	@Override
	public List<LoaderMessage> restoreMessages(List<BigDecimal> messagesIds) throws Exception {

		XMLQueryHelper restoreQuery = this.xmlQueryHelperMap.get(QueryTypes.RESTORE_QUERY.getQueryName());

		if (messagesIds  != null && messagesIds.size()>0) {
			// preparing Ids
			String messagesIdsString = messagesIds.toString();
			// to String in list for BigDecimal or warpper classes will return [of numbers] ; so we need to extract them
			messagesIdsString = messagesIdsString.substring(1, messagesIdsString.indexOf("]"));

			restoreSQL=String.format(restoreSQL, messagesIdsString) ;
		}		

		LOGGER.debug("Restore Messages from :: " + restoreQuery.getTableName() + " :: Currently Messages Counts in Restore table :: " + messagesIds.size());

		List<LoaderMessage> mesgList = new ArrayList<LoaderMessage>();

		Connection conn =  oracleDBType ? datasource.getConnection() : jtdsDataSource.getConnection();

		// No need for prepared statement this query will be executed one time only
		Statement stat = conn.createStatement();

		ResultSet res = stat.executeQuery(restoreSQL);

		while (res.next()) {
			LoaderMessage loadermesg = new LoaderMessage();
			Clob rowdata = res.getClob(restoreQuery.getTxtCol());
			Clob rowHistory= null; 
			if (restoreQuery.getHistoryCol() != null) {
				 rowHistory = res.getClob(restoreQuery.getHistoryCol()); 
			} 
			
			
			if (rowdata != null) {
				loadermesg.setRowData(LoaderUtils.convertClob2String(rowdata));
			}

			if (rowHistory != null) {
				loadermesg.setRowHistory(LoaderUtils.convertClob2String(rowHistory));
			}
			loadermesg.setMesgType(res.getString(restoreQuery.getMesgType()));
			loadermesg.setMessageSequenceNo(res.getBigDecimal(restoreQuery.getSeqNumb()));
			mesgList.add(loadermesg);

			/*
			 * The following columns optional; so if the column name not null
			 * that means it must be passed to the above query
			 */
			if (restoreQuery.getUnitName() != null) {
				loadermesg.getMesgProperties().put("unitname", res.getString(restoreQuery.getUnitName()));
			}

			if (restoreQuery.getCreationDate() != null) {
				loadermesg.getMesgProperties().put("creationdate", res.getTimestamp(restoreQuery.getCreationDate()));
			}

			if (restoreQuery.getCreationOperator() != null) {
				loadermesg.getMesgProperties().put("creationoperator", res.getString(restoreQuery.getCreationOperator()));
			}

			LOGGER.debug("Current Message ID :: " + loadermesg.getMessageSequenceNo());
		}

		res.close();
		conn.close();

		return mesgList;
	}

	@Override
	public void finish() {

	}


	public void initDataSource() throws Exception {
		DataSourceParser dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
		this.xmlDataSource = dataSourceParser.getXmlDataSource();
		this.xmlQueryHelperMap = dataSourceParser.getXmlQueryHelperMap();
		// get normal fetch process query 
		if(!xmlDataSource.isDbIntegrationEnabled()){
			return;
		}

		if(xmlDataSource.getDbType().equalsIgnoreCase("oracle")){
			oracleDBType = true;
			datasource = new OracleDataSource();
			datasource.setDriverType("thin");
			datasource.setServerName(xmlDataSource.getIp());
			if(xmlDataSource.getDbName() != null && !xmlDataSource.getDbName().isEmpty()) {
				datasource.setDatabaseName(xmlDataSource.getDbName());
			} else {
				// then the service name selected
				datasource.setServiceName(xmlDataSource.getServiceName());
			}

			datasource.setPortNumber(Integer.parseInt(xmlDataSource.getPort()));
			datasource.setUser(xmlDataSource.getUsername());
			datasource.setPassword(xmlDataSource.getPassword());
			System.out.println("DB User "+datasource.getUser());

		}


		if(xmlDataSource.getDbType().equalsIgnoreCase("mssql")){
			oracleDBType = false;

			jtdsDataSource = new JtdsDataSource();

			jtdsDataSource.setServerName(xmlDataSource.getIp());
			jtdsDataSource.setDatabaseName(xmlDataSource.getDbName());
			jtdsDataSource.setPortNumber(Integer.parseInt(xmlDataSource.getPort()));
			jtdsDataSource.setUser(xmlDataSource.getUsername());
			jtdsDataSource.setPassword(xmlDataSource.getPassword());

		}
		lockCon =  oracleDBType ? datasource.getConnection() : jtdsDataSource.getConnection();
		normalSql=initQuery(QueryTypes.NORMAL_PROCESS_QUERY);
		System.out.println(normalSql);
		restoreSQL=initQuery(QueryTypes.RESTORE_QUERY);



	}

	private String initQuery( QueryTypes queryType) {

		String sql="";
		XMLQueryHelper xmlQueryHelper = this.xmlQueryHelperMap.get(queryType.getQueryName());
		colums = new StringBuffer(xmlQueryHelper.getTxtCol() + ", " + xmlQueryHelper.getHistoryCol() + ", "+ xmlQueryHelper.getSeqNumb() + ", " + xmlQueryHelper.getMesgType());

		if (xmlQueryHelper.getUnitName() != null) {
			colums.append(", " + xmlQueryHelper.getUnitName());
		}

		if (xmlQueryHelper.getCreationDate() != null) {
			colums.append(", " + xmlQueryHelper.getCreationDate());
		}

		if (xmlQueryHelper.getCreationOperator() != null) {
			colums.append(", " + xmlQueryHelper.getCreationOperator());
		}

		fromTable = xmlQueryHelper.getTableName();
		LOGGER.debug("Preparing SOURCE QUERY :: " + fromTable);
		if (xmlQueryHelper.getWhere() != null) {
			whereStmt = xmlQueryHelper.getWhere();
		}
		if(queryType.equals(QueryTypes.NORMAL_PROCESS_QUERY)){

			if(oracleDBType){
				sql = "select * from ( select " + colums.toString() + " from " + fromTable + " where 1=1 " + whereStmt +" ) where rownum <= ?";
			}else{
				sql = "select * from ( select top (?) " + colums.toString() + " from " + fromTable + " where 1=1 " + whereStmt +" )sql ";
			}


		}else{
			sql = "select " + colums.toString() + " from " + fromTable + " where 1=1 " + whereStmt;
		}


		return sql;
	}

	public BigDecimal getLastProcessMesg() {
		return lastProcessMesg;
	}

	public void setLastProcessMesg(BigDecimal lastProcessMesg) {
		this.lastProcessMesg = lastProcessMesg;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public Connection getLockCon() {
		return lockCon;
	}

	public void setLockCon(Connection lockCon) {
		this.lockCon = lockCon;
	}

}
