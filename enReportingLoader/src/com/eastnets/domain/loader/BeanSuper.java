package com.eastnets.domain.loader;

import java.math.BigDecimal;
import java.util.Date;

public class BeanSuper {
	private String name;
	private BigDecimal big;
	
	private Date big2;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBig() {
		return big;
	}

	public void setBig(BigDecimal big) {
		this.big = big;
	}

	public Date getBig2() {
		return big2;
	}

	public void setBig2(Date big2) {
		this.big2 = big2;
	}


}
