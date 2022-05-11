package com.eastnets.service.common;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import com.eastnets.domain.admin.User;




public interface ReportService 
{
	public static enum ReportTypes {pdf,xls,docx};
	public static enum ReportFillingStatus {
		ERROR, FINISHED, CANCELING, CANCELLED, FILLING,READY_FOR_NEW_REQUEST
	}

	public ByteArrayOutputStream exportReport(JasperFillDetails jasperFillDetails, String loggedInUser, String reportName, String reportExtension, Map<String, Object> param,
			String excelVersion, User user) throws Exception;
	public boolean isActivateProfile();
	public void setActivateProfile(boolean activateProfile) ;
	public void cancelReport(JasperFillDetails jasperFillDetails) throws JRException;
	public void PrepareForNewGeneration(JasperFillDetails jasperFillDetails);
}
