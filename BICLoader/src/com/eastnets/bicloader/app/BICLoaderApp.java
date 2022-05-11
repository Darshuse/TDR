package com.eastnets.bicloader.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.eastnets.application.BaseApp;
import com.eastnets.bicloader.endec.ConnectionSettings;
import com.eastnets.bicloader.endec.EnEcfParser;
import com.eastnets.bicloader.loadervalidators.CommandParamValidator;
import com.eastnets.config.DBType;
import com.eastnets.dao.bicloader.BicFile;

public class BICLoaderApp extends BaseApp {

	private static final long serialVersionUID = 1L;
	private String ecfFile;
	private static final Logger LOGGER = Logger.getLogger("BICLoader");

	static final String BIC_LOADER_HEADER = "Eastnets BIC Loader - 3.2.0.5";

	private BICLoaderConfig appConfigBean;

	private transient CommandParamValidator validator;
	private static final String DBNAME = "-dbname";
	private static final String DB_TYPE = "-dbtype";
	private static final String IP = "-ip";
	private static final String DBPORT = "-port";
	private static final String INSTANCENAME = "-instancename";
	private static final String SERVICENAME = "-servicename";
	private static final String USER = "-u";
	private static final String PASSWORD = "-p";
	private static final String ECF = "-ecf";
	private static final String VERBOSE = "-v";
	private static final String PATH = "-path";
	private static final String FILE_NAME = "-f";
	private static final String GPI_FILE = "-gpi";
	private static final String BANK_FILE = "-bankfile";
	private static final String BIC_DU_FILE = "-bicdue";
	private static final String BANK_DIRECTORY_FILE = "-bankdirectory";
	private static final String DELETE_OLD = "-deleteold";
	private static final String AUTO = "-auto";
	private static final String DELTA_FILE = "-delta";
	private static final String FULL_FILE = "-full";

	public void startProcess(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try {

			BicFile bicFile;
			boolean returnValue = false;
			returnValue = initProcess(args);
			if (!returnValue) {
				scanner.close();
				return;
			}

			returnValue = false;

			if (appConfigBean.isAutoFiles()) {
				for (String file : appConfigBean.getFiles()) {
					bicFile = createBicFile(file);

					if (!validator.checkFileAgainstArgument(bicFile, appConfigBean.isBankdirectory(), appConfigBean.isBankFile(), appConfigBean.isBicdue(), appConfigBean.isGpiCorr())) {
						LOGGER.error("File name " + bicFile.getFilename() + " doesn't match file type provided in the arguments");
						LOGGER.info("For BICPLUS files use -bankdirectory ");
						LOGGER.info("For BICDIR files use -bankfile or -bicdue");
						LOGGER.info("For gpi files use -gpi");
						returnValue = false;
						continue;
					}

					if (getServiceLocater().getBicLoaderService().checkFileWasProcessedBefore(file)) {

						System.out.println("YOU HAVE ALREADY APPLIED A FILE WITH THE SAME NAME (\"" + bicFile.getFilename() + "\"), DO YOU WANT TO ADD IT AGAIN ? (Y/N)");
						String continueString = scanner.next();
						if (!continueString.equalsIgnoreCase("Y")) {
							returnValue = true;
							continue;
						}
					}

					returnValue = passFilename(bicFile);
				}

			} else {
				bicFile = createBicFile(appConfigBean.getFile());

				if (!validator.checkFileAgainstArgument(bicFile, appConfigBean.isBankdirectory(), appConfigBean.isBankFile(), appConfigBean.isBicdue(), appConfigBean.isGpiCorr())) {

					LOGGER.info("For BICPLUS files use -bankdirectory ");
					LOGGER.info("For BICDIR files use -bankfile or -bicdue");
					LOGGER.info("For gpi files use -gpi");

					scanner.close();
					throw new Exception("File name " + bicFile.getFilename() + " doesn't match file type provided in the arguments");
				}
				if (getServiceLocater().getBicLoaderService().checkFileWasProcessedBefore(appConfigBean.getFile())) {

					System.out.println("YOU HAVE ALREADY APPLIED A FILE WITH THE SAME NAME (\"" + bicFile.getFilename() + "\"), DO YOU WANT TO ADD IT AGAIN ? (Y/N)");
					String continueString = scanner.next();
					if (continueString.equalsIgnoreCase("Y")) {
						returnValue = passFilename(bicFile);
					} else {
						returnValue = true;
					}
				} else {
					returnValue = passFilename(bicFile);
				}

			}

			scanner.close();
			if (returnValue) {
				LOGGER.info("Finished Successfully");
			} else {
				LOGGER.info("Failed");
			}

		} catch (Exception e) {
			if (e.getMessage() == null || e.getMessage().isEmpty()) {
				System.exit(0);
			} else {
				if (!e.getMessage().contains("doesn't match file type provided in the arguments")) {
					e.printStackTrace();
				}
				LOGGER.error("Error, reason : " + e.getMessage());
			}
		}
	}

