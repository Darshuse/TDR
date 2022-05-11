package com.eastnets.service.loader.loaderReaderServiceDelegates;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.eastnets.cluster.ClusterManager;
import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.MesgPK;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.enReportingLoader.config.AppConfigBean;
import com.eastnets.service.loader.LoaderServiceImp;

/**
 * Class created to contain all database insertion/restore operations from our
 * database in case of reading from client database or MQ
 * 
 * @author OAlKhalili
 *
 */
public class QueueMessagesDelegate {

	private static final Logger LOGGER = Logger.getLogger(LoaderServiceImp.class);

	public enum INSERTION_MODE {
		Normal, Restore, NormalWithData, RestoreWithData
	}

	private LoaderDAO loaderDAO;

	private AppConfigBean appConfigBean;

	/*
	 * No need to fetch the last sequence no; since it's not unique , this
	 * sequence no well be filled in UMIDL
	 */
	private long lastSeqNo = -1;
	private final String lastSeqNoCounterName = "lastSeqNo";

	int mesgCount = 0;

	/*
	 * Well be used to fill UMIDL
	 */
	private static final DateFormat dateF = new SimpleDateFormat("yyMMddS");
	private Date date;
	// loader settings
	private LoaderConnection loaderConnection;
	private Connection lockCon;
	private String aid;
	private BlockingQueue<LoaderMessage> inputBlockingQueue;
	private BlockingQueue<LoaderMessage> outputBlockingQueue;

	private ClusterManager clusterManager;

	public void init(String aid, BlockingQueue<LoaderMessage> inputBlockingQueue,
			BlockingQueue<LoaderMessage> outputBlockingQueue) {
		LOGGER.info("Start initiating for Loader Readers");
		LOGGER.info("AID loaded from Config :: " + aid); 
		loaderConnection = loaderDAO.getLastLoaderConnection();
		if(appConfigBean.isClusteringEnabled()){
			if (clusterManager.isFirstMember()) {
				// load last processed row 
				lastSeqNo = loaderConnection.getLastUmidh() - 1; 
			} 
			clusterManager.createClusterCounterIfNull(lastSeqNoCounterName, lastSeqNo); 
		}
		else{
			lastSeqNo = loaderConnection.getLastUmidh() - 1;

		}

		this.aid = aid;
		this.inputBlockingQueue = inputBlockingQueue;
		this.outputBlockingQueue = outputBlockingQueue;
	}

	/**
	 * 
	 * @param messagesList
	 * @param mode
	 *            Normal|Restore|NormalWithData|RestoreWithData Process Mode
	 * @return
	 */
	public int queueMessage(LoaderMessage loaderMessage, String mode) {
		ArrayList<LoaderMessage> oneitemList = new ArrayList<LoaderMessage>();
		oneitemList.add(loaderMessage);
		loaderConnection = loaderDAO.getLastLoaderConnection();
		return queueMessages(oneitemList, mode,lockCon);
	}

