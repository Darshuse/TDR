package com.eastnets.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CommonServicePropertiesInitiator {

	private static final Logger LOGGER = LogManager.getLogger(CommonServicePropertiesInitiator.class);

	public CommonServicePropertiesInitiator() {
	}

	@Bean
	public static PropertyPlaceholderConfigurer placeholderConfigurer() {

		Properties props = new Properties();

		try {

			props.load(new ClassPathResource("commonService.properties").getInputStream());

		} catch (IOException e) {
			LOGGER.error("context", e);
		}

		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		configurer.setProperties(props);

		return configurer;
	}

}
