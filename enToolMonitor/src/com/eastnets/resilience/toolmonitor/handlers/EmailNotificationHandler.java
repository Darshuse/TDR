package com.eastnets.resilience.toolmonitor.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.resilience.toolmonitor.Globals;
import com.eastnets.resilience.toolmonitor.NotificationHandler;
import com.eastnets.domain.csm.ClientServerConnection;
import com.eastnets.service.ServiceLocator;

public class EmailNotificationHandler implements NotificationHandler {
	String mailServer;
	Integer mailPort;
	String mailUser;
	String mailPassword;
	
	String mailSubject;
	String mailFrom;
	String mailTo;	
	String mailCC;
	String templateFile;
	
	String templateFileContent= "";
			
	String dateFormat = "yyyy/MM/dd HH:mm:ss";
	
	
	@Override
	public boolean loadSettings(Element config) {
		if ( config == null ){
			System.out.println(Globals.getDateString() +  "Invalid Configuration for mail sender");
			return false;
		}
		try {
					
			mailServer= getSubElementValue(  config, "MailServer" );
			String temp= getSubElementValue(  config, "MailPort" );
			mailPort=25;
			if ( !isEmpty(temp) ){
				mailPort = Integer.parseInt(temp);
			}
			
			mailUser= getSubElementValue(  config, "MailUser" );
			mailPassword= getSubElementValue(  config, "MailPassword" );
			try{
				mailPassword= AESEncryptDecrypt.decrypt(mailPassword);//if the password is encrypted - decrypt it 
			}catch(Exception ex){}
			
			mailSubject = getSubElementValue(  config, "MailSubject" );
			mailFrom = getSubElementValue(  config, "MailFrom" );
			mailTo = getSubElementValue(  config, "MailTo" );	
			mailCC = getSubElementValue(  config, "MailCC" );
			templateFile = getSubElementValue(  config, "TemplateFile" );
			String dateFrmt= getSubElementValue(  config, "DateFormat" );
			if ( !isEmpty(dateFrmt) ){
				dateFormat= dateFrmt;
			}
			
			
			if ( isEmpty(mailServer) || isEmpty(mailUser) || isEmpty(mailPassword) || isEmpty(mailFrom) || isEmpty(mailTo) ){
				System.out.println(Globals.getDateString() +  "Mail Configuratoin is missing."); 
				return false;
			}
			return true;
			
		} catch (Exception ex) {
			System.out.println(Globals.getDateString() + "Error : parsing mail settings failed ('"  + ex +"')");	
		}
		return false;
	}

	private boolean isEmpty(String string) {		
		return string == null || string.trim().isEmpty() ;
	}

	private String getSubElementValue(Element eElement, String elementName) {
		NodeList list = eElement.getElementsByTagName(elementName);
		if ( list.getLength() > 0  ){
			return list.item(0).getTextContent();
		}		
		return null;
	}

	@Override
	public void handle(ClientServerConnection connection, ServiceLocator serviceLocator) {
		
		serviceLocator.getMailService().setMailSettings(mailServer, mailPort, mailUser, mailPassword, mailFrom );

		String tempSubject = "( " + connection.getServerName() + ":" + connection.getServerPort() + ", " + connection.getClientName() + " ) connection status changed to : " + formatStatus( connection.getStatus(), connection.getServerName(), connection.getClientName() );
		System.out.println(Globals.getDateString() + "Sending email : " + tempSubject );
		
		if ( !isEmpty( mailSubject ) ){
			tempSubject= mailSubject;
		}
		serviceLocator.getMailService().sendMail(tempSubject, mailTo, mailCC, getMailContent( connection ), null , true);
		System.out.println(Globals.getDateString() + "Mail sent successfully.");
	}

