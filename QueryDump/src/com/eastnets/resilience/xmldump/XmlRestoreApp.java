package com.eastnets.resilience.xmldump;

import java.util.ArrayList;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.service.license.LicenseService;

class XmlRestoreApp extends BaseApp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9087824989714903073L;
	XmlRestoreApp(ConfigBean configBean) throws PortNumberRangeException{
		init(configBean);//this will initialise everything
		
	}
	public boolean isLicensed() throws Exception {
	    /*
		boolean validLicense = getServiceLocater().getLicenseService().checkLicense();
		ArrayList<String> licensedProducts =  null;
		if (validLicense) {
			licensedProducts = getServiceLocater().getLicenseService().getLicenseProducts();
		}

		if (licensedProducts!=null ) {

			return licensedProducts.contains( XmlRestoreConstants.PRODUCT_ID );


		}
		return false;
		*/
	    LicenseService licSrvc = getServiceLocater().getLicenseService();
	    return licSrvc.checkLicense() &&
	            (
	                    licSrvc.checkProduct(XmlRestoreConstants.PRODUCT_ID)
	                    ||
	                    getServiceLocater().getCommonService().getDbVersion() == 2.6
	            );
	}
}
