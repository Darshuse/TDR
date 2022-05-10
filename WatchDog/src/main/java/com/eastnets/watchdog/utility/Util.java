package com.eastnets.watchdog.utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.entities.AppendixPK;
import com.eastnets.entities.JrnlPK;
import com.eastnets.entities.WDAppeKey;
import com.eastnets.entities.WDJrnlKey;
import com.eastnets.watchdog.config.DBConfiguration;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.resultbeans.WDAppeKeyBean;
import com.eastnets.watchdog.resultbeans.WDJrnlKeyBean;

@Service
public class Util {

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	public static String getDatabaseURL(DBConfiguration dbConfig) {
		StringBuffer dbUrl = null;

		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl = new StringBuffer("jdbc:oracle:thin:@");
		} else {
			dbUrl = new StringBuffer("jdbc:jtds:sqlserver://");
		}

		if (dbConfig.getDbType().equalsIgnoreCase("Oracle")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPort());
			if (dbConfig.getServiceName() != null && !dbConfig.getServiceName().trim().isEmpty()) {
				dbUrl.append('/').append(dbConfig.getServiceName());
			} else {
				dbUrl.append(':').append(dbConfig.getDbName());
			}

		} else if (dbConfig.getDbType().equalsIgnoreCase("MSSQL")) {
			dbUrl.append(dbConfig.getServerName()).append(':').append(dbConfig.getPort());
			dbUrl.append(";databaseName=").append(dbConfig.getDbName());
			dbUrl.append(";instance=").append(dbConfig.getInstanceName());
		}
		return dbUrl.toString();
	}

	public String amountFormat(String text, String currency, String thousandAmountFormat, String decimalAmountFormat) {
		String moneyFormat = "###,###.";
		int numberOfDeciamlDigits = 0;

		if (text != null && !text.isEmpty() && text.contains(".")) {
			String fraction = text.substring(text.indexOf(".") + 1);
			numberOfDeciamlDigits = fraction.length();
		}

		for (int i = 0; i < numberOfDeciamlDigits; i++) {
			moneyFormat += "0";
		}

		DecimalFormat formatter = new DecimalFormat(moneyFormat);

		/*
		 * check amount configured separator from default config file
		 */
		if ((thousandAmountFormat != null && !thousandAmountFormat.isEmpty()) || (decimalAmountFormat != null && !decimalAmountFormat.isEmpty())) {
			String fraction = "";
			int lastIndex = text.lastIndexOf('.');
			if (lastIndex != -1) {

				fraction = text.substring(lastIndex + 1);
			}

			Double number = Double.parseDouble(text);

			String foramtedNumberStr = formatter.format(number);

			String[] parts = foramtedNumberStr.split("\\.");
			String result = "";
			if (parts == null || parts.length == 0) {
				// Do nothing
			} else {
				if (fraction.isEmpty()) {
					result = parts[0] + ".";
					// return part zero only
				} else {
					result = parts[0] + "." + fraction.substring(0, fraction.length());
				}
			}

			if (thousandAmountFormat.equals(",")) {
				text = result;
				return text;
			} else if (decimalAmountFormat.equals(",")) {
				text = result;
				text = text.replace(",", "-"); // , = -
				text = text.replace(".", ",");
				text = text.replace("-", ".");
				return text;
			} else {
				return text;
			}

		} else {
			return text;
		}

	}

	public void checkForDebug(boolean enable) {
		Boolean debug = watchdogConfiguration.getDebug();
		if (enable) {
			if (debug)
				LogManager.getRootLogger().setLevel(Level.DEBUG);
		} else {
			LogManager.getRootLogger().setLevel(Level.INFO);
		}

	}

	public List<WDAppeKey> convertWdAppeBeanToEntity(List<WDAppeKeyBean> wdJrnlKeys) {
		List<WDAppeKey> convrtabelList = new ArrayList<WDAppeKey>();
		wdJrnlKeys.stream().forEach(wdAppeBean -> {
			WDAppeKey wdAppeKey = new WDAppeKey();
			AppendixPK appendixPK = new AppendixPK();
			appendixPK.setAid(wdAppeBean.getAid());
			appendixPK.setAppeDateTime(wdAppeBean.getAppeDateTime());
			appendixPK.setAppeInstNum(wdAppeBean.getAppeInstNum());
			appendixPK.setAppeSeqNbr(wdAppeBean.getAppeSeqNbr());
			appendixPK.setAppeSUmidh(wdAppeBean.getAppeSUmidh());
			appendixPK.setAppeSUmidl(wdAppeBean.getAppeSUmidl());
			wdAppeKey.setAppendixPK(appendixPK);
			wdAppeKey.setLastUpdate(wdAppeBean.getLastUpdate());
			wdAppeKey.setMesgCreaDateTime(wdAppeBean.getxCreaDtetime());
			convrtabelList.add(wdAppeKey);
		});

		return convrtabelList;

	}

	public List<WDJrnlKey> convertWdJrnlBeanToEntity(List<WDJrnlKeyBean> wdJrnlKeys) {
		List<WDJrnlKey> convrtabelList = new ArrayList<WDJrnlKey>();
		wdJrnlKeys.stream().forEach(wdJrnlKeyBean -> {
			WDJrnlKey jrnlKey = new WDJrnlKey();
			JrnlPK jrnlPK = new JrnlPK();
			jrnlPK.setAid(wdJrnlKeyBean.getAid());
			jrnlPK.setJrnlRevDateTime(wdJrnlKeyBean.getJrnlRevDateTime());
			jrnlPK.setJrnlSeqNumber(wdJrnlKeyBean.getJrnlSeqNumber());
			jrnlKey.setJrnlPK(jrnlPK);
			jrnlKey.setJrnlCompName(wdJrnlKeyBean.getJrnlCompName());
			jrnlKey.setJrnlEventNumber(wdJrnlKeyBean.getJrnlEventNumber());
			jrnlKey.setLastUpdate(wdJrnlKeyBean.getLastUpdate());
			jrnlKey.setJrnlDateTime(wdJrnlKeyBean.getJrnlDateTime());
			convrtabelList.add(jrnlKey);
		});

		return convrtabelList;

	}

}
