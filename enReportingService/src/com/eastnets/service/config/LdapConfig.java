package com.eastnets.service.config;

public class LdapConfig{
	public String domain= "";
	public String url= "";
	public String base= "";
	public String userDn= "";
	public String password= "";
	public boolean pooled = false;
	public String referral= "ignore";
	public Integer numberOfTries=1;
	public String attrUsername= "";
}
