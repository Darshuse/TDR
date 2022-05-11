package com.eastnets.messageStore;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.eastnets.configuration.AppConfig;
import com.eastnets.license.service.LicenseServiceImpl;

@ComponentScan(basePackages = { "com.eastnets" })
@SpringBootApplication
public class MessageStoreApplication implements CommandLineRunner {

	private static final Logger LOGGER = LogManager.getLogger(MessageStoreApplication.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private LicenseServiceImpl licenseServiceImpl;

	public static void main(String[] args) {

		LOGGER.info("MessageStoreAPI Tool 3.2.0 Build 1");
		SpringApplication.run(MessageStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Setting Logging level according to the defined property
		setLoggingLevel();

		if (!checkProduct()) {
			LOGGER.error("MessageStoreAPI Tool is not licensed....");
			System.exit(1);
		} else {
			LOGGER.info("MessageStoreAPI license is checked successfuly...");
			LOGGER.info("Starting ...");
		}

	}

	public boolean checkProduct() {
		try {
			return licenseServiceImpl.checkProduct();
		} catch (Exception e) {

			LOGGER.error(e.getMessage());
			return false;
		}

	}

	private void setLoggingLevel() {
		String loggingLevel = appConfig.getDebugLevel();
		Logger root = Logger.getRootLogger();
		if (loggingLevel == null || loggingLevel.isEmpty()) {
			loggingLevel = "INFO";
		}

		switch (loggingLevel.toUpperCase()) {

		case "TRACE":
			root.setLevel(Level.TRACE);
			break;
		case "DEBUG":
			root.setLevel(Level.DEBUG);
			break;
		case "WARN":
			root.setLevel(Level.WARN);
			break;
		case "INFO":
			root.setLevel(Level.INFO);
			break;
		case "ERROR":
			root.setLevel(Level.ERROR);
			break;
		default:
			root.setLevel(Level.INFO);
		}

	}

}
