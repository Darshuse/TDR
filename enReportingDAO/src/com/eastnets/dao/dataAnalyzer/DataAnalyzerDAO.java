
package com.eastnets.dao.dataAnalyzer;

import java.util.List;

import com.eastnets.domain.dataAnalyzer.ResourceLookup;

public interface DataAnalyzerDAO {
	public String getBiDefaultRepository();

	public List<ResourceLookup> getResourcesTree(String folderName);
}
