package com.eastnets.resilience.filetransfer;

import java.util.List;

import com.eastnets.resilience.filetransfer.bean.TransferFile;

public interface FileTransferClient {

	int changeLocalDirectory(String newDirectory) throws Exception;

	int changeRemoteDirectory(String newDirectory) throws Exception;

	int connect(String server, int port) throws Exception;

	int disconnect() throws Exception;

	int getDefaultPort() throws Exception;

	int getFiles(List<TransferFile> files) throws Exception;

	void interrupt();

	boolean isConnected() throws Exception;

	int login(String userName, String password) throws Exception;

	int logout() throws Exception;

	int putFiles(List<TransferFile> files) throws Exception;

}
