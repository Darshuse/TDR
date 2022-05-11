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

import com.eastnets.dao.admin.procedure.AdminAddLoginProcedure;
import com.eastnets.dao.admin.procedure.AdminAddRoleMemberProcedure;
import com.eastnets.dao.admin.procedure.AdminAddSRVRoleMemberProcedure;
import com.eastnets.dao.admin.procedure.AdminDefaultDBProcedure;
import com.eastnets.dao.admin.procedure.AdminDropLoginProcedure;
import com.eastnets.dao.admin.procedure.AdminDropRoleMemberProcedure;
import com.eastnets.dao.admin.procedure.AdminDropSRVRoleMemberProcedure;
import com.eastnets.dao.admin.procedure.AdminGrantDBAccessProcedure;
import com.eastnets.dao.admin.procedure.AdminGrantLoginProcedure;
import com.eastnets.dao.admin.procedure.AdminHelperSRVRoleMemberProcedure;
import com.eastnets.dao.admin.procedure.AdminHelperUserProcedure;
import com.eastnets.dao.admin.procedure.AdminRevokeDBAccessProcedure;
import com.eastnets.dao.admin.procedure.AdminRevokeLoginProcedure;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.User;

/**
 * Administration SQL DAO Implementation
 * 
 * @author EastNets
 * @since September 4, 2012
 */
