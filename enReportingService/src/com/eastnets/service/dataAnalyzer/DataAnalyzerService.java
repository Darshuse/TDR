
package com.eastnets.service.dataAnalyzer;

import java.util.List;
import java.util.Map;

import com.eastnets.domain.dataAnalyzer.ResourceLookup;
import com.eastnets.service.Service;

public interface DataAnalyzerService extends Service {

	public List<ResourceLookup> getResourceFromJasperFolder(String url);

	public String buildApiUrl(String url, String restServiceType, Map<String, String> urlParam);

	public String getTokenKey(String organizationName, String username, String roles, String param);

	public String getBiDefaultRepository();

	public List<ResourceLookup> getResourcesTree(String folderName);

}
