package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AMP")
@XmlAccessorType(XmlAccessType.FIELD)
public class AMP {
	@XmlElement(name = "Status")
	Status StatusObject;
	@XmlElement(name = "Header")
	Header HeaderObject;
	@XmlElement(name = "ProtocolParameters")
	ProtocolParameters ProtocolParametersObject;
	@XmlElement(name = "ChannelParameters")
	ChannelParameters ChannelParametersObject;
	@XmlElement(name = "ExtractedFields")
	private String ExtractedFields;
	@XmlElement(name = "BulkInfo")
	BulkInfo BulkInfoObject;
	@XmlElement(name = "FlexFields")
	private String FlexFields;
	@XmlElement(name = "Data")
	Data DataObject;

	// Getter Methods

	public Status getStatus() {
		return StatusObject;
	}

	public Header getHeader() {
		return HeaderObject;
	}

	public ProtocolParameters getProtocolParameters() {
		return ProtocolParametersObject;
	}

	public ChannelParameters getChannelParameters() {
		return ChannelParametersObject;
	}

	public String getExtractedFields() {
		return ExtractedFields;
	}

	public BulkInfo getBulkInfo() {
		return BulkInfoObject;
	}

	public String getFlexFields() {
		return FlexFields;
	}

	public Data getData() {
		return DataObject;
	}

	// Setter Methods

	public void setStatus(Status StatusObject) {
		this.StatusObject = StatusObject;
	}

	public void setHeader(Header HeaderObject) {
		this.HeaderObject = HeaderObject;
	}

	public void setProtocolParameters(ProtocolParameters ProtocolParametersObject) {
		this.ProtocolParametersObject = ProtocolParametersObject;
	}

	public void setChannelParameters(ChannelParameters ChannelParametersObject) {
		this.ChannelParametersObject = ChannelParametersObject;
	}

	public void setExtractedFields(String ExtractedFields) {
		this.ExtractedFields = ExtractedFields;
	}

	public void setBulkInfo(BulkInfo BulkInfoObject) {
		this.BulkInfoObject = BulkInfoObject;
	}

	public void setFlexFields(String FlexFields) {
		this.FlexFields = FlexFields;
	}

	public void setData(Data DataObject) {
		this.DataObject = DataObject;
	}

}