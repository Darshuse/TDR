/**
 * 
 */
package com.eastnets.reporting.licensing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eastnets.reporting.licensing.beans.BICLicenseInfo;
import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.ConnectionLicenseInfo;
import com.eastnets.reporting.licensing.beans.Licensable;
import com.eastnets.reporting.licensing.beans.License;
import com.eastnets.reporting.licensing.beans.LicenseMisc;
import com.eastnets.reporting.licensing.beans.Product;
import com.eastnets.reporting.licensing.beans.TrafficBand;
import com.eastnets.reporting.licensing.exception.LicenseException;

/**
 * 
 * @author EastNets
 * @since dDec 11, 2012
 * 
 */
public class LicenseUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8147180936522343383L;
	private static final String numbersStrings[] = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

	private static String getNumbersString(final int number, final String toAppend) {
		String concatToAndReturn = "";
		final char numberString[] = String.valueOf(number).toCharArray();
		for (int i = 0; i < numberString.length; i++) {
			concatToAndReturn += numbersStrings[numberString[i] - '0'];
		}
		return concatToAndReturn + ((toAppend == null) ? "" : toAppend);
	}

	public static String GenerateLicenseInfoString(final BICLicenseInfo licenseInfo) {
		final String dataString = getNumbersString(licenseInfo.getCurrentMonthCount(), ",") + getNumbersString(licenseInfo.getViolationMonthsCount(), ",") + getNumbersString(licenseInfo.getCurrentMonth(), ",")
				+ getNumbersString(licenseInfo.getCurrentYear(), null);
		return String.format("%o,%o,%o,%o,%s", licenseInfo.getCurrentMonthCount(), licenseInfo.getViolationMonthsCount(), licenseInfo.getCurrentMonth(), licenseInfo.getCurrentYear(),
				new String(Sign(dataString.toCharArray(), licenseInfo.getCurrentMonthCount() + licenseInfo.getViolationMonthsCount() + licenseInfo.getCurrentMonth() + licenseInfo.getCurrentYear())));
	}

	public static String GenerateLicenseInfoString(final ConnectionLicenseInfo licenseInfo) {
		final String dataString = getNumbersString(licenseInfo.isNewMessages() ? 12 : 340, ",") + getNumbersString(56, ",") + getNumbersString(78, ",") + getNumbersString(900, null);
		return String.format("%o,%o,%o,%o,%s", licenseInfo.isNewMessages() ? 12 : 340, 56, 78, 900, new String(Sign(dataString.toCharArray(), (licenseInfo.isNewMessages() ? 12 : 340) + 56 + 78 + 900)));
	}

	public static boolean ParseCheckLicenseInfoString(final BICLicenseInfo licenseInfo, final String licenseInfoString) {
		int currentMonthCount;
		int violationMonthsCount;
		int currentMonth;
		int currentYear;
		String key;
		final String[] scannedStrings = licenseInfoString.split(",");
		if (scannedStrings == null || scannedStrings.length != 5) {
			return false;
		}
		currentMonthCount = Integer.valueOf(scannedStrings[0], 8);
		violationMonthsCount = Integer.valueOf(scannedStrings[1], 8);
		currentMonth = Integer.valueOf(scannedStrings[2], 8);
		currentYear = Integer.valueOf(scannedStrings[3], 8);
		key = scannedStrings[4];
		final String dataString = getNumbersString(currentMonthCount, ",") + getNumbersString(violationMonthsCount, ",") + getNumbersString(currentMonth, ",") + getNumbersString(currentYear, null);
		if (check(dataString.toCharArray(), currentMonthCount + violationMonthsCount + currentMonth + currentYear, key.toCharArray())) {
			licenseInfo.setCurrentMonthCount(currentMonthCount);
			licenseInfo.setViolationMonthsCount((byte) violationMonthsCount);
			licenseInfo.setCurrentMonth((byte) currentMonth);
			licenseInfo.setCurrentYear((short) currentYear);
			return true;
		}
		return false;
	}

	public static boolean ParseCheckLicenseInfoString(final ConnectionLicenseInfo licenseInfo, final String licenseInfoString) {
		int n1;
		int n2;
		int n3;
		int n4;
		String key;
		final String[] scannedStrings = licenseInfoString.split(",");
		if (scannedStrings == null || scannedStrings.length != 5) {
			return false;
		}
		n1 = Integer.valueOf(scannedStrings[0], 8);
		n2 = Integer.valueOf(scannedStrings[1], 8);
		n3 = Integer.valueOf(scannedStrings[2], 8);
		n4 = Integer.valueOf(scannedStrings[3], 8);
		key = scannedStrings[4];
		if ((n1 == 12 || n1 == 340) && n2 == 56 && n3 == 78 && n4 == 900) {
			final String dataString = getNumbersString(n1, ",") + getNumbersString(n2, ",") + getNumbersString(n3, ",") + getNumbersString(n4, null);
			if (check(dataString.toCharArray(), n1 + n2 + n3 + n4, key.toCharArray())) {
				licenseInfo.setNewMessages(n1 == 12);
				return true;
			}
		}
		return false;
	}

	public static License readLicenseFromFile(String fileName) {
		try {
			return readLicenseFromFile(new FileInputStream(new File(fileName)));
		} catch (Exception ex) {
			throw new LicenseException(ex);
		}
	}

	public static License readLicenseFromFile(InputStream inputStream) {
		Document document;
		try {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			documentBuilderFactory.setFeature(FEATURE, true);

			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			documentBuilderFactory.setFeature(FEATURE, false);

			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			documentBuilderFactory.setFeature(FEATURE, false);

			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			documentBuilderFactory.setFeature(FEATURE, false);

			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks" (see reference below)
			documentBuilderFactory.setXIncludeAware(false);
			documentBuilderFactory.setExpandEntityReferences(false);

			document = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
		} catch (Exception e) {
			throw new LicenseException("error reading license file.", e);
		}

		document.getDocumentElement().normalize();

		// verify key is exists in license file
		if (document.getElementsByTagName("left") == null || document.getElementsByTagName("left").item(0) == null) {
			throw new LicenseException("Invalid License File");
		}
		if (document.getElementsByTagName("right") == null || document.getElementsByTagName("right").item(0) == null) {
			throw new LicenseException("Invalid License File");
		}

		String keys[] = new String[2];
		keys[0] = document.getElementsByTagName("left").item(0).getTextContent();
		keys[1] = document.getElementsByTagName("right").item(0).getTextContent();

		Integer maxConnections = Integer.valueOf(document.getElementsByTagName("connections").item(0).getTextContent());
		Integer maxUsers = Integer.valueOf(document.getElementsByTagName("users").item(0).getTextContent());
		String licenseID = document.getElementsByTagName("licenseid").item(0).getTextContent();

		String licenseDateEncryptedString = document.getElementsByTagName("licenseValidation").item(0).getTextContent();

		LicenseMisc licenseMisc = BeanFactory.getInstance().getNewLicenseMisc(maxConnections, maxUsers, licenseID, licenseDateEncryptedString);
		License license = BeanFactory.getInstance().getNewLicense(licenseMisc);

		NodeList nodes = document.getElementsByTagName("bic");
		for (int index = 0; index < nodes.getLength(); index++) {
			Element node = (Element) nodes.item(index);
			license.getBicCodes().add(BeanFactory.getInstance().getNewBICCode(node.getElementsByTagName("code").item(0).getTextContent(), TrafficBand.getVolumBand(Integer.valueOf(node.getElementsByTagName("volume").item(0).getTextContent())),
					Integer.valueOf(node.getElementsByTagName("volume").item(0).getTextContent())));
		}
		nodes = document.getElementsByTagName("product");
		for (int index = 0; index < nodes.getLength(); index++) {
			Element node = (Element) nodes.item(index);
			String expirationdate = node.getElementsByTagName("expirationdate").item(0).getTextContent();

			Product newProduct = BeanFactory.getInstance().getNewProduct(node.getElementsByTagName("id").item(0).getTextContent(), node.getElementsByTagName("description").item(0).getTextContent(),
					(expirationdate == null || expirationdate.length() == 0) ? null : Date.valueOf(expirationdate), Boolean.valueOf(node.getElementsByTagName("status").item(0).getTextContent()));

			// To distinguish between Traffic
			if (newProduct.getID().equals("13") || newProduct.getID().equals("15")) {
				license.getTraffics().add(newProduct);
			} else {
				license.getProducts().add(newProduct);
			}
		}

		license.setLicenseKeys(keys);

		try {
			license.setLicenseDate(License.getDateFromEncryptedString(licenseDateEncryptedString, false));
		} catch (Exception e) {
			throw new LicenseException("Invalid License File");
		}

		if (!LicenseUtils.checkLicense(license)) {
			throw new LicenseException("Invalid License File");
		}

		return license;
	}

	public static boolean WriteLicenseToFile(final License license, final String licenseFile) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final Element rootElement = (Element) document.appendChild(document.createElement("license"));
		final Element infoElement = (Element) rootElement.appendChild(document.createElement("info"));
		final Element keysElement = (Element) infoElement.appendChild(document.createElement("key"));
		keysElement.appendChild(document.createElement("left")).setTextContent(license.getLicenseKeys()[0]);
		keysElement.appendChild(document.createElement("right")).setTextContent(license.getLicenseKeys()[1]);

		try {
			infoElement.appendChild(document.createElement("licenseValidation")).setTextContent(license.getLicenseDateEncrypted(false));
			/*
			 * Calendar cal= Calendar.getInstance(); cal.set(1900, 01, 1 ); license.setLicenseDate(cal.getTime()); System.out.println( license.getLicenseDateEncrypted(true) );
			 */
		} catch (Exception e) {
			throw new LicenseException("Invalid License File");
		}

		infoElement.appendChild(document.createElement("licenseid")).setTextContent(license.getLicenseMisc().getLicenseID());
		infoElement.appendChild(document.createElement("users")).setTextContent(String.valueOf(license.getLicenseMisc().getMaxUsers()));
		infoElement.appendChild(document.createElement("connections")).setTextContent(String.valueOf(license.getLicenseMisc().getMaxConnections()));
		final Element bicsElement = (Element) rootElement.appendChild(document.createElement("bics"));
		for (final BicCode bic : license.getBicCodes()) {
			final Element bicElement = (Element) bicsElement.appendChild(document.createElement("bic"));
			bicElement.appendChild(document.createElement("code")).setTextContent(bic.getBicCode());
			bicElement.appendChild(document.createElement("volume")).setTextContent(String.valueOf(bic.getBandVolume()));
		}
		final Element products = (Element) rootElement.appendChild(document.createElement("products"));
		for (final Product product : license.getProducts()) {
			final Element productElement = (Element) products.appendChild(document.createElement("product"));
			productElement.appendChild(document.createElement("id")).setTextContent(product.getID());
			productElement.appendChild(document.createElement("description")).setTextContent(product.getDescription());
			productElement.appendChild(document.createElement("expirationdate")).setTextContent(product.getExpirationDate() == null ? "" : product.getExpirationDate().toString());
			productElement.appendChild(document.createElement("status")).setTextContent(String.valueOf(product.isLicensed()));
		}
		for (final Product product : license.getTraffics()) {
			final Element productElement = (Element) products.appendChild(document.createElement("product"));
			productElement.appendChild(document.createElement("id")).setTextContent(product.getID());
			productElement.appendChild(document.createElement("description")).setTextContent(product.getDescription());
			productElement.appendChild(document.createElement("expirationdate")).setTextContent(product.getExpirationDate() == null ? "" : product.getExpirationDate().toString());
			productElement.appendChild(document.createElement("status")).setTextContent(String.valueOf(product.isLicensed()));
		}
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.transform(new DOMSource(document), new StreamResult(new File(licenseFile)));
		return true;
	}

	private static char[] str2Hex(final char[] data) {
		final char[] hexString = new char[16];
		Arrays.fill(hexString, (char) 0);
		for (int i = 0; i < 8; i++) {
			hexString[i * 2] = (char) (data[i] / 16 < 10 ? (data[i] / 16) + 48 : (data[i] / 16) + 'A' - 10);
			hexString[(i * 2) + 1] = (char) (data[i] % 16 < 10 ? (data[i] % 16) + 48 : (data[i] % 16) + 'A' - 10);
		}
		return hexString;
	}

	private static char[] HashStr(final char[] secret, final char[] password, final int offset) {
		final char tempHashStr[] = new char[8];
		Arrays.fill(tempHashStr, (char) 0);
		for (int count = 0; count < secret.length; count++) {
			tempHashStr[(offset + count) % 8] ^= (secret[count] ^ password[count % password.length]);
		}
		return str2Hex(tempHashStr);
	}

	private static char[] Sign(final char[] data, final int offset) {
		return HashStr(data, "dmaaelgf".toCharArray(), offset);
	}

	public static boolean check(final char[] data, final int offset, final char[] key) {
		return new String(key).equals(new String(Sign(data, offset)));
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private static <Element> String toStringLicensableSortedWithNoDuplicates(final Licensable<Element>[] elements) {
		String toString = "";
		Licensable<Element> previousElement = null;
		Arrays.sort(elements);
		for (final Licensable<Element> element : elements) {
			if (previousElement == null || element.compareTo((Element) previousElement) != 0) {
				if (element.includeInLicense()) {
					toString += element.toLicenseDataString();
				}
				previousElement = element;
			}
		}
		return toString;
	}

	public static String[] generateLicenseKeys(License license) {
		if (license.getProducts().size() <= 0) {
			throw new LicenseException("No Products Supplied");
		}
		if (license.getTraffics().size() <= 0) {
			throw new LicenseException("No Traffics Supplied");
		}
		if (license.getBicCodes().size() <= 0) {
			throw new LicenseException("No BIC's Supplied");
		}
		try {
			LinkedList<Product> productsAndtraffics = new LinkedList<Product>();
			productsAndtraffics.addAll(license.getProducts());
			productsAndtraffics.addAll(license.getTraffics());
			final Product[] products = productsAndtraffics.toArray(new Product[0]);
			final BicCode[] bics = license.getBicCodes().toArray(new BicCode[0]);
			final String strBics = toStringLicensableSortedWithNoDuplicates(bics);
			final String strProducts = toStringLicensableSortedWithNoDuplicates(products);
			final String strData = license.getLicenseMisc().getLicenseID() + license.getLicenseMisc().getMaxUsers() + license.getLicenseMisc().getMaxConnections() + strBics + strProducts;
			final int offset = license.getLicenseMisc().getLicenseID().length() + license.getLicenseMisc().getMaxUsers() + license.getLicenseMisc().getMaxConnections() + strBics.length() / 8 + strProducts.length();
			final String strKey = new String(Sign(strData.toCharArray(), offset));
			return (strKey.length() == 16) ? (new String[] { strKey.substring(0, 8), strKey.substring(8) }) : null;
		} catch (Exception ex) {
			throw new LicenseException(ex);
		}
	}

	public static boolean checkLicense(final License license) {
		final String[] tmpKeys = generateLicenseKeys(license);
		return tmpKeys[0].equals(license.getLicenseKeys()[0]) && tmpKeys[1].equals(license.getLicenseKeys()[1]) || license.getLicenseDate() == null;
	}
}
