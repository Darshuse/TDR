package com.eastnets.bicloader.loadervalidators;

import java.io.File;

import org.apache.log4j.Logger;

import com.eastnets.bicloader.app.BICLoaderConfig;
import com.eastnets.dao.bicloader.BicFile;

public class CommandParamValidator {

	Logger log;

	public CommandParamValidator() {
		log = Logger.getLogger("BICLoader");
	}

	public String checkFileExit(BICLoaderConfig cnfgBean) {
		if (cnfgBean.isAutoFiles()) {
			if (cnfgBean.getDirectory() == null || cnfgBean.getDirectory().length() <= 0) {
				return "-path";
			}
			if (!(new File(cnfgBean.getDirectory())).exists()) {
				return "-path <Target path does not exist>";
			}
			File[] listOfFiles = (new File(cnfgBean.getDirectory())).listFiles();
			if (listOfFiles.length <= 0) {
				return "-path <Target path is empty>";
			}

			for (int i = 0; i < listOfFiles.length; i++) {
				if (cnfgBean.isBankFile() && (listOfFiles[i].getName().contains("FI")
						|| listOfFiles[i].getName().contains("CT") || listOfFiles[i].getName().contains("CU"))) {
					cnfgBean.getFiles().add(listOfFiles[i].getName());
				} else if (cnfgBean.isBicdue() && (listOfFiles[i].getName().contains("COUNTRY_CODE")
						|| listOfFiles[i].getName().contains("CURRENCY_CODE")
						|| cnfgBean.isDeltaFile() && listOfFiles[i].getName().contains("DELTA")
								&& listOfFiles[i].getName().contains("BIC")
						|| cnfgBean.isFullFile() && listOfFiles[i].getName().contains("FULL")
								&& listOfFiles[i].getName().contains("BIC"))) {
					cnfgBean.getFiles().add(listOfFiles[i].getName());
				} else if (cnfgBean.isBankdirectory() && (listOfFiles[i].getName().contains("COUNTRY_CODE")
						|| listOfFiles[i].getName().contains("CURRENCY_CODE")
						|| cnfgBean.isDeltaFile() && listOfFiles[i].getName().contains("DELTA")
								&& (listOfFiles[i].getName().contains("BANKDIRECTORYPLUS")
										|| listOfFiles[i].getName().contains("BICPLUS"))
						|| cnfgBean.isFullFile() && listOfFiles[i].getName().contains("FULL")
								&& (listOfFiles[i].getName().contains("BANKDIRECTORYPLUS")
										|| listOfFiles[i].getName().contains("BICPLUS")))) {
					cnfgBean.getFiles().add(listOfFiles[i].getName());
				} else if (cnfgBean.isGpiCorr() && listOfFiles[i].getName().contains("GPI")
						&& (cnfgBean.isDeltaFile() && listOfFiles[i].getName().contains("DELTA")
								|| cnfgBean.isFullFile() && listOfFiles[i].getName().contains("FULL"))) {
					cnfgBean.getFiles().add(listOfFiles[i].getName());
				}
			}

			if (cnfgBean.getFiles().isEmpty()) {
				return "-path <Target path does not have taregt files>";
			}
		} else

		{
			if ((cnfgBean.getFile() == null || cnfgBean.getFile().length() <= 0)
					&& (cnfgBean.getDirectory() == null || cnfgBean.getDirectory().length() <= 0)) {
				return "-P and -f";
			}
			if (!(new File((new StringBuilder(String.valueOf(cnfgBean.getDirectory()))).append("/")
					.append(cnfgBean.getFile()).toString())).exists()) {
				return "-f <Target file is not exist>";
			}
		}
		return "VALID";
	}

