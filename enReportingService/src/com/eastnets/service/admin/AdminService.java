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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.eastnets.service.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Administration Service Interface
 * 
 * @author EastNets
 * @since July 22, 2012
 */
public interface AdminService extends Service {

	// Loader Settings
	/**
	 * Get loader settings
	 * 
	 * @param loggedInUser
	 * @return LoaderSettings
	 */
	public LoaderSettings getLoaderSettings(String loggedInUser);

	/**
	 * Update Loader Settings
	 * 
	 * @param loggedInUser
	 * @param loaderSettings
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateLoaderSettings(String loggedInUser, LoaderSettings loaderSettings, LoaderSettings oldLoaderSetting);

	// BICs
	/**
	 * Get Licensed Bics
	 * 
	 * @param loggedInUser
	 * @return List<String>
	 */
	public List<BicLicenseInfo> getLicensedBICs(String loggedInUser);

	/**
	 * 
	 * @return
	 */
	public List<String> getBICs();

	/**
	 * Add new BIC
	 * 
	 * @param loggedInUser
	 * @param bic
	 */
	public void addBIC(String loggedInUser, String bic);

	/**
	 * Delete BIC
	 * 
	 * @param loggedInUser
	 * @param bic
	 */
	public void deleteBIC(String loggedInUser, String bic);

	/**
	 * Update BIC (only training could be updated)
	 * 
	 * @param loggedInUser
	 * @param oldBIC
	 * @param newBIC
	 */
	public void updateBIC(String loggedInUser, String oldBIC, String newBIC);

	// UNITs
	/**
	 * Get All units availabe to the whole system (either it is used or not) , by reading it from "ldHelperUnitName" table
	 * 
	 * @param loggedInUser
	 * @return List<String>
	 */
	public List<String> getAllUnits(String loggedInUser);

	/**
	 * Get only the used units by system, by reading it from "sunit" table
	 * 
	 * @param loggedInUser
	 * @return List<String>
	 */
	public List<String> getUsedUnits(String loggedInUser);

	/**
	 * Update used units
	 * 
	 * @param loggedInUser
	 * @param units
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<String> updateUsedUnits(String loggedInUser, List<String> units, List<String> oldUnits);

	// Profiles
	/**
	 * Get profiles
	 * 
	 * @param loggedInUser
	 * @return List<Profile>
	 */
	public List<Profile> getProfiles(String loggedInUser);

	/**
	 * Get profiles
	 * 
	 * @param profileName
	 * @return List<Profile>
	 */
	public List<Profile> getProfilesByProfileName(String loggedInUser, String profileName);

	/**
	 * Get profile data by profile name
	 * 
	 * @param loggedInUser
	 * @param profileName
	 * @return Profile
	 */
	public Profile getProfile(String loggedInUser, String profileName);

	/**
	 * Update approval status for profile
	 * 
	 * @param loggedInUser
	 * @param profile
	 * @param isApproved
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Profile updateProfileApprovalStatus(String loggedInUser, Profile profile, boolean isApproved);

	/**
	 * Update profile
	 * 
	 * @param loggedInUser
	 * @param profile
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateProfile(String loggedInUser, ProfileDetails profile, ProfileDetails oldProfileDetails);

	/**
	 * Add new profile
	 * 
	 * @param loggedInUser
	 * @param profile
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addProfile(String loggedInUser, ProfileDetails profile);

	/*
	 * return if the end user can run the back end tools
	 */
	public boolean hasLoaderRole(User user);

	/**
	 * Delete profile
	 * 
	 * @param loggedInUser
	 * @param profile
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteProfile(String loggedInUser, Profile profile);

	// Actions (Permissions)
	/**
	 * Get Watchdog Available Actions (Permissions)
	 * 
	 * @param loggedInUser
	 * @return List<Action>
	 */
	public List<Action> getWatchdogAvailableActions(String loggedInUser);

	/**
	 * Get Reporting Available Actions (Permissions)
	 * 
	 * @param loggedInUser
	 * @return List<Action>
	 */
	public List<Action> getReportingAvailableActions(String loggedInUser);

	/**
	 * Get Viewer Available Actions (Permissions)
	 * 
	 * @param loggedInUser
	 * @return List<Action>
	 */
	public List<Action> getViewerAvailableActions(String loggedInUser, License license);

	/**
	 * Get Dashboard Available Actions (Permissions)
	 * 
	 * @param loggedInUser
	 * @return List<Action>
	 */
	public List<Action> getDashboardAvailableActions(String loggedInUser);

	/**
	 * Get business integellence Available Actions (Permissions)
	 * 
	 * @param loggedInUser
	 * @return List<Action>
	 */
	public List<Action> getBusinessIntegellenceAvailableActions(String loggedInUser);

