package com.eastnets.service.loader.parser;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.beans.AMPData.Header;
import com.eastnets.beans.AMPData.InterAct;
import com.eastnets.mx.mapping.AddressFullName;
import com.eastnets.mx.mapping.AddressInfo;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.FINNetworkInfo;
import com.eastnets.mx.mapping.FileDigestAlgorithm;
import com.eastnets.mx.mapping.InterfaceInfo;
import com.eastnets.mx.mapping.Message;
import com.eastnets.mx.mapping.MessageContext;
import com.eastnets.mx.mapping.MessageCreator;
import com.eastnets.mx.mapping.MessageNature;
import com.eastnets.mx.mapping.Network;
import com.eastnets.mx.mapping.NetworkInfo;
import com.eastnets.mx.mapping.Priority;
import com.eastnets.mx.mapping.RoutingInstruction;
import com.eastnets.mx.mapping.SAAInfo;
import com.eastnets.mx.mapping.SWIFTNetNetworkInfo;
import com.eastnets.mx.mapping.SWIFTNetSecurityInfo;
import com.eastnets.mx.mapping.SecurityInfo;
import com.eastnets.mx.mapping.SubFormat;
import com.eastnets.mx.mapping.ValidationLevel;

/**
 * @author MKassab
 * 
 */
public abstract class MessageMapper {

	protected static Message getMessageNode(DataPDU pdu) {
		Message mxMessageNode = null;
		if (pdu.getHeader().getTransmissionReport() != null) {
			mxMessageNode = pdu.getHeader().getTransmissionReport().getMessage();
		} else if (pdu.getHeader().getMessage() != null) {
			mxMessageNode = pdu.getHeader().getMessage();
		}

		return mxMessageNode;
	}

