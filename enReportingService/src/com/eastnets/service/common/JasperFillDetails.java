package com.eastnets.service.common;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;

public class JasperFillDetails {

	private JasperReportAsyncFillHandler jasperReportAsyncFillHandler;
	private JasperReportAsynchListener fillListener;
	private JasperReport jasperReport;
	private JRAbstractLRUVirtualizer virtualizer = null;
	private String fillHandlerName;

	public JasperReportAsyncFillHandler getJasperReportAsyncFillHandler() {
		return jasperReportAsyncFillHandler;
	}

	public void setJasperReportAsyncFillHandler(JasperReportAsyncFillHandler jasperReportAsyncFillHandler) {
		this.jasperReportAsyncFillHandler = jasperReportAsyncFillHandler;
	}

	public JasperReportAsynchListener getFillListener() {
		return fillListener;
	}

	public void setFillListener(JasperReportAsynchListener fillListener) {
		this.fillListener = fillListener;
	}

	public JasperReport getJasperReport() {
		return jasperReport;
	}

	public void setJasperReport(JasperReport jasperReport) {
		this.jasperReport = jasperReport;
	}

	public JRAbstractLRUVirtualizer getVirtualizer() {
		return virtualizer;
	}

	public void setVirtualizer(JRAbstractLRUVirtualizer virtualizer) {
		this.virtualizer = virtualizer;
	}

	public String getFillHandlerName() {
		return fillHandlerName;
	}

	public void setFillHandlerName(String fillHandlerName) {
		this.fillHandlerName = fillHandlerName;
	}

}
