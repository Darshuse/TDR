package com.eastnets.service.loader.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eastnets.beans.AMPData.AMP;
import com.eastnets.domain.loader.Appe;
import com.eastnets.domain.loader.AppePK;
import com.eastnets.domain.loader.LoaderMessage;
import com.eastnets.mx.mapping.DataPDU;
import com.eastnets.mx.mapping.Intervention;
import com.eastnets.mx.mapping.Message;
import com.eastnets.mx.mapping.NRType;
import com.eastnets.mx.mapping.NetworkDeliveryStatus;
import com.eastnets.mx.mapping.RMAResult;
import com.eastnets.mx.mapping.ReceiverDeliveryStatus;
import com.eastnets.mx.mapping.SubFormat;
import com.eastnets.mx.mapping.SwAny;
import com.eastnets.resilience.mtutil.XMLUitl;

/**
 * @author MKassab
 * 
 */
public class AppendixMapper extends MessageMapper {

	private static final Logger LOGGER = Logger.getLogger(AppendixMapper.class);

	public static Appe mappAppe(DataPDU xmlRoot, LoaderMessage loaderMessage) {
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Start parsing appendix for message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {

			LOGGER.debug("Start parsing appendix for message :: " + loaderMessage.getMessageSequenceNo());
		}

		Appe appe;
		appe = new Appe();
		Message message = getMessageNode(xmlRoot);
		AppePK pK = new AppePK();
		pK.setAppeInstNum(0);

		if (message.getNetworkInfo() != null) {

			if (message.getNetworkInfo().getSessionNr() == null || message.getNetworkInfo().getSeqNr() == null || message.getNetworkInfo().getNetwork() == null) {
				LOGGER.debug("This message doesn't have appendix :: " + message.getMessageIdentifier());
				/**
				 * if network info is null then there is no appendix
				 */
				return null;
			}

			Integer seqNr = message.getNetworkInfo().getSeqNr();

			if (seqNr == null || seqNr.equals(0)) {
				pK.setAppeSeqNbr(0);
				appe.setAppeSequenceNbr(new BigDecimal(0));
				appe.setAppeTransmissionNbr(new BigDecimal(0));
			} else {
				pK.setAppeSeqNbr(seqNr);
				appe.setAppeSequenceNbr(new BigDecimal(seqNr));
				appe.setAppeTransmissionNbr(new BigDecimal(seqNr));
			}

			if (message.getNetworkInfo().getNetwork().value() != null) {
				appe.setAppeIappName(message.getNetworkInfo().getNetwork().value());
			} else {
				appe.setAppeIappName(" ");
			}
			appe.setAppeSessionNbr(new BigDecimal(message.getNetworkInfo().getSessionNr()));
			if (message.getNetworkInfo().getSWIFTNetNetworkInfo() != null) {
				appe.setAppeSwiftRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSWIFTRef());
				appe.setAppeSwiftRequestRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSNLRef());
				appe.setAppeSnlEndpoint(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSNLEndPoint());
				appe.setAppeSnfQueueName(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSnFQueueName());
				appe.setAppeSnfInputTime(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSnFInputTime());
				appe.setAppeSwiftResponseRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseSWIFTRef());
				appe.setAppeResponseRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseSNLRef());
				appe.setAppeRespCbtReference(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseReference());
				appe.setAppeRespPossibleDupCrea("FALSE");
				appe.setAppeRespResponderDn(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseResponderDN());

				String snFQueueName = message.getNetworkInfo().getSWIFTNetNetworkInfo().getSnFQueueName();
				if (snFQueueName != null && !snFQueueName.isEmpty()) {
					appe.setAppeSessionHolder(snFQueueName);
				} else {
					appe.setAppeSessionHolder(message.getNetworkInfo().getNetwork().value());
				}
			}

			if (message.getSecurityInfo() != null) {
				if (message.getSecurityInfo().getSWIFTNetSecurityInfo() != null) {
					if (message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested() != null) {
						boolean isNRRequested = message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested();
						appe.setAppeNrIndicator(new BigDecimal(isNRRequested ? 1 : 0));
					}
					appe.setAppeNonrepWarning(message.getSecurityInfo().getSWIFTNetSecurityInfo().getNRWarning());
					appe.setAppeSignerDn(message.getSecurityInfo().getSWIFTNetSecurityInfo().getSignerDN());
					appe.setAppeRespNonrepWarning(message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRWarning());

					if (message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRType() != null) {
						String value = "NR_TYPE_";
						NRType responseNRType = message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRType();
						switch (responseNRType) {
						case SVC_MAND:
							value = value + "MANDATORY";
							break;
						case SVC_OPT:
							value = value + "OPTIONAL";
							break;
						default:
							value = null;
							break;
						}
						appe.setAppeNonrepType(value);
					}

				}
			}

			if (message.getNetworkInfo().getFINNetworkInfo() != null) {
				if (message.getNetworkInfo().getFINNetworkInfo().getLocalOutputTime() != null) {
					try {
						appe.setAppeLocalOutputTime(new SimpleDateFormat("dd/MM/yyyy").parse(message.getNetworkInfo().getFINNetworkInfo().getLocalOutputTime()));
					} catch (ParseException e) {
						e.printStackTrace();
						LOGGER.error(e);
					}
				}
			}

		} else {
			LOGGER.debug("This message doesn't have appendix :: " + message.getMessageIdentifier());
			/**
			 * if network info is null then there is no appendix
			 */
			return null;
		}

		appe.setXAppeLast(new BigDecimal(1));
		appe.setAppeToken(new BigDecimal(0));

		if (xmlRoot.getHeader() != null) {

			if (xmlRoot.getHeader().getTransmissionReport() != null && xmlRoot.getHeader().getTransmissionReport().getInterventions() != null && xmlRoot.getHeader().getTransmissionReport().getInterventions().getIntervention() != null
					&& !xmlRoot.getHeader().getTransmissionReport().getInterventions().getIntervention().isEmpty()) {

				List<Intervention> intervention = xmlRoot.getHeader().getTransmissionReport().getInterventions().getIntervention();
				String interValue = intervention.get(intervention.size() - 1).getCreationTime();

				try {
					pK.setAppeDateTime(new SimpleDateFormat("yyyyMMddHHmmSS").parse(interValue));
				} catch (ParseException e) {
					LOGGER.error(e);
					LOGGER.error("Error During parsing date :: appe date time :: " + interValue);
				}

				Intervention intervention2 = intervention.get(intervention.size() - 1);
				if (intervention2.getContents() == null) {
					SwAny content = intervention2.getContents();
					List<Object> contents = content.getContent();
					StringBuilder data = new StringBuilder();
					for (Object object : contents) {
						if (object instanceof NodeList) {
							try {
								data.append(XMLUitl.getNodeAsXML((Node) object));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					appe.setAppeAckNackText(data.toString());
				}
			} else if (xmlRoot.getHeader().getDeliveryReport() != null) {

				if (xmlRoot.getHeader().getDeliveryReport().getInterventions() != null && xmlRoot.getHeader().getDeliveryReport().getInterventions().getIntervention() != null
						&& !xmlRoot.getHeader().getDeliveryReport().getInterventions().getIntervention().isEmpty()) {
					List<Intervention> deliveryintervention = xmlRoot.getHeader().getDeliveryReport().getInterventions().getIntervention();
					List<Object> contents = deliveryintervention.get(deliveryintervention.size() - 1).getContents().getContent();
					StringBuilder data = new StringBuilder();
					for (Object object : contents) {
						if (object instanceof NodeList) {
							try {
								data.append(XMLUitl.getNodeAsXML((Node) object));
							} catch (Exception exp) {
								exp.printStackTrace();
							}
						}
					}
					appe.setAppeAckNackText(data.toString());

					if (deliveryintervention.size() > 0) {
						Intervention intv = deliveryintervention.get(0);
						try {
							pK.setAppeDateTime(new SimpleDateFormat("yyyyMMddHHmmSS").parse(intv.getCreationTime()));
						} catch (ParseException e) {
							LOGGER.error(e);
							LOGGER.error("Error During parsing date :: appe date time :: " + intv.getCreationTime());
						}

					}

				}

				if (xmlRoot.getHeader().getDeliveryReport().getReceiverDeliveryStatus() != null) {
					String valString = "EM_";
					ReceiverDeliveryStatus receiverDeliveryStatus = xmlRoot.getHeader().getDeliveryReport().getReceiverDeliveryStatus();
					if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_DELIVERED) {
						valString = valString + "DELIVERED";
					} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_ABORTED) {
						valString = valString + "ABORTED";
					} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_DELAYED_NAK) {
						valString = valString + "DELAYED_NAK";
					} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_FCP_RELEASED) {
						valString = valString + "FCP_RELEASED";
					} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_OVERDUE) {
						valString = valString + "OVERDUE";
					} else {
						valString = null;
					}
					appe.setAppeRcvDeliveryStatus(valString);
				}

			} else {

				// in case if no interventions

				if (loaderMessage.getMesgProperties() != null && loaderMessage.getMesgProperties().get("creationdate") != null) {
					Date date = (Date) loaderMessage.getMesgProperties().get("creationdate");
					pK.setAppeDateTime(date);
				} else {
					pK.setAppeDateTime(new Date());
				}

			}

			if (xmlRoot.getHeader().getTransmissionReport() != null && xmlRoot.getHeader().getTransmissionReport().getNetworkDeliveryStatus() != null) {
				String concatString = "DLV_";
				NetworkDeliveryStatus networkDeliveryStatus = xmlRoot.getHeader().getTransmissionReport().getNetworkDeliveryStatus();
				if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_ACKED) {
					concatString = concatString + "ACKED";
				} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_NACKED) {
					concatString = concatString + "NACKED";
				} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_REJECTED_LOCALLY) {
					concatString = concatString + "REJECTED_LOCALLY";
				} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_ABORTED) {
					concatString = concatString + "ABORTED";
				} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_TIMED_OUT) {
					concatString = concatString + "TIMED_OUT";
				} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_WAITING_ACK) {
					concatString = concatString + "WAITING_ACK";
				} else {
					concatString = null;
				}
				appe.setAppeNetworkDeliveryStatus(concatString);
			}

		}

		if (xmlRoot.getHeader() != null && xmlRoot.getHeader().getTransmissionReport() != null && xmlRoot.getHeader().getTransmissionReport().getReportingApplication() != null) {
			String reporting = xmlRoot.getHeader().getTransmissionReport().getReportingApplication().value();
			appe.setAppeCreaApplServName(reporting);
		}

		if (message.getSubFormat() == SubFormat.INPUT) {
			appe.setAppeType("APPE_EMISSION");
			appe.setAppeCreaMpfnName("_SI_to_SWIFTNet");

		} else {
			appe.setAppeType("APPE_RECEPTION");
			appe.setAppeCreaMpfnName("_SI_from_SWIFTNet");
		}
		// same as appe creation mpfn name
		appe.setAppeCreaRpName(appe.getAppeCreaMpfnName());

		if (message.getSecurityInfo() != null) {
			if (message.getSecurityInfo().getFINSecurityInfo() != null) {
				if (message.getSecurityInfo().getFINSecurityInfo() != null) {
					if (message.getSecurityInfo().getFINSecurityInfo().getChecksumResult() != null) {
						appe.setAppeChecksumResult("CHCK_" + message.getSecurityInfo().getFINSecurityInfo().getChecksumResult().value().toUpperCase());
					}
					appe.setAppePacResult(message.getSecurityInfo().getFINSecurityInfo().getPACValue());
					appe.setAppePacValue(message.getSecurityInfo().getFINSecurityInfo().getPACValue());
				}
				appe.setAppeChecksumValue("CHCK_" + message.getSecurityInfo().getFINSecurityInfo().getChecksumValue());

			}

			if (message.getSecurityInfo().getRMAResult() != null) {
				RMAResult rmaResult = message.getSecurityInfo().getRMAResult();
				String endValue = "RMA_CHECK_";
				switch (rmaResult) {
				case SUCCESS:
					endValue = endValue + "SUCCESS";
					break;
				case BYPASSED:
					endValue = endValue + "BYPASSED";
					break;
				case NO_RECORD:
					endValue = endValue + "NO_RECORD";
					break;
				case NOT_ENABLED:
					endValue = endValue + "NOT_ENABLED";
					break;
				case INVALID_PERIOD:
					endValue = endValue + "INVALID_PERIOD";
					break;
				case UNAUTHORISED:
					endValue = endValue + "UNAUTHORISED";
					break;

				default:
					endValue = null;
					break;
				}
				appe.setAppeRmaCheckResult(endValue);
			}
		}

		if (appe.getAppeAckNackText() != null) {
			String appeAckNackText = appe.getAppeAckNackText();
			int indexOf = appeAckNackText.indexOf("405:");
			if (indexOf != -1) {
				appe.setAppeNakReason(appeAckNackText.substring(indexOf + 4));
			}

		}
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Appendix for message :: " + loaderMessage.getMesg().getMesgUumid() + " :: parsed");
			LOGGER.debug("Appendix details for message :: " + loaderMessage.getMesg().getMesgUumid() + " :: " + appe.toString());

		} else {
			LOGGER.debug("Appendix for message :: " + loaderMessage.getMessageSequenceNo() + " :: parsed");
			LOGGER.debug("Appendix details for message :: " + loaderMessage.getMessageSequenceNo() + " :: " + appe.toString());

		}

		appe.setId(pK);
		return appe;
	}

	public static Appe mappAppe(AMP amp, LoaderMessage loaderMessage) {
		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Start parsing appendix for message :: " + loaderMessage.getMesg().getMesgUumid());
		} else {

			LOGGER.debug("Start parsing appendix for message :: " + loaderMessage.getMessageSequenceNo());
		}

		Appe appe;
		appe = new Appe();
		Message message = getMessageNode(amp);
		AppePK pK = new AppePK();
		pK.setAppeInstNum(0);

		if (message.getNetworkInfo() != null) {

			Integer seqNr = message.getNetworkInfo().getSeqNr();

			if (seqNr == null || seqNr.equals(0)) {
				pK.setAppeSeqNbr(0);
				appe.setAppeSequenceNbr(new BigDecimal(0));
				appe.setAppeTransmissionNbr(new BigDecimal(0));
			} else {
				pK.setAppeSeqNbr(seqNr);
				appe.setAppeSequenceNbr(new BigDecimal(seqNr));
				appe.setAppeTransmissionNbr(new BigDecimal(seqNr));
			}

			if (message.getNetworkInfo().getNetwork().value() != null) {
				appe.setAppeIappName(message.getNetworkInfo().getNetwork().value());
			} else {
				appe.setAppeIappName(" ");
			}

			if (message.getNetworkInfo().getSWIFTNetNetworkInfo() != null) {
				appe.setAppeSwiftRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSWIFTRef());
				appe.setAppeSwiftRequestRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSNLRef());
				appe.setAppeSnlEndpoint(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSNLEndPoint());
				appe.setAppeSnfQueueName(message.getNetworkInfo().getSWIFTNetNetworkInfo().getSnFQueueName());
				appe.setAppeSwiftResponseRef(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseSWIFTRef());
				appe.setAppeRespResponderDn(message.getNetworkInfo().getSWIFTNetNetworkInfo().getResponseResponderDN());
			}

			if (message.getSecurityInfo() != null) {
				if (message.getSecurityInfo().getSWIFTNetSecurityInfo() != null) {
					if (message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested() != null) {
						boolean isNRRequested = message.getSecurityInfo().getSWIFTNetSecurityInfo().isIsNRRequested();
						appe.setAppeNrIndicator(new BigDecimal(isNRRequested ? 1 : 0));
					}
					appe.setAppeNonrepWarning(message.getSecurityInfo().getSWIFTNetSecurityInfo().getNRWarning());
					appe.setAppeSignerDn(message.getSecurityInfo().getSWIFTNetSecurityInfo().getSignerDN());
					appe.setAppeRespNonrepWarning(message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRWarning());

					if (message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRType() != null) {
						String value = "NR_TYPE_";
						NRType responseNRType = message.getSecurityInfo().getSWIFTNetSecurityInfo().getResponseNRType();
						switch (responseNRType) {
						case SVC_MAND:
							value = value + "MANDATORY";
							break;
						case SVC_OPT:
							value = value + "OPTIONAL";
							break;
						default:
							value = null;
							break;
						}
						appe.setAppeNonrepType(value);
					}

				}
			}

			if (message.getNetworkInfo().getFINNetworkInfo() != null) {
				if (message.getNetworkInfo().getFINNetworkInfo().getLocalOutputTime() != null) {
					try {
						appe.setAppeLocalOutputTime(new SimpleDateFormat("dd/MM/yyyy").parse(message.getNetworkInfo().getFINNetworkInfo().getLocalOutputTime()));
					} catch (ParseException e) {
						e.printStackTrace();
						LOGGER.error(e);
					}
				}
			}

		} else {
			LOGGER.debug("This message doesn't have appendix :: " + message.getMessageIdentifier());
			/**
			 * if network info is null then there is no appendix
			 */
			return null;
		}

		appe.setXAppeLast(new BigDecimal(1));
		appe.setAppeToken(new BigDecimal(0));

		String valString = "EM_";
		ReceiverDeliveryStatus receiverDeliveryStatus = ReceiverDeliveryStatus.RCV_DELIVERED;
		if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_DELIVERED) {
			valString = valString + "DELIVERED";
		} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_ABORTED) {
			valString = valString + "ABORTED";
		} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_DELAYED_NAK) {
			valString = valString + "DELAYED_NAK";
		} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_FCP_RELEASED) {
			valString = valString + "FCP_RELEASED";
		} else if (receiverDeliveryStatus == ReceiverDeliveryStatus.RCV_OVERDUE) {
			valString = valString + "OVERDUE";
		} else {
			valString = null;
		}
		appe.setAppeRcvDeliveryStatus(valString);

		String concatString = "DLV_";
		NetworkDeliveryStatus networkDeliveryStatus = NetworkDeliveryStatus.NETWORK_ACKED;
		if (loaderMessage.isMessageIsCancelled()) {

			networkDeliveryStatus = NetworkDeliveryStatus.NETWORK_NACKED;
		} else {

			networkDeliveryStatus = NetworkDeliveryStatus.NETWORK_ACKED;
		}
		if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_ACKED) {
			concatString = concatString + "ACKED";
		} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_NACKED) {
			concatString = concatString + "NACKED";
		} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_REJECTED_LOCALLY) {
			concatString = concatString + "REJECTED_LOCALLY";
		} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_ABORTED) {
			concatString = concatString + "ABORTED";
		} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_TIMED_OUT) {
			concatString = concatString + "TIMED_OUT";
		} else if (networkDeliveryStatus == NetworkDeliveryStatus.NETWORK_WAITING_ACK) {
			concatString = concatString + "WAITING_ACK";
		} else {
			concatString = null;
		}
		appe.setAppeNetworkDeliveryStatus(concatString);

		appe.setAppeCreaApplServName("AMH-INTERFACE");

		// in case if no interventions

		Date appeDateTime = null;
		try {
			String appeDate = amp.getHeader().getDateCreated();
			appeDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(appeDate.replace("T", " "));
		} catch (Exception e) {
			appeDateTime = new Date();
		}

		if (appeDateTime == null) {

			appeDateTime = new Date();
		}

		pK.setAppeDateTime(appeDateTime);

		if (message.getSubFormat() == SubFormat.INPUT) {
			appe.setAppeType("APPE_EMISSION");
			appe.setAppeCreaMpfnName("_SI_to_SWIFTNet");

		} else {
			appe.setAppeType("APPE_RECEPTION");
			appe.setAppeCreaMpfnName("_SI_from_SWIFTNet");
		}

		try {
			if (appe.getAppeAckNackText() != null) {
				String appeAckNackText = appe.getAppeAckNackText();
				int indexOf = appeAckNackText.indexOf("405:");
				if (indexOf != -1) {
					appe.setAppeNakReason(appeAckNackText.substring(indexOf + 4));
				}

			}
		} catch (Exception e) {
			appe.setAppeNakReason("XXXX");
		}

		// NEW AMH
		// same as appe creation mpfn name
		appe.setAppeCreaRpName(appe.getAppeCreaMpfnName());
		appe.setAppeRmaCheckResult("RMA_CHECK_SUCCESS");
		appe.setAppeSessionNbr(new BigDecimal(0));
		appe.setAppeChecksumResult("CHCK_" + "SUCCESS");
		// appe.setAppeChecksumValue("CHCK_" + message.getSecurityInfo().getFINSecurityInfo().getChecksumValue());
		appe.setAppeRespPossibleDupCrea("FALSE");

		if (amp.getProtocolParameters() != null && amp.getProtocolParameters().getInterAct() != null) {
			if (amp.getProtocolParameters().getInterAct().getSwiftNet() != null) {
				String requireSignatureList = amp.getProtocolParameters().getInterAct().getSwiftNet().getRequireSignatureList();
				if (requireSignatureList != null) {

					appe.setAppeUsePkiSignature((requireSignatureList.equalsIgnoreCase("FALSE") ? new BigDecimal(0) : new BigDecimal(1)));
				} else {

					appe.setAppeUsePkiSignature(new BigDecimal(0));
				}

				String snFOutputSessionId = amp.getProtocolParameters().getInterAct().getSwiftNet().getSnFOutputSessionId();
				appe.setAppeSessionHolder(snFOutputSessionId);

				String snFInputTime = amp.getProtocolParameters().getInterAct().getSwiftNet().getSnFInputTime();
				appe.setAppeSnfInputTime(snFInputTime);
			} else {
				appe.setAppeUsePkiSignature(new BigDecimal(0));

			}
		}

		if (amp.getChannelParameters() != null) {
			String code = amp.getChannelParameters().getCode();
			appe.setAppeResponseRef(code);
			if (amp.getChannelParameters().getBackendChannel() != null) {
				String backendChannelCode = amp.getChannelParameters().getBackendChannel().getCode();
				appe.setAppeRespCbtReference(backendChannelCode);
			}

		}

		if (loaderMessage.getMessageSequenceNo() == null) {
			LOGGER.debug("Appendix for message :: " + loaderMessage.getMesg().getMesgUumid() + " :: parsed");
			LOGGER.debug("Appendix details for message :: " + loaderMessage.getMesg().getMesgUumid() + " :: " + appe.toString());

		} else {
			LOGGER.debug("Appendix for message :: " + loaderMessage.getMessageSequenceNo() + " :: parsed");
			LOGGER.debug("Appendix details for message :: " + loaderMessage.getMessageSequenceNo() + " :: " + appe.toString());

		}

		appe.setId(pK);
		return appe;
	}

}
