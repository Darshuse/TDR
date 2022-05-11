/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.dao.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.admin.procedure.AdminHelperUserProcedure;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.BICPerProfile;
import com.eastnets.domain.admin.MsgCatPerProfile;
import com.eastnets.domain.admin.UnitPerProfile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.domain.security.UserApprovalStatusInfo;
import com.eastnets.utils.Utils;

/**
 * Security DAO Implementation
 * 
 * @author EastNets
 * @since July 15, 2012
 */
public abstract class SecurityDAOImp extends DAOBaseImp implements SecurityDAO {
	/**
	* 
	*/
	private static final long serialVersionUID = 2446439978385693696L;

	private Connection con = null;
	private String dbUrl;
	private AdminHelperUserProcedure adminHelperUserProcedure;
	private ApplicationFeatures applicationFeatures;

	public AdminHelperUserProcedure getAdminHelperUserProcedure() {
		return adminHelperUserProcedure;
	}

	public void setAdminHelperUserProcedure(AdminHelperUserProcedure adminHelperUserProcedure) {
		this.adminHelperUserProcedure = adminHelperUserProcedure;
	}

	@Override
	public void authenticate(User user) throws SQLException {
		try {
			con = DriverManager.getConnection(dbUrl, user.getUserName(), user.getPassword());
		} catch (SQLException e) {
			closeConnection(con);
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	private void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException sqlE) {
				// do nothing
			}
		}
	}

	@Override
	public void changeUserDBPassword(String userName, String newUserPassword, boolean unlockUser) throws SQLException {
		if (getApplicationFeatures().isNewSecurity()) {
			String passwordChanged = "UPDATE SUSER SET CHANGEPASSWORD = 0 WHERE USERNAME = ?";
			getJdbcTemplate().update(passwordChanged, new Object[] { userName });
		}
	}

	@Override
	public void resetPassword(String userName, String newUserPassword, boolean unlockUser) throws SQLException {
		if (getApplicationFeatures().isNewSecurity()) {
			String passwordChanged = "UPDATE SUSER SET CHANGEPASSWORD = 1,PASSWORDRESETDATE=? WHERE USERNAME = ?";
			getJdbcTemplate().update(passwordChanged, new Object[] { Utils.afterCurrentDayIgnoreTime(1), userName });
		}
	}

	public UserApprovalStatusInfo checkUserApproval(String username) throws SQLException {

		// check the user approval status
		String query = "(" + "  select 1 as tmp, ApprovalStatus, 'User: ' " + (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "||" : "+") + " statusDescription as StatusDescriptionStr"
				+ "  from sUser, sApprovalStatuses" + "  where upper(UserName) = ? and sUser.ApprovalStatus = sApprovalStatuses.Status" + ")" + "union (" + " select 2 as tmp, sUserGroup.ApprovalStatus, 'Profile: ' "
				+ (getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE ? "||" : "+") + " sApprovalStatuses.StatusDescription  as StatusDescriptionStr" + " from sUserGroup, sApprovalStatuses , sUser"
				+ " where sUserGroup.groupid = sUser.groupid and sUserGroup.ApprovalStatus = sApprovalStatuses.Status and  upper(UserName) = ?" + ")" + "order by tmp";

		return getJdbcTemplate().query(query, new Object[] { username.toUpperCase(), username.toUpperCase() }, new ResultSetExtractor<UserApprovalStatusInfo>() {
			@Override
			public UserApprovalStatusInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				UserApprovalStatusInfo info = new UserApprovalStatusInfo();
				// first row is for the user
				if (rs.next()) {
					info.setStatus(rs.getInt("ApprovalStatus"));
					info.setDescription(rs.getString("StatusDescriptionStr"));
				}
				if (info.getStatus() != 0) {
					return info;
				}

				// second row is for the profile
				info = new UserApprovalStatusInfo();
				if (rs.next()) {
					info.setStatus(rs.getInt("ApprovalStatus"));
					info.setDescription(rs.getString("StatusDescriptionStr"));
					return info;
				}

				return null;
			}
		});
	}

	public UserApprovalStatusInfo getUserApprovedStatus() throws SQLException {
		String query = "select Status, StatusDescription from sApprovalStatuses where Status = 0";
		return getJdbcTemplate().query(query, new Object[] {}, new ResultSetExtractor<UserApprovalStatusInfo>() {
			@Override
			public UserApprovalStatusInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UserApprovalStatusInfo info = new UserApprovalStatusInfo();
					info.setStatus(rs.getInt("Status"));
					info.setDescription(rs.getString("StatusDescription"));
					return info;
				}
				return null;
			}
		});
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	@Override
	public List<Pair<String, String>> getRoles() {
		String selectQuery = "SELECT role_name, role_display FROM sRoles";

		List<Pair<String, String>> roles = jdbcTemplate.query(selectQuery, new RowMapper<Pair<String, String>>() {
			@Override
			public Pair<String, String> mapRow(ResultSet rs, int arg1) throws SQLException {

				Pair<String, String> role = new Pair<String, String>();
				role.setKey(rs.getString("role_name").toUpperCase());
				role.setValue(rs.getString("role_display"));
				return role;
			}
		});

		return roles;
	}

	@Override
	public List<Pair<String, String>> getRoleDisplayNames(List<String> roles) {
		String selectQuery = "SELECT role_name, role_display FROM sRoles where 1=1  ";
		boolean first = true;
		for (int i = 1; i <= roles.size(); i++) {
			if (i == 1) {
				selectQuery += " and UPPER(role_name) in  ( ";
			}
			if (!first) {
				selectQuery += ", ";
			}
			selectQuery += "'" + roles.get(i - 1).toUpperCase() + "'";
			first = false;
			if (i == roles.size()) {
				selectQuery += " ) ";
			}
		}

		List<Pair<String, String>> roles_filled = jdbcTemplate.query(selectQuery, new RowMapper<Pair<String, String>>() {
			@Override
			public Pair<String, String> mapRow(ResultSet rs, int arg1) throws SQLException {
				Pair<String, String> role = new Pair<String, String>();

				role.setKey(rs.getString("role_name").toUpperCase());
				role.setValue(rs.getString("role_display"));

				return role;
			}
		});

		return roles_filled;
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
	public void removeProfileRoles(Long profileId) {
		String query = "";
		query = "delete sGrantedRoles where group_id= ? ";
		getJdbcTemplate().update(query, new Object[] { profileId });
	}

	@Override
	public void setProfileRoles(Long profileId, List<String> profileRoles) {
		String query = "";
		query = "insert into sGrantedRoles( role_name, group_id ) values ( ?, ? )";
		for (String role : profileRoles) {
			getJdbcTemplate().update(query, new Object[] { role.toUpperCase(), profileId });
		}

	}

	@Override
	public void setReportingUserPassword(User user) {
		/*
		 * Make expiration date for the generated password, after tow days of current date
		 */
		String password = null;
		if (user.getSalt() == null || user.getSalt().trim().isEmpty()) {
			password = user.getPasswordEncrypted().toString();
		} else {
			password = user.getHashedPassword();
		}
		String query = "insert into sUserPasswords( SALT,USERID, PASSWORD, PASSWORD_DATE, AUTOGENERATED) values (?, ?, ?, ?,?)";
		jdbcTemplate.update(query, new Object[] { user.getSalt(), user.getUserId(), password, Utils.afterCurrentDay(0), ((user.isAutoGeneratedPassword()) ? 1 : 0) });

	}

	@Override
	public void deleteUserPasswords(User user) {
		String query = "delete sUserPasswords where USERID = ?";
		jdbcTemplate.update(query, new Object[] { user.getUserId() });
	}

	@Override
	public void updateLastLogin(long userId, boolean isDBUser, String type) {
		if (isDBUser) {
			String query = "update sUser set LASTLOGIN = ? where userName = ? ";
			jdbcTemplate.update(query, new Object[] { new Date(), type });

		} else {
			String query = "update sUser set LASTLOGIN = ? where USERID = ? ";
			jdbcTemplate.update(query, new Object[] { new Date(), userId });
		}
	}

	@Override
	public void updateSecretKey(String username, String secretKey) {
		String query = "update sUser set SecretKey =? where userName = ? ";
		jdbcTemplate.update(query, new Object[] { secretKey, username });
	}

	@Override
	public PasswordPolicy getPasswordPolicy() {

		String selectQuery = "SELECT MinLength, MaxLength, CanContainUsername, CanContainReversedUsername, CanContainUsernamePart, NamePartLength, CanContainSpaces, MustContainUpperCase, MustContainLowerCase, MustContainNumber, MustContainSpecialChar, MustStartWithAlpha, PasswordExpirationDays, PasswordExpirationWarningDays, PasswordHistoryLenght , PasswordNumberOfTries FROM sPasswordPolicy";
		PasswordPolicy policy = jdbcTemplate.queryForObject(selectQuery, new RowMapper<PasswordPolicy>() {
			@Override
			public PasswordPolicy mapRow(ResultSet rs, int arg1) throws SQLException {
				PasswordPolicy policy = new PasswordPolicy();

				policy.setMinLength(rs.getInt("MinLength"));
				policy.setMaxLength(rs.getInt("MaxLength"));
				policy.setCanContainUsername(rs.getBoolean("CanContainUsername"));
				policy.setCanContainReversedUsername(rs.getBoolean("CanContainReversedUsername"));
				policy.setCanContainUsernamePart(rs.getBoolean("CanContainUsernamePart"));
				policy.setNamePartLength(rs.getInt("NamePartLength"));
				policy.setCanContainSpaces(rs.getBoolean("CanContainSpaces"));
				policy.setMustContainUpperCase(rs.getBoolean("MustContainUpperCase"));
				policy.setMustContainLowerCase(rs.getBoolean("MustContainLowerCase"));
				policy.setMustContainNumber(rs.getBoolean("MustContainNumber"));
				policy.setMustContainSpecialChar(rs.getBoolean("MustContainSpecialChar"));
				policy.setMustStartWithAlpha(rs.getBoolean("MustStartWithAlpha"));
				policy.setPasswordExpirationInDays(rs.getInt("PasswordExpirationDays"));
				policy.setPasswordExpirationWarningInDays(rs.getInt("PasswordExpirationWarningDays"));
				policy.setPasswordHistoryLenght(rs.getInt("PasswordHistoryLenght"));
				policy.setNumberOfAttempts(rs.getInt("PasswordNumberOfTries"));
				return policy;
			}
		});
		return policy;
	}

	@Override
	public void updatePasswordPolicy(PasswordPolicy policy) {
		String query = "Update sPasswordPolicy set " + " MinLength = ?," + " MaxLength = ?," + " CanContainUsername = ?," + " CanContainReversedUsername = ?," + " CanContainUsernamePart = ?," + " NamePartLength = ?," + " CanContainSpaces = ?,"
				+ " MustContainUpperCase = ?," + " MustContainLowerCase = ?," + " MustContainNumber = ?," + " MustContainSpecialChar = ?," + " MustStartWithAlpha = ?," + " PasswordExpirationDays = ?," + " PasswordExpirationWarningDays = ?,"
				+ " PasswordHistoryLenght = ?," + " PasswordNumberOfTries = ?";

		jdbcTemplate.update(query,
				new Object[] { policy.getMinLength(), policy.getMaxLength(), policy.isCanContainUsername(), policy.isCanContainReversedUsername(), policy.isCanContainUsernamePart(), policy.getNamePartLength(), policy.isCanContainSpaces(),
						policy.isMustContainUpperCase(), policy.isMustContainLowerCase(), policy.isMustContainNumber(), policy.isMustContainSpecialChar(), policy.isMustStartWithAlpha(), policy.getPasswordExpirationInDays(),
						policy.getPasswordExpirationWarningInDays(), policy.getPasswordHistoryLenght(), policy.getNumberOfAttempts() });
	}

	@Override
	public Date getPasswordResetDate(long userId, int userType) {

		String query;
		if (userType == User.AUTH_REPORTING || userType == User.AUTH_REPORTING_TOTP) {
			// get passwords ordered by date and just consider the first one
			query = "select PASSWORD_DATE AS PASSWORD_DAYS FROM SUSERPASSWORDS WHERE USERID = ? ORDER BY PASSWORD_DATE DESC";
		} else {
			query = "select PASSWORDRESETDATE AS PASSWORD_DAYS from SUSER WHERE USERID = ? ORDER BY PASSWORDRESETDATE DESC";
		}
		List<Date> passwordResetDate = getJdbcTemplate().query(query, new Object[] { userId }, new RowMapper<Date>() {

			@Override
			public Date mapRow(ResultSet rs, int arg1) throws SQLException {
				/*
				 * if ( !rs.next() ){ return null; }
				 */

				return rs.getTimestamp("PASSWORD_DAYS");
			}
		});
		if (passwordResetDate != null && passwordResetDate.size() > 0) {
			return passwordResetDate.get(0);
		}
		return null;
	}

	@Override
	public boolean isAutoGeneratedPassword(Long userId, int userType) {
		// get passwords ordered by date and just consider the first one
		String query = "SELECT AUTOGENERATED FROM SUSERPASSWORDS WHERE USERID = ? ORDER BY PASSWORD_DATE DESC";

		if (userType == User.AUTH_DB) {
			query = "SELECT CHANGEPASSWORD AUTOGENERATED FROM SUSER WHERE USERID = ?";
		}

		List<Integer> autogenerated = getJdbcTemplate().query(query, new Object[] { userId }, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("AUTOGENERATED");
			}
		});
		if (autogenerated != null) {
			if (autogenerated.isEmpty()) {
				return false;
			} else {
				return autogenerated.get(0) != 0;
			}
		}
		return true;
	}

	@Override
	public List<BICPerProfile> getBICsPerProfile(final Long profileId, Integer isSelectedBIC) {
		String getBICsQuery = String.format("SELECT BICCode, Member FROM sBICPerGroupView WHERE GroupId = %d AND member = %d ORDER BY BICCode", profileId, isSelectedBIC);
		List<BICPerProfile> bics = jdbcTemplate.query(getBICsQuery, new RowMapper<BICPerProfile>() {
			public BICPerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
				BICPerProfile bic = new BICPerProfile();
				bic.setProfileId(profileId);
				bic.setBicCode(rs.getString("BICCode"));
				bic.setIsSelectedBIC(rs.getInt("Member"));
				return bic;
			}
		});

		return bics;
	}

	@Override
	public List<UnitPerProfile> getUnitsPerProfile(final Long profileId, Integer isSelectedUnit) {
		String getUnitsQuery = String.format("SELECT LTRIM(RTRIM(Unit)) as Unit, Member FROM sUnitPerGroupView WHERE GroupId = %d AND member = %d ORDER BY Unit", profileId, isSelectedUnit);

		List<UnitPerProfile> units = jdbcTemplate.query(getUnitsQuery, new RowMapper<UnitPerProfile>() {
			public UnitPerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnitPerProfile unit = new UnitPerProfile();
				unit.setProfileId(profileId);
				unit.setUnit(rs.getString("Unit"));
				unit.setIsSelectedBIC(rs.getInt("Member"));
				return unit;
			}
		});
		return units;
	}

	@Override
	public List<MsgCatPerProfile> getMsgCatPerProfile(final Long profileId, Integer isSelectedMsgCat) {
		String getMsgCat = String.format("SELECT Category, Description, Member FROM sMsgPerGroupView WHERE GroupId = %d AND member = %d ORDER BY Category", profileId, isSelectedMsgCat);

		List<MsgCatPerProfile> msgCats = jdbcTemplate.query(getMsgCat, new RowMapper<MsgCatPerProfile>() {
			public MsgCatPerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
				MsgCatPerProfile msgCat = new MsgCatPerProfile();
				msgCat.setProfileId(profileId);
				msgCat.setMsgCat(rs.getString("Category"));
				msgCat.setDesc(rs.getString("Description"));
				msgCat.setIsSelectedMsgCat(rs.getInt("Member"));
				return msgCat;
			}
		});
		return msgCats;
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	@Override
	public List<Action> getAuthorizedActions(Integer moduleId, Long profileId) {

		StringBuilder selectQuery = new StringBuilder();

		selectQuery.append("SELECT sObjects.ID,");
		selectQuery.append(" sObjects.ProgramID, ");
		selectQuery.append(" sObjects.Name, ");
		selectQuery.append(" sAdmit.GroupId ");
		selectQuery.append(" FROM sObjects ");
		selectQuery.append(" INNER JOIN ");
		selectQuery.append(" sAdmit ON sObjects.ID = sAdmit.ObjectID AND ");
		selectQuery.append(" sObjects.ProgramId = sAdmit.ProgramId ");
		selectQuery.append(" WHERE (sObjects.ProgramID = ");
		selectQuery.append(moduleId);
		selectQuery.append(" ) ");
		selectQuery.append(" AND (sAdmit.GroupID = ");
		selectQuery.append(profileId);
		selectQuery.append(") ");
		selectQuery.append(" ORDER BY sObjects.ID ");

		List<Action> actions = jdbcTemplate.query(selectQuery.toString(), new RowMapper<Action>() {
			@Override
			public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
				Action action = new Action();
				action.setActionId(rs.getLong("ID"));
				action.setActionName(rs.getString("NAME"));
				action.setModuleId(rs.getInt("PROGRAMID"));
				action.setProfileId(rs.getInt("GROUPID"));
				return action;
			}
		});
		return actions;
	}

	@Override
	public User getLastLogIn(String userName) {
		String selectQuery = "select LastLogin from sUser where UserName='" + userName + "'";
		List<User> user = jdbcTemplate.query(selectQuery, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int arg1) throws SQLException {
				User user = new User();
				user.setLastLoginDays(rs.getTimestamp("LastLogin"));
				return user;

			}
		});

		if (user.size() == 0) {

			return null;
		}
		return user.get(0);
	}

	@Override
	public String getSecretKey(String username) {

		String query = "select SecretKey from sUser where username = ?";

		String key = getJdbcTemplate().queryForObject(query, String.class, username);

		return key;
	}

	@Override
	public User getNumberOfPasswordAttempts(String userName) {

		try {

			String selectQuery = "select sUser.numberPasswordAttempts as numberPasswordAttempts," + "sUser.username as username , " + "sUser.userID as userID , " + " sUser.AuthenticationMethod as AuthenticationMethod"
					+ " from sUser where UserName = ?";
			List<User> user = jdbcTemplate.query(selectQuery, new Object[] { userName }, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int arg1) throws SQLException {
					User user = new User();
					user.setNumberPasswordAttempts(rs.getInt("numberPasswordAttempts"));
					user.setAuthenticationMethod(rs.getInt("AuthenticationMethod"));
					user.setUserId(rs.getLong("userID"));
					user.setUserName(rs.getString("username"));
					return user;

				}
			});

			if (user.size() == 0) {

				return null;
			}
			return user.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void updateNumberOfPasswordAttempts(String userName, Long userId, int curuntAttemptsNumber) {
		String query = "Update sUser set sUser.numberPasswordAttempts = ? where sUser.userID = ?";
		try {

			jdbcTemplate.update(query, new Object[] { curuntAttemptsNumber + 1, userId });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void makeUserDisabel(String userName, Long userId, int curuntAttemptsNumber) {
		String query = "Update sUser set sUser.Disabled = 1 where sUser.userID = ?";
		try {

			jdbcTemplate.update(query, new Object[] { userId });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}