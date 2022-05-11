package com.eastnets.service.loader.monitoring;

import com.eastnets.dao.loader.monitoring.LoaderMonitoringDao;
import com.eastnets.service.ServiceBaseImp;

public class LoaderMonitoringServiceImpl extends ServiceBaseImp implements LoaderMonitoringService {

	private LoaderMonitoringDao loaderMonitoringDao;
	private static final long serialVersionUID = -6344216711482002240L;

	@Override
	public Long getStatisticsCount() {

		return loaderMonitoringDao.getStatisticsCount();
	}

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg2) {
		loaderMonitoringDao.insertIntoErrorld(errorExe, errorLevel, errorModule, errMsg2);
	}

	public LoaderMonitoringDao getLoaderNotificationDao() {
		return loaderMonitoringDao;
	}

	public void setLoaderMonitoringDao(LoaderMonitoringDao loaderNotificationDao) {
		this.loaderMonitoringDao = loaderNotificationDao;
	}
}
