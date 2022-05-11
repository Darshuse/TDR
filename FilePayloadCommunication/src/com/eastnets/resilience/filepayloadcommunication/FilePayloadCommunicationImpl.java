package com.eastnets.resilience.filepayloadcommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.resilience.digest.DigestAlgorithm;
import com.eastnets.resilience.digest.DigestAlgorithmFactory;
import com.eastnets.resilience.filepayloadcommunication.commands.Command;
import com.eastnets.resilience.filepayloadcommunication.commands.CommandType;
import com.eastnets.resilience.filepayloadcommunication.commands.HeartBeatCmd;
import com.eastnets.resilience.filepayloadcommunication.commands.PayloadTransferResult;
import com.eastnets.resilience.filepayloadcommunication.commands.PeekReq;
import com.eastnets.resilience.filepayloadcommunication.commands.PeekRes;
import com.eastnets.resilience.filepayloadcommunication.commands.PushReq;
import com.eastnets.resilience.filepayloadcommunication.commands.PushRes;
import com.eastnets.resilience.filepayloadcommunication.commands.RegisterPeerCmd;

public abstract class FilePayloadCommunicationImpl {
	static {
		Command.registerCommandFactory(CommandType.COMMAND_REGISTER_PEER, RegisterPeerCmd.class);
		Command.registerCommandFactory(CommandType.COMMAND_HEART_BEAT, HeartBeatCmd.class);
		Command.registerCommandFactory(CommandType.COMMAND_PEEK_REQ, PeekReq.class);
		Command.registerCommandFactory(CommandType.COMMAND_PEEK_RES, PeekRes.class);
		Command.registerCommandFactory(CommandType.COMMAND_PUSH_REQ, PushReq.class);
		Command.registerCommandFactory(CommandType.COMMAND_PUSH_RES, PushRes.class);
	}

	private class CommandHandler implements Runnable {
		private String receivedString;
		private Thread callerThread;

		CommandHandler(String receivedString) {
			this.receivedString = receivedString;
		}

		@Override
		public void run() {
			if (receivedString == null) {
				return;
			}
			FilePayloadCommunicationImpl.currentTasks.add(callerThread);
			try {
				Command cmd = parse(receivedString);
				if (cmd != null) {
					System.out.println(getDateStr() + "receiving : " + FilePayloadCommunicationImpl.toString(cmd.getType()));
				} else {
					System.out.println(getDateStr() + "null Command received. ");
				}
				processCommand(cmd);
			} catch (Exception e) {
				Throwable tr = e.getCause();
				if (tr == null) {
					tr = e;
				}

				if (tr != null && tr.getMessage() != null && tr.getMessage().contains("error :")) {
					System.out.println(getDateStr() + tr.getMessage());
				} else {
					System.out.println(getDateStr() + "Error :" + tr.getMessage());
				}
				System.out.println(getDateStr() + "Disconnecting peer");
				try {
					disconnectPeer();
				} catch (Exception e1) {
				}
			}
			FilePayloadCommunicationImpl.currentTasks.remove(callerThread);
		}

		public void setCallerThread(Thread callerThread) {
			this.callerThread = callerThread;
		}
	}

	protected static String getDateStr() {
		return dateFormat.format(new Date());
	}

	Socket socket = null;

	BufferedReader in = null;
	DataOutputStream out = null;
	Integer maxTrialCount = 10;

	private static List<Thread> currentTasks = Collections.synchronizedList(new ArrayList<Thread>());

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

	/**
	 * compare the file digest with the passed digestValue
	 * 
	 * @param filePath
	 * @param digestValue
	 * @param digestAlg
	 * @return
	 */
	boolean checkDigest(String filePath, String digestValue, String digestAlg) {
		DigestAlgorithm digestChecker;
		try {
			digestChecker = DigestAlgorithmFactory.getDigestAlgorithm(digestAlg);
			return digestChecker.checkDigest64String(filePath, digestValue);
		} catch (Exception e) {
			System.out.println(getDateStr() + "Digest Error :" + e.getMessage());
		}
		return false;
	}

