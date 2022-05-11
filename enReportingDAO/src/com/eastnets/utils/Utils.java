package com.eastnets.utils;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Utils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2791405811823860948L;
	private static final Logger LOGGER = Logger.getLogger(Utils.class);

	public static Date afterCurrentDay(int numberOfDays) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
		return calendar.getTime();
	}

	public static Date afterCurrentDayIgnoreTime(int numberOfDays) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String xmlFormatter(String data) {
		StringWriter stringWriter = null;
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));

			/*
			 * XPath xPath = XPathFactory.newInstance().newXPath(); NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);
			 * 
			 * for (int i = 0; i < nodeList.getLength(); ++i) { Node node = nodeList.item(i); node.getParentNode().removeChild(node); }
			 */
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			stringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(stringWriter);

			transformer.transform(new DOMSource(document), streamResult);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	// Formating Currency Of MSG
	public static BigDecimal formatAmount(BigDecimal amount, String currency, Map<String, Integer> currenciesMap) {

		try {
			if (amount == null || (currency == null || currency.isEmpty())) {
				return amount;
			}

			LOGGER.debug("currency to add decimal format is " + currency + " and the amount is " + amount);
			Integer numberOfDigits = currenciesMap.get(currency);

			if (numberOfDigits == null) {
				return amount;
			}

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
			numberFormat.setMaximumFractionDigits(numberOfDigits);
			// 54279 : TDR 3.2: Amount decimal digits are truncated at trailing 0
			// String format = numberFormat.format(amount);
			String format = amount.toString();
			// String value = format.substring(1);
			String digits = "";
			if (format.contains(".")) {
				String[] amountValue = format.split("\\.");
				digits = amountValue[1];
			}

			if (digits.length() < numberOfDigits && digits.length() > 0 && digits != "") {
				while (digits.length() < numberOfDigits) {
					digits = digits + "0";
					format = format + "0";
				}
			}

			if (digits.length() > numberOfDigits && digits.length() > 0 && digits != "") {
				while (digits.length() > numberOfDigits) {
					digits = digits.substring(0, digits.length() - 1);
					format = format.substring(0, format.length() - 1);
				}
			}
			LOGGER.debug("currency is " + currency + " and the amount is " + format);
			BigDecimal bigDecimal = new BigDecimal(format);
			LOGGER.debug("formated amount is " + bigDecimal);
			return bigDecimal;

		} catch (Exception e) {
			LOGGER.debug("Filed when Parsing amount");
		}

		return null;

	}

}
