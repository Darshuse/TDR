package com.eastnets.service.dataAnalyzer;

import java.util.List;
import java.util.Map;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.dataAnalyzer.DataAnalyzerDAO;
import com.eastnets.domain.Config;
import com.eastnets.domain.dataAnalyzer.ResourceLookup;
import com.eastnets.domain.dataAnalyzer.Resources;
import com.eastnets.service.ServiceBaseImp;

public class DataAnalyzerServiceImp extends ServiceBaseImp implements DataAnalyzerService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215648612962202425L;
	private DataAnalyzerDAO dataAnalyzerDAO;
	private CommonDAO commonDAO;
	private Config config;
	private UrlServiceBuilder urlServiceBuilder;

	@Override
	public List<ResourceLookup> getResourceFromJasperFolder(String url) {
		Resources resources = null;

		return null;
	}

	@Override
	public String buildApiUrl(String serverUrl, String restServiceType, Map<String, String> urlParam) {
		return urlServiceBuilder.buildRestApiUrl(serverUrl, restServiceType, urlParam);
	}

	@Override
	public String getTokenKey(String organizationName, String username, String roles, String param) {

		return "";
	}

	@Override
	public String getBiDefaultRepository() {
		// TODO Auto-generated method stub
		return dataAnalyzerDAO.getBiDefaultRepository();
	}

	public CommonDAO getCommonDAO() {
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO) {
		this.commonDAO = commonDAO;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public DataAnalyzerDAO getDataAnalyzerDAO() {
		return dataAnalyzerDAO;
	}

	public void setDataAnalyzerDAO(DataAnalyzerDAO dataAnalyzerDAO) {
		this.dataAnalyzerDAO = dataAnalyzerDAO;
	}

	public UrlServiceBuilder getUrlServiceBuilder() {
		return urlServiceBuilder;
	}

	public void setUrlServiceBuilder(UrlServiceBuilder urlServiceBuilder) {
		this.urlServiceBuilder = urlServiceBuilder;
	}

	@Override
	public List<ResourceLookup> getResourcesTree(String folderName) {
		// TODO Auto-generated method stub
		return dataAnalyzerDAO.getResourcesTree(folderName);
	}
}
