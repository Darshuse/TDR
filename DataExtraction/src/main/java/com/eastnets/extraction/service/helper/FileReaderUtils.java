package com.eastnets.extraction.service.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastnets.extraction.controller.ExtractorController;

public class FileReaderUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorController.class);

	public static List<String> getContentAsList(String filePath) {

		List<String> fileContent = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("BIC file path not exist!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileContent;
	}

}