	public String checkDBConnection(BICLoaderConfig cnfgBean) {
		if (cnfgBean.getServerName() == null || cnfgBean.getServerName().length() <= 0) {
			return "-IP";
		}
		if (cnfgBean.getUsername() == null || cnfgBean.getUsername().length() <= 0) {
			return "-U";
		}
		if (cnfgBean.getPassword() == null || cnfgBean.getPassword().length() <= 0) {
			return "-P";
		}
		if (cnfgBean.getPortNumber() == null || cnfgBean.getPortNumber().length() <= 0) {
			return "-port";
		}
		if ((cnfgBean.getDatabaseName() == null || cnfgBean.getDatabaseName().length() <= 0)
				&& (cnfgBean.getDbServiceName() == null || cnfgBean.getDbServiceName().length() == 0)) {
			return "-dbname or -dbServiceName";
		} else {
			return "VALID";
		}
	}

	public String checkMandatoryParam(BICLoaderConfig cnfgBean) {
		if (cnfgBean.getDirectory() == null || cnfgBean.getDirectory().length() <= 0) {
			return "-path";
		}
		if (!cnfgBean.isAutoFiles() && (cnfgBean.getFile() == null || cnfgBean.getFile().length() <= 0)) {
			return "-f or -auto (EAITHER SPECIFY FILE NAME OR MAKE IT AUTO) ";
		}
		if (cnfgBean.isAutoFiles() && cnfgBean.getFile() != null && cnfgBean.getFile().length() > 0) {
			return "-f or -auto (ONLY ONE CAN BE USED)";
		}
		if (!cnfgBean.isBankFile() && !cnfgBean.isGpiCorr() && !cnfgBean.isBicdue() && !cnfgBean.isBankdirectory()) {
			return "You have to Specify File Type: -bankfile or -gpi or -bicdu or -bankdirectory";
		}
		if (cnfgBean.isBankFile() && cnfgBean.isGpiCorr() && cnfgBean.isBicdue() && cnfgBean.isBankdirectory()) {
			return "-bankfile or -gpi or -bicdue or -bankdirectory (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isBankFile() && cnfgBean.isGpiCorr()) {
			return "-bankfile and -gpi (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isBankFile() && cnfgBean.isBicdue()) {
			return "-bankfile and bicdue (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isBankFile() && cnfgBean.isBankdirectory()) {
			return "-bankfile and -bankdirectory (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isGpiCorr() && cnfgBean.isBicdue()) {
			return "-gpi and -bicdue (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isGpiCorr() && cnfgBean.isBankdirectory()) {
			return "-gpi and -bankdirectory (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isBicdue() && cnfgBean.isBankdirectory()) {
			return "-bicdue and -bankdirectory (ONLY ONE SHOULD BE USED)";
		}

		if (cnfgBean.isAutoFiles() && !cnfgBean.isFullFile() && !cnfgBean.isDeltaFile()) {
			return "-full or -delta (WHEN -auto IS USED, YOU HAVE TO SPECIFY FILE TYPE (FULL OR DELTA BUT NOT BOTH))";
		}
		if (cnfgBean.isAutoFiles() && cnfgBean.isFullFile() && cnfgBean.isDeltaFile()) {
			return "-full or -delta (ONLY ONE SHOULD BE USED)";
		}
		if (cnfgBean.isAutoFiles() && cnfgBean.isDeltaFile() && cnfgBean.isDeleteOld()) {
			return "-deleteold (CANNOT BE USED WHEN -delta AND -auto ARE USED TOGETHER) ";
		} else {
			return "VALID";
		}
	}

	public boolean checkFileAgainstArgument(BicFile bicFile, boolean isBankDirectory, boolean isBankFile,
			boolean isBicDue, boolean isGPI) {
		boolean returnValue = true;

		String fileName = bicFile.getFilename().toUpperCase();
		if ((isBankDirectory && (fileName.contains("BICDIR"))
				|| ((isBankFile || isBicDue) && (fileName.contains("BICPLUS") || fileName.contains("BANKDIRECTORYPLUS")))
				|| ((isBankFile || isBicDue || isBankDirectory) && fileName.contains("GPI"))
				|| (isGPI) && (!fileName.contains("GPI")))) {

			returnValue = false;

		}
		return returnValue;

	}

}
