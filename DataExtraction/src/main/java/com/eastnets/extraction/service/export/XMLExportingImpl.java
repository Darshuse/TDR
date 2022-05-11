package com.eastnets.extraction.service.export;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.bean.XMLMessage;
import com.eastnets.extraction.bean.XmlExportResult;
import com.eastnets.extraction.dao.search.XMLReaderDAO;
import com.eastnets.extraction.service.helper.ExportingUtils;
import com.eastnets.extraction.service.helper.FileWriterUtils;

@Service
public class XMLExportingImpl implements ExportingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLExportingImpl.class);

	@Autowired
	private XMLReaderDAO xmlReaderDAO;

	@Autowired
	private ExportingUtils exportingUtils;

	@Override
	public void exportMessages(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile) {

		LOGGER.info("MX Messages preparing process started.");

		XmlExportResult exportResult = getXmlExportResult(searchResult, statisticsFile, searchParam);
		List<String> xmlMessagesList = exportResult.getXmlMessage();

		if (exportResult.getXmlMessage() != null && !exportResult.getXmlMessage().isEmpty()) {
			LOGGER.info("MX Messages preparing process finished.");
		} else {
			LOGGER.info("No MX Messages will be generated.");
		}

		if (!searchParam.isDryRun() && xmlMessagesList != null) {
			List<String> resultList = new ArrayList<String>();
			for (String string : xmlMessagesList) {
				if (string != null && string.length() > 0) {
					resultList.add(xmlFormatter(string));
				}
			}
			LOGGER.info("MX Messages file/s generator started.");
			FileWriterUtils.generateFile(resultList, searchParam, "xml", statisticsFile);
			LOGGER.info("MX Messages file/s generator finished.");

		}

	}

	private XmlExportResult getXmlExportResult(List<SearchResult> searchResult, StatisticsFile statisticsFile, SearchParam searchParam) {

		int numberOfEmptyMXMessages = statisticsFile.getNumberOfEmptyMXMessages();

		List<String> detailedMessages = new ArrayList<String>();

		XmlExportResult exportResult = new XmlExportResult();

		try {

			List<SearchResult> mxMessages = searchResult.stream().filter(x -> "MX".equalsIgnoreCase(x.getMesgFrmtName()) || "AnyXML".equalsIgnoreCase(x.getMesgFrmtName())).collect(Collectors.toList());
			String buildCompsiteKeyString = exportingUtils.buildCompsiteKeyString(mxMessages);
			if (buildCompsiteKeyString != null && !buildCompsiteKeyString.isEmpty()) {
				List<XMLMessage> xmlMessages = xmlReaderDAO.getXMLMessage(buildCompsiteKeyString, true);
				mxMessages.stream()
						.forEach(n -> n.setXmlMessages(xmlMessages.stream().filter(x -> x.getAid().equals(n.getAid()) && x.getMesgUmidl().equals(n.getMesgUmidl()) && x.getMesgUmidh().equals(n.getMesgUmidh())).collect(Collectors.toList())));

				if (xmlMessages != null && !xmlMessages.isEmpty()) {

					for (XMLMessage xmlMessage : xmlMessages) {

						if (xmlMessage != null && xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {
							detailedMessages.add(xmlMessage.getXmlContent());
						} else {
							numberOfEmptyMXMessages++;
						}
					}
				}
			}

			numberOfEmptyMXMessages += mxMessages.stream().filter(x -> x.getXmlMessages().isEmpty()).count();

		} catch (Exception ex) {
			LOGGER.error("Print Message error : " + ex.getMessage());
		}
		statisticsFile.setNumberOfEmptyMXMessages(numberOfEmptyMXMessages);
		if (detailedMessages != null && detailedMessages.size() > 0) {
			statisticsFile.setNumberOfGeneratedMX(detailedMessages.size() + statisticsFile.getNumberOfGeneratedMX());
			exportResult.setXmlMessage(detailedMessages);
		}

		exportResult.setMissingXMLContentCount(numberOfEmptyMXMessages);

		return exportResult;
	}

	public static String xmlFormatter(String data) {
		StringWriter stringWriter = null;
		try {
			data = checkAMP(data);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));

			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);

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
}
