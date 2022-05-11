package com.eastnets.textbreak.bean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.textbreak.readers.DataReader;
import com.eastnets.textbreak.readers.DbReader;
import com.eastnets.textbreak.service.TextBreakRepositoryService;

@Configuration
public class DataSourceFactory {

	private static final Logger LOGGER = Logger.getLogger(DataSourceFactory.class);

	@Autowired
	private TextBreakRepositoryService textBreakRepositoryService;

	@Autowired
	TextBreakConfig textBreakConfig;

	@Bean
	public DataReader getDataSource() {
		LOGGER.debug("Start textbreak with ofline mode");
		return new DbReader();
	}

	public TextBreakRepositoryService getTextBreakRepositoryService() {
		return textBreakRepositoryService;
	}

	public void setTextBreakRepositoryService(TextBreakRepositoryService textBreakRepositoryService) {
		this.textBreakRepositoryService = textBreakRepositoryService;
	}

}
