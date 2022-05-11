package com.eastnets.dao.bicloader;

public class BICLoaderOracleDAOImp extends  BICLoaderDAOImp {

	private static final long serialVersionUID = 5080254994514807240L;

	/**
	 * 
	 */

	
	@Override
	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1,
			String errMsg2) {
		Long mesgID = jdbcTemplate.queryForLong("select LDERRORS_ID.NEXTVAL from dual ");
		
		String insertQuery = "INSERT INTO ldErrors(ErrID,ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery,
				new Object[] { mesgID, errorExe, new java.util.Date(), errorLevel, errorModule, errMsg1, "" });
	}
}
