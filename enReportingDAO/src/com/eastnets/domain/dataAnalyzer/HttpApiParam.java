package com.eastnets.domain.dataAnalyzer;

public enum HttpApiParam {
	FLOW_ID("_flowId"),
	THEME("theme"),
	DECORATE("decorate"),
	USERNAME("j_username"),
	PASSWORD("j_password"),
	REPORT_UNIT("reportUnit"),
	FROM_DESIGNER("fromDesigner"),
	VIEW_REPORT("viewReport"),
	OUTPUT("output"),
	VIEW_DASHBOARD_AS_FRAME("viewAsDashboardFrame"),
	EVENTID("_eventId"),
	REPORT_TYPE("reportType"),
	REALM("realm"),
	REPORT_UNITE_URL("reportUnitURI"),
	RESOURCE_TYPE("resourceType");

	private final String HttpApiParam;

	private HttpApiParam(String HttpApiParam) {
		this.HttpApiParam = HttpApiParam;
	}

	public String getHttpApiParam() {
		return HttpApiParam;
	}



}
