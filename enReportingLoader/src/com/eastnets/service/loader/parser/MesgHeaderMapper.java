package com.eastnets.service.loader.parser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.domain.loader.Mesg;
import com.eastnets.domain.loader.MesgPK;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Message;
import com.eastnets.mx.mapping.MessageNature;
import com.eastnets.mx.mapping.Priority;

/**
 * @author MKassab
 * 
 */
public class MesgHeaderMapper extends MessageMapper {

	private static final Logger LOGGER = Logger.getLogger(MesgHeaderMapper.class);

	public static Mesg mapMesg(DataPDU dataPdu, LoaderMessage loaderMessage) {
		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Start parsing message header for message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {
			LOGGER.debug("Start parsing message header for message :: " + loaderMessage.getMessageSequenceNo());

		}

		Mesg mesg = new Mesg();

		Message message = getMessageNode(dataPdu);

		if (message == null) {
			return null;
		}

		String gen = "None";

		MesgPK mesgPK = new MesgPK(); // empty key, will be filled later.

		/*
		 * The following lines well test if unit name, creation date, and creation operation exist in DB or not
		 */
		Map<String, Object> messageProperties = loaderMessage.getMesgProperties();
		String creationOperater = "Unknown";
		Date date = new Date();
		String unitName = "None";
		if (messageProperties != null && !messageProperties.isEmpty()) {
			if (messageProperties.get("unitname") != null && !messageProperties.get("unitname").toString().isEmpty()) {
				unitName = messageProperties.get("unitname").toString();
			}
			if (messageProperties.get("creationdate") != null) {
				date = (Date) messageProperties.get("creationdate");
			}
			if (messageProperties.get("creationoperator") != null && !messageProperties.get("creationoperator").toString().isEmpty()) {
				creationOperater = messageProperties.get("creationoperator").toString();
			}
		}
		// ====

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		// Set time fields to zero
		cal.set(Calendar.MILLISECOND, 0);

		mesg.setId(mesgPK);
		mesg.setMesgCreaRpName(gen);
		mesg.setMesgCreaOperNickname(creationOperater);
		mesg.setMesgCreaDateTime(cal.getTime());
		mesg.setMesgModOperNickname(creationOperater);
		mesg.setMesgModDateTime(date);
		mesg.setMesgVerfOperNickname(gen);
		mesg.setXInst0UnitName(unitName);

		int con = 0;
		mesg.setMesgClass("MESG_NORMAL");
		mesg.setMesgIsTextReadonly(new BigDecimal(con));
		mesg.setMesgCreaMpfnName("mpc");
		mesg.setMesgIsDeleteInhibited(new BigDecimal(con));
		mesg.setMesgIsTextModified(new BigDecimal(con));
		mesg.setMesgIsPartial(new BigDecimal(con));
		mesg.setMesgStatus("COMPLETED");
		mesg.setMesgToken(new BigDecimal(con));
		mesg.setMesgUumidSuffix(new BigDecimal(0));
		mesg.setXCategory("0");
		mesg.setArchived(new BigDecimal(0));
		mesg.setLastUpdate(new Date());
		mesg.setRestored(new BigDecimal(0));

		if (message.getInterfaceInfo() != null && message.getInterfaceInfo().getValidationLevel() != null) {
			mesg.setMesgValidationRequested(message.getInterfaceInfo().getValidationLevel().value() == null ? "VAL_NO_VALIDATION" : message.getInterfaceInfo().getValidationLevel().value());
			mesg.setMesgValidationPassed(message.getInterfaceInfo().getValidationLevel().value() == null ? "VAL_NO_VALIDATION" : message.getInterfaceInfo().getValidationLevel().value());
		} else {
			mesg.setMesgValidationRequested("VAL_NO_VALIDATION");
			mesg.setMesgValidationPassed("VAL_NO_VALIDATION");
		}

		if (message.getInterfaceInfo() != null && message.getInterfaceInfo().getMessageCreator() != null) {
			mesg.setMesgCreaApplServName(message.getInterfaceInfo().getMessageCreator().value());
		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getFINNetworkInfo() != null) {
			mesg.setMesgCopyServiceId(message.getNetworkInfo().getFINNetworkInfo().getCopyService());
			if (message.getNetworkInfo().getFINNetworkInfo().isIsRetrieved() != null) {
				int val = message.getNetworkInfo().getFINNetworkInfo().isIsRetrieved() ? 1 : 0;
				mesg.setMesgIsRetrieved(new BigDecimal(val));
			} else {
				mesg.setMesgIsRetrieved(new BigDecimal(0));
			}

			mesg.setMesgUserPriorityCode(message.getNetworkInfo().getFINNetworkInfo().getUserPriority());
		} else {
			mesg.setMesgIsRetrieved(new BigDecimal(0));
		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getSWIFTNetNetworkInfo() != null) {
			mesg.setMesgDelvOverdueWarnReq(message.getNetworkInfo().getSWIFTNetNetworkInfo().getOverdueWarningDelay() == null ? new BigDecimal(0) : new BigDecimal(message.getNetworkInfo().getSWIFTNetNetworkInfo().getOverdueWarningDelay()));
			if (message.getNetworkInfo().getSWIFTNetNetworkInfo().isIsPossibleDuplicateResponse() != null) {
				int val = message.getNetworkInfo().getSWIFTNetNetworkInfo().isIsPossibleDuplicateResponse() ? 1 : 0;
				mesg.setMesgUserIssuedAsPde(new BigDecimal(val));
			} else {
				mesg.setMesgUserIssuedAsPde(new BigDecimal(0));
			}

			mesg.setMesgRequestType(message.getNetworkInfo().getSWIFTNetNetworkInfo().getRequestType() == null ? message.getMessageIdentifier() : message.getNetworkInfo().getSWIFTNetNetworkInfo().getRequestType());

			String mesgPayLoadType = null;

			if (message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes() != null && message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute() != null
					&& message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0) != null) {

				String name = message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0).getName();

				if (name != null && name.equalsIgnoreCase("type")) {
					mesgPayLoadType = message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0).getValue();
				}

			}
			mesg.setMesgPayloadType(mesgPayLoadType);
		}

		if (message.getSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList() != null
				&& message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0) != null) {
			mesg.setMesgSignDigestValue(message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0).getDigestValue());
			mesg.setMesgSignDigestReference(message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0).getDigestRef());
		}

		mesg.setMesgFrmtName(message.getFormat());
		mesg.setMesgIdentifier(message.getMessageIdentifier());

		if (message.getNetworkInfo() != null) {
			if (message.getNetworkInfo().isIsNotificationRequested() != null) {
				int val = message.getNetworkInfo().isIsNotificationRequested() ? 1 : 0;
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(val));
			} else {
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(0));
			}
			if (message.getNetworkInfo().isIsPossibleDuplicate() != null) {
				int val = message.getNetworkInfo().isIsPossibleDuplicate() ? 1 : 0;
				mesg.setMesgZz41IsPossibleDup(new BigDecimal(val));
			}
			mesg.setMesgService(message.getNetworkInfo().getService());

			String service = message.getNetworkInfo().getService();
			String serviceVal = service.substring(service.length() - 2, service.length());
			mesg.setMesgIsLive(serviceVal.equalsIgnoreCase("!p") ? new BigDecimal(0) : new BigDecimal(1));

			if (message.getNetworkInfo().getDuplicateHistory() != null && message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM() != null && message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0) != null) {
				if (message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0).getName() != null) {
					mesg.setMesgPossibleDupCreation(message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0).getName().getLocalPart());
				}
			}

		} else {
			mesg.setMesgNetworkDelvNotifReq(new BigDecimal(0));
			mesg.setMesgZz41IsPossibleDup(new BigDecimal(0));
			mesg.setMesgIsLive(new BigDecimal(1));
		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getFINNetworkInfo() != null) {
			mesg.setMesgReleaseInfo(message.getNetworkInfo().getFINNetworkInfo().getReleaseInfo());
		}

		if (message.getSender() != null && message.getSender().getFullName() != null) {
			if (message.getSender().getFullName().getX1() != null) {
				mesg.setMesgSenderX1(message.getSender().getFullName().getX1());
			} else {
				mesg.setMesgSenderX1("");
			}
			mesg.setMesgSenderX2(message.getSender().getFullName().getX2());
			mesg.setMesgSenderX3(message.getSender().getFullName().getX3());
			mesg.setMesgSenderX4(message.getSender().getFullName().getX4());
			mesg.setMesgSenderBranchInfo(message.getSender().getFullName().getBranchInformation());
			mesg.setMesgSenderCityName(message.getSender().getFullName().getCityName());
			mesg.setMesgSenderCtryCode(message.getSender().getFullName().getCountryCode());
			mesg.setMesgSenderInstitutionName(message.getSender().getFullName().getFinancialInstitution());
			mesg.setMesgSenderLocation(message.getSender().getFullName().getLocation());
			mesg.setMesgRequestorDn(message.getSender().getDN().trim());
		}

		if (message.getSubFormat() != null) {
			mesg.setMesgSubFormat(message.getSubFormat().value() != null ? message.getSubFormat().value().toUpperCase() : null);
		}

		if (message.getInterfaceInfo() != null) {
			mesg.setMesgUserReferenceText(message.getInterfaceInfo().getUserReference());
		}

		if (message.getReceiver() != null) {
			if (message.getReceiver().getFullName() != null) {
				mesg.setXReceiverX1(message.getReceiver().getFullName().getX1());
				mesg.setXReceiverX2(message.getReceiver().getFullName().getX2());
				mesg.setXReceiverX3(message.getReceiver().getFullName().getX3());
				mesg.setXReceiverX4(message.getReceiver().getFullName().getX4());
			}
		}

		if (message.getMessageIdentifier() != null) {
			mesg.setMesgMesgUserGroup(message.getMessageIdentifier().substring(0, 4));
			mesg.setMesgSyntaxTableVer(message.getMessageIdentifier().substring(message.getMessageIdentifier().length() - 2, message.getMessageIdentifier().length()));
			mesg.setMesgType(message.getMessageIdentifier().substring(5, 8));
		} else {
			mesg.setMesgMesgUserGroup("");
			mesg.setMesgSyntaxTableVer("");
		}

		MessageNature valueAsENUM = null;
		String targetValue = null;
		String concatValue = "_MSG";
		if (message.getInterfaceInfo() != null) {
			valueAsENUM = message.getInterfaceInfo().getMessageNature();
			if (valueAsENUM != null) {
				targetValue = valueAsENUM.value().toUpperCase() + concatValue;
				mesg.setMesgNature(targetValue);
			}
		} else {
			LOGGER.warn("Message Nature is empty!");
		}

		Priority prioAsENUM = null;
		String priValue = null;
		String priConcatValue = "PRI_";
		if (message.getNetworkInfo() != null) {
			prioAsENUM = message.getNetworkInfo().getPriority();
			if (prioAsENUM != null) {
				priValue = priConcatValue + prioAsENUM.value().toUpperCase();
			}
			mesg.setMesgNetworkPriority(priValue);
		} else {
			LOGGER.warn("Priority is empty!");
		}

		String assginedValue = "CORR_TYPE_";
		if (mesg.getMesgSenderX4() != null) {
			assginedValue = assginedValue + "INDIVIDUAL";
		} else if (mesg.getMesgSenderX3() != null) {
			assginedValue = assginedValue + "APPLICATION";
		} else if (mesg.getMesgSenderX2() != null) {
			assginedValue = assginedValue + "DEPARTMENT";
		} else if (mesg.getMesgSenderX1() != null) {
			assginedValue = assginedValue + "INSTITUTION";
		} else {
			assginedValue = assginedValue + "N_A";
		}
		mesg.setMesgSenderCorrType(assginedValue);

		String IO = "I";
		String bic11 = null;
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			IO = "I";
			bic11 = mesg.getXReceiverX1();

		} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
			IO = "O";
			bic11 = mesg.getMesgSenderX1();
		}

		String fullVaue = IO + bic11 + mesg.getMesgType() + mesg.getMesgUserReferenceText();
		mesg.setMesgUumid(fullVaue);
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			mesg.setXOwnLt(mesg.getMesgSenderX1().substring(0, 8));
		} else {
			mesg.setXOwnLt(mesg.getXReceiverX1().substring(0, 8));
		}

		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Finish parsing message header :: " + loaderMessage.getMesg().getMesgUumid() + " :: with the following details :: " + mesg.toString());
		} else {
			LOGGER.debug("Finish parsing message header :: " + loaderMessage.getMessageSequenceNo() + " :: with the following details :: " + mesg.toString());

		}

		return mesg;
	}

	public static Mesg mapMesg(AMP amp, LoaderMessage loaderMessage) {
		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Start parsing message header for message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {
			LOGGER.debug("Start parsing message header for message :: " + loaderMessage.getMessageSequenceNo());

		}

		Mesg mesg = new Mesg();

		Message message = getMessageNode(amp);

		if (message == null) {
			return null;
		}

		String gen = "None";

		MesgPK mesgPK = new MesgPK(); // empty key, will be filled later.

		/*
		 * The following lines well test if unit name, creation date, and creation operation exist in DB or not
		 */
		Map<String, Object> messageProperties = loaderMessage.getMesgProperties();
		String creationOperater = "Unknown";
		Date date = new Date();
		String unitName = "None";
		if (messageProperties != null && !messageProperties.isEmpty()) {
			if (messageProperties.get("unitname") != null && !messageProperties.get("unitname").toString().isEmpty()) {
				unitName = messageProperties.get("unitname").toString();
			}
			if (messageProperties.get("creationdate") != null) {
				// date = (Date) messageProperties.get("creationdate");
			}
			if (messageProperties.get("creationoperator") != null && !messageProperties.get("creationoperator").toString().isEmpty()) {
				creationOperater = messageProperties.get("creationoperator").toString();
			}
		}

		Date creatDateTime = null;
		Date modfDateTime = null;
		String creatDate = "";
		String modfDate = "";
		if (amp.getHeader() != null) {
			creatDate = amp.getHeader().getDateCreated();
			modfDate = amp.getHeader().getDateReceived();
			if (amp.getHeader().getOriginatorApplication() != null && !amp.getHeader().getOriginatorApplication().isEmpty()) {
				mesg.setMesgCreaMpfnName(amp.getHeader().getOriginatorApplication());
			}
		}
		try {
			// 2020-06-04T12:33:02.518+02:00
			creatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(creatDate.replace("T", " "));
		} catch (Exception e) {
			LOGGER.info("creatDate is null !!!");
		}

		try {
			// 2020-06-04T12:33:02.518+02:00
			modfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(modfDate.replace("T", " "));
		} catch (Exception e) {
			LOGGER.debug("modfDateTime is null ");

		}

		Calendar cal = Calendar.getInstance();
		if (creatDateTime == null) {
			creatDateTime = new Date();
		}
		cal.setTime(creatDateTime);

		// Set time fields to zero
		cal.set(Calendar.MILLISECOND, 0);

		mesg.setId(mesgPK);
		mesg.setMesgCreaRpName(gen);
		mesg.setMesgCreaOperNickname(creationOperater);
		mesg.setMesgCreaDateTime(cal.getTime());
		mesg.setMesgModOperNickname(creationOperater);
		if (modfDateTime != null) {
			mesg.setMesgModDateTime(modfDateTime);
		} else {
			mesg.setMesgModDateTime(creatDateTime);

		}
		mesg.setMesgVerfOperNickname(gen);
		try {
			mesg.setXInst0UnitName(amp.getHeader().getOwner());
		} catch (Exception e) {
			mesg.setXInst0UnitName(unitName);
		}

		int con = 0;
		mesg.setMesgClass("MESG_NORMAL");
		mesg.setMesgIsTextReadonly(new BigDecimal(con));

		mesg.setMesgIsDeleteInhibited(new BigDecimal(con));
		mesg.setMesgIsTextModified(new BigDecimal(con));
		mesg.setMesgIsPartial(new BigDecimal(con));

		try {
			if (loaderMessage.isMessageIsCancelled()) {
				mesg.setMesgStatus("FinalCancelledAuto Cancellation");
			} else {
				mesg.setMesgStatus("FinalDistributedOK");
			}
		} catch (Exception e) {
			mesg.setMesgStatus("COMPLETED");
		}

		mesg.setMesgToken(new BigDecimal(con));
		mesg.setMesgUumidSuffix(new BigDecimal(0));
		mesg.setXCategory("0");
		mesg.setArchived(new BigDecimal(0));
		mesg.setLastUpdate(new Date());
		mesg.setRestored(new BigDecimal(0));
		if (loaderMessage.isMessageIsCancelled()) {
			mesg.setMesgFrmtName("Internal");
		} else {

			mesg.setMesgFrmtName("MX");
		}

		mesg.setMesgIdentifier(message.getMessageIdentifier());
		try {
			String requestCrypto = amp.getProtocolParameters().getInterAct().getSwiftNet().getRequestCrypto();
			mesg.setMesgSecurityRequired((requestCrypto.equalsIgnoreCase("FALSE") ? new BigDecimal(0) : new BigDecimal(1)));

		} catch (Exception e) {
			mesg.setMesgSecurityRequired(new BigDecimal(0));
		}

		try {
			String requireSignatureList = amp.getProtocolParameters().getInterAct().getSwiftNet().getRequireSignatureList();
			mesg.setMesgUsePkiSignature((requireSignatureList.equalsIgnoreCase("FALSE") ? new BigDecimal(0) : new BigDecimal(1)));

		} catch (Exception e) {
			mesg.setMesgUsePkiSignature(new BigDecimal(0));
		}

		try {
			String copyIndicator = amp.getProtocolParameters().getInterAct().getSwiftNet().getCopyIndicator();
			mesg.setMesgIsCopyRequired((copyIndicator.equalsIgnoreCase("FALSE") ? new BigDecimal(0) : new BigDecimal(1)));

		} catch (Exception e) {

			mesg.setMesgIsCopyRequired(new BigDecimal(0));
		}

		try {
			String authNotifIndicator = amp.getProtocolParameters().getInterAct().getSwiftNet().getAuthNotifIndicator();
			mesg.setMesgAuthDelvNotifReq((authNotifIndicator.equalsIgnoreCase("FALSE") ? new BigDecimal(0) : new BigDecimal(1)));

		} catch (Exception e) {

			mesg.setMesgAuthDelvNotifReq(new BigDecimal(0));
		}

		if (message.getInterfaceInfo() != null && message.getInterfaceInfo().getValidationLevel() != null) {
			mesg.setMesgValidationRequested(message.getInterfaceInfo().getValidationLevel().value() == null ? "VAL_NO_VALIDATION" : message.getInterfaceInfo().getValidationLevel().value());
			mesg.setMesgValidationPassed(message.getInterfaceInfo().getValidationLevel().value() == null ? "VAL_NO_VALIDATION" : message.getInterfaceInfo().getValidationLevel().value());
		} else {
			mesg.setMesgValidationRequested("VAL_NO_VALIDATION");
			mesg.setMesgValidationPassed("VAL_NO_VALIDATION");
		}

		mesg.setMesgCreaApplServName("AMH Interface");

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getFINNetworkInfo() != null) {
			mesg.setMesgCopyServiceId(message.getNetworkInfo().getFINNetworkInfo().getCopyService());
			if (message.getNetworkInfo().getFINNetworkInfo().isIsRetrieved() != null) {
				int val = message.getNetworkInfo().getFINNetworkInfo().isIsRetrieved() ? 1 : 0;
				mesg.setMesgIsRetrieved(new BigDecimal(val));
			} else {
				mesg.setMesgIsRetrieved(new BigDecimal(0));
			}

			mesg.setMesgUserPriorityCode(message.getNetworkInfo().getFINNetworkInfo().getUserPriority());
		} else {
			mesg.setMesgIsRetrieved(new BigDecimal(0));
		}

		try {
			String pdeIndication = amp.getHeader().getPDEIndication();
			if (pdeIndication != null) {
				mesg.setMesgUserIssuedAsPde((pdeIndication.equalsIgnoreCase("true")) ? new BigDecimal(1) : new BigDecimal(0));
			}

		} catch (Exception e) {
			mesg.setMesgUserIssuedAsPde(new BigDecimal(0));

		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getSWIFTNetNetworkInfo() != null) {
			mesg.setMesgDelvOverdueWarnReq(message.getNetworkInfo().getSWIFTNetNetworkInfo().getOverdueWarningDelay() == null ? new BigDecimal(0) : new BigDecimal(message.getNetworkInfo().getSWIFTNetNetworkInfo().getOverdueWarningDelay()));

			mesg.setMesgRequestType(message.getNetworkInfo().getSWIFTNetNetworkInfo().getRequestType() == null ? message.getMessageIdentifier() : message.getNetworkInfo().getSWIFTNetNetworkInfo().getRequestType());

			String mesgPayLoadType = null;

			if (message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes() != null && message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute() != null
					&& message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0) != null) {

				String name = message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0).getName();

				if (name != null && name.equalsIgnoreCase("type")) {
					mesgPayLoadType = message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponsePayloadAttributes().getPayloadAttribute().get(0).getValue();
				}

			}
			mesg.setMesgPayloadType(mesgPayLoadType);
		}

		if (message.getSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList() != null
				&& message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0) != null) {
			mesg.setMesgSignDigestValue(message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0).getDigestValue());
			mesg.setMesgSignDigestReference(message.getSecurityInfo().getSWIFTNetSecurityInfo().getDigestList().getDigest().get(0).getDigestRef());
		}

		if (message.getNetworkInfo() != null) {
			if (message.getNetworkInfo().isIsNotificationRequested() != null) {
				int val = message.getNetworkInfo().isIsNotificationRequested() ? 1 : 0;
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(val));
			} else {
				mesg.setMesgNetworkDelvNotifReq(new BigDecimal(0));
			}
			if (message.getNetworkInfo().isIsPossibleDuplicate() != null) {
				int val = message.getNetworkInfo().isIsPossibleDuplicate() ? 1 : 0;
				mesg.setMesgZz41IsPossibleDup(new BigDecimal(val));
			}
			mesg.setMesgService(message.getNetworkInfo().getService());

			String service = message.getNetworkInfo().getService();
			if (service != null && !service.isEmpty()) {
				try {
					String serviceVal = service.substring(service.length() - 2, service.length());
					mesg.setMesgIsLive(serviceVal.equalsIgnoreCase("!p") ? new BigDecimal(0) : new BigDecimal(1));
				} catch (Exception e) {
					mesg.setMesgIsLive(new BigDecimal(0));
				}
			}

			if (message.getNetworkInfo().getDuplicateHistory() != null && message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM() != null && message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0) != null) {
				if (message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0).getName() != null) {
					mesg.setMesgPossibleDupCreation(message.getNetworkInfo().getDuplicateHistory().getPDEOrPDM().get(0).getName().getLocalPart());
				}
			}

		} else {
			mesg.setMesgNetworkDelvNotifReq(new BigDecimal(0));
			mesg.setMesgZz41IsPossibleDup(new BigDecimal(0));
			mesg.setMesgIsLive(new BigDecimal(1));
		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getFINNetworkInfo() != null) {
			mesg.setMesgReleaseInfo(message.getNetworkInfo().getFINNetworkInfo().getReleaseInfo());
		}

		if (message.getSender() != null && message.getSender().getFullName() != null) {
			if (loaderMessage.isMessageIsCancelled()) {
				mesg.setMesgSenderX1("XXXXXXXXXXX");
			} else {
				if (message.getSender().getFullName().getX1() != null) {
					mesg.setMesgSenderX1(message.getSender().getFullName().getX1().toUpperCase());
				} else {
					mesg.setMesgSenderX1("XXXXXXXXXXX");
				}
			}
			mesg.setMesgSenderX2(message.getSender().getFullName().getX2());
			mesg.setMesgSenderX3(message.getSender().getFullName().getX3());
			mesg.setMesgSenderX4(message.getSender().getFullName().getX4());
			mesg.setMesgSenderBranchInfo(message.getSender().getFullName().getBranchInformation());
			mesg.setMesgSenderCityName(message.getSender().getFullName().getCityName());
			mesg.setMesgSenderCtryCode(message.getSender().getFullName().getCountryCode());
			mesg.setMesgSenderInstitutionName(message.getSender().getFullName().getFinancialInstitution());
			mesg.setMesgSenderLocation(message.getSender().getFullName().getLocation());
			try {
				mesg.setMesgRequestorDn(message.getSender().getDN().trim());
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			mesg.setMesgSenderX1("XXXXXXXXXXX");
		}

		if (message.getSubFormat() != null) {
			mesg.setMesgSubFormat(message.getSubFormat().value() != null ? message.getSubFormat().value().toUpperCase() : null);
		}

		try {
			mesg.setMesgRelTrnRef(amp.getHeader().getMessageReference());
		} catch (Exception e) {
			mesg.setMesgRelTrnRef("");
		}

		try {
			mesg.setMesgUserReferenceText(amp.getProtocolParameters().getInterAct().getParameters().getMsgRef());
		} catch (Exception e) {
			mesg.setMesgUserReferenceText(message.getSenderReference());
		}

		if (message.getInterfaceInfo() != null) {

			mesg.setMesgTrnRef(message.getInterfaceInfo().getUserReference());
		}

		if (message.getReceiver() != null) {
			if (loaderMessage.isMessageIsCancelled()) {
				mesg.setXReceiverX1("XXXXXXXXXXX");
			} else {
				if (message.getReceiver().getFullName() != null) {
					mesg.setXReceiverX1(message.getReceiver().getFullName().getX1().toUpperCase());
					mesg.setXReceiverX2(message.getReceiver().getFullName().getX2());
					mesg.setXReceiverX3(message.getReceiver().getFullName().getX3());
					mesg.setXReceiverX4(message.getReceiver().getFullName().getX4());
				}
			}

		} else {
			mesg.setXReceiverX1("XXXXXXXXXXX");
		}

		try {
			if (message.getMessageIdentifier() != null) {

				mesg.setMesgSyntaxTableVer(message.getMessageIdentifier().substring(message.getMessageIdentifier().length() - 2, message.getMessageIdentifier().length()));
				mesg.setMesgType(message.getMessageIdentifier().substring(5, 8));
			} else {
				mesg.setMesgSyntaxTableVer("");
			}
		} catch (Exception e) {
			mesg.setMesgSyntaxTableVer("1905");
			mesg.setMesgType("000");
		}

		try {
			mesg.setMesgMesgUserGroup(amp.getHeader().getProcessingType());
		} catch (Exception e) {

			mesg.setMesgMesgUserGroup("XXXX");
		}

		MessageNature valueAsENUM = null;
		String targetValue = null;
		String concatValue = "_MSG";
		if (message.getInterfaceInfo() != null) {
			valueAsENUM = message.getInterfaceInfo().getMessageNature();
			if (valueAsENUM != null) {
				targetValue = valueAsENUM.value().toUpperCase() + concatValue;
				mesg.setMesgNature(targetValue);
			}
		} else {
			LOGGER.warn("Message Nature is empty!");
		}

		Priority prioAsENUM = null;
		String priValue = null;
		String priConcatValue = "PRI_";
		if (message.getNetworkInfo() != null) {
			prioAsENUM = message.getNetworkInfo().getPriority();
			if (prioAsENUM != null) {
				priValue = priConcatValue + prioAsENUM.value().toUpperCase();
			}
			mesg.setMesgNetworkPriority(priValue);
		} else {
			LOGGER.warn("Priority is empty!");
		}

		String assginedValue = "CORR_TYPE_";
		if (mesg.getMesgSenderX4() != null) {
			assginedValue = assginedValue + "INDIVIDUAL";
		} else if (mesg.getMesgSenderX3() != null) {
			assginedValue = assginedValue + "APPLICATION";
		} else if (mesg.getMesgSenderX2() != null) {
			assginedValue = assginedValue + "DEPARTMENT";
		} else if (mesg.getMesgSenderX1() != null) {
			assginedValue = assginedValue + "INSTITUTION";
		} else {
			assginedValue = assginedValue + "N_A";
		}
		mesg.setMesgSenderCorrType(assginedValue);

		String IO = "I";
		String bic11 = null;
		if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
			IO = "I";
			bic11 = mesg.getXReceiverX1();

		} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
			IO = "O";
			bic11 = mesg.getMesgSenderX1();
		}

		String fullVaue = IO + bic11 + mesg.getMesgType() + ((mesg.getMesgUserReferenceText() != null) ? mesg.getMesgUserReferenceText() : "");
		mesg.setMesgUumid(fullVaue);
		try {

			if (loaderMessage.isMessageIsCancelled()) {
				mesg.setXOwnLt("XXXXXXXX");
			} else {
				if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
					mesg.setXOwnLt(mesg.getMesgSenderX1().substring(0, 8).toUpperCase());
				} else {
					mesg.setXOwnLt(mesg.getXReceiverX1().substring(0, 8).toUpperCase());
				}
			}

		} catch (Exception e) {
			mesg.setXOwnLt("XXXXXXXX");

		}

		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Finish parsing message header :: " + loaderMessage.getMesg().getMesgUumid() + " :: with the following details :: " + mesg.toString());
		} else {
			LOGGER.debug("Finish parsing message header :: " + loaderMessage.getMessageSequenceNo() + " :: with the following details :: " + mesg.toString());

		}

		try {
			String mesgUetr = amp.getProtocolParameters().getInterAct().getSwiftNet().getE2EMessageID();
			mesg.setMesgUetr(mesgUetr);

		} catch (Exception e) {
			LOGGER.debug("");
		}

		try {
			if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
				mesg.setX_LAST_EMI_APPE_IAPP_NAME("SWIFTNet");
			} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
				mesg.setX_LAST_REC_APPE_IAPP_NAME("SWIFTNet");
			}
		} catch (Exception e) {
			mesg.setX_LAST_EMI_APPE_IAPP_NAME("SWIFTNet");
		}

		try {
			String snFOutputSequenceNumber = amp.getProtocolParameters().getInterAct().getSwiftNet().getSnFOutputSequenceNumber();
			if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
				mesg.setX_Last_Emi_Appe_Seq_Nbr(new BigDecimal(Integer.parseInt(snFOutputSequenceNumber)));
			} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
				mesg.setX_Last_Rec_Appe_Seq_Nbr(new BigDecimal(Integer.parseInt(snFOutputSequenceNumber)));
			}
		} catch (Exception e) {
			mesg.setX_Last_Emi_Appe_Seq_Nbr(new BigDecimal(0));
		}

		try {
			String snFOutputSessionId = amp.getProtocolParameters().getInterAct().getSwiftNet().getSnFOutputSessionId();
			String substringAfter = StringUtils.substringAfter(snFOutputSessionId, "!p:p:");
			if (mesg.getMesgSubFormat().equalsIgnoreCase("INPUT")) {
				mesg.setX_LAST_EMI_APPE_SESSION_NBR(new BigDecimal(Integer.parseInt(substringAfter)));
			} else if (mesg.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
				mesg.setX_LAST_REC_APPE_SESSION_NBR(new BigDecimal(Integer.parseInt(substringAfter)));
			}
		} catch (Exception e) {
			mesg.setX_LAST_EMI_APPE_SESSION_NBR(new BigDecimal(0));
		}
		return mesg;
	}

	public static void main(String[] args) {

		String snFOutputSessionId = "mohmmkassab123";
		String substringAfter = StringUtils.substringAfter(snFOutputSessionId, "kassab");
		System.out.println(substringAfter);

	}

}
