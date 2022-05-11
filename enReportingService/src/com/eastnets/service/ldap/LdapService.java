package com.eastnets.service.ldap;

import java.util.Map;

import com.eastnets.service.Service;

public interface LdapService extends Service {

	public Map<String,String> getLdapUserEmailsBySn();
}
