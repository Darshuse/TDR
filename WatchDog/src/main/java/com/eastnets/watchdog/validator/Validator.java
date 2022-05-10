package com.eastnets.watchdog.validator;

import com.eastnets.watchdog.config.DBConfiguration;
import com.eastnets.watchdog.config.MQConfiguration;
import com.eastnets.watchdog.config.MailConfiguration;
import com.eastnets.watchdog.config.WatchdogConfiguration;

public class Validator {

	public boolean validateParams(WatchdogConfiguration configBean) {

		return false;
	}

	private boolean checkMailMandatoryFields(MailConfiguration mailConfigBean) {

		if (mailConfigBean == null) {
			System.out.println("Mail configurations must be provided");
			return false;
		} else {
			if (isNullOrEmpty(mailConfigBean.getMailServer())) {
				System.out.println("Mail server name must be provided");
				return false;
			}
			if (isNullOrEmpty(mailConfigBean.getMailFrom())) {
				System.out.println("Mail Address to send from must be provided");
				return false;
			}
			if (isNullOrEmpty(mailConfigBean.getPassword())) {
				System.out.println("Mail password must be provided");
				return false;
			}
			if (isNullOrEmpty(mailConfigBean.getMailTo())) {
				System.out.println("Mail to send to must be provided");
				return false;
			}
		}

		return true;
	}

	private boolean checkMQMandatoryFields(MQConfiguration mqConfigBean) {

		if (mqConfigBean == null) {
			System.out.println("MQ Configurations must be provided");
			return false;
		} else {
			if (isNullOrEmpty(mqConfigBean.getServerName())) {
				System.out.println("MQ Server name must be provided");
				return false;
			}
			if (isNullOrEmpty(mqConfigBean.getPort().toString())) {
				System.out.println("MQ Port number must be provided");
				return false;
			}
		}

		return true;
	}

	private boolean checkDBMandatoryFields(DBConfiguration dbConfigBean) {
		if (dbConfigBean == null) {
			System.out.println("DB Configurations must be provided");
			return false;
		} else {

			if (isNullOrEmpty(dbConfigBean.getServerName())) {
				System.out.println("DB Server name must be provided");
				return false;
			}
			if (isNullOrEmpty(dbConfigBean.getUsername())) {
				System.out.println("DB Username must be provided");
				return false;
			}

			if (isNullOrEmpty(dbConfigBean.getPassword())) {
				System.out.println("DB Password must be provided");
				return false;
			}

			if (isNullOrEmpty(dbConfigBean.getDbType())) {
				System.out.println("DB Type must be provided");
				return false;
			}

			if (dbConfigBean.getDbType().equalsIgnoreCase("Oracle")) {
				if (isNullOrEmpty(dbConfigBean.getServiceName())) {
					System.out.println("Oracle Service name must be provided");
					return false;
				}
			}

			if (dbConfigBean.getDbType().equalsIgnoreCase("MSSQL")) {
				if (isNullOrEmpty(dbConfigBean.getInstanceName()) || isNullOrEmpty(dbConfigBean.getDbName())) {
					System.out.println("MSSQL instance name or database name must be provided");
					return false;
				}
			}

		}

		return true;
	}

	public boolean checkLicense() {
		return false;
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.isEmpty())
			return true;
		return false;
	}
}
