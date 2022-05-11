package com.eastnets.service.gpi;

public enum EndPointStatusDesc {

	RECEIVEDANDSENT("Received and sent"), RECEIVEDBUTNOTYSETSENT("Received but not yet sent"), NOTYETRECEIVEDORSENT(
			"Not yet Received Or Sent"), REJECTED("Rejected"), ARRIVEDATNONTRACEABLEBANK(
					"Arrived at non traceable bank"), RECEIVEDBUTNOTYETCREDITED("Received but not yet credited");

	EndPointStatusDesc(String desc) {
		this.desc = desc;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
