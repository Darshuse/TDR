package com.eastnets.message.summary;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.eastnets.message.summary.configuration.GlobalConfiguration;

@SpringBootApplication
@ComponentScan(basePackages = "com.eastnets.*")
public class MessageSummaryApplication implements CommandLineRunner {

	private static final Logger LOGGER = LogManager.getLogger(MessageSummaryApplication.class);

	@Autowired
	GlobalConfiguration globalConfiguration;

	public static void main(String[] args) {

		System.out.println("Running AnalyticsParserTool_3_2_0_2..\n");
		SpringApplication.run(MessageSummaryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Setting Logging level according to the defined property
		setLoggingLevel();

		LOGGER.info("Starting AnalyticsParser Tool");
	}

	private void setLoggingLevel() {
		String loggingLevel = globalConfiguration.getDebugLevel();
		// Logger root = Logger.getRootLogger();
		if (loggingLevel == null || loggingLevel.isEmpty()) {
			loggingLevel = "INFO";
		}

		switch (loggingLevel.toUpperCase()) {

		case "TRACE":
			Configurator.setLevel("com.eastnets", Level.TRACE);
			break;
		case "DEBUG":
			Configurator.setLevel("com.eastnets", Level.DEBUG);
			break;
		case "WARN":
			Configurator.setLevel("com.eastnets", Level.WARN);
			break;
		case "INFO":
			Configurator.setLevel("com.eastnets", Level.INFO);
			break;
		case "ERROR":
			Configurator.setLevel("com.eastnets", Level.ERROR);
			break;
		default:
			Configurator.setLevel("com.eastnets", Level.INFO);
		}

	}

}
