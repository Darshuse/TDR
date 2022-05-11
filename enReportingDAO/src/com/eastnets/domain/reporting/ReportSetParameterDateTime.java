package com.eastnets.domain.reporting;

import java.io.Serializable;

public class ReportSetParameterDateTime extends ReportSetParameterDateBase
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7199593976288552435L;

	@Override
	public boolean getWithDate() {
		return true;
	}

}
