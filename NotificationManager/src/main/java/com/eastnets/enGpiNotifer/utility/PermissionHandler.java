package com.eastnets.enGpiNotifer.utility;

import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.service.ServiceLocator;

public class PermissionHandler {
	private static final String NOTIFACTION_MANGER_ID = "27";
	private ServiceLocator serviceLocator;
	private static final Logger LOGGER = Logger.getLogger(PermissionHandler.class);

	public boolean checkNotifationMangerLic(boolean enableUC) {
		if (enableUC) {
			LOGGER.info("Notification manager Work on Universal Confirmation mode");
			return true;
		}
		if (!serviceLocator.getLicenseService().checkLicense()) {
			serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "License", "NO LICENCSE", null);
			LOGGER.info("Notification manager is not licensed.");
			System.exit(1);
			return false;
		}
		if (!serviceLocator.getLicenseService().checkProduct(NOTIFACTION_MANGER_ID)) {
			serviceLocator.getViewerService().insertIntoErrorld("GPINotifier", "Failed", "License", "NO LICENCSE", null);
			LOGGER.info("Notification manager is not licensed.");
			System.exit(1);
			return false;
		}
		LOGGER.info("Notification manager license is checked successfuly...");
		return true;
	}

	public boolean checkNotifationMangerUserRoals(String userName) {
		User user = serviceLocator.getAdminService().getValidUser(userName);
		if (user == null) {
			LOGGER.error("Invalid user ....");
			System.exit(1);
			return false;
		}
		List<String> roles = serviceLocator.getSecurityService().getProfileRoles(user.getProfile().getGroupId());

		if (!roles.contains("SIDE_GPINPTIFACTION")) {
			LOGGER.error("The User is not authorized to run Notification manager  ....");
			System.exit(1);
			return false;

		}
		LOGGER.info("Notification manager Roles is checked successfuly...");
		return true;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

}
