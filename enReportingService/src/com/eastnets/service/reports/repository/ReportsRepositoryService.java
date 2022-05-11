package com.eastnets.service.reports.repository;

import java.util.List;

import com.eastnets.domain.reports.repository.DirectoryMetadata;
import com.eastnets.domain.reports.repository.FileMetadata;

public interface ReportsRepositoryService {

	public List<DirectoryMetadata> getDirectories(String path);

	public List<FileMetadata> getFilesInDirectory(String path);

}
