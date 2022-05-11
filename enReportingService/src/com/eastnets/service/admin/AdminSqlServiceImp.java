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

import com.eastnets.dao.admin.AdminSqlDAO;
import com.eastnets.dao.admin.AdminDAO;
import com.eastnets.dao.archive.ArchiveDAO;
import com.eastnets.dao.license.LicenseDAO;
import com.eastnets.domain.admin.User;

/**
 * Administration SQL Service Implementation
 * @author EastNets
 * @since September 5, 2012
 */
public class AdminSqlServiceImp extends AdminServiceImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4163226367629314271L;
	private AdminSqlDAO adminDAO;
	private AdminSqlDAO archiveDAO;
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
		
		return this.adminDAO;
	}
		
	@Override
	public void setAdminDAO(AdminDAO AdminDAO) {
		this.adminDAO = (AdminSqlDAO)AdminDAO;
	}
	
	public ArchiveDAO getArchiveDAO() {
		return (ArchiveDAO) archiveDAO;
	}

	public void setArchiveDAO(ArchiveDAO archiveDAO) {
		this.archiveDAO = (AdminSqlDAO) archiveDAO;
	}

	/* (non-Javadoc)
	 * @see com.eastnets.service.admin.AdminServiceImp#removeUserRoles(com.eastnets.domain.admin.User)
	 */
	@Override
	protected void removeUserRoles(User user) {
		if ( getApplicationFeatures().isNewSecurity() ){
			return ;
		}
		//keep the old implementation
		
		if(!user.isDatabaseUserOld()) {
			return;
		}
		
		List<String> userOldRoles = getUserRoles(user);
		
		for (String oldRoleName : userOldRoles) {
			//* Bug 30826 : Reporting: Change users roles failed
		    /* dropSRVRoleMember generated permissions exception after removing sysadmin and securityadmin
		     * but we still need to remove those roles in customers who did not remove those roles.
		     * the solution is to keep the code for latter customers and just swallow the exception in former ones.
		     */
		    try {
    			if(oldRoleName.equalsIgnoreCase(("SIDE_ADMIN_SECURITY"))) {
    				adminDAO.dropSRVRoleMember(user, "securityadmin");
    				adminDAO.dropRoleMember(user, "db_securityadmin");
    			}
    			else if(oldRoleName.equalsIgnoreCase("SIDE_ADMIN")){
    				adminDAO.dropSRVRoleMember(user, "sysadmin");
    				adminDAO.dropRoleMember(user, "db_accessadmin");
    			}
		    }
		    catch(Exception ex) {
		    }
			//*/

			if(!oldRoleName.equalsIgnoreCase("public"))
				adminDAO.dropRoleMember(user, oldRoleName);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.eastnets.service.admin.AdminServiceImp#addUserRoles(com.eastnets.domain.admin.User)
	 */
	@Override
	protected void addUserRoles(User user) {
		if ( getApplicationFeatures().isNewSecurity() ){
			return ;
		}
		//keep the old implementation
		
		if(!user.isDatabaseUserOld()) {
			return;
		}
		
		List<String> userRoles = user.getUserRolesOld();
		for (String roleName  : userRoles) {
			/* Ticket 30045 : Removing the Sysadmin and Securityadmin Roles in the SQL server
			if( roleName.equalsIgnoreCase("SIDE_ADMIN")  ) {
				this.adminDAO.addSRVRoleMember(user,"sysadmin");
				this.adminDAO.addRoleMemeber(user, "db_accessadmin");
				
			}else if(roleName.equalsIgnoreCase("SIDE_ADMIN_SECURITY")) {
				this.adminDAO.addSRVRoleMember(user,"securityadmin");
				this.adminDAO.addRoleMemeber( user, "db_securityadmin");
			}
			*/
			this.adminDAO.addRoleMemeber(user, roleName);
		}
		
	}

	@Override
	protected void removeUserLoaderRole(User user) {
		adminDAO.dropRoleMember(user, "SIDE_LOADER");
		adminDAO.dropRoleMember(user, "SIDE_OPER");
		adminDAO.dropRoleMember(user, "SIDE_REPORT");
	}

	@Override
	protected void addUserLoaderRole(User user) {
		adminDAO.addRoleMemeber(user, "SIDE_LOADER");
		adminDAO.addRoleMemeber(user, "SIDE_OPER");
		adminDAO.addRoleMemeber(user, "SIDE_REPORT");
	}
	
	@Override
	public void grantRecoveryRole(User user) {
		adminDAO.addRoleMemeber(user, "SIDE_RECOVERY");
	}

	@Override
	public void removeRecoveryRole(User user) {
		adminDAO.dropRoleMember(user, "SIDE_RECOVERY");	
	}
	
	@Override
	protected void addSpecialGrants(User user) {
		adminDAO.grantDbAccess(user);
	}

	@Override
	public void fillIsDatabaseUser(List<User> users) {
		List<String> databaseUsers = adminDAO.getDatabaseUsers();
		for(User user : users) {
			if(databaseUsers.contains(user.getUserName())) {
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
		adminDAO.changeAuthMethodForNonDbUsers(authMethod);

	}

}
