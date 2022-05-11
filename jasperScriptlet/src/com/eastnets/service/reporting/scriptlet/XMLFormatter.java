package com.eastnets.service.reporting.scriptlet;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLFormatter {

	
	public static String formatXMLText(String xmlText) {
		 StringWriter stringWriter = null ;
		try {
		    Document document = DocumentBuilderFactory.newInstance()
		            .newDocumentBuilder()
		            .parse(new InputSource(new ByteArrayInputStream(xmlText.getBytes("utf-8"))));
		 
		    XPath xPath = XPathFactory.newInstance().newXPath();
		    NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
		                                                  document,
		                                                  XPathConstants.NODESET);
		 
		    for (int i = 0; i < nodeList.getLength(); ++i) {
		        Node node = nodeList.item(i);
		        node.getParentNode().removeChild(node);
		    }
		 
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		 
		    stringWriter = new StringWriter();
		    StreamResult streamResult = new StreamResult(stringWriter);
		 
		    transformer.transform(new DOMSource(document), streamResult);
		 
		   // System.out.println(stringWriter.toString());
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		
		return stringWriter.toString();
	}
}
