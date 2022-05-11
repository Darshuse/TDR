package com.eastnets.dao.bicloader;

import java.util.List;
import java.util.Map;

import com.eastnets.dao.DAO;

public interface BICLoaderDAO extends DAO {

	public boolean saveGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean saveBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean saveBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteGPICorr() throws Exception;

	public boolean saveFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteFICorr() throws Exception;

	public boolean saveCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteCTCorr() throws Exception;

	public boolean saveCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean deleteCUCorr() throws Exception;

	public boolean updateGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean updateBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception;

	public boolean checkFileToBeProcessed(String filename);

	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1,
			String errMsg2);

	public void updateldBicsHistory(BicFile bicFile, String tablename);

}
