package com.eastnets.watchdog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.dao.WatchdogOracleDaoImpl;
import com.eastnets.watchdog.dao.WatchdogSQLDaoImpl;

@Configuration
public class DAOFactory {

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Bean
	public WatchdogDaoImpl getWatchDogDAO() {
		if (watchdogConfiguration.getDbType().trim().equalsIgnoreCase("ORACLE")) {
			return new WatchdogOracleDaoImpl();
		} else {
			return new WatchdogSQLDaoImpl();
		}
	}
}
