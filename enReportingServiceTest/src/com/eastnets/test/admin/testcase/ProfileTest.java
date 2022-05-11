package com.eastnets.test.admin.testcase;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.admin.Action;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.test.BaseTest;


public class ProfileTest extends BaseTest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6155922703158328232L;
	private static String profileName = "test" + (new Date()).getTime();
	private static Profile testAddedProfile;
	
	@Test
    public void testGetAvailableWatchdogActions() {
    	List<Action> actions = this.getServiceLocater().getAdminService().getWatchdogAvailableActions(getLoggedInUser());
    	Assert.isTrue(actions.size() > 0);
	}	
	
	@Test
    public void testGetAvailableReportingActions() {
    	List<Action> actions = this.getServiceLocater().getAdminService().getReportingAvailableActions(getLoggedInUser());
    	Assert.isTrue(actions.size() > 0);
    }	
	
	@Test
    public void testGetAvailableViewerActions() {
    	List<Action> actions = this.getServiceLocater().getAdminService().getViewerAvailableActions(getLoggedInUser(),null);
    	Assert.isTrue(actions.size() > 0);
    }	
	
	@Test
    public void testAddProfile() {
		List<Profile> profiles = this.getServiceLocater().getAdminService().getProfiles(getLoggedInUser());
		testAddedProfile = profiles.get(0);
		testAddedProfile.setName(profileName);
		testAddedProfile.setGroupId(null);
		ProfileDetails details= new ProfileDetails(testAddedProfile); 
    	this.getServiceLocater().getAdminService().addProfile(getLoggedInUser(),details);
    	List<Profile> latestProfilesList = this.getServiceLocater().getAdminService().getProfiles(getLoggedInUser());
    	testAddedProfile.setGroupId(latestProfilesList.get(latestProfilesList.size()-1).getGroupId());
    	Assert.isTrue(profiles.size()+1 == latestProfilesList.size());
    }
	
	@Test
	public void testUpdateApprovalStatus(){
		Long approvalStatusId = testAddedProfile.getApprovalStatus().getId();
		Assert.isTrue(approvalStatusId.longValue() == 3);
		this.getServiceLocater().getAdminService().updateProfileApprovalStatus("LSA", testAddedProfile, true);
		Assert.isTrue(testAddedProfile.getApprovalStatus().getId().longValue() == 1);
		this.getServiceLocater().getAdminService().updateProfileApprovalStatus("RSA", testAddedProfile, true);
		Assert.isTrue(testAddedProfile.getApprovalStatus().getId().longValue() == 0);
		
	}
	
	@Test
	public void testUpdateProfile(){
		String updatedDescription = "TEST " + (new Date()).getTime();
		testAddedProfile.setDescription(updatedDescription);
		ProfileDetails details= new ProfileDetails(testAddedProfile); 
		this.getServiceLocater().getAdminService().updateProfile(getLoggedInUser(), details,null);
		Assert.isTrue(this.getServiceLocater().getAdminService().getProfile(getLoggedInUser(), testAddedProfile.getName()).getDescription().equals(updatedDescription));
		
		
	}	
	
	@Test
    public void testGetProfiles() {
    	List<Profile> profiles = this.getServiceLocater().getAdminService().getProfiles(getLoggedInUser());
    	Assert.isTrue(profiles.size() > 0);
    }
	
	@Test
    public void testGetProfile() {
		Profile profile = this.getServiceLocater().getAdminService().getProfile(getLoggedInUser(),testAddedProfile.getName());
    	Assert.isTrue(profile.getGroupId().equals(testAddedProfile.getGroupId()));
    }
	
	@Test
    public void testDeleteProfile() {
		int beforeDeleteSize = this.getServiceLocater().getAdminService().getProfiles(getLoggedInUser()).size();
		this.getServiceLocater().getAdminService().deleteProfile(getLoggedInUser(), testAddedProfile);
		int afterDeleteSize = this.getServiceLocater().getAdminService().getProfiles(getLoggedInUser()).size();
		Assert.isTrue(beforeDeleteSize -1 == afterDeleteSize);
    }	

	@Test
	
	public void groupAccessPrograms(){
		List<Integer> groupAccessProgramIds = this.getServiceLocater().getAdminService().getGroupAccessProgramIds(1L);
		for(Integer id:groupAccessProgramIds){
			System.out.println(id);
		}
	}

}
