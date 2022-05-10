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
package com.eastnets.resilience.xmldump.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.eastnets.resilience.xsd.InputStartEntry;
import com.eastnets.resilience.xsd.eventjournal.EventReturn;
import com.eastnets.resilience.xsd.messaging.Message;

/**
 * saa_query XML dump output parser (both message and event)
 * 
 * @author EHakawati
 * 
 */
public class SaaQueryParser implements Parser {

	// set up a StAX reader
	private XMLInputFactory xmlif;
	private XMLStreamReader xmlr;
	private JAXBContext ucontext;
	private Unmarshaller unmarshaller;
	private int batchSize;
	private boolean parsingStarted = false;

	// Message of EventReturn
	private Class<? extends InputStartEntry> objectsType;

	/**
	 * Constructor
	 * 
	 * @param file
	 * @param batchSize
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws JAXBException
	 */
	public SaaQueryParser(File file, int batchSize) throws FileNotFoundException, XMLStreamException, JAXBException {

		this.batchSize = batchSize;

		xmlif = XMLInputFactory.newInstance();
		xmlr = xmlif.createXMLStreamReader(new BufferedReader(new FileReader(file)));

		if (startParsing()) {
			ucontext = JAXBContext.newInstance(getObjectsType());
			unmarshaller = ucontext.createUnmarshaller();
		}

	}

	/**
	 * @return list of message of events
	 */
	@Override
	public List<? extends InputStartEntry> getNextBatch() {
		try {

			List<InputStartEntry> objectsList = new LinkedList<InputStartEntry>();
			int nbrRecords = 0;

			while (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && nbrRecords < this.batchSize) {

				nbrRecords++;

				// parse XML block into POJOs
				JAXBElement<? extends InputStartEntry> pt = unmarshaller.unmarshal(xmlr, getObjectsType());
				objectsList.add(pt.getValue());

				if (xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
					xmlr.next();
				}
			}

			if (nbrRecords == 0) {
				return null;
			} else {
				return objectsList;
			}

		} catch (XMLStreamException e) {

			e.printStackTrace();
		} catch (JAXBException e) {

			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void endParsing() {
		try {
			xmlr.close();
		} catch (XMLStreamException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean startParsing() {

		if (parsingStarted) {
			return true;
		}

		parsingStarted = true;

		boolean startParsing = true;
		try {
			xmlr.nextTag();
			xmlr.require(XMLStreamConstants.START_ELEMENT, null, "Messages");
			xmlr.nextTag();
			setObjectsType(Message.class);

		} catch (XMLStreamException e) {
			try {
				xmlr.require(XMLStreamConstants.START_ELEMENT, null, "Events");
				xmlr.nextTag();
				setObjectsType(EventReturn.class);
			} catch (XMLStreamException ex) {
				startParsing = false;
			}
		}

		return startParsing;

	}

	/**
	 * @return object class (Message/EventReturn)
	 */
	public Class<? extends InputStartEntry> getObjectsType() {
		return objectsType;
	}

	/**
	 * @param objectsType
	 *            (Message/EventReturn)
	 */
	public void setObjectsType(Class<? extends InputStartEntry> objectsType) {
		this.objectsType = objectsType;
	}

}
