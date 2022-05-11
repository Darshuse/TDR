package com.eastnets.service.loader.parser;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Message;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.loader.Appe;
import com.eastnets.domain.loader.Inst;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Mesg;
import com.eastnets.domain.loader.Rfile;
import com.eastnets.domain.loader.Rintv;
import com.eastnets.domain.loader.RintvPK;
import com.eastnets.domain.loader.Text;
import com.eastnets.domain.loader.XmlTextMessage;
import com.eastnets.domain.viewer.PayloadFile;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.service.loader.exceptions.MessageParsingException;
import com.eastnets.service.loader.helper.jms.JMSSender;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.ZipInputStream; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MxMessageParser implements MessageParser {
    public enum PayloadType {NOT_SEPA,SEPA,SEPA_BLK}
	private Unmarshaller umarsh;
	String nodeName="";
	private static final Logger LOGGER = Logger.getLogger(MxMessageParser.class);
	private LoaderMessage loaderMessage;
//	private SimpleDateFormat dateF = new SimpleDateFormat("hh:mm:ss");
	
	private JMSSender moveToErrorQSender;
	private List <String> sepaXSD;
	XmlTextMessage mappSepaText=new XmlTextMessage();
	public MxMessageParser(LoaderMessage loaderMessage,Unmarshaller unmarsh,List <String> sepaXSD) {
			this.umarsh = unmarsh;		
			this.loaderMessage = loaderMessage;
			this.sepaXSD = sepaXSD;
	}

	@Override
	public LoaderMessage parse() throws Exception {
//		LOGGER.info("start parsing ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() + " @ " + dateF.format(new Date()));
		
		if (loaderMessage.getRowData() == null || loaderMessage.getRowData().length() == 0) {
			if(loaderMessage.getMessageSequenceNo() == null){
				LOGGER.error("Empty MX Message :: " + loaderMessage.getMesg().getMesgUumid());	

			}else{
				LOGGER.error("Empty MX Message :: " + loaderMessage.getMessageSequenceNo());	
			}
			
//			LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));
			throw new MessageParsingException(null,loaderMessage);
		}

		// get the MX string
		int startIngIndex = loaderMessage.getRowData().indexOf("<?xml");
		if (startIngIndex < 0) {
			if(loaderMessage.getMessageSequenceNo() == null){
				
				LOGGER.error("Invalid MX message" + loaderMessage.getMesg().getMesgUumid());		
			}else{

				LOGGER.error("Invalid MX message" + loaderMessage.getMessageSequenceNo());	
				
			}
//			LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));
			throw new MessageParsingException(null,loaderMessage);
		}
		// need for try catch to warp the exception in form of
		// MessageParsingException
		try {
			String mxStr = loaderMessage.getRowData().substring(startIngIndex);
			DataPDU dataPdu = null;
			synchronized (umarsh) {  
				@SuppressWarnings("unchecked")
				JAXBElement<DataPDU> unmarshal = (JAXBElement<DataPDU>) umarsh.unmarshal(new StreamSource(new StringReader(mxStr)));
				dataPdu = unmarshal.getValue();
			}
			List<Appe> appes = new ArrayList<Appe>();
			List<Inst> intances = new ArrayList<Inst>();
			List<XmlTextMessage> xmlTextMessages = new ArrayList<XmlTextMessage>();
			XmlTextMessage mappText=null; 
			Rfile rfile=null;
			LOGGER.debug("MX Message :: parsing Message Header");
			Mesg mesg = MesgHeaderMapper.mapMesg(dataPdu, loaderMessage);
			LOGGER.debug("MX Message :: parsing Message Instance");
			Inst mapInst = InstanceMapper.mapInst(dataPdu, loaderMessage);
			if(mesg.getMesgFrmtName().equalsIgnoreCase("MX")){
				LOGGER.debug("MX Messsage :: parsing Message Text");
				 mappText = TextMapper.mappText(dataPdu);	 
			}else if(mesg.getMesgFrmtName().equalsIgnoreCase("File")){
				rfile=fileMapper.mappFile(dataPdu);
				
			}
			
		    
			LOGGER.debug("MX Message :: parsing Message Appendix");
			Appe mappAppe = AppendixMapper.mappAppe(dataPdu, loaderMessage);

			if (mappAppe != null) {
				fillInstTransmissionDetails(mesg,mapInst,mappAppe);
				appes.add(mappAppe);
				mapInst.setRappes(appes);
			} else {
				mapInst.setRappes(null);
			}
			
			
			
			// get unit name form message header
			mapInst.setInstUnitName(mesg.getXInst0UnitName());
			intances.add(mapInst);
			mesg.setRinsts(intances);
			if(mesg.getMesgFrmtName().equalsIgnoreCase("MX")){
				xmlTextMessages.add(mappText);
				mesg.setrXmlText(xmlTextMessages);
				mesg.getrXmlText().get(0).getXmlTextPk().setCreateDate(mesg.getMesgCreaDateTime());
				
			}else if(mesg.getMesgFrmtName().equalsIgnoreCase("File")){
				mesg.setRfile(rfile);
				mesg.getRfile().setMesgCreaDateTime(mesg.getMesgCreaDateTime());
		
			}
			
			loaderMessage.setMesg(mesg);
			/*
			 * this will set aid, umidl and umidh for each object in this new
			 * message.
			 */
			loaderMessage.fillMesgKeys();
			if(mesg.getMesgFrmtName().equalsIgnoreCase("File")){
				preparePayload(loaderMessage,mappSepaText,rfile,mesg,dataPdu);	 
			}
	
			fillRintv(loaderMessage);
		} catch (Exception e) {
//			LOGGER.info("End with errors ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));
			throw new MessageParsingException(e, loaderMessage);
		}
//		LOGGER.info("End ::: MX Message ::: Message ID ::: " + loaderMessage.getMessageSequenceNo() +" @ " +dateF.format(new Date()));
	
		return loaderMessage;

	}
	
	
	public void preparePayload(LoaderMessage loaderMessage,XmlTextMessage mappSepaText,Rfile rfile,Mesg mesg,DataPDU pdu){ 
		nodeName="";
		boolean checkblkMesg = false; 
		boolean resultSepa = false; 
		List<XmlTextMessage> xmlTextMessages = new ArrayList<XmlTextMessage>();
		DocumentBuilder builder = null;  
		Document doc = null;  
		if(!isValidityPayload(loaderMessage.getPayload()))
			return ; 

		try { 
			builder = createDocumentBuilder(); 
			doc=createXmlDocument(builder,loaderMessage.getPayload()); 
			Element parentElements = (Element) doc.getFirstChild();

			switch (findPayloadType(parentElements)) {
			case SEPA: {
				resultSepa = true;
				break;
			}
			case SEPA_BLK:{
				resultSepa = true;
				checkblkMesg = true;
				break;

			}
			default:
				break; 
			} 
			if (resultSepa == true)  
				buildSepaMessage(builder,doc,nodeName,parentElements,checkblkMesg,xmlTextMessages,mesg,pdu,rfile);

		} catch (Exception e) { 
			e.printStackTrace();
		}  
		mesg.setrXmlText(xmlTextMessages); 
		mesg.getRfile().setPayloadTest(loaderMessage.getPayload()); 

	}

	
	private  PayloadType findPayloadType(Element parentElements){
		  nodeName =parentElements.getNodeName().substring(parentElements.getNodeName().indexOf(':')+1); 
		  if (sepaXSD.contains(nodeName)){
			  
			  return PayloadType.SEPA_BLK;
		  }
		  else{
			  nodeName =parentElements.getAttribute("xmlns").substring(parentElements.getAttribute("xmlns").indexOf("xsd:")+4); 
			  if (sepaXSD.contains(nodeName)){
				  
				  return PayloadType.SEPA;
			  }
			  
			  
		  }
		
		return PayloadType.NOT_SEPA;
	}
	
	
	
	private void buildSepaMessage(	DocumentBuilder builder,Document doc,String nodeName, Element parentElements,boolean checkblkMesg,List<XmlTextMessage> xmlTextMessages,Mesg mesg,DataPDU pdu,Rfile rfile) throws Exception{

		int blkflag = 0; 
		String msgType = null; 
		String msgText = null; 
		int countOfMesg = 0;  
		Document tempDoc = null; 
		//new instance of XPATH with expression
		XPath xPath =  XPathFactory.newInstance().newXPath();		

		// msgExpression to store XPATH expression
		String msgExpression = null;

		if (checkblkMesg == true)
		{
			// XPATH expression to get bulk messages from SEPA(PAYLOAD File)
			msgExpression = "/*/*[*][*]";

		}
		else{
			// XPATH expression to get non-bulk messages from SEPA(PAYLOAD File)
			msgExpression = "/*"; 
		} 
		// compile XPATH to get Messages nodes
		NodeList messages = null;

		try {
			messages = (NodeList) xPath.compile(msgExpression).evaluate(doc, XPathConstants.NODESET);

		} catch (XPathExpressionException e) {
		 
		}			

		for (int i= 0; i<messages.getLength();i++){
			Element message = (Element)messages.item(i);
			
			// prefix to get prefix node name
			String prefix = null;

			// get message identifier
			if (checkblkMesg == true){
				blkflag =1;
				msgType = message.getAttribute("xmlns").substring(message.getAttribute("xmlns").indexOf("xsd:")+4);

				prefix = parentElements.getNodeName().substring(0,parentElements.getNodeName().indexOf(':')+1);

				// create new document to put each msg with root node
				tempDoc = builder.newDocument();
				
				// get root element from doc and put it into new doc
				Node newRoot =tempDoc.appendChild(tempDoc.adoptNode(tempDoc.createElement("Document").cloneNode(false)));
				
				// put the message into doc
				newRoot.appendChild(tempDoc.adoptNode(message.cloneNode(true)));
			}
			else{
				blkflag=0;
				msgType = nodeName;

			}

			// convert document into String
			StringWriter stringWriter = new StringWriter();
			Transformer transformer = null;

			try {
				transformer = TransformerFactory.newInstance().newTransformer();
				
				if (checkblkMesg == true){
					transformer.transform(new DOMSource(tempDoc), new StreamResult(stringWriter));
				}
				else{
					transformer.transform(new DOMSource(message), new StreamResult(stringWriter));
					
				}
				
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
					continue;
			} 

			if (checkblkMesg == true){
				msgText = stringWriter.toString().replaceAll(prefix, "");
			}else{
				msgText = stringWriter.toString();
			}

			// get xml message size
			int mesgSize = msgText.getBytes().length/1024;

		     countOfMesg++;
		     
		     mappSepaText = TextSepaMapper.mappText(pdu,getFilePayload(rfile), msgText, msgType,countOfMesg,mesgSize, blkflag, messages.getLength());
		    // mappSepaText.setMsgDocument("<kassab></kassab>");
		     mappSepaText.getXmlTextPk().setCreateDate(mesg.getMesgCreaDateTime());
		     xmlTextMessages.add(mappSepaText);
			
		}
		
		
	}
	
	
	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException{
 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		 factory= DocumentBuilderFactory.newInstance();
		 return factory.newDocumentBuilder();
	
	}
	
	
	private Document createXmlDocument(DocumentBuilder builder,String payload) throws ParserConfigurationException, SAXException, IOException{
		 InputSource payloadInputStrem = new InputSource(new StringReader(payload));
		 return builder.parse(payloadInputStrem);  
	}
	
	
	private boolean isValidityPayload(String payload){
		boolean validPayload=true;
		if(payload == null ){
			validPayload=false;
			return validPayload;
		}  
		if(loaderMessage.getPayload().isEmpty()){
			validPayload=false;
			return validPayload;
		} 
       return validPayload; 
	}

	
	
	public FilePayload getFilePayload(Rfile rfile){
		FilePayload filePayload=new FilePayload();
		filePayload.setAid((int)rfile.getId().getAid()); 
		filePayload.setSumidh((int)rfile.getId().getFileSUmidh());
		filePayload.setSumidl((int)rfile.getId().getFileSUmidl());
		return filePayload;
		
	}
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	
	private void fillRintv(LoaderMessage loaderMessage){
		if(loaderMessage.getMessageSequenceNo() == null){
			LOGGER.debug("Start Parsing Rintv Inst :: " + loaderMessage.getMesg().getMesgUumid());		
			
		}else{ 
			LOGGER.debug("Start Parsing Rintv Inst :: " + loaderMessage.getMessageSequenceNo());	 
		}
		List<Rintv> lisrRIntv = new ArrayList<Rintv>();
		Rintv rintv=new Rintv();
		
		rintv.setId(new RintvPK());  
		rintv.getId().setAid(loaderMessage.getMesg().getId().getAid());
		rintv.getId().setiNTVSUMIDH(loaderMessage.getMesg().getId().getMesgSUmidh());
		rintv.getId().setiNTVSUMIDL(loaderMessage.getMesg().getId().getMesgSUmidl());
		rintv.getId().setIntvDateTime(loaderMessage.getMesg().getMesgCreaDateTime());
		
		//Content
		rintv.setIntvMergedText(loaderMessage.getRowHistory()); 
		rintv.setIntvAppeDateTime(loaderMessage.getMesg().getMesgCreaDateTime()); 
		//rintv.setxCreaDateTimeMesg(loaderMessage.getMesg().getMesgCreaDateTime());
		lisrRIntv.add(rintv);
		
		if(loaderMessage.getMesg().getRinsts().get(0) != null){
			loaderMessage.getMesg().getRinsts().get(0).setrIntv(lisrRIntv); 
		}
		
	}

	@Override
	public LoaderMessage call() throws Exception {
		/*
		 * Parse and return the loader message
		 */
		LoaderMessage loaderMessage = null;
		try{
			loaderMessage = parse();
		} catch(Exception exception){
			moveToErrorQueue(loaderMessage.getRowData());
			throw exception;// re-throw exception to maintain old execution flow
		}
		return loaderMessage;
	}

	public LoaderMessage getLoaderMessage() {
		return loaderMessage;
	}

	public void setLoaderMessage(LoaderMessage loaderMessage) {
		this.loaderMessage = loaderMessage;
	}
	
	private void fillInstTransmissionDetails(Mesg mesg, Inst inst, Appe appe){
		inst.setInstAppeSeqNbr(appe.getAppeTransmissionNbr());
		inst.setInstAppeDateTime(appe.getId().getAppeDateTime());
		
		if(mesg.getMesgSubFormat().equals("INPUT")){
			inst.setXLastEmiAppeDateTime(appe.getId().getAppeDateTime());
			inst.setXLastEmiAppeSeqNbr(appe.getAppeTransmissionNbr());
		}else{
			inst.setXLastRecAppeDateTime(appe.getId().getAppeDateTime());
			inst.setXLastRecAppeSeqNbr(appe.getAppeTransmissionNbr());
		}
	}

	private void moveToErrorQueue(String rowData){
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
