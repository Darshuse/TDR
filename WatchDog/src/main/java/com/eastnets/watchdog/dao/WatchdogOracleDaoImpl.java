package com.eastnets.watchdog.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.resultbeans.LdErrors;

public class WatchdogOracleDaoImpl extends WatchdogDaoImpl {

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Override
	public void saveWDMessageRequestResult(List<WDMessageRequestResult> messageRequestsResults) {
		boolean partdb = watchdogConfiguration.isPartitioned();
		// X_CREA_DATE_TIME_MESG
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDUSERREQUESTRESULT (ID, AID, APPE_S_UMIDL, APPE_S_UMIDH, APPE_INST_NUM, APPE_DATE_TIME, APPE_SEQ_NBR, REQUEST_ID, MESG_CREA_OPER_NICKNAME, MESG_CREA_DATE_TIME , MESG_RECEIVER_SWIFT_ADDRESS, MESG_SENDER_SWIFT_ADDRESS, MESG_SUB_FORMAT, MESG_TYPE, MESG_TRN_REF, X_FIN_CCY, X_FIN_AMOUNT, X_FIN_VALUE_DATE, APPE_SESSION_HOLDER, APPE_CREA_MPFN_NAME, APPE_SESSION_NBR, APPE_SEQUENCE_NBR, INSERT_TIME,"
				+ " DESCRIPTION, USERNAME, USERGROUP, PROCESSED, IDENTIFIER, REQUESTOR_DN, RESPONDER_DN ";
		if (!partdb) {
			insertStatemnt += ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt += ",X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		}

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDMessageRequestResult wdMessageRequestResult = messageRequestsResults.get(index);
				Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select WDUSERREQUESTRESULT_ID.nextval from dual ");
				preparedStatement.setBigDecimal(1, (BigDecimal) queryForMap.get("NEXTVAL"));
				preparedStatement.setLong(2, wdMessageRequestResult.getAid());
				preparedStatement.setLong(3, wdMessageRequestResult.getAppeUmidl());
				preparedStatement.setLong(4, wdMessageRequestResult.getAppeUmidh());
				preparedStatement.setLong(5, wdMessageRequestResult.getAppeInstNum());
				preparedStatement.setDate(6, new java.sql.Date(wdMessageRequestResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(7, wdMessageRequestResult.getAppeSeqNumber());
				preparedStatement.setLong(8, wdMessageRequestResult.getRequestId());
				preparedStatement.setString(9, wdMessageRequestResult.getMesgCreateOperNickname());
				preparedStatement.setDate(10, new java.sql.Date(wdMessageRequestResult.getMesgCreateDateTime().getTime()));
				preparedStatement.setString(11, wdMessageRequestResult.getMesgReceiverSwiftAddress());
				preparedStatement.setString(12, wdMessageRequestResult.getMesgSenderSwiftAddress());
				preparedStatement.setString(13, wdMessageRequestResult.getMesgSubFormat());
				preparedStatement.setString(14, wdMessageRequestResult.getMesgType());
				preparedStatement.setString(15, wdMessageRequestResult.getMesgTrnRef());
				preparedStatement.setString(16, wdMessageRequestResult.getxFinCcy());
				preparedStatement.setBigDecimal(17, wdMessageRequestResult.getxFinAmount());
				preparedStatement.setDate(18, new java.sql.Date(wdMessageRequestResult.getxFinValueDate().getTime()));
				preparedStatement.setString(19, wdMessageRequestResult.getAppeSessionHolder());
				preparedStatement.setString(20, wdMessageRequestResult.getAppeCreaMPFNName());
				preparedStatement.setBigDecimal(21, wdMessageRequestResult.getAppeSessionNumber());
				preparedStatement.setBigDecimal(22, wdMessageRequestResult.getAppeSequenceNumber());
				preparedStatement.setDate(23, new java.sql.Date(wdMessageRequestResult.getInsertTime().getTime()));
				preparedStatement.setString(24, wdMessageRequestResult.getDescription());
				preparedStatement.setString(25, wdMessageRequestResult.getUsername());
				preparedStatement.setLong(26, wdMessageRequestResult.getUserGroup());
				preparedStatement.setLong(27, wdMessageRequestResult.getProcessed());
				preparedStatement.setString(28, wdMessageRequestResult.getIdentifier());
				preparedStatement.setString(29, wdMessageRequestResult.getRequestorDN());
				preparedStatement.setString(30, wdMessageRequestResult.getResponderDN());
				if (partdb) {
					preparedStatement.setDate(31, new java.sql.Date(wdMessageRequestResult.getMesgCreateDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return messageRequestsResults.size();
			}
		});

	}

	@Override
	public void saveWDEmailNotification(List<WDEmailNotification> emailNotifications) {
		boolean partdb = watchdogConfiguration.isPartitioned();
		// X_CREA_DATE_TIME_MESG
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDEMAILNOTIFICATION ( ID,DESCRIPTION,USERNAME,AID,UMIDL,UMIDH,MSGTYPE,WDID,PROCESS_STATUS ";
		if (!partdb) {
			insertStatemnt += ") VALUES (?,?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt += ",MESG_CREA_DATE_TIME) VALUES (?,?,?,?,?,?,?,?,?,?)";
		}

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDEmailNotification wdEmailNotification = emailNotifications.get(index);
				Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select WDEMAILNOTIFICATION_ID.nextval from dual ");
				preparedStatement.setBigDecimal(1, (BigDecimal) queryForMap.get("NEXTVAL"));
				preparedStatement.setString(2, wdEmailNotification.getDescription());
				preparedStatement.setString(3, wdEmailNotification.getUsername());
				preparedStatement.setLong(4, wdEmailNotification.getAid());
				preparedStatement.setLong(5, wdEmailNotification.getUmidl());
				preparedStatement.setLong(6, wdEmailNotification.getUmidh());
				preparedStatement.setString(7, wdEmailNotification.getMessageType());
				preparedStatement.setLong(8, wdEmailNotification.getWdID());
				preparedStatement.setLong(9, 0);
				if (partdb) {
					preparedStatement.setDate(10, new java.sql.Date(wdEmailNotification.getMesgCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return emailNotifications.size();
			}
		});

	}

	@Override
	public void saveNackResults(List<WDNackResult> nackResults) {
		boolean partdb = watchdogConfiguration.isPartitioned();
		// X_CREA_DATE_TIME_MESG
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDNACKRESULT ( SYSID,AID,APPE_S_UMIDH,APPE_S_UMIDL,APPE_INST_NUM,APPE_DATE_TIME,APPE_SEQ_NBR,INSERT_TIME";
		if (!partdb) {
			insertStatemnt += ") VALUES (?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt += ",X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?,?,?)";
		}

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDNackResult wdNackResult = nackResults.get(index);
				Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select WDNACKRESULT_ID.nextval from dual ");
				preparedStatement.setBigDecimal(1, (BigDecimal) queryForMap.get("NEXTVAL"));
				preparedStatement.setLong(2, wdNackResult.getAid());
				preparedStatement.setLong(3, wdNackResult.getAppeSUmidh());
				preparedStatement.setLong(4, wdNackResult.getAppeSUmidl());
				preparedStatement.setLong(5, wdNackResult.getAppeInstNum());
				preparedStatement.setDate(6, new java.sql.Date(wdNackResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(7, wdNackResult.getAppeSeqNbr());
				preparedStatement.setDate(8, new java.sql.Date(wdNackResult.getInsertTime().getTime()));
				if (partdb) {
					preparedStatement.setDate(9, new java.sql.Date(wdNackResult.getTempCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return nackResults.size();
			}
		});

	}

	@Override
	public void savePossibleDuplicates(List<WDPossibleDuplicateResult> possibleDuplicates) {
		boolean partdb = watchdogConfiguration.isPartitioned();
		// X_CREA_DATE_TIME_MESG
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDPOSSIBLEDUPRESULT ( SYSID,AID,APPE_S_UMIDH,APPE_S_UMIDL,APPE_INST_NUM,APPE_DATE_TIME,APPE_SEQ_NBR,MESG_USER_ISSUED_AS_PDE,MESG_POSSIBLE_DUP_CREATION,INSERT_TIME ";
		if (!partdb) {
			insertStatemnt += ") VALUES (?,?,?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt += ",X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		}

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDPossibleDuplicateResult wdPossibleDuplicateResult = possibleDuplicates.get(index);
				Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select WDPOSSIBLEDUPRESULT_ID.nextval from dual ");
				preparedStatement.setBigDecimal(1, (BigDecimal) queryForMap.get("NEXTVAL"));
				preparedStatement.setLong(2, wdPossibleDuplicateResult.getAid());
				preparedStatement.setLong(3, wdPossibleDuplicateResult.getAppeSUmidh());
				preparedStatement.setLong(4, wdPossibleDuplicateResult.getAppeSUmidl());
				preparedStatement.setLong(5, wdPossibleDuplicateResult.getAppeInstNum());
				preparedStatement.setDate(6, new java.sql.Date(wdPossibleDuplicateResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(7, wdPossibleDuplicateResult.getAppeSeqNbr());
				preparedStatement.setBigDecimal(8, wdPossibleDuplicateResult.getMesgUserIssuedAsPDE());
				preparedStatement.setString(9, wdPossibleDuplicateResult.getMesgPossibleDupCreation());
				preparedStatement.setDate(10, new java.sql.Date(wdPossibleDuplicateResult.getInsertTime().getTime()));
				if (partdb) {
					preparedStatement.setDate(11, new java.sql.Date(wdPossibleDuplicateResult.getTempCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return possibleDuplicates.size();
			}
		});
	}

	@Override
	public void saveEventResults(List<WDEventRequestResult> eventRequestsResults) {
		boolean partdb = watchdogConfiguration.isPartitioned();
		// X_CREA_DATE_TIME_MESG
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDEVENTREQUESTRESULT ( ID,AID,JRNL_REV_DATE_TIME,JRNL_SEQ_NBR,REQUEST_ID,DESCRIPTION,USERNAME,PROCESSED,INSERT_TIME ";
		if (!partdb) {
			insertStatemnt += ") VALUES (?,?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt += ",jrnl_date_time) VALUES (?,?,?,?,?,?,?,?,?,?)";
		}

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDEventRequestResult wdEventRequestResult = eventRequestsResults.get(index);
				Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select WDEVENTREQUESTRESULT_ID.nextval from dual ");
				preparedStatement.setBigDecimal(1, (BigDecimal) queryForMap.get("NEXTVAL"));
				preparedStatement.setLong(2, wdEventRequestResult.getAid());
				preparedStatement.setLong(3, wdEventRequestResult.getJrnlRevDateTime());
				preparedStatement.setLong(4, wdEventRequestResult.getJrnlSeqNumber());
				preparedStatement.setInt(5, wdEventRequestResult.getRequestId());
				preparedStatement.setString(6, wdEventRequestResult.getDescription());
				preparedStatement.setString(7, wdEventRequestResult.getUsername());
				preparedStatement.setInt(8, wdEventRequestResult.getProcessed());
				preparedStatement.setDate(9, new java.sql.Date(wdEventRequestResult.getInsertTime().getTime()));
				if (partdb) {
					preparedStatement.setDate(10, new java.sql.Date(wdEventRequestResult.getTempCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return eventRequestsResults.size();
			}
		});

	}

	@Override
	public void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2) {

		LdErrors ldErrors = new LdErrors(errName, errorDate, errorEvel, errorModule, errorMesage1, errorMessage2);
		String sql = "select LDERRORS_ID.NEXTVAL from dual";
		Long id = jdbcTemplate.queryForObject(sql, Long.class);

		String insertStatment = "INSERT INTO ldErrors(ErrID,ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertStatment, id, ldErrors.getErrName(), LocalDateTime.now(), ldErrors.getErrorEvel(), ldErrors.getErrorModule(), ldErrors.getErrorMesage1(), ldErrors.getErrorMessage2());

	}

}
