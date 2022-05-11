package com.eastnets.domain.reporting;

import java.io.Serializable;

public class ReportingOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5823759969101917207L;
	private String reportsDirectoryPath;

	public String getReportsDirectoryPath() {
		return reportsDirectoryPath;
	}

	public void setReportsDirectoryPath(String reportsDirectoryPath) {
		this.reportsDirectoryPath = reportsDirectoryPath;
	}
	
	
}
