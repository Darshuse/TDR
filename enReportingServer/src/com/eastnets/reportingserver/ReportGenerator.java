package com.eastnets.reportingserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.common.JasperFillDetails;
import com.eastnets.service.common.ReportService;
import com.eastnets.service.reporting.ReportingService;

abstract public class ReportGenerator {
	static Logger log = Logger.getLogger(ReportGenerator.class.getName());
	public static final String DEFAULT_DATE_PATTERN = "ddMMyyyy_HHmm_ssSS";
	public static final String DATE_PATTERN = "YYYYMMdd_HHmmss";

	public static String getFormatedDateAs(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String dateFormated = simpleDateFormat.format(new Date());
		return dateFormated;
	}

	private Map<String, Integer> currenciesMap;

	public abstract String generateReport(Long reportSetId, User user, String criteriaName);

	protected ByteArrayOutputStream getReportAsStream(Long reportSetId, String extension, User user) throws Exception {

		String userName = ReportingServerApp.getAppConfigBean().getUsername();

		ServiceLocator serviceLocater = ReportingServerApp.reportingServerApp.getServiceLocater();
		if (serviceLocater == null)
			serviceLocater = ReportingServerSchedule.rss.getServiceLocater();

		ReportingService reportingService = serviceLocater.getReportingService();
		ReportService reportService = serviceLocater.getReportService();
		currenciesMap = serviceLocater.getViewerService().getCurrenciesISO();
		ReportSet reportSet = new ReportSet();
		reportSet.setId(reportSetId);
		reportSet = reportingService.getReportSet(userName, reportSet);

		Long reportId = reportSet.getReportId();
		Report report = reportingService.getReport(userName, reportId);

		String reportName = reportingService.getReportPath(report);

		List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
		List<Parameter> jasperReportParamatersList = reportingService.convertReportSetValuesToParamters(reportSetValues);
		Map<String, Object> jasperReportParmatersMap = reportingService.convertParametersToJasperParamters(report, jasperReportParamatersList, ReportingServerApp.getAppConfigBean().getReportTypes(), currenciesMap);

		String name = report.getName();
		if ("ISN GAPS".equalsIgnoreCase(name)) {
			reportingService.fillIsnGaps(userName, jasperReportParmatersMap);
		} else if ("OSN GAPS".equalsIgnoreCase(name)) {
			reportingService.fillOsnGaps(userName, jasperReportParmatersMap);
		}
		reportService.setActivateProfile(false);
		JasperFillDetails jasperFillDetails = new JasperFillDetails();
		ByteArrayOutputStream outputStream = reportService.exportReport(jasperFillDetails, userName, reportName, extension, jasperReportParmatersMap, "2010", user);

		return outputStream;
	}

	protected String saveToFile(String fileName, String extension, ByteArrayOutputStream exportReport) throws Exception {
		if (extension.equalsIgnoreCase("txt") || extension.equalsIgnoreCase("ctl")) {
			// do noth
		} else {
			String dateFormated = getFormatedDateAs(DEFAULT_DATE_PATTERN);
			fileName = fileName + "_" + dateFormated;

		}

		if (exportReport.size() > 0) {

			String directoryName = ReportingServerApp.getAppConfigBean().getOutpuReportPath();
			String fullFileName = null;
			if (fileName.contains(".xls") || fileName.contains(".pdf") || fileName.contains(".doc")) {
				fullFileName = getFormatedName(fileName, directoryName);
			} else {
				fullFileName = String.format("%s%s%s.%s", directoryName, File.separator, fileName, extension);

			}
			// String FileName = String.format("%s%s%s.%s", directoryName, File.separator,fileName, extension);
			File file = new File(fullFileName);
			log.info("Report: " + fullFileName);
			FileOutputStream fileOutputStream = null;
			try {
				if (file.exists() == false) {
					file.createNewFile();
				} else {
					file.delete();
					file.createNewFile();
				}

				fileOutputStream = new FileOutputStream(fullFileName);
				exportReport.writeTo(fileOutputStream);
				return fullFileName;

			} catch (FileNotFoundException e) {
				throw new Exception(e);
			} finally {
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						throw new Exception(e);
					}
				}
			} // finally
		}

		return null;
	}

	public String getFormatedName(String fileName, String directoryName) {
		String fullFileName = null;
		if (fileName.contains(".xls")) {
			fileName = fileName.replace(".xls", "");
			fullFileName = String.format("%s%s%s.%s", directoryName, File.separator, fileName, "xls");
		}
		if (fileName.contains(".pdf")) {
			fileName = fileName.replace(".pdf", "");
			fullFileName = String.format("%s%s%s.%s", directoryName, File.separator, fileName, "pdf");
		}

		if (fileName.contains(".doc")) {
			fileName = fileName.replace(".doc", "");
			fullFileName = String.format("%s%s%s.%s", directoryName, File.separator, fileName, "doc");
		}

		return fullFileName;
	}
}
