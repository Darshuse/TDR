package com.eastnets.resilience;

import java.util.HashMap;
import java.util.Map;

public class StringMapper {
	private static Map< String, String > visualStrings = new HashMap< String, String >();
		static {
		//message fields
		visualStrings.put("MESG_S_UMID",					"SUmid");
		visualStrings.put("MESG_VALIDATION_REQUESTED", 		"ValidationRequested");
		visualStrings.put("MESG_VALIDATION_PASSED", 		"ValidationPassed");
		visualStrings.put("MESG_CLASS", 					"Class");
		visualStrings.put("MESG_IS_TEXT_READONLY", 			"TextReadonly");
		visualStrings.put("MESG_IS_DELETE_INHIBITED", 		"DeleteInhibited");
		visualStrings.put("MESG_IS_TEXT_MODIFIED", 			"TextModified");
		visualStrings.put("MESG_IS_PARTIAL", 				"Partial");
		visualStrings.put("MESG_STATUS", 					"Status");
		visualStrings.put("MESG_CREA_APPL_SERV_NAME", 		"CreationApplication");
		visualStrings.put("MESG_CREA_MPFN_NAME", 			"CreationMpfnName");
		visualStrings.put("MESG_CREA_RP_NAME", 				"CreationRoutingPoint");
		visualStrings.put("MESG_CREA_OPER_NICKNAME", 		"CreationOperator");
		visualStrings.put("MESG_CREA_DATE_TIME", 			"CreationDate");
		visualStrings.put("MESG_MOD_OPER_NICKNAME", 		"ModificationOperator");
		visualStrings.put("MESG_MOD_DATE_TIME", 			"ModificationDate");
		visualStrings.put("MESG_CAS_TARGET_RP_NAME", 		"CasTargetRoutingPoint");
		visualStrings.put("MESG_RECOVERY_ACCEPT_INFO", 		"RecoveryAcceptInfo");
		visualStrings.put("MESG_UUMID", 					"Uumid");
		visualStrings.put("MESG_UUMID_SUFFIX", 				"UumidSuffix");
		visualStrings.put("MESG_SENDER_CORR_TYPE", 			"SenderCorrespondentType");
		visualStrings.put("MESG_SENDER_X1", 				"SenderCorrespondentInstitutionName");
		visualStrings.put("MESG_FRMT_NAME", 				"FormatName");
		visualStrings.put("MESG_SUB_FORMAT", 				"SubFormat");
		visualStrings.put("MESG_SYNTAX_TABLE_VER", 			"SyntaxTableVersion");
		visualStrings.put("MESG_NATURE", 					"Nature");
		visualStrings.put("MESG_NETWORK_APPL_IND", 			"NetworkApplicationIndication");
		visualStrings.put("MESG_TYPE", 						"Type");
		visualStrings.put("MESG_IS_LIVE", 					"Live");
		visualStrings.put("MESG_NETWORK_PRIORITY", 			"NetworkPriority");
		visualStrings.put("MESG_DELV_OVERDUE_WARN_REQ", 	"DeliveryOverdueWarningRequired");
		visualStrings.put("MESG_NETWORK_DELV_NOTIF_REQ",	"NetworkDeliveryNotificationRequired");
		visualStrings.put("MESG_USER_REFERENCE_TEXT", 		"UserReferenceText");
		visualStrings.put("MESG_FIN_VALUE_DATE", 			"FinValueDate");
		visualStrings.put("MESG_FIN_CCY_AMOUNT", 			"FinCurrencyAmount");
		visualStrings.put("MESG_TRN_REF", 					"TransactionReference");
		visualStrings.put("MESG_REL_TRN_REF", 				"RelatedTransactionReference");
		visualStrings.put("MESG_MESG_USER_GROUP", 			"MessageUserGroup");
		visualStrings.put("MESG_ZZ41_IS_POSSIBLE_DUP", 		"PossibleDuplicate");
		visualStrings.put("MESG_IDENTIFIER", 				"MessageIdentifier");
		visualStrings.put("MESG_SENDER_SWIFT_ADDRESS", 		"SenderSwiftAddress");
		visualStrings.put("MESG_RECEIVER_SWIFT_ADDRESS",	"ReceiverSwiftAddress");
		visualStrings.put("MESG_SERVICE", 					"Service");
		
		//instance fields
		visualStrings.put("INST_S_UMID",					"SUmid");
		visualStrings.put("INST_NUM",						"Number");
		visualStrings.put("INST_TYPE",						"Type");
		visualStrings.put("INST_NOTIFICATION_TYPE",			"NotificationType");
		visualStrings.put("INST_STATUS",					"Status");
		visualStrings.put("INST_RELATED_NBR",				"RelatedNumber");
		visualStrings.put("INST_APPE_DATE_TIME",			"AppendixDateTime");
		visualStrings.put("INST_APPE_SEQ_NBR",				"AppendixSequenceNumber");
		visualStrings.put("INST_UNIT_NAME",					"UnitName");
		visualStrings.put("INST_RP_NAME",					"RoutingPoint");
		visualStrings.put("INST_MPFN_NAME",					"MpfnName");
		visualStrings.put("INST_MPFN_HANDLE",				"MpfnHandle");
		visualStrings.put("INST_PROCESS_STATE",				"ProcessState");
		visualStrings.put("INST_LAST_MPFN_RESULT",			"LastMpfnResult");
		visualStrings.put("INST_RELATIVE_REF",				"RelativeReference");
		visualStrings.put("INST_SM2000_PRIORITY",			"Priority");
		visualStrings.put("INST_DEFERRED_TIME",				"DeferredTime");
		visualStrings.put("INST_CREA_APPL_SERV_NAME",		"CreationApplication");
		visualStrings.put("INST_CREA_MPFN_NAME",			"CreationMpfnName");
		visualStrings.put("INST_CREA_RP_NAME",				"CreationRoutingPoint");
		visualStrings.put("INST_CREA_DATE_TIME",			"CreationDate");
		visualStrings.put("INITIAL_TARGET_RP_NAME",			"InitialTargetRoutingPoint");
		visualStrings.put("INST_AUTH_OPER_NICKNAME",		"AuthoriserOperator");
		visualStrings.put("INST_LAST_OPER_NICKNAME",		"LastOperator");
		visualStrings.put("INST_CREA_DATE_TIMEInQueueSince","InQueueSince");
		visualStrings.put("INST_RECEIVER_CORR_TYPE",		"ReceiverCorrespondentType");
		visualStrings.put("INST_RECEIVER_X1",				"ReceiverCorrespondentInstitutionName");
		visualStrings.put("INST_RECEIVER_INSTITUTION_NAME",	"ReceiverInstitutionName");
		visualStrings.put("INST_RECEIVER_BRANCH_INFO",		"ReceiverBranchInfo");
		visualStrings.put("INST_RECEIVER_CITY_NAME",		"ReceiverCityName");
		visualStrings.put("INST_RECEIVER_CTRY_CODE",		"ReceiverCountryCode");
		visualStrings.put("INST_RECEIVER_NETWORK_IAPP_NAM",	"ReceiverNetworkIntegratedApplicationName");
		visualStrings.put("INST_RCV_DELIVERY_STATUS",		"ReceiverDeliveryStatus");
		visualStrings.put("INST_INTV_DATE_TIME",			"InterventionDateTime");
		visualStrings.put("INST_INTV_SEQ_NBR",				"InterventionSequenceNumber");
		
		
		//interventions
		visualStrings.put("INTV_S_UMID",			"SUmid");
		visualStrings.put("INTV_INST_NUM",			"InstanceNumber");
		visualStrings.put("INTV_DATE_TIME",			"DateTime");
		visualStrings.put("INTV_SEQ_NBR",			"InternalSequenceNumber");
		visualStrings.put("INTV_INTY_NUM",			"Number");
		visualStrings.put("INTV_INTY_NAME",			"Name");
		visualStrings.put("INTV_INTY_CATEGORY",		"Category");
		visualStrings.put("INTV_OPER_NICKNAME",		"Operator");
		visualStrings.put("INTV_APPL_SERV_NAME",	"Application");
		visualStrings.put("INTV_MPFN_NAME",			"MpfnName");
		visualStrings.put("INTV_APPE_SEQ_NBR",		"AppendixSequenceNumber");
		visualStrings.put("INTV_LENGTH",			"Length");
		visualStrings.put("INTV_MERGED_TEXT",		"Text");
		
		//appendix
		visualStrings.put("APPE_S_UMID",					"SUmid");
		visualStrings.put("APPE_INST_NUM",					"InstanceNumber");
		visualStrings.put("APPE_DATE_TIME",					"DateTime");
		visualStrings.put("APPE_SEQ_NBR",					"InternalSequenceNumber");
		visualStrings.put("APPE_IAPP_NAME",					"IntegratedApplication");
		visualStrings.put("APPE_TYPE",						"Type");
		visualStrings.put("APPE_SESSION_HOLDER",			"SessionHolder");
		visualStrings.put("APPE_SESSION_NBR",				"SessionNumber");
		visualStrings.put("APPE_SEQUENCE_NBR",				"SequenceNumber");
		visualStrings.put("APPE_TRANSMISSION_NBR",			"TransmissionNumber");
		visualStrings.put("APPE_CREA_APPL_SERV_NAME",		"CreatingApplication");
		visualStrings.put("APPE_CREA_MPFN_NAME",			"CreatingMpfn");
		visualStrings.put("APPE_CREA_RP_NAME",				"CreatingRoutingPoint");
		visualStrings.put("APPE_CHECKSUM_RESULT",			"ChecksumResult");
		visualStrings.put("APPE_CHECKSUM_VALUE",			"ChecksumValue");
		visualStrings.put("APPE_CONN_RESPONSE_CODE",		"ConnectionResponseCode");
		visualStrings.put("APPE_CONN_RESPONSE_TEXT",		"ConnectionResponseText");
		visualStrings.put("APPE_RCV_DELIVERY_STATUS",		"ReceiverDeliveryStatus");
		visualStrings.put("APPE_NETWORK_DELIVERY_STATUS",	"NetworkDeliveryStatus");
		visualStrings.put("APPE_ACK_NACK_TEXT",				"AckNackText");
		visualStrings.put("APPE_NAK_REASON",				"NackReason");
		visualStrings.put("APPE_REMOTE_INPUT_REFERENCE",	"RemoteInputReference");
		visualStrings.put("APPE_REMOTE_INPUT_TIME",			"RemoteInputTime");
		visualStrings.put("APPE_LOCAL_OUTPUT_TIME",			"LocalOutputTime");
		visualStrings.put("APPE_PKI_AUTH_VALUE",			"PkiAuthenticationValue");
		visualStrings.put("PKI_APPE_COMBINED_AUTH_RES",		"PkiCombinedAuthenticationResult");
		visualStrings.put("APPE_PKI_PAC2_RESULT",			"PkiProprietaryAuthenticationResult");
		visualStrings.put("APPE_PKI_AUTHORISATION_RES",		"PkiAuthorisationResult");
		visualStrings.put("APPE_PKI_AUTHENTICATION_RES",	"PkiAuthenticationResult");
		visualStrings.put("APPE_COMBINED_AUTH_RES",			"CombinedAuthenticationResult");
		visualStrings.put("APPE_COMBINED_PAC_RES",			"CombinedProprietaryAuthenticationResult");
		visualStrings.put("APPE_RMA_CHECK_RESULT",			"RmaCheckResult");
		visualStrings.put("APPE_SENDER_SWIFT_ADDRESS",		"SenderSwiftAddress");
		
		//text
		visualStrings.put("TEXT_S_UMID",					"SUmid");		
		visualStrings.put("TEXT_DATA_BLOCK",				"DataBlock");
		visualStrings.put("TEXT_SWIFT_BLOCK_3",				"SwiftBlock3");
		visualStrings.put("TEXT_SWIFT_BLOCK_5",				"SwiftBlock5");
		
	}

	public static String fieldLabel(String field) {
		String val= visualStrings.get(field);
		
		if ( val == null ){
			EnLogger.logW("Field \"" + field +"\" not found in appendix mapping." );
			val= field;
		}
		return val;
	}
	
	

}
