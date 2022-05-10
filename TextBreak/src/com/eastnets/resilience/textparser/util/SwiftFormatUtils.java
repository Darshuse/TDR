/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.textparser.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.eastnets.resilience.textparser.db.bean.Correspondent;
import com.eastnets.resilience.textparser.db.dao.CorrespondentDAO;
import com.eastnets.resilience.textparser.db.dao.CountryDAO;
import com.eastnets.resilience.textparser.db.dao.CurrencyDAO;
import com.eastnets.resilience.textparser.db.dao.impl.CorrespondentDAOImpl;
import com.eastnets.resilience.textparser.db.dao.impl.CountryDAOImpl;
import com.eastnets.resilience.textparser.db.dao.impl.CurrencyDAOImpl;
import com.eastnets.resilience.textparser.exception.CorrespondentNotFound;
import com.eastnets.resilience.textparser.exception.CountryNotFound;
import com.eastnets.resilience.textparser.exception.CurrencyNotFound;

/**
 * This class provides methods to convert field components to objects. It
 * handles for example; dates, currencies and general functions defined in the
 * SWIFT standard.
 * <ul>
 * <li>&lt;DATE&gt; MMDD | YYMMDD | YYMM | YYYYMMDD
 * <li>&lt;AMOUNT&gt; ###,### (digits with comma as decimal separator)
 * <li>&lt;TIME&gt; HHmmss | HH | HHmm
 * </ul>
 * 
 * @author EHakawati
 * @version 1.0
 */
public class SwiftFormatUtils {
	private static final String PADDING = "    ";

	private static transient final java.util.logging.Logger logger = java.util.logging.Logger
			.getLogger(SwiftFormatUtils.class.getName());

	/**
	 * Parses a DATE string into a Calendar object.
	 * 
	 * @param date
	 *            string to parse
	 * @param format
	 *            string of format
	 * @return parsed date or <code>null</code> if the argument did not matched
	 *         the expected date format
	 * @throws ParseException 
	 */
	
	
	public static String getDate(String date, String pattern, String format) {
		
		try {
		if (date != null && !date.isEmpty()) {
			SimpleDateFormat formatter;
			
			if (pattern.equalsIgnoreCase("YYMMDD"))
				formatter = new SimpleDateFormat("yyMMdd",Locale.ENGLISH);	
			else if (pattern.equalsIgnoreCase("YYYYMMDD"))
				formatter = new SimpleDateFormat("yyyyMMdd",Locale.ENGLISH);		
			else
				formatter = new SimpleDateFormat();
			
			Date dateFormated = formatter.parse(date);
			formatter.applyLocalizedPattern(format);	
			return formatter.format(dateFormated);
			
		} 
		return date;
		}catch(Exception e) {
			
			System.out.println("System's Default Locale: " + "\n" + "Locale Country: " + Locale.getDefault().getCountry() + "\n" + "Locale Language: " +  Locale.getDefault().getLanguage());
			e.printStackTrace();
			
			return "";
			
		}
	}

	/**
	 * Parses a value into a java Number (BigDecimal) using the comma for
	 * decimal separator.
	 * 
	 * @param amount
	 *            to parse
	 * @return Number of the parsed amount or <code>null</code> if the number
	 *         could not be parsed
	 */
	public static Number getNumber(String amount) {
		Number number = null;
		if (amount != null) {
			try {
				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
				symbols.setDecimalSeparator(',');
				DecimalFormat df = new DecimalFormat("00.##", symbols);
				df.setParseBigDecimal(true);
				number = df.parse(amount);
			} catch (ParseException e) {
				logger.warning("Error parsing number: " + e.getMessage());
			}
		}
		return number;
	}

