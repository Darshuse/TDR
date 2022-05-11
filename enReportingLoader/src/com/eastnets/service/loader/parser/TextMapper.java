package com.eastnets.service.loader.parser;

import java.math.BigDecimal;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.XmlTextMessage;
import com.eastnets.domain.loader.XmlTextPK;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Message;
import com.eastnets.resilience.mtutil.XMLUitl;

public class TextMapper extends MessageMapper {

	public static XmlTextMessage mappText(DataPDU pdu) throws Exception {
		XmlTextMessage xmlTextMessage = new XmlTextMessage();
		XmlTextPK xmlPk = new XmlTextPK();
		xmlPk.setTextMsgOrder(1);
		xmlTextMessage.setXmlTextPk(xmlPk);
		Message message = getMessageNode(pdu);
		xmlTextMessage.setMsgFrmtName(message.getFormat());
		xmlTextMessage.setMsgIdentifier(message.getMessageIdentifier());
		xmlTextMessage.setBlkFlag(0);
		xmlTextMessage.setMsgSize(new BigDecimal(0));
		if (pdu.getBody().getContent() != null && !pdu.getBody().getContent().isEmpty()) {

			if (pdu.getBody().getContent().get(0) instanceof NodeList) {
				xmlTextMessage.setMsgHeader(XMLUitl.getNodeAsXML((Node) pdu.getBody().getContent().get(0)));
			}
			if (pdu.getBody().getContent().get(1) instanceof NodeList) {
				xmlTextMessage.setMsgDocument(XMLUitl.getNodeAsXML((Node) pdu.getBody().getContent().get(1)));
			}
		}
		return xmlTextMessage;

	}

	public static XmlTextMessage mappText(AMP amp, LoaderMessage loaderMessage) throws Exception {
		XmlTextMessage xmlTextMessage = new XmlTextMessage();
		XmlTextPK xmlPk = new XmlTextPK();
		xmlPk.setTextMsgOrder(1);
		xmlTextMessage.setXmlTextPk(xmlPk);
		Message message = getMessageNode(amp);
		xmlTextMessage.setMsgFrmtName("MX");
		if (message.getMessageIdentifier() != null) {
			if (message.getMessageIdentifier().equalsIgnoreCase("camt.056.001.01.ch.01")) {
				xmlTextMessage.setMsgIdentifier("camt.056.001.01");
			} else {
				xmlTextMessage.setMsgIdentifier(message.getMessageIdentifier());
			}
		}
		xmlTextMessage.setBlkFlag(0);
		xmlTextMessage.setMsgSize(new BigDecimal(0));
		xmlTextMessage.setMsgHeader(amp.getProtocolParameters().getInterAct().getParameters().getApplicationHeader());
		xmlTextMessage.setMsgDocument(loaderMessage.getPayload());

		return xmlTextMessage;

	}

	// private static void getTextBlock5Text(DataPDU value) throws Exception {
	// List<Object> string = value.getBody().getContent();
	// for (Object object : string) {
	// if (object instanceof NodeList) {
	//
	// }
	// }
	// }

}
