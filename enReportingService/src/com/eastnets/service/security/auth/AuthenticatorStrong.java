package com.eastnets.service.security.auth;

import java.util.ArrayList;
import java.util.List;

import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.service.common.PasswordChangeStatus;

public class AuthenticatorStrong implements Authenticator {

	private SecurityDAO securityDAO;
	private ApplicationFeatures applicationFeatures;
	
	@Override
	public int authenticationMethod() {
		return User.AUTH_STRONG;
	}

	@Override
	public boolean authenticate(User user, String domain) throws Exception {
		
		boolean retValue= false;
		if(getApplicationFeatures().isNewSecurity()){
			List<String> roles;
			//if authenticator
			if ( user.getUserName().equalsIgnoreCase("LSA") || user.getUserName().equalsIgnoreCase("RSA") ){
				roles = new ArrayList<String>();
				roles.add( "SIDE_ADMIN_SECURITY");
				roles.add( "SIDE_ADMIN");
				roles.add( "SIDE_ADMIN_OPERATOR");
			}
			else{
				roles = getSecurityDAO().getProfileRoles(user.getProfile().getGroupId());
			}
			//
			user.getProfile().setProfileRoles( roles );
			
			//authentication success
			retValue= true;
		}
		else{
			getSecurityDAO().fillUserRoles(user);
			//authentication success
			retValue= true;
		}
		return retValue;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user,
			String newUserPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {
		throw new UnsupportedOperationException("Change user password during strong authenticaion not supported");
	}

	@Override
	public PasswordChangeStatus resetPassword(User user,
			String newUserPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {
		throw new UnsupportedOperationException("reset user password during strong authenticaion not supported");
	}

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}
	
	

}
