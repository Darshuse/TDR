package com.eastnets.util;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
@EnableJms
public class JMSConfiguration {
	private static final Logger LOGGER = Logger.getLogger(JMSConfiguration.class);

	@Autowired()
	private AppConfigBean appConfigBean;

	@Bean(name = "mqConnectionFactory")
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		try {
			factory.setHostName(appConfigBean.getHostName());
			factory.setPort(appConfigBean.getIbmPort());
			factory.setChannel(appConfigBean.getChannelName());
			factory.setQueueManager(appConfigBean.getQueueManager());
			factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

			if (appConfigBean.getSSLCipherSpec() != null && !appConfigBean.getSSLCipherSpec().isEmpty()) {
				factory.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SPEC, appConfigBean.getSSLCipherSpec());
			}

			if (appConfigBean.getSslPeerName() != null && !appConfigBean.getSslPeerName().isEmpty()) {
				factory.setStringProperty(WMQConstants.WMQ_SSL_PEER_NAME, appConfigBean.getSslPeerName());
			}

			if (!appConfigBean.isNoAuthentication()) {
				factory.setStringProperty(WMQConstants.USERID, (appConfigBean.getMqUserName().isEmpty()) ? " " : appConfigBean.getMqUserName());
				factory.setStringProperty(WMQConstants.PASSWORD, (appConfigBean.getMqPassword().isEmpty()) ? " " : appConfigBean.getMqPassword());
			}

			factory.setSSLFipsRequired(appConfigBean.isEnableFIPS());

			if (appConfigBean.getSslCipherSuite() != null && !appConfigBean.getSslCipherSuite().isEmpty()) {
				factory.setStringProperty(MQConstants.SSL_CIPHER_SUITE_PROPERTY, appConfigBean.getSslCipherSuite());
				factory.setSSLCipherSuite(appConfigBean.getSslCipherSuite().trim());
			}

			if (!defaultString(appConfigBean.getSslKeyStore()).isEmpty()) {
				System.setProperty("javax.net.ssl.keyStore", defaultString(appConfigBean.getSslKeyStore()));

				String decryptedKeyStorePassword;
				try {
					decryptedKeyStorePassword = AESEncryptDecrypt.decrypt(appConfigBean.getSslKeyStorePassword());
					System.setProperty("javax.net.ssl.keyStorePassword", defaultString(decryptedKeyStorePassword));
				} catch (Exception e) {
					// Nothing to do
					System.out.println("key store password already decrypted ");
					System.setProperty("javax.net.ssl.keyStorePassword", defaultString(appConfigBean.getSslKeyStorePassword()));
				}

				System.setProperty("javax.net.ssl.keyStoreType", defaultString(appConfigBean.getSslKeyStoreType(), "JKS"));
			}

			if (!defaultString(appConfigBean.getSslTrustStore()).isEmpty()) {
				System.setProperty("javax.net.ssl.trustStore", defaultString(appConfigBean.getSslTrustStore()));

				String decryptedTrustStorePassword;
				try {
					decryptedTrustStorePassword = AESEncryptDecrypt.decrypt(appConfigBean.getSslTrustStorePassword());
					System.setProperty("javax.net.ssl.trustStorePassword", defaultString(decryptedTrustStorePassword));
				} catch (Exception e) {
					// Nothing to do
					System.out.println("trust store password already decrypted ");
					System.setProperty("javax.net.ssl.trustStorePassword", defaultString(appConfigBean.getSslTrustStorePassword()));
				}

				System.setProperty("javax.net.ssl.trustStoreType", defaultString(appConfigBean.getSslTrustStoreType(), "JKS"));
			}

			if (appConfigBean.getUseIBMCipherMappings().equalsIgnoreCase("false")) {
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
