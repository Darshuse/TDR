package com.eastnets.service.loader.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.beans.AMPData.Header;
import com.eastnets.beans.AMPData.HistoryLine;
import com.eastnets.beans.AMPData.ProtocolParameters;
import com.eastnets.beans.AMPData.StructuredHistory;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.loader.Appe;
import com.eastnets.domain.loader.Inst;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Mesg;
import com.eastnets.domain.loader.Rfile;
import com.eastnets.domain.loader.Rintv;
import com.eastnets.domain.loader.RintvPK;
import com.eastnets.domain.loader.XmlTextMessage;
import com.eastnets.service.loader.exceptions.MessageParsingException;
import com.eastnets.service.loader.helper.jms.JMSSender;

/**
 * @author MKassab
 * 
 */
public class AmhMessageParser implements MessageParser {

	public enum PayloadType {
		NOT_SEPA, SEPA, SEPA_BLK
	}

	private Unmarshaller umarsh;
	String nodeName = "";
	private static final Logger LOGGER = Logger.getLogger(AmhMessageParser.class);
	private LoaderMessage loaderMessage;

	private JMSSender moveToErrorQSender;
	private List<String> sepaXSD;
	XmlTextMessage mappSepaText = new XmlTextMessage();

	final String HISTORY_TEXT_INDECETOR = "E N D  H I S T O R Y";
	final String MESSAGE_TEXT_INDECETOR = "E N D  M E S S A G E";
	final String MESSAGE_RAW_TEXT_INDECETOR = "E N D  R A W  M E S S A G E";

	public AmhMessageParser(LoaderMessage loaderMessage, Unmarshaller unmarsh, List<String> sepaXSD) {
		this.umarsh = unmarsh;
		this.loaderMessage = loaderMessage;
		this.sepaXSD = sepaXSD;
	}

