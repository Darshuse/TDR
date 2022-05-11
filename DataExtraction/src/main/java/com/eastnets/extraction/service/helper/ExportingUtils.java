package com.eastnets.extraction.service.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.MessageDetails;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.config.YAMLConfig;

@Service
public class ExportingUtils {

	@Autowired
	private YAMLConfig config;

	public String buildFileName(String fileName, String extension) {
		fileName = fileName + extension;
		fileName = fileName.replace("/", "-");
		fileName = fileName.replace("\\", "-");
		fileName = fileName.replace(":", "-");
		fileName = fileName.replace(" ", "_");

		return fileName;

	}

	public String buildCompsiteKeyString(List<SearchResult> searchResult) {
		StringBuilder compsiteKey = new StringBuilder();
		if (searchResult != null & !searchResult.isEmpty()) {
			if (config.isPartitioned()) {
				searchResult.forEach((event) -> {
					compsiteKey.append('(').append(event.getAid()).append(',').append((event.getMesgUmidl())).append(',').append(event.getMesgUmidh()).append(',').append(event.getDateTimeForQuery()).append("),");
				});
			} else {
				searchResult.forEach((event) -> {
					compsiteKey.append('(').append(event.getAid()).append(',').append((event.getMesgUmidl())).append(',').append(event.getMesgUmidh()).append("),");
				});
			}
			return compsiteKey.substring(0, compsiteKey.length() - 1);
		}
		return "";
	}

	public String buildCompsiteKeyForMessageDetails(List<MessageDetails> messageDetails) {
		StringBuilder compsiteKey = new StringBuilder();
		if (messageDetails != null & !messageDetails.isEmpty()) {
			if (config.isPartitioned()) {
				messageDetails.forEach((event) -> {
					compsiteKey.append('(').append(event.getAid()).append(',').append((event.getMesgUmidl())).append(',').append(event.getMesgUmidh()).append(',').append(event.getDateTimeForQuery()).append("),");
				});
			} else {
				messageDetails.forEach((event) -> {
					compsiteKey.append('(').append(event.getAid()).append(',').append((event.getMesgUmidl())).append(',').append(event.getMesgUmidh()).append("),");
				});
			}
			return compsiteKey.substring(0, compsiteKey.length() - 1);
		}
		return "";
	}
}
