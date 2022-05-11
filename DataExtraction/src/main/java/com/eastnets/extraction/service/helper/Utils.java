package com.eastnets.extraction.service.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Utils implements Serializable {

	private static final long serialVersionUID = -1120104116854705372L;

	public static String xmlFormatter(String data) {
		StringWriter stringWriter = null;
		try {
			data = checkAMP(data);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));

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
		if (stringWriter == null) {
			return "";
		}
		return stringWriter.toString();
	}

	private static String checkAMP(String data) {
		data = data.replaceAll("&", "&amp;");
		return data;
	}

	public static Node readXML(String xmlFile) {

		// Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Element root = null;
		try {
			builder = factory.newDocumentBuilder();

			// Build Document
			Document document = builder.parse(new File(xmlFile));

			// Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();

			// Here comes the root node
			root = document.getDocumentElement();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return root;
	}

	public static HashMap<String, List<String>> readRules(Node root) {
		HashMap<String, List<String>> criterias = new HashMap<String, List<String>>();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				// if node is column, then we create new node with matched name
				if (child.getNodeName().equalsIgnoreCase("Criteria")) {
					Node field = searchNodeByName(child, "Field");
					if (field != null) {
						List<String> oldList = criterias.get(field.getTextContent());
						Node value = searchNodeByName(child, "Value");
						if (oldList == null || oldList.isEmpty()) {
							oldList = new ArrayList<String>();
						}
						oldList.add(value.getTextContent());
						criterias.put(field.getTextContent(), oldList);
					}
				}
			}
		}

		return criterias;
	}

	private static Node searchNodeByName(Node parent, String query) {
		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase(query)) {
					return child;
				}
			}
		}
		return null;
	}
}
