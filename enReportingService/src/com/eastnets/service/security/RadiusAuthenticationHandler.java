package com.eastnets.service.security;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.service.config.RadiusConfig;

import net.jradius.client.RadiusClient;
import net.jradius.client.auth.RadiusAuthenticator;
import net.jradius.dictionary.Attr_UserName;
import net.jradius.dictionary.Attr_UserPassword;
import net.jradius.packet.AccessAccept;
import net.jradius.packet.AccessReject;
import net.jradius.packet.AccessRequest;
import net.jradius.packet.RadiusRequest;
import net.jradius.packet.RadiusResponse;
import net.jradius.packet.attribute.AttributeList;

public class RadiusAuthenticationHandler {

	protected static final Logger LOGGER = LogManager.getLogger(RadiusAuthenticationHandler.class);

	private String configFile = "";
	private String domain;
	private String url;
	private String secret;
	private String authPort;
	private String protocol;
	private String attempts;
	private String timeOut;
	private int numberOfServers;
	private int authenticationMethod;
	private int serverNumber;
	List<RadiusConfig> configurations;

	public void init() {
		try {

			configurations = parseRadiusConfigFile();

		} catch (Exception e) {
			System.out.println("Radius Configuration Error");
			e.printStackTrace();
		}

	}

	public boolean authenticate(String username, String password, String validationKey, String domain)
			throws Exception {

		if (domain != null && !domain.trim().isEmpty() && username.startsWith(domain + "\\")) {
			username = username.substring(domain.length() + 1);// remove the
																// domain
		}

		if (configurations.isEmpty()) {

			System.out.println("There are no Radius Configurations.");

			return false;
		}

		numberOfServers = configurations.size();

		System.out.println("Number of Radius servers provided: " + numberOfServers);
		
		RadiusResponse radiusResponse;

		for (int i = 0; i < numberOfServers; i++) {
			
			serverNumber = i + 1;
			
			System.out.println("Trying to connect to server number: " + serverNumber);

			radiusResponse = radiusConnection(configurations.get(i), username, password + validationKey);

			boolean isAuthenticated = (radiusResponse instanceof AccessAccept);

			if (isAuthenticated) {

				System.out.println("Connected to server number " + (serverNumber));

				if (numberOfServers > 1 && serverNumber != 1) {
					System.out.println("Logical swapping between the servers to consider the working one as the default server.");
					Collections.swap(configurations, 0, i);
				}

				return true;
				
			} else if (radiusResponse instanceof AccessReject && radiusResponse.getCode() != -99) {
				
				System.out.println("Access Rejected By Server number " + (serverNumber));
				System.out.println("Not Authenticated!");
				return false;
			}

			if (!isAuthenticated) {
				System.out.println("Not Authenticated!");

			}
		}
		
		System.out.println("Connection Failed to any of The Servers.");
		return false;

	}

	private RadiusResponse radiusConnection(RadiusConfig radiusConfig, String username, String password)
			throws NumberFormatException, IOException {

		RadiusResponse reply = new AccessReject();
		
		try {

			
			InetAddress host = InetAddress.getByName(radiusConfig.url);
			
			System.out.println("URL: "  + radiusConfig.url);
			System.out.println("Username: " + username);
			RadiusClient rc;

			String decryptedSecret = "";

			try {

				decryptedSecret = AESEncryptDecrypt.decrypt(radiusConfig.secret);
				radiusConfig.secret = decryptedSecret;
				
			} catch (Exception e) {
				System.out.println("Secret Key Already Decrepted!");
			}

			rc = new RadiusClient(host, radiusConfig.secret, Integer.parseInt(radiusConfig.authPort),
					Integer.parseInt(radiusConfig.accPort), Integer.parseInt(radiusConfig.timeOut));

			AttributeList attrs = new AttributeList();
			attrs.add(new Attr_UserName(username));

			RadiusRequest request = new AccessRequest(rc, attrs);

			request.addAttribute(new Attr_UserPassword(password));

			System.out.println("Sending:\n" + request.toString());

			reply = rc.authenticate((AccessRequest) request, getAuthenticator(radiusConfig.protocol),
					Integer.parseInt(radiusConfig.attempts));

			if(reply != null)
			System.out.println("Received:\n" + reply.toString());

			return reply;


		} catch (Exception e) {

			System.out.println("Error in RADIUS Connection");
			System.out.println("Could'nt connect to server number " + serverNumber);
			RadiusResponse errorResponse = new AccessReject();
			errorResponse.setCode(-99);
			return errorResponse;
		}

	}

	private List<RadiusConfig> parseRadiusConfigFile() throws ParserConfigurationException, SAXException, IOException {

		List<RadiusConfig> radiusConfiguration = new ArrayList<RadiusConfig>();
		if (configFile == null || configFile.isEmpty()) {
			return radiusConfiguration;
		}

		File fXmlFile = new File(configFile);
		if (!fXmlFile.exists() || !fXmlFile.isFile()) {
			System.out.println("The File: \"" + configFile + "\" does not exist.");
			return radiusConfiguration;
		}
		if (fXmlFile.length() == 0) {

			return radiusConfiguration;
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		doc.getDocumentElement().normalize();
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try {

			XPathExpression expr = xpath.compile("//ENRADIUS//Server");
			NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nList.getLength(); i++) {

				RadiusConfig config = new RadiusConfig();
				Element node = (Element) nList.item(i);
				config.url = getSubElementValue(node, "url");
				config.secret = getSubElementValue(node, "secret");
				config.authPort = getSubElementValue(node, "authport");
				config.accPort = getSubElementValue(node, "accport");
				config.protocol = getSubElementValue(node, "authprotocol").toLowerCase();
				config.attempts = getSubElementValue(node, "attempts");
				config.timeOut = getSubElementValue(node, "timeout");
				radiusConfiguration.add(config);
			}

			return radiusConfiguration;
		} catch (XPathExpressionException e) {
			LOGGER.error("Error in Parsing Configuration File");
			e.printStackTrace();
		}
		return null;
	}

	private RadiusAuthenticator getAuthenticator(String authMethod)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class AuthenticationMethodClass = Class
				.forName("net.jradius.client.auth." + authMethod.toUpperCase() + "Authenticator");
		return (RadiusAuthenticator) AuthenticationMethodClass.newInstance();

	}

	private String getSubElementValue(Element parentElement, String elementName) {
		NodeList list = parentElement.getElementsByTagName(elementName);
		if (list.getLength() > 0) {
			return list.item(0).getTextContent();
		}
		return null;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAuthPort() {
		return authPort;
	}

	public void setAuthPort(String authPort) {
		this.authPort = authPort;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public int getAuthenticationMethod() {
		return authenticationMethod;
	}

	public void setAuthenticationMethod(int authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

}
