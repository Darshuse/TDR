
package com.eastnets.textbreak.utility;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.TextBreakConfig;

@Service
public class ArgumentHandlerTb {
	@Autowired
	TextBreakConfig textBreakConfig;

	private static final String KEY_DBNAME = "-dbname";
	private static final String KEY_PORT = "-port";
	private static final String KEY_DBTYPE = "-dbtype";
	private static final String KEY_ECF = "-ecf";
	private static final String KEY_IP = "-S";
	private static final String KEY_PASS = "-p";
	private static final String KEY_USER = "-u";
	private static final String ENABLE_DEBUG = "-v";
	private static final String SERVICE_NAME = "-servicename";
	private static final String KEY_AID = "-aid";
	private static final String INSTANCE_NAME = "-instancename";
	private static final String partitioned = "-partitioned";
	private static final String MESSAGE_NUMBER = "-x";
	private static final String DAY_NUMBER = "-d";
	private static final String FROM_DATE = "-from";
	private static final String TO_DATE = "-to";
	private static final String ALL_MESSAGES = "-all";
	private static final String XML_CONIFG = "-configfile";
	private static final String MQ_SERVER = "-mqServerIp";
	private static final String MQ_PORT = "-mqServerPort";
	private static final Logger LOGGER = Logger.getLogger(ArgumentHandlerTb.class);

	public boolean isvalidTextBreakConfig() {
		if (textBreakConfig == null) {
			displayUsage("");
			return false;
		}

		if (!textBreakConfig.isEnableTnsName()) {
			if (textBreakConfig.getPortNumber() == null || textBreakConfig.getPortNumber().isEmpty()) {
				displayUsage(KEY_PORT);
				return false;
			}
			if (textBreakConfig.getServerName() == null || textBreakConfig.getServerName().isEmpty()) {
				displayUsage(KEY_IP);
				return false;
			}
		}

		if (textBreakConfig.getDbType() == null || textBreakConfig.getDbType().isEmpty()) {
			displayUsage(KEY_DBTYPE);
			return false;
		}

		if (textBreakConfig.getPassword() == null || textBreakConfig.getPassword().isEmpty()) {
			displayUsage(KEY_PASS);
			return false;
		}

		if (textBreakConfig.getDbUsername() == null || textBreakConfig.getDbUsername().isEmpty()) {
			displayUsage(KEY_USER);
			return false;
		}

		if (textBreakConfig.getAid() == null || textBreakConfig.getAid().isEmpty()) {
			displayUsage(KEY_AID);
			return false;
		}

		if (textBreakConfig.isEnableDebug()) {
			enableDebugLoggerMode();
		}

		return true;

	}

	private void enableDebugLoggerMode() {
		Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
		Configurator.setRootLevel(Level.DEBUG);

	}

	private TextBreakConfig getXmlArgsConfig(String[] args) {
		String confPath = "";
		for (int i = 0; i < args.length; i++) {
			String value = args[i];
			if (XML_CONIFG.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					confPath = args[i];
				} else {
					displayUsage(value);
					return null;
				}
			} else {
				displayUsage(value);
				return null;
			}
		}

		try {
			XmlConfigrationParser xmlConfigrationParser = new XmlConfigrationParser(confPath);
			return xmlConfigrationParser.textBreakConfig;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void fillCommandLineArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String value = args[i];
			if (KEY_AID.equalsIgnoreCase(value)) {
				if (i++ < args.length) {
					textBreakConfig.setAid(args[i]);
				}
			} else {
				displayUsage("");
				return;
			}

		}

		return;
	}

	private boolean isXmlArgument(String[] mainArgs) {
		LOGGER.info("check configration type .. ");
		return Arrays.stream(mainArgs).anyMatch("-configfile"::equals);
	}

	public void displayUsage(String value) {
		System.out.println(" missing or invalid paramter " + KEY_AID + "\t\t: SWIFT Alliance ");

	}
}
