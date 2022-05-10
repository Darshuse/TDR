package com.eastnets.watchdog.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.eastnets.watchdog.config.WatchdogConfiguration;

class XMLConfigParser {

	private static final Logger logger = Logger.getLogger(XMLConfigParser.class);

	protected WatchdogConfiguration parseConfigFile(String configFile) throws ParserConfigurationException, SAXException, IOException {
		return null;
	}
}
