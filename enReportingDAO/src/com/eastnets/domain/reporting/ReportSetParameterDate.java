package com.eastnets.domain.reporting;

import java.io.Serializable;

public class ReportSetParameterDate extends ReportSetParameterDateBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4918725513534383402L;

	@Override
	public boolean getWithDate() {
		return false;
	}

}
