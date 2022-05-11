package com.eastnets.reportingserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.reportingserver.reportBeans.ScheduleConstants;
import com.eastnets.reportingserver.reportUtil.Util;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.reporting.ReportingService;

public abstract class ReportRunner implements Runnable {

	public List<CriteriaSchedule> DailyScheduleList = new ArrayList<CriteriaSchedule>();
	public List<CriteriaSchedule> WeeklyScheduleList = new ArrayList<CriteriaSchedule>();
	public List<CriteriaSchedule> MonthlyScheduleList = new ArrayList<CriteriaSchedule>();

	public abstract void runner(ReportGenerator reportGenerator, Calendar calendar, User user) throws Exception;

	public void separateCriteriaSchedule() throws Exception {

		List<CriteriaSchedule> criteriaSchedules = getCriteriaSchedule();
		CriteriaSchedule criteriaSchedule = null;
		Integer scheduleType = null;
		DailyScheduleList = new ArrayList<CriteriaSchedule>();
		WeeklyScheduleList = new ArrayList<CriteriaSchedule>();
		MonthlyScheduleList = new ArrayList<CriteriaSchedule>();

		for (int i = 0; i < criteriaSchedules.size(); i++) {

			criteriaSchedule = criteriaSchedules.get(i);
			scheduleType = criteriaSchedule.getSchdlType();

			if (scheduleType.intValue() == ScheduleConstants.DAILY_SCHEDULE) {
				DailyScheduleList.add(criteriaSchedule);
			} else if (scheduleType.intValue() == ScheduleConstants.WEEKLY_SCHEDULE) {
				WeeklyScheduleList.add(criteriaSchedule);
			} else if (scheduleType.intValue() == ScheduleConstants.MONTHLY_SCHEDULE) {
				MonthlyScheduleList.add(criteriaSchedule);
			}

			criteriaSchedule = null;
		}

	}

	private List<CriteriaSchedule> getCriteriaSchedule() throws Exception {

		ServiceLocator serviceLocator = ReportingServerSchedule.rss.getServiceLocater();
		ReportingService reportingService = serviceLocator.getReportingService();

		return reportingService.getCriteriaSchedule();
	}

	public void setRemainingConfgParam(CriteriaSchedule criteriaSchedule, Calendar calendar) throws Exception {

		AppConfigBean appConfigBean = ReportingServerApp.getAppConfigBean();
		// appConfigBean.setOutpuReportPath(criteriaSchedule.getOutputPath());
		appConfigBean.setReportTypes(criteriaSchedule.getFileFormat());
		appConfigBean.setMailTo(criteriaSchedule.getMailTo());
		appConfigBean.setMailCc(criteriaSchedule.getMailCc());
		// appConfigBean.setMailSubject("Criteria_"+criteriaSchedule.getCriteriaId() + "_" + Util.formatDate(calendar.getTime()) );
		appConfigBean.setOutpuReportName("Criteria_" + criteriaSchedule.getCriteriaId() + "_" + Util.formatDate(calendar.getTime()));

	}

}
