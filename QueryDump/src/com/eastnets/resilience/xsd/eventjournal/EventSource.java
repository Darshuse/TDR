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
package com.eastnets.resilience.xsd.eventjournal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventSource", propOrder = { "location", "archive" })
public class EventSource {

	@XmlElement(name = "Location", required = true)
	protected EventLocation location;

	@XmlElement(name = "Archive")
	protected List<ArchiveTableIdentifier> archive;

	public EventLocation getLocation() {
		return this.location;
	}

	public void setLocation(EventLocation paramEventLocation) {
		this.location = paramEventLocation;
	}

	public List<ArchiveTableIdentifier> getArchive() {
		if (this.archive == null)
			this.archive = new ArrayList<ArchiveTableIdentifier>();
		return this.archive;
	}
}
