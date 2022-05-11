package admin.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.AdminSettings;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.LoaderSettings;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.service.admin.AdminService;

import enReporting.test.base.BaseAppTest;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "Test_Files/Admin_Test.xls", loaderType = LoaderType.EXCEL)
public class AdminTestGetting extends BaseAppTest {

	private static AdminService adminService;

	@BeforeClass
	public static void init() {

		adminService = (AdminService) getContext().getBean("adminService");

	}

	@Test
	public void testGetProfile(@Param(name = "PROFILE_NAME") String profileName,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		Profile profile = adminService.getProfile(getLoggedInUser(), profileName.trim());

		assertNotNull(profile);
	}

	@Test
	public void testHasLoaderRoleTrue(@Param(name = "username") String username,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		User user = adminService.getUser(username.trim());

		if (expectedResult)

			assert (adminService.hasLoaderRole(user));

		else

			assert (!adminService.hasLoaderRole(user));
	}

	@Test
	public void testGetUsersByFilter(@Param(name = "USER_NAME") String username,
			@Param(name = "PROFILE_NAME") String profileName, @Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		List<User> users = adminService.getUsersByFilter(username,"", profileName);

		if (expectedResult) {
			assertNotNull(users);
			assert (!users.isEmpty());
		} else {
			assert (users == null || users.isEmpty());
		}

	}

	@Test
	public void testCheckRole(@Param(name = "DB_ROLE_NAME") String roleName,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		boolean checkResult = adminService.checkRole(roleName);
		if (expectedResult)
			assertTrue(checkResult);
		else
			assertFalse(checkResult);

	}

	@Test
	public void testGetUserWithID(@Param(name = "USER_ID") Long userId,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		User user = adminService.getUser(userId);

		if (expectedResult)
			assertNotNull(user);
		else
			assertNull(user);

	}

	@Test
	public void testIsDataBaseUser(@Param(name = "USER_NAME") String username,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		if (expectedResult)
			assertTrue(adminService.isDataBaseUser(getLoggedInUser(), username));
		else
			assertFalse(adminService.isDataBaseUser(getLoggedInUser(), username));
	}

	@Test
	public void testIsProfileHaveUsers(@Param(name = "PROFILE_NAME") String profileName,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		if (expectedResult)
			assertTrue(adminService.isProfileHaveUsers(getLoggedInUser(),
					adminService.getProfile(getLoggedInUser(), profileName)));

		else
			assertFalse(adminService.isProfileHaveUsers(getLoggedInUser(),
					adminService.getProfile(getLoggedInUser(), profileName)));
	}

	@Test
	public void testUserHasSessionOnDB(@Param(name = "USER_NAME") String username,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		if (expectedResult)
			assertTrue(adminService.userHasSessionOnDB(getLoggedInUser(), username));
		else
			assertFalse(adminService.userHasSessionOnDB(getLoggedInUser(), username));

	}

	@Test
	public void testUserHasRelationsOnDB(@Param(name = "USER_NAME") String username,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		if (expectedResult)
			assertTrue(adminService.userHasRelationsOnDB(username));
		else
			assertFalse(adminService.userHasRelationsOnDB(username));
	}

	@Test
	public void testGetLoaderSettings() {

		LoaderSettings loaderSettings = adminService.getLoaderSettings(getLoggedInUser());

		assertNotNull(loaderSettings);
	}

	@Test
	public void testGetLicensedBics() {

		List<BicLicenseInfo> bics = adminService.getLicensedBICs(getLoggedInUser());

		assert (bics != null && !bics.isEmpty());
	}

	@Test
	public void testGetBics() {

		List<String> bics = adminService.getBICs();
		assert (bics != null && !bics.isEmpty());
	}

	@Test
	public void testGetAllUnits() {

		List<String> units = adminService.getAllUnits(getLoggedInUser());
		assert (units != null && !units.isEmpty());

	}

	@Test
	public void testGetUsedUnits() {
		List<String> units = adminService.getUsedUnits(getLoggedInUser());
		assert (units != null && !units.isEmpty());

	}

	@Test
	public void testGetProfiles() {

		List<Profile> profiles = adminService.getProfiles(getLoggedInUser());

		assert (profiles != null && !profiles.isEmpty());
	}

	@Test
	public void testGetWatchdogAvailableActions() {

		List<Action> actions = adminService.getWatchdogAvailableActions(getLoggedInUser());

		assertNotNull(actions);
	}

	@Test
	public void testGetReportingAvailableActions() {

		List<Action> actions = adminService.getReportingAvailableActions(getLoggedInUser());

		assertNotNull(actions);
	}

	@Test
	public void testGetDashboardAvailableActions() {

		List<Action> actions = adminService.getDashboardAvailableActions(getLoggedInUser());

		assertNotNull(actions);
	}

	@Test
	public void testGetLoaderConnections() {

		List<LoaderConnection> connections = adminService.getLoaderConnections(getLoggedInUser());

		assertNotNull(connections);
	}

	@Test
	public void testGetUsers() {

		List<User> users = adminService.getUsers(getLoggedInUser());

		assertNotNull(users);
		assert (!users.isEmpty());
	}

	@Test
	public void testGetSettings() {

		AdminSettings adminSettings = adminService.getSettings();
		assertNotNull(adminSettings);
	}

	@Test
	public void testGetUserRoles(@Param(name = "USER_NAME") String username) {

		List<String> userRoles = adminService.getUserRoles(username);
		assertNotNull(userRoles);

	}

}
