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

import java.util.List;

import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.User;
/**
 * Administration SQL DAO Interface
 * @author EastNets
 * @since September 4, 2012
 */
public interface AdminSqlDAO extends AdminDAO {
	
	/**
	 * Call "sp_addrolemember" procedure.
	 * sp_addrolemember: adds a security account as a member of an existing Microsoft® SQL Server™ database role 
	 * in the current database.
	 * @param user
	 * @param roleName
	 */
	public void addRoleMemeber(User user, String roleName);
	
	/** 
	 * Call "sp_addsrvrolemember" procedure.
	 * sp_addsrvrolemember : adds a login as a member of a fixed server role.
	 * @param user
	 * @param roleName
	 */
	public void addSRVRoleMember(User user, String roleName);
	
	/**
	 * Call "sp_grantdbaccess" procedure.
	 * sp_grantdbaccess: adds a security account in the current database for a Microsoft® SQL Server™ 
	 * login or Microsoft Windows NT® user or group, 
	 * and enables it to be granted permissions to perform activities in the database.
	 * @param user
	 */
	public void grantDbAccess(User user);
	
	/**
	 * Call "sp_defaultdb" procedure.
	 * sp_defaultdb : changes the default database for a login.
	 * @param user
	 */
	public void grantDefaultDB(User user) ;
	
	/**
	 * Call "sp_grantlogin" procedure.
	 * sp_grantlogin: Allows a Microsoft® Windows NT® user or group account to connect to Microsoft SQL Server™ 
	 * using Windows Authentication.
	 * @param user
	 */
	public void grantLogin(User user);
	
	/**
	 * Call "sp_droprolemember" procedure.
	 * sp_droprolemember: Removes a security account from a Microsoft® SQL Server™ role in the current database.
	 * @param user
	 * @param roleName
	 */
	public void dropRoleMember(User user, String roleName);
	
	/**
	 * Call "sp_dropsrvrolemember" procedure
	 * sp_dropsrvrolemember: removes a Microsoft® SQL Server™ login or a Microsoft Windows NT® 
	 * user or group from a fixed server role.
	 * @param user
	 * @param roleName
	 */
	public void dropSRVRoleMember(User user, String roleName);
	
	/**
	 * This method returns the database users names
	 * @return List<String> Users names
	 * @author EastNets
	 * @since Nov 1, 2012
	 */
	public List<String> getDatabaseUsers();

	/**
	 * Add new loader connection
	 * @param loaderConnection
	 */
	public void addLoaderConnection(LoaderConnection loaderConnection);
	
}
