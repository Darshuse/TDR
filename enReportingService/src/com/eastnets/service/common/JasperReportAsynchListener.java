package com.eastnets.service.common;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import com.eastnets.service.common.ReportService.ReportFillingStatus;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;

public class JasperReportAsynchListener implements AsynchronousFilllListener {

	private String reportExtension;
	private String excelVersion = "2010";
	private ByteArrayOutputStream byteOutStream;
	private ReportFillingStatus fillingStatus;
	private JRAbstractLRUVirtualizer virtualizer;
	private String errorLog;
	static Logger log = Logger.getLogger(JasperReportAsynchListener.class.getName());

	public JasperReportAsynchListener() {
		byteOutStream = new ByteArrayOutputStream();
		this.fillingStatus = ReportFillingStatus.FILLING;
	}

	@Override
	public void reportFinished(JasperPrint jsaperprint) {
		/*
		 * Need to catch any runtime exception here because the report may be finished at the same time when the user request to cancel the report generation; so may raise interrupted exception
		 */
		try {
			printReport(reportExtension, jsaperprint);
			this.fillingStatus = ReportFillingStatus.FINISHED;
			if (virtualizer != null) {
				virtualizer.cleanup();
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void reportFillError(Throwable arg0) {
		this.fillingStatus = ReportFillingStatus.ERROR;
		errorLog = arg0.getMessage();
		log.error(" ::Error in Generating Report ::" + arg0.getMessage());
		if (virtualizer != null) {
			virtualizer.cleanup();
		}
	}

	@Override
	public void reportCancelled() {
		this.fillingStatus = ReportFillingStatus.CANCELLED;
		System.out.println(" filling cancelled....");
		if (virtualizer != null) {
			virtualizer.cleanup();
		}
	}

	private ByteArrayOutputStream printReport(String reportExtension, JasperPrint jasperPrint) {
		try {

			if (reportExtension.equalsIgnoreCase(ReportService.ReportTypes.pdf.name())) {
				JRPdfExporter exporterpdf = new JRPdfExporter();
				exporterpdf.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporterpdf.setParameter(JRExporterParameter.OUTPUT_STREAM, byteOutStream);

				exporterpdf.exportReport();
			}
			if (reportExtension.equalsIgnoreCase(ReportService.ReportTypes.xls.name())) {

				if (excelVersion.equalsIgnoreCase("2010")) {
					JRXlsxExporter exporterxls = new JRXlsxExporter();// 2010 excel rows to have MORE than 1,400,000 rows
					// JRXlsExporter exporterxls=new JRXlsExporter();// 2007 excel rows to have UNDER 65,000 rows
					// JExcelApiExporter exporterxls = new JExcelApiExporter(); //old object
					exporterxls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporterxls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteOutStream);
					exporterxls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporterxls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporterxls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporterxls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporterxls.exportReport();
				}
				// else {
				// JExcelApiExporter exporterxls = new JExcelApiExporter();
				// exporterxls.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				// exporterxls.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteOutStream);
				// exporterxls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				// exporterxls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				// exporterxls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				// exporterxls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				// exporterxls.exportReport();
				// }
			}
			if (reportExtension.equalsIgnoreCase(ReportService.ReportTypes.docx.name()) || reportExtension.equalsIgnoreCase("doc")) {
				JRDocxExporter exporterdocx = new JRDocxExporter();
				exporterdocx.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporterdocx.setParameter(JRExporterParameter.OUTPUT_STREAM, byteOutStream);
				exporterdocx.exportReport();
			}

		} catch (Exception ex) {
			byteOutStream = null;
		}
		return byteOutStream;
	}

	public String getReportExtension() {
		return reportExtension;
	}

	public void setReportExtension(String reportExtension) {
		this.reportExtension = reportExtension;
	}

	public ByteArrayOutputStream getByteOutStream() {
		return byteOutStream;
	}

	public void setByteOutStream(ByteArrayOutputStream pout) {
		this.byteOutStream = pout;
	}

	public String getExcelVersion() {
		return excelVersion;
	}

	public void setExcelVersion(String excelVersion) {
		this.excelVersion = excelVersion;
	}

	public ReportFillingStatus getFillingStatus() {
		return fillingStatus;
	}

	public void setFillingStatus(ReportFillingStatus fillingStatus) {
		this.fillingStatus = fillingStatus;
	}

	public JRAbstractLRUVirtualizer getVirtualizer() {
		return virtualizer;
	}

	public void setVirtualizer(JRAbstractLRUVirtualizer virtualizer) {
		this.virtualizer = virtualizer;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

}
