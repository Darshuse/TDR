/**
 * 
 */
package com.eastnets.dao.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.eastnets.dao.admin.procedure.AdminHelperUserProcedure;
import com.eastnets.domain.admin.User;

/**
 * 
 * @author EastNets
 * @since dNov 1, 2012
 * 
 */
public class SecuritySqlDAOImp extends SecurityDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6978582594667745847L;

	public void fillUserRoles(final User user) throws SQLException {

		AdminHelperUserProcedure adminHelperUserProcedure = getAdminHelperUserProcedure();
		List<String> execute = adminHelperUserProcedure.execute(user.getUserName());
		user.setUserRolesOld(execute);
	}

	@Override
	public void changeUserDBPassword(String userName, String newUserPassword, boolean unlockUser) throws SQLException {
		String query = "ALTER LOGIN \"" + userName + "\" WITH PASSWORD = '" + getDBSafeString(newUserPassword) + "'";
		if (unlockUser) {
			query += " unlock";
		}
		getJdbcTemplate().update(query);
		super.changeUserDBPassword(userName, newUserPassword, unlockUser);
	}

	@Override
	public void resetPassword(String userName, String newUserPassword, boolean unlockUser) throws SQLException {
		String query = "ALTER LOGIN \"" + userName + "\" WITH PASSWORD = '" + getDBSafeString(newUserPassword) + "'";
		if (unlockUser) {
			query += " unlock";
		}
		getJdbcTemplate().update(query);
		super.resetPassword(userName, newUserPassword, unlockUser);
	}

	@Override
	public boolean authenticateReportingUser(User user) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(user.getUserName());

		String query = " select top 1  sUser.USERID as USERID, password from  suser inner join sUserPasswords on sUserPasswords.USERID = sUser.USERID " + " where username =? order by PASSWORD_DATE desc ";

		Boolean found = getJdbcTemplate().query(query, parameters.toArray(), new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					if (user.getSalt() == null || user.getSalt().trim().isEmpty()) {
						return rs.getString("password").equals(user.getPasswordEncrypted().toString());
					} else {
						return rs.getString("password").equals(user.getHashedPassword());
					}
				}
				return false;
			}
		});

		return found.booleanValue();
	}

	@Override
	public boolean checkReportingUserPasswordUsed(Long userId, String password, int passwordHistoryLenght) {
		String query = "select * from ( select * from ( select top " + passwordHistoryLenght + " * from sUserPasswords where UserID = ? and AutoGenerated = 0 order by PASSWORD_DATE desc ) sUserPasswords ) sUserPasswords where password = ?";

		Boolean found = getJdbcTemplate().query(query, new Object[] { userId, password }, new ResultSetExtractor<Boolean>() {
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		});
		return found.booleanValue();
	}

	@Override
	public void resetSecretKey(User user) {
		String query = "update sUser set secretKey = null where username = ?";
		getJdbcTemplate().update(query, user.getUserName());

	}

	@Override
	public String getUserSalt(User user) {

		String query = "select SALT SALT from (select top 1 * from sUserPasswords where userid = (select userid from suser where UPPER(username) = UPPER('" + user.getUserName().replace("'", "''") + "')) order by PASSWORD_DATE desc) sUserPasswords";
		return jdbcTemplate.query(query, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getString("SALT") : null;
			}
		});
	}

}