package com.eastnets.service.reporting;

import java.io.Serializable;
import java.util.List;

import com.eastnets.domain.Config;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;

public abstract class ReportParamtersFactory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4336711053345336532L;
	private Config config;
	
	/**
	 * Abstract method that create parameters depends on the derived class 
	 * @param parametersList
	 * @return List<Parameter>
	 */
	public abstract List<Parameter> convertReportSetToParamters(List<Parameter> parametersList);
	/**
	 * Abstract method that create parameters depends on the derived class
	 * @param report
	 * @return List<Parameter>
	 */
	public abstract List<Parameter> convertReportSetToParamters(Report report);
	

	public Config getConfig() {
		return config;
	}
	
	public void setConfig(Config config) {
		this.config = config;
	}
	
	public boolean isValide(List<Parameter> parametersList){
		
		if(parametersList != null & !parametersList.isEmpty()){
			return true;
		}
		return false;
	}
}
