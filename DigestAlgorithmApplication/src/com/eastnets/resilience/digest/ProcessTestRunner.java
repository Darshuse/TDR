package com.eastnets.resilience.digest;

import java.util.Date;

public class ProcessTestRunner {

	public static void main(String args[]) {

		//ProcessTestRunner obj = new ProcessTestRunner();
		try {
			Date startTime = new Date();

			DigestAlgorithm digestAlg = DigestAlgorithmFactory
					.getDigestAlgorithm("SHA256");

			boolean value = digestAlg.checkDigest64String(
					"D:\\test\\Sa3eedFodeInKuwait.flv",
					"gzM0cfL6Prl3RrHeiT2ZtTF3690VoCuBd0R2oRl1rhc=");

			Date endTime = new Date();
			
			System.out.println("Result :" + value);
			System.out.println("Start Time :" + startTime);
			System.out.println("End Time :" + endTime);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
