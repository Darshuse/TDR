package monitoring.test;

import java.util.Date;
import java.util.List;

import org.bouncycastle.util.IPAddress;
import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.Assert;

import com.eastnets.domain.SortOrder;
import com.eastnets.domain.monitoring.Alliance;
import com.eastnets.domain.monitoring.AuditLog;
import com.eastnets.domain.monitoring.ErrorMessage;
import com.eastnets.domain.monitoring.ReportingDBInfo;
import com.eastnets.domain.monitoring.Statistics;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.service.monitoring.MonitoringService;

import enReporting.test.base.BaseTestApp;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "Test_Files/Monitoring_Test.xls", loaderType = LoaderType.EXCEL)
public class MonitoringServiceTest extends BaseTestApp {

	private static MonitoringService monitoringService;

	@BeforeClass
	public static void init() {

		monitoringService = (MonitoringService) getContext().getBean("monitoringService");

	}

	@Test
	public void testGetUpdatedMessages(@Param(name = "USERNAME") String username,
			@Param(name = "SORT_ORDER") String order, @Param(name = "From") Long from, @Param(name = "To") Long to) {

		SortOrder sortOrder = (order.equalsIgnoreCase("ascending") ? SortOrder.ascending : SortOrder.descending);

		List<UpdatedMessage> updatedMessages = monitoringService.getUpdatedMessages(username, sortOrder, from, to);
		Assert.notNull(updatedMessages);
	}

	@Test
	public void testGetStatistics(@Param(name = "USERNAME") String username) {
		Statistics statistics = monitoringService.getStatistics(username);
		Assert.notNull(statistics);
	}

	@Test
	public void testGetTraces(@Param(name = "USERNAME") String username) {
		List<ErrorMessage> traces = monitoringService.getTraces(username);
		Assert.notNull(traces);
		Assert.notEmpty(traces);
	}

	@Test
	public void testGetTracesbyFilter(@Param(name = "USERNAME") String username,
			@Param(name = "MODULE_FILTER") String moduleFilter, @Param(name = "LEVEL_FILTER") String levelFilter,
			@Param(name = "SORT_ORDER") String order, @Param(name = "From") Long from, @Param(name = "To") Long to) {

		SortOrder sortOrder = (order.equalsIgnoreCase("ascending") ? SortOrder.ascending : SortOrder.descending);

		List<ErrorMessage> traces = monitoringService.getTraces(username, moduleFilter, levelFilter, sortOrder, from,
				to);
		Assert.notNull(traces);
	}

	// LOADER', 'Archive', 'Restore', 'JrnlArchive
	@Test
	public void testGetAuditLogs(@Param(name = "LOGN_NAME_FILTER") String loginNameFilter,
			@Param(name = "PROGRAM_NAME_FILTER") String programNameFilter,
			@Param(name = "EVENT_FILTER") String eventFilter,
			@Param(name = "ACTION_FILTER") String actionFilter,
			@Param(name = "FROM_DATE_FILTER") Date fromDate,
			@Param(name = "TO_DATE_FILTER") Date toDate,
			@Param(name = "IPADDRESS_FILTER") String ipAddress,
			@Param(name = "SORT_ORDER") String order,
			@Param(name = "FROM") Long from, @Param(name = "To") Long to) {

		SortOrder sortOrder = (order.equalsIgnoreCase("ascending") ? SortOrder.ascending : SortOrder.descending);

		List<AuditLog> auditLogs = monitoringService.getAuditLogs(loginNameFilter, programNameFilter,eventFilter,actionFilter,
				fromDate,toDate,ipAddress,sortOrder, from, to);

		Assert.notNull(auditLogs);
		Assert.notEmpty(auditLogs);
	}

	@Test
	public void testGetReportingInfo(@Param(name = "USERNAME") String username) {
		ReportingDBInfo reportingDBInfo = monitoringService.getReportingDBInfo(username);
		Assert.notNull(reportingDBInfo);
		Assert.notNull(reportingDBInfo.getDatabaseVersion());
		Assert.notNull(reportingDBInfo.getLastDatabaseUpdate());
	}

	@Test
	public void testGetAlliance(@Param(name = "USERNAME") String username, @Param(name = "ID") String id) {
		Alliance alliance = monitoringService.getAlliance(username, id);
		Assert.notNull(alliance);
		Assert.notNull(alliance.getInstanceName());
	}

