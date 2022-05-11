package com.eastnets.resilience.digest;

public interface DigestAlgorithm {

	boolean checkDigest64String(String fileName, String digest)
			throws Exception;

	String getAlgorithmType() throws Exception;

	byte[] getDigest(String fileName) throws Exception;

}
