package com.eastnets.test.reporting.testcase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.Assert;

import com.eastnets.config.ConfigBean;
import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.service.common.JasperFillDetails;
import com.eastnets.service.common.ReportService;
import com.eastnets.service.common.ReportService.ReportTypes;
import com.eastnets.service.reporting.ReportFactory;
import com.eastnets.service.reporting.ReportingService;
import com.eastnets.test.BaseTest;

public class ReportingTest extends BaseTest {


	
//	private String directoryName = "E:\\WebClient2.6\\Reports 3.1\\reports_sql";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1188505005668714709L;

	protected void readDirectory(File directory,List<Report> reportsList){
		
		if(directory != null){
			
			if(directory.isDirectory()){
				File[] listFiles = directory.listFiles();
				for (File file : listFiles) {
					readDirectory(file, reportsList);
				}
			}
			else{
				String absolutePath = directory.getAbsolutePath();
				if(absolutePath.endsWith(".jrxml")){
					Report report = ReportFactory.create(absolutePath);
					if(report != null){
						reportsList.add(report);
						
					}
				}
			}
		}
		
	}

	protected void saveToFile(Report report, ByteArrayOutputStream exportReport) {
		if(exportReport.size()>0){
		
			ConfigBean configBean = this.getConfigBean();
			String directoryName = configBean.getReportsDirectoryPath();
			
			String pdfFileName = String.format("%s%s%s%s", directoryName, File.separator,report.getFileName(), ".pdf");
			File file = new File(pdfFileName);
			
			FileOutputStream fileOutputStream = null;
			try {
				if(file.exists() == false){
					file.createNewFile();
				}else{
					file.delete();
					file.createNewFile();
				}
				
				fileOutputStream = new FileOutputStream(pdfFileName);
				exportReport.writeTo(fileOutputStream);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(fileOutputStream != null){
					try {
						fileOutputStream.close();
					} catch (IOException e) {
	
						e.printStackTrace();
					}
				}
			}
		}
	}
	/*
	@Test
	public void readParamters(){
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		
		List<Report> reportsList = new ArrayList<Report>();
		File directory = new File(directoryName);
		readDirectory(directory, reportsList);
		
		Assert.notNull(reportsList);
		Assert.notEmpty(reportsList);
		
		for (Report report : reportsList) {
			reportingService.addReportParameters(getLoggedInUser(), report);

		}		
	}
	
	@Test
	public void testDeleteReportSetValues() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
			
			List<ReportSet> reportSets = reportingService.getReportSets(userName, report);
			
			for (ReportSet reportSet : reportSets) {				
				reportingService.deleteReportSetValues(userName, reportSet);
			}
		}
	}
	
	@Test
	public void testDeleteReportSet() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
			
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);			
			for (ReportSet reportSet : reportSetsList) {				
				reportingService.deleteReportSet(userName, reportSet);
			}
		}
	}



	@Test
	public void testAddReport(){
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reportsList = new ArrayList<Report>();
		File directory = new File(directoryName);
		readDirectory(directory, reportsList);
		
		Assert.notNull(reportsList);
		Assert.notEmpty(reportsList);
		
		for (Report report : reportsList) {
			
			reportingService.addReport(userName, report);
		}		
	}
	*/
	/*
	@Test
	public void testAddReportParameters() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		File  directory = new File(directoryName);
		List<Report> reportsList = new ArrayList<Report>();
		readDirectory(directory, reportsList);
		Assert.notEmpty(reportsList);
		
		for (Report report : reportsList) {
			reportingService.addReportParameters(userName, report );
		}
	}

	@Test
	public void testAddReportSet() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		Assert.notNull(reports);
		Assert.notEmpty(reports);
		for (Report report : reports) {
			ReportSet reportSet = new ReportSet();
			reportSet.setReportId(report.getId());
			reportSet.setName("Test_" + report.getName());
			reportingService.addReportSet(userName, reportSet);
			reportSet = reportingService.getReportSet(userName, reportSet);
			Assert.notNull(reportSet);
		}
	}


	
	@Test
	public void testAddReportSetValues() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
			
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
			Assert.notNull(reportSetsList);
			Assert.notEmpty(reportSetsList);

			List<Parameter> reportParametersList = reportingService.getReportParameters(userName, report);
			Assert.notNull(reportParametersList);
			Assert.notEmpty(reportParametersList);
			
			Calendar fromDate = Calendar.getInstance();
			fromDate.set(2009, Calendar.JANUARY, 1,0,0);
			Calendar toDate = Calendar.getInstance();
			String formatDate =null;
			
			String dateFormatString = "dd/MM/yyyy hh:mm:ss a";
			DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
			String dateTimeFormatString ="dd/MM/yyyy";
			DateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatString);
			
			for (ReportSet reportSet : reportSetsList) {
								
				for (Parameter parameter : reportParametersList) {
					ReportSetParameter reportSetParametersValue = (ReportSetParameter)parameter;
					String name = null;
					switch(reportSetParametersValue.getType().intValue()){
						
					case Constants.REPORT_PARAMTER_TYPE_CURRENCY_AMOUNT:
						reportSetParametersValue.setFirstValue(null);
						reportSetParametersValue.setSecondValue(null);
						break;
						
					case Constants.REPORT_PARAMTER_TYPE_DATE_TIME:
						name = reportSetParametersValue.getName().toUpperCase().trim();
						
							formatDate = dateTimeFormat.format(fromDate.getTime());
							reportSetParametersValue.setFirstValue(formatDate);
							formatDate = dateTimeFormat.format(toDate.getTime());
							reportSetParametersValue.setSecondValue(formatDate);
						break;
					case Constants.REPORT_PARAMTER_TYPE_DATE:
						name = reportSetParametersValue.getName().toUpperCase().trim();
						
						formatDate = dateFormat.format(fromDate.getTime());
						reportSetParametersValue.setFirstValue(formatDate);
						formatDate = dateFormat.format(toDate.getTime());
						reportSetParametersValue.setSecondValue(formatDate);
						break;
						
					case Constants.REPORT_PARAMTER_TYPE_NUMBER:
						reportSetParametersValue.setValue(null);
						break;
					case Constants.REPORT_PARAMTER_TYPE_STRING:
						
						name = reportSetParametersValue.getName().toUpperCase().trim();
						if(name.equals("MESSAGE TYPE")){
							reportSetParametersValue.setFirstValue(null);
						}else if(name.equals("SENDER")){
							reportSetParametersValue.setFirstValue(null);
						}else if(name.equals("RECEIVER")){
							reportSetParametersValue.setFirstValue(null);
						}else if(name.equals("Reference")){
							reportSetParametersValue.setFirstValue(null);
						}else if(name.equals("Format")){
							reportSetParametersValue.setFirstValue(null);
						}
						break;
					default:
							
					}
					
				}
				reportingService.addReportSetValues(userName, reportSet, reportParametersList);
			}
		}		
	}
	*/
	/*
	 
	 @Test
	public void testReportFactory() {

		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		List<Report> reportsList = new ArrayList<Report>();
		File directory = new File(directoryName);
		readDirectory(directory, reportsList);
		
		for (Report report : reportsList) {
			
			Assert.notNull(report);
			
			List<Parameter> reportParametersList = report
			.getReportParametersList();
			
			
			String message = String.format("report Id: %d, report name: %s",
					report.getId(), report.getName());
			System.out.println(message);
			Assert.notNull(reportParametersList);
			Assert.notEmpty(reportParametersList);
			
			for (Parameter parameter : reportParametersList) {
				
				ReportSetParameter reportSetParameter = (ReportSetParameter) parameter;
				message = String.format("parameter description %s, parameter name: %s,parameter value: %s, first name: %s, first value: %s, second name: %s, second value: %s, mandatory: %s",
						reportSetParameter.getDescription(),
						reportSetParameter.getName(),
						reportSetParameter.getValue(),
						reportSetParameter.getFirstName(),
						reportSetParameter.getFirstValue(),
						reportSetParameter.getSecondName(),
						reportSetParameter.getSecondValue(),
						reportSetParameter.isMandatory());
				System.out.println(message);
			}
		}
	}
 */
	/*
	@Test
	public void testGetReports() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		
		Assert.notNull(reports);
		Assert.notEmpty(reports);
	}

	
	@Test
	public void testGetReportByReportId() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		
		for (Report report : reports) {
			Report report2 = reportingService.getReport(userName, report.getId());
			Assert.notNull(report2);
			
		}
	}

	
	@Test
	public void testGetReportByReportName() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		
		for (Report report : reports) {
			Report report2 = reportingService.getReport(userName, report.getName());
			Assert.notNull(report2);
			
		}
	}


	@Test
	public void testGetReportParametersByReport() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		
		Assert.notNull(reports);
		Assert.notEmpty(reports);
		
		for (Report report : reports) {
			List<Parameter> reportParameters = reportingService.getReportParameters(userName, report);
			Assert.notNull(reportParameters);
			Assert.notEmpty(reportParameters);
		}
		
	}

	@Test
	public void testGetReportParametersByReportId() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		
		List<Report> reports = reportingService.getReports(userName);
		
		Assert.notNull(reports);
		Assert.notEmpty(reports);
		
		for (Report report : reports) {
			List<Parameter> reportParameters = reportingService.getReportParameters(userName, report.getId());
			Assert.notNull(reportParameters);
			Assert.notEmpty(reportParameters);
		}
	}

	@Test
	public void testGetReportSetsByReport() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
			
			List<ReportSet> reportSets = reportingService.getReportSets(userName, report);
			
			Assert.notNull(reportSets);
			Assert.notEmpty(reportSets);
		}
	}
	
	@Test
	public void testGetReportSetByReportSet() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
			ReportSet reportSet = new ReportSet();
			reportSet.setReportId(report.getId());
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
			Assert.notNull(reportSetsList);
			Assert.notEmpty(reportSetsList);
			for (ReportSet reportSet2 : reportSetsList) {
				reportSet2 = reportingService.getReportSet(userName, reportSet2);
				Assert.notNull(reportSet2);
			}
		}
	}
		
	@Test
	public void TestGetReportSetValuesByReportSet() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);

		for (Report report : reports) {
						
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
			Assert.notNull(reportSetsList);
			Assert.notEmpty(reportSetsList);
			for (ReportSet reportSet : reportSetsList) {
				List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
				Assert.notNull(reportSetValues);
				Assert.notEmpty(reportSetValues);
				ReportParamtersFactory factory = new JasperReportParamtersFactory();
				List<Parameter> createReportParameter = factory.convertReportSetToParamters(reportSetValues);
				Assert.notNull(createReportParameter);
				Assert.notEmpty(createReportParameter);
			}
		}		
	}
	
	
	@Test
	public void testUpdateReportSetValues() {
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);
		
		for (Report report : reports) {
			
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
			Assert.notNull(reportSetsList);
			Assert.notEmpty(reportSetsList);
			for (ReportSet reportSet : reportSetsList) {
				List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
				Assert.notNull(reportSetValues);
				Assert.notEmpty(reportSetValues);
				reportingService.updateReportSetValues(userName, reportSet,reportSetValues);
				
				
			}
		}
	}

	@Test
	public void testGetReportsCategories(){
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<String> categories = reportingService.getReportsCategories(userName);
		Assert.notNull(categories);
		Assert.notEmpty(categories);
		for (String string : categories) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGetReportsByCategory(){
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		String userName = getLoggedInUser();
		List<String> categories = reportingService.getReportsCategories(userName);
		Assert.notNull(categories);
		Assert.notEmpty(categories);
		
		for (String category : categories) {
			List<Report> reportsList = reportingService.getReports(userName, category);
			Assert.notNull(reportsList);
			Assert.notEmpty(reportsList);
		}
	}
*/
	 
