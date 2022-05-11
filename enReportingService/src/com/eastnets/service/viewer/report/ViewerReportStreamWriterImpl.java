package com.eastnets.service.viewer.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ViewerReportStreamWriterImpl implements ViewerReportStreamWriter{
	ByteArrayOutputStream baos;
	public ViewerReportStreamWriterImpl(ByteArrayOutputStream baos) {
		this.baos = baos;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		if ( baos != null  && out != null){
			out.write(baos.toByteArray());
		}
	}

	@Override
	public int getLength(){
		if ( baos == null ){
			return 0;
		}
		return baos.size();
	}


}
