package com.eastnets.watchdog.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.watchdog.resultbeans.LdErrors;

public class WatchdogSQLDaoImpl extends WatchdogDaoImpl {

	@Override
	public void saveWDMessageRequestResult(List<WDMessageRequestResult> messageRequestsResults) {
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDUSERREQUESTRESULT ( AID, APPE_S_UMIDL, APPE_S_UMIDH, APPE_INST_NUM, APPE_DATE_TIME, APPE_SEQ_NBR, REQUEST_ID, MESG_CREA_OPER_NICKNAME, MESG_CREA_DATE_TIME , MESG_RECEIVER_SWIFT_ADDRESS, MESG_SENDER_SWIFT_ADDRESS, MESG_SUB_FORMAT, MESG_TYPE, MESG_TRN_REF, X_FIN_CCY, X_FIN_AMOUNT, X_FIN_VALUE_DATE, APPE_SESSION_HOLDER, APPE_CREA_MPFN_NAME, APPE_SESSION_NBR, APPE_SEQUENCE_NBR, INSERT_TIME,"
				+ " DESCRIPTION, USERNAME, USERGROUP, PROCESSED, IDENTIFIER, REQUESTOR_DN, RESPONDER_DN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDMessageRequestResult wdMessageRequestResult = messageRequestsResults.get(index);
				preparedStatement.setLong(1, wdMessageRequestResult.getAid());
				preparedStatement.setLong(2, wdMessageRequestResult.getAppeUmidl());
				preparedStatement.setLong(3, wdMessageRequestResult.getAppeUmidh());
				preparedStatement.setLong(4, wdMessageRequestResult.getAppeInstNum());
				preparedStatement.setDate(5, new java.sql.Date(wdMessageRequestResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(6, wdMessageRequestResult.getAppeSeqNumber());
				preparedStatement.setLong(7, wdMessageRequestResult.getRequestId());
				preparedStatement.setString(8, wdMessageRequestResult.getMesgCreateOperNickname());
				preparedStatement.setDate(9, new java.sql.Date(wdMessageRequestResult.getMesgCreateDateTime().getTime()));
				preparedStatement.setString(10, wdMessageRequestResult.getMesgReceiverSwiftAddress());
				preparedStatement.setString(11, wdMessageRequestResult.getMesgSenderSwiftAddress());
				preparedStatement.setString(12, wdMessageRequestResult.getMesgSubFormat());
				preparedStatement.setString(13, wdMessageRequestResult.getMesgType());
				preparedStatement.setString(14, wdMessageRequestResult.getMesgTrnRef());
				preparedStatement.setString(15, wdMessageRequestResult.getxFinCcy());
				preparedStatement.setBigDecimal(16, wdMessageRequestResult.getxFinAmount());
				preparedStatement.setDate(17, new java.sql.Date(wdMessageRequestResult.getxFinValueDate().getTime()));
				preparedStatement.setString(18, wdMessageRequestResult.getAppeSessionHolder());
				preparedStatement.setString(19, wdMessageRequestResult.getAppeCreaMPFNName());
				preparedStatement.setBigDecimal(20, wdMessageRequestResult.getAppeSessionNumber());
				preparedStatement.setBigDecimal(21, wdMessageRequestResult.getAppeSequenceNumber());
				preparedStatement.setDate(22, new java.sql.Date(wdMessageRequestResult.getInsertTime().getTime()));
				preparedStatement.setString(23, wdMessageRequestResult.getDescription());
				preparedStatement.setString(24, wdMessageRequestResult.getUsername());
				preparedStatement.setLong(25, wdMessageRequestResult.getUserGroup());
				preparedStatement.setLong(26, wdMessageRequestResult.getProcessed());
				preparedStatement.setString(27, wdMessageRequestResult.getIdentifier());
				preparedStatement.setString(28, wdMessageRequestResult.getRequestorDN());
				preparedStatement.setString(29, wdMessageRequestResult.getResponderDN());
			}

			@Override
			public int getBatchSize() {
				return messageRequestsResults.size();
			}
		});

	}

	@Override
	public void saveWDEmailNotification(List<WDEmailNotification> emailNotifications) {
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDEMAILNOTIFICATION (DESCRIPTION,USERNAME,AID,UMIDL,UMIDH,MSGTYPE,WDID,PROCESS_STATUS ) VALUES (?,?,?,?,?,?,?,?)";

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDEmailNotification wdEmailNotification = emailNotifications.get(index);
				preparedStatement.setString(1, wdEmailNotification.getDescription());
				preparedStatement.setString(2, wdEmailNotification.getUsername());
				preparedStatement.setLong(3, wdEmailNotification.getAid());
				preparedStatement.setLong(4, wdEmailNotification.getUmidl());
				preparedStatement.setLong(5, wdEmailNotification.getUmidh());
				preparedStatement.setString(6, wdEmailNotification.getMessageType());
				preparedStatement.setLong(7, wdEmailNotification.getWdID());
				preparedStatement.setLong(8, 0);
			}

