/**
 * 
 */
package com.eastnets.enGpiNotifer.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mkassab
 *
 */
@Configuration
public class PropConfig {



	@Bean
	public PropertyPlaceholderConfigurer placeholderConfigurer()  {

		String inPropertiesFile = "notifIconfig.properties";
		InputStream is=null;
		try {
			is = new FileInputStream(inPropertiesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		configurer.setProperties(props);
		// if(configApp.getEnableDebugMode()){
		// enableDebugLoggerMode();
		// }
		// setProperties(prop);

		return configurer;
	}

}
