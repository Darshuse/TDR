package com.eastnets.extraction.bean;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class StatisticsFile {

	private int numberOfGeneratedMessages;
	private int numberOfSwiftMessages;
	private int numberOfMXMessages;
	private int numberOfFileMessages;
	private int numberOfXMLMessages;
	private int numberOfEmptyMXMessages;

	private int numberOfGeneratedRJE;
	private int numberOfGeneratedMX;
	private int numberOfGeneratedXMLMessages;
	private int numberOfGeneratedExitPoint;
	private int numberofGeneratedFile;
	private int numberOfMessagesThatHaveErrors;
	private Set<String> generatedFileNames = new HashSet<String>();

	private String startingTime;
	private String finishingTime;

	private SearchParam searchParam;

	public int getNumberOfGeneratedMessages() {
		return numberOfGeneratedMessages;
	}

	public void setNumberOfGeneratedMessages(int numberOfGeneratedMessages) {
		this.numberOfGeneratedMessages = numberOfGeneratedMessages;
	}

	public int getNumberOfSwiftMessages() {
		return numberOfSwiftMessages;
	}

	public void setNumberOfSwiftMessages(int numberOfSwiftMessages) {
		this.numberOfSwiftMessages = numberOfSwiftMessages;
	}

	public int getNumberOfMXMessages() {
		return numberOfMXMessages;
	}

	public void setNumberOfMXMessages(int numberOfMXMessages) {
		this.numberOfMXMessages = numberOfMXMessages;
	}

	public int getNumberOfFileMessages() {
		return numberOfFileMessages;
	}

	public void setNumberOfFileMessages(int numberOfFileMessages) {
		this.numberOfFileMessages = numberOfFileMessages;
	}

	public SearchParam getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(SearchParam searchParam) {
		this.searchParam = searchParam;
	}

	public int getNumberOfGeneratedRJE() {
		return numberOfGeneratedRJE;
	}

	public void setNumberOfGeneratedRJE(int numberOfGeneratedRJE) {
		this.numberOfGeneratedRJE = numberOfGeneratedRJE;
	}

	public int getNumberOfGeneratedMX() {
		return numberOfGeneratedMX;
	}

	public void setNumberOfGeneratedMX(int numberOfGeneratedMX) {
		this.numberOfGeneratedMX = numberOfGeneratedMX;
	}

	public int getNumberOfGeneratedExitPoint() {
		return numberOfGeneratedExitPoint;
	}

	public void setNumberOfGeneratedExitPoint(int numberOfGeneratedExitPoint) {
		this.numberOfGeneratedExitPoint = numberOfGeneratedExitPoint;
	}

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getFinishingTime() {
		return finishingTime;
	}

	public void setFinishingTime(String finishingTime) {
		this.finishingTime = finishingTime;
	}

	public Set<String> getGeneratedFileNames() {
		return generatedFileNames;
	}

	public void setGeneratedFileNames(Set<String> generatedFileNames) {
		this.generatedFileNames = generatedFileNames;
	}

	public void addGeneratedFileName(String generatedFileName) {
		generatedFileNames.add(generatedFileName);
	}

	public int getNumberOfEmptyMXMessages() {
		return numberOfEmptyMXMessages;
	}

	public void setNumberOfEmptyMXMessages(int numberOfEmptyMXMessages) {
		this.numberOfEmptyMXMessages = numberOfEmptyMXMessages;
	}

	public int getNumberOfXMLMessages() {
		return numberOfXMLMessages;
	}

	public void setNumberOfXMLMessages(int numberOfXMLMessages) {
		this.numberOfXMLMessages = numberOfXMLMessages;
	}

	public int getNumberOfGeneratedXMLMessages() {
		return numberOfGeneratedXMLMessages;
	}

	public void setNumberOfGeneratedXMLMessages(int numberOfGeneratedXMLMessages) {
		this.numberOfGeneratedXMLMessages = numberOfGeneratedXMLMessages;
	}

	public int getNumberofGeneratedFile() {
		return numberofGeneratedFile;
	}

	public void setNumberofGeneratedFile(int numberofGeneratedFile) {
		this.numberofGeneratedFile = numberofGeneratedFile;
	}

	public int getNumberOfMessagesThatHaveErrors() {
		return numberOfMessagesThatHaveErrors;
	}

	public void setNumberOfMessagesThatHaveErrors(int numberOfMessagesThatHaveErrors) {
		this.numberOfMessagesThatHaveErrors = numberOfMessagesThatHaveErrors;
	}

}
