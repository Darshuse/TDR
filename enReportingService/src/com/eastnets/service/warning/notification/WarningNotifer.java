package com.eastnets.service.warning.notification;

public interface WarningNotifer {

	/**
	 * This method is to notify interested end users that there exists an error in some component or it does not work for some reason
	 */
	public void notifyUser();

	public void observeLoader();

}
