package com.eastnets.application;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.config.PortNumberRangeException;
import com.eastnets.service.ServiceLocator;

public abstract class BaseApp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9137987382823278401L;
	protected ClassPathXmlApplicationContext ctx;
	private ServiceLocator serviceLocator;
	private ConfigBean configBean;

	static {

	}

	public BaseApp() {
		configBean = new ConfigBean();
	}

	protected void alterSession() {

		if (configBean.getDatabaseType() == DBType.ORACLE) {
			JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
			jdbcTemplate.execute("alter session set current_schema = " + getConfigBean().getSchemaName());

			// in some locale( like Hungarian ), the order by statement orders
			// the numbers after the letters, when checking license we order the
			// BICs
			// and generate the sign based on it, so we fail to verify the
			// license due to the change of this string.
			// The following line force the NLS_LANGUAGE for the current
			// database session to AMERICAN which is what we have already tested
			// and we are sure that it works fine.
			jdbcTemplate.execute("ALTER SESSION SET NLS_LANGUAGE= 'AMERICAN'");

		}

	}

	public ConfigBean getConfigBean() {
		return configBean;
	}

	protected Connection getConnection() throws Exception {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		return jdbcTemplate.getDataSource().getConnection();
	}

	public ServiceLocator getServiceLocater() {

		return serviceLocator;
	}

	public static Properties createPropertiesConfig(ConfigBean configBean) {
		Properties properties = new Properties();
		properties.setProperty("serverName", configBean.getServerName());
		properties.setProperty("tnsEnabled", configBean.getTnsEnabled().toString());
		properties.setProperty("tnsPath", configBean.getTnsPath());
		properties.setProperty("databaseName", configBean.getDatabaseName());
		properties.setProperty("dbType",configBean.getDbType());
		properties.setProperty("portNumber", configBean.getPortNumber());
		properties.setProperty("username", configBean.getUsername());
		properties.setProperty("password", configBean.getPassword());
		properties.setProperty("schemaName", configBean.getSchemaName());
		properties.setProperty("tableSpace", configBean.getTableSpace());
		properties.setProperty("mailHost", configBean.getMailHost());
		properties.setProperty("mailPort", configBean.getMailPort());
		properties.setProperty("mailUsername", configBean.getMailUsername());
		properties.setProperty("mailPassword", configBean.getMailPassword());
		properties.setProperty("mailFrom", configBean.getMailFrom());
		properties.setProperty("mailTo", configBean.getMailTo());
		properties.setProperty("mailCc", configBean.getMailCc());
		properties.setProperty("mailSubject", configBean.getMailSubject());
		properties.setProperty("mailAuthenticationRequired", configBean.getMailAuthenticationRequired().toString());
		properties.setProperty("mailBody", configBean.getMailBody());
		properties.setProperty("DBType", configBean.getDatabaseType().toString());
		properties.setProperty("AuthMethod", configBean.getAuthMethod().toString());
		properties.setProperty("LdapURL", configBean.getLdapURL());
		properties.setProperty("LDAPBase", configBean.getLdapBase());
		properties.setProperty("LDAPUserDn", configBean.getLdapUserDn());
		properties.setProperty("LDAPPassword", configBean.getLdapPassword());
		properties.setProperty("LDAP_ATTR_USERNAME", configBean.getLdapATTR_USERNAME());
		properties.setProperty("viewerProcedureDebug", configBean.getViewerProcedureDebug().toString());
		properties.setProperty("reportsDirectoryPath", configBean.getReportsDirectoryPath());
		properties.setProperty("reportMaxFetchSize", configBean.getReportMaxFetchSize().toString());
		properties.setProperty("monitoringDayHistory", configBean.getMonitoringDayHistory().toString());
		properties.setProperty("monitoringDisplayJournals", configBean.getMonitoringDisplayJournals().toString());
		properties.setProperty("monitoringDisplayWarnings", configBean.getMonitoringDisplayWarnings().toString());
		properties.setProperty("Partitioned", configBean.getPartitioned().toString());
		properties.setProperty("instanceName", configBean.getInstanceName().toString());
		properties.setProperty("DBServiceName", configBean.getDbServiceName().toString());
		properties.setProperty("reportLogoPath", configBean.getReportLogoPath());
		properties.setProperty("sAAAid", configBean.getsAAAid());
		properties.setProperty("reportLogoPath", configBean.getReportLogoPath());
		properties.setProperty("showLog", configBean.getShowLog());

		return properties;

	}

	public void init(ConfigBean configBean) throws PortNumberRangeException {

		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		configurer.setProperties(createPropertiesConfig(configBean));

		ctx = new ClassPathXmlApplicationContext();
		ctx.addBeanFactoryPostProcessor(configurer);
		ctx.setConfigLocation("classpath:/spring-config/applicationContext.xml");
		ctx.refresh();

		serviceLocator = (ServiceLocator) ctx.getBean("serviceLocator");
		this.configBean = (ConfigBean) ctx.getBean("configBean");

		this.configBean.setAuthMethod(configBean.getAuthMethod());
		this.configBean.setDatabaseName(configBean.getDatabaseName());
		this.configBean.setDatabaseType(configBean.getDatabaseType());
		this.configBean.setLdapATTR_USERNAME(configBean.getLdapATTR_USERNAME());
		this.configBean.setLdapBase(configBean.getLdapBase());
		this.configBean.setLdapPassword(configBean.getLdapPassword());
		this.configBean.setLdapURL(configBean.getLdapURL());
		this.configBean.setLdapUserDn(configBean.getLdapUserDn());
		this.configBean.setMailHost(configBean.getMailHost());
		this.configBean.setMailPort(configBean.getMailPort());
		this.configBean.setMailPassword(configBean.getMailPassword());
		this.configBean.setMailUsername(configBean.getMailUsername());
		this.configBean.setMailFrom(configBean.getMailFrom());
		this.configBean.setMailTo(configBean.getMailTo());
		this.configBean.setMailCc(configBean.getMailCc());
		this.configBean.setMailSubject(configBean.getMailSubject());
		this.configBean.setMailBody(configBean.getMailBody());
		this.configBean.setMonitoringDayHistory(configBean.getMonitoringDayHistory());
		this.configBean.setMonitoringDisplayJournals(configBean.getMonitoringDisplayJournals());
		this.configBean.setMonitoringDisplayWarnings(configBean.getMonitoringDisplayWarnings());
		this.configBean.setPassword(configBean.getPassword());
		this.configBean.setPortNumber(configBean.getPortNumber());
		this.configBean.setReportMaxFetchSize(configBean.getReportMaxFetchSize());
		this.configBean.setReportsDirectoryPath(configBean.getReportsDirectoryPath());
		this.configBean.setSchemaName(configBean.getSchemaName());
		this.configBean.setServerName(configBean.getServerName());
		this.configBean.setTnsEnabled(configBean.getTnsEnabled());
		this.configBean.setTnsPath(configBean.getTnsPath());
		this.configBean.setTableSpace(configBean.getTableSpace());
		this.configBean.setUsername(configBean.getUsername());
		this.configBean.setViewerProcedureDebug(configBean.getViewerProcedureDebug());
		this.configBean.setPartitioned(configBean.getPartitioned());
		this.configBean.setInstanceName(configBean.getInstanceName());
		this.configBean.setReportLogoPath(configBean.getReportLogoPath());

		alterSession();
	}

	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}

}
