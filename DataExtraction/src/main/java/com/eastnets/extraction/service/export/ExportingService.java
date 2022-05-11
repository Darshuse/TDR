package com.eastnets.extraction.service.export;

import java.util.List;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;

public interface ExportingService {

	public abstract void exportMessages(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile);

}
