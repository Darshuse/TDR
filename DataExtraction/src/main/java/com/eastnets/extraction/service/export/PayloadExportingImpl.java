package com.eastnets.extraction.service.export;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.extraction.bean.PayloadFile;
import com.eastnets.extraction.bean.PayloadType;
import com.eastnets.extraction.bean.SearchParam;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.StatisticsFile;
import com.eastnets.extraction.dao.search.SearchDAO;
import com.eastnets.extraction.service.helper.FileWriterUtils;

@Service
public class PayloadExportingImpl implements ExportingService {

	@Autowired
	private SearchDAO searchDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(PayloadExportingImpl.class);

	@Override
	public void exportMessages(List<SearchResult> searchResult, SearchParam searchParam, StatisticsFile statisticsFile) {

		List<String> lines = new ArrayList<String>();
		for (SearchResult result : searchResult) {
			if (result.getMesgFrmtName().equalsIgnoreCase("file")) {

				try {
					PayloadFile payLoadFile = getPayloadFile(result.getAid().toString(), result.getMesgUmidl().toString(), result.getMesgUmidh().toString(), result.getMesgCreaDateTime(),
							searchDAO.getMessageFile(result.getAid(), result.getMesgUmidl(), result.getMesgUmidh(), result.getMesgCreaDateTime()).getPayloadType());

					if (payLoadFile.getFileText() != null && payLoadFile.getFileText().length() > 0) {

						lines.add(XMLExportingImpl.xmlFormatter(payLoadFile.getFileText()));
						statisticsFile.setNumberofGeneratedFile(statisticsFile.getNumberofGeneratedFile() + 1);

					} else {
						LOGGER.warn("The PayLoad File is empty.");
						statisticsFile.setNumberOfMessagesThatHaveErrors(statisticsFile.getNumberOfMessagesThatHaveErrors() + 1);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		FileWriterUtils.generateFile(lines, searchParam, "txt", statisticsFile);

	}

	public PayloadFile getPayloadFile(String aid, String umidl, String umidh, Date creation_date_time, PayloadType payloadType) throws Exception {
		if (payloadType == null || payloadType.isNotLoaded()) {
			return new PayloadFile();
		}
		if (payloadType.isBinary()) {
			return searchDAO.getPayloadFile(aid, umidl, umidh, creation_date_time);
		}
		return searchDAO.getPayloadFileText(aid, umidl, umidh, creation_date_time);

	}
}
