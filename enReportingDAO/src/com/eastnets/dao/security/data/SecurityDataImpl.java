package com.eastnets.dao.security.data;

import com.eastnets.dao.DAOBaseImp;

public class SecurityDataImpl extends DAOBaseImp implements SecurityDataDAO {

	private SecurityDataBean securityDataBean;

	public void init() {
		securityDataBean = new SecurityDataBean();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2936816367027813273L;

	@Override
	public SecurityDataBean getAllSecurityDataByGroupId(Long groupId) {
		securityDataBean.setGroupId(groupId);
		securityDataBean.setAllLicenseBics(getJdbcTemplate().queryForList(" SELECT BICCODE FROM SBICUSERGROUP WHERE GROUPID= ? ", new Object[] { groupId }, String.class));
		securityDataBean.setAllCategoriesList(getJdbcTemplate().queryForList(" SELECT CATEGORY FROM SMSGUSERGROUP WHERE GROUPID= ? ", new Object[] { groupId }, String.class));
		securityDataBean.setAllUnitsList(getJdbcTemplate().queryForList(" SELECT UNIT FROM SUNITUSERGROUP WHERE GROUPID= ? ", new Object[] { groupId }, String.class));
		return securityDataBean;
	}

}
