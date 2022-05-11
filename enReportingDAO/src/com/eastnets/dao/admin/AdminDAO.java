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

package com.eastnets.dao.admin;

import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.AdminSettings;
import com.eastnets.domain.admin.CsmConnection;
import com.eastnets.domain.admin.JIResourceFolder;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.LoaderSettings;
import com.eastnets.domain.admin.MsgCatPerProfile;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.admin.UserThemePreferences;

/**
 * Administration DAO Interface
 * 
 * @author EastNets
 * @since July 19, 2012
 */
public interface AdminDAO extends DAO {
	/**
	 * Get loader settings
	 * 
	 * @return LoaderSettings
	 */
	public LoaderSettings getLoaderSettings();

	/**
	 * Update loader settings
	 * 
	 * @param loaderSettings
	 * 
	 */
	public void updateLoaderSettings(LoaderSettings loaderSettings);

	/**
	 * Get available message types
	 * 
	 * @return List<String>
	 */
	public List<String> getAvailableMessageTypes();

	/**
	 * Get selected(used) message types
	 * 
	 * @return List<String>
	 */
	public List<String> getSelectedMessageTypes();

	/**
	 * Get licensed BICs
	 * 
	 * @return List<String>
	 */
	public List<String> getLicensedBICs();

	/**
	 * Add new BIC
	 * 
	 * @param bic
	 */
	public void addBIC(String bic);

	/**
	 * Delete BIC
	 * 
	 * @param bic
	 */
	public void deleteBIC(String bic);

	/**
	 * Get all units (used + availabe to be used)
	 * 
	 * @return List<String>
	 */
	public List<String> getAllUnits();

	/**
	 * Get used units
	 * 
	 * @return List<String>
	 */
	public List<String> getUsedUnits();

	/**
	 * Get all units that are assigned to a profile
	 * 
	 * @return List<String>
	 */
	public List<String> getAssignedUnits();

	/**
	 * Add unit
	 * 
	 * @param unit
	 */
	public void addUnit(String unit);

	/**
	 * Delete unit
	 * 
	 * @param unit
	 */
	public void deleteUnit(String unit);

	/**
	 * Get all profiles
	 * 
	 * @return List<Profile>
	 */
	public List<Profile> getProfiles();

	/**
	 * Get profile general information
	 * 
	 * @param groupId
	 * @return Profile
	 */
	public Profile getProfileGeneralInfo(String groupId);

	/**
	 * Get available actions for a certain module
	 * 
	 * @param moduleId
	 * @return List<Action>
	 */
	public List<Action> getAvailableActions(Integer moduleId);

	/**
	 * Get loader connections
	 * 
	 * @return List<LoaderConnection>
	 */
	public List<LoaderConnection> getLoaderConnections();

	/**
	 * Get loader connection by aid
	 * 
	 * @param aid
	 * @return
	 */
	public LoaderConnection getLoaderConnection(Long aid);

	/**
	 * Add new loader connection
	 * 
	 * @param loaderConnection
	 */
	public void addLoaderConnection(LoaderConnection loaderConnection);

	/**
	 * Update loader connection
	 * 
	 * @param loaderConnection
	 */
	public void updateLoaderConnection(LoaderConnection loaderConnection);

	/**
	 * Delete loader parse message types
	 */
	public void deleteLdParseMsgeTypes();

	/**
	 * Add loader parse message type
	 * 
	 * @param type
	 */
	public void addLdParseMsgeType(String type);

	/**
	 * Get max loader connections
	 * 
	 * @return
	 */
	public Integer getMaxLoaderConnections();

	/**
	 * Sync loader connection from certain date
	 * 
	 * @param loaderConnection
	 * @param date
	 */
	public void syncLoaderConenction(LoaderConnection loaderConnection, Date date);

	/**
	 * Sync loader connection
	 * 
	 * @param loaderConnection
	 */
	public void syncLoaderConenction(LoaderConnection loaderConnection);

