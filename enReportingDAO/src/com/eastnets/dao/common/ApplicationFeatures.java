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

package com.eastnets.dao.common;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * ApplicationFeatures : holds all application features that can be turned on or off by choice, those features can be set from database, environment, preferences or any other way
 * 
 * @author EastNets
 * @since January 28, 2014
 */
public class ApplicationFeatures implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4729970189040924104L;

	private JdbcTemplate jdbcTemplate;
	private DBPortabilityHandler dbPortabilityHandler;
	private Boolean filePayloadSupported;
	private Boolean cmsConnectionSupported;
	private Boolean newSecurity;
	private Boolean addressBookSupported;
	private Boolean analyticsSupported;
	private Boolean recoveryRoleExist;
	private Boolean enableWdmsChanges;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DBPortabilityHandler getDbPortabilityHandler() {
		return dbPortabilityHandler;
	}

	public void setDbPortabilityHandler(DBPortabilityHandler dbPortabilityHandler) {
		this.dbPortabilityHandler = dbPortabilityHandler;
	}

	public boolean isFilePayloadSupported() {
		if (filePayloadSupported == null) {
			String query = "select PROPVALUE from reportingproperties where PROPNAME = 'FilePayload' ";

			List<String> values = jdbcTemplate.query(query, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String value = rs.getString("PROPVALUE");
					return value;
				}
			});
			filePayloadSupported = (values.size() != 0 && values.get(0).equalsIgnoreCase("1"));
		}
		return filePayloadSupported.booleanValue();
	}

	public boolean isCmsConnectionSupported() {

		if (cmsConnectionSupported == null) {
			String query = "select count(*) as count from csm_connection";

			try {
				jdbcTemplate.query(query, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Integer value = rs.getInt("count");
						return value;
					}
				});

				cmsConnectionSupported = new Boolean("true");
			} catch (Exception ex) {
				cmsConnectionSupported = new Boolean("false");
			}
		}

		return cmsConnectionSupported.booleanValue();
	}

	public boolean isEnableWdmsChangesSupported() {
		if (enableWdmsChanges == null) {
			String query = "select IGNORE_FIELD_OPTION from WDUSERSEARCHPARAMETER";

			try {
				jdbcTemplate.query(query, new RowMapper<Long>() {
					@Override
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						Long value = rs.getLong("IGNORE_FIELD_OPTION");
						return value;
					}
				});

				enableWdmsChanges = new Boolean("true");
			} catch (Exception ex) {
				enableWdmsChanges = new Boolean("false");
			}
		}

		return enableWdmsChanges.booleanValue();
	}

	public boolean isAnalyticsSupported() {

		if (analyticsSupported == null) {
			String query = "select BIDIRECTORY from sUserGroup";

			try {
				jdbcTemplate.query(query, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						String value = rs.getString("BIDIRECTORY");
						return value;
					}
				});

				analyticsSupported = new Boolean("true");
			} catch (Exception ex) {
				analyticsSupported = new Boolean("false");
			}
		}

		return analyticsSupported.booleanValue();
	}

	public boolean isNewSecurity() {
		if (newSecurity == null) {
			String query = "select count(*) as count from SROLES";
			try {
				Integer count = jdbcTemplate.queryForObject(query, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Integer value = rs.getInt("count");
						return value;
					}
				});
				if (count != null) {
					newSecurity = new Boolean("true");
				} else {
					newSecurity = new Boolean("false");
				}
			} catch (Exception ex) {
				newSecurity = new Boolean("false");
			}
		}

		return newSecurity.booleanValue();
	}

	public boolean isAddressBookSupported() {

		if (addressBookSupported == null) {

			String query = "select count(*) as count from sUserAddressBook";

			try {
				jdbcTemplate.query(query, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						Integer value = rs.getInt("count");
						return value;
					}
				});

				addressBookSupported = new Boolean("true");

			} catch (Exception ex) {
				addressBookSupported = new Boolean("false");
			}

		}

		return addressBookSupported.booleanValue();
	}

	public boolean isRecoveryRoleSupported() {
		return false;
		// if (recoveryRoleExist == null) {
		// String recoveryQuery = "SELECT COUNT(*) AS RECOVERY FROM DBA_ROLES WHERE ROLE = 'SIDE_RECOVERY'";
		//
		// if (dbPortabilityHandler.getDbType() == DBPortabilityHandler.DB_TYPE_MSSQL) {
		// recoveryQuery = "SELECT DATABASE_PRINCIPAL_ID('side_recovery') AS RECOVERY";
		// }
		// try {
		// Integer recovery = jdbcTemplate.queryForObject(recoveryQuery,
		// new RowMapper<Integer>() {
		// @Override
		// public Integer mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// Integer value = rs.getInt("RECOVERY");
		// return value;
		// }
		// });
		// if (recovery != null) {
		// recoveryRoleExist = new Boolean("true");
		// } else {
		// recoveryRoleExist = new Boolean("false");
		// }
		// } catch (Exception ex) {
		// recoveryRoleExist = new Boolean("false");
		// }
		// }
		// return recoveryRoleExist.booleanValue();
	}

	public Boolean getEnableWdmsChanges() {
		return enableWdmsChanges;
	}

	public void setEnableWdmsChanges(Boolean enableWdmsChanges) {
		this.enableWdmsChanges = enableWdmsChanges;
	}

}
