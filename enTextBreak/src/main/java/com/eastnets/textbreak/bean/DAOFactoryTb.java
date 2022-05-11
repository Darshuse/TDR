package com.eastnets.textbreak.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.textbreak.dao.TextBreakDaoImpl;
import com.eastnets.textbreak.dao.TextBreakOracleDaoImpl;
import com.eastnets.textbreak.dao.TextBreakSqlDaoImpl;

@Configuration
public class DAOFactoryTb {

	@Autowired
	TextBreakConfig textBreakConfig;

	@Bean
	public TextBreakDaoImpl getTextBreakDAO() {
		if (textBreakConfig.getDbType().trim().equalsIgnoreCase("ORACLE")) {
			return new TextBreakOracleDaoImpl();
		} else {
			return new TextBreakSqlDaoImpl();
		}
	}
}
