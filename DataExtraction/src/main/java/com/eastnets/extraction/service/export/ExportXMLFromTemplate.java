/**
 * 
 */
package com.eastnets.extraction.service.export;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.service.helper.FileNameUtils;
import com.eastnets.extraction.service.helper.FileWriterUtils;
import com.eastnets.extraction.service.helper.Utils;

/**
 * @author AAlrbee
 *
 */
@Service
public class ExportXMLFromTemplate implements ExportingService {

	private static final String GEN_XML_ROOT_NODE_NAME = "Messages";
	private static final String MESSAGE_NODE_NAME = "Message";

	private static final String GEN_XML_ROOT_FILE_NODE_NAME = "Files";
	private static final String FILE_NODE_NAME = "File";
	private static final String TEMPLATE_ROOT_NODE_NAME = "Format";
	private static final String TAG_NODE_NAME = "Column";
	private static final String FIELD_NODE_NAME = "field_name";
	private static final String DATE_FORMAT = "date_format";
	private static final String LABEL_NODE_NAME = "label";
	private static final int MIN_DECIMALS = 2;
	private static final int MAX_DECIMALS = 4;
	private static String cDataNodes = "SENDER_INFO , RECEIVER_INFO , TEXT_DATA_BLOCK , TEXT_DATA_BLOCK_EXPANDED , HISTORY_DATA_BLOCK";
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportXMLFromTemplate.class);

	public Document xmlDoc;
	private Map<String, Object> data;

	public String exportXMLMessages(List<Map<String, Object>> resultList, SearchParam searchParam, StatisticsFile statisticsFile) {

		String fileName = searchParam.getFilePath() + File.separatorChar + FileNameUtils.getFormatedFileName(searchParam) + "_" + searchParam.getTransactionNumber();
		generateXML(searchParam.getXmlTemplateFile(), fileName, resultList, searchParam, statisticsFile);
		return fileName;

	}

	public void generateXML(String templetPath, String exportedFileName, List<Map<String, Object>> resultList, SearchParam searchParam, StatisticsFile statisticsFile) {

		List<String> nodeList = new ArrayList<String>();

		Path file = Paths.get(exportedFileName);
		// this node from XML template.
		Node rootNode = Utils.readXML(templetPath);
		if (rootNode.getNodeType() == Node.ELEMENT_NODE && rootNode.getNodeName().equalsIgnoreCase(TEMPLATE_ROOT_NODE_NAME)) {
			// this node for generated XML file
			String rootStatus;

			// Return the message node from Template XML
			if (searchParam.getSource().equalsIgnoreCase("EXTRACT_FILE_VIEW")) {
				rootNode = searchNodeByName(searchNodeByName(rootNode, GEN_XML_ROOT_FILE_NODE_NAME), FILE_NODE_NAME);
				rootStatus = "file";
			} else {
				rootNode = searchNodeByName(searchNodeByName(rootNode, GEN_XML_ROOT_NODE_NAME), MESSAGE_NODE_NAME);
				rootStatus = "message";
			}
			Element messages = createNewXML(rootStatus);

			if (rootNode != null) {
				for (Map<String, Object> data : resultList) {
					this.data = data;
					Node message;
					// this node for generated XML file
					if (searchParam.getSource().equalsIgnoreCase("EXTRACT_FILE_VIEW")) {
						message = buildFile(rootNode, data);
					} else {
						message = buildMessage(rootNode, data);
					}
					messages.appendChild(message);
					try {
						nodeList.add(nodeToString(message));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO loop one time for test
				}
				statisticsFile.setNumberOfGeneratedXMLMessages(nodeList.size() + statisticsFile.getNumberOfGeneratedXMLMessages());
				statisticsFile.setNumberOfGeneratedMessages(statisticsFile.getNumberOfGeneratedXMLMessages());
				LOGGER.info("Total number of messages generated so far : " + statisticsFile.getNumberOfGeneratedMessages());
				if (!searchParam.isDryRun() && nodeList.size() > 0) {
					FileWriterUtils.generateFile(nodeList, searchParam, ".xml", statisticsFile);
				}
			}
		}
	}

	private Node buildFile(Node rootNode, Map<String, Object> data) {
		// this node for generated XML file
		Node message = xmlDoc.createElement(FILE_NODE_NAME);
		nextChildLvl(message, rootNode, data);
		return message;
	}

	private Node buildMessage(Node rootNode, Map<String, Object> data) {
		// this node for generated XML file
		Node message = xmlDoc.createElement(MESSAGE_NODE_NAME);
		nextChildLvl(message, rootNode, data);
		return message;
	}

	private void nextChildLvl(Node parent, Node rootNode, Map<String, Object> data) {

		// this node from XML template.
		NodeList nodeList = rootNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				// if node is column, then we create new node with matched name
				if (child.getNodeName().equalsIgnoreCase(TAG_NODE_NAME)) {
					Node tagNode = createTagNode(child, data);
					parent.appendChild(tagNode);
				} else {
					// If it's not, then we create new node with same node name, and go on it's
					// children.
					Node other = xmlDoc.createElement(child.getNodeName());
					parent.appendChild(other);
					nextChildLvl(other, child, data);
				}
			}
		}
	}

	private Node createTagNode(Node child, Map<String, Object> data) {
		String nodeName = getNodeName(child);
		Node tagNode = xmlDoc.createElement(nodeName);
		// Check node name, to test if it's cData or normal text.
		String labelNode = searchNodeByName(child, FIELD_NODE_NAME).getTextContent();
		String nodeValue = getNodeValue(labelNode.toLowerCase()).toString();
		if (cDataNodes.contains(labelNode.toUpperCase())) {
			if ((((String) data.get("mesg_format")).equalsIgnoreCase("MX") || ((String) data.get("mesg_format")).equalsIgnoreCase("AnyXML")) && labelNode.toUpperCase().equalsIgnoreCase("TEXT_DATA_BLOCK")) {
				try {
					String nValue = nodeValue == null || nodeValue.equals("") ? nodeValue : ("\n").concat(nodeValue).concat("\n");
					nValue = "<MX>" + nValue + "</MX>";
					List<Node> appendXmlMessage = appendXmlMessage(nValue, "/*");
					appendXmlMessage.stream().forEach(node -> {
						tagNode.appendChild(xmlDoc.importNode(node, true));
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				tagNode.appendChild(xmlDoc.createCDATASection(nodeValue == null || nodeValue.equals("") ? nodeValue : ("\n").concat(nodeValue).concat("\n")));
			}

		} else {
			if (nodeName.equals("Amount")) {
				nodeValue = amountTagDecimalFormattter(nodeValue);
			}
			Node node = searchNodeByName(child, DATE_FORMAT);
			if (node != null) {
				try {
					if (nodeValue != null && !nodeValue.isEmpty()) {
						String dateFormat = node.getTextContent();
						Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(nodeValue);
						nodeValue = new SimpleDateFormat(dateFormat).format(date);
					}
				} catch (ParseException e) {
					LOGGER.error("please add valid date format " + e.getMessage());

				}
			}

			tagNode.setTextContent(nodeValue);
		}
		return tagNode;
	}

	private String amountTagDecimalFormattter(String nodeValue) {
		if (nodeValue == null || nodeValue.isEmpty()) {
			return "";
		}
		String value = nodeValue;
		boolean containsDecimals = false;
		String digits = "";
		if (value.contains(".")) {
			containsDecimals = true;
			String[] amountValue = value.split("\\.");
			digits = amountValue[1];
		}
		if (digits.length() < MIN_DECIMALS) {
			value = containsDecimals ? value : value + ".";
			while (digits.length() < MIN_DECIMALS) {
				digits = digits + "0";
				value = value + "0";
			}
		}
		if (digits.length() > MAX_DECIMALS) {
			while (digits.length() > MAX_DECIMALS) {
				digits = digits.substring(0, digits.length() - 1);
				value = value.substring(0, value.length() - 1);
			}
		} 
		return value;
	}
	
	
	public List<Node> appendXmlMessage(String xml, String xpathExpression) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
		List<Node> nodeList = new ArrayList<>(); // Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance(); // Create XPath object
		XPath xpath = xpathFactory.newXPath();
		try {
			// Create XPathExpression object
			XPathExpression expr = xpath.compile(xpathExpression); // Evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				NodeList childNodes = nodes.item(i).getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					nodeList.add(childNodes.item(j));
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodeList;
	}

	private Object getNodeValue(String labelNode) {
		Object value = null;
		value = data.get(labelNode);
		if (labelNode.equalsIgnoreCase("type")) {
			if ((data.get("mesg_format")).toString().equalsIgnoreCase("internal")) {
				value = data.get("mesg_format");
			}
		}
		if (value == null) {
			if (data.containsKey(labelNode)) {
				value = "";
			} else {
				value = "";
			}
		}
		return value;
	}

	// TODO format cData nodes
	// private Node cDataNodeFormat(Node parent , String text) {
	//
	// DOMImplementationLS lsImp = (DOMImplementationLS)xmlDoc.getImplementation();
	//
	// LSSerializer ser = lsImp.createLSSerializer();
	// ser.getDomConfig().setParameter("xml-declaration", false);
	// DocumentFragment cDataFormat = xmlDoc.createDocumentFragment();
	// String xml = ser.writeToString(cDataFormat);
	// return parent.appendChild(xmlDoc.createCDATASection(xml));
	// }

	private String getNodeName(Node parent) {
		Node labelNode = searchNodeByName(parent, LABEL_NODE_NAME);
		// if no label for the node, we take the node name.
		if (labelNode == null) {
			labelNode = searchNodeByName(parent, FIELD_NODE_NAME);
		}
		return labelNode.getTextContent();
	}

	private Node searchNodeByName(Node parent, String query) {
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

	public Element createNewXML(String rootStatus) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Element root = null;

		try {
			builder = factory.newDocumentBuilder();
			xmlDoc = builder.newDocument();
			// this node for generated XML file
			if (rootStatus.equalsIgnoreCase("file")) {
				root = xmlDoc.createElement(GEN_XML_ROOT_FILE_NODE_NAME);
			} else {
				root = xmlDoc.createElement(GEN_XML_ROOT_NODE_NAME);
			}
			xmlDoc.appendChild(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	// public void createXMLFile(String file, SearchParam searchParam) {
	//
	// try {
	//
	// XPath xpath = XPathFactory.newInstance().newXPath();
	// NodeList nodes = (NodeList) xpath.evaluate("//Messages/Message", xmlDoc, XPathConstants.NODESET);
	//
	// String fileName = file;
	// int fileNumber = 0, currentFileSize = 0;
	// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	// Document currentDoc = dbf.newDocumentBuilder().newDocument();
	// Node rootNode = currentDoc.createElement("Messages");
	// File currentFile = new File(fileName + fileNumber + ".xml");
	//
	// for (int i = 1; i <= nodes.getLength(); i++) {
	// Node imported = currentDoc.importNode(nodes.item(i - 1), true);
	//
	// int temp = getMessageSize(imported);
	//
	// if (currentFileSize + temp > searchParam.getFileSize() && searchParam.getFileSize() != 0) {
	// // writeToFile(rootNode, currentFile);
	// rootNode = currentDoc.createElement("Messages");
	// currentFile = new File((fileName + (++fileNumber)) + ".xml");
	// currentFileSize = 0;
	// }
	// currentFileSize += temp;
	// rootNode.appendChild(imported);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private static int getMessageSize(Node node) {
	//
	// String xml = "";
	// try {
	// StringWriter writer = new StringWriter();
	// Transformer transformer;
	// transformer = TransformerFactory.newInstance().newTransformer();
	// transformer.transform(new DOMSource(node), new StreamResult(writer));
	// xml = writer.toString();
	// } catch (TransformerFactoryConfigurationError | TransformerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return xml.length();
	// }

	// private static String nodeToString(Node node) {
	// String xml = "";
	// try {
	// StringWriter writer = new StringWriter();
	// Transformer transformer;
	// transformer = TransformerFactory.newInstance().newTransformer();
	// transformer.transform(new DOMSource(node), new StreamResult(writer));
	// xml = writer.toString();
	// } catch (TransformerFactoryConfigurationError | TransformerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return xml;
	// }

	private static String nodeToString(Node node) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(node);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;
	}

	@Override
	public void exportMessages(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile) {
	}
}
