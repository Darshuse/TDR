package com.eastnets.utils;

import com.eastnets.encdec.AESEncryptDecrypt;

/*import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;*/
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/*import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;*/
import javax.net.ssl.X509TrustManager;

public class SSLConfig {
	
	private String keyStore;
	private String keyStorePassword;
	private String keyStoreType;
	
	private String trustStore;
	private String trustStorePassword;
	private String trustStoreType;
	
	public String initDone()
	{
		if ( !defaultString(keyStore).isEmpty() )
		{
			System.setProperty("javax.net.ssl.keyStore", defaultString(keyStore));
			
			String decryptedKeyStorePassword;
			try {
				decryptedKeyStorePassword = AESEncryptDecrypt.decrypt(keyStorePassword);
				System.setProperty("javax.net.ssl.keyStorePassword", defaultString(decryptedKeyStorePassword));
			} catch (Exception e) {
				// Nothing to do
				System.out.println("key store password already decrypted ");
				System.setProperty("javax.net.ssl.keyStorePassword", defaultString(keyStorePassword));
			}
			
			System.setProperty("javax.net.ssl.keyStoreType", defaultString(keyStoreType, "JKS"));			
		}
		if ( !defaultString(trustStore).isEmpty() )
		{
			System.setProperty("javax.net.ssl.trustStore", defaultString(trustStore) );
			
			String decryptedTrustStorePassword;
			try {
				decryptedTrustStorePassword = AESEncryptDecrypt.decrypt(trustStorePassword);
				System.setProperty("javax.net.ssl.trustStorePassword", defaultString(decryptedTrustStorePassword));
			} catch (Exception e) {
				// Nothing to do
				System.out.println("trust store password already decrypted ");
				System.setProperty("javax.net.ssl.trustStorePassword", defaultString(trustStorePassword));
			}
			
			System.setProperty("javax.net.ssl.trustStoreType", defaultString(trustStoreType, "JKS") );			
		}

		//TODO this is a security breach, don't use it 
		//change the SSL Context so that that it accept any cert
		/*SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		*/
		return "";
	}
	

	private String defaultString(String string) {
		return defaultString( string, "" );
	}
	private String defaultString(String string, String defaultValue ) {
		if ( string == null )
			return defaultValue;
		return string;
	}


	public String getKeyStore() {
		return keyStore;
	}


	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}


	public String getKeyStorePassword() {
		return keyStorePassword;
	}


	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}


	public String getKeyStoreType() {
		return keyStoreType;
	}


	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}


	public String getTrustStore() {
		return trustStore;
	}


	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}


	public String getTrustStorePassword() {
		return trustStorePassword;
	}


	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}


	public String getTrustStoreType() {
		return trustStoreType;
	}


	public void setTrustStoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}	
}

class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    	//System.out.println(arg1);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    	System.out.println(arg1);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}