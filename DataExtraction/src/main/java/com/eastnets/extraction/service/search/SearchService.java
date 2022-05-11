package com.eastnets.extraction.service.search;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.eastnets.extraction.bean.AllianceInstance;
import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;

public interface SearchService {

	/**
	 * Search for messages on db based on certain search criteria
	 * 
	 * @param searchParam
	 * @return List<SearchResult>
	 * @throws InterruptedException
	 * @throws SQLException
	 */

	public List<SearchResult> search(SearchParam searchParam) throws InterruptedException, SQLException;

	public List<Map<String, Object>> searchMode4(SearchParam searchParam) throws InterruptedException, SQLException;

	public void flagExtractedMessages(SearchParam searchParam, List<String> allAid, List<String> allUmidl, List<String> allUmidh);

	public List<AllianceInstance> CashAllianceInstanceByAid();

}
