package com.eastnets.domain.reporting;

import java.util.List;

import com.eastnets.domain.BaseEntity;

public class ReportSet extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4134793145519199162L;
	private Long id;
	private Long reportId;
	private String name;
	private String modifiedName;
	List<Long> assignedProfiles;
	List<String> assignedProfilesNames;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	/**
	 * 
	 * @return the name with out a any double couts to view it on the UI, handle it with any module
	 */
	public String getName() {
		if (name != null) {
			if (name.contains("''")) {
				name = name.replace("''", "'");
			}
		}
		return name;
	}

	public void setName(String name) {
		// this is to support the UI insertion of a single coute (')
		if (name.contains("'")) {
			modifiedName = name.replace("'", "''");
			this.name = name;
		}
		this.name = name;
	}

	/**
	 * 
	 * @return the name with the modification to support for ex: an insert statment to the DB
	 */
	public String getModifiedName() {
		return modifiedName;
	}

	public List<Long> getAssignedProfiles() {
		return assignedProfiles;
	}

	public void setAssignedProfiles(List<Long> assignedProfiles) {
		this.assignedProfiles = assignedProfiles;
	}

	public List<String> getAssignedProfilesNames() {
		return assignedProfilesNames;
	}

	public void setAssignedProfilesNames(List<String> assignedProfilesNames) {
		this.assignedProfilesNames = assignedProfilesNames;
	}

}
