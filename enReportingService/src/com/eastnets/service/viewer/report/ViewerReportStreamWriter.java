package com.eastnets.service.viewer.report;

import java.io.IOException;
import java.io.OutputStream;

public interface ViewerReportStreamWriter {

	/**
	 * write the report to the passed output stream
	 * @param out
	 * @throws IOException
	 */
	void write(OutputStream out) throws IOException;
	
	/**
	 * get the length in bytes of the report data 
	 * @return
	 */
	public int getLength();
}
