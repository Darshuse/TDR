package com.eastnets.reportingserver.reportGenerators;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.reportingserver.ReportGenerator;


public class FileReportGenerator extends ReportGenerator {

	static Logger log = Logger.getLogger(FileReportGenerator.class.getName());

	@Override
	public String generateReport(Long reportSetId, User user,String criatraName) {
		
		String fileName = "";
		try {
			String extension = ReportingServerApp.getAppConfigBean().getReportTypes().name();
			
			ByteArrayOutputStream outputStream = this.getReportAsStream(reportSetId, extension, user);
			
			if(outputStream == null){
				log.error(" :: Error in Generating Report ::");
				return null ;
			}
			String outpuReportName = ReportingServerApp.getAppConfigBean().getOutpuReportName();
			
			fileName = this.saveToFile(outpuReportName, extension, outputStream);
			log.info("Report " + outpuReportName + " is generated.");
		} catch (Exception e) {
			log.error("Error",e);
		}
		
		return fileName;
	}
}
