package com.eastnets.configuration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import com.eastnets.encdec.AESEncryptDecrypt;

@Component
public class AppContainerCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	private static final Logger LOGGER = LogManager.getLogger(AppContainerCustomizer.class);

	@Autowired
	private AppConfig appConfig;

	public void customize(ConfigurableServletWebServerFactory factory) {

		factory.setPort(appConfig.getServerConfig().getPort());

		String password = appConfig.getServerConfig().getSslkeyPassword();
		try {
			password = AESEncryptDecrypt.decrypt(password);
		} catch (Exception e) {
			LOGGER.debug("SSL password already decrypted ");
		}

		Ssl ssl = new Ssl();
		ssl.setKeyAlias(appConfig.getServerConfig().getSslkeyAlias());
		ssl.setKeyStore(appConfig.getServerConfig().getSslkeyStore());
		ssl.setKeyStorePassword(password);
		ssl.setKeyStoreProvider("SUN");
		ssl.setKeyStoreType("JKS");
		factory.setSsl(ssl);

		LOGGER.debug("Prot number :" + appConfig.getServerConfig().getPort());

	}

}
