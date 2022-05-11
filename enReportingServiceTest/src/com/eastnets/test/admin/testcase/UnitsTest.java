package com.eastnets.test.admin.testcase;

import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.test.BaseTest;

public class UnitsTest extends BaseTest{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3039290894230625730L;

	@Test
    public void testGetAllUnits(){
    	List<String> units = this.getServiceLocater().getAdminService().getAllUnits(getLoggedInUser());
    	Assert.notEmpty(units);   	
    }
 
/*    @Test
    public void testUsedUnitsGetUpdate(){// just make sure that call is done without any problems is enough here
    	List<String> units = serviceLocator.getAdminService().getUsedUnits(getLoggedInUser());
    	Assert.notEmpty(units);   	
    	serviceLocator.getAdminService().updateUsedUnits(getLoggedInUser(), units);
    	Assert.isTrue(true);// this means that call is done for updateusedunits without problems
    }    
*/
}
