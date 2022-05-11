package com.eastnets.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.configuration.AppConfig;
import com.eastnets.entities.Instance;
import com.eastnets.entities.InstancePK;
import com.eastnets.entities.Intervention;
import com.eastnets.entities.IntvPK;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.PKMesg;
import com.eastnets.entities.PKText;
import com.eastnets.entities.Text;
import com.eastnets.models.MessageRequest;
import com.eastnets.models.MessageResponse;
import com.eastnets.repositories.InterventionRepository;
import com.eastnets.repositories.MesgRepository;
import com.eastnets.repositories.TextRepository;
import com.eastnets.util.Constants;
import com.eastnets.util.DateTimeUtil;

@Service
public class MessageServiceImpl implements MessageService {

	private static final Logger LOGGER = LogManager.getLogger(MessageServiceImpl.class);

	@Autowired
	private MesgRepository mesgRepository;
	@Autowired
	private TextRepository textRepository;
	@Autowired
	private InterventionRepository intvRepository;
	@Autowired
	private AppConfig appConfig;

	@Override
	public Mesg getMesgs() {
		PKMesg pKMesg = new PKMesg(1l, 620077548l, -288l);
		Optional<Mesg> mesgBean = mesgRepository.findMesgByMsgId(pKMesg);
		return mesgBean.get();

	}

	public boolean isMessageExist(PKMesg pkMesg) {
		Optional<Mesg> mesgOptional = mesgRepository.findMesgByMsgId(pkMesg);
		return mesgOptional.isPresent();

	}

	public boolean isTextExist(PKText pkText) {
		Optional<Text> textOptional = textRepository.findTextByTextId(pkText);
		return textOptional.isPresent();
	}

	public boolean isInterventionExist(IntvPK intvPK) {
		Optional<Intervention> intvOptional = intvRepository.findInterventionByIntvId(intvPK);
		return intvOptional.isPresent();
	}

