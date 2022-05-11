package com.eastnets.dao.loader.monitoring;

import com.eastnets.dao.DAOBaseImp;

public class LoaderMonitoringOracleDaoImp extends DAOBaseImp implements LoaderMonitoringDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long getStatisticsCount() {

		Long statisticsCount = null;

		String queryString = ("SELECT COUNT(new_msg_count) FROM (SELECT * FROM ldUpdateStatistics WHERE rownum <= 5 order by ldupdatestatistics.insert_time desc)\r\n"
				+ "WHERE new_msg_count <10 AND (refresh_count = 0 or refresh_count is null) AND jrnl_msg_count = '0' AND overrun = '1'");

		statisticsCount = jdbcTemplate.queryForLong(queryString);

		return statisticsCount;

	}

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg2) {
		Long mesgID = jdbcTemplate.queryForObject("select LDERRORS_ID.NEXTVAL from dual", Long.class);
		String insertQuery = "INSERT INTO ldErrors(ErrID,ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, new Object[] { mesgID, errorExe, new java.util.Date(), errorLevel, errorModule, "", errMsg2 });
	}

}