	public void fillCoruptedMessages() {
		try {
			loaderMessage.setMessageIsCancelled(true);

			AMP amp = new AMP();
			LOGGER.debug("End XML(AMH) Message from Input Queue Using unmarshal");
			List<Appe> appes = new ArrayList<Appe>();
			List<Inst> intances = new ArrayList<Inst>();
			// XmlTextMessage mappText = null;
			Rfile rfile = null;
			LOGGER.debug("Start fillPayLoad :: ");
			String payload = "";
			try {
				payload = amp.getData().getStorage().getPayload();
			} catch (Exception e) {
				payload = null;
			}
			if (payload != null) {
				if (payload.contains("<NS1:Document")) {
					fillPayLoadPrefix(loaderMessage, amp, "NS1");
				} else {
					fillPayLoad(loaderMessage, amp);
				}
			} else {
				LOGGER.error("PayLoad null !!! ");
				loaderMessage.setPayload(loaderMessage.getRowData());
			}
			LOGGER.debug("Start fillPayLoad :: ");
			LOGGER.debug("AMH Message :: parsing Message Header");
			Mesg mesg = MesgHeaderMapper.mapMesg(amp, loaderMessage);
			LOGGER.debug("AMH Message :: parsing Message Instance");
			Inst mapInst = InstanceMapper.mapInst(amp, loaderMessage);
			rfile = fileMapper.mappFile(amp, loaderMessage);
			LOGGER.debug("AMH Message :: parsing Message Appendix");
			Appe mappAppe = AppendixMapper.mappAppe(amp, loaderMessage);

			if (mappAppe != null) {
				fillInstTransmissionDetails(mesg, mapInst, mappAppe);
				appes.add(mappAppe);
				mapInst.setRappes(appes);
			} else {
				mapInst.setRappes(null);
			}

			// get unit name form message header
			mapInst.setInstUnitName(mesg.getXInst0UnitName());
			intances.add(mapInst);
			mesg.setRinsts(intances);

			mesg.setRfile(rfile);
			mesg.getRfile().setMesgCreaDateTime(mesg.getMesgCreaDateTime());

			loaderMessage.setMesg(mesg);
			/*
			 * this will set aid, umidl and umidh for each object in this new message.
			 */
			loaderMessage.fillMesgKeys();
			fillXmlText(loaderMessage, mappSepaText, rfile, mesg, amp);
			fillRintv(loaderMessage);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public LoaderMessage parse() throws Exception {
		AMP amp = null;
		AMP payloadAmp = null;
		try {

			if (loaderMessage.getRowData() == null || loaderMessage.getRowData().length() == 0) {
				if (loaderMessage.getMessageSequenceNo() == null) {
					LOGGER.error("Empty AMH Message :: " + loaderMessage.getMesg().getMesgUumid());

				} else {
					LOGGER.error("Empty AMH Message :: " + loaderMessage.getMessageSequenceNo());
				}

				// LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));
				throw new MessageParsingException(null, loaderMessage);
			}

			LOGGER.debug("Start Parsing XML(AMH) Message from Input Queue Using unmarshal");
			synchronized (umarsh) {
				amp = (AMP) umarsh.unmarshal(new StreamSource(new StringReader(loaderMessage.getRowData())));
			}
			try {
				loaderMessage.setMessageIsCancelled((amp.getStatus().getCurrentStatus().getStatusGranularity().getAction().equalsIgnoreCase("Cancelled") ? true : false));
			} catch (Exception e) {
				loaderMessage.setMessageIsCancelled(false);

			}

			// doMappingForAmpObject(amp, payloadAmp);
			LOGGER.debug("End XML(AMH) Message from Input Queue Using unmarshal");
			List<Appe> appes = new ArrayList<Appe>();
			List<Inst> intances = new ArrayList<Inst>();
			// XmlTextMessage mappText = null;
			Rfile rfile = null;
			LOGGER.debug("Start fillPayLoad :: ");
			String payload = "";
			try {
				payload = amp.getData().getStorage().getPayload();
			} catch (Exception e) {
				payload = null;
			}
			if (payload != null) {
				if (payload.contains("<NS1:Document")) {
					fillPayLoadPrefix(loaderMessage, amp, "NS1");
				} else {
					fillPayLoad(loaderMessage, amp);
				}
			} else {
				LOGGER.error("PayLoad null !!! ");
				loaderMessage.setPayload(loaderMessage.getRowData());
			}
			LOGGER.debug("End fillPayLoad :: ");

			LOGGER.debug("AMH Message :: parsing  Structured Or UnStructured History ");
			fillStructuredUnStructuredHistory(amp, loaderMessage);
			LOGGER.debug("AMH Message :: parsing Message Header");
			Mesg mesg = MesgHeaderMapper.mapMesg(amp, loaderMessage);
			LOGGER.debug("AMH Message :: parsing Message Instance");
			Inst mapInst = InstanceMapper.mapInst(amp, loaderMessage);
			rfile = fileMapper.mappFile(amp, loaderMessage);
			LOGGER.debug("AMH Message :: parsing Message Appendix");
			Appe mappAppe = AppendixMapper.mappAppe(amp, loaderMessage);

			if (mappAppe != null) {
				fillInstTransmissionDetails(mesg, mapInst, mappAppe);
				appes.add(mappAppe);
				mapInst.setRappes(appes);
			} else {
				mapInst.setRappes(null);
			}

			// get unit name form message header
			mapInst.setInstUnitName(mesg.getXInst0UnitName());
			intances.add(mapInst);
			mesg.setRinsts(intances);

			mesg.setRfile(rfile);
			mesg.getRfile().setMesgCreaDateTime(mesg.getMesgCreaDateTime());

			loaderMessage.setMesg(mesg);
			/*
			 * this will set aid, umidl and umidh for each object in this new message.
			 */
			loaderMessage.fillMesgKeys();
			fillXmlText(loaderMessage, mappSepaText, rfile, mesg, amp);
			fillRintv(loaderMessage);

		} catch (Exception e) {
			if (e instanceof UnmarshalException) {
				fillCoruptedMessages();
				LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo());
			} else {
				LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo());
				throw new MessageParsingException(e, loaderMessage);
			}
		}
		// LOGGER.info("End ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));

		return loaderMessage;

	}

