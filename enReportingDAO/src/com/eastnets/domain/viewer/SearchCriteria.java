package com.eastnets.domain.viewer;

import java.io.Serializable;

public class SearchCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513137400405235127L;
	public final static String SAA_SOURCE = "sourceSelectedSAA";
	public final static String CREATION_DATE = "creationDate";
	public final static String timeFrom = "timeFrom";
	public final static String timeTo = "timeTo";
	public final static String UMID_FORMAT = "umidFormat";
	public final static String UMID_IO = "umidIO";
	public final static String UMID_CORRESPONDENT = "umidCorrespondent";
	public final static String UMID_TYPE = "umidType";
	public final static String UMID_QUAL = "umidQual";
	public final static String UMID_REFERENCE = "umidReference";
	public final static String SERVICE_NAME = "serviceName";
	public final static String SERVICE_NAME_EXT = "serviceNameExt";
	public final static String IDENTIFIER = "identifier";
	public final static String CONTENT_SENDER = "contentSender";
	public final static String CONTENT_SENDER_INSTITUTION = "contentSenderInstitution";
	public final static String CONTENT_SENDER_DEPARTMENT = "contentSenderDepartment";
	public final static String CONTENT_SENDER_LASTNAME = "contentSenderLastName";
	public final static String CONTENT_SENDER_FIRSTNAME = "contentSenderFirstName";
	public final static String CONTENT_RECEIVER = "contentReceiver";
	public final static String CONTENT_RECEIVER_INSTITUTION = "contentReceiverInstitution";
	public final static String CONTENT_RECEIVER_DEPARTMENT = "contentReceiverDepartment";
	public final static String CONTENT_RECEIVER_LASTNAME = "contentReceiverLastName";
	public final static String CONTENT_RECEIVER_FIRSTNAME = "contentReceiverFirstName";
	public final static String CONTENT_NATURE = "contentNature";
	public final static String CONTENT_TRANSACTION_REFERENCE = "contentTransactionReference";
	public final static String CONTENT_RELATED_REFERENCE = "contentRelatedRefference";
	public final static String CONTENT_AMOUNT_FROM = "contentAmountFrom";
	public final static String CONTENT_AMOUNT_TO = "contentAmountTo";
	public final static String CONTENT_AMOUNT_CURRENCY = "contentAmountCurrency";
	public final static String CONTENT_VALUE_DATE_FROM = "contentValueDateFrom";
	public final static String CONTENT_VALUE_DATE_TO = "contentValueDateTo";
	public final static String CONTENT_SEARCH_TEXT = "contentSearchText";
	public final static String INTERVENTIONS_NETWORK_NAME = "interventionsNetworkName";
	public final static String INTERVENTIONS_FROM_TO_NETWORK = "interventionsFromToNetwork";
	public final static String INTERVENTIONS_SESSION_HOLDER = "interventionsSessionHolder";
	public final static String INTERVENTIONS_SESSION_NUMBER = "interventionsSessionNumber";
	public final static String INTERVENTIONS_SEQUENCE_NUMBER_FROM = "interventionsSequenceNumberFrom";
	public final static String INTERVENTIONS_SEQUENCE_NUMBER_TO = "interventionsSequenceNumberTo";
	public final static String INTERVENTIONS_NETWORK_STATUS = "emiNetworkDeliveryStatus";
	public final static String INSTANCE_STATUS = "instanceStatus";
	public final static String QUEUES_SELECTED = "queuesSelected";
	public final static String UNITS_SELECTED = "unitsSelected";
	public final static String FIELD_SEARCH = "fieldSearchItemList";
	public final static String TIME_ZONE = "timeZoneOffset";
	public final static String INTERVENTIONS_HISTORY_TEXT = "historyText";
	// support old date format
	public final static String CREATION_FROM_DATE = "creationFromDate";
	public final static String CREATION_TO_DATE = "creationToDate";

	public final static String SEARCH_IN = "searchIn";
	public final static String BANKING_PRIORITY = "bankingPriority";
	public final static String FIN_COPY = "finCopy";
	public final static String FIN_INFORM = "finInform";
	public final static String MUR = "mur";
	public final static String UETR = "uetr";
	public final static String SLA_ID = "SLA_ID";
	public final static String REQUESTOR_DN = "requestorDN";
	public final static String RESPONDER_DN = "responderDN";
	public final static String LOGICAL_FILE_NAME = "logicalFileName";
	public final static String TRANSFER_DESCRIPTION = "transferDescription";
	public final static String SELECTED_XPATH_EXPRESSIONS = "xpathXpression";
	public final static String MX_KEYWORD_1 = "mxKeyword1";
	public final static String MX_KEYWORD_2 = "mxKeyword2";
	public final static String MX_KEYWORD_3 = "mxKeyword3";
	public final static String MULTIPLE_COUNTRY = "sourceSelectedCountry";
	public final static String MULTIPLE_CURRENCY = "sourceSelectedCurancy";
	public final static String MULTIPLE_MT = "sourceSelectedMT";
	public final static String copy = "copy";
	public final static String INCLUDE_SYS_MSG = "includeSyeMesg";
	public final static String SEARCH_TEXT_CASE_SENSITIVE = "CaseSensitive";

	public final static String Account_With_INS = "accountWithInstitution";
	public final static String ORDERING_INS = "orderingInstitution";
	public final static String ORDERING_Cust = "orderingCustomer";
	public final static String BENFICIARY_CUST = "beneficiaryCustomer";
	public final static String DETAILS_OF_CHARGE = "detailOfCharge";
	public final static String DEDUCT_FROM = "deductsFrom";
	public final static String DEDUCT_TO = "deductsTo";
	public final static String RESAON_CODE = "reasonCodes";
	public final static String STATUS_CODE = "statusCode";
	public final static String STATUS_ORG_BIC = "statusOriginatorBic";
	public final static String FORWORD_BIC = "forwordBic";
	public final static String SENDER_CORR = "senderCorr";
	public final static String RECIVER_CORR = "receiverCorr";
	public final static String REIMBURSEMENT_INST = "reimbursementInst";
	public final static String INS_AMOUNT_FROM = "insAmountFrom";
	public final static String INS_AMOUNT_TO = "insAmountTo";
	public final static String GPI_CUR = "gpiCur";
	public final static String ENABEL_GPI = "enabelGpi";
	public final static String TRANSACTION_STATUS = "transactionStatus";
	public final static String GSRP_REASON = "gSRPReasonCode";
	public final static String ENABLE_GPI = "enableGpi";
	// new confirmation formats
	public final static String SETTLMENT_METHOD = "sattlmentMethod";
	public final static String CLERING_SYSTEM = "clearingSystemList";
	public final static String SERVICE_TYPE = "serviceType";
}
