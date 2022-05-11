/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.eastnets.encdec.AESEncryptDecrypt;

/**
 * Used to encrypt password that exists in spring datasource config file.
 * @author EastNets
 * @since September 9, 2012
 */
public class SecureDataSource extends BasicDataSource implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4477242247532431569L;
	private boolean isCalled = false;
    private String schema;
    private String dbType;
    private boolean tnsEnabled;
    private String tnsPath;
    
    @Override
	protected synchronized DataSource createDataSource() throws SQLException {
		if (tnsEnabled) {
			System.setProperty("oracle.net.tns_admin", tnsPath);
		}
		
    	if(!isCalled){
        String decryptedPassword;
		try {
			decryptedPassword = AESEncryptDecrypt.decrypt( super.getPassword() );
			super.setPassword( decryptedPassword );
		} catch (Exception e) {
			// Nothing to do
			System.out.println("password already decrypted ");
		}
		isCalled = true;
    	}
        return super.createDataSource();
    }

    
	@Override
	public String getPassword() {
		String password = super.getPassword();
		System.out.println("Here is encrypted password :::: " + password);
		try {
			String decryptedPassword = AESEncryptDecrypt.decrypt(password); 
			System.out.println("Here is decrypted password :::: " + decryptedPassword);
			return decryptedPassword;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}


	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDbType() {
		return dbType;
	}


	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public boolean isTnsEnabled() {
		return tnsEnabled;
	}


	public void setTnsEnabled(boolean tnsEnabled) {
		this.tnsEnabled = tnsEnabled;
	}

	public String getTnsPath() {
		return tnsPath;
	}


	public void setTnsPath(String tnsPath) {
		this.tnsPath = tnsPath;
	}


	public void setInitializeConnectionSql(String initializeConnectionSql) {
		String schma= this.schema;
		if ( schma ==null || schma.trim().isEmpty() ){
			schma= "SIDE";
		}
		if (dbType.equalsIgnoreCase("Oracle")) {
			//in some locale( like Hungarian ), the order by statement orders the numbers after the letters, when checking license we order the BICs 
			//and generate the sign based on it, so we fail to verify the license due to the change of this string.
			//The following line force the NLS_LANGUAGE for the current database session to AMERICAN which is what we have already tested and we are sure that it works fine.

			super.setConnectionInitSqls( Arrays.asList(new String[] {"alter session set current_schema = " + schema, "ALTER SESSION SET NLS_LANGUAGE= 'AMERICAN'"}));
		}
	}

}