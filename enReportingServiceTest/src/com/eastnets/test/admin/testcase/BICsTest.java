package com.eastnets.test.admin.testcase;

import java.util.List;
import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.test.BaseTest;

public class BICsTest extends BaseTest {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4435201069583403211L;

	@Test
    public void testGetLicensedBICs(){
    	
    	List<BicLicenseInfo> bics = this.getServiceLocater().getAdminService().getLicensedBICs(getLoggedInUser());
    	Assert.notEmpty(bics);
    }
    
    @Test
    public void testBICGetAddUpdateDeleteOperations() {
    	List<BicLicenseInfo> bics = this.getServiceLocater().getAdminService().getLicensedBICs(getLoggedInUser());
    	int count = bics.size();
//    	this.getServiceLocater().getAdminService().deleteBIC(getLoggedInUser(),"XXXXXXX0");
    	this.getServiceLocater().getAdminService().addBIC(getLoggedInUser(),"XXXXXXX0");
    	bics = this.getServiceLocater().getAdminService().getLicensedBICs(getLoggedInUser());
    	Assert.isTrue(bics.size() == count +1);//check add bic
    	
    	this.getServiceLocater().getAdminService().updateBIC(getLoggedInUser(),"XXXXXXX0", "XXXXXXY0");
    	bics = this.getServiceLocater().getAdminService().getLicensedBICs(getLoggedInUser());
    	boolean isBicUpdated = false;
    	for(BicLicenseInfo bic:bics){
    		if(bic.getBicCode().getBicCode().equals("XXXXXXY0")){
    			isBicUpdated = true;
    		}
    	}    	
    	Assert.isTrue(isBicUpdated);//check update bic
    	
    	this.getServiceLocater().getAdminService().deleteBIC(getLoggedInUser(),"XXXXXXY0");
    	bics = this.getServiceLocater().getAdminService().getLicensedBICs(getLoggedInUser());
    	Assert.isTrue(bics.size() == count);//check delete bic
    }
    
}