	private BicFile createBicFile(String file) {

		BicFile bicFile = new BicFile();
		bicFile.setPath(appConfigBean.getDirectory());
		bicFile.setFilename(file);
		bicFile.setDeleteOldOption(appConfigBean.isDeleteOld());
		if (appConfigBean.isGpiCorr()) {
			bicFile.setFileType("gpi");
		} else if (appConfigBean.isBankdirectory()) {
			bicFile.setFileType("BankDirectoryPlus");
		} else if (appConfigBean.isBankFile()) {
			bicFile.setFileType("BankFile");
		} else if (appConfigBean.isBicdue()) {
			bicFile.setFileType("BICDUE");
		}
		if (!appConfigBean.isDeltaFile()) {
			bicFile.setFullFile(true);
		}

		return bicFile;
	}

	public boolean processGPICORR(BicFile bicFile) {
		List<String> strBuff = null;
		List<Map<String, String>> parsedContent = null;
		boolean returnValue = false;

		LOGGER.info("<processGPICORR> : Processing GPI correspondent file");

		try {

			LOGGER.debug("<processGPICORR> : read file " + appConfigBean.getDirectory() + "/" + appConfigBean.getFile());

			strBuff = getServiceLocater().getBicLoaderService().readTxtFile(bicFile.getPath() + "/" + bicFile.getFilename());

			if (strBuff == null) {
				throw new Exception("Failed to read the file");
			}

			LOGGER.debug("<processGPICORR> : parse file " + appConfigBean.getDirectory() + "/" + "GPI_V1_MONTHLY_FULL_20170623.txt");

			parsedContent = getServiceLocater().getBicLoaderService().parsingtxtFile(strBuff);

			if (parsedContent == null) {
				throw new Exception("Failed to parse the file");
			}

			if (appConfigBean.isDeleteOld() && !appConfigBean.isDeltaFile()) {
				LOGGER.debug("<processGPICORR> : delete an old data from the database");

				returnValue = getServiceLocater().getBicLoaderService().deleteGPICorr();

				if (!returnValue) {
					throw new Exception("Failed to delete an old data from the database");
				}
				appConfigBean.setDeleteOld(false);
			}

			LOGGER.debug("<processGPICORR> : store file " + appConfigBean.getDirectory() + "/" + "GPI_V1_MONTHLY_FULL_20170623.txt");

			returnValue = getServiceLocater().getBicLoaderService().saveGPICorr(parsedContent, bicFile);

			if (!returnValue) {
				throw new Exception("Failed to store the data into database");
			}
		} catch (Exception e) {
			returnValue = false;
			getServiceLocater().getBicLoaderService().insertIntoErrorld("BICLoader", "error", "BICLoader", "an error has occurred during process GPI correspondent file", "");
			LOGGER.error("<processGPICORR> : an error has occurred during process GPI correspondent file, reason : " + e.getMessage());
		}

		return returnValue;
	}

	public boolean processCorrFile(BicFile bicFile) {
		boolean retrunValue = false;

		if (appConfigBean.isBankFile()) {
			LOGGER.info("<processBankFile> : Processing SWIFT Bank File");
		} else if (appConfigBean.isBicdue()) {
			LOGGER.info("<processBICDue> : Processing BIC Due correspondent file");
		} else if (appConfigBean.isBankdirectory()) {
			LOGGER.info("<processBICDue> : Processing Bank Directory correspondent file");
		}

		String filename = bicFile.getFilename();
		if ((filename.contains("FI")) || (filename.contains("DELTA")) || (filename.contains("FULL"))) {
			retrunValue = processFICorr(bicFile);

		} else if ((filename.contains("CT")) || (filename.contains("COUNTRY_CODE"))) {
			retrunValue = processCTCorr(bicFile);

		} else if ((filename.contains("CU")) || (filename.contains("CURRENCY_CODE"))) {
			retrunValue = processCUCorr(bicFile);
		}

		return retrunValue;
	}

