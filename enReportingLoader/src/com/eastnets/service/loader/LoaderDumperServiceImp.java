package com.eastnets.service.loader;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.log4j.Logger;

import com.eastnets.dao.filepayload.procedure.InsertXmlTexMessage;
import com.eastnets.dao.license.LicenseDAO;
import com.eastnets.dao.loader.LoaderDAO;
import com.eastnets.domain.filepayload.FilePayload;
import com.eastnets.domain.license.BicLicenseInfo;
import com.eastnets.domain.loader.AppePart;
import com.eastnets.domain.loader.InstPart;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.LoaderMessage.MessageSource;
import com.eastnets.domain.loader.LoaderMessage.MessageStatus;
import com.eastnets.domain.loader.Mesg;
import com.eastnets.domain.loader.RintvPart;
import com.eastnets.domain.loader.TextPK;
import com.eastnets.domain.loader.TextPart;
import com.eastnets.domain.loader.XmlTextMessage;
import com.eastnets.domain.loader.XmlTextPK;
import com.eastnets.enReportingLoader.Main;
import com.eastnets.service.loader.helper.DataSourceParser;
import com.eastnets.service.loader.helper.JPAUtil;
import com.eastnets.service.loader.helper.XMLDataSouceHelper;
import com.eastnets.service.loader.helper.jms.JMSSender;
import com.eastnets.service.loader.loaderReaderServiceDelegates.UnqueueMessagesDelegate;

/**
 * @author MKassab
 * 
 */
public class LoaderDumperServiceImp extends LoaderServiceImp implements Runnable {
	private int sleepTime = 5000;
	private int batchsize = 1000;

	private List<Observer> observerList;

	private LoaderDAO loaderDAO;

	private LicenseDAO licenseDAO;

	private List<BicLicenseInfo> bicInfoList;

	private InsertXmlTexMessage insertXmlMsg;
	private JMSSender moveToErrorQSender;

	private XMLDataSouceHelper xmlDataSouceHelper;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager manager;
	private boolean isPartDb;

	private static final Logger LOGGER = Logger.getLogger(LoaderDumperServiceImp.class);

	private UnqueueMessagesDelegate unqueueMessagesDelegate;

	protected EntityManager initJPAManager() {
		LOGGER.debug("Entity Manager Factory Initialized");
		manager = entityManagerFactory.createEntityManager();
		return manager;
	}

	private void closeJPAManager() {
		if (manager != null && manager.isOpen()) {
			manager.close();
			LOGGER.info("Entity Manager has been closed");
		}
		if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}

