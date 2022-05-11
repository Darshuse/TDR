package com.eastnets.extraction.dao.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.eastnets.extraction.bean.MXMessageType;
import com.eastnets.extraction.bean.SEPABulkMessageType;
import com.eastnets.extraction.bean.SEPAMessageType;
import com.eastnets.extraction.bean.XMLMessage;
import com.eastnets.extraction.bean.XMLType;
import com.eastnets.extraction.config.YAMLConfig;
import com.eastnets.extraction.service.helper.DBPortabilityHandler;

@Component
public class XMLReaderDAOImp implements XMLReaderDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLReaderDAOImp.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private YAMLConfig config;

	@Override
	public List<XMLMessage> getXMLMessage(String compsiteKeyString, boolean mxMessage) {

		String query = "";
		if (mxMessage) {
			query = "SELECT AID, XMLTEXT_S_UMIDL, XMLTEXT_S_UMIDH, MESG_IDENTIFIER, XMLTEXT_SIZE, MESG_FRMT_NAME, XMLTEXT_MESG_ORDER, BLK_FLAG FROM RXMLTEXT WHERE (MESG_FRMT_NAME = 'MX' OR MESG_FRMT_NAME = 'AnyXML') AND ";
		} else {
			query = "SELECT AID, XMLTEXT_S_UMIDL, XMLTEXT_S_UMIDH, MESG_IDENTIFIER, XMLTEXT_SIZE, MESG_FRMT_NAME, XMLTEXT_MESG_ORDER, BLK_FLAG FROM RXMLTEXT WHERE ";
		}

		if (config.isPartitioned()) {
			query += "(AID, XMLTEXT_S_UMIDL, XMLTEXT_S_UMIDH, MESG_CREA_DATE_TIME) IN (" + compsiteKeyString + ")";
		} else {
			query += "(AID, XMLTEXT_S_UMIDL, XMLTEXT_S_UMIDH ) IN (" + compsiteKeyString + ")";
		}

		List<XMLMessage> xmlMessages = jdbcTemplate.query(query, new RowMapper<XMLMessage>() {

			@Override
			public XMLMessage mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				XMLMessage xmlMessage = new XMLMessage();
				xmlMessage.setAid(resultSet.getInt("AID"));
				xmlMessage.setMesgUmidl(resultSet.getInt("XMLTEXT_S_UMIDL"));
				xmlMessage.setMesgUmidh(resultSet.getInt("XMLTEXT_S_UMIDH"));
				xmlMessage.setXmlContentSize(resultSet.getInt("XMLTEXT_SIZE"));
				xmlMessage.setBulkDistinguisherId(resultSet.getInt("XMLTEXT_MESG_ORDER"));
				int bulkFlag = resultSet.getInt("BLK_FLAG");
				String formatName = resultSet.getString("MESG_FRMT_NAME");
				/*
				 * determine xml format type file : SEPAType file & bulk flag = 1 : SEPABulkType XML : MXType
				 */
				XMLType xmlType;
				if (formatName.equalsIgnoreCase("file")) {
					if (bulkFlag == 1) {
						// bulk xml type
						xmlType = new SEPABulkMessageType();
					} else {
						xmlType = new SEPAMessageType();
					}
				} else {
					xmlType = new MXMessageType();
				}
				xmlType.setXsdName(resultSet.getString("MESG_IDENTIFIER"));
				xmlMessage.setXmlType(xmlType);
				return xmlMessage;
			}
		});

		if (DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
			// fetchSQLServerXMLContent(compsiteKeyString, xmlMessage.getBulkDistinguisherId());
			fetchSQLServerXMLContent(compsiteKeyString, 0);
		} else {
			if (xmlMessages != null && !xmlMessages.isEmpty()) {
				StringBuilder orderId = new StringBuilder("(0),");
				xmlMessages.stream().filter(x -> !orderId.toString().contains(x.getBulkDistinguisherId().toString())).forEach((event) -> orderId.append('(').append(event.getBulkDistinguisherId()).append("),"));
				String substring = orderId.substring(0, orderId.length() - 1);

				xmlMessages = fetchOracleXMLContent(compsiteKeyString, substring, xmlMessages);
			}
		}

		for (XMLMessage xmlMessage : xmlMessages) {
			/*
			 * determine if we need to fetch the XML content at this moment by depending on the content size
			 */
			// fetch XML content
			if (DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
				String xmlTempConntant = "";

				xmlTempConntant = fetchSQLServerXMLContent(compsiteKeyString, xmlMessage.getBulkDistinguisherId());
				if (xmlTempConntant != null && !xmlTempConntant.isEmpty()) {
					xmlTempConntant = xmlTempConntant.replaceAll("apos;", "&apos;");
				}
				xmlMessage.setXmlContent(xmlTempConntant);
			} else if (DBPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
				if (xmlMessage.getXmlContent() != null && !xmlMessage.getXmlContent().isEmpty()) {
					xmlMessage.setXmlContent(xmlMessage.getXmlContent().replaceAll("apos;", "&apos;"));
				}
			}

		}
		return xmlMessages;
	}

	private String fetchSQLServerXMLContent(String compsiteKeyString, int distinguisherId) {

		String eagerXMLContentQuery = "SELECT XML_TEXT.XMLTEXT_DATA XMLTEXT_DATA, XML_TEXT.XMLTEXT_HEADER XMLTEXT_HEADER FROM RXMLTEXT XML_TEXT WHERE (XML_TEXT.AID,  XML_TEXT.XMLTEXT_S_UMIDL, XML_TEXT.XMLTEXT_S_UMIDH) IN (" + compsiteKeyString
				+ ") AND XML_TEXT.XMLTEXT_MESG_ORDER = ?";

		@SuppressWarnings("deprecation")
		String xmlContent = jdbcTemplate.query(eagerXMLContentQuery, new Object[] { distinguisherId }, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					String xmlData = resultSet.getString("XMLTEXT_DATA");
					String xmlDataHeader = resultSet.getString("XMLTEXT_HEADER");
					if (xmlDataHeader != null && !xmlDataHeader.isEmpty()) {

						xmlData = "<MX>".concat(xmlDataHeader).concat(xmlData).concat("</MX>");
					}
					return xmlData;
				}
				return null;
			}
		});
		return xmlContent;
	}

	private List<XMLMessage> fetchOracleXMLContent(String compsiteKeyString, String distinguisherId, List<XMLMessage> xmlMessage) {
		String eagerXMLContentQuery = "SELECT XML_TEXT.AID AID, XML_TEXT.XMLTEXT_S_UMIDL UMIDL, XML_TEXT.XMLTEXT_S_UMIDH UMIDH, XMLTEXT_MESG_ORDER, XMLTEXT_SIZE, XML_TEXT.XMLTEXT_DATA.getCLOBVal() XMLTEXT_DATA, XML_TEXT.XMLTEXT_HEADER.getCLOBVal() XMLTEXT_HEADER FROM RXMLTEXT XML_TEXT WHERE XML_TEXT.XMLTEXT_MESG_ORDER IN ("
				+ distinguisherId + ") AND ";

		if (config.isPartitioned()) {
			eagerXMLContentQuery += "(XML_TEXT.AID, XML_TEXT.XMLTEXT_S_UMIDL, XML_TEXT.XMLTEXT_S_UMIDH, XML_TEXT.MESG_CREA_DATE_TIME) IN (" + compsiteKeyString + ")";
		} else {
			eagerXMLContentQuery += "(XML_TEXT.AID, XML_TEXT.XMLTEXT_S_UMIDL, XML_TEXT.XMLTEXT_S_UMIDH) IN (" + compsiteKeyString + ")";
		}

		@SuppressWarnings("deprecation")
		List<String> result = jdbcTemplate.query(eagerXMLContentQuery, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet resultSet, int rowNum) throws SQLException, DataAccessException {

				Clob xmlData = resultSet.getClob("XMLTEXT_DATA");
				Clob xmlDataHeader = resultSet.getClob("XMLTEXT_HEADER");

				Integer aid = resultSet.getInt("AID");
				Integer umidl = resultSet.getInt("UMIDL");
				Integer umidh = resultSet.getInt("UMIDH");
				Integer xmlContentSize = resultSet.getInt("XMLTEXT_SIZE");
				Integer distinguisherId = resultSet.getInt("XMLTEXT_MESG_ORDER");

				String body = null;
				String header = null;
				try {
					if (xmlDataHeader != null) {
						header = clobToString(xmlDataHeader);
					}
					if (xmlData != null) {
						body = clobToString(xmlData);
					}
					if (header != null && !header.isEmpty()) {
						body = "<MX>".concat(header).concat(body).concat("</MX>");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ((xmlContentSize / 1024) <= 1) {
					xmlMessage.stream().filter(x -> x.getAid().equals(aid) && x.getMesgUmidl().equals(umidl) && x.getMesgUmidh().equals(umidh) && x.getBulkDistinguisherId().equals(distinguisherId)).findFirst().get().setXmlContent(body);
				} else {
					xmlMessage.stream().filter(x -> x.getAid().equals(aid) && x.getMesgUmidl().equals(umidl) && x.getMesgUmidh().equals(umidh) && x.getBulkDistinguisherId().equals(distinguisherId)).findFirst().get()
							.setXmlContent("Message size is more than 1 Mega byte,to see the content please download the message");
				}
				return body;

			}
		});
		return xmlMessage;
	}

	private String clobToString(Clob data) throws SQLException, IOException {
		InputStream inputStream = data.getAsciiStream();
		StringWriter stringWriter = new StringWriter();
		IOUtils.copy(inputStream, stringWriter, Charset.defaultCharset());
		return stringWriter.toString();
	}

}
