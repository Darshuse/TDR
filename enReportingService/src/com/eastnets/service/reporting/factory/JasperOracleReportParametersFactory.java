package com.eastnets.service.reporting.factory;

import java.util.List;
import java.util.Map;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.Config;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.service.reporting.JasperReportParamtersFactory;

public class JasperOracleReportParametersFactory extends
		JasperReportParamtersFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2200182218966492694L;

	@Override
	public Map<String, Object> createJasperReportParameter(List<Parameter> parametersList){
		
		Map<String, Object> createJasperReportParameter = super.createJasperReportParameter(parametersList);
		
		Config config = getConfig();
		Integer maxRows = config.getReportMaxFetchSize();
		maxRows = (maxRows == null)? 100 : maxRows;
		String oracleRowLimitCondition = String.format(" and rownum <= %d" , maxRows);
		String dbPrefix = "side";	 
		createJasperReportParameter.put(Constants.JASPER_SQLSERVER_ROW_LIMIT_PARAM_NAME, "");
		createJasperReportParameter.put(Constants.JASPER_ORACLE_ROW_LIMIT_PARAM_NAME, oracleRowLimitCondition);
		createJasperReportParameter.put(Constants.JASPER_DB_PREFIX_PARAM_NAME, dbPrefix);
		createJasperReportParameter.put(Constants.JASPER_PREFIX_LOGO_NAME,config.getReportLogoPath());
//		createJasperReportParameter.put(Constants.LOGO_NAME, config.getLogoName());
		
		return createJasperReportParameter;
	}
}
