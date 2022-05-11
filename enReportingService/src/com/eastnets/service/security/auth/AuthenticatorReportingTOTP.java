package com.eastnets.service.security.auth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.eastnets.dao.commonUtilities.HashingUtility;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.service.common.PasswordChangeStatus;

public class AuthenticatorReportingTOTP implements Authenticator {

	static final Logger LOGGER = LogManager.getLogger(AuthenticatorReportingTOTP.class);
	private SecurityDAO securityDAO;

	@Override
	public int authenticationMethod() {
		return User.AUTH_REPORTING_TOTP;
	}

	@Override
	public boolean authenticate(User user, String domain) throws Exception {
		user.setUserName(user.getUserName().replaceAll("'", "''"));
		if (user.getUserName().equalsIgnoreCase("LSA") || user.getUserName().equalsIgnoreCase("RSA")) {

			boolean retValue = false;
			getSecurityDAO().authenticate(user);

			List<String> roles;

			roles = new ArrayList<String>();
			roles.add("SIDE_ADMIN_SECURITY");
			roles.add("SIDE_ADMIN");
			roles.add("SIDE_ADMIN_OPERATOR");

			user.getProfile().setProfileRoles(roles);

			retValue = true;

			return retValue;
		}

		if (domain != null && !domain.trim().isEmpty()) {
			LOGGER.info("Domain is not supported for database users");
		}

		boolean passwordAuthenticated = false;
		
		passwordAuthenticated = securityDAO.authenticateReportingUser(user);

		if (passwordAuthenticated) {
			user.getProfile().setProfileRoles(getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()));
			return true;
		} else
			return false;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user, String newPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {

		String hashedNewPassword=null;
		byte[] salt=null;
		try{
			//generate new salt for new password
			salt=HashingUtility.getSalt();
			
		if (passwordPolicy.getPasswordHistoryLenght() > 0) {
				//check if the new password and the old password are same
				if(user.getSalt() == null || user.getSalt().trim().isEmpty())
				{
					hashedNewPassword= AESEncryptDecrypt.encryptOneWay(newPassword);
				}else {
					hashedNewPassword = HashingUtility.getHashedPassword(newPassword, user.getSalt().getBytes());
				}		
			boolean passwordUsed = securityDAO.checkReportingUserPasswordUsed(user.getUserId(), hashedNewPassword,
					passwordPolicy.getPasswordHistoryLenght());
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
		securityDAO.resetSecretKey(user);
		securityDAO.setReportingUserPassword(user);
		return PasswordChangeStatus.SUCCESS;

	}

	@Override
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy,
			boolean unlockUser) {
		return this.changeUserPassword(user, newUserPassword, passwordPolicy, unlockUser);
	}

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}

	public void setRoles(User user) {
		user.getProfile().setProfileRoles(getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()));

	}

}
