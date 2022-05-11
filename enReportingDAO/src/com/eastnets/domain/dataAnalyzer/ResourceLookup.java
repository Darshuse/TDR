package com.eastnets.domain.dataAnalyzer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "resourceLookup")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceLookup {

	@XmlElement(name = "creationDate")
	private String creationDate;
	@XmlElement(name = "description")
	private String description;
	@XmlElement(name = "label")
	private String label;
	@XmlElement(name = "permissionMask")
	private String permissionMask;
	@XmlElement(name = "updateDate")
	private String updateDate;
	@XmlElement(name = "uri")
	private String uri;
	@XmlElement(name = "version")
	private String version;
	@XmlElement(name = "resourceType")
	private String resourceType;

	private String trimmedLabel;

	public String getCreationDate() {
		return creationDate;
	}

	public ResourceLookup() {

	}

	public ResourceLookup(String label, String resourceType) {
		super();
		this.label = label;
		this.resourceType = resourceType;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPermissionMask() {
		return permissionMask;
	}

	public void setPermissionMask(String permissionMask) {
		this.permissionMask = permissionMask;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	
	@Override
	public String toString() { 
		return label;
	}
	
	public String getTrimmedLabel() {
		if (resourceType.equalsIgnoreCase("adhocDataView") || resourceType.equalsIgnoreCase("reportUnit")) {
			if (label.length() <= 20)
				return label;
			else {
				return label.substring(0, 21) + "...";
			}
		} else if (resourceType.equalsIgnoreCase("dashboard")) {
			if (label.length() <= 15)
				return label;
			else {
				return label.substring(0, 16) + "...";
			}
		} else {
			return label;
		}

	}

	public void setTrimmedLabel(String trimmedLabel) {
		this.trimmedLabel = trimmedLabel;
	}

}