package com.eastnets.domain.filepayload;

import java.io.Serializable;
import java.util.Set;

public class FilePayloadGlobalSettings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5218828741402934079L;
	private int maxTryCount;
	private int delayMinutes;
	private int chunkCount;
	private String payloadTransferePath;
	
	private boolean extractZip= false;
	private boolean verbose= false;
	
	
	
	public boolean isExtractZip() {
		return extractZip;
	}
	public void setExtractZip(boolean extractZip) {
		this.extractZip = extractZip;
	}
	/**
	 * set of extensions that should be treated as text 
	 */
	private Set<String> extensionSet;
	
	public int getMaxTryCount() {
		return maxTryCount;
	}
	public void setMaxTryCount(int maxTryCount) {
		this.maxTryCount = maxTryCount;
	}
	public int getDelayMinutes() {
		return delayMinutes;
	}
	public void setDelayMinutes(int delayMinutes) {
		this.delayMinutes = delayMinutes;
	}
	public int getChunkCount() {
		return chunkCount;
	}
	public void setChunkCount(int chunkCount) {
		this.chunkCount = chunkCount;
	}
	public String getPayloadTransferePath() {
		return payloadTransferePath;
	}
	public void setPayloadTransferePath(String payloadTransferePath) {
		this.payloadTransferePath = payloadTransferePath;
	}
	public Set<String> getExtensionSet() {
		return extensionSet;
	}
	public void setExtensionSet(Set<String> extensionSet) {
		this.extensionSet = extensionSet;
	}
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
