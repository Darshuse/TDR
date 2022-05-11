package com.eastnets.resilience.filetransfer;

public class FileTransferClientFactory {
	public static FileTransferClient getFileTransferClient(String clientType)
			throws Exception {
		return (FileTransferClient) Class.forName(
				"com.eastnets.resilience.filetransfer.clients." + clientType)
				.newInstance();
	}
}
