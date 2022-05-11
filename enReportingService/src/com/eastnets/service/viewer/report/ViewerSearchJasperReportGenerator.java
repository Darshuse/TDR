package com.eastnets.service.viewer.report;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.Alliance;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.service.util.ReportType;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

public class ViewerSearchJasperReportGenerator {
	ViewerDAO viewerDAO;

	private final static int maxLineLength = 55;
	private final static int maxLineLengthLandscape = 100;

	private static final String PORTRAIT_JASPER_PATH = "/jasperReports/viewerMessagePortrait.jasper";
	private static final String LANDSCAPE_JASPER_PATH = "/jasperReports/viewerMessageLandscape.jasper";
	private static final String Exit_POINT_JASPER_PATH = "/jasperReports/viewerMessageExitPoint.jasper";
	private String creatByMsg = "";

	public ViewerSearchJasperReportGenerator(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	public ViewerReportStreamWriter generateExitPointReport(List<SearchResultEntity> messages, String msgType, ReportType type, String dateTimeFormat, String loggedInUser, String searchCriteria, boolean expandText, boolean landscape,
			ViewerSearchParam params, Boolean showSummary, Boolean showBlockFour, String reportLogo, List<String> mesgList) throws Exception {
		// get the messages to be printed
		List<ViewerSearchExitPointReportEntity> messagesWithText = getSearchExitPointReportEntities(mesgList);

		// fill the report parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("P_GENERATED_BY", mesgList.get(0));

		String reportPath = Exit_POINT_JASPER_PATH;

		// get the compiled report as a stream
		InputStream inputStream = ViewerSearchJasperReportGenerator.class.getResourceAsStream(reportPath);

		// create a datasource from the messages
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(messagesWithText);

		// fill the data in the report
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, beanColDataSource);
		inputStream.close();

		// chose the exporter based on the type required
		JRExporter exporter = null;
		if (type == ReportType.PDF) {
			exporter = new JRPdfExporter();
		} else if (type == ReportType.DOC) {
			exporter = new JRDocxExporter();
		} else {
			System.out.println("Unhandled report type \"" + type.name() + "\"");
			// use pdf
			exporter = new JRPdfExporter();
		}

		// ByteArrayOutputStream to hold the file generated
		ByteArrayOutputStream pout = new ByteArrayOutputStream();

		// set some parameters fro the exporter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pout);

		// fill the generated report into the byte stream
		exporter.exportReport();

		return new ViewerReportStreamWriterBytesImpl(pout);
	}

	public ViewerReportStreamWriter generateReport(List<SearchResultEntity> messages, String msgType, ReportType type, String dateTimeFormat, String loggedInUser, String searchCriteria, boolean expandText, boolean landscape, ViewerSearchParam params,
			Boolean showSummary, Boolean showBlockFour, String reportLogo, String thousandAmountFormat, String decimalAmountFormat) throws Exception {
		// get the messages to be printed
		List<ViewerSearchReportEntity> messagesWithText = getSearchReportEntities(loggedInUser, messages, expandText, showBlockFour, thousandAmountFormat, decimalAmountFormat);

		// fill the report parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("P_GENERATED_BY", loggedInUser);
		parameters.put("P_DATE_PATTERN", dateTimeFormat);
		parameters.put("P_MESG_TYPE", msgType);
		parameters.put("P_TOTAL_MESSAGES_COUNT", String.format("%d", messages.size()));
		parameters.put("P_SEARCH_CRITERIA", searchCriteria);
		parameters.put("P_LOGO_PATH", reportLogo);
		if (showSummary == true) {
			ReportSummary summary = new ReportSummary(messages, viewerDAO, params);
			parameters.put("P_SUMMARY_HEADER", summary.getReportSummaryHeader());
			parameters.put("P_SUMMARY", summary.getReportSummaryString());

		} else {
			parameters.put("P_SUMMARY_HEADER", "");
			parameters.put("P_SUMMARY", "");
		}

		String reportPath = PORTRAIT_JASPER_PATH;

		if (landscape) {
			reportPath = LANDSCAPE_JASPER_PATH;
		}

		// get the compiled report as a stream
		InputStream inputStream = ViewerSearchJasperReportGenerator.class.getResourceAsStream(reportPath);

		// create a datasource from the messages
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(messagesWithText);

		// fill the data in the report
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, beanColDataSource);
		inputStream.close();

