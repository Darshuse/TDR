package admin.test;

import java.util.List;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.service.admin.AdminService;

import enReporting.test.base.BaseAppTest;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "Test_Files/Admin_Test.xls", loaderType = LoaderType.EXCEL)
public class AdminTestUpdating extends BaseAppTest {

	private static AdminService adminService;

	@BeforeClass
	public static void init() {

		adminService = (AdminService) getContext().getBean("adminService");

	}

	/*
	 * If BIC value doesn't match any value in the database. IT'S PREFERED TO
	 * DELETE THE SAME BIC YOU ADDED in AdminTestAdding Class.
	 */

	@Test
	public void testDeleteBic(@Param(name = "BIC") String bic,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		adminService.deleteBIC(getLoggedInUser(), bic.trim());

		List<String> bics = adminService.getBICs();

		assert (!bics.contains(bic.trim()));
	}

	/*
	 * If PROFILE_NAME value doesn't match any value in the database. IT'S
	 * PREFERED TO DELETE THE SAME PROFILE YOU ADDED in AdminTestAdding Class.
	 */

	@Test
	public void testDeleteProfile(@Param(name = "PROFILE_NAME") String profileName,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		try {

			Profile profile = adminService.getProfile(getLoggedInUser(), profileName.trim());
			adminService.deleteProfile(getLoggedInUser(), profile);
			List<Profile> profiles = adminService.getProfiles(getLoggedInUser());

			assert (!profiles.contains(profile));

		} catch (Exception e) {
			if (expectedResult) {
				assert (false);
				return;
			}
			assert (true);
		}

	}

	/*
	 * If USER_NAME value doesn't match any value in the database. IT'S PREFERED
	 * TO DELETE THE SAME USER YOU ADDED in AdminTestAdding Class.
	 */

	@Test
	public void testDeleteUser(@Param(name = "USER_NAME") String username,
			@Param(name = "EXPECTED_RESULT") Boolean expectedResult) {

		try {
			User user = adminService.getUser(username);
			adminService.deleteUser(getLoggedInUser(), user);

			assert (!adminService.getUsers(getLoggedInUser()).contains(user));

		} catch (Exception e) {

			if (expectedResult)
				assert (false);
			assert (true);
		}

	}

}
