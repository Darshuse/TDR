package com.eastnets.dao.security.data;

import java.util.List;

/**
 * 
 * @author AHammad
 *
 */
public class SecurityDataBean {

	private Long groupId;

	private List<String> allLicenseBics;

	private List<String> allCategoriesList;

	private List<String> allUnitsList;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<String> getAllLicenseBics() {
		return allLicenseBics;
	}

	public void setAllLicenseBics(List<String> allLicenseBics) {
		this.allLicenseBics = allLicenseBics;
	}

	public List<String> getAllCategoriesList() {
		return allCategoriesList;
	}

	public void setAllCategoriesList(List<String> allCategoriesList) {
		this.allCategoriesList = allCategoriesList;
	}

	public List<String> getAllUnitsList() {
		return allUnitsList;
	}

	public void setAllUnitsList(List<String> allUnitsList) {
		this.allUnitsList = allUnitsList;
	}

}
