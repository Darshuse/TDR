package com.eastnets.domain.dataAnalyzer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="resources")
@XmlAccessorType(XmlAccessType.FIELD)
public class Resources {

	@XmlElement(name="resourceLookup")
	private List<ResourceLookup> resourceLookup;

	public List<ResourceLookup> getResourceLookup() {
		return resourceLookup;
	}

	public void setResourceLookup(List<ResourceLookup> resourceLookup) {
		this.resourceLookup = resourceLookup;
	}

}
