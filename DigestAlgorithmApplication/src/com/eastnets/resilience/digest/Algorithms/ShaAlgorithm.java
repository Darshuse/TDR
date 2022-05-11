package com.eastnets.resilience.digest.Algorithms;

import java.io.FileInputStream;
import java.io.Serializable;
import java.security.MessageDigest;


import com.eastnets.resilience.digest.DigestAlgorithm;

public abstract class ShaAlgorithm implements Serializable, DigestAlgorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4136031815239514921L;

	@Override
	public boolean checkDigest64String(String fileName, String digest)
			throws Exception {

		String str64 = new sun.misc.BASE64Encoder().encode(this
				.getDigest(fileName));
		
		return str64 != null ? str64.equals(digest != null ? digest : "")
				: false;
	}

	@Override
	public byte[] getDigest(String fileName) throws Exception {

		MessageDigest md = MessageDigest.getInstance(this.getAlgorithmType());
		FileInputStream fis = new FileInputStream(fileName);
		byte[] dataBytes = new byte[1024];
		int nread = 0;

		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}
		;
		fis.close();
		byte[] mdbytes = md.digest();

		return mdbytes;
	}

}
