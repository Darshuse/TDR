package com.eastnets.service.common;

import java.util.Properties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.eastnets.encdec.AESEncryptDecrypt;


public class MailSenderEN {
	private String host;
	private int port;
	private String username;
	private String password;
 	private Properties javaMailProperties;
 	private Boolean authenticationRequired; 	

	JavaMailSenderImpl getJavaMailSenderImpl()
 	{
 		JavaMailSenderImpl imp = new JavaMailSenderImpl();
 		
 		imp.setHost(host);
 		imp.setPort(port);
 		if ( authenticationRequired  ){
	 		imp.setUsername(username);
	 		
	 		String decryptedPassword;
	 		try {
				decryptedPassword = AESEncryptDecrypt.decrypt(password);
				imp.setPassword( decryptedPassword );
			} catch (Exception e) {
				// Nothing to do
				System.out.println("Mail password already decrypted ");
				imp.setPassword( password );
			}
 		}
 		imp.setJavaMailProperties(javaMailProperties);
 		
 		return imp; 		
 	}
 	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Properties getJavaMailProperties() {
		return javaMailProperties;
	}
	public void setJavaMailProperties(Properties javaMailProperties) {
		this.javaMailProperties = javaMailProperties;
	}

 	public Boolean getAuthenticationRequired() {
		return authenticationRequired;
	}

	public void setAuthenticationRequired(Boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	} 	
}
