package com.eastnets.beans.AMPData;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class StructuredHistory {
	@XmlElement(name = "HistoryLine")
	private List<HistoryLine> HistoryLine = new ArrayList<HistoryLine>();

	public List<HistoryLine> getHistoryLine() {
		return HistoryLine;
	}

	public void setHistoryLine(List<HistoryLine> historyLine) {
		HistoryLine = historyLine;
	}

}
