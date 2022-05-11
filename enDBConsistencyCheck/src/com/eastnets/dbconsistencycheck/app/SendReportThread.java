package com.eastnets.dbconsistencycheck.app;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class SendReportThread implements Runnable {

	
	public SendReportThread(DBConsistencyCheckApp dbcheck, DBConsistencyCheckConfig appConfig , ReportInfoBean infoBean){
		this.app = dbcheck;
		this.appConfigBean = appConfig;
		this.infoBean = infoBean;	
	}

	
	Logger log = Logger.getLogger("DBConsistencyCheck");
	
	private DBConsistencyCheckConfig appConfigBean;
	private MailReport mailReport = new MailReport();
	private DBConsistencyCheckApp app;
	private ReportInfoBean infoBean;
	
	public ReportInfoBean getInfoBean() {
		return infoBean;
	}

	public void setInfoBean(ReportInfoBean infoBean) {
		this.infoBean = infoBean;
	}

	@Override
	public void run() {

		// to store result of each process
		int result = 1;

		getDateFormat("HH:mm:ss");

		// get end of day time to compare with current time 
		try {
			
			Calendar currentTime, reportTime;
			while (result == 1){
				// get current time
				currentTime = Calendar.getInstance();
				currentTime.setTime(new Date());
				
				reportTime = Calendar.getInstance();
				reportTime.setTime(new Date());
				reportTime.set(Calendar.HOUR_OF_DAY,(appConfigBean.getReportTime().get(Calendar.HOUR_OF_DAY)));
				reportTime.set(Calendar.MINUTE,(appConfigBean.getReportTime().get(Calendar.MINUTE)));
				reportTime.set(Calendar.SECOND,(appConfigBean.getReportTime().get(Calendar.SECOND)));
				
				// check current time between active time & end time
				if (currentTime.getTime().compareTo(reportTime.getTime()) == 0 )							
					result = mailReport.sendReport(appConfigBean, app , infoBean, true);
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	// Getter & Setter 

	public SimpleDateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
	}

	public  DBConsistencyCheckConfig getAppConfigBean() {
		return appConfigBean;
	}

	public  void setAppConfigBean(DBConsistencyCheckConfig appConfigBean) {
		this.appConfigBean = appConfigBean;
	}
	
	public DBConsistencyCheckApp getApp() {
		return app;
	}

	public void setApp(DBConsistencyCheckApp app) {
		this.app = app;
	}
	
	public MailReport getMailReport() {
		return mailReport;
	}
	public void setMailReport(MailReport mailReport) {
		this.mailReport = mailReport;
	}


}