	/**
	 * Parses a Number into a SWIFT string number ####,## where trunked zero
	 * decimals and mandatory decimal separator.
	 * <ul>
	 * <li>Example: 1234,</li>
	 * <li>Example: 1234,56</li>
	 * </ul>
	 * 
	 * @param number
	 *            to parse
	 * @return Number of the parsed amount or <code>null</code> if the number is
	 *         null
	 */
	public static String getNumber(Number number) {
		if (number != null) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator(',');
			DecimalFormat df = new DecimalFormat("0.##", symbols);
			df.setParseBigDecimal(true);
			df.setDecimalSeparatorAlwaysShown(true);
			String formatted = df.format(number);
			String result = StringUtils.replaceChars(formatted, '.', ',');
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	private static Calendar getCalendar(String value, final String format) {
		if (value != null) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat(format);
				sdf.setLenient(false);
				final Date d = sdf.parse(value);
				final Calendar cal = new GregorianCalendar();
				cal.setTime(d);
				return cal;
			} catch (ParseException e) {
				logger.warning( "Caught exception in SwiftFormatUtils, method getCalendar: " + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * @param hhmmss
	 *            hour, minutes and seconds
	 * @return a Calendar set with the given hour, or and hour and minutes
	 */
	public static String getTime(String time, String pattern, String format) throws ParseException {
		

		
		if (time != null && !time.isEmpty()) {
			SimpleDateFormat formatter;
			
			if (pattern.equalsIgnoreCase("HHMM")){
				formatter = new SimpleDateFormat("HHmm");
				format = "HH:mm";
			}
			else if (pattern.equalsIgnoreCase("HHMMSS")){
				formatter = new SimpleDateFormat("HHmmss");	
				format = "HH:mm:ss";
			}
			else
				formatter = new SimpleDateFormat();
			
			Date dateFormated = formatter.parse(time);
			formatter.applyLocalizedPattern(format);	
			return formatter.format(dateFormated);
			
		} 
		return time;
	
	}

	/**
	 * @param string
	 *            with a single character
	 * @return the Character for the given string
	 */
	public static Character getSign(String string) {
		if (StringUtils.isNotEmpty(string)) {
			return Character.valueOf(string.charAt(0));
		}
		return null;
	}


	/**
	 * Format/expand currency symbol
	 * 
	 * @param currency
	 * @return
	 */
	public static String getCurrency(String shortCode) {
		CurrencyDAO dao = new CurrencyDAOImpl();

		try {
			return shortCode + " \t \t" + dao.getCurrency(shortCode).getName();
		} catch (SQLException e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getCurrency : " + e.getMessage());
		} catch (CurrencyNotFound e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getCurrency : " + e.getMessage());
		}
		return shortCode;

	}
	
	public static String getCountry(String shortCode) {
		CountryDAO dao = new CountryDAOImpl();

		try {
			return shortCode + "\t \t \t \t \t" + dao.getCountry(shortCode).getCountryName();
		} catch (SQLException e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getCountry : " + e.getMessage());
		} catch (CountryNotFound e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getCountry : " + e.getMessage());
		}
		return shortCode;

	}

	/**
	 * Format/expand currency symbol
	 * 
	 * @param currency
	 * @return
	 */
	public static String getFormatedBic(String bicCode, Timestamp timestamp) {

		String bic = bicCode;
		if (bicCode.length() == 8) {
			bic = bicCode + "XXX";
		} else if (bicCode.length() == 12) {
			bic = bic.substring(0, 8) + bic.substring(10);
		}

		CorrespondentDAO dao = new CorrespondentDAOImpl();

		try {
			//TFS24274field 41A ( MT700) comes with extra \r\n. so getCorrespondent could not able to a get its details from rCorr.
			if(bicCode.length()>11){
				bicCode=bicCode.substring(0,11);
			}
			Correspondent correspondent = dao.getCorrespondent(bicCode, timestamp);
			String correspondentText= correspondent.toString();
			correspondentText= correspondentText.replaceAll("\r\n", "\r\n" + PADDING + " ");
			
			return bicCode + correspondentText;
		} catch (SQLException e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getFormatedBic : " + e.getMessage());
		} catch (CorrespondentNotFound e) {
			logger.warning("Caught exception in SwiftFormatUtils, method getFormatedBic :" + e.getMessage() );
		}
		return bicCode;

	}
}
