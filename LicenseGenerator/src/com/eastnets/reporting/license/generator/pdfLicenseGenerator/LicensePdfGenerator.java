package com.eastnets.reporting.license.generator.pdfLicenseGenerator;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.Product;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class LicensePdfGenerator {
	// public static void main(String args[]) throws SAXException, IOException, ParserConfigurationException {
	// public void generateLicensePdf(String xmlFilePath, String cutomerName, String contactName, String concerns) throws ParserConfigurationException, SAXException, IOException {
	// File file = new File(xmlFilePath);
	// DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	// Document document = (Document) documentBuilder.parse(file);
	// List<BicCode> bics = new ArrayList<BicCode>();
	// List<MyProduct> products = new ArrayList<MyProduct>();
	//
	// for (int i = 0; i < document.getElementsByTagName("bic").getLength(); i++) {
	//
	// String bic_code = document.getElementsByTagName("bic").item(i).getTextContent();
	// String[] parts = bic_code.split("\n");
	// String space = parts[0];
	// String code = parts[1];
	// String band = parts[2];
	// BicCode bicCode = new BicCode();
	// bicCode.setCode(code);
	// bicCode.setBand(band);
	//
	// bics.add(bicCode);
	// }
	//
	// for (int i = 0; i < document.getElementsByTagName("product").getLength(); i++) {
	//
	// String productString = document.getElementsByTagName("product").item(i).getTextContent();
	// String[] parts = productString.split("\n");
	// String space = parts[0];
	// String code = parts[1];
	// String description = parts[2];
	// String space2 = parts[3];
	// String licenseBoolean = parts[4];
	// boolean licensed = Boolean.parseBoolean(licenseBoolean);
	// MyProduct product = new MyProduct(code, description, null, licensed);
	// products.add(product);
	// }
	//
	// // String customerName = document.getElementsByTagName("customerName").item(0).getTextContent();
	// // String contactName = document.getElementsByTagName("contactName").item(0).getTextContent();
	// // String concerns = document.getElementsByTagName("concerns").item(0).getTextContent();
	// String userNumber = document.getElementsByTagName("users").item(0).getTextContent();
	// String lsa = document.getElementsByTagName("left").item(0).getTextContent();
	// String rsa = document.getElementsByTagName("right").item(0).getTextContent();
	// LicensePdfGenerator licensePdfGenerator = new LicensePdfGenerator();
	// licensePdfGenerator.pdfGenration(bics, cutomerName, contactName, concerns, userNumber, lsa, rsa, products, xmlFilePath);
	//
	// }

	// public void pdfGenration(List<BicCode> bicString, String customerName, String contactName, String concerns, String userNumber, String lsa, String rsa, List<MyProduct> products, String path) {
	//
	//
	// }

	private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

		// create a new cell with the specified Text and Font
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		// set the cell alignment
		cell.setVerticalAlignment(align);
		// set the cell column span in case you want to merge two or more cells
		cell.setColspan(colspan);
		// in case there is no text and you wan to create an empty row
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}
		if (align == Element.ALIGN_RIGHT) {
			cell.setBackgroundColor(BaseColor.GRAY);
		}
		// add the call to the table
		table.addCell(cell);

	}

	public void generateLicensePdf(List<BicCode> bics, List<Product> productsSelected, List<Product> trafficSelected, String fileNameAndType, String cutomerName, String contactName, String concerns, String userNumber, String lsa, String rsa,
			String maxConn) {

		com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4);
		doc.addTitle(" Licenes PDF");
		try {
			fileNameAndType = fileNameAndType.replace(".xml", ".pdf");
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(fileNameAndType));
			writer.setPageEvent(new Footer());
			doc.open();
			Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(0, 0, 0));
			Font bf12 = new Font(FontFamily.TIMES_ROMAN, 9);

			BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/images/logo.png"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			Image image1 = Image.getInstance(baos.toByteArray());
			image1.setAlignment(Element.ALIGN_CENTER);
			image1.scaleAbsolute(150, 30);
			doc.add(image1);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);
			doc.add(Chunk.NEWLINE);

			Date date = new Date();
			String pattern = "MM/dd/yyyy";
			DateFormat df = new SimpleDateFormat(pattern);
			String todayAsString = df.format(date);
			Paragraph paragraph = new Paragraph(" Date: " + todayAsString + "\r\n Customer Name: " + cutomerName + "\r\n Contact Name: " + contactName + "\r\n Concerns: " + concerns + "\r\n");
			paragraph.setFont(bf12);
			doc.add(Chunk.NEWLINE);
			doc.add(paragraph);
			doc.add(Chunk.NEWLINE);

			PdfPTable Producttable = new PdfPTable(2);
			Producttable.setWidthPercentage(90f);
			insertCell(Producttable, " Module ", Element.ALIGN_RIGHT, 1, bfBold12);
			insertCell(Producttable, " Expiration Date ", Element.ALIGN_RIGHT, 1, bfBold12);
			LinkedList<Product> allSelectedProduct = new LinkedList<Product>();
			allSelectedProduct.addAll(productsSelected);
			allSelectedProduct.addAll(trafficSelected);
			allSelectedProduct.remove(trafficSelected.get(0));
			for (int i = 0; i < allSelectedProduct.size(); i++) {

				if (allSelectedProduct.get(i).getDescription() != null) {
					insertCell(Producttable, allSelectedProduct.get(i).getDescription(), Element.ALIGN_LEFT, 1, bf12);
				}

				if (allSelectedProduct.get(i).getExpirationDate() == null) {
					insertCell(Producttable, " ", Element.ALIGN_LEFT, 1, bf12);
				} else {

					insertCell(Producttable, allSelectedProduct.get(i).getExpirationDate().toString(), Element.ALIGN_LEFT, 1, bf12);
				}

			}
			doc.add(Chunk.NEWLINE);
			paragraph.add(Producttable);
			doc.add(Producttable);
			// // doc.setPageCount(doc.getPageNumber() + 1);
			// ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("For any further information about our services/products, please visit www.eastnets.com"), 240,
			// 30, 0);
			// ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + doc.getPageNumber()), 550, 30, 0);

			Paragraph Destinationsparagraph = new Paragraph("Licensed Destinations :");
			Destinationsparagraph.setFont(bf12);
			doc.add(Chunk.NEWLINE);
			doc.add(Destinationsparagraph);
			doc.add(Chunk.NEWLINE);
			PdfPTable table = null;
			if (bics.size() < 4) {
				int columns = bics.size() * 2;
				table = new PdfPTable(columns);

				for (int i = 0; i < bics.size(); i++) {
					insertCell(table, "BIC code", Element.ALIGN_RIGHT, 1, bfBold12);
					insertCell(table, "Band", Element.ALIGN_RIGHT, 1, bfBold12);
				}

			} else {
				table = new PdfPTable(8);
				insertCell(table, "BIC code", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "Band", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "BIC code", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "Band", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "BIC code", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "Band", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "BIC code", Element.ALIGN_RIGHT, 1, bfBold12);
				insertCell(table, "Band", Element.ALIGN_RIGHT, 1, bfBold12);

			}

			table.setWidthPercentage(90f);

			for(BicCode bicCode: bics) {
				if(bicCode!=null) {
					insertCell(table, bicCode.getBicCode(), Element.ALIGN_LEFT, 1, bf12);
					insertCell(table, String.valueOf(bicCode.getBandVolume()), Element.ALIGN_LEFT, 1, bf12);					
				}
			}
			
			/*
			 * In case the number of BICs less than 4, that means we don't need
			 * to complete the table with empty cells if needed, but if the number
			 * of BICs more or equal 4, that means we should fill the extra cells
			 * to complete the table. 
			 * ex: if we have 46 BIC, we will have 11 complete row, each row has 
			 * 	   4 BICs, and the row number 12 will have 2 BICs, so here I complete
			 * 	   the extra 4 cells (each 2 cells represent a BIC)
			 */
			for(int i=bics.size();i%4!=0 && bics.size()>=4 ;i++) {
				insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);				
				insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);	
			}
			

			paragraph.add(table);
			doc.add(table);
			doc.add(Chunk.NEWLINE);
			Paragraph licensed_environment_paragraph = new Paragraph("Licensed Environment : ");
			licensed_environment_paragraph.setFont(bf12);
			doc.add(Chunk.NEWLINE);
			doc.add(licensed_environment_paragraph);
			doc.add(Chunk.NEWLINE);
			PdfPTable licensedEnvironmentTable = new PdfPTable(2);
			licensedEnvironmentTable.setWidthPercentage(90f);
			insertCell(licensedEnvironmentTable, "Alliance Servers ", Element.ALIGN_RIGHT, 1, bfBold12);
			insertCell(licensedEnvironmentTable, maxConn, Element.ALIGN_LEFT, 1, bf12);
			insertCell(licensedEnvironmentTable, "Number of Users ", Element.ALIGN_RIGHT, 1, bfBold12);
			insertCell(licensedEnvironmentTable, userNumber, Element.ALIGN_LEFT, 1, bf12);

			paragraph.add(licensedEnvironmentTable);
			doc.add(licensedEnvironmentTable);

			Paragraph security_credentials_paragraph = new Paragraph("Default Security Credentials : ");
			doc.add(Chunk.NEWLINE);
			security_credentials_paragraph.setFont(bf12);
			doc.add(Chunk.NEWLINE);
			doc.add(security_credentials_paragraph);

			PdfPTable securityCredentoalsTable = new PdfPTable(2);
			securityCredentoalsTable.setWidthPercentage(90f);
			insertCell(securityCredentoalsTable, "Left Security Authorizer (LSA) Password: ", Element.ALIGN_RIGHT, 1, bfBold12);
			insertCell(securityCredentoalsTable, lsa, Element.ALIGN_LEFT, 1, bf12);
			insertCell(securityCredentoalsTable, "Right Security Authorizer (RSA) Password: ", Element.ALIGN_RIGHT, 1, bfBold12);
			insertCell(securityCredentoalsTable, rsa, Element.ALIGN_LEFT, 1, bf12);

			doc.add(Chunk.NEWLINE);
			paragraph.add(securityCredentoalsTable);
			doc.add(securityCredentoalsTable);

			// if (doc.getPageNumber() > 1) {
			// doc.setPageCount(doc.getPageNumber() + 1);
			// ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("For any further information about our services/products, please visit www.eastnets.com"), 240,
			// 30, 0);
			// ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + doc.getPageNumber()), 550, 30, 0);
			// }

		} catch (Exception e) {
			System.out.println(e);
		}
		doc.close();

	}

	class Footer extends PdfPageEventHelper {

		private int numPage;

		public void onStartPage(PdfWriter writer, Document document) {
			numPage++;
		}

		public void onEndPage(PdfWriter writer, Document document) {

			try {
				Rectangle page = document.getPageSize();

				PdfPTable footer = new PdfPTable(2);

				PdfPCell cellFooter = new PdfPCell(new Phrase("For any further information, please visit www.eastnets.com"));
				cellFooter.setHorizontalAlignment(Element.ALIGN_LEFT);
				cellFooter.setBorder(0);
				footer.addCell(cellFooter);

				cellFooter = new PdfPCell(new Phrase(String.format("page. %d", numPage)));
				cellFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cellFooter.setBorder(0);

				footer.addCell(cellFooter);

				footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

			} catch (Exception e) {
				throw new ExceptionConverter(e);
			}
		}
	}

	private BicCode getNextBic(Iterator<BicCode> iterator) {
		BicCode nextBic = null;
		if (iterator.hasNext()) {
			nextBic = iterator.next();
		}
		return nextBic;
	}

}
