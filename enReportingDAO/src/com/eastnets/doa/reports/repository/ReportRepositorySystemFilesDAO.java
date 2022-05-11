package com.eastnets.doa.reports.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.reports.repository.DirectoryMetadata;
import com.eastnets.domain.reports.repository.FileMetadata;

public class ReportRepositorySystemFilesDAO extends DAOBaseImp implements
		ReportsRepositoryDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 237044129819339498L;
	private String extensions = "pdf,doc,docx,xls,xlsx,csv";

	@Override
	public synchronized List<DirectoryMetadata> getDirectories(String path) {
		File directory = new File(path);
		File[] files = directory.listFiles();
		List<DirectoryMetadata> directoryMetadataList = new ArrayList<DirectoryMetadata>();
		DirectoryMetadata directoryMetadata;
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					directoryMetadata = new DirectoryMetadata();
					directoryMetadata.setDirectoryLocation(file.getPath());
					directoryMetadata.setDirectoryName(file.getName());
					directoryMetadata.setModificaitonDate(new Date(file
							.lastModified()));
					directoryMetadataList.add(directoryMetadata);
					directoryMetadata.setSubDirectories(getDirectories(file
							.getPath()));
					directoryMetadata.setFiles(getFilesInDirectory(file
							.getPath()));
				}
			}
		}
		return directoryMetadataList;
	}

	@Override
	public List<DirectoryMetadata> getDirectoriesIncludeRoot(String path) {
		File directory = new File(path);
		List<DirectoryMetadata> rootDirectoryList = new ArrayList<DirectoryMetadata>();
		DirectoryMetadata mainDirectory = new DirectoryMetadata();
		mainDirectory.setDirectoryLocation(path);
		mainDirectory.setDirectoryName(directory.getName());
		mainDirectory.setModificaitonDate(new Date(directory.lastModified()));
		mainDirectory.setSubDirectories(getDirectories(path));
		mainDirectory.setFiles(getFilesInDirectory(path));
		rootDirectoryList.add(mainDirectory);
		return rootDirectoryList;
	}

	@Override
	public synchronized List<FileMetadata> getFilesInDirectory(String path) {
		File directory = new File(path);
		File[] files = directory.listFiles();
		List<FileMetadata> fileMetadataList = new ArrayList<FileMetadata>();
		FileMetadata fileMetadata;
		String fileName;
		String extension;
		long fileSize = 0;
		if (files != null) {
			for (File file : files) {
				fileName = file.getName();
				extension = fileName.substring(fileName.indexOf(".") + 1);
				if (isValidExtension(extension)) {
					fileMetadata = new FileMetadata();
					fileMetadata.setFileName(fileName);
					fileMetadata.setFileLocation(file.getPath());
					fileMetadata.setFileExtension(extension);
					fileSize = file.length();
					if (fileSize == 0) {
						fileMetadata.setFileSize("0");
					} else {
						fileMetadata.setFileSize((Math
								.round((fileSize / 1024) + 0.5)) + ""); // in KB
					}
					fileMetadata.setModificationDate(new Date(file
							.lastModified()));
					fileMetadataList.add(fileMetadata);
				}
			}
		}
		return fileMetadataList;

	}

	private boolean isValidExtension(String extension) {
		return extensions.contains(extension);
	}

	public static void main(String args[]) {
		ReportRepositorySystemFilesDAO systemFilesDAO = new ReportRepositorySystemFilesDAO();
		List<DirectoryMetadata> dirs = systemFilesDAO.getDirectories("D:/");
		for (DirectoryMetadata dir : dirs) {
			System.out
					.println("File Location :: " + dir.getDirectoryLocation());
			System.out.println("Name :: " + dir.getDirectoryName());
			System.out.println("Modification Date :: "
					+ dir.getModificaitonDate());
		}

	}

}
