package com.eastnets.dao.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.viewer.MXMessageType;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.SEPABulkMessageType;
import com.eastnets.domain.viewer.SEPAMessageType;
import com.eastnets.domain.viewer.XMLMessage;
import com.eastnets.domain.viewer.XMLType;

public class XMLReaderDAOImp extends DAOBaseImp implements XMLReaderDAO {

	private static final long serialVersionUID = -3027439040276965494L;
	private boolean partitioned;
	private ViewerDAO viewerDAO;
	private String loggedInUser;

	@Override
	public List<XMLMessage> getXMLMessage(int aid, int umidl, int umidh, Date mesg_crea_date, boolean mxMessage) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(aid);
		parameters.add(umidh);
		parameters.add(umidl);

		MessageDetails messageDetails = null;
		try {
			messageDetails = viewerDAO.getMessageDetails(aid, umidl, umidh, mesg_crea_date, 0, loggedInUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String query = "";
		if (mxMessage) {
			query = "SELECT MESG_IDENTIFIER,XMLTEXT_SIZE,MESG_FRMT_NAME,XMLTEXT_MESG_ORDER,BLK_FLAG FROM RXMLTEXT WHERE (MESG_FRMT_NAME = 'MX' OR MESG_FRMT_NAME = 'AnyXML') AND AID = ? AND XMLTEXT_S_UMIDH = ? AND XMLTEXT_S_UMIDL = ?";
		} else {
			query = "SELECT MESG_IDENTIFIER,XMLTEXT_SIZE,MESG_FRMT_NAME,XMLTEXT_MESG_ORDER,BLK_FLAG FROM RXMLTEXT WHERE AID = ? AND XMLTEXT_S_UMIDH = ? AND XMLTEXT_S_UMIDL = ?";
		}

		if (isPartitioned()) {
			query += " and MESG_CREA_DATE_TIME = ? ";
			parameters.add(new Timestamp(mesg_crea_date.getTime()));
		}

		List<XMLMessage> xmlMessages = super.getJdbcTemplate().query(query, parameters.toArray(), new RowMapper<XMLMessage>() {

			@Override
			public XMLMessage mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				XMLMessage xmlMessage = new XMLMessage();
				XMLType xmlType;
				xmlMessage.setXmlContentSize(resultSet.getInt("XMLTEXT_SIZE"));
				xmlMessage.setBulkDistinguisherId(resultSet.getInt("XMLTEXT_MESG_ORDER"));
				int bulkFlag = resultSet.getInt("BLK_FLAG");
				String formatName = resultSet.getString("MESG_FRMT_NAME");
				/*
				 * determine xml format type file : SEPAType file & bulk flag = 1 : SEPABulkType XML : MXType
				 */
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

		for (XMLMessage xmlMessage : xmlMessages) {
			/*
			 * determine if we need to fetch the XML content at this moment by depending on the content size
			 */
			if ((xmlMessage.getXmlContentSize() / 1024) <= 1) {
				// fetch XML content
				String xmlTempConntant = "";
				if (super.getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
					xmlTempConntant = fetchSQLServerXMLContent(aid, umidl, umidh, xmlMessage.getBulkDistinguisherId(), messageDetails.getMesgCreaDateTimeOnDB());
					if (xmlTempConntant != null && !xmlTempConntant.isEmpty()) {
						xmlTempConntant = xmlTempConntant.replaceAll("apos;", "&apos;");
					}
					xmlTempConntant = addXMLHeader(xmlTempConntant, messageDetails);
					xmlMessage.setXmlContent(xmlTempConntant);

				} else if (super.getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE) {
					xmlTempConntant = fetchOracleXMLContent(aid, umidl, umidh, xmlMessage.getBulkDistinguisherId(), messageDetails.getMesgCreaDateTimeOnDB());
					if (xmlTempConntant != null && !xmlTempConntant.isEmpty()) {
						xmlTempConntant = xmlTempConntant.replaceAll("apos;", "&apos;");
						xmlTempConntant = addXMLHeader(xmlTempConntant, messageDetails);
					}
					xmlMessage.setXmlContent(xmlTempConntant);
				}

			} else {
				xmlMessage.setXmlContent("Message size is more than 1 Mega byte,to see the content please download the message");
			}
		}
		return xmlMessages;
	}

	private String addXMLHeader(String xmlContent, MessageDetails messageDetails) {
		StringBuilder xmlHeader = new StringBuilder("");
		xmlHeader.append("<Header>");
		xmlHeader.append("<Message>");

		xmlHeader.append("<SenderReference>");
		if (messageDetails.getMesgUumid() != null && messageDetails.getMesgCreaDateTime() != null && messageDetails.getMesgUumidSuffix() != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
			String dateStr = format.format(messageDetails.getMesgCreaDateTime());
			xmlHeader.append(messageDetails.getMesgUumid() + "$" + dateStr + messageDetails.getMesgUumidSuffix());
		}
		xmlHeader.append("</SenderReference>");

		xmlHeader.append("<MessageIdentifier>");
		if (messageDetails.getMesgIdentifier() != null) {
			xmlHeader.append(messageDetails.getMesgIdentifier());
		}
		xmlHeader.append("</MessageIdentifier>");

		xmlHeader.append("<Format>");
		if (messageDetails.getMesgFrmtName() != null) {
			xmlHeader.append(messageDetails.getMesgFrmtName());
		}
		xmlHeader.append("</Format>");

		xmlHeader.append("<SubFormat>");
		if (messageDetails.getMesgSubFormat() != null) {
			xmlHeader.append(messageDetails.getMesgSubFormat());
		}
		xmlHeader.append("</SubFormat>");

		xmlHeader.append("<Sender>");
		xmlHeader.append("<DN>");
		if (messageDetails.getMesgRequestorDn() != null) {
			xmlHeader.append(messageDetails.getMesgRequestorDn());
		}
		xmlHeader.append("</DN>");
		xmlHeader.append("<FullName>");
		xmlHeader.append("<X1>");
		if (messageDetails.getMesgSenderX1() != null) {
			xmlHeader.append(messageDetails.getMesgSenderX1());
		}
		xmlHeader.append("</X1>");
		xmlHeader.append("</FullName>");
		xmlHeader.append("</Sender>");

		xmlHeader.append("<Receiver>");
		xmlHeader.append("<DN>");
		if (messageDetails.getInstResponderDn() != null) {
			xmlHeader.append(messageDetails.getInstResponderDn());
		}
		xmlHeader.append("</DN>");
		xmlHeader.append("<FullName>");
		xmlHeader.append("<X1>");
		if (messageDetails.getInstReceiverX1() != null) {
			xmlHeader.append(messageDetails.getInstReceiverX1());
		}
		xmlHeader.append("</X1>");
		xmlHeader.append("</FullName>");
		xmlHeader.append("</Receiver>");

		xmlHeader.append("<InterfaceInfo>");
		xmlHeader.append("<UserReference>");
		if (messageDetails.getMesgUserReferenceText() != null) {
			xmlHeader.append(messageDetails.getMesgUserReferenceText());
		}
		xmlHeader.append("</UserReference>");

		xmlHeader.append("<MessageCreator>");
		if (messageDetails.getInstMessageCreator() != null) {
			xmlHeader.append(messageDetails.getInstMessageCreator());
		}
		xmlHeader.append("</MessageCreator>");

		xmlHeader.append("<MessageContext>");
		if (messageDetails.getInstMessageContext() != null) {
			xmlHeader.append(messageDetails.getInstMessageContext());
		}
		xmlHeader.append("</MessageContext>");

		xmlHeader.append("<MessageNature>");
		if (messageDetails.getMesgNatureFormatted() != null) {
			xmlHeader.append(messageDetails.getMesgNatureFormatted());
		}
		xmlHeader.append("</MessageNature>");

		xmlHeader.append("<Sumid>");
		xmlHeader.append(getSUMID(messageDetails.getMesgUmidh(), messageDetails.getMesgUmidl()));
		xmlHeader.append("</Sumid>");

		xmlHeader.append("</InterfaceInfo>");

		xmlHeader.append("<NetworkInfo>");

		xmlHeader.append("<Priority>");
		if (messageDetails.getPriority() != null) {
			xmlHeader.append(messageDetails.getPriority());
		}
		xmlHeader.append("</Priority>");
		xmlHeader.append("<IsPossibleDuplicate>");
		if (messageDetails.getMesgPossibleDupCreation() != null) {
			xmlHeader.append(messageDetails.getMesgPossibleDupCreation());
		}
		xmlHeader.append("</IsPossibleDuplicate>");
		xmlHeader.append("<Service>");
		if (messageDetails.getMesgService() != null) {
			xmlHeader.append(messageDetails.getMesgService());
		}
		xmlHeader.append("</Service>");

		xmlHeader.append("<SWIFTNetNetworkInfo>");
		xmlHeader.append("<RequestType>");
		if (messageDetails.getMesgRequestType() != null) {
			xmlHeader.append(messageDetails.getMesgRequestType());
		}
		xmlHeader.append("</RequestType>");

		xmlHeader.append("<Reference>");
		if (messageDetails.getInstCBTReference() != null) {
			xmlHeader.append(messageDetails.getInstCBTReference());
		}
		xmlHeader.append("</Reference>");

		xmlHeader.append("<IsCopyRequested>");
		if (messageDetails.getMesgIsCopyRequest() != null) {
			xmlHeader.append(messageDetails.getMesgIsCopyRequest());
		}
		xmlHeader.append("</IsCopyRequested>");

		xmlHeader.append("</SWIFTNetNetworkInfo>");

		xmlHeader.append("</NetworkInfo>");

		xmlHeader.append("<SecurityInfo>");

		xmlHeader.append("<IsSigningRequested>");
		if (messageDetails.getMesgSecurityRequiredBool() != null) {
			xmlHeader.append(messageDetails.getMesgSecurityRequiredBool());
		}
		xmlHeader.append("</IsSigningRequested>");

		xmlHeader.append("<SWIFTNetSecurityInfo>");
		xmlHeader.append("<IsNRRequested>");
		if (messageDetails.getAppeNonRepudiationType() != null) {
			xmlHeader.append(messageDetails.getAppeNonRepudiationType());
			if (messageDetails.getAppeNonRepudiationType().length() == 0) {
				xmlHeader.append(" ");
			}
		} else if (messageDetails.getAppeNonRepudiationType() == null) {
			xmlHeader.append(" ");
		}
		xmlHeader.append("</IsNRRequested>");
		xmlHeader.append("</SWIFTNetSecurityInfo>");

		xmlHeader.append("</SecurityInfo>");

		xmlHeader.append("</Message>");
		xmlHeader.append("</Header>");

		int index = xmlContent.indexOf("<MX>");
		if (index != -1) {
			index += 4;
			xmlContent = xmlContent.substring(0, index) + " " + xmlHeader.toString() + " " + xmlContent.substring(index);
		}
		return xmlContent;
	}

	public String getSUMID(Integer umidh, Integer umidl) {
		return Integer.toHexString(umidl) + Integer.toHexString(umidh);
	}

	public boolean isPartitioned() {
		return partitioned && getDbPortabilityHandler().getDbType() == DBPortabilityHandler.DB_TYPE_ORACLE;
	}

	public void setPartitioned(boolean partitioned) {
		this.partitioned = partitioned;
	}

	public ViewerDAO getViewerDAO() {
		return viewerDAO;
	}

	public void setViewerDAO(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	private String fetchOracleXMLContent(int aid, int umidl, int umidh, int distinguisherId, Date mesg_crea_date) {
		String eagerXMLContentQuery = "SELECT XML_TEXT.XMLTEXT_DATA.getCLOBVal()XMLTEXT_DATA,XML_TEXT.XMLTEXT_HEADER.getCLOBVal() XMLTEXT_HEADER FROM RXMLTEXT XML_TEXT WHERE XML_TEXT.AID = ? AND XML_TEXT.XMLTEXT_S_UMIDH = ? AND XML_TEXT.XMLTEXT_S_UMIDL = ? AND XML_TEXT.MESG_CREA_DATE_TIME = ? AND XML_TEXT.XMLTEXT_MESG_ORDER = ?";
		String xmlContent = super.getJdbcTemplate().query(eagerXMLContentQuery, new Object[] { aid, umidh, umidl, new Timestamp(mesg_crea_date.getTime()), distinguisherId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					Clob xmlData = resultSet.getClob("XMLTEXT_DATA");
					Clob xmlDataHeader = resultSet.getClob("XMLTEXT_HEADER");
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
					return body;
				}
				return null;
			}
		});
		return xmlContent;
	}

	private String fetchSQLServerXMLContent(int aid, int umidl, int umidh, int distinguisherId, Date mesg_crea_date) {

		String eagerXMLContentQuery = "SELECT XML_TEXT.XMLTEXT_DATA XMLTEXT_DATA,XML_TEXT.XMLTEXT_HEADER XMLTEXT_HEADER FROM RXMLTEXT XML_TEXT WHERE XML_TEXT.AID = ? AND XML_TEXT.XMLTEXT_S_UMIDH = ? AND XML_TEXT.XMLTEXT_S_UMIDL = ? AND XML_TEXT.XMLTEXT_MESG_ORDER = ?";
		String xmlContent = super.getJdbcTemplate().query(eagerXMLContentQuery, new Object[] { aid, umidh, umidl, distinguisherId }, new ResultSetExtractor<String>() {

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

	private String clobToString(Clob data) throws SQLException, IOException {
		InputStream inputStream = data.getAsciiStream();
		StringWriter stringWriter = new StringWriter();
		IOUtils.copy(inputStream, stringWriter, Charset.defaultCharset());
		return stringWriter.toString();
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	@Override
	public void fillLoggedInUserName(String loggedInUserName) {
		setLoggedInUser(loggedInUserName);
	}

}
