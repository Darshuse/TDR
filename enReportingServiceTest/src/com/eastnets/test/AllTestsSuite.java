package com.eastnets.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.eastnets.test.admin.AdminTestsSuite;
import com.eastnets.test.archive.ArchiveTestSuite;
import com.eastnets.test.common.CommonServiceTestSuite;
import com.eastnets.test.dashboard.DashboardTestSuite;
import com.eastnets.test.monitoring.MonitoringTestSuite;
import com.eastnets.test.security.SecurityServiceTestSuite;
import com.eastnets.test.viewer.ViewerTestSuite;
import com.eastnets.test.watchdog.WatchDogTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	AdminTestsSuite.class, 
	ArchiveTestSuite.class,
	CommonServiceTestSuite.class,
	DashboardTestSuite.class, 
	MonitoringTestSuite.class, 
	SecurityServiceTestSuite.class, 
//	SwingTestSuite.class,
	ViewerTestSuite.class,
	WatchDogTestSuite.class})
public class AllTestsSuite {

}
