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
 * Administration oracle DAO Interface
 * @author EastNets
 * @since September 4, 2012
 */
public interface AdminOracleDAO extends AdminDAO{

	/**
	 * Grant role for a certain user (oracle implementation)
	 * @param user
	 * @param roleName
	 */
	public void grantRole(User user, String roleName);
	
	/**
	 * Grant role to user with admin option (oracle implementation)
	 * @param user
	 * @param roleName
	 */
	public void grantRoleWithAdmin(User user, String roleName);
	
	/**
	 * Grant alter user to certain user with admin option (oracle implementation)
	 * @param user
	 */
	public void grantAlterUserWithAdmin(User user);
	
	/**
	 * Grant drop user to certain user with admin option (oracle implementation)
	 * @param user
	 */
	public void grantDropUserWithAdmin(User user);
	
	/**
	 * Grant create user to certain user with admin option (oracle implementation)
	 * @param user
	 */
	public void grantCreateUserWithAdmin(User user);
	
	/**
	 * Grant create session to certain user (oracle implementation)
	 * @param user
	 */
	public void grantCreateSession(User user);
	
	/**
	 * Grant revoke create user to certain user (oracle implementation)
	 * @param user
	 */
	public void grantRevokeCreateUser(User user);
	
	/**
	 * Grant revoke drop user to certain user (oracle implementation)
	 * @param user
	 */
	public void grantRevokeDropUser(User user);
	
	/**
	 * Grant revoke alter user to certain user (oracle implementation)
	 * @param user
	 */
	public void grantRevokeAlterUser(User user);
	
	/**
	 * Grant revoke revoke role to certain user (oracle implementation)
	 * @param user
	 * @param roleName
	 */
	public void grantRevokeRole(User user ,String roleName);
	
	/**
	 * Add new loader connection
	 * @param loaderConnection
	 */
	public void addLoaderConnection(LoaderConnection loaderConnection);
	
	 /* This method returns the database users names
	 * @return List<String> Users names
	 * @author EastNets
	 * @since Nov 1, 2012
	 */
	public List<String> getDatabaseUsers();
}
