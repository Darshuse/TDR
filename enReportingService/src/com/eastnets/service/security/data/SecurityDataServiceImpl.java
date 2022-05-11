package com.eastnets.service.security.data;

import com.eastnets.dao.security.data.SecurityDataBean;
import com.eastnets.dao.security.data.SecurityDataDAO;
import com.eastnets.service.ServiceBaseImp;

/**
 * @since 27/072020
 * @version 1.0
 * @author AHammad
 *
 */
public class SecurityDataServiceImpl extends ServiceBaseImp implements SecurityDataService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2752455184403793752L;

	private SecurityDataCache securityDataCache;

	private SecurityDataDAO securityDataDAO;

	@Override
	public SecurityDataBean getAllSecurityDataByGroupId(Long groupId) {
		SecurityDataBean securityDataBean = securityDataCache.find(groupId);
		if (securityDataBean != null)
			return securityDataBean;
		SecurityDataBean securityDataByGroupId = securityDataDAO.getAllSecurityDataByGroupId(groupId);
		securityDataCache.cache(securityDataByGroupId, false);
		return securityDataByGroupId;

	}

	@Override
	public void overwriteMapValue(Long groupId) {
		SecurityDataBean securityDataBean = securityDataCache.find(groupId);
		if (securityDataBean != null) {
			SecurityDataBean securityDataByGroupId = securityDataDAO.getAllSecurityDataByGroupId(groupId);
			securityDataCache.cache(securityDataByGroupId, true);
		}

	}

	public SecurityDataCache getSecurityDataCache() {
		return securityDataCache;
	}

	public void setSecurityDataCache(SecurityDataCache securityDataCache) {
		this.securityDataCache = securityDataCache;
	}

	public SecurityDataDAO getSecurityDataDAO() {
		return securityDataDAO;
	}

	public void setSecurityDataDAO(SecurityDataDAO securityDataDAO) {
		this.securityDataDAO = securityDataDAO;
	}

}
