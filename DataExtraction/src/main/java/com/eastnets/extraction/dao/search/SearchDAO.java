package com.eastnets.extraction.dao.search;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eastnets.extraction.bean.AllianceInstance;
import com.eastnets.extraction.bean.AppendixDetails;
import com.eastnets.extraction.bean.AppendixExtDetails;
import com.eastnets.extraction.bean.CorrInfo;
import com.eastnets.extraction.bean.FileAct;
import com.eastnets.extraction.bean.InstanceDetails;
import com.eastnets.extraction.bean.InstanceTransmissionPrintInfo;
import com.eastnets.extraction.bean.InterventionDetails;
import com.eastnets.extraction.bean.MessageDetails;
import com.eastnets.extraction.bean.PayloadFile;
import com.eastnets.extraction.bean.SearchResult;
import com.eastnets.extraction.bean.TextFieldData;
import com.eastnets.extraction.service.helper.Pair;

@Component
public interface SearchDAO {
	public List<SearchResult> execSearchQuery(String query);

	public List<MessageDetails> getMessageDetails(String compsiteKeyString, boolean enableDebugFull) throws Exception;

	public List<InstanceDetails> getInstanceList(String compsiteKeyString);

	public List<TextFieldData> getTextFieldData(String compsiteKeyString);

	public List<TextFieldData> getSystemTextFieldData(String compsiteKeyString);

	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, Date mesg_crea_date);

	public Pair<String, String> getMTExpantion(int aid, int umidl, int umidh, Date mesg_crea_date, String mesgType);

	public String getExpandedMesssageText(Integer aid, Integer umidl, Integer umidh, String syntaxVersion, String messageType, String unexpandedText, Date messageDate, String thousandAmountFormat, String decimalAmountFormat) throws SQLException;

	public List<AppendixDetails> getAppendixList(String compsiteKeyString);

	public AppendixExtDetails getAppendixDetails(int aid, int umidl, int umidh, Date mesg_crea_date, int inst_num, Long intv_seq_num, Date intv_date_time, final int timeZoneOffset);

	public CorrInfo getCorrInfo(CorrInfo corr);

	public CorrInfo getBicInfoStr(String bic);

	public InstanceTransmissionPrintInfo getInstanceTransmissionPrintInfo(int aid, int umidl, int umidh, java.util.Date mesg_crea_date, int timeZoneOffset);

	public List<String> isBeingUpdated(String compsiteKeyString);

	public List<InterventionDetails> getInterventionList(String compsiteKeyString);

	public PayloadFile getPayloadFile(String aid, String umidl, String umidh, Date creation_date_time) throws Exception;

	public PayloadFile getPayloadFileText(String aid, String umidl, String umidh, Date creation_date_time) throws Exception;

	public FileAct getMessageFile(int aid, int umidl, int umidh, Date creation_date_time);

	public List<Map<String, Object>> execSearchQueryAsResultSet(String query);

	public void execUpdateQuery(String query);

	public Date getDateOfPreviousExtractedMessage();

	public List<AllianceInstance> CashAllianceInstanceByAid();

}
