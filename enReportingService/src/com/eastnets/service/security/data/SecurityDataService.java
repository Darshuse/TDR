package com.eastnets.service.security.data;

import com.eastnets.dao.security.data.SecurityDataBean;

/**
 * @since 27/072020
 * @version 1.0
 * @author AHammad
 *
 */
public interface SecurityDataService {

	/**
	 * A convenience method to retrieve all Security (license BICS,Category,UNITS) based on LoggedIn user group
	 * 
	 * @param groupId
	 * @return List of all Units
	 */
	public SecurityDataBean getAllSecurityDataByGroupId(Long groupId);

	/**
	 * A convenience method to refresh value when updating Profile
	 * 
	 * @param groupId
	 * @return void
	 */
	public void overwriteMapValue(Long groupId);
}
