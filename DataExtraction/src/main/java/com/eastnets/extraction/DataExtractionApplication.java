package com.eastnets.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataExtractionApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataExtractionApplication.class);

	public static void main(String[] args) {

		try {

			new SpringApplicationBuilder(DataExtractionApplication.class).bannerMode(Mode.OFF).run(args);

		} catch (Exception e) {
			LOGGER.error("DataExtraction  exception  :: " + e.getMessage());

			e.printStackTrace();
		}

	}

}
