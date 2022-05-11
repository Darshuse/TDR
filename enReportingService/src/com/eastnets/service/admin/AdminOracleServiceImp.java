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

package com.eastnets.service.admin;

import java.util.List;

import com.eastnets.dao.admin.AdminDAO;
import com.eastnets.dao.admin.AdminOracleDAO;
import com.eastnets.dao.archive.ArchiveDAO;
import com.eastnets.dao.license.LicenseDAO;
import com.eastnets.domain.admin.User;

/**
 * Administration Oracle Service Implementation
 * 
 * @author EastNets
 * @since September 5, 2012
 */
public class AdminOracleServiceImp extends AdminServiceImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2827784348899462641L;
	private AdminOracleDAO adminDAO;
	private ArchiveDAO archiveDAO;
	private LicenseDAO licenseDAO;

	@Override
	public LicenseDAO getLicenseDAO() {
		return licenseDAO;
	}

	@Override
	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	@Override
	public AdminDAO getAdminDAO() {
		return adminDAO;
	}

	@Override
	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = (AdminOracleDAO) adminDAO;
	}

	public ArchiveDAO getArchiveDAO() {
		return archiveDAO;
	}

	public void setArchiveDAO(ArchiveDAO archiveDAO) {
		this.archiveDAO = archiveDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.service.admin.AdminServiceImp#removeUserRoles(com.eastnets.domain.admin.User)
	 */
	@Override
	protected void removeUserRoles(User user) {
		if (getApplicationFeatures().isNewSecurity()) {
			return;
		}
		List<String> oldUserRoles = adminDAO.getUserRoles(user);

		// must revoke all roles for users

		if (oldUserRoles.contains("SIDE_ADMIN_SECURITY") || oldUserRoles.contains("SIDE_ADMIN")) {

			adminDAO.grantRevokeCreateUser(user);
			adminDAO.grantRevokeDropUser(user);
			adminDAO.grantRevokeAlterUser(user);
		}

		for (String roleOldName : oldUserRoles) {

			adminDAO.grantRevokeRole(user, roleOldName);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.service.admin.AdminServiceImp#addUserRoles(com.eastnets.domain.admin.User)
	 */
	@Override
	protected void addUserRoles(User user) {
		if (getApplicationFeatures().isNewSecurity()) {
			return;
		}

		if (!user.isDatabaseUserOld()) {
			return;
		}

		adminDAO.grantCreateSession(user);
		if (user.getUserRolesOld().contains("SIDE_ADMIN") || user.getUserRolesOld().contains("SIDE_ADMIN_SECURITY")) {

			adminDAO.grantCreateUserWithAdmin(user);
			adminDAO.grantDropUserWithAdmin(user);
			adminDAO.grantAlterUserWithAdmin(user);
		}

		for (String roleName : user.getUserRolesOld()) {

			if (roleName == "SIDE_ADMIN_SECURITY" || roleName == "SIDE_ADMIN") {
				adminDAO.grantRoleWithAdmin(user, roleName);
			} else {
				adminDAO.grantRole(user, roleName);
			}

		}
	}

	@Override
	protected void removeUserLoaderRole(User user) {
		try {
			adminDAO.grantRevokeRole(user, "SIDE_LOADER");
			adminDAO.grantRevokeRole(user, "SIDE_REPORT");
		} catch (Exception ex) {
			if (!ex.getMessage().contains("not granted")) {
				System.out.println("Error in removeUserLoaderRole:" + ex.getMessage());
			}
		}
		try {
			adminDAO.grantRevokeRole(user, "SIDE_OPER");
		} catch (Exception ex) {
			if (!ex.getMessage().contains("not granted")) {
				System.out.println("Error in removeUserLoaderRole:" + ex.getMessage());
			}
		}

	}

	@Override
	protected void addUserLoaderRole(User user) {
		adminDAO.grantRole(user, "SIDE_LOADER");
		adminDAO.grantRole(user, "SIDE_OPER");
		adminDAO.grantRole(user, "SIDE_REPORT");
	}

	@Override
	public void grantRecoveryRole(User user) {
		adminDAO.grantRole(user, "SIDE_RECOVERY");
	}

	@Override
	public void removeRecoveryRole(User user) {
		try {
			adminDAO.grantRevokeRole(user, "SIDE_RECOVERY");
		} catch (Exception ex) {
			if (!ex.getMessage().contains("not granted")) {
				System.out.println("Error in removeUserLoaderRole:" + ex.getMessage());
			}
		}
	}

	@Override
	protected void addSpecialGrants(User user) {
		// no special grants for the user on oracle
	}

	@Override
	public void fillIsDatabaseUser(List<User> users) {
		List<String> databaseUsers = adminDAO.getDatabaseUsers();
		for (User user : users) {
			if (databaseUsers.contains(user.getUserName().toUpperCase())) {
				user.setDatabaseUserOld(true);
			}
		}
	}

	@Override
	public boolean checkRole(String db_roleName) {
		// TODO Auto-generated method stub
		return adminDAO.checkRole(db_roleName);
	}

	@Override
	public void changeAuthMethodForNonDbUsers(Long authMethod) {
		// TODO Auto-generated method stub
		adminDAO.changeAuthMethodForNonDbUsers(authMethod);
	}

}
