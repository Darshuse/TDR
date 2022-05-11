package com.eastnets.service.warning.notification;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eastnets.service.common.MailService;
import com.eastnets.service.loader.monitoring.LoaderMonitoringService;

public class LoaderNotWorkingNotifer implements WarningNotifer {

	private static final String WARNING = "WARNING";

	private static final String LOADER = "LOADER";

	private static final Logger LOGGER = Logger.getLogger(LoaderNotWorkingNotifer.class);

	private LoaderMonitoringService loaderMonitoringService;
	private MailService mailService;
	private String LoaderNotificationEmailTO;
	private String LoaderNotificationSubject;
	private String LoaderNotificationContent;

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public void observeLoader() {
		if (StringUtils.isNotBlank(LoaderNotificationEmailTO)) {
			long count = loaderMonitoringService.getStatisticsCount();
			if (count == 5) {
				String warningContnet = "Issue in Loader performance email will be sent to " + LoaderNotificationEmailTO + " with content : " + LoaderNotificationContent;
				LOGGER.warn(warningContnet);
				loaderMonitoringService.insertIntoErrorld(LOADER, WARNING, LOADER, warningContnet);
				notifyUser();
			}
		}
	}

	@Override
	public void notifyUser() {
		mailService.sendMail(LoaderNotificationSubject, LoaderNotificationEmailTO, LoaderNotificationContent, null, false);
	}

	public String getLoaderNotificationEmailTO() {
		return LoaderNotificationEmailTO;
	}

	public void setLoaderNotificationEmailTO(String loaderNotificationEmailTO) {
		LoaderNotificationEmailTO = loaderNotificationEmailTO;
	}

	public String getLoaderNotificationSubject() {
		return LoaderNotificationSubject;
	}

	public void setLoaderNotificationSubject(String loaderNotificationSubject) {
		LoaderNotificationSubject = loaderNotificationSubject;
	}

	public String getLoaderNotificationContent() {
		return LoaderNotificationContent;
	}

	public void setLoaderNotificationContent(String loaderNotificationContent) {
		LoaderNotificationContent = loaderNotificationContent;
	}

	public LoaderMonitoringService getLoaderMonitoringService() {
		return loaderMonitoringService;
	}

	public void setLoaderMonitoringService(LoaderMonitoringService loaderMonitoringService) {
		this.loaderMonitoringService = loaderMonitoringService;
	}
}
