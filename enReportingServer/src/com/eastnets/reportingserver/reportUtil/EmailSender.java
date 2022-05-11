package com.eastnets.reportingserver.reportUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

import com.eastnets.reportingserver.ReportingServerApp;
import com.eastnets.reportingserver.ReportingServerSchedule;
import com.eastnets.reportingserver.reportBeans.AppConfigBean;
import com.eastnets.reportingserver.reportGenerators.TextControlReportGenerator;
import com.eastnets.service.common.MailService;

public class EmailSender {
	static Logger log = Logger.getLogger(TextControlReportGenerator.class.getName());

	private static String bodyWithAttach = "Dear,\n\nThe Criteria %s for Report %s has been generated with file name %s\n\nThanks.";
	private static String bodyWithoutAttach = "Dear,\n\nThe Criteria %s for Report %s has been generated.\n\nThanks.";
	private static String subject = "Report Generator";

	public static void sendEmail(Long reportsetId, String criteriaId, String toMail, String ccMail, String generationTimeString, String extension, byte[] attachmentContent, boolean includeAttachment, String mailSubject, String mailBody) {
		try {

			AppConfigBean appConfigBean = ReportingServerApp.getAppConfigBean();

			if (mailSubject != null && !mailSubject.isEmpty()) { /* if the user Wrote his own subject -> override it, else use the default */
				subject = mailSubject;
			}

			/*
			 * if extension is xls then make it xlsx since we support only > 2007 excel
			 */
			if (extension.equals("xls")) {
				extension = "xlsx";
			}
			String criteriaName = ReportingServerSchedule.rss.getServiceLocater().getReportingService().getCriteriaName(criteriaId);
			String attachmentName = criteriaName + "_" + generationTimeString + '.' + extension;
			if (attachmentContent != null && includeAttachment) {

				/*
				 * check attachment size if it's exceed the max size limit
				 */
				int maxSize = appConfigBean.getMaxAttachSize();

				/*
				 * This size in MB equal content array size / 1024 KB then /1024 to get the result in MB;so I will divide by 1048576
				 */
				double attachmentSize = attachmentContent.length / 1048576d;

				if (attachmentSize > maxSize) {
					includeAttachment = false;
					NumberFormat formatter = new DecimalFormat("#0.0000");
					log.warn("The attachment will be excluded from the email because it's size exceed the limit :: file name :: " + attachmentName + " :: file size :: " + formatter.format(attachmentSize) + " MB");
				}

			}

			MailService mailService = ReportingServerSchedule.rss.getServiceLocater().getMailService();

			String reportName = ReportingServerSchedule.rss.getServiceLocater().getReportingService().getReportName(reportsetId);
			String body = "";
			if (includeAttachment)
				body = String.format(bodyWithAttach, criteriaName, reportName, attachmentName);
			else
				body = String.format(bodyWithoutAttach, criteriaName, reportName);

			if (mailBody != null && !mailBody.isEmpty()) { /* if user Wrote his own Body -> override it, else use the default */
				body = mailBody;
			}

			mailService.sendMail(subject, toMail, ccMail, body, attachmentContent, attachmentName, false, includeAttachment);

			log.info("mail to addresses : " + toMail);

			if (ccMail != null && !ccMail.isEmpty()) {
				log.info("Mail cc addresses : " + ccMail);
			}

		} catch (Exception e) {
			log.error("Error", e);
		}
	}

}
