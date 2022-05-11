package com.eastnets.beans;


import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SearchCriteria {

	public static final String DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
	private static final String REPORT_NAME = "MT103_" ;
	private static final String EXT = ".xls";
	private String reportDirectoryPath;
	private String fromDate;
	private String toDate;
	private String xOwnLt;
	
		
	public String getReportName() {
		
		SimpleDateFormat formatReader = new SimpleDateFormat(DATE_TIME_PATTERN);
		String dateStr= formatReader.format(Calendar.getInstance().getTime());//currentdate
		
		return REPORT_NAME + dateStr.replaceAll(" ", "_").replaceAll("/", "").replaceAll(":", "") + EXT ;
	}
	
	
	public String getReportDirectoryPath() {
		return reportDirectoryPath;
	}
	public void setReportDirectoryPath(String reportDirectoryPath) {
		this.reportDirectoryPath = reportDirectoryPath;
	}
	
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	
	public String getxOwnLt() {
		return xOwnLt;
	}
	public void setxOwnLt(String xOwnLt) {
		this.xOwnLt = xOwnLt;
	}
}
