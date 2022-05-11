package com.eastnets.resilience.filetransfer.bean;

public class TransferFile {

	private String fileName;
	private int transferStatus = 0;

	private String destinationFileName;//the destination filename 

	public TransferFile(String fileName,String destinationFileName, int transferStatus) {
		this.fileName = fileName;
		this.destinationFileName= destinationFileName;
		this.transferStatus = transferStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public int getTransferStatus() {
		return transferStatus;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setTransferStatus(int transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getDestinationFileName() {
		return destinationFileName;
	}

	public void setDestinationFileName(String destinationFileName) {
		this.destinationFileName = destinationFileName;
	}

}
