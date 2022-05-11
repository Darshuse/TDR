package com.eastnets.resilience.digest.Algorithms;

import java.io.Serializable;

public class SHA1 extends ShaAlgorithm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1732659438714777157L;

	@Override
	public String getAlgorithmType() throws Exception {
		return "SHA-1";
	}

}
