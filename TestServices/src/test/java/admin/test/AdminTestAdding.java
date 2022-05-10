package admin.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eastnets.domain.admin.ApprovalStatus;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.domain.admin.User;
import com.eastnets.service.admin.AdminService;

import enReporting.test.base.BaseTestApp;

/*
 * This test class is used to test Methods that add data to the database, which means after running this class the data you inserted in the excel file will be added to the database.
 * Same data used in these adding test methods must be used in the update and delete test methods.
 * 
 * Normally this is considered as bad practice, but we had a very good reason:
 *  - We need to have the test cases succeed on different databases and -We don't want for our new data to affect other components, so just to be safe.
 *  
 *  There's an excel file provided under Test_Files folder named 'Admin_Test.xls' in which you provide input data and the expected result for the methods in this class that takes parameters.
 *  
 *  Some of the parameters will actually be converted to an Array and some parameters MUST have particular values, so READ THE COMMENT Above every test method to understand what data to insert.
 *  
 *  There's Another file named 'Input_Data_Options' that contains the possible values for some special parameters which must take specific values so just copy and paste on Admin_Test file. 
 *  
 *  If you want to know what a particular number of a specific field represents, check the Constants and User classes.  
 *  
 *  For Parameters that take more than one value, values should be separated by a comma.
 *  
 *  Check the provided Admin_Test file and see how they are inserted.
 *  
 */

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "Test_Files/Admin_Test.xls", loaderType = LoaderType.EXCEL)
public class AdminTestAdding extends BaseTestApp {

	private static AdminService adminService;

	@BeforeClass
	public static void init() {

		adminService = (AdminService) getContext().getBean("adminService");

	}

	/*
	 * BIC should be a valid BIC (8 characters and end with zero '0') If the BIC
	 * already exists an exception would be thrown
	 * 
	 */

	@Test
	public void testAddBic(@Param(name = "BIC") String bic, @Param(name = "EXPECTED_RESULT") Boolean expectedResult)
			throws SQLException {

		try {
			adminService.addBIC(getLoggedInUser(), bic.trim());

		} catch (Exception e) {

			if (expectedResult)
				assert (false);

			else
				assert (true);

			return;
		}

		List<String> bics = adminService.getBICs();

		if (expectedResult)
			assert (bics.contains(bic.trim()));
		else
			assert (!bics.contains(bic.trim()));
	}

	/*
	 * Password can be anything if the Authentication method is not equal to 2
	 * (DATABASE USER). Username can be anything, Profilename must be a valid
	 * profilename, which means it has to be stored in the database, Fullname is
	 * optional, AuthenticationMethod should be between 0 and 5 ( Defines user
	 * type ) ApprovalId must be between 0 and 3 ( 0 is approved, 3 is no
	 * approved
	 * 
	 */

	@Test
	public void testAddUser(@Param(name = "USER_NAME") String username,
			@Param(name = "PROFILE_NAME") String profilename, @Param(name = "FULL_NAME") String fullname,
			@Param(name = "PASSWORD") String password, @Param(name = "AUTH_METHOD") Integer authMethod,
			@Param(name = "APPROVAL_STATUS") Long approvalID) {

		ApprovalStatus approvalStatus = new ApprovalStatus();
		approvalStatus.setId(approvalID);

		Profile profile = adminService.getProfile(getLoggedInUser(), profilename);

		if (profile == null)
			assert (false) : "FAILED TO GET THE PROFILE";

		User user = new User();
		user.setApprovalStatus(approvalStatus);
		user.setPassword(password);
		user.setUserName(username);

		if (fullname != null)
			user.setFullUserName(fullname);

		user.setAuthenticationMethod(authMethod);
		user.setProfile(profile);
		adminService.addUser(getLoggedInUser(), user);

		List<User> users = adminService.getUsers(getLoggedInUser());

		assert (users.contains(user));

	}