	// Loader Connection
	/**
	 * Get defined loader connections
	 * 
	 * @param loggedInUser
	 * @return List<LoaderConnection>
	 */
	public List<LoaderConnection> getLoaderConnections(String loggedInUser);

	/**
	 * 
	 * @param loggedInUser
	 * @param maxConnection
	 * @return
	 */
	public List<LoaderConnection> getLoaderConnections(String loggedInUser, Integer maxConnection);

	/**
	 * Get loader connection details for certain aid
	 * 
	 * @param loggedInUser
	 * @param aid
	 * @return LoaderConnection
	 */
	public LoaderConnection getLoaderConnection(String loggedInUser, Long aid);

	/**
	 * Add new loader connection
	 * 
	 * @param loggedInUser
	 * @param loaderConnection
	 */
	public void addLoaderConnection(String loggedInUser, LoaderConnection loaderConnection);

	/**
	 * Update loader connection
	 * 
	 * @param loggedInUser
	 * @param loaderConnection
	 */
	public void updateLoaderConnection(String loggedInUser, LoaderConnection loaderConnection, LoaderConnection oldLoaderConnection);

	/**
	 * Sync loader connection to certain date
	 * 
	 * @param loggedInUser
	 * @param loaderConnection
	 * @param date
	 */
	public void syncLoaderConnection(String loggedInUser, LoaderConnection loaderConnection, Date date);

	/**
	 * Sync loader connection
	 * 
	 * @param loggedInUser
	 * @param loaderConnection
	 */
	public void syncLoaderConnection(String loggedInUser, LoaderConnection loaderConnection);

	public boolean checkIfMessageExists(LoaderConnection loaderConnection, Date date) throws Exception;

	// Users
	/**
	 * Get all system users
	 * 
	 * @param loggedInUser
	 * @return List<User>
	 */
	public List<User> getUsers(String loggedInUser);

	/**
	 * Get all system users
	 * 
	 * @param userName
	 * @param profileName
	 * @return List<User>
	 */
	public List<User> getUsersByFilter(String loggedUserName, String userName, String profileName);

	/**
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public User getUser(String loggedInUser);

	/**
	 * 
	 * @param db_roleName
	 * @return
	 */
	public boolean checkRole(String db_roleName);

	/**
	 * Get user details by user id
	 * 
	 * @param loggedInUser
	 * @param userId
	 * @return User
	 */
	public User getUser(String loggedInUser, Long userId);

	/**
	 * Add new user (either DB user or just application user)
	 * 
	 * @param loggedInUser
	 * @param user
	 */
	// @Transactional (propagation=Propagation.REQUIRED,
	// rollbackFor=Exception.class) TODO this method should be transactional but
	// we removed the transaction because of issue on MSSQL
	// "java.sql.SQLException: The procedure 'sys.sp_dropsrvrolemember' cannot
	// be executed within a transaction."
	public void addUser(String loggedInUser, User user);

	/**
	 * 
	 * Grant for user SIDE_RECOVERY role \n
	 * 
	 * @param user
	 * 
	 */
	public void grantRecoveryRole(User user);

	/**
	 * Revoke for user SIDE_RECOVERY role \n
	 * 
	 * @param user
	 */
	public void removeRecoveryRole(User user);

	/**
	 * Update user
	 * 
	 * @param loggedInUser
	 * @param user
	 */
	// @Transactional (propagation=Propagation.REQUIRED,
	// rollbackFor=Exception.class)
	// but we removed the transaction because of issue on MSSQL
	// "java.sql.SQLException: The procedure 'sys.sp_dropsrvrolemember' cannot
	// be executed within a transaction."
	public void updateUser(String loggedInUser, User user, User oldUser, Profile userProfile);

	/**
	 * Delete user
	 * 
	 * @param loggedInUser
	 * @param user
	 * @param isDatabaseUser
	 * @throws Exception
	 */
	public void deleteUser(String loggedInUser, User user, boolean deleteUserTemplages) throws Exception;

	/**
	 * Get all system roles
	 * 
	 * @param loggedInUser
	 * @return
	 */
	public List<Pair<String, String>> getRoles(String loggedInUser);

	/**
	 * Update approval status for user
	 * 
	 * @param loggedInUser
	 * @param user
	 * @param isApproved
	 * @return User
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User updateUserApprovalStatus(String loggedInUser, User user, boolean isApproved);

	// Message Categories
	/**
	 * Get all message categories
	 * 
	 * @return List<MsgCatPerProfile>
	 */
	public List<MsgCatPerProfile> getAllCategories();

	/**
	 * Generate Profiles Report
	 * 
	 * @param profiles
	 * @author EastNets
	 * @since Oct 30, 2012
	 * @return byte[] a byte stream representing the report pdf content
	 */
	public byte[] generateProfilesReport(String loggedinUser, List<ProfileDetails> profiles, String reportLogo) throws IOException, JRException;