	public boolean passFilename(BicFile bicFile) {
		boolean retrunValue = false;

		if (appConfigBean.isGpiCorr()) {
			retrunValue = processGPICORR(bicFile);
		} else {
			retrunValue = processCorrFile(bicFile);
		}

		return retrunValue;
	}

	public boolean processFICorr(BicFile bicFile) {
		boolean retrunValue = false;

		try {

			LOGGER.debug("<processFICorr> : read file " + bicFile.getPath() + "/" + bicFile.getFilename());

			retrunValue = getServiceLocater().getBicLoaderService().parseAndStoreFiFile(bicFile.getPath() + "/" + bicFile.getFilename(), appConfigBean.isDeleteOld(), appConfigBean.isDeltaFile(), bicFile);

			if (!retrunValue) {
				throw new Exception("Failed to store data into database");
			}

		} catch (Exception e) {
			retrunValue = false;
			LOGGER.error("<processFICorr> : an error has occurred during process FI Correspondent, reason : " + e.getMessage());
		}

		return retrunValue;

	}

	public boolean processCTCorr(BicFile bicFile) {
		List<String> strBuff = null;
		List<Map<String, String>> parsedContent = null;
		boolean retrunValue = false;

		try {
			LOGGER.debug("<processCTCorr> : read file " + bicFile.getFilename());

			strBuff = getServiceLocater().getBicLoaderService().readTxtFile(bicFile.getPath() + "/" + bicFile.getFilename());
			if (strBuff == null) {
				throw new Exception("Failed to read the file");
			}
			LOGGER.debug("<processCTCorr> : parse file " + bicFile.getFilename());

			parsedContent = getServiceLocater().getBicLoaderService().parsingtxtFile(strBuff);
			if (parsedContent == null) {
				throw new Exception("Failed to parse the file");
			}
			if (appConfigBean.isDeleteOld()) {
				LOGGER.debug("<processCTCorr> : delete an old data from the database");

				retrunValue = getServiceLocater().getBicLoaderService().deleteCTCorr();

				if (!retrunValue) {
					throw new Exception("Failed to delete an old data from the database");
				}
			}
			LOGGER.debug("<processCTCorr> : store file " + bicFile.getFilename());

			if (appConfigBean.isDeltaFile()) {
				retrunValue = getServiceLocater().getBicLoaderService().updateCTCorr(parsedContent, bicFile);
			} else {
				retrunValue = getServiceLocater().getBicLoaderService().saveCTCorr(parsedContent, bicFile);
			}

			if (!retrunValue) {
				throw new Exception("Failed to store the data into database");
			}
		} catch (Exception e) {
			retrunValue = false;
			LOGGER.error("<processCTCorr> : an error has occurred during process CT Correspondent, reason : " + e.getMessage());
		}

		return retrunValue;
	}

	public boolean processCUCorr(BicFile bicFile) {
		List<String> strBuff = null;
		List<Map<String, String>> parsedContent = null;
		boolean retrunValue = false;

		try {
			LOGGER.debug("<processCUCorr> : read file " + bicFile.getFilename());

			strBuff = getServiceLocater().getBicLoaderService().readTxtFile(bicFile.getPath() + "/" + bicFile.getFilename());
			if (strBuff == null) {
				throw new Exception("Failed to read the file");
			}
			LOGGER.debug("<processCUCorr> : parse file " + bicFile.getFilename());

			parsedContent = getServiceLocater().getBicLoaderService().parsingtxtFile(strBuff);
			if (parsedContent == null) {
				throw new Exception("Failed to parse the file");
			}
			LOGGER.debug("<processCUCorr> : remove duplicate currency code " + bicFile.getFilename());

			parsedContent = getServiceLocater().getBicLoaderService().checkMapDuplicate(parsedContent, "CURRENCY CODE");
			if (parsedContent == null) {
				throw new Exception("Failed to check and remove the duplicate currency");
			}
			if (appConfigBean.isDeleteOld()) {
				LOGGER.debug("<processCUCorr> : delete an old data from the database");
				retrunValue = getServiceLocater().getBicLoaderService().deleteCUCorr();

				if (!retrunValue) {
					throw new Exception("Failed to delete an old data from the database");
				}
			}
			LOGGER.debug("<processCUCorr> : store file " + bicFile.getFilename());

			if (appConfigBean.isDeltaFile()) {
				retrunValue = getServiceLocater().getBicLoaderService().updateCUCorr(parsedContent, bicFile);
			} else {
				retrunValue = getServiceLocater().getBicLoaderService().saveCUCorr(parsedContent, bicFile);
			}

			if (!retrunValue) {
				throw new Exception("Failed to store the data into database");
			}
		} catch (Exception e) {
			retrunValue = false;
			LOGGER.error("<processCUCorr> : an error has occurred during process CU Correspondent, reason : " + e.getMessage());
		}

		return retrunValue;
	}

