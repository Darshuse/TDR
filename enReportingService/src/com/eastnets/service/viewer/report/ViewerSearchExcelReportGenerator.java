package com.eastnets.service.viewer.report;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.common.CommonDAO;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.DatabaseInfo;
import com.eastnets.domain.Pair;
import com.eastnets.domain.TreeNode;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.EntryNodeParent;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.viewer.nodes.AlternativeNode;
import com.eastnets.domain.viewer.nodes.FieldNode;
import com.eastnets.domain.viewer.nodes.LoopNode;
import com.eastnets.domain.viewer.nodes.OptionNode;
import com.eastnets.domain.viewer.nodes.SequenceNode;
import com.eastnets.resilience.mtutil.GenerateMTTypes;
import com.eastnets.resilience.mtutil.MTType;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.resilience.textparser.syntax.entry.Option;
import com.eastnets.resilience.textparser.syntax.entry.Option.OptionEntry;
import com.eastnets.service.viewer.helper.ViewerServiceUtils;

public class ViewerSearchExcelReportGenerator {
	ViewerDAO viewerDAO;
	CommonDAO commonDAO;

	private int totalMessages;
	private Map<String, MTType> mttypes;

	public ViewerSearchExcelReportGenerator(ViewerDAO viewerDAO, CommonDAO commonDAO) {
		this.viewerDAO = viewerDAO;
		this.commonDAO = commonDAO;
		try {
			this.mttypes = GenerateMTTypes.getMtTypesMap();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private String getTDRVersion(int minor) {
		if (minor == 0) {
			return "enReporting 3.0";
		} else if (minor == 1) {
			return "enReporting 3.1";
		} else if (minor == 2) {
			return "enReporting 3.2";
		}
		return "enReporting 3.1";
	}

	@SuppressWarnings("unchecked")
	public ViewerReportStreamWriter generateReport(List<SearchResultEntity> messages, String msgType, String dateTimeFormat, List<EntryNode> fields, String searchCriteria, String loggedInUser, ViewerSearchParam params, Boolean showSummary,
			Boolean showBlockFour) throws Exception {
		totalMessages = 0;

		XSSFWorkbook workbook = new XSSFWorkbook();
		List<DatabaseInfo> databaseInfos = commonDAO.getDatabaseInfo();
		XSSFSheet sheet = workbook.createSheet(getTDRVersion(databaseInfos.get(databaseInfos.size() - 1).getMinor()));

		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setWrapText(true);

		// create search criteria cells style
		XSSFCellStyle criteriaStyle = workbook.createCellStyle();
		XSSFFont criteriaFont = workbook.createFont();
		criteriaFont.setFontName("Courier New");
		criteriaStyle.setFont(criteriaFont);
		criteriaStyle.setWrapText(true);
		criteriaStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);

		int columnsCount = 0;

		// start from the row1
		int rowindex = 1;
		int columnIndex = 0;
		// create a row for the headers
		Row row = sheet.createRow(rowindex++);
		row.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));

		Cell cell = row.createCell(columnIndex++);
		cell.setCellValue("DATE&TIME");
		cell.setCellStyle(style);

		cell = row.createCell(columnIndex++);
		cell.setCellValue("ISN");
		cell.setCellStyle(style);

		cell = row.createCell(columnIndex++);
		cell.setCellValue("SENDER");
		cell.setCellStyle(style);

		cell = row.createCell(columnIndex++);
		cell.setCellValue("RECEIVER");
		cell.setCellStyle(style);

		List<String> filedHeaders = new ArrayList<String>();
		listFiledHeaders(fields, filedHeaders);
		int fieldsCount = 0, f32aIndex = -1; // counters to know the index of F32A header and match it with its' value

		for (String filedHeader : filedHeaders) {
			if (filedHeader.contains("F32A")) {
				f32aIndex = fieldsCount;
				cell = row.createCell(columnIndex++);
				filedHeader = "F32A";
				String valueDate = filedHeader + " \r\nValue Date";
				cell.setCellValue(valueDate);
				cell.setCellStyle(style);
				cell = row.createCell(columnIndex++);
				String curr = filedHeader + " \r\nCurrency";
				cell.setCellValue(curr);
				cell.setCellStyle(style);
				cell = row.createCell(columnIndex++);
				String amount = filedHeader + " \r\nAmount";
				cell.setCellValue(amount);
				cell.setCellStyle(style);
			} else {
				cell = row.createCell(columnIndex++);
				cell.setCellValue(filedHeader);
				cell.setCellStyle(style);
			}
			fieldsCount++;
		}