	/**
	 * 
	 * @param messagesList
	 * @param mode
	 *            Normal|Restore|NormalWithData|RestoreWithData Process Mode
	 * @return
	 */
	public int queueMessages(List<LoaderMessage> messagesList, String mode, Connection lockCon) {

		List<BigDecimal> messagesIds = new ArrayList<BigDecimal>();

		for (LoaderMessage msg : messagesList) {
			mesgCount++;
			try {
				messagesIds.add(msg.getMessageSequenceNo());
				preapareForQueueingMessage(msg);
				// TODO:: see which is better for performance to add them bulk
				// on queue (addall) or like current implementation one by one 
			} catch (Exception e) {
				String errMessage="Failed";
				if(e.getMessage() != null){
					errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
				}
				

				if(Main.isDbReader()){
					loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

				}else{
					loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

				}
				e.printStackTrace();
			}
		}

		if (mode.equals("Normal")) {
			loaderDAO.addProcessingRows(messagesIds, appConfigBean.getSAAAid());
			if (!messagesList.isEmpty()) {
				LoaderMessage loaderMessage = messagesList.get(messagesList.size() - 1); 
				loaderConnection.setLastProcessedSequenceNo(loaderMessage.getMessageSequenceNo());
				loaderConnection.setLastUmidh(loaderMessage.getMesgId().getMesgSUmidh());
				loaderConnection.setLastUmidl(loaderMessage.getMesgId().getMesgSUmidl());
				//update the last updated row
				try { 
					loaderDAO.updateLastProcessedRow(loaderConnection);
				} catch (Exception e) {
					String errMessage="Failed";
					if(e.getMessage() != null){
						errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
					}
					

					if(Main.isDbReader()){
						loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");		

					}else{
						loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

					}
					e.printStackTrace();
				}
			}
		} else if (mode.equals("NormalWithData")  ) {
			loaderDAO.addProcessingMQRows(messagesList, appConfigBean.getSAAAid()); 
			if (!messagesList.isEmpty()) {
				LoaderMessage loaderMessage = messagesList.get(messagesList.size() - 1);
				loaderConnection.setLastUmidh(loaderMessage.getMesgId().getMesgSUmidh());
				loaderConnection.setLastUmidl(loaderMessage.getMesgId().getMesgSUmidl());
				//update the last updated row
				try { 
					loaderDAO.updateLastProcessedMQRow(loaderConnection);
				} catch (Exception e) {
					String errMessage="Failed";
					if(e.getMessage() != null){
						errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
					}
					

					if(Main.isDbReader()){
						loaderDAO.insertIntoErrorld("DB Connector", "Failed", "",errMessage, "");		

					}else{
						loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

					}
					e.printStackTrace();
				}
			}
		}
		else if(mode.equals("RestoreWithData") ){
			if (!messagesList.isEmpty()) {
				LoaderMessage loaderMessage = messagesList.get(messagesList.size() - 1);
				loaderConnection.setLastUmidh(loaderMessage.getMesgId().getMesgSUmidh());
				loaderConnection.setLastUmidl(loaderMessage.getMesgId().getMesgSUmidl());
				//update the last updated row
				try { 
					loaderDAO.updateLastProcessedMQRow(loaderConnection);
				} catch (Exception e) {
					String errMessage="Failed";
					if(e.getMessage() != null){
						errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
					}
					

					if(Main.isDbReader()){
						loaderDAO.insertIntoErrorld("DB Connector", "Failed", "",errMessage, "");		

					}else{
						loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");			

					}
					e.printStackTrace();
				}
			}
			
			
		}
		
		
		
		
		for (LoaderMessage msg : messagesList) { 
			try { 
				outputBlockingQueue.put(msg);
			} catch (InterruptedException e) {
				String errMessage="Failed";
				if(e.getMessage() != null){
					errMessage= (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
				}
				

				if(Main.isDbReader()){
					loaderDAO.insertIntoErrorld("DB Connector", "Failed", "",errMessage, "");		

				}else{
					loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "",errMessage, "");			

				}
				e.printStackTrace();
			}
		}

		
		if(mode.equals("NormalWithData") || mode.equals("RestoreWithData") ){

			LOGGER.info("Read " + messagesList.size() + " messages from MQ.");	

		}else{ 
			LOGGER.info("Read " + messagesList.size() + " messages from DB.");
		} 
		return mesgCount;
	}

	private void preapareForQueueingMessage(LoaderMessage message) {
		MesgPK mesgId = new MesgPK();
		mesgId.setAid(Long.parseLong(appConfigBean.getSAAAid()));
		date = new Date();
		String dateTimeF = dateF.format(date);
		// set last umidh and decrement the sequence by one
		mesgId.setMesgSUmidh(clusterManager.getClusterCounterNextValue(lastSeqNoCounterName,lastSeqNo--));
		mesgId.setMesgSUmidl(Long.parseLong(dateTimeF));
		message.setMesgId(mesgId);
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public BlockingQueue<LoaderMessage> getInputBlockingQueue() {
		return inputBlockingQueue;
	}

	public void setInputBlockingQueue(BlockingQueue<LoaderMessage> inputBlockingQueue) {
		this.inputBlockingQueue = inputBlockingQueue;
	}

	public BlockingQueue<LoaderMessage> getOutputBlockingQueue() {
		return outputBlockingQueue;
	}

	public void setOutputBlockingQueue(BlockingQueue<LoaderMessage> outputBlockingQueue) {
		this.outputBlockingQueue = outputBlockingQueue;
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	public void setClusterManager(ClusterManager clusterManager) {
		this.clusterManager = clusterManager;
	}

	public String getLastSeqNoCounterName() {
		return lastSeqNoCounterName;
	}

	public AppConfigBean getAppConfigBean() {
		return appConfigBean;
	}

	public void setAppConfigBean(AppConfigBean appConfigBean) {
		this.appConfigBean = appConfigBean;
	}

	public Connection getLockCon() {
		return lockCon;
	}

	public void setLockCon(Connection lockCon) {
		this.lockCon = lockCon;
	}

}