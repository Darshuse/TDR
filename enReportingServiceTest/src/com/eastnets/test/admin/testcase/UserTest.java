package com.eastnets.test.admin.testcase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.Pair;
import com.eastnets.domain.admin.ApprovalStatus;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.test.BaseTest;

public class UserTest extends BaseTest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3745166673316458106L;
	private static User testAddedUser;
	private static String userName = "test" + (new Date()).getTime();
	
	@Test
	public void testAddUser() {

		User user = new User();
		ApprovalStatus approvalStatus = new ApprovalStatus();
		approvalStatus.setId(Constants.UNAPPROVED_WAITING_RSA_AND_LSA_APPROVAL);

		Profile profile = this.getServiceLocater().getAdminService()
				.getProfiles(getLoggedInUser()).get(0);

		user.setApprovalStatus(approvalStatus);
		user.setProfile(profile);

		user.setFullUserName("AddedByTest");
		user.setPassword("test");
		user.setUserName(userName);
		List<String> userRoles = new ArrayList<String>();

		profile.setProfileRoles(userRoles);

		userRoles.add("SIDE_WATCHDOG");
		userRoles.add("SIDE_ADMIN_OPERATOR");
		userRoles.add("SIDE_REPORT");
		userRoles.add("SIDE_ADMIN");
		userRoles.add("SIDE_EVENT");
		userRoles.add("SIDE_OPER");
		userRoles.add("SIDE_ADMIN_SECURITY");
		userRoles.add("SIDE_LOADER");
		userRoles.add("SIDE_VIEWER");
		user.setAuthenticationMethod(User.AUTH_REPORTING);
		this.getServiceLocater().getAdminService().addUser(getLoggedInUser(), user);
		List<User> users = this.getServiceLocater().getAdminService().getUsers(getLoggedInUser());
		testAddedUser = users.get(users.size()-1);
		Assert.isTrue(user.getFullUserName().equals(testAddedUser.getFullUserName()));
	}
	
	@Test
	public void testUpdateUserAprovalStatus()
	{
		Long approvalStatusId = testAddedUser.getApprovalStatus().getId();
		Assert.isTrue(approvalStatusId.longValue() == 3);
		this.getServiceLocater().getAdminService().updateUserApprovalStatus("LSA", testAddedUser, true);
		Assert.isTrue(testAddedUser.getApprovalStatus().getId().longValue() == 1);
		this.getServiceLocater().getAdminService().updateUserApprovalStatus("RSA", testAddedUser, true);
		Assert.isTrue(testAddedUser.getApprovalStatus().getId().longValue() == 0);
	}		
	
	@Test
	public void testGetUsers() {
		List<User> users = this.getServiceLocater().getAdminService().getUsers(getLoggedInUser());
		Assert.notNull(users);
		Assert.isTrue(users.size()>0);
	}

	@Test
	public void testUpdateUser() {
		testAddedUser.setFullUserName("TestUserToUpdate");
		this.getServiceLocater().getAdminService().updateUser(getLoggedInUser(), testAddedUser,null,null);
		User user = this.getServiceLocater().getAdminService().getUser(getLoggedInUser(), testAddedUser.getUserId());
		Assert.isTrue(user.getFullUserName().equals("TestUserToUpdate"));

	}

	@Test
	public void testDeleteUser() {
		User user = null;
		try{
			testAddedUser.setDeleteFromDatabase(true);
			this.getServiceLocater().getAdminService().deleteUser(getLoggedInUser(),testAddedUser);
			user = this.getServiceLocater().getAdminService().getUser(getLoggedInUser(),testAddedUser.getUserId());
		}catch(Exception e){
		}
		Assert.isNull(user);
	
	}


	@Test
	public void testGetRoles() {
		List<Pair<String, String>> roles = this.getServiceLocater().getAdminService().getRoles(getLoggedInUser());
		Assert.notNull(roles);
		Assert.notEmpty(roles);
	}
	


}
