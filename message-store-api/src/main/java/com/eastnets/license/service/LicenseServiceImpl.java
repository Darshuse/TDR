package com.eastnets.license.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.license.ModuleValidationInfo;
import com.eastnets.license.dao.LicenseDAOImpl;

@Service
public class LicenseServiceImpl {

	@Autowired
	private LicenseDAOImpl licenseDAOImpl;

	public boolean checkProduct() throws Exception {
		return isLicensedProd();
	}

	private boolean isLicensedProd() throws Exception {
		ModuleValidationInfo moduleInfo = licenseDAOImpl.checkProduct();

		java.util.Date expirationDate = moduleInfo.getExpirationDate();
		Integer licensed = moduleInfo.getLicensed();

		return licensed != null && licensed.toString().equals("1")
				&& (expirationDate == null || expirationDate.after(new java.util.Date()));

	}

}