	@Test
	public void TestGenerateAllJasperReport() {

		//this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		ReportService reportServiceGenerator = this.getServiceLocater().getReportService();
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);
		
		for (Report report : reports) {
			
			Assert.notNull(report);
		
			List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
			String message = report.getId()+ ", " + report.getName();
			//Assert.notNull(reportSetsList,  message);
			//Assert.notEmpty(reportSetsList, message);
			
			if(reportSetsList == null || reportSetsList.isEmpty()){
				continue;
			}
			
		
			Map<String, Object> reportParameters = null;
			for (ReportSet reportSet : reportSetsList) {
				List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
				
				Assert.notNull(reportSetValues, message);
				Assert.notEmpty(reportSetValues,message);
				
				List<Parameter> reportParametersList = reportingService.convertReportSetValuesToParamters(reportSetValues);
				Assert.notNull(reportParametersList, message);
				Assert.notEmpty(reportParametersList, message);
				
				reportParameters = reportingService.convertParametersToJasperParamters(report, reportParametersList, ReportTypes.pdf,null);
				Assert.notNull(reportParameters, message);
				Assert.notEmpty(reportParameters, message);			

				String reportName = reportingService.getReportPath(report);
				ByteArrayOutputStream exportReport;
				try {
					JasperFillDetails jasperFillDetails = new JasperFillDetails();
					exportReport = reportServiceGenerator.exportReport(jasperFillDetails,userName,reportName, ReportService.ReportTypes.pdf.name(), reportParameters,"2010", null);
					Assert.notNull(exportReport, message);
					saveToFile(report, exportReport);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//TODO we don't have a user object, passing null is bad for report generation

				this.alterSession();
			}
		}
	}
	/*
	@Test
	public void testGeneratReportById(){
		
		this.directoryName = this.getServiceLocater().getConfig().getReportsDirectoryPath();
		ReportingService reportingService = this.getServiceLocater().getReportingService();
		ReportService reportServiceGenerator = this.getServiceLocater().getReportService();
		String userName = getLoggedInUser();
		Report report = reportingService.getReport(userName, 7000L);
		
		Assert.notNull(report);
		
		Map<String, Object> createJasperReportParameter = null;
		List<ReportSet> reportSetsList = reportingService.getReportSets(userName, report);
		Assert.notNull(reportSetsList, report.getName());
		Assert.notEmpty(reportSetsList, report.getName());
		
		for (ReportSet reportSet : reportSetsList) {
			List<Parameter> reportSetValues = reportingService.getReportSetValues(userName, reportSet);
			
			Assert.notNull(reportSetValues, report.getName());
			Assert.notEmpty(reportSetValues,report.getName());
			
			List<Parameter> reportParametersList = reportingService.convertReportSetValuesToParamters(reportSetValues);
			Assert.notNull(reportParametersList, report.getName());
			Assert.notEmpty(reportParametersList, report.getName());
			
			createJasperReportParameter = reportingService.convertParametersToJasperParamters(report, reportParametersList, ReportTypes.pdf);
			Set<Entry<String, Object>> entrySet = createJasperReportParameter.entrySet();
			for (Entry<String,Object> e : entrySet) {
				System.out.printf("%s, %s\n",e.getKey(), e.getValue());
			}
			
			Assert.notNull(createJasperReportParameter, report.getName());
			Assert.notEmpty(createJasperReportParameter, report.getName());		
			
			String reportName = reportingService.getReportPath(report);
			ByteArrayOutputStream exportReport = reportServiceGenerator.exportReport(getLoggedInUser(),reportName, ReportService.ReportTypes.pdf.name(), createJasperReportParameter);
			Assert.notNull(exportReport);
			saveToFile(report, exportReport);		
		}
	}
*/
}

