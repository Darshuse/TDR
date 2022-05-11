
package com.eastnets.textbreak.utility;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.eastnets.textbreak.bean.TextBreakConfig;

public class TextBreakHelper {

	public TextBreakHelper() {
	}

	private static Properties createPropertiesConfig(TextBreakConfig configBean) {
		Properties properties = new Properties();
		properties.setProperty("serverName", configBean.getServerName());
		properties.setProperty("databaseName", configBean.getDatabaseName());
		properties.setProperty("dbType", configBean.getDbType());
		properties.setProperty("schemaName", configBean.getSchemaName());
		properties.setProperty("portNumber", configBean.getPortNumber());
		properties.setProperty("schemaName", configBean.getSchemaName());
		properties.setProperty("tableSpace", configBean.getTableSpace());
		properties.setProperty("username", configBean.getUsername());
		properties.setProperty("password", configBean.getPassword());
		properties.setProperty("Partitioned", configBean.getPartitioned().toString());
		properties.setProperty("instanceName", configBean.getInstanceName());
		properties.setProperty("DBServiceName", configBean.getDbServiceName());
		properties.setProperty("aid", configBean.getAid());
		properties.setProperty("apacheMqServer", configBean.getApacheMqServerIp());
		properties.setProperty("apacheMqPort", configBean.getApacheMqServerPort());
		properties.setProperty("textBreakReader", configBean.getTextBreakReaderBeanName());
		properties.setProperty("jdbcDriver", configBean.getJdbcDriver());
		properties.setProperty("databasePlatform", configBean.getDatabasePlatform());
		properties.setProperty("databaseUrl", configBean.getDatabaseUrl());
		properties.setProperty("messageNumber", configBean.getMessageNumber());
		properties.setProperty("fromDate", configBean.getFromDate());
		properties.setProperty("toDate", configBean.getToDate());
		properties.setProperty("allMessages", configBean.getAllMessages());
		properties.setProperty("dayNumber", configBean.getDayNumber());

		return properties;

	}

	public static void prepareForPlaceholderConfigurer(TextBreakConfig textBreakConfig) {
		Properties prop = null;
		OutputStream output = null;
		prop = createPropertiesConfig(textBreakConfig);
		try {
			output = new FileOutputStream("textBreakConfig.properties");
			prop.store(output, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
