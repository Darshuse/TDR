package com.eastnets.dao.loader.monitoring;

public interface LoaderMonitoringDao {

	public Long getStatisticsCount();

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg2);

}
