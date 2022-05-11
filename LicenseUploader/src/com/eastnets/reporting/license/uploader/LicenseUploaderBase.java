package com.eastnets.reporting.license.uploader;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.service.ServiceLocator;


public class LicenseUploaderBase extends BaseApp {

	private static final long serialVersionUID = 5013204289134780137L;

	private static LicenseUploaderBase  licenseUploaderBaseApp;
	
	private LicenseUploaderBase() {
	}

	static{
		licenseUploaderBaseApp = new LicenseUploaderBase();
	}

	public static void init() throws PortNumberRangeException {
		licenseUploaderBaseApp.init(licenseUploaderBaseApp.getConfigBean());
	}
	
	public static ConfigBean getAppConfigBean() {
		return licenseUploaderBaseApp.getConfigBean();
	}
	
	public static ServiceLocator getAppServiceLocator() {
		return licenseUploaderBaseApp.getServiceLocater();
	}
}
