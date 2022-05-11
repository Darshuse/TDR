package com.eastnets.resilience.filepayloadcommunication.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.resilience.filepayloadcommunication.FilePayloadCommunicationImpl;
import com.eastnets.resilience.filepayloadcommunication.Globals;
import com.eastnets.resilience.filepayloadcommunication.commands.Command;
import com.eastnets.resilience.filepayloadcommunication.commands.HeartBeatCmd;
import com.eastnets.resilience.filepayloadcommunication.commands.PayloadTransferResult;
import com.eastnets.resilience.filepayloadcommunication.commands.PushReq;
import com.eastnets.resilience.filepayloadcommunication.commands.PushRes;
import com.eastnets.resilience.filepayloadcommunication.commands.RegisterPeerCmd;
import com.eastnets.resilience.filetransfer.FileTransferClient;
import com.eastnets.resilience.filetransfer.FileTransferClientFactory;
import com.eastnets.resilience.filetransfer.bean.TransferFile;

public class FilePayloadServiceServer extends FilePayloadCommunicationImpl {

	Socket clientSocket;
	Date lastActionTime = new Date();
	boolean isHeartBeatRequested;
	ServerCommandLineArgumentsParser commandLineArgumentsParser;

	public static boolean stopRequestProcessing = false;

	public static List<FileTransferClient> fileTransferClients = Collections.synchronizedList(new ArrayList<FileTransferClient>());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// write the header
		System.out.println(Globals.serverHeader);
		System.out.println(String.format("%0" + (Globals.serverHeader.length()) + "d", 0).replace("0", "-"));

		final FilePayloadServiceServer app = new FilePayloadServiceServer();

		// validate the arguments
		app.commandLineArgumentsParser = new ServerCommandLineArgumentsParser();
		if (!app.commandLineArgumentsParser.parseAndValidate(args)) {
			// display the application usage
			app.commandLineArgumentsParser.displayUsage();
			return;
		}
		app.commandLineArgumentsParser.displayArguments();