	/*
	 * YOU NEED TO CHECK 'Input_Data_Options.xls' FILE TO KNOW WHAT VALUES CAN
	 * BE INSERTED FOR EACH PARAMETER, FOR PARAMETERS THAT TAKES MORE THAN ONE
	 * VALUE REMEMBER TO INSERT A COMMAS ',' BETWEEN THE VALUES.
	 * 
	 * PROFILE_NAME SHOULD BE A NEW PROFILE NAME, REMEBER TO USE THIS EXACT NAME
	 * IN 'testDeleteProfile' METHOD FOR THE DATABASE TO BE CONSISTENT
	 * 
	 * PROFILE_DESC IS OPTIONAL
	 * 
	 * APPROVAL_STATUS_ID SHOULD BE BETWEEN 0 and 3
	 * 
	 * FOR OTHER PARAMETERS YOU NEED TO CHECK THE 'Input_Data_Options'. IT'S
	 * MANDATORY FOR YOU TO ADD ONLY PROVIDED VALUES IN THE INPUT FILE OR AN
	 * EXCEPTION MAY BE THROWN OR MAYBE IT WON't BUT OTHER COMPONENTS OF THE
	 * SYSTEM MAY BE AFFECTED.
	 */
	@Test
	public void testAddProfile(@Param(name = "PROFILE_NAME") String profilename,
			@Param(name = "PROFILE_DESC") String description, @Param(name = "GROUP_ID") String groupID,
			@Param(name = "APPROVAL_STATUS_ID") Long approvalID, @Param(name = "PROFILE_ROLES") String profileRoles,
			@Param(name = "REPORTING_ACTIONS") String reportingActions,
			@Param(name = "VIEWER_ACTIONS") String viewerActions,
			@Param(name = "WATCHDOG_ACTIONS") String watchdogActions,
			@Param(name = "DASHBOARD_ACTIONS") String dashboardActions,
			@Param(name = "AUTHORIZED_BICS") String authorizedBICs,
			@Param(name = "MESSAGE_CATEGORIES") String messageCategories,
			@Param(name = "AUTHORIZED_UNITS") String authorizedUnits) {

		Profile profile = new Profile();

		ApprovalStatus approvalStatus = new ApprovalStatus();
		approvalStatus.setId(approvalID);

		profile.setName(profilename);
		profile.setApprovalStatus(approvalStatus);

		if (description != null && !description.isEmpty())
			profile.setDescription(description);

		String[] reportingActionsArray, viewerActionsArray, watchdogActionsArray, authorizedBICsArray,
				messageCategoriesArray, authorizedUnitsArray, rolesArray, dashboardActionsArray;
		List<String> rolesList = new ArrayList<>();
		List<String> reportingActionsList = new ArrayList<>();
		List<String> viewerActionsList = new ArrayList<>();
		List<String> watchdogActionsList = new ArrayList<>();
		List<String> authorizedBICsList = new ArrayList<>();
		List<String> messageCategoriesList = new ArrayList<>();
		List<String> authorizedUnitsList = new ArrayList<>();
		List<String> dashboardActionsList = new ArrayList<>();

		if (profileRoles != null) {
			rolesArray = profileRoles.split(",");
			rolesList = Arrays.asList(rolesArray);
			profile.setProfileRoles(rolesList);
		}

		ProfileDetails profileDetails = new ProfileDetails(profile);

		if (reportingActions != null) {
			reportingActionsArray = reportingActions.split(",");
			reportingActionsList = Arrays.asList(reportingActionsArray);
			profileDetails.setAuthorizedReportingActions(reportingActionsList);
		}

		if (viewerActions != null) {
			viewerActionsArray = viewerActions.split(",");
			viewerActionsList = Arrays.asList(viewerActionsArray);
			profileDetails.setAuthorizedViewerActions(viewerActionsList);
		}

		if (watchdogActions != null) {
			watchdogActionsArray = watchdogActions.split(",");
			watchdogActionsList = Arrays.asList(watchdogActionsArray);
			profileDetails.setAuthorizedWatchdogActions(watchdogActionsList);
		}
		if (authorizedBICs != null) {
			authorizedBICsArray = authorizedBICs.split(",");
			authorizedBICsList = Arrays.asList(authorizedBICsArray);
			profileDetails.setAuthorizedBICCodes(authorizedBICsList);
		}
		if (messageCategories != null) {
			messageCategoriesArray = messageCategories.split(",");
			messageCategoriesList = Arrays.asList(messageCategoriesArray);
			profileDetails.setAuthorizedMessageCategories(messageCategoriesList);
		}
		if (authorizedUnits != null) {
			authorizedUnitsArray = authorizedUnits.split(",");
			authorizedUnitsList = Arrays.asList(authorizedUnitsArray);
			profileDetails.setAuthorizedUnits(authorizedUnitsList);
		}
		if (dashboardActions != null) {
			dashboardActionsArray = dashboardActions.split(",");
			dashboardActionsList = Arrays.asList(dashboardActionsArray);
			profileDetails.setAuthorizedDashboardActions(dashboardActionsList);
		}

		adminService.addProfile(getLoggedInUser(), profileDetails);

		assert (adminService.getProfile(getLoggedInUser(), profilename) != null);

	}

}
