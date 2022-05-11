package com.eastnets.resilience.digest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DigestAlgorithmFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081205855174496499L;
	private static Map<String, DigestAlgorithm> digestAlgorithms = new HashMap<String, DigestAlgorithm>();

	public static DigestAlgorithm getDigestAlgorithm(String algorithmClass)
			throws Exception {
		algorithmClass = algorithmClass.replace("-", "");// remove the - in the
															// algorithm name
		if (!digestAlgorithms.containsKey(algorithmClass)) {
			Object obj = Class.forName(
					"com.eastnets.resilience.digest.Algorithms."
							+ algorithmClass).newInstance();
			digestAlgorithms.put(algorithmClass, (DigestAlgorithm) obj);
		}

		return digestAlgorithms.get(algorithmClass);
	}

}