	/**
	 * check if the file is the same file identified by the size and digest
	 * 
	 * @param listOfFiles
	 * @param digest
	 * @param digestAlg
	 * @param size
	 * @return PAYLOAD_SUCCESS, PAYLOAD_FILE_NOT_FOUND, PAYLOAD_SIZE_NOT_MATCHED and PAYLOAD_DIGEST_NOT_MATCHED
	 */
	private PayloadTransferResult checkFile(File payloadFile, String digest, String digestAlg, int size) {
		if (!payloadFile.exists()) {// file does not exist
			return PayloadTransferResult.PAYLOAD_FILE_NOT_FOUND;
		}
		if (payloadFile.length() != size) {// size not match
			return PayloadTransferResult.PAYLOAD_SIZE_NOT_MATCH;
		}

		if (!checkDigest(payloadFile.getAbsolutePath(), digest, digestAlg)) { // digest not match
			return PayloadTransferResult.PAYLOAD_DIGEST_NOT_MATCH;
		}
		// all good
		return PayloadTransferResult.PAYLOAD_SUCCESS;
	}
	
	/**
	 * check for the payload file, and check for any extra extension added to the payload file ( ., .SNL...),
	 * this will get all files starting with the original file name and then check 
	 * @param filePath
	 * @param fileName
	 * @param payload
	 * @return
	 */
	protected PayloadTransferResult checkAllFiles(final String filePath, final String fileName, FilePayload payload) {
		PayloadTransferResult result= PayloadTransferResult.PAYLOAD_FILE_NOT_FOUND;
		
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if ( pathname.isFile() && pathname.getName().startsWith(fileName) ){
					return true;
				}
				return false;
			}
		});
		
		if ( listOfFiles == null ){
			return result;
		}

		for (int i = 0; i < listOfFiles.length; i++) {
			result = checkFile( listOfFiles[i], payload.getFileDigest(), payload.getFileDigestAlgo(), payload.getFileSize() );
			if ( result == PayloadTransferResult.PAYLOAD_SUCCESS ) {
				payload.setPayloadFilePath(listOfFiles[i].getAbsolutePath());
				return result;
			}
		}
		return result;
	}

	/**
	 * decrypt the passed buffer
	 * 
	 * @param buffer
	 * @return decrypted version of the buffer
	 * @throws Exception
	 */
	String decrypt(String buffer) throws Exception {
		return AESEncryptDecrypt.decrypt(buffer);
	}

	/**
	 * disconnect the connected peer
	 * 
	 * @throws Exception
	 */
	protected abstract void disconnectPeer() throws Exception;

	/**
	 * encrypt the passed buffer
	 * 
	 * @param buffer
	 * @return encrypted version of the buffer
	 * @throws Exception
	 */
	String encrypt(String buffer) throws Exception {
		return AESEncryptDecrypt.encrypt(buffer);
	}

	protected String getPayloadIdentifier(FilePayload payload) {
		return String.format("%s^%d^%s^%s", payload.getFileName(), payload.getFileSize(), payload.getFileDigest(), payload.getFileDigestAlgo());
	}

	protected void handleCommand(String receivedString) {
		CommandHandler commandProcessRunnable = new CommandHandler(receivedString);
		Thread thread = new Thread(commandProcessRunnable);
		commandProcessRunnable.setCallerThread(thread);
		thread.start();
	}

	protected void interrupt() {
	}

	protected void killCurrentThreads() {
		if (currentTasks.isEmpty()) {
			return;
		}
		System.out.println(getDateStr() + "stopRequestProcessing");

		this.interrupt();

		// wait just one second for threads to finish
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		// kill threads
		for (Thread thread : currentTasks) {
			thread.interrupt();
		}
		currentTasks.clear();
	}

	Command parse(String receivedString) throws Exception {
		if (receivedString != null && receivedString.length() > 3) {
			int sepIndex = receivedString.indexOf(";;");
			if (sepIndex == -1) {
				return null;
			}
			String commandType = receivedString.substring(0, sepIndex);

			Command command = Command.getCommandByType(CommandType.fromString(commandType));

			command.parse(receivedString.substring(sepIndex + 2));
			return command;
		}
		return null;
	}

	/**
	 * process the received command and send the response, this should not be called directly. Called handleCommand() instead.
	 * 
	 * @param command
	 *            : command received from peer
	 * @param thread
	 * @return response command to be sent back to peer, may be null if no reply is required
	 * @throws Exception
	 */
	protected abstract void processCommand(Command command) throws Exception;

	/**
	 * receive a command from the registered socket
	 * 
	 * @return the parsed command, or null if the command was invalid, socket not initialized or something went wrong
	 * @throws Exception
	 */
	protected String receive() throws Exception {
		if (in == null) {
			return null;
		}

		// the command structure is like ( without the | )
		// 10 chars for the length|command_type|;;|command_data
		String readBuffer = receive(10);

		int length = 0;
		length = Integer.parseInt(readBuffer);
		String bufferStr = receive(length);

		return decrypt(bufferStr);// decrypt the received string
	}

	private String receive(int length) throws Exception {
		int trialCount = 0;
		String readBuffer = "";

		int charCount = 0;
		while (readBuffer.length() != length && trialCount != maxTrialCount) {
			int ch = 0;
			while (charCount < length && (ch = in.read()) != -1) {
				readBuffer += (char) ch;
				++charCount;
			}
			if (ch == -1) {
				throw new Exception("Connection lost");
			}
			if (charCount != length) {
				/*
				 * System.out.println(getDateStr() + "try " + (trialCount + 1) + " got " + readBuffer.length() + " characters out of " + length);
				 */
				Thread.sleep(50);
			}
			++trialCount;
		}
		if (readBuffer.length() != length) {
			throw new Exception("Incomplete command received");
		}
		return readBuffer;
	}

	/**
	 * register a socket that will be used for sending and receiving if the connection was reset and a new socket was made, the new socket should be
	 * registered here
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	protected int registerSocket(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());
		return 0;
	}

	/**
	 * send the command using the registered socket
	 * 
	 * @param command
	 * @return
	 * @throws Exception
	 */
	protected int send(Command command) throws Exception {
		if (out == null || command == null) {
			return -1;
		}
		System.out.println(getDateStr() + "Sending : " + toString(command.getType()));
		// the command structure is like ( without the | )
		// 10 chars for the length|command_type|;;|command_data
		String buffer = command.getType().toString() + ";;" + command.prepare();
		buffer = encrypt(buffer);// encrypt the buffer before sending

		buffer = String.format("%010d%s", buffer.length(), buffer);

		out.writeBytes(buffer);
		out.flush();
		return 0;

	}

	protected static String toString(PayloadTransferResult result) {
		switch (result) {

		case PAYLOAD_DIGEST_NOT_MATCH:
			return "File digest not match";
		case PAYLOAD_FILE_NOT_FOUND:
			return "File not found";
		case PAYLOAD_SIZE_NOT_MATCH:
			return "File size not match";
		case PAYLOAD_SUCCESS:
			return "File transfered successfully";
		case PAYLOAD_TRANSFER_FAILED:
			return "File transfer failed";
		case PAYLOAD_UNKNOWN:
			return "Unknown";
		default:
			break;
		}
		return "Unknown";
	}

	protected static String toString(CommandType commandType) {
		switch (commandType) {
		case COMMAND_HEART_BEAT:
			return "Heart beat";
		case COMMAND_PEEK_REQ:
			return "Peek request";
		case COMMAND_PEEK_RES:
			return "Peek response";
		case COMMAND_PUSH_REQ:
			return "Push request";
		case COMMAND_PUSH_RES:
			return "Push response";
		case COMMAND_REGISTER_PEER:
			return "Register peer";
		case COMMAND_UNKNOWN:
			return "Unknown";

		}
		return "Unknown";
	}
}
