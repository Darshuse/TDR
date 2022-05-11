package com.eastnets.service.filepayload;

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

import com.eastnets.dao.filepayload.FilePayloadDAO;
import com.eastnets.domain.filepayload.FilePayload;

public class ParsingFileBean extends ParsingFile{


	private FilePayloadDAO filePayloadDAO;
	

	public FilePayloadDAO getFilePayloadDAO() {
		return filePayloadDAO;
	}
	public void setFilePayloadDAO(FilePayloadDAO filePayloadDAO) {
		this.filePayloadDAO = filePayloadDAO;
	}
	
	@Override
	public boolean parsingFile(File xmlFile, List <String> sepaXSD ,FilePayload payload, List <String> filesEXT) {


		// TODO Auto-generated method stub

		// checkblkMesg to check bulk mesg or not
		boolean checkblkMesg = false;

		// resultSepa to detect sepa mesg or not
		boolean resultSepa = false;

		// blkflag to store nlk_flag in rxmltext table
		int blkflag = 0;

		// msgType to store mesg type / mesg identifier
		String msgType = null;

		// msgText to store message xml text 
		String msgText = null;

		// countOfMesg to sotre number of message in file
		int countOfMesg = 0;

		// flag to check if xml or not
		boolean xmlFileType = false;
		
		// temp document in case of bulk message
		Document tempDoc = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		//	parsing xml file in
		Document doc = null;
		
		// in case of zip file
		InputStream fileStream = null;
		ZipInputStream zipFile = null;
		
		int daoResult = 0;
		
		boolean isCompreseed =false;
		
		try {
			
			
			builder = factory.newDocumentBuilder();
			isCompreseed = isFileCompressed(xmlFile, payload);
			//to check the file is compressed or not
			if (isCompreseed == true){
				
				fileStream = new FileInputStream (xmlFile){
					@Override
					public void close() throws IOException {
						//System.out.println("close");
						//super.close();
					}
				};

				zipFile = new ZipInputStream(fileStream){
					@Override
					public void close() throws IOException {
						//System.out.println("close");
						//super.close();
					}
				};
				
				// extract the zip file and get the file from zip file
				zipFile.getNextEntry();
				InputStream sepaStream = zipFile;

				// parsing xml file in
				doc = builder.parse(sepaStream);
			}
			else{
				doc = builder.parse(xmlFile);	
			}
			
			xmlFileType = true;

		} catch (Exception e ) {
			// TODO Auto-generated catch block
			System.out.println(getDateStr() + "NOT XML File, For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
		}	
		
		if (xmlFileType == true){

			// to check types of message with  XML Types Table

			// get first node (root) of xml content
			Element parentElements = (Element) doc.getFirstChild();

			// store the xml type in nodeName
			String nodeName =parentElements.getNodeName().substring(parentElements.getNodeName().indexOf(':')+1);

			// check blk mesg
			if (sepaXSD.contains(nodeName)){
				resultSepa = true;
				checkblkMesg = true;
			}else{
				nodeName =parentElements.getAttribute("xmlns").substring(parentElements.getAttribute("xmlns").indexOf("xsd:")+4);

				// check non blk mesg
				if (sepaXSD.contains(nodeName)){
					resultSepa = true;
				}
			}

			if (resultSepa == true){

				System.out.println(getDateStr() + "Detect SEPA Message, For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh()); 

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
					// TODO Auto-generated catch block
					System.out.println(getDateStr() +e.getMessage()+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
					return false;
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
						System.out.println(getDateStr() + e.getMessage()+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
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
					 
					daoResult= filePayloadDAO.updateSEPAText( payload, msgText, msgType,countOfMesg,mesgSize, blkflag, messages.getLength());
					
					if (daoResult == 1){
						System.out.println(getDateStr() +"SEPA Message Inserted Success with Result is Equal : "+ "<"+daoResult+"> "+" <Message Identifier:> "+msgType+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh() );
					}else{
						System.out.println(getDateStr() +"Failed Insert SEPA Message with Result is Equal : "+ "<"+daoResult+"> "+" <Message Identifier:> "+msgType+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
						return false;
					}
					
				}

			}else{
				// not sepa message update the text into rFile table	
			}	
		}
		
		InputStream fileInputStream = null;
		// update in rFile table
		if (isCompreseed == true){
			
			try {
				fileInputStream = new FileInputStream(xmlFile);
				// insert into blob as binary
				filePayloadDAO.updateFilePayload( payload, fileInputStream);
				
				// insert into clob - content of file
				zipFile = new ZipInputStream(fileInputStream);
				zipFile.getNextEntry();
				InputStream archiveInputStream =zipFile; 
				filePayloadDAO.updateFilePayloadText( payload, archiveInputStream);

				fileStream.close();
				zipFile.close();
				archiveInputStream.close();
				fileInputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(getDateStr() +e.getMessage()+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
			}			

			
		}else{
			try {
				
				String extension = payload.getFileName().substring(payload.getFileName().lastIndexOf(".") + 1, payload.getFileName().length());
			
				fileInputStream = new FileInputStream(xmlFile);
				if (filesEXT.contains(extension)){
					
					// insert into clob - content of file
					filePayloadDAO.updateFilePayloadText( payload, fileInputStream);
					
				}else{
					// insert into blob as binary
					filePayloadDAO.updateFilePayload( payload, fileInputStream);
				}
				fileInputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(getDateStr() +e.getMessage()+" <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
			}
		}		
		return true;
	}

	
}


