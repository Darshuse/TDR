package com.eastnets.message.summary.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.audit.dao.AuditDAO;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.configuration.GlobalConfiguration;

@Repository
public class MessageWriterDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	GlobalConfiguration globalConfiguration;

	@Autowired
	AuditDAO auditDAO;

	private static final Logger LOGGER = LogManager.getLogger(MessageWriterDAO.class);

	@Transactional
	public void insertMessages(List<MessageSummaryDTO> messageSummaryDTOs) {

		try {

			LOGGER.debug("Inserting: " + (messageSummaryDTOs.size()) + " Messages");
			String insertStatemnt = "INSERT INTO MESG_SUMMARY VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
						MessageSummaryDTO messageSummaryDTO = messageSummaryDTOs.get(index);
						preparedStatement.setLong(1, messageSummaryDTO.getAid());
						preparedStatement.setLong(2, messageSummaryDTO.getUmidl());
						preparedStatement.setLong(3, messageSummaryDTO.getUmidh());
						preparedStatement.setString(4, messageSummaryDTO.getMesgReference());
						preparedStatement.setString(5, messageSummaryDTO.getMesgRelatedReference());
						preparedStatement.setString(6, messageSummaryDTO.getMesgCreateMPFNName());
						preparedStatement.setString(7, messageSummaryDTO.getMesgCreateRPName());
						preparedStatement.setString(8, messageSummaryDTO.getMesgCreateOperatorNickName());
						preparedStatement.setDate(9, messageSummaryDTO.getMesgCreateDateTime());
						preparedStatement.setString(10, messageSummaryDTO.getModificationOperatorNickName());
						preparedStatement.setDate(11, messageSummaryDTO.getModificationDateTime());
						preparedStatement.setString(12, messageSummaryDTO.getMesgFormatName());
						preparedStatement.setString(13, messageSummaryDTO.getSenderBIC());
						preparedStatement.setString(14, messageSummaryDTO.getxOwnLT());
						preparedStatement.setString(15, messageSummaryDTO.getxInst0UnitName());
						preparedStatement.setString(16, messageSummaryDTO.getxCategory());
						preparedStatement.setLong(17, messageSummaryDTO.getArchived());
						preparedStatement.setString(18, messageSummaryDTO.getMesgStatus());
						preparedStatement.setString(19, messageSummaryDTO.getMesgNetworkPriority());
						preparedStatement.setString(20, messageSummaryDTO.getMesgPossibleDuplicateCreation());
						preparedStatement.setString(21, messageSummaryDTO.getMesgReceiverSwiftAddress());
						preparedStatement.setString(22, messageSummaryDTO.getMesgSubFormat());
						preparedStatement.setString(23, messageSummaryDTO.getMesgType());
						preparedStatement.setString(24, messageSummaryDTO.getxFinCcy());
						preparedStatement.setDouble(25, messageSummaryDTO.getxFinAmount());
						preparedStatement.setDate(26, messageSummaryDTO.getxFinValueDate());
						preparedStatement.setString(27, messageSummaryDTO.getReceicerBIC());
						preparedStatement.setString(28, messageSummaryDTO.getMesgSLA());
						preparedStatement.setString(29, messageSummaryDTO.getxInst0RPName());
						preparedStatement.setString(30, messageSummaryDTO.getxLastEMIAppeDelvStatus());
						preparedStatement.setString(31, messageSummaryDTO.getMesgStatusCode());
						preparedStatement.setString(32, messageSummaryDTO.getMesgReasonCode());
						preparedStatement.setString(33, messageSummaryDTO.getMesgNakCode());
						preparedStatement.setString(34, messageSummaryDTO.getMesgCharges());
						preparedStatement.setString(35, messageSummaryDTO.getAppeSessionHolder());
						preparedStatement.setDate(36, messageSummaryDTO.getAppeDateTime());
						preparedStatement.setString(37, messageSummaryDTO.getAppeType());
						preparedStatement.setString(38, messageSummaryDTO.getMessagePartner());
						preparedStatement.setString(39, messageSummaryDTO.getOwnerBIC11());
						preparedStatement.setString(40, messageSummaryDTO.getOwnerInstitutionName());
						preparedStatement.setString(41, messageSummaryDTO.getOwnerBranchCode());
						preparedStatement.setString(42, messageSummaryDTO.getOwnerBranchInfo());
						preparedStatement.setString(43, messageSummaryDTO.getOwnerCountryCode());
						preparedStatement.setString(44, messageSummaryDTO.getOwnerCountryName());
						preparedStatement.setString(45, messageSummaryDTO.getOwnerGeoLocation());
						preparedStatement.setString(46, messageSummaryDTO.getCounterPartBIC8());
						preparedStatement.setString(47, messageSummaryDTO.getCounterPartBIC11());
						preparedStatement.setString(48, messageSummaryDTO.getCounterPartInstitutionName());
						preparedStatement.setString(49, messageSummaryDTO.getCounterPartBranchCode());
						preparedStatement.setString(50, messageSummaryDTO.getCounterPartBranchInfo());
						preparedStatement.setString(51, messageSummaryDTO.getCounterPartCountryCode());
						preparedStatement.setString(52, messageSummaryDTO.getCounterPartCountryName());
						preparedStatement.setString(53, messageSummaryDTO.getCounterPartGeoLocation());
						preparedStatement.setString(54, messageSummaryDTO.getCurrencyName());
						preparedStatement.setString(55, messageSummaryDTO.getBusinessArea());
						preparedStatement.setString(56, messageSummaryDTO.getMesgCopyService());
						preparedStatement.setString(57, messageSummaryDTO.getTransmissionDelay());
						preparedStatement.setString(58, messageSummaryDTO.getCategoryDescription());
						preparedStatement.setString(59, messageSummaryDTO.getCustomerRoute());
						preparedStatement.setString(60, messageSummaryDTO.getGeoRoute());
						preparedStatement.setString(61, messageSummaryDTO.getDay());
						preparedStatement.setString(62, messageSummaryDTO.getMonth());
						preparedStatement.setString(63, messageSummaryDTO.getYear());
						preparedStatement.setString(64, messageSummaryDTO.getQuarterlyPeriod());
						preparedStatement.setString(65, messageSummaryDTO.getPeriod());
						preparedStatement.setDouble(66, messageSummaryDTO.getBaseAmount());
						preparedStatement.setString(67, messageSummaryDTO.getStanderValueBucket());
						preparedStatement.setInt(68, 0);
						preparedStatement.setInt(69, messageSummaryDTO.getGpiFlag());
						preparedStatement.setString(70, messageSummaryDTO.getMesgUETR());
					}

					@Override
					public int getBatchSize() {
						return messageSummaryDTOs.size();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			LOGGER.debug((messageSummaryDTOs.size()) + " Messages Inserted");
			auditDAO.insertIntoErrorld("Success",
					"Done Inserting " + (messageSummaryDTOs.size()) + " Messages to MESG_SUMMARY Table");

		} catch (Exception e) {

			if (messageSummaryDTOs.size() == 1) {
				MessageSummaryDTO messageDTO = messageSummaryDTOs.get(0);
				auditDAO.insertIntoErrorld("Error",
						"Error Inserting message to MESG_SUMMARY table for the following id:" + "AID: "
								+ messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + " UMIDH: "
								+ messageDTO.getUmidh());
				LOGGER.error("Message ID: AID: " + messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + "UMIDH: "
						+ messageDTO.getUmidh());
			} else {
				auditDAO.insertIntoErrorld("Error", "Error Inserting messages into MESG_SUMMARY table Messages ");
				LOGGER.error("Error Inserting messages into MESG_SUMMARY table");
				LOGGER.error("Try using bulk size of 1 to know the id of the message caused the failure");
			}

			e.printStackTrace();
			throw e;
		}
	}

	public void updateMessages(List<MessageSummaryDTO> messageSummaryDTOs) {
		try {
			LOGGER.debug("Updating: " + (messageSummaryDTOs.size()) + " Messages");
			String insertStatemnt = "update mesg_summary set  " + "MESG_REFERENCE = ? ,"
					+ "MESG_RELATED_REFERENCE = ? ," + "MESG_CREA_MPFN_NAME = ?  , " + "MESG_CREA_RP_NAME = ?  , "
					+ "MESG_CREA_OPER_NICKNAME = ?  , " + "MESG_CREA_DATE_TIME = ?  , "
					+ "MESG_MOD_OPER_NICKNAME = ?  , " + "MESG_MOD_DATE_TIME = ?  , " + "MESG_FRMT_NAME = ?  , "
					+ "MESG_SENDER_X1 = ?  , " + "X_OWN_LT = ?  , " + "X_INST0_UNIT_NAME = ?  , " + "X_CATEGORY = ?  , "
					+ "ARCHIVED = ?  , " + "MESG_STATUS = ?  , " + "MESG_NETWORK_PRIORITY = ?  , "
					+ "MESG_POSSIBLE_DUP_CREATION = ?  , " + "MESG_RECEIVER_SWIFT_ADDRESS = ?  , "
					+ "MESG_SUB_FORMAT = ?  , " + "MESG_TYPE = ?  , " + "X_FIN_CCY = ?  , " + "X_FIN_AMOUNT = ?  , "
					+ "X_FIN_VALUE_DATE = ?  , " + "X_RECEIVER_X1 = ?  , " + "MESG_SLA = ?  , "
					+ "X_INST0_RP_NAME = ?  , " + "X_LAST_EMI_APPE_DELV_STS = ?  , " + "MESG_STATUS_CODE = ?  , "
					+ "MESG_REASON_CODE = ?  , " + "MESG_NAK_CODE = ?  , " + "MESG_CHARGES = ?  , "
					+ "APPE_SESSION_HOLDER = ?  , " + "APPE_DATE_TIME = ?  , " + "APPE_TYPE = ?  , "
					+ "MESSAGE_PARTNER = ?  , " + "OWNER_BIC_11 = ?  , " + "OWNER_INSTITUTION_NAME = ?  , "
					+ "OWNER_BRANCH_CODE = ?  , " + "OWNER_BRANCH_INFO = ?  , " + "OWNER_CTRY_CODE = ?  , "
					+ "OWNER_CTRY_NAME = ?  , " + "OWNER_GEO_LOCATION = ?  , " + "COUNTERPART_BIC_8 = ?  , "
					+ "COUNTERPART_BIC_11 = ?  , " + "COUNTERPART_INSTITUTION_NAME = ?  , "
					+ "COUNTERPART_BRANCH_CODE = ?  , " + "COUNTERPART_BRANCH_INFO = ?  , "
					+ "COUNTERPART_CTRY_CODE = ?  , " + "COUNTERPART_CTRY_NAME = ?  , "
					+ "COUNTERPART_GEO_LOCATION = ?  , " + " CURRENCYNAME = ?  , " + " Business_Area = ? ,"
					+ " mesg_copy_service_id = ? ," + " Transmission_Delay = ? ," + " Customer_Route = ? ,"
					+ "  Geo_Route = ? , " + "  Day = ? , " + "  Month = ? ," + "  Year = ? , "
					+ "  Quarterly_Period = ? ," + "  Period = ? ," + "  Base_Amount = ? ,"
					+ "  Standard_Value_Bucket = ? ," + "  GPI_FLAG = ? ," + " UETR=?"
					+ " where MESG_S_UMIDH = ? and MESG_S_UMIDL= ? and AID = ?  ";

			jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
					MessageSummaryDTO messageSummaryDTO = messageSummaryDTOs.get(index);
					preparedStatement.setString(1, messageSummaryDTO.getMesgReference());
					preparedStatement.setString(2, messageSummaryDTO.getMesgRelatedReference());
					preparedStatement.setString(3, messageSummaryDTO.getMesgCreateMPFNName());
					preparedStatement.setString(4, messageSummaryDTO.getMesgCreateRPName());
					preparedStatement.setString(5, messageSummaryDTO.getMesgCreateOperatorNickName());
					preparedStatement.setDate(6, messageSummaryDTO.getMesgCreateDateTime());
					preparedStatement.setString(7, messageSummaryDTO.getModificationOperatorNickName());
					preparedStatement.setDate(8, messageSummaryDTO.getModificationDateTime());
					preparedStatement.setString(9, messageSummaryDTO.getMesgFormatName());
					preparedStatement.setString(10, messageSummaryDTO.getSenderBIC());
					preparedStatement.setString(11, messageSummaryDTO.getxOwnLT());
					preparedStatement.setString(12, messageSummaryDTO.getxInst0UnitName());
					preparedStatement.setString(13, messageSummaryDTO.getxCategory());
					preparedStatement.setLong(14, messageSummaryDTO.getArchived());
					preparedStatement.setString(15, messageSummaryDTO.getMesgStatus());
					preparedStatement.setString(16, messageSummaryDTO.getMesgNetworkPriority());
					preparedStatement.setString(17, messageSummaryDTO.getMesgPossibleDuplicateCreation());
					preparedStatement.setString(18, messageSummaryDTO.getMesgReceiverSwiftAddress());
					preparedStatement.setString(19, messageSummaryDTO.getMesgSubFormat());
					preparedStatement.setString(20, messageSummaryDTO.getMesgType());
					preparedStatement.setString(21, messageSummaryDTO.getxFinCcy());
					preparedStatement.setDouble(22, messageSummaryDTO.getxFinAmount());
					preparedStatement.setDate(23, messageSummaryDTO.getMesgCreateDateTime());
					preparedStatement.setString(24, messageSummaryDTO.getReceicerBIC());
					preparedStatement.setString(25, messageSummaryDTO.getMesgSLA());
					preparedStatement.setString(26, messageSummaryDTO.getxInst0RPName());
					preparedStatement.setString(27, messageSummaryDTO.getxLastEMIAppeDelvStatus());
					preparedStatement.setString(28, messageSummaryDTO.getMesgStatusCode());
					preparedStatement.setString(29, messageSummaryDTO.getMesgReasonCode());
					preparedStatement.setString(30, messageSummaryDTO.getMesgNakCode());
					preparedStatement.setString(31, messageSummaryDTO.getMesgCharges());
					preparedStatement.setString(32, messageSummaryDTO.getAppeSessionHolder());
					preparedStatement.setDate(33, messageSummaryDTO.getAppeDateTime());
					preparedStatement.setString(34, messageSummaryDTO.getAppeType());
					preparedStatement.setString(35, messageSummaryDTO.getMessagePartner());
					preparedStatement.setString(36, messageSummaryDTO.getOwnerBIC11());
					preparedStatement.setString(37, messageSummaryDTO.getOwnerInstitutionName());
					preparedStatement.setString(38, messageSummaryDTO.getOwnerBranchCode());
					preparedStatement.setString(39, messageSummaryDTO.getOwnerBranchInfo());
					preparedStatement.setString(40, messageSummaryDTO.getOwnerCountryCode());
					preparedStatement.setString(41, messageSummaryDTO.getOwnerCountryName());
					preparedStatement.setString(42, messageSummaryDTO.getOwnerGeoLocation());
					preparedStatement.setString(43, messageSummaryDTO.getCounterPartBIC8());
					preparedStatement.setString(44, messageSummaryDTO.getCounterPartBIC11());
					preparedStatement.setString(45, messageSummaryDTO.getCounterPartInstitutionName());
					preparedStatement.setString(46, messageSummaryDTO.getCounterPartBranchCode());
					preparedStatement.setString(47, messageSummaryDTO.getCounterPartBranchInfo());
					preparedStatement.setString(48, messageSummaryDTO.getCounterPartCountryCode());
					preparedStatement.setString(49, messageSummaryDTO.getCounterPartCountryName());
					preparedStatement.setString(50, messageSummaryDTO.getCounterPartGeoLocation());
					preparedStatement.setString(51, messageSummaryDTO.getCurrencyName());
					preparedStatement.setString(52, messageSummaryDTO.getBusinessArea());
					preparedStatement.setString(53, messageSummaryDTO.getMesgCopyService());
					preparedStatement.setString(54, messageSummaryDTO.getTransmissionDelay());
					preparedStatement.setString(55, messageSummaryDTO.getCustomerRoute());
					preparedStatement.setString(56, messageSummaryDTO.getGeoRoute());
					preparedStatement.setString(57, messageSummaryDTO.getDay());
					preparedStatement.setString(58, messageSummaryDTO.getMonth());
					preparedStatement.setString(59, messageSummaryDTO.getYear());
					preparedStatement.setString(60, messageSummaryDTO.getQuarterlyPeriod());
					preparedStatement.setString(61, messageSummaryDTO.getPeriod());
					preparedStatement.setDouble(62, messageSummaryDTO.getBaseAmount());
					preparedStatement.setString(63, messageSummaryDTO.getStanderValueBucket());
					preparedStatement.setInt(64, messageSummaryDTO.getGpiFlag());
					preparedStatement.setString(65, messageSummaryDTO.getMesgUETR());
					preparedStatement.setLong(66, messageSummaryDTO.getUmidh());
					preparedStatement.setLong(67, messageSummaryDTO.getUmidl());
					preparedStatement.setLong(68, messageSummaryDTO.getAid());
				}

				@Override
				public int getBatchSize() {
					return messageSummaryDTOs.size();
				}
			});

			LOGGER.debug((messageSummaryDTOs.size()) + " Messages Updated");
			auditDAO.insertIntoErrorld("Success",
					"Done Updating " + (messageSummaryDTOs.size()) + " Messages to MESG_SUMMARY Table");
		} catch (Exception e) {

			if (messageSummaryDTOs.size() == 1) {
				MessageSummaryDTO messageDTO = messageSummaryDTOs.get(0);
				auditDAO.insertIntoErrorld("Error",
						"Error updating message in MESG_SUMMARY table for the following id:" + "AID: "
								+ messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + " UMIDH: "
								+ messageDTO.getUmidh());

				LOGGER.error("Error Updating message in MESG_SUMMARY table");
				LOGGER.error("Message ID - AID: " + messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + "UMIDH: "
						+ messageDTO.getUmidh());
			} else {
				auditDAO.insertIntoErrorld("Error", "Error updating messages in MESG_SUMMARY table");
				LOGGER.error("Error Updating messages in MESG_SUMMARY table");
				LOGGER.error("Try using bulk size of 1 to know the id of the message caused the failure");
			}

			throw e;
		}
	}

	public void updateMessageStatus(List<MessageSummaryDTO> messageSummaryDTOs) {

		try {

			LOGGER.trace("Updating Fetch Status to 1 for  " + (messageSummaryDTOs.size()) + "  Messages");

			String insertStatemnt = "Update rmesg set FETCH_STATUS = 1 where aid = ? and mesg_s_umidl = ?  and  mesg_s_umidh = ?";

			jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
					MessageSummaryDTO messageSummaryDTO = messageSummaryDTOs.get(index);
					preparedStatement.setLong(1, messageSummaryDTO.getAid());
					preparedStatement.setLong(2, messageSummaryDTO.getUmidl());
					preparedStatement.setLong(3, messageSummaryDTO.getUmidh());
				}

				@Override
				public int getBatchSize() {
					return messageSummaryDTOs.size();
				}
			});

			LOGGER.trace("Done Updating Fetch_Status in RMESG table for " + (messageSummaryDTOs.size()) + " Messages");

		} catch (Exception e) {
			if (messageSummaryDTOs.size() == 1) {
				MessageSummaryDTO messageDTO = messageSummaryDTOs.get(0);
				auditDAO.insertIntoErrorld("Error",
						"Error Updating Messages status in RMESG table for the following id:" + "AID: "
								+ messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + " UMIDH: "
								+ messageDTO.getUmidh());

				LOGGER.error("Message ID: AID: " + messageDTO.getAid() + " UMIDL: " + messageDTO.getUmidl() + "UMIDH: "
						+ messageDTO.getUmidh());
			} else {
				auditDAO.insertIntoErrorld("Error", "Error Updating Messages status in RMESG table ");
				LOGGER.error("Error Updating Messages status in RMESG table ");
				LOGGER.error("Try using bulk size of 1 to know the id of the message caused the failure");
			}
			e.printStackTrace();
			throw e;
		}

	}

}
