/**
 * 
 */
package com.eastnets.service.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.reporting.ReportingDAO;
import com.eastnets.domain.ApplicationSetting;
import com.eastnets.domain.Config;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ReportSetParameter;
import com.eastnets.domain.reporting.ReportSetParameterTypeFactory;
import com.eastnets.domain.reporting.ReportingOptions;
import com.eastnets.domain.reporting.ScheduleDay;
import com.eastnets.domain.reporting.ScheduleType;
import com.eastnets.domain.reporting.StxPattern;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.service.common.ReportService.ReportTypes;

/**
 * 
 * @author EastNets
 * @since dNov 11, 2012
 * 
 */
public class ReportingServiceImpl extends ServiceBaseImp implements ReportingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1477587095544743591L;
	private ReportingDAO reportingDAO;
	private CommonDAO commonDAO;
	private Config config;

	JasperReportParamtersFactory reportParamtersFactory;

	String dateTimePattern;
	String datePattern;
	String timePattern;

	public ReportingDAO getReportingDAO() {
		return reportingDAO;
	}

	public void setReportingDAO(ReportingDAO reportingDAO) {
		this.reportingDAO = reportingDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public JasperReportParamtersFactory getReportParamtersFactory() {
		return reportParamtersFactory;
	}

	public void setReportParamtersFactory(JasperReportParamtersFactory reportParamtersFactory) {
		this.reportParamtersFactory = reportParamtersFactory;
	}

	public void setDateTimeFormats(String dateTimePattern, String datePattern, String timePattern) {
		this.dateTimePattern = dateTimePattern;
		this.datePattern = datePattern;
		this.timePattern = timePattern;

		if (reportParamtersFactory != null) {
			reportParamtersFactory.setDateTimeFormats(dateTimePattern, datePattern, timePattern);
		}
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void addReport(String userName, Report report) {

		reportingDAO.addReport(report);
	}

	@Override
	public List<Report> getReports(String userName) {

		return reportingDAO.getReports();
	}

	@Override
	public List<Report> getReports(String userName, String category) {
		return reportingDAO.getReports(category);
	}

	@Override
	public Report getReport(String userName, Long reportId) {
		return reportingDAO.getReport(reportId);
	}

	@Override
	public String getReportName(Long reportsetId) {
		return reportingDAO.getReportName(reportsetId);
	}

	@Override
	public String getCriteriaName(String criteriaId) {
		return reportingDAO.getCriteraName(criteriaId);
	}

	@Override
	public Report getReport(String userName, String reportName) {

		return reportingDAO.getReport(reportName);
	}

	@Override
	public List<String> getReportsCategories(String userName) {
		return reportingDAO.getReportsCategories();
	}

	@Override
	public void addReportParameters(String userName, Report report) {

		List<Parameter> reportParametersList = report.getReportParametersList();
		for (Parameter parameter : reportParametersList) {
			reportingDAO.addReportParameters(report, parameter);
		}

	}

	@Override
	public List<Parameter> getReportParameters(User user, Report report) {

		return reportingDAO.getReportParameters(report);
	}

	@Override
	public List<Parameter> getReportParameters(User user, Long reportId) {
		return reportingDAO.getReportParameters(reportId);
	}

	@Override
	public void addReportSet(String userName, ReportSet reportSet) {
		reportingDAO.addReportSet(reportSet);
	}

	@Override
	public Long addReportSetthenReturnId(String userName, ReportSet reportSet) {
		return reportingDAO.addReportSetThenReturnId(reportSet);

	}

	@Override
	public List<ReportSet> getReportSets(String userName, Report report) {

		return reportingDAO.getReportSets(report);
	}

	@Override
	public ReportSet getReportSet(String userName, ReportSet reportSet) {
		return reportingDAO.getReportSet(reportSet);
	}

	public ReportSet getReportSet(Long reportSet_id, Long report_id) {
		return reportingDAO.getReportSet(reportSet_id, report_id);
	}

	@Override
	public ReportSet getReportSetByName(String userName, ReportSet reportSet) {
		return reportingDAO.getReportSetByName(reportSet);
	}

	@Override
	public ReportSet getReportSet(String userName, Long reportId, String reportSetName) {

		return reportingDAO.getReportSet(userName, reportId, reportSetName);
	}

	@Override
	public void deleteGeneratedReports(List<Integer> generatedIds) {
		reportingDAO.deleteGeneratedReports(generatedIds);
	}

	@Override
	public void deleteReportSet(String userName, ReportSet reportSet) {
		this.deleteCriteriaSchedule(reportSet.getId());
		reportingDAO.deleteReportSet(reportSet);
	}

	@Override
	public void addReportSetValues(String userName, ReportSet reportSet, List<Parameter> parameters) {
		reportingDAO.addReportSetValues(reportSet, parameters);
	}

	@Override
	public List<Parameter> getReportSetValues(String userName, ReportSet reportSet) {
		List<Parameter> reportSetValues = reportingDAO.getReportSetValues(reportSet);
		return reportSetValues;
	}

	@Override
	public void updateReportSetValues(String userName, ReportSet reportSet, List<Parameter> parameters) {

		reportingDAO.updateReportSetValues(reportSet, parameters);

	}

	@Override
	public void deleteReportSetValues(String userName, ReportSet reportSet) {
		reportingDAO.deleteReportSetValues(reportSet);
	}

	@Override
	public ReportingOptions getReportingOptions(String userName, Long userId) {

		Long aid = 0L;
		List<ApplicationSetting> applicationSettings = commonDAO.getApplicationSettings(Constants.APPLICATION_REPORTING_OPTIONS, userId, aid);

		if (applicationSettings == null || applicationSettings.isEmpty()) {
			return null;
		}

		return null;
	}

	@Override
	public String getReportPath(Report report) {

		String reportName = String.format("%s%s%s%s%s.jrxml", config.getReportsDirectoryPath(), File.separator, report.getCategory(), File.separator, report.getFileName());
		return reportName;
	}

	@Override
	public List<Parameter> convertReportSetValuesToParamters(List<Parameter> reportSetValues) {

		List<Parameter> createReportParameter = reportParamtersFactory.convertReportSetToParamters(reportSetValues);
		return createReportParameter;

	}

	@Override
	public Map<String, Object> convertParametersToJasperParamters(Report report, List<Parameter> reportParametersList, ReportTypes reportType, Map<String, Integer> currenciesMap) {

		String reportDirectory = String.format("%s%s%s%s", config.getReportsDirectoryPath(), File.separator, report.getCategory(), File.separator);
		ReportSetParameter paramter = ReportSetParameterTypeFactory.getReportSetParameter((long) Constants.REPORT_PARAMTER_TYPE_STRING);
		paramter.setName(Constants.JASPER_REPORT_DIR);
		paramter.setValue(reportDirectory);
		paramter.setType((long) Constants.REPORT_PARAMTER_TYPE_STRING);
		reportParametersList.add(paramter);

		paramter = ReportSetParameterTypeFactory.getReportSetParameter((long) Constants.REPORT_PARAMTER_TYPE_STRING);
		paramter.setName(Constants.EXPORT_FORMAT_PARAM_NAME);
		paramter.setValue(reportType.toString());
		paramter.setType((long) Constants.REPORT_PARAMTER_TYPE_STRING);
		reportParametersList.add(paramter);

		paramter = ReportSetParameterTypeFactory.getReportSetParameter((long) Constants.REPORT_PARAMTER_TYPE_CURRENCIES_MAP);
		paramter.setName(Constants.CURRENCIES_MAP);
		paramter.setValue(formatMap(currenciesMap));
		paramter.setType((long) Constants.REPORT_PARAMTER_TYPE_CURRENCIES_MAP);
		reportParametersList.add(paramter);
		try {
			FileInputStream fis = new FileInputStream(config.getReportsDirectoryPath() + File.separator + "i18n_en.properties");

			if (fis != null) {

				ResourceBundle resourceBundle = new PropertyResourceBundle(fis);

				paramter = ReportSetParameterTypeFactory.getReportSetParameter((long) Constants.REPORT_PARAMTER_TYPE_RESOURCE_BUNDLE);
				paramter.setName(Constants.JASPER_REPORT_RESOURCE_BUNDLE);
				paramter.setValue(resourceBundle);
				paramter.setType((long) Constants.REPORT_PARAMTER_TYPE_RESOURCE_BUNDLE);
				reportParametersList.add(paramter);
			}

		} catch (FileNotFoundException ex) {
			// default resource bundle file is used.
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Map<String, Object> createJasperReportParameter = reportParamtersFactory.createJasperReportParameter(reportParametersList);
		return createJasperReportParameter;
	}

	private Map<String, String> formatMap(Map<String, Integer> currenciesMap) {

		Map<String, String> currenciesString = new HashMap<>();

		for (String currencyCode : currenciesMap.keySet()) {
			currenciesString.put(currencyCode, amountFormatPattern(currenciesMap.get(currencyCode)));
		}

		return currenciesString;

	}

	private String amountFormatPattern(Integer numberOfDigits) {

		String format = "#,##0.";

		for (int i = 0; i < numberOfDigits; i++) {
			format += "0";
		}

		return format;
	}

	@Override
	public void fillIsnGaps(String userName, Map<String, Object> reportParametersMap) {
		reportingDAO.fillIsnGaps(reportParametersMap);

	}

	@Override
	public void fillOsnGaps(String userName, Map<String, Object> reportParametersMap) {
		reportingDAO.fillOsnGaps(reportParametersMap);
	}

	@Override
	public List<CriteriaSchedule> getCriteriaSchedule() {
		return reportingDAO.getCriteriaSchedule();
	}

	@Override
	public List<CriteriaSchedule> getCriteriaSchedule(String criteriaId) {
		return reportingDAO.getCriteriaSchedule(criteriaId);
	}

	@Override
	public List<ScheduleType> getScheduleType() {
		return reportingDAO.getScheduleType();
	}

	@Override
	public List<ScheduleDay> getScheduleDay() {
		return reportingDAO.getScheduleDay();
	}

	@Override
	public void addCriteriaSchedule(CriteriaSchedule criteriaSchedule) {
		reportingDAO.addCriteriaSchedule(criteriaSchedule);
	}

	public void updateCriteriaSchedule(CriteriaSchedule criteriaSchedule) {
		reportingDAO.updateCriteriaSchedule(criteriaSchedule);
	}

	public void deleteCriteriaSchedule(Long criteriaId) {
		reportingDAO.deleteCriteriaSchedule(criteriaId);
	}

	@Override
	public void addGeneratedReport(GeneratedReport rep) {
		reportingDAO.addGeneratedReport(rep);
	}

	@Override
	public InputStream getGeneratedReport(Integer generationId) {
		return reportingDAO.getGenertedReport(generationId);
	}

	@Override
	public List<GeneratedReport> getGeneratedReports(Long criteriaId) {
		return reportingDAO.getGeneratedReports(criteriaId);
	}

	@Override
	public List<ReportSet> getReportSets(Long user_profile_id, Report report) {
		// TODO Auto-generated method stub
		return reportingDAO.getReportSets(user_profile_id, report);
	}

	@Override

	public void addProfileReportSets(List<Long> profiles, Long reportSetId) {
		reportingDAO.addProfileReportSets(profiles, reportSetId);
	}

	@Override
	public List<Long> getProfilesPerSet(ReportSet set) {
		return reportingDAO.getProfilesPerSet(set);
	}

	@Override
	public List<TextControlMessage> getTextControlMessages(Date fromdate, Date toDtae) {
		// TODO Auto-generated method stub
		return reportingDAO.getTextControlMessages(fromdate, toDtae);
	}

	@Override
	public List<TextControlMessagesField> getTextControlMessagesField(int aid, Long mesgUmidl, Long mesgUmidh) {
		// TODO Auto-generated method stub
		return reportingDAO.getTextControlMessagesField(aid, mesgUmidl, mesgUmidh);
	}

	@Override
	public List<StxPattern> getFieldPattern(int fieldId) {
		// TODO Auto-generated method stub
		return reportingDAO.getFieldPattern(fieldId);
	}

	@Override
	public void assignAllProfilesToNonAssignedSets(List<Long> profiles) {
		reportingDAO.assignAllProfilesToNonAssignedSets(profiles);
	}

	@Override
	public void updateProfileReportSets(List<Long> profiles, ReportSet reportSet) {
		reportingDAO.updateProfileReportSets(profiles, reportSet);
	}

}
