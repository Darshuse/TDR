package com.eastnets.service.common;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.service.ServiceBaseImp;
import com.sun.mail.util.MailSSLSocketFactory;

@Import({ com.sun.mail.imap.IMAPProvider.class, com.sun.mail.smtp.SMTPProvider.class })
public class MailServiceImpl extends ServiceBaseImp implements MailService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7081934217132665745L;
	private JavaMailSenderImpl mailSender;
	private SimpleMailMessage simpleMailMessage;
	private VelocityEngine velocityEngine;

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

	public void setMailSender(MailSenderEN mailSender) {
		this.mailSender = mailSender.getJavaMailSenderImpl();

		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();

			Properties javaMailProperties = this.mailSender.getJavaMailProperties();

			sf.setTrustAllHosts(true);
			javaMailProperties.put("mail.smtp.ssl.socketFactory", sf);

			this.mailSender.setJavaMailProperties(javaMailProperties);

		} catch (GeneralSecurityException e) {
		}
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Override
	public String sendMail(String subject, String to, String content, List<String> attachments, boolean isHTML) {
		return sendMail(subject, to, null, content, attachments, isHTML);
	}

	private MimeMessageHelper prepareMessageBody(MimeMessage message, String subject, String to, String cc, String content, boolean isHTML) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(simpleMailMessage.getFrom());
		helper.setSubject(subject);

		String[] toMail = to.split("(;|,)");
		for (int i = 0; i < toMail.length; ++i) {
			/*
			 * make sure we don't have extra spaces at the beginning end of the email address
			 */
			toMail[i] = toMail[i].trim();
		}
		helper.setTo(toMail);

		String[] ccMail = null;

		if (cc != null && !cc.trim().isEmpty()) {
			ccMail = cc.split("(;|,)");
			for (int i = 0; i < ccMail.length; ++i) {
				/*
				 * make sure we don't have extra spaces at the beginning end of the email address
				 */
				ccMail[i] = ccMail[i].trim();
			}
		}

		if (ccMail != null && ccMail.length > 0) {
			helper.setCc(ccMail);
		}

		if (!isHTML) {
			Map<String, String> model = new HashMap<String, String>();
			model.put("body", content);

			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mailTemplate.vm", model);
			helper.setText(text, true);
		} else {
			helper.setText(content, true);
		}
		return helper;
	}

	@Override
	public String sendMail(String subject, String to, String cc, String content, byte[] attachmentContent, String attachmentName, boolean isHTML, boolean includeAttachment) {
		MimeMessage message = mailSender.createMimeMessage();
		try {

			MimeMessageHelper helper = prepareMessageBody(message, subject, to, cc, content, isHTML);
			if (includeAttachment) {
				if (attachmentContent != null) {
					helper.addAttachment(attachmentName, new ByteArrayResource(attachmentContent));
				}
			}

			mailSender.send(message);
		} catch (VelocityException e) {
			throw new WebClientException(e);

		} catch (Exception e) {
			if (StringUtils.containsIgnoreCase(e.getMessage(), "Authentication unsuccessful") || StringUtils.containsIgnoreCase(e.getMessage(), "Authentication failed")) {
				return "Mail server authentication failed, please contact your network administrator to solve this issue.";
			}
			if (StringUtils.containsIgnoreCase(e.getMessage(), "timeout")) {
				return "Mail server timeout, please contact your network administrator to solve this issue.";
			}
			if (StringUtils.containsIgnoreCase(e.getMessage(), "Local address contains control or whitespace")) {
				return "Invalid email address.";
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "MessagingException: Could not connect to SMTP host")) {
				return String.format("Could not connect to SMTP host %s, port %d, please contact your network administrator to solve this issue.", mailSender.getHost(), mailSender.getPort());
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Client does not have permissions to send as this sender")) {
				return String.format("'%s' does not have permissions to send as '%s', please contact your network administrator to solve this issue.", mailSender.getUsername(), simpleMailMessage.getFrom());
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Unable to relay for ")) {
				return String.format("SMTP serve unable to relay for '%s', please contact your network administrator to solve this issue.", to);
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Insufficient system resources")) {
				return String.format("SMTP server insufficient system resources, please contact your network administrator to solve this issue.");
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Temporary authentication failure")) {
				return String.format("SMTP server temporary authentication failure, please contact your network administrator to solve this issue.");
			}

			throw new WebClientException(e);
		}
		return null;
	}

	@Override
	public String sendMail(String subject, String to, String cc, String content, List<String> attachments, boolean isHTML) {
		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = prepareMessageBody(message, subject, to, cc, content, isHTML);
			if (attachments != null) {
				for (String filename : attachments) {
					filename = filename.replace('\\', '/');
					filename = filename.replace('/', File.separatorChar);
					DataSource dataSource = new FileDataSource(filename);
					helper.addAttachment(filename.substring(filename.lastIndexOf(File.separator) + 1), dataSource);
				}
			}

			mailSender.send(message);

		} catch (VelocityException e) {
			throw new WebClientException(e);

		} catch (Exception e) {
			if (StringUtils.containsIgnoreCase(e.getMessage(), "Authentication unsuccessful") || StringUtils.containsIgnoreCase(e.getMessage(), "Authentication failed")) {
				return "Mail server authentication failed, please contact your network administrator to solve this issue.";
			}
			if (StringUtils.containsIgnoreCase(e.getMessage(), "timeout")) {
				return "Mail server timeout, please contact your network administrator to solve this issue.";
			}
			if (StringUtils.containsIgnoreCase(e.getMessage(), "Local address contains control or whitespace")) {
				return "Invalid email address.";
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "MessagingException: Could not connect to SMTP host")) {
				return String.format("Could not connect to SMTP host %s, port %d, please contact your network administrator to solve this issue.", mailSender.getHost(), mailSender.getPort());
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Client does not have permissions to send as this sender")) {
				return String.format("'%s' does not have permissions to send as '%s', please contact your network administrator to solve this issue.", mailSender.getUsername(), simpleMailMessage.getFrom());
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Unable to relay for ")) {
				return String.format("SMTP serve unable to relay for '%s', please contact your network administrator to solve this issue.", to);
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Insufficient system resources")) {
				return String.format("SMTP server insufficient system resources, please contact your network administrator to solve this issue.");
			}

			if (StringUtils.containsIgnoreCase(e.getMessage(), "Temporary authentication failure")) {
				return String.format("SMTP server temporary authentication failure, please contact your network administrator to solve this issue.");
			}

			throw new WebClientException(e);
		}
		return "";
	}

	@Override
	public void setMailSettings(String mailServer, Integer mailPort, String mailUser, String mailPassword, String mailFrom) {
		mailSender.setHost(mailServer);
		mailSender.setPort(mailPort);
		mailSender.setUsername(mailUser);
		mailSender.setPassword(mailPassword);
		simpleMailMessage.setFrom(mailFrom);
	}

}
