package com.eastnets.test.admin.testcase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.LoaderSettings;
import com.eastnets.test.BaseTest;

public class LoaderTest extends BaseTest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4098382155901168671L;
	private static LoaderSettings loaderSettings;

	
    @Test
    public void testGetLoaderSettings() {
    	loaderSettings = this.getServiceLocater().getAdminService().getLoaderSettings(getLoggedInUser());
    	Assert.notNull(loaderSettings);
    	Assert.notNull(loaderSettings.getActiveTime());
    }
   
    @Test
    public void testUpdateLoaderSettings() {
    	LoaderSettings loaderSettings = this.getServiceLocater().getAdminService().getLoaderSettings(getLoggedInUser());
    	Assert.notNull(loaderSettings);    	
    	Assert.notNull(loaderSettings.getActiveTime()); 
    	int activeTime = loaderSettings.getActiveTime();
    	loaderSettings.setActiveTime(activeTime + 1);
    	this.getServiceLocater().getAdminService().updateLoaderSettings(getLoggedInUser(), loaderSettings,null);
    	loaderSettings = this.getServiceLocater().getAdminService().getLoaderSettings(getLoggedInUser());
    	Assert.isTrue(loaderSettings.getActiveTime() == activeTime + 1); 
    	// return value back as before
    	loaderSettings.setActiveTime(activeTime);
    	this.getServiceLocater().getAdminService().updateLoaderSettings(getLoggedInUser(), loaderSettings,null);
    }
    
	 @Test
	  public void testAddLoaderConnections()
	  {
		  List<LoaderConnection> loaderConnections = this.getServiceLocater().getAdminService().getLoaderConnections(getLoggedInUser());
		  int beforeAddConnectionSize = loaderConnections.size();
		  long i =102;
		  LoaderConnection loaderConnection1 = new LoaderConnection();
		  loaderConnection1.setAllianceInfo("Info " + i);
		  loaderConnection1.setTimeOut(60 + i);
		  loaderConnection1.setServerAddress("192.168.100.1" + (60 + i));
		  loaderConnection1.setPort(601 + i);
		  this.getServiceLocater().getAdminService().addLoaderConnection(getLoggedInUser(), loaderConnection1);
		  
		  Assert.isTrue(loaderConnection1.getReturenStatus() == 1);
		  		  
		  loaderConnections = this.getServiceLocater().getAdminService().getLoaderConnections(getLoggedInUser());
		  int afterAddConnectionSize = loaderConnections.size();
		  Assert.isTrue(beforeAddConnectionSize +1 == afterAddConnectionSize);

	  }

    
    @Test
    public void testGetLoaderConnections() {
    	loaderSettings = this.getServiceLocater().getAdminService().getLoaderSettings(getLoggedInUser());
    	Assert.notNull(loaderSettings);
    	Assert.notNull(loaderSettings.getActiveTime());
    }   
    
	@Test
	public void testSyncLoaderConnection()
	{
		
		List<LoaderConnection> loaderConnections = this.getServiceLocater().getAdminService().getLoaderConnections(getLoggedInUser());
		
		Assert.notNull(loaderConnections);
		Assert.notEmpty(loaderConnections);
		
		for (LoaderConnection loaderConnection : loaderConnections) {			  
			this.getServiceLocater().getAdminService().syncLoaderConnection(getLoggedInUser(), loaderConnection);
			
		}
	}
	@Test
	public void testSyncLoaderConnectionByDate()
	{
		
		List<LoaderConnection> loaderConnections = this.getServiceLocater().getAdminService().getLoaderConnections(getLoggedInUser());
		
		Assert.notNull(loaderConnections);
		Assert.notEmpty(loaderConnections);
		Calendar instance = Calendar.getInstance();
		instance.set(2012, Calendar.AUGUST, 26);
		Date date = instance.getTime();
		
		for (LoaderConnection loaderConnection : loaderConnections) {			  
			this.getServiceLocater().getAdminService().syncLoaderConnection(getLoggedInUser(), loaderConnection, date);
			
		}
	}
	
}
