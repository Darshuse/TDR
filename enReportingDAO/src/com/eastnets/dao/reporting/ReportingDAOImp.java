package com.eastnets.dao.reporting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.reporting.procedure.ReportingAddReportSetProcedure;
import com.eastnets.dao.reporting.procedure.ReportingAddReportSetValuesProcedure;
import com.eastnets.dao.reporting.procedure.ReportingDeleteReportSetProcedure;
import com.eastnets.dao.reporting.procedure.ReportingISNGapProcedure;
import com.eastnets.dao.reporting.procedure.ReportingOSNGapProcedure;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ReportSetParameter;
import com.eastnets.domain.reporting.ReportSetParameterTypeFactory;
import com.eastnets.domain.reporting.ScheduleDay;
import com.eastnets.domain.reporting.ScheduleType;
import com.eastnets.domain.reporting.StxPattern;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;

public abstract class ReportingDAOImp extends DAOBaseImp implements ReportingDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5847394849346224132L;
	private ReportingAddReportSetProcedure reportingAddReportSetProcedure;
	private ReportingAddReportSetValuesProcedure reportingAddReportSetValuesProcedure;
	private ReportingDeleteReportSetProcedure reportingDeleteReportSetProcedure;
	private ReportingISNGapProcedure reportingISNGapProcedure;
	private ReportingOSNGapProcedure reportingOSNGapProcedure;

	public ReportingAddReportSetProcedure getReportingAddReportSetProcedure() {
		return reportingAddReportSetProcedure;
	}

	public void setReportingAddReportSetProcedure(ReportingAddReportSetProcedure reportingAddReportSetProcedure) {
		this.reportingAddReportSetProcedure = reportingAddReportSetProcedure;
	}

	public ReportingAddReportSetValuesProcedure getReportingAddReportSetValuesProcedure() {
		return reportingAddReportSetValuesProcedure;
	}

	public void setReportingAddReportSetValuesProcedure(ReportingAddReportSetValuesProcedure reportingAddReportSetValuesProcedure) {
		this.reportingAddReportSetValuesProcedure = reportingAddReportSetValuesProcedure;
	}

	public ReportingDeleteReportSetProcedure getReportingDeleteReportSetProcedure() {
		return reportingDeleteReportSetProcedure;
	}

	public void setReportingDeleteReportSetProcedure(ReportingDeleteReportSetProcedure reportingDeleteReportSetProcedure) {
		this.reportingDeleteReportSetProcedure = reportingDeleteReportSetProcedure;
	}

	public ReportingISNGapProcedure getReportingISNGapProcedure() {
		return reportingISNGapProcedure;
	}

	public void setReportingISNGapProcedure(ReportingISNGapProcedure reportingISNGapProcedure) {
		this.reportingISNGapProcedure = reportingISNGapProcedure;
	}

	public ReportingOSNGapProcedure getReportingOSNGapProcedure() {
		return reportingOSNGapProcedure;
	}

	public void setReportingOSNGapProcedure(ReportingOSNGapProcedure reportingOSNGapProcedure) {
		this.reportingOSNGapProcedure = reportingOSNGapProcedure;
	}

	@Override
	public void addReport(Report report) {

		String insertStatement = String.format("insert into PSET_REPORT VALUES(%d,'%s','%s','%s')", report.getId(), report.getName(), report.getCategory(), report.getFileName());
		jdbcTemplate.execute(insertStatement);
	}

	@Override
	public List<Report> getReports() {

		List<Report> reportsList = null;
		String selectQuery = "select id, report, category, file_name from PSET_REPORT order by id";
		reportsList = jdbcTemplate.query(selectQuery, new RowMapper<Report>() {

			@Override
			public Report mapRow(ResultSet rs, int arg1) throws SQLException {

				Report report = new Report();
				report.setId(rs.getLong("id"));
				report.setName(rs.getString("report"));
				report.setCategory(rs.getString("category"));
				report.setFileName(rs.getString("file_name"));
				return report;
			}

		});
		return reportsList;
	}

	@Override
	public List<Report> getReports(String category) {
		List<Report> reportsList = null;

		List<Object> parameters = new ArrayList<>();
		parameters.add(category);
		String selectQuery = "select id, report, category, file_name from PSET_REPORT where CATEGORY = ?  order by id";
		reportsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<Report>() {

			@Override
			public Report mapRow(ResultSet rs, int arg1) throws SQLException {

				Report report = new Report();
				report.setId(rs.getLong("id"));
				report.setName(rs.getString("report"));
				report.setCategory(rs.getString("category"));
				report.setFileName(rs.getString("file_name"));
				return report;
			}

		});
		return reportsList;
	}

	@Override
	public String getReportName(Long reportsetId) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(reportsetId);
		String reportNameQuery = "SELECT REPORT FROM PSET_REPORT WHERE ID = (SELECT REPORT_ID FROM PSET_SET WHERE ID = ?) ";
		String reportName = jdbcTemplate.queryForObject(reportNameQuery, parameters.toArray(), String.class);

		return reportName;
	}

	@Override
	public String getCriteraName(String criteriaId) {
		List<Object> parameters = new ArrayList<>();
		int id = Integer.parseInt(criteriaId);
		parameters.add(id);

		String criteriaNameQuery = "SELECT NAME FROM PSET_SET WHERE ID = ?";
		String criteriaName = jdbcTemplate.queryForObject(criteriaNameQuery, parameters.toArray(), String.class);
		return criteriaName;
	}

	@Override
	public Report getReport(Long reportId) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportId);

		List<Report> reportsList = null;
		String selectQuery = "select id, report, category,file_name from PSET_REPORT where id = ?";
		reportsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<Report>() {

			@Override
			public Report mapRow(ResultSet rs, int arg1) throws SQLException {

				Report report = new Report();
				report.setId(rs.getLong("id"));
				report.setName(rs.getString("report"));
				report.setCategory(rs.getString("category"));
				report.setFileName(rs.getString("file_name"));
				return report;
			}

		});

		if (reportsList != null && !reportsList.isEmpty()) {
			return reportsList.get(0);
		}
		return null;
	}

	@Override
	public Report getReport(String reportName) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportName);

		List<Report> reportsList = null;
		String selectQuery = "select id, report, category, file_name from PSET_REPORT where report = ?";
		reportsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<Report>() {

			@Override
			public Report mapRow(ResultSet rs, int arg1) throws SQLException {

				Report report = new Report();
				report.setId(rs.getLong("id"));
				report.setName(rs.getString("report"));
				report.setCategory(rs.getString("category"));
				report.setFileName(rs.getString("file_name"));
				return report;
			}

		});

		if (reportsList != null && !reportsList.isEmpty()) {
			return reportsList.get(0);
		}
		return null;
	}

	@Override
	public List<String> getReportsCategories() {
		List<String> categoriesList = null;
		String selectQuery = "select distinct CATEGORY from PSET_REPORT where CATEGORY <> 'NULL' order by CATEGORY";
		categoriesList = jdbcTemplate.query(selectQuery, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getString("category");

			}

		});
		return categoriesList;
	}

	@Override
	public void addReportParameters(Report report, Parameter parameter) {

		String insertStatement = "insert into pReportParameters(report_id, id, name, type, first_param, second_param, optional, description, min_length, max_length ) VALUES(%d,%d,'%s',%d,'%s','%s',%d,'%s',%d,%d)";
		ReportSetParameter reportSetParameter = (ReportSetParameter) parameter;

		int optional = (reportSetParameter.isMandatory() == true) ? 0 : 1;

		String description = reportSetParameter.getDescription();
		description = (description == null) ? "" : description;

		insertStatement = String.format(insertStatement, report.getId(), reportSetParameter.getId(), reportSetParameter.getName(), reportSetParameter.getType(), reportSetParameter.getFirstName(), reportSetParameter.getSecondName(), optional,
				description, reportSetParameter.getMinLengthValue(), reportSetParameter.getMaxLengthValue());
		jdbcTemplate.execute(insertStatement);
	}

	@Override
	public List<Parameter> getReportParameters(Report report) {
		List<Parameter> reportsList = null;
		String selectQuery = String.format("select report_id, id, name, type, first_param, second_param, optional, description, min_length, max_length from pReportParameters where   report_id = %d order by id", report.getId());
		reportsList = jdbcTemplate.query(selectQuery, new RowMapper<Parameter>() {
			@Override
			public Parameter mapRow(ResultSet rs, int arg1) throws SQLException {
				long type = rs.getLong("type");

				ReportSetParameter parameter = ReportSetParameterTypeFactory.getReportSetParameter(type);
				parameter.setId(rs.getLong("id"));
				parameter.setName(rs.getString("name"));
				parameter.setType(type);
				parameter.setFirstName(rs.getString("first_param"));
				parameter.setSecondName(rs.getString("second_param"));
				int optional = rs.getInt("optional");
				parameter.setMandatory(optional == 0);
				String string = rs.getString("description");
				string = (string == null) ? string : string.replace("\\n", "\n");
				parameter.setDescription(string);
				parameter.setMinLengthValue(rs.getInt("min_length"));
				parameter.setMaxLengthValue(rs.getInt("max_length"));
				return parameter;
			}

		});
		return reportsList;
	}

	@Override
	public List<Parameter> getReportParameters(Long reportId) {
		List<Parameter> parametersList = null;
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportId);

		String selectQuery = "select id, name, type, first_param, second_param, optional, description, min_length, max_length from pReportParameters where REPORT_ID = ? order by id";
		parametersList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<Parameter>() {

			@Override
			public Parameter mapRow(ResultSet rs, int arg1) throws SQLException {
				long type = rs.getLong("type");
				ReportSetParameter parameter = ReportSetParameterTypeFactory.getReportSetParameter(type);
				parameter.setId(rs.getLong("id"));
				parameter.setName(rs.getString("name"));
				parameter.setType(type);
				parameter.setFirstName(rs.getString("first_param"));
				parameter.setSecondName(rs.getString("second_param"));
				int optional = rs.getInt("optional");
				parameter.setMandatory(optional == 0);
				String string = rs.getString("description");
				string = (string == null) ? string : string.replace("\\n", "\n");
				parameter.setDescription(string);
				parameter.setMinLengthValue(rs.getInt("min_length"));
				parameter.setMaxLengthValue(rs.getInt("max_length"));

				return parameter;
			}

		});
		return parametersList;
	}

	@Override
	public void addReportSet(ReportSet reportSet) {

		this.reportingAddReportSetProcedure.execute(reportSet);
	}

	public Long addReportSetThenReturnId(ReportSet reportSet) {

		return this.reportingAddReportSetProcedure.execute(reportSet);
	}

	@Override
	public ReportSet getReportSet(Long reportSet_id, Long report_id) {

		String selectQuery = "select id, report_id, name  from pset_set where id = ? and report_id = ? ";
		try {
			return jdbcTemplate.queryForObject(selectQuery, new Object[] { reportSet_id, report_id }, new RowMapper<ReportSet>() {

				@Override
				public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

					ReportSet reportSet = new ReportSet();
					reportSet.setId(rs.getLong("id"));
					reportSet.setReportId(rs.getLong("report_id"));
					reportSet.setName(rs.getString("name"));
					return reportSet;
				}

			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public ReportSet getReportSetByName(ReportSet reportSet) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportSet.getName());

		List<ReportSet> reportSetsList = null;
		String selectQuery = "select id, report_id, name  from pset_set where name = ?";
		reportSetsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ReportSet>() {

			@Override
			public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

				ReportSet reportSet = new ReportSet();
				reportSet.setId(rs.getLong("id"));
				reportSet.setReportId(rs.getLong("report_id"));
				reportSet.setName(rs.getString("name"));
				return reportSet;
			}

		});

		if (reportSetsList != null) {
			return reportSetsList.get(0);
		}

		return null;
	}

	@Override
	public ReportSet getReportSet(String userName, Long reportId, String reportSetName) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportId);
		parameters.add(reportSetName.toUpperCase());

		List<ReportSet> reportSetsList = null;
		String selectQuery = "select id, report_id, name from pset_set where report_id = ? and UPPER(name) = ? order by id";
		reportSetsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ReportSet>() {

			@Override
			public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

				ReportSet reportSet = new ReportSet();
				reportSet.setId(rs.getLong("id"));
				reportSet.setReportId(rs.getLong("report_id"));
				reportSet.setName(rs.getString("name"));
				return reportSet;
			}

		});

		if (reportSetsList != null && !reportSetsList.isEmpty()) {

			return reportSetsList.get(0);
		}

		return null;
	}

	@Override
	public List<ReportSet> getReportSets(Report report) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(report.getId());

		List<ReportSet> reportSetsList = null;
		String selectQuery = "select id, report_id, name  from pset_set where REPORT_ID = ? order by id";
		reportSetsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ReportSet>() {

			@Override
			public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

				ReportSet reportSet = new ReportSet();
				reportSet.setId(rs.getLong("id"));
				reportSet.setReportId(rs.getLong("report_id"));
				reportSet.setName(rs.getString("name"));
				return reportSet;
			}

		});

		return reportSetsList;
	}

	@Override
	public List<ReportSet> getReportSets(Long user_Profile_id, Report report) {

		List<ReportSet> reportSetsList = new ArrayList();
		List<Long> set_Ids = null;
		// filter saved criteria based on user profile
		String selectQuery = String.format("select set_id from group_sets where group_id = ? order by id");
		set_Ids = jdbcTemplate.query(selectQuery, new Object[] { user_Profile_id }, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int arg1) throws SQLException {

				Long set_id = rs.getLong("set_id");
				return set_id;
			}

		});

		if (!set_Ids.isEmpty() && set_Ids != null) {
			for (Long id : set_Ids) {
				ReportSet set = getReportSet(id, report.getId());
				if (set != null) {
					reportSetsList.add(set);
				}
			}

		}
		if (!reportSetsList.isEmpty()) {
			return reportSetsList;
		}
		return null;
	}

	@Override
	public void deleteReportSet(ReportSet reportSet) {
		this.reportingDeleteReportSetProcedure.execute(reportSet);
	}

	@Override
	public void addReportSetValues(ReportSet reportSet, List<Parameter> parameters) {

		this.reportingAddReportSetValuesProcedure.execute(reportSet, parameters);
	}

	@Override
	public List<Parameter> getReportSetValues(ReportSet reportSet) {

		List<Parameter> parametersList = null;
		String selectQuery = String.format(
				"select pset_set.report_id,set_id, idx, ui_paramname, pset_value.type, rep_param1, rep_param2, value1, value2,description, min_length, max_length,optional from pset_value, pset_set ,pReportParameters where SET_ID = %d and pset_value.set_id = pset_set.id and pset_set.report_id = pReportParameters.report_id and pset_value.idx = pReportParameters.id order by pset_value.idx",
				reportSet.getId());
		parametersList = jdbcTemplate.query(selectQuery, new RowMapper<Parameter>() {
			@Override
			public Parameter mapRow(ResultSet rs, int arg1) throws SQLException {
				long type = rs.getLong("type");
				ReportSetParameter parameter = ReportSetParameterTypeFactory.getReportSetParameter(type);
				parameter.setId(rs.getLong("idx"));
				parameter.setName(rs.getString("ui_paramname"));
				parameter.setType(type);
				parameter.setFirstName(rs.getString("rep_param1"));
				parameter.setSecondName(rs.getString("rep_param2"));
				parameter.setFirstValue(rs.getString("value1"));
				parameter.setSecondValue(rs.getString("value2"));
				String string = rs.getString("description");
				string = (string == null) ? string : string.replace("\\n", "\n");
				parameter.setDescription(string);
				parameter.setMinLengthValue(rs.getInt("MIN_LENGTH"));
				parameter.setMaxLengthValue(rs.getInt("MAX_LENGTH"));
				int optional = rs.getInt("optional");
				parameter.setMandatory(optional == 0);
				return parameter;
			}

		});
		return parametersList;
	}

	@Override
	public void updateReportSetValues(ReportSet reportSet, List<Parameter> parameters) {
		this.reportingAddReportSetValuesProcedure.execute(reportSet, parameters);
	}

	@Override
	public void deleteReportSetValues(ReportSet reportSet) {
		String deleteQuery = String.format("delete pset_value where SET_ID = %d", reportSet.getId());
		jdbcTemplate.execute(deleteQuery);
	}

	@Override
	public void fillIsnGaps(Map<String, Object> reportParametersMap) {
		reportingISNGapProcedure.execute(reportParametersMap);

	}

	@Override
	public void fillOsnGaps(Map<String, Object> reportParametersMap) {
		reportingOSNGapProcedure.execute(reportParametersMap);

	}

	@Override
	public List<CriteriaSchedule> getCriteriaSchedule() {

		List<CriteriaSchedule> criteriaScheduleList = null;
		String selectQuery = "SELECT id, sch_type_id, sch_hours, CREATED_BY, sch_day, sch_date, pset_set_id, sch_disable, file_format, mail_to, mail_cc, send_notification, attachment_report FROM pset_schedule ";
		criteriaScheduleList = jdbcTemplate.query(selectQuery, new RowMapper<CriteriaSchedule>() {

			@Override
			public CriteriaSchedule mapRow(ResultSet rs, int arg1) throws SQLException {

				CriteriaSchedule criteriaSchedule = new CriteriaSchedule();

				criteriaSchedule.setId(rs.getInt("id"));
				criteriaSchedule.setSchdlType(rs.getInt("sch_type_id"));
				criteriaSchedule.setSchdlHours(rs.getString("sch_hours"));
				criteriaSchedule.setSchdlDay(rs.getInt("sch_day"));
				criteriaSchedule.setSchdlDate(rs.getInt("sch_date"));
				criteriaSchedule.setCriteriaId(rs.getInt("pset_set_id"));
				criteriaSchedule.setSchDisable(rs.getInt("sch_disable") == 1 ? true : false);
				criteriaSchedule.setFileFormat(rs.getString("file_format"));
				criteriaSchedule.setMailTo(rs.getString("mail_to"));
				criteriaSchedule.setMailCc(rs.getString("mail_cc"));
				criteriaSchedule.setUserId(rs.getLong("CREATED_BY"));
				criteriaSchedule.setNotifyAfterGeneration(rs.getInt("send_notification") == 1 ? true : false);
				criteriaSchedule.setAttachGeneratedReport(rs.getInt("attachment_report") == 1 ? true : false);

				return criteriaSchedule;
			}
		});

		return criteriaScheduleList;
	}

	@Override
	public List<CriteriaSchedule> getCriteriaSchedule(String criteriaId) {

		List<CriteriaSchedule> criteriaScheduleList = null;
		String selectQuery = "SELECT id, sch_type_id, sch_hours, sch_day, sch_date, sch_disable, file_format, mail_to, mail_cc,send_notification,attachment_report FROM pset_schedule WHERE  PSET_SET_ID = " + criteriaId;
		criteriaScheduleList = jdbcTemplate.query(selectQuery, new RowMapper<CriteriaSchedule>() {

			@Override
			public CriteriaSchedule mapRow(ResultSet rs, int arg1) throws SQLException {

				CriteriaSchedule criteriaSchedule = new CriteriaSchedule();

				criteriaSchedule.setId(rs.getInt("id"));
				criteriaSchedule.setSchdlType(rs.getInt("sch_type_id"));
				criteriaSchedule.setSchdlHours(rs.getString("sch_hours"));
				criteriaSchedule.setSchdlDay(rs.getInt("sch_day"));
				criteriaSchedule.setSchdlDate(rs.getInt("sch_date"));
				criteriaSchedule.setSchDisable(rs.getInt("sch_disable") == 1 ? true : false);
				criteriaSchedule.setFileFormat(rs.getString("file_format"));
				criteriaSchedule.setMailTo(rs.getString("mail_to"));
				criteriaSchedule.setMailCc(rs.getString("mail_cc"));
				criteriaSchedule.setNotifyAfterGeneration(rs.getInt("send_notification") == 1 ? true : false);
				criteriaSchedule.setAttachGeneratedReport(rs.getInt("attachment_report") == 1 ? true : false);

				return criteriaSchedule;
			}
		});

		return criteriaScheduleList;
	}

	@Override
	public List<ScheduleType> getScheduleType() {

		List<ScheduleType> scheduleTypeList = null;
		String selectQuery = "SELECT id, schedule_type FROM pset_sch_type";
		scheduleTypeList = jdbcTemplate.query(selectQuery, new RowMapper<ScheduleType>() {

			@Override
			public ScheduleType mapRow(ResultSet rs, int arg1) throws SQLException {

				ScheduleType scheduleType = new ScheduleType();

				scheduleType.setId(rs.getInt("id"));
				scheduleType.setName(rs.getString("schedule_type"));

				return scheduleType;
			}
		});

		return scheduleTypeList;
	}

	@Override
	public List<ScheduleDay> getScheduleDay() {

		List<ScheduleDay> scheduleDayList = null;
		String selectQuery = "SELECT id, schedule_day FROM pset_sch_day";
		scheduleDayList = jdbcTemplate.query(selectQuery, new RowMapper<ScheduleDay>() {

			@Override
			public ScheduleDay mapRow(ResultSet rs, int arg1) throws SQLException {

				ScheduleDay scheduleDay = new ScheduleDay();

				scheduleDay.setId(rs.getInt("id"));
				scheduleDay.setName(rs.getString("schedule_day"));

				return scheduleDay;
			}
		});

		return scheduleDayList;
	}

	@Override
	public void updateCriteriaSchedule(CriteriaSchedule criteriaSchedule) {

		int sendNotification = criteriaSchedule.isNotifyAfterGeneration() ? 1 : 0;
		int attachReport = criteriaSchedule.isAttachGeneratedReport() ? 1 : 0;

		StringBuilder updateQuery = new StringBuilder();

		updateQuery.append("UPDATE pset_schedule SET ");
		updateQuery.append(" SCH_HOURS = ?");
		updateQuery.append(", SCH_DAY = ?");
		updateQuery.append(", SCH_DATE = ?");
		updateQuery.append(", SCH_TYPE_ID = ?");
		updateQuery.append(", SCH_DISABLE = ?");
		updateQuery.append(", FILE_FORMAT = ?");
		updateQuery.append(", MAIL_TO = ?");
		updateQuery.append(", MAIL_CC = ?");
		updateQuery.append(", SEND_NOTIFICATION = ?");
		updateQuery.append(", ATTACHMENT_REPORT = ?");
		updateQuery.append(" WHERE");
		updateQuery.append("  PSET_SET_ID = ? ");

		jdbcTemplate.update(updateQuery.toString(), new Object[] { criteriaSchedule.getSchdlHours(), criteriaSchedule.getSchdlDay(), criteriaSchedule.getSchdlDate() != null ? criteriaSchedule.getSchdlDate() : "", criteriaSchedule.getSchdlType(),
				criteriaSchedule.isSchDisable() ? 1 : 0, criteriaSchedule.getFileFormat(), criteriaSchedule.getMailTo(), criteriaSchedule.getMailCc(), sendNotification, attachReport, criteriaSchedule.getCriteriaId() });

	}

	@Override
	public void deleteGeneratedReports(List<Integer> generatedIds) {

		StringBuilder generatedIdsArgument = new StringBuilder();
		for (Integer generatedId : generatedIds) {
			generatedIdsArgument.append(generatedId).append(',');
		}

		String removeStatement = "DELETE FROM PSET_GENERATED_REPORTS WHERE ID IN (" + generatedIdsArgument.substring(0, generatedIdsArgument.length() - 1) + ')';
		jdbcTemplate.update(removeStatement);
	}

	@Override
	public void deleteCriteriaSchedule(Long criteriaId) {
		jdbcTemplate.execute("DELETE FROM pset_schedule WHERE PSET_SET_ID = " + criteriaId);
	}

	@Override
	public List<GeneratedReport> getGeneratedReports(Long criteriaId) {
		String selectQuery = "SELECT ID,PSET_SET_ID  ,START_TIME  ,GENERATION_TIME  ,GENERATION_STATUS  " + ",GENERATION_LOG  ,REPORT_FORMAT  ,DOWNLOAD_COUNT  ,GROUP_ID   "
				+ "FROM PSET_GENERATED_REPORTS where PSET_SET_ID =? ORDER BY GENERATION_TIME DESC";
		List<GeneratedReport> generatedReportsList = null;

		generatedReportsList = jdbcTemplate.query(selectQuery, new Object[] { criteriaId }, new RowMapper<GeneratedReport>() {

			@Override
			public GeneratedReport mapRow(ResultSet rs, int rowNum) throws SQLException {

				GeneratedReport rep = new GeneratedReport();

				rep.setId(rs.getInt("ID"));
				rep.setCriteriaId(rs.getLong("PSET_SET_ID"));
				rep.setStartTime(rs.getTimestamp("START_TIME"));
				rep.setEndTime(rs.getTimestamp("GENERATION_TIME"));
				rep.setStatus(rs.getInt("GENERATION_STATUS"));
				rep.setLog(rs.getString("GENERATION_LOG"));
				rep.setFormat(rs.getString("REPORT_FORMAT"));
				rep.setDownloadCount(rs.getInt("DOWNLOAD_COUNT"));
				rep.setGroupId(rs.getLong("GROUP_ID"));

				return rep;
			}
		});

		return generatedReportsList;
	}

	public void addProfileReportSets(List<Long> profiles, Long reportSetId) {
		for (int i = 0; i < profiles.size(); i++) {
			String insertStatement = "INSERT INTO group_sets( group_id, set_id) " + "	VALUES(" + profiles.get(i) + "," + reportSetId + ")";

			jdbcTemplate.execute(insertStatement);

		}

	}

	public List<Long> getProfilesPerSet(ReportSet set) {
		List<Long> profiles_ids = null;
		String selectStatement = "select group_id  from group_sets  " + "	where  set_id = ? ";
		// getProfileGeneralInfo

		profiles_ids = jdbcTemplate.query(selectStatement, new Object[] { set.getId() }, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Long profile_id = rs.getLong("group_id");

				return profile_id;
			}

		});

		return profiles_ids;
	}

	@Override
	public List<TextControlMessage> getTextControlMessages(Date fromdate, Date toDtae) {
		String selectQuery = "select * from RSMESG m " + " INNER JOIN RSTEXT t " + " on m.aid=t.aid and " + " m.mesg_s_umidl=t.text_s_umidl and " + " m.mesg_s_umidh=text_s_umidh "
				+ " where m.mesg_type in ('103','202')  and  m.mesg_crea_date_time between ? and ?";

		List<TextControlMessage> textControlMessages = null;

		textControlMessages = jdbcTemplate.query(selectQuery, new Object[] { fromdate, toDtae }, new RowMapper<TextControlMessage>() {

			@Override
			public TextControlMessage mapRow(ResultSet rs, int rowNum) throws SQLException {

				TextControlMessage targetMessage = new TextControlMessage();
				targetMessage.setAid(rs.getInt("aid"));
				targetMessage.setUmidh(rs.getLong("mesg_s_umidh"));
				targetMessage.setUnidl(rs.getLong("mesg_s_umidl"));
				targetMessage.setMesgType(rs.getString("mesg_type"));
				targetMessage.setStxVearsin(rs.getString("mesg_syntax_table_ver"));
				targetMessage.setMesgSubFormat(rs.getString("mesg_sub_format"));
				targetMessage.setMesgReceiver(rs.getString("mesg_receiver_swift_address"));
				targetMessage.setMesgSender(rs.getString("mesg_sender_X1"));
				targetMessage.setTextDataBlcok(rs.getString("TEXT_DATA_BLOCK"));
				targetMessage.setMesgIdinfier(rs.getString("MESG_IDENTIFIER"));

				return targetMessage;
			}
		});

		return textControlMessages;
	}

	@Override
	public List<TextControlMessagesField> getTextControlMessagesField(int aid, Long mesgUmidl, Long mesgUmidh) {
		String selectQuery = "select * from rtextfield  " + " where  text_s_umidl = ? and text_s_umidh = ? and aid = ? ";

		List<TextControlMessagesField> generatedReportsList = null;

		generatedReportsList = jdbcTemplate.query(selectQuery, new Object[] { mesgUmidl, mesgUmidh, aid }, new RowMapper<TextControlMessagesField>() {

			@Override
			public TextControlMessagesField mapRow(ResultSet rs, int rowNum) throws SQLException {

				TextControlMessagesField targetMessage = new TextControlMessagesField();
				targetMessage.setFieldCode(String.valueOf(rs.getInt("field_code")));
				targetMessage.setFieldCnt(rs.getInt("field_cnt"));
				targetMessage.setValue(rs.getString("value"));
				targetMessage.setFieldOption(rs.getString("field_option"));
				targetMessage.setSequence(rs.getString("sequence_id"));
				return targetMessage;
			}
		});

		return generatedReportsList;
	}

	@Override
	public List<StxPattern> getFieldPattern(int fieldId) {
		String selectQuery = "SELECT   NB_ROWS,MIN_CHAR,MAX_CHAR,type FROM stxPats   WHERE field_idx= ? order by id desc";

		List<StxPattern> generatedReportsList = null;

		generatedReportsList = jdbcTemplate.query(selectQuery, new Object[] { fieldId }, new RowMapper<StxPattern>() {

			@Override
			public StxPattern mapRow(ResultSet rs, int rowNum) throws SQLException {

				StxPattern targetMessage = new StxPattern();
				targetMessage.setPatternMaxChar(rs.getInt("MAX_CHAR"));
				targetMessage.setPatternMinChar(rs.getInt("MIN_CHAR"));
				targetMessage.setPatternNbNows(rs.getInt("NB_ROWS"));
				targetMessage.setType(rs.getString("type"));
				return targetMessage;
			}
		});

		return generatedReportsList;
	}

	@Override
	public List<ReportSet> getReportSet() {
		List<Object> parameters = new ArrayList<>();
		List<ReportSet> reportSetsList = null;
		String selectQuery = "select id, report_id, name  from pset_set";
		reportSetsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ReportSet>() {

			@Override
			public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

				ReportSet reportSet = new ReportSet();
				reportSet.setId(rs.getLong("id"));
				reportSet.setReportId(rs.getLong("report_id"));
				reportSet.setName(rs.getString("name"));
				return reportSet;
			}

		});

		if (reportSetsList != null && !reportSetsList.isEmpty()) {
			return reportSetsList;
		}

		return null;
	}

	@Override
	public List<Long> getAllGroupSets() {
		List<Long> sets_Id = null;
		String selectStatement = "select DISTINCT set_id  from group_sets  ";
		// getProfileGeneralInfo

		sets_Id = jdbcTemplate.query(selectStatement, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Long set_id = rs.getLong("set_id");

				return set_id;
			}

		});

		return sets_Id;
	}

	public void assignAllProfilesToNonAssignedSets(List<Long> profiles) {
		List<ReportSet> AllSets = getReportSet();

		// List<Long> idList = AllSets.stream().map(ReportSet::getId).collect(Collectors.toList());

		if (AllSets != null && !AllSets.isEmpty()) {
			List<Long> idList = new ArrayList<Long>();
			for (ReportSet set : AllSets) {
				idList.add(set.getId());
			}
			List<Long> Group_Set_Ids = getAllGroupSets();
			idList.removeAll(Group_Set_Ids);

			List<Long> sets_ids_need_to_assign = idList;
			for (Long set_id : sets_ids_need_to_assign) {

				addProfileReportSets(profiles, set_id);
			}
		}
	}

	public void updateProfileReportSets(List<Long> profiles, ReportSet reportSet) {
		deleteFromGroupSetBySetId(reportSet);

		addProfileReportSets(profiles, reportSet.getId());
	}

	public void deleteFromGroupSetBySetId(ReportSet reportSet) {
		String deleteStatement = "delete from group_sets " + "	where  set_id = " + reportSet.getId();

		jdbcTemplate.execute(deleteStatement);

	}

	@Override
	public ReportSet getReportSet(ReportSet reportSet) {
		List<Object> parameters = new ArrayList<>();
		parameters.add(reportSet.getId());
		List<ReportSet> reportSetsList = null;
		String selectQuery = "select id, report_id, name  from pset_set where id = ?";
		reportSetsList = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<ReportSet>() {

			@Override
			public ReportSet mapRow(ResultSet rs, int arg1) throws SQLException {

				ReportSet reportSet = new ReportSet();
				reportSet.setId(rs.getLong("id"));
				reportSet.setReportId(rs.getLong("report_id"));
				reportSet.setName(rs.getString("name"));
				return reportSet;
			}

		});

		if (reportSetsList != null) {
			return reportSetsList.get(0);
		}

		return null;
	}

}
