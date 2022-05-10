/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.xsd.messaging;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.eastnets.resilience.xmldump.utils.StringUtils;
import com.eastnets.resilience.xsd.BaseObject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Text", propOrder = { "identifier", "dataBlock", "swiftBlock3", "swiftBlock5", "swiftBlockU" })
public class Text extends BaseObject {

	@XmlElement(name = "Identifier")
	protected TextIdentifier identifier;

	@XmlElement(name = "DataBlock")
	protected String dataBlock;

	@XmlElement(name = "SwiftBlock3")
	protected String swiftBlock3;

	@XmlElement(name = "SwiftBlock5")
	protected String swiftBlock5;

	@XmlElement(name = "SwiftBlockU")
	protected String swiftBlockU;

	public TextIdentifier getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(TextIdentifier paramTextIdentifier) {
		this.identifier = paramTextIdentifier;
	}

	public String getDataBlock() {
		return this.dataBlock;
	}

	public void setDataBlock(String paramString) {
		this.dataBlock = paramString;
	}

	public String getSwiftBlock3() {
		return this.swiftBlock3;
	}

	public void setSwiftBlock3(String paramString) {
		this.swiftBlock3 = paramString;
	}

	public String getSwiftBlock5() {
		return this.swiftBlock5;
	}

	public void setSwiftBlock5(String paramString) {
		this.swiftBlock5 = paramString;
	}

	public String getSwiftBlockU() {
		return this.swiftBlockU;
	}

	public void setSwiftBlockU(String paramString) {
		this.swiftBlockU = paramString;
	}

	@Override
	public int getUmidL() {
		return StringUtils.getUmidl(this.getIdentifier().getSUmid());
	}

	@Override
	public int getUmidH() {
		return StringUtils.getUmidh(this.getIdentifier().getSUmid());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UmidL " + this.getUmidL() + "\n");
		builder.append("UmidH " + this.getUmidH() + "\n");
		builder.append("Block length " + (this.getDataBlock() == null ? 0 : this.getDataBlock().length()) + "\n");
		builder.append("Block3  " + this.getSwiftBlock3() + "\n");
		builder.append("Block5  " + this.getSwiftBlock5() + "\n");
		return builder.toString();
	}
}
