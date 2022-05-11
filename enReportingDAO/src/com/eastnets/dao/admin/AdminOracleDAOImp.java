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

package com.eastnets.dao.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.User;

/**
 * Administration Oracle DAO Implementation
 * 
 * @author EastNets
 * @since September 4, 2012
 */
public class AdminOracleDAOImp extends AdminDAOImp implements AdminOracleDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3045468992397768023L;

	@Override
	public void addDatabaseUser(User user) {

		StringBuilder createUserQuery = new StringBuilder();
		String query = "";

		// Add to database
		createUserQuery = new StringBuilder();
		createUserQuery.append("CREATE USER \"");
		createUserQuery.append(user.getUserName().toUpperCase());
		createUserQuery.append("\" IDENTIFIED BY \"");
		createUserQuery.append(user.getPassword());
		createUserQuery.append("\" DEFAULT TABLESPACE ");
		createUserQuery.append(getConfig().getTableSpace().toUpperCase());
		createUserQuery.append(" QUOTA UNLIMITED ON ");
		createUserQuery.append(getConfig().getTableSpace().toUpperCase());

		query = (createUserQuery.toString());
		jdbcTemplate.execute(query);
		grantCreateSession(user);

	}

	@Override
	public List<String> getUserRoles(String username) {
		List<Object> parameters = new ArrayList<Object>();

		username = username.replaceAll("'", "''");
		parameters.add(username);
		String selectUsersQuery = "SELECT u.USERNAME AS USERNAME, r.ROLE_NAME AS GRANTED_ROLE FROM SUSER u, SGRANTEDROLES r WHERE USERNAME = ? AND u.GROUPID = r.GROUP_ID";

		List<String> userRoles = null;
		userRoles = jdbcTemplate.query(selectUsersQuery, parameters.toArray(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String roleName = rs.getString("GRANTED_ROLE");
				return roleName.toUpperCase();
			}
		});

		List<String> roles = new ArrayList<String>();
		for (String role : userRoles) {
			if (role != null && role.startsWith("SIDE") && !roles.contains(role)) {
				roles.add(role);
			}
		}
		return roles;
	}

	public boolean checkRole(String db_roleName) {

		List<Object> parameters = new ArrayList<Object>();
		String query = "select count(*)  tmp from USER_ROLE_PRIVS where GRANTED_ROLE in (?,'DBA')";
		parameters.add(db_roleName);
		Integer count = jdbcTemplate.queryForObject(query, parameters.toArray(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer status = rs.getInt("tmp");
				return status;
			}
		});
		/*
		 * 
		 * Bug 25185:en.Reporting3.1: modify ReportingServer in the distribution
		 * 
		 */
		return (count >= 1);
	}

	@Override
	public List<Pair<String, String>> getRoles() {
		String selectQuery = "select distinct a.granted_role	from dba_role_privs a left outer join	(select granted_role from role_role_privs where role in (select GRANTED_ROLE FROM user_ROLE_privs) and role like 'SIDE_%' union select GRANTED_ROLE FROM user_ROLE_privs ) b on a.granted_role=b.granted_role WHERE a.GRANTED_ROLE like 'SIDE_%'";

		List<Pair<String, String>> userRoles = jdbcTemplate.query(selectQuery, new RowMapper<Pair<String, String>>() {
			@Override
			public Pair<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {

				Pair<String, String> role = new Pair<String, String>();
				String roleName = rs.getString("Granted_role");
				role.setKey(roleName);

				return role;
			}
		});

		return userRoles;
	}

	@Override
	public void grantRole(User user, String roleName) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();

		selectUsersQuery.append("GRANT ");
		selectUsersQuery.append(roleName);
		selectUsersQuery.append(" TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantRoleWithAdmin(User user, String roleName) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();

		selectUsersQuery.append("GRANT ");
		selectUsersQuery.append(roleName);
		selectUsersQuery.append(" TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");
		selectUsersQuery.append(" WITH ADMIN OPTION ");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantAlterUserWithAdmin(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("GRANT ALTER USER TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");
		selectUsersQuery.append(" WITH ADMIN OPTION ");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantDropUserWithAdmin(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("GRANT DROP USER TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");
		selectUsersQuery.append(" WITH ADMIN OPTION ");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantCreateUserWithAdmin(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("GRANT CREATE USER TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");
		selectUsersQuery.append(" WITH ADMIN OPTION ");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantCreateSession(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("GRANT CREATE SESSION TO ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantRevokeCreateUser(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("REVOKE CREATE USER FROM ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);

	}

	@Override
	public void grantRevokeDropUser(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("REVOKE DROP USER FROM ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);

	}

	@Override
	public void grantRevokeAlterUser(User user) {

		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("REVOKE ALTER USER FROM ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void grantRevokeRole(User user, String roleName) {
		StringBuilder selectUsersQuery;
		String query;
		selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("REVOKE ");
		selectUsersQuery.append(roleName);
		selectUsersQuery.append(" FROM ");
		selectUsersQuery.append("\"" + user.getUserName().toUpperCase() + "\"");

		query = (selectUsersQuery.toString());
		jdbcTemplate.execute(query);
	}

	@Override
	public void deleteDatabaseUser(User user) {

		String deleteDatabaseUserQuery = null;

		// oracle database, execute drop user
		deleteDatabaseUserQuery = "DROP USER \"" + user.getUserName().toUpperCase() + "\" CASCADE";
		jdbcTemplate.execute(deleteDatabaseUserQuery);

	}

	public boolean isDataBaseUser(String username) {

		username = username.replaceAll("'", "''");

		List<Object> parameters = new ArrayList<>();
		username = username.replaceAll("'", "''");
		parameters.add(username);

		String queryText = "SELECT USERNAME FROM ALL_USERS WHERE UPPER(USERNAME)=UPPER(?)";
		List<String> users = jdbcTemplate.query(queryText, parameters.toArray(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {
				return rs.getString("USERNAME").trim();
			}
		});

		if (users != null && !users.isEmpty())
			return true;

		return false;
	}

	@Override
	public List<String> getDatabaseUsers() {
		String queryText = "SELECT USERNAME FROM ALL_USERS";
		List<String> users = jdbcTemplate.query(queryText, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {
				// return rs.getString("LoginName").trim();
				return rs.getString("USERNAME").trim();
			}
		});

		return users;
	}

	@Override
	public boolean userHasSessionOnDB(String username) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(username);

		String queryText = "SELECT sid FROM V$SESSION WHERE upper(username) = upper(?) ";
		List<String> users = jdbcTemplate.query(queryText, parameters.toArray(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {
				return rs.getString("sid").trim();
			}
		});

		if (users != null && !users.isEmpty())
			return true;

		return false;
	}

	@Override
	public boolean userHasRelationsOnDB(String username) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(username);

		boolean flag = false;

		String checkUserSchedules = "Select Count(*) from PSET_SCHEDULE WHERE CREATED_BY = (select userID from sUser where upper(username) =upper(?))";

		int totalSchedules = jdbcTemplate.queryForInt(checkUserSchedules, parameters.toArray());
		if (totalSchedules > 0)
			flag = true;

		String checkUserTemplates = "Select Count(*) from rMsgSearchTemplates WHERE created_by = (select userID from sUser where upper(username) =upper(?))";

		int totalTemplates = jdbcTemplate.queryForInt(checkUserTemplates, parameters.toArray());

		if (totalTemplates > 0)
			flag = true;

		return flag;

	}

	@Override
	public void changeAuthMethodForNonDbUsers(Long authMethod) {

		String query = "update suser s  set AUTHENTICATIONMETHOD =" + authMethod + " where not exists (select user_id from all_users a where upper(a.username)=upper(s.username))";
		jdbcTemplate.execute(query);
	}

	@Override
	public void saveBaseBiRepPath(String loginUser, String repPath, boolean setAsDefault) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"INSERT INTO JIResourceFolder (ID,  version,uri ,hidden,name,label,description ,parent_folder,creation_date ,update_date ,createdBy)  VALUES ((select max(ID)+1 from JIResourceFolder),?,?,?,?,?,?,   (select ID from JIResourceFolder where uri='/public'),?,?,?)");
				statement.setInt(1, 0);
				statement.setString(2, "/public/" + repPath);
				statement.setInt(3, 0);
				statement.setString(4, repPath);
				statement.setString(5, repPath);
				statement.setString(6, repPath);
				statement.setDate(7, new java.sql.Date(new Date().getTime()));
				statement.setDate(8, new java.sql.Date(new Date().getTime()));
				statement.setString(9, loginUser);
				return statement;
			}
		});

		if (setAsDefault) {
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement("UPDATE LDGLOBALSETTINGS SET BI_DEFAULT_PATH = ?");
					statement.setString(1, "/public/" + repPath);

					return statement;
				}
			});
		}

	}
}