	public Mesg messageParser(MessageRequest message, long aid, long umidl, long umidh,
			LocalDateTime mesgCreateDateTime) throws Exception {
		PKMesg pKMesg = new PKMesg();
		pKMesg.setAid(aid);
		pKMesg.setUmidl(umidl);
		pKMesg.setUmidh(umidh);

		Mesg mesg = new Mesg();
		mesg.setMsgId(pKMesg);
		String uumid = message.getMesgSubFormat()
				+ (message.getMesgSubFormat().equalsIgnoreCase("I") ? message.getxReceiverX1() : message.getSender())
				+ message.getMesgType() + message.getMesgTrnRef();
		mesg.setMesgUumid(uumid);
		mesg.setMesgCreaMpfnName(message.getCurrentStatusName());
		mesg.setMesgCreaRpName(message.getCurrentStatusName());
		mesg.setMesgCreaOperNickname(message.getCreatedByName());
		mesg.setMesgCreaDateTime(mesgCreateDateTime);
		mesg.setMesgModOperNickname(message.getCreatedByName());
		mesg.setMesgModDateTime(mesgCreateDateTime);
		mesg.setMesgFrmtName(message.getMesgFrmtName());
		mesg.setMesgNature(Constants.MESG_NATURE);
		mesg.setMesgSenderX1(message.getSender());
		mesg.setMesgSenderCorrType(Constants.MESG_SENDER_CORR_TYPE);
		Long uumidSuffix = Long.valueOf(umidl);
		mesg.setMesgUumidSuffix(uumidSuffix);
		String xOwnLt = message.getMesgSubFormat().equalsIgnoreCase("I")
				? message.getMesgSenderSwiftAddress().substring(0, 8)
				: message.getMesgReceiverSwiftAddress().substring(0, 8);
		mesg.setxOwnLt(xOwnLt);
		mesg.setxInst0UnitName(message.getUnitName());
		mesg.setxCategory(message.getMesgType().substring(0, 1));
		mesg.setArchived(Constants.ARCHIVED);
		mesg.setMesgStatus(message.getMesgStatus() ? Constants.MESG_STATUS_LIVE : Constants.MESG_STATUS_COMPELTED);
		mesg.setMesgFinCcyAmount(message.getxFinCcy() + " " + message.getxFinAmount());
		mesg.setMesgFinValueDate(message.getValueDate());
		mesg.setMesgIsLive(message.getMesgStatus() ? Constants.MESG_LIVE : Constants.MESG_COMPLETED);
		mesg.setMesgIsRetrieved(Constants.MESG_IS_RETRIEVED);
		mesg.setMesgMesgUserGroup(message.getMesgUserGroup());
		// TODO
		mesg.setMesgNetworkApplInd("FIN");
		mesg.setMesgNetworkPriority(
				message.getMesgNetworkPriority().equalsIgnoreCase("N") ? Constants.MESG_NETWORK_PRIORITY_NORMAL
						: Constants.MESG_NETWORK_PRIORITY_URGENT);
		mesg.setMesgReceiverSwiftAddress(message.getMesgReceiverSwiftAddress());
		mesg.setMesgRelTrnRef(message.getMesgRelTrnRef());
		mesg.setMesgSenderSwiftAddress(message.getMesgSenderSwiftAddress());
		mesg.setMesgSubFormat(message.getMesgSubFormat().equalsIgnoreCase("I") ? Constants.MESG_SUB_FORMAT_I
				: Constants.MESG_SUB_FORMAT_O);
		mesg.setMesgTrnRef(message.getMesgTrnRef());
		mesg.setMesgType(message.getMesgType());
		mesg.setxFinCcy(message.getxFinCcy());
		mesg.setxFinAmount(message.getxFinAmount());

		if (message.getValueDate() != null && !message.getValueDate().isEmpty()) {
			mesg.setxFinValueDate(DateTimeUtil.convertStringToLocalDate(message.getValueDate()));
		}

		mesg.setxReceiverX1(message.getxReceiverX1());
		mesg.setMesgE2eTransactionReference(message.getMesgE2ETransactionReference());
		mesg.setMesgSLA(message.getMesgSla());
		mesg.setxInst0RpName(message.getMesgStatus() ? message.getCurrentStatusName() : null);
		mesg.setMesgIdentifier("fin." + message.getMesgType());
		mesg.setMesgSyntaxTableVer(message.getMesgSyntaxVersion());
		return mesg;

	}

	public Instance instanceParser(MessageRequest message, long aid, long umidl, long umidh,
			LocalDateTime mesgCreateDateTime) {

		InstancePK instancePK = new InstancePK();

		instancePK.setAid(aid);
		instancePK.setInstSUmidl(umidl);
		instancePK.setInstSUmidh(umidh);
		instancePK.setInstNum(Long.valueOf(Constants.INST_NUM));

		Instance instanceBean = new Instance();

		instanceBean.setId(instancePK);
		instanceBean
				.setInstStatus(message.getMesgStatus() ? Constants.INST_STATUS_LIVE : Constants.INST_STATUS_COMPELTED);
		instanceBean.setInstAppeDateTime(mesgCreateDateTime);
		instanceBean.setInstAppeSeqNbr(Constants.INST_APPE_SEQ_NBR);
		instanceBean.setInstUnitName(message.getUnitName());
		instanceBean.setInstLastMpfnResult(Constants.INST_LAST_MPFN_RESULT);
		instanceBean.setInstSm2000Priority(Constants.INST_SM2000_PRIORITY);
		instanceBean.setInstDeferredTime(mesgCreateDateTime);
		instanceBean.setInstCreaRpName(message.getCurrentStatusName());
		instanceBean.setInstCreaDateTime(mesgCreateDateTime);
		instanceBean.setInstRpName(message.getMesgStatus() ? message.getCurrentStatusName() : null);
		instanceBean.setInstMpfnName(message.getCurrentStatusName());
		instanceBean.setInstReceiverX1(message.getxReceiverX1());

		return instanceBean;

	}

