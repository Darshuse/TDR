package com.eastnets.reportingserver.reportRunners;


import java.util.Calendar;

import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.reportingserver.ReportGenerator;
import com.eastnets.reportingserver.ReportRunner;
import com.eastnets.reportingserver.ReportingServerSchedule;
import com.eastnets.reportingserver.reportUtil.Util;


public class MonthlyReportRrunner extends ReportRunner {

	
	ReportGenerator reportGenerator = null;
	CriteriaSchedule criteriaSchedule = null;
	Calendar calendar = null;
	User user;
	
	@Override
	public void runner(ReportGenerator reportGenerator, Calendar calendar, User user) throws Exception {
		this.user= user;
		//int month = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
		int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
		int minute     = calendar.get(Calendar.MINUTE);
		
		for(int i = 0; i < MonthlyScheduleList.size(); i ++) {
			
			criteriaSchedule = MonthlyScheduleList.get(i);
			
			if(criteriaSchedule.isSchDisable())
				continue;
						
			if(	criteriaSchedule.getSchdlDate() != null  && criteriaSchedule.getSchdlDate().intValue() == dayOfMonth) {
				
				if(	criteriaSchedule.getSchdlHours() != null 
						&& !criteriaSchedule.getSchdlHours().isEmpty() 
						&& criteriaSchedule.getSchdlHours().equals(Util.formatNumberto2Digit(hourOfDay) + ":" + Util.formatNumberto2Digit(minute))) {
					
					if(criteriaSchedule.getCriteriaId() != null){							 	
						
						MonthlyReportRrunner obj = new MonthlyReportRrunner();
						User requestedUser = ReportingServerSchedule.rss.getServiceLocater().getAdminService().getUser(criteriaSchedule.getUserId());
						Profile userProfile = ReportingServerSchedule.rss.getServiceLocater().getAdminService().getProfile(requestedUser.getUserName(), requestedUser.getProfile().getName());
						criteriaSchedule.setOutputPath(userProfile.getRpDirectory()+DailyReportRrunner.REPORTS);
						obj.reportGenerator = reportGenerator;
						obj.calendar = calendar;
						obj.criteriaSchedule = criteriaSchedule;
						
						new Thread(obj).start();
					} else 
				 		System.out.println("Criteria not found");
			}}
		}
	}
	
	
	@Override
	public void run() {
		
		try {
			
			execute(this);
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	
	public synchronized void execute(MonthlyReportRrunner obj) throws Exception {
		
		obj.setRemainingConfgParam(obj.criteriaSchedule, obj.calendar);	
		obj.reportGenerator.generateReport(new Long(obj.criteriaSchedule.getCriteriaId().intValue()), user,obj.criteriaSchedule.getCriteriaId().toString());
	}
	
}
