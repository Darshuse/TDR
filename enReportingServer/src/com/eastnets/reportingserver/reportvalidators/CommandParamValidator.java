package com.eastnets.reportingserver.reportvalidators;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import com.eastnets.config.ConfigBean;
import com.eastnets.config.ConfigBean.DestenationTypes;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;

public class CommandParamValidator {
	static Logger log = Logger.getLogger(CommandParamValidator.class.getName());

	public boolean checkFileName(AppConfigBean cnfgBean) {
		log.debug("getting ready to check File Name  Validation ... ");
		if (cnfgBean.getOutpuReportName() != null && cnfgBean.getOutpuReportName().length() > 0)
			if (cnfgBean.getOutpuReportName().matches(".*[\\?\\\\/:\"*<>|].*"))
				return true;
			else
				return false;

		return true;
	}

	public String checkEmailMandatoryParams(AppConfigBean cnfgBean) {
		log.debug("getting ready to check Email Mandatory params ... ");
		if (cnfgBean.getDestenationTypes().name().equals(ConfigBean.DestenationTypes.email.name())) {

			if (cnfgBean.getMailHost() == null || cnfgBean.getMailHost().length() <= 0)
				return "-smtp";
			if (cnfgBean.getMailFrom() == null || cnfgBean.getMailFrom().length() <= 0)
				return "-from";
			if (cnfgBean.getMailTo() == null || cnfgBean.getMailTo().length() <= 0)
				return "-to";
			// if (cnfgBean.getMailUsername() == null
			// || cnfgBean.getMailUsername().length() <= 0)
			// return "-MU";
			// if (cnfgBean.getMailPassword() == null
			// || cnfgBean.getMailPassword().length() <= 0)
			// return "-MP";
		}

		return "VALID";
	}

	public String checkDBConnection(AppConfigBean cnfgBean) {
		log.debug("checking DB params...");
		if (cnfgBean.getTnsEnabled() && cnfgBean.getTnsPath() != null && !cnfgBean.getTnsPath().isEmpty() && cnfgBean.getServerName() != null && !cnfgBean.getServerName().isEmpty()) {
			return "VALID";
		}

		if (cnfgBean.getServerName() == null || cnfgBean.getServerName().length() <= 0)
			return "-IP";
		if (cnfgBean.getUsername() == null || cnfgBean.getUsername().length() <= 0)
			return "-U";
		if (cnfgBean.getPassword() == null || cnfgBean.getPassword().length() <= 0)
			return "-P";
		if (cnfgBean.getPortNumber() == null || cnfgBean.getPortNumber().length() <= 0)
			return "-port";
		if (cnfgBean.getDatabaseName() == null || cnfgBean.getDatabaseName().length() <= 0) {
			if (cnfgBean.getDbServiceName() == null || cnfgBean.getDbServiceName().length() == 0) {
				return "-dbname or -servicename";
			}
		}
		log.debug("DB params are vaild..");

		return "VALID";
	}

	public String checkReportMandatoryParam(AppConfigBean cnfgBean) {
		log.debug("getting ready to check Mandatory paramaters...");
		if (cnfgBean.getReportsDirectoryPath() == null || cnfgBean.getReportsDirectoryPath().length() <= 0)
			return "-rtemplatepath";

		if (cnfgBean.getOutpuReportPath() == null || cnfgBean.getOutpuReportPath().length() <= 0)
			return "-rpath";

		if (cnfgBean.getReportId() == null || cnfgBean.getReportId().length() <= 0)
			return "-r";

		return "VALID";
	}

	public boolean checkDestinationValue(String paramValue) {

		if (paramValue.equals("PRINT") || paramValue.equals("FILE") || paramValue.equals("EMAIL") || paramValue.equals("CUSTOM")) {
			return true;
		}

		return false;
	}

	public boolean isValidEmail(String emailAddress) {

		try {
			new InternetAddress(emailAddress).validate();
		} catch (AddressException ex) {
			return false;
		}

		return true;
	}

