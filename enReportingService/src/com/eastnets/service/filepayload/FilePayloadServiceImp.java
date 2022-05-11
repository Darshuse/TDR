/**
 * Copyright (c) 2013 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.filepayload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.dao.DataAccessException;

import com.eastnets.dao.filepayload.FilePayloadDAO;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.filepayload.FilePayloadGlobalSettings;
import com.eastnets.domain.filepayload.FilePayloadSettings;
import com.eastnets.service.ServiceBaseImp;

/**
 * FilePayload Service Implementation
 * @author EastNets
 * @since September 08, 2013
 */
public class FilePayloadServiceImp extends ServiceBaseImp implements FilePayloadService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9121515130539643571L;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	private FilePayloadDAO filePayloadDAO;
	private List <String> sepaXSD;
	private List <String> txtFileExt;
	ParsingFileBean parsingfile;
		
	
	
	

	
	
	public List<String> getTxtFileExt() {
		return txtFileExt;
	}

	public void setTxtFileExt(List<String> txtFileExt) {
		this.txtFileExt = txtFileExt;
	}

	public ParsingFileBean getParsingfile() {
		return parsingfile;
	}

	public void setParsingfile(ParsingFileBean parsingfile) {
		this.parsingfile = parsingfile;
	}

	public List<String> getXsdSchema() {
		return sepaXSD;
	}

	public void setXsdSchema(List<String> xsdSchema) {
		this.sepaXSD = xsdSchema;
	}

	public FilePayloadDAO getFilePayloadDAO() {
		return filePayloadDAO;
	}

	public void setFilePayloadDAO( FilePayloadDAO payloadDAO ) {
		this.filePayloadDAO = payloadDAO;
	}


	@Override
	public List<FilePayload> getFilesToRetrieve( String loggedInUser, int aid, int maxTryCount, int delayMinutes, int chunkCount ) {
		List<FilePayload> payloads = filePayloadDAO.getFilesToRetrieve( aid, maxTryCount, delayMinutes, chunkCount);
		try {
			if ( payloads != null && payloads.size() != 0  ){
				filePayloadDAO.updateTrials(payloads );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payloads;
	}
	
	@Override
	public int updateSEPAText(FilePayload filePayload, String mesgText, String mesgId, int mesgOrder, int mesgSize,
			int blkFlag, int numMesgs) {
		return filePayloadDAO.updateSEPAText(filePayload, mesgText, mesgId, mesgOrder, mesgSize, blkFlag, numMesgs);
		
	}

	@Override
	public void updateFilesPayload( String loggedInUser, List<FilePayload> filesPayload, FilePayloadGlobalSettings globalSettings ) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean result;

		for (FilePayload payload :  filesPayload){
			try{

				//get the input stream from the file
				File fileAct = new File(payload.getPayloadFilePath());
				result= parsingfile.parsingFile(fileAct, sepaXSD, payload, txtFileExt);
				
				if (result == true){
					System.out.println(getDateStr() + "Parsing and insert the file successfully , For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
					
				}else{
					System.out.println(getDateStr() + "Failed to parse and insert the file, For <File Name:> "+payload.getFileName()+" and <UMIDl:> "+ payload.getSumidl()+" and <UMIDh:> "+payload.getSumidh());
				}
								

			}catch( Exception e ){
				sb.append( String.format("Unable to update Payload for message( %d, %d, %d )=>%s\r\n", payload.getAid(), payload.getSumidl(), payload.getSumidh(), e.getMessage()  ) );
			}
		}
		if ( sb.length() != 0 ){
			//System.err.println(sb.toString());
			//throw new Exception( sb.toString() );
		}
	}
	
	protected static String getDateStr() {
		return dateFormat.format(new Date());
	}

	@Override
	public void updateTrials( String loggedInUser, List<FilePayload> filePayloads ) throws DataAccessException {
		filePayloadDAO.updateTrials(filePayloads);		
	}

	@Override
	public FilePayloadGlobalSettings getGlobalSettings( String loggedInUser ) {
		
		// cashing XSD Identifier 
		sepaXSD =  filePayloadDAO.getXSDIdentifier();
		
		
		FilePayloadGlobalSettings globalSettings= filePayloadDAO.getGlobalSettings();
		if ( globalSettings.getPayloadTransferePath() == null  ){
			globalSettings.setPayloadTransferePath("");
		}

		String value = filePayloadDAO.getReportingProperty("ExtractZipFiles");
		boolean valid = false;
		if ( value != null ){
			valid = "1".equalsIgnoreCase(value.trim());
		}
		globalSettings.setExtractZip(valid);

		value = filePayloadDAO.getReportingProperty("PayloadClientVerbose");
		valid = false;
		if ( value != null ){
			valid = "1".equalsIgnoreCase(value.trim());
		}
		globalSettings.setVerbose(valid);

		// cashing files extension
		List<String> extensionRowList= filePayloadDAO.getTextFileExtensions();
		String allExtensions= "";
		for ( String extRow : extensionRowList ){
			extRow= extRow.trim();
			if ( extRow.startsWith(",") ){
				extRow= extRow.substring(1);
			}
			if ( extRow.endsWith(",") ){
				extRow= extRow.substring(0, extRow.length() -1 );
			}
			if ( extRow.isEmpty() ){
				continue;
			}
			if ( !allExtensions.isEmpty() ){
				allExtensions+= ",";
			}
			allExtensions+= extRow;
		}
		
		String[] exts= allExtensions.split(",");
		txtFileExt= new  ArrayList<String> ();
		for ( String ext : exts ){
			txtFileExt.add( ext.trim().toLowerCase() );
		}
		
		return globalSettings;
	}

	@Override
	public FilePayloadSettings getConnectionSettings(String loggedInUser, int aid) {
		FilePayloadSettings settings= filePayloadDAO.getConnectionSettings( aid );
		if ( settings == null ){
			return  null;
		}
		if ( settings.getLocalPath() == null  ){
			settings.setLocalPath("");
		}
		if ( settings.getRemotePath() == null  ){
			settings.setRemotePath("");
		}
		return settings;
	}


	@Override
	public void updateStatus(String loggedInUser, List<FilePayload> payloads, List<String> results) {
		if ( payloads == null  ){
			return;
		}

		for ( int index = 0; index <  payloads.size() ; ++index ){
			FilePayload payload=  payloads.get(index);

			String status = null;//default value
			if ( results.size() > index ){
				status= results.get(index);
			}
			filePayloadDAO.updateStatus(payload, status, 0);
		}
	}

	@Override
	public void resetRequestedFlag(String loggedInUser, int aid) {
		filePayloadDAO.resetRequestedFlag(aid);		
	}

	@Override
	public void resetRequested(String loggedInUser, int aid , double requestedResetTimeHours) {
		filePayloadDAO.resetRequested( aid, requestedResetTimeHours );		
	}



	private InputStream getInputStreamFromZip(FilePayload payload, FilePayloadGlobalSettings globalSettings) {
		//make sure that the input is correct
		if ( payload == null || globalSettings== null || !globalSettings.isExtractZip() ){
			return null;
		}

		if ( globalSettings.isVerbose() ){
			System.out.println("\t\t\t-Trying to extract \"" + payload.getFileName() + "\"" );
		}

		// start checking the zip file ( if it is a zip file )
		ZipFileInfo zipResult = checkZip(payload.getPayloadFilePath());
		ZipFileInfo gzResult = zipResult;
		if (zipResult.getStatus() != ZipFileInfo.Status.VALID) {
			gzResult = checkGZip(payload.getPayloadFilePath());

		}

		if (zipResult.getStatus() != ZipFileInfo.Status.VALID && gzResult.getStatus() != ZipFileInfo.Status.VALID) {
			if (globalSettings.isVerbose()) {
				if (zipResult.getStatus() == ZipFileInfo.Status.INVALID) {
					zipResult = gzResult;
				}
				if (zipResult.getStatus() == ZipFileInfo.Status.OVERFLOW) {
					System.out.println("\t\t\t-More than one entry found in the archive.");
				} else {
					System.out.println("\t\t\t-Not archive.");
				}
				return null;
			}
		}


		if ( globalSettings.isVerbose() ){
			System.out.println("\t\t\t-Valid archive.");
		}
		if ( zipResult.getInputStream() != null ){
			return zipResult.getInputStream();
		}
		return gzResult.getInputStream();
	}

	private ZipFileInfo checkGZip(String payloadFilePath) {
		ZipFileInfo info = new ZipFileInfo();
		GZIPInputStream zipInputStream= null;
		try {
			// open the zip file
			zipInputStream = new GZIPInputStream(new FileInputStream(payloadFilePath));
			// get the contents of the zip file
			if ( zipInputStream.available() == 0 ){
				zipInputStream.close();
				info.setStatus(ZipFileInfo.Status.EMPTY);
				return info;
			}

			info.setInputStream(zipInputStream);
			info.setStatus(ZipFileInfo.Status.VALID);
			return info;
		} catch (Exception e) {
			if ( zipInputStream != null ){
				try {
					zipInputStream.close();
				} catch (IOException e1) {
				}
			}
		}
		info.setStatus(ZipFileInfo.Status.INVALID);
		return info;
	}

	private ZipFileInfo checkZip( String payloadFilePath) {

		ZipFileInfo info = new ZipFileInfo();
		try {
			// open the zip file
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(payloadFilePath));
			// get the contents of the zip file

			ZipEntry zipEntry = zipInputStream.getNextEntry();

			// the zip file is empty
			if (zipEntry == null) {
				zipInputStream.close();
				info.setStatus(ZipFileInfo.Status.EMPTY);
				return info;
			}
			info.setFilename(zipEntry.getName());

			// the zip file has more than one file in it
			zipEntry = zipInputStream.getNextEntry();
			if (zipEntry != null) {
				zipInputStream.close();
				info.setStatus(ZipFileInfo.Status.OVERFLOW);
				return info;
			}
			zipInputStream.close();
			info.setStatus(ZipFileInfo.Status.VALID);

			//get the stream and make it point to the entry inside 
			zipInputStream = new ZipInputStream(new FileInputStream(payloadFilePath));
			zipInputStream.getNextEntry();
			info.setInputStream(zipInputStream);
			return info;
		} catch (Exception e) {
		}
		info.setStatus(ZipFileInfo.Status.INVALID);
		return info;
	}

}

class ZipFileInfo{
	enum Status{VALID, INVALID, EMPTY, OVERFLOW};

	private Status status = Status.VALID;
	private String filename= null;
	private InputStream inputStream;

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