		// create new cell to hold Full Text Message header

		if (showBlockFour) {
			cell = row.createCell(columnIndex++);
			cell.setCellValue("Full Text Message");
			cell.setCellStyle(style);

		}

		columnsCount = columnIndex;

		style = workbook.createCellStyle();
		style.setWrapText(true);

		List<EntryNode> filedList = new ArrayList<EntryNode>();
		listFileds(fields, filedList);

		for (SearchResultEntity message : messages) {
			if (!msgType.startsWith(message.getMesgType())) {
				System.out.println("warning while generating report for message type " + msgType + " : on aid " + message.getAid() + " Message (" + message.getMesgUmidl() + ", " + message.getMesgUmidh() + ") type is " + message.getMesgType());
				System.out.println(" Message (" + message.getMesgUmidl() + ", " + message.getMesgUmidh() + ") Will not be included in the report");
				continue;
			}

			// MessageDetails messageDetails = messageDetailsMap.get(message.getMessagePK());

			// calculate data needed for header
			++totalMessages;

			// add a row for this message
			row = sheet.createRow(rowindex++);
			columnIndex = 0;

			cell = row.createCell(columnIndex++);
			cell.setCellValue(message.getMesgCreaDateTimeStr());
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			if (message.getMesgSubFormat().equalsIgnoreCase("INPUT"))
				cell.setCellValue(message.getEmiSequenceNbrFormatted());
			else
				cell.setCellValue(message.getRecSequenceNbrFormatted());
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue(message.getMesgSenderX1());
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue(message.getInstReceiverX1());
			cell.setCellStyle(style);

			LinkedHashMap<String, List<String>> fieldValues = new LinkedHashMap<String, List<String>>();
			StringBuilder textBlock = new StringBuilder("");
			String messageSyntaxVersion = StringUtils.defaultString(message.getMesgSyntaxTableVer(), "0000");
			Message parser = viewerDAO.getMessageParser(messageSyntaxVersion, msgType.substring(0, 3));
			listFiledValues(loggedInUser, message, fields, fieldValues, textBlock, parser, null);
			// check if mesg have mandatory fields
			// if (fieldValues.get("F32A") == null) {
			// invalideMesg();
			// }
			int maxHeight = 1;
			fieldsCount = 0;
			int valuesCount = 0;
			ArrayList<Row> blockfourRowRef = new ArrayList<Row>(); // this per message
			Row rowRef;
			blockfourRowRef.add(row);
			for (List<String> fieldValuesList : fieldValues.values()) {
				if (fieldValuesList == null || fieldValuesList.size() == 0) {
					cell = row.createCell(columnIndex++);
					cell.setCellValue("");
					cell.setCellStyle(style);
				} else if (fieldsCount == f32aIndex && fieldValuesList.size() > 0) {
					fill32A(fieldValuesList.get(0), columnIndex, row, style);
					columnIndex += 3;
				} else {
					valuesCount = 0;
					for (String singleFieldValue : fieldValuesList) {
						if (valuesCount < blockfourRowRef.size()) {
							rowRef = blockfourRowRef.get(valuesCount);
						} else {
							rowRef = sheet.createRow(rowindex++);
							blockfourRowRef.add(rowRef);
						}
						cell = rowRef.createCell(columnIndex);
						cell.setCellValue(singleFieldValue);
						cell.setCellStyle(style);
						maxHeight = Math.max(maxHeight, StringUtils.countMatches(singleFieldValue, "\n"));
						valuesCount++;
					}
					columnIndex++;
				}
				fieldsCount++;
			}

			// store the Full Text Message value into one cell
			if (showBlockFour) {
				cell = row.createCell(columnsCount - 1);
				cell.setCellValue(textBlock.toString());
				cell.setCellStyle(style);

			}

			float rowHight = maxHeight > 1 ? (maxHeight * sheet.getDefaultRowHeightInPoints()) : (4 * sheet.getDefaultRowHeightInPoints());
			// set the Max height of message rows
			for (Row r : blockfourRowRef) {
				r.setHeightInPoints(rowHight);
			}
		}

