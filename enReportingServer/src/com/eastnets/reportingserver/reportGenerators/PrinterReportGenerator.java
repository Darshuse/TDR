package com.eastnets.reportingserver.reportGenerators;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.service.common.ReportService.ReportTypes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;  
import java.awt.print.PrinterJob;  
import java.awt.print.Book;
import java.nio.ByteBuffer;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import com.eastnets.reportingserver.ReportGenerator;


public class PrinterReportGenerator extends ReportGenerator {
	static Logger log = Logger.getLogger(PrinterReportGenerator.class.getName());

	@Override
	public String generateReport(Long reportSetId, User user,String criatraName) {
		
		String fileName = "";
		try {
			
			String extension = ReportingServerApp.getAppConfigBean().getReportTypes().name();
			
			ByteArrayOutputStream outputStream = this.getReportAsStream(reportSetId, extension, user);
			
			if(outputStream == null){
				log.error(" :: Error in Generating Report ::");
				return null;
			}
			
			String outpuReportName = ReportingServerApp.getAppConfigBean().getOutpuReportName();
					
			outpuReportName=outpuReportName+"_"+criatraName;
			log.debug("out put report name is "+outpuReportName);
			fileName = this.saveToFile(outpuReportName, extension, outputStream);
			log.info("Report " + outpuReportName + " is generated.");
			print(fileName);
			log.info("Report " + outpuReportName + " is printed.");
		} catch (Exception e) {
			log.error("Error",e);
		}
		
		return fileName;
	}
	
	void print(String fileName) throws Exception {
		
			int copiesNumber = ReportingServerApp.getAppConfigBean().getCopiesCount();
			PrintService ps = null;
			if(ReportingServerApp.getAppConfigBean().getPrinterName() != null && ReportingServerApp.getAppConfigBean().getPrinterName().length() > 0)
				ps = getPrintService(ReportingServerApp.getAppConfigBean().getPrinterName().trim());
			else
				ps = PrintServiceLookup.lookupDefaultPrintService();
			log.info("Print Service Name: " + ps.getName());
			//Identify Document Type
			if(ReportingServerApp.getAppConfigBean().getReportTypes().equals(ReportTypes.pdf)) {
				printPDF(fileName, ps, copiesNumber);
			} else if(ReportingServerApp.getAppConfigBean().getReportTypes().equals(ReportTypes.xls)) {
				printExcelFile(fileName, ps, copiesNumber);
			} else if(ReportingServerApp.getAppConfigBean().getReportTypes().equals(ReportTypes.docx)) {
				printWordFile(fileName, ps, copiesNumber);
			}
					    
	}
	
	
	public static PrintService  getPrintService(String printServiceName) throws Exception {
		
		PrintService[] printServices = PrinterJob.lookupPrintServices();
        PrintService currentPrintService = null;
        int i; 
        
		for (i = 0; i < printServices.length; i++) {
			if (printServices[i].getName().equalsIgnoreCase(printServiceName)) {
				currentPrintService = printServices[i];
				break;
			}
		}
		
		if (i == printServices.length) {
			throw new PrinterException("Invalid print service name: " + printServiceName);
		}
		
		return currentPrintService;
	}
		
	private void printExcelFile(String file, PrintService ps, int copiesNumber) throws Exception {
	      
	      String vbs = "Dim AppExcel\r\n"
	    		  + "Dim appWord\r\n"
	    		  + "Dim defaulPrinter\r\n"
	              + "Set AppExcel = CreateObject(\"Excel.application\")\r\n"
	              + "Set appWord = CreateObject(\"Word.application\")\r\n"
	              + "defaulPrinter = appWord.ActivePrinter\r\n" 
	              + "appWord.ActivePrinter = \"" + ps.toString().substring(ps.toString().indexOf(":") + 1).trim() + "\"\r\n"
	              + "AppExcel.Workbooks.Open(\"" + file + "\")\r\n"
	              + "AppExcel.ActiveWindow.SelectedSheets.PrintOut ,," + copiesNumber + " \r\n"
	              + "appWord.ActivePrinter = defaulPrinter\r\n"
	              + "AppExcel.Quit\r\n"
	              + "Set AppExcel = Nothing"
	              + "appWord.Quit\r\n"
	              + "Set appWord = Nothing\r\n";
	      
	      File vbScript = File.createTempFile("vbScript", ".vbs");
	      vbScript.deleteOnExit();
	      FileWriter fw = new java.io.FileWriter(vbScript);
	      fw.write(vbs);
	      fw.close();
	      Process p = Runtime.getRuntime().exec("cscript " + vbScript.getPath());
	      p.waitFor(); 

	}
	
