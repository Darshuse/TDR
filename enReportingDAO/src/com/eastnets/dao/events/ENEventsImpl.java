package com.eastnets.dao.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.events.procedure.LockReadEventsProcedure;
import com.eastnets.domain.events.ENEventMetadata;

public class ENEventsImpl extends DAOBaseImp implements ENEventsDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3136295466254204924L;
	private String defaultSchema;
	private LockReadEventsProcedure lockReadEventsProcedure;

	@Override
	public List<ENEventMetadata> fetchNewEvents(int bulkSize) {

		String sequences = lockReadEventsProcedure.execute(bulkSize);

		String query = "SELECT SEQ_NUM,AID,S_UMIDL,S_UMIDH,INST_NUM,DATE_TIME,SEQ_NBR,TABLE_NAME,OPERATION_MODE,INSERTION_DATE_TIME FROM " + defaultSchema + '.' + "RUPDATENOTIFIER WHERE SEQ_NUM IN (" + sequences + ")";
		List<ENEventMetadata> events = jdbcTemplate.query(query, new RowMapper<ENEventMetadata>() {

			@Override
			public ENEventMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
				ENEventMetadata eventMetadata = new ENEventMetadata();
				eventMetadata.setSequenceNumber(rs.getString("SEQ_NUM"));
				eventMetadata.setAid(rs.getInt("AID"));
				eventMetadata.setUmidl(rs.getString("S_UMIDL"));
				eventMetadata.setUmidh(rs.getString("S_UMIDH"));
				eventMetadata.setDateTime(rs.getTimestamp("DATE_TIME"));
				eventMetadata.setInstNumber(rs.getString("INST_NUM"));
				// eventMetadata.setCreationDate(rs.getDate("CREA_DATE_TIME"));
				eventMetadata.setSeqNBR(rs.getString("SEQ_NBR"));
				eventMetadata.setTableName(rs.getString("TABLE_NAME"));
				eventMetadata.setOperationMode(rs.getInt("OPERATION_MODE"));
				eventMetadata.setCreationDate(rs.getTimestamp("INSERTION_DATE_TIME"));
				return eventMetadata;
			}
		});
		return events;
	}

	@Override
	public void bulkInsertEventHistory(final List<ENEventMetadata> eventMetadataList) {
		String insertStatemnt = "INSERT INTO " + defaultSchema + '.' + " RUPDATENOTIFIERHISTORY (SEQ_NUM,AID,S_UMIDL,S_UMIDH,INST_NUM,DATE_TIME,SEQ_NBR,TABLE_NAME,OPERATION_MODE,INSERTION_DATE_TIME) VALUES (?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				ENEventMetadata eventMetadata = eventMetadataList.get(index);
				preparedStatement.setString(1, eventMetadata.getSequenceNumber());
				preparedStatement.setInt(2, eventMetadata.getAid());
				preparedStatement.setString(3, eventMetadata.getUmidl());
				preparedStatement.setString(4, eventMetadata.getUmidh());
				preparedStatement.setString(5, eventMetadata.getInstNumber());
				if (eventMetadata.getDateTime() != null) {
					preparedStatement.setTimestamp(6, new Timestamp(eventMetadata.getDateTime().getTime()));
				} else {
					preparedStatement.setTimestamp(6, null);
				}
				preparedStatement.setString(7, eventMetadata.getSeqNBR());
				preparedStatement.setString(8, eventMetadata.getTableName());
				preparedStatement.setInt(9, eventMetadata.getOperationMode());
				preparedStatement.setTimestamp(10, new Timestamp(eventMetadata.getCreationDate().getTime()));
			}

			@Override
			public int getBatchSize() {
				return eventMetadataList.size();
			}
		});
	}

	@Override
	public void removeSentMessages() {
		// Status = 1 means these messages sent successfully
		try {
			String deleteStatement = "DELETE FROM " + defaultSchema + ".RUPDATENOTIFIER WHERE STATUS = 1";
			jdbcTemplate.update(deleteStatement);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void updateEvents(List<ENEventMetadata> eventMetadataList, int status) {

		try {
			StringBuilder sequenceNumbers = new StringBuilder();
			for (ENEventMetadata eventMetadata : eventMetadataList) {
				sequenceNumbers.append(eventMetadata.getSequenceNumber()).append(",");
			}

			String UpdateStatement = "UPDATE " + defaultSchema + '.' + "RUPDATENOTIFIER SET STATUS = ? WHERE SEQ_NUM IN (" + sequenceNumbers.substring(0, sequenceNumbers.length() - 1) + ")";
			jdbcTemplate.update(UpdateStatement, new Object[] { status });
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void removeHistoryEvents(List<ENEventMetadata> eventMetadataList) {

		try {
			StringBuilder sequenceNumbers = new StringBuilder();
			for (ENEventMetadata eventMetadata : eventMetadataList) {
				sequenceNumbers.append(eventMetadata.getSequenceNumber()).append(",");
			}

			String deleteQuery = "DELETE FROM " + defaultSchema + '.' + "RUPDATENOTIFIERHISTORY WHERE SEQ_NUM IN (" + sequenceNumbers.substring(0, sequenceNumbers.length() - 1) + ")";
			jdbcTemplate.update(deleteQuery);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public LockReadEventsProcedure getLockReadEventsProcedure() {
		return lockReadEventsProcedure;
	}

	public void setLockReadEventsProcedure(LockReadEventsProcedure lockReadEventsProcedure) {
		this.lockReadEventsProcedure = lockReadEventsProcedure;
	}

	@Override
	public void changeProcessingToNewEvents() {
		String updateStatement = "UPDATE " + defaultSchema + '.' + "RUPDATENOTIFIER SET STATUS = 0 WHERE STATUS = 3";
		jdbcTemplate.update(updateStatement);
	}

}
