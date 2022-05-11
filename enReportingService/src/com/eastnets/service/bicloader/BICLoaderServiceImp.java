package com.eastnets.service.bicloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.eastnets.dao.bicloader.BICLoaderDAO;
import com.eastnets.dao.bicloader.BicFile;
import com.eastnets.service.ServiceBaseImp;

public class BICLoaderServiceImp extends ServiceBaseImp implements BICLoaderService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient BICLoaderDAO bicLoaderDAO;
	private static final Logger LOGGER = Logger.getLogger("BICLoader");
	// Getter & Setter

	public BICLoaderDAO getBicLoaderDAO() {
		return bicLoaderDAO;
	}

	public void setBicLoaderDAO(BICLoaderDAO bicLoaderDAO) {
		this.bicLoaderDAO = bicLoaderDAO;
	}

	@Override
	public List<String> readTxtFile(String fileName) throws Exception {

		List<String> strBuffer = null;
		try {
			strBuffer = IOUtils.readLines(new FileInputStream(new File(fileName)), StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return strBuffer;
	}

	@Override
	public boolean parseAndStoreFiFile(String file, boolean deleteOld, boolean deltaFile, BicFile bicFile) {

		boolean returnValue = false;
		boolean deleted = false;

		/*
		 * Try with Resources is best practice, it will close all open resources
		 * when it completes or when an exception is thrown ( No need for
		 * Finally )
		 * 
		 * Also The use of BuffereReader is recommended but can be replaced with
		 * Scanner. The UTF8 is the standard of SWIFTs files.
		 */

		try (BufferedReader bufferReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(file)), StandardCharsets.UTF_8))) {

			// SWIFT separates columns by \t in their BIC, CU and CT files.
			String delims = "\t";

			// Read Header Line and Split it into Array of Strings.
			String headerLine = bufferReader.readLine();
			String[] headers = headerLine.split(delims);

			// Each line in the file will be stored initially in this variable.
			String contentLine = null;

			// This Map will contain every header and it correspondent value in
			// each line.
			Map<String, String> lineContentHeaderMap;

			// This List will contain all Maps of the file. ( A Map is a line
			// with the header columns as keys and it's columns as values ).
			List<Map<String, String>> lines = new ArrayList<>();

			// This Counter is only for Logging.
			int linesCounter = 1;

			// Looping through the files ( Starting from the second line as the
			// first is always the Header. ( Which values will be used as Maps
			// Keys
			while ((contentLine = bufferReader.readLine()) != null) {
				returnValue = false;
				// Parse the content of the line and return a Map<Header column,
				// Line Value>
				lineContentHeaderMap = parseContent(contentLine, headers);

				// Add the parsed Map to the List of maps
				lines.add(lineContentHeaderMap);

				// As we already have one entry in the list and the user decided
				// to delete all the content of rCorr first, We will clear rCorr
				// before Storing the maps. And You can't delete if you are
				// parsing a delta file (Update)

				if (deleteOld && !deltaFile && !deleted) {
					LOGGER.debug("<parseAndStoreFiFile> : delete an old data from the database");
					returnValue = deleteFICorr();

					if (!returnValue) {
						throw new Exception("Failed to delete old data from the database");
					}

					deleted = true;
				}

				// Process every 1000 Lines together, then clear the list and
				// repeat the process to the remaining lines.

				if (lines.size() >= 1000) {
					LOGGER.debug("<processFICorr> : Storing UNIQUE BICs from lines #" + linesCounter + " to Line #"
							+ (linesCounter + (lines.size() - 1)) + "  of the parsed file: " + bicFile.getFilename());

					linesCounter += lines.size() - 1;
					returnValue = storeFIFile(lines, deltaFile, bicFile);

					lines = new ArrayList<>();

					if (!returnValue) {
						throw new Exception("Failed to store data into database");
					}

				}

			}
			// Store remaining lines. for example: if there are 12439 lines.
			// When the while loop ends there will be 439 lines not stored yet.
			if (!lines.isEmpty()) {
				LOGGER.debug("<processFICorr> : Storing UNIQUE BICs from lines #" + linesCounter + " to Line #"
						+ (linesCounter + (lines.size() - 1)) + "  of the parsed file: " + bicFile.getFilename());
				returnValue = storeFIFile(lines, deltaFile, bicFile);

				lines = new ArrayList<>();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}

	private boolean storeFIFile(List<Map<String, String>> lines, boolean deltaFile, BicFile bicFile) throws Exception {
		boolean retrunValue = false;

		if (bicFile.getFileType().equalsIgnoreCase("BankFile")) {

			if (deltaFile) {
				LOGGER.debug("<parseAndStoreFiFile> : Updating BankFile");
				retrunValue = updateFICorr(lines, bicFile);
			} else {
				LOGGER.debug("<parseAndStoreFiFile> : Storing BankFile");
				retrunValue = saveFICorr(lines, bicFile);
			}

		} else if (bicFile.getFileType().equalsIgnoreCase("BICDUE")) {
			if (deltaFile) {
				LOGGER.debug("<parseAndStoreFiFile> : Updating BICDUE");
				retrunValue = updateBICDueCorr(lines, bicFile);
			} else {
				LOGGER.debug("<parseAndStoreFiFile> : Storing BICDUE");
				retrunValue = saveBICDueCorr(lines, bicFile);
			}

		} else if (bicFile.getFileType().equalsIgnoreCase("BankDirectoryPlus")) {
			if (deltaFile) {
				LOGGER.debug("<parseAndStoreFiFile> : Updating BankDirectoryPlus");
				retrunValue = updateBankDirectoryCorr(lines, bicFile);
			} else {
				LOGGER.debug("<parseAndStoreFiFile> : Storing BankDirectoryPlus");
				retrunValue = saveBankDirectoryCorr(lines, bicFile);
			}
		}

		if (!retrunValue) {
			throw new Exception("Failed to store data into database");
		}
		return retrunValue;
	}

	public Map<String, String> parseContent(String contentLine, String[] fileHeader) {

		String delims = "\t";
		Map<String, String> line;

		String[] splitedBuff = contentLine.split(delims);

		if (splitedBuff.length <= 0)
			return null;

		line = new HashMap<>();

		for (int j = 0; j < splitedBuff.length; j++)
			line.put(fileHeader[j], splitedBuff[j]);

		return line;
	}

	@Override
	public List<Map<String, String>> parsingContent(List<String> textBuff, List<String> fileHeader) {

		String delims = "\t";
		List<Map<String, String>> lines = new ArrayList<>();
		Map<String, String> line;

		for (int i = 1; i < textBuff.size(); i++) {
			String[] splitedBuff = textBuff.get(i).split(delims);

			if (splitedBuff.length <= 0)
				return null;

			line = new HashMap<>();

			for (int j = 0; j < splitedBuff.length; j++)
				line.put(fileHeader.get(j), splitedBuff[j]);

			lines.add(line);
		}

		return lines;
	}

	@Override
	public List<Map<String, String>> parsingtxtFile(List<String> textBuff) {

		List<String> parsaedHeader = null;
		List<Map<String, String>> parsedContent = null;

		parsaedHeader = parsingHeader(textBuff.get(0));
		if (parsaedHeader == null)
			return null;

		parsedContent = parsingContent(textBuff, parsaedHeader);
		if (parsedContent == null)
			return null;

		return parsedContent;
	}

	@Override
	public List<String> parsingHeader(String textBuff) {

		String delims = "\t";
		List<String> header = new ArrayList<>();

		String[] splitedBuff = textBuff.split(delims);

		if (splitedBuff.length <= 0)
			return null;

		for (int i = 0; i < splitedBuff.length; i++)
			header.add(splitedBuff[i]);

		return header;

	}

	@Override
	public boolean saveGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveGPICorr(contents, bicFile);
	}

	@Override
	public boolean updateGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		return bicLoaderDAO.updateGPICorr(contents, bicFile);
	}

	@Override
	public boolean saveFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveFICorr(contents, bicFile);
	}

	@Override
	public boolean saveCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveCTCorr(contents, bicFile);
	}

	@Override
	public boolean saveCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveCUCorr(contents, bicFile);
	}

	@Override
	public List<Map<String, String>> checkMapDuplicate(List<Map<String, String>> contents, final String field) {

		ArrayList<Map<String, String>> uniqueContents = new ArrayList<Map<String, String>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3944985225107663174L;

			@Override
			public boolean contains(Object obj) {

				@SuppressWarnings("unchecked")
				Map<String, String> content = (Map<String, String>) obj;
				for (Map<String, String> map : this) {
					if (map.containsValue(String.valueOf(content.get(field))))
						return true;
				}

				return false;
			}
		};

		for (Map<String, String> content : contents) {
			if (!(uniqueContents.contains(content)))
				uniqueContents.add(content);
		}

		return uniqueContents;
	}

	@Override
	public boolean deleteFICorr() throws Exception {
		return bicLoaderDAO.deleteFICorr();
	}

	@Override
	public boolean deleteCTCorr() throws Exception {
		return bicLoaderDAO.deleteCTCorr();
	}

	@Override
	public boolean deleteCUCorr() throws Exception {
		return bicLoaderDAO.deleteCUCorr();
	}

	@Override
	public boolean deleteGPICorr() throws Exception {
		return bicLoaderDAO.deleteGPICorr();
	}

	@Override
	public boolean saveBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveBICDueCorr(contents, bicFile);
	}

	@Override
	public boolean saveBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.saveBankDirectoryCorr(contents, bicFile);
	}

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1,
			String errMsg2) {

		bicLoaderDAO.insertIntoErrorld(errorExe, errorLevel, errorModule, errMsg1, errMsg2);
	}

	@Override
	public boolean updateFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.updateFICorr(contents, bicFile);
	}

	@Override
	public boolean updateCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.updateCTCorr(contents, bicFile);

	}

	@Override
	public boolean updateCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.updateCUCorr(contents, bicFile);

	}

	@Override
	public boolean updateBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.updateBICDueCorr(contents, bicFile);
	}

	@Override
	public boolean updateBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		return bicLoaderDAO.updateBankDirectoryCorr(contents, bicFile);
	}

	@Override
	public boolean checkFileWasProcessedBefore(String filename) {
		return bicLoaderDAO.checkFileToBeProcessed(filename);
	}
}
