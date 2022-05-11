package com.eastnets.domain.reporting;

import javax.validation.constraints.Pattern;


public class CriteriaSchedule {

	private Integer id;
	private Integer criteriaId;
	private Integer schdlType = 1;
	private String schdlHours;
	private Integer schdlDay = 1;
	private Integer schdlDate = 1;
	private Long userId;
	private boolean schDisable;
	private String fileFormat;
	//@Pattern(regexp = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b" , message="Invalid email address")
	private String mailTo;
	private String mailCc;
	private String outputPath;
	private boolean notifyAfterGeneration;
	private boolean attachGeneratedReport;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(Integer criteriaId) {
		this.criteriaId = criteriaId;
	}
	
	
	public Integer getSchdlType() {
		return schdlType;
	}
	public void setSchdlType(Integer schdlType) {
		this.schdlType = schdlType;
	}
	
	
	public String getSchdlHours() {
		return schdlHours;
	}
	public void setSchdlHours(String schdlHours) {
		this.schdlHours = schdlHours;
	}
	
	
	public Integer getSchdlDay() {
		return schdlDay;
	}
	public void setSchdlDay(Integer schdlDay) {
		this.schdlDay = schdlDay;
	}
	
	
	public Integer getSchdlDate() {
		return schdlDate;
	}
	public void setSchdlDate(Integer schdlDate) {
		this.schdlDate = schdlDate;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	public boolean isSchDisable() {
		return schDisable;
	}
	public void setSchDisable(boolean schDisable) {
		this.schDisable = schDisable;
	}
	
	
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
		
	public String getMailCc() {
		return mailCc;
	}
	
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	
	public String getOutputPath() {
		return outputPath;
	}
	
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	public boolean isNotifyAfterGeneration() {
		return notifyAfterGeneration;
	}
	
	public void setNotifyAfterGeneration(boolean notifyAfterGeneration) {
		this.notifyAfterGeneration = notifyAfterGeneration;
	}
	
	public boolean isAttachGeneratedReport() {
		return attachGeneratedReport;
	}
	
	public void setAttachGeneratedReport(boolean attachGeneratedReport) {
		this.attachGeneratedReport = attachGeneratedReport;
	}
	
	
	
}
