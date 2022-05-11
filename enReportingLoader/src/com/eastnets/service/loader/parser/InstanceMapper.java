package com.eastnets.service.loader.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.loader.Inst;
import com.eastnets.domain.loader.InstPK;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Message;

/**
 * @author MKassab
 * 
 */
public class InstanceMapper extends MessageMapper {

	private static final Logger LOGGER = Logger.getLogger(InstanceMapper.class);

	public static Inst mapInst(DataPDU dataPdu, LoaderMessage loaderMessage) throws ParseException {
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Start parsing instance message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {

			LOGGER.debug("Start parsing instance message :: " + loaderMessage.getMessageSequenceNo());

		}
		Inst inst = new Inst();

		Message message = getMessageNode(dataPdu);

		// default date is 1-1-1970
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.YEAR, 1970);

		Date defaultDate = calendar.getTime();

		String gen = "None";
		InstPK instPK = new InstPK();
		instPK.setInstNum(0);
		inst.setId(instPK);

		inst.setInstType("INST_TYPE_ORIGINAL");
		inst.setInstNotificationType("INST_NOTIFICATION_NONE");
		inst.setInstStatus("COMPLETED");
		inst.setInstRelatedNbr(new BigDecimal(0));
		inst.setInstUnitName(gen);
		// useless but just to bet clear
		inst.setInstProcessState(new BigDecimal(0));
		inst.setInstLastMpfnResult("R_SUCCESS");
		inst.setInstRelativeRef(new BigDecimal(0));
		inst.setInstSm2000Priority(new BigDecimal(1000));
		inst.setInstDeferredTime(defaultDate);
		inst.setInstCreaMpfnName("mpc");
		inst.setInstCreaRpName("REP_DUMMY_QUEUE");
		inst.setInstCreaDateTime(new Date());
		inst.setInitialTargetRpName(gen);
		inst.setInstToken(new BigDecimal(0));
		inst.setInstDeliveryMode("DELIVERY_MODE_REAL_TIME");
		inst.setInstAppeSeqNbr(new BigDecimal(0)); // default seq number, will be override if there is appe.
		inst.setInstAppeDateTime(defaultDate);// default appe date will be override if there is appe.

		if (message.getInterfaceInfo() != null && message.getInterfaceInfo().getMessageCreator() != null) {
			inst.setInstCreaApplServName(message.getInterfaceInfo().getMessageCreator().value());
		}

		if (message.getReceiver() != null && message.getReceiver().getFullName() != null) {
			inst.setInstReceiverX1(message.getReceiver().getFullName().getX1());
			inst.setInstReceiverX2(message.getReceiver().getFullName().getX2());
			inst.setInstReceiverX3(message.getReceiver().getFullName().getX3());
			inst.setInstReceiverX4(message.getReceiver().getFullName().getX4());
			inst.setInstReceiverBranchInfo(message.getReceiver().getFullName().getBranchInformation());
			inst.setInstReceiverCityName(message.getReceiver().getFullName().getCityName());
			inst.setInstReceiverCtryCode(message.getReceiver().getFullName().getCountryCode());
			inst.setInstReceiverInstitutionName(message.getReceiver().getFullName().getFinancialInstitution());
			inst.setInstReceiverLocation(message.getReceiver().getFullName().getLocation());
		}

