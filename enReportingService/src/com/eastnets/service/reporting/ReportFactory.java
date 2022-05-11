package com.eastnets.service.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.domain.reporting.ReportSetParameter;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSetParameterTypeFactory;
import com.eastnets.service.reporting.ReportParamtersFactory;

public class ReportFactory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 272400589146276675L;

	private static List<Parameter> getParameters(String reportFileFullName) {

		List<Parameter> parametersList = new ArrayList<Parameter>();
		
		File file = new File(reportFileFullName);
		
		if(file.exists()){
			String name = file.getName();
			String absolutePath = file.getAbsolutePath();
			absolutePath = absolutePath.replaceAll(name, "");
			name = name.replaceAll(".jrxml", ".dat");
			
			String datFile = String.format("%s%s",absolutePath,name);
			file = new File(datFile);
			if(file.exists() == false){
				return parametersList;
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(datFile));
				String line = null ;
				while( (line = reader.readLine()) !=null){
					ReportSetParameter reportSetParameter = ReportSetParameterTypeFactory.getDefaultReportSetParameter();//default 
					reportSetParameter.setValue(line);
					parametersList.add(reportSetParameter);
				}
				reader.close();
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						
					}
				}
			}
		}
		
		return parametersList;
	}
	
	public static Report create(String reportFileFullName){
		
		ReportParamtersFactory factory = new ReportSetParameterFactory();
		Report report = new Report();
		List<Parameter> parametersList = getParameters(reportFileFullName);

		if(parametersList == null || parametersList.isEmpty()){
			return null;
		}
		Parameter parameter = parametersList.get(0);
		String value = (String)parameter.getValue();
		String[]records = value.split(";");
		parametersList.remove(0);		
		
		Long reportID = Long.parseLong(records[0]);
		report.setId(reportID);
		value = records[1];
		report.setName(value);
		
		File file = new File(reportFileFullName);
		value = file.getName();
		value = value.replace(".jrxml", "");
		report.setFileName(value);
		
		file = file.getParentFile();
		value = file.getName();
		report.setCategory(value);
		
		
		List<Parameter> reportParamtersList = factory.convertReportSetToParamters(parametersList);
		report.setReportParametersList(reportParamtersList);
		
		return report;
	}
}
