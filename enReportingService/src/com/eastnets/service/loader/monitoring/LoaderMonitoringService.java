package com.eastnets.service.loader.monitoring;

public interface LoaderMonitoringService {

	public Long getStatisticsCount();

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg2);

}
