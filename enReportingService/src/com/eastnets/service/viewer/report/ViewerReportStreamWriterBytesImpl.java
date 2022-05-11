package com.eastnets.service.viewer.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ViewerReportStreamWriterBytesImpl implements ViewerReportStreamWriter {

	ByteArrayOutputStream byteArrayOutputStream;
	
	public ViewerReportStreamWriterBytesImpl(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		if ( byteArrayOutputStream != null  && out != null){
			out.write(byteArrayOutputStream.toByteArray());
		}
	}

	@Override
	public int getLength() {
		return byteArrayOutputStream.size();
	}

}
