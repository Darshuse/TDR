package com.eastnets.dao.loader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.eastnets.config.DBType;
import com.eastnets.dao.DAO;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageStatus;
import com.eastnets.domain.loader.TextPart;
import com.eastnets.domain.monitoring.UpdatedMessage;

/**
 * @author MKassab
 * 
 */
public interface LoaderDAO extends DAO {

	/**
	 * @return list of Message format from table ldHelperMesgFormat
	 */
	public List<String> getMesgFormat();

	/**
	 * @param mesgFormat
	 * 
	 *            add mesgFormat to ldHelperMesgFormat
	 */
	public void addMesgFormat(String mesgFormat);

	/**
	 * @return list of message Nature from table ldHelperMesgNature
	 */
	public List<String> getMesgNature();

	/**
	 * @param mesgNautre
	 * 
	 *            add mesgNautre to table ldHelperMesgNature
	 */
	public void addMesgNature(String mesgNautre);

	/**
	 * @return list of message Qualifiers from table ldHelperMesgQualifier
	 */
	public List<String> getMesgQualifier();

	/**
	 * @param mesgQualifier
	 * 
	 *            add mesgQualifier to table ldHelperMesgQualifier
	 */
	public void addMesgQualifier(String mesgQualifier);

	/**
	 * @return list of message unit name from table ldHelperUnitName
	 */
	public List<String> getMesgUnitName();

	/**
	 * @param mesgNautre
	 * 
	 *            add unitName to table ldHelperUnitName
	 */
	public void addMesgUnitName(String unitName);

	/**
	 * @return list of message RP name ( routing point) from table ldHelperRPName
	 */
	public List<String> getMesgRPName();

	/**
	 * @param mesgNautre
	 * 
	 *            add unitName to table ldHelperRPName
	 */
	public void addMesgRPName(String rpName);

	/**
	 * 
	 * @param ids
	 *            these ids well be converted to Processing rows and inserted on DB in batch update
	 */
	public void addProcessingRows(List<BigDecimal> ids, String aid);

	/**
	 * 
	 * @return
	 */
	public Long getLastSequenceMqProcessing();

	/**
	 * 
	 * @param processingData
	 */
	public void addProcessingMQRows(List<LoaderMessage> messagesList, String aid);

	/**
	 * change the status of the processing row
	 */
	public void updateMessageStatus(BigDecimal id, MessageStatus status, String aid);

	/**
	 * 
	 * @return the last processed row before shutdown like the last processed number,UMIDL, and UMIDH.The last processed row well be wrapped inside LoaderConnection Class and well be updated
	 *         frequently after each successful insertion of data
	 */
	public LoaderConnection getLastLoaderConnection();

	public void removeDatabaseSourcedProcessingRows(List<LoaderMessage> list, String aid);

	public void removeMQSourcedProcessingRows(List<LoaderMessage> list, String aid, Long lastId);

	/**
	 * 
	 * Update the last processed rows , this method usually will be called after reading from the source and persist the reading rows in processed target (for instance the target is DB table)
	 * 
	 * @param loaderConnection
	 * 
	 */
	public void updateLastProcessedRow(LoaderConnection loaderConnection, Connection lockCon) throws Exception;

	/**
	 * 
	 * This method well try to see if there is any rows in processing table, usually this table may have rows if the loader crashed or if the user decide to kill the loader process
	 * 
	 * @return list of Ids that still exist in processing table
	 * 
	 */
	public List<BigDecimal> restoreProcessingRows(String aid);

	/**
	 * 
	 * This method well try to see if there is any rows in processing table, usually this table may have rows if the loader crashed or if the user decide to kill the loader process
	 * 
	 * @return list of loader messages that still exist in processing table
	 * 
	 */
	public List<LoaderMessage> restoreMQProcessingRows(String aid);

	/**
	 * log in a statistics table how many rows was persisted per every predefined period
	 * 
	 * @param message
	 *            statistic details
	 */
	public void addNewStatistics(UpdatedMessage message, DBType dbType);

	/**
	 * Check weather if the database load is saving data to is partitioned
	 * 
	 * @return
	 */
	public ArrayList<String> getXSDIdentifier();

	public boolean isPartitionedDatabase();

	public BigDecimal getLastSeq();

	public void updateLastProcessedMQRow(LoaderConnection loaderConnection);

	public void insertIntoErrorTabel(String message);

	public void updateLastProcessedRow(LoaderConnection loaderConnection);

	public void updateMessageStatusForMQ(BigDecimal id, MessageStatus status, String aid);

	public void insertIntoErrorld(String errExeName, String errlevel, String errmodule, String errMsg1, String errMsg2);

	public void updateTest(String xml, long umidl);

	public void insertIntoRtext(TextPart text, boolean isPartDB);

	public long getMxseqAppeRecordId();

}
