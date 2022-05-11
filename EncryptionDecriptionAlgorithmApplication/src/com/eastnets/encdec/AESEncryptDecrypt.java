/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.encdec;

import java.io.Serializable;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESEncryptDecrypt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -549895562661124453L;
	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'd', '3', 'b', 'e', '4', '6', '4', '1', '2', '9', 'b', '7', 'd', '8', 'a', '7' };

	public static synchronized String encrypt(String valueToEnc) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = Base64.encodeBase64String(encValue);
		return encryptedValue;
	}

	public static synchronized String decrypt(String encryptedValue) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);
		// byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decordedValue = Base64.decodeBase64(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public static synchronized String encryptOneWay(String plaintext) throws Exception {
		MessageDigest msgDigest = null;
		String hashValue = null;

		msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update(plaintext.getBytes("UTF-8"));
		byte rawByte[] = msgDigest.digest();
		hashValue = Base64.encodeBase64String(rawByte);
		return hashValue;
	}

	public static synchronized String decrypt(String encryptedValue, String salt) throws Exception {

		byte[] raw = salt.getBytes();

		Cipher cipher;

		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, "AES"));

		return new String(cipher.doFinal(hex2Byte(encryptedValue)));

	}

	protected static byte[] hex2Byte(String str) {

		byte[] bytes = new byte[str.length() / 2];

		for (int i = 0; i < bytes.length; i++) {

			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);

		}

		return bytes;

	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(AESEncryptDecrypt.encryptOneWay("test1234;kjsdfglmn34iusdlmnerplkjadfpkgj56piou*^%#$UKGFJHG&*%$%$yu8tijh3250iusdfgoklmn34lkm&(&^#$^%jh23ljh"));
		System.out.println(AESEncryptDecrypt.encryptOneWay("test1234;kjsdfglmn34iusdlmnerplkjadfpkgj56piou*^%#$UKGFJHG&*%$%$yu8tijh3250iusdfgoklmn34lkm&(&^#$^%jh23ljh"));

		System.out.println(AESEncryptDecrypt.encryptOneWay("test"));
	}
}
