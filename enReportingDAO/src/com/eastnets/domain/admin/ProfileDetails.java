package com.eastnets.domain.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

public class ProfileDetails implements Serializable, Comparable<ProfileDetails>, Diffable<ProfileDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8110024706150779290L;
	private Profile profile;
	private List<String> authorizedReportingActions;
	private List<String> authorizedViewerActions;
	private List<String> authorizedWatchdogActions;
	private List<String> authorizedDashboardActions;
	private List<String> authorizedBusinessIntelligenceActions;
	private List<String> authorizedBICCodes;
	private List<String> authorizedUnits;
	private List<String> authorizedMessageCategories;

	// Jsaper Data source only
	private List profileRoles;

	public ProfileDetails(Profile profile) {

		authorizedReportingActions = new ArrayList<String>();
		authorizedViewerActions = new ArrayList<String>();
		authorizedBusinessIntelligenceActions = new ArrayList<String>();
		authorizedWatchdogActions = new ArrayList<String>();
		authorizedBICCodes = new ArrayList<String>();
		authorizedUnits = new ArrayList<String>();
		authorizedMessageCategories = new ArrayList<String>();
		authorizedDashboardActions = new ArrayList<String>();
		this.profile = profile;

		ArrayList<String> usedUnits = new ArrayList<String>();
		usedUnits.add("None");
		// this.setAuthorizedUnits(usedUnits);
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<String> getAuthorizedBICCodes() {
		return authorizedBICCodes;
	}

	public void setAuthorizedBICCodes(List<String> authorizedBICCodes) {
		this.authorizedBICCodes.clear();
		if (authorizedBICCodes == null || authorizedBICCodes.isEmpty()) {
			return;
		}
		this.authorizedBICCodes.addAll(authorizedBICCodes);
		Collections.sort(this.authorizedBICCodes);
	}

	public List<String> getAuthorizedUnits() {
		return authorizedUnits;
	}

	public void setAuthorizedUnits(List<String> authorizedUnits) {
		this.authorizedUnits.clear();
		if (authorizedUnits == null || authorizedUnits.isEmpty()) {
			return;
		}
		this.authorizedUnits.addAll(authorizedUnits);
		Collections.sort(this.authorizedUnits);
	}

	public List<String> getAuthorizedReportingActions() {
		return authorizedReportingActions;
	}

	public void setAuthorizedReportingActions(List<String> authorizedReportingActions) {
		this.authorizedReportingActions.clear();
		if (authorizedReportingActions == null || authorizedReportingActions.isEmpty()) {
			return;
		}

		this.authorizedReportingActions.addAll(authorizedReportingActions);
		Collections.sort(this.authorizedReportingActions);
	}

	public List<String> getAuthorizedBusinessIntegellenceActions() {
		return authorizedBusinessIntelligenceActions;
	}

	public void setAuthorizedBusinessIntegellenceActions(List<String> authorizedBusinessIntegellenceActions) {
		this.authorizedBusinessIntelligenceActions.clear();
		if (authorizedBusinessIntegellenceActions == null || authorizedBusinessIntegellenceActions.isEmpty()) {
			return;
		}

		this.authorizedBusinessIntelligenceActions.addAll(authorizedBusinessIntegellenceActions);
		Collections.sort(this.authorizedBusinessIntelligenceActions);
	}

	public List<String> getAuthorizedViewerActions() {
		return authorizedViewerActions;
	}

	public void setAuthorizedViewerActions(List<String> authorizedViewerActions) {
		this.authorizedViewerActions.clear();
		if (authorizedViewerActions == null || authorizedViewerActions.isEmpty()) {
			return;
		}
		this.authorizedViewerActions.addAll(authorizedViewerActions);
		Collections.sort(this.authorizedViewerActions);
	}

	public List<String> getAuthorizedWatchdogActions() {
		return authorizedWatchdogActions;
	}

	public void setAuthorizedWatchdogActions(List<String> authorizedWatchdogActions) {
		this.authorizedWatchdogActions.clear();
		if (authorizedWatchdogActions == null || authorizedWatchdogActions.isEmpty()) {
			return;
		}
		this.authorizedWatchdogActions.addAll(authorizedWatchdogActions);
		Collections.sort(this.authorizedWatchdogActions);
	}

	public List<String> getAuthorizedMessageCategories() {
		return authorizedMessageCategories;
	}

	public void setAuthorizedMessageCategories(List<String> authorizedMessageCategories) {
		this.authorizedMessageCategories.clear();
		if (authorizedMessageCategories == null || authorizedMessageCategories.isEmpty()) {
			return;
		}
		this.authorizedMessageCategories.addAll(authorizedMessageCategories);
		Collections.sort(this.authorizedMessageCategories);
	}

	@Override
	public int compareTo(ProfileDetails o) {

		if (o == null) {
			return 1;
		}

		if (this.profile != o.profile) {
			if (this.profile != null && !this.profile.equals(o.profile)) {
				return -1;
			} else if (o.profile != null && !o.profile.equals(this.profile)) {
				return -1;
			}
		}

		if (this.authorizedBICCodes != o.authorizedBICCodes) {
			if (this.authorizedBICCodes != null && !this.authorizedBICCodes.equals(o.authorizedBICCodes)) {
				return -1;
			} else if (o.authorizedBICCodes != null && !o.authorizedBICCodes.equals(this.authorizedBICCodes)) {
				return -1;
			}
		}
		if (this.authorizedMessageCategories != o.authorizedMessageCategories) {
			if (this.authorizedMessageCategories != null && !this.authorizedMessageCategories.equals(o.authorizedMessageCategories)) {
				return -1;
			} else if (o.authorizedMessageCategories != null && !o.authorizedMessageCategories.equals(this.authorizedMessageCategories)) {
				return -1;
			}
		}
		if (this.authorizedReportingActions != o.authorizedReportingActions) {
			if (this.authorizedReportingActions != null && !this.authorizedReportingActions.equals(o.authorizedReportingActions)) {
				return -1;
			} else if (o.authorizedReportingActions != null && !o.authorizedReportingActions.equals(this.authorizedReportingActions)) {
				return -1;
			}
		}
		if (this.authorizedUnits != o.authorizedUnits) {
			if (this.authorizedUnits != null && !this.authorizedUnits.equals(o.authorizedUnits)) {
				return -1;
			} else if (o.authorizedUnits != null && !o.authorizedUnits.equals(this.authorizedUnits)) {
				return -1;
			}
		}
		if (this.authorizedViewerActions != o.authorizedViewerActions) {
			if (this.authorizedViewerActions != null && !this.authorizedViewerActions.equals(o.authorizedViewerActions)) {
				return -1;
			} else if (o.authorizedViewerActions != null && !o.authorizedViewerActions.equals(this.authorizedViewerActions)) {
				return -1;
			}
		}
		if (this.authorizedWatchdogActions != o.authorizedWatchdogActions) {
			if (this.authorizedWatchdogActions != null && !this.authorizedWatchdogActions.equals(o.authorizedWatchdogActions)) {
				return -1;
			} else if (o.authorizedWatchdogActions != null && !o.authorizedWatchdogActions.equals(this.authorizedWatchdogActions)) {
				return -1;
			}
		}
		if (this.authorizedDashboardActions != o.authorizedDashboardActions) {
			if (this.authorizedDashboardActions != null && !this.authorizedDashboardActions.equals(o.authorizedDashboardActions)) {
				return -1;
			} else if (o.authorizedDashboardActions != null && !o.authorizedDashboardActions.equals(this.authorizedDashboardActions)) {
				return -1;
			}
		}

		if (this.authorizedBusinessIntelligenceActions != o.authorizedBusinessIntelligenceActions) {
			if (this.authorizedBusinessIntelligenceActions != null && !this.authorizedBusinessIntelligenceActions.equals(o.authorizedBusinessIntelligenceActions)) {
				return -1;
			} else if (o.authorizedBusinessIntelligenceActions != null && !o.authorizedBusinessIntelligenceActions.equals(this.authorizedBusinessIntelligenceActions)) {
				return -1;
			}
		}

		return 0;
	}

	public List<String> getAuthorizedDashboardActions() {
		return authorizedDashboardActions;
	}

	public void setAuthorizedDashboardActions(List<String> authorizedDashboardActions) {
		this.authorizedDashboardActions = authorizedDashboardActions;
	}

	public String getName() {
		return profile.getName();
	}

	public void setName(String name) {
		profile.setName(name);
	}

	public String getDescription() {
		return profile.getDescription();
	}

	public void setDescription(String description) {
		profile.setDescription(description);
	}

	public ApprovalStatus getApprovalStatus() {
		return profile.getApprovalStatus();
	}

	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		profile.setApprovalStatus(approvalStatus);
	}

	/**
	 * for jasper data source only
	 */
	public List getProfileRoles() {
		if (profile != null) {
			profileRoles = profile.getProfileRoles();
			return profileRoles;
		} else {
			return null;
		}

	}

	@Override
	public DiffResult diff(ProfileDetails oldProfileDetails) {

		DiffBuilder compare = new DiffBuilder(this, oldProfileDetails, null);
		compare.append("Profile Name", this.profile.getName(), oldProfileDetails.getProfile().getName());
		compare.append("Profile Description", this.profile.getDescription(), oldProfileDetails.getProfile().getDescription());
		compare.append("Profile Directory", this.profile.getRpDirectory(), StringUtils.defaultString(oldProfileDetails.getProfile().getRpDirectory()));

		compare.append("Authorized Dashboard Action", this.authorizedDashboardActions, oldProfileDetails.authorizedDashboardActions);

		compare.append("Authorized Report Action", this.authorizedReportingActions, oldProfileDetails.authorizedReportingActions);

		compare.append("Authorized Viewer Action", this.authorizedViewerActions, oldProfileDetails.authorizedViewerActions);

		compare.append("Authorized Watchdog Action", this.authorizedWatchdogActions, oldProfileDetails.authorizedWatchdogActions);

		compare.append("Authorized Roles", this.profile.getProfileRoles(), oldProfileDetails.getProfile().getProfileRoles());

		compare.append("Authorized Bic Code", this.authorizedBICCodes, oldProfileDetails.authorizedBICCodes);

		compare.append("Authorized Unit", this.authorizedUnits, oldProfileDetails.authorizedUnits);

		compare.append("Authorized Categores", this.authorizedMessageCategories, oldProfileDetails.authorizedMessageCategories);

		return compare.build();

	}

}