	/**
	 * Get list of users
	 * 
	 * @return List<User>
	 */
	public List<User> getUsers();

	/**
	 * Get user by id
	 * 
	 * @param userId
	 * @return User
	 */
	public User getUser(Long userId);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public User getUser(String userName);

	/**
	 * 
	 * @param userName
	 * @param profileName
	 * @return
	 */
	public List<User> getUsers(String userName, String profileName);

	/**
	 * Add database user
	 * 
	 * @param user
	 */
	public void addDatabaseUser(User user);

	/**
	 * Add reporting user
	 * 
	 * @param user
	 */
	public void addReportingUser(User user);

	/**
	 * Update user
	 * 
	 * @param user
	 */
	public void updateUser(User user);

	/**
	 * Delete database user
	 * 
	 * @param user
	 */
	public void deleteDatabaseUser(User user);

	/**
	 * Delete reporting user
	 * 
	 * @param user
	 */
	public void deleteReportingUser(User user);

	/**
	 * Delete application setting for specific user
	 * 
	 * @param profile
	 */
	public void deleteApplicationSettingsForUser(User user);

	/**
	 * Delete reporting users that assigned to certain profile
	 * 
	 * @param profile
	 */
	public void deleteReportingUser(Profile profile);

	/**
	 * Get user roles as list of strings
	 * 
	 * @param user
	 * @return List<String>
	 */
	public List<String> getUserRoles(User user);

	/**
	 * 
	 * @param db_roleName
	 * @return
	 */
	public boolean checkRole(String db_roleName);

	/**
	 * Get Roles, the key is the Granted Role and the value is the Admin Option
	 * 
	 * @return List<Pair<String, String>>
	 */
	public List<Pair<String, String>> getRoles();

	/**
	 * Delete profile access rights for a certain module
	 * 
	 * @param profileId
	 * @param moduleId
	 */
	public void deleteProfileAccessRights(Long profileId, Integer moduleId);

	/**
	 * Delete profile units
	 * 
	 * @param profileId
	 */
	public void deleteProfileUnits(Long profileId);

	/**
	 * Delete profile BICs
	 * 
	 * @param profileId
	 */
	public void deleteProfileBICs(Long profileId);

	/**
	 * Delete group sets
	 * 
	 * @param profileId
	 */
	public void deleteProfileGroupSets(Long profileId);

	/**
	 * Delete profile message categories
	 * 
	 * @param profileId
	 */
	public void deleteProfileMsgCats(Long profileId);

	/**
	 * Assign access rights to profile
	 * 
	 * @param profile
	 * @param programID
	 * @param ObjectID
	 */
	public void assignAccessRightsToProfile(Profile profile, Integer programID, Long ObjectID);

	/**
	 * Assign units to profile
	 * 
	 * @param profile
	 * @param units
	 */
	public void assignUnitsToProfile(Profile profile, List<String> units);

	/**
	 * Assign bics to profile
	 * 
	 * @param profile
	 * @param bics
	 */
	public void assignBICsToProfile(Profile profile, List<String> bics);

	/**
	 * Assign message categories to profile
	 * 
	 * @param profile
	 * @param msgCategories
	 */
	public void assignMsgCatsToProfile(Profile profile, List<String> msgCategories);

	/**
	 * Update approval status for a certain user
	 * 
	 * @param user
	 */
	public void updateApprovalStatus(User user);

	/**
	 * Update approval status for a certain profile
	 * 
	 * @param profile
	 */
	public void updateApprovalStatus(Profile profile);

	/**
	 * Update profile
	 * 
	 * @param profile
	 */
	public void updateProfile(Profile profile);

	/**
	 * Add profile
	 * 
	 * @param profile
	 */
	public void addProfile(Profile profile);

	/**
	 * Get profile by profile name
	 * 
	 * @param profileName
	 * @return
	 */
	public Profile getProfile(String profileName);

