package com.eastnets.message.summary.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eastnets.audit.service.AuditService;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.configuration.GlobalConfiguration;

@Repository
public abstract class MessageReaderDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	GlobalConfiguration globalConfiguration;

	@Autowired
	AuditService auditService;

	public abstract List<MessageSummaryDTO> getMessageSummaryMesgInformation();

	public abstract String generateQuery();

	public abstract String getAppendixColumnsQuery();

	public abstract String getCurrenciesColumnsQuery();

	public abstract String buildFetchMessagesConditionsQuery();

}
