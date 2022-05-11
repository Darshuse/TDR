package com.eastnets.resilience.filetransfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.eastnets.resilience.filetransfer.bean.TransferFile;

public class ProcessTestRunner {

	private static final String LOG4J_PROPERTY_FILE = "log4j.properties";
	public static void main(String args[]) {

		PropertyConfigurator.configure(LOG4J_PROPERTY_FILE);
		ProcessTestRunner obj = new ProcessTestRunner();
		try {
			FileTransferClient client = FileTransferClientFactory
					.getFileTransferClient("SFTP");

			client.connect("localhost", 22);
			obj.log.info("Is Connected:" + client.isConnected());

			client.login("max", "edis");
			obj.log.info("Default Port:" + client.getDefaultPort());

			client.changeRemoteDirectory("lib32");
			client.changeLocalDirectory("D:\\tfs");

			List<TransferFile> files = new ArrayList<TransferFile>();
			TransferFile tf = new TransferFile("xx1.txt", "xx1.txt", 0);
			files.add(tf);

			client.putFiles(files);
			client.disconnect();

		} catch (Exception ex) {
			obj.log.info(ex.getMessage());
		}
	}

	private Logger log = Logger.getLogger(ProcessTestRunner.class.getName());

}
