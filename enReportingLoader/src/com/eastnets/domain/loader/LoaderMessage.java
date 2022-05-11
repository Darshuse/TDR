package com.eastnets.domain.loader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MKassab
 * 
 */
public class LoaderMessage implements Serializable {

	private static final long serialVersionUID = -4575197029648905685L;

	public enum MessageStatus {
		NORMAL("normal"), CORRUPTED("corrupted"), UNLICENSED("unlicensed");
		private String messageStatus;

		public String getMessageStatus() {
			return messageStatus;
		}

		MessageStatus(String messageStatus) {
			this.messageStatus = messageStatus;
		}

	}

	public enum MessageSource {
		XML("XML"), DATABASE("FIN");
		private String messageSource;

		public String getMessageSource() {
			return messageSource;
		}

		MessageSource(String messageSource) {
			this.messageSource = messageSource;
		}
	}

	private MessageSource messageSource = MessageSource.DATABASE;
	boolean messageIsCancelled = false;

	public boolean isMessageIsCancelled() {
		return messageIsCancelled;
	}

	public void setMessageIsCancelled(boolean messageIsCancelled) {
		this.messageIsCancelled = messageIsCancelled;
	}

	/**
	 * holds the original message text, for example RJE or XML message text.
	 */
	private String rowData = "";
	private String rowHistory = "";
	private Connection lockCon;
	private String mesgType = "";
	private String messageTextFromMQ;
	private String payload;

	/**
	 * Holds any property data from the message source, this property could be used during the message processing.
	 */
	private Map<String, Object> mesgProperties = new HashMap<String, Object>();

	/**
	 * message header data
	 */
	private Mesg mesg;
	private Rintv rintv;
	private MesgPK mesgId;
	private Mesg mesgTemp;

	/*
	 * This Id came from the integration table, as they guarantee it's a unique no and we can depend on it to know the last fetched row.
	 */
	private BigDecimal messageSequenceNo;

	public String getRowData() {
		return rowData;
	}

	public void setRowData(String rowData) {
		this.rowData = rowData;
	}

	public Map<String, Object> getMesgProperties() {
		return mesgProperties;
	}

	public void setMesgProperties(Map<String, Object> mesgProperties) {
		this.mesgProperties = mesgProperties;
	}

	public Mesg getMesg() {
		return mesg;
	}

	public void setMesg(Mesg mesg) {
		this.mesg = mesg;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public void fillMesgKeys() {
		MesgPK id = mesg.getId();

		if (id == null) {
			return;
		}
		id.setAid(mesgId.getAid());
		id.setMesgSUmidh(mesgId.getMesgSUmidh());
		id.setMesgSUmidl(mesgId.getMesgSUmidl());

		TextPK textPk = null;
		XmlTextPK xmlText = null;
		RfilePK rfile = null;
		if (mesg.getRtext() != null) {
			textPk = mesg.getRtext().getId();
			textPk.setAid(mesgId.getAid());
			textPk.setTextSUmidh(mesgId.getMesgSUmidh());
			textPk.setTextSUmidl(mesgId.getMesgSUmidl());
		} else {

			if (mesg.getMesgFrmtName().equalsIgnoreCase("MX") || mesg.getMesgFrmtName().equalsIgnoreCase("Internal")) {
				if (getMesgType().equalsIgnoreCase("AMH") || mesg.getMesgFrmtName().equalsIgnoreCase("Internal")) {
					rfile = mesg.getRfile().getId();
					rfile.setAid(mesgId.getAid());
					rfile.setFileSUmidh(mesgId.getMesgSUmidh());
					rfile.setFileSUmidl(mesgId.getMesgSUmidl());
				} else {
					xmlText = mesg.getrXmlText().get(0).getXmlTextPk();
					xmlText.setAid(mesgId.getAid());
					xmlText.setTextSUMIDH(mesgId.getMesgSUmidh());
					xmlText.setTextSUMIDL(mesgId.getMesgSUmidl());
				}

			} else if (mesg.getMesgFrmtName().equalsIgnoreCase("File")) {
				rfile = mesg.getRfile().getId();
				rfile.setAid(mesgId.getAid());
				rfile.setFileSUmidh(mesgId.getMesgSUmidh());
				rfile.setFileSUmidl(mesgId.getMesgSUmidl());

			}

		}

		for (Inst inst : mesg.getRinsts()) {
			InstPK instPk = inst.getId();
			instPk.setAid(mesgId.getAid());
			instPk.setInstSUmidh(mesgId.getMesgSUmidh());
			instPk.setInstSUmidl(mesgId.getMesgSUmidl());

			if (inst.getRappes() != null) {
				for (Appe appe : inst.getRappes()) {
					AppePK appePk = appe.getId();
					appePk.setAid(mesgId.getAid());
					appePk.setAppeSUmidh(mesgId.getMesgSUmidh());
					appePk.setAppeSUmidl(mesgId.getMesgSUmidl());
				}
			}

		}

	}

	@Override
	public String toString() {
		return "LoaderMessage [rowData=" + rowData + ", rowHistory=" + rowHistory + ", mesgType=" + mesgType + ", mesgProperties=" + mesgProperties + ", mesg=" + mesg + ", mesgId=" + mesgId + ", Seq No ID = " + messageSequenceNo + "]";
	}

	public MesgPK getMesgId() {
		return mesgId;
	}

	public void setMesgId(MesgPK mesgId) {
		this.mesgId = mesgId;
	}

	public BigDecimal getMessageSequenceNo() {
		return messageSequenceNo;
	}

	public void setMessageSequenceNo(BigDecimal messageSequenceNo) {
		this.messageSequenceNo = messageSequenceNo;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Rintv getRintv() {
		return rintv;
	}

	public void setRintv(Rintv rintv) {
		this.rintv = rintv;
	}

	public String getRowHistory() {
		return rowHistory;
	}

	public void setRowHistory(String rowHistory) {
		this.rowHistory = rowHistory;
	}

	public Connection getLockCon() {
		return lockCon;
	}

	public void setLockCon(Connection lockCon) {
		this.lockCon = lockCon;
	}

	public String getMessageTextFromMQ() {
		return messageTextFromMQ;
	}

	public void setMessageTextFromMQ(String messageTextFromMQ) {
		this.messageTextFromMQ = messageTextFromMQ;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Mesg getMesgTemp() {
		return mesgTemp;
	}

	public void setMesgTemp(Mesg mesgTemp) {
		this.mesgTemp = mesgTemp;
	}

	public static void main(String args[]) {

		// create an empty array list1 with an initial capacity
		ArrayList<BeanSuper> arrlist = new ArrayList<BeanSuper>();
		ArrayList<BeanSuper> arrlist2 = new ArrayList<BeanSuper>();
		BeanSuper beanSuper = new BeanSuper();
		beanSuper.setName("kassab");
		arrlist.add(beanSuper);

		for (BeanSuper beanSuper2 : arrlist) {
			BeanSuper beanSuper3 = new BeanSuper();
			beanSuper3.setName(beanSuper2.getName());
			arrlist2.add(beanSuper3);
		}

		arrlist.get(0).setName(null);
		System.out.println(arrlist2.get(0).getName());

	}

}
