package com.eastnets.service.loader.helper;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.eastnets.config.DBType;
import com.eastnets.enReportingLoader.config.AppConfigBean;

public class JPAUtil {

	private static Map<String, String> map;

	private static Map<String, String> getConnectionParamterMap(AppConfigBean appConfigBean) {
		if (map == null) {
			map = new HashMap<String, String>();
			map.put(PersistenceUnitProperties.JDBC_URL, getDatabaseURL(appConfigBean));
			map.put(PersistenceUnitProperties.JDBC_USER, appConfigBean.getUsername());
			map.put(PersistenceUnitProperties.JDBC_PASSWORD, appConfigBean.getPassword());
			map.put(PersistenceUnitProperties.BATCH_WRITING, "JDBC");
			map.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, "1000");
			map.put(PersistenceUnitProperties.VALIDATION_MODE, "NONE");
			
			//map.put(PersistenceUnitProperties.BEAN_VALIDATION_NO_OPTIMISATION, "TRUE");
			if(appConfigBean.getDatabaseType().equals(DBType.ORACLE)){
				map.put(PersistenceUnitProperties.JDBC_DRIVER, "oracle.jdbc.OracleDriver");
			}else{
				map.put(PersistenceUnitProperties.JDBC_DRIVER, "net.sourceforge.jtds.jdbc.Driver");
			} 
		}
		return map;
	}

	public static String getDatabaseURL(AppConfigBean appConfigBean) {
		StringBuffer dbUrl = null;
		
		if(appConfigBean.getDatabaseType().equals(DBType.ORACLE)){
			dbUrl= new StringBuffer("jdbc:oracle:thin:@");
		}else{
			dbUrl= new StringBuffer("jdbc:jtds:sqlserver://"); 
		}
		

		if (appConfigBean.getDatabaseType() == DBType.ORACLE) {
			dbUrl.append(appConfigBean.getServerName()).append(':').append(appConfigBean.getPortNumber());
			if (appConfigBean.getDbServiceName() != null && !appConfigBean.getDbServiceName().trim().isEmpty()) {
				dbUrl.append('/').append(appConfigBean.getDbServiceName());
			} else {
				dbUrl.append(':').append(appConfigBean.getDatabaseName());
			}

		}else if (appConfigBean.getDatabaseType() == DBType.MSSQL){
			dbUrl.append(appConfigBean.getServerName()).append(':').append(appConfigBean.getPortNumber());
			dbUrl.append(";databaseName=").append(appConfigBean.getDatabaseName());
			dbUrl.append(";instance=").append(appConfigBean.getInstanceName());
		}
		System.out.println(dbUrl.toString());
		return dbUrl.toString();
	}

	public static void setMap(Map<String, String> map) {
		JPAUtil.map = map;
	}

	public static EntityManagerFactory createEntityManagerFactory(AppConfigBean appConfigBean) {

		EntityManagerFactory entityManagerFactory = null;
		try {
			if(appConfigBean.getDatabaseType().equals(DBType.ORACLE)){
				entityManagerFactory = Persistence.createEntityManagerFactory("jpaPersitant", getConnectionParamterMap(appConfigBean));
			 

			}else{ 
				entityManagerFactory = Persistence.createEntityManagerFactory("jpaPersitantSQL", getConnectionParamterMap(appConfigBean));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return entityManagerFactory;
	}

}
