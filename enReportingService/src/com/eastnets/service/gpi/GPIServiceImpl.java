package com.eastnets.service.gpi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.CorrInfo;
import com.eastnets.domain.viewer.AppendixDetails;
import com.eastnets.domain.viewer.CoveType;
import com.eastnets.domain.viewer.DaynamicMsgDetailsParam;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.EndPointStatus;
import com.eastnets.domain.viewer.GpiAgent;
import com.eastnets.domain.viewer.GpiConfirmation;
import com.eastnets.domain.viewer.GpiType;
import com.eastnets.domain.viewer.LineStatus;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.MessageKey;
import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.service.ServiceLocator;
import com.eastnets.utils.Utils;

public class GPIServiceImpl implements GPIService {

	private GPIProperties gpiProperties = new GPIProperties();
	private List<GpiAgent> messageList;
	private GpiType gpiType;
	private List<GpiAgent> coveAgents;
	private List<GpiAgent> beforeIntermediateAgents;
	private ServiceLocator serviceLocator;
	private Map<String, CorrInfo> bicExpandedMap;
	private ViewerDAO viewerDAO;

	// parsing REGEX
	private static final Pattern pattern = Pattern.compile(":(\\w+?):(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern pattern77e = Pattern.compile(":77E:(.*)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	private static final String DLVACKED = "DLV_ACKED";

	private boolean needFormater = false;
	boolean showCreditedAmountBeforeConvert;
	boolean useFormatAmount;
	private boolean convertCharges;
	private boolean calculateChargesWhenZero;
	private boolean checkOnlyonBic6;
	private Map<String, Integer> currencies;
	private boolean show71FCharges;
	boolean first103 = true;

	String completedChargesType = "";
	boolean addedCompletedChargesType = false;
	List<String> chargesTypeList = new ArrayList<>();
	List<GpiConfirmation> confirmationListAlradyChecked = new ArrayList<GpiConfirmation>();

	boolean calcluteChargesAlradyChecked = false;

	public void init() {
		currencies = viewerDAO.getCurrenciesISO();
	}

	/**
	 * This method used For Update the Tracker Using confirmation messes
	 * 
	 * @return GPIServiceBean This contain List of GpiAgent
	 */

	@Override
	public GPIServiceBean getGpiLists(String userName, String uetr, String trackerBic, String acountNumber, String msgDirection, boolean showStopRecal, boolean isViewerTraker, String fromDate, String toDate, MessageDetails details,
			boolean showCreditedAmountBeforeConvert, boolean useFormatAmount, boolean convertCharges, boolean calculateChargesWhenZero, boolean checkOnlyonBic6, boolean show71FCharges) {

		try {
			this.showCreditedAmountBeforeConvert = false;
			this.convertCharges = false;
			chargesTypeList = new ArrayList<>();
			bicExpandedMap = new HashMap<String, CorrInfo>();
			if (isViewerTraker) {
				gpiProperties.setMessageDetails(details);
			} else {
				MessageKey key = serviceLocator.getViewerService().getMessageKeyByUetr(uetr, acountNumber, msgDirection, isViewerTraker, fromDate, toDate);
				if (key == null)
					return null;
				DaynamicMsgDetailsParam daynamicMsgDetailsParam = buildLaodMsgParam(false, false, key);
				gpiProperties.setMessageDetails(serviceLocator.getViewerService().getMessageDetails(daynamicMsgDetailsParam));
			}
			this.showCreditedAmountBeforeConvert = showCreditedAmountBeforeConvert;
			this.convertCharges = convertCharges;
			this.useFormatAmount = useFormatAmount;
			this.calculateChargesWhenZero = calculateChargesWhenZero;
			this.checkOnlyonBic6 = checkOnlyonBic6;
			this.show71FCharges = show71FCharges;
			gpiProperties.setShow71FCharges(show71FCharges);
			gpiProperties.setShowCreditedAmountBeforeConvert(showCreditedAmountBeforeConvert);
			gpiProperties.setUniqueEndToEndTransactionReference(uetr);
			getAvailableGpiAgent(trackerBic, showStopRecal);
			fillBeforeIntermediateAgents(userName, gpiProperties.getMessageDetails().getMesgSenderX1(), gpiProperties.getMessageDetails().getInstReceiverX1());
			GPIServiceBean gpiServiceBean = new GPIServiceBean();
			gpiServiceBean.setMessageList(messageList);
			gpiServiceBean.setCoveAgents(coveAgents);
			gpiServiceBean.setGpiProperties(gpiProperties);
			gpiServiceBean.setBeforeIntermediateAgents(beforeIntermediateAgents);
			completedChargesType = "";
			addedCompletedChargesType = false;
			confirmationListAlradyChecked = new ArrayList<GpiConfirmation>();
			calcluteChargesAlradyChecked = false;
			first103 = true;
			return gpiServiceBean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// check and fill the out put message before sender
	private void fillBeforeIntermediateAgents(String userName, String senderBic, String reciver) {
		beforeIntermediateAgents = null;
		DetailsHistory inComingMessage = getMessageFromDetailsHistory("Output", "");
		boolean withinitStopAndRecallRequst = checkForgSRP();
		MessageDetails messageDetails = null;
		if (inComingMessage != null && checkIfIncomingBicEqualToSenderOrReciver(inComingMessage, senderBic, reciver)) {
			if (!inComingMessage.isComesFrom199Updated()) {
				try {
					messageDetails = serviceLocator.getViewerService()
							.getMessageDetails(buildMsgDetailsParam(userName, inComingMessage.getAid(), inComingMessage.getMesgUmidl(), inComingMessage.getMesgUmidh(), inComingMessage.getMesgCreaDateTime(), false, false));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beforeIntermediateAgents = new ArrayList<>();
				GpiAgent senderFromOutputMessage = gpiAgentCreator(getCityName(inComingMessage.getSenderBic()), getCountryName(inComingMessage.getSenderBic()), getInstitutionName(inComingMessage.getSenderBic()), inComingMessage.getSenderBic(),
						inComingMessage.getMesgTrnRef(), inComingMessage.getMesgCreaDateTime(), LineStatus.Arrived, EndPointStatus.ReceivedAndSend, true, inComingMessage.getMesgCreaDateTimeStr(), "", inComingMessage.getSenderDeducts(),
						inComingMessage.getSenderDeductsCur(), inComingMessage.getMesgCopyServiceId());
				senderFromOutputMessage.setMesgDirction(gpiProperties.getMessageDetails().getMesgSubFormat());
				GpiAgent ordrinCurField52 = new GpiAgent();
				if (messageDetails != null) {
					String mesgUnExpandedText = messageDetails.getMesgUnExpandedText();
					if (mesgUnExpandedText != null && mesgUnExpandedText.contains("52A")) {
						ordrinCurField52.setSenderBic(getInstitutionName(inComingMessage.getOrdringInstution()));
						ordrinCurField52.setFiled52(true);
						ordrinCurField52.setFiled52NotA(false);
					} else {
						ordrinCurField52.setSenderBic(inComingMessage.getOrdringInstution());
						ordrinCurField52.setFiled52(false);
						ordrinCurField52.setFiled52NotA(true);
					}
				}
				beforeIntermediateAgents.add(ordrinCurField52);
				if (withinitStopAndRecallRequst) {
					senderFromOutputMessage.setgSRPParty(true);
					senderFromOutputMessage.setgSRPMsg("Stop & recall request initiated");
				}
				beforeIntermediateAgents.add(senderFromOutputMessage);

			} else {
				beforeIntermediateAgents = new ArrayList<>();
				GpiAgent senderFromOutputMessage = gpiAgentCreator(getCityName(inComingMessage.getSenderBic()), getCountryName(inComingMessage.getSenderBic()), getInstitutionName(inComingMessage.getSenderBic()), inComingMessage.getSenderBic(),
						inComingMessage.getMesgTrnRef(), inComingMessage.getMesgCreaDateTime(), LineStatus.Arrived, EndPointStatus.ReceivedAndSend, true, inComingMessage.getMesgCreaDateTimeStr(), "", inComingMessage.getSenderDeducts(),
						inComingMessage.getSenderDeductsCur(), inComingMessage.getMesgCopyServiceId());
				senderFromOutputMessage.setMesgDirction(gpiProperties.getMessageDetails().getMesgSubFormat());
				GpiAgent ordrinCurField52 = new GpiAgent();
				ordrinCurField52.setSenderBic("");
				ordrinCurField52.setFiled52(false);
				ordrinCurField52.setFiled52NotA(false);

				if (withinitStopAndRecallRequst) {
					senderFromOutputMessage.setgSRPParty(true);
					senderFromOutputMessage.setgSRPMsg("Stop & recall request initiated");
				}

				beforeIntermediateAgents.add(senderFromOutputMessage);
			}
		}

	}

	private boolean checkIfIncomingBicEqualToSenderOrReciver(DetailsHistory inComingMessage, String senderBic, String reciver) {
		if (!checkOnlyonBic6) {
			if (!inComingMessage.getSenderBic().equalsIgnoreCase(senderBic) && !inComingMessage.getSenderBic().equalsIgnoreCase(reciver)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (!inComingMessage.getSenderBic().substring(0, 7).equalsIgnoreCase(senderBic.substring(0, 7)) && !inComingMessage.getSenderBic().substring(0, 7).equalsIgnoreCase(reciver.substring(0, 7))) {
				return true;
			} else {
				return false;
			}
		}

	}

	private boolean checkForgSRP() {
		List<GpiConfirmation> gsrpMessagesList = serviceLocator.getViewerService().getGpiAgent("", gpiProperties.getUniqueEndToEndTransactionReference(), gpiProperties.getTimeZoneOffset(), gpiProperties.getParam().getSourceSelectedSAA(), true,
				gpiProperties.getMessageDetails().getMesgCreaDateTime(), "'199'", "'192'", "'196'");

		return prepareInitiateGSRPForIntemadaryCase(gsrpMessagesList);
	}

	private DaynamicMsgDetailsParam buildMsgDetailsParam(String userName, int aid, int mesgUmidl, int mesgumidh, Date mesgCreaDateTime, boolean includeHistory, boolean includeMessageNotes) {
		DaynamicMsgDetailsParam daynamicMsgDetailsParam = new DaynamicMsgDetailsParam();
		daynamicMsgDetailsParam.setLoggedInUser(userName);
		daynamicMsgDetailsParam.setAid(aid);
		daynamicMsgDetailsParam.setUmidh(mesgumidh);
		daynamicMsgDetailsParam.setUmidl(mesgUmidl);
		daynamicMsgDetailsParam.setMesg_crea_date(mesgCreaDateTime);
		daynamicMsgDetailsParam.setTimeZoneOffset(0);
		daynamicMsgDetailsParam.setIncludeHistory(includeHistory);
		daynamicMsgDetailsParam.setIncludeMessageNotes(includeMessageNotes);
		return daynamicMsgDetailsParam;

	}

	public GPIProperties getGpiProperties() {
		return gpiProperties;
	}

	public void setGpiProperties(GPIProperties gpiProperties) {
		this.gpiProperties = gpiProperties;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public void getAvailableGpiAgent(String trackerBic, boolean showStopRecal) {
		try {

			List<GpiConfirmation> gpiConfirmationsList = serviceLocator.getViewerService().getGpiAgent(trackerBic, gpiProperties.getMessageDetails().getUetr(), 0, gpiProperties.getParam().getSourceSelectedSAA(), false,
					gpiProperties.getMessageDetails().getMesgCreaDateTime(), "'199'", "'299'", "'196'");
			prepareAvailableAgent(gpiConfirmationsList);
			if (showStopRecal) {
				prepareGSRP(gpiConfirmationsList, trackerBic);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepperStatusDiv(String statusCode) {
		if ((statusCode.equalsIgnoreCase("ACSC") || statusCode.equalsIgnoreCase("ACCC")) && !gpiProperties.isRejectPayment()) {
			gpiProperties.setStatusMessageGpi("Completed");
			gpiProperties.setSatausImage("/images/completedGpi.png");
			gpiProperties.setStatusStyle("completed");
		} else if (statusCode.equalsIgnoreCase("ACSP") && !gpiProperties.isRejectPayment()) {
			gpiProperties.setStatusMessageGpi("In Progress");
			gpiProperties.setSatausImage("/images/settingGPI.png");
			gpiProperties.setStatusStyle("progres");
		} else if (statusCode.equalsIgnoreCase("RJCT")) {
			gpiProperties.setStatusMessageGpi("Rejected");
			gpiProperties.setSatausImage("/images/rejectIcone.png");
			gpiProperties.setStatusStyle("progres");
			gpiProperties.setRejectPayment(true);
		} else if (statusCode.equalsIgnoreCase("CNCL")) {
			gpiProperties.setStatusMessageGpi("Cancelled");
			gpiProperties.setSatausImage("/images/rejectIcone.png");
			gpiProperties.setStatusStyle("progres");
			gpiProperties.setRejectPayment(true);
		} else {
			if (!gpiProperties.isRejectPayment()) {
				gpiProperties.setStatusMessageGpi("In Progress");
				gpiProperties.setSatausImage("/images/settingGPI.png");
				gpiProperties.setStatusStyle("progres");
			}
		}
	}

	private DaynamicMsgDetailsParam buildLaodMsgParam(boolean includeHistory, boolean includeMessageNotes, MessageKey key) {
		int aid = key.getAid();
		int umidl = key.getUnidl();
		int umidh = key.getUmidh();
		String mesgCreaDateTimeStr = key.getMesgCreaDateTimeStr();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date mesgCreaDate = null;
		try {
			mesgCreaDate = sdf.parse(mesgCreaDateTimeStr);
		} catch (Exception e) {
			throw new WebClientException("the parameter mesgCreaDateTime passed to ViewerController.loadMessage() is not valid, error message : " + e.getMessage());
		}
		DaynamicMsgDetailsParam daynamicMsgDetailsParam = new DaynamicMsgDetailsParam();
		daynamicMsgDetailsParam.setLoggedInUser("side");
		daynamicMsgDetailsParam.setAid(aid);
		daynamicMsgDetailsParam.setUmidh(umidh);
		daynamicMsgDetailsParam.setUmidl(umidl);
		daynamicMsgDetailsParam.setMesg_crea_date(mesgCreaDate);
		daynamicMsgDetailsParam.setTimeZoneOffset(0);
		daynamicMsgDetailsParam.setIncludeHistory(includeHistory);
		daynamicMsgDetailsParam.setIncludeMessageNotes(includeMessageNotes);
		return daynamicMsgDetailsParam;
	}

	/**
	 * This method used For Update the Tracker Using confirmation messes
	 * 
	 * @param gpiConfirmationsList
	 *            List of confirmation messages 199 ,299
	 * @return void
	 */

	private void prepareAvailableAgent(List<GpiConfirmation> gpiConfirmationsList) {
		gpiProperties.resetGpiValue();
		prepperStatusDiv("");
		prepperStatusDivCov("");
		messageList = initialTrackerNode();
		fillGpiDetailsHistories();

		String first103Charges = getChargesFromOld103();
		if (first103Charges != null && !first103Charges.isEmpty()) {
			first103 = false;
			chargesTypeList.add(first103Charges);
		} else {
			first103 = true;
			String mesgCharges = gpiProperties.getMessageDetails().getMesgCharges();
			if (mesgCharges != null && !mesgCharges.isEmpty()) {
				chargesTypeList.add(mesgCharges);

			}
		}

		// Update Incoming message Even if we don't have 199 confirmation , thats done using input message
		// This is for Convert 103 input message to confirmation messes to update the Tracker UI
		// and we must sort gpiConfirmationsList to set the new confirmation on correct position based on mesgCreatDataTime
		if (gpiProperties.getGpiType().equals(GpiType.With_Beneficiary_Customer)) {
			GpiConfirmation updateIncominMessages = getIncoming103AsConfirmationMessage();
			if (updateIncominMessages != null) {
				// String networkDeliveryStatus = getNetworkDeliveryStatus(updateIncominMessages.getAid(), updateIncominMessages.getMesgUmidl(), updateIncominMessages.getMesgUmidh(),
				// updateIncominMessages.getMesgCreaDateTime());
				if (updateIncominMessages.getMesgNakedCode() == null || updateIncominMessages.getMesgNakedCode().isEmpty()) {
					gpiConfirmationsList.add(updateIncominMessages);
					Collections.sort(gpiConfirmationsList);
				}
			}

		}

		// Update Cover using 202 out messages
		GpiConfirmation extra299 = getIncoming202AsConfirmationMessage();
		if (extra299 != null) {
			if (gpiConfirmationsList != null) {
				gpiConfirmationsList.add(extra299);
				Collections.sort(gpiConfirmationsList);
			}
		}

		// InitialDraw this flag for Draw tracker if there is no confirmation message , so no update InitialDraw = true
		if ((gpiConfirmationsList.isEmpty())) {
			gpiProperties.setInitialDraw(true);
			if (gpiProperties.getGpiType().equals(GpiType.Without_Beneficiary_Customer)) {
				messageList.get(messageList.size() - 1).setLastElement(true);
			}
			return;
		}
		HashMap<String, List<GpiConfirmation>> confirmationMap = getConfirmationMap(gpiConfirmationsList);
		if (confirmationMap.get("199").isEmpty()) {
			gpiProperties.setInitialDraw(true);
		} else {
			prepareDataBasedOnGpiType(confirmationMap.get("199"));
		}
		prepareCoveConfirmation(confirmationMap.get("299"), getCoveAgents());

	}

	private String getChargesFromOld103() {
		try {
			String mesgSenderX1 = gpiProperties.getMessageDetails().getMesgSenderX1();
			List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
			if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
				return "";
			}

			DetailsHistory detailsHistory = detailsHistoriesList.get(detailsHistoriesList.size() - 1);
			if (mesgSenderX1.substring(0, 7).equalsIgnoreCase(detailsHistory.getSenderBic().substring(0, 7))) {
				// do nothing
			} else {
				GpiConfirmation gpiConfirmation = new GpiConfirmation();
				gpiConfirmation.setStatusCode("ACSP");
				gpiConfirmation.setReasonCode("G000");
				gpiConfirmation.setAid(detailsHistory.getAid());
				gpiConfirmation.setMesgUmidl(detailsHistory.getMesgUmidl());
				gpiConfirmation.setMesgUmidh(detailsHistory.getMesgUmidh());
				gpiConfirmation.setMesgCreaDateTime(detailsHistory.getMesgCreaDateTime());
				gpiConfirmation.setStatusOriginator(detailsHistory.getSenderBic());
				gpiConfirmation.setForwardedTo(detailsHistory.getMesgTrnRef());
				gpiConfirmation.setMesgCreaDateTime(detailsHistory.getMesgCreaDateTime());
				gpiConfirmation.setSendDate(detailsHistory.getMesgCreaDateTimeStr());
				gpiConfirmation.setMsgSendAmount(detailsHistory.getSenderDeducts());
				gpiConfirmation.setMsgSendCurr(detailsHistory.getSenderDeductsCur());
				gpiConfirmation.setMesgType("199");
				confirmationListAlradyChecked.add(gpiConfirmation);
				return detailsHistory.getMesgCharges();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	/**
	 * This method used to determine witch tracker type will proceed thats based on 75A Filed if its BIC or not
	 * 
	 * @param gpiConfirmationsList
	 *            List of confirmation messages 199
	 * @return void
	 */
	private void prepareDataBasedOnGpiType(List<GpiConfirmation> gpiConfirmationsList) {
		if (gpiProperties.getGpiType().equals(GpiType.With_Beneficiary_Customer)) {
			prepareWithBeneficiaryCustmerCsase(gpiConfirmationsList);
		} else if (gpiProperties.getGpiType().equals(GpiType.Without_Beneficiary_Customer)) {
			// Based on current 199 Confirmation messages , may be there is a new Agents you need to added on the main message list
			// this is very dynamic way may be will user for others
			preperMsgList(gpiConfirmationsList);
			// Because now we don't know where we must set this flag "LastElement" , we must wait until preperMsgList(gpiConfirmationsList); called
			messageList.get(messageList.size() - 1).setLastElement(true);
			// After we done with Message List so now we know what we have as a Agents , so all we need to peeper every agent =(items on message list )
			// with updates status code , reason code , amount ....etc.
			prepareWithoutBeneficiaryCustmerCsase(gpiConfirmationsList);

		}
	}

	private void preperMsgList(List<GpiConfirmation> gpiConfirmationsList) {
		String statusCode;
		String reasonCode;
		String forwordedBic;
		for (GpiConfirmation confirmation : gpiConfirmationsList) {
			statusCode = confirmation.getStatusCode();
			reasonCode = confirmation.getReasonCode();
			forwordedBic = confirmation.getForwardedTo();
			if (statusCode != null && statusCode.equalsIgnoreCase("ACSP")) {
				if (reasonCode != null && (reasonCode.equalsIgnoreCase("G000") || reasonCode.equalsIgnoreCase("G001"))) {
					if (forwordedBic != null) {
						boolean bicExsist = bicExsist(forwordedBic);
						if (!bicExsist) {
							messageList.add(gpiAgentCreator(getCityName(forwordedBic), getCountryName(forwordedBic), getInstitutionName(forwordedBic), forwordedBic, "", null, LineStatus.receiveLine, EndPointStatus.IntermediateReceiveWithOutSend,
									false, confirmation.getSendDate(), "", "", "", ""));
							gpiProperties.getBicList().add(confirmation.getForwardedTo());

						}
					}
				}
			}
		}
	}

	// if (comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(1).getMessageBIC()) || comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(0).getMessageBIC())) {
	private void prepareAgentIncaseNoBeneficiaryCustmer(GpiAgent gpiAgent, List<GpiConfirmation> gpiConfirmationsList) {
		String messageBIC = gpiAgent.getMessageBIC();
		String confirmationMesgCharges = "";
		for (GpiConfirmation status : gpiConfirmationsList) {
			if ((status.getStatusCode().equals("ACSP") && (status.getReasonCode().equals("G000") || status.getReasonCode().equals("G001")))) {
				if (!status.isUpdateFromIncoming()) {
					if (!first103 || (!status.getStatusOriginator().substring(0, 7).equalsIgnoreCase(messageList.get(0).getMessageBIC().substring(0, 7)))) {
						if (confirmationListAlradyChecked.isEmpty() || !checkCongirmationAlradyExsist(status)) {

							String chargeOutputTypeRelated = getRecentOutputRelated199(status.getStatusOriginator(), status.getForwardedTo());
							// get it from related 199
							if (chargeOutputTypeRelated != null && !chargeOutputTypeRelated.isEmpty()) {
								chargesTypeList.add(chargeOutputTypeRelated);
								confirmationMesgCharges = chargeOutputTypeRelated;
							} else {
								String chargeTypeRelated = getRecentRelated199(status.getStatusOriginator(), status.getForwardedTo());
								// get it from related 199
								if (chargeTypeRelated != null && !chargeTypeRelated.isEmpty()) {
									chargesTypeList.add(chargeTypeRelated);
									chargeOutputTypeRelated = chargeTypeRelated;
								} else {
									String chargesType = status.getMesgCharges();
									if (chargesType != null && !chargesType.isEmpty()) {
										chargesTypeList.add(chargesType);
										chargeOutputTypeRelated = chargesType;
									} else {
										DetailsHistory correspondent103 = getCorrespondent103(status.getStatusOriginator(), status.getForwardedTo());
										if (correspondent103 != null) {
											if (correspondent103.getMesgCharges() != null && !correspondent103.getMesgCharges().isEmpty()) {
												chargesTypeList.add(correspondent103.getMesgCharges());
												chargeOutputTypeRelated = correspondent103.getMesgCharges();

											} else {
												if (chargesTypeList != null && !chargesTypeList.isEmpty()) {
													chargesTypeList.add(chargesTypeList.get(chargesTypeList.size() - 1));
													chargeOutputTypeRelated = chargesTypeList.get(chargesTypeList.size() - 1);
												}
											}
										} else {
											if (chargesTypeList != null && !chargesTypeList.isEmpty()) {
												chargesTypeList.add(chargesTypeList.get(chargesTypeList.size() - 1));
												chargeOutputTypeRelated = chargesTypeList.get(chargesTypeList.size() - 1);
											}
										}
									}
								}

							}

						}
					}
				}

			}

			if (!confirmationMesgCharges.isEmpty()) {
				status.setMesgCharges(confirmationMesgCharges);
			}

			if (messageBIC != null && status.getStatusOriginator() != null) {
				if (messageBIC.substring(0, 7).equalsIgnoreCase(status.getStatusOriginator().substring(0, 7)) || comparingTowBic(status.getStatusOriginator(), messageList.get(0).getMessageBIC())) {
					if (status.getStatusCode().equals("ACSC") || status.getStatusCode().equals("ACCC")) {
						drawAgentWithCurrentStatus(gpiAgent, "ACCC", status, EndPointStatus.ReceivedAndSend, LineStatus.receiveLine);
						sumDeducts(status.getMsgSendAmount(), status.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
						gpiProperties.setCreditedAmount(calculateCreditedAmount("BEN", status.getCreditedAmount(), status.getCreditedCur(), status.getMesgExchangeRate(), status.getExChangeRateFromCcy(), status.getExChangeRateToCcy()));
						gpiProperties.setShowDetailsCreditedAmount(true);
						gpiAgent.setMesgCharges(completedChargesType);

					} else if (status.getStatusCode().equals("RJCT")) {
						drawAgentWithCurrentStatus(gpiAgent, "RJCT", status, EndPointStatus.IntermediateRejectPayment, LineStatus.receiveLine);
					} else if (status.getStatusCode().equals("ACSP") && status.getReasonCode().equals("G000")) {
						if (gpiAgent.isLastElement()) {
							drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.ReceivedPaymentNotCreditedIt, LineStatus.receiveLine);
						} else {
							drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.ReceivedAndSend, LineStatus.receiveLine);
						}

						gpiAgent.setMesgCharges(status.getMesgCharges());
					} else if (status.getStatusCode().equals("ACSP") && status.getReasonCode().equals("G001")) {
						drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.NonTracabelBeneficiaryBank, LineStatus.receiveLine);
						gpiAgent.setMesgCharges(status.getMesgCharges());
					} else if (status.getStatusCode().equals("ACSP") && status.getReasonCode().equals("G002")) {
						drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.ReceivedPaymentNotCreditedIt, LineStatus.receiveLine);
					} else if (status.getStatusCode().equals("ACSP") && status.getReasonCode().equals("G003")) {
						drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.ReceivedPaymentNotCreditedIt, LineStatus.receiveLine);
					} else if (status.getStatusCode().equals("ACSP") && status.getReasonCode().equals("G004")) {
						drawAgentWithCurrentStatus(gpiAgent, "ACSP", status, EndPointStatus.ReceivedPaymentNotCreditedIt, LineStatus.receiveLine);
					}
				}
			}
			if (!status.isUpdateFromIncoming()) {
				confirmationListAlradyChecked.add(status);
			}

		}
		gpiProperties.setBeneficiaryBank(gpiAgent);
	}

	private boolean checkCongirmationAlradyExsist(GpiConfirmation status) {
		for (GpiConfirmation confirmation : confirmationListAlradyChecked) {
			if (confirmation.getStatusCode().equals("ACSP") && (confirmation.getReasonCode().equals("G000") || confirmation.getReasonCode().equals("G001"))) {
				if (confirmation.getStatusOriginator().equalsIgnoreCase(status.getStatusOriginator()) && confirmation.getForwardedTo().equalsIgnoreCase(status.getForwardedTo())) {
					return true;
				}
			}
		}
		return false;
	}

	private void prepareWithoutBeneficiaryCustmerCsase(List<GpiConfirmation> gpiConfirmationsList) {
		int index = 0;
		for (GpiAgent agent : messageList) {
			if (index == 0) {
				index++;
				continue;
			}
			prepareAgentIncaseNoBeneficiaryCustmer(agent, gpiConfirmationsList);
		}

	}

	private void preperCoveAgent(EndPointStatus endPointStatus, LineStatus lineStatus, GpiAgent agent) {
		agent.setEndPointStatus(endPointStatus);
		agent.setLineStatus(lineStatus);
		agent.setEndPointFormatted(getEndPointStr(endPointStatus));
	}

	private void prepareCoveConfirmation(List<GpiConfirmation> gpiConfirmationsList, List<GpiAgent> coveAgents) {
		if (coveAgents == null) {
			return;
		}

		for (GpiAgent agent : coveAgents) {
			for (GpiConfirmation confirmation : gpiConfirmationsList) {

				String statusOriginator = (confirmation.getStatusOriginator() != null && !confirmation.getStatusOriginator().isEmpty()) ? confirmation.getStatusOriginator().substring(0, 7) : "";
				String forwardedTo = (confirmation.getForwardedTo() != null && !confirmation.getForwardedTo().isEmpty() ? confirmation.getForwardedTo().substring(0, 7) : "");

				if (agent.getMessageBIC().substring(0, 7).equalsIgnoreCase(statusOriginator)) {
					if (confirmation.getStatusCode().equalsIgnoreCase("ACSP")) {
						prepperStatusDivCov("ACSP");
						if (confirmation.getReasonCode().equalsIgnoreCase("G000")) {
							preperCoveAgent(EndPointStatus.ReceivedAndSendCoverPayment, LineStatus.ARRIVED_COVE, agent);
							gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo().substring(0, 7), confirmation.getSendDate());
							agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
							agent.setSendDate(confirmation.getSendDate());
							agent.setDisableSendTimeIcone(false);
							agent.setDisableReceiveTimeIcone(false);
							agent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
							agent.setMesg_trn_ref(confirmation.getMsgTrnRef());
						} else if (confirmation.getReasonCode().equalsIgnoreCase("G001")) {
							preperCoveAgent(EndPointStatus.NonTracabelBank, LineStatus.ARRIVED_COVE, agent);
							gpiProperties.getNonGpiCoveAgent().add(confirmation.getForwardedTo());
							gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo().substring(0, 7), confirmation.getSendDate());
							agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
							agent.setSendDate(confirmation.getSendDate());
							agent.setDisableSendTimeIcone(false);
							agent.setDisableReceiveTimeIcone(false);
							agent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
							agent.setMesg_trn_ref(confirmation.getMsgTrnRef());
						} else if (confirmation.getReasonCode().equalsIgnoreCase("G002")) {
							preperCoveAgent(EndPointStatus.IntermediateReceiveWithOutSend, LineStatus.ARRIVED_COVE, agent);
							agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
							agent.setDisableSendTimeIcone(true);
							agent.setDisableReceiveTimeIcone(false);
							agent.setMesg_trn_ref(confirmation.getMsgTrnRef());
						} else if (confirmation.getReasonCode().equalsIgnoreCase("G003")) {
							preperCoveAgent(EndPointStatus.IntermediateReceiveWithOutSend, LineStatus.ARRIVED_COVE, agent);
							agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
							agent.setDisableSendTimeIcone(true);
							agent.setDisableReceiveTimeIcone(false);
							agent.setMesg_trn_ref(confirmation.getMsgTrnRef());
						}
					} else if (confirmation.getStatusCode().equalsIgnoreCase("RJCT")) {
						prepperStatusDivCov("RJCT");
						preperCoveAgent(EndPointStatus.IntermediateRejectPayment, LineStatus.ARRIVED_COVE, agent);
						gpiProperties.getArrivedCoveDate().put((confirmation.getForwardedTo() != null && !confirmation.getForwardedTo().isEmpty() ? confirmation.getForwardedTo().substring(0, 7) : ""), confirmation.getSendDate());
						agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						agent.setSendDate(confirmation.getSendDate());
						agent.setDisableSendTimeIcone(false);
						agent.setDisableReceiveTimeIcone(false);
						agent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
						agent.setMesg_trn_ref(confirmation.getMsgTrnRef());

					} else if (confirmation.getStatusCode().equalsIgnoreCase("ACSC") || confirmation.getStatusCode().equalsIgnoreCase("ACCC")) {
						prepperStatusDivCov("ACCC");
						preperCoveAgent(EndPointStatus.ReceivedAndSendCoverPayment, LineStatus.ARRIVED_COVE, agent);
						gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo(), confirmation.getSendDate());
						agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						agent.setSendDate(confirmation.getSendDate());
						agent.setDisableSendTimeIcone(false);
						agent.setDisableReceiveTimeIcone(false);
						agent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
						agent.setLineStatusForLastAgent(LineStatus.ARRIVED_COVE);
						agent.setMesg_trn_ref(confirmation.getMsgTrnRef());
					}
				} else if (agent.getMessageBIC().substring(0, 7).equalsIgnoreCase(forwardedTo)) {
					if (isNonCoveGpiAgent(confirmation.getForwardedTo())) {
						agent.setEndPointStatus(EndPointStatus.NotReceiveAndSend);
						agent.setLineStatus(LineStatus.NOT_ARRIVED_COVE);
						agent.setEndPointFormatted(getEndPointStr(EndPointStatus.NotReceiveAndSend));
						agent.setDisableReceiveTimeIcone(false);
						agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(agent.getMessageBIC().substring(0, 7)));
					} else {
						agent.setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
						agent.setLineStatus(LineStatus.ARRIVED_COVE);
						agent.setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateReceiveWithOutSend));
						agent.setDisableReceiveTimeIcone(false);
						agent.setArriveDate(gpiProperties.getArrivedCoveDate().get(agent.getMessageBIC().substring(0, 7)));
					}

				}
			}
		}

		handlingWhenUpdateComeFrom103Reviver(gpiConfirmationsList);

	}

	/**
	 * 
	 * Using this method for special handling when the 299 comes from not cove agent but from the direct receiver of 103
	 * 
	 */
	private void handlingWhenUpdateComeFrom103Reviver(List<GpiConfirmation> gpiConfirmationsList) {

		GpiAgent lastCoveAgent = coveAgents.get(coveAgents.size() - 1);
		// IF 299 Comes from direct reviver of original 103
		for (GpiConfirmation confirmation : gpiConfirmationsList) {

			String statusOriginator = (confirmation.getStatusOriginator() != null && !confirmation.getStatusOriginator().isEmpty()) ? confirmation.getStatusOriginator().substring(0, 7) : "";
			String instReceiverX1 = gpiProperties.getMessageDetails().getInstReceiverX1();
			String forwardedTo = (confirmation.getForwardedTo() != null && !confirmation.getForwardedTo().isEmpty() ? confirmation.getForwardedTo().substring(0, 7) : "");

			if (instReceiverX1.substring(0, 7).equalsIgnoreCase(statusOriginator)) {
				if (confirmation.getStatusCode().equalsIgnoreCase("ACSP")) {
					prepperStatusDivCov("ACSP");
					if (confirmation.getReasonCode().equalsIgnoreCase("G000")) {
						preperCoveAgent(EndPointStatus.ReceivedAndSendCoverPayment, LineStatus.ARRIVED_COVE, lastCoveAgent);
						gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo().substring(0, 7), confirmation.getSendDate());
						lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						lastCoveAgent.setSendDate(confirmation.getSendDate());
						lastCoveAgent.setDisableSendTimeIcone(false);
						lastCoveAgent.setDisableReceiveTimeIcone(false);
						lastCoveAgent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
						lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());
					} else if (confirmation.getReasonCode().equalsIgnoreCase("G001")) {
						preperCoveAgent(EndPointStatus.NonTracabelBank, LineStatus.ARRIVED_COVE, lastCoveAgent);
						gpiProperties.getNonGpiCoveAgent().add(confirmation.getForwardedTo());
						gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo().substring(0, 7), confirmation.getSendDate());
						lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						lastCoveAgent.setSendDate(confirmation.getSendDate());
						lastCoveAgent.setDisableSendTimeIcone(false);
						lastCoveAgent.setDisableReceiveTimeIcone(false);
						lastCoveAgent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
						lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());
					} else if (confirmation.getReasonCode().equalsIgnoreCase("G002")) {
						preperCoveAgent(EndPointStatus.IntermediateReceiveWithOutSend, LineStatus.ARRIVED_COVE, lastCoveAgent);
						lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						lastCoveAgent.setDisableSendTimeIcone(true);
						lastCoveAgent.setDisableReceiveTimeIcone(false);
						lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());
					} else if (confirmation.getReasonCode().equalsIgnoreCase("G003")) {
						preperCoveAgent(EndPointStatus.IntermediateReceiveWithOutSend, LineStatus.ARRIVED_COVE, lastCoveAgent);
						lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
						lastCoveAgent.setDisableSendTimeIcone(true);
						lastCoveAgent.setDisableReceiveTimeIcone(false);
						lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());
					}
				} else if (confirmation.getStatusCode().equalsIgnoreCase("RJCT")) {
					prepperStatusDivCov("RJCT");
					preperCoveAgent(EndPointStatus.IntermediateRejectPayment, LineStatus.ARRIVED_COVE, lastCoveAgent);
					gpiProperties.getArrivedCoveDate().put((confirmation.getForwardedTo() != null && !confirmation.getForwardedTo().isEmpty() ? confirmation.getForwardedTo().substring(0, 7) : ""), confirmation.getSendDate());
					lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
					lastCoveAgent.setSendDate(confirmation.getSendDate());
					lastCoveAgent.setDisableSendTimeIcone(false);
					lastCoveAgent.setDisableReceiveTimeIcone(false);
					lastCoveAgent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
					lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());

				} else if (confirmation.getStatusCode().equalsIgnoreCase("ACSC") || confirmation.getStatusCode().equalsIgnoreCase("ACCC")) {
					prepperStatusDivCov("ACCC");
					preperCoveAgent(EndPointStatus.ReceivedAndSendCoverPayment, LineStatus.ARRIVED_COVE, lastCoveAgent);
					gpiProperties.getArrivedCoveDate().put(confirmation.getForwardedTo(), confirmation.getSendDate());
					lastCoveAgent.setArriveDate(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)));
					lastCoveAgent.setSendDate(confirmation.getSendDate());
					lastCoveAgent.setDisableSendTimeIcone(false);
					lastCoveAgent.setDisableReceiveTimeIcone(false);
					lastCoveAgent.setTakenTime(calculateTimeTakenForCov(gpiProperties.getArrivedCoveDate().get(confirmation.getStatusOriginator().substring(0, 7)), confirmation.getSendDate()));
					lastCoveAgent.setLineStatusForLastAgent(LineStatus.ARRIVED_COVE);
					lastCoveAgent.setMesg_trn_ref(confirmation.getMsgTrnRef());
				}
			} else if (instReceiverX1.substring(0, 7).equalsIgnoreCase(forwardedTo)) {
				if (confirmation.getStatusCode().equalsIgnoreCase("ACSP")) {
					if (confirmation.getReasonCode().equalsIgnoreCase("G000") || confirmation.getReasonCode().equalsIgnoreCase("G001")) {
						lastCoveAgent.setLineStatusForLastAgent(LineStatus.ARRIVED_COVE);
					}
				}
			}
		}

	}

	private String calculateTimeTakenForCov(String arrDate, String sendDate) {
		String dateStart = arrDate;
		String dateStop = sendDate;
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		String timeTaken = "";
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		long diffSeconds = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			timeTaken = String.valueOf(diffHours) + " hours " + diffMinutes + " min " + diffSeconds + " sec";
		} catch (Exception e) {
			e.printStackTrace();
		}
		gpiProperties.totalCovHours = gpiProperties.totalCovHours + diffHours;
		gpiProperties.totalCovMin = gpiProperties.totalCovMin + diffMinutes;
		gpiProperties.totalCovSec = gpiProperties.totalCovSec + diffSeconds;

		if (gpiProperties.totalCovSec >= 60) {
			if (gpiProperties.totalCovSec == 60) {
				gpiProperties.totalCovSec = 0;
			} else {
				long sec = gpiProperties.totalCovSec - 60;
				gpiProperties.totalCovSec = sec;
			}
			gpiProperties.totalCovMin = gpiProperties.totalCovMin + 1;
		}

		if (gpiProperties.totalCovMin >= 60) {
			if (gpiProperties.totalCovMin == 60) {
				gpiProperties.totalCovMin = 0;
			} else {
				long min = gpiProperties.totalCovMin - 60;
				gpiProperties.totalCovMin = min;
			}
			gpiProperties.totalCovHours = gpiProperties.totalCovHours + 1;

		}
		gpiProperties.setTotalDurationMesgCov(gpiProperties.totalCovHours + " hr " + gpiProperties.totalCovMin + " min " + gpiProperties.totalCovSec + " sec");
		return timeTaken;
	}

	public void prepperStatusDivCov(String statusCode) {
		if (statusCode.equalsIgnoreCase("ACSC") || statusCode.equalsIgnoreCase("ACCC")) {
			gpiProperties.setStatusMessageCov("Completed");
			gpiProperties.setSatausCovImage("/images/completedGpi.png");
			gpiProperties.setStatusCovStyle("completed");
		} else if (statusCode.equalsIgnoreCase("RJCT")) {
			gpiProperties.setStatusMessageCov("Rejected");
			gpiProperties.setSatausCovImage("/images/rejectIcone.png");
			gpiProperties.setStatusCovStyle("Rejected");
		} else {
			gpiProperties.setStatusMessageCov("In Progress");
			gpiProperties.setSatausCovImage("/images/settingGPI.png");
			gpiProperties.setStatusCovStyle("progres");
		}
	}

	private ArrayList<GpiAgent> initialTrackerNode() {
		String beneficiaryBankCode = gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode();
		ArrayList<GpiAgent> messagesList = new ArrayList<>();
		boolean accountWithInstitution = checkAccountWithInstitution(beneficiaryBankCode, gpiProperties.getMessageDetails().getInstReceiverX1());

		if (!accountWithInstitution) {
			messagesList
					.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getMesgSenderX1()), getCountryName(gpiProperties.getMessageDetails().getMesgSenderX1()), getInstitutionName(gpiProperties.getMessageDetails().getMesgSenderX1()),
							gpiProperties.getMessageDetails().getMesgSenderX1(), gpiProperties.getMessageDetails().getMesgTrnRef(), gpiProperties.getMessageDetails().getMesgCreaDateTimeOnDB(), null, null, true,
							gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", getChargesAmountFor103(), getChargesCurFor103(), gpiProperties.getMessageDetails().getMesgCopyServiceId()));
			gpiProperties.getBicList().add(gpiProperties.getMessageDetails().getMesgSenderX1());
			sumDeducts(getChargesAmountFor103(), getChargesCurFor103(), "");
			messagesList.add(
					gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getInstReceiverX1()), getCountryName(gpiProperties.getMessageDetails().getInstReceiverX1()), getInstitutionName(gpiProperties.getMessageDetails().getInstReceiverX1()),
							gpiProperties.getMessageDetails().getInstReceiverX1(), "", null, null, EndPointStatus.SECONED_AGENT, false, gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", "", "", ""));
			gpiProperties.getBicList().add(gpiProperties.getMessageDetails().getInstReceiverX1());
			gpiProperties.setGpiType(GpiType.Without_Beneficiary_Customer);
		} else {
			gpiProperties.setGpiType(GpiType.With_Beneficiary_Customer);
			gpiProperties.setHasIntermediaryGpiAgent(true);
			messagesList
					.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getMesgSenderX1()), getCountryName(gpiProperties.getMessageDetails().getMesgSenderX1()), getInstitutionName(gpiProperties.getMessageDetails().getMesgSenderX1()),
							gpiProperties.getMessageDetails().getMesgSenderX1(), gpiProperties.getMessageDetails().getMesgTrnRef(), gpiProperties.getMessageDetails().getMesgCreaDateTimeOnDB(), null, null, true,
							gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", getChargesAmountFor103(), getChargesCurFor103(), gpiProperties.getMessageDetails().getMesgCopyServiceId()));

			gpiProperties.getBicList().add(gpiProperties.getMessageDetails().getMesgSenderX1());
			sumDeducts(getChargesAmountFor103(), getChargesCurFor103(), "");
			messagesList.add(
					gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getInstReceiverX1()), getCountryName(gpiProperties.getMessageDetails().getInstReceiverX1()), getInstitutionName(gpiProperties.getMessageDetails().getInstReceiverX1()),
							gpiProperties.getMessageDetails().getInstReceiverX1(), "", null, null, EndPointStatus.SECONED_AGENT, false, gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", "", "", ""));

			gpiProperties.getBicList().add(gpiProperties.getMessageDetails().getMesgReceiverSwiftAddress());
			messagesList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode()), getCountryName(gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode()),
					getInstitutionName(gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode()), gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode(), "", null, LineStatus.defultLine, EndPointStatus.NotReceiveAndSend, false, "", "",
					"", "", ""));
			gpiProperties.getBicList().add(gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode());
			messagesList.get(2).setLastElement(true);
		}
		fillInitCoveMessage();
		messagesList.get(0).setMesgCharges(gpiProperties.getMessageDetails().getMesgCharges());
		return messagesList;

	}

	private boolean checkAccountWithInstitution(String beneficiaryBankCode, String instReceiverX1) {
		String mesgUnExpandedText = gpiProperties.getMessageDetails().getMesgUnExpandedText();
		try {
			if (!mesgUnExpandedText.contains("57A")) {
				return false;
			}
			if (beneficiaryBankCode == null || beneficiaryBankCode.isEmpty()) {
				return false;
			} else if (beneficiaryBankCode.length() != instReceiverX1.length() && beneficiaryBankCode.substring(0, 7).equalsIgnoreCase(instReceiverX1.substring(0, 7))) {
				return false;
			} else if (beneficiaryBankCode.length() == instReceiverX1.length() && beneficiaryBankCode.equalsIgnoreCase(instReceiverX1)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	private void fillCoveWithDiffTowAcount() {
		ArrayList<GpiAgent> messagesCoveList = new ArrayList<>();
		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getSenderCorr()), getCountryName(gpiProperties.getMessageDetails().getSenderCorr()), getInstitutionName(gpiProperties.getMessageDetails().getSenderCorr()),
				gpiProperties.getMessageDetails().getSenderCorr(), null, gpiProperties.getMessageDetails().getMesgCreaDateTimeOnDB(), LineStatus.NOT_ARRIVED_COVE, EndPointStatus.COVE_AGENT, false,
				gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", "", "", ""));

		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getRecieverCorr()), getCountryName(gpiProperties.getMessageDetails().getRecieverCorr()),
				getInstitutionName(gpiProperties.getMessageDetails().getRecieverCorr()), gpiProperties.getMessageDetails().getRecieverCorr(), null, null, LineStatus.NOT_ARRIVED_COVE, EndPointStatus.NotReceiveAndSend, false, "", "", "", "", ""));
		setCoveAgents(messagesCoveList);
	}

	private void fillInitCoveMessage() {
		CoveType coveType = gpiProperties.getMessageDetails().getCoveType();
		switch (coveType) {
		case coveWithShaerdAcount:
			fillCoveWithShaerdAcount();
			break;

		case coveWithDiffTowAcount:// 54
			fillCoveWithDiffTowAcount();
			break;

		case coveWithDiffThreAcount:// 55
			fillCoveWithDiffThreAcount();
			break;
		case notCovePayment:
			setCoveAgents(null);
			break;
		default:
			break;
		}
		if (coveAgents != null) {
			gpiProperties.setHasCovPayment(true);
			gpiProperties.getArrivedCoveDate().put(coveAgents.get(0).getMessageBIC().substring(0, 7), coveAgents.get(0).getArriveDate());
		}
	}

	private void fillCoveWithShaerdAcount() {
		ArrayList<GpiAgent> messagesCoveList = new ArrayList<>();
		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getSenderCorr()), getCountryName(gpiProperties.getMessageDetails().getSenderCorr()), getInstitutionName(gpiProperties.getMessageDetails().getSenderCorr()),
				gpiProperties.getMessageDetails().getSenderCorr(), null, gpiProperties.getMessageDetails().getMesgCreaDateTimeOnDB(), LineStatus.NOT_ARRIVED_COVE, EndPointStatus.COVE_AGENT, false,
				gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", "", "", ""));

		setCoveAgents(messagesCoveList);
	}

	private void fillCoveWithDiffThreAcount() {
		ArrayList<GpiAgent> messagesCoveList = new ArrayList<>();
		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getSenderCorr()), getCountryName(gpiProperties.getMessageDetails().getSenderCorr()), getInstitutionName(gpiProperties.getMessageDetails().getSenderCorr()),
				gpiProperties.getMessageDetails().getSenderCorr(), null, gpiProperties.getMessageDetails().getMesgCreaDateTimeOnDB(), LineStatus.NOT_ARRIVED_COVE, EndPointStatus.COVE_AGENT, false,
				gpiProperties.getMessageDetails().getMesgCreaDateTimeWithZoneStr(), "", "", "", ""));

		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getRecieverCorr()), getCountryName(gpiProperties.getMessageDetails().getRecieverCorr()),
				getInstitutionName(gpiProperties.getMessageDetails().getRecieverCorr()), gpiProperties.getMessageDetails().getRecieverCorr(), null, null, LineStatus.NOT_ARRIVED_COVE, EndPointStatus.NotReceiveAndSend, false, "", "", "", "", ""));
		messagesCoveList.add(gpiAgentCreator(getCityName(gpiProperties.getMessageDetails().getReimbInst()), getCountryName(gpiProperties.getMessageDetails().getReimbInst()), getInstitutionName(gpiProperties.getMessageDetails().getReimbInst()),
				gpiProperties.getMessageDetails().getReimbInst(), null, null, LineStatus.NOT_ARRIVED_COVE, EndPointStatus.NotReceiveAndSend, false, "", "", "", "", ""));
		setCoveAgents(messagesCoveList);

	}

	private String getCityName(String bic) {
		CorrInfo corrInfo = bicExpandedMap.get(bic.substring(0, 7));
		if (corrInfo != null) {
			return corrInfo.getCorrCityName();
		}
		CorrInfo bicInfo = serviceLocator.getViewerService().getBicInfo(bic.substring(0, 7));

		if (bicInfo != null) {
			bicExpandedMap.put(bic.substring(0, 7), bicInfo);
			return bicInfo.getCorrCityName();

		}

		return "";
	}

	private String getInstitutionName(String bic) {
		CorrInfo corrInfo = bicExpandedMap.get(bic.substring(0, 7));
		if (corrInfo != null) {
			return corrInfo.getCorrInstitutionName();
		}
		CorrInfo bicInfo = serviceLocator.getViewerService().getBicInfo(bic.substring(0, 7));

		if (bicInfo != null) {
			bicExpandedMap.put(bic.substring(0, 7), bicInfo);
			return bicInfo.getCorrInstitutionName();
		}

		return "";

	}

	private String getCountryName(String bic) {
		CorrInfo corrInfo = bicExpandedMap.get(bic.substring(0, 7));
		if (corrInfo != null) {
			return corrInfo.getCorrCtryName();
		}
		CorrInfo bicInfo = serviceLocator.getViewerService().getBicInfo(bic.substring(0, 7));

		if (bicInfo != null) {

			bicExpandedMap.put(bic.substring(0, 7), bicInfo);

			return bicInfo.getCorrCtryName();
		}

		return "";

	}

	private void sumDeducts(String amount, String cur, String deductType) {

		if (amount != null && !amount.isEmpty()) {
			gpiProperties.setTotalDeducts(gpiProperties.getTotalDeducts() + Double.parseDouble(amount));
		}

	}

	private GpiAgent gpiAgentCreator(String city, String contry, String name, String bic, String mesgTrnRef, Date creatDateTime, LineStatus lineStatus, EndPointStatus endPointStatus, boolean isFirst, String arriveDate, String sendDate,
			String msgSendAmount, String msgSendCur, String marketInfrastructure) {
		GpiAgent agent = new GpiAgent();
		agent.setName(name);
		agent.setMessageBIC(bic);
		agent.setCityName(city);
		agent.setCountryName(contry);
		agent.setMsgSendAmount(msgSendAmount);
		agent.setMsgSendCurr(msgSendCur);
		agent.setArriveDate(arriveDate);
		agent.setSendDate(sendDate);

		agent.setMarketInfrastructure(marketInfrastructure);

		if (marketInfrastructure != null && !marketInfrastructure.isEmpty()) {
			agent.setShowMarketInfrastructure(true);
		} else {
			agent.setShowMarketInfrastructure(false);
		}

		if (arriveDate == null) {
			agent.setDisableReceiveTimeIcone(true);
		} else if (arriveDate.isEmpty()) {
			agent.setDisableReceiveTimeIcone(true);
		} else {
			agent.setDisableReceiveTimeIcone(false);
		}

		if (sendDate == null) {
			agent.setDisableSendTimeIcone(true);

		} else if (sendDate.isEmpty()) {
			agent.setDisableSendTimeIcone(true);

		} else {
			agent.setDisableSendTimeIcone(false);
		}

		if (mesgTrnRef != null && !mesgTrnRef.isEmpty()) {
			agent.setMesg_trn_ref(mesgTrnRef);
		}

		if (creatDateTime != null) {
			agent.setCreationDate(creatDateTime);
		}

		if (lineStatus != null) {
			agent.setLineStatus(lineStatus);

		}
		if (endPointStatus != null) {
			if (endPointStatus.equals(EndPointStatus.SECONED_AGENT)) {
				setReciceLeg(agent);
			} else if (endPointStatus.equals(EndPointStatus.COVE_AGENT)) {
				setCovLeg(agent);
			} else {
				agent.setEndPointStatus(endPointStatus);
			}
		}

		if (isFirst) {
			agent.setFirstGpiAgent(true);
		} else {
			agent.setFirstGpiAgent(false);
		}

		if (endPointStatus != null && !endPointStatus.equals(EndPointStatus.COVE_AGENT)) {
			agent.setEndPointFormatted(getEndPointStr(endPointStatus));
		} else {
			if (isFirst) {
				agent.setEndPointFormatted("Received and sent");
			} else {
				agent.setEndPointFormatted("Not yet received or sent");

			}

		}
		return agent;
	}

	private void setCovLeg(GpiAgent agent) {
		String networkDeliveryStatus = getNetworkDeliveryStatusCov();
		if (networkDeliveryStatus.equalsIgnoreCase(DLVACKED)) {
			agent.setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
			agent.setEndPointFormatted(getEndPointStr(agent.getEndPointStatus()));
		} else {
			agent.setEndPointStatus(EndPointStatus.NotReceiveAndSend);
			agent.setEndPointFormatted(getEndPointStr(agent.getEndPointStatus()));
		}
	}

	private String getNetworkDeliveryStatusCov() {
		List<GpiConfirmation> gpiConfirmationsList = serviceLocator.getViewerService().getGpiAgent("", gpiProperties.getUniqueEndToEndTransactionReference(), gpiProperties.getTimeZoneOffset(), gpiProperties.getParam().getSourceSelectedSAA(), false,
				gpiProperties.getMessageDetails().getMesgCreaDateTime(), "'202'", "'205'");

		if (gpiConfirmationsList != null && !gpiConfirmationsList.isEmpty())
			return DLVACKED;

		return "";
	}

	private void setReciceLeg(GpiAgent agent) {
		// String networkDeliveryStatus = getNetworkDeliveryStatus103();
		String networkDeliveryStatus = DLVACKED;
		if (networkDeliveryStatus.equalsIgnoreCase(DLVACKED)) {
			agent.setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
		} else {
			agent.setEndPointStatus(EndPointStatus.NotReceiveAndSend);
		}
	}

	private String getNetworkDeliveryStatus103() {
		int aid = gpiProperties.getMessageDetails().getAid();
		int umidl = gpiProperties.getMessageDetails().getMesgUmidl();
		int umidh = gpiProperties.getMessageDetails().getMesgUmidh();
		Date mesgCreateDateTime = gpiProperties.getMessageDetails().getMesgCreaDateTime();
		List<AppendixDetails> appendixList = serviceLocator.getViewerService().getAppendixList(aid, umidl, umidh, mesgCreateDateTime, 0, 0);
		if (appendixList != null && !appendixList.isEmpty()) {
			return appendixList.get(appendixList.size() - 1).getAppeNetworkDeliveryStatus();
		}
		return "";
	}

	private String getNetworkDeliveryStatus(int aid, int umidl, int umidh, Date mesgCreateDateTime) {
		List<AppendixDetails> appendixList = serviceLocator.getViewerService().getAppendixList(aid, umidl, umidh, mesgCreateDateTime, 0, 0);
		if (appendixList != null && !appendixList.isEmpty()) {
			return appendixList.get(appendixList.size() - 1).getAppeNetworkDeliveryStatus();
		}
		return "";
	}

	private String getEndPointStr(EndPointStatus endPointStatus) {
		String endPointStatusStr = "";

		switch (endPointStatus) {
		case ReceivedAndSend:
			endPointStatusStr = EndPointStatusDesc.RECEIVEDANDSENT.getDesc();
			break;

		case IntermediateReceiveWithOutSend:
			endPointStatusStr = EndPointStatusDesc.RECEIVEDBUTNOTYSETSENT.getDesc();
			break;

		case NotReceiveAndSend:
			endPointStatusStr = EndPointStatusDesc.NOTYETRECEIVEDORSENT.getDesc();
			break;

		case IntermediateRejectPayment:
			endPointStatusStr = EndPointStatusDesc.REJECTED.getDesc();
			break;

		case NonTracabelBank:
			endPointStatusStr = EndPointStatusDesc.ARRIVEDATNONTRACEABLEBANK.getDesc();
			break;

		case ReceivedPaymentNotCreditedIt:
			endPointStatusStr = EndPointStatusDesc.RECEIVEDBUTNOTYETCREDITED.getDesc();
			break;

		case NonTracabelBeneficiaryBank:
			endPointStatusStr = EndPointStatusDesc.ARRIVEDATNONTRACEABLEBANK.getDesc();
			break;
		case ReceivedAndSendCoverPayment:
			endPointStatusStr = EndPointStatusDesc.RECEIVEDANDSENT.getDesc();
			break;
		}

		return endPointStatusStr;

	}

	private void fillGpiDetailsHistories() {
		MessageDetails messageDetails = gpiProperties.getMessageDetails();
		try {
			gpiProperties.setDetailsHistoriesList(serviceLocator.getViewerService().getGpiDetailsHistory(gpiProperties.getMessageDetails().getMesgCreaDateTime(), messageDetails.getUetr(), ""));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private HashMap<String, List<GpiConfirmation>> getConfirmationMap(List<GpiConfirmation> confirmations) {
		HashMap<String, List<GpiConfirmation>> confirmatioMap = new HashMap<>();
		List<GpiConfirmation> normalConfirmations = new ArrayList<>();
		List<GpiConfirmation> coveConfirmation = new ArrayList<>();
		List<GpiConfirmation> gSRPConfirmation = new ArrayList<>();
		for (GpiConfirmation confirmation : confirmations) {
			if (confirmation.getStatusCode() != null && (confirmation.getStatusCode().equalsIgnoreCase("ACSP") || confirmation.getStatusCode().equalsIgnoreCase("ACSC") || confirmation.getStatusCode().equalsIgnoreCase("ACCC")
					|| confirmation.getStatusCode().equalsIgnoreCase("RJCT"))) {

				if (confirmation.getMesgType().equalsIgnoreCase("299")) {
					coveConfirmation.add(confirmation);
				} else if (confirmation.getMesgType().equalsIgnoreCase("199")) {
					normalConfirmations.add(confirmation);
				}
			}
		}
		confirmatioMap.put("199", normalConfirmations);
		confirmatioMap.put("299", coveConfirmation);
		confirmatioMap.put("192", gSRPConfirmation);
		return confirmatioMap;
	}

	/**
	 * Using this method for update incoming message "output" using the correspondent input message with same UETR
	 */
	private GpiConfirmation getIncoming103AsConfirmationMessage() {

		try {
			// Check if the message sub Format is "Output"
			if (gpiProperties.getMessageDetails().getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
				// Get the Input message , to use it for update traker
				DetailsHistory outGoing = getMessageFromDetailsHistory("INPUT", "");
				if (outGoing != null) {
					GpiConfirmation gpiConfirmation = new GpiConfirmation();
					gpiConfirmation.setStatusCode("ACSP");
					gpiConfirmation.setReasonCode("G000");
					gpiConfirmation.setAid(outGoing.getAid());
					gpiConfirmation.setMesgUmidl(outGoing.getMesgUmidl());
					gpiConfirmation.setMesgUmidh(outGoing.getMesgUmidh());
					gpiConfirmation.setMesgCreaDateTime(outGoing.getMesgCreaDateTime());
					gpiConfirmation.setStatusOriginator(outGoing.getSenderBic());
					gpiConfirmation.setForwardedTo(outGoing.getReceiverBic());
					gpiConfirmation.setMsgTrnRef(outGoing.getMesgTrnRef());
					gpiConfirmation.setMesgCreaDateTime(outGoing.getMesgCreaDateTime());
					gpiConfirmation.setSendDate(outGoing.getMesgCreaDateTimeStr());
					gpiConfirmation.setMsgSendAmount(outGoing.getSenderDeducts());
					gpiConfirmation.setMsgSendCurr(outGoing.getSenderDeductsCur());
					gpiConfirmation.setMesgType("199");
					gpiConfirmation.setMesgNakedCode(outGoing.getMesgNakedCode());
					gpiConfirmation.setUpdateFromIncoming(true);

					// preparDirictIntermediateAgent(gpiConfirmation);
					gpiProperties.setUpdateFromIncoming(true);
					return gpiConfirmation;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Using this method for update incoming message "output" using the correspondent input message with same UETR
	 */
	private GpiConfirmation getIncoming202AsConfirmationMessage() {

		try {
			// Check if the message sub Format is "Output"
			// Get the Input message , to use it for update traker
			DetailsHistory outGoing = getMessageFromDetailsHistory("OUTPUT", "202");
			if (outGoing != null) {
				GpiConfirmation gpiConfirmation = new GpiConfirmation();
				gpiConfirmation.setStatusCode("ACSP");
				gpiConfirmation.setReasonCode("G000");
				gpiConfirmation.setAid(outGoing.getAid());
				gpiConfirmation.setMesgUmidl(outGoing.getMesgUmidl());
				gpiConfirmation.setMesgUmidh(outGoing.getMesgUmidh());
				gpiConfirmation.setMesgCreaDateTime(outGoing.getMesgCreaDateTime());
				gpiConfirmation.setStatusOriginator(outGoing.getSenderBic());
				gpiConfirmation.setForwardedTo(outGoing.getReceiverBic());
				gpiConfirmation.setMsgTrnRef(outGoing.getMesgTrnRef());
				gpiConfirmation.setMesgCreaDateTime(outGoing.getMesgCreaDateTime());
				gpiConfirmation.setSendDate(outGoing.getMesgCreaDateTimeStr());
				gpiConfirmation.setMsgSendAmount(outGoing.getSenderDeducts());
				gpiConfirmation.setMsgSendCurr(outGoing.getSenderDeductsCur());
				gpiConfirmation.setMesgType("299");
				// preparDirictIntermediateAgent(gpiConfirmation);
				gpiProperties.setUpdateFromIncoming(true);
				return gpiConfirmation;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	private DetailsHistory getMessageFromDetailsHistory(String dir, String mesgType) {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
		if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
			return null;
		}
		DetailsHistory history = null;

		if (mesgType.equalsIgnoreCase("202")) {
			for (DetailsHistory detailsHistory : detailsHistoriesList) {
				if (detailsHistory.getMsgType().equalsIgnoreCase("202") && detailsHistory.getMesgSubFormat().equalsIgnoreCase(dir)) {
					history = detailsHistory;
					break;
				}
			}
		} else {
			for (DetailsHistory detailsHistory : detailsHistoriesList) {
				if (detailsHistory.getMsgType().equalsIgnoreCase("103") && detailsHistory.getMesgSubFormat().equalsIgnoreCase(dir)) {
					history = detailsHistory;
					break;
				}
			}

			if (history == null && dir.equalsIgnoreCase("output")) {
				DetailsHistory detailsHistory = detailsHistoriesList.get(detailsHistoriesList.size() - 1);
				if (detailsHistory.getMsgType().equalsIgnoreCase("199")) {
					history = detailsHistory;
					history.setComesFrom199Updated(true);
				}
			}
		}

		return history;
	}

	private String getRecentOutputRelated199(String orgentor, String forwordingTo) {
		try {
			List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
			if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
				return null;
			}
			for (DetailsHistory detailsHistory : detailsHistoriesList) {
				if (detailsHistory.getMesgSubFormat().equalsIgnoreCase("OUTPUT")) {
					if (detailsHistory.getMsgType().equalsIgnoreCase("199")) {
						if (detailsHistory.getSenderBic().equalsIgnoreCase(orgentor) && detailsHistory.getReceiverBic().equalsIgnoreCase(forwordingTo)) {
							return detailsHistory.getMesgCharges();
						}
					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";
	}

	private String getRecentRelated199(String orgentor, String forwordingTo) {
		try {
			List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
			if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
				return null;
			}
			for (DetailsHistory detailsHistory : detailsHistoriesList) {
				if (detailsHistory.getSenderBic().equalsIgnoreCase(orgentor) && detailsHistory.getReceiverBic().equalsIgnoreCase(forwordingTo)) {
					if (detailsHistory.getMsgType().equalsIgnoreCase("199")) {
						String mesgCharges = detailsHistory.getMesgCharges();
						if (mesgCharges != null && !mesgCharges.isEmpty()) {
							return mesgCharges;
						}
					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";
	}

	private DetailsHistory getCorrespondent103(String organaitor, String forwordTo) {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();

		if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
			return null;
		}
		DetailsHistory history = null;

		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType().equalsIgnoreCase("103")) {
				if (detailsHistory.getSenderBic().substring(0, 7).equalsIgnoreCase(organaitor.substring(0, 7)) && detailsHistory.getReceiverBic().substring(0, 7).equalsIgnoreCase(forwordTo.substring(0, 7))) {
					history = detailsHistory;
					break;
				}

			}
		}
		return history;
	}

	private DetailsHistory getCorrespondent103ForCompletedMessages(String relatedRef) {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();

		if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
			return null;
		}
		DetailsHistory history = null;

		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType().equalsIgnoreCase("103")) {
				if (detailsHistory.getMesgTrnRef().equalsIgnoreCase(relatedRef)) {
					history = detailsHistory;
					break;
				}

			}
		}
		return history;
	}

	private String getExChangeRateFrom199() {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType() != null && detailsHistory.getMsgType().equalsIgnoreCase("199")) {
				if (detailsHistory.getMesgExchangeRate() != null && !detailsHistory.getMesgExchangeRate().isEmpty()) {
					String mesgExchangeRate = detailsHistory.getMesgExchangeRate();
					return mesgExchangeRate;
				}
			}
		}

		return "";
	}

	private String getExChangeRateCcyFrom199() {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType() != null && detailsHistory.getMsgType().equalsIgnoreCase("199")) {
				if (detailsHistory.getMesgExchangeRate() != null && !detailsHistory.getMesgExchangeRate().isEmpty()) {
					String mesgExchangeCcy = detailsHistory.getExChangeRateFromCcy();
					return mesgExchangeCcy;
				}
			}
		}

		return "";
	}

	private boolean comparingTowBic(String bicOne, String bicTow) {
		try {
			if (bicOne == null || bicTow == null) {
				return false;
			}
			if (bicOne.isEmpty() || bicTow.isEmpty()) {
				return false;
			}

			if (bicOne.length() == bicTow.length() && bicOne.equalsIgnoreCase(bicTow)) {
				return true;
			} else if (bicOne.length() != bicTow.length() && bicOne.substring(0, 7).equalsIgnoreCase(bicTow.substring(0, 7))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private void overrideBenfisharyCustamer(List<GpiConfirmation> gpiConfirmationsList, GpiAgent bifCustmer) {
		String mesgUnExpandedText = gpiProperties.getMessageDetails().getMesgUnExpandedText();
		if (mesgUnExpandedText != null && !mesgUnExpandedText.contains("57A")) {
			if (!mesgUnExpandedText.contains("56A")) {
				for (GpiConfirmation confirmation : gpiConfirmationsList) {
					if (confirmation.getStatusOriginator() != null && confirmation.getStatusOriginator().substring(0, 7).equalsIgnoreCase(messageList.get(0).getMessageBIC().substring(0, 7))) {

						continue;
					}

					if (confirmation.getStatusCode().equalsIgnoreCase("ACSP") && (confirmation.getReasonCode().equalsIgnoreCase("G000") || confirmation.getReasonCode().equalsIgnoreCase("G001"))) {
						bifCustmer.setCityName(getCityName(confirmation.getForwardedTo()));
						bifCustmer.setCountryName(getCountryName(confirmation.getForwardedTo()));
						bifCustmer.setName(getInstitutionName(confirmation.getForwardedTo()));
						bifCustmer.setMessageBIC(confirmation.getForwardedTo());
						gpiProperties.getBicList().add(confirmation.getForwardedTo());
						break;
					}
				}
			} else {
				GpiConfirmation targetConfirmation = null;
				for (GpiConfirmation confirmation : gpiConfirmationsList) {
					if (confirmation.getStatusOriginator() != null && confirmation.getStatusOriginator().substring(0, 7).equalsIgnoreCase(messageList.get(0).getSenderBic().substring(0, 7))) {

						continue;
					}
					String statusOriginator = confirmation.getStatusOriginator();
					List<String> bicList = gpiProperties.getBicList();
					boolean newBic = true;
					for (String bic : bicList) {
						if (bic.substring(0, 7).equalsIgnoreCase(statusOriginator.substring(0, 7))) {
							newBic = false;
							break;
						}
					}
					if (newBic) {
						if (confirmation.getStatusCode().equalsIgnoreCase("ACSP") && (confirmation.getReasonCode().equalsIgnoreCase("G000") || confirmation.getReasonCode().equalsIgnoreCase("G001"))) {
							targetConfirmation = confirmation;
							break;
						}
					}
				}
				if (targetConfirmation != null) {
					bifCustmer.setCityName(getCityName(targetConfirmation.getForwardedTo()));
					bifCustmer.setCountryName(getCountryName(targetConfirmation.getForwardedTo()));
					bifCustmer.setName(getInstitutionName(targetConfirmation.getForwardedTo()));
					bifCustmer.setMessageBIC(targetConfirmation.getForwardedTo());
					gpiProperties.getBicList().add(targetConfirmation.getForwardedTo());
				}

			}
		}
	}

	private void prepareWithBeneficiaryCustmerCsase(List<GpiConfirmation> gpiConfirmationsList) {
		GpiAgent bifCustmer = messageList.get(messageList.size() - 1);
		bifCustmer.setFirstGpiAgent(false);
		messageList.remove(messageList.size() - 1);
		String confirmationMesgCharges = "";
		boolean getStatusOriginatorAsBefisharyBank = false;

		// Here you want to override the Filed 57 IN case you don have Option A

		// overrideBenfisharyCustamer(gpiConfirmationsList, bifCustmer);

		for (GpiConfirmation gpiConfirmation : gpiConfirmationsList) {
			confirmationMesgCharges = "";
			if (gpiConfirmation.getStatusCode().equals("ACSP") && (gpiConfirmation.getReasonCode().equals("G000") || gpiConfirmation.getReasonCode().equals("G001"))) {
				if (!first103 || (!gpiConfirmation.getStatusOriginator().substring(0, 7).equalsIgnoreCase(messageList.get(0).getMessageBIC().substring(0, 7)))) {

					if (!gpiConfirmation.isUpdateFromIncoming()) {
						if (confirmationListAlradyChecked.isEmpty() || !checkCongirmationAlradyExsist(gpiConfirmation)) {
							String chargeOutputTypeRelated = getRecentOutputRelated199(gpiConfirmation.getStatusOriginator(), gpiConfirmation.getForwardedTo());
							// get it from related 199
							if (chargeOutputTypeRelated != null && !chargeOutputTypeRelated.isEmpty()) {
								chargesTypeList.add(chargeOutputTypeRelated);
								confirmationMesgCharges = chargeOutputTypeRelated;
							} else {
								String chargeTypeRelated = getRecentRelated199(gpiConfirmation.getStatusOriginator(), gpiConfirmation.getForwardedTo());
								// get it from related 199
								if (chargeTypeRelated != null && !chargeTypeRelated.isEmpty()) {
									chargesTypeList.add(chargeTypeRelated);
									confirmationMesgCharges = chargeTypeRelated;
								} else {
									String chargesType = gpiConfirmation.getMesgCharges();
									if (chargesType != null && !chargesType.isEmpty()) {
										chargesTypeList.add(chargesType);
										confirmationMesgCharges = chargesType;
									} else {
										DetailsHistory correspondent103 = getCorrespondent103(gpiConfirmation.getStatusOriginator(), gpiConfirmation.getForwardedTo());
										if (correspondent103 != null) {
											if (correspondent103.getMesgCharges() != null && !correspondent103.getMesgCharges().isEmpty()) {
												chargesTypeList.add(correspondent103.getMesgCharges());
												confirmationMesgCharges = correspondent103.getMesgCharges();
											} else {
												if (chargesTypeList != null && !chargesTypeList.isEmpty()) {
													chargesTypeList.add(chargesTypeList.get(chargesTypeList.size() - 1));
													confirmationMesgCharges = chargesTypeList.get(chargesTypeList.size() - 1);
												}
											}
										} else {
											if (chargesTypeList != null && !chargesTypeList.isEmpty()) {
												chargesTypeList.add(chargesTypeList.get(chargesTypeList.size() - 1));
												confirmationMesgCharges = chargesTypeList.get(chargesTypeList.size() - 1);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			if (!confirmationMesgCharges.isEmpty()) {
				gpiConfirmation.setMesgCharges(confirmationMesgCharges);
				if (comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(1).getMessageBIC()) || comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(0).getMessageBIC())) {
					// doNothing
				} else {
					updateMessageCharges(gpiConfirmation);
				}
			}

			if (comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(1).getMessageBIC()) || comparingTowBic(gpiConfirmation.getStatusOriginator(), messageList.get(0).getMessageBIC())) {
				preparDirictIntermediateAgent(gpiConfirmation);
			}
			if (!bicExsist(gpiConfirmation.getForwardedTo())) {
				GpiAgent createGpiAgentWithStatus = createGpiAgentWithStatus(gpiConfirmation, gpiConfirmationsList);
				if (createGpiAgentWithStatus != null) {
					messageList.add(createGpiAgentWithStatus);
				}
			}

			if (comparingTowBic(gpiConfirmation.getStatusOriginator(), bifCustmer.getMessageBIC())) {
				preparBinfisharyCuctumer(gpiConfirmation, bifCustmer);
				getStatusOriginatorAsBefisharyBank = true;
			}

			if (!gpiConfirmation.isUpdateFromIncoming()) {
				confirmationListAlradyChecked.add(gpiConfirmation);
			}
		}

		GpiConfirmation confirmationWhen57AForWord = getConfirmationWhen57AForWord(gpiConfirmationsList, bifCustmer.getMessageBIC());
		if (confirmationWhen57AForWord != null && (confirmationWhen57AForWord.getStatusCode().equalsIgnoreCase("ACSP") && confirmationWhen57AForWord.getReasonCode().equalsIgnoreCase("G001"))) {
			if (getStatusOriginatorAsBefisharyBank) {
				bifCustmer.setLineStatus(LineStatus.NoLongerTracableIntemidiantAgent);
			} else {
				bifCustmer.setEndPointStatus(EndPointStatus.ReceivedPaymentNotCreditedIt);
				bifCustmer.setLineStatus(LineStatus.NoLongerTracableIntemidiantAgent);
				bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedPaymentNotCreditedIt));
				try {
					bifCustmer.setArriveDate(gpiProperties.getBicArrivedDate().get(confirmationWhen57AForWord.getForwardedTo().substring(0, 7)));
					bifCustmer.setSendDate("");
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} else if (confirmationWhen57AForWord != null && !getStatusOriginatorAsBefisharyBank) {
			prepperStatusDiv("ACSP");
			gpiProperties.setForwardToBeneficiaryCustomer(true);
			bifCustmer.setEndPointStatus(EndPointStatus.ReceivedPaymentNotCreditedIt);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedPaymentNotCreditedIt));
			bifCustmer.setArriveDate(gpiProperties.getBicArrivedDate().get(confirmationWhen57AForWord.getForwardedTo().substring(0, 7)));
			bifCustmer.setSendDate("");

		}

		if (bifCustmer.getArriveDate() == null) {
			bifCustmer.setDisableReceiveTimeIcone(true);

		} else if (bifCustmer.getArriveDate().isEmpty()) {

			bifCustmer.setDisableReceiveTimeIcone(true);

		} else {
			bifCustmer.setDisableReceiveTimeIcone(false);
		}

		if (bifCustmer.getSendDate() == null) {
			bifCustmer.setDisableSendTimeIcone(true);
		} else if (bifCustmer.getSendDate().isEmpty()) {
			bifCustmer.setDisableSendTimeIcone(true);
		} else {
			bifCustmer.setDisableSendTimeIcone(false);
		}

		gpiProperties.setBeneficiaryBank(bifCustmer);
		messageList.add(bifCustmer);
		if (gpiProperties.isNonTraceableBeneficiaryBank() && (gpiProperties.getNonGpiBic() != null && !gpiProperties.getNonGpiBic().isEmpty())) {
			GpiAgent nonGpiAgent = new GpiAgent();
			nonGpiAgent.setEndPointStatus(EndPointStatus.NotReceiveAndSend);
			nonGpiAgent.setLineStatus(LineStatus.NoLongerTracableIntemidiantAgent);
			nonGpiAgent.setEndPointFormatted(getEndPointStr(EndPointStatus.NotReceiveAndSend));
			nonGpiAgent.setArriveDate(gpiProperties.getBicArrivedDate().get(gpiProperties.getNonGpiBic()));
			nonGpiAgent.setNonGpiAgent(true);
			messageList.add(nonGpiAgent);
		}
	}

	private void updateMessageCharges(GpiConfirmation gpiConfirmation) {
		try {
			for (GpiAgent agent : messageList) {
				if (gpiConfirmation.getStatusOriginator() != null && !gpiConfirmation.getStatusOriginator().isEmpty()) {
					if (agent.getMessageBIC().substring(0, 8).equalsIgnoreCase(gpiConfirmation.getStatusOriginator().substring(0, 8))) {
						agent.setMesgCharges(gpiConfirmation.getMesgCharges());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private GpiConfirmation getConfirmationWhen57AForWord(List<GpiConfirmation> gpiConfirmationsList, String mesgBic) {
		GpiConfirmation tConfirmation = null;
		for (GpiConfirmation confirmation : gpiConfirmationsList) {
			String forwardedTo = confirmation.getForwardedTo();
			if (forwardedTo != null) {
				if (mesgBic.length() != forwardedTo.length() && forwardedTo.substring(0, 7).equalsIgnoreCase(mesgBic.substring(0, 7))) {
					tConfirmation = confirmation;
				} else if (mesgBic.length() == forwardedTo.length() && forwardedTo.equalsIgnoreCase(mesgBic)) {
					tConfirmation = confirmation;
				}
			}
		}

		return tConfirmation;
	}

	private void preparDirictIntermediateAgent(GpiConfirmation gpiConfirmation) {

		if (gpiConfirmation.getStatusCode().equals("RJCT")) {
			prepperStatusDiv("RJCT");
			messageList.get(1).setEndPointStatus(EndPointStatus.IntermediateRejectPayment);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateRejectPayment));
			messageList.get(1).setSendDate(gpiConfirmation.getSendDate());
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());

		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G000")) {
			prepperStatusDiv("ACSP");
			messageList.get(1).setEndPointStatus(EndPointStatus.ReceivedAndSend);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedAndSend));
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			messageList.get(1).setSendDate(gpiConfirmation.getSendDate());
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			messageList.get(1).setTakenTime(calculateTimeTaken(messageList.get(1).getArriveDate(), gpiConfirmation.getSendDate()));
			gpiProperties.getBicArrivedDate().put(gpiConfirmation.getForwardedTo().substring(0, 7), gpiConfirmation.getSendDate());
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
			messageList.get(1).setMesgCharges(gpiConfirmation.getMesgCharges());
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G001")) {
			gpiProperties.setFindNonTracabel(true);
			prepperStatusDiv("ACSP");
			messageList.get(1).setEndPointStatus(EndPointStatus.NonTracabelBank);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.NonTracabelBank));
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			messageList.get(1).setSendDate(gpiConfirmation.getSendDate());
			messageList.get(1).setTakenTime(calculateTimeTaken(messageList.get(1).getArriveDate(), gpiConfirmation.getSendDate()));
			messageList.get(1).setMesgCharges(gpiConfirmation.getMesgCharges());

			try {

				gpiProperties.getBicArrivedDate().put(gpiConfirmation.getForwardedTo().substring(0, 7), gpiConfirmation.getSendDate());
			} catch (Exception e) {
				// TODO: handle exception
			}
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
			if (gpiConfirmation.getForwardedTo() != null && !gpiConfirmation.getForwardedTo().isEmpty()) {
				gpiProperties.getBicArrivedDate().put(gpiConfirmation.getForwardedTo().substring(0, 7), gpiConfirmation.getSendDate());
			}
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G002")) {
			prepperStatusDiv("ACSP");
			messageList.get(1).setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateReceiveWithOutSend));
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G003")) {
			prepperStatusDiv("ACSP");
			messageList.get(1).setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateReceiveWithOutSend));
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G004")) {
			prepperStatusDiv("ACSP");
			messageList.get(1).setEndPointStatus(EndPointStatus.IntermediateReceiveWithOutSend);
			messageList.get(1).setLineStatus(LineStatus.receiveLine);
			messageList.get(1).setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			messageList.get(1).setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			messageList.get(1).setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateReceiveWithOutSend));
			messageList.get(1).setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
		}

		// check if dateTime send
		if (gpiConfirmation.getSendDate() == null) {
			messageList.get(1).setDisableSendTimeIcone(true);
		} else if (gpiConfirmation.getSendDate().isEmpty()) {
			messageList.get(1).setDisableSendTimeIcone(true);
		} else {
			messageList.get(1).setDisableSendTimeIcone(false);
		}

	}

	private void preparBinfisharyCuctumer(GpiConfirmation gpiConfirmation, GpiAgent bifCustmer) {

		gpiProperties.setForwardToBeneficiaryCustomer(true);

		if (gpiConfirmation.getStatusCode().equals("RJCT")) {
			prepperStatusDiv("RJCT");
			bifCustmer.setEndPointStatus(EndPointStatus.IntermediateRejectPayment);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.IntermediateRejectPayment));
			bifCustmer.setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			bifCustmer.setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			bifCustmer.setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			bifCustmer.setArriveDate(gpiProperties.getBicArrivedDate().get(gpiConfirmation.getStatusOriginator().substring(0, 7)));
			bifCustmer.setSendDate(gpiConfirmation.getSendDate());
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
			bifCustmer.setTakenTime(calculateTimeTaken(gpiProperties.getBicArrivedDate().get(gpiConfirmation.getStatusOriginator().substring(0, 7)), gpiConfirmation.getSendDate()));
		} else if (gpiConfirmation.getStatusCode().equals("ACSC") || gpiConfirmation.getStatusCode().equals("ACCC")) {
			prepperStatusDiv("ACCC");
			gpiProperties.setShowDetailsCreditedAmount(true);
			bifCustmer.setEndPointStatus(EndPointStatus.ReceivedAndSend);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedAndSend));
			bifCustmer.setArriveDate(gpiProperties.getBicArrivedDate().get(gpiConfirmation.getStatusOriginator().substring(0, 7)));
			bifCustmer.setSendDate(gpiConfirmation.getSendDate());
			bifCustmer.setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			bifCustmer.setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			bifCustmer.setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
			gpiProperties.setCreditedAmount(
					calculateCreditedAmount("BEN", gpiConfirmation.getCreditedAmount(), gpiConfirmation.getCreditedCur(), gpiConfirmation.getMesgExchangeRate(), gpiConfirmation.getExChangeRateFromCcy(), gpiConfirmation.getExChangeRateToCcy()));
			bifCustmer.setTakenTime(calculateTimeTaken(gpiProperties.getBicArrivedDate().get(gpiConfirmation.getStatusOriginator().substring(0, 7)), gpiConfirmation.getSendDate()));
			bifCustmer.setMesgCharges(completedChargesType);
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G001")) {
			prepperStatusDiv("ACSP");
			bifCustmer.setEndPointStatus(EndPointStatus.NonTracabelBeneficiaryBank);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.NonTracabelBeneficiaryBank));
			bifCustmer.setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			bifCustmer.setMsgSendAmount(gpiConfirmation.getMsgSendAmount());
			bifCustmer.setMsgSendCurr(gpiConfirmation.getMsgSendCurr());
			sumDeducts(gpiConfirmation.getMsgSendAmount(), gpiConfirmation.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
			if (gpiConfirmation.getForwardedTo() != null && !gpiConfirmation.getForwardedTo().isEmpty()) {
				gpiProperties.getBicArrivedDate().put(gpiConfirmation.getForwardedTo().substring(0, 7), gpiConfirmation.getSendDate());
				gpiProperties.setNonGpiBic(gpiConfirmation.getForwardedTo());
			}
			gpiProperties.setNonTraceableBeneficiaryBank(true);
		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && gpiConfirmation.getReasonCode().equals("G000")) {
			prepperStatusDiv("ACSP");
			bifCustmer.setEndPointStatus(EndPointStatus.ReceivedPaymentNotCreditedIt);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedPaymentNotCreditedIt));
			bifCustmer.setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());

		} else if (gpiConfirmation.getStatusCode().equals("ACSP") && (gpiConfirmation.getReasonCode().equals("G002") || gpiConfirmation.getReasonCode().equals("G003") || gpiConfirmation.getReasonCode().equals("G004"))) {
			prepperStatusDiv("ACSP");
			bifCustmer.setEndPointStatus(EndPointStatus.ReceivedPaymentNotCreditedIt);
			bifCustmer.setLineStatus(LineStatus.receiveLine);
			bifCustmer.setMesg_trn_ref(gpiConfirmation.getMsgTrnRef());
			bifCustmer.setEndPointFormatted(getEndPointStr(EndPointStatus.ReceivedPaymentNotCreditedIt));

		}
	}

	private String calculateTimeTaken(String arrDate, String sendDate) {
		String dateStart = arrDate;
		String dateStop = sendDate;
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		String timeTaken = "";
		long diffMinutes = 0;
		long diffHours = 0;
		long diffDays = 0;
		long diffSeconds = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			timeTaken = String.valueOf(diffHours) + " hours " + diffMinutes + " min " + diffSeconds + " sec";
		} catch (Exception e) {
			e.printStackTrace();
		}
		gpiProperties.totalHours = gpiProperties.totalHours + diffHours;
		gpiProperties.totalMin = gpiProperties.totalMin + diffMinutes;
		gpiProperties.totalSec = gpiProperties.totalSec + diffSeconds;

		if (gpiProperties.totalSec >= 60) {
			if (gpiProperties.totalSec == 60) {
				gpiProperties.totalSec = 0;
			} else {
				long sec = gpiProperties.totalSec - 60;
				gpiProperties.totalSec = sec;
			}
			gpiProperties.totalMin = gpiProperties.totalMin + 1;
		}

		if (gpiProperties.totalMin >= 60) {
			if (gpiProperties.totalMin == 60) {
				gpiProperties.totalMin = 0;
			} else {
				long min = gpiProperties.totalMin - 60;
				gpiProperties.totalMin = min;
			}
			gpiProperties.totalHours = gpiProperties.totalHours + 1;

		}
		gpiProperties.setTotalDuration(gpiProperties.totalHours + " hr " + gpiProperties.totalMin + " min " + gpiProperties.totalSec + " sec");
		return timeTaken;
	}

	private String calculateCreditedAmount(String deductsType, String credetedAmount, String credetedCCy, String confirmationExchangeRate, String exChangeRateFromCcy, String exChangeRateToCcy) {

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);

		if (credetedAmount == null || credetedAmount.isEmpty()) {

			return "";
		}
		// get credited amount from last 199 that have status code ACCC "Completed"
		BigDecimal creditedAmountBig = new BigDecimal(credetedAmount.replaceAll("[^0-9?!\\.]", ""));
		String orginalCcy = (gpiProperties.getMessageDetails().getMesgInstructedCur() != null ? gpiProperties.getMessageDetails().getMesgInstructedCur() : gpiProperties.getMessageDetails().getxFinCcy());
		// get total Charges/Deducts From this (instructed amount - credited amount )
		BigDecimal orginalAmount = (gpiProperties.getMessageDetails().getInstructedAmount() != null) ? gpiProperties.getMessageDetails().getInstructedAmount() : gpiProperties.getMessageDetails().getxFinAmount();
		// In-case any one is null so you must take the value from text itself
		if (orginalCcy == null || orginalAmount == null) {
			try {
				ParsedMessage extractParsedMessage = extractParsedMessage(gpiProperties.getMessageDetails().getMesgUnExpandedText());
				List<ParsedField> parsedFields = extractParsedMessage.getParsedFields();
				for (ParsedField field : parsedFields) {
					if (field.getFieldCode() != null && field.getFieldCode().contains("32") && field.getFieldOption().contains("A")) {
						String value = field.getValue();
						orginalCcy = value.substring(6, 9);
						orginalAmount = (BigDecimal) decimalFormat.parse(value.substring(9).replaceAll(",", "."));
						gpiProperties.getMessageDetails().setxFinCcy(orginalCcy);
						gpiProperties.getMessageDetails().setxFinAmount(orginalAmount);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// In case the Original CCY Thats comes from 103 messages not equal with CCY thats comes from 199 Completed message so here
		// We must parsed the amount to original CCY based on the Ex-Change Rate thats comes either form 103 or 199 thats done using calculatExChangeRate()
		if (orginalCcy != null && !orginalCcy.equalsIgnoreCase(credetedCCy)) {
			// Calculation For Exchange Rate
			creditedAmountBig = calculatAmountBasedOnExChangeRate(creditedAmountBig, confirmationExchangeRate, exChangeRateFromCcy, exChangeRateToCcy, orginalCcy);

		}

		// we need to format the amounts as we format any amounts on TDR Using formatAmount
		// In case there is problem with formatAmount we just return a amounts as is without any format
		String credetedAmountFormated = "";
		try {
			credetedAmountFormated = Utils.formatAmount(creditedAmountBig, credetedCCy, currencies).toString();
		} catch (Exception e) {
			credetedAmountFormated = creditedAmountBig.toString();
		}

		// Here we calculated the Deduct based on this (original amount from 103) - (credited Amount from Completed message)
		// we add new if statement only for on customer EMBD as Fowling
		// 1. The currency of field 32 in the MT103 messages will be used as the display currency of the charges of the message, based on a configuration parameter that will be added to them.
		// 2. The calculation of the charges will be to transfer either the instructed amount of the message or the credited amount to the currency of field 32, to perform the below equation and find
		// the charges: instructed amount of MT103 (in field 32 currency) – credited amount of last MT199 with ACCC status (in field 32 currency) = charges (in field 32 currency).
		// 3. The exchange rate in an MT103 message must be quoted as Debit to Credit currency.
		if (orginalAmount != null) {
			if (show71FCharges) {
				String cur32A = gpiProperties.getMessageDetails().getxFinCcy();
				calculateChargesAmount(null, null, cur32A);
			} else if (convertCharges) {
				BigDecimal credit = new BigDecimal(credetedAmount.replaceAll("[^0-9?!\\.]", "")); // still without convert
				String cur32A = gpiProperties.getMessageDetails().getxFinCcy();
				String cur33B = gpiProperties.getMessageDetails().getMesgInstructedCur();
				if (!cur32A.equalsIgnoreCase(cur33B)) { // 33B >> 23A
					orginalAmount = convertInstructedAmountAs32CCYBasedOnExChangeRate(orginalAmount, confirmationExchangeRate, exChangeRateFromCcy, exChangeRateToCcy, cur33B);
				}
				if (!cur32A.equalsIgnoreCase(credetedCCy)) {
					credit = calculatAmountBasedOnExChangeRate(credit, confirmationExchangeRate, exChangeRateFromCcy, exChangeRateToCcy, credetedCCy);
				}
				calculateChargesAmount(orginalAmount, credit, cur32A);

			} else {
				calculateChargesAmount(orginalAmount, creditedAmountBig, orginalCcy);
			}
		}

		gpiProperties.setCredetedCCy(credetedCCy);
		// Just for UI IF This flag True so we need just to show the value before convert
		if (showCreditedAmountBeforeConvert)

		{
			return revertExChangeConvertorForCreditedAmount(credetedAmount, credetedCCy);
		}

		return credetedAmountFormated;

	}

	private boolean checkIfAllChargesEqualsZero(String allSenderChargeAmount) {
		String[] allSenderChargeAmountArry = allSenderChargeAmount.split(",");

		// check if only zero charges
		boolean zeroCharges = true;

		for (String charg : allSenderChargeAmountArry) {
			if (charg.equalsIgnoreCase("0") || charg.equalsIgnoreCase("0.0") || charg.equalsIgnoreCase("0.00")) {
				// DoNoting
			} else {
				zeroCharges = false;
				break;
			}

		}
		return zeroCharges;
	}

	private List<String> getAll71FCharges(String allCharges) {
		List<String> allDeducts = new ArrayList<>();
		String[] split = allCharges.split(":");
		String[] amount = split[0].split(",");
		String[] cur = split[1].split(",");
		for (int i = 0; i <= amount.length - 1; i++) {
			allDeducts.add(amount[i] + " " + cur[i]);
		}
		return allDeducts;
	}

	public static boolean checkIfAllChargesCurEquals(String allSenderCur) {
		String[] allSenderCurArry = allSenderCur.split(",");
		for (int i = 1; i < allSenderCurArry.length; i++) {
			if (!allSenderCurArry[0].equalsIgnoreCase(allSenderCurArry[i])) {
				return false;
			}
		}

		return true;
	}

	private void calculateChargesAmount(BigDecimal amountOne, BigDecimal amountTow, String ccy) {

		if (calcluteChargesAlradyChecked) {
			return;
		}

		String allCharges = getAllCharges();// ALL_DEDUCTS
		if (show71FCharges) {
			checkIfChargesCodeContinsOur();
			if (allCharges != null && !allCharges.isEmpty()) {
				if (allCharges.contains(":")) {
					List<String> all71fCharges = getAll71FCharges(allCharges);
					if (all71fCharges != null && !all71fCharges.isEmpty()) {
						String all71fChargesStr = String.join(" + ", all71fCharges);
						gpiProperties.setTotalDeductsStr(all71fChargesStr);

						if (chargesTypeList.size() < all71fCharges.size()) {
							if (completedChargesType != null && !chargesTypeList.isEmpty()) {
								chargesTypeList.add(completedChargesType);
								addedCompletedChargesType = true;
							}
						}
						try {
							if (chargesTypeList.size() > all71fCharges.size()) {
								chargesTypeList = chargesTypeList.subList(0, all71fCharges.size());
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						gpiProperties.setAllDeductType(String.join(" + ", chargesTypeList));
					}

				}
				return;
			} else {

				gpiProperties.setTotalDeductsStr("0.00" + " " + ccy);
				gpiProperties.setDeductCur(ccy);
				return;
			}
		} else {
			DecimalFormat chargesDecimalFormat = new DecimalFormat("####.00");
			// check if original amount equal with credited amount
			if (amountOne.compareTo(amountTow) == -1 || amountOne.compareTo(amountTow) == 0) {
				gpiProperties.setDeductCur(ccy);
				gpiProperties.setTotalDeductsStr("0.00");
				return;
			}

			if (allCharges != null && !allCharges.isEmpty()) {
				if (allCharges.contains(":")) {
					String[] split = allCharges.split(":");
					if (split.length == 2) {
						// check if all charges equal zero so we don't need to do any convert
						boolean zeroCharges = checkIfAllChargesEqualsZero(split[0]);

						if (zeroCharges) {
							gpiProperties.setDeductCur(ccy);
							gpiProperties.setTotalDeductsStr("0.00");

							// check again
							BigDecimal subtract = amountOne.subtract(amountTow);
							BigDecimal one = new BigDecimal("1");
							if (subtract.compareTo(one) == 0 || subtract.compareTo(one) == -1) {
								return;
							} else {
								if (calculateChargesWhenZero) {
									if (needFormater) {
										try {
											chargesDecimalFormat.setRoundingMode(RoundingMode.DOWN);
											if (!useFormatAmount) {
												gpiProperties.setTotalDeductsStr(chargesDecimalFormat.format(subtract));
											} else {
												gpiProperties.setTotalDeductsStr(Utils.formatAmount(subtract, ccy, currencies).toString());
											}
										} catch (Exception e) {
											gpiProperties.setTotalDeductsStr(subtract.toString());
										}
									} else {
										gpiProperties.setTotalDeductsStr(subtract.toString());
									}
									gpiProperties.setDeductCur(ccy);
									return;
								} else {
									return;
								}

							}

						}
						boolean allChargesCurEquals = checkIfAllChargesCurEquals(split[1]);
						if (allChargesCurEquals) {
							gpiProperties.setDeductCur(ccy);
							double sum = 0;
							for (String charge : split[0].split(",")) {
								sum = sum + Double.parseDouble(charge);
							}
							BigDecimal sumBig = new BigDecimal(sum);
							if (!useFormatAmount) {
								gpiProperties.setTotalDeductsStr(chargesDecimalFormat.format(sumBig));
							} else {
								gpiProperties.setTotalDeductsStr(Utils.formatAmount(sumBig, ccy, currencies).toString());
							}
							return;
						}
					}

				}

			}

			BigDecimal subtract = amountOne.subtract(amountTow);

			// needFormater when you calculated creditedAmount using multiply so the amount as Bigdicimal will be not formated , but if you use divide so we don't need for formatter
			if (needFormater) {
				try {

					chargesDecimalFormat.setRoundingMode(RoundingMode.DOWN);
					if (!useFormatAmount) {
						gpiProperties.setTotalDeductsStr(chargesDecimalFormat.format(subtract));
					} else {
						gpiProperties.setTotalDeductsStr(Utils.formatAmount(subtract, ccy, currencies).toString());
					}
				} catch (Exception e) {
					gpiProperties.setTotalDeductsStr(subtract.toString());
				}
			} else {
				gpiProperties.setTotalDeductsStr(subtract.toString());
			}
			gpiProperties.setDeductCur(ccy);
		}

	}

	private void checkIfChargesCodeContinsOur() {
		try {
			boolean continseOtheChargesCode = false;
			for (String chargeCode : chargesTypeList) {
				if (chargeCode.equalsIgnoreCase("BEN") || chargeCode.equalsIgnoreCase("SHA")) {
					continseOtheChargesCode = true;
				}
			}

			if (continseOtheChargesCode) {
				chargesTypeList.remove("OUR");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private String getAllCharges() {
		List<DetailsHistory> detailsHistoriesList = gpiProperties.getDetailsHistoriesList();
		if (detailsHistoriesList == null || detailsHistoriesList.isEmpty()) {
			return null;
		}
		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType().equalsIgnoreCase("199") && detailsHistory.getStatusCode().equalsIgnoreCase("ACCC")) {
				if (detailsHistory.getAllDeducts() != null && !detailsHistory.getAllDeducts().isEmpty()) {
					if (detailsHistory.getMesgSubFormat().equalsIgnoreCase("Input")) {
						String senderBic = detailsHistory.getOrginlaSender();
						senderBic = (senderBic == null || senderBic.isEmpty()) ? detailsHistory.getSenderBic() : senderBic;
						String mesgBeneficiaryBankCode = gpiProperties.getMessageDetails().getMesgBeneficiaryBankCode();
						if (mesgBeneficiaryBankCode != null && !mesgBeneficiaryBankCode.isEmpty()) {
							if (senderBic.substring(0, 7).equalsIgnoreCase(mesgBeneficiaryBankCode.substring(0, 7))) {
								completedChargesType = "";
								String chargesType = detailsHistory.getMesgCharges();
								if (chargesType != null && !chargesType.isEmpty()) {
									completedChargesType = chargesType;
								} else {
									completedChargesType = "undefined";
								}
								return detailsHistory.getAllDeducts();
							}
						}
					}
				}
			}
		}

		for (DetailsHistory detailsHistory : detailsHistoriesList) {
			if (detailsHistory.getMsgType().equalsIgnoreCase("199") && detailsHistory.getStatusCode().equalsIgnoreCase("ACCC")) {
				if (detailsHistory.getAllDeducts() != null && !detailsHistory.getAllDeducts().isEmpty()) {
					completedChargesType = "";
					String chargesType = detailsHistory.getMesgCharges();
					if (chargesType != null && !chargesType.isEmpty()) {
						completedChargesType = chargesType;
					} else {
						completedChargesType = "undefined";
					}
					return detailsHistory.getAllDeducts();
				}
			}
		}
		return "";
	}

	/**
	 * For Solving 59822 : TDR 3.2.5: GPI - edit the currency of the credited amount. Based on showCreditedAmountBeforeConvert Flag return the original value for Credited Amount before Exchange Rate
	 * just for show on UI But we still using the value after convert for calculated deduct
	 * 
	 */
	private String revertExChangeConvertorForCreditedAmount(String credetedAmount, String credetedCCy) {
		BigDecimal creditedAmountBig = new BigDecimal(credetedAmount.replaceAll("[^0-9?!\\.]", ""));
		String credetedAmountFormated = "";
		try {
			credetedAmountFormated = Utils.formatAmount(creditedAmountBig, credetedCCy, currencies).toString();
		} catch (Exception e) {
			credetedAmountFormated = creditedAmountBig.toString();
		}

		return credetedAmountFormated;
	}

	private ParsedMessage extractParsedMessage(String text) {

		ParsedMessage parsedMessage = new ParsedMessage();

		// check 77E field (sub message)
		String field77E = null;
		Matcher matcher77e = pattern77e.matcher(text);
		if (matcher77e.find()) {
			field77E = matcher77e.group(1);
			text = matcher77e.replaceAll("");
		}

		/*
		 * fixed for the following bugs 32142 Dynamic Report: MT586 Message Expand Failed 32140 Dynamic Report: MT565 Message Expand Failed work arround to ignore last space char in text
		 */
		text = text.replaceAll("\\s+$", "");
		// line splitter
		String[] fields = text.split("\\\\r\\\\n:|\\r\\n:|\n:");

		// create ParsedField and attach it to the ParsedMessage object
		for (String field : fields) {
			Matcher matcher = pattern.matcher(":" + field);
			if (matcher.find()) {

				ParsedField parsedField = new ParsedField();
				String code = matcher.group(1);
				parsedField.setFieldCode(code);
				parsedField.setFieldOption(getFieldCodeOption(code));
				parsedField.setValue(matcher.group(2));
				parsedMessage.addParsedField(parsedField);
			}
		}

		// add sub message (77E)
		if (field77E != null) {
			ParsedField parsedField = new ParsedField();
			parsedField.setFieldCode("77E");
			parsedField.setValue(field77E);
			parsedField.setFieldOption("E");
			parsedMessage.addParsedField(parsedField);
		}

		return parsedMessage;
	}

	private String getFieldCodeOption(String code) {
		if (code != null && code.length() == 3) {
			return code.substring(2);
		}
		return null;
	}

	/**
	 * In case the Original CCY Thats comes from 103 messages not equal with CCY thats comes from 199 Completed message so here We must parsed the amount to original CCY based on the Ex-Change Rate
	 * thats comes either form 103 or 199 thats done using calculatExChangeRate()
	 * 
	 * @return {@link BigDecimal}
	 * 
	 */
	private BigDecimal calculatAmountBasedOnExChangeRate(BigDecimal creditedAmountBig, String confirmationExchangeRate, String exChangeRateFromCcy, String exChangeRateToCcy, String orginalCcy) {
		needFormater = false;

		// get Exchange rate from complete 199
		String exChangeRateStr = (confirmationExchangeRate);

		// If exChange rate still null so we need to get it from others 199 messages
		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			exChangeRateStr = getExChangeRateFrom199();
		}

		// get exchange rate from 103
		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			exChangeRateStr = gpiProperties.getMessageDetails().getMesgExchangeRate();

		}

		// If exChange rate still null you must take it from 103 input messages In-Case Your direction is output
		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			DetailsHistory outGoing = getMessageFromDetailsHistory("INPUT", "");
			exChangeRateStr = outGoing.getMesgExchangeRate();
		}

		// if it is not empty so its coming from 199 completed

		if (exChangeRateFromCcy == null || exChangeRateFromCcy.isEmpty()) {
			exChangeRateFromCcy = getExChangeRateCcyFrom199();
		}

		if (exChangeRateFromCcy == null || exChangeRateFromCcy.isEmpty()) {
			exChangeRateFromCcy = gpiProperties.getMessageDetails().getMesgInstructedCur();
		}

		// Get the Exchange rate from latest 199 /103
		if (exChangeRateStr != null && !exChangeRateStr.isEmpty()) {
			exChangeRateStr = exChangeRateStr.replaceAll(",", ".");
			BigDecimal exChangeRateBigDecimal = new BigDecimal(exChangeRateStr);
			BigDecimal amountAfterApplingExChangeRate = null;

			if (exChangeRateFromCcy != null && exChangeRateFromCcy.equalsIgnoreCase(orginalCcy)) {
				needFormater = true;
				try {
					amountAfterApplingExChangeRate = creditedAmountBig.multiply(exChangeRateBigDecimal);
				} catch (Exception e) {
					amountAfterApplingExChangeRate = new BigDecimal(creditedAmountBig.doubleValue() * exChangeRateBigDecimal.doubleValue());
				}

			} else {
				try {
					amountAfterApplingExChangeRate = creditedAmountBig.divide(exChangeRateBigDecimal, 2, RoundingMode.HALF_EVEN);
				} catch (Exception e) {
					amountAfterApplingExChangeRate = creditedAmountBig.divide(exChangeRateBigDecimal, RoundingMode.HALF_EVEN);
				}
			}

			return amountAfterApplingExChangeRate;
		}
		return creditedAmountBig;
	}

	/**
	 * In case the Original CCY Thats comes from 103 messages not equal with CCY thats comes from 199 Completed message so here We must parsed the amount to original CCY based on the Ex-Change Rate
	 * thats comes either form 103 or 199 thats done using calculatExChangeRate()
	 * 
	 * @return {@link BigDecimal}
	 * 
	 */
	private BigDecimal convertInstructedAmountAs32CCYBasedOnExChangeRate(BigDecimal creditedAmountBig, String confirmationExchangeRate, String exChangeRateFromCcy, String exChangeRateToCcy, String orginalCcy) {
		needFormater = false;

		// get Exchange rate from complete 199
		String exChangeRateStr = (confirmationExchangeRate);

		// If exChange rate still null so we need to get it from others 199 messages
		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			exChangeRateStr = getExChangeRateFrom199();
		}

		// get exchange rate from 103

		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			exChangeRateStr = gpiProperties.getMessageDetails().getMesgExchangeRate();
		}

		// If exChange rate still null you must take it from 103 input messages In-Case Your direction is output
		if (exChangeRateStr == null || exChangeRateStr.isEmpty()) {
			DetailsHistory outGoing = getMessageFromDetailsHistory("INPUT", "");
			exChangeRateStr = outGoing.getMesgExchangeRate();
		}

		// if it is not empty so its coming from 199 completed

		if (exChangeRateFromCcy == null || exChangeRateFromCcy.isEmpty()) {
			exChangeRateFromCcy = getExChangeRateCcyFrom199();
		}

		if (exChangeRateFromCcy == null || exChangeRateFromCcy.isEmpty()) {
			exChangeRateFromCcy = gpiProperties.getMessageDetails().getMesgInstructedCur();// EUR//USD
		}

		// Get the Exchange rate from latest 199 /103
		if (exChangeRateStr != null && !exChangeRateStr.isEmpty()) {
			exChangeRateStr = exChangeRateStr.replaceAll(",", ".");
			BigDecimal exChangeRateBigDecimal = new BigDecimal(exChangeRateStr);
			BigDecimal amountAfterApplingExChangeRate = null;
			// EUR//USD//
			if (exChangeRateFromCcy != null && exChangeRateFromCcy.equalsIgnoreCase(orginalCcy)) {
				needFormater = true;
				try {
					amountAfterApplingExChangeRate = creditedAmountBig.multiply(exChangeRateBigDecimal);
				} catch (Exception e) {
					amountAfterApplingExChangeRate = new BigDecimal(creditedAmountBig.doubleValue() * exChangeRateBigDecimal.doubleValue());
				}

			} else {
				try {
					amountAfterApplingExChangeRate = creditedAmountBig.divide(exChangeRateBigDecimal, 2, RoundingMode.HALF_EVEN);
				} catch (Exception e) {
					amountAfterApplingExChangeRate = creditedAmountBig.divide(exChangeRateBigDecimal, RoundingMode.HALF_EVEN);
				}
			}

			return amountAfterApplingExChangeRate;
		}
		return creditedAmountBig;
	}

	public static void main(String[] args) {
		/*
		 * BigDecimal exChangeRateBigDecimal = new BigDecimal("3.76215"); BigDecimal multiply = new BigDecimal("12443.66").multiply(exChangeRateBigDecimal); System.out.println(multiply); DecimalFormat
		 * decimalFormat = new DecimalFormat("####.00"); decimalFormat.setRoundingMode(RoundingMode.DOWN); // System.out.println(decimalFormat.format(multiply));
		 * 
		 * System.out.println(decimalFormat.format(new BigDecimal("15").subtract(new BigDecimal("14.99400"))));
		 * 
		 * // System.out.println(3.76215 * 12443.66);
		 */

		// BigDecimal one = new BigDecimal("0.414");
		// BigDecimal two = new BigDecimal("1");

		// System.out.println(one.compareTo(two));

		List<String> all71fCharges = new GPIServiceImpl().getAll71FCharges("105.0,0.0:AED,GBP");
		System.out.println(all71fCharges.subList(0, 2));
	}

	private boolean bicExsist(String bic) {
		for (String bicName : gpiProperties.getBicList()) {
			if (bic == null || bic.equals("")) {
				return true;
			} else if (bicName.substring(0, 7).equalsIgnoreCase(bic.substring(0, 7))) {
				return true;
			}
		}
		return false;
	}

	private boolean forwordingBicExsist(String bic) {
		for (String bicName : gpiProperties.getForwordingBicList()) {
			if (bic == null || bic.equals("")) {
				return true;
			} else if (bicName.substring(0, 7).equalsIgnoreCase(bic.substring(0, 7))) {
				return true;
			}
		}
		return false;
	}

	private GpiAgent createGpiAgentWithStatus(GpiConfirmation gpiConfirmation, List<GpiConfirmation> gpiConfirmationsList) {
		GpiAgent agent = null;

		if (gpiConfirmation.getReasonCode() != null && (gpiConfirmation.getReasonCode().equals("G001"))) {
			gpiProperties.setForwardToNonGpiAgent(true);
			GpiAgent nonGpiAgent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(), "", null,
					LineStatus.NoLongerTracableIntemidiantAgent, EndPointStatus.NonTracabelBank, false, gpiProperties.getBicArrivedDate().get(gpiConfirmation.getForwardedTo().substring(0, 7)), "", "", "", "");
			nonGpiAgent.setNonGpiAgent(true);
			return nonGpiAgent;
		}

		for (GpiConfirmation confirmationForStatusUpdate : gpiConfirmationsList) {
			if (gpiConfirmation.getForwardedTo().substring(0, 7).equals(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7))) {
				if (confirmationForStatusUpdate.getStatusCode().equals("RJCT")) {
					prepperStatusDiv("RJCT");
					agent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(),
							confirmationForStatusUpdate.getMsgTrnRef(), null, LineStatus.receiveLine, EndPointStatus.IntermediateRejectPayment, false,
							gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)), "", confirmationForStatusUpdate.getMsgSendAmount(), confirmationForStatusUpdate.getMsgSendCurr(), "");
				} else if (confirmationForStatusUpdate.getStatusCode().equals("ACSP") && confirmationForStatusUpdate.getReasonCode().equals("G000")) {
					prepperStatusDiv("ACSP");
					gpiProperties.getBicList().add(gpiConfirmation.getForwardedTo());
					gpiProperties.getBicArrivedDate().put(confirmationForStatusUpdate.getForwardedTo().substring(0, 7), confirmationForStatusUpdate.getSendDate());
					agent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(),
							confirmationForStatusUpdate.getMsgTrnRef(), null, LineStatus.receiveLine, EndPointStatus.ReceivedAndSend, false, gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)),
							confirmationForStatusUpdate.getSendDate(), confirmationForStatusUpdate.getMsgSendAmount(), confirmationForStatusUpdate.getMsgSendCurr(), "");
					agent.setTakenTime(calculateTimeTaken(gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)), confirmationForStatusUpdate.getSendDate()));
					sumDeducts(confirmationForStatusUpdate.getMsgSendAmount(), confirmationForStatusUpdate.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());

				} else if (confirmationForStatusUpdate.getStatusCode().equals("ACSP") && (confirmationForStatusUpdate.getReasonCode().equals("G002") || confirmationForStatusUpdate.getReasonCode().equals("G003"))) {
					prepperStatusDiv("ACSP");
					gpiProperties.getBicList().add(gpiConfirmation.getForwardedTo());
					agent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(),
							confirmationForStatusUpdate.getMsgTrnRef(), null, LineStatus.receiveLine, EndPointStatus.IntermediateReceiveWithOutSend, false,
							gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)), "", "", "", "");
				} else if (confirmationForStatusUpdate.getStatusCode().equals("ACSP") && confirmationForStatusUpdate.getReasonCode().equals("G004")) {
					prepperStatusDiv("ACSP");
					agent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(),
							confirmationForStatusUpdate.getMsgTrnRef(), null, LineStatus.receiveLine, EndPointStatus.IntermediateReceiveWithOutSend, false,
							gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)), confirmationForStatusUpdate.getSendDate(), "", "", "");
				}

				else if (confirmationForStatusUpdate.getStatusCode().equals("ACSP") && confirmationForStatusUpdate.getReasonCode().equals("G001")) {
					gpiProperties.setFindNonTracabel(true);
					prepperStatusDiv("ACSP");
					gpiProperties.getBicList().add(gpiConfirmation.getForwardedTo());
					agent = gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(),
							confirmationForStatusUpdate.getMsgTrnRef(), null, LineStatus.receiveLine, EndPointStatus.NonTracabelBank, false, gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)),
							confirmationForStatusUpdate.getSendDate(), confirmationForStatusUpdate.getMsgSendAmount(), confirmationForStatusUpdate.getMsgSendCurr(), "");
					agent.setTakenTime(calculateTimeTaken(gpiProperties.getBicArrivedDate().get(confirmationForStatusUpdate.getStatusOriginator().substring(0, 7)), confirmationForStatusUpdate.getSendDate()));
					gpiProperties.getBicArrivedDate().put(confirmationForStatusUpdate.getForwardedTo().substring(0, 7), confirmationForStatusUpdate.getSendDate());
					sumDeducts(confirmationForStatusUpdate.getMsgSendAmount(), confirmationForStatusUpdate.getMsgSendCurr(), gpiProperties.getMessageDetails().getMesgCharges());
					agent.setMesgCharges(gpiConfirmation.getMesgCharges());

				}

			}

		}
		if (agent == null) {
			boolean forwordingBicExsist = forwordingBicExsist(gpiConfirmation.getForwardedTo());
			if (!forwordingBicExsist) {
				gpiProperties.getForwordingBicList().add(gpiConfirmation.getForwardedTo());
				return gpiAgentCreator(getCityName(gpiConfirmation.getForwardedTo()), getCountryName(gpiConfirmation.getForwardedTo()), getInstitutionName(gpiConfirmation.getForwardedTo()), gpiConfirmation.getForwardedTo(), "", null,
						LineStatus.receiveLine, EndPointStatus.IntermediateReceiveWithOutSend, false, gpiProperties.getBicArrivedDate().get(gpiConfirmation.getForwardedTo().substring(0, 7)), "", "", "", "");
			} else {
				return agent;
			}
		}

		return agent;
	}

	private boolean isNonCoveGpiAgent(String forwardedTo) {
		boolean isNonGpi = false;
		for (String coveAgent : gpiProperties.getNonGpiCoveAgent()) {
			if (forwardedTo.equalsIgnoreCase(coveAgent)) {
				isNonGpi = true;
			}
		}
		return isNonGpi;
	}

	private void prepareGSRP(List<GpiConfirmation> gpiConfirmationsList, String trackerBic) {

		List<GpiConfirmation> gsrpMessagesList = serviceLocator.getViewerService().getGpiAgent(trackerBic, gpiProperties.getUniqueEndToEndTransactionReference(), gpiProperties.getTimeZoneOffset(), gpiProperties.getParam().getSourceSelectedSAA(),
				true, gpiProperties.getMessageDetails().getMesgCreaDateTime(), "'199'", "'192'", "'196'");

		prepareInitiateGSRP(gsrpMessagesList);
		perpareTrackerResponse(gpiConfirmationsList);
		prepareAgentResponse(gpiConfirmationsList);

	}

	private void prepareAgentResponse(List<GpiConfirmation> gpiConfirmationsList) {
		for (GpiConfirmation gpiConfirmation : gpiConfirmationsList) {
			if (gpiConfirmation.getStatusCode() != null && gpiConfirmation.getStatusCode().equalsIgnoreCase("CNCL")) {
				setGSRPStatusOnAgent(gpiConfirmation.getStatusOriginator(), gpiConfirmation.getStatusCode(), gpiConfirmation.getReasonCode());
				gpiProperties.setHasgSRPRequest(true);
				String institutionName = getInstitutionName(gpiConfirmation.getStatusOriginator());
				gpiProperties.setgSRPAgentResponse(GSRPReasonCode.valueOf(gpiConfirmation.getStatusCode()).getDesc() + (institutionName != null && !institutionName.isEmpty() ? " at " + institutionName : ""));
				// gpiProperties.setCreditedAmount(calculateCreditedAmount("BEN"));
				GpiConfirmation finalConfirmation = gpiConfirmationsList.get(gpiConfirmationsList.size() - 1);
				if (finalConfirmation.getStatusCode() != null && finalConfirmation.getStatusCode().equalsIgnoreCase("RJCT")) {
					prepperStatusDiv("RJCT");
				} else {
					prepperStatusDiv("CNCL");
				}

			} else if (gpiConfirmation.getStatusCode() != null && ((gpiConfirmation.getStatusCode().equalsIgnoreCase("RJCR") && !gpiConfirmation.getReasonCode().equalsIgnoreCase("FRNA"))
					|| (gpiConfirmation.getStatusCode().equalsIgnoreCase("PDCR") && !gpiConfirmation.getReasonCode().contains("00")))) {
				setGSRPStatusOnAgent(gpiConfirmation.getStatusOriginator(), gpiConfirmation.getStatusCode(), gpiConfirmation.getReasonCode());
				gpiProperties.setHasgSRPRequest(true);
				String institutionName = getInstitutionName(gpiConfirmation.getStatusOriginator());
				gpiProperties.setgSRPAgentResponse(GSRPReasonCode.valueOf(gpiConfirmation.getReasonCode()).getDesc() + (institutionName != null && !institutionName.isEmpty() ? " at " + institutionName : ""));

			}
		}

	}

	private void prepareInitiateGSRP(List<GpiConfirmation> gsrpMessagesList) {

		for (GpiConfirmation gsrpMessage : gsrpMessagesList) {
			if (messageList.get(0).isgSRPParty()) {
				break;
			}
			if ((gsrpMessage.getMesgType().equalsIgnoreCase("199") || gsrpMessage.getMesgType().equalsIgnoreCase("192")) && gsrpMessage.getMesgSla().equalsIgnoreCase("002")) {
				if (gsrpMessage.getMesgSubFormat().equalsIgnoreCase("INPUT") || gpiProperties.getMessageDetails().getMesgSubFormat().equalsIgnoreCase("Output")) {
					if (gsrpMessage.getStatusCode() == null && gsrpMessage.getReasonCode() != null && !gsrpMessage.getReasonCode().isEmpty()) {
						gpiProperties.setHasgSRPRequest(true);
						if (gsrpMessage.getReasonCode().equalsIgnoreCase("CUST") || gsrpMessage.getReasonCode().equalsIgnoreCase("AGNT")) {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getReasonCode() + "REQ").getDesc());
						} else {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getReasonCode()).getDesc());
						}
						messageList.get(0).setgSRPParty(true);
						messageList.get(0).setgSRPMsg("Stop & recall request initiated");
					} else {
						gpiProperties.setHasgSRPRequest(true);
						if (gsrpMessage.getStatusCode().equalsIgnoreCase("CUST") || gsrpMessage.getStatusCode().equalsIgnoreCase("AGNT")) {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getStatusCode() + "REQ").getDesc());
						} else {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getStatusCode()).getDesc());
						}
						messageList.get(0).setgSRPParty(true);
						messageList.get(0).setgSRPMsg("Stop & recall request initiated");
					}

				}

			}
		}
	}

	private boolean prepareInitiateGSRPForIntemadaryCase(List<GpiConfirmation> gsrpMessagesList) {
		try {

			for (GpiConfirmation gsrpMessage : gsrpMessagesList) {
				if ((gsrpMessage.getMesgType().equalsIgnoreCase("199") || gsrpMessage.getMesgType().equalsIgnoreCase("192")) && gsrpMessage.getMesgSla().equalsIgnoreCase("002") && gsrpMessage.getMesgSubFormat().equalsIgnoreCase("Output")) {

					if (gsrpMessage.getStatusCode() == null && gsrpMessage.getReasonCode() != null && !gsrpMessage.getReasonCode().isEmpty()) {
						gpiProperties.setHasgSRPRequest(true);
						if (gsrpMessage.getReasonCode().equalsIgnoreCase("CUST") || gsrpMessage.getReasonCode().equalsIgnoreCase("AGNT")) {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getReasonCode() + "REQ").getDesc());
						} else {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getReasonCode()).getDesc());
						}
						return true;
					} else {
						gpiProperties.setHasgSRPRequest(true);
						if (gsrpMessage.getStatusCode().equalsIgnoreCase("CUST") || gsrpMessage.getStatusCode().equalsIgnoreCase("AGNT")) {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getStatusCode() + "REQ").getDesc());
						} else {
							gpiProperties.setgSRPRequestReason(GSRPReasonCode.valueOf(gsrpMessage.getStatusCode()).getDesc());
						}
						return true;
					}

				}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private void perpareTrackerResponse(List<GpiConfirmation> gpiConfirmationsList) {

		for (GpiConfirmation gpiConfirmation : gpiConfirmationsList) {
			if (gpiConfirmation.getStatusCode() != null && ((gpiConfirmation.getStatusCode().equalsIgnoreCase("PDCR") && gpiConfirmation.getReasonCode().contains("00"))
					|| (gpiConfirmation.getStatusCode().equalsIgnoreCase("RJCR") && (gpiConfirmation.getReasonCode().equalsIgnoreCase("FRNA"))))) {

				gpiProperties.setHasgSRPRequest(true);
				if (gpiConfirmation.getReasonCode() != null) {
					gpiProperties.setgSRPTrackerResponse(GSRPReasonCode.valueOf(gpiConfirmation.getStatusCode().trim() + gpiConfirmation.getReasonCode().trim()).getDesc());
				}
			}

		}

	}

	private void setGSRPStatusOnAgent(String agentBic, String statusCode, String reasonCode) {
		for (GpiAgent agent : messageList) {
			if (agent.getMessageBIC().substring(0, 7).equalsIgnoreCase((agentBic != null && !agentBic.isEmpty()) ? agentBic.substring(0, 7) : "")) {
				agent.setgSRPParty(true);
				if (statusCode.equalsIgnoreCase("CNCL")) {
					agent.setEndPointStatus(EndPointStatus.CANCELLED);
					agent.setgSRPMsg(GSRPReasonCode.valueOf(statusCode).getDesc());
				} else if (statusCode.equalsIgnoreCase("PDCR")) {
					agent.setgSRPMsg(GSRPReasonCode.valueOf(reasonCode).getDesc());
				} else if (statusCode.equalsIgnoreCase("RJCR")) {
					agent.setgSRPMsg(GSRPReasonCode.valueOf(reasonCode).getDesc());
				}
				return;
			}
		}
	}

	private void drawAgentWithCurrentStatus(GpiAgent gpiAgent, String statusCode, GpiConfirmation status, EndPointStatus endPointStatus, LineStatus lineStatus) {
		prepperStatusDiv(statusCode);
		gpiAgent.setEndPointStatus(endPointStatus);
		gpiAgent.setLineStatus(lineStatus);
		gpiAgent.setEndPointFormatted(getEndPointStr(endPointStatus));
		gpiAgent.setMesg_trn_ref(status.getMsgTrnRef());

		gpiAgent.setMsgSendAmount(status.getMsgSendAmount());
		gpiAgent.setMsgSendCurr(status.getMsgSendCurr());
		if (status.getSendDate() != null && !status.getSendDate().isEmpty()) {
			gpiAgent.setSendDate(status.getSendDate());
			gpiAgent.setDisableSendTimeIcone(false);
			gpiAgent.setTakenTime(calculateTimeTaken(gpiAgent.getArriveDate(), gpiAgent.getSendDate()));

		}

	}

	public List<GpiAgent> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<GpiAgent> messageList) {
		this.messageList = messageList;
	}

	public GpiType getGpiType() {
		return gpiType;
	}

	public void setGpiType(GpiType gpiType) {
		this.gpiType = gpiType;
	}

	public List<GpiAgent> getCoveAgents() {
		return coveAgents;
	}

	public void setCoveAgents(List<GpiAgent> coveAgents) {
		this.coveAgents = coveAgents;
	}

	public static String getDlvacked() {
		return DLVACKED;
	}

	public List<GpiAgent> getBeforeIntermediateAgents() {
		return beforeIntermediateAgents;
	}

	public void setBeforeIntermediateAgents(List<GpiAgent> beforeIntermediateAgents) {
		this.beforeIntermediateAgents = beforeIntermediateAgents;
	}

	private String getChargesAmountFor103() {
		String mesgSndChargesAmount = gpiProperties.getMessageDetails().getMesgSndChargesAmount();
		String mesgSndChargesCur = gpiProperties.getMessageDetails().getMesgSndChargesCurr();
		if ((mesgSndChargesCur != null && !mesgSndChargesCur.isEmpty())) {
			return mesgSndChargesAmount;
		}
		return gpiProperties.getMessageDetails().getMesgRcvChargesAmount();

	}

	private String getChargesCurFor103() {
		String mesgSndChargesCur = gpiProperties.getMessageDetails().getMesgSndChargesCurr();
		if (mesgSndChargesCur != null && !mesgSndChargesCur.isEmpty()) {
			return mesgSndChargesCur;
		}
		return gpiProperties.getMessageDetails().getMesgRcvChargesCurr();

	}

	public Map<String, CorrInfo> getBicExpandedMap() {
		return bicExpandedMap;
	}

	public void setBicExpandedMap(Map<String, CorrInfo> bicExpandedMap) {
		this.bicExpandedMap = bicExpandedMap;
	}

	public ViewerDAO getViewerDAO() {
		return viewerDAO;
	}

	public void setViewerDAO(ViewerDAO viewerDAO) {
		this.viewerDAO = viewerDAO;
	}

	public boolean isCalculateChargesWhenZero() {
		return calculateChargesWhenZero;
	}

	public void setCalculateChargesWhenZero(boolean calculateChargesWhenZero) {
		this.calculateChargesWhenZero = calculateChargesWhenZero;
	}

	public boolean isCheckOnlyonBic6() {
		return checkOnlyonBic6;
	}

	public void setCheckOnlyonBic6(boolean checkOnlyonBic6) {
		this.checkOnlyonBic6 = checkOnlyonBic6;
	}

	public boolean isShow71FCharges() {
		return show71FCharges;
	}

	public void setShow71FCharges(boolean show71fCharges) {
		show71FCharges = show71fCharges;
	}

}