	@Test
	public void testGetModulesList(@Param(name = "USERNAME") String username,
			@Param(name = "LEVEL_FILTER") String levelFilter) {

		List<String> moduleList = monitoringService.getModulesList(username, levelFilter);
		Assert.notNull(moduleList);
		Assert.notNull(moduleList);
	}

	@Test
	public void testGetUpdatedMessagesCount(@Param(name = "USERNAME") String username) {

		Assert.notNull(monitoringService.getUpdatedMessagesCount(username));
	}

	@Test
	public void testGetTracesCount(@Param(name = "USERNAME") String username,
			@Param(name = "MODULE_FILTER") String moduleFilter, @Param(name = "LEVEL_FILTER") String levelFilter) {

		Assert.notNull(monitoringService.getTracesCount(username, moduleFilter, levelFilter));

	}

	@Test
	public void testGetLoginNamesList(@Param(name = "USENRAME") String username,
			@Param(name = "PROGRAM_NAME_FILTER") String programNameFilter,
			@Param(name = "EVENT_FITLER") String eventFilter) {

		Assert.notNull(monitoringService.getLoginNamesList(programNameFilter, eventFilter));

	}

	@Test
	public void testGetProgramsNamesList(@Param(name = "USERNAME") String username,
			@Param(name = "LOGIN_NAME_FILTER") String loginNameFilter,
			@Param(name = "EVENT_FILTER") String eventFilter) {

		List<String> programsNamesList = monitoringService.getProgramsNamesList(loginNameFilter, eventFilter);
		Assert.notNull(programsNamesList);
		Assert.notEmpty(programsNamesList);
	}

	@Test
	public void testGetEventsList(@Param(name = "USERNAME") String username,
			@Param(name = "LOGIN_NAME_FILTER") String loginNameFilter,
			@Param(name = "PROGRAM_NAME_FILTER") String programNameFilter) {

		List<String> eventList = monitoringService.getEventsList(loginNameFilter, programNameFilter);
		Assert.notNull(eventList);
		Assert.notEmpty(eventList);

	}

	@Test
	public void testGetLevelsList(@Param(name = "USERNAME") String username,
			@Param(name = "MODULE_FILTER") String moduleFilter) {

		List<String> levelList = monitoringService.getLevelsList(username, moduleFilter);
		Assert.notNull(levelList);
		Assert.notEmpty(levelList);

	}

	@Test
	public void testGetAuditLogsCount(@Param(name = "LOGIN_NAME_FILTER") String loginNameFilter,
			@Param(name = "PROGRAM_NAME_FILTER") String programNameFilter,
			@Param(name = "EVENT_FILTER") String eventFilter,
			@Param(name = "ACTION_FILTER") String actionFilter,
			@Param(name = "FROM_DATE_FILTER") Date fromDate,
			@Param(name = "TO_DATE_FILTER") Date toDate,
			@Param(name = "IPADDRESS_FILTER") String ipAddress) {

		Assert.notNull(monitoringService.getAuditLogsCount(loginNameFilter, programNameFilter, eventFilter,actionFilter,fromDate,toDate,ipAddress));
	}

	@Test
	public void testGetFilterAuditLogs(@Param(name = "showDetails") boolean showDetails,
			@Param(name = "LOGIN_NAME_FILTER") String loginNameFilter,
			@Param(name = "PROGRAM_NAME_FILTER") String programNameFilter,
			@Param(name = "EVENT_FILTER") String eventFilter,
			@Param(name = "ACTION_FILTER") String actionFilter,
			@Param(name = "FROM_DATE_FILTER") Date fromDate,
			@Param(name = "TO_DATE_FILTER") Date toDate,
			@Param(name = "IPADDRESS_FILTER") String ipAddress,
			@Param(name = "SORT_ORDER") String order,
			@Param(name = "FROM") Long from, @Param(name = "TO") Long to) {

		SortOrder sortOrder = (order.equalsIgnoreCase("ascending") ? SortOrder.ascending : SortOrder.descending);

		List<AuditLog> auditLogs = monitoringService.getAuditLogFile(showDetails,loginNameFilter, programNameFilter,eventFilter,actionFilter,
				fromDate,toDate,ipAddress,sortOrder);

		Assert.notNull(auditLogs);
		Assert.notEmpty(auditLogs);
	}
}
