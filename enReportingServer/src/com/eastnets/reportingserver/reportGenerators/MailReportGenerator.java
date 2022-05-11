package com.eastnets.reportingserver.reportGenerators;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.reportingserver.ReportGenerator;
import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.service.common.MailService;

public class MailReportGenerator extends ReportGenerator {
	static Logger log = Logger.getLogger(MailReportGenerator.class.getName());

	@Override
	public String generateReport(Long reportSetId, User user, String critraName) {

		String fileName = "";
		try {
			AppConfigBean appConfigBean = ReportingServerApp.getAppConfigBean();
			String extension = appConfigBean.getReportTypes().name();

			ByteArrayOutputStream outputStream = this.getReportAsStream(
					reportSetId, extension, user);
			
			if(outputStream == null){
				log.error(" :: Error in Generating Report ::");
				return null;
			}
			String outpuReportName = appConfigBean.getOutpuReportName();
			outpuReportName = outpuReportName + "_" + critraName;
			fileName = this
					.saveToFile(outpuReportName, extension, outputStream);
			log.info("Report " + outpuReportName + " is generated.");
			List<String> fileNames = new ArrayList<String>();
			fileNames.add(fileName);

			MailService mailService = ReportingServerApp.reportingServerApp
					.getServiceLocater().getMailService();

			mailService.sendMail(appConfigBean.getMailSubject(),
					appConfigBean.getMailTo(), appConfigBean.getMailBody(),
					fileNames, false);
			log.info("Mail is sent to " + appConfigBean.getMailTo());
		} catch (Exception e) {
			log.error("Error", e);
		}

		return fileName;
	}

}
