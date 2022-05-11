/**
 * 
 */
package com.eastnets.dao.license;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import com.eastnets.dao.common.Constants;


/**
 * 
 * @author EastNets
 * @since January 15, 2013
 * 
 */
public class LicenseOracleDAOImpl extends LicenseDAOImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2006147440394204603L;

	@Override
    public void deleteLicensedBICExceptTnT() {
        jdbcTemplate.execute("delete sLicensedBIC where substr(BICCODE,8,1) <> '0'");
    }
	
	@Override
	public void deleteNonParentTnTLicensedBIC() {
	    jdbcTemplate.execute("delete from sLicensedBIC " +
	                         "    where substr(BICCODE,8,1) = '0' " +
	                         "      and substr(BICCODE,1,7) not in " +
	                         "              (select substr(BICCODE,1,7) " +
	                         "                   from sLicensedBIC " +
	                         "                   where substr(BICCODE,8,1) <> '0')");
	}



	@Override
	public boolean checkLicenseViolationError() throws Exception {
	
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1 ;
		
		String errorQueryString = "select COUNT(*) as count from sLicensedBIC " +
				" where OCT2DEC(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',') + 1 , 1 )) >= " + Constants.LICENSE_ERROR_MONTH_COUNT +
				" and OCT2DEC(replace(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',',1,2) + 1 , 2 ),',','')) + 1 = " + month ;
		
		boolean licenseError = false;
		
		List<Long> errorList = (List<Long>) jdbcTemplate.query(errorQueryString, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return new Long(rs.getLong("count"));
			}
		});
		
		if(!errorList.isEmpty() && errorList.get(0) > 0 ){
			licenseError = true;
		}
				
		return licenseError;
	}
	
	@Override
	public boolean checkLicenseViolationWarning() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1 ;
		
		String warningQueryString = "select COUNT(*) as count from sLicensedBIC " +
				" where OCT2DEC(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',') + 1 , 1 )) in (" + Constants.LICENSE_WARNING_MONTH_COUNT + ") " + 
				" and OCT2DEC(replace(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',',1,2) + 1 , 2 ),',','')) + 1 = " + month ;
		
		boolean licenseWarning = false;
		
		List<Long> warningList = (List<Long>) jdbcTemplate.query(warningQueryString, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return new Long(rs.getLong("count"));
			}
		});
		
		if(!warningList.isEmpty() && warningList.get(0) > 0 ){
			licenseWarning = true;
		}
		
		return licenseWarning;
	}
	
	
	
	
	@Override
	public int getViolationCount() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1 ;
		
		String queryString = " select max(OCT2DEC(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',') + 1 , 1 ))) as violationMonthCount " +
		" from sLicensedBIC where OCT2DEC(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',') + 1 , 1 )) in (" + Constants.LICENSE_WARNING_MONTH_COUNT + ")" +
		" and OCT2DEC(replace(SUBSTR( LicenseInfo, INSTR(LicenseInfo, ',',1,2) + 1 , 2 ),',','')) + 1 = " + month ;
				
		List<Integer> list = (List<Integer>) jdbcTemplate.query(queryString, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {	
				return new Integer(rs.getInt("violationMonthCount"));
			}
		});
		
		if(list.isEmpty()){
			return 0;
		}
		
		return list.get(0).intValue();
	}
	
}