	@SuppressWarnings("null")
	protected static Message getMessageNode(AMP amp) {
		Header messageHeader = null;
		InterAct interAct = null;
		String receiverAddress = "XXXXXXXX";
		String senderAddress = "XXXXXXXX";
		if (amp.getHeader() != null) {
			messageHeader = amp.getHeader();
		}
		if (amp.getProtocolParameters() != null && amp.getProtocolParameters().getInterAct() != null) {
			interAct = amp.getProtocolParameters().getInterAct();
		}

		Message mxMessageNode = new Message();
		if (messageHeader != null) {

			mxMessageNode.setSenderReference(messageHeader.getMessageReference());
			if (messageHeader.getDirection() != null && !messageHeader.getDirection().isEmpty()) {
				mxMessageNode.setSubFormat(SubFormat.fromValue((messageHeader.getDirection().equals("DISTRIBUTION")) ? SubFormat.OUTPUT.value() : SubFormat.INPUT.value()));

			} else {

				mxMessageNode.setSubFormat(SubFormat.fromValue(SubFormat.INPUT.value()));
			}

			mxMessageNode.setFileLogicalName("");
			mxMessageNode.setFormat(messageHeader.getMessageFormat());
			mxMessageNode.setMessageIdentifier(messageHeader.getMessageFormat());
			if (messageHeader.getReceiverAddress() != null) {
				receiverAddress = messageHeader.getReceiverAddress();
			}
			if (messageHeader.getSenderAddress() != null) {
				senderAddress = messageHeader.getSenderAddress();
			}

		} else {

			mxMessageNode.setSenderReference("");
			mxMessageNode.setSubFormat(SubFormat.fromValue(SubFormat.INPUT.value()));
			mxMessageNode.setFileLogicalName("");
			mxMessageNode.setFormat("XXXXX");
			mxMessageNode.setMessageIdentifier("XXXXXXXXXX");

		}

		try {
			if (receiverAddress.contains("=") && receiverAddress.contains(",")) {
				String[] RESPONDERDN = receiverAddress.split(",");
				String reciverBIC = "";
				for (String res : RESPONDERDN) {
					if (res.contains("o=")) {
						reciverBIC = res;
						break;
					}
				}
				reciverBIC = reciverBIC.substring(reciverBIC.indexOf('=') + 1);
				AddressInfo reciver = new AddressInfo();
				reciver.setDN(receiverAddress);
				AddressFullName addressFullName = new AddressFullName();
				addressFullName.setX1(reciverBIC + "XXX");
				reciver.setFullName(addressFullName);
				reciver.setNickname("");
				mxMessageNode.setReceiver(reciver);
			} else {
				AddressInfo reciver = new AddressInfo();
				if (interAct != null) {
					if (interAct.getSwiftNetRoute() != null) {
						reciver.setDN(interAct.getSwiftNetRoute().getResponder());
					}
				}
				AddressFullName addressFullName = new AddressFullName();
				if (receiverAddress.length() > 8) {
					addressFullName.setX1(receiverAddress);
				} else {

					addressFullName.setX1(receiverAddress + "XXX");
				}

				reciver.setFullName(addressFullName);
				reciver.setNickname("");
				mxMessageNode.setReceiver(reciver);

			}

		} catch (Exception e) {
			AddressInfo reciver = new AddressInfo();
			AddressFullName addressFullName = new AddressFullName();
			if (receiverAddress.length() > 8) {
				addressFullName.setX1(receiverAddress);
			} else {

				addressFullName.setX1("XXXXXXXX" + "XXX");
			}

			reciver.setFullName(addressFullName);
			reciver.setNickname("");
			mxMessageNode.setReceiver(reciver);
		}

		try {
			if (senderAddress.contains("=") && senderAddress.contains(",")) {
				String[] REQUESTORDN = senderAddress.split(",");
				String senderBic = "";
				for (String req : REQUESTORDN) {
					if (req.contains("o=")) {
						senderBic = req;
						break;
					}
				}

				senderBic = senderBic.substring(senderBic.indexOf('=') + 1);

				AddressInfo sender = new AddressInfo();
				sender.setDN(senderAddress);
				AddressFullName addressFullNameForSender = new AddressFullName();
				addressFullNameForSender.setX1(senderBic + "XXX");
				sender.setFullName(addressFullNameForSender);
				sender.setNickname("");

				mxMessageNode.setSender(sender);

			} else {

				AddressInfo sender = new AddressInfo();
				if (interAct != null) {
					if (interAct.getSwiftNetRoute() != null) {
						sender.setDN(interAct.getSwiftNetRoute().getRequestor());
					}
				}
				AddressFullName addressFullNameForSender = new AddressFullName();
				if (senderAddress.length() > 8) {

					addressFullNameForSender.setX1(senderAddress);
				} else {

					addressFullNameForSender.setX1(senderAddress + "XXX");
				}
				sender.setFullName(addressFullNameForSender);
				sender.setNickname("");

				mxMessageNode.setSender(sender);

			}

		} catch (Exception e) {
			AddressInfo sender = new AddressInfo();

			AddressFullName addressFullNameForSender = new AddressFullName();
			if (senderAddress.length() > 8) {

				addressFullNameForSender.setX1(senderAddress);
			} else {

				addressFullNameForSender.setX1("XXXXXXXX" + "XXX");
			}
			sender.setFullName(addressFullNameForSender);
			sender.setNickname("");

			mxMessageNode.setSender(sender);
		}
		// InterfaceInfo
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setIsModificationAllowed(true);
		interfaceInfo.setMessageContext(MessageContext.fromValue("Original"));
		interfaceInfo.setMessageCreator(MessageCreator.fromValue("SWIFTNetInterface"));
		interfaceInfo.setMessageNature(MessageNature.fromValue("Financial"));
		interfaceInfo.setRoutingCode("");
		RoutingInstruction routingInstruction = new RoutingInstruction();
		routingInstruction.setRoutingPoint("");
		interfaceInfo.setRoutingInstruction(routingInstruction);
		if (interAct != null) {
			interfaceInfo.setUserReference(interAct.getSwiftNet().getUserReference());

		}
		interfaceInfo.setValidationLevel(ValidationLevel.fromValue("None"));

		mxMessageNode.setInterfaceInfo(interfaceInfo);

		NetworkInfo networkInfo = new NetworkInfo();
		try {
			if (messageHeader != null && messageHeader.getNetworkPriority() != null) {
				networkInfo.setPriority(Priority.fromValue(messageHeader.getNetworkPriority()));
			} else {

				networkInfo.setPriority(Priority.fromValue("Normal"));
			}
		} catch (Exception e) {
			networkInfo.setPriority(Priority.fromValue("Normal"));

		}

		networkInfo.setDuplicateHistory(null);
		FINNetworkInfo finNetworkInfo = new FINNetworkInfo();
		finNetworkInfo.setCopyService("");
		finNetworkInfo.setCorrespondentInputReference("");
		finNetworkInfo.setCorrespondentInputTime("");
		finNetworkInfo.setDelayedMessage("");
		finNetworkInfo.setIsRetrieved(null);
		finNetworkInfo.setLocalOutputTime(null);
		finNetworkInfo.setMessageSyntaxVersion("");
		finNetworkInfo.setReleaseInfo("");
		finNetworkInfo.setSystemOriginated("");
		finNetworkInfo.setSystemOriginated("");
		finNetworkInfo.setUserPriority("");
		finNetworkInfo.setValidationIdentifier("");

		networkInfo.setFINNetworkInfo(finNetworkInfo);
		if (interAct != null) {
			if (interAct.getSwiftNet() != null) {
				String delNotRequest = interAct.getSwiftNet().getDelNotRequest();
				networkInfo.setIsNotificationRequested((delNotRequest.equalsIgnoreCase("FALSE") ? false : true));
			}
		} else {
			networkInfo.setIsNotificationRequested(false);
		}

		networkInfo.setIsPossibleDuplicate(false);
		networkInfo.setNetwork(Network.fromValue("SWIFTNet"));
		if (interAct != null) {
			networkInfo.setService(interAct.getSwiftNetRoute().getService());
		}

		SWIFTNetNetworkInfo swiftNetNetworkInfo = new SWIFTNetNetworkInfo();
		swiftNetNetworkInfo.setCreationTime("");
		swiftNetNetworkInfo.setFileDescription("");
		swiftNetNetworkInfo.setFileEndTime("");
		swiftNetNetworkInfo.setFileInfo("");
		swiftNetNetworkInfo.setFileStartTime("");
		swiftNetNetworkInfo.setIsAuthNotificationRequested(true);
		if (interAct != null) {
			swiftNetNetworkInfo.setIsCopyRequested(Boolean.valueOf(interAct.getSwiftNet().getCopyIndicator()));
			swiftNetNetworkInfo.setResponseResponderDN(interAct.getSwiftNetRoute().getResponder());
			swiftNetNetworkInfo.setRequestType(interAct.getSwiftNetRoute().getRequestType());
		} else {
			swiftNetNetworkInfo.setIsCopyRequested(false);
		}

		swiftNetNetworkInfo.setIsPossibleDuplicateResponse(false);
		swiftNetNetworkInfo.setNotificationRequestType("");
		swiftNetNetworkInfo.setNotificationResponderDN("");
		swiftNetNetworkInfo.setOrigSnfRef("");
		swiftNetNetworkInfo.setOverdueWarningDelay(0);
		swiftNetNetworkInfo.setOverdueWarningTime("");
		swiftNetNetworkInfo.setReference("");

		swiftNetNetworkInfo.setSWIFTRef("");
		swiftNetNetworkInfo.setSNLRef("");
		swiftNetNetworkInfo.setSNLEndPoint("");
		swiftNetNetworkInfo.setSnFQueueName("");
		swiftNetNetworkInfo.setSnFInputTime("");
		swiftNetNetworkInfo.setResponseSWIFTRef("");
		swiftNetNetworkInfo.setResponseSNLRef("");
		swiftNetNetworkInfo.setResponseReference("");

		networkInfo.setSWIFTNetNetworkInfo(swiftNetNetworkInfo);

		mxMessageNode.setNetworkInfo(networkInfo);

		SAAInfo ssInfo = new SAAInfo();
		mxMessageNode.setSAAInfo(ssInfo);
		SecurityInfo securityInfo = new SecurityInfo();

		securityInfo.setFINSecurityInfo(null);
		securityInfo.setIsSigningRequested(null);
		securityInfo.setRMAResult(null);

		SWIFTNetSecurityInfo swiftNetSecurityInfo = new SWIFTNetSecurityInfo();
		if (interAct != null) {
			if (interAct.getSwiftNet() != null) {
				try {
					String nrIndicator = amp.getProtocolParameters().getInterAct().getSwiftNet().getNRIndicator();
					swiftNetSecurityInfo.setIsNRRequested((nrIndicator.equalsIgnoreCase("FALSE") ? false : true));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} else {
			swiftNetSecurityInfo.setIsNRRequested(false);

		}
		if (amp.getData() != null) {
			try {
				if (amp.getData().getDigestAlgorithm() != null) {
					if (!amp.getData().getDigestAlgorithm().isEmpty()) {
						swiftNetSecurityInfo.setFileDigestAlgorithm(FileDigestAlgorithm.fromValue(amp.getData().getDigestAlgorithm()));
					} else {
						swiftNetSecurityInfo.setFileDigestAlgorithm(FileDigestAlgorithm.fromValue("SHA-256"));

					}
				} else {
					swiftNetSecurityInfo.setFileDigestAlgorithm(FileDigestAlgorithm.fromValue("SHA-256"));

				}
				swiftNetSecurityInfo.setFileDigestValue(amp.getData().getDigest());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		securityInfo.setSWIFTNetSecurityInfo(swiftNetSecurityInfo);
		mxMessageNode.setSecurityInfo(securityInfo);
		return mxMessageNode;
	}

}
