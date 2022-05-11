package com.eastnets.service.security.auth;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.Config;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.service.common.PasswordChangeStatus;
import com.eastnets.service.security.RadiusAuthenticationHandler;

public class AuthenticatorRADIUS implements Authenticator {

	private String configFile;
	private Config config;
	private SecurityDAO securityDAO;
	private ApplicationFeatures applicationFeatures;
	private RadiusAuthenticationHandler radiusAuthenticationHandler;

	@Override
	public int authenticationMethod() {
		return User.AUTH_RADIUS;
	}

	@Override
	public boolean authenticate(User user, String domain) throws Exception {

		boolean authenticated = radiusAuthenticationHandler.authenticate(user.getUserName(), user.getPassword(),
				user.getValidationKey(), domain);

		if (!authenticated) {

			return false;
		}

		user.getProfile().setProfileRoles(getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()));

		return true;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {
		throw new WebClientException("Change password is unsupported for RADIUS authnticated users.");
	}

	@Override
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {
		throw new WebClientException("Change password is unsupported for RADIUS authnticated users.");
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

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public RadiusAuthenticationHandler getRadiusAuthenticationHandler() {
		return radiusAuthenticationHandler;
	}

	public void setRadiusAuthenticationHandler(RadiusAuthenticationHandler radiusAuthenticationHandler) {
		this.radiusAuthenticationHandler = radiusAuthenticationHandler;
	}

}
