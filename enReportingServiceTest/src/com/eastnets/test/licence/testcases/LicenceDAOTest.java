package com.eastnets.test.licence.testcases;

import org.junit.Assert;
import org.junit.Test;

import com.eastnets.test.BaseTest;

public class LicenceDAOTest extends BaseTest {
//    @Test
//    public void testGetLicensedBICs(){
//    	this.getServiceLocater().getLicenceService().checkLicence();
//    } 
    
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2179659248974553883L;

	@Test
    public void testCheckProduct(){
    	Assert.assertTrue(this.getServiceLocater().getLicenseService().checkProduct("01"));
    } 
}
