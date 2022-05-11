package com.eastnets.dbconsistencycheck.app;

public class ReportInfoBean {

	// to store number of messages that status ok
	private	int countOkMsg =0;
	// to store number of messages that checked
	private int countMsg = 0;
	// to store number of messages that status missing
	private int countMissingMsg = 0;
	// to store  DB check start period 
	private String startCheck ="";
	// to store  DB check end period 
	private String endCheck = "";
	// to store  DB reort status 
	private String reportStatus = "";
	// to store number of messages that status out of date
	private int countOutOfDateMsg = 0;
	// to store date of report
	private String reportDate = "";


	// Getter & Setter

	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public int getCountOutOfDateMsg() {
		return countOutOfDateMsg;
	}
	public void setCountOutOfDateMsg(int countOutOfDateMsg) {
		this.countOutOfDateMsg = countOutOfDateMsg;
	}
	public int getCountOkMsg() {
		return countOkMsg;
	}
	public void setCountOkMsg(int countOkMsg) {
		this.countOkMsg = countOkMsg;
	}
	public int getCountMsg() {
		return countMsg;
	}
	public void setCountMsg(int countMsg) {
		this.countMsg = countMsg;
	}
	public int getCountMissingMsg() {
		return countMissingMsg;
	}
	public void setCountMissingMsg(int countMissingMsg) {
		this.countMissingMsg = countMissingMsg;
	}
	public String getStartCheck() {
		return startCheck;
	}
	public void setStartCheck(String startCheck) {
		this.startCheck = startCheck;
	}
	public String getEndCheck() {
		return endCheck;
	}
	public void setEndCheck(String endCheck) {
		this.endCheck = endCheck;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}


}