		row = sheet.createRow(0);
		columnIndex = 0;

		style = workbook.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);

		/*
		 * cell = row.createCell(0); cell.setCellStyle(style);
		 * 
		 * //merge the header cells CellRangeAddress region = new CellRangeAddress( 0, 0, 0, columnsCount-1); sheet.addMergedRegion(region);
		 * 
		 * String header = getHeader(msgType,loggedInUser,dateTimeFormat); cell.setCellValue(header);
		 * 
		 * String[] lines= header.split("\n"); row.setHeightInPoints((lines.length * sheet.getDefaultRowHeightInPoints()));
		 * 
		 * //create row1 for search criteria row = sheet.createRow(1); cell = row.createCell(0);
		 * 
		 * //merge the search criteria row cells (row1) region = new CellRangeAddress( 1, 1, 0, columnsCount-1); sheet.addMergedRegion(region);
		 * 
		 * //fill search criteria cell String formatedSearchCriteria=getFormatedCriteria(searchCriteria); cell.setCellValue(formatedSearchCriteria); cell.setCellStyle(criteriaStyle);
		 * 
		 * 
		 * lines=formatedSearchCriteria.split("\n"); row.setHeightInPoints((lines.length+1) *sheet.getDefaultRowHeightInPoints());
		 */

		if (showSummary == true) {
			ReportSummary summary = new ReportSummary(messages, viewerDAO, params);
			TreeNode<String> reportSummaryTree = summary.getReportSummary();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

			boolean isThereValueDate = false;
			if (summary.getMinValueDate() != null && summary.getMaxValueDate() != null) {
				isThereValueDate = true;
				rowindex++;
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue("From Value Date :");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue(DATE_FORMAT.format(summary.getMinValueDate()));

				row = sheet.createRow(rowindex++);
				columnIndex = 0;

				cell = row.createCell(columnIndex++);
				cell.setCellValue("To Value Date     :");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue(DATE_FORMAT.format(summary.getMaxValueDate()));

			}
			cell = row.createCell(columnIndex++);

			rowindex++;
			row = sheet.createRow(rowindex++);
			columnIndex = 0;

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Sender");
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Receiver");
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Currency");
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Total Amount");
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Total messages");
			cell.setCellStyle(style);

			cell = row.createCell(columnIndex++);
			cell.setCellValue("Average Amount");
			cell.setCellStyle(style);
			if (isThereValueDate) {
				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Value/Day");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Value/Week");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Value/Month");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Value/Year");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Volume/Day");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Volume/Week");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Volume/Month");
				cell.setCellStyle(style);

				cell = row.createCell(columnIndex++);
				cell.setCellValue("Average Volume/Year");
				cell.setCellStyle(style);
			}

			// add the footer

			DecimalFormat decimalFormat = new DecimalFormat("#.##");

			Long interval = 1l;
			Double day = 1.0;
			Double week = 1.0;
			Double month = 1.0;
			Double year = 1.0;
			if (isThereValueDate) {
				interval = summary.getMaxValueDate().getTime() - summary.getMinValueDate().getTime();
				if (interval == 0) {
					day = 1.0;
					week = 1.0;
					month = 1.0;
					year = 1.0;
				} else {
					day = (double) ((interval / (1000 * 60 * 60)) / 24) + 1;
					week = day / 7;
					month = day / 30;
					year = day / 365;
				}
			}

			for (TreeNode<String> senderReceiver : reportSummaryTree.getChildren()) {
				String senderReceiverStr = senderReceiver.getData();
				for (TreeNode<String> currency : senderReceiver.getChildren()) {
					String currencyStr = currency.getData();
					BigDecimal amount = ((Pair<BigDecimal, Integer>) currency.getValue()).getKey();
					Integer count = ((Pair<BigDecimal, Integer>) currency.getValue()).getValue();

					row = sheet.createRow(rowindex++);
					columnIndex = 0;

					String[] bics = senderReceiverStr.split(":");

					cell = row.createCell(columnIndex++);
					cell.setCellValue(bics[0]);

					cell = row.createCell(columnIndex++);
					cell.setCellValue(bics[1]);

					cell = row.createCell(columnIndex++);
					cell.setCellValue(currencyStr);

					cell = row.createCell(columnIndex++);
					cell.setCellValue(amount.doubleValue());

					cell = row.createCell(columnIndex++);
					cell.setCellValue(count);

					cell = row.createCell(columnIndex++);
					cell.setCellValue(amount.divide(new BigDecimal(count), 10, RoundingMode.HALF_UP).doubleValue());
					if (isThereValueDate) {
						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(amount.divide(new BigDecimal(day), 10, RoundingMode.HALF_UP).doubleValue()));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(amount.divide(new BigDecimal(week), 10, RoundingMode.HALF_UP).doubleValue()));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(amount.divide(new BigDecimal(month), 10, RoundingMode.HALF_UP).doubleValue()));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(amount.divide(new BigDecimal(year), 10, RoundingMode.HALF_UP).doubleValue()));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(count / day));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(count / week));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(count / month));

						cell = row.createCell(columnIndex++);
						cell.setCellValue(decimalFormat.format(count / year));
					}
				}
			}

			style = workbook.createCellStyle();
			style.setFont(font);
			style.setWrapText(true);

			rowindex += 2;
			row = sheet.createRow(rowindex++);
			cell = row.createCell(0);
			cell.setCellStyle(style);

			columnIndex = 0;
			// merge the header cells
			CellRangeAddress region2 = new CellRangeAddress(rowindex - 1, rowindex - 1, 0, columnsCount - 1);
			sheet.addMergedRegion(region2);

			String header2 = getHeader(msgType, loggedInUser, dateTimeFormat);
			cell.setCellValue(header2);

			String[] lines2 = header2.split("\n");
			row.setHeightInPoints((lines2.length * sheet.getDefaultRowHeightInPoints()));

			// create row1 for search criteria
			row = sheet.createRow(rowindex++);
			cell = row.createCell(0);
			cell.setCellStyle(style);

			// merge the search criteria row cells (row1)
			region2 = new CellRangeAddress(rowindex - 1, rowindex - 1, 0, columnsCount - 1);
			sheet.addMergedRegion(region2);

			// fill search criteria cell
			String formatedSearchCriteria2 = getFormatedCriteria(searchCriteria);
			cell.setCellValue(formatedSearchCriteria2);
			cell.setCellStyle(criteriaStyle);

			lines2 = formatedSearchCriteria2.split("\n");
			row.setHeightInPoints((lines2.length + 1) * sheet.getDefaultRowHeightInPoints());

		}

		/*
		 * 
		 * //autofit columns
		 */
		for (int i = 0; i < columnsCount; ++i) {
			sheet.autoSizeColumn(i);
		}

		// the workbook.getBytes().length does not give the exact written file length so i had to write it to a byte stream so that we can accurateley give its size
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			workbook.write(baos);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ViewerReportStreamWriterImpl(baos);
	}

	private void fill32A(String fieldValue, int columnIndex, Row row, XSSFCellStyle style) {
		String valueDate;
		String curr;
		String amount;
		Cell cell;
		if (fieldValue.isEmpty() || fieldValue.equals("") || fieldValue == null) {
			valueDate = "";
			curr = "";
			amount = "";
		} else {
			valueDate = fieldValue.substring(0, 6);
			curr = fieldValue.substring(6, 9);
			amount = fieldValue.substring(9, fieldValue.length());
		}
		cell = row.createCell(columnIndex++);
		cell.setCellValue(valueDate);
		cell.setCellStyle(style);

		cell = row.createCell(columnIndex++);
		cell.setCellValue(curr);
		cell.setCellStyle(style);

		cell = row.createCell(columnIndex++);
		cell.setCellValue(amount);
		cell.setCellStyle(style);

	}

	private String getHeader(String msgType, String loggedInUser, String dateTimeFormat) {
		String header;
		header = "Date                 :" + new SimpleDateFormat(dateTimeFormat).format(new java.util.Date());
		header += "\nGenerated by   : " + loggedInUser;
		header += "\nMessage Type  : " + msgType;
		header += "\nTotal Messages : " + totalMessages;

		return header;
	}

	private String getFormatedCriteria(String searchCriteria) {
		String formatedString = "Search Criteria :";

		String[] lines = searchCriteria.split("\n");

		for (String line : lines) {
			if (line.trim() != "")
				formatedString += "\n                 " + line;
		}

		return formatedString;
	}

	private void listFileds(List<EntryNode> fields, List<EntryNode> filedList) {
		for (EntryNode field : fields) {
			if (!field.isChecked())
				continue;
			if (field.getClass() == FieldNode.class || field.getClass() == AlternativeNode.class || field.getClass() == OptionNode.class) {
				filedList.add(field);
			} else if (field.getClass() == LoopNode.class || field.getClass() == SequenceNode.class) {
				EntryNodeParent fieldGroup = (EntryNodeParent) field;
				listFileds(fieldGroup.getChildNodes(), filedList);
			} else {
				System.out.println("warning : Unhandled syntax node type " + field.getClass().getName());
			}
		}
	}

	private void listFiledHeaders(List<EntryNode> fields, List<String> fieldHeaders) {
		for (EntryNode field : fields) {
			if (!field.isChecked())
				continue;
			if (field.getClass() == FieldNode.class || field.getClass() == AlternativeNode.class || field.getClass() == OptionNode.class) {
				fieldHeaders.add(field.getTag() + "\r\n" + field.getExpansion());
			} else if (field.getClass() == LoopNode.class || field.getClass() == SequenceNode.class) {
				// EntryNodeParent fieldGroup = (EntryNodeParent)field;
				// listFiledHeaders( fieldGroup.getChildNodes(), fieldHeaders );
			} else {
				System.out.println("warning : Unhandled syntax node type " + field.getClass().getName());
			}
		}
	}

	private void listFiledValues(String loggedInUser, SearchResultEntity message, List<EntryNode> fields, LinkedHashMap<String, List<String>> fieldValues, StringBuilder textBlock, Message parser, MessageDetails messageDetails) throws Exception {

		if (message.getTimeZoneOffset() == null) {
			message.setTimeZoneOffset(0);
		}
		String messageText = ViewerServiceUtils.getMessageTextBlock(loggedInUser, message.getAid(), message.getMesgUmidl(), message.getMesgUmidh(), message.getMesgCreaDateTimeOnDB(), message.getTimeZoneOffset(), viewerDAO, messageDetails);

		// Remove the last occurrence of "\n" if it's exist
		if (messageText.lastIndexOf("\n") + 1 == messageText.length()) {
			messageText = messageText.substring(0, messageText.lastIndexOf("\n") - 1);
		}
		textBlock.delete(0, textBlock.length());
		textBlock.append(messageText);
		if (parser != null) {
			parser.setExcelExport(true);
			ParsedMessage parsedMesg = parser.parseMessageText(messageText);
			message.setNotValidMsg(parsedMesg.isInvalidMessages());
			fillValues(fields, fieldValues, parsedMesg);
			if (message.getxFinAmount() == null || message.getxFinCcy() == null || message.getxFinCcy().isEmpty()) {
				MTType mt = mttypes.get(message.getMesgIdentifier().substring(4).toUpperCase());
				if (mt == null) {
					mt = mttypes.get(message.getMesgType());
				}
				Map<String, String> keywords = parsedMesg.getExpandedMessageMap(mt.getAmtField(), mt.getCcyField(), mt.getValueDateField());
				fillMessageKeywords(message, keywords);
			}
		}
	}

	private void fillValues(List<EntryNode> fields, LinkedHashMap<String, List<String>> fieldValues, ParsedMessage parsedMesg) {
		String fieldEntryStr;
		String fieldKey = ""; // loopid+fiedcode
		boolean exist = false;
		for (EntryNode field : fields) {
			if (!field.isChecked())
				continue;
			if (field.getClass() == FieldNode.class || field.getClass() == AlternativeNode.class || field.getClass() == OptionNode.class) {
				exist = false;
				fieldEntryStr = (field.getTag().length() <= 4 ? field.getTag().substring(1) : field.getTag().substring(1, 3));
				if (field.getParent() != null)
					fieldKey = field.getParent().getName() + field.getName() + fieldEntryStr;
				else
					fieldKey = field.getName() + fieldEntryStr;
				for (ParsedField parsedField : parsedMesg.getParsedFields()) {

					if (field.getParent() != null) {

						// if seq
						if (field.getParent().getClass() == SequenceNode.class && !field.getParent().getTag().substring(1).equals(parsedField.getSequence())) {
							continue;
						}
						// if loop
						if (field.getParent().getClass() == LoopNode.class && !field.getParent().getName().equals(parsedField.getLoopId())) {
							continue;
						}
					} else {// field does not has parent
						if (parsedField.getLoopId() != null || parsedField.getSequence() != null)
							continue;
					}

					if (parsedField.getExpansion() == null) {
						parsedField.setExpansion("");
					}
					if (parsedField.getFieldCode() == null) {
						parsedField.setFieldCode("");
					}
					if ((fieldEntryStr.endsWith("a") && parsedField.getFieldCode().startsWith(fieldEntryStr.substring(0, 2)))
							|| (parsedField.getFieldCode().startsWith(fieldEntryStr) && parsedField.getExpansion().equalsIgnoreCase(field.getExpansion()))) {

						// See if the iterated field header have optional values
						if (field.getNode().isOptional() && parsedField.getFieldCode().length() > 2) {
							if (field.getNode() instanceof Option) {
								List<OptionEntry> optionEntries = ((Option) field.getNode()).getOptions();

								boolean optionEntryFound = false;
								// find if the parsed field have any possible optional value from the optional entries
								for (OptionEntry optionEntry : optionEntries) {

									if (optionEntry.getOptionChoice().equals(parsedField.getFieldCode().substring(2))) {
										optionEntryFound = true;
										break;
									}
								}
								/*
								 * If the current parsed field doesn't exist in the option entries of the header field\n that means the iterated parsed field must not mapped now and will later mapped
								 * to another field ex. - F50a Instructing Party - non-FI BIC - F50a Ordering Customer - ID
								 */
								if (!optionEntryFound) {
									continue;
								}
							}
						}

						if (fieldValues.containsKey(fieldKey)) {
							List<String> valuesList = fieldValues.get(fieldKey);
							valuesList.add(parsedField.getValue());// handles fields in loop.
						} else {
							List<String> valuesList = new ArrayList<String>();
							valuesList.add(parsedField.getValue());
							fieldValues.put(fieldKey, valuesList);
						}
						exist = true;
						if (field.getParent() == null || (field.getParent() != null && field.getParent() instanceof SequenceNode)) {
							// now we find match of the field so if the current field is not in loop we have break.
							break;
						}
					}
				}
				if (exist == false) {
					fieldValues.put(fieldKey, null);
				}
			}
		}
	}

	private void fillMessageKeywords(SearchResultEntity message, Map<String, String> keywords) {
		// Amount value formater
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);

		for (String str : keywords.keySet()) {

			// transaction
			/*
			 * String [] ar = str.split(":");
			 * 
			 * if(ar[0].equalsIgnoreCase("20")){ mesg.setMesgTrnRef(keywords.get(str)); }else if(ar[0].equalsIgnoreCase("20C")){ mesg.setMesgTrnRef(keywords.get(str)); }
			 */
			try {
				if (str.contains(":AMOUNT")) {
					BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(keywords.get(str));
					message.setxFinAmount(bigDecimal);
				}
			} catch (ParseException ex) {
				System.out.println(String.format("RJE Message:: Error while pasring  amount[%s] of message  of type %s", keywords.get(str), message.getMesgIdentifier()));
			} catch (Exception ex) {
				System.out.println(String.format("RJE Message:: Error while pasring  amount of message  of type %s", message.getMesgIdentifier()));
			}

			if (str.contains(":CURRENCY")) {
				message.setxFinCcy(keywords.get(str));
			}

			if (str.contains(":DATE")) {
				String dateFormat = "yyMMdd";
				if (keywords.get(str).length() >= 8) {
					dateFormat = "yyyyMMdd";

				} else if (keywords.get(str).length() == 6) {
					dateFormat = "yyMMdd";
				}

				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				Date valueDate = null;
				try {
					String dateString = keywords.get(str);
					if (dateString != null && dateString.length() > 8) {
						dateString = dateString.substring(0, 8);
					}
					valueDate = sdf.parse(dateString);
				} catch (Exception e) {
					throw new WebClientException("the parameter valueDate in RJEMessageParser is not valid, error message : " + e.getMessage());
				}

				message.setxFinValueDate(new java.sql.Date(valueDate.getTime()));

			}
		}
	}
}
