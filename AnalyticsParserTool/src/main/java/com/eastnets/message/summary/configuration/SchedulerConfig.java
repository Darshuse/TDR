package com.eastnets.message.summary.configuration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.eastnets.calculated.measures.service.CalculatedMeasuresService;
import com.eastnets.commonLkup.service.CommonLkupService;
import com.eastnets.message.summary.MessageSummaryApplication;
import com.eastnets.message.summary.service.MessageSummaryService;

@Configuration
@EnableScheduling
@PropertySource(value = { "file:${EN_REPORTING_CONFIG_HOME}/analyticsParserTool.properties" })
public class SchedulerConfig {

	private static final Logger LOGGER = LogManager.getLogger(SchedulerConfig.class);

	@Autowired
	public CommonLkupService commonLkupService;

	@Autowired
	public GlobalConfiguration globalConfiguration;

	@Autowired
	public MessageSummaryService messageSummaryService;

	@Autowired
	public CalculatedMeasuresService calculatedMeasuresService;

	@Autowired
	MessageSummaryApplication messageSummaryApplication;

	@Scheduled(cron = "${analyticsParserScheduler}")
	public void startMessageSummary() {
		LOGGER.info("Starting AnalyticsParser process");
		commonLkupService.cacheLookUpTables();
		messageSummaryService.startMigrationService();
	}

	@Scheduled(cron = "${analyticsParserDetailsScheduler}")
	public void startCalculatedMeasures() {
		LOGGER.info("Starting AnalyticsParserDetails process");
		commonLkupService.cacheLookUpTables();
		calculatedMeasuresService.startCalculatedMeasures();

	}

}
