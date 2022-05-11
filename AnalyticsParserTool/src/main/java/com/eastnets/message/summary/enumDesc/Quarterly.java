package com.eastnets.message.summary.enumDesc;

public enum Quarterly {
	Q1(1,2,3,"Q1"), Q2(4,5,6,"Q2"), Q3(7,8,9,"Q3"), Q4(10,11,12,"Q4");

	private final int m1;
	private final int m2;
	private final int m3;
	private final String Quarter ;
	Quarterly(int m1,int m2,int m3,String Quarter) {
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		this.Quarter = Quarter;
	}

	public int getM1() {
		return m1;
	}

	public int getM2() {
		return m2;
	}

	public int getM3() {
		return m3;
	}

	public String getQuarter() {
		return Quarter;
	}

 

	 
}