			@Override
			public int getBatchSize() {
				return emailNotifications.size();
			}
		});

	}

	@Override
	public void saveNackResults(List<WDNackResult> nackResults) {
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDNACKRESULT (AID,APPE_S_UMIDH,APPE_S_UMIDL,APPE_INST_NUM,APPE_DATE_TIME,APPE_SEQ_NBR,INSERT_TIME ) VALUES (?,?,?,?,?,?,?)";

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDNackResult wdNackResult = nackResults.get(index);
				preparedStatement.setLong(1, wdNackResult.getAid());
				preparedStatement.setLong(2, wdNackResult.getAppeSUmidh());
				preparedStatement.setLong(3, wdNackResult.getAppeSUmidl());
				preparedStatement.setLong(4, wdNackResult.getAppeInstNum());
				preparedStatement.setDate(5, new java.sql.Date(wdNackResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(6, wdNackResult.getAppeSeqNbr());
				preparedStatement.setDate(7, new java.sql.Date(wdNackResult.getInsertTime().getTime()));

			}

			@Override
			public int getBatchSize() {
				return nackResults.size();
			}
		});

	}

	@Override
	public void savePossibleDuplicates(List<WDPossibleDuplicateResult> possibleDuplicates) {
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDPOSSIBLEDUPRESULT (AID,APPE_S_UMIDH,APPE_S_UMIDL,APPE_INST_NUM,APPE_DATE_TIME,APPE_SEQ_NBR,MESG_USER_ISSUED_AS_PDE,MESG_POSSIBLE_DUP_CREATION,INSERT_TIME ) VALUES (?,?,?,?,?,?,?,?,?) ";

		//
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDPossibleDuplicateResult wdPossibleDuplicateResult = possibleDuplicates.get(index);
				preparedStatement.setLong(1, wdPossibleDuplicateResult.getAid());
				preparedStatement.setLong(2, wdPossibleDuplicateResult.getAppeSUmidh());
				preparedStatement.setLong(3, wdPossibleDuplicateResult.getAppeSUmidl());
				preparedStatement.setLong(4, wdPossibleDuplicateResult.getAppeInstNum());
				preparedStatement.setDate(5, new java.sql.Date(wdPossibleDuplicateResult.getAppeDateTime().getTime()));
				preparedStatement.setLong(6, wdPossibleDuplicateResult.getAppeSeqNbr());
				preparedStatement.setBigDecimal(7, wdPossibleDuplicateResult.getMesgUserIssuedAsPDE());
				preparedStatement.setString(8, wdPossibleDuplicateResult.getMesgPossibleDupCreation());
				preparedStatement.setDate(9, new java.sql.Date(wdPossibleDuplicateResult.getInsertTime().getTime()));
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
		insertStatemnt = "INSERT INTO WDEVENTREQUESTRESULT (AID,JRNL_REV_DATE_TIME,JRNL_SEQ_NBR,REQUEST_ID,DESCRIPTION,USERNAME,PROCESSED,INSERT_TIME) VALUES (?,?,?,?,?,?,?,?) ";

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDEventRequestResult wdEventRequestResult = eventRequestsResults.get(index);
				preparedStatement.setLong(1, wdEventRequestResult.getAid());
				preparedStatement.setLong(2, wdEventRequestResult.getJrnlRevDateTime());
				preparedStatement.setLong(3, wdEventRequestResult.getJrnlSeqNumber());
				preparedStatement.setInt(4, wdEventRequestResult.getRequestId());
				preparedStatement.setString(5, wdEventRequestResult.getDescription());
				preparedStatement.setString(6, wdEventRequestResult.getUsername());
				preparedStatement.setInt(7, wdEventRequestResult.getProcessed());
				preparedStatement.setDate(8, new java.sql.Date(wdEventRequestResult.getInsertTime().getTime()));
				if (partdb) {
					preparedStatement.setDate(9, new java.sql.Date(wdEventRequestResult.getTempCreaDateTime().getTime()));
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
		String insertStatment = "INSERT INTO dbo.ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertStatment, ldErrors.getErrName(), LocalDateTime.now(), ldErrors.getErrorEvel(), ldErrors.getErrorModule(), ldErrors.getErrorMesage1(), ldErrors.getErrorMessage2());

	}

}
