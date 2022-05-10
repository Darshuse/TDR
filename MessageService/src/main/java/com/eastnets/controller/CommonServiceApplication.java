package com.eastnets.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

import com.eastnets.config.CommonServiceConfiguration;

@SpringBootApplication
@EnableCaching
@ImportResource("classpath:/spring-config/applicationContext.xml")
public class CommonServiceApplication {

	private static final Logger logger = LogManager.getLogger(CommonServiceApplication.class);

	public static void main(String[] args) {

		logger.debug("WatchdogApplication main method");

		ArgumentsHandler argumentsHandler = new ArgumentsHandler();
		CommonServiceConfiguration config = argumentsHandler.readCommandArgs(args);

		if (config != null) {
			argumentsHandler.createPropertyFile(config);
		}

		SpringApplication.run(CommonServiceApplication.class, args);

	}

}
