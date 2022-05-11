package com.eastnets.service.reporting;

import java.util.ArrayList;
import java.util.List;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSetParameter;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.ReportSetParameterTypeFactory;
import com.eastnets.service.reporting.ReportParamtersFactory;

public class ReportSetParameterFactory extends ReportParamtersFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540977861199145138L;

	private ReportSetParameter getParameter(String[] records) {
		long type= Long.parseLong(records[1]);
		
		ReportSetParameter reportSetParameter = ReportSetParameterTypeFactory.getReportSetParameter(type);
				
		
		reportSetParameter.setName(records[0]);
		reportSetParameter.setType(type);
		reportSetParameter.setFirstName(records[2]);
		reportSetParameter.setSecondName(records[3]);
		reportSetParameter.setDescription(records[5]);
		
		reportSetParameter.setMinLengthValue(Integer.parseInt( (records[7] == null || records[7].equals("") )? "0":records[7]) );
		reportSetParameter.setMaxLengthValue(Integer.parseInt( (records[8] == null || records[8].equals("") )? "0":records[8]) );				
		reportSetParameter.setMandatory("false".equals(records[6].toLowerCase()));
			
		
		return reportSetParameter;
	}
	
	public boolean isValide(List<Parameter> parametersList){
		
		if(super.isValide(parametersList)){
			
			if(!parametersList.isEmpty()){
				return true;
			}
		}

		return false;
	}
	
	@Override
	public List<Parameter> convertReportSetToParamters(List<Parameter> parametersList) {
		
		List<Parameter> datFileParametersList = new ArrayList<Parameter>();		
		ReportSetParameter reportSetParameter =  null;
		Long current = 0L;
		if(isValide(parametersList)){			
			
			for (Parameter parameter: parametersList) {
				String value = (String)parameter.getValue();
				value += ".";
				String[] records = value.split(";");
				if (records[0].startsWith(Constants.STORED_PROCEDURE_CALL)){
					String Suffix = records[0].substring(Constants.STORED_PROCEDURE_CALL.length());
					if(Suffix!= null && Suffix.length()>0){
		
						reportSetParameter = ReportSetParameterTypeFactory.getDefaultReportSetParameter();
						reportSetParameter.setName(Constants.STORED_PROCEDURE_CALL);
						int last = Suffix.indexOf('(');
						String SPName = Suffix.substring(1,last);
						reportSetParameter.setValue(SPName);
						
						
					}
				}else{
					reportSetParameter =  getParameter(records);
					reportSetParameter.setId(current);
				}
				current++;
				datFileParametersList.add(reportSetParameter);
				
			}
		}
		return datFileParametersList;
	}
	
	@Override
	public List<Parameter> convertReportSetToParamters(Report report) {
		List<Parameter> reportParametersList = report.getReportParametersList();
		return this.convertReportSetToParamters(reportParametersList);
	}

}
