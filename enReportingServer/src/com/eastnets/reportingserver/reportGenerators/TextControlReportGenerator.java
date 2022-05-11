package com.eastnets.reportingserver.reportGenerators;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.StxPattern;
import com.eastnets.domain.reporting.TextControlBean;
import com.eastnets.domain.reporting.TextControlMessage;
import com.eastnets.domain.reporting.TextControlMessagesField;
import com.eastnets.reportingserver.ReportGenerator;
import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.reportingserver.ReportingServerSchedule;
import com.eastnets.reportingserver.reportUtil.EmailSender;
import com.eastnets.reportingserver.reportUtil.TextControlMessageBuilder;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.db.bean.FullField;
import com.eastnets.resilience.textparser.syntax.Message;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.reporting.ReportingService;

public class TextControlReportGenerator extends ReportGenerator {

	static Logger log = Logger.getLogger(TextControlReportGenerator.class.getName());
	private DateFormat dateF = new SimpleDateFormat("yyyyMMdd_HHmmss");
	ServiceLocator globelServiceLocater;
	public static final String DATE_PATTERN_CTL = "yyyyMMdd";
	private Date controlFileDate;

	@Override
	public String generateReport(Long reportSetId, User user, String criteriaName) {
		try {

			checkDebugMode();
			log.debug("Start Generate Report for TXT,CTL");
			String textFilePath = "tempTxetCoRepoT" + ".txt";
			String controlFilePathFilePath = "tempTxetCoRepoT" + ".ctl";
			log.debug("Start init ServiceLocator ...");
			initServiceLocator();
			log.debug("Start Build Text File ...");
			int messageNumber = buildTextFile(reportSetId, textFilePath);
			log.debug("End Build Text File ...");
			log.debug("Start Build CTL File ...");
			if (messageNumber == 0) {
				handelEmpatyReport(textFilePath, controlFilePathFilePath, reportSetId, criteriaName);
				log.debug("End generate Report");
				return null;
			}
			buildCTLFile(controlFilePathFilePath, messageNumber);
			log.debug("End Build CTL File ...");

			notifyGeneratedReport(textFilePath, "txt", getFormatedDateAs(DATE_PATTERN), reportSetId, criteriaName);
			notifyGeneratedReport(controlFilePathFilePath, "ctl", getFormatedDateAs(DATE_PATTERN), reportSetId, criteriaName);
			log.debug("End generate Report");
			deleteTempFile(textFilePath, controlFilePathFilePath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void deleteTempFile(String textFilePath, String controlFilePathFilePath) {
		try {

			FileWriter textFileWriter = new FileWriter(new File(textFilePath));
			textFileWriter.write("");
			textFileWriter.close();

			FileWriter ctlFileWriter = new FileWriter(new File(controlFilePathFilePath));
			ctlFileWriter.write("");
			ctlFileWriter.close();

			Path p1 = Paths.get(textFilePath);
			Files.delete(p1);

			Path p2 = Paths.get(controlFilePathFilePath);
			Files.delete(p2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handelEmpatyReport(String textFilePath, String controlFilePathFilePath, Long reportSetId, String criteriaName) {
		try {
			FileWriter textFileWriter = new FileWriter(new File(textFilePath));
			textFileWriter.write("");
			textFileWriter.close();
			buildCTLFile(controlFilePathFilePath, 0);
			log.debug("End Build CTL File ...");
			notifyGeneratedReport(textFilePath, "txt", getFormatedDateAs(DATE_PATTERN), reportSetId, criteriaName);
			notifyGeneratedReport(controlFilePathFilePath, "ctl", getFormatedDateAs(DATE_PATTERN), reportSetId, criteriaName);
			deleteTempFile(textFilePath, controlFilePathFilePath);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void checkDebugMode() {
		boolean isDebug = ReportingServerApp.getAppConfigBean().isDebug();
		if (isDebug == true) {
			log.setLevel(Level.DEBUG);
		} else {
			log.setLevel(Level.INFO);
		}

	}

	private void initServiceLocator() {
		ServiceLocator serviceLocater = ReportingServerApp.reportingServerApp.getServiceLocater();
		if (serviceLocater == null)
			serviceLocater = ReportingServerSchedule.rss.getServiceLocater();
		globelServiceLocater = serviceLocater;
	}

	private int buildTextFile(Long reportSetId, String textFilePath) {
		List<TextControlMessage> textControlMessages = null;
		try {
			textControlMessages = getTextControlMessages(reportSetId);
			log.debug("Number of message = " + textControlMessages.size());
			if (textControlMessages.isEmpty() || textControlMessages.size() == 0) {
				return 0;
			}
			TextControlBean textControlBean = null;
			int rowNo = 1;
			FileWriter textFileWriter = new FileWriter(new File(textFilePath));
			log.debug("Start Build Messages for Text Report ...");
			for (TextControlMessage textControlMessage : textControlMessages) {
				log.debug("Message  >> Aid = " + textControlMessage.getAid() + " UMIDL= " + textControlMessage.getUnidl() + " UMIDH= " + textControlMessage.getUmidh());
				textControlBean = buildMessageForReport(textControlMessage, globelServiceLocater, rowNo);
				if (textControlBean == null) {
					log.debug("Error during Build ..  For " + "Message  >> Aid = " + textControlMessage.getAid() + " UMIDL= " + textControlMessage.getUnidl() + " UMIDH= " + textControlMessage.getUmidh());
					continue;
				}
				textFileWriter.write(textControlBean.getTextReport());
				textFileWriter.write(System.getProperty("line.separator"));
				rowNo = rowNo + 1;
			}
			log.debug("End Build Messages for Text Report ...");
			textFileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return textControlMessages.size();
	}

	private List<TextControlMessage> getTextControlMessages(Long reportSetId) {
		Date fromDate = null;
		Date toDate = null;
		String userName = ReportingServerApp.getAppConfigBean().getUsername();
		ReportingService reportingService = globelServiceLocater.getReportingService();
		ReportSet reportSet = new ReportSet();
		reportSet.setId(reportSetId);
		reportSet = reportingService.getReportSet(userName, reportSet);
		List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
		List<Parameter> jasperReportParamatersList = reportingService.convertReportSetValuesToParamters(reportSetValues);
		for (Parameter parameter : jasperReportParamatersList) {
			if (parameter.getName().equalsIgnoreCase("P_FROM_DATE")) {
				fromDate = (Date) parameter.getValue();
			} else if (parameter.getName().equalsIgnoreCase("P_TO_DATE")) {
				toDate = (Date) parameter.getValue();
			}
		}
		log.debug("Start getting message from Database Based on date criteria ... FromDate = " + fromDate.toString() + " To Date = " + toDate.toString());
		controlFileDate = fromDate;
		return globelServiceLocater.getReportingService().getTextControlMessages(fromDate, toDate);
	}

	private void buildCTLFile(String controlFilePathFilePath, int messageNumber) {
		try {
			String controlFileDate = TextControlMessageBuilder.getFormatedControlFileDate(DATE_PATTERN_CTL, this.controlFileDate);
			FileWriter ctlFileWriter = null;
			ctlFileWriter = new FileWriter(new File(controlFilePathFilePath));
			ctlFileWriter.write(TextControlMessageBuilder.buildSummaryReportCTL(controlFileDate, controlFileDate, messageNumber));
			ctlFileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createByteArray(Object obj) throws IOException {
		List<String> list = new ArrayList<String>();
		list.add("foo");
		list.add("bar");
		list.add("baz");
		// write to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (String element : list) {
			out.writeUTF(element);
		}
		byte[] bytes = baos.toByteArray();

		System.out.println();
	}

	public static void main(String[] args) {

		String fileName = getFormatedDateAs(DATE_PATTERN_CTL);
		System.out.println(fileName);

	}

	private void notifyGeneratedReport(String generatedReportPath, String generatedReportExtention, String fileName, Long reportSetId, String criteriaName) {
		try {
			log.debug("Start notify Generated Report For >> " + generatedReportExtention + " File");
			Calendar cal = Calendar.getInstance();
			ByteArrayOutputStream outputStream = null;
			GeneratedReport generatedReport = new GeneratedReport();
			outputStream = getFileByteArrayOutputStream(new File(generatedReportPath));
			generatedReport.setStartTime(cal.getTime());
			generatedReport.setStatus(1);
			generatedReport.setBlob(outputStream.toByteArray());
			generatedReport.setFormat(generatedReportExtention);
			generatedReport.setGroupId((long) 1);
			generatedReport.setEndTime(cal.getTime());
			generatedReport.setCriteriaId(reportSetId);
			this.saveToFileReport(fileName, generatedReportExtention, outputStream);
			globelServiceLocater.getReportingService().addGeneratedReport(generatedReport);
			sendMail(generatedReport, reportSetId, criteriaName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected String saveToFileReport(String fileName, String extension, ByteArrayOutputStream exportReport) throws Exception {

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

	private void sendMail(GeneratedReport generatedReport, Long reportSetId, String criteriaName) {
		List<CriteriaSchedule> criteriaSchedules = globelServiceLocater.getReportingService().getCriteriaSchedule(criteriaName);
		if (criteriaSchedules != null && !criteriaSchedules.isEmpty()) {
			CriteriaSchedule criteriaSchedule = criteriaSchedules.get(0);
			if (criteriaSchedule.isNotifyAfterGeneration()) {
				if (criteriaSchedule.isAttachGeneratedReport()) {
					EmailSender.sendEmail(reportSetId, criteriaName, criteriaSchedule.getMailTo(), criteriaSchedule.getMailCc(), dateF.format(generatedReport.getEndTime()), generatedReport.getFormat(), generatedReport.getBlob(),
							criteriaSchedule.isAttachGeneratedReport(), ReportingServerApp.getAppConfigBean().getMailSubject(), ReportingServerApp.getAppConfigBean().getMailBody());
				} else {
					EmailSender.sendEmail(reportSetId, criteriaName, criteriaSchedule.getMailTo(), criteriaSchedule.getMailCc(), dateF.format(generatedReport.getEndTime()), generatedReport.getFormat(), generatedReport.getBlob(),
							criteriaSchedule.isAttachGeneratedReport(), ReportingServerApp.getAppConfigBean().getMailSubject(), ReportingServerApp.getAppConfigBean().getMailBody());
				}
			}
		}
	}

	private TextControlBean buildMessageForReport(TextControlMessage textControlMessage, ServiceLocator serviceLocater, int rowNo) {
		TextControlBean textControlBean = null;
		String messageSyntaxVersion = StringUtils.defaultString(textControlMessage.getStxVearsin(), "0000");
		Message parser = serviceLocater.getViewerService().getMessageParser(messageSyntaxVersion, textControlMessage.getMesgType());

		// get All Field For Message (103,202) Based ON syntax Version
		// delete all duplicated field from list by check option value
		List<FullField> fullFields = getMessageFullFields(parser.getFullFields(), textControlMessage);

		// get only field from message Text Block4
		List<ParsedField> parsedFields = getMessageParsedFields(parser, textControlMessage.getTextDataBlcok());

		if (parsedFields == null) {
			log.debug("Error during parsing ..  For " + "Message  >> Aid = " + textControlMessage.getAid() + " UMIDL= " + textControlMessage.getUnidl() + " UMIDH= " + textControlMessage.getUmidh());
			return null;
		}

		// Add ParsedField value to full field List
		List<TextControlMessagesField> textControlMessagesFields = mergeParsedFullFields(fullFields, parsedFields, textControlMessage);

		// Build Formated Text For one message
		textControlBean = TextControlMessageBuilder.buildMessage(textControlMessage, textControlMessagesFields, rowNo);

		return textControlBean;
	}

	private List<TextControlMessagesField> mergeParsedFullFields(List<FullField> fields, List<ParsedField> parsedFields, TextControlMessage textControlMessage) {
		StringBuilder feildOption = new StringBuilder();
		List<TextControlMessagesField> textControlMessagesFields = null;
		textControlMessagesFields = new ArrayList<TextControlMessagesField>();

		for (FullField fullField : fields) {
			List<String> valus = getFieldValue(parsedFields, fullField, feildOption);
			if (valus.isEmpty()) {
				TextControlMessagesField messagesField = new TextControlMessagesField();
				messagesField.setFieldCode(String.valueOf(fullField.getFeildCodeCr()));
				messagesField.setFieldOption(fullField.getFeildOptionCr());
				messagesField.setValue("");
				messagesField.setSequence(fullField.getFeildSequanceCr());
				messagesField.setFieldTag(fullField.getFieldTagCr());
				messagesField.setFielRowNum(fullField.getLineNumber());

				textControlMessagesFields.add(messagesField);
			} else {
				TextControlMessagesField messagesField = new TextControlMessagesField();
				messagesField.setFieldCode(String.valueOf(fullField.getFeildCodeCr()));
				messagesField.setFieldOption(feildOption.toString());
				messagesField.setValue(getListAsStringSeparated(valus));
				messagesField.setFieldTag(fullField.getFieldTagCr());
				messagesField.setSequence(fullField.getFeildSequanceCr());
				messagesField.setFielRowNum(fullField.getLineNumber());
				textControlMessagesFields.add(messagesField);
			}
			feildOption = new StringBuilder();
			// changeRowFieldNumber(fullField,textControlMessage);
		}
		return textControlMessagesFields;
	}

	private void changeRowFieldNumber(TextControlMessagesField controlMessagesField, TextControlMessage textControlMessage) {
		// TODO Auto-generated method stub
		if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fi.103")) {
			if (controlMessagesField.getFieldCode().equalsIgnoreCase("20")) {
				controlMessagesField.setFielRowNum(2);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("57")) {
				controlMessagesField.setFielRowNum(10);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("77")) {
				controlMessagesField.setFielRowNum(44); // curunt 77B
			}
		} else if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fi.103.S")) {
			if (controlMessagesField.getFieldCode().equalsIgnoreCase("20")) {
				controlMessagesField.setFielRowNum(2);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("57")) {
				controlMessagesField.setFielRowNum(10);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("59")) {
				controlMessagesField.setFielRowNum(9);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("77")) {
				controlMessagesField.setFielRowNum(39); // curunt 77B
			}
		} else if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fi.103.REMT")) {
			if (controlMessagesField.getFieldCode().equalsIgnoreCase("20")) {
				controlMessagesField.setFielRowNum(2);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("57")) {
				controlMessagesField.setFielRowNum(10);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("59")) {
				controlMessagesField.setFielRowNum(9);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("77")) {
				controlMessagesField.setFielRowNum(39); // curunt 77B
			}
		} else if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fi.202")) {
			if (controlMessagesField.getFieldCode().equalsIgnoreCase("13C")) {
				controlMessagesField.setFielRowNum(12);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("32")) {
				controlMessagesField.setFielRowNum(10);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("54")) {
				controlMessagesField.setFielRowNum(10); // curunt 77B
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("58")) {
				controlMessagesField.setFielRowNum(16); // curunt 77B
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("72")) {
				controlMessagesField.setFielRowNum(53); // curunt 77B
			}
		} else if (textControlMessage.getMesgIdinfier().equalsIgnoreCase("fi.202.COV")) {
			if (controlMessagesField.getFieldCode().equalsIgnoreCase("13C")) {
				controlMessagesField.setFielRowNum(12);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("32")) {
				controlMessagesField.setFielRowNum(10);
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("54")) {
				controlMessagesField.setFielRowNum(10); // curunt 77B
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("58")) {
				controlMessagesField.setFielRowNum(16); // curunt 77B
			} else if (controlMessagesField.getFieldCode().equalsIgnoreCase("72")) {
				controlMessagesField.setFielRowNum(8); // curunt 77B
			}
		}
	}

	private String getListAsStringSeparated(List<String> valus) {
		if (valus.size() == 1) {
			return valus.get(0);
		}
		if (valus.size() > 5) {
			List<String> list = valus.subList(0, 5);
			return String.join("\r\n", list);
		}
		return String.join("\r\n", valus);
	}
	/*
	 * 
	 * 
	 * feildCodeCr feildOptionCr feildSequanceCr lineNumber
	 * 
	 */

	private List<String> getMessageFielTemplet(String fileName) {
		List<String> messgeTempletList = new ArrayList<>();
		BufferedReader br = null;
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
			br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();

			while (line != null) {
				messgeTempletList.add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messgeTempletList;
	}

	private List<FullField> getMessageFullFields(List<FullField> fullFields, TextControlMessage textControlMessage) {
		List<FullField> fields = new ArrayList<>();

		if (textControlMessage.getMesgIdinfier().contains("103")) {
			List<String> messageFields103 = getMessageFielTemplet("103_templet.txt");
			for (String messageField : messageFields103) {
				String[] messageFieldArr = messageField.split(",");
				FullField field = new FullField(messageFieldArr[0], Integer.parseInt(messageFieldArr[1].trim()), messageFieldArr[2], messageFieldArr[3].equalsIgnoreCase("null") ? null : messageFieldArr[3],
						Integer.parseInt(messageFieldArr[4].trim()));
				fields.add(field);
			}

		} else if (textControlMessage.getMesgIdinfier().contains("202")) {
			List<String> messageFields103 = getMessageFielTemplet("202_templet.txt");
			for (String messageField : messageFields103) {
				String[] messageFieldArr = messageField.split(",");
				FullField field = new FullField(messageFieldArr[0], Integer.parseInt(messageFieldArr[1].trim()), messageFieldArr[2], messageFieldArr[3], Integer.parseInt(messageFieldArr[4].trim()));
				fields.add(field);
			}

		}

		/*
		 * for(FullField field:fullFields){ if( field.getFieldCode() !=59 &&(field.getFieldOptionOptionChoice() == null || field.getFieldOptionOptionChoice().isEmpty() ||
		 * field.getFieldOptionOptionChoice().equalsIgnoreCase("0"))){ fields.add(field); }else{ if(!fieldExsist(fields,field)){ fields.add(field); } } }
		 */
		return fields;
	}

	private List<ParsedField> getMessageParsedFields(Message parser, String mesgText) {
		ParsedMessage list = null;
		try {
			list = parser.parseMessageText(mesgText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list.getParsedFields();
	}

	private int getFeildRowNumber(int fieldIdx, String fullTag) {
		if (fullTag.equalsIgnoreCase("77T") || fullTag.equalsIgnoreCase("13C") || fullTag.equalsIgnoreCase("71F") || fullTag.equalsIgnoreCase("23E")) {
			return 5;
		} else if (fullTag.equalsIgnoreCase("71G") || fullTag.equalsIgnoreCase("71A") || fullTag.equalsIgnoreCase("32A") || fullTag.equalsIgnoreCase("33B")) {
			return 1;
		}

		List<StxPattern> patterns = globelServiceLocater.getReportingService().getFieldPattern(fieldIdx);
		int rowNum = 0;
		for (StxPattern stxPattern : patterns) {
			if (stxPattern.getType().equalsIgnoreCase("Field Header") || stxPattern.getType().equalsIgnoreCase("New Line")) {
				// Do nothing
			} else {
				rowNum = rowNum + stxPattern.getPatternNbNows();
			}
		}

		return rowNum;

	}

	private boolean checkIsFiftiesFields(String fieldCode) {
		if (fieldCode.equalsIgnoreCase("50") || fieldCode.equalsIgnoreCase("52") || fieldCode.equalsIgnoreCase("53") || fieldCode.equalsIgnoreCase("54") || fieldCode.equalsIgnoreCase("55") || fieldCode.equalsIgnoreCase("56")
				|| fieldCode.equalsIgnoreCase("57") || fieldCode.equalsIgnoreCase("58") || fieldCode.equalsIgnoreCase("59")) {
			return true;
		}
		return false;

	}

	/**
	 * This method responsible for get value from ParsedField List .... here you must know ,there is some field maybe have more than one value its repetitive field
	 * 
	 * @param List<FullField>,FullField
	 * @return boolean
	 */
	private List<String> getFieldValue(List<ParsedField> parsedFields, FullField fullField, StringBuilder fieldOption) {
		List<String> valueList = new ArrayList<>();

		if (fullField.getFeildSequanceCr() != null && fullField.getFeildSequanceCr().contains("tempB")) {
			return new ArrayList<>();
		}

		for (ParsedField field : parsedFields) {
			if (!fullField.getFeildOptionCr().equalsIgnoreCase("0") || fullField.getFeildCodeCr() == 59) {
				if (fullField.getFeildCodeCr() == 59 && field.getFieldOption() == null) {
					field.setFieldOption("0");
				}
				if (field.getFieldCode().contains(String.valueOf(fullField.getFeildCodeCr())) && field.getFieldOption().equalsIgnoreCase(fullField.getFeildOptionCr())) {
					if (field.getSequence() == null) {
						valueList.add(field.getValue());
						fieldOption.append(field.getFieldOption());
					} else if (field.getSequence().equalsIgnoreCase(fullField.getFeildSequanceCr())) {
						valueList.add(field.getValue());
						fieldOption.append(field.getFieldOption());
					}
				}
			} else if (field.getFieldCode().equalsIgnoreCase(fullField.getFieldTagCr().trim())) {
				if (field.getSequence() == null) {
					valueList.add(field.getValue());
					fieldOption.append(field.getFieldOption());
				} else if (field.getSequence().equalsIgnoreCase(fullField.getFeildSequanceCr())) {
					valueList.add(field.getValue());
					fieldOption.append(field.getFieldOption());
				}
			}
		}
		return valueList;
	}

	/**
	 * This method responsible for check if any duplicated field inside FullField List Like 50 ....
	 * 
	 * @param List<FullField>,FullField
	 * @return boolean
	 */
	private boolean fieldExsist(List<FullField> fields, FullField field) {
		for (FullField fullField : fields) {
			if (field.getFieldCode() == fullField.getFieldCode()) {
				if (field.getSequenceEntryId() == null) {
					return true;
				} else if (field.getSequenceEntryId().equalsIgnoreCase(fullField.getSequenceEntryId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method uses java.io.FileInputStream to read file content into a byte array
	 * 
	 * @param file
	 * @return
	 */
	private static byte[] readFileToByteArray(File file) {
		FileInputStream fis = null;
		// Creating a byte array using the length of the file
		// file.length returns long which is cast to int
		byte[] bArray = new byte[(int) file.length()];
		try {
			fis = new FileInputStream(file);
			fis.read(bArray);
			fis.close();

		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}
		return bArray;
	}

	/**
	 * This method uses java.io.FileInputStream to read file content into a byte array
	 * 
	 * @param file
	 * @return ByteArrayOutputStream
	 */
	private static ByteArrayOutputStream getFileByteArrayOutputStream(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(file.exists() + "!!");
		// InputStream in = resource.openStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1000000];
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
				// Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
				// System.out.println("read " + readNum + " bytes,");
			}

			fis.close();
		} catch (IOException ex) {
		}

		return bos;
	}

}
