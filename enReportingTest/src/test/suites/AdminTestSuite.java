package test.suites;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import admin.test.AdminTestGetting;
import admin.test.AdminTestAdding;
import admin.test.AdminTestUpdating;
import viewer.test.MessageViewerTest;

/*
 * This class is the main suite to run test classes,
 * it has a specific order for admin test classes,
 * as it runs at first test methods that store new data on the database,
 * then it runs test methods that update or delete the added data, and then it runs methods which are used to retrieve data.
 *  Check each one of these classes and read the comments above the class declaration to understand how each one works.
 */

@RunWith(Suite.class)
@SuiteClasses({ AdminTestAdding.class, AdminTestUpdating.class, AdminTestGetting.class})
public class AdminTestSuite {

	private static final Logger logger = Logger.getLogger(AdminTestSuite.class);

	public static void main(String[] args) {

		Result result = JUnitCore.runClasses(AdminTestSuite.class);

		logger.info("Number of Test Case: " + result.getRunCount());
		System.out.println();

		if (result.wasSuccessful()) {
			logger.info("FINISHED SUCCESSFULLY");

		} else {
			logger.error("FAILED:  " + "Number of Failures: " + result.getFailureCount());

		}

	}

}
