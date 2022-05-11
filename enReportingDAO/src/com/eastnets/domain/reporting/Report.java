package com.eastnets.domain.reporting;

import java.io.Serializable;
import java.util.List;

public class Report implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6132626213768636284L;
	
	private Long id;
	private String name;
	private String category;
	private String fileName;
	
	List<Parameter>reportParametersList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getReportParametersList() {
		return reportParametersList;
	}

	public void setReportParametersList(List<Parameter> reportParametersList) {
		this.reportParametersList = reportParametersList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
