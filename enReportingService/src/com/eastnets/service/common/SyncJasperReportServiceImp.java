package com.eastnets.service.common;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;

import com.eastnets.domain.admin.User;

public class SyncJasperReportServiceImp extends JasperReportServiceImp {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 908078717487050139L;

	@Override
	public void cancelReport(JasperFillDetails jasperFillDetails) throws JRException {
		/**
		 * No implementation details needed till now
		 */
	}

	@Override
	public ByteArrayOutputStream fillReport(JasperFillDetails jasperFillDetails,String loggedInUser, String reportName, String reportExtension, Map<String, Object> param, String excelVersion, User user)
			throws Exception {
		
		AsynchronousFillHandle handle = AsynchronousFillHandle.createHandle(jasperFillDetails.getJasperReport(), param, connection);
		JasperReportAsynchListener fillListener = new JasperReportAsynchListener();
		fillListener.setReportExtension(reportExtension);
		fillListener.setExcelVersion(excelVersion);
		handle.addListener(fillListener);
		handle.startFill();
		while (true) {
			if (fillListener.getFillingStatus().equals(ReportService.ReportFillingStatus.FILLING)){
				if (Thread.interrupted()) {
					handle.cancellFill();
					break;
				}
				Thread.sleep(2000);
				
			}else if(fillListener.getFillingStatus().equals(ReportService.ReportFillingStatus.FINISHED)){
				break;
			} else{
				throw new Exception(fillListener.getErrorLog());
			}
				
		}
		
		return fillListener.getByteOutStream();
	}
}