	@Override
	public void run() {
		LoaderMessage mesg;
		if (appConfigBean.getEnableDebugMode()) {
			batchsize = 1;
		}
		List<LoaderMessage> list = new ArrayList<LoaderMessage>();
		LOGGER.info("Starting Dumping Service");
		while (true) {
			try {
				list.clear();
				for (int i = 0; i < batchsize; i++) {
					mesg = inputBlockingQueue.poll(0, TimeUnit.SECONDS);
					if (mesg == null) {
						break;
					}
					// System.out.print(" :: "+mesg.getMessageSequenceNo()+":: ");
					list.add(mesg);
				}
				if (list.size() > 0) {
					// System.out.println();
					persistLoaderMessage(list);
				} else {
					Thread.sleep(sleepTime);
				}

				if (Thread.interrupted()) {
					closeJPAManager();
					throw new InterruptedException();
				}

			} catch (InterruptedException e) {
				String errMessage = "Failed";
				if (e.getMessage() != null) {
					errMessage = (e.getMessage().length() >= 200) ? e.getMessage().substring(0, 199) : e.getMessage();
				}
				if (Main.isDbReader()) {
					loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessage, "");

				} else {
					loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessage, "");

				}

				closeJPAManager();
				e.printStackTrace();
			}
		}
	}

	private void moveToErrorQueue(String message) {
		if (message != null) {
			boolean sentSuccessfully = moveToErrorQSender.sendMesage(message);
			if (sentSuccessfully) {
				LOGGER.info("Message Moved To Error Queue");
			}
		}
	}

	private void persistLoaderMessage(List<LoaderMessage> list) {

		boolean insertText = true;
		EntityTransaction entityTransaction = manager.getTransaction();
		entityTransaction.begin();
		// Prepare a Messages Ids that eligible for removing from processing table
		List<List<LoaderMessage>> tempLoderList2 = new ArrayList<List<LoaderMessage>>();
		List<LoaderMessage> tempLoderList = new ArrayList<LoaderMessage>();
		for (LoaderMessage loaderMessage : list) {
			try {
				// //check License BIC
				if (!loaderMessage.isMessageIsCancelled()) {
					if (!isLicensedBIc(loaderMessage.getMesg().getxOwnLt())) {
						LOGGER.error(String.format("Message ID ::%s has a unlicensed BIC :: %s ", loaderMessage.getMessageSequenceNo().toPlainString(), loaderMessage.getMesg().getxOwnLt()));
						String errMessageText = (loaderMessage.getRowData().length() >= 1000) ? loaderMessage.getRowData().substring(0, 999) : loaderMessage.getRowData();
						if (loaderMessage.getMessageSource().equals(MessageSource.XML)) {
							loaderDAO.updateMessageStatusForMQ(loaderMessage.getMessageSequenceNo(), MessageStatus.UNLICENSED, appConfigBean.getSAAAid());
							loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "UNLICENSED Message", errMessageText, "");
						} else {

							loaderDAO.updateMessageStatus(loaderMessage.getMessageSequenceNo(), MessageStatus.UNLICENSED, appConfigBean.getSAAAid());
							loaderDAO.insertIntoErrorld("DB Connector", "Failed", "UNLICENSED Message", errMessageText, "");

						}
						continue;// don't persist mesg and don't remove it from processing rows
					}
				}

				prepareDataForPartitionDatabase(loaderMessage.getMesg());
				if (loaderMessage.getMesg().getMesgFrmtName().equalsIgnoreCase("File") || loaderMessage.getMesgType().equalsIgnoreCase("AMH") || loaderMessage.getMesg().getMesgFrmtName().equalsIgnoreCase("Internal")) {
					tempLoderList = prepareXMLText(loaderMessage.getMesg(), loaderMessage.getMesgType(), loaderMessage.getRowData(), loaderMessage.isMessageIsCancelled());
					tempLoderList2.add(tempLoderList);
					loaderMessage.getMesg().setrXmlText(null);
				}
				manager.persist(loaderMessage.getMesg());
				// Notify subscribers that we've added a new message to DB
				setChanged();
				notifyObservers(loaderMessage);

				LOGGER.debug("Message ID :: " + loaderMessage.getMesgId() + "persisted in DB");
			} catch (Exception e) {
				insertText = false;
				e.printStackTrace();
				String errMessageText = (loaderMessage.getRowData().length() >= 1000) ? loaderMessage.getRowData().substring(0, 999) : loaderMessage.getRowData();
				if (Main.isDbReader()) {
					loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessageText, "");

				} else {
					loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessageText, "");
					if (xmlDataSouceHelper.isUseErrorQueue()) {
						moveToErrorQueue(loaderMessage.getRowData());
					}

				}
			}

		}
		if (list.size() == 1) {
			LOGGER.debug("Start Persisting for ::: " + list.get(0).getMessageSequenceNo());
		} else {
			LOGGER.debug("Start Persisting for ::: " + list.size());
		}
		try {
			entityTransaction.commit();
		} catch (Exception e) {
			insertText = false;
			e.printStackTrace();
			String errMessageText = (list.get(0).getRowData().length() >= 1000) ? list.get(0).getRowData().substring(0, 999) : list.get(0).getRowData();
			if (Main.isDbReader()) {
				loaderDAO.insertIntoErrorld("DB Connector", "Failed", "", errMessageText, "");
			} else {
				loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessageText, "");
				loaderDAO.insertIntoErrorld("MQ Connector", "Failed", "", errMessageText, "");
				if (xmlDataSouceHelper.isUseErrorQueue()) {
					moveToErrorQueue(list.get(0).getRowData());
				}

			}
		}
		if (list.size() == 1) {
			LOGGER.debug("End Persisting for ::: " + list.get(0).getMessageSequenceNo());
			LOGGER.debug("Commit on DB for ::: " + list.get(0).getMessageSequenceNo());
		} else {
			LOGGER.debug("End Persisting for ::: " + list.size());
			LOGGER.debug("Commit on DB for :: " + list.size() + " :: ROWS");
		}

		if (insertText) {
			for (List<LoaderMessage> mesgNeedUpdate : tempLoderList2) {
				updateSepaText(mesgNeedUpdate);
			}
		}

		if (insertText) {
			if (list.size() > 0)
				unqueueMessagesDelegate.removeProcessingRows(list, appConfigBean.getSAAAid());
		}

	}

	private void updateSepaText(List<LoaderMessage> tempLoderTest) {
		for (LoaderMessage lodMesg : tempLoderTest) {
			FilePayload filePayload = new FilePayload();
			filePayload.setAid((int) lodMesg.getMesg().getId().getAid());
			filePayload.setSumidh((int) lodMesg.getMesg().getId().getMesgSUmidh());
			filePayload.setSumidl((int) lodMesg.getMesg().getId().getMesgSUmidl());
			Calendar cal = Calendar.getInstance();
			cal.setTime(lodMesg.getMesg().getMesgCreaDateTime());
			cal.set(Calendar.MILLISECOND, 0);
			filePayload.setCreationDateTime(new java.sql.Timestamp(cal.getTimeInMillis()));
			for (XmlTextMessage xmlTextMessage : lodMesg.getMesg().getrXmlText()) {
				if (xmlTextMessage.getMsgFrmtName().equalsIgnoreCase("MX")) {
					try {
						if (!lodMesg.isMessageIsCancelled()) {
							insertXmlMsg.execute(filePayload, xmlTextMessage.getMsgDocument(), ((xmlTextMessage.getMsgHeader() == null || xmlTextMessage.getMsgHeader().isEmpty()) ? "<AppHdr></AppHdr>" : xmlTextMessage.getMsgHeader()),
									xmlTextMessage.getMsgIdentifier(), 0, 0, 0);
						} else {
							fillMesgText(filePayload, xmlTextMessage, lodMesg);
						}
					} catch (Exception e) {
						fillMesgText(filePayload, xmlTextMessage, lodMesg);
					}

				} else {
					getBaseServiceLocator().getFilePayloadService().updateSEPAText(filePayload, xmlTextMessage.getMsgDocument(), xmlTextMessage.getMsgIdentifier(), (int) xmlTextMessage.getXmlTextPk().getTextMsgOrder(), 0, xmlTextMessage.getBlkFlag(),
							(int) xmlTextMessage.getXmlTextPk().getTextMsgOrder());
				}

			}
		}
	}

	private void fillMesgText(FilePayload filePayload, XmlTextMessage xmlTextMessage, LoaderMessage lodMesg) {
		TextPart text = new TextPart();
		TextPK id = new TextPK();
		id.setAid(filePayload.getAid());
		id.setTextSUmidh(filePayload.getSumidh());
		id.setTextSUmidl(filePayload.getSumidl());
		text.setId(id);
		text.setTextDataBlock(lodMesg.getRowData());
		text.setMesgCreaDateTime(filePayload.getCreationDateTime());
		loaderDAO.insertIntoRtext(text, isPartDb);

	}

	private List<LoaderMessage> prepareXMLText(Mesg mainMesg, String formatType, String rowdata, boolean messgesIsCanleed) {

		List<LoaderMessage> tempLoderList = new ArrayList<LoaderMessage>();
		LoaderMessage loaderMessage2 = new LoaderMessage();
		Mesg mesg = new Mesg();
		mesg.setId(mainMesg.getId());
		mesg.setMesgCreaDateTime(mainMesg.getMesgCreaDateTime());
		ArrayList<XmlTextMessage> xmlTextMesg = new ArrayList<XmlTextMessage>();

		if (mainMesg.getrXmlText() != null) {
			for (XmlTextMessage message : mainMesg.getrXmlText()) {
				XmlTextMessage xmlMesg = new XmlTextMessage();
				xmlMesg.setMsgDocument(message.getMsgDocument());
				if (formatType.equalsIgnoreCase("AMH") || mainMesg.getMesgFrmtName().equalsIgnoreCase("Internal")) {
					xmlMesg.setMsgHeader(message.getMsgHeader());
					xmlMesg.setMsgFrmtName("MX");
				}
				xmlMesg.setMsgIdentifier(message.getMsgIdentifier());
				XmlTextPK xmPK = new XmlTextPK();
				xmPK.setAid(message.getXmlTextPk().getAid());
				xmPK.setTextSUMIDH(message.getXmlTextPk().getTextSUMIDH());
				xmPK.setTextSUMIDL(message.getXmlTextPk().getTextSUMIDL());
				xmPK.setCreateDate(message.getXmlTextPk().getCreateDate());
				xmPK.setTextMsgOrder(message.getXmlTextPk().getTextMsgOrder());
				xmlMesg.setXmlTextPk(xmPK);
				xmlMesg.setBlkFlag(message.getBlkFlag());
				xmlTextMesg.add(xmlMesg);
			}
			mesg.setrXmlText(xmlTextMesg);
			loaderMessage2.setMesg(mesg);
			loaderMessage2.setRowData(rowdata);
			loaderMessage2.setMessageIsCancelled(messgesIsCanleed);

			tempLoderList.add(loaderMessage2);
		}

		return tempLoderList;

	}

	private void prepareDataForPartitionDatabase(Mesg mesg) throws IllegalAccessException, InvocationTargetException {
		if (mesg == null || !isPartDb) {
			return;
		}

		Converter converter = new DateConverter(null);
		BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
		beanUtilsBean.getConvertUtils().register(converter, BigDecimal.class);
		beanUtilsBean.getConvertUtils().register(converter, Date.class);

		// inst
		ArrayList<InstPart> rinstsPart = new ArrayList<InstPart>();
		InstPart instPart = new InstPart();
		AppePart appePart = new AppePart();
		RintvPart rintvPart = new RintvPart();

		if (mesg.getRinsts() != null) {
			BeanUtils.copyProperties(instPart, mesg.getRinsts().get(0));
			instPart.setMesgCreaDateTime(mesg.getMesgCreaDateTime());
			rinstsPart.add(instPart);

			if (mesg.getRinsts().get(0).getRappes() != null && !mesg.getRinsts().get(0).getRappes().isEmpty()) {
				BeanUtils.copyProperties(appePart, mesg.getRinsts().get(0).getRappes().get(0));
				appePart.setMesgCreaDateTime(mesg.getMesgCreaDateTime());
				if (!xmlDataSouceHelper.isAppId()) {
					appePart.setAppeRecordId(new BigDecimal(mesg.getId().getMesgSUmidh()));
				} else {
					appePart.setAppeRecordId(new BigDecimal(loaderDAO.getMxseqAppeRecordId()));
				}
				rinstsPart.get(0).addRappePart(appePart);
			}

			if (mesg.getRinsts().get(0).getrIntv() != null) {
				BeanUtils.copyProperties(rintvPart, mesg.getRinsts().get(0).getrIntv().get(0));
				rintvPart.setMesgCreaDateTime(mesg.getMesgCreaDateTime());
				rinstsPart.get(0).addRintPart(rintvPart);
				mesg.setRinstsPart(rinstsPart);

			}
			mesg.getRinstsPart().get(0).setRappes(null);
			mesg.getRinstsPart().get(0).setrIntv(null);
			mesg.setRinsts(null);
		}

		if (mesg.getRtext() != null) {
			TextPart textPart = new TextPart();
			BeanUtils.copyProperties(textPart, mesg.getRtext());
			textPart.setMesgCreaDateTime(mesg.getMesgCreaDateTime());
			mesg.setRtextPart(textPart);
			mesg.setRtext(null);

		}

	}

	private boolean isPartitionedDatabase() {
		return loaderDAO.isPartitionedDatabase();
	}

	@SuppressWarnings("unused") // it is called from spring, when creating this object.
	private void init() {

		entityManagerFactory = JPAUtil.createEntityManagerFactory(appConfigBean);

		if (entityManagerFactory == null) {
			LOGGER.error("Faild to create JPA entity manager factory");
			return;
		}
		manager = entityManagerFactory.createEntityManager();

		if (observerList == null || observerList.size() == 0) {
			return;
		}

		for (Observer observer : observerList) {
			this.addObserver(observer);
		}

		// initialize bicInfoList from db

		bicInfoList = licenseDAO.getBicLicenseInfo();

		if (bicInfoList == null || bicInfoList.size() == 0) {
			LOGGER.error("Faild to get the license BICs.");
		}

		DataSourceParser dataSourceParser = null;
		try {
			dataSourceParser = new DataSourceParser(getAppConfigBean().getDbConfigFilePath());
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.xmlDataSouceHelper = dataSourceParser.getXmlDataSource();
		isPartDb = isPartitionedDatabase();
	}

	public LoaderDAO getLoaderDAO() {
		return loaderDAO;
	}

	public void setLoaderDAO(LoaderDAO loaderDAO) {
		this.loaderDAO = loaderDAO;
	}

	public List<Observer> getObserverList() {
		return observerList;
	}

	public void setObserverList(List<Observer> observerList) {
		this.observerList = observerList;
	}

	public LicenseDAO getLicenseDAO() {
		return licenseDAO;
	}

	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	private boolean isLicensedBIc(String bic) {
		if (bic == null || bic.isEmpty()) {
			return false;
		}

		if (bic.equalsIgnoreCase("XXXXXXXX")) {
			return true;
		}

		for (BicLicenseInfo bicInfo : bicInfoList) {
			if (bicInfo.getBicCode().getBicCode().equalsIgnoreCase(bic)) {
				return true;
			}
		}

		return false;
	}

	public int getBatchsize() {
		return batchsize;
	}

	public void setBatchsize(int batchsize) {
		this.batchsize = batchsize;
	}

	public UnqueueMessagesDelegate getUnqueueMessagesDelegate() {
		return unqueueMessagesDelegate;
	}

	public void setUnqueueMessagesDelegate(UnqueueMessagesDelegate unqueueMessagesDelegate) {
		this.unqueueMessagesDelegate = unqueueMessagesDelegate;
	}

	public InsertXmlTexMessage getInsertXmlMsg() {
		return insertXmlMsg;
	}

	public void setInsertXmlMsg(InsertXmlTexMessage insertXmlMsg) {
		this.insertXmlMsg = insertXmlMsg;
	}

	public JMSSender getMoveToErrorQSender() {
		return moveToErrorQSender;
	}

	public void setMoveToErrorQSender(JMSSender moveToErrorQSender) {
		this.moveToErrorQSender = moveToErrorQSender;
	}

	public XMLDataSouceHelper getXmlDataSouceHelper() {
		return xmlDataSouceHelper;
	}

	public void setXmlDataSouceHelper(XMLDataSouceHelper xmlDataSouceHelper) {
		this.xmlDataSouceHelper = xmlDataSouceHelper;
	}

	public boolean isPartDb() {
		return isPartDb;
	}

	public void setPartDb(boolean isPartDb) {
		this.isPartDb = isPartDb;
	}
}
