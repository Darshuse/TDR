package com.eastnets.beans.AMPData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
	private String Size;
	private String Digest;
	private String DigestAlgorithm;
	@XmlElement(name = "Storage")
	Storage StorageObject;

	// Getter Methods

	public String getSize() {
		return Size;
	}

	public String getDigest() {
		return Digest;
	}

	public String getDigestAlgorithm() {
		return DigestAlgorithm;
	}

	public Storage getStorage() {
		return StorageObject;
	}

	// Setter Methods

	public void setSize(String Size) {
		this.Size = Size;
	}

	public void setDigest(String Digest) {
		this.Digest = Digest;
	}

	public void setDigestAlgorithm(String DigestAlgorithm) {
		this.DigestAlgorithm = DigestAlgorithm;
	}

	public void setStorage(Storage StorageObject) {
		this.StorageObject = StorageObject;
	}
}