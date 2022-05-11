package com.eastnets.extraction.service.statistic;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.StatisticsFile;

@Service
public interface StatisticalService {

	public abstract void generateStatisticalFile(StatisticsFile statisticsFile, SearchParam searchParam);

	// There will be more...

}
