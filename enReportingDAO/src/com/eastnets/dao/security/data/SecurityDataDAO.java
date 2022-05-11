package com.eastnets.dao.security.data;

/**
 * @since 27/072020
 * @version 1.0
 * @author AHammad
 *
 */
public interface SecurityDataDAO {

	// /**
	// * A convenience method to retrieve all license BICS based on LoggedIn user group
	// *
	// * @param groupId
	// * @return List of all license BICS
	// */
	// public SecurityDataBean getAllLicenseBicsByGroupId(Long groupId);
	//
	// /**
	// * A convenience method to retrieve all Category based on LoggedIn user group
	// *
	// * @param groupId
	// * @return List of all Category
	// */
	// public SecurityDataBean getAllCategoryByGroupId(Long groupId);
	//
	// /**
	// * A convenience method to retrieve all UNITS based on LoggedIn user group
	// *
	// * @param groupId
	// * @return List of all Units
	// */
	// public SecurityDataBean getAllUnitsByGroupId(Long groupId);

	/**
	 * A convenience method to retrieve all Security (license BICS,Category,UNITS) based on LoggedIn user group
	 * 
	 * @param groupId
	 * @return List of all Units
	 */
	public SecurityDataBean getAllSecurityDataByGroupId(Long groupId);

}