	private void printWordFile(String file, PrintService ps, int copiesNumber) throws Exception {
		
	      String vbs = "Dim appWord\r\n"
	    		  + "Dim defaulPrinter\r\n"
	              + "Set appWord = CreateObject(\"Word.application\")\r\n"
	    		  + "defaulPrinter = appWord.ActivePrinter\r\n" 
	              + "appWord.ActivePrinter = \"" + ps.toString().substring(ps.toString().indexOf(":") + 1).trim() + "\"\r\n"
	              + "appWord.Documents.Open(\"" + file + "\") \r\n"
	              + "appWord.PrintOut False,,,,,,," + copiesNumber + " \r\n"
	              + "appWord.ActivePrinter = defaulPrinter\r\n"
	              + "appWord.Quit\r\n"
	              + "Set appWord = Nothing\r\n";
	      
	      File vbScript = File.createTempFile("vbScript", ".vbs");
	      vbScript.deleteOnExit();
	      FileWriter fw = new java.io.FileWriter(vbScript);
	      fw.write(vbs);
	      fw.close();
	      Process p = Runtime.getRuntime().exec("cscript " + vbScript.getPath());
	      p.waitFor(); 
	}
	
	
	 /**
     * This method prints the specified PDF to specified printer under specified
     *
     * @param filePath      Path of PDF file
     * @param printerName   Printer name
     * @param jobName Print job name
     * @throws IOException
     * @throws PrinterException
     */

	public void printPDF(String filePath, PrintService ps, int copiesNumber) throws IOException, PrinterException {

		FileInputStream fileInputStream = new FileInputStream(filePath);
		byte[] pdfContent = new byte[fileInputStream.available()];
		fileInputStream.read(pdfContent, 0, fileInputStream.available());
		ByteBuffer buffer = ByteBuffer.wrap(pdfContent);		
		fileInputStream.close();

		final PDFFile pdfFile = new PDFFile(buffer);
		Printable printable = new Printable() {
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				int pagenum = pageIndex + 1;
				if ((pagenum >= 1) && (pagenum <= pdfFile.getNumPages())) {
					Graphics2D graphics2D = (Graphics2D) graphics;
					PDFPage page = pdfFile.getPage(pagenum);
					Rectangle imageArea = new Rectangle((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY(),	(int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
					graphics2D.translate(0, 0);
					PDFRenderer pdfRenderer = new PDFRenderer(page, graphics2D,	imageArea, null, null);
					try {
						page.waitForFinish();
						pdfRenderer.run();
					} catch (InterruptedException exception) {
						log.error("Error Printing page " + pagenum + ": " + exception.getMessage(),exception);
					}

					return PAGE_EXISTS;
				} else {
					return NO_SUCH_PAGE;
				}
			}
		};

		PrinterJob printJob = PrinterJob.getPrinterJob();

		// Identify Number of Copies.
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(copiesNumber));

		PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
		
		// printJob.setJobName(jobName);
		Book book = new Book();
		book.append(printable, pageFormat, pdfFile.getNumPages());
		printJob.setPageable(book);
		Paper paper = new Paper();

		if ( 1 <= pdfFile.getNumPages()) {
			PDFPage page = pdfFile.getPage(1);
			if ( page.getRotation() == 90 ){
				pageFormat.setOrientation(PageFormat.LANDSCAPE);
				paper.setSize(page.getHeight(), page.getWidth() );
			}
			else{
				paper.setSize(page.getWidth(), page.getHeight());
			}
			log.info("Paper orientation: " +  (page.getRotation() == 90 ? "Landscape" : "Portrait"));
		}
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
		pageFormat.setPaper(paper);
		
		printJob.setPrintService(ps);
		printJob.print(aset);
	}
}
