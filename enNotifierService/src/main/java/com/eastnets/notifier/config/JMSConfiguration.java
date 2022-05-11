package com.eastnets.notifier.config;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
@EnableJms
public class JMSConfiguration {

	@Autowired
	private ProjectProperties projectProperties;

	@Bean
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory factory = new MQQueueConnectionFactory();

		try {

			factory.setHostName(projectProperties.getHostName());
			factory.setPort(projectProperties.getIbmPort());
			factory.setChannel(projectProperties.getChannelName());
			factory.setQueueManager(projectProperties.getQueueManager());
			factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

			if (projectProperties.getSSLCipherSpec() != null && !projectProperties.getSSLCipherSpec().isEmpty()) {
				factory.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SPEC, projectProperties.getSSLCipherSpec());
			}

			if (projectProperties.getSslPeerName() != null && !projectProperties.getSslPeerName().isEmpty()) {
				factory.setStringProperty(WMQConstants.WMQ_SSL_PEER_NAME, projectProperties.getSslPeerName());
			}

			if (!projectProperties.isNoAuthentication()) {
				factory.setStringProperty(WMQConstants.USERID, projectProperties.getMqUserName());
				factory.setStringProperty(WMQConstants.PASSWORD, projectProperties.getMqPassword());
			}

			factory.setSSLFipsRequired(projectProperties.isEnableFIPS());

			if (projectProperties.getSslCipherSuite() != null && !projectProperties.getSslCipherSuite().isEmpty()) {
				factory.setStringProperty(MQConstants.SSL_CIPHER_SUITE_PROPERTY, projectProperties.getSslCipherSuite());
				factory.setSSLCipherSuite(projectProperties.getSslCipherSuite().trim());
			}

			if (!defaultString(projectProperties.getSslKeyStore()).isEmpty()) {
				System.setProperty("javax.net.ssl.keyStore", defaultString(projectProperties.getSslKeyStore()));

				String decryptedKeyStorePassword;
				try {
					decryptedKeyStorePassword = AESEncryptDecrypt.decrypt(projectProperties.getSslKeyStorePassword());
					System.setProperty("javax.net.ssl.keyStorePassword", defaultString(decryptedKeyStorePassword));
				} catch (Exception e) {
					// Nothing to do
					System.out.println("key store password already decrypted ");
					System.setProperty("javax.net.ssl.keyStorePassword", defaultString(projectProperties.getSslKeyStorePassword()));
				}

				System.setProperty("javax.net.ssl.keyStoreType", defaultString(projectProperties.getSslKeyStoreType(), "JKS"));
			}

			if (!defaultString(projectProperties.getSslTrustStore()).isEmpty()) {
				System.setProperty("javax.net.ssl.trustStore", defaultString(projectProperties.getSslTrustStore()));

				String decryptedTrustStorePassword;
				try {
					decryptedTrustStorePassword = AESEncryptDecrypt.decrypt(projectProperties.getSslTrustStorePassword());
					System.setProperty("javax.net.ssl.trustStorePassword", defaultString(decryptedTrustStorePassword));
				} catch (Exception e) {
					// Nothing to do
					System.out.println("trust store password already decrypted ");
					System.setProperty("javax.net.ssl.trustStorePassword", defaultString(projectProperties.getSslTrustStorePassword()));
				}

				System.setProperty("javax.net.ssl.trustStoreType", defaultString(projectProperties.getSslTrustStoreType(), "JKS"));
			}

			if (projectProperties.getUseIBMCipherMappings().equalsIgnoreCase("false")) {
				System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
			} else {
				System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "true");
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return factory;
	}

	private String defaultString(String string) {
		return defaultString(string, "");
	}

	private String defaultString(String string, String defaultValue) {
		if (string == null)
			return defaultValue;
		return string;
	}

}
