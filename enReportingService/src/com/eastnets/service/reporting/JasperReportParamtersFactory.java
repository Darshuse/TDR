package com.eastnets.service.reporting;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastnets.utils.ApplicationUtils;
import com.eastnets.dao.common.Constants;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.reporting.JasperReportParamter;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSetParameter;

public class JasperReportParamtersFactory extends ReportParamtersFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368088684470733300L;
	String dateTimePattern; 
	String datePattern;
	String timePattern;
	

	private String formatParamName(String name){
		
		if(name == null || name.trim().equals("")){
			return null;
		}
		return String.format("P_%s", name.toUpperCase());
	}
	
	private String repareValue(String paramValue){
		if(paramValue == null || paramValue.trim().equals("")){
			return paramValue;
		}
		
		paramValue = paramValue.replaceAll("\\*", "%");
		paramValue = paramValue.replaceAll("\\?", "_");
		paramValue = paramValue.trim();
		return paramValue;
	}
	
	private void addDateTimePatternParameter(Map<String, Object> paramsMap) {
	
		if(dateTimePattern == null){
			dateTimePattern = "dd/MM/yyyy hh:mm:ss a";
		}
		if(datePattern == null){
			datePattern = "dd/MM/yyyy";
		}
		if(timePattern == null){
			timePattern = "hh:mm:ss a";
		}		
		
		paramsMap.put(Constants.JASPER_REPORT_DATE_TIME_PATTERN, dateTimePattern);
		paramsMap.put(Constants.JASPER_REPORT_DATE_PATTERN, datePattern);
		paramsMap.put(Constants.JASPER_REPORT_TIME_PATTERN, timePattern);

	}
	

	@Override
	public List<Parameter> convertReportSetToParamters(List<Parameter> parametersList){
		
		List<Parameter> returnList = new ArrayList<Parameter>();
		JasperReportParamter jasperParameter = null;
		
		for (Parameter parameter : parametersList) {
		
			ReportSetParameter reportSetParameter = (ReportSetParameter)parameter;

			String firstValue = repareValue(reportSetParameter.getFirstValue());
			String secondValue = repareValue(reportSetParameter.getSecondValue());
			
			int type = reportSetParameter.getType().intValue();
			
			String formatFirstName = this.formatParamName(reportSetParameter.getFirstName());
			String formatSecondName = this.formatParamName(reportSetParameter.getSecondName());
			BigDecimal parseDoubleNumber = null;
			AdvancedDate advancedDate1 = null;
			Date date2 = null;
			switch (type){
			
			case Constants.REPORT_PARAMTER_TYPE_CURRENCY_AMOUNT:
				if(formatFirstName !=null){
					if(firstValue == null){
						
					}else{
						jasperParameter = new JasperReportParamter();
						jasperParameter.setName(formatFirstName);
						jasperParameter.setValue(firstValue.toUpperCase());
						returnList.add(jasperParameter);
					}
				}
				if(formatSecondName !=null){
					if(secondValue == null){
						
					}else{
						jasperParameter = new JasperReportParamter();
						parseDoubleNumber = ApplicationUtils.parseDoubleNumber(secondValue);
						jasperParameter.setName(formatSecondName);
						if(parseDoubleNumber != null){
							jasperParameter.setValue(parseDoubleNumber);
							returnList.add(jasperParameter);
						}
					}
				}
				break;
				
			case Constants.REPORT_PARAMTER_TYPE_DATE:
				advancedDate1= (AdvancedDate) reportSetParameter.getFirstValueObject();
				if(formatFirstName !=null){
					
					/*if(firstValue == null|| firstValue.trim().isEmpty()){
						advancedDate1 = null;
					}else{
						ApplicationUtils.parseAdvancedDate(advancedDate1, firstValue, false);
					}*/
					jasperParameter = new JasperReportParamter();
					jasperParameter.setName(formatFirstName);
					jasperParameter.setValue(advancedDate1.getDateValue(true));
					returnList.add(jasperParameter);
				}
				if(formatSecondName !=null){
					date2= (Date) reportSetParameter.getSecondValueObject();
					
					//if we are not selecting date range, then set the second date to today 
					if (advancedDate1.getType() != AdvancedDate.TYPE_DATE ){
						date2= null;
					}
					
					if ( date2 == null ){
						date2=  AdvancedDate.getToday(23, 59, 59);
					}else{
						date2=  AdvancedDate.getDate(date2, 23, 59, 59);
					}
					advancedDate1.setSecondDate(date2);
					
											
					jasperParameter = new JasperReportParamter();
					jasperParameter.setName(formatSecondName);
					jasperParameter.setValue(advancedDate1.getDateTo());
					returnList.add(jasperParameter);
				}
				
				break;
				
			case Constants.REPORT_PARAMTER_TYPE_DATE_TIME:

				advancedDate1= (AdvancedDate) reportSetParameter.getFirstValueObject();
				Timestamp value1 = null;
				Timestamp value2 = null;
				if(formatFirstName !=null){
					jasperParameter.setName(formatFirstName);
					value1 = new Timestamp(advancedDate1.getDateValue(true).getTime());
					jasperParameter.setValue(value1);
					returnList.add(jasperParameter);
				}
				if(formatSecondName !=null){
					date2= (Date) reportSetParameter.getSecondValueObject();
					
					//if we are not selecting date range, then set the second date to today 
					if (advancedDate1.getType() != AdvancedDate.TYPE_DATE ){
						date2= null;
					}
					

					if ( date2 == null ){
						date2=  AdvancedDate.getToday(23, 59, 59);
					}else{
						date2=  AdvancedDate.getDate(date2, 23, 59, 59);
					}
					advancedDate1.setSecondDate(date2);
					
										
					value2 = new Timestamp(advancedDate1.getDateTo().getTime());
					
					jasperParameter = new JasperReportParamter();
					jasperParameter.setName(formatSecondName);
					jasperParameter.setValue( value2 );
					returnList.add(jasperParameter);
				}
				break;
			case Constants.REPORT_PARAMTER_TYPE_NUMBER:
				if(formatFirstName !=null){
					if(firstValue == null){
						
					}else{
						jasperParameter = new JasperReportParamter();
						jasperParameter.setName(formatFirstName);
						parseDoubleNumber = ApplicationUtils.parseDoubleNumber(firstValue);
						jasperParameter.setValue(parseDoubleNumber);
						returnList.add(jasperParameter);
					}
				}
				if(formatSecondName !=null){
					if(secondValue == null){
						
					}else{
						jasperParameter = new JasperReportParamter();
						jasperParameter.setName(formatSecondName);
						parseDoubleNumber = ApplicationUtils.parseDoubleNumber(secondValue);
						jasperParameter.setValue(parseDoubleNumber);
						returnList.add(jasperParameter);
					}
				}
				break;
			case Constants.REPORT_PARAMTER_TYPE_STRING:
				if(formatFirstName !=null){
					if(firstValue == null ||firstValue.trim().equals("")){
						firstValue = "%";
					}
					
					jasperParameter = new JasperReportParamter();
					jasperParameter.setName(formatFirstName);
					jasperParameter.setValue(firstValue);
					returnList.add(jasperParameter);
				}
				if(formatSecondName !=null ){
					if(secondValue == null  || secondValue.trim().equals("")){
						secondValue = "%";
					}
					jasperParameter = new JasperReportParamter();
					jasperParameter.setName(formatSecondName);
					jasperParameter.setValue(secondValue);
					returnList.add(jasperParameter);
				}
				break;
				default:
					//as string
					if(formatFirstName !=null){
						if(firstValue == null  || firstValue.trim().equals("")){
							firstValue = "%";
						}
						
						jasperParameter = new JasperReportParamter();
						jasperParameter.setName(formatFirstName);
						jasperParameter.setValue(firstValue);
						returnList.add(jasperParameter);
					}
					if(formatSecondName !=null){
						if(secondValue == null  || secondValue.trim().equals("")){
							secondValue = "%";
						}
						jasperParameter = new JasperReportParamter();
						jasperParameter.setName(formatSecondName);
						jasperParameter.setValue(secondValue);
						returnList.add(jasperParameter);
					}
			}
		}
		return returnList;
	}
	@Override
	public List<Parameter> convertReportSetToParamters(Report report) {

		return this.convertReportSetToParamters(report.getReportParametersList());
	}

	public Map<String, Object> createJasperReportParameter(List<Parameter> parametersList){
		
		Map<String, Object> retMap = new HashMap<String, Object>();
	
		for (Parameter parameter : parametersList) {
			retMap.put(parameter.getName(), parameter.getValue());
		}
		
		addDateTimePatternParameter(retMap);
		return retMap;
	}
	
	public void setDateTimeFormats( String dateTimePattern, String datePattern, String timePattern ){
		this.dateTimePattern = dateTimePattern; 
		this.datePattern = datePattern;
		this.timePattern = timePattern;
	}
	
	
}

