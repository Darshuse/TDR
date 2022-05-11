/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Config POJO
 * @author EastNets
 * @since July 11, 2012
 */
public class Config implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4753970382648932092L;
	private String databaseName;
	private String schemaName;
	private String tableSpace;
	private String serverName;
	private String databaseProtNumber;
	private Boolean viewerProcedureDebug = true;
	private String viewerProcedureDebugStr ;
	
	private Integer reportMaxFetchSize;
	private String reportMaxFetchSizeStr;
	private String reportsDirectoryPath;
	private String instanceName ;
	private String DBServiceName ;
	
	private String defaultUserRoles ;
	private int searchOptimizer;
	private String reportLogoPath;	
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDBServiceName() {
		return DBServiceName;
	}
	public void setDBServiceName(String DBServiceName) {
		this.DBServiceName = DBServiceName;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
	public void setViewerProcedureDebug(Boolean viewerProcedureDebug) {
		this.viewerProcedureDebug = viewerProcedureDebug;
	}
	public Boolean getViewerProcedureDebug() {
		return viewerProcedureDebug;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public Integer getReportMaxFetchSize() {
		return reportMaxFetchSize;
	}

	public void setReportMaxFetchSize(Integer reportMaxFetchSize) {
		this.reportMaxFetchSize = reportMaxFetchSize;
	}

	public String getReportsDirectoryPath() {
		return reportsDirectoryPath;
	}

	public void setReportsDirectoryPath(String reportsDirectoryPath) {
		this.reportsDirectoryPath = reportsDirectoryPath;
	}
	public String getViewerProcedureDebugStr() {
		return viewerProcedureDebugStr;
	}
	public void setViewerProcedureDebugStr(String viewerProcedureDebugStr) {
		this.viewerProcedureDebugStr = viewerProcedureDebugStr;
	}
	public String getReportMaxFetchSizeStr() {
		return reportMaxFetchSizeStr;
	}
	public void setReportMaxFetchSizeStr(String reportMaxFetchSizeStr) {
		this.reportMaxFetchSizeStr = reportMaxFetchSizeStr;
	}
	public String getDatabaseProtNumber() {
		return databaseProtNumber;
	}
	public void setDatabaseProtNumber(String databaseProtNumber) {
		this.databaseProtNumber = databaseProtNumber;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getDefaultUserRoles() {
		return defaultUserRoles;
	}

	public List<String> getDefaultUserRolesList() {
		String[] rolesArr = defaultUserRoles.split(";");;
		List<String> roles = new ArrayList<String>();
		for (String role : rolesArr ){
			if ( !role.trim().isEmpty() ){
				roles.add(role.trim().toUpperCase());
			}
		}
		return roles;
	}
	
	public void setDefaultUserRoles(String defaultUserRoles) {
		this.defaultUserRoles = defaultUserRoles;
	}
	public int getSearchOptimizer() {
		return searchOptimizer;
	}
	public void setSearchOptimizer(int searchOptimizer) {
		this.searchOptimizer = searchOptimizer;
	}
	public String getReportLogoPath() {
		return reportLogoPath;
	}
	public void setReportLogoPath(String reportLogoPath) {
		this.reportLogoPath = reportLogoPath;
	}
	
}
