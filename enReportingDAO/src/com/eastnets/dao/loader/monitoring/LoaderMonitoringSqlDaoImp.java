package com.eastnets.dao.loader.monitoring;

import com.eastnets.dao.DAOBaseImp;

public class LoaderMonitoringSqlDaoImp extends DAOBaseImp implements LoaderMonitoringDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long getStatisticsCount() {

		Long statisticsCount = null;

		String queryString = ("select count(new_msg_count) from ( select top 5 * from ldUpdateStatistics order by insert_time desc ) as incidents where new_msg_count <10 and (refresh_count is null or refresh_count=0) and jrnl_msg_count = '0' and overrun = '1'");

		statisticsCount = jdbcTemplate.queryForLong(queryString);

		return statisticsCount;

	}

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg2) {

		String insertQuery = "INSERT INTO ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, new Object[] { errorExe, new java.util.Date(), errorLevel, errorModule, "", errMsg2 });
	}
}
