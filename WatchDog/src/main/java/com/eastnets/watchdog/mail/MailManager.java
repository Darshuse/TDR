package com.eastnets.watchdog.mail;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.eastnets.watchdog.checker.MessageChecker;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.resultbeans.CorrespondentBean;
import com.eastnets.watchdog.resultbeans.EmailNotification;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.utility.Util;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MailManager {

	private static final Logger LOGGER = Logger.getLogger(MessageChecker.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Configuration config;

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	private Map<String, CorrespondentBean> correspondentsInformation = new HashMap<>();

	@Autowired
	Util util;

	@Autowired
	WatchdogDaoImpl watchdogDaoImpl;

	private Map<String, Integer> currenciesMap;

	@PostConstruct
	public void init() {
		cacheCorrespondentsInformation();
	}

	private void cacheCorrespondentsInformation() {
		List<CorrespondentBean> correspondentsInformation = watchdogDaoImpl.cacheCorrespondentsInformation();
		if (correspondentsInformation != null && !correspondentsInformation.isEmpty()) {
			setCorrespondentsInformation(correspondentsInformation.stream().collect(Collectors.toMap(CorrespondentBean::getCorrBIC11, correspondent -> correspondent, (oldValue, newValue) -> newValue)));
		}

		currenciesMap = watchdogDaoImpl.getCurrenciesISO();

	}

	public boolean sendEmail(EmailNotification emailNotification) {
		LOGGER.info("Sending Noitification via Email");
		if (emailNotification instanceof MessageEmailNotification) {
			return sendMessageNotification((MessageEmailNotification) emailNotification);
		} else if (emailNotification instanceof EventEmailNotification) {
			return sendEventNotification((EventEmailNotification) emailNotification);
		}

		LOGGER.error("Email Notification is not instance of Event nor of Message");
		return false;

	}

	private boolean sendEventNotification(EventEmailNotification emailNotification) {
		MailRequest request = new MailRequest("", emailNotification.getUsername(), watchdogConfiguration.getMailSubject(), watchdogConfiguration.getMailFrom());
		MailResponse mailResponse = preperAndSendEventsNotifaction(request, createEventsRequstPropertiesMap(emailNotification), "watchdogEventRequst.ftl");
		if (mailResponse.getStatus() == 1) {
			return false;
		} else {
			return true;
		}
	}

	private boolean sendMessageNotification(MessageEmailNotification emailNotification) {
		MailRequest request = new MailRequest("", emailNotification.getUsername(), watchdogConfiguration.getMailSubject(), watchdogConfiguration.getMailFrom());
		MailResponse mailResponse = preperAndSendMessageNotifaction(request, createMessageRequstPropertiesMap(emailNotification), "watchdogMessageRequst.ftl");
		if (mailResponse.getStatus() == 1) {
			return false;
		} else {
			return true;
		}
	}

	public MailResponse preperAndSendMessageNotifaction(MailRequest request, Map<String, Object> model, String templetName) {

		MimeMessage message = javaMailSender.createMimeMessage();
		MailResponse response = new MailResponse();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			util.checkForDebug(false);
			Template t = config.getTemplate(templetName);
			util.checkForDebug(true);

			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			LOGGER.debug("Sending to: " + request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getMailFrom());

			if (request.getTo().trim().contains(";")) {
				String[] emails = request.getTo().trim().split(";");
				for (String email : emails) {
					LOGGER.debug("Sending email to: " + email);
					helper.setTo(email.trim());
					javaMailSender.send(message);
					LOGGER.info("Email sent successfully to: " + email);
				}
			} else {
				LOGGER.debug("Sending email to: " + request.getTo().trim());
				helper.setTo(request.getTo().trim());
				javaMailSender.send(message);
				LOGGER.info("Email sent successfully to: " + request.getTo().trim());
			}

			response.setMessage("Email sent successfully to: " + request.getTo());
			response.setStatus(0);

		} catch (Exception e) {
			LOGGER.error("Email wast not sent");
			response.setMessage("Failed to Sent Mail through Mail Server");
			response.setStatus(1);
			e.printStackTrace();
		}
		return response;
	}

	public MailResponse preperAndSendEventsNotifaction(MailRequest request, Map<String, Object> model, String templetName) {

		MimeMessage message = javaMailSender.createMimeMessage();
		MailResponse response = new MailResponse();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			util.checkForDebug(false);
			Template t = config.getTemplate(templetName);
			util.checkForDebug(true);
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			LOGGER.debug("Sending to: " + request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getMailFrom());

			if (request.getTo().trim().contains(";")) {
				String[] emails = request.getTo().trim().split(";");
				for (String email : emails) {
					LOGGER.debug("Sending email to: " + email);
					helper.setTo(email.trim());
					javaMailSender.send(message);
					LOGGER.info("Email sent successfully to: " + email);
				}
			} else {
				LOGGER.debug("Sending email to: " + request.getTo().trim());
				helper.setTo(request.getTo().trim());
				javaMailSender.send(message);
				LOGGER.info("Email sent successfully to: " + request.getTo().trim());
			}

			response.setMessage("Email sent successfully to: " + request.getTo());
			response.setStatus(0);

		} catch (Exception e) {
			LOGGER.error("Email wast not sent");
			response.setMessage("Failed to Sent Mail through Mail Server");
			response.setStatus(1);
			e.printStackTrace();
		}
		return response;
	}

	private Map<String, Object> createMessageRequstPropertiesMap(MessageEmailNotification messageNotification) {
		String mesgText = messageNotification.getMesgText();
		Map<String, Object> model = new HashMap<>();
		model.put("mesgTrnRef", StringUtils.defaultString(messageNotification.getMesgTrnRef(), ""));
		model.put("xFinAmount", util.amountFormat(messageNotification.getxFinAmount().toString(), messageNotification.getxFinCcy(), ",", "."));
		model.put("xFinCcy", messageNotification.getxFinCcy());
		model.put("messageType", StringUtils.defaultString(messageNotification.getMessageType(), ""));
		model.put("mesgRelTrnRef", StringUtils.defaultString(messageNotification.getMesgRelTrnRef(), ""));
		model.put("mesgUumid", StringUtils.defaultString(messageNotification.getMesgUumid(), ""));
		model.put("mesgUumidSuffix", messageNotification.getMesgUumidSuffix());
		model.put("mesgSenderX1", StringUtils.defaultString(messageNotification.getMesgSenderX1(), ""));
		model.put("xReceiverX1", StringUtils.defaultString(messageNotification.getxReceiverX1(), ""));
		model.put("mesgSubFormat", StringUtils.defaultString(messageNotification.getMesgSubFormat(), ""));
		model.put("mesgNature", StringUtils.defaultString(messageNotification.getMesgNature(), ""));
		model.put("xFinValueDate", messageNotification.getFinValueDateFormate());
		if (watchdogConfiguration.isEnableExpandText()) {
			String expandedText = watchdogDaoImpl.getExpandedMesssageText(messageNotification.getMesgSyntaxTableVer(), messageNotification.getMessageType(), mesgText, messageNotification.getMesgCreaDateTime(), "", "", currenciesMap);
			model.put("mesgText", expandedText.replace("\r\n", " <br> "));
		} else {
			model.put("mesgText", mesgText.replace("\r\n", " <br> "));
		}
		model.put("description", messageNotification.getDescription());
		if (!watchdogConfiguration.isIncludeSwiftText()) {
			model.put("displayText", "none");
		} else {
			model.put("displayText", "inline");
		}

		Map<String, CorrespondentBean> correspondentsInformation = getCorrespondentsInformation();

		// get Sender info
		CorrespondentBean senderCorrespondentBean = correspondentsInformation.get(messageNotification.getMesgSenderX1());
		if (senderCorrespondentBean != null) {
			String senderCorrBranchInfo = senderCorrespondentBean.getCorrBranchInfo();
			model.put("senderCorrBranchInfo", StringUtils.defaultString(senderCorrBranchInfo, ""));
			String senderCorrCountryCode = senderCorrespondentBean.getCorrCountryCode();
			model.put("senderCorrCountryCode", StringUtils.defaultString(senderCorrCountryCode, ""));
			String senderCorrCountryName = senderCorrespondentBean.getCorrCountryName();
			model.put("senderCorrCountryName", StringUtils.defaultString(senderCorrCountryName, ""));
			String senderCorrGeoLocation = senderCorrespondentBean.getCorrGeoLocation();
			model.put("senderCorrGeoLocation", StringUtils.defaultString(senderCorrGeoLocation, ""));
		}

		// get RECIVER info
		CorrespondentBean reciverCorrespondentBean = correspondentsInformation.get(messageNotification.getxReceiverX1());
		if (reciverCorrespondentBean != null) {
			String reciverCorrBranchInfo = reciverCorrespondentBean.getCorrBranchInfo();
			model.put("reciverCorrBranchInfo", StringUtils.defaultString(reciverCorrBranchInfo, ""));
			String reciverCorrCountryCode = reciverCorrespondentBean.getCorrCountryCode();
			model.put("reciverCorrCountryCode", StringUtils.defaultString(reciverCorrCountryCode, ""));
			String reciverCorrCountryName = reciverCorrespondentBean.getCorrCountryName();
			model.put("reciverCorrCountryName", StringUtils.defaultString(reciverCorrCountryName, ""));
			String reciverCorrGeoLocation = reciverCorrespondentBean.getCorrGeoLocation();
			model.put("reciverCorrGeoLocation", StringUtils.defaultString(reciverCorrGeoLocation, ""));
		}

		return model;
	}

	private Map<String, Object> createEventsRequstPropertiesMap(EventEmailNotification emailNotification) {
		Map<String, Object> model = new HashMap<>();

		model.put("description", emailNotification.getDescription());

		model.put("jrnlEventClass", StringUtils.defaultString(emailNotification.getJrnlEventClass(), ""));

		model.put("jrnlEventNum", emailNotification.getJrnlEventNum());

		model.put("jrnlEventName", StringUtils.defaultString(emailNotification.getJrnlEventName(), ""));

		model.put("jrnlCompName", StringUtils.defaultString(emailNotification.getJrnlCompName(), ""));

		model.put("jrnlEventServerity", StringUtils.defaultString(emailNotification.getJrnlEventServerity(), ""));

		model.put("jrnlApplServName", StringUtils.defaultString(emailNotification.getJrnlApplServName(), ""));

		model.put("jrnlOperNickName", StringUtils.defaultString(emailNotification.getJrnlOperNickName(), ""));

		model.put("jrnlHostname", StringUtils.defaultString(emailNotification.getJrnlHostname(), ""));

		model.put("jrnlMergedText", StringUtils.defaultString(emailNotification.getJrnlMergedText().replace("\r\n", " <br> "), ""));

		return model;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public Map<String, CorrespondentBean> getCorrespondentsInformation() {
		return correspondentsInformation;
	}

	public void setCorrespondentsInformation(Map<String, CorrespondentBean> correspondentsInformation) {
		this.correspondentsInformation = correspondentsInformation;
	}

}
