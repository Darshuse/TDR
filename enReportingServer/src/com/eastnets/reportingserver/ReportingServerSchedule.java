package com.eastnets.reportingserver;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.eastnets.application.BaseApp;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.reportingserver.reportGenerators.DatabaseReportGenerator;
import com.eastnets.reportingserver.reportRunners.DailyReportRrunner;
import com.eastnets.reportingserver.reportRunners.MonthlyReportRrunner;
import com.eastnets.reportingserver.reportRunners.WeeklyReportRrunner;

public class ReportingServerSchedule extends BaseApp implements Runnable {

	/**
	 * 
	 */
	static Logger log = Logger.getLogger(DatabaseReportGenerator.class.getName());
	private static final long serialVersionUID = 1640641629599323227L;
	public static ReportingServerApp reportingServerApp;
	public static ReportingServerSchedule rss;

	private static final String DB_PROPERTY_FILE = "/scheduleConfig.properties";
	private static final String ECF = "ecf";
	private static final String DBNAME = "dbname";
	private static final String DBTYPE = "dbtype";
	private static final String REPORT_TEMPLATE_PATH = "rtemplatepath";
	private static final String REPORT_PATH = "rpath";
	private static final String MAIL_SMTP = "smtp";
	private static final String MAIL_FROM = "from";
	private static final String MAIL_USERNAME = "MU";
	private static final String MAIL_PASSWORD = "MP";

	@SuppressWarnings("unused")
	private String ecf, dbname, dbtype, reportTemplatePath, reportPath;
	@SuppressWarnings("unused")
	private String mailSMTP, mailFrom, mailUsername, mailPassword;

	public List<CriteriaSchedule> DailyScheduleList = new ArrayList<CriteriaSchedule>();
	public List<CriteriaSchedule> WeeklyScheduleList = new ArrayList<CriteriaSchedule>();
	public List<CriteriaSchedule> MonthlyScheduleList = new ArrayList<CriteriaSchedule>();
	private User loggedInUser;

	static {
		reportingServerApp = new ReportingServerApp();
		rss = new ReportingServerSchedule();
	}

	public ReportingServerSchedule() {
	}

	public void initConfig() throws Exception {

		Properties dbProbFile = new Properties();
		dbProbFile.load(new FileInputStream(System.getenv("EASTNETS_CONFIG_HOME").replaceAll("file:", "") + DB_PROPERTY_FILE));

		this.ecf = dbProbFile.getProperty(ECF);
		this.dbname = dbProbFile.getProperty(DBNAME);
		this.dbtype = dbProbFile.getProperty(DBTYPE);
		this.reportTemplatePath = dbProbFile.getProperty(REPORT_TEMPLATE_PATH);
		this.reportPath = dbProbFile.getProperty(REPORT_PATH);
		this.mailSMTP = dbProbFile.getProperty(MAIL_SMTP);
		this.mailFrom = dbProbFile.getProperty(MAIL_FROM);
		this.mailUsername = dbProbFile.getProperty(MAIL_USERNAME);
		this.mailPassword = dbProbFile.getProperty(MAIL_PASSWORD);

	}

	public ReportingServerSchedule(AppConfigBean appConfigBean) {
		if (appConfigBean != null) {
			try {
				rss.init(appConfigBean);
				String userName = appConfigBean.getUsername();
				// boolean userHasRole = rss.getServiceLocater().getAdminService().checkRole("SIDE_REPORT");

				if (rss.getServiceLocater().getAdminService().checkRole("SIDE_REPORT")) {
					rss.loggedInUser = rss.getServiceLocater().getAdminService().getUserWithoutRoles(userName);
					if (rss.loggedInUser == null) {
						reportingServerApp.displayUsage("Provided user is not a reporting users.");
						return;
					} else if (rss.loggedInUser != null && rss.loggedInUser.getApprovalStatus().getId() != 0) {
						log.error(rss.loggedInUser.getApprovalStatus().getDescription());
						return;
					}
				} else {
					reportingServerApp.displayUsage("Provided user does not have reporting role.");
					return;
				}

				Thread t = new Thread(rss);
				t.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
		} else {
			System.err.println("missing or invalid parameter");
		}
	}

	@Override
	public void run() {

		try {

			while (true) {

				AppConfigBean appConfigBean = ReportingServerApp.getAppConfigBean();
				ReportRunner runner = new DailyReportRrunner();

				runner.separateCriteriaSchedule();

				this.DailyScheduleList = runner.DailyScheduleList;
				this.WeeklyScheduleList = runner.WeeklyScheduleList;
				this.MonthlyScheduleList = runner.MonthlyScheduleList;

				ReportGenerator reportGenerator = reportingServerApp.initReportGenerator(appConfigBean);
				Calendar calendar = Calendar.getInstance();
				// System.out.println(calendar.get(Calendar.MINUTE));

				if (this.DailyScheduleList != null && !this.DailyScheduleList.isEmpty())
					runner.runner(reportGenerator, calendar, loggedInUser);

				if (this.WeeklyScheduleList != null && !this.WeeklyScheduleList.isEmpty()) {
					runner = null;
					runner = new WeeklyReportRrunner();
					setRunnerList(runner);
					runner.runner(reportGenerator, calendar, loggedInUser);
				}

				if (this.MonthlyScheduleList != null && !this.MonthlyScheduleList.isEmpty()) {
					runner = null;
					runner = new MonthlyReportRrunner();
					setRunnerList(runner);
					runner.runner(reportGenerator, calendar, loggedInUser);
				}

				runner = null;
				Thread.sleep(60 * 1000);// Sleep 1 Minute Before Next Run
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void setRunnerList(ReportRunner runner) {

		runner.DailyScheduleList = this.DailyScheduleList;
		runner.WeeklyScheduleList = this.WeeklyScheduleList;
		runner.MonthlyScheduleList = this.MonthlyScheduleList;
	}

}
