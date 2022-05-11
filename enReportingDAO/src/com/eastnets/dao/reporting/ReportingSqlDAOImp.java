package com.eastnets.dao.reporting;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;

public class ReportingSqlDAOImp extends ReportingDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8879640305903144793L;

	@Override
	public void addCriteriaSchedule(CriteriaSchedule criteriaSchedule) {

		int sendNotification = criteriaSchedule.isNotifyAfterGeneration() ? 1 : 0;
		int attachReport = criteriaSchedule.isAttachGeneratedReport() ? 1 : 0;

		String insertQuery = "INSERT INTO pset_schedule ( PSET_SET_ID, SCH_HOURS, SCH_DAY, SCH_DATE, CREATED_BY, SCH_TYPE_ID, SCH_DISABLE, FILE_FORMAT, MAIL_TO, MAIL_CC,SEND_NOTIFICATION,ATTACHMENT_REPORT) " + "VALUES ( "
				+ criteriaSchedule.getCriteriaId() + ", '" + criteriaSchedule.getSchdlHours() + "', " + criteriaSchedule.getSchdlDay() + "," + criteriaSchedule.getSchdlDate() + "," + criteriaSchedule.getUserId() + " , "
				+ criteriaSchedule.getSchdlType() + " , " + (criteriaSchedule.isSchDisable() ? 1 : 0) + " , '" + criteriaSchedule.getFileFormat() + "' , '" + criteriaSchedule.getMailTo() + "' , '" + criteriaSchedule.getMailCc() + "' , "
				+ sendNotification + " , " + attachReport + " )";

		jdbcTemplate.execute(insertQuery);
	}

	public void addGeneratedReport(GeneratedReport rep) {
		String insertStatement = "insert into PSET_GENERATED_REPORTS(PSET_SET_ID,GENERATED_REPORT,REPORT_FORMAT,GENERATION_STATUS,GROUP_ID,GENERATION_LOG,START_TIME,GENERATION_TIME) VALUES (?,?,?,?,?,?,?,?)";
		LobHandler lobHandler = new DefaultLobHandler(); // reusable object
		try {
			jdbcTemplate.update(insertStatement, new Object[] { rep.getCriteriaId(), new SqlLobValue(rep.getBlob(), lobHandler), rep.getFormat(), rep.getStatus(), rep.getGroupId(), rep.getLog(), rep.getStartTime(), rep.getEndTime() },
					new int[] { Types.INTEGER, Types.BLOB, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP });
		} catch (DataAccessException e) {
			System.out.println("Report can't be inserted to PSET_GENERATED_REPORTS Table Check Your arguments ");
			e.printStackTrace();
			throw e;
		}
	}
	//

	@Override
	public InputStream getGenertedReport(Integer generationId) {
		String queryString = "select  generated_report from PSET_GENERATED_REPORTS where id=?";
		List<InputStream> genertedReport = jdbcTemplate.query(queryString, new Object[] { generationId }, new RowMapper<InputStream>() {
			public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
				Blob b = rs.getBlob("generated_report");
				InputStream is = b.getBinaryStream();
				return is;
			}
		});

		if (genertedReport.size() > 0)
			return genertedReport.get(0);

		return null;
	}

}
