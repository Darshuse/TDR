package com.eastnets.watchdog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDEmailNotification;
import com.eastnets.entities.WDEventRequestResult;
import com.eastnets.entities.WDMessageRequestResult;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.entities.WDNackResult;
import com.eastnets.entities.WDPossibleDuplicateResult;
import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedLoop;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.textbreak.procedure.CheckRoleProcedure;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.resultbeans.CorrespondentBean;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.resultbeans.User;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;

@Service
public class WatchdogDaoImpl extends WatchdogDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	Connection expandConn = null;

	@Override
	public void deleteWDkeysNonPart(List<AppendixPK> appendixPKs) {
		String deleteStatment = "delete from wdkeys where aid=? and APPE_S_UMIDL=? and APPE_S_UMIDH=? AND APPE_INST_NUM=?";

		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				AppendixPK appendixPK = appendixPKs.get(index);
				preparedStatement.setLong(1, appendixPK.getAid());
				preparedStatement.setLong(2, appendixPK.getAppeSUmidl());
				preparedStatement.setLong(3, appendixPK.getAppeSUmidh());
				preparedStatement.setLong(4, appendixPK.getAppeInstNum());
			}

			@Override
			public int getBatchSize() {
				return appendixPKs.size();
			}
		});

	}

	@Override
	public void deleteWDkeys(List<WDAppeKey> wdAppeKeys, boolean flag) {
		String deleteStatment = "delete from wdkeys where aid=? and APPE_S_UMIDL=? and APPE_S_UMIDH=? AND APPE_INST_NUM=?";

		if (watchdogConfiguration.isPartitioned()) {
			deleteStatment += " and X_CREA_DATE_TIME_MESG = ?";
		}

		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDAppeKey wdAppeKey = wdAppeKeys.get(index);
				AppendixPK appendixPK = wdAppeKey.getAppendixPK();
				preparedStatement.setLong(1, appendixPK.getAid());
				preparedStatement.setLong(2, appendixPK.getAppeSUmidl());
				preparedStatement.setLong(3, appendixPK.getAppeSUmidh());
				preparedStatement.setLong(4, appendixPK.getAppeInstNum());
				if (watchdogConfiguration.isPartitioned()) {
					preparedStatement.setDate(5, new java.sql.Date(wdAppeKey.getMesgCreaDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return wdAppeKeys.size();
			}
		});

	}

	@Override
	public void deleteWDkeys(List<AppendixPK> appendixPKs) {
		String deleteStatment = "delete from wdkeys where aid=? and APPE_S_UMIDL=? and APPE_S_UMIDH=? AND APPE_INST_NUM=?";

		if (watchdogConfiguration.isPartitioned()) {
			deleteStatment += " and X_CREA_DATE_TIME_MESG = ?";
		}

		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				AppendixPK appendixPK = appendixPKs.get(index);
				preparedStatement.setLong(1, appendixPK.getAid());
				preparedStatement.setLong(2, appendixPK.getAppeSUmidl());
				preparedStatement.setLong(3, appendixPK.getAppeSUmidh());
				preparedStatement.setLong(4, appendixPK.getAppeInstNum());
				if (watchdogConfiguration.isPartitioned()) {
					preparedStatement.setDate(5, new java.sql.Date(appendixPK.getMesgCreateDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return appendixPKs.size();
			}
		});

	}

	@Override
	public void deleteWdJrnlKey(List<JrnlPK> jrnlPKs) {
		String deleteStatment = "delete from WDJRNLKEYS where aid=? and JRNL_REV_DATE_TIME=? and JRNL_SEQ_NBR=? ";

		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				JrnlPK jrnlPK = jrnlPKs.get(index);
				preparedStatement.setInt(1, jrnlPK.getAid());
				preparedStatement.setInt(2, jrnlPK.getJrnlRevDateTime());
				preparedStatement.setLong(3, jrnlPK.getJrnlSeqNumber());
			}

			@Override
			public int getBatchSize() {
				return jrnlPKs.size();
			}
		});

	}

	@Override
	public void deleteMessageRequest(List<WDMessageSearchRequest> messageSearchRequests) {
		String deleteStatment = "delete from WDUSERSEARCHPARAMETER where REQUESTID = ?";

		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDMessageSearchRequest messageSearchRequest = messageSearchRequests.get(index);
				preparedStatement.setInt(1, messageSearchRequest.getRequestID());
			}

			@Override
			public int getBatchSize() {
				return messageSearchRequests.size();
			}
		});

	}

	@Override
	public void updateWdJurnalKey(List<WDJrnlKeyBean> matchedJrnlKeys) {
		String deleteStatment = "update WDJRNLKEYS set PROCESS_STATUS=1 where AID= ? and JRNL_REV_DATE_TIME=? and JRNL_SEQ_NBR = ? ";
		jdbcTemplate.batchUpdate(deleteStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDJrnlKeyBean wdJrnlKey = matchedJrnlKeys.get(index);
				preparedStatement.setInt(1, wdJrnlKey.getAid());
				preparedStatement.setInt(2, wdJrnlKey.getJrnlRevDateTime());
				preparedStatement.setLong(3, wdJrnlKey.getJrnlSeqNumber());
			}

			@Override
			public int getBatchSize() {
				return matchedJrnlKeys.size();
			}
		});

	}

	@Override
	public void updateWdAppeKey(List<WDAppeKeyBean> matchedAppeKeys) {

		String updateStatment = "update WDKEYS set PROCESS_STATUS=1 where AID= ? and APPE_S_UMIDL=? and APPE_S_UMIDH = ? ";

		if (watchdogConfiguration.isPartitioned()) {
			updateStatment += " and X_CREA_DATE_TIME_MESG = ?";
		}

		jdbcTemplate.batchUpdate(updateStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDAppeKeyBean wdAppeKeyBean = matchedAppeKeys.get(index);
				preparedStatement.setLong(1, wdAppeKeyBean.getAid());
				preparedStatement.setLong(2, wdAppeKeyBean.getAppeSUmidl());
				preparedStatement.setLong(3, wdAppeKeyBean.getAppeSUmidh());
				if (watchdogConfiguration.isPartitioned()) {
					preparedStatement.setDate(4, new java.sql.Date(wdAppeKeyBean.getxCreaDtetime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return matchedAppeKeys.size();
			}
		});

	}

	@Override
	public void updateMessageWDEmailNotification(List<MessageEmailNotification> emailNotifications) {
		String updateStatment = "UPDATE WDEMAILNOTIFICATION  SET PROCESS_STATUS = 1 where id = ?  ";

		if (watchdogConfiguration.isPartitioned()) {
			updateStatment += " and MESG_CREA_DATE_TIME = ?";
		}

		jdbcTemplate.batchUpdate(updateStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				MessageEmailNotification messageEmailNotification = emailNotifications.get(index);
				preparedStatement.setInt(1, messageEmailNotification.getId());
				if (watchdogConfiguration.isPartitioned()) {
					preparedStatement.setDate(2, new java.sql.Date(messageEmailNotification.getMesgCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return emailNotifications.size();
			}
		});

	}

	@Override
	public void updateEventWDEmailNotification(List<EventEmailNotification> eventEmailNotifications) {
		String updateStatment = "UPDATE WDEMAILNOTIFICATION  SET PROCESS_STATUS = 1 where id = ?  ";

		if (watchdogConfiguration.isPartitioned()) {
			updateStatment += " and MESG_CREA_DATE_TIME = ?";
		}

		jdbcTemplate.batchUpdate(updateStatment, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				EventEmailNotification eventEmailNotification = eventEmailNotifications.get(index);
				preparedStatement.setInt(1, eventEmailNotification.getId());
				if (watchdogConfiguration.isPartitioned()) {
					preparedStatement.setDate(2, new java.sql.Date(eventEmailNotification.getJrnlDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return eventEmailNotifications.size();
			}
		});

	}

	@Override
	public void deleteMessageWDEmailNotification(MessageEmailNotification emailNotification) {

		Boolean partitioned = watchdogConfiguration.isPartitioned();
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(emailNotification.getId());

		String insertStatemnt = "delete from  WDEMAILNOTIFICATION   where id = ? ";

		if (partitioned) {
			insertStatemnt = insertStatemnt + " and MESG_CREA_DATE_TIME = ?";
			parameters.add(emailNotification.getMesgCreaDateTime());
		}

		jdbcTemplate.update(insertStatemnt, parameters.toArray());

	}

	@Override
	public void deleteEventWDEmailNotification(EventEmailNotification eventEmailNotification) {
		Boolean partitioned = watchdogConfiguration.isPartitioned();
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(eventEmailNotification.getId());

		String insertStatemnt = "delete from  WDEMAILNOTIFICATION   where id = ? ";

		if (partitioned) {
			insertStatemnt = insertStatemnt + " and MESG_CREA_DATE_TIME = ?";
			parameters.add(eventEmailNotification.getJrnlDateTime());
		}

		jdbcTemplate.update(insertStatemnt, parameters.toArray());
	}

	public List<CorrespondentBean> cacheCorrespondentsInformation() {

		List<CorrespondentBean> listOfcorrespondent = new ArrayList<>();
		try {
			String query = "SELECT DISTINCT C.CORR_X1, C.CORR_INSTITUTION_NAME, C.CORR_BRANCH_INFO, C.CORR_CTRY_CODE, C.CORR_CTRY_NAME FROM RCORR C ORDER BY CORR_X1 ";

			Object[] param = null;
			listOfcorrespondent = jdbcTemplate.query(query, param, new RowMapper<CorrespondentBean>() {
				public CorrespondentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					CorrespondentBean correspondentBean = new CorrespondentBean();
					String bic11 = rs.getString("CORR_X1");
					if (bic11 != null && !bic11.isEmpty()) {
						String bic8 = bic11.substring(0, 8);
						correspondentBean.setCorrBIC11(bic11);
						correspondentBean.setCorrBIC8(bic8);
						correspondentBean.setCorrBranchCode(bic11.substring(8, 11));
						correspondentBean.setCorrInstitutionName(rs.getString("CORR_INSTITUTION_NAME"));
						correspondentBean.setCorrBranchInfo(rs.getString("CORR_BRANCH_INFO"));
						correspondentBean.setCorrCountryName(rs.getString("CORR_CTRY_NAME"));
						correspondentBean.setCorrCountryCode(rs.getString("CORR_CTRY_CODE"));
						return correspondentBean;
					}
					return null;
				}
			});

			if (listOfcorrespondent == null || listOfcorrespondent.isEmpty()) {
				listOfcorrespondent = new ArrayList<>();
			}

			return listOfcorrespondent;

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public String getExpandedMesssageText(String syntaxVersion, String messageType, String unexpandedText, Date messageDate, String thousandAmountFormat, String decimalAmountFormat, Map<String, Integer> currencies) {
		ParsedMessage list = null;
		try {
			if (expandConn == null || expandConn.isClosed()) {
				expandConn = jdbcTemplate.getDataSource().getConnection();
			}
			Message messageParser = Syntax.getParser(syntaxVersion, messageType, expandConn);
			list = messageParser.parseMessageText(unexpandedText);
			List<ParsedField> parsedFields = list.getParsedFields();
			Map<Integer, ParsedLoop> parsedLoops = list.getParsedLoops();

			for (Integer key : parsedLoops.keySet()) {
				ParsedLoop loop = parsedLoops.get(key);
			}
		} catch (UnrecognizedBlockException ex) {

			return "Message Expand Failed: Message Contains Unrecognized Block";
		} catch (Exception ex) {
			return "Message Expand Failed: " + ex.getMessage();
		}
		String val = unexpandedText;
		if (list != null) {
			val = list.getExpandedMessage(new Timestamp(messageDate.getTime()), thousandAmountFormat, decimalAmountFormat, currencies);
		}
		return val;
	}

	@Override
	public List<Long> getTest() {
		String queryString = "select * from WDJRNLKEYS";
		List<Long> formatList = jdbcTemplate.query(queryString, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long format = rs.getLong("JRNL_SEQ_NBR");
				return format;
			}
		});

		return formatList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getCurrenciesISO() {

		return jdbcTemplate.query("select DISTINCT CURRENCYCODE , NUMBEROFDIGITS from CU ", new ResultSetExtractor<Map>() {
			@Override
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<String, Integer> results = new HashMap<>();
				while (rs.next()) {
					results.put(rs.getString("CURRENCYCODE"), rs.getInt("NUMBEROFDIGITS"));
				}
				return results;
			}
		});

	}

	@Override
	public void insertIntoWdEmailNotifaction(List<WDEmailNotification> emailNotifications) {
		String insertStatemnt = "";
		insertStatemnt = "INSERT INTO WDEMAILNOTIFICATION (DESCRIPTION,PROCESS_STATUS) VALUES (?,?)";

		// NEXTVAL
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				WDEmailNotification wdEmailNotification = emailNotifications.get(index);
				preparedStatement.setString(1, wdEmailNotification.getDescription());
				preparedStatement.setLong(2, 0);

			}

			@Override
			public int getBatchSize() {
				return emailNotifications.size();
			}
		});

	}

	@Override
	public boolean sCheckRoals() {
		CheckRoleProcedure checkRoleProcedure = new CheckRoleProcedure(jdbcTemplate);
		Integer status = checkRoleProcedure.execute("SIDE_LOADER");
		if (status != 1)
			return false;

		return true;
	}

	@Override
	public User getUserProfileId(String username) {

		StringBuilder selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("SELECT");
		selectUsersQuery.append(" sUser.UserName AS UserName");
		selectUsersQuery.append(" , sUser.FullUserName AS FullUserName");
		selectUsersQuery.append(" , sUser.GroupID AS GroupID");
		selectUsersQuery.append(" FROM sUser ");

		selectUsersQuery.append(" WHERE ");

		selectUsersQuery.append(" sUser.UserName NOT IN ('LSA_USER' , 'RSA_USER' ) ");
		selectUsersQuery.append("AND UPPER(sUser.UserName) = upper('" + username + "') ");
		selectUsersQuery.append("  ORDER BY UserName");
		List<User> query = jdbcTemplate.query(selectUsersQuery.toString(), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rownum) throws SQLException {
				User user = new User();
				user.setGroupId(rs.getLong("GroupID"));
				user.setUsername(rs.getString("UserName"));
				return user;
			}
		});
		return (query != null && !query.isEmpty()) ? query.get(0) : new User();

	}

	@Override
	public List<String> getProfileRoles(Long profileID) {
		String selectQuery = "SELECT role_name FROM sGrantedRoles where group_id = " + profileID;
		List<String> roles = jdbcTemplate.query(selectQuery, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("role_name").toUpperCase();
			}
		});

		return roles;
	}

	@Override
	public void saveWDMessageRequestResult(List<WDMessageRequestResult> messageRequestsResults) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveWDEmailNotification(List<WDEmailNotification> emailNotifications) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveNackResults(List<WDNackResult> nackResults) {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePossibleDuplicates(List<WDPossibleDuplicateResult> possibleDuplicates) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveEventResults(List<WDEventRequestResult> eventRequestsResults) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2) {
		// TODO Auto-generated method stub

	}
}
