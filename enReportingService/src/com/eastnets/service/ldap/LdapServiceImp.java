package com.eastnets.service.ldap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.ldap.core.simple.SimpleLdapTemplate;

import com.eastnets.domain.Pair;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.service.config.LdapConfig;
import com.eastnets.service.security.LdapAuthenticationHandler;

public class LdapServiceImp extends ServiceBaseImp implements LdapService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	private String base;
	private String mail;
	private String cn;
	private String sn;
	private LdapAuthenticationHandler ldapAuthenticationHandler;

	@Override
	public Map<String, String> getLdapUserEmailsBySn() {
		// TODO Auto-generated method stub

		Map<String, String> ldapUsers = new HashMap<String, String>();

		try {

			List<Pair<SimpleLdapTemplate, LdapConfig>> ldapTemplates = ldapAuthenticationHandler
					.getLdapTemplates();

			// System.out.println(ldapTemplates.size());

			for (Pair<SimpleLdapTemplate, LdapConfig> template : ldapTemplates) {

				String[] attrIDs = { mail, cn };

				SearchControls ctls = new SearchControls();
				ctls.setReturningAttributes(attrIDs);
				
				ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

				// String filter = "(&(sn=fatimaLD)(mail=*))";
			
				String filter = "(&(" + sn + "=*))";
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"com.sun.jndi.ldap.LdapCtxFactory");
				env.put(Context.PROVIDER_URL, template.getValue().url);
				DirContext ctx = null;
				try {
					ctx = new InitialDirContext(env);
				} catch (Exception e) {
					// TODO: handle exception
				//	System.err.println("Error");
				}

				if (ctx != null) {
					NamingEnumeration e = ctx.search(template.getValue().base,
							filter, ctls);

					while (e.hasMore()) {
						SearchResult entry = (SearchResult) e.next();

						
						if(entry.getAttributes().get(attrIDs[1]) != null && entry.getAttributes().get(attrIDs[0]) != null){
							ldapUsers.put(entry.getAttributes().get(attrIDs[1]).get()
									.toString(), entry.getAttributes().get(attrIDs[0])
									.get().toString());
						}


					}
				}

			}
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ldapUsers;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public LdapAuthenticationHandler getLdapAuthenticationHandler() {
		return ldapAuthenticationHandler;
	}

	public void setLdapAuthenticationHandler(
			LdapAuthenticationHandler ldapAuthenticationHandler) {
		this.ldapAuthenticationHandler = ldapAuthenticationHandler;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	

}
