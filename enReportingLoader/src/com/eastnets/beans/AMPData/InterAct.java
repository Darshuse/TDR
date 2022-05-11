package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class InterAct {

	@XmlElement(name = "SwiftNet")
	SwiftNet SwiftNetObject;
	@XmlElement(name = "SwiftNetRoute")
	SwiftNetRoute SwiftNetRouteObject;
	@XmlElement(name = "Parameters")
	Parameters ParametersObject;

	// Getter Methods

	public SwiftNet getSwiftNet() {
		return SwiftNetObject;
	}

	public SwiftNetRoute getSwiftNetRoute() {
		return SwiftNetRouteObject;
	}

	public Parameters getParameters() {
		return ParametersObject;
	}

	// Setter Methods

	public void setSwiftNet(SwiftNet SwiftNetObject) {
		this.SwiftNetObject = SwiftNetObject;
	}

	public void setSwiftNetRoute(SwiftNetRoute SwiftNetRouteObject) {
		this.SwiftNetRouteObject = SwiftNetRouteObject;
	}

	public void setParameters(Parameters ParametersObject) {
		this.ParametersObject = ParametersObject;
	}
}