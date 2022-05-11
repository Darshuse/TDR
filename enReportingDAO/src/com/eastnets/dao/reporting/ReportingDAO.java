package com.eastnets.dao.reporting;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eastnets.dao.DAO;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ScheduleDay;
import com.eastnets.domain.reporting.ScheduleType;
import com.eastnets.domain.reporting.StxPattern;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;

public interface ReportingDAO extends DAO {

	/**
	 * Add record to PSET_REPORT
	 * 
	 * @param report
	 */
	public void addReport(Report report);

	/**
	 * Get all reports from PSET_REPORT table
	 * 
	 * @return List<Report>
	 */
	public List<Report> getReports();

	/**
	 * 
	 * @param category
	 * @return
	 */
	public List<Report> getReports(String category);

	/**
	 * Get report from PSET_REPORT table
	 * 
	 * @param reportId
	 * @return Report
	 */
	public Report getReport(Long reportId);

	/**
	 * Get report from PSET_REPORT table by report name
	 * 
	 * @param reportName
	 * @return Report
	 */
	public Report getReport(String reportName);

	/**
	 * 
	 * @param criteriaId
	 * @return
	 */
	public String getCriteraName(String criteriaId);

	/**
	 * 
	 * @param reportsetId
	 * @return report Name
	 */
	public String getReportName(Long reportsetId);

	/**
	 * 
	 * @return
	 */
	public List<String> getReportsCategories();

	/**
	 * Add records to PREPORTPARAMETERS
	 * 
	 * @param report
	 */
	public void addReportParameters(Report report, Parameter parameter);

	/**
	 * Get records from PREPORTPARAMETERS
	 * 
	 * @param report
	 * @return List<Parameter>
	 */
	public List<Parameter> getReportParameters(Report report);

	/**
	 * Get records from PREPORTPARAMETERS
	 * 
	 * @param reportId
	 * @return List<Parameter>
	 */
	public List<Parameter> getReportParameters(Long reportId);

	/**
	 * Add new report set to PSET_SET table by calling REPSAVEPARAMSET stored procedure
	 * 
	 * @param reportSet
	 * @return Long
	 */
	public void addReportSet(ReportSet reportSet);

	/**
	 * Get all Sets for specific report from PSET_SET table
	 * 
	 * @param report
	 * @return List<ReportSet>
	 */
	public List<ReportSet> getReportSets(Long user_Profile_id, Report report);

	public List<ReportSet> getReportSets(Report report);

	/**
	 * Get report set from PSET_SET table
	 * 
	 * @param reportSet
	 * @return ReportSet
	 */
	public ReportSet getReportSet(Long reportSet_id, Long report_id);

	public ReportSet getReportSet(ReportSet reportSet);

	/**
	 * Get report set from PSET_SET table
	 * 
	 * @param reportSet
	 * @return ReportSet
	 */
	public ReportSet getReportSetByName(ReportSet reportSet);

	/**
	 * Get report set from PSET_SET table by report id and report set name
	 * 
	 * @param userName
	 * @param reportId
	 * @param reportSetName
	 * @return
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
	 * @param reportSet
	 */
	public void deleteReportSet(ReportSet reportSet);

	/**
	 * Add new report set values by calling REPSAVEPARAMVALUE stored procedure
	 * 
	 * @param reportSet
	 * @param report
	 */
	public void addReportSetValues(ReportSet reportSet, List<Parameter> parameters);

	/**
	 * Get all report set values from PSET_VALUE table
	 * 
	 * @param reportSet
	 * @return List<Parameter>
	 */
	public List<Parameter> getReportSetValues(ReportSet reportSet);

	/**
	 * Update record set values in PSET_VALUE table
	 * 
	 * @param reportSet
	 * @param report
	 */
	public void updateReportSetValues(ReportSet reportSet, List<Parameter> parameters);

	/**
	 * Delete report set values from PSET_VALUE table by calling REPDELETEPARAMSET stored procedure
	 * 
	 * @param reportSet
	 */
	public void deleteReportSetValues(ReportSet reportSet);

	public void fillIsnGaps(Map<String, Object> reportParametersMap);

	public void fillOsnGaps(Map<String, Object> reportParametersMap);

	public List<CriteriaSchedule> getCriteriaSchedule();

	public List<CriteriaSchedule> getCriteriaSchedule(String criteriaId);

	public List<ScheduleType> getScheduleType();

	public List<ScheduleDay> getScheduleDay();

	public void addCriteriaSchedule(CriteriaSchedule criteriaSchedule);

	public void updateCriteriaSchedule(CriteriaSchedule criteriaSchedule);

	public void deleteCriteriaSchedule(Long criteriaId);

	public void addGeneratedReport(GeneratedReport genertedReport);

	public InputStream getGenertedReport(Integer generationId);

	public List<GeneratedReport> getGeneratedReports(Long criteriaId);

	public List<TextControlMessage> getTextControlMessages(Date fromdate, Date toDtae);

	public List<TextControlMessagesField> getTextControlMessagesField(int aid, Long mesgUmidl, Long mesgUmidh);

	public List<StxPattern> getFieldPattern(int fieldId);

	public void addProfileReportSets(List<Long> profiles, Long reportSetId);

	public List<Long> getProfilesPerSet(ReportSet set);

	public Long addReportSetThenReturnId(ReportSet reportSet);

	// public List<ReportSet> getSetsPerProfile(Report report) ;
	public void assignAllProfilesToNonAssignedSets(List<Long> profiles);

	public List<ReportSet> getReportSet();

	public List<Long> getAllGroupSets();

	public void updateProfileReportSets(List<Long> profiles, ReportSet reportSet);
}
