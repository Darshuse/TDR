package com.eastnets.service.security.data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.eastnets.dao.security.data.SecurityDataBean;

public class SecurityDataCache {

	private Map<Long, SecurityDataBean> allSecurityDataByUserGroupMap;

	public void init() {
		allSecurityDataByUserGroupMap = new ConcurrentHashMap<Long, SecurityDataBean>();

	}

	/**
	 * 
	 * @param securityDataBean
	 */
	public void cache(SecurityDataBean securityDataBean, boolean withOverride) {
		if (withOverride) {
			allSecurityDataByUserGroupMap.put(securityDataBean.getGroupId(), securityDataBean);
		} else {

			allSecurityDataByUserGroupMap.putIfAbsent(securityDataBean.getGroupId(), securityDataBean);
		}

	}

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public SecurityDataBean find(Long groupId) {

		return Optional.ofNullable(allSecurityDataByUserGroupMap.get(groupId)).orElse(null);
	}

}
