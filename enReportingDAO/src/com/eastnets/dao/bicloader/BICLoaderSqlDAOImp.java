package com.eastnets.dao.bicloader;

public class BICLoaderSqlDAOImp extends BICLoaderDAOImp{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8870648151213579898L;

	
	@Override
	public void insertIntoErrorld(String errorExe, String errorLevel, String errorModule, String errMsg1,
			String errMsg2) {

		String insertQuery = "INSERT INTO ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery,
				new Object[] { errorExe, new java.util.Date(), errorLevel, errorModule, errMsg1, "" });
	}
}
