package com.eastnets.extraction.service.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckerUtils.class);

	public static String checkDirection(String direction) {
		if (direction == null || direction.length() == 0) {
			return "";
		} else if (direction.equalsIgnoreCase("input") && direction.equalsIgnoreCase("output")) {
			LOGGER.error("Selected direction is not valid.");
			return "";
		}
		return direction;
	}

	public static String checkDateColumn(String dateColumn) {
		if (dateColumn == null || dateColumn.length() == 0) {
			return "mesg_crea_date_time";
		} else if (dateColumn.equalsIgnoreCase("mesg_crea_date_time") && dateColumn.equalsIgnoreCase("appe_crea_date_time")) {
			LOGGER.error("There is not column equals to " + dateColumn + ", you can select either mesg_crea_date_time or appe_crea_date_time");
			return "";
		}
		return dateColumn;
	}

	public static List<String> checkAid(String aid) {
		if (aid != null && aid.length() > 0)
			return Arrays.asList(aid.split(" "));
		else {
			LOGGER.warn("No Aid selected, it will extract from all available Aids.");
			return new ArrayList<String>();
		}
	}

	public static String checkMesgFormat(String mesgFormat) {

		if (mesgFormat != null && mesgFormat.length() > 0) {
			return mesgFormat;
		} else {
			LOGGER.warn("No Format selected, it will extract using any format.");
			return "Any";
		}
	}

	public static String checkFilePath(String filePath) {
		if (filePath != null && filePath.trim().length() > 0) {
			return filePath;
		} else {
			LOGGER.warn("No path is selected, Default path is the same path that the tool is in.");
			return System.getProperty("user.dir");
		}
	}

	public static int checkMode(Integer mode) {
		if (mode != null) {
			return mode;
		} else {
			LOGGER.warn("No Mode selected. default mode is 7.");
			return 7;
		}
	}

	public static double checkFileSize(String fileSize) {

		if (fileSize.length() > 0) {
			return (Double.parseDouble(fileSize));
		} else {
			LOGGER.warn("No File size selected.");
			return 0.0;
		}
	}

	public static int checkDbType(String DbType) {
		if (DbType.equalsIgnoreCase("MSSQL")) {
			return 1;
		} else {
			return 0;
		}
	}

	public static int checkSkipWeeks(String skipWeeks) {
		if (skipWeeks == null || skipWeeks.length() == 0) {
			return 0;
		}
		return Integer.parseInt(skipWeeks);
	}

	public static Integer checkMessageNumber(String messageNumber) {
		if (messageNumber == null || messageNumber.isEmpty()) {
			return 1000;
		}
		return Integer.parseInt(messageNumber);
	}

	public static boolean defaultIsFalse(Boolean value) {
		if (value == null) {
			return false;
		}
		return value;
	}
}
