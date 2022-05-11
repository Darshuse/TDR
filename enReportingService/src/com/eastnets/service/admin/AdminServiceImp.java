package com.eastnets.service.admin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eastnets.dao.admin.AdminDAO;
import com.eastnets.dao.common.ApplicationFeatures;
import com.eastnets.dao.common.Constants;
import com.eastnets.dao.commonUtilities.HashingUtility;
import com.eastnets.dao.license.LicenseDAO;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.AdminSettings;
import com.eastnets.domain.admin.CsmConnection;
import com.eastnets.domain.admin.JIResourceFolder;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.LoaderSettings;
import com.eastnets.domain.admin.MsgCatPerProfile;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.domain.admin.SaveUserEnum;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.admin.UserThemePreferences;
import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.service.reports.ReportGenerator;
import com.eastnets.service.util.AdminUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Administration Service Implementation
 * 
 * @author EastNets
 * @since July 22, 2012
 */
public abstract class AdminServiceImp extends ServiceBaseImp implements AdminService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7332328963234392642L;
	private ApplicationFeatures applicationFeatures;
	private SecurityDAO securityDAO;

	public abstract AdminDAO getAdminDAO();

	public abstract void setAdminDAO(AdminDAO adminDAO);

	public abstract LicenseDAO getLicenseDAO();

	public abstract void setLicenseDAO(LicenseDAO licenseDAO);

	protected abstract void removeUserRoles(User user);

	protected abstract void addUserRoles(User user);

	protected abstract void addSpecialGrants(User user);

	protected abstract void removeUserLoaderRole(User user);

	protected abstract void addUserLoaderRole(User user);

	public abstract void fillIsDatabaseUser(List<User> users);

	static final Logger logger = Logger.getLogger(AdminServiceImp.class);

	public ApplicationFeatures getApplicationFeatures() {
		return applicationFeatures;
	}

	public void setApplicationFeatures(ApplicationFeatures applicationFeatures) {
		this.applicationFeatures = applicationFeatures;
	}

	/*
	 * add any validation criteria to Loader Connections for insertion and updating transactions
	 */
	private boolean isValidLoaderConnection(LoaderConnection laoderConnection) {

		return (laoderConnection.getTimeOut() > 1 && laoderConnection.getTimeOut() < 999);
	}

	@Override
	public LoaderSettings getLoaderSettings(String profile) {
		LoaderSettings loaderSettings = getAdminDAO().getLoaderSettings();
		List<String> availableTypes = getAdminDAO().getAvailableMessageTypes();
		loaderSettings.setAvailableMessageTypes(availableTypes);
		List<String> selectedTypes = getAdminDAO().getSelectedMessageTypes();
		loaderSettings.setSelectedMessageTypes(selectedTypes);
		Integer disableEndTime = loaderSettings.getDisableStartTime() + loaderSettings.getDisableDuration();
		if (disableEndTime > 1440) {
			disableEndTime = disableEndTime - 1440;
		} else if (disableEndTime == 1440) {
			disableEndTime = disableEndTime - 1;
		}
		loaderSettings.setDisableEndTime(disableEndTime);

		return loaderSettings;
	}

	@Override
	public List<String> getSelectedMessageTypes() {
		// TODO Auto-generated method stub
		return getAdminDAO().getSelectedMessageTypes();
	}

	@Override
	public void updateLoaderSettings(String profile, LoaderSettings loaderSettings, LoaderSettings oldLoaderSetting) {
		Integer disableDuration;
		if (loaderSettings.getDisableStartTime() > loaderSettings.getDisableEndTime()) {
			disableDuration = 1440 - (loaderSettings.getDisableStartTime() - loaderSettings.getDisableEndTime());
		} else {
			disableDuration = loaderSettings.getDisableEndTime() - loaderSettings.getDisableStartTime();
		}
		Integer parseTextBlock = 2;
		List<String> types = getAdminDAO().getSelectedMessageTypes();

		int prevMesgCount = 0;
		if (types != null) {
			prevMesgCount = types.size();
		}

		if ((loaderSettings.getAvailableMessageTypes().size() + prevMesgCount) - loaderSettings.getSelectedMessageTypes().size() == 0) {
			parseTextBlock = 1;
		} else if (loaderSettings.getSelectedMessageTypes().isEmpty()) {
			parseTextBlock = 0;
		}

		loaderSettings.setDisableDuration(disableDuration);
		loaderSettings.setParseTextBlock(parseTextBlock);

		getAdminDAO().updateLoaderSettings(loaderSettings);
		List<String> selectedMessageTypes = loaderSettings.getSelectedMessageTypes();
		getAdminDAO().deleteLdParseMsgeTypes();
		if (!selectedMessageTypes.isEmpty()) {
			for (String type : selectedMessageTypes)
				getAdminDAO().addLdParseMsgeType(type);
		}

		List<String> availableTypes = getAdminDAO().getAvailableMessageTypes();
		loaderSettings.setAvailableMessageTypes(availableTypes);

	}

	@Override
	public List<BicLicenseInfo> getLicensedBICs(String loggedInUser) {
		return getLicenseDAO().getBicLicenseInfo();
	}

	@Override
	public List<String> getBICs() {
		return getAdminDAO().getLicensedBICs();
	}

	@Override
	public void addBIC(String loggedInUser, String bic) {
		getAdminDAO().addBIC(bic);

	}

	@Override
	public void deleteBIC(String loggedInUser, String bic) {
		getAdminDAO().deleteBIC(bic);

	}

	@Override
	public List<String> getAllUnits(String loggedInUser) {
		return getAdminDAO().getAllUnits();
	}

	@Override
	public List<Profile> getProfiles(String loggedInUser) {
		return getAdminDAO().getProfiles();
	}

	@Override
	public List<Profile> getProfilesByProfileName(String loggedInUser, String profileName) {
		profileName = getDBSafeString(profileName);
		return getAdminDAO().getProfiles(profileName);
	}

	@Override
	public List<LoaderConnection> getLoaderConnections(String profile, Integer maxConnection) {

		List<LoaderConnection> connections = new ArrayList<LoaderConnection>();
		List<LoaderConnection> allConnections = getAdminDAO().getLoaderConnections();
		// the system returns number of connections based on predefined setting
		// for maximum connections number
		for (int i = 0; i < maxConnection; i++) {

			if (i >= allConnections.size())
				break;

			connections.add(allConnections.get(i));
		}

		return connections;
	}

	@Override
	public List<LoaderConnection> getLoaderConnections(String loggedInUser) {

		return getAdminDAO().getLoaderConnections();
	}

	@Override
	public LoaderConnection getLoaderConnection(String loggedInUser, Long aid) {
		return getAdminDAO().getLoaderConnection(aid);
	}

	@Override
	public void addLoaderConnection(String loggedInUser, LoaderConnection loaderConnection) {

		if (isValidLoaderConnection(loaderConnection)) {
			getAdminDAO().addLoaderConnection(loaderConnection);
		}

	}

	@Override
	public void updateLoaderConnection(String loggedInUser, LoaderConnection loaderConnection, LoaderConnection oldLoaderConnection) {
		if (isValidLoaderConnection(loaderConnection)) {
			getAdminDAO().updateLoaderConnection(loaderConnection);
		}

	}

	@Override
	public List<Action> getWatchdogAvailableActions(String profile) {
		return getAdminDAO().getAvailableActions(Constants.MODULE_ID_WATCHDOG);
	}

	@Override
	public List<Action> getReportingAvailableActions(String profile) {
		return getAdminDAO().getAvailableActions(Constants.MODULE_ID_REPORTING);
	}

	@Override
	public List<Action> getViewerAvailableActions(String profile, License license) {

		List<Action> actions = getAdminDAO().getAvailableActions(Constants.MODULE_ID_VIEWER);
		boolean licenseExist = false;
		for (Product product : license.getProducts()) {
			if (product.getDescription().equals("Dynamic Report") && product.isLicensed()) {
				licenseExist = true;
				break;
			}
		}

		if (!licenseExist) {
			for (Action action : actions) {
				if (action.getActionName().equals("Generate Reports")) {
					actions.remove(action);
					break;
				}

			}
		}

		return actions;
	}

	@Override
	public List<Action> getDashboardAvailableActions(String profile) {
		return getAdminDAO().getAvailableActions(Constants.MODULE_ID_DASHBOARD);
	}

	@Override
	public List<Action> getBusinessIntegellenceAvailableActions(String loggedInUser) {
		return getAdminDAO().getAvailableActions(Constants.MODULE_ID_Business_Integellence);
	}

	@Override
	public void syncLoaderConnection(String profile, LoaderConnection loaderConnection, Date date) {
		getAdminDAO().syncLoaderConenction(loaderConnection, date);
	}

	@Override
	public void syncLoaderConnection(String profile, LoaderConnection loaderConnection) {
		getAdminDAO().syncLoaderConenction(loaderConnection);

	}

	@Override
	public boolean checkIfMessageExists(LoaderConnection loaderConnection, Date date) throws Exception {

		boolean dateExist = false;

		List<Date> maxMesgCreationDate = getAdminDAO().getMaxMesgCreationDatebteween(loaderConnection, date);

		if (maxMesgCreationDate == null || maxMesgCreationDate.isEmpty() || maxMesgCreationDate.get(0) == null) {

			maxMesgCreationDate = getAdminDAO().getMaxMesgCreationDate(loaderConnection, date);

		}

		if (maxMesgCreationDate != null && !maxMesgCreationDate.isEmpty() && maxMesgCreationDate.get(0) != null)
			dateExist = true;

		return dateExist;
	}

	@Override
	public List<User> getUsers(String loggedInUser) {
		List<User> actualUsers = new ArrayList<User>();
		List<User> users = getAdminDAO().getUsers();

		fillIsDatabaseUser(users);
		for (User user : users) {
			if (user.getAuthenticationMethod() == User.AUTH_DB) {
				user.setCanRunLoader(hasLoaderRole(user));
			}
		}

		// Remove LSA and RSA From user List

		for (User user : users) {
			if (!user.getUserName().equals("RSA") && !user.getUserName().equals("LSA"))
				actualUsers.add(user);
		}
		return actualUsers;
	}

	@Override
	public User getValidUser(String userName) {
		AdminDAO adminDAO = getAdminDAO();
		User user = adminDAO.getUser(userName);
		if (user == null)
			return null;
		return user;
	}

	@Override
	public void setRoles(User user) {
		AdminDAO adminDAO = getAdminDAO();
		List<String> userRoles = adminDAO.getUserRoles(user);
		user.setUserRolesOld(userRoles);
	}

	@Override
	public List<User> getUsersByFilter(String loggedUserName, String userName, String profileName) {
		AdminDAO adminDAO = getAdminDAO();
		userName = getDBSafeString(userName);
		profileName = getDBSafeString(profileName);
		List<User> users = adminDAO.getUsers(userName, profileName);

		fillIsDatabaseUser(users);
		for (User user : users) {
			if (user.getAuthenticationMethod() == User.AUTH_DB) {
				user.setCanRunLoader(hasLoaderRole(user));
			}
		}
		return users;
	}

	protected String getDBSafeString(String str) {
		if (str == null) {
			return "";
		}
		return str.replace("'", "''");
	}

	@Override
	public User getUser(String userName) {

		AdminDAO adminDAO = getAdminDAO();
		User user = adminDAO.getUser(userName);
		if (user != null) {

			boolean databaseUser = user.getAuthenticationMethod() == User.AUTH_DB;

			if (databaseUser) {
				user.setCanRunLoader(hasLoaderRole(user));
			}

		}

		return user;
	}

	@Override
	public User getUser(String loggedInUser, Long userId) {
		AdminDAO adminDAO = getAdminDAO();
		User user = adminDAO.getUser(userId);
		if (user != null) {
			boolean databaseUser = user.getAuthenticationMethod() == User.AUTH_DB;

			if (databaseUser) {
				user.setDatabaseUserOld(getAdminDAO().isDataBaseUser(user.getUserName()));
				user.setCanRunLoader(hasLoaderRole(user));
			}
		}
		return user;
	}

	@Override
	public void updateUser(String loggedInUser, User user, User oldUser, Profile userProfile) {
		// we can't remove the roles of the generic user
		if (user.getUserName().trim().equalsIgnoreCase("generic")) {
			return;
		}

		User dbUser = getAdminDAO().getUser(user.getUserName());

		/*
		 * Added By Mohammad Alzarai to allow update user type from DB to Report User only
		 */

		boolean authenticationMethodChanged = (dbUser.getAuthenticationMethod().equals(user.getAuthenticationMethod()));
		getAdminDAO().updateUser(user);

		boolean databaseUser = user.isDatabaseUser();

		if (databaseUser) {
			if (user.isCanRunLoader()) {
				addUserLoaderRole(user);
			} else {
				removeUserLoaderRole(user);
			}

		}

		if (!authenticationMethodChanged && user.getAuthenticationMethod() != 2) {

			/*
			 * to allow update user type from DB and create password for Reporting User only
			 */
			if (user.getAuthenticationMethod() == 0 || user.getAuthenticationMethod() == 5) {
				user.setUserId(dbUser.getUserId());
				byte[] salt = null;
				try {
					salt = HashingUtility.getSalt();
					user.setSalt(Base64.getEncoder().encodeToString(salt));
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getSecurityDAO().setReportingUserPassword(user);
			}

			/*
			 * Drop DB user
			 */
			getAdminDAO().deleteDatabaseUser(user);
		}
	}

	protected List<String> getUserRoles(User user) {

		return this.getAdminDAO().getUserRoles(user);
	}

	@Override
	public List<Pair<String, String>> getRoles(String loggedInUser) {
		return this.getSecurityDAO().getRoles();
	}

	@Override
	public User updateUserApprovalStatus(String loggedInUser, User user, boolean isApproved) {

		Long newStatus = user.getApprovalStatus().getId();
		if (loggedInUser != null && (loggedInUser.equalsIgnoreCase("LSA") || loggedInUser.equalsIgnoreCase("RSA"))) {
			if (isApproved) {

				if (newStatus == 3L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 2L : 1L;
				} else if (newStatus == 2L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 2L : 0L;
				} else if (newStatus == 1L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 0L : 1L;
				}

			} else {

				if (newStatus == 0L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 1L : 2L;
				} else if (newStatus == 1L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 1L : 3L;
				} else if (newStatus == 2L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 3L : 2L;
				}

			}
			user.getApprovalStatus().setId(newStatus);
			getAdminDAO().updateApprovalStatus(user);

			return getUser(loggedInUser, user.getUserId());
		} else {
			// not LSA or RSA then if disapprove change the status to
			// disapprove
			if (!isApproved) {

				newStatus = 3L;
				user.getApprovalStatus().setId(newStatus);
				getAdminDAO().updateApprovalStatus(user);

				return getUser(loggedInUser, user.getUserId());
			}
		}
		return null;

	}

	@Override
	public Profile updateProfileApprovalStatus(String loggedInUser, Profile profile, boolean isApproved) {

		Long newStatus = profile.getApprovalStatus().getId();

		if (loggedInUser != null && (loggedInUser.equalsIgnoreCase("LSA") || loggedInUser.equalsIgnoreCase("RSA"))) {
			if (isApproved) {

				if (newStatus == 3L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 2L : 1L;
				} else if (newStatus == 2L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 2L : 0L;
				} else if (newStatus == 1L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 0L : 1L;
				}

			} else {

				if (newStatus == 0L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 1L : 2L;
				} else if (newStatus == 1L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 1L : 3L;
				} else if (newStatus == 2L) {
					newStatus = (loggedInUser.equalsIgnoreCase("RSA")) ? 3L : 2L;
				}
			}

			profile.getApprovalStatus().setId(newStatus);
			getAdminDAO().updateApprovalStatus(profile);

			return getAdminDAO().getProfileGeneralInfo((profile.getGroupId().toString()));
		} else {

			if (!isApproved) {

				newStatus = 3L;
				profile.getApprovalStatus().setId(newStatus);
				getAdminDAO().updateApprovalStatus(profile);

				return getAdminDAO().getProfileGeneralInfo((profile.getGroupId().toString()));
			}
		}

		return null;
	}

	@Override
	public Profile getProfileById(String groubId) {
		// TODO Auto-generated method stub
		return getAdminDAO().getProfileGeneralInfo(groubId);
	}

	@Override
	public void updateProfile(String loggedInUser, ProfileDetails profileDetails, ProfileDetails oldProfileDetails) {

		this.getAdminDAO().updateProfile(profileDetails.getProfile());

		Long profileId = profileDetails.getProfile().getGroupId();
		this.getSecurityDAO().removeProfileRoles(profileId);
		this.getSecurityDAO().setProfileRoles(profileId, profileDetails.getProfile().getProfileRoles());
		this.getAdminDAO().deleteProfileBICs(profileId);
		this.getAdminDAO().assignBICsToProfile(profileDetails.getProfile(), profileDetails.getAuthorizedBICCodes());

		this.getAdminDAO().deleteProfileMsgCats(profileId);
		this.getAdminDAO().assignMsgCatsToProfile(profileDetails.getProfile(), profileDetails.getAuthorizedMessageCategories());

		this.getAdminDAO().deleteProfileUnits(profileId);
		this.getAdminDAO().assignUnitsToProfile(profileDetails.getProfile(), profileDetails.getAuthorizedUnits());

		deleteAllProfileActions(profileDetails.getProfile());
		assignActionsToProfile(profileDetails.getProfile(), Constants.MODULE_ID_REPORTING, profileDetails.getAuthorizedReportingActions());
		assignActionsToProfile(profileDetails.getProfile(), Constants.MODULE_ID_VIEWER, profileDetails.getAuthorizedViewerActions());
		assignActionsToProfile(profileDetails.getProfile(), Constants.MODULE_ID_WATCHDOG, profileDetails.getAuthorizedWatchdogActions());
		assignActionsToProfile(profileDetails.getProfile(), Constants.MODULE_ID_DASHBOARD, profileDetails.getAuthorizedDashboardActions());
		assignActionsToProfile(profileDetails.getProfile(), Constants.MODULE_ID_Business_Integellence, profileDetails.getAuthorizedBusinessIntegellenceActions());
	}

	@Override
	public List<String> updateUsedUnits(String loggedInUser, List<String> currSelectedUnits, List<String> oldUnits) {

		List<String> oldUsedUnits = this.getAdminDAO().getUsedUnits();
		List<String> changedUnits = new ArrayList<String>();

		// detect the removed unites
		for (String unit : oldUsedUnits) {

			if (!isExistInList(currSelectedUnits, unit)) {
				this.getAdminDAO().deleteUnit(unit);
				changedUnits.add(unit);
			}
		}

		// detect the added unites
		for (String unit : currSelectedUnits) {
			if (!isExistInList(oldUsedUnits, unit)) {
				this.getAdminDAO().addUnit(unit);
				changedUnits.add(unit);
			}
		}

		return changedUnits;
	}

	@Override
	public void addProfile(String loggedInUser, ProfileDetails profileDetails) {
		this.getAdminDAO().addProfile(profileDetails.getProfile());
		Profile newProfile = this.getProfile(loggedInUser, profileDetails.getProfile().getName());
		getSecurityDAO().setProfileRoles(newProfile.getGroupId(), profileDetails.getProfile().getProfileRoles());
		this.getAdminDAO().assignBICsToProfile(newProfile, profileDetails.getAuthorizedBICCodes());
		this.getAdminDAO().assignMsgCatsToProfile(newProfile, profileDetails.getAuthorizedMessageCategories());
		this.getAdminDAO().assignUnitsToProfile(newProfile, profileDetails.getAuthorizedUnits());
		assignActionsToProfile(newProfile, Constants.MODULE_ID_REPORTING, profileDetails.getAuthorizedReportingActions());
		assignActionsToProfile(newProfile, Constants.MODULE_ID_VIEWER, profileDetails.getAuthorizedViewerActions());
		assignActionsToProfile(newProfile, Constants.MODULE_ID_WATCHDOG, profileDetails.getAuthorizedWatchdogActions());
		assignActionsToProfile(newProfile, Constants.MODULE_ID_DASHBOARD, profileDetails.getAuthorizedDashboardActions());
		assignActionsToProfile(newProfile, Constants.MODULE_ID_Business_Integellence, profileDetails.getAuthorizedBusinessIntegellenceActions());
	}

	private void assignActionsToProfile(Profile profile, int moduleID, List<String> actionIDs) {
		for (String objectID : actionIDs) {
			this.getAdminDAO().assignAccessRightsToProfile(profile, moduleID, Long.valueOf(objectID));
		}
	}

	private void deleteProfileActions(Profile profile, int moduleID) {
		this.getAdminDAO().deleteProfileAccessRights(profile.getGroupId(), moduleID);
	}

	private void deleteAllProfileActions(Profile profile) {
		deleteProfileActions(profile, Constants.MODULE_ID_EVENTS);
		deleteProfileActions(profile, Constants.MODULE_ID_REPORTING);
		deleteProfileActions(profile, Constants.MODULE_ID_VIEWER);
		deleteProfileActions(profile, Constants.MODULE_ID_WATCHDOG);
		deleteProfileActions(profile, Constants.MODULE_ID_DASHBOARD);
		deleteProfileActions(profile, Constants.MODULE_ID_Business_Integellence);
	}

	@Override
	public Profile getProfile(String loggedInUser, String profileName) {
		profileName = getDBSafeString(profileName);
		return this.getAdminDAO().getProfile(profileName);
	}

	@Override
	public void deleteProfile(String loggedInUser, Profile profile) {

		Long profileId = profile.getGroupId();
		AdminDAO adminDAO = this.getAdminDAO();

		adminDAO.deleteProfileUnits(profileId);

		getSecurityDAO().removeProfileRoles(profileId);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_WATCHDOG);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_EVENTS);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_REPORTING);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_VIEWER);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_DASHBOARD);
		adminDAO.deleteProfileAccessRights(profileId, Constants.MODULE_ID_Business_Integellence);

		adminDAO.deleteProfileMsgCats(profileId);

		adminDAO.deleteProfileBICs(profileId);
		adminDAO.deleteProfileGroupSets(profileId);
		adminDAO.deleteProfile(loggedInUser, profile);
	}

	@Override
	public List<MsgCatPerProfile> getAllCategories() {
		return this.getAdminDAO().getAllMessageCategories();
	}

	@Override
	public void updateBIC(String loggedInUser, String oldBIC, String newBIC) {
		getAdminDAO().addBIC(newBIC);
		getAdminDAO().deleteBIC(oldBIC);
	}

	@Override
	public List<String> getUsedUnits(String loggedInUser) {
		return this.getAdminDAO().getUsedUnits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.service.admin.AdminService#generateProfilesReport(java.util. List)
	 */
	@Override
	public byte[] generateProfilesReport(String loggedInUser, List<ProfileDetails> profiles, String reportLogo) throws IOException, JRException {
		final List<MsgCatPerProfile> allCategories = getAllCategories();
		List<ProfileDetails> profilesCopy = new ArrayList<ProfileDetails>();
		ProfileDetails profile1;

		for (ProfileDetails profile : profiles) {
			Profile tmpProfile = new Profile();
			profile1 = new ProfileDetails(tmpProfile);

			tmpProfile.setProfileRoles(profile.getProfile().getProfileRoles());
			List<String> actionsDescriptions = getActionsDescriptions(profile.getAuthorizedReportingActions(), Constants.MODULE_ID_REPORTING);
			profile1.setAuthorizedReportingActions(actionsDescriptions);
			List<String> actionsDescriptions2 = getActionsDescriptions(profile.getAuthorizedViewerActions(), Constants.MODULE_ID_VIEWER);
			profile1.setAuthorizedViewerActions(actionsDescriptions2);
			profile1.setAuthorizedWatchdogActions(getActionsDescriptions(profile.getAuthorizedWatchdogActions(), Constants.MODULE_ID_WATCHDOG));
			profile1.setAuthorizedDashboardActions(getActionsDescriptions(profile.getAuthorizedDashboardActions(), Constants.MODULE_ID_DASHBOARD));
			profile1.setAuthorizedBusinessIntegellenceActions(getActionsDescriptions(profile.getAuthorizedDashboardActions(), Constants.MODULE_ID_Business_Integellence));
			List<String> categoriesDescriptions = new ArrayList<String>();
			for (MsgCatPerProfile msgCatPerProfile : allCategories) {
				List<String> authorizedMessageCategories = profile.getAuthorizedMessageCategories();
				if (authorizedMessageCategories.contains(msgCatPerProfile.getMsgCat())) {
					categoriesDescriptions.add(msgCatPerProfile.getMsgCat() + ": " + msgCatPerProfile.getDesc());
				}
			}
			profile1.setAuthorizedMessageCategories(categoriesDescriptions);

			profile1.setAuthorizedBICCodes(profile.getAuthorizedBICCodes());
			profile1.setAuthorizedUnits(profile.getAuthorizedUnits());
			tmpProfile.setName(profile.getProfile().getName());
			tmpProfile.setDescription(profile.getProfile().getDescription());
			tmpProfile.setApprovalStatus(profile.getProfile().getApprovalStatus());

			profilesCopy.add(profile1);
		}
		return ReportGenerator.getProfilesReportAsBytes(profilesCopy, getReportParamaters(loggedInUser, profilesCopy.size(), reportLogo));

	}

	// @Override
	// public XSSFWorkbook generateProfileXlsxReport(String loggedInUser, List<ProfileDetails> profileDetailsList, String reportLogo) throws JRException, IOException {
	// return ReportGenerator.getProfilesXlsxReport(profileDetailsList, getReportParamaters(loggedInUser, profileDetailsList.size(), reportLogo));
	// }

	@Override
	public JasperPrint generateProfileXlsxReport(String loggedInUser, List<ProfileDetails> profileDetailsList, String reportLogo) throws JRException, IOException {
		return ReportGenerator.getProfilesXlsxReport(profileDetailsList, getReportParamaters(loggedInUser, profileDetailsList.size(), reportLogo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eastnets.service.admin.AdminService#generateUsersReport(java.util. List)
	 */

	@Override
	public byte[] generateUsersReport(String loggedInUser, List<User> users, String reportLogo) throws IOException, JRException {
		return ReportGenerator.getUsersReportAsBytes(users, getReportParamaters(loggedInUser, users.size(), reportLogo));

	}

	@Override
	public List<String> getActionsDescriptions(List<String> ids, int modeule) {
		List<String> descriptions = new ArrayList<>();
		List<Action> availableActions = getAdminDAO().getAvailableActions(modeule);
		for (Action action : availableActions) {
			if (ids.contains(action.getActionId().toString())) {
				descriptions.add(action.getActionName());
			}
		}

		return descriptions;
	}

	private Map<String, Object> getReportParamaters(String loggedInUser, int listSize, String reportLogo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("P_GENERATED_BY", loggedInUser);
		parameters.put("P_NUMBER_OF_PROFILES", listSize);
		parameters.put("P_DATE_PATTERN", "dd/MM/yyyy");
		parameters.put("P_NEW_SECUIRTY", getApplicationFeatures().isNewSecurity());
		parameters.put("P_LOGO_PATH", reportLogo);
		parameters.put("P_LOGO_PATH", reportLogo);
		return parameters;
	}

	private boolean isExistInList(List<String> list, String value) {
		for (String element : list) {
			if (element.equals(value))
				return true;
		}

		return false;
	}

	@Override
	public String checkRemovedUnits(String loggedInUser, List<String> currSelectedUnits) {
		List<String> assignedUnits = getAdminDAO().getAssignedUnits();

		List<String> oldUsedUnits = this.getAdminDAO().getUsedUnits();
		StringBuilder strBuilder = new StringBuilder();

		// detect the removed unites
		for (String unit : oldUsedUnits) {
			// add unit into the string if it is removed AND assigned to a
			// profile.
			if (!isExistInList(currSelectedUnits, unit) && isExistInList(assignedUnits, unit)) {
				if (strBuilder.length() != 0)
					strBuilder.append(", ");
				strBuilder.append(unit);
			}
		}

		return strBuilder.toString();
	}

	@Override
	public boolean isDataBaseUser(String loggedInUser, String username) {
		username = username.replaceAll("'", "''");
		return getAdminDAO().isDataBaseUser(username);
	}

	@Override
	public boolean isProfileHaveUsers(String loggedInUser, Profile profile) {
		return getAdminDAO().isProfileHaveUsers(profile);
	}

	@Override
	public boolean userHasSessionOnDB(String loggedInUser, String username) {
		username = username == null ? "" : username.replaceAll("'", "''");
		return getAdminDAO().userHasSessionOnDB(username);
	}

	@Override
	public boolean userHasRelationsOnDB(String username) {
		username = username == null ? "" : username.replaceAll("'", "''");
		return getAdminDAO().userHasRelationsOnDB(username);
	}

	@Override
	public List<CsmConnection> getCsmConnection() {
		return getAdminDAO().getCsmConnection();
	}

	@Override
	public void addCsmConnection(CsmConnection csmConnection) {
		getAdminDAO().addCsmConnection(csmConnection);
	}

	@Override
	public void updateCsmConnection(String serverName, Integer serverPort, CsmConnection csmConnection) {
		getAdminDAO().updateCsmConnection(serverName, serverPort, csmConnection);
	}

	@Override
	public void deleteCsmConnection(CsmConnection csmConnection) {
		getAdminDAO().deleteCsmConnection(csmConnection);
	}

	@Override
	public boolean checkCsmFeatures() {
		return getApplicationFeatures().isCmsConnectionSupported();
	}

	@Override
	public boolean checkCsmConnection(CsmConnection currentCsmConnection) {
		return getAdminDAO().checkCsmConnection(currentCsmConnection);
	}

	@Override
	public List<Pair<String, String>> getRoleDisplayNames(String userName, List<String> roles) {
		return getSecurityDAO().getRoleDisplayNames(roles);
	}

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}

	@Override
	public void updateSettings(String username, AdminSettings settings, AdminSettings oldSettings) throws Exception {
		AdminSettings tempSettings = getAdminDAO().getSettings();
		if (tempSettings == null) {
			getAdminDAO().createSettings(settings);
		} else {
			getAdminDAO().updateSettings(settings);
		}
	}

	@Override
	public void restSettings() {
		getAdminDAO().restSettings();
	}

	@Override
	public void deleteUser(String loggedInUser, User user, boolean deleteUserTemplages) throws Exception {
		// delete the user from the reporting application (sUsers) before trying
		// to delete him from the database
		// cause sometimes the deletion from the database fails due to the fact
		// that the user already has session on the database ( logged in by some
		// other application )

		if (deleteUserTemplages) {
			getAdminDAO().deleteSearchTemplatesForUser(user);
		}
		getAdminDAO().deleteApplicationSettingsForUser(user);

		getSecurityDAO().deleteUserPasswords(user);
		getAdminDAO().deleteReportingUser(user);

		if ((user.isDatabaseUser() || user.isDatabaseUserOld()) && user.isDeleteFromDatabase()) {
			getAdminDAO().deleteDatabaseUser(user);
		}
	}

	@Override
	public AdminSettings getSettings() {
		AdminSettings settings = getAdminDAO().getSettings();
		if (settings == null) {
			settings = new AdminSettings();
		}

		return settings;
	}

	@Override
	public List<String> getUserRoles(String username) {
		return getAdminDAO().getUserRoles(username);
	}

	@Override
	public boolean hasLoaderRole(User user) {
		if (!getAdminDAO().isDataBaseUser(user.getUserName())) {
			return false;
		}
		List<String> roles = getAdminDAO().getUserRoles(user.getUserName());

		boolean userHasLoaderRols = user.isCanRunLoader() || roles.contains("SIDE_LOADER");
		return userHasLoaderRols && roles.contains("SIDE_OPER");
	}

	@Override
	public void addUser(String loggedInUser, User user) {

		boolean isDatabaseUser = user.isDatabaseUser() && user.isCreateOnDatabase();
		// user.setUserName(user.getUserName().replaceAll("'", "''"));
		if (isDatabaseUser) {
			getAdminDAO().addDatabaseUser(user);
			addSpecialGrants(user);
			if (user.isCanRunLoader()) {
				addUserLoaderRole(user);
			}
		}

		if (user.isDatabaseUser() && user.isGrantRecoveryRole()) {
			grantRecoveryRole(user);
		}
		getAdminDAO().addReportingUser(user);

		if (user.isApplicationUser()) {
			try {
				User dbUser = getAdminDAO().getUser(user.getUserName());
				user.setUserId(dbUser.getUserId());
				byte[] salt = null;
				salt = HashingUtility.getSalt();
				user.setSalt(Base64.getEncoder().encodeToString(salt));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			getSecurityDAO().setReportingUserPassword(user);
		}
	}

	@Override
	public User getUser(Long userId) {
		return getAdminDAO().getUser(userId);
	}

	@Override
	public String getUserApprovalStatusDiscription(Long statusId) {
		return getAdminDAO().getUserApprovalStatusDiscription(statusId);
	}

	@Override
	public void setUserGroupId(String userName, Long userId, Long newGroupId) {

		getAdminDAO().setUserGroupId(userId, newGroupId);
	}

	@Override
	public List<User> filterDormantUsers(String username, int dormancyPeriod, String userName, String profileName) {
		userName = getDBSafeString(userName);
		profileName = getDBSafeString(profileName);
		List<User> users = getAdminDAO().filterDormantUsers(dormancyPeriod, userName, profileName);

		fillIsDatabaseUser(users);
		for (User user : users) {
			if (user.getAuthenticationMethod() == User.AUTH_DB) {
				user.setCanRunLoader(hasLoaderRole(user));
			}
		}
		return users;

	}

	@Override
	public String getUserAuthenticationMethod(String userName) {
		return getAdminDAO().getUserAuthenticationMethod(userName);
	}

	@Override
	public User getUserWithoutRoles(String userName) {

		AdminDAO adminDAO = getAdminDAO();
		return adminDAO.getUser(userName);
	}

	@Override
	public List<Integer> getGroupAccessProgramIds(Long groupId) {
		return getAdminDAO().getGroupAccessProgramIds(groupId);
	}

	@Override
	public SaveUserEnum validateUserToSave(User currentUser, User loggedInUser, boolean resettingPasswordViaEmail) {
		boolean databaseUser = currentUser.isDatabaseUser() && currentUser.isCreateOnDatabase();

		if (databaseUser && isDataBaseUser(loggedInUser.getUserName(), currentUser.getUserName())) {
			return SaveUserEnum.DB_USER_EXISTS;
		}

		if (resettingPasswordViaEmail && (currentUser.getEmail() == null || currentUser.getEmail().trim().isEmpty())) {
			return SaveUserEnum.USER_MAIL_REQUIRED;
		}

		else if ((resettingPasswordViaEmail) ? !AdminUtils.isValidEmail(currentUser.getEmail().trim()) : false) {
			return SaveUserEnum.INVALID_EMAIL_ADDRESS;

		} else if (!resettingPasswordViaEmail && currentUser.getEmail() != null && (!AdminUtils.isValidEmail(currentUser.getEmail().trim()))) {

			return SaveUserEnum.INVALID_EMAIL_ADDRESS;
		}

		if (currentUser.getExpirationDate() != null && currentUser.getExpirationDate().before(new Date())) {
			return SaveUserEnum.SELECT_A_DATE_AFTER_TODAY;

		}

		if (currentUser.getProfile() == null || currentUser.getProfile().getGroupId() == null) {
			return SaveUserEnum.SELECT_USER_PROFILE;
		}

		if (currentUser.getAuthenticationMethod() == User.AUTH_REPORTING || currentUser.getAuthenticationMethod() == User.AUTH_REPORTING_TOTP) {

			return SaveUserEnum.REPORTING_USER;

		}

		if (currentUser.getAuthenticationMethod() == User.AUTH_DB && currentUser.isCreateOnDatabase() && currentUser.isAutoGeneratedPassword()) {

			return SaveUserEnum.DB_USER_WITH_AUTOGENERATE_PASSWORD;
		}

		if (currentUser.getAuthenticationMethod() != User.AUTH_DB || !currentUser.isCreateOnDatabase() || !currentUser.isAutoGeneratedPassword()) {

			return SaveUserEnum.DB_USER;
		}

		return null;
	}

	@Override
	public StringBuilder exportProfilesCSV(String loginUser, List<Profile> profiles, String csvSeperator, String profileName, String profileStatus, String description) {

		StringBuilder csvBuilder = new StringBuilder();
		// Generate Header
		csvBuilder.append("\"" + profileName + "\"" + csvSeperator);
		csvBuilder.append("\"" + profileStatus + "\"" + csvSeperator);
		csvBuilder.append("\"" + description + "\"" + csvSeperator);
		csvBuilder.append("\r\n");
		for (Profile profile : profiles) {
			if (profile.isChecked()) {
				csvBuilder.append("\"" + (profile.getName() == null ? "" : profile.getName()) + "\"" + csvSeperator);
				csvBuilder.append("\"" + profile.getApprovalStatus().getDescription() + "\"" + csvSeperator);
				csvBuilder.append("\"" + (profile.getDescription() == null ? "" : profile.getDescription()) + "\"" + csvSeperator);
				csvBuilder.append("\r\n");
			}
		}

		return csvBuilder;
	}

	@Override
	public Workbook exportUsersXLSX(String loginUser, List<User> users, String csvSeperator, String userName, String fullUserName, String authenticationMethod, String userEmail, String lastLogin, String userExpirationDate, String profile,
			String LastLoginDaysAsString, String fourEyeRole, String canRunLoader) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Users");
		DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
		int rowIndex = 0;
		int columnIndex = 0;
		Row row = sheet.createRow(rowIndex++);
		row.createCell(columnIndex++).setCellValue(userName);
		row.createCell(columnIndex++).setCellValue(fullUserName);
		row.createCell(columnIndex++).setCellValue(authenticationMethod);

		row.createCell(columnIndex++).setCellValue(userEmail);
		row.createCell(columnIndex++).setCellValue(lastLogin);
		row.createCell(columnIndex++).setCellValue(userExpirationDate);
		row.createCell(columnIndex++).setCellValue(profile);
//		row.createCell(columnIndex++).setCellValue(LastLoginDaysAsString);
		row.createCell(columnIndex++).setCellValue(fourEyeRole);
		row.createCell(columnIndex++).setCellValue(canRunLoader);

		for (User user : users) {
			if (user.isChecked()) {
				row = sheet.createRow(rowIndex++);
				columnIndex = 0;

				row.createCell(columnIndex++).setCellValue(user.getUserName() == null ? "" : user.getUserName());
				row.createCell(columnIndex++).setCellValue(user.getFullUserName() == null ? "" : user.getFullUserName());
				String authenticationMethodUser = user.getAuthenticationMethod() == 0 ? "Reporting"
						: user.getAuthenticationMethod() == 1 ? "LDAP" : user.getAuthenticationMethod() == 2 ? "Database" : user.getAuthenticationMethod() == 3 ? "Radius" : user.getAuthenticationMethod() == 5 ? "ReportingTOTP" : "";
				row.createCell(columnIndex++).setCellValue(authenticationMethodUser);
				row.createCell(columnIndex++).setCellValue(user.getEmail() == null ? "" : user.getEmail());
				row.createCell(columnIndex++).setCellValue(user.getLastLoginDaysAsString() == null ? "" : user.getLastLoginDaysAsString());
				row.createCell(columnIndex++).setCellValue(user.getExpirationDate() == null ? "" : dateF.format(user.getExpirationDate()));
				row.createCell(columnIndex++).setCellValue(user.getProfile().getName() == null ? "" : user.getProfile().getName());
//				row.createCell(columnIndex++).setCellValue(user.getLastLoginDaysAsString() == null ? "" : user.getLastLoginDaysAsString());

				if (user.getAuthenticator() == -1)
					row.createCell(columnIndex++).setCellValue("-");
				else
					row.createCell(columnIndex++).setCellValue(user.getAuthenticator());

				if (user.getAuthenticationMethod() == User.AUTH_DB) {
					user.setCanRunLoader(hasLoaderRole(user));
					row.createCell(columnIndex++).setCellValue(user.isCanRunLoader() ? "Yes" : "No");
				}
			}
		}
		return workbook;
	}

	@Override
	public StringBuilder exportUsersCSV(String loginUser, List<User> users, String csvSeperator, String userName, String fullUserName, String authenticationMethod, String userEmail, String userExpirationDate, String profile,
			String LastLoginDaysAsString, String fourEyeRole, String canRunLoader) {

		StringBuilder csvBuilder = new StringBuilder();
		// Generate Header
		csvBuilder.append("\"" + userName + "\"" + csvSeperator);
		csvBuilder.append("\"" + fullUserName + "\"" + csvSeperator);
		csvBuilder.append("\"" + authenticationMethod + "\"" + csvSeperator);
		csvBuilder.append("\"" + userEmail + "\"" + csvSeperator);
		csvBuilder.append("\"" + userExpirationDate + "\"" + csvSeperator);
		csvBuilder.append("\"" + profile + "\"" + csvSeperator);
		csvBuilder.append("\"" + LastLoginDaysAsString + "\"" + csvSeperator);
		csvBuilder.append("\"" + fourEyeRole + "\"" + csvSeperator);
		csvBuilder.append("\"" + canRunLoader + "\"" + csvSeperator);
		csvBuilder.append("\r\n");

		DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
		for (User user : users) {
			if (user.isChecked()) {
				csvBuilder.append("\"" + (user.getUserName() == null ? "" : user.getUserName()) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getFullUserName() == null ? "" : user.getFullUserName()) + "\"" + csvSeperator);
				String authenticationMethodUser = user.getAuthenticationMethod() == 0 ? "Reporting"
						: user.getAuthenticationMethod() == 1 ? "LDAP" : user.getAuthenticationMethod() == 2 ? "Database" : user.getAuthenticationMethod() == 3 ? "Radius" : user.getAuthenticationMethod() == 5 ? "ReportingTOTP" : "";
				csvBuilder.append("\"" + (user.getAuthenticatorAsString() == null ? "" : authenticationMethodUser) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getEmail() == null ? "" : user.getEmail()) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getExpirationDate() == null ? "" : dateF.format(user.getExpirationDate())) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getProfile().getName() == null ? "" : user.getProfile().getName()) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getLastLoginDaysAsString() == null ? "" : user.getLastLoginDaysAsString()) + "\"" + csvSeperator);
				csvBuilder.append("\"" + (user.getAuthenticator() == -1 ? "-" : user.getAuthenticator()) + "\"" + csvSeperator);

				if (user.getAuthenticationMethod() == User.AUTH_DB) {
					user.setCanRunLoader(hasLoaderRole(user));
					csvBuilder.append("\"" + (user.isCanRunLoader() ? "Yes" : "No") + "\"" + csvSeperator);
				}

				csvBuilder.append("\r\n");
			}
		}
		return csvBuilder;

	}

	@Override
	public void saveBaseCur(String loginUser, String baseCur) {
		getAdminDAO().saveBaseCur(loginUser, baseCur);

	}

	@Override
	public List<String> getAvailableBaseCurrencies() {
		return getAdminDAO().getAvailableBaseCurrencies();
	}

	@Override
	public String getBaseCurrencyFromDB() {
		return getAdminDAO().getBaseCurrencyFromDB();
	}

	@Override
	public void saveBaseBiRepPath(String loginUser, String repPath, boolean setAsDefault) {
		getAdminDAO().saveBaseBiRepPath(loginUser, repPath, setAsDefault);
	}

	@Override
	public List<JIResourceFolder> getJIResourceFolder() {
		List<JIResourceFolder> jiResourceFolder = getAdminDAO().getJIResourceFolder();
		List<JIResourceFolder> targetJiResourceFolder = new ArrayList<>();
		for (JIResourceFolder folder : jiResourceFolder) {
			int countMatches = StringUtils.countMatches(folder.getUri(), "/");
			if (countMatches <= 2) {
				targetJiResourceFolder.add(folder);
			}
		}

		return targetJiResourceFolder;
	}

	@Override
	public void updateBiDefaultPath(String biDefPath) {
		getAdminDAO().updateBiDefaultPath(biDefPath);

	}

	@Override
	public String getDefaultBiPathFromDB() {
		return getAdminDAO().getDefaultBiPathFromDB();
	}

	@Override
	public void deleteJasperFolder(String folderUri) {
		getAdminDAO().deleteJasperFolder(folderUri);
	}

	@Override
	public void markJasperFolderAsDeleted(String folderUri) {
		getAdminDAO().markJasperFolderAsDeleted(folderUri);
	}

	@Override
	public Integer getWDEnablePDFTemplates(String userName) {
		return getAdminDAO().getWDEnablePDFTemplates();
	}

	@Override
	public void updateWDSettings(boolean enablePDFTemplates) {
		getAdminDAO().updateWDSettings(enablePDFTemplates);
	}

	@Override
	public UserThemePreferences getUserThemePreferences(Long userId) {
		// TODO Auto-generated method stub
		return getAdminDAO().getUserThemePreferences(userId);
	}

	@Override
	public void updateUserThemePreferences(UserThemePreferences userThemePreferences, boolean update) {
		getAdminDAO().updateUserThemePreferences(userThemePreferences, update);

	}
}
