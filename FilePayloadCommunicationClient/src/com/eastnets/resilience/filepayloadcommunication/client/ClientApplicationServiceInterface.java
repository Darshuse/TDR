package com.eastnets.resilience.filepayloadcommunication.client;

import java.util.ArrayList;

import com.eastnets.application.BaseApp;
import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;

public class ClientApplicationServiceInterface extends BaseApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3545189482941963583L;

	public ClientApplicationServiceInterface(ClientCommandLineArgumentsParser commandLineArgumentsParser) {
		ConfigBean configBean = new ConfigBean();

		configBean.setDatabaseName(commandLineArgumentsParser.getDbName());
		configBean.setServerName(commandLineArgumentsParser.getDbServer());
		// to handle db service name (oracle db type
		if (commandLineArgumentsParser.getDbName() == null)
			configBean.setDbServiceName(commandLineArgumentsParser.getDbServiceName());
		
		configBean.setPortNumber(commandLineArgumentsParser.getDbPort());
		configBean.setUsername(commandLineArgumentsParser.getDbUser());
		try {
			configBean.setPasswordText(commandLineArgumentsParser.getDbPassword());
		} catch (Exception e) {
		}

		configBean.setSchemaName(commandLineArgumentsParser.getDbType() == DatabaseType.ORACLE ? "side" : "dbo");
		configBean.setTableSpace("sidedb");
		configBean.setDatabaseType(commandLineArgumentsParser.getDbType() == DatabaseType.ORACLE ? DBType.ORACLE : DBType.MSSQL);
		configBean.setInstanceName(commandLineArgumentsParser.getInstanceName());
		

		// initialize the application
		try {
			init(configBean);
		} catch (PortNumberRangeException e) {
			e.printStackTrace();
		}
	}
	

	public boolean isLicensed(){
		boolean validLicense = getServiceLocater().getLicenseService().checkLicense();
		ArrayList<String> licensedProducts =  null;
		if (validLicense) {
			licensedProducts = getServiceLocater().getLicenseService().getLicenseProducts();
		}

		if (licensedProducts!=null ) {

			return licensedProducts.contains( "18" );


		}
		return false;
	}
}
