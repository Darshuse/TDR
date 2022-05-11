package com.eastnets.enReportingLoader;

import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.cluster.ClusterManager;
import com.eastnets.domain.admin.User;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;

/**
 * @author MKassab
 * 
 */
public class ServiceLauncher /* extends BaseApp */ {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 5275049801457064994L;

	private static final String DBConnectorID = "22";
	private static final String MQConnectorID = "24";
	private static final Logger LOGGER = Logger.getLogger(ServiceLauncher.class);
	private XMLDataSouceHelper xmlDataSouceHelper;
	private List<Runnable> serviceList;

	private AppConfigBean appConfigBean = null;

	private ServiceLocator serviceLocator;

	private ClusterManager clusterManager;

	public void init() {
		try {
			LOGGER.info("Initializing Launcher");
			// super.init(appConfigBean);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void launch() {
		DataSourceParser dataSourceParser = null;
		try {
			dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
		if (xmlDataSouceHelper.isDbIntegrationEnabled()) {
			checkDBConnectorLic();
		} else if (xmlDataSouceHelper.isIbmMQIntegrationEnabled() || xmlDataSouceHelper.isApacheMqIntegrationEnabled()) {
			checkMQConnectorLic();
		}

		try {
			clusterManager.startCluster();
		} catch (Exception e) {
			LOGGER.info("Failed to start cluster");
			/*
			 * Rethrow to prevent running other services in-case cluster is enabled and cannot be started
			 */
			System.exit(1);
			return;
		}
		Thread t;
		for (Runnable r : serviceList) {
			t = new Thread(r);
			t.start();
		}
	}

	private void checkDBConnectorLic() {
		if (!serviceLocator.getLicenseService().checkLicense()) {
			LOGGER.error("DBConnector is not licensed....");
			System.exit(1);
			return;
		}
		if (!serviceLocator.getLicenseService().checkProduct(DBConnectorID)) {
			LOGGER.error("DBConnector Product is not licensed....");
			System.exit(1);
			return;
		}

		LOGGER.info("DBConnector license is checked successfuly...");
		User user = serviceLocator.getAdminService().getValidUser(appConfigBean.getUsername());
		if (user == null) {
			LOGGER.error("Invalid user ....");
			System.exit(1);
			return;
		}
		List<String> roles = serviceLocator.getSecurityService().getProfileRoles(user.getProfile().getGroupId());

		if (!roles.contains("SIDE_CONNECTOR")) {
			LOGGER.error("The User is not authorized to run DB Connector  ....");
			System.exit(1);
			return;

		}
		LOGGER.info("DBConnector Roles is checked successfuly...");
		LOGGER.info("Starting all services.......");

	}

	private void checkMQConnectorLic() {
		/*
		 * if (!serviceLocator.getLicenseService().checkLicense()) { LOGGER.error("MQConnector is not licensed...."); System.exit(1); return; } if
		 * (!serviceLocator.getLicenseService().checkProduct(MQConnectorID)) { LOGGER.error("MQConnector Product is not licensed...."); System.exit(1); return; }
		 * 
		 * LOGGER.info("MQConnector license is checked successfuly..."); User user = serviceLocator.getAdminService().getValidUser(appConfigBean.getUsername()); if (user == null) {
		 * LOGGER.error("Invalid user ...."); System.exit(1); return; } List<String> roles = serviceLocator.getSecurityService().getProfileRoles(user.getProfile().getGroupId());
		 * 
		 * if (!roles.contains("SIDE_CONNECTOR")) { LOGGER.error("The User is not authorized to run Connector  ...."); System.exit(1); return;
		 * 
		 * } LOGGER.info("MQConnector Roles is checked successfuly..."); LOGGER.info("Starting all services.......");
		 */

	}

	public static boolean isDBReader() {
		if (new ServiceLauncher().xmlDataSouceHelper == null) {
			return false;
		}
		if (new ServiceLauncher().xmlDataSouceHelper.isDbIntegrationEnabled()) {
			return true;
		}
		return false;
	}

	public void destroy() {
		clusterManager.stopClusterIfConnected();
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public List<Runnable> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Runnable> serviceList) {
		this.serviceList = serviceList;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	public void setClusterManager(ClusterManager clusterManager) {
		this.clusterManager = clusterManager;
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}
}