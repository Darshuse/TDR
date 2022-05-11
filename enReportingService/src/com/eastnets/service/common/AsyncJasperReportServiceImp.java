package com.eastnets.service.common;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;

import com.eastnets.domain.admin.User;

public class AsyncJasperReportServiceImp extends JasperReportServiceImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6129237355735590685L;

	@Override
	public void cancelReport(JasperFillDetails jasperFillDetails) throws JRException {

		if (!jasperFillDetails.getFillListener().getFillingStatus().equals(ReportService.ReportFillingStatus.FINISHED)
				&& !jasperFillDetails.getFillListener().getFillingStatus().equals(ReportService.ReportFillingStatus.CANCELLED)
				&& !jasperFillDetails.getFillListener().getFillingStatus().equals(ReportService.ReportFillingStatus.ERROR)
				&& !jasperFillDetails.getFillListener().getFillingStatus().equals(ReportService.ReportFillingStatus.READY_FOR_NEW_REQUEST) ) {
			/*
			 * We need this indicator 'Canceling' status because the report base
			 * filler (base filler this is a Jasper class0) trying to continue
			 * filling the report although Jasper receiving a cancel request, so
			 * sometimes specially when the report's data are very large the
			 * cancel request take time to be executed
			 */
			try {
				jasperFillDetails.getFillListener().setFillingStatus(ReportFillingStatus.CANCELING);

				jasperFillDetails.getJasperReportAsyncFillHandler().cancelFill(jasperFillDetails.getFillHandlerName());
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public ByteArrayOutputStream fillReport(JasperFillDetails jasperFillDetails, String loggedInUser, String reportName, String reportExtension, Map<String, Object> param,
			String excelVersion, User user) throws Exception {
		AsynchronousFillHandle handle = AsynchronousFillHandle.createHandle(jasperFillDetails.getJasperReport(), param, connection);
		handle.setThreadName(jasperFillDetails.getFillHandlerName());

		JasperReportAsynchListener fillListener = new JasperReportAsynchListener();
		fillListener.setReportExtension(reportExtension);
		fillListener.setExcelVersion(excelVersion);
		handle.addListener(fillListener);
		jasperFillDetails.setFillListener(fillListener);

		JasperReportAsyncFillHandler jasperReportAsyncFillHandler = new JasperReportAsyncFillHandler();
		jasperReportAsyncFillHandler.setAsynchronousFillHandle(handle);
		jasperReportAsyncFillHandler.startFill();
		jasperFillDetails.setJasperReportAsyncFillHandler(jasperReportAsyncFillHandler);
		jasperFillDetails.getFillListener().setFillingStatus(ReportService.ReportFillingStatus.FILLING);
		jasperFillDetails.getFillListener().setVirtualizer(jasperFillDetails.getVirtualizer());
		return null;
	}

}
