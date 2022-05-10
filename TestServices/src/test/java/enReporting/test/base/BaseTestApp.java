package enReporting.test.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTestApp {

	private static ApplicationContext context;

	static {
		context = new ClassPathXmlApplicationContext("spring-config/applicationContext.xml");
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public String getLoggedInUser() {
		return "side";
	}

	public Long getLoggedInUserId() {
		return 174L;
	}

	public Long getLoggedGroupId() {
		return 2L;
	}

}
