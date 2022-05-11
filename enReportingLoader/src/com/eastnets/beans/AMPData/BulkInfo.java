package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class BulkInfo {
	private String BulkType;
	private String TotalNumberOfMessages;
	private String TotalFileSize;
	private String SequenceNumber;
	private String bulkedFileSize;

	// Getter Methods

	public String getBulkType() {
		return BulkType;
	}

	public String getTotalNumberOfMessages() {
		return TotalNumberOfMessages;
	}

	public String getTotalFileSize() {
		return TotalFileSize;
	}

	public String getSequenceNumber() {
		return SequenceNumber;
	}

	public String getBulkedFileSize() {
		return bulkedFileSize;
	}

	// Setter Methods

	public void setBulkType(String BulkType) {
		this.BulkType = BulkType;
	}

	public void setTotalNumberOfMessages(String TotalNumberOfMessages) {
		this.TotalNumberOfMessages = TotalNumberOfMessages;
	}

	public void setTotalFileSize(String TotalFileSize) {
		this.TotalFileSize = TotalFileSize;
	}

	public void setSequenceNumber(String SequenceNumber) {
		this.SequenceNumber = SequenceNumber;
	}

	public void setBulkedFileSize(String bulkedFileSize) {
		this.bulkedFileSize = bulkedFileSize;
	}
}