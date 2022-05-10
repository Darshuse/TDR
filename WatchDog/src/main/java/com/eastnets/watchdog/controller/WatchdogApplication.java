package com.eastnets.watchdog.controller;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import com.eastnets.watchdog.service.ServiceInitiator;
import com.eastnets.watchdog.utility.WatchDogConstants;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "com.eastnets.*")
@ImportResource("classpath:/spring-config/applicationContext.xml")
public class WatchdogApplication {

	private static ApplicationContext applicationContext;

	private static final Logger logger = Logger.getLogger(WatchdogApplication.class);

	static {

		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}

	public static void main(String[] args) {

		logger.debug("WatchdogApplication main method");
		printProductInfo();
		applicationContext = SpringApplication.run(WatchdogApplication.class, args);
		ServiceInitiator serviceInitiator = (ServiceInitiator) applicationContext.getBean("serviceInitiator");
		serviceInitiator.startApplication(applicationContext);
	}

	private static void printProductInfo() {
		System.out.println(String.format("%s\n%s %s.%s.%s Build %s", WatchDogConstants.EN_COPYRIGHT, WatchDogConstants.PRODUCT_NAME, WatchDogConstants.PRODUCT_VERSION_MAJOR, WatchDogConstants.PRODUCT_VERSION_MINOR,
				WatchDogConstants.PRODUCT_VERSION_PATCH, WatchDogConstants.PRODUCT_VERSION_BUILD));
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		WatchdogApplication.applicationContext = applicationContext;
	}

}
