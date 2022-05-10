package com.eastnets.watchdog.mail;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.eastnets.watchdog.config.DAOFactory;
import com.eastnets.watchdog.config.WatchdogConfiguration;
import com.eastnets.watchdog.dao.WatchdogDaoImpl;
import com.eastnets.watchdog.resultbeans.EmailNotification;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;
import com.eastnets.watchdog.service.WatchDogRepositoryService;
import com.eastnets.watchdog.utility.Constants;

@Service
public class MailService implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(MailService.class);

	@Autowired
	WatchDogRepositoryService watchDogService;
	@Autowired
	WatchdogConfiguration watchdogConfiguration;

	@Autowired
	MailManager mailManager;

	@Autowired
	@Qualifier("EmailsNotificationsToBeSentQueue")
	BlockingQueue<EmailNotification> emailsNotificationsToBeSentQueue;

	@Autowired
	WatchdogDaoImpl watchdogDaoImpl;

	@Autowired
	DAOFactory daoFactory;

	public void run() {
		LOGGER.info("Running Mail Service");

		while (true) {
			EmailNotification emailNotification = null;
			try {

				LOGGER.trace("Fetching Email Notifications from the Queue");
				emailNotification = emailsNotificationsToBeSentQueue.take();
				boolean emailSent = mailManager.sendEmail(emailNotification);
				if (emailSent) {
					// removeNotificationFromDB(emailNotification);
				}

			} catch (Exception e) {
				String message = e.getMessage();
				if (message == null) {
					message = "";
				}
				daoFactory.getWatchDogDAO().insertLdErrors(Constants.PROGRAM_NAME, LocalDateTime.now(), Constants.TRACE_LEVEL_E, Constants.PROGRAM_NAME, "",
						"Exception when Sendig  Email " + "   Error Messages : " + ((message.length() < 400) ? message : message.substring(0, 399)));
				e.printStackTrace();
			}
		}
	}

	private void removeNotificationFromDB(EmailNotification emailNotification) {
		// If email notification is sent, we remove it from DB.
		LOGGER.debug("Removing Email Notification from DB");
		if (emailNotification instanceof MessageEmailNotification) {
			MessageEmailNotification messageEmailNotification = (MessageEmailNotification) emailNotification;
			watchdogDaoImpl.deleteMessageWDEmailNotification(messageEmailNotification);
			LOGGER.debug("Email Notification removed successfully");
		} else if (emailNotification instanceof EventEmailNotification) {
			EventEmailNotification eventEmailNotification = (EventEmailNotification) emailNotification;
			watchdogDaoImpl.deleteEventWDEmailNotification(eventEmailNotification);
			LOGGER.debug("Email Notification removed successfully");
		}

	}

	public BlockingQueue<EmailNotification> getEmailsNotificationsToBeSentQueue() {
		return emailsNotificationsToBeSentQueue;
	}

	public void setEmailsNotificationsToBeSentQueue(BlockingQueue<EmailNotification> emailsNotificationsToBeSentQueue) {
		this.emailsNotificationsToBeSentQueue = emailsNotificationsToBeSentQueue;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

}
