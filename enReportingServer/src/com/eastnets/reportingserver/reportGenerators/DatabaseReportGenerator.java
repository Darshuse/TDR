package com.eastnets.reportingserver.reportGenerators;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.reporting.CriteriaSchedule;
import com.eastnets.domain.reporting.GeneratedReport;
import com.eastnets.reportingserver.ReportGenerator;
import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.reportingserver.ReportingServerSchedule;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.service.ServiceLocator;
import com.eastnets.service.common.MailService;

public class DatabaseReportGenerator extends ReportGenerator{

	static Logger log = Logger.getLogger(DatabaseReportGenerator.class.getName());
	private DateFormat dateF = new SimpleDateFormat("yyyyMMdd_HHmmss");
	private String subject = "Report Generator";
	private String bodyWithAttach = "Dear,\n\nThe Criteria %s for Report %s has been generated with file name %s\n\nThanks.";
	private String bodyWithoutAttach = "Dear,\n\nThe Criteria %s for Report %s has been generated.\n\nThanks.";

	@Override
	public String generateReport(Long reportSetId, User user,String criatraName) {
		boolean sendEmailFlag = true;
		String fileName = "";
		ByteArrayOutputStream outputStream = null;
		ServiceLocator serviceLocater = ReportingServerSchedule.rss.getServiceLocater();
		String extension = ReportingServerApp.getAppConfigBean().getReportTypes().name();
		GeneratedReport rep = new GeneratedReport();
		Calendar cal = Calendar.getInstance();
		rep.setStartTime(cal.getTime());
		String errorLog = "";
		try{
			outputStream = this.getReportAsStream(reportSetId, extension, user);	
		}catch(Exception ex){
			errorLog = ex.getMessage();
			log.error(errorLog);
			int end = errorLog.length() > 2000 ? 2000 : errorLog.length();
			errorLog = errorLog.substring(0, end);
			sendEmailFlag = false;
		}

		cal = Calendar.getInstance();
		rep.setEndTime(cal.getTime());
		rep.setCriteriaId(reportSetId);
		byte []data = null;
		if(outputStream == null) {
			/*
			 * Failed to generate report status
			 */
			rep.setStatus(0);
			rep.setBlob(null);
			rep.setFormat(extension);
			rep.setLog(errorLog);
		} else {
			rep.setStatus(1);
			data = outputStream.toByteArray();
			rep.setBlob(outputStream.toByteArray());
			rep.setFormat(extension);
			rep.setGroupId((long)1);
		}
		
		serviceLocater.getReportingService().addGeneratedReport(rep);
	
		
		if(sendEmailFlag){
		List<CriteriaSchedule> criteriaSchedules =  serviceLocater.getReportingService().getCriteriaSchedule(criatraName);
		if (criteriaSchedules != null && !criteriaSchedules.isEmpty()) {
			CriteriaSchedule criteriaSchedule = criteriaSchedules.get(0);
			if (criteriaSchedule.isNotifyAfterGeneration()) {
				if (criteriaSchedule.isAttachGeneratedReport()) {
					sendEmail(reportSetId, criatraName, criteriaSchedule.getMailTo(), criteriaSchedule.getMailCc(), dateF.format(rep.getEndTime()),
							extension, data, criteriaSchedule.isAttachGeneratedReport(),ReportingServerApp.getAppConfigBean().getMailSubject(),ReportingServerApp.getAppConfigBean().getMailBody());
				} else {
					sendEmail(reportSetId, criatraName, criteriaSchedule.getMailTo(), criteriaSchedule.getMailCc(), dateF.format(rep.getEndTime()),
							extension, data, criteriaSchedule.isAttachGeneratedReport(),ReportingServerApp.getAppConfigBean().getMailSubject(),ReportingServerApp.getAppConfigBean().getMailBody());
				}
			}
		}
		}
		
		return fileName;
	}
	
	
	private void sendEmail(Long reportsetId,String criteriaId, String toMail, String ccMail, String generationTimeString, String extension, byte[] attachmentContent,
			boolean includeAttachment, String mailSubject,String mailBody) {
		try {
			
			
			AppConfigBean appConfigBean = ReportingServerApp.getAppConfigBean();
			
			
			
			if(mailSubject!=null&&!mailSubject.isEmpty()){	/* if the user Wrote his own subject -> override it, else use the default */
			subject=mailSubject;
			}
			
			
			
			/*
			 * if extension is xls then make it xlsx since we support only > 2007 excel
			 */
			if(extension.equals("xls")) {
				extension = "xlsx";
			}
			String criteriaName = ReportingServerSchedule.rss.getServiceLocater().getReportingService().getCriteriaName(criteriaId);
			String attachmentName = criteriaName +"_"+ generationTimeString + '.' + extension;
			if (attachmentContent != null && includeAttachment) {

				/*
				 * check attachment size if it's exceed the max size limit
				 */
				int maxSize = appConfigBean.getMaxAttachSize();

				/*
				 * This size in MB equal content array size / 1024 KB then /1024
				 * to get the result in MB;so I will divide by 1048576
				 */
				double attachmentSize = attachmentContent.length / 1048576d;
				
				if(attachmentSize > maxSize) {
					includeAttachment = false;
					NumberFormat formatter = new DecimalFormat("#0.0000");  
					log.warn("The attachment will be excluded from the email because it's size exceed the limit :: file name :: "+attachmentName+" :: file size :: "+formatter.format(attachmentSize)+" MB");
				}

			}

			MailService mailService = ReportingServerSchedule.rss.getServiceLocater().getMailService();

			String reportName = ReportingServerSchedule.rss.getServiceLocater().getReportingService().getReportName(reportsetId);
			String body="";
			if(includeAttachment)
				body = String.format(bodyWithAttach, criteriaName, reportName, attachmentName);
			else
				body = String.format(bodyWithoutAttach, criteriaName, reportName);
			
			if(mailBody!=null&&!mailBody.isEmpty()) { /* if user Wrote his own Body -> override it, else use the default */
				body=mailBody;
			}
			
			mailService.sendMail(subject, toMail, ccMail, body, attachmentContent, attachmentName, false, includeAttachment);
			
			log.info("mail to addresses : " + toMail);
			
			if(ccMail != null && !ccMail.isEmpty()) {
				log.info("Mail cc addresses : " + ccMail);				
			}
			
		} catch (Exception e) {
			log.error("Error", e);
		}
	}
}
