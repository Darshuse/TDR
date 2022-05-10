package com.eastnets.enGpiNotifer.source;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiNotifer.DAO.LoaderDAO;
import com.eastnets.enGpiNotifer.Notifaction.Notifaction;
import com.eastnets.enGpiNotifer.builder.GpiQueryBulder;
import com.eastnets.enGpiNotifer.config.AppConfigBean;
import com.eastnets.enGpiNotifer.service.GpiService;
import com.eastnets.enGpiNotifer.utility.DataSourceParser;
import com.eastnets.enGpiNotifer.utility.GpiDirectory;
import com.eastnets.enGpiNotifer.utility.PermissionHandler;
import com.eastnets.service.ServiceLocator;

public class SaaSource implements MessageSource {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5275049801457064994L;
	private AppConfigBean appConfigBean;
	private ServiceLocator serviceLocator;
	private LoaderDAO loaderDAO;
	private Map<String, GpiDirectory> gpiDirectoryMap;
	private PermissionHandler permissionHandler;

	String query = "";
	private Notifaction notifaction;
	private static final Logger LOGGER = Logger.getLogger(SaaSource.class);

	public void init() {
		try {
			// gpiDirectoryMap=loaderDAO.getGpiDirectory();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startSourceMonotoring(DataSourceParser dataSourceParser) throws Exception {
		boolean enabelUC = dataSourceParser.getxMLNotifierConfigHelper().isEnabelUC();
		permissionHandler.checkNotifationMangerLic(enabelUC);
		permissionHandler.checkNotifationMangerUserRoals(dataSourceParser.getXmlDataSource().getUsername());
		LOGGER.info("Start Notification Manager Job ....");
		query = GpiQueryBulder.buildQuery(dataSourceParser, appConfigBean.getEnableDebugMode(), enabelUC);
		LOGGER.debug("End build Query ... ");
		LOGGER.debug("Start getting Pending Message From DB ... ");

		List<NotifierMessage> gpiMessegesPendenig = null;
		List<NotifierMessage> resultMessages = null;
		while (true) {
			try {
				gpiMessegesPendenig = serviceLocator.getViewerService().getPendingGpiMesg(query, dataSourceParser.getxMLNotifierConfigHelper().getMailAttempts(), dataSourceParser.getxMLNotifierConfigHelper().getConfirmAttempts(),
						dataSourceParser.getxMLNotifierConfigHelper().getTimeInterval());
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while getPendingGpiMesg() SQL Exception ", null);
				e.printStackTrace();
			}
			try {
				resultMessages = GpiService.compareGpiMessages(gpiMessegesPendenig, gpiDirectoryMap, dataSourceParser.getxMLNotifierConfigHelper().getDurationTime());
			} catch (Exception e) {
				serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "GPINotifier", "while compareGpiMessages()  Exception ", null);
				e.printStackTrace();
			}

			if (resultMessages != null && !resultMessages.isEmpty()) {
				LOGGER.debug("Start notification cycle for pending messages    ...");
				notifaction.notify(dataSourceParser, gpiMessegesPendenig, null);
				LOGGER.debug("End notification cycle for pending Messages    ...");

			} else {
				LOGGER.debug("No message pending in queue ...");

			}

			try {
				Long sleepPeriod = dataSourceParser.getxMLNotifierConfigHelper().getSleepPeriod();
				LOGGER.info("Notifier  will sleep for " + sleepPeriod + " Seconds");
				Thread.sleep((sleepPeriod) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public Map<String, GpiDirectory> getGpiDirectoryMap() {
		return gpiDirectoryMap;
	}

	public void setGpiDirectoryMap(Map<String, GpiDirectory> gpiDirectoryMap) {
		this.gpiDirectoryMap = gpiDirectoryMap;
	}

	public Notifaction getNotifaction() {
		return notifaction;
	}

	public void setNotifaction(Notifaction notifaction) {
		this.notifaction = notifaction;
	}

	public PermissionHandler getPermissionHandler() {
		return permissionHandler;
	}

	public void setPermissionHandler(PermissionHandler permissionHandler) {
		this.permissionHandler = permissionHandler;
	}

}
