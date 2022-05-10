package com.eastnets.enGpiNotifer.source;

import com.eastnets.enGpiNotifer.utility.DataSourceParser;

public interface MessageSource { 
	
	public void startSourceMonotoring(DataSourceParser dataSourceParser ) throws Exception;
}
