package com.eastnets.textbreak.enTextBreak;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.eastnets.textbreak.utility.ArgumentHandlerTb;

@SpringBootApplication
@ComponentScan(basePackages = "com.eastnets.*")
public class EnTextBreakApplication implements CommandLineRunner {
	private static final String COPYRIGHT = "Copyright 1999-2021 EastNets";
	private static final String APP_NAME = "TextBreak Service 9.0.0";
	public static final Logger LOGGER;
	static ApplicationContext applicationContext = null;

	static {
		LOGGER = Logger.getLogger(EnTextBreakApplication.class);
	}

	@Autowired
	TextBreakInitializer textBreakInitializer;

	@Autowired
	ArgumentHandlerTb argumentHandler;

	public static void main(String[] args) {
		LOGGER.info(APP_NAME);
		LOGGER.info(COPYRIGHT);
		applicationContext = SpringApplication.run(EnTextBreakApplication.class, args);
		LOGGER.info("Start TextBreak service ..");
	}

	/**
	 * This Method to get serviceInitiator Bean and start all TextBreak service
	 */

	@Override
	public void run(String... args) throws Exception {
		// For Overwrite the arguments from prop file
		if (args != null && args.length != 0) {
			argumentHandler.fillCommandLineArgs(args);
		}
		textBreakInitializer.startService();
	}

}