	public String checkAdditionalParam(AppConfigBean cnfgBean) {
		log.debug("getting ready to check Email params and other Additional params...");
		if (cnfgBean.getDestenationTypes().equals(DestenationTypes.printer)) {

			if (cnfgBean.getMailHost() != null && cnfgBean.getMailHost().length() > 0)
				return "-smtp should be used with EMAIL mode only";
			if (cnfgBean.getMailPort() != null && cnfgBean.getMailPort().length() > 0 && !cnfgBean.getMailPort().equals("25"))
				return "-Mport should be used with EMAIL mode only";
			if (cnfgBean.getMailUsername() != null && cnfgBean.getMailUsername().length() > 0)
				return "-MU should be used with EMAIL mode only";
			if (cnfgBean.getMailPassword() != null && cnfgBean.getMailPassword().length() > 0)
				return "-MP should be used with EMAIL mode only";
			if (cnfgBean.getMailFrom() != null && cnfgBean.getMailFrom().length() > 0 && !cnfgBean.getMailFrom().equals("enReporting3"))
				return "-from should be used with EMAIL mode only";
			if (cnfgBean.getMailTo() != null && cnfgBean.getMailTo().length() > 0)
				return "-to should be used with EMAIL mode only";
			if (cnfgBean.getMailCc() != null && cnfgBean.getMailCc().length() > 0)
				return "-cc should be used with EMAIL mode only";
			if (cnfgBean.getMailSubject() != null && cnfgBean.getMailSubject().length() > 0)
				return "-subject should be used with EMAIL mode only";
			if (cnfgBean.getMailBody() != null && cnfgBean.getMailBody().length() > 0)
				return "-body should be used with EMAIL mode only";

		} else if (cnfgBean.getDestenationTypes().equals(DestenationTypes.email)) {

			if (cnfgBean.getPrinterName() != null && cnfgBean.getPrinterName().length() > 0)
				return "-p should be used with PRINT mode only";
			if (cnfgBean.getCopiesCount() > 0 && cnfgBean.getCopiesCount() != 1)
				return "-c should be used with PRINT mode only";

		} else if (cnfgBean.getDestenationTypes().equals(DestenationTypes.file)) {

			if (cnfgBean.getMailHost() != null && cnfgBean.getMailHost().length() > 0)
				return "-smtp should be used with EMAIL mode only";
			if (cnfgBean.getMailPort() != null && cnfgBean.getMailPort().length() > 0 && !cnfgBean.getMailPort().equals("25"))
				return "-Mport should be used with EMAIL mode only";
			if (cnfgBean.getMailUsername() != null && cnfgBean.getMailUsername().length() > 0)
				return "-MU should be used with EMAIL mode only";
			if (cnfgBean.getMailPassword() != null && cnfgBean.getMailPassword().length() > 0)
				return "-MP should be used with EMAIL mode only";
			if (cnfgBean.getMailFrom() != null && cnfgBean.getMailFrom().length() > 0 && !cnfgBean.getMailFrom().equals("enReporting3"))
				return "-from should be used with EMAIL mode only";
			if (cnfgBean.getMailTo() != null && cnfgBean.getMailTo().length() > 0)
				return "-to should be used with EMAIL mode only";
			if (cnfgBean.getMailCc() != null && cnfgBean.getMailCc().length() > 0)
				return "-cc should be used with EMAIL mode only";
			if (cnfgBean.getMailSubject() != null && cnfgBean.getMailSubject().length() > 0)
				return "-subject should be used with EMAIL mode only";
			if (cnfgBean.getMailBody() != null && cnfgBean.getMailBody().length() > 0)
				return "-body should be used with EMAIL mode only";

			if (cnfgBean.getPrinterName() != null && cnfgBean.getPrinterName().length() > 0)
				return "-p should be used with PRINT mode only";
			if (cnfgBean.getCopiesCount() > 0 && cnfgBean.getCopiesCount() != 1)
				return "-c should be used with PRINT mode only";
		}

		return "VALID";
	}

}