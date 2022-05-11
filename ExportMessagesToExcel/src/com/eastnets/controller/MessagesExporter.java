package com.eastnets.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import com.eastnets.beans.MessageField;
import com.eastnets.beans.MessageKey;
import com.eastnets.beans.SearchCriteria;


public class MessagesExporter {

	
	public void exportMessages(Map<MessageKey, List<MessageField>> messages, SearchCriteria searchCriteriaObj) throws Exception {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("sheet1");
		/*DataFormat df =*/ workbook.createDataFormat();
		int rownum = 0;
		
		setFirstHeader(sheet, rownum, workbook);
		rownum = rownum + 2 ;
		setSecondHeader(sheet, rownum, workbook, searchCriteriaObj);
		rownum = rownum + 3;
		setColumnsHeader(sheet, rownum, workbook);
		setData(messages, sheet, ++rownum);
		
		//create excel file
		FileOutputStream out =	new FileOutputStream(new File(searchCriteriaObj.getReportDirectoryPath() + File.separator + searchCriteriaObj.getReportName()));    
		workbook.write(out);    
		out.close();	
	}
	
	
	private void setFirstHeader(HSSFSheet sheet, int rownum, HSSFWorkbook workbook) {
		
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        headerFont.setFontName("Tahoma");
        
		Row row = sheet.createRow(rownum); 		
		Cell cell = row.createCell(0);
		cell.setCellValue("MT 103  / Sent");
		
		CellStyle curStyle = cell.getCellStyle();
		headerFont.setFontHeightInPoints((short) 24);
		curStyle.setFont(headerFont);
		cell.setCellStyle(curStyle);	
	}
	
	
	private void setSecondHeader(HSSFSheet sheet, int rownum, HSSFWorkbook workbook, SearchCriteria searchCriteriaObj) throws Exception {
				
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontName("Tahoma");
			
		Row row = sheet.createRow(rownum); 
		Cell cell = row.createCell(0);
		cell.setCellValue("Sender LT:");
		CellStyle curStyle = cell.getCellStyle();
		headerFont.setFontHeightInPoints((short) 12);
		curStyle.setFont(headerFont);
		cell.setCellStyle(curStyle);
		
		cell = row.createCell(1);
		cell.setCellValue(searchCriteriaObj.getxOwnLt());
		cell = row.createCell(3);
		cell.setCellValue("From:");
		cell = row.createCell(4);
		cell.setCellValue(searchCriteriaObj.getFromDate());
		cell = row.createCell(6);
		cell.setCellValue("To:");
		cell = row.createCell(7);
		cell.setCellValue(searchCriteriaObj.getToDate());
		
		row = sheet.createRow(++rownum); 
		cell = row.createCell(0);
		cell.setCellValue("Currencies:");
		cell = row.createCell(3);
		cell.setCellValue("Exclude US BICs:");
		cell = row.createCell(4);
		cell.setCellValue("False");
		cell = row.createCell(6);
		cell.setCellValue("Exclude US ADDR:");
		cell = row.createCell(7);
		cell.setCellValue("False");
		cell = row.createCell(9);
		cell.setCellValue("OFAC Hits Only:");
		cell = row.createCell(10);
		cell.setCellValue("False");
		
	}


	public void setColumnsHeader(HSSFSheet sheet, int rownum, HSSFWorkbook workbook) throws Exception {
		
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontName("Tahoma");
        
		Row row = sheet.createRow(rownum);    		
		Cell cell = row.createCell(0);
		cell.setCellValue("DATE&TIME");
		
		CellStyle curStyle = cell.getCellStyle();
		headerFont.setFontHeightInPoints((short) 8);
		curStyle.setFont(headerFont);
		cell.setCellStyle(curStyle);
		
		cell = row.createCell(1);
		cell.setCellValue("ISN");
		
		cell = row.createCell(2);
		cell.setCellValue("RECEIVER");
		
		cell = row.createCell(3);
		cell.setCellValue("Field :20:\nReference");
		
		cell = row.createCell(4);
		cell.setCellValue("Field :32A:\nValue Date");
		
		cell = row.createCell(5);
		cell.setCellValue("\nCurrency");
		
		cell = row.createCell(6);
		cell.setCellValue("\nAmount");
		
		cell = row.createCell(7);
		cell.setCellValue("Field :50a:\nOrdering Customer");
		
		cell = row.createCell(8);
		cell.setCellValue("Field :51a:\nSending Institution");
		
		cell = row.createCell(9);
		cell.setCellValue("Field :52a:\nOrdering Institution");
		
		cell = row.createCell(10);
		cell.setCellValue("Field :53a:\nSender's Correspondent");
		
		cell = row.createCell(11);
		cell.setCellValue("Field :54a:\nReceiver's Correspondent");
		
		cell = row.createCell(12);
		cell.setCellValue("Field :55a:\nThird Reimbursement Inst");
		
		cell = row.createCell(13);
		cell.setCellValue("Field :56a:\nIntermediary Institution");
		
		cell = row.createCell(14);
		cell.setCellValue("Field :57a:\nAccount W/Institution");

		cell = row.createCell(15);
		cell.setCellValue("Field :59a:\nBeneficiary Customer");
		
		cell = row.createCell(16);
		cell.setCellValue("Field :70:\nRemittance Information");
		
		cell = row.createCell(17);
		cell.setCellValue("Field :72:\nSender to Receiver Information");
		
	}
	
	
	public void setData(Map<MessageKey, List<MessageField>> messages, HSSFSheet sheet, int rownum) throws Exception {
		
		 Set<MessageKey> keys = messages.keySet();  
		 Cell cell ;
		 MessageField  messageField;
			
		 for(MessageKey messageKey : keys) {
			 
			 	List<MessageField> messageFields = messages.get(messageKey);
		 	 	Row row = sheet.createRow(rownum++);  
		 		messageField = messageFields.get(0);
		 		
			 	cell = row.createCell(0);
				cell.setCellValue(messageField.getCreationDate());
				
				cell = row.createCell(1);
				cell.setCellValue("");
				
				cell = row.createCell(2);
				cell.setCellValue(messageField.getReceiverX1());
					
				for(int i=0; i < messageFields.size(); i++) {
					
					setFields(cell, messageFields, row, i);													
				}	
		 }
	}


	private void setFields(Cell cell, List<MessageField> messageFields, Row row, int i) throws Exception {
		
		MessageField currentField = messageFields.get(i);
		
		if ( currentField.getFieldCode().equals("20")) { 
			cell = row.createCell(3);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("32") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("A")) { 
			cell = row.createCell(4);
			cell.setCellValue(currentField.getValue().substring(0, 6));
			
			cell = row.createCell(5);
			cell.setCellValue(currentField.getValue().substring(6, 9));
			
			cell = row.createCell(6);
			cell.setCellValue(currentField.getValue().substring(9, currentField.getValue().indexOf(',')));
			return;
		}
		
		if ( currentField.getFieldCode().equals("50") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(7);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("51") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(8);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("52") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(9);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("53") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(10);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("54") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(11);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("55") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(12);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("56") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(13);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("57") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(14);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("59") && currentField.getFieldOption() != null && currentField.getFieldOption().equalsIgnoreCase("a")) { 
			cell = row.createCell(15);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("70") ) { 
			cell = row.createCell(16);
			cell.setCellValue(currentField.getValue());
		}
		
		if ( currentField.getFieldCode().equals("72") ) { 
			cell = row.createCell(17);
			cell.setCellValue(currentField.getValue());
		}
		
	}
	
}
