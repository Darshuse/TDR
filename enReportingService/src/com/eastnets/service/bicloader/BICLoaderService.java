package com.eastnets.service.bicloader;

import java.util.List;
import java.util.Map;

import com.eastnets.dao.bicloader.BicFile;
import com.eastnets.service.Service;

public interface BICLoaderService extends Service {

	public List<String> readTxtFile(String fileName) throws Exception;

	public List<Map<String, String>> parsingtxtFile(List<String> textBuff);

	public List<String> parsingHeader(String textBuff);

	public List<Map<String, String>> parsingContent(List<String> textBuff, List<String> fileHeader);

	public boolean saveGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean saveBICDueCorr(List<Map<String, String>> content, BicFile bicFile) throws Exception;

	public boolean saveBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteGPICorr() throws Exception;

	public boolean saveFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteFICorr() throws Exception;

	public boolean saveCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteCTCorr() throws Exception;

	public boolean saveCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteCUCorr() throws Exception;

	public List<Map<String, String>> checkMapDuplicate(List<Map<String, String>> contents, String field);

	public boolean updateFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public void insertIntoErrorld(String errorExe, String errorLevel, String errModule, String errMsg1, String errMsg2);

	public boolean checkFileWasProcessedBefore(String filename);

	public boolean parseAndStoreFiFile(String fileName, boolean deleteOld, boolean deltaFile, BicFile bicFile);

}
