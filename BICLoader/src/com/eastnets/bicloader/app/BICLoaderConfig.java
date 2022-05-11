package com.eastnets.bicloader.app;

import com.eastnets.config.ConfigBean;
import java.util.List;

public class BICLoaderConfig extends ConfigBean {
	private static final long serialVersionUID = 1L;
	private boolean debug;
	private boolean bUseTextBicFile;
	private String directory;
	private String file;
	private List<String> files;
	private boolean bUseDatBicFile;
	private boolean bUseXmlBicFile;
	private boolean gpiCorr;
	private boolean bankFile;
	private boolean bicdue;
	private boolean bankdirectory;
	private boolean fullFile;
	private boolean deltaFile;
	private boolean deleteOld;
	private boolean autoFiles;

	public BICLoaderConfig() {
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean isDeleteOld() {
		return deleteOld;
	}

	public void setDeleteOld(boolean deleteOld) {
		this.deleteOld = deleteOld;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isbUseTextBicFile() {
		return bUseTextBicFile;
	}

	public void setbUseTextBicFile(boolean bUseTextBicFile) {
		this.bUseTextBicFile = bUseTextBicFile;
	}

	public boolean isbUseDatBicFile() {
		return bUseDatBicFile;
	}

	public void setbUseDatBicFile(boolean bUseBatBicFile) {
		bUseDatBicFile = bUseBatBicFile;
	}

	public boolean isGpiCorr() {
		return gpiCorr;
	}

	public void setGpiCorr(boolean gpiCorr) {
		this.gpiCorr = gpiCorr;
	}

	public boolean isBankFile() {
		return bankFile;
	}

	public void setBankFile(boolean bankFile) {
		this.bankFile = bankFile;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isAutoFiles() {
		return autoFiles;
	}

	public void setAutoFiles(boolean autoFiles) {
		this.autoFiles = autoFiles;
	}

	public boolean isBicdue() {
		return bicdue;
	}

	public void setBicdue(boolean bicdue) {
		this.bicdue = bicdue;
	}

	public boolean isbUseXmlBicFile() {
		return bUseXmlBicFile;
	}

	public void setbUseXmlBicFile(boolean bUseXmlBicFile) {
		this.bUseXmlBicFile = bUseXmlBicFile;
	}

	public boolean isBankdirectory() {
		return bankdirectory;
	}

	public void setBankdirectory(boolean bankdirectory) {
		this.bankdirectory = bankdirectory;
	}

	public boolean isFullFile() {
		return fullFile;
	}

	public void setFullFile(boolean fullFile) {
		this.fullFile = fullFile;
	}

	public boolean isDeltaFile() {
		return deltaFile;
	}

	public void setDeltaFile(boolean deltaFile) {
		this.deltaFile = deltaFile;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
}