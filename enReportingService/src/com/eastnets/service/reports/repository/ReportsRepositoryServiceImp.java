package com.eastnets.service.reports.repository;

import java.util.List;

import com.eastnets.doa.reports.repository.ReportsRepositoryDAO;
import com.eastnets.domain.reports.repository.DirectoryMetadata;
import com.eastnets.domain.reports.repository.FileMetadata;
import com.eastnets.service.ServiceBaseImp;

public class ReportsRepositoryServiceImp extends ServiceBaseImp implements ReportsRepositoryService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5295684208549298060L;
	private ReportsRepositoryDAO reportsRepository;

	@Override
	public List<DirectoryMetadata> getDirectories(String path) {
		return reportsRepository.getDirectoriesIncludeRoot(path);
	}

	@Override
	public List<FileMetadata> getFilesInDirectory(String path) {
		return reportsRepository.getFilesInDirectory(path);
	}

	public ReportsRepositoryDAO getReportsRepository() {
		return reportsRepository;
	}

	public void setReportsRepository(ReportsRepositoryDAO reportsRepository) {
		this.reportsRepository = reportsRepository;
	}

	
	
}