		// start the heartbeat thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (app.clientSocket != null && app.clientSocket.isConnected()) {
							if ((new Date().getTime() - app.lastActionTime.getTime()) >= app.commandLineArgumentsParser.getTimeout() * 1000) {
								if (app.isHeartBeatRequested) {
									System.out.println(getDateStr() + "Client not responding, resetting connection.");
									app.disconnectPeer();
								} else {
									HeartBeatCmd hbCmd = new HeartBeatCmd();
									app.send(hbCmd);
									app.isHeartBeatRequested = true;
								}
							}
						}
						Thread.sleep(app.commandLineArgumentsParser.getTimeout() * 1000);
					} catch (Exception e) {
						Throwable tr = e.getCause();
						if (tr == null) {
							tr = e;
						}
						System.out.println("Heartbeat Error: " + tr.getMessage());
						try {
							app.disconnectPeer();
						} catch (Exception ex) {
						}
					}
				}
			}
		}).start();

		while (true) {
			try {
				// start listening for the client
				ServerSocket serverSocket = new ServerSocket(app.commandLineArgumentsParser.getPort());

				System.out.println(getDateStr() + "Waiting client connection on port: " + app.commandLineArgumentsParser.getPort());

				app.clientSocket = serverSocket.accept(); // this will block
															// here until a
															// client is
															// connected

				serverSocket.close();// to prevent any other client to connect
										// to the server while this client is
										// connected

				app.lastActionTime = new Date();// reset the time of last action
												// received from client
				System.out.println(getDateStr() + String.format("Client(%s) connected.", app.clientSocket.getInetAddress().getHostAddress()));

				app.registerSocket(app.clientSocket);

				stopRequestProcessing = false;// reset the stop thread flag
				while (app.clientSocket != null && app.clientSocket.isConnected()) {
					String receivedStr = app.receive();
					app.handleCommand(receivedStr);
				}
			} catch (Exception e) {
				try {
					app.disconnectPeer();
				} catch (Exception ez) {
					app.clientSocket = null;
				}
				Throwable tr = e.getCause();
				if (tr == null) {
					tr = e;
				}
				System.out.println("Connection error: " + tr.getMessage());
				System.out.println("Resetting connection.");
			}
			app.killCurrentThreads();
		}
	}

	/**
	 * connect to the file transfer client
	 * 
	 * @param server
	 * @param port
	 * @return 0 if success
	 * @throws Exception
	 */
	int connectToFileTransferClient(FileTransferClient fileTransferClient, String server, int port) throws Exception {
		return fileTransferClient.connect(server, port);
	}

	@Override
	protected void disconnectPeer() throws IOException {
		/* try { */
		if (clientSocket != null) {
			clientSocket.shutdownInput();
			clientSocket.shutdownOutput();
			clientSocket.close();
		}
		/*
		 * } catch (IOException e) { System.out.println(getDateStr() + "Error: " + e.getMessage()); }
		 */
		clientSocket = null;
	}

	@Override
	protected void interrupt() {
		FilePayloadServiceServer.stopRequestProcessing = true;
		for (FileTransferClient fileTransferClient : FilePayloadServiceServer.fileTransferClients) {
			fileTransferClient.interrupt();// interrupt the file transfer
											// operation
		}
		FilePayloadServiceServer.fileTransferClients.clear();
	}

	/**
	 * login to the file transfer client
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	int loginToFileTransferClient(FileTransferClient fileTransferClient, String userName, String password) throws Exception {
		return fileTransferClient.login(userName, password);
	}

	@Override
	protected void processCommand(Command command) throws Exception {
		lastActionTime = new Date();// reset the time of last action received
									// from client
		isHeartBeatRequested = false;
		if (command == null) {
			return;
		}

		switch (command.getType()) {
		case COMMAND_REGISTER_PEER:

			RegisterPeerCmd registerClientCommand = (RegisterPeerCmd) command;
			RegisterPeerCmd respond = new RegisterPeerCmd(Globals.protocolVersion);
			// send the respond then check values
			send(respond);

			if (registerClientCommand.getProtocolVersion() != Globals.protocolVersion) {
				if (registerClientCommand.getProtocolVersion() > Globals.protocolVersion) {
					System.out
							.println(getDateStr()
									+ "Client is using a newer communication protocol, please contact Eastnets® to get the latest version of the Payload transfer server.");
				} else {
					System.out
							.println(getDateStr()
									+ "Client is using an older communication protocol, please contact Eastnets® to get the latest version of the Payload transfer client.");
				}

				System.out.println(getDateStr() + "Disconnecting client...");
				disconnectPeer();
			}
			break;
		case COMMAND_HEART_BEAT:
			// isHeartBeatRequested= false;
			break;
		case COMMAND_PUSH_REQ:
			PushReq pushRequestCommand = (PushReq) command;
			String clientPath = pushRequestCommand.getLocalPath();
			String localPath = pushRequestCommand.getServerPath();
			List<FilePayload> payloads = pushRequestCommand.getFilesPayload();
			Map<String, PayloadTransferResult> transferResult = null;

			System.out.println(getDateStr() + "File transfer request:");
			System.out.println(getDateStr() + "\tLocal Files Path : " + localPath);
			System.out.println(getDateStr() + "\tRemote Files Path : " + clientPath);
			System.out.println(getDateStr() + "\tRequested Payloads : ");
			for (FilePayload payload : payloads) {
				System.out.println(getDateStr() + "\t\t" + payload.getFileName());
			}

			System.out.println(getDateStr() + "\tTransfering Payloads: ");
			// send the files
			boolean transfered = true;
			FileTransferClient fileTransferClient = FileTransferClientFactory.getFileTransferClient(commandLineArgumentsParser.getFtServerType());
			if (fileTransferClient != null) {
				fileTransferClients.add(fileTransferClient);
				if (stopRequestProcessing)
					break;// stop the operation when requested
				try {
					transfered = connectToFileTransferClient(fileTransferClient, commandLineArgumentsParser.getFtsServer(),
							commandLineArgumentsParser.getFtsPort()) == 1;
				} catch (Exception e) {
					String message = e.getMessage();
					if (message.equals("Connection refused: connect")) {
						message = "Connection refused.";
					}
					throw new Exception("File transfer server connection error : " + message);
				}
				if (stopRequestProcessing)
					break;// stop the operation when requested
				if (transfered) {
					try {
						transfered = loginToFileTransferClient(fileTransferClient, commandLineArgumentsParser.getFtsUser(),
								commandLineArgumentsParser.getFtsPassword()) == 1;
					} catch (Exception e) {
						throw new Exception("File transfer server login error : " + e.getMessage());
					}
				}
				if (stopRequestProcessing)
					break;// stop the operation when requested
				if (transfered) {
					try {
						transferResult = transferFiles(fileTransferClient, payloads, localPath, clientPath);
					} catch (Exception e) {
						throw new Exception("File transfer error : " + e.getMessage());
					}
				}
				// will not change the transfered flag as if its true here the
				// files should be transfered successfully
				if (transfered) {
					fileTransferClient.disconnect();
				}

				fileTransferClients.remove(fileTransferClient);
			} else {
				transfered = false;
			}

			if (stopRequestProcessing)
				break;// stop the operation when requested
			if (!transfered) {
				// set all payloads transfer result to PAYLOAD_TRANSFER_FAILED,
				// then pass it back to the client
				transferResult = new HashMap<String, PayloadTransferResult>();
				for (FilePayload payload : payloads) {
					transferResult.put(getPayloadIdentifier(payload), PayloadTransferResult.PAYLOAD_TRANSFER_FAILED);
				}
			}

			if (stopRequestProcessing)
				break;// stop the operation when requested
			// prepare response
			PushRes response = new PushRes(transferResult);
			send(response);
			break;
		default:
			System.out.println(getDateStr() + "Unhandled command :" + command.getType());
			break;
		}
	}

	/**
	 * transfer the files using the file transfer client
	 * 
	 * @param filesPayload
	 * @param localPath
	 * @param remotePath
	 * @return map for each payload file and its transfer result
	 * @throws Exception
	 */
	Map<String, PayloadTransferResult> transferFiles(FileTransferClient fileTransferClient, List<FilePayload> filesPayload, String localPath,
			String remotePath) throws Exception {
		Map<String, PayloadTransferResult> results = new HashMap<String, PayloadTransferResult>();
		List<TransferFile> filesToTransfer = new ArrayList<TransferFile>();
		Map<TransferFile, FilePayload> payloadTransferResult = new HashMap<TransferFile, FilePayload>();
		Map<String, PayloadTransferResult> printResults = new HashMap<String, PayloadTransferResult>();
		for (FilePayload payload : filesPayload) {
			
			PayloadTransferResult result = checkAllFiles(localPath + File.separator ,  payload.getFileName(), payload );
			if (result != PayloadTransferResult.PAYLOAD_SUCCESS) {
				results.put(getPayloadIdentifier(payload), result);
				printResults.put(payload.getFileName(), result);
				continue;// this file is not valid, we will not try to send it
			}
			TransferFile fileTransfer = new TransferFile(payload.getPayloadFilePath(), payload.getFileName(), 0);
			payloadTransferResult.put(fileTransfer, payload);
			filesToTransfer.add(fileTransfer);
		}

		if (stopRequestProcessing) {
			printTransferResult(printResults);
			return results;// stop the operation when requested
		}
		fileTransferClient.changeRemoteDirectory(remotePath);
		fileTransferClient.changeLocalDirectory(localPath);
		fileTransferClient.putFiles(filesToTransfer);

		for (TransferFile fileTransfer : filesToTransfer) {
			if (fileTransfer.getTransferStatus() != 1) {
				results.put(getPayloadIdentifier(payloadTransferResult.get(fileTransfer)), PayloadTransferResult.PAYLOAD_TRANSFER_FAILED);
				printResults.put(fileTransfer.getDestinationFileName(), PayloadTransferResult.PAYLOAD_TRANSFER_FAILED);
			} else {
				results.put(getPayloadIdentifier(payloadTransferResult.get(fileTransfer)), PayloadTransferResult.PAYLOAD_SUCCESS);
				printResults.put(fileTransfer.getDestinationFileName(), PayloadTransferResult.PAYLOAD_SUCCESS);
			}
		}

		printTransferResult(printResults);
		return results;
	}

	private void printTransferResult(Map<String, PayloadTransferResult> results) {
		Iterator<Entry<String, PayloadTransferResult>> itr = results.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, PayloadTransferResult> entry = itr.next();
			System.out.println(getDateStr() + "\t\t File : \"" + entry.getKey() + "\" - " + toString(entry.getValue()));
		}

	}
}
