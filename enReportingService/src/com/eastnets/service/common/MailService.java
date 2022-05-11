package com.eastnets.service.common;

import java.util.List;


public interface MailService {
	
	/**
	 * send an e-mail message  
	 * @param subject
	 * @param to
	 * @param content
	 * @param attachments
	 * @param isHTML
	 * @return error message if error occurred or empty string if operation succeeded 
	 */
	public String sendMail(String subject, String to, String content, List<String> attachments, boolean isHTML) ;
	
	
	/**
	 * 
	 * @param subject
	 * @param to
	 * @param cc
	 * @param content
	 * @param attachment
	 * @param isHTML
	 * @return
	 */
	public String sendMail(String subject, String to, String cc, String content, byte[] attachmentContent,String attachmentName, boolean isHTML,boolean includeAttachment);
	
	/**
	 * send an e-mail message  
	 * @param subject
	 * @param to
	 * @param cc
	 * @param content
	 * @param attachments
	 * @param isHTML
	 * @return error message if error occurred or empty string if operation succeeded 
	 */
	public String sendMail(String subject, String to, String cc, String content, List<String> attachments, boolean isHTML) ;
	
	/**
	 * @param mailServer
	 * @param mailPort
	 * @param mailUser
	 * @param mailPassword
	 * @param mailFrom
	 */
	public void setMailSettings(String mailServer, Integer mailPort, String mailUser, String mailPassword, String mailFrom);
}