		String valueToConcat = "CORR_TYPE_";
		if (inst.getInstReceiverX4() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "INDIVIDUAL");
		} else if (inst.getInstReceiverX3() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "APPLICATION");
		} else if (inst.getInstReceiverX2() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "DEPARTMENT");
		} else if (inst.getInstReceiverX1() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "INSTITUTION");
		} else {
			inst.setInstReceiverCorrType(valueToConcat + "N_A");
		}

		if (message.getNetworkInfo() != null && message.getNetworkInfo().getNetwork() != null) {
			inst.setInstReceiverNetworkIappNam(message.getNetworkInfo().getNetwork().value());
		}

		if (message.getReceiver() != null) {
			inst.setInstResponderDn(message.getReceiver().getDN());
		}

		if (message.getSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo() != null) {
			if (message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested() != null) {
				Boolean isNRRequested = message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested();
				inst.setInstNrIndicator(isNRRequested ? new BigDecimal(1) : new BigDecimal(0));
			}
			inst.setInstCbtReference(message.getNetworkInfo().getSWIFTNetNetworkInfo().getReference());
		}
		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Finish parsing instance message :: " + loaderMessage.getMesg().getMesgUumid() + " :: with the following details :: " + inst.toString());

		} else {
			LOGGER.debug("Finish parsing instance message :: " + loaderMessage.getMessageSequenceNo() + " :: with the following details :: " + inst.toString());
		}
		return inst;
	}

	public static Inst mapInst(AMP amp, LoaderMessage loaderMessage) throws ParseException {
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Start parsing instance message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {

			LOGGER.debug("Start parsing instance message :: " + loaderMessage.getMessageSequenceNo());

		}
		Inst inst = new Inst();

		Message message = getMessageNode(amp);

		// default date is 1-1-1970
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.YEAR, 1970);

		Date defaultDate = calendar.getTime();

		String gen = "None";
		InstPK instPK = new InstPK();
		instPK.setInstNum(0);
		inst.setId(instPK);

		inst.setInstType("INST_TYPE_ORIGINAL");
		inst.setInstNotificationType("INST_NOTIFICATION_NONE");
		inst.setInstStatus("COMPLETED");
		inst.setInstRelatedNbr(new BigDecimal(0));
		inst.setInstUnitName(gen);
		if (amp.getHeader() != null) {

			inst.setInstMpfnName(amp.getHeader().getWorkflow());

			inst.setInstCreaMpfnName(amp.getHeader().getWorkflowModel());
			if (amp.getHeader().getBackendChannel() != null && !amp.getHeader().getBackendChannel().isEmpty()) {
				inst.setInstCreaRpName(amp.getHeader().getBackendChannel());
			}
			inst.setInstReceiverNetworkIappNam(amp.getHeader().getNetworkChannel());

		}
		// useless but just to bet clear
		inst.setInstProcessState(new BigDecimal(0));
		inst.setInstLastMpfnResult("R_SUCCESS");
		inst.setInstRelativeRef(new BigDecimal(0));
		inst.setInstSm2000Priority(new BigDecimal(1000));
		inst.setInstDeferredTime(defaultDate);
		inst.setInstCreaDateTime(new Date());
		inst.setInitialTargetRpName(gen);
		inst.setInstToken(new BigDecimal(0));
		try {

			String deliveryMode = amp.getProtocolParameters().getInterAct().getSwiftNet().getDeliveryMode();
			inst.setInstDeliveryMode(deliveryMode);

		} catch (Exception e) {
			inst.setInstDeliveryMode("DELIVERY_MODE_REAL_TIME");

		}

		inst.setInstAppeSeqNbr(new BigDecimal(0)); // default seq number, will be override if there is appe.
		inst.setInstAppeDateTime(defaultDate);// default appe date will be override if there is appe.
		inst.setInstCreaApplServName("AMH Interface");

		if (message.getReceiver() != null && message.getReceiver().getFullName() != null) {
			if (loaderMessage.isMessageIsCancelled()) {
				inst.setInstReceiverX1("XXXXXXXXXXX");
			} else {
				if (message.getReceiver().getFullName().getX1() != null) {
					inst.setInstReceiverX1(message.getReceiver().getFullName().getX1().toUpperCase());
				} else {
					inst.setInstReceiverX1("XXXXXXXXXXX");
				}
			}
			inst.setInstReceiverX2(message.getReceiver().getFullName().getX2());
			inst.setInstReceiverX3(message.getReceiver().getFullName().getX3());
			inst.setInstReceiverX4(message.getReceiver().getFullName().getX4());
			inst.setInstReceiverBranchInfo(message.getReceiver().getFullName().getBranchInformation());
			inst.setInstReceiverCityName(message.getReceiver().getFullName().getCityName());
			inst.setInstReceiverCtryCode(message.getReceiver().getFullName().getCountryCode());
			inst.setInstReceiverInstitutionName(message.getReceiver().getFullName().getFinancialInstitution());
			inst.setInstReceiverLocation(message.getReceiver().getFullName().getLocation());
		} else {
			inst.setInstReceiverX1("XXXXXXXXXXX");
		}

		String valueToConcat = "CORR_TYPE_";
		if (inst.getInstReceiverX4() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "INDIVIDUAL");
		} else if (inst.getInstReceiverX3() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "APPLICATION");
		} else if (inst.getInstReceiverX2() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "DEPARTMENT");
		} else if (inst.getInstReceiverX1() != null) {
			inst.setInstReceiverCorrType(valueToConcat + "INSTITUTION");
		} else {
			inst.setInstReceiverCorrType(valueToConcat + "N_A");
		}

		if (message.getReceiver() != null) {
			inst.setInstResponderDn(message.getReceiver().getDN());
		}

		if (message.getSecurityInfo() != null && message.getSecurityInfo().getSWIFTNetSecurityInfo() != null) {
			if (message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested() != null) {
				Boolean isNRRequested = message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested();
				inst.setInstNrIndicator(isNRRequested ? new BigDecimal(1) : new BigDecimal(0));
			}
			inst.setInstCbtReference(message.getNetworkInfo().getSWIFTNetNetworkInfo().getReference());
		}
		if (loaderMessage.getMessageSequenceNo() == null) {

			LOGGER.debug("Finish parsing instance message :: " + loaderMessage.getMesg().getMesgUumid() + " :: with the following details :: " + inst.toString());

		} else {
			LOGGER.debug("Finish parsing instance message :: " + loaderMessage.getMessageSequenceNo() + " :: with the following details :: " + inst.toString());
		}
		return inst;
	}
}
