package com.eastnets.enGpiLoader.source;

import com.eastnets.enGpiLoader.utility.DataSourceParser;

public interface MessageSource {

	
	public void startSourceMonotoring(DataSourceParser dataSourceParser ) throws Exception;
}
