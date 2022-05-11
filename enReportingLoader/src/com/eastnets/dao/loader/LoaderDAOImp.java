package com.eastnets.dao.loader;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.config.DBType;
import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageSource;
import com.eastnets.domain.loader.LoaderMessage.MessageStatus;
import com.eastnets.domain.loader.TextPart;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.util.LoaderUtils;

/**
 * @author MKassab
 * 
 */
public abstract class LoaderDAOImp extends DAOBaseImp implements LoaderDAO {

	private static final long serialVersionUID = 6192266956471394148L;
	private AppConfigBean appConfigBean;
	protected static final Logger LOGGGER = Logger.getLogger(LoaderDAOImp.class);

	@Override
	public List<String> getMesgFormat() {

		LOGGGER.debug("fetching messages formats names");
		String queryString = "select MESG_FRMT_NAME from ldHelperMesgFormat ";
		List<String> mesgFormatList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("MESG_FRMT_NAME");
			}
		});
		LOGGGER.debug("messages formats names list :: " + (mesgFormatList != null ? mesgFormatList.size() : "empty"));
		return mesgFormatList;
	}

	@Override
	public void addMesgFormat(String mesgFormat) {
		LOGGGER.debug("Adding new Mesg Format Helper :: " + mesgFormat);
		String queryString = "insert into ldHelperMesgFormat values ('" + mesgFormat + "')";
		jdbcTemplate.update(queryString);
	}

	@Override
	public void insertIntoRtext(TextPart text, boolean isPartDB) {
		String insertString = null;
		insertString = "INSERT INTO RTEXT (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,TEXT_TOKEN,TEXT_DATA_BLOCK_LEN,X_TEXT_CHECKSUM,TEXT_DATA_LAST,TEXT_DATA_BLOCK,TEXT_SWIFT_BLOCK_5,TEXT_SWIFT_PROMPTED" + ((isPartDB) ? ",X_CREA_DATE_TIME_MESG" : "")
				+ ",TEXT_SWIFT_BLOCK_U)" + "" + ((isPartDB) ? "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)" : "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		if (isPartDB) {
			jdbcTemplate.update(insertString, text.getId().getAid(), text.getId().getTextSUmidl(), text.getId().getTextSUmidh(), 0, new BigDecimal(text.getTextDataBlock().length()), 0, 0, text.getTextDataBlock(), null, null,
					text.getMesgCreaDateTime(), null);
		} else {

			jdbcTemplate.update(insertString, text.getId().getAid(), text.getId().getTextSUmidl(), text.getId().getTextSUmidh(), 0, new BigDecimal(text.getTextDataBlock().length()), 0, 0, text.getTextDataBlock(), null, null, null);
		}
		LOGGGER.debug("Insert new record on :: RTEXT ");

	}

	@Override
	public List<String> getMesgNature() {
		LOGGGER.debug("fetching mesages natures");
		String queryString = "select MESG_NATURE from ldHelperMesgNature";
		List<String> mesgNatureList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("MESG_NATURE");
			}
		});
		LOGGGER.debug("messages nature list :: " + (mesgNatureList != null ? mesgNatureList.size() : "empty"));
		return mesgNatureList;
	}

	public boolean isPartitionedDatabase() {

		if (!(getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE)) {
			return false;
		}

		LOGGGER.debug("checking if database is partitioned");
		String queryString = "select count(*) cnt from all_tab_columns where  COLUMN_NAME='X_CREA_DATE_TIME_MESG' and table_name='RINST'";

		Integer count = jdbcTemplate.query(queryString, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getInt("cnt");
				}
				return null;
			}
		});

		boolean isPartitioned = count == 1;
		if (isPartitioned) {
			LOGGGER.debug("database partitioning is enabled");
		} else {
			LOGGGER.debug("database partitioning is not enabled");
		}

		return isPartitioned;
	}

	@Override
	public void addMesgNature(String mesgNature) {
		LOGGGER.debug("adding new message nature :: " + mesgNature);
		String queryString = "insert into ldHelperMesgNature values ('" + mesgNature + "')";
		jdbcTemplate.update(queryString);
	}

	@Override
	public List<String> getMesgQualifier() {
		LOGGGER.debug("fetching qualifers names");
		String queryString = "select QUALIFIER_NAME from ldHelperMesgQualifier";
		List<String> mesgQualifierList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("QUALIFIER_NAME");
			}
		});
		LOGGGER.debug("qualifers names list :: " + (mesgQualifierList != null ? mesgQualifierList.size() : "empty"));
		return mesgQualifierList;
	}

	@Override
	public void addMesgQualifier(String mesgQualifier) {
		LOGGGER.debug("adding new message qualifer :: " + mesgQualifier);
		String queryString = "insert into ldHelperMesgQualifier values ('" + mesgQualifier + "')";
		jdbcTemplate.update(queryString);

	}

	@Override
	public List<String> getMesgUnitName() {
		LOGGGER.debug("fetching message units names ");
		String queryString = "select INST_UNIT_NAME from ldHelperUnitName";
		List<String> untiNameList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("INST_UNIT_NAME");
			}
		});
		LOGGGER.debug("units names list :: " + (untiNameList != null ? untiNameList.size() : "empty"));
		return untiNameList;
	}

	@Override
	public void addMesgUnitName(String unitName) {
		LOGGGER.debug("adding new message unit name :: " + unitName);
		String queryString = "insert into ldHelperUnitName values ('" + unitName + "')";
		jdbcTemplate.update(queryString);

	}

	@Override
	public List<String> getMesgRPName() {
		LOGGGER.debug("fetching message RP Names ");
		String queryString = "select INST_RP_NAME from ldHelperRPName";
		List<String> rpNameList = (List<String>) jdbcTemplate.query(queryString, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("INST_RP_NAME");
			}
		});
		LOGGGER.debug("RP names list :: " + (rpNameList != null ? rpNameList.size() : "empty"));
		return rpNameList;
	}

	@Override
	public void addMesgRPName(String rpName) {
		LOGGGER.debug("adding new message rp name :: " + rpName);
		String queryString = "insert into ldHelperRPName values ('" + rpName + "')";
		jdbcTemplate.update(queryString);
	}

	@Override
	public void updateMessageStatus(BigDecimal id, MessageStatus status, String aid) {
		String updateString = "UPDATE LDPROCESSINGROWS SET STATUS = ? WHERE ID = ? AND AID = ?";
		jdbcTemplate.update(updateString, status.ordinal(), id, aid);
		LOGGGER.debug(String.format("Change the status of current parsing row :: Message Id :: %s to [%d:%s]", id.toPlainString(), status.ordinal(), status.toString()));
	}

	@Override
	public void updateMessageStatusForMQ(BigDecimal id, MessageStatus status, String aid) {
		String updateString = "UPDATE LDPROCESSINGMQROWS SET STATUS = ? WHERE  AID = ? and ID = ?";
		jdbcTemplate.update(updateString, status.ordinal(), aid, id);
		LOGGGER.debug(String.format("Change the status of current parsing row :: Message Id :: %s to [%d:%s]", id.toPlainString(), status.ordinal(), status.toString()));
	}

	public void addNewStatistics(UpdatedMessage message, DBType dbType) {
		try {
			String insertString = null;
			if (appConfigBean.getDatabaseType().equals(DBType.ORACLE)) {
				insertString = "INSERT INTO LDUPDATESTATISTICS VALUES(LDUPDATESTATISTICS_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			} else {
				insertString = "INSERT INTO LDUPDATESTATISTICS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
			Date now = new Date();
			jdbcTemplate.update(insertString, message.getID(), now, now, message.getTotalTime(), message.getOverrun(), message.getNewMsgCount(), message.getUpdateMsgCount(), message.getOnTimeCount(), message.getNotifiedMsgCount(), 0,
					message.getErrorCount(), message.getWarningCount(), message.getFailedCount(), message.getJrnlMsgCount(), message.getOrigin(), 0, 0, 0);
			LOGGGER.debug("Insert new record on :: LDUPDATESTATISTICS ");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void addProcessingRows(List<BigDecimal> ids, String aid) {
		String insertProcessingRowsStatement = "INSERT INTO LDPROCESSINGROWS(ID,CREATION_DATE,AID) VALUES(?,?,?)";
		// Prepare for batch insert
		LOGGGER.debug("Preparing for batch insert");
		List<Object[]> valuesList = new ArrayList<Object[]>();
		Date date = new Date(); // creationDate
		Object[] rowValues = null;
		for (BigDecimal id : ids) {
			rowValues = new Object[] { id, date, aid };
			valuesList.add(rowValues);
		}
		jdbcTemplate.batchUpdate(insertProcessingRowsStatement, valuesList);
		LOGGGER.debug("Batch update Finished");
	}

	@Override
	public abstract void addProcessingMQRows(List<LoaderMessage> messagesList, String aid);

	@Override
	public void removeDatabaseSourcedProcessingRows(List<LoaderMessage> list, String aid) {

		String deleteProcessingRowsStatements = "DELETE FROM LDPROCESSINGROWS WHERE AID = ? AND ID = ? AND STATUS=0";

		LOGGGER.debug("Preparing Eligible ID for removing from DB");

		Object[] argsPerRow;
		List<Object[]> batchRows = new ArrayList<Object[]>();
		for (LoaderMessage loaderMessage : list) {
			argsPerRow = new Object[] { aid, loaderMessage.getMessageSequenceNo() };
			batchRows.add(argsPerRow);
		}

		jdbcTemplate.batchUpdate(deleteProcessingRowsStatements, batchRows);

		/*
		 * No need to iterate over list of wrapper classes or BigDecimal list , toString will return always [comma separated values]
		 */
		// String idsString = ids.toString();

		// after '[' to ']'
		// idsString = idsString.substring(1, idsString.length() - 1);
		// deleteProcessingRowsStatements = deleteProcessingRowsStatements.replace("?", idsString);
		// jdbcTemplate.update(deleteProcessingRowsStatements, aid, idsString);
		// LOGGGER.debug("Removing the following IDS :: " + idsString.substring(0, idsString.length() - 1));
	}

	public Long getLastSequenceMqProcessing() {
		LOGGGER.debug("Fetching last Seq processing MQ row from :: LDPROCESSINGMQROWS");
		String lastIdProcessed = "";
		if (appConfigBean.getDatabaseType().equals(DBType.ORACLE)) {
			lastIdProcessed = "select PRCESSINGMQROWS_ID.nextval S FROM dual";
			Long lastId = jdbcTemplate.queryForLong(lastIdProcessed);
			return lastId - 1;
		} else {
			lastIdProcessed = "SELECT IDENT_CURRENT('LDPROCESSINGMQROWS') as id";
			Long lastId = jdbcTemplate.queryForLong(lastIdProcessed);
			return lastId;
		}
	}

	@Override
	public void removeMQSourcedProcessingRows(List<LoaderMessage> list, String aid, Long lastId) {

		String deleteProcessingRowsStatements = "DELETE FROM LDPROCESSINGMQROWS WHERE AID = ? AND ID = ?  And STATUS=0";
		for (LoaderMessage loaderMessage : list) {
			jdbcTemplate.update(deleteProcessingRowsStatements, new Object[] { appConfigBean.getSAAAid(), loaderMessage.getMessageSequenceNo() });
		}
		LOGGGER.debug("Preparing Eligible ID for removing from DB");
	}

	@Override
	public LoaderConnection getLastLoaderConnection() {
		LOGGGER.debug("Fetching last processing row from :: LDSETTINGS");
		String lastProcessedQuery = "SELECT LD_LAST_PROCESSED_SEQ_NO,LAST_UMIDL,LAST_UMIDH FROM LDSETTINGS WHERE AID = ?";
		LoaderConnection loaderConnection = jdbcTemplate.query(lastProcessedQuery, new Object[] { appConfigBean.getSAAAid() }, new ResultSetExtractor<LoaderConnection>() {
			@Override
			public LoaderConnection extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LoaderConnection loaderConnection = new LoaderConnection();
					loaderConnection.setAid(Long.parseLong(appConfigBean.getSAAAid()));
					loaderConnection.setLastProcessedSequenceNo(rs.getBigDecimal("LD_LAST_PROCESSED_SEQ_NO"));
					loaderConnection.setLastUmidl(rs.getLong("LAST_UMIDL"));
					loaderConnection.setLastUmidh(rs.getLong("LAST_UMIDH"));
					return loaderConnection;
				}
				return null;
			}
		});
		return loaderConnection;
	}

	@Override
	public BigDecimal getLastSeq() {
		LOGGGER.debug("Fetching last processing row from :: LDSETTINGS");
		String lastProcessedQuery = "SELECT LD_LAST_PROCESSED_SEQ_NO FROM LDSETTINGS WHERE AID = ? for update";
		LoaderConnection loaderConnection = jdbcTemplate.query(lastProcessedQuery, new Object[] { appConfigBean.getSAAAid() }, new ResultSetExtractor<LoaderConnection>() {
			@Override
			public LoaderConnection extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LoaderConnection loaderConnection = new LoaderConnection();
					loaderConnection.setLastProcessedSequenceNo(rs.getBigDecimal("LD_LAST_PROCESSED_SEQ_NO"));

					return loaderConnection;
				}
				return null;
			}
		});
		return loaderConnection.getLastProcessedSequenceNo();
	}

	@Override
	public void updateLastProcessedRow(LoaderConnection loaderConnection, Connection lockCon) throws Exception {
		String updateLastProcessedStatement = "UPDATE LDSETTINGS SET LD_LAST_PROCESSED_SEQ_NO = ?, LAST_UMIDL = ?, LAST_UMIDH = ? WHERE AID = ?";
		PreparedStatement preparedStatement = lockCon.prepareStatement(updateLastProcessedStatement);
		preparedStatement.setBigDecimal(1, loaderConnection.getLastProcessedSequenceNo());
		preparedStatement.setLong(2, loaderConnection.getLastUmidl());
		preparedStatement.setLong(3, loaderConnection.getLastUmidh());
		preparedStatement.setString(4, appConfigBean.getSAAAid());
		preparedStatement.executeUpdate();
		lockCon.commit();
		LOGGGER.info("Last Processed No for AID :: " + appConfigBean.getSAAAid() + " :: update to :: " + loaderConnection.getLastProcessedSequenceNo() + " :: last UMIDL :: " + loaderConnection.getLastUmidl() + " :: Last UMIDH :: "
				+ loaderConnection.getLastUmidh());
	}

	@Override
	public void updateLastProcessedRow(LoaderConnection loaderConnection) {
		String updateLastProcessedStatement = "UPDATE LDSETTINGS SET LD_LAST_PROCESSED_SEQ_NO = ?, LAST_UMIDL = ?, LAST_UMIDH = ? WHERE AID = ?";

		jdbcTemplate.update(updateLastProcessedStatement, loaderConnection.getLastProcessedSequenceNo(), loaderConnection.getLastUmidl(), loaderConnection.getLastUmidh(), appConfigBean.getSAAAid());
		LOGGGER.info("Last Processed No for AID :: " + appConfigBean.getSAAAid() + " :: update to :: " + loaderConnection.getLastProcessedSequenceNo() + " :: last UMIDL :: " + loaderConnection.getLastUmidl() + " :: Last UMIDH :: "
				+ loaderConnection.getLastUmidh());
	}

	@Override
	public void updateLastProcessedMQRow(LoaderConnection loaderConnection) {
		String updateLastProcessedStatement = "UPDATE LDSETTINGS SET   LAST_UMIDL = ?, LAST_UMIDH = ? WHERE AID = ?";
		jdbcTemplate.update(updateLastProcessedStatement, loaderConnection.getLastUmidl(), loaderConnection.getLastUmidh(), appConfigBean.getSAAAid());

		LOGGGER.info("Last Processed No for AID :: " + appConfigBean.getSAAAid() + " :: last UMIDL :: " + loaderConnection.getLastUmidl() + " :: Last UMIDH :: " + loaderConnection.getLastUmidh());
	}

	@Override
	public void updateTest(String xml, long umidl) {
		String updateLastProcessedStatement = "UPDATE rxmltext SET   XMLTEXT_DATA = ? WHERE XMLTEXT_S_UMIDL = ?";
		jdbcTemplate.update(updateLastProcessedStatement, xml, umidl, appConfigBean.getSAAAid());

	}

	@Override
	public List<BigDecimal> restoreProcessingRows(String aid) {
		String restoreProcessingRowsQuery = "SELECT ID FROM LDPROCESSINGROWS WHERE aid=? and STATUS = 0 ORDER BY ID ASC";
		List<BigDecimal> processingIds = (List<BigDecimal>) jdbcTemplate.query(restoreProcessingRowsQuery, new Object[] { aid }, new RowMapper<BigDecimal>() {
			public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getBigDecimal("ID");
			}
		});
		return processingIds;
	}

	@Override
	public List<LoaderMessage> restoreMQProcessingRows(String aid) {
		String restoreProcessingRowsQuery = "SELECT ID,PROCESSING_DATA,MESSAGE_HISTORY FROM LDPROCESSINGMQROWS WHERE AID=? and STATUS = 0 ORDER BY ID ASC";
		List<LoaderMessage> processingMQRows = (List<LoaderMessage>) jdbcTemplate.query(restoreProcessingRowsQuery, new Object[] { aid }, new RowMapper<LoaderMessage>() {
			public LoaderMessage mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				LoaderMessage loaderMessage = new LoaderMessage();

				Clob rowdata = resultSet.getClob("PROCESSING_DATA");
				Clob rowHistory = resultSet.getClob("MESSAGE_HISTORY");
				Long msgID = resultSet.getLong("ID");
				if (rowdata != null) {
					loaderMessage.setMessageSource(MessageSource.XML);
					loaderMessage.setRowData(LoaderUtils.convertClob2String(rowdata));
					loaderMessage.setRowHistory(LoaderUtils.convertClob2String(rowHistory));
					if (LoaderUtils.convertClob2String(rowdata).contains("DataPDU")) {
						loaderMessage.setMesgType("XML");
					} else if (LoaderUtils.convertClob2String(rowdata).contains("<AMP")) {
						loaderMessage.setMesgType("AMH");
					} else {
						loaderMessage.setMesgType("103");
					}

					loaderMessage.setMessageSequenceNo(new BigDecimal(msgID));
				}

				return loaderMessage;
			}
		});
		return processingMQRows;
	}

	@Override
	public void insertIntoErrorTabel(String message) {
		String insertQuery = "INSERT INTO errorTable(invalidMessage) VALUES(?)";
		jdbcTemplate.update(insertQuery, message);
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	@Override
	public ArrayList<String> getXSDIdentifier() {
		// TODO Auto-generated method stub
		String sql = "SELECT identifier_value FROM xmlTypes WHERE xml_type='SEPA' and type_status = '1'";
		ArrayList<String> xsdlist = new ArrayList<String>();
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		for (Map row : rows) {
			String xsdName = ((String) row.get("identifier_value"));
			xsdlist.add(xsdName);
		}
		return xsdlist;
	}

	@Override
	public long getMxseqAppeRecordId() {
		Long mesgID = jdbcTemplate.queryForLong("select seq_appe_record_id.NEXTVAL from dual ");

		return mesgID;
	}

}