	/**
	 * Get getProfiles by profile name
	 * 
	 * @param profileName
	 * @return
	 */
	public List<Profile> getProfiles(String profileName);

	/**
	 * Delete profile
	 * 
	 * @param loggedInUser
	 * @param profile
	 */
	public void deleteProfile(String loggedInUser, Profile profile);

	/**
	 * Get all message categories
	 * 
	 * @return List<MsgCatPerProfile>
	 */
	public List<MsgCatPerProfile> getAllMessageCategories();

	/**
	 * Delete used units
	 */
	public void deleteUsedUnits();

	/**
	 * Return the query that returns the users
	 * 
	 * @return
	 * @author EastNets
	 * @since Nov 1, 2012
	 */
	public String getUsersQuery();

	/**
	 * Return the query that returns the users
	 * 
	 * @return
	 * @author EastNets
	 * 
	 */
	public String getUsersQuery(String userName, String profileName);

	/**
	 * Execute the given query to get the users
	 * 
	 * @param query
	 * @return
	 * @author EastNets
	 * @since Nov 1, 2012
	 */
	public List<User> executeUsersQuery(String query, String userName, String profileName);

	public boolean isDataBaseUser(String username);

	public boolean isProfileHaveUsers(Profile profile);

	public List<Date> getMaxMesgCreationDatebteween(LoaderConnection loaderConnection, Date date);

	public List<Date> getMaxMesgCreationDate(LoaderConnection loaderConnection, Date date);

	/**
	 * the user has session on the database or not , note that enReporting dont create a session for the logged in user, as we always use the generic user
	 * 
	 * @param username
	 * @return
	 */
	public boolean userHasSessionOnDB(String username);

	public boolean userHasRelationsOnDB(String username);

	public void deleteSearchTemplatesForUser(User user);

	public List<CsmConnection> getCsmConnection();

	public boolean checkCsmConnection(CsmConnection currentCsmConnection);

	public void addCsmConnection(CsmConnection csmConnection);

	public void updateCsmConnection(String serverName, Integer serverPort, CsmConnection csmConnection);

	public void deleteCsmConnection(CsmConnection csmConnection);

	public void updateSettings(AdminSettings settimgs) throws Exception;

	public void restSettings();

	public AdminSettings getSettings();

	void createSettings(AdminSettings settings);

	public List<String> getUserRoles(String username);

	/**
	 * Execute the given query to get the users Approval Status
	 * 
	 * @param satusId
	 * @return approvalStatus
	 * @author EastNets
	 * @since Jan 10, 2016
	 */

	public String getUserApprovalStatusDiscription(Long statusId);

	public List<User> filterDormantUsers(int dormancyPeriod, String userName, String profileName);

	// public void changeDoramncyStatus(User user);

	void setUserGroupId(Long userId, Long newGroupId);

	// public void changeDoramncyStatus(User user);

	public String getUserAuthenticationMethod(String userName);

	public List<Integer> getGroupAccessProgramIds(Long groupId);

	public void changeAuthMethodForNonDbUsers(Long authMethod);

	public void saveBaseCur(String loginUser, String baseCur);

	public List<String> getAvailableBaseCurrencies();

	public String getBaseCurrencyFromDB();

	public void saveBaseBiRepPath(String loginUser, String repPath, boolean setAsDefault);

	public List<JIResourceFolder> getJIResourceFolder();

	public void updateBiDefaultPath(String biDefPath);

	public String getDefaultBiPathFromDB();

	public void deleteJasperFolder(String folderUri);

	public void markJasperFolderAsDeleted(String folderUri);

	public Integer getWDEnablePDFTemplates();

	public void updateWDSettings(boolean enablePDFTemplates);

	public UserThemePreferences getUserThemePreferences(Long userId);

	public void updateUserThemePreferences(UserThemePreferences userThemePreferences, boolean update);

}
