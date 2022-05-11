package com.eastnets.service.loader.parser;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.loader.XmlTextMessage;
import com.eastnets.domain.loader.XmlTextPK;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Message;
import com.eastnets.resilience.mtutil.XMLUitl;

/**
 * @author MKassab
 * 
 */
public class TextSepaMapper extends TextMapper {

	public static XmlTextMessage mappText(DataPDU pdu, final FilePayload filePayload, String mesgText, String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs) throws Exception {
		XmlTextMessage xmlTextMessage = new XmlTextMessage();
		XmlTextPK xmlPk = new XmlTextPK();
		xmlPk.setTextMsgOrder(mesgOrder);
		xmlPk.setAid(filePayload.getAid());
		xmlPk.setTextSUMIDH(filePayload.getSumidh());
		xmlPk.setTextSUMIDL(filePayload.getSumidl());
		xmlTextMessage.setXmlTextPk(xmlPk);
		xmlTextMessage.setMsgFrmtName("File");
		xmlTextMessage.setMsgIdentifier(mesgId);
		xmlTextMessage.setBlkFlag(new Integer(blkFlag));
		xmlTextMessage.setMsgSize(new BigDecimal(0));
		Document doc = XMLUitl.convertStringToDocument(mesgText);
		xmlTextMessage.setMsgDocument(XMLUitl.convertDocumentToString(doc));
		return xmlTextMessage;

	}

	public static XmlTextMessage mappText(AMP amp, final FilePayload filePayload, String mesgText, String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs) throws Exception {
		XmlTextMessage xmlTextMessage = new XmlTextMessage();
		XmlTextPK xmlPk = new XmlTextPK();
		Message message = getMessageNode(amp);
		xmlPk.setTextMsgOrder(mesgOrder);
		xmlPk.setAid(filePayload.getAid());
		xmlPk.setTextSUMIDH(filePayload.getSumidh());
		xmlPk.setTextSUMIDL(filePayload.getSumidl());
		xmlTextMessage.setXmlTextPk(xmlPk);
		xmlTextMessage.setMsgFrmtName("MX");
		if (message.getMessageIdentifier() != null) {
			if (message.getMessageIdentifier().equalsIgnoreCase("camt.056.001.01.ch.01")) {
				xmlTextMessage.setMsgIdentifier("camt.056.001.01");
			} else {
				xmlTextMessage.setMsgIdentifier(message.getMessageIdentifier());
			}
		}

		xmlTextMessage.setBlkFlag(new Integer(blkFlag));
		xmlTextMessage.setMsgSize(new BigDecimal(0));
		xmlTextMessage.setMsgDocument(mesgText);
		try {

			xmlTextMessage.setMsgHeader(amp.getProtocolParameters().getInterAct().getParameters().getApplicationHeader());
		} catch (Exception e) {

			xmlTextMessage.setMsgHeader(null);
		}

		return xmlTextMessage;

	}

	public static void main(String[] args) {
		String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlString)));
			System.out.println(document.getChildNodes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
