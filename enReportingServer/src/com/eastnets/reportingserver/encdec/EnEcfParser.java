package com.eastnets.reportingserver.encdec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class EnEcfParser {
	
	//ECF handlers
	private static final byte[] keyValue = new byte[] { 'd', '3', 'b', 'e', '4', '6', '4', '1', '2', '9', 'b', '7', 'd', '8', 'a', '7' };

	
	public static ConnectionSettings parseECF(String filePath) throws Exception {
		
		try {

			ConnectionSettings cs = new ConnectionSettings();

			File ecfFile = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(ecfFile));

			String encryptedValue = reader.readLine();
			reader.close();

			Key key = new SecretKeySpec(keyValue, "AES");

			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.decodeBase64(encryptedValue);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);

			String[] args = decryptedValue.split("\t");

			cs.setUserName(args[0]);

			cs.setPassword(args[1]);

			String server = args[2];
			int index = server.lastIndexOf("/");
			if (index != -1) {
				cs.setServiceName(server.substring(index + 1));
				server = server.substring(0, index);
			}
			index = server.lastIndexOf(":");
			if (index != -1) {
				cs.setPortNumber(Integer.parseInt(server.substring(index + 1)));
				server = server.substring(0, index);
			}
			cs.setServerName(server);

			return cs;
		} catch (Exception ex) {
			throw new Exception("Invalid ECF file : " + ex.getMessage());
		}
	}

}
