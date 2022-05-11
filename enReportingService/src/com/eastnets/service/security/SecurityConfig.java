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

package com.eastnets.service.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.eastnets.domain.admin.User;

/**
 * Security Config Bean
 * @author EastNets
 * @since September 3, 2012
 */
public class SecurityConfig implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1219053715129750480L;

	private List<UrlRolePermissions> urlPermissions = new ArrayList<UrlRolePermissions>();
	
	private List<String> unSecureURLs = new ArrayList<String>();
	
	private String loginURL;
	
	private String changepasswordURL;

	public List<UrlRolePermissions> getUrlPermissions() {
		return urlPermissions;
	}

	public void setUrlPermissions(List<UrlRolePermissions> urlPermissions) {
		this.urlPermissions = urlPermissions;
	}
	
	public boolean doesUserHasPermissionOnCurrentRequest(User user, String requestedUrl)
	{
		if(user == null)
		{
			return false;
		}
		
		
		if(isURLUnsecured(requestedUrl))
		{
			return true;
		}
		
		if(isLoginURL(requestedUrl))
		{
			return true;
		}
				
		for (UrlRolePermissions permission : urlPermissions) {
			
			if(requestedUrl.startsWith(permission.getUrl()))
			{
				return checkUserPermissionOnUrl(user, permission);
			}
		}
		
		return false;
	}

	public boolean isLoginURL(String requestedUrl) {
		return requestedUrl.startsWith(loginURL);
	}

	private boolean isURLUnsecured(String requestedUrl) {
		
		for (String url : getUnSecureURLs()) {
			if(requestedUrl.startsWith(url))
			{
				return true;
			}
		}
		
		return false;
	}

	private boolean checkUserPermissionOnUrl(User user,
			UrlRolePermissions permission) {
		
		List<String> profileRoles = user.getProfile().getProfileRoles();
		
		List<String> urlRoles = permission.getRoles();
		
		for (String urlRole : urlRoles) {
			if(profileRoles.contains(urlRole))
			{
				return true;
			}
		}
		
		return false;
	}

	public void setUnSecureURLs(List<String> unSecureURLs) {
		this.unSecureURLs = unSecureURLs;
	}

	public List<String> getUnSecureURLs() {
		return unSecureURLs;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getLoginURL() {
		return loginURL;
	}

	public String getChangepasswordURL() {
		return changepasswordURL;
	}

	public void setChangepasswordURL(String changepasswordURL) {
		this.changepasswordURL = changepasswordURL;
	}
	
}
