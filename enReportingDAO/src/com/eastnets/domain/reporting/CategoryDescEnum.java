package com.eastnets.domain.reporting;

public enum CategoryDescEnum {

	Audit_Reports("Providing a range of audit SWIFT traffic reports that the user can use for different purposes"), Custom_Reports("en.TDR provides custom reports, for reporting needs that are not covered with the standard reports"), Events_Reports(
			"Provide all information about events that occurs on SWIFT Alliance Access, it can be shown based on different criteria"), File_Act(
					"The purpose of this report is to give an idea about the exchanged FileAct traffic per correspondent bank."), MT_Reports("Providing all sent and received SWIFT messages in the specified period by type"), MX_Reports(
							"Providing all sent and received MX messages for the period specified"), OFAC_Reports("Provide detailed lists of the violations of OFAC and actions of the OFAC violations"), Stats_Reports(
									"Providing statistics for MT messages and shows all type of messages grouped by BIC."), Traffic_Reports(
											"Different reports are available including : Appli Monitoring & Average Text Length ..etc"), Ultimate_Beneficiary_Originator(
													"The purpose of this report is to show info of the ordering customer and the beneficiary"), Watchdog_Reports(
															"The purpose of this report is to provide information about all alerts generated through the Watchdog module.");

	private CategoryDescEnum(String desc) {
		this.setDesc(desc);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String desc;

}
