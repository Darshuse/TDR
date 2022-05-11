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

package com.eastnets.service.security;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.admin.AdminDAO;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.dao.security.data.SecurityDataBean;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.dao.xml.XMLReaderDAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.BICPerProfile;
import com.eastnets.domain.admin.MsgCatPerProfile;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.domain.admin.UnitPerProfile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.admin.User.LoginStatus;
import com.eastnets.domain.security.UserApprovalStatusInfo;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.service.common.PasswordChangeStatus;
import com.eastnets.service.common.PasswordExpirationStatus;
import com.eastnets.service.common.PasswordPolicyHandler;
import com.eastnets.service.security.auth.Authenticator;

/**
 * Security Service Implementation
 * 
 * @author EastNets
 * @since July 15, 2012
 */
public class SecurityServiceImp implements SecurityService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1345295532559462444L;

	private SecurityDAO securityDAO;
	private CommonDAO commonDAO;
	private ViewerDAO viewerDAO;
	private XMLReaderDAO xmlReaderDAO;

	private AdminDAO adminDAO;
	private ApplicationFeatures applicationFeatures;

	private PasswordPolicyHandler passwordPolicyHandler;

	Map<Integer, Authenticator> authenticatorsMap = new HashMap<Integer, Authenticator>();

	static final Logger logger = Logger.getLogger(SecurityServiceImp.class);

	@Override
	public User authenticate(User user, String authenticationMethodFromConfig, boolean enableLdapCaseInsensitive) throws Exception {

		User dbUser = null;
		String domain = null;
		Integer authenticationMethod = null;
		authenticationMethod = user.getAuthenticationMethod();
		dbUser = getAdminDAO().getUser(user.getUserName());
		user.setUserName(user.getUserName().replaceAll("''", "'"));

		// TFS 32051 ignore case sensitivity checking for DB SQL users
		if (dbUser != null && dbUser.getAuthenticationMethod() != User.AUTH_DB && !dbUser.getUserName().equals(user.getUserName())) {
			if (enableLdapCaseInsensitive) {
				if (dbUser.getAuthenticationMethod() != User.AUTH_LDAP) {
					return null;
				}
			} else {
				return null;
			}
		}

		String username = user.getUserName();
		if (username.contains("\\")) {
			String[] userWithDomain = username.split("\\\\", 2);
			domain = userWithDomain[0];
			String usernameWithoutDomain = userWithDomain[1];
			if (dbUser == null) {
				dbUser = getAdminDAO().getUser(usernameWithoutDomain);
				username = usernameWithoutDomain;
			}
		}

		if (dbUser == null) {
			return null;
		}

		if (domain != null && !domain.isEmpty() && !dbUser.getUserName().equalsIgnoreCase(user.getUserName()) && dbUser.getAuthenticationMethod() != User.AUTH_LDAP) {
			throw new Exception("Domain only supported for LDAP users.");
		}

		dbUser.setPassword(user.getPassword());
		dbUser.setValidationKey(user.getValidationKey());
		dbUser.setSalt(user.getSalt());
		user = dbUser;

		Authenticator authentiocator = getAuthenticator(user, authenticationMethodFromConfig);

		if (authentiocator == null) {
			logger.error("Error : No Authenticator found for method " + user.getAuthenticationMethod());
			return null;
		}

		if ((((user.getAuthenticationMethod() == 5) && user.getSecretKey() != null)

				|| user.getAuthenticationMethod() == 3)

				&& (user.getValidationKey() == null || user.getValidationKey().isEmpty())) {

			logger.error("Key for RADIUS User or TOTP Users CANNOT be EMPTY !");
			return null;
		}

		try {
			boolean authenticated = authentiocator.authenticate(user, domain);
			if (!authenticated) {
				return null;
			}
		} catch (Exception ex) {
			user = null;
			if (ex.getMessage().contains("Reason:") || ex.getMessage().contains("the account is currently locked") || (ex.getMessage().contains("ORA") && !ex.getMessage().contains("ORA-01017"))) {
				throw ex;
			}
			logger.info("Info : Database Login error : " + ex.getMessage());
		}

		return user;
	}

	public User validateUserLoginStatus(User user) {
		if (user != null) {
			/*
			 * when dealing with domain names, we need the user name as it is stored in the database to be used to get the approval status message when the user/profile is not approved
			 */
			String usernameWithoutDomain = user.getUserName();
			if ((!usernameWithoutDomain.equalsIgnoreCase("LSA") && !usernameWithoutDomain.equalsIgnoreCase("RSA")) && !isUserApproved(usernameWithoutDomain)) {
				user.setLoginStatus(LoginStatus.LOGIN_USER_NOT_APPROVED);
				return user;
			}

			if (user.isDisabled()) {
				user.setLoginStatus(LoginStatus.LOGIN_USER_DISABLED);
				return user;
			}

			if (user.getExpirationDays() != null && user.getExpirationDays() <= 0) {
				user.setLoginStatus(LoginStatus.LOGIN_USER_EXPIRED);
				return user;
			}

			Pair<PasswordExpirationStatus, Integer> expirationStatus = new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.NOT_EXPIRED, 0);
			if (!usernameWithoutDomain.equalsIgnoreCase("LSA") && !usernameWithoutDomain.equalsIgnoreCase("RSA") && (user.getAuthenticationMethod() == User.AUTH_REPORTING || user.getAuthenticationMethod() == User.AUTH_REPORTING_TOTP)) {
				// check if the password is expired

				expirationStatus = getPasswordExpirationStatus(usernameWithoutDomain, user.getUserId(), user.getAuthenticationMethod());
				boolean autoGenerated = isAutoGeneratedPassword(usernameWithoutDomain, user.getUserId(), user.getAuthenticationMethod());
				if (autoGenerated && expirationStatus.getValue() >= 1) {
					user.setLoginStatus(LoginStatus.AUTOGENERATED_PASSWORD_EXPIRED);
					return user;
				}
				if (expirationStatus.getKey() == PasswordExpirationStatus.EXPIRED || autoGenerated) {
					user.setLoginStatus(LoginStatus.LOGIN_PASSWORD_EXPIRED);
					return user;
				}
			}
			user.setLoginStatus(LoginStatus.LOGIN_SUCCESS);
		}
		return user;
	}

	public User getLastLogIn(String userName) {
		userName = userName.replaceAll("'", "''");
		return securityDAO.getLastLogIn(userName);
	}

	@Override
	public String getSecretKey(String username) {
		String secretKey = securityDAO.getSecretKey(username);

		if (secretKey == null) {
			return null;
		}

		String decryptedSecretKey = "";

		try {

			decryptedSecretKey = AESEncryptDecrypt.decrypt(secretKey);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedSecretKey;
	}

	@Override
	public PasswordChangeStatus changeUserPassword(User user, String newUserPassword, String authenticationMethod, boolean enableLdapCaseInsensitive) throws Exception {

		try {
			User dbUser = authenticate(user, authenticationMethod, enableLdapCaseInsensitive);
			if (dbUser == null) {
				return PasswordChangeStatus.USER_NOT_FOUND;
			}
			user.setUserId(dbUser.getUserId());
		} catch (Exception ex) {
			boolean ignoreException = false;
			// SqlServer account password expired
			if (ex.getMessage().contains("Reason: The password of the account must be changed")) {
				ignoreException = true;
			}

			// Oracle password expired
			if (ex.getMessage().contains("ORA-28001")) {
				ignoreException = true;
			}

			if (!ignoreException) {
				throw ex;
			}
		}
		PasswordChangeStatus status = PasswordChangeStatus.SUCCESS;
		if (user.getAuthenticationMethod() == User.AUTH_REPORTING || user.getAuthenticationMethod() == User.AUTH_REPORTING_TOTP) {

			status = passwordPolicyHandler.validatePassword(user.getUserName(), newUserPassword);
		}

		if (status != PasswordChangeStatus.SUCCESS) {
			return status;
		}

		Authenticator authentiocator = getAuthenticator(user, authenticationMethod);
		if (authentiocator == null) {
			logger.error("Error : No Authenticator found for method " + user.getAuthenticationMethod());
			return PasswordChangeStatus.AUTHENTICATOR_NOT_FOUND;
		}
		return authentiocator.changeUserPassword(user, newUserPassword, passwordPolicyHandler.getPolicy(), false);
	}

	@Override
	public String resetUserPassword(String userName, User user, String authenticationMethod) {
		String newPassword = passwordPolicyHandler.getRandomString();
		newPassword = generateAndValidatePassword(userName, user.getUserName());

		Authenticator authentiocator = getAuthenticator(user, authenticationMethod);
		if (authentiocator == null) {
			throw new WebClientException("No Authenticator found for method " + user.getAuthenticationMethod());
		}
		user.setAutoGeneratedPassword(true);
		PasswordChangeStatus status = null;
		status = authentiocator.resetPassword(user, newPassword, passwordPolicyHandler.getPolicy(), true);
		if (status == PasswordChangeStatus.SUCCESS) {
			return newPassword;
		}
		if (status == PasswordChangeStatus.PASSWORD_USED_BEFORE) {
			throw new WebClientException("New password should not be in the previous " + passwordPolicyHandler.getPolicy().getPasswordHistoryLenght() + " passwords");
		}
		return null;
	}

	@Override
	public String generateAndValidatePassword(String username, String passwordUsername) {
		PasswordChangeStatus status = PasswordChangeStatus.INVALID_CHAR;
		int loopBreak = 0;
		String newPassword = "";
		do {
			newPassword = passwordPolicyHandler.getNewPolicyPassword();
			status = passwordPolicyHandler.validatePassword(passwordUsername, newPassword);

			if (loopBreak++ > 3) {
				logger.info(loopBreak + "th trial to generate a valid password, generation status: " + status.toString());
			}
			if (loopBreak > 10) {
				logger.info("Unable to generate a valid password, last generated password will be used without validation");
				status = PasswordChangeStatus.SUCCESS;
				break;
			}
		} while (status != PasswordChangeStatus.SUCCESS);
		return newPassword;
	}

	@Override
	public boolean isUserApproved(String username) throws WebClientException {
		UserApprovalStatusInfo info = getUserAprovalStatus(username);
		return (info != null && info.getStatus() == 0) ? true : false;
	}

	@Override
	public String getUserApprovalDescription(String userName) throws WebClientException {
		UserApprovalStatusInfo info = getUserAprovalStatus(userName);
		return (info != null && info.getDescription() != null) ? info.getDescription() : "";
	}

	@Override
	public void updateLastLogin(String username, long userId, boolean isDBUser, String type) {

		getSecurityDAO().updateLastLogin(userId, isDBUser, type);

	}

	@Override
	public void updateSecretKey(String username, String secretKey) {

		String encryptedSecretKey = "";
		try {
			encryptedSecretKey = AESEncryptDecrypt.encrypt(secretKey);

		} catch (Exception e) {
			e.printStackTrace();
		}
		getSecurityDAO().updateSecretKey(username, encryptedSecretKey);
	}

	@Override
	public boolean isAutogenerationPasswordExpired(String loggedInUsername, long userId, int userType) {
		if (isAutoGeneratedPassword(loggedInUsername, userId, userType)) {
			Date passwordResetDate = securityDAO.getPasswordResetDate(userId, userType);
			Date now = new Date();
			if (passwordResetDate != null && now.after(passwordResetDate)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Pair<PasswordExpirationStatus, Integer> getPasswordExpirationStatus(String loggedInUsername, long userId, int userType) {

		Date passwordResetDate = securityDAO.getPasswordResetDate(userId, userType);

		if (passwordResetDate == null) {
			logger.info("Password Expiration date is not set for user " + userId + ", user password will be treated as expired");
			return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.NOT_EXPIRED, 9999);
		}

		// make time to be static for the last second of today
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date todayDate = calendar.getTime();

		boolean isAutoGenerated = isAutoGeneratedPassword(loggedInUsername, userId, userType);
		/*
		 * Handle the auto generated password expiration
		 */
		if (isAutoGenerated) {
			if (passwordResetDate != null) {
				long diffInMills = passwordResetDate.getTime() - todayDate.getTime();
				long diffInDays = TimeUnit.DAYS.convert(diffInMills, TimeUnit.MILLISECONDS);
				if (diffInDays < 0) { // password expired
					return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.EXPIRED, 2);
				}
			}
		}

		if (passwordPolicyHandler.getPolicy().getPasswordExpirationInDays() == 0) {
			return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.DISABLED, 0);
		}

		/*
		 * Handle the password expiration and warning policy if it's required by client
		 */
		// convert the password expiration in days to date
		Calendar passwordExpirationCalendar = GregorianCalendar.getInstance();
		passwordExpirationCalendar.setTime(passwordResetDate);
		passwordExpirationCalendar.add(Calendar.DAY_OF_MONTH, passwordPolicyHandler.getPolicy().getPasswordExpirationInDays());
		// make time to be static for the last second of password expiration
		// date
		passwordExpirationCalendar.set(Calendar.HOUR_OF_DAY, 23);
		passwordExpirationCalendar.set(Calendar.MINUTE, 59);
		passwordExpirationCalendar.set(Calendar.SECOND, 59);
		passwordExpirationCalendar.set(Calendar.MILLISECOND, 0);
		// calculate the password expiration date
		Date passwordExpirationDate = passwordExpirationCalendar.getTime();
		long diffInMills = passwordExpirationDate.getTime() - todayDate.getTime();
		long diffInDays = 0;
		if (passwordExpirationDate.equals(todayDate)) {
			diffInDays = -1;
		} else {
			diffInDays = TimeUnit.DAYS.convert(diffInMills, TimeUnit.MILLISECONDS);
		}

		if (diffInDays < 0) { // password expired
			return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.EXPIRED, 2);
		}
		/*
		 * If the warn in days equals to zero; that means the client does not care about the warning of the password expiration for his end users
		 */
		if (passwordPolicyHandler.getPolicy().getPasswordExpirationWarningInDays() != 0 && (diffInDays <= passwordPolicyHandler.getPolicy().getPasswordExpirationWarningInDays())) {
			return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.WARN, (int) diffInDays);
		}

		return new Pair<PasswordExpirationStatus, Integer>(PasswordExpirationStatus.NOT_EXPIRED, 1);
	}

	@Override
	public boolean isAutoGeneratedPassword(String username, Long userId, int userType) {
		return getSecurityDAO().isAutoGeneratedPassword(userId, userType);
	}

	@Override
	public ProfileDetails getProfileDetails(String loggedInUser, Profile profile) {
		if (profile == null) {
			return null;
		}

		ProfileDetails profileDetails = new ProfileDetails(profile);

		Long profileId = profile.getGroupId();
		profile.setProfileRoles(getSecurityDAO().getProfileRoles(profileId));
		profileDetails.setAuthorizedBICCodes(getSelectedBICsPerProfile(profileId));
		profileDetails.setAuthorizedMessageCategories(getMessageCategoriesAsString(getSelectedMsgCatPerProfile(profileId)));
		profileDetails.setAuthorizedReportingActions(getActionsAsString(getReportingAuthorizedActions(loggedInUser, profileId)));
		profileDetails.setAuthorizedUnits(getSelecteUnitsPerProfile(profileId));
		profileDetails.setAuthorizedViewerActions(getActionsAsString(getViewerAuthorizedActions(loggedInUser, profileId)));
		profileDetails.setAuthorizedWatchdogActions(getActionsAsString(getWatchdogAuthorizedActions(loggedInUser, profileId)));
		profileDetails.setAuthorizedDashboardActions(getActionsAsString(getDashboardAuthorizedActions(loggedInUser, profileId)));
		profileDetails.setAuthorizedBusinessIntegellenceActions(getActionsAsString(getgetBusinessIntegellenceAuthorizedActions(loggedInUser, profileId)));
		return profileDetails;
	}

	@Override
	public List<String> getProfileRoles(Long profileID) {
		return getSecurityDAO().getProfileRoles(profileID);
	}

	@Override
	public List<ProfileDetails> getProfileDetails(String loggedInUser, List<Profile> selectedProfiles) {
		List<ProfileDetails> profilesDetails = new ArrayList<ProfileDetails>();
		for (Profile profile : selectedProfiles) {
			ProfileDetails profileDetails = new ProfileDetails(profile);

			Long profileId = profile.getGroupId();
			profile.setProfileRoles(getSecurityDAO().getProfileRoles(profileId));
			profileDetails.setAuthorizedBICCodes(getSelectedBICsPerProfile(profileId));
			profileDetails.setAuthorizedMessageCategories(getMessageCategoriesAsString(getSelectedMsgCatPerProfile(profileId)));
			profileDetails.setAuthorizedReportingActions(getActionsAsString(getReportingAuthorizedActions(loggedInUser, profileId)));
			profileDetails.setAuthorizedUnits(getSelecteUnitsPerProfile(profileId));
			profileDetails.setAuthorizedViewerActions(getActionsAsString(getViewerAuthorizedActions(loggedInUser, profileId)));
			profileDetails.setAuthorizedWatchdogActions(getActionsAsString(getWatchdogAuthorizedActions(loggedInUser, profileId)));
			profileDetails.setAuthorizedDashboardActions(getActionsAsString(getDashboardAuthorizedActions(loggedInUser, profileId)));
			profileDetails.setAuthorizedDashboardActions(getActionsAsString(getgetBusinessIntegellenceAuthorizedActions(loggedInUser, profileId)));
			profilesDetails.add(profileDetails);
		}
		return profilesDetails;
	}

	private List<String> getSelectedBICsPerProfile(Long profileId) {
		List<BICPerProfile> bicsPerProfile = getSecurityDAO().getBICsPerProfile(profileId, BICPerProfile.SELECTED_BIC_CODE);
		List<String> bics = new ArrayList<String>();
		for (BICPerProfile bicPerProfile : bicsPerProfile) {
			bics.add(bicPerProfile.getBicCode());
		}
		return bics;
	}

	private List<String> getSelecteUnitsPerProfile(Long profileId) {
		List<UnitPerProfile> unitsPerProfile = getSecurityDAO().getUnitsPerProfile(profileId, UnitPerProfile.SELECTED_UNIT);
		List<String> units = new ArrayList<String>();
		for (UnitPerProfile unitPerProfile : unitsPerProfile) {
			units.add(unitPerProfile.getUnit());
		}
		return units;
	}

	private List<MsgCatPerProfile> getSelectedMsgCatPerProfile(Long profileId) {
		return getSecurityDAO().getMsgCatPerProfile(profileId, MsgCatPerProfile.SELECTED_MSG_CAT);
	}

	private List<String> getMessageCategoriesAsString(List<MsgCatPerProfile> values) {
		List<String> list = new ArrayList<>();

		for (MsgCatPerProfile msgCatPerProfile : values) {
			list.add(msgCatPerProfile.getMsgCat());
		}

		return list;
	}

	private List<String> getActionsAsString(List<Action> actions) {
		List<String> list = new ArrayList<String>();

		for (Action action : actions) {
			list.add(action.getActionId().toString());
		}

		return list;
	}

	private List<Action> getWatchdogAuthorizedActions(String profile, Long groupId) {
		return getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_WATCHDOG, groupId);
	}

	private List<Action> getReportingAuthorizedActions(String profile, Long groupId) {
		return getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_REPORTING, groupId);
	}

	private List<Action> getViewerAuthorizedActions(String profile, Long groupId) {
		return getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_VIEWER, groupId);
	}

	private List<Action> getDashboardAuthorizedActions(String profile, Long groupId) {
		return getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_DASHBOARD, groupId);
	}

	private List<Action> getgetBusinessIntegellenceAuthorizedActions(String profile, Long groupId) {
		return getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_Business_Integellence, groupId);
	}

	private Authenticator getAuthenticator(User user, String authenticationMethodFromConfig) {
		/*
		 * If the authentication method in the application configuration is Strong, that means we don't care about user authentication method type, in other words the authentication managed by SSL
		 * through signed certificate
		 */
		if (authenticationMethodFromConfig != null && authenticationMethodFromConfig.equalsIgnoreCase("strong")) {
			return authenticatorsMap.get(User.AUTH_STRONG);
		}

		return authenticatorsMap.get(user.getAuthenticationMethod());// might
																		// return
																		// null
																		// if
																		// user.getAuthenticationMethod()
																		// is
																		// not
																		// mapped
	}

	private UserApprovalStatusInfo getUserAprovalStatus(String userName) {
		UserApprovalStatusInfo info = null;
		try {
			boolean fourEyesEnabled = commonDAO.is4EyeFeatureEnabled();
			if (fourEyesEnabled) {
				info = securityDAO.checkUserApproval(userName);
			} else {
				info = securityDAO.getUserApprovedStatus();
			}
		} catch (SQLException e) {
			throw new WebClientException(e);
		}
		return info;
	}

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public AdminDAO getAdminDAO() {
		return adminDAO;
	}

	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	public PasswordPolicyHandler getPasswordPolicyHandler() {
		return passwordPolicyHandler;
	}

	public void setPasswordPolicyHandler(PasswordPolicyHandler passwordPolicyHandler) {
		this.passwordPolicyHandler = passwordPolicyHandler;
	}

	public void setAuthenticators(List<Authenticator> authenticators) {
		if (authenticators == null) {
			return;
		}
		for (Authenticator authenticator : authenticators) {
			authenticatorsMap.put(authenticator.authenticationMethod(), authenticator);
		}
	}

	@Override
	public void setTOTPRoles(User user) {
		user.getProfile().setProfileRoles(getSecurityDAO().getProfileRoles(user.getProfile().getGroupId()));
	}

	@Override
	public User getNumberOfPasswordAttempts(String userName) {
		return securityDAO.getNumberOfPasswordAttempts(userName);
	}

	@Override
	public void updateNumberOfPasswordAttempts(String userName, Long userId, int curuntAttemptsNumber) {
		securityDAO.updateNumberOfPasswordAttempts(userName, userId, curuntAttemptsNumber);
	}

	@Override
	public void makeUserDisabel(String userName, Long userId, int curuntAttemptsNumber) {
		securityDAO.makeUserDisabel(userName, userId, curuntAttemptsNumber);

	}

	@Override
	public LoginStatus getLoginStatus(User user) {

		user.setUserName(user.getUserName().replaceAll("''", "'"));
		String username = user.getUserName();
		Long userID = user.getUserId();
		Integer authenticationMethod = user.getAuthenticationMethod();
		boolean autoGenerated = isAutoGeneratedPassword(username, userID, authenticationMethod);

		if (user.getAuthenticationMethod() == User.AUTH_DB) {
			user.setCanRunLoader(hasLoaderRole(user));
		}
		if ((!username.equalsIgnoreCase("LSA") && !username.equalsIgnoreCase("RSA")) && !isUserApproved(username)) {
			return LoginStatus.LOGIN_USER_NOT_APPROVED;
		}

		if (user.isDisabled()) {
			return LoginStatus.LOGIN_USER_DISABLED;
		}

		if (user.getExpirationDays() != null && user.getExpirationDays() <= 0) {
			return LoginStatus.LOGIN_USER_EXPIRED;
		}

		Pair<PasswordExpirationStatus, Integer> expirationStatus = new Pair<>(PasswordExpirationStatus.NOT_EXPIRED, 0);

		if (!username.equalsIgnoreCase("LSA") && !username.equalsIgnoreCase("RSA")) {

			if ((authenticationMethod != User.AUTH_REPORTING && authenticationMethod != User.AUTH_REPORTING_TOTP) && isAutogenerationPasswordExpired(username, userID, authenticationMethod)) {

				return LoginStatus.AUTOGENERATED_PASSWORD_EXPIRED;
			}

			// check if the password is expired
			if ((authenticationMethod == User.AUTH_REPORTING) || (authenticationMethod == User.AUTH_REPORTING_TOTP)) {

				expirationStatus = getPasswordExpirationStatus(username, userID, authenticationMethod);

				if (autoGenerated && expirationStatus.getValue() >= 1 && expirationStatus.getKey() == PasswordExpirationStatus.EXPIRED) {
					return LoginStatus.AUTOGENERATED_PASSWORD_EXPIRED;
				}

				if (expirationStatus.getKey() == PasswordExpirationStatus.EXPIRED || autoGenerated) {
					return LoginStatus.LOGIN_PASSWORD_EXPIRED;
				}

				if (user.getSalt() == null || user.getSalt().trim().isEmpty()) {
					return LoginStatus.LOGIN_PASSWORD_EXPIRED;

				}
			}

			if (user.getAuthenticationMethod() == User.AUTH_DB && autoGenerated) {
				return LoginStatus.LOGIN_PASSWORD_EXPIRED;
			}

		}

		if (autoGenerated) {
			return LoginStatus.LOGIN_PASSWORD_EXPIRED;
		}

		if (user.getAuthenticationMethod() == 5) {
			return LoginStatus.TWO_FACTOR_AUTH;
		}

		return null;
	}

	public boolean hasLoaderRole(User user) {
		if (!getAdminDAO().isDataBaseUser(user.getUserName())) {
			return false;
		}
		List<String> roles = getAdminDAO().getUserRoles(user.getUserName());
		return roles.contains("SIDE_LOADER") && roles.contains("SIDE_OPER");
	}

	@Override
	public String getUserSalt(User user) {
		return securityDAO.getUserSalt(user);

	}

	@Override
	public List<String> getBIAuthrizationList(Long profileID) {
		return getActionsAsString(getSecurityDAO().getAuthorizedActions(Constants.MODULE_ID_Business_Integellence, profileID));
	}

	public ViewerDAO getViewerDAO() {
		return viewerDAO;
	}

	public void setViewerDAO(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	public XMLReaderDAO getXmlReaderDAO() {
		return xmlReaderDAO;
	}

	public void setXmlReaderDAO(XMLReaderDAO xmlReaderDAO) {
		this.xmlReaderDAO = xmlReaderDAO;
	}

	@Override
	public void fillSecurityDataBean(SecurityDataBean securityDataBean) {
		viewerDAO.fillSecurityDataBean(securityDataBean);
	}

	public void fillLoggedInUserName(String loggedInUser) {
		xmlReaderDAO.fillLoggedInUserName(loggedInUser);
	}

}
