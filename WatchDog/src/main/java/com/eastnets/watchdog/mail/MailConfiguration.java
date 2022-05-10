package com.eastnets.watchdog.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.eastnets.watchdog.config.WatchdogConfiguration;

@Configuration
public class MailConfiguration {

	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties props = mailSender.getJavaMailProperties();
		mailSender.setHost(watchdogConfiguration.getMailServer());
		mailSender.setPort(watchdogConfiguration.getMailPort());
		if (watchdogConfiguration.isMailAuthenticationRequired()) {
			mailSender.setUsername(watchdogConfiguration.getMailUsername());
			mailSender.setPassword(watchdogConfiguration.getMailPassword());
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "false");
		mailSender.setJavaMailProperties(props);
		return mailSender;
	}
}
