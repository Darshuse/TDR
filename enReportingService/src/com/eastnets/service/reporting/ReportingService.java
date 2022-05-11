/**
 * 
 */
package com.eastnets.service.reporting;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ReportingOptions;
import com.eastnets.domain.reporting.ScheduleDay;
import com.eastnets.domain.reporting.ScheduleType;
import com.eastnets.domain.reporting.StxPattern;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;
import com.eastnets.service.common.ReportService.ReportTypes;

/**
 * 
 * @author EastNets
 * @since dNov 11, 2012
 * 
 */
public interface ReportingService {

	/**
	 * Add record to PSET_REPORT
	 * 
	 * @param userName
	 * @param report
	 */
	public void addReport(String userName, Report report);

	/**
	 * Get all reports from PSET_REPORT table
	 * 
	 * @param userName
	 * @return
	 */
	public List<Report> getReports(String userName);

	/**
	 * 
	 * @param userName
	 * @param category
	 * @return
	 */
	public List<Report> getReports(String userName, String category);

	/**
	 * Get all reports from PSET_REPORT table
	 * 
	 * @param userName
	 * @param reportId
	 * @return
	 */
	public Report getReport(String userName, Long reportId);

	/**
	 * 
	 * @param reportsetId
	 * @return
	 */
	public String getReportName(Long reportsetId);

	/**
	 * 
	 * @param criteriaId
	 * @return
	 */
	public String getCriteriaName(String criteriaId);

	/**
	 * Get report from PSET_REPORT table
	 * 
	 * @param userName
	 * @param reportName
	 * @return
	 */
	public Report getReport(String userName, String reportName);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<String> getReportsCategories(String userName);

	/**
	 * Add records to PREPORTPARAMETERS
	 * 
	 * @param userName
	 * @param report
	 */
	public void addReportParameters(String userName, Report report);

	/**
	 * Get records from PREPORTPARAMETERS
	 * 
	 * @param userName
	 * @param report
	 * @return
	 */
	public List<Parameter> getReportParameters(User user, Report report);

	/**
	 * Get records from PREPORTPARAMETERS
	 * 
	 * @param userName
	 * @param reportId
	 * @return
	 */

	public List<Parameter> getReportParameters(User user, Long reportId);

	/**
	 * Add new report set to PSET_SET table by calling REPSAVEPARAMSET stored procedure
	 * 
	 * @param userName
	 * @param reportSet
	 * @return
	 */
	public void addReportSet(String userName, ReportSet reportSet);

	/**
	 * Get all Sets for specific report from PSET_SET table
	 * 
	 * @param userName
	 * @param report
	 * @return
	 */
	public List<ReportSet> getReportSets(Long user_profile_id, Report report);

	public List<ReportSet> getReportSets(String userName, Report report);

	/**
	 * Get report set from PSET_SET table
	 * 
	 * @param userName
	 * @param reportSet
	 * @return
	 */
	public ReportSet getReportSet(String userName, ReportSet reportSet);

	/**
	 * Get report set from PSET_SET table
	 * 
	 * @param userName
	 * @param reportSet
	 * @return
	 */
	public ReportSet getReportSetByName(String userName, ReportSet reportSet);

	/**
	 * 
	 * @param userName
	 * @param reportId
	 * @param reportSetName
	 */
	public ReportSet getReportSet(String userName, Long reportId, String reportSetName);

	/**
	 * 
	 * @param generatedIds
	 */
	public void deleteGeneratedReports(List<Integer> generatedIds);

	/**
	 * Delete report set from PSET_SET table by calling REPDELETEPARAMSET stored procedure
	 * 
	 * @param userName
	 * @param reportSet
	 */
	public void deleteReportSet(String userName, ReportSet reportSet);

	/**
	 * Add new report set values by calling REPSAVEPARAMVALUE stored procedure
	 * 
	 * @param userName
	 * @param reportSet
	 * @param report
	 */
	public void addReportSetValues(String userName, ReportSet reportSet, List<Parameter> parameters);

	/**
	 * Get all report set values from PSET_VALUE table
	 * 
	 * @param userName
	 * @param reportSet
	 * @return
	 */
	public List<Parameter> getReportSetValues(String userName, ReportSet reportSet);

	/**
	 * Update record set values in PSET_VALUE table
	 * 
	 * @param userName
	 * @param reportSet
	 * @param report
	 */
	public void updateReportSetValues(String userName, ReportSet reportSet, List<Parameter> parameters);

	/**
	 * Delete report set values from PSET_VALUE table by calling REPDELETEPARAMSET stored procedure
	 * 
	 * @param userName
	 * @param reportSet
	 */
	public void deleteReportSetValues(String userName, ReportSet reportSet);

	/**
	 * get Reporting Options from wcApplicationSettings
	 * 
	 * @param userName
	 * @return
	 */
	public ReportingOptions getReportingOptions(String user, Long userId);

	/**
	 * get report full path
	 * 
	 * @param report
	 * @return String
	 */
	public String getReportPath(Report report);

	/**
	 * convert report set values to list of report parameters
	 * 
	 * @param reportSetValues
	 * @return List<Parameter>
	 */
	public List<Parameter> convertReportSetValuesToParamters(List<Parameter> reportSetValues);

	/**
	 * prepare report set values to use them in export reports
	 * 
	 * @param report
	 * @param reportParametersList
	 * @param reportType
	 * @return Map<String,Object>
	 */
	public Map<String, Object> convertParametersToJasperParamters(Report report, List<Parameter> reportParametersList, ReportTypes reportType, Map<String, Integer> currenciesMap);

	public void fillIsnGaps(String userName, Map<String, Object> reportParametersMap);

	public void fillOsnGaps(String userName, Map<String, Object> reportParametersMap);

	/**
	 * needed to initialise the date format data
	 * 
	 * @param dateTimePattern
	 * @param datePattern
	 * @param timePattern
	 */
	void setDateTimeFormats(String dateTimePattern, String datePattern, String timePattern);

	public List<CriteriaSchedule> getCriteriaSchedule();

	public List<CriteriaSchedule> getCriteriaSchedule(String criteriaId);

	public List<ScheduleType> getScheduleType();

	public List<ScheduleDay> getScheduleDay();

	public void addCriteriaSchedule(CriteriaSchedule criteriaSchedule);

	public void updateCriteriaSchedule(CriteriaSchedule criteriaSchedule);

	public void deleteCriteriaSchedule(Long criteriaId);

	public void addGeneratedReport(GeneratedReport rep);

	public InputStream getGeneratedReport(Integer generationId);

	public List<GeneratedReport> getGeneratedReports(Long criteriaId);

	public List<TextControlMessage> getTextControlMessages(Date fromdate, Date toDtae);

	public List<TextControlMessagesField> getTextControlMessagesField(int aid, Long mesgUmidl, Long mesgUmidh);

	public List<StxPattern> getFieldPattern(int fieldId);

	public Long addReportSetthenReturnId(String userName, ReportSet reportSet);

	public void addProfileReportSets(List<Long> profiles, Long reportSetId);

	public List<Long> getProfilesPerSet(ReportSet set);

	public void assignAllProfilesToNonAssignedSets(List<Long> profiles);

	public ReportSet getReportSet(Long reportSet_id, Long report_id);

	public void updateProfileReportSets(List<Long> profiles, ReportSet reportSet);

}