	private void fillStructuredUnStructuredHistory(AMP amp, LoaderMessage loaderMessage) {
		if (amp.getStatus() == null) {
			return;
		}
		// this if we fill history from fillpayload method

		try {
			String history = loaderMessage.getRowHistory();
			if (history == null) {
				history = "";
			}
			history = history + System.lineSeparator();
			String textHistory = amp.getStatus().getTextHistory();
			StructuredHistory structuredHistory = amp.getStatus().getStructuredHistory();
			if (textHistory != null && !textHistory.isEmpty()) {
				history = history + textHistory;
			} else if (structuredHistory != null) {
				List<HistoryLine> historyLine = structuredHistory.getHistoryLine();
				StringBuilder historyLines = new StringBuilder();
				for (HistoryLine line : historyLine) {

					historyLines.append("HistoryLine [");
					if (line.getHistoryDate() != null && !line.getHistoryDate().isEmpty()) {
						historyLines.append(" HistoryDate : " + line.getHistoryDate());
					}

					if (line.getPhase() != null && !line.getPhase().isEmpty()) {

						historyLines.append(" , Phase : " + line.getPhase());
					}
					if (line.getAction() != null && !line.getAction().isEmpty()) {

						historyLines.append(" , Action" + line.getAction());
					}

					if (line.getReason() != null && !line.getReason().isEmpty()) {

						historyLines.append(" , Reason : " + line.getReason());
					}

					if (line.getEntity() != null && !line.getEntity().isEmpty()) {

						historyLines.append(" , Entity : " + line.getEntity());
					}

					if (line.getDescription() != null && !line.getDescription().isEmpty()) {

						historyLines.append(" , Description : " + line.getDescription());
					}
					historyLines.append(" ] ");
					history = history + historyLines.toString() + System.lineSeparator();
					historyLines = new StringBuilder();

				}

			}
			loaderMessage.setRowHistory(history);
		} catch (Exception e) {
			LOGGER.error("fillStructuredUnStructuredHistory  Exception >>>>>> On  message ");
		}

	}

