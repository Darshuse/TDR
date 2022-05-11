package com.eastnets.service.security.auth;


import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.Config;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.service.common.PasswordChangeStatus;
import com.eastnets.service.security.LdapAuthenticationHandler;

public class AuthenticatorLDAP implements Authenticator {
	
	private LdapAuthenticationHandler ldapAuthenticationHandler;

	private Config config;
	private SecurityDAO securityDAO;
	private ApplicationFeatures applicationFeatures;
	

	@Override
	public int authenticationMethod() {
		return User.AUTH_LDAP;
	}

	@Override
	public boolean authenticate(User user, String domain) throws Exception {
		String username = user.getUserName();

		String password = user.getPassword();

		boolean authenticated = ldapAuthenticationHandler.authenticate( username, password, domain );
		if ( !authenticated  ){
			return false;
		}
		if( !getApplicationFeatures().isNewSecurity() ){
			if (!user.isDatabaseUserOld()) {
				user.setUserRolesOld(config.getDefaultUserRolesList());
			}
		}
		else{
			user.getProfile().setProfileRoles( getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()) );
		}
		
		return true;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy, boolean unlockUser) {
		throw new WebClientException("Change password is unsupported for LDAP authnticated users.");
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
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
	
	public LdapAuthenticationHandler getLdapAuthenticationHandler() {
		return ldapAuthenticationHandler;
	}

	public void setLdapAuthenticationHandler(LdapAuthenticationHandler ldapAuthenticationHandler) {
		this.ldapAuthenticationHandler = ldapAuthenticationHandler;
	}

	@Override
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy, boolean unlockUser) {
		throw new WebClientException("Change password is unsupported for LDAP authnticated users.");
	}

}