	private String getMailContent(ClientServerConnection connection) {
		String mailTemplate = getMailTemplate();
		
		mailTemplate= mailTemplate.replace("<!--ServerPort-->", connection.getServerPort().toString());
		mailTemplate= mailTemplate.replace("<!--ServerName-->", connection.getServerName());
		mailTemplate= mailTemplate.replace("<!--ConnectionDownAlarm-->", connection.getConnectionDownAlarm().toString());
		mailTemplate= mailTemplate.replace("<!--ClientName-->", connection.getClientName());
		mailTemplate= mailTemplate.replace("<!--ClientDownAlarm-->", connection.getClientDownAlarm().toString());
		mailTemplate= mailTemplate.replace("<!--HPPeriodSeconds-->", connection.getHPPeriodSeconds().toString());
				
		mailTemplate= mailTemplate.replace("<!--ThresholdPercent-->", connection.getThresholdPercent().toString() );
		mailTemplate= mailTemplate.replace("<!--AlarmIdleCycles-->", connection.getAlarmIdleCycles().toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
		mailTemplate= mailTemplate.replace("<!--LastServer-->", sdf.format( connection.getLastServer() ));
		mailTemplate= mailTemplate.replace("<!--LastClient-->", sdf.format( connection.getLastClient() ));
		
		mailTemplate= mailTemplate.replace("<!--Status-->", formatStatus( connection.getStatus(), connection.getServerName(), connection.getClientName() ));
		mailTemplate= mailTemplate.replace("<!--StatusInt-->", connection.getStatus().toString() );
		mailTemplate= mailTemplate.replace("<!--SecondsFromLastServerRequest-->", connection.getSecondsFromLastServerRequest().toString());
		mailTemplate= mailTemplate.replace("<!--SecondsFromLastClientRequest-->", connection.getSecondsFromLastClientRequest().toString());

		return mailTemplate;
	}

	private CharSequence formatStatus(int status, String serverName, String clientName) {
		String statusStr= "";
		if ( (status & ClientServerConnection.STATUS_SERVER_DOWN) != 0 ){
			statusStr= "'" + serverName + "' ";
		}
		boolean both= false;
		if ( (status & ClientServerConnection.STATUS_CLIENT_DOWN) != 0 ){
			if ( (status & ClientServerConnection.STATUS_SERVER_DOWN) != 0 ){
				statusStr += "and ";
				both= true;
			}
			statusStr += "'" + clientName + "' ";
		}
		
		if ( statusStr.isEmpty() ){
			if ( (status & ClientServerConnection.STATUS_CONNECTION_DOWN) != 0 ){
				statusStr= "Connection ";
			}
		}
		if ( statusStr.isEmpty() ){
			statusStr = "'" + serverName + "' " + "'" + clientName + "' " + " are both running";
		}else{
			if ( both ){
				statusStr += "are both down";
			}else{
				statusStr += "is down";
			}
		}
		
		return statusStr;
	}

	private String getMailTemplate() {
		if (!isEmpty(templateFileContent)) {
			return templateFileContent;
		}

		if (!isEmpty(templateFile)) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(new File(templateFile));

				byte[] bytes = new byte[102400];
				int length = -1;
				templateFileContent= "";
				while ((length = fis.read(bytes)) > 0) {
					templateFileContent += new String(bytes, 0, length);
				}

				fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return templateFileContent;
		}
		return  "<html>\n" +
				"  <head><title>enCSM</title></head>\n" +
				"  <body>\n" +
				"    <table>\n" +
				"      <tr>\n" +
				"        <td style=\"width:120px;\">Server</td>\n" +
				"        <td><!--ServerName--> port <!--ServerPort--></td>\n" +
				"      </tr>\n" +
				"      <tr>\n" +
				"        <td> Client </td>\n" +
				"        <td><!--ClientName--></td>\n" +
				"      </tr>\n" + 
				"      <tr>\n" +
				"        <td>Status</td>\n" +
				"        <td><!--Status--></td>\n" +
				"      </tr>\n" +
				"    </table>\n" +
				"  </body>\n" +
				"</html>";
	}

}
