package com.eastnets.reportingserver.reportBeans;

import com.eastnets.config.ConfigBean;
import com.eastnets.service.common.ReportService.ReportTypes;

public class AppConfigBean extends ConfigBean {

	/**
	* 
	*/
	private static final long serialVersionUID = 6956352400921395868L;
	// Reporting Module
	private boolean showAllCriteriasName;
	private String reportId;
	private String criteriaName;
	private String printerName;
	private String outpuReportPath;
	private String outpuReportName;
	private ReportTypes reportTypes;
	private DestenationTypes destenationTypes;
	private int copiesCount = 1;
	private boolean debug;
	private boolean scheduleMode;

	public boolean isShowAllCriteriasName() {
		return showAllCriteriasName;
	}

	public void setShowAllCriteriasName(boolean showAllCriteriasName) {
		this.showAllCriteriasName = showAllCriteriasName;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getOutpuReportPath() {
		return outpuReportPath;
	}

	public void setOutpuReportPath(String outpuReportPath) {
		this.outpuReportPath = outpuReportPath;
	}

	public String getOutpuReportName() {
		return outpuReportName;
	}

	public void setOutpuReportName(String outpuReportName) {
		this.outpuReportName = outpuReportName;
	}

	public ReportTypes getReportTypes() {
		return reportTypes;
	}

	public void setReportTypes(ReportTypes reportTypes) {
		this.reportTypes = reportTypes;
	}

	public void setReportTypes(String reportTypes) {
		if ("PDF".equals(reportTypes)) {
			this.reportTypes = ReportTypes.pdf;
		} else if ("EXCEL".equals(reportTypes)) {
			this.reportTypes = ReportTypes.xls;
		} else if ("WORD".equals(reportTypes)) {
			this.reportTypes = ReportTypes.docx;
		}
	}

	public DestenationTypes getDestenationTypes() {
		return destenationTypes;
	}

	public void setDestenationTypes(DestenationTypes destenationTypes) {
		this.destenationTypes = destenationTypes;
	}

	public void setDestenationTypes(String destenationTypes) {

		if ("PRINT".equals(destenationTypes)) {
			this.setDestenationTypes(DestenationTypes.printer);
		} else if ("FILE".equals(destenationTypes)) {
			this.setDestenationTypes(DestenationTypes.file);
		} else if ("EMAIL".equals(destenationTypes)) {
			this.setDestenationTypes(DestenationTypes.email);
		} else if ("CUSTOM".equals(destenationTypes)) {
			this.setDestenationTypes(DestenationTypes.custom);
		} else {
			this.setDestenationTypes(DestenationTypes.printer);
		}
	}

	public int getCopiesCount() {
		return copiesCount;
	}

	public void setCopiesCount(int copiesCount) {

		if (copiesCount < 1)
			this.copiesCount = 1;
		else
			this.copiesCount = copiesCount;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setScheduleMode(boolean scheduleMode) {
		this.scheduleMode = scheduleMode;
	}

	public boolean isScheduleMode() {
		return scheduleMode;
	}

}