	private void doMappingForAmpObject(AMP amp, AMP payloadAmp) {
		if (payloadAmp == null)
			return;

		Header header = amp.getHeader();
		Header payLodheader = payloadAmp.getHeader();

		ProtocolParameters protocolParameters = amp.getProtocolParameters();
		ProtocolParameters payLoadprotocolParameters = payloadAmp.getProtocolParameters();

		if (header == null) {
			amp.setHeader(payLodheader);
		} else {

			BeanUtils.copyProperties(payLodheader, header, getNullPropertyNames(payLodheader));
		}

		if (protocolParameters == null) {
			amp.setProtocolParameters(payLoadprotocolParameters);
		} else {
			BeanUtils.copyProperties(payLoadprotocolParameters, protocolParameters, getNullPropertyNames(payLodheader));
		}
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			} else {
				if (srcValue instanceof String) {
					String tagValue = srcValue.toString();
					if (tagValue.isEmpty()) {
						emptyNames.add(pd.getName());
					}
				}
			}
		}

		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	private void fillPayLoad(LoaderMessage loadMesg, AMP amp) {
		String payload = amp.getData().getStorage().getPayload();
		try {
			// TODO Auto-generated method stub
			if (payload.contains(HISTORY_TEXT_INDECETOR) || payload.contains(MESSAGE_TEXT_INDECETOR) || payload.contains(MESSAGE_RAW_TEXT_INDECETOR)) {
				String messgaeHistory = payload.substring(0, payload.indexOf("<Document") - 1);
				String mesgDocumants = payload.substring(payload.indexOf("<Document"), payload.indexOf("</Document>") + ("</Document>".length()));
				loadMesg.setPayload(mesgDocumants.replaceAll("\n|\r", ""));
				loadMesg.setRowHistory(messgaeHistory);

			} else {
				String mesgDocumantsPay = payload.substring(payload.indexOf("<Document"), payload.indexOf("</Document>") + ("</Document>".length()));
				loadMesg.setPayload(mesgDocumantsPay.replaceAll("\n|\r", ""));
			}
		} catch (Exception e) {
			LOGGER.error("fillPayLoad  Exception >>>>>> On  message ");
			loadMesg.setPayload(payload);
		}

	}

	private void fillPayLoadPrefix(LoaderMessage loadMesg, AMP amp, String Prefix) {

		String payload = amp.getData().getStorage().getPayload();
		try {
			// TODO Auto-generated method stub
			if (payload.contains(HISTORY_TEXT_INDECETOR) || payload.contains(MESSAGE_TEXT_INDECETOR) || payload.contains(MESSAGE_RAW_TEXT_INDECETOR)) {
				String messgaeHistory = payload.substring(0, payload.indexOf("<" + Prefix + ":Document") - 1);
				String mesgDocumants = payload.substring(payload.indexOf("<" + Prefix + ":Document"), payload.indexOf("</" + Prefix + ":Document>") + (("</" + Prefix + ":Document>").length()));
				loadMesg.setPayload(mesgDocumants.replaceAll("\n|\r", ""));
				loadMesg.setRowHistory(messgaeHistory);

			} else {
				String mesgDocumantsPay = payload.substring(payload.indexOf("<" + Prefix + ":Document"), payload.indexOf("</" + Prefix + ":Document>") + (("</" + Prefix + ":Document>").length()));
				loadMesg.setPayload(mesgDocumantsPay.replaceAll("\n|\r", ""));
			}
		} catch (Exception e) {
			LOGGER.error("fillPayLoad  Exception >>>>>> On  message " + amp.getData().getStorage().getPayload());
			loadMesg.setPayload(payload);
		}

	}

	public void fillXmlText(LoaderMessage loaderMessage, XmlTextMessage mappSepaText, Rfile rfile, Mesg mesg, AMP amp) {
		List<XmlTextMessage> xmlTextMessages = new ArrayList<XmlTextMessage>();
		buildXmlMessage(xmlTextMessages, mesg, amp, rfile);
		mesg.setrXmlText(xmlTextMessages);

	}

	private void buildXmlMessage(List<XmlTextMessage> xmlTextMessages, Mesg mesg, AMP amp, Rfile rfile) {

		try {
			mappSepaText = TextSepaMapper.mappText(amp, getFilePayload(rfile), loaderMessage.getPayload(), "", 0, 0, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mappSepaText.getXmlTextPk().setCreateDate(mesg.getMesgCreaDateTime());
		xmlTextMessages.add(mappSepaText);

	}

	public FilePayload getFilePayload(Rfile rfile) {
		FilePayload filePayload = new FilePayload();
		filePayload.setAid((int) rfile.getId().getAid());
		filePayload.setSumidh((int) rfile.getId().getFileSUmidh());
		filePayload.setSumidl((int) rfile.getId().getFileSUmidl());
		return filePayload;

	}

	public static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	private void fillRintv(LoaderMessage loaderMessage) {
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Start Parsing Rintv Inst :: " + loaderMessage.getMesg().getMesgUumid());

		} else {
			LOGGER.debug("Start Parsing Rintv Inst :: " + loaderMessage.getMessageSequenceNo());
		}
		List<Rintv> lisrRIntv = new ArrayList<Rintv>();
		Rintv rintv = new Rintv();

		rintv.setId(new RintvPK());
		rintv.getId().setAid(loaderMessage.getMesg().getId().getAid());
		rintv.getId().setiNTVSUMIDH(loaderMessage.getMesg().getId().getMesgSUmidh());
		rintv.getId().setiNTVSUMIDL(loaderMessage.getMesg().getId().getMesgSUmidl());
		rintv.getId().setIntvDateTime(loaderMessage.getMesg().getMesgCreaDateTime());

		// Content
		if (loaderMessage.isMessageIsCancelled()) {
			rintv.setIntvMergedText(loaderMessage.getRowData());
		} else {
			rintv.setIntvMergedText(loaderMessage.getRowHistory());
		}
		rintv.setIntvAppeDateTime(loaderMessage.getMesg().getMesgCreaDateTime());
		// rintv.setxCreaDateTimeMesg(loaderMessage.getMesg().getMesgCreaDateTime());
		lisrRIntv.add(rintv);

		if (loaderMessage.getMesg().getRinsts().get(0) != null) {
			loaderMessage.getMesg().getRinsts().get(0).setrIntv(lisrRIntv);
		}

	}

	@Override
	public LoaderMessage call() throws Exception {
		/*
		 * Parse and return the loader message
		 */
		return parse();
	}

	public LoaderMessage getLoaderMessage() {
		return loaderMessage;
	}

	public void setLoaderMessage(LoaderMessage loaderMessage) {
		this.loaderMessage = loaderMessage;
	}

	private void fillInstTransmissionDetails(Mesg mesg, Inst inst, Appe appe) {
		inst.setInstAppeSeqNbr(appe.getAppeTransmissionNbr());
		inst.setInstAppeDateTime(appe.getId().getAppeDateTime());

		if (mesg.getMesgSubFormat().equals("INPUT")) {
			inst.setXLastEmiAppeDateTime(appe.getId().getAppeDateTime());
			inst.setXLastEmiAppeSeqNbr(appe.getAppeTransmissionNbr());
		} else {
			inst.setXLastRecAppeDateTime(appe.getId().getAppeDateTime());
			inst.setXLastRecAppeSeqNbr(appe.getAppeTransmissionNbr());
		}
	}

	private void moveToErrorQueue(String rowData) {
		moveToErrorQSender.sendMesage(rowData);
		LOGGER.info("Message Moved To Error Queue");
	}

	public JMSSender getMoveToErrorQSender() {
		return moveToErrorQSender;
	}

	public void setMoveToErrorQSender(JMSSender moveToErrorQSender) {
		this.moveToErrorQSender = moveToErrorQSender;
	}

}
