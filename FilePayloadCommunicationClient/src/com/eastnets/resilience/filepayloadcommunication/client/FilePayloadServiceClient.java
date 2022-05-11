package com.eastnets.resilience.filepayloadcommunication.client;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import com.eastnets.domain.Pair;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.filepayload.FilePayloadGlobalSettings;
import com.eastnets.domain.filepayload.FilePayloadSettings;
import com.eastnets.resilience.filepayloadcommunication.FilePayloadCommunicationImpl;
import com.eastnets.resilience.filepayloadcommunication.Globals;
import com.eastnets.resilience.filepayloadcommunication.commands.Command;
import com.eastnets.resilience.filepayloadcommunication.commands.HeartBeatCmd;
import com.eastnets.resilience.filepayloadcommunication.commands.PayloadTransferResult;
import com.eastnets.resilience.filepayloadcommunication.commands.PushReq;
import com.eastnets.resilience.filepayloadcommunication.commands.PushRes;
import com.eastnets.resilience.filepayloadcommunication.commands.RegisterPeerCmd;

public class FilePayloadServiceClient extends FilePayloadCommunicationImpl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// write the header
		System.out.println(Globals.clientHeader);
		System.out.println(String.format("%0" + (Globals.clientHeader.length()) + "d", 0).replace("0", "-"));

		final FilePayloadServiceClient app = new FilePayloadServiceClient();
		

		/*app.dostuff();
		int hello = 0;
		hello++;
		if ( hello == 1 ){
			return;
		}*/

		// validate the arguments
		app.commandLineArgumentsParser = new ClientCommandLineArgumentsParser();
		if (!app.commandLineArgumentsParser.parseAndValidate(args)) {
			// display the application usage
			app.commandLineArgumentsParser.displayUsage();
			return;
		}
		app.commandLineArgumentsParser.displayArguments();

		while (true) {
			try {

				// Initializing database connection
				app.appServiceInterface = new ClientApplicationServiceInterface(app.commandLineArgumentsParser);
				

				//check license
				if ( !app.appServiceInterface.isLicensed()){
					System.out.println("Payload client is not licensed, Please contact Eastnets support to obtain a license.");
					System.exit(1);
				}
				else{
					//this will be printed later so that we don't ruin the style :) 
					//System.out.println("Payload client is licensed.");
				}
				
				// FilePayloadService filePayloadService =
				// app.appServiceInterface.getServiceLocater().getFilePayloadService();
				app.globalSettings = app.appServiceInterface.getServiceLocater().getFilePayloadService()
						.getGlobalSettings(app.commandLineArgumentsParser.getDbUser());
				app.settings = app.appServiceInterface.getServiceLocater().getFilePayloadService()
						.getConnectionSettings(app.commandLineArgumentsParser.getDbUser(), app.commandLineArgumentsParser.getAid());
				if (app.settings == null) {
					throw new Exception("Unable to get settings for aid " + app.commandLineArgumentsParser.getAid());
				}
				//for now we don't need the stand-alone mode, if needed later remove the following line 
				app.settings.setStandalone(true);
				//System.out.println( "Stand-alone Mode              : " + ( app.settings.isStandalone() ? "On" : "Off" ));
				System.out.println( "Local payload path            : " + app.getLocalPath() );
				
				//if ( app.globalSettings.isVerbose() )
				{
					System.out.println( "Cunnk size                    : " + app.globalSettings.getChunkCount() );
					System.out.println( "Delay (minutes)               : " + app.globalSettings.getDelayMinutes() );
					System.out.println( "Payload maximum retries       : " + app.globalSettings.getMaxTryCount() );
					System.out.println( "Extract archives              : " + ( app.globalSettings.isExtractZip() ? "On" : "Off" ) );					
				}
				
				System.out.println("Payload client is licensed.");
				System.out.println();
				
				
				
				app.resetApp = false;
				app.errorString= "";
				app.requestedPayloads.clear();

				// start the file requesting thread
				fileSendThread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (true) {
								if (!app.settings.isStandalone() &&( app.socket == null || !app.socket.isConnected() )) {
									// while not connected just wait a second
									// and check for connection again
									Thread.sleep(1000);
									continue;
								}
								
								app.sendFileListToServer();

								// now lets wait until the next cycle

								if (app.globalSettings.getDelayMinutes() >= 1) {
									Thread.sleep(app.globalSettings.getDelayMinutes() * 60 * 1000);
								} else {
									Thread.sleep(60000);// wait a minute if the
														// DelayMinutes in not
														// valid
								}
							}
						} catch (Exception e) {
							try {
								app.disconnectPeer();
							} catch (Exception e1) {
							}
							app.resetApp = true;
							app.errorString= e.getMessage();
							e.printStackTrace();
						}
					}
				});
				fileSendThread.start();

				// start timer for resetting the requested flag for payloads
				// that was requested long time ago
				resetTimer = new Timer();
				resetTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						app.resetRequested(app.commandLineArgumentsParser.getRequestedResetTimeHours());
					}
				}, 1 * 60 * 60 * 1000, 1 * 60 * 60 * 1000);// run each hour

				while (true ) {
					
					try {

						// we got a new connection so reset the requested flag
						// for all payloads
						app.appServiceInterface.getServiceLocater().getFilePayloadService()
								.resetRequestedFlag(app.commandLineArgumentsParser.getDbUser(), app.commandLineArgumentsParser.getAid());

						
						if ( !app.settings.isStandalone() ){
							System.out.println(getDateStr()
									+ String.format("Connecting to server \"%s:%d\"", app.settings.getServerAddress(), app.settings.getServerPort()));
							app.socket = new Socket(app.settings.getServerAddress(), app.settings.getServerPort()); // this will trigger an exception
																													// if failed to connect

							System.out.println(getDateStr() + "Connected to server.");
							app.registerSocket(app.socket);
 
							app.initializeCommunication();

						}
						
					} catch (Exception ex) {
						if (app.resetApp) {
							throw new Exception("Database connection error");
						}
						String message = ex.getMessage();
						if (message.equals("Connection refused: connect")) {
							message = "Connection refused.";
						}
						System.out.println(getDateStr() + "Unable to connect to server : " + message);
						try {
							Thread.sleep(2000);// wait 2 seconds then try to
												// connect again
						} catch (InterruptedException e) {
						}
						continue;
					}
					if ( !app.settings.isStandalone() ){
						while (app.socket != null && app.socket.isConnected()) {
							String receivedStr = app.receive();
							app.handleCommand(receivedStr);
						}
					}else{
						while ( true ){
							if (app.resetApp) {
								throw new Exception(app.errorString);
							}
							Thread.sleep(1000);//wait a second
						}
					}
				}
			} catch (CannotGetJdbcConnectionException ex) {
				Throwable tr = ex.getCause();
				if (tr == null) {
					tr = ex;
				}
				System.out.println(getDateStr() + "Database Error: " + tr.getMessage());
			} catch (Exception e) {
				System.out.println(getDateStr() + "Error: " + e.getMessage());
			} finally {
				System.out.println(getDateStr() + "Restarting ... ");
				System.out.println();
				if (fileSendThread != null && fileSendThread.isAlive()) {
					fileSendThread.interrupt();
				}
				if (resetTimer != null) {
					resetTimer.cancel();
				}
			}
			try {
				// just wait 2 seconds and start over
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	Socket socket;

	ClientCommandLineArgumentsParser commandLineArgumentsParser;

	// could be more than one Payload record waiting for the same file, don't
	// want to restrict it
	Map<String, List<FilePayload>> requestedPayloads = new HashMap<String, List<FilePayload>>();
	FilePayloadGlobalSettings globalSettings;

	FilePayloadSettings settings;
	ClientApplicationServiceInterface appServiceInterface;

	protected boolean resetApp = false;
	protected String errorString = "";
	private static Thread fileSendThread;

	private static Timer resetTimer;

	/**
	 * check for the passed payloads, any unmatched payload will be requested, exisitingPayloads will contain any payload that exists in the directory
	 * and match for the digest and file size files should be in globalSettings.getPayloadTransferePath() + File.separator + settings.getLocalPath() +
	 * File.separator + payload.getFileName()
	 * 
	 * @param payloads
	 * @param existingPayloads
	 * @return list of payloads to be retrieved from the server
	 */
	private List<Pair<FilePayload, PayloadTransferResult>> checkPayloads(List<FilePayload> payloads, List<Pair<FilePayload, PayloadTransferResult>> existingPayloads) {
		if (payloads.isEmpty()) {
			return new ArrayList<Pair<FilePayload,PayloadTransferResult>>();
		}
		List<Pair<FilePayload, PayloadTransferResult>> payloadToRetrieved = new ArrayList<Pair<FilePayload, PayloadTransferResult>>();
		String dateStr = getDateStr();
		String spaces = String.format("%0" + dateStr.length() + "d", 0).replace("0", " ");
		System.out.println(dateStr + "Payloads local check: " );
		
		for (FilePayload payload : payloads) {
			PayloadTransferResult result = checkAllFiles(getLocalPath(),  payload.getFileName(), payload );
			if (result != PayloadTransferResult.PAYLOAD_SUCCESS) {
				payloadToRetrieved.add(new Pair<FilePayload, PayloadTransferResult>(payload, result ));
			} else {
				existingPayloads.add(new Pair<FilePayload, PayloadTransferResult>(payload, PayloadTransferResult.PAYLOAD_SUCCESS));
			}
			String res = toString(result);
			if (result == PayloadTransferResult.PAYLOAD_SUCCESS) {
				res = "File found";
			}
			System.out.println(spaces + "- \"" + payload.getFileName() + "\" - " + res);
		}
		return payloadToRetrieved;
	}

	@Override
	protected void disconnectPeer() throws Exception {
		if (socket != null) {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		}

		// update the payloads that we are waiting for
		List<Pair<FilePayload, PayloadTransferResult>> failPayloads = new ArrayList<Pair<FilePayload, PayloadTransferResult>>();
		Iterator<Entry<String, List<FilePayload>>> iter = requestedPayloads.entrySet().iterator();
		while (iter.hasNext()) {
			List<FilePayload> payloads = iter.next().getValue();
			for (FilePayload payload : payloads) {
				failPayloads.add(new Pair<FilePayload, PayloadTransferResult>(payload, PayloadTransferResult.PAYLOAD_UNKNOWN));
			}
		}
		updatePayloads(failPayloads);
		socket = null;
	}

	private void initializeCommunication() throws Exception {
		RegisterPeerCmd cmd = new RegisterPeerCmd(Globals.protocolVersion);
		send(cmd);
	}

	@Override
	protected void processCommand(Command command) throws Exception {
		if (command == null) {
			return;
		}
		switch (command.getType()) {
		case COMMAND_REGISTER_PEER:

			RegisterPeerCmd registerServerCommand = (RegisterPeerCmd) command;

			if (registerServerCommand.getProtocolVersion() != Globals.protocolVersion) {
				if (registerServerCommand.getProtocolVersion() > Globals.protocolVersion) {
					System.out
							.println(getDateStr()
									+ "Server is using a newer communication protocol, please contact Eastnets® to get the latest version of the Payload transfer client.");
				} else {
					System.out
							.println(getDateStr()
									+ "Server is using an older communication protocol, please contact Eastnets® to get the latest version of the Payload transfer server.");
				}

				System.out.println(getDateStr() + "Disconnecting...");
				disconnectPeer();
			}
			break;
		case COMMAND_HEART_BEAT:
			HeartBeatCmd cmd = new HeartBeatCmd();
			send(cmd);// respond the the heartbeat request with a heartbeat
						// response
			break;
		case COMMAND_PUSH_RES:
			PushRes pushResponseCommand = (PushRes) command;
			Map<String, PayloadTransferResult> result = pushResponseCommand.getPayloadTransferResult();

			Iterator<Entry<String, PayloadTransferResult>> iterator = result.entrySet().iterator();

			System.out.println(getDateStr() + "File transfer response:");
			List<Pair<FilePayload, PayloadTransferResult>> filePayloads = new ArrayList<Pair<FilePayload, PayloadTransferResult>>();
			while (iterator.hasNext()) {
				Entry<String, PayloadTransferResult> entry = iterator.next();
				List<FilePayload> payloads = requestedPayloads.get(entry.getKey());// get all payloads waiting for the file
				if (payloads != null && !payloads.isEmpty()) {
					FilePayload payload = payloads.get(0);// we only care about
															// just one payload
					PayloadTransferResult transferResult = entry.getValue();
					System.out.println(getDateStr() + "\t File : \"" + payload.getFileName() + "\" - " + toString(transferResult));
					if (transferResult == PayloadTransferResult.PAYLOAD_SUCCESS) {
						// check if the file was transfered successfully
						transferResult= checkAllFiles(getLocalPath(), payload.getFileName(), payload);
						//transferResult = checkFile( getLocalPath() + payload.getFileName(), payload.getFileDigest(), payload.getFileDigestAlgo(), payload.getFileSize());
						String res = toString(transferResult);
						if (transferResult == PayloadTransferResult.PAYLOAD_SUCCESS) {
							res = "File found.";
						}
						System.out.println(getDateStr() + "\t\t Local File check: " + res);
					}
					filePayloads.add(new Pair<FilePayload, PayloadTransferResult>(payload, transferResult));
				}
			}
			// payloadSuccess(successPayloads);//this will handle the all the
			// requestedPayloads with the same key
			updatePayloads(filePayloads);
			break;
		default:
			System.out.println(getDateStr() + "Unhandled command :" + command.getType());
			break;

		}
	}

	protected void resetRequested(double requestedResetTimeHours) {
		appServiceInterface.getServiceLocater().getFilePayloadService()
				.resetRequested(commandLineArgumentsParser.getDbUser(), commandLineArgumentsParser.getAid(), requestedResetTimeHours);
	}

	private void sendFileListToServer() throws Exception {
		if ( settings.isStandalone() ){
			System.out.println("Getting payload list from Database: ");
		}
		
		//get payload chunk to request or load
		List<FilePayload> payloads = appServiceInterface
				.getServiceLocater()
				.getFilePayloadService()
				.getFilesToRetrieve(commandLineArgumentsParser.getDbUser(), commandLineArgumentsParser.getAid(), globalSettings.getMaxTryCount(),
						globalSettings.getDelayMinutes(), globalSettings.getChunkCount());
		
		if ( settings.isStandalone() ){
			System.out.println("found " + payloads.size() +  " payloads.");
		}

		// first check if we have some payloads that have been already requested
		List<FilePayload> payloadsToTransfer = new ArrayList<FilePayload>();
		for (FilePayload payload : payloads) {

			List<FilePayload> payloadList = null;
			boolean addPayload = true;
			boolean dontSend = false;
			String id = getPayloadIdentifier(payload);
			if (requestedPayloads.containsKey(id)) {
				dontSend = true;
				payloadList = requestedPayloads.get(id);
				if (payloadList.contains(payload)) {
					addPayload = false;// don't add the payload to the list if
										// it was already added
				}
			}
			if (payloadList == null) {
				payloadList = new ArrayList<FilePayload>();
			}

			if (addPayload) {
				payloadList.add(payload);
				requestedPayloads.put(id, payloadList);
				if (!dontSend) {
					payloadsToTransfer.add(payload);
				}
			}
		}

		// check for existing payloads
		List<Pair<FilePayload, PayloadTransferResult>> existingPayloads = new ArrayList<Pair<FilePayload, PayloadTransferResult>>();
		List<Pair<FilePayload, PayloadTransferResult>> nonExistingPayloads = new ArrayList<Pair<FilePayload, PayloadTransferResult>>();
		
		nonExistingPayloads = checkPayloads(payloadsToTransfer, existingPayloads);
		// payloadsToTransfer now doesn't contain any payload in the existingPayloads
		if ( settings.isStandalone() ){
			//in stand-alone mode we don't request the files from the server so files should be updated on the database when the are not found
			existingPayloads.addAll(nonExistingPayloads);
		}
				
		updatePayloads(existingPayloads);
		if ( settings.isStandalone() ){
			//in stand-alone we dont want to request files from any server
			return; 
		}
		payloadsToTransfer.clear();
		for ( Pair<FilePayload, PayloadTransferResult> nonExistingPayload : nonExistingPayloads ) {
			payloadsToTransfer.add(nonExistingPayload.getKey());
		}
		// make sure we have something to request
		if (payloadsToTransfer.isEmpty()) {
			System.out.println(getDateStr() + "No payloads to request");
			return;
		}

		System.out.println(getDateStr() + "File transfer request:");
		System.out.println(getDateStr() + "\tLocal Files Path : " + settings.getLocalPath());
		System.out.println(getDateStr() + "\tRemote Files Path : " + settings.getRemotePath());
		System.out.println(getDateStr() + "\tRequesting files : ");
		for (FilePayload payload : payloadsToTransfer) {
			System.out.println(getDateStr() + "\t\t File : " + payload.getFileName());
		}
		// request files from server
		PushReq request = new PushReq(payloadsToTransfer, settings.getRemotePath(), settings.getLocalPath());
		send(request);
	}

	private void updatePayloads(List<Pair<FilePayload, PayloadTransferResult>> payloads) throws Exception {
		if (payloads == null || payloads.isEmpty()) {
			return;
		}
		System.out.println(getDateStr() + "Updating payloads on database.");

		// prepare the path for the files that is on client machine now.
		String filesPath = getLocalPath();
		
		List<FilePayload> payloadsToUpdate = new ArrayList<FilePayload>();
		List<FilePayload> payloadsSucceeded = new ArrayList<FilePayload>();

		List<String> payloadsToUpdateStatus = new ArrayList<String>();
		for (Pair<FilePayload, PayloadTransferResult> payloadRes : payloads) {
			// get all payloads with the same file name, size, digest and digest
			// algorithm
			String id = getPayloadIdentifier(payloadRes.getKey());
			List<FilePayload> payloadList= requestedPayloads.get(id);
			for (FilePayload payload : payloadList) {
				payloadsToUpdate.add(payload);
				if (payloadRes.getValue() != PayloadTransferResult.PAYLOAD_UNKNOWN) {
					payloadsToUpdateStatus.add(payloadRes.getValue().toString());
				} else {
					payloadsToUpdateStatus.add(null);// don't update the status
				}

				// update file path for success payloads
				if (payloadRes.getValue() == PayloadTransferResult.PAYLOAD_SUCCESS  ) {
					payloadsSucceeded.add(payload);
					if ( payload.getPayloadFilePath() == null || payload.getPayloadFilePath().isEmpty() ){
						payload.setPayloadFilePath(filesPath + payload.getFileName());
					}else{
						//all identical payloads should have the same path 
						for (FilePayload pl : payloadList) {
							pl.setPayloadFilePath(payload.getPayloadFilePath());
						}
					}
				}

				// remove from the requestedPayloads list
				requestedPayloads.remove(id);
			}
		}
		appServiceInterface.getServiceLocater().getFilePayloadService().updateFilesPayload(commandLineArgumentsParser.getDbUser(), payloadsSucceeded, this.globalSettings );

		// clear the requested flag, and update the status
		appServiceInterface.getServiceLocater().getFilePayloadService()
				.updateStatus(commandLineArgumentsParser.getDbUser(), payloadsToUpdate, payloadsToUpdateStatus);

		System.out.println(getDateStr() + "Done updating payloads on database.");
	}

	private String getLocalPath() {
		String filesPath = globalSettings.getPayloadTransferePath().trim();
		if (!filesPath.isEmpty() && !(filesPath.endsWith("\\") || filesPath.endsWith("/"))) {
			filesPath += File.separator;
		}
		filesPath += settings.getLocalPath().trim();
		if (!filesPath.isEmpty() && !(filesPath.endsWith("\\") || filesPath.endsWith("/"))) {
			filesPath += File.separator;
		}
		return filesPath;
	}
	
	/*private void dostuff() {
		FilePayload fp = new FilePayload();
		fp.setAid(0);
		fp.setFileDigest("zCFi3KivLKuTIhJDgJ4tPycOlKFpvo+MbWHPvtHRRjo=");
		fp.setFileDigestAlgo("SHA-256");
		fp.setFileName("hello.txt");
		fp.setFileSize(3245);
		System.out.println( checkAllFiles("C:\\files\\client", "hello.txt", fp) );
	}*/
}
