package reporting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.Assert;

import com.eastnets.domain.reporting.Parameter;
import com.eastnets.domain.reporting.Report;
import com.eastnets.domain.reporting.ReportSet;
import com.eastnets.domain.reporting.ReportSetParameter;
import com.eastnets.domain.reporting.ReportSetParameterTypeFactory;
import com.eastnets.service.reporting.ReportingService;

import enReporting.test.base.BaseTestApp;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "Test_Files/Report_Test.xls", loaderType = LoaderType.EXCEL)
public class ReportTest extends BaseTestApp {
	private ReportingService reportingService;

	@Before
	public void init() {
		reportingService = (ReportingService) getContext().getBean("reportingService");
	}

	@Test
	public void testGetReportsByCategory(@Param(name = "category") String category,
			@Param(name = "reportExpected") String reportExpected) {
		boolean validCategory = false;
		String userName = getLoggedInUser();
		List<Report> reportsList = reportingService.getReports(userName, category);
		Assert.notNull(reportsList);
		Assert.notEmpty(reportsList);
		for (Report report : reportsList) {
			if (report.getName().equalsIgnoreCase(reportExpected)) {
				validCategory = true;
				break;

			}
			assert (validCategory == true);
		}
	}

	@Test
	public void testGetReportByReportId(@Param(name = "id") Long id,
			@Param(name = "reportExpected") String reportExpected) {
		String userName = getLoggedInUser();
		Report report = reportingService.getReport(userName, id);
		assertEquals(reportExpected, report.getName());
	}

	@Test
	public void testGetReports() {
		String userName = getLoggedInUser();
		List<Report> reports = reportingService.getReports(userName);
		Assert.notNull(reports);
		Assert.notEmpty(reports);
	}

	@Test
	public void testGetCriteraName(@Param(name = "id") String id,
			@Param(name = "criteraExpected") String criteraExpected) {
		assertEquals(criteraExpected, reportingService.getCriteriaName(id));

	}

	@Test
	public void testGetReportsCategories() {
		String userName = getLoggedInUser();
		List<String> categories = reportingService.getReportsCategories(userName);
		Assert.notNull(categories);
		Assert.notEmpty(categories);
	}

	// @Test
	// public void testAddParam(@Param(name = "reportID") Long
	// reportID,@Param(name = "paramID") Long paramID,@Param(name = "newParam")
	// String newParam){
	// boolean isValidAdd=false;
	// String userName = getLoggedInUser();
	// Report report = reportingService.getReport(userName, reportID);
	// List<Parameter> parametersList = new ArrayList<Parameter>();
	// ReportSetParameter reportSetParameter =
	// ReportSetParameterTypeFactory.getDefaultReportSetParameter();//default
	// reportSetParameter.setValue(newParam);
	// reportSetParameter.setId(paramID);
	// reportSetParameter.setName(newParam);
	// reportSetParameter.setType(new Long(1));
	// reportSetParameter.setFirstName("mohmmad");
	// reportSetParameter.setSecondName("kassab");
	// reportSetParameter.setMaxLengthValue(20);
	// reportSetParameter.setMinLengthValue(0);
	// parametersList.add(reportSetParameter);
	// report.setReportParametersList(parametersList);
	// reportingService.addReportParameters(getLoggedInUser(), report);
	// for(Parameter
	// parameters:reportingService.getReportParameters("",report)){
	// if(parameters.getName().equalsIgnoreCase(newParam)){
	// isValidAdd=true;
	// }
	// }
	// assert(isValidAdd==true);
	// }

	@Test
	public void testGetReportParameters(@Param(name = "id") Long id,
			@Param(name = "expectedParamNumber") int expectedParamNumber) {
		String userName = getLoggedInUser();
		Report report = reportingService.getReport(userName, id);
		List<Parameter> reportParameters = reportingService.getReportParameters("", report);
		Assert.notNull(reportParameters);
		Assert.notEmpty(reportParameters);
		assert (reportParameters.size() == expectedParamNumber);
	}

	@Test
	public void testAddNewCriteria(@Param(name = "id") Long id, @Param(name = "criteriaName") String criteriaName) {
		String userName = getLoggedInUser();
		Report report = reportingService.getReport(userName, id);
		ReportSet reportSet = new ReportSet();
		reportSet.setReportId(report.getId());
		reportSet.setName(criteriaName);
		reportingService.addReportSet(userName, reportSet);
		reportSet = reportingService.getReportSet(userName, reportSet);
		assertEquals(reportSet.getName(), criteriaName);
	}

	@Test
	public void testGetCriteriaByName(@Param(name = "criteriaName") String criteriaName) {
		String userName = getLoggedInUser();
		ReportSet reportSet = new ReportSet();
		reportSet.setName(criteriaName);
		assertEquals(reportingService.getReportSetByName(userName, reportSet).getName(), criteriaName);
	}

	@Test
	public void testDeleteCriteria(@Param(name = "criteriaID") Long criteriaID) {
		String userName = getLoggedInUser();
		ReportSet reportSet = new ReportSet();
		reportSet.setId(criteriaID);
		reportingService.deleteReportSet(userName, reportSet);
	}
}
