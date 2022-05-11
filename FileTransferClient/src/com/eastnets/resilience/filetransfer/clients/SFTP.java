package com.eastnets.resilience.filetransfer.clients;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.eastnets.resilience.filetransfer.FileTransferClient;
import com.eastnets.resilience.filetransfer.bean.TransferFile;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

public class SFTP implements FileTransferClient {

	private SshClient sshClient = new SshClient();
	private SftpClient sftpClient;
	private Logger log = Logger.getLogger(SFTP.class.getName());
	boolean interrupted = false;

	@Override
	public int changeLocalDirectory(String newDirectory) throws Exception {

		int status = 0;// fail

		sftpClient.lcd(newDirectory);
		log.info("Local Directory :" + sftpClient.lpwd());
		status = 1;// success

		return status;
	}

	@Override
	public int changeRemoteDirectory(String newDirectory) throws Exception {

		int status = 0;// fail

		sftpClient.cd(newDirectory);
		log.info("Remote Directory :" + sftpClient.pwd());
		status = 1;// success

		return status;
	}

	@Override
	public int connect(String server, int port) throws Exception {

		int status = 0;// fail

		SshConnectionProperties properties = new SshConnectionProperties();
		properties.setHost(server);

		if (port > 0)
			properties.setPort(port);

		if (!isConnected()) {
			sshClient.connect(properties, new IgnoreHostKeyVerification());
			status = 1;// success
		}
		interrupted = false;
		return status;
	}

	@Override
	public int disconnect() throws Exception {

		int status = 0;// fail

		if (isConnected()) {
			sshClient.disconnect();
			status = 1;// success
		}

		return status;
	}

	@Override
	public int getDefaultPort() throws Exception {
		return 22;
	}

	@Override
	public int getFiles(List<TransferFile> files) throws Exception {

		List<TransferFile> filesStatus = new ArrayList<TransferFile>();

		for (TransferFile currentFile : files) {
			
			int transfered= 1;
			try{
				sftpClient.get(currentFile.getFileName(), currentFile.getDestinationFileName());
			}catch( Exception e ){
				transfered= 0;
			}
			currentFile.setTransferStatus(transfered);
			
			filesStatus.add(currentFile);
		}

		files = filesStatus;

		if (files.size() == filesStatus.size())
			return 1;// success
		else
			return 0;// fail
	}

	@Override
	public void interrupt() {
		interrupted = true;
	}

	@Override
	public boolean isConnected() throws Exception {
		return sshClient.isConnected();
	}

	@Override
	public int login(String userName, String password) throws Exception {

		PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();

		pwd.setUsername(userName);
		pwd.setPassword(password);

		int result = sshClient.authenticate(pwd);

		if (result == AuthenticationProtocolState.COMPLETE) {
			sftpClient = sshClient.openSftpClient();
			return 1;// success
		}

		return 0;// fail
	}

	@Override
	public int logout() throws Exception {

		int status = 0;// fail

		if (!sftpClient.isClosed()) {
			sftpClient.quit();
			status = 1;// success
		}

		return status;
	}

	@Override
	public int putFiles(List<TransferFile> files) throws Exception {

		List<TransferFile> filesStatus = new ArrayList<TransferFile>();
		
		for (TransferFile currentFile : files) {
			if (interrupted) {
				break;
			}
			
			int transfered= 1;
			try{
				sftpClient.put(currentFile.getFileName(), currentFile.getDestinationFileName());
			}catch( Exception e ){
				transfered= 0;
			}
			currentFile.setTransferStatus(transfered);
			filesStatus.add(currentFile);
		}

		files = filesStatus;

		if (files.size() == filesStatus.size())
			return 1;// success
		else
			return 0;// fail
	}

}
