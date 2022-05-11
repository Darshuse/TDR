package com.eastnets.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eastnets.dao.CommonDAO;
import com.eastnets.dao.CommonOracleDAOImp;
import com.eastnets.dao.CommonSqlDAOImp;
import com.eastnets.util.Constants;

@Configuration
public class DaoFactoryConfig {

	@Autowired
	private AppConfig appConfig;

	@Bean
	public CommonDAO getCommonDAOImpl() {

		CommonDAO commonDAO = null;

		if (appConfig.getDbConfig().getDbType().equals(Constants.DATABASE_TYPE_ORACLE)) {
			commonDAO = new CommonOracleDAOImp();
		} else if (appConfig.getDbConfig().getDbType().equals(Constants.DATABASE_TYPE_MSSQL)) {
			commonDAO = new CommonSqlDAOImp();
		}

		return commonDAO;

	}

}