	public boolean initProcess(String[] args) {
		try {
			System.out.println(BIC_LOADER_HEADER);

			LOGGER.info("<initProcess> : Passing user parameters into application");

			boolean result = loadParameters(args);
			if (!result) {
				return false;
			}

			LOGGER.info("<initProcess> : Check database parameters");

			String checkParam = validator.checkDBConnection(appConfigBean);
			if (!checkParam.equals("VALID")) {
				displayUsage(checkParam);
				return false;
			}

			displayDBArguments();

			LOGGER.info("<initProcess> : Check mandatory paramaters");

			checkParam = validator.checkMandatoryParam(appConfigBean);
			if (!checkParam.equals("VALID")) {
				displayUsage(checkParam);
				return false;
			}

			LOGGER.info("<initProcess> : Check File Name/directory is correct ");

			checkParam = validator.checkFileExit(appConfigBean);
			if (!checkParam.equals("VALID")) {
				displayUsage(checkParam);
				return false;
			}

			LOGGER.info("<initProcess> : Check file extension is valid");

			init(appConfigBean);

			LOGGER.info("<initProcess> : Initializing BICLoader Service Successfully. ");

			displayAppArguments();

			return true;
		} catch (Exception e) {
			LOGGER.error("<initProcess> : an error has occurred during initialize the process, reason : " + e.getMessage());
		}
		return false;
	}

