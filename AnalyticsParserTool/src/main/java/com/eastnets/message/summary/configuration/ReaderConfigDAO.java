package com.eastnets.message.summary.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.calculated.measures.dao.CalculatedMeasuresReaderDAO;
import com.eastnets.calculated.measures.dao.CalculatedMeasuresReaderOracleDAO;
import com.eastnets.calculated.measures.dao.CalculatedMeasuresReaderSqlDAO;
import com.eastnets.message.summary.dao.MessageReaderDAO;
import com.eastnets.message.summary.dao.MessageReaderOracleDAO;
import com.eastnets.message.summary.dao.MessageReaderSqlDAO;

@Configuration
public class ReaderConfigDAO {

	@Autowired
	GlobalConfiguration globalConifguration;

	@Bean
	public MessageReaderDAO getMessageReaderDAO() {

		if (globalConifguration.getDbType().trim().equalsIgnoreCase("ORACLE")) {
			return new MessageReaderOracleDAO();
		} else {
			return new MessageReaderSqlDAO();
		}
	}

	@Bean
	public CalculatedMeasuresReaderDAO getCalculatedMeasuresDAO() {
		CalculatedMeasuresReaderDAO calculatedMeasuresReaderDAO = null;
		if (globalConifguration.getDbType().trim().equalsIgnoreCase("ORACLE")) {
			calculatedMeasuresReaderDAO = new CalculatedMeasuresReaderOracleDAO();
		} else {
			calculatedMeasuresReaderDAO = new CalculatedMeasuresReaderSqlDAO();
		}

		return calculatedMeasuresReaderDAO;
	}

}
