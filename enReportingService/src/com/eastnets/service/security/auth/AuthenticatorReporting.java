package com.eastnets.service.security.auth;

import java.util.Base64;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.eastnets.dao.commonUtilities.HashingUtility;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.service.common.PasswordChangeStatus;

public class AuthenticatorReporting implements Authenticator {

	private SecurityDAO securityDAO;
	protected static final Logger LOGGER = LogManager.getLogger(AuthenticatorReporting.class);

	@Override
	public int authenticationMethod() {
		return User.AUTH_REPORTING;
	}

	@Override
	public boolean authenticate(User user, String domain) {
		if (domain != null && !domain.trim().isEmpty()) {
			LOGGER.info("Domain is not supported for database users");
		}

		user.setUserName(user.getUserName().replaceAll("'", "''"));

		boolean authenticated = securityDAO.authenticateReportingUser(user);
		if (!authenticated) {
			return false;
		}
		user.getProfile().setProfileRoles(getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()));
		return true;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user, String newPassword, PasswordPolicy passwordPolicy, boolean unlockUser) {

		String hashedNewPassword = null;
		byte[] salt = null;
		// generate new salt for new password
		try {
			salt = HashingUtility.getSalt();

			if (passwordPolicy.getPasswordHistoryLenght() > 0) {

				// check if the new password and the old password are same
				if (user.getSalt() == null || user.getSalt().trim().isEmpty()) {
					hashedNewPassword = AESEncryptDecrypt.encryptOneWay(newPassword);
				} else {
					hashedNewPassword = HashingUtility.getHashedPassword(newPassword, user.getSalt().getBytes());
				}

				boolean passwordUsed = securityDAO.checkReportingUserPasswordUsed(user.getUserId(), hashedNewPassword, passwordPolicy.getPasswordHistoryLenght());
				if (passwordUsed) {
					return PasswordChangeStatus.PASSWORD_USED_BEFORE;
				}
			}
		} catch (Exception ex) {
			LOGGER.info("Password decryption failed : " + ex.getMessage());
			return PasswordChangeStatus.FAIL;
		}

		user.setPassword(newPassword);
		user.setSalt(Base64.getEncoder().encodeToString(salt));
		securityDAO.setReportingUserPassword(user);
		return PasswordChangeStatus.SUCCESS;
	}

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}

	@Override
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy, boolean unlockUser) {
		return this.changeUserPassword(user, newUserPassword, passwordPolicy, unlockUser);
	}

}
