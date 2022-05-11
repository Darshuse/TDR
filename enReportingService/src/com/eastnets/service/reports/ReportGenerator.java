/**
 * 
 */
package com.eastnets.service.reports;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.eastnets.domain.admin.ProfileDetails;
import com.eastnets.domain.admin.User;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 
 * @author EastNets
 * @since dOct 24, 2012
 * 
 */
public class ReportGenerator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6996425504602547326L;
	private static final String PROFILES_JASPER_PATH = "/jasperReports/profilesReport.jasper";
	private static final String USERS_JASPER_PATH = "/jasperReports/usersReport.jasper";

	public static byte[] getProfilesReportAsBytes(List<ProfileDetails> profiles, Map<String, Object> params) throws JRException, IOException {
		return getReportAsBytes(profiles, PROFILES_JASPER_PATH, params);
	}

	// public static XSSFWorkbook getProfilesXlsxReport(List<ProfileDetails> profiles, Map<String, Object> params) throws JRException, IOException {
	// return getXlsxReport(profiles, PROFILES_JASPER_PATH, params);
	// }

	public static JasperPrint getProfilesXlsxReport(List<ProfileDetails> profiles, Map<String, Object> params) throws JRException, IOException {
		return getXlsxReport(profiles, PROFILES_JASPER_PATH, params);
	}

	public static byte[] getUsersReportAsBytes(List<User> users, Map<String, Object> params) throws JRException, IOException {
		return getReportAsBytes(users, USERS_JASPER_PATH, params);
	}

	private static byte[] getReportAsBytes(Collection<?> records, String jasperFilePath, Map<String, Object> params) throws JRException, IOException {
		InputStream inputStream = ReportGenerator.class.getResourceAsStream(jasperFilePath);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(records);
		final byte[] bytes = JasperRunManager.runReportToPdf(inputStream, params, beanColDataSource);
		inputStream.close();
		return bytes;
	}

	private static JasperPrint getXlsxReport(Collection<?> records, String jasperFilePath, Map<String, Object> params) throws JRException, IOException {

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(records);
		params.put("ItemDataSource", beanColDataSource);
		InputStream inputStream = ReportGenerator.class.getResourceAsStream(jasperFilePath);
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, beanColDataSource);
		return jasperPrint;
	}
	//
	// private static XSSFWorkbook getXlsxReport(List<ProfileDetails> records, String filePath, Map<String, Object> params) throws JRException, IOException {
	// // Blank workbook
	// XSSFWorkbook workbook = new XSSFWorkbook();
	// filePath = "src\\profilesReport";
	// // Create a blank sheet
	// XSSFSheet sheet = workbook.createSheet("profilesReport");
	// int rownum = 0;
	// int cellnum = 0;
	// String logo_path = (String) params.get("P_LOGO_PATH");
	// InputStream inputStream = null;
	// if (logo_path.startsWith("logo")) {
	// inputStream = ReportGenerator.class.getClassLoader().getResourceAsStream("/" + logo_path);
	// } else {
	// inputStream = new FileInputStream(logo_path);
	// }
	//
	// byte[] bytes = IOUtils.toByteArray(inputStream);
	// int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	// inputStream.close();
	// CreationHelper helper = workbook.getCreationHelper();
	// Drawing drawing = sheet.createDrawingPatriarch();
	// ClientAnchor anchor = helper.createClientAnchor();
	//
	// anchor.setCol1(6); // Column B
	// anchor.setRow1(1); // Row 3
	// anchor.setCol2(8); // Column C
	// anchor.setRow2(1); // Row 4
	// Picture pict = drawing.createPicture(anchor, pictureIdx);
	//
	// Row profilerow = sheet.createRow(rownum++);
	// XSSFCellStyle cellStyle = workbook.createCellStyle();
	// cellStyle.getFont().setFontHeightInPoints((short) 25);
	// cellStyle.getFont().setBold(true);
	//
	// XSSFCellStyle smallCellStyle = workbook.createCellStyle();
	// cellStyle.getFont().setFontHeightInPoints((short) 10);
	// cellStyle.getFont().setBold(false);
	//
	// Cell profileCell = profilerow.createCell(0);
	// profileCell.setCellValue("Profiles");
	// profileCell.setCellStyle(cellStyle);
	// String generated_by = (String) params.get("P_GENERATED_BY");
	// Integer number_of_profiles = (Integer) params.get("P_NUMBER_OF_PROFILES");
	// String date_pattern = (String) params.get("P_DATE_PATTERN");
	//
	// Row row = sheet.createRow(rownum++);
	// Cell gcell = row.createCell(0);
	// gcell.setCellValue("GENERATED_BY");
	// gcell.setCellStyle(smallCellStyle);
	// row.createCell(cellnum++).setCellValue(generated_by);
	// row.createCell(cellnum + 3).setCellValue("NUMBER_OF_PROFILES");
	// row.createCell(cellnum + 4).setCellValue(number_of_profiles);
	// Row row2 = sheet.createRow(rownum++);
	// row2.createCell(cellnum++).setCellValue("Date");
	// SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern);
	// String date_string = simpleDateFormat.format(new Date());
	// Date date;
	// try {
	// date = simpleDateFormat.parse(date_string);
	// row2.createCell(0).setCellValue("Date:");
	// row2.createCell(1).setCellValue(date_string);
	// } catch (ParseException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// sheet.createRow(rownum++);
	// sheet.createRow(rownum++);
	//
	// ProfileDetails actionList = (ProfileDetails) records.get(0);
	// List<String> reportingAction = actionList.getAuthorizedReportingActions();
	// List<String> messageViewerActions = actionList.getAuthorizedViewerActions();
	// List<String> watchdogActions = actionList.getAuthorizedWatchdogActions();
	// List<String> dashboardActions = actionList.getAuthorizedDashboardActions();
	// List<String> profileRolesActions = actionList.getProfileRoles();
	// List<String> bicCodeActions = actionList.getAuthorizedBICCodes();
	// List<String> unitActions = actionList.getAuthorizedUnits();
	// List<String> messageCategoriesActions = actionList.getAuthorizedMessageCategories();
	// // check if each list not empty before loop
	// Row row3 = sheet.createRow(rownum++);
	// row3.createCell(0).setCellValue(" Authorization:");
	// Row row4 = sheet.createRow(rownum++);
	// row4.createCell(0).setCellValue(" - Report Manager Actions:");
	// if (reportingAction.size() > 0) {
	// for (String s : reportingAction) {
	// Row reportRow = sheet.createRow(rownum++);
	// reportRow.createCell(2).setCellValue(s);
	// }
	// } else {
	// row4.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	// Row messageViwerRow = sheet.createRow(rownum++);
	// messageViwerRow.createCell(0).setCellValue(" Message Viewer Actions");
	// if (messageViewerActions.size() > 0) {
	// for (String s : messageViewerActions) {
	// Row viewertRow = sheet.createRow(rownum++);
	// viewertRow.createCell(2).setCellValue(s);
	// }
	// } else {
	// messageViwerRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	//
	// Row watchdogRow = sheet.createRow(rownum++);
	// watchdogRow.createCell(0).setCellValue(" Watchdog Actions");
	// if (watchdogActions.size() > 0) {
	// for (String s : watchdogActions) {
	// Row watchdogdetailsRow = sheet.createRow(rownum++);
	// watchdogdetailsRow.createCell(2).setCellValue(s);
	// }
	// } else {
	// watchdogRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	// Row dashBoardRow = sheet.createRow(rownum++);
	// dashBoardRow.createCell(0).setCellValue(" Dashboard Actions");
	// if (dashboardActions.size() > 0) {
	// for (String s : dashboardActions) {
	// Row dashboarddetailsRow = sheet.createRow(rownum++);
	// dashboarddetailsRow.createCell(2).setCellValue(s);
	// }
	//
	// } else {
	// dashBoardRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	// Row profileRolesRow = sheet.createRow(rownum++);
	// profileRolesRow.createCell(0).setCellValue(" Roles");
	// if (profileRolesActions.size() > 0) {
	// for (String s : profileRolesActions) {
	// Row RolesRow = sheet.createRow(rownum++);
	// RolesRow.createCell(2).setCellValue(s);
	// }
	//
	// } else {
	// profileRolesRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	//
	// }
	// Row bicCodeRow = sheet.createRow(rownum++);
	// profileRolesRow.createCell(0).setCellValue(" BIC Codes");
	// if (bicCodeActions.size() > 0) {
	// for (String s : bicCodeActions) {
	// Row CodeRow = sheet.createRow(rownum++);
	// CodeRow.createCell(2).setCellValue(s);
	// }
	//
	// } else {
	// bicCodeRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	// Row unitRow = sheet.createRow(rownum++);
	// unitRow.createCell(0).setCellValue(" Units");
	// if (unitActions.size() > 0) {
	//
	// for (String s : unitActions) {
	// Row unitDetailsRow = sheet.createRow(rownum++);
	// unitDetailsRow.createCell(2).setCellValue(s);
	// }
	//
	// } else {
	// unitRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	// Row messageCategoriesRow = sheet.createRow(rownum++);
	// messageCategoriesRow.createCell(0).setCellValue(" - Message Categories");
	// if (messageCategoriesActions.size() > 0) {
	//
	// for (String s : messageCategoriesActions) {
	// Row categoriesRow = sheet.createRow(rownum++);
	// categoriesRow.createCell(2).setCellValue(s);
	// }
	// } else {
	// messageCategoriesRow.createCell(cellnum++).setCellValue(" No Authorized Actions");
	// }
	//
	// // try {
	// // FileOutputStream outputStream = new FileOutputStream(new File(filePath));
	// // workbook.write(outputStream);
	// // } catch (FileNotFoundException e) {
	// // e.printStackTrace();
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	//
	// return workbook;
	//
	// }

	// private static JasperPrint getXlsxReport(Collection<?> records, String jasperFilePath, Map<String, Object> params) throws JRException, IOException {
	//
	// JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(records);
	// params.put("ItemDataSource", beanColDataSource);
	// InputStream inputStream = ReportGenerator.class.getResourceAsStream(jasperFilePath);
	// JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, beanColDataSource);
	// return jasperPrint;
	// }

}