	public Text textParser(MessageRequest message, long aid, long umidl, long umidh, LocalDateTime mesgCreateDateTime) {
		PKText pKText = new PKText();
		pKText.setAid((long) appConfig.getLdSettingConfig().getAid());
		pKText.setTextSUmidl(Long.valueOf(umidl));
		pKText.setTextSUmidh((long) umidh);

		Text text = new Text();
		text.setTextId(pKText);
		text.setTextToken(Constants.TEXT_TOKEN);

		int indexOfBlockFour = message.getTextDataBlock().indexOf("{4:");
		int indexOfBlockFive = message.getTextDataBlock().lastIndexOf("{5:");

		String blockFour = null;
		if (indexOfBlockFour > 0) { // some of system message doesn't has block4

			if (indexOfBlockFive > 0) {

				blockFour = message.getTextDataBlock().substring(indexOfBlockFour + 3, indexOfBlockFive).trim();
				if (blockFour.endsWith("\r\n-}"))
					blockFour = blockFour.substring(0, blockFour.length() - 4);

				else if (blockFour.endsWith("-}"))

					blockFour = blockFour.substring(0, blockFour.length() - 2);
			} else {
				int endIndex = message.getTextDataBlock().lastIndexOf("\r\n-}");
				if (endIndex < 0) {
					endIndex = message.getTextDataBlock().lastIndexOf("-}");
				}
				blockFour = message.getTextDataBlock().substring(indexOfBlockFour + 3, endIndex).trim();
			}
		}

		if (blockFour != null && !blockFour.isEmpty()) {
			blockFour = blockFour.replace("\n", "\r\n");
			text.setTextDataBlock("\r\n" + blockFour.trim());
			text.setTextDataBlockLen(message.getTextDataBlock().length());
		}

		return text;
	}

	public Intervention interventionParser(MessageRequest message, long aid, long umidl, long umidh,
			LocalDateTime mesgCreateDateTime) {

		IntvPK intvPK = new IntvPK();

		intvPK.setAid(aid);
		intvPK.setIntvSUmidl(umidl);
		intvPK.setIntvSUmidh(umidh);
		intvPK.setIntvInstNum(Constants.INST_NUM);
		intvPK.setIntvDateTime(mesgCreateDateTime);
		intvPK.setIntvSeqNo(1);

		String mergedText = null;
		String instanceName = null;
		Integer instanceNo;

		if (isInterventionExist(intvPK)) {
			mergedText = Constants.INTV_INSTANCE_STATUS_ROUTED + " to " + message.getCurrentStatusName();
			instanceName = Constants.INTV_INSTANCE_NAME_ROUTED;
			instanceNo = Constants.INTV_INSTANCE_NO_ROUTED;
		} else {
			mergedText = Constants.INTV_INSTANCE_STATUS_CREATED + " at " + message.getCurrentStatusName();
			instanceName = Constants.INTV_INSTANCE_NAME_CREATED;
			instanceNo = Constants.INTV_INSTANCE_NO_CREATED;
		}

		// completed
		if (!message.getMesgStatus()) {
			mergedText = Constants.INTV_INSTANCE_STATUS_COMPLETED;
			instanceName = Constants.INTV_INSTANCE_NAME_COMPLETED;
			instanceNo = Constants.INTV_INSTANCE_NO_COMPLETED;
		}

		Integer seqNo = intvRepository.maxSeqNo(aid, umidl, umidh, Constants.INST_NUM, mesgCreateDateTime);
		seqNo = (Optional.ofNullable(seqNo).orElse(0)) + 1;

		Intervention interventionBean = new Intervention();

		intvPK.setIntvSeqNo(seqNo);
		interventionBean.setIntvId(intvPK);
		interventionBean.setIntvIntyNo(instanceNo);
		interventionBean.setIntvIntyName(instanceName);
		interventionBean.setIntvIntyCategory(Constants.INTV_INSTANCE_CATEGORY_ROUTED);
		interventionBean.setIntvOperNickname(message.getCreatedByName());
		interventionBean.setIntvApplServName(message.getSource());
		interventionBean.setIntvMpfnName(message.getCurrentStatusName());
		interventionBean.setIntvAppeDateTime(mesgCreateDateTime);
		interventionBean.setIntvAppeSeqNo(seqNo);
		interventionBean.setIntvToken(Constants.INTV_TOKEN);
		interventionBean.setIntvLength(mergedText.length());
		interventionBean.setIntvMergedText(mergedText);

		return interventionBean;

	}

