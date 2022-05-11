package com.eastnets.extraction.service.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.StatisticsFile;

public class FileWriterUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWriterUtils.class);

	private static void writeRJEData(double dataSize, String dataBlock, SearchParam searchParam, StatisticsFile statisticsFile) {

		String filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getRjeFileName();
		if (searchParam.getGeneratedFilesData().getRjeFilesCounter() != 0) {
			filePath = filePath + searchParam.getGeneratedFilesData().getRjeFilesCounter();
		}
		filePath = filePath + ".rje";

		if (isFileExist(filePath)) {
			if (getFileSizeMegaBytes(filePath) + dataSize + 0.0011 <= searchParam.getFileSize() || searchParam.getFileSize() == 0) {

				try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
					out.println(dataBlock);
				} catch (IOException e) {
					LOGGER.error("Error in writeRJEData " + e.getMessage(), e);
				}
			} else {
				searchParam.getGeneratedFilesData().setRjeFilesCounter(searchParam.getGeneratedFilesData().getRjeFilesCounter() + 1);
				writeRJEData(dataSize, dataBlock, searchParam, statisticsFile);
			}
		} else {
			try {
				new FileOutputStream(filePath, true).close();
				statisticsFile.addGeneratedFileName(filePath);
				writeRJEData(dataSize, dataBlock, searchParam, statisticsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void writeExitPointData(double dataSize, String dataBlock, SearchParam searchParam, StatisticsFile statisticsFile) {

		String filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getExitPointFileName();
		if (searchParam.getGeneratedFilesData().getExitPointFilesCounter() != 0) {
			filePath = filePath + searchParam.getGeneratedFilesData().getExitPointFilesCounter();
		}
		filePath = filePath + ".txt";

		if (isFileExist(filePath)) {
			if (getFileSizeMegaBytes(filePath) + dataSize + 0.0011 <= searchParam.getFileSize() || searchParam.getFileSize() == 0) {

				try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
					out.println(dataBlock);
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
			} else {
				searchParam.getGeneratedFilesData().setExitPointFilesCounter(searchParam.getGeneratedFilesData().getExitPointFilesCounter() + 1);
				writeExitPointData(dataSize, dataBlock, searchParam, statisticsFile);
			}
		} else {
			try {
				new FileOutputStream(filePath, true).close();
				statisticsFile.addGeneratedFileName(filePath);
				writeExitPointData(dataSize, dataBlock, searchParam, statisticsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void writeMXData(double dataSize, String dataBlock, SearchParam searchParam, boolean newTransaction, boolean newFile, StatisticsFile statisticsFile) {

		String filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getMxFileName();

		if (searchParam.getGeneratedFilesData().getMxFilesCounter() != 0) {
			filePath = filePath + searchParam.getGeneratedFilesData().getMxFilesCounter();
		}
		filePath = filePath + ".xml";

		if (isFileExist(filePath)) {
			if (newTransaction && getFileSizeMegaBytes(filePath) > 0) {
				removeLastLine(filePath);
				newTransaction = false;
			}

			if (getFileSizeMegaBytes(filePath) + dataSize + 0.0011 <= searchParam.getFileSize() || searchParam.getFileSize() == 0) {

				try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
					if (newFile) {
						out.println("<Messages>");
					}
					out.println(dataBlock);
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
			} else {
				closeXMLTag(filePath, searchParam);
				searchParam.getGeneratedFilesData().setMxFilesCounter(searchParam.getGeneratedFilesData().getMxFilesCounter() + 1);
				writeMXData(dataSize, dataBlock, searchParam, newTransaction, false, statisticsFile);
			}
		} else {
			try {
				new FileOutputStream(filePath, true).close();
				statisticsFile.addGeneratedFileName(filePath);
				writeMXData(dataSize, dataBlock, searchParam, newTransaction, true, statisticsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void writeXMLData(double dataSize, String dataBlock, SearchParam searchParam, boolean newTransaction, boolean newFile, StatisticsFile statisticsFile) {

		String filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getXmlFileName();

		if (searchParam.getGeneratedFilesData().getXmlFilesCounter() != 0) {
			filePath = filePath + searchParam.getGeneratedFilesData().getXmlFilesCounter();
		}
		filePath = filePath + ".xml";

		if (isFileExist(filePath)) {
			if (newTransaction && getFileSizeMegaBytes(filePath) > 0) {
				removeLastLine(filePath);
				newTransaction = false;
			}

			if (getFileSizeMegaBytes(filePath) + dataSize + 0.0011 <= searchParam.getFileSize() || searchParam.getFileSize() == 0) {

				try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
					if (newFile) {
						if (searchParam.getSource().equalsIgnoreCase("EXTRACT_FILE_VIEW")) {
							out.println("<Files>");
						} else {
							out.println("<Messages>");
						}
					}
					out.println(dataBlock);
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
			} else {
				closeXMLTag(filePath, searchParam);
				searchParam.getGeneratedFilesData().setXmlFilesCounter(searchParam.getGeneratedFilesData().getXmlFilesCounter() + 1);
				writeXMLData(dataSize, dataBlock, searchParam, newTransaction, false, statisticsFile);
			}
		} else {
			try {
				new FileOutputStream(filePath, true).close();
				statisticsFile.addGeneratedFileName(filePath);
				writeXMLData(dataSize, dataBlock, searchParam, newTransaction, true, statisticsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void removeLastLine(String filePath) {
		RandomAccessFile f;
		try {
			f = new RandomAccessFile(filePath, "rw");
			long length = f.length() - 1;
			byte b;
			do {
				length -= 1;
				f.seek(length);
				b = f.readByte();
			} while (b != 10 && length > 0);
			f.setLength(length + 1);
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateFile(List<String> content, SearchParam searchParam, String extension, StatisticsFile statisticsFile) {

		boolean newTransaction = true;

		for (String partOfTheContent : content) {
			if (extension.equalsIgnoreCase("rje")) {
				writeRJEData(getStringSizeInMB(partOfTheContent), partOfTheContent, searchParam, statisticsFile);
			} else if (extension.equalsIgnoreCase("txt")) {
				writeExitPointData(getStringSizeInMB(partOfTheContent), partOfTheContent, searchParam, statisticsFile);
			} else if (extension.equalsIgnoreCase("xml")) {
				writeMXData(getStringSizeInMB(partOfTheContent), partOfTheContent, searchParam, newTransaction, false, statisticsFile);
			} else if (extension.equalsIgnoreCase(".xml")) {
				writeXMLData(getStringSizeInMB(partOfTheContent), partOfTheContent, searchParam, newTransaction, false, statisticsFile);
			}
			newTransaction = false;
		}

		if (extension.equalsIgnoreCase("xml") || extension.equalsIgnoreCase(".xml")) {

			String filePath;
			if (extension.equalsIgnoreCase("xml")) {
				filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getMxFileName();
			} else {
				filePath = searchParam.getFilePath() + File.separator + searchParam.getGeneratedFilesData().getXmlFileName();
			}

			if (extension.equalsIgnoreCase("xml")) {
				if (searchParam.getGeneratedFilesData().getMxFilesCounter() != 0) {
					filePath = filePath + searchParam.getGeneratedFilesData().getMxFilesCounter();
				}
			} else {
				if (searchParam.getGeneratedFilesData().getXmlFilesCounter() != 0) {
					filePath = filePath + searchParam.getGeneratedFilesData().getXmlFilesCounter();
				}
			}

			if (extension.equalsIgnoreCase("xml")) {
				filePath = filePath + ".xml";
			} else {
				filePath = filePath + ".xml";
			}
			closeXMLTag(filePath, searchParam); // check when the fileCounter increased. > 0
		}

	}

	private static void closeXMLTag(String filePath, SearchParam searchParam) {
		try (FileWriter fw = new FileWriter(filePath, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
			if (searchParam.getSource().equalsIgnoreCase("EXTRACT_FILE_VIEW")) {
				out.println("</Files>");
			} else {
				out.println("</Messages>");
			}
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	private static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		boolean exists = file.exists();
		return exists;
	}

	private static double getFileSizeMegaBytes(String filePath) {
		File file = new File(filePath);
		return (double) file.length() / (1024 * 1024);
	}

	private static double getStringSizeInMB(String messageContent) {
		return ((messageContent.length() / 1024.0) / 1024.0);
	}

	public static void writeStatisticsFile(String filePath, List<String> statisticsData) {

		String file = filePath + File.separator + "Statistics_" + FileNameUtils.getFormatedFileName("%Y_%M_%D") + ".txt";

		if (isFileExist(file)) {
			try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {

				for (String data : statisticsData) {
					out.println(data);
				}
			} catch (IOException e) {
				LOGGER.error("Statistic file did not get generated: " + e.getMessage());
			}

			LOGGER.info("Statistics file generator finished.");

		} else {

			try {
				new FileOutputStream(file, true).close();
				writeStatisticsFile(filePath, statisticsData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
