package com.eastnets.service.security.auth;

import java.sql.SQLException;

import com.eastnets.domain.admin.User;
import com.eastnets.domain.security.PasswordPolicy;
import com.eastnets.service.common.PasswordChangeStatus;

public interface Authenticator {
	
	/**
	 * @return the id of the authentication method that this authenticator will handle. As specified in the User.AuthenticationMethod and the sUsers Table
	 */
	public int authenticationMethod();

	/**
	 * Authenticate user
	 * @param user
	 * @param domain might be null or empty
	 * @return User
	 * @throws Exception 
	 * @throws WebClientException
	 */
	public boolean authenticate(User user, String domain) throws Exception ;

	/**
	 * Change user password
	 * @param user
	 * @param newUserPassword
	 * @param passwordPolicy 
	 * @return boolean
	 * @throws WebClientException
	 */
	public PasswordChangeStatus changeUserPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy, boolean unlockUser) ;
	
	/**
	 * 
	 * @param userName
	 * @param newUserPassword
	 * @param unlockUser
	 * @throws SQLException
	 */
	public PasswordChangeStatus resetPassword(User user, String newUserPassword, PasswordPolicy passwordPolicy,boolean unlockUser);
}
