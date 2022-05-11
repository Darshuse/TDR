package com.eastnets.doa.reports.repository;

import java.util.List;

import com.eastnets.dao.DAO;
import com.eastnets.domain.reports.repository.DirectoryMetadata;
import com.eastnets.domain.reports.repository.FileMetadata;

public interface ReportsRepositoryDAO extends DAO  {

	public List<DirectoryMetadata> getDirectories(String path);

	public List<FileMetadata> getFilesInDirectory(String path);
	
	public List<DirectoryMetadata> getDirectoriesIncludeRoot(String path);

}
