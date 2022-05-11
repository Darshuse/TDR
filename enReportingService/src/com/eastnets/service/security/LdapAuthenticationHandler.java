package com.eastnets.service.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.eastnets.domain.Pair;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.service.config.LdapConfig;

public class LdapAuthenticationHandler {

	private String configFile;
	private String domain;
	private String url;
	private String base;
	private String userDn;
	private String password;
	private boolean pooled = false;
	private String referral;
	private Integer numberOfTries;
	private String attrUsername;

	protected static final Logger LOGGER = LogManager.getLogger(LdapAuthenticationHandler.class);

	// map each ldapTemplate with the username search attribute
	private List<Pair<SimpleLdapTemplate, LdapConfig>> ldapTemplates = new ArrayList<Pair<SimpleLdapTemplate, LdapConfig>>();

	public void init() {
		List<LdapConfig> ldapConfigs = null;
		try {
			ldapConfigs = parseLdapConfigFile();
		} catch (Exception e) {
			LOGGER.error("Error : Parsing LDAP settings file failed. Reason : " + e.getMessage());
			ldapConfigs = new ArrayList<LdapConfig>();
		}

		LdapConfig defaultConfig = getDefaultTemplate();
		ldapConfigs.add(0, defaultConfig);

		for (LdapConfig config : ldapConfigs) {
			SimpleLdapTemplate template = getLdapTempate(config);
			if (template != null) {
				ldapTemplates.add(new Pair<SimpleLdapTemplate, LdapConfig>(template, config));
			}
		}
	}

