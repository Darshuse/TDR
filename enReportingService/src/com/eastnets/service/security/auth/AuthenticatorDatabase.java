package com.eastnets.service.security.auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.service.common.PasswordChangeStatus;

public class AuthenticatorDatabase implements Authenticator {
	
	private SecurityDAO securityDAO;
	private ApplicationFeatures applicationFeatures;
	protected static final Logger LOGGER = LogManager.getLogger(AuthenticatorDatabase.class);


	@Override
	public int authenticationMethod() {
		return User.AUTH_DB;
	}

	@Override
	public boolean authenticate(User user, String domain) throws SQLException {
		if ( domain !=null && !domain.trim().isEmpty() ){
			LOGGER.info("Domain is not supported for database users");
		}

		boolean retValue= false;
		getSecurityDAO().authenticate(user);
		
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
	public PasswordChangeStatus changeUserPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy, boolean unlockUser) {
		
		try {
			getSecurityDAO().changeUserDBPassword(user.getUserName(), newUserPassword, unlockUser);
		} catch (SQLException e) {
			throw new WebClientException(e);
		}

		return PasswordChangeStatus.SUCCESS;
	}
	
	@Override
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy,boolean unlockUser) {
		try {
			getSecurityDAO().resetPassword(user.getUserName(), newUserPassword, unlockUser);
		} catch (SQLException e) {
			throw new WebClientException(e);
		}

		return PasswordChangeStatus.SUCCESS;
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