	public boolean loadParameters(String[] args) {
		try {
			if (args.length <= 0) {
				LOGGER.error("<loadParameters> : Missing parameters");
				displayUsage("");
				return false;
			}

			appConfigBean.setDatabaseType(DBType.ORACLE);
			appConfigBean.setPortNumber("1521");

			for (int i = 0; i < args.length; i++) {
				String value = args[i];

				if (USER.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setUsername(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (PASSWORD.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setPassword(args[i]);
					} else {
						displayUsage(value);
						return false;

					}

				} else if (IP.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setServerName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (ECF.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						LOGGER.debug("<loadParameters> :Parse Encrypted Connection File...");

						ecfFile = args[i];

						ConnectionSettings cs = EnEcfParser.parseECF(ecfFile);

						if (cs.getServerName() != null) {
							appConfigBean.setServerName(cs.getServerName());
						}
						if (cs.getUserName() != null) {
							appConfigBean.setUsername(cs.getUserName());
						}
						if (cs.getPassword() != null) {
							appConfigBean.setPassword(cs.getPassword());
						}
						if (cs.getPortNumber() != null) {
							appConfigBean.setPortNumber(cs.getPortNumber().toString());
						}
						if (cs.getServiceName() != null) {
							appConfigBean.setDatabaseName(cs.getServiceName());
						}
					} else {
						displayUsage(value);
						return false;
					}

				} else if (DBNAME.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setDatabaseName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (INSTANCENAME.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setInstanceName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (SERVICENAME.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setDbServiceName(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (DB_TYPE.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setDatabaseType(args[i].equalsIgnoreCase("mssql") ? DBType.MSSQL : DBType.ORACLE);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (DBPORT.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setPortNumber(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (VERBOSE.equalsIgnoreCase(value)) {
					appConfigBean.setDebug(true);
					Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
					Configurator.setRootLevel(Level.DEBUG);
					LOGGER.info("<loadParameters> : Debug started......");

				} else if (PATH.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setDirectory(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (FILE_NAME.equalsIgnoreCase(value)) {
					if (i++ < args.length) {
						appConfigBean.setFile(args[i]);
					} else {
						displayUsage(value);
						return false;
					}

				} else if (GPI_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setGpiCorr(true);

				} else if (BANK_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setBankFile(true);

				} else if (BIC_DU_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setBicdue(true);

				} else if (BANK_DIRECTORY_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setBankdirectory(true);

				} else if (DELETE_OLD.equalsIgnoreCase(value)) {
					appConfigBean.setDeleteOld(true);

				} else if (AUTO.equalsIgnoreCase(value)) {
					appConfigBean.setAutoFiles(true);

				} else if (DELTA_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setDeltaFile(true);

				} else if (FULL_FILE.equalsIgnoreCase(value)) {
					appConfigBean.setFullFile(true);

				} else {
					displayUsage(value);
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("<loadParameters> : an error has occurred during pass user parameters into application, reason : " + e.getMessage());
			displayUsage("");
		}
		return false;
	}

	public void displayUsage(String value) {

		System.out.printf("\n %s Missing or Invalid parameter\n", value);
		System.out.println("\nUsage : \n");

		System.out.println("  " + USER + "\t\t : Username for database connection");
		System.out.println("  " + PASSWORD + "\t\t : Password for databasae connection");
		System.out.println("  " + IP + "\t\t : The IP address or server name for database connection, default is localhost");
		System.out.println("  " + "[" + ECF + "]" + "\t\t : Encrypted Connection File to connect to database");
		System.out.println("  " + DB_TYPE + "\t : Database Type, oracle or mssql database, default is oracle");
		System.out.println("  " + DBPORT + "\t\t : Database port, default 1521 for oracle and 1433 for mssql");
		System.out.println("  " + DBNAME + "\t : Database name");
		System.out.println("  " + "[" + SERVICENAME + "]" + "\t : Service name");
		System.out.println("  " + "[" + INSTANCENAME + "]" + "\t : instance name");
		System.out.println("  " + FILE_NAME + "\t\t : File name to process");
		System.out.println("  " + AUTO + "\t\t : Automatic mode to process all files found in Directory path");
		System.out.println("  " + PATH + "\t\t : Directory where the files are located");
		System.out.println("  " + FULL_FILE + "\t\t : Process full version for BIC file used only with -auto param for -bicDU or -bankdirectoryplus");
		System.out.println("  " + DELTA_FILE + "\t : Process delta version for BIC file used only with -auto param for -bicDU or -bankdirectoryplus ");

		System.out.println("  " + "[" + DELETE_OLD + "]" + "\t : Flag to delete data from database");
		System.out.println("  " + BANK_FILE + "\t : Process bank file correspondent");
		System.out.println("  " + GPI_FILE + "\t\t : Process gpi Directory");
		System.out.println("  " + BIC_DU_FILE + "\t : Process BICDIR correspondent file");
		System.out.println("  " + BANK_DIRECTORY_FILE + " : Process BankDirectoryPlus or BICPLUS correspondent file");
		System.out.println("  " + "[" + VERBOSE + "]" + "\t : Enable debugging to include more details in the log file. This is used in problem troubleshooting");

	}

	public void displayAppArguments() {
		System.out.println(String.format("%020d", Integer.valueOf(0)).replace("0", "-"));
		System.out.println("File Path\t\t: " + (!appConfigBean.getFile().isEmpty() ? appConfigBean.getFile() : appConfigBean.getDirectory()));

		System.out.println("Debug Mode\t\t: " + (appConfigBean.isDebug() ? "ON" : "OFF"));

		System.out.println(String.format("%020d", Integer.valueOf(0)).replace("0", "-"));
	}

	public void displayDBArguments() {

		System.out.println("Database arguments:");
		System.out.println(String.format("%020d", Integer.valueOf(0)).replace("0", "-"));

		System.out.println("Database Type\t\t: " + (appConfigBean.getDatabaseType().equalsName("Oracle") ? "Oracle" : "Sql Server"));

		if (ecfFile == null) {
			System.out.println("Database server name\t: " + appConfigBean.getServerName());

			if (appConfigBean.getDatabaseType().equalsName("Oracle")) {
				if ((appConfigBean.getDbServiceName() != null) && (!appConfigBean.getDbServiceName().isEmpty())) {
					System.out.println("Database service name\t: " + appConfigBean.getDbServiceName());
				} else {
					System.out.println("Database name\t\t: " + appConfigBean.getDatabaseName());
				}
			} else {
				System.out.println("Database instance name\t: " + appConfigBean.getInstanceName());
			}
			System.out.println("Database port\t\t: " + appConfigBean.getPortNumber());

			System.out.println("Database user name\t: " + appConfigBean.getUsername());

			System.out.println("Database user password\t: " + String.format(new StringBuilder("%0").append(appConfigBean.getPassword().length()).append("d").toString(), Integer.valueOf(0)).replace("0", "*"));
		} else {
			System.out.println("Encrypted connection file\t\t: " + ecfFile);
		}

		System.out.println(String.format("%020d", Integer.valueOf(0)).replace("0", "-"));
	}

	public BICLoaderConfig getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(BICLoaderConfig appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public CommandParamValidator getValidator() {
		return validator;
	}

	public void setValidator(CommandParamValidator validator) {
		this.validator = validator;
	}
}