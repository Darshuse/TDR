package com.eastnets.watchdog.resultbeans;

import java.math.BigDecimal;

public class PossibleDuplicateCustomMessageInfo {

	private BigDecimal mesgUserIssuedAsPDE = new BigDecimal(0);
	private String mesgPossibleDupCreation;

	public PossibleDuplicateCustomMessageInfo(BigDecimal mesgUserIssuedAsPDE, String mesgPossibleDupCreation) {
		this.mesgUserIssuedAsPDE = mesgUserIssuedAsPDE;
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

	public BigDecimal getMesgUserIssuedAsPDE() {
		return mesgUserIssuedAsPDE;
	}

	public void setMesgUserIssuedAsPDE(BigDecimal mesgUserIssuedAsPDE) {
		this.mesgUserIssuedAsPDE = mesgUserIssuedAsPDE;
	}

	public String getMesgPossibleDupCreation() {
		return mesgPossibleDupCreation;
	}

	public void setMesgPossibleDupCreation(String mesgPossibleDupCreation) {
		this.mesgPossibleDupCreation = mesgPossibleDupCreation;
	}

}