	/**
	 * Generate Users Report
	 * 
	 * @param users
	 * @author EastNets
	 * @since Oct 30, 2012
	 * @return byte[] a byte stream representing the report pdf content
	 */
	public byte[] generateUsersReport(String loggedinUser, List<User> users, String reportLogo) throws IOException, JRException;

	/**
	 * Check if one of deleted unit is used in a profile config
	 * 
	 * @return
	 */
	public String checkRemovedUnits(String loggedInUser, List<String> currSelectedUnits);

	/**
	 * 
	 * @param loggedInUser
	 * @param username
	 * @return
	 */
	public boolean isDataBaseUser(String loggedInUser, String username);

	/**
	 * 
	 * @param loggedInUser
	 * @param profile
	 * @return
	 */
	public boolean isProfileHaveUsers(String loggedInUser, Profile profile);

	/**
	 * check if the user has session on the database or not( using other application, this will not check if the user logged in into our application )
	 * 
	 * @param loggedInUser
	 * @param username
	 * @return true if the user has session on the database
	 */
	public boolean userHasSessionOnDB(String loggedInUser, String username);

	public boolean userHasRelationsOnDB(String username);

	public List<CsmConnection> getCsmConnection();

	public void addCsmConnection(CsmConnection csmConnection);

	public void updateCsmConnection(String serverName, Integer serverPort, CsmConnection csmConnection);

	public void deleteCsmConnection(CsmConnection csmConnection);

	public boolean checkCsmFeatures();

	public boolean checkCsmConnection(CsmConnection currentCsmConnection);

	public List<Pair<String, String>> getRoleDisplayNames(String userName, List<String> roles);

	public void updateSettings(String username, AdminSettings settings, AdminSettings oldSettings) throws Exception;

	/*
	 * That means there no user name and password for backend tools
	 */
	public void restSettings();

	public AdminSettings getSettings();

	public List<String> getUserRoles(String username);

	public void setRoles(User user);

	public User getUser(Long userId);

	public String getUserApprovalStatusDiscription(Long statusId);

	public void setUserGroupId(String userName, Long userId, Long newGroupId);

	public List<User> filterDormantUsers(String username, int dormancyPeriod, String userName, String Profile);

	public String getUserAuthenticationMethod(String userName);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserWithoutRoles(String userName);

	public List<Integer> getGroupAccessProgramIds(Long groupId);

	public void changeAuthMethodForNonDbUsers(Long authMethod);

	/**
	 * getUser For MQ And DB Connector
	 * 
	 */
	public User getValidUser(String userName);

	public List<String> getActionsDescriptions(List<String> ids, int modeule);

	// public XSSFWorkbook generateProfileXlsxReport(String loggedInUser, List<ProfileDetails> profileDetailsList, String reportLogo) throws JRException, IOException;

	public SaveUserEnum validateUserToSave(User currentUser, User loggedInUser, boolean resettingPasswordViaEmail);

	public Profile getProfileById(String groubId);

	public StringBuilder exportProfilesCSV(String loginUser, List<Profile> profiles, String csvSeperator, String profileName, String profileStatus, String description);

	public StringBuilder exportUsersCSV(String loginUser, List<User> users, String csvSeperator, String userName, String fullUserName, String authenticationMethod, String userEmail, String userExpirationDate, String profile,
			String LastLoginDaysAsString, String fourEyeRole, String canRunLoader);

	public Workbook exportUsersXLSX(String loginUser, List<User> users, String csvSeperator, String userName, String fullUserName, String authenticationMethod, String userEmail, String lastLogin, String userExpirationDate, String profile,
			String LastLoginDaysAsString, String fourEyeRole, String canRunLoader) throws IOException;

	public JasperPrint generateProfileXlsxReport(String loggedInUser, List<ProfileDetails> profileDetailsList, String reportLogo) throws JRException, IOException;

	public void saveBaseCur(String loginUser, String baseCur);

	public List<String> getAvailableBaseCurrencies();

	public String getBaseCurrencyFromDB();

	public void saveBaseBiRepPath(String loginUser, String repPath, boolean setAsDefault);

	public List<JIResourceFolder> getJIResourceFolder();

	public void updateBiDefaultPath(String biDefPath);

	public String getDefaultBiPathFromDB();

	public void deleteJasperFolder(String folderUri);

	public void markJasperFolderAsDeleted(String folderUri);

	public Integer getWDEnablePDFTemplates(String userName);

	public void updateWDSettings(boolean enablePDFTemplates);

	public List<String> getSelectedMessageTypes();

	public UserThemePreferences getUserThemePreferences(Long userId);

	public void updateUserThemePreferences(UserThemePreferences userThemePreferences, boolean update);

}
