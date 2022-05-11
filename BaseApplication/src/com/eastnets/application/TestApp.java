package com.eastnets.application;

import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;

public class TestApp extends BaseApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2843994143876228552L;

	/**
	 * @param args
	 */

	public static void main(String[] args) throws Exception {
		ConfigBean configBean = new ConfigBean();
		configBean.setDatabaseName("ORCL");
		configBean.setServerName("192.168.50.74");
		configBean.setPortNumber("1525");
		configBean.setUsername("generic");
		try {
			configBean.setPasswordText("cireneg");
		} catch (Exception e) {
			configBean.setPassword("Fw92juWzvj4XVxDrNLXQCQ==");
		}
		configBean.setSchemaName("side");
		configBean.setTableSpace("sidedb");
		configBean.setDatabaseType(DBType.ORACLE);

		TestApp app = new TestApp();
		app.init(configBean);

		System.out.println("approval: "
				+ app.getServiceLocater().getSecurityService()
						.getUserApprovalDescription(configBean.getUsername()));
		System.out.println("DB: "
				+ app.getServiceLocater().getCommonService().getDataBaseInfo());
		System.out.println("Currencies: "
				+ app.getServiceLocater().getCommonService().getCurrencies());

		/*
		 * MessageDetails messageDetails =
		 * app.getServiceLocater().getViewerService
		 * ().getMessageDetails(configBean.getUsername() , 2, 787240838,
		 * -770449, 0);
		 */

		/*
		 * System.out.println("Message Expand: \r\n----------------\r\n" +
		 * app.getServiceLocater
		 * ().getViewerService().getMessageExpandedText(configBean.getUsername()
		 * , messageDetails));
		 */

		System.out.println(app.getServiceLocater().getLicenseService()
				.checkLicense());
		System.out.println(app.getServiceLocater().getLicenseService()
				.checkProduct("10"));

	}

}
