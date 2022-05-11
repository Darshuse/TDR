package com.eastnets.dbconsistencycheck.app;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class MailReport {

	private static final Logger log = Logger.getLogger("DBConsistencyCheck");

	public synchronized int sendReportEmail(DBConsistencyCheckConfig  config, DBConsistencyCheckApp app,  ReportInfoBean infoBean , boolean isEndDay){

		try {

			log.info("Mail is sent to " + config.getMailTo());		
			List<String> fileNames = new ArrayList<String>();
			fileNames.add(config.getReportFile());

			// set mail subject
			config.setMailSubject("en.TDR - DB Consistency Check Service Report");

			config.setMailBody((isEndDay == true ? "End of day report, \n Summary:" : "Summary:" ) +    "\n Report Date : "+ infoBean .getReportDate()+"\n DB check period: Start: "+ infoBean.getStartCheck()+"\t End: "+ infoBean.getEndCheck()+"\n Status: " + infoBean.getReportStatus()+ "\n Number of messages in Alliance: "+ infoBean.getCountOkMsg()+"\n Number of messages in en.TDR: "+ infoBean.getCountMsg() +"\n Number of missing messages: " + infoBean.getCountMissingMsg() + "\n Attached is the detailed report.");
			app.getServiceLocater().getMailService().sendMail(config.getMailSubject(),
					config.getMailTo(),config.getMailCc(), config.getMailBody(),
					fileNames, false);

			return 1;
		} catch (Exception e) {
			log.error("Failed to send Email, reason: " +e.getMessage());
			return 0;
		}

	}


	public synchronized int sendReport(DBConsistencyCheckConfig  config, DBConsistencyCheckApp app, ReportInfoBean infoBean , boolean isEndDay){
		int result = 0;
		result = sendReportEmail(config, app,  infoBean, isEndDay);

		if (result == 0)
			return 0;

		return 1;	
	}
}