	@Transactional
	@Override
	public MessageResponse saveMessage(MessageRequest message) {
		MessageResponse messageResponse = new MessageResponse();

		Mesg messageBean = new Mesg();
		Text textBean = new Text();
		Instance instanceBean = new Instance();
		Intervention intervetionBean = new Intervention();
		List<Instance> instancesList = new ArrayList<>();
		List<Intervention> intervetionList = new ArrayList<>();
		boolean isMessageExist = false;

		try {

			long aid = (long) appConfig.getLdSettingConfig().getAid();
			String umidlStr = message.getMesgCreaDateTime().substring(0, 10);
			long umidl = Long.valueOf(umidlStr);
			long umidh = (long) Integer.valueOf(message.getId()) * -1;
			LocalDateTime mesgCreateDateTime = DateTimeUtil.convertStringToLocalDateTime(message.getMesgCreaDateTime());

			LOGGER.debug("Start mapping Message fields");
			messageBean = messageParser(message, aid, umidl, umidh, mesgCreateDateTime);
			LOGGER.debug("Message fields mapped susscessfully");

			LOGGER.debug("Start mapping Text fields");
			textBean = textParser(message, aid, umidl, umidh, mesgCreateDateTime);
			LOGGER.debug("Text fields mapped susscessfully");

			LOGGER.debug("Start mapping Instance fields");
			instanceBean = instanceParser(message, aid, umidl, umidh, mesgCreateDateTime);
			LOGGER.debug("Instance fields mapped susscessfully");

			LOGGER.debug("Start mapping Intervention fields");
			intervetionBean = interventionParser(message, aid, umidl, umidh, mesgCreateDateTime);
			LOGGER.debug("Intervention fields mapped susscessfully");

			instancesList.add(instanceBean);
			messageBean.setInstances(instancesList);

			intervetionList.add(intervetionBean);
			messageBean.setIntervention(intervetionList);

			isMessageExist = isMessageExist(messageBean.getMsgId());
			mesgRepository.save(messageBean);
			/*
			 * text -- i will save the text without using the relation with mesg entity
			 * because there is a bug in Hibernate when we want to merge one-to-one relation
			 */
			textRepository.save(textBean);

			if (!isMessageExist) {

				LOGGER.info("The Message with Umidh: " + messageBean.getMsgId().getUmidh() + " and Umidl: "
						+ messageBean.getMsgId().getUmidl() + " was inserted successfully.");

				messageResponse.setStatusCode(HttpStatus.OK);
				messageResponse.setStatusMsg("The Message with Umidh: " + messageBean.getMsgId().getUmidh()
						+ " and Umidl: " + messageBean.getMsgId().getUmidl() + " was inserted successfully.");

			} else {
				LOGGER.info("The Message with Umidh: " + messageBean.getMsgId().getUmidh() + " and Umidl: "
						+ messageBean.getMsgId().getUmidl() + " was updated successfully.");

				messageResponse.setStatusCode(HttpStatus.CREATED);
				messageResponse.setStatusMsg("The Message with Umidh: " + messageBean.getMsgId().getUmidh()
						+ " and Umidl: " + messageBean.getMsgId().getUmidl() + " was updated successfully.");

			}

		} catch (Exception ex) {
			messageResponse.setStatusCode(HttpStatus.EXPECTATION_FAILED);
			messageResponse.setStatusMsg("failed to save the Message: " + ex.getMessage());

			LOGGER.error("failed to save the Message: " + ex.getMessage());

		}

		return messageResponse;

	}

}