	public boolean authenticate(String username, String userPassword, String domain) throws Exception {
		String err = "";

		// if the user as domain{\}username in the suser table, remove the
		// 'domain\' from the user name
		// domain{\}username without the {}, it was added to prevent unicode
		// error in eclipse
		if (domain != null && !domain.trim().isEmpty() && username.startsWith(domain + "\\")) {
			username = username.substring(domain.length() + 1);// remove the
																// domain
		}

		boolean domainfound = false;

		for (int i = 0; i < ldapTemplates.size(); i++) {

			Pair<SimpleLdapTemplate, LdapConfig> template = ldapTemplates.get(i);
			if (domain != null && !domain.trim().isEmpty() && !domain.trim().equalsIgnoreCase(template.getValue().domain)) {
				continue;
			}

			domainfound = true;

			int templateTries = template.getValue().numberOfTries;

			if (templateTries == 0) {
				templateTries = 1;
			}

			for (int j = 0; j < templateTries; j++) {

				try {

					AndFilter filter = new AndFilter();
					filter.and(new EqualsFilter(template.getValue().attrUsername, username));
					boolean authenticated = template.getKey().authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), userPassword);
					if (authenticated) {
						return true;
					}

				} catch (Exception e) {

					Throwable tr = e;
					while (tr.getCause() != null) {
						tr = tr.getCause();
					}

					e.printStackTrace();

					if (i == ldapTemplates.size() - 1 && j == templateTries - 1) {
						throw e;
					} else {
						LOGGER.error("Error in LDAP server (" + template.getValue().url + "): " + tr.getMessage());
						err += " Error in LDAP server (" + template.getValue().url + ") : " + tr.getMessage() + ".\n";
					}
				}
			}
		}

		if (!domainfound && domain != null && !domain.trim().isEmpty()) {
			String message = String.format("Domain \"%s\" was not found.", domain);
			LOGGER.info("LDAP authentication Error : " + message);
			throw new Exception(message);
		}
		if (!err.trim().isEmpty()) {
			throw new Exception(err);
		}

		return false;

	}

	private SimpleLdapTemplate getLdapTempate(LdapConfig config) {
		if (config.url == null || config.url.trim().isEmpty()) {
			if (!defaultString(config.base).isEmpty() || !defaultString(config.userDn).isEmpty()) {
				System.err.println("ldap server url is required.");
			}
			return null;
		}

		LdapContextSource contextSource = new LdapContextSource();
		String[] urlsTemp = config.url.trim().split(";");
		List<String> urls = new ArrayList<String>();
		for (String url : urlsTemp) {
			if (!url.trim().isEmpty()) {
				urls.add(url.trim());
			}
		}
		urlsTemp = new String[urls.size()];
		for (int i = 0; i < urls.size(); ++i) {
			urlsTemp[i] = urls.get(i);
		}
		contextSource.setUrls(urlsTemp);
		contextSource.setBase(defaultString(config.base));
		contextSource.setUserDn(defaultString(config.userDn));

		String password = defaultString(config.password);
		try {
			password = AESEncryptDecrypt.decrypt(defaultString(config.password));
		} catch (Exception e) {
		}
		contextSource.setPassword(password);
		contextSource.setPooled(config.pooled);
		contextSource.setReferral(defaultString(config.referral, "ignore"));

		try {
			contextSource.afterPropertiesSet();
		} catch (Exception e) {
			LOGGER.error("Error : Adding ldap server '" + config.url + "' faild due to : " + e.getMessage());
			return null;
		}

		return new SimpleLdapTemplate(contextSource);
	}

	private String defaultString(String str, String defaultValue) {
		return str == null ? defaultValue : str.trim();
	}

	private String defaultString(String str) {
		return defaultString(str, "");
	}

	private LdapConfig getDefaultTemplate() {
		LdapConfig config = new LdapConfig();
		config.base = base;
		config.url = url;
		config.userDn = userDn;
		config.password = password;
		config.pooled = pooled;
		config.referral = referral;
		config.attrUsername = attrUsername;
		config.domain = domain;
		config.numberOfTries = numberOfTries;
		return config;
	}

	private List<LdapConfig> parseLdapConfigFile() throws ParserConfigurationException, SAXException, IOException {
		List<LdapConfig> configurations = new ArrayList<LdapConfig>();

		if (configFile == null || configFile.isEmpty()) {
			return configurations;
		}

		File fXmlFile = new File(configFile);

		if (!fXmlFile.exists() || !fXmlFile.isFile()) {
			LOGGER.info("The File: \"" + configFile + "\" does not exist.");
			return configurations;
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try {
			XPathExpression expr = xpath.compile("//ENLDAP//Server");
			NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nList.getLength(); i++) {
				LdapConfig config = new LdapConfig();
				Element node = (Element) nList.item(i);
				config.url = getSubElementValue(node, "url");
				config.base = getSubElementValue(node, "base");
				config.userDn = getSubElementValue(node, "userDn");
				config.password = getSubElementValue(node, "password");
				config.attrUsername = getSubElementValue(node, "attrUsername");
				config.referral = getSubElementValue(node, "referral");
				config.domain = getSubElementValue(node, "domain");
				try {
					config.numberOfTries = Integer.parseInt(getSubElementValue(node, "numberOfTries"));
				} catch (Exception e) {
					config.numberOfTries = 1;
				}
				String pooled = getSubElementValue(node, "pooled");
				if (pooled != null) {
					config.pooled = pooled.equalsIgnoreCase("true");
				}
				configurations.add(config);
			}

			return configurations;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPooled() {
		return pooled;
	}

	public void setPooled(boolean pooled) {
		this.pooled = pooled;
	}

	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public String getAttrUsername() {
		return attrUsername;
	}

	public void setAttrUsername(String attrUsername) {
		this.attrUsername = attrUsername;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<Pair<SimpleLdapTemplate, LdapConfig>> getLdapTemplates() {
		return ldapTemplates;
	}

	public void setLdapTemplates(List<Pair<SimpleLdapTemplate, LdapConfig>> ldapTemplates) {
		this.ldapTemplates = ldapTemplates;
	}

	public Integer getNumberOfTries() {
		return numberOfTries;
	}

	public void setNumberOfTries(Integer numberOfTries) {
		this.numberOfTries = numberOfTries;
	}

}