		// chose the exporter based on the type required
		JRExporter exporter = null;
		if (type == ReportType.PDF) {
			exporter = new JRPdfExporter();
		} else if (type == ReportType.DOC) {
			exporter = new JRDocxExporter();
		} else {
			System.out.println("Unhandled report type \"" + type.name() + "\"");
			// use pdf
			exporter = new JRPdfExporter();
		}

		// ByteArrayOutputStream to hold the file generated
		ByteArrayOutputStream pout = new ByteArrayOutputStream();

		// set some parameters fro the exporter
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pout);

		// fill the generated report into the byte stream
		exporter.exportReport();

		return new ViewerReportStreamWriterBytesImpl(pout);
	}

	public static String generateSearchCriteria(CommonDAO commonDAO, ViewerSearchParam params, List<FieldSearchInfo> fieldSearch, String dateTimeFormat, String dateFormat, boolean isPortrait) {
		String c = addValue("Creation From", params.getCreationDate().getDateFrom(), dateTimeFormat);
		c += addValue("Creation To", params.getCreationDate().getDateTo(), dateTimeFormat);
		List<String> listSAANames = getSAANames(commonDAO, params.getSourceSelectedSAA());
		c += addValue("SAA", listSAANames, "; ", isPortrait);

		c += addValue("Format", params.getUmidFormat(), "Any");
		c += addValue("Direction", params.getUmidIO());
		c += addValue("Correspondent", params.getUmidCorrespondent());
		c += addValue("Qualifier", params.getUmidQual());
		c += addValue("Reference", params.getUmidReference());
		c += addValue("Sender", params.getContentSender(), "Any");
		c += addValue("Sender Inst", params.getContentSenderInstitution());
		c += addValue("Sender Dep", params.getContentSenderDepartment());
		c += addValue("Sndr First Name", params.getContentSenderFirstName());
		c += addValue("Sndr Last Name", params.getContentSenderLastName());
		c += addValue("Receiver", params.getContentReceiver(), "Any");
		c += addValue("Receiver Inst", params.getContentReceiverInstitution());
		c += addValue("Receiver Dept", params.getContentReceiverDepartment());
		c += addValue("Rcvr First Name", params.getContentReceiverFirstName());
		c += addValue("Rcvr Last Name", params.getContentReceiverLastName());
		c += addValue("Nature", params.getContentNature(), "All");
		c += addValue("Transaction Ref", params.getContentTransactionReference());
		c += addValue("Related Ref", params.getContentRelatedRefference());
		c += addValue("Amount From", params.getContentAmountFrom());
		c += addValue("Amount To", params.getContentAmountTo());
		c += addValue("Currency", params.getContentAmountCurrency());
		c += addValue("ValueDate From", params.getContentValueDateFrom(), dateFormat);
		c += addValue("ValueDate To", params.getContentValueDateTo(), dateFormat);
		c += addValue("Search Text", params.getContentSearchText());

		c += addValue("Network Name", params.getInterventionsNetworkName(), "Any");
		c += addValue("From/To Network", params.getInterventionsFromToNetwork(), "Any");
		c += addValue("Session Holder", params.getInterventionsSessionHolder());
		c += addValue("Session Number", params.getInterventionsSessionNumber());
		c += addValue("Seq Number From", params.getInterventionsSequenceNumberFrom());
		c += addValue("Seq Number To", params.getInterventionsSequenceNumberTo());

		c += addValue("Org Inst Status", params.getInstanceStatus(), "Any");
		c += addValue("Queues", params.getQueuesSelected(), ";", isPortrait);
		c += addValue("Units", params.getUnitsSelected(), ";", isPortrait);

		boolean firstField = true;
		if (fieldSearch != null) {
			for (FieldSearchInfo field : fieldSearch) {
				String label = "";
				if (firstField) {
					label = "Text Fields";
				}
				firstField = false;
				c += addValue(label, field.toString());
			}
		}

		return c;
	}

	private static List<String> getSAANames(CommonDAO commonDAO, List<String> sourceSelectedSAA) {
		List<String> selectedSAAs = new ArrayList<String>();
		List<Alliance> listAlliances = commonDAO.getAlliances();
		for (Alliance a : listAlliances) {
			if (sourceSelectedSAA.contains(a.getId()))
				selectedSAAs.add(a.getId() + ":" + a.getDisplayName());
		}
		return selectedSAAs;
	}

	private static String addValue(String label, List<String> items, String joinToken, boolean portrait) {
		if (items == null || items.isEmpty()) {
			return "";
		}

		String value = "";
		String line = "";
		int maxLength = (portrait ? maxLineLength : maxLineLengthLandscape);
		for (String item : items) {
			if (line.length() + item.length() + joinToken.length() > maxLength) {
				value += addValue(label, line, "");
				label = "";
				line = "";
			}
			line += item + joinToken;
		}
		line = line.substring(0, line.length() - joinToken.length());
		value += addValue(label, line, "");

		return value;
	}

	private static String addValue(String label, Date value, String format) {
		if (value == null) {
			return "";
		}
		return addValue(label, new SimpleDateFormat(format).format(value));
	}

	private static String addValue(String label, String value) {
		return addValue(label, value, "");
	}

	private static String addValue(String label, String value, String rejectValue) {
		if (value != null && !value.equalsIgnoreCase(rejectValue)) {
			int length = 16 - label.length();
			if (length < 1) {
				length = 1;
			}

			return String.format("%s%" + length + "s: %s\n", label, "", value);
		}
		return "";
	}

	private List<ViewerSearchReportEntity> getSearchReportEntities(String loggedInUser, List<SearchResultEntity> messages, boolean expandText, boolean showBlockFour, String thousandAmountFormat, String decimalAmountFormat) throws Exception {
		List<ViewerSearchReportEntity> messagesWithText = new ArrayList<ViewerSearchReportEntity>();

		// List<MessageDetails> messageDetailsList = viewerDAO.getMessageDetailsList(messages, loggedInUser);
		// Map<String, MessageDetails> messageDetailsMap = new HashedMap();

		/*
		 * for (MessageDetails messageDetails : messageDetailsList) { messageDetailsMap.put(messageDetails.getMessagePK(), messageDetails); }
		 */

		for (SearchResultEntity message : messages) {
			ViewerSearchReportEntity entry = new ViewerSearchReportEntity(message);
			// MessageDetails messageDetails = messageDetailsMap.get(message.getMessagePK());
			if (showBlockFour == true) {
				String textBlock = ViewerServiceUtils.getMessageTextBlock(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), message.getTimeZoneOffset(), viewerDAO, null);

				if (expandText) {
					try {
						textBlock = viewerDAO.getExpandedMesssageText(message.getMesgSyntaxTableVer(), message.getMesgType(), textBlock, message.getMesgCreaDateTime(), thousandAmountFormat, decimalAmountFormat);
					} catch (Exception e) {
						System.out.println(String.format("Unable to expand text for message( %d, %d, %d ). Reason : %s", message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), e.getMessage()));
					}
				}
				entry.setText(textBlock);
			}

			messagesWithText.add(entry);
		}
		return messagesWithText;
	}

	private List<ViewerSearchExitPointReportEntity> getSearchExitPointReportEntities(List<String> msgList) throws Exception {
		List<ViewerSearchExitPointReportEntity> messagesWithText = new ArrayList<ViewerSearchExitPointReportEntity>();

		for (String message : msgList) {
			ViewerSearchExitPointReportEntity entry = new ViewerSearchExitPointReportEntity();

			if (!message.isEmpty()) {
				if (!message.contains("Created by")) {
					String[] res = message.split("\n");
					int pageLines = 75;
					int len = res.length;
					switch (len / pageLines) {
					case 9:
						entry.setText9(fillTextFromIndex(pageLines * 9, pageLines * 10, res));
					case 8:
						entry.setText8(fillTextFromIndex(pageLines * 8, pageLines * 9, res));
					case 7:
						entry.setText7(fillTextFromIndex(pageLines * 7, pageLines * 8, res));
					case 6:
						entry.setText6(fillTextFromIndex(pageLines * 6, pageLines * 7, res));
					case 5:
						entry.setText5(fillTextFromIndex(pageLines * 5, pageLines * 6, res));
					case 4:
						entry.setText4(fillTextFromIndex(pageLines * 4, pageLines * 5, res));
					case 3:
						entry.setText3(fillTextFromIndex(pageLines * 3, pageLines * 4, res));
					case 2:
						entry.setText2(fillTextFromIndex(pageLines * 2, pageLines * 3, res));
					case 1:
						entry.setText1(fillTextFromIndex(pageLines, pageLines * 2, res));
					case 0:
						entry.setText(fillTextFromIndex(0, pageLines, res));
					}
					messagesWithText.add(entry);
				}
			}
		}
		return messagesWithText;
	}

	private String fillTextFromIndex(int start, int end, String[] res) {
		String result = "";
		if (end > res.length)
			end = res.length;
		for (; start < end; start++) {
			result += res[start] + "\n";
		}
		return result;
	}
}