public class AdminSqlDAOImp extends AdminDAOImp implements AdminSqlDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3394605133629008065L;
	private AdminRevokeDBAccessProcedure adminRevokeDBAccessProcedure;
	private AdminRevokeLoginProcedure adminRevokeLoginProcedure;
	private AdminDropLoginProcedure adminDropLoginProcedure;
	private AdminGrantLoginProcedure adminGrantLoginProcedure;
	private AdminDefaultDBProcedure adminDefaultDBProcedure;
	private AdminAddLoginProcedure adminAddLoginProcedure;
	private AdminGrantDBAccessProcedure adminGrantDBAccessProcedure;
	private AdminAddSRVRoleMemberProcedure adminAddSRVRoleMemberProcedure;
	private AdminAddRoleMemberProcedure adminAddRoleMemberProcedure;
	private AdminHelperUserProcedure adminHelperUserProcedure;
	private AdminDropRoleMemberProcedure adminDropRoleMemberProcedure;
	private AdminDropSRVRoleMemberProcedure adminDropSRVRoleMemberProcedure;
	private AdminHelperSRVRoleMemberProcedure adminHelperSRVRoleMemberProcedure;

	public AdminDropRoleMemberProcedure getAdminDropRoleMemberProcedure() {
		return adminDropRoleMemberProcedure;
	}

	public void setAdminDropRoleMemberProcedure(AdminDropRoleMemberProcedure adminDropRoleMemberProcedure) {
		this.adminDropRoleMemberProcedure = adminDropRoleMemberProcedure;
	}

	public AdminDropSRVRoleMemberProcedure getAdminDropSRVRoleMemberProcedure() {
		return adminDropSRVRoleMemberProcedure;
	}

	public void setAdminDropSRVRoleMemberProcedure(AdminDropSRVRoleMemberProcedure adminDropSRVRoleMemberProcedure) {
		this.adminDropSRVRoleMemberProcedure = adminDropSRVRoleMemberProcedure;
	}

	public AdminHelperSRVRoleMemberProcedure getAdminHelperSRVRoleMemberProcedure() {
		return adminHelperSRVRoleMemberProcedure;
	}

	public void setAdminHelperSRVRoleMemberProcedure(AdminHelperSRVRoleMemberProcedure adminHelperSRVRoleMemberProcedure) {
		this.adminHelperSRVRoleMemberProcedure = adminHelperSRVRoleMemberProcedure;
	}

	public void setAdminAddRoleMemberProcedure(AdminAddRoleMemberProcedure adminAddRoleMemberProcedure) {
		this.adminAddRoleMemberProcedure = adminAddRoleMemberProcedure;
	}

	public AdminRevokeDBAccessProcedure getAdminRevokeDBAccessProcedure() {
		return adminRevokeDBAccessProcedure;
	}

	public void setAdminRevokeDBAccessProcedure(AdminRevokeDBAccessProcedure adminRevokeDBAccessProcedure) {
		this.adminRevokeDBAccessProcedure = adminRevokeDBAccessProcedure;
	}

	public AdminRevokeLoginProcedure getAdminRevokeLoginProcedure() {
		return adminRevokeLoginProcedure;
	}

	public void setAdminRevokeLoginProcedure(AdminRevokeLoginProcedure adminRevokeLoginProcedure) {
		this.adminRevokeLoginProcedure = adminRevokeLoginProcedure;
	}

	public AdminDropLoginProcedure getAdminDropLoginProcedure() {
		return adminDropLoginProcedure;
	}

	public AdminGrantLoginProcedure getAdminGrantLoginProcedure() {
		return adminGrantLoginProcedure;
	}

	public void setAdminGrantLoginProcedure(AdminGrantLoginProcedure adminGrantLoginProcedure) {
		this.adminGrantLoginProcedure = adminGrantLoginProcedure;
	}

	public AdminDefaultDBProcedure getAdminDefaultDBProcedure() {
		return adminDefaultDBProcedure;
	}

	public void setAdminDefaultDBProcedure(AdminDefaultDBProcedure adminDefaultDBProcedure) {
		this.adminDefaultDBProcedure = adminDefaultDBProcedure;
	}

	public AdminAddLoginProcedure getAdminAddLoginProcedure() {
		return adminAddLoginProcedure;
	}

	public void setAdminAddLoginProcedure(AdminAddLoginProcedure adminAddLoginProcedure) {
		this.adminAddLoginProcedure = adminAddLoginProcedure;
	}

	public AdminGrantDBAccessProcedure getAdminGrantDBAccessProcedure() {
		return adminGrantDBAccessProcedure;
	}

	public void setAdminGrantDBAccessProcedure(AdminGrantDBAccessProcedure adminGrantDBAccessProcedure) {
		this.adminGrantDBAccessProcedure = adminGrantDBAccessProcedure;
	}

	public AdminAddSRVRoleMemberProcedure getAdminAddSRVRoleMemberProcedure() {
		return adminAddSRVRoleMemberProcedure;
	}

	public void setAdminAddSRVRoleMemberProcedure(AdminAddSRVRoleMemberProcedure adminAddSRVRoleMemberProcedure) {
		this.adminAddSRVRoleMemberProcedure = adminAddSRVRoleMemberProcedure;
	}

	public void setAdminDropLoginProcedure(AdminDropLoginProcedure adminDropLoginProcedure) {
		this.adminDropLoginProcedure = adminDropLoginProcedure;
	}

	public void setAdminAddRoleMember(AdminAddRoleMemberProcedure adminAddfdRoleMember) {
		this.adminAddRoleMemberProcedure = adminAddfdRoleMember;
	}

	public AdminAddRoleMemberProcedure getAdminAddRoleMemberProcedure() {
		return adminAddRoleMemberProcedure;
	}

	public void setAdminHelperUserProcedure(AdminHelperUserProcedure adminHelperUserProcedure) {
		this.adminHelperUserProcedure = adminHelperUserProcedure;
	}

	public AdminHelperUserProcedure getAdminHelperUserProcedure() {
		return adminHelperUserProcedure;
	}

	@Override
	public void addDatabaseUser(User user) {

		getAdminAddLoginProcedure().execute(user);

	}

	@Override
	public void deleteDatabaseUser(User user) {

		// delete database user from MSSQL Database
		// call MSSQL stored procedures

		getAdminRevokeDBAccessProcedure().execute(user);

		if (user.getUserName().indexOf("\\") > -1) {
			getAdminRevokeLoginProcedure().execute(user);
		} else {
			getAdminDropLoginProcedure().execute(user);
		}

	}

	@Override
	public List<String> getUserRoles(String username) {
		List<String> userRoles = adminHelperUserProcedure.execute(username);

		List<String> roles = new ArrayList<String>();
		for (String role : userRoles) {
			if (role.toUpperCase().startsWith("SIDE") && !roles.contains(role.toUpperCase())) {
				roles.add(role.toUpperCase());
			}
		}

		return roles;
	}

	@Override
	public List<Pair<String, String>> getRoles() {

		String query = "exec sp_helprole";

		List<Pair<String, String>> roles = jdbcTemplate.query(query, new RowMapper<Pair<String, String>>() {
			@Override
			public Pair<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pair<String, String> role = new Pair<String, String>();
				String roleName = rs.getString("rolename");
				if (roleName.startsWith("side")) {
					role.setKey(roleName);
				} else {
					return null;
				}
				return role;
			}
		});

		while (roles.contains(null)) {
			roles.remove(null);
		}
		return roles;
	}

	@Override
	public void dropRoleMember(User user, String roleName) {
		this.adminDropRoleMemberProcedure.execute(user, roleName);
	}

	@Override
	public void dropSRVRoleMember(User user, String roleName) {
		this.adminDropSRVRoleMemberProcedure.execute(user, roleName);
	}

	@Override
	public void addRoleMemeber(User user, String roleName) {
		getAdminAddRoleMemberProcedure().execute(user, roleName);
	}

	@Override
	public void addSRVRoleMember(User user, String roleName) {
		getAdminAddSRVRoleMemberProcedure().execute(user, roleName);
	}

	@Override
	public void grantDbAccess(User user) {
		getAdminGrantDBAccessProcedure().execute(user);
	}

	@Override
	public void grantDefaultDB(User user) {
		getAdminDefaultDBProcedure().execute(user);
	}

	@Override
	public void grantLogin(User user) {
		getAdminGrantLoginProcedure().execute(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.dao.admin.AdminSqlDAO#getDatabaseUsers()
	 */
	@Override
	public List<String> getDatabaseUsers() {
		// Ticket 30045 : Removing the Sysadmin and Securityadmin Roles in the SQL server
		// String queryText = "exec sp_helplogins";
		String queryText = "select name from sys.database_principals where type in ('S', 'U')";
		List<String> users = jdbcTemplate.query(queryText, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {
				// return rs.getString("LoginName").trim();
				return rs.getString("name").trim();
			}
		});

		return users;
	}

	public boolean isDataBaseUser(String username) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(getDBSafeString(username));

		// Ticket 49883 : Removing the Sysadmin and Securityadmin Roles in the SQL server (Allow update of ldAdminSettings by user who does not have sysadmin or securityAdmin roles)
		String queryText = "select name from sys.database_principals where upper(name) = upper(?)";

		List<String> users = jdbcTemplate.query(queryText, parameters.toArray(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {

				return rs.getString("name").trim();

			}
		});

		if (users != null && !users.isEmpty())
			return true;

		return false;

	}

	@Override
	public boolean userHasSessionOnDB(String username) {

		List<Object> parameters = new ArrayList<>();
		parameters.add(getDBSafeString(username));

		String queryText = "SELECT session_id FROM sys.dm_exec_sessions WHERE upper(login_name) = upper(?) ";
		List<String> users = jdbcTemplate.query(queryText, parameters.toArray(), new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rownum) throws SQLException {
				return rs.getString("session_id").trim();
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

		String checkUserSchedules = "Select Count(*) from PSET_SCHEDULE WHERE CREATED_BY = (select userID from sUser where upper(username) = upper(?))";

		int totalSchedules = jdbcTemplate.queryForInt(checkUserSchedules, parameters.toArray());

		if (totalSchedules > 0)
			flag = true;

		String checkUserTemplates = "Select Count(*) from rMsgSearchTemplates WHERE created_by = (select userID from sUser where upper(username) = upper(?))";

		int totalTemplates = jdbcTemplate.queryForInt(checkUserTemplates, parameters.toArray());

		if (totalTemplates > 0)
			flag = true;

		return flag;
	}

	@Override
	public boolean checkRole(String db_roleName) {

		List<Object> parameters = new ArrayList<Object>();
		String query = " SELECT IS_MEMBER(?) as 'result' ";
		parameters.add(db_roleName);

		Integer count = jdbcTemplate.queryForObject(query, parameters.toArray(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Integer status = rs.getInt("result");
				return status;
			}
		});

		/*
		 * CheckRoleProcedure procedure = new CheckRoleProcedure(jdbcTemplate); Integer result = (Integer) procedure.execute(db_roleName);
		 */
		return (count == 1);
	}

	@Override
	public void changeAuthMethodForNonDbUsers(Long authMethod) {

		String query = "update suser   set AuthenticationMethod= " + authMethod + " where not exists (select sid from master.dbo.syslogins  where upper(name) = upper(username))";

		jdbcTemplate.execute(query);
	}

	@Override
	public void saveBaseBiRepPath(String loginUser, String repPath, boolean setAsDefault) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"INSERT INTO JIResourceFolder ( version,uri ,hidden,name,label,description ,parent_folder,creation_date ,update_date,createdBy)  VALUES (?,?,?,?,?,?,   (select ID from JIResourceFolder where uri='/public'),?,?,?)");
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
