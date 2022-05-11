package com.eastnets.resilience.digest.Algorithms;

import java.io.Serializable;

public class SHA256 extends ShaAlgorithm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8205297547422759104L;

	@Override
	public String getAlgorithmType() throws Exception {
		return "SHA-256";
	}

}
