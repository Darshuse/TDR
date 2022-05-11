package com.eastnets.resilience.filepayloadcommunication.server;

public class ServerCommandLineArgumentsParser {
	private Integer port = null;
	private String ftsServer = null;
	private Integer ftsPort = null;
	private String ftsUser = null;
	private String ftsPassword = null;
	private Integer timeout = 30;
	private String ftServerType = "SFTP";

	public void displayArguments() {
		System.out.println("Passed arguments:");
		System.out.println("Listen Port                         : " + port);
		System.out.println("File Transfere server name          : " + ftsServer);
		System.out.println("File Transfere server port          : " + ftsPort);
		System.out.println("File Transfere server user name     : " + ftsUser);
		System.out.println("File Transfere server user password : " + String.format("%0" + (ftsPassword.length()) + "d", 0).replace("0", "*"));
		System.out.println("Client heartbeat timeout            : " + timeout);
		// System.out.println("File transfer server type           : " +
		// ftServerType );
		System.out.println();
	}

	public void displayUsage() {
		System.out.println("Usage:");
		System.out.println("-port  \"port\"                : Port to listen on for client");
		System.out.println("-fts \"server name\"           : File transfer server name");
		System.out.println("-fts_port \"port\"             : File transfer server port");
		System.out.println("-fts_user \"user\"             : File transfer server user name ");
		System.out.println("-fts_password \"password\"     : File transfer server user password");
		System.out.println("[-timeout \"timeout seconds\"] : Client heartbeat timeout, default is 30 seconds");
		// System.out.println("[-fts_type \"type\"]           : File transfer server type, default is SFTP");
		System.out.println();
	}

	public String getFtServerType() {
		return ftServerType;
	}

	public String getFtsPassword() {
		return ftsPassword;
	}

	public Integer getFtsPort() {
		return ftsPort;
	}

	public String getFtsServer() {
		return ftsServer;
	}

	public String getFtsUser() {
		return ftsUser;
	}

	public Integer getPort() {
		return port;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public boolean parseAndValidate(String[] args) {
		String param = "";
		try {
			for (int index = 0; index < args.length; index++) {
				param = args[index];

				if ("-port".equalsIgnoreCase(param)) {
					// port
					setPort(Integer.parseInt(args[++index]));
				} else if ("-fts".equalsIgnoreCase(param)) {
					// file transfer server
					setFtsServer(args[++index]);
				} else if ("-fts_port".equalsIgnoreCase(param)) {
					// file transfer server port
					setFtsPort(Integer.parseInt(args[++index]));
				} else if ("-fts_user".equalsIgnoreCase(param)) {
					// file transfer server user
					setFtsUser(args[++index]);
				} else if ("-fts_password".equalsIgnoreCase(param)) {
					// file transfer server user password
					setFtsPassword(args[++index]);
				} else if ("-timeout".equalsIgnoreCase(param)) {
					// hearbeat timeout
					setTimeout(Integer.parseInt(args[++index]));
				} else if ("-fts_type".equalsIgnoreCase(param)) {
					// file transfer server type
					setFtServerType(args[++index]);
				} else {
					System.out.println("Unhandled commandline argument :" + param);
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception reading parameter \"" + param + "\": " + ex.getMessage());
			return false;
		}
		if (port == null && ftsServer == null && ftsPort == null && ftsUser == null && ftsPassword == null) {
			return false;
		}
		if (port == null) {
			System.out.println("Error: The Parameter -port is missing.");
			return false;
		}
		if (ftsServer == null) {
			System.out.println("Error: The Parameter -fts is missing.");
			return false;
		}
		if (ftsPort == null) {
			System.out.println("Error: The Parameter -fts_port is missing.");
			return false;
		}
		if (ftsUser == null) {
			System.out.println("Error: The Parameter -fts_user is missing.");
			return false;
		}
		if (ftsPassword == null) {
			System.out.println("Error: The Parameter -fts_password is missing.");
			return false;
		}

		return true;
	}

	public void setFtServerType(String ftServerType) {
		this.ftServerType = ftServerType;
	}

	public void setFtsPassword(String ftsPassword) {
		this.ftsPassword = ftsPassword;
	}

	public void setFtsPort(Integer ftsPort) {
		this.ftsPort = ftsPort;
	}

	public void setFtsServer(String ftsServer) {
		this.ftsServer = ftsServer;
	}

	public void setFtsUser(String ftsUser) {
		this.ftsUser = ftsUser;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

}
