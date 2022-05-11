package com.eastnets.calculated.measures.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eastnets.audit.dao.AuditDAO;
import com.eastnets.commonLkup.service.CommonLkupService;
import com.eastnets.message.summary.Bean.CalculatedMeasuresBean;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.Bean.TextFieldBean;
import com.eastnets.message.summary.configuration.GlobalConfiguration;
import com.eastnets.message.summary.dao.MessageWriterDAO;
import com.eastnets.message.summary.enumDesc.Field70Options;
import com.eastnets.message.summary.enumDesc.Field72Options;
import com.eastnets.message.summary.enumDesc.Role;
import com.eastnets.util.Constants;

@Repository
public class CalculatedMeasureWriterDAO {

	private static final Logger LOGGER = LogManager.getLogger(MessageWriterDAO.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public GlobalConfiguration globalConfiguration;

	@Autowired
	public CommonLkupService commonLkupService;

	@Autowired
	AuditDAO auditDAO;

	public void writeCalculatedMeasures(List<MessageSummaryDTO> messageSummaryList) {

		LOGGER.info("Start parsing the MessageSummarry list");
		List<CalculatedMeasuresBean> calculatedMeasuresList = new ArrayList<>();

		for (MessageSummaryDTO messageSummaryBean : messageSummaryList) {
			CalculatedMeasuresBean calculatedMeasuresBean = new CalculatedMeasuresBean();
			calculatedMeasuresBean.setAid(messageSummaryBean.getAid());
			calculatedMeasuresBean.setUmidh(messageSummaryBean.getUmidh());
			calculatedMeasuresBean.setUmidl(messageSummaryBean.getUmidl());
			calculatedMeasuresBean.setMesgCreateDateTime(messageSummaryBean.getMesgCreateDateTime());
			if (!messageSummaryBean.getFieldsOptionsValueList().isEmpty()) {

				try {
					setTextFieldCalculatedMeasures(messageSummaryBean, calculatedMeasuresBean);
					calculatedMeasuresList.add(calculatedMeasuresBean);

				} catch (Exception e) {
					updateFaildToParseMessagesStatus(calculatedMeasuresBean);
					LOGGER.error("Error in parsing MessageSummarry message with AID: " + messageSummaryBean.getAid() + " UMIDL: " + messageSummaryBean.getUmidl() + " UMIDH: " + messageSummaryBean.getUmidh() + " " + e);

					auditDAO.insertIntoErrorld("Error", "Error in parsing MessageSummarry message with AID: " + messageSummaryBean.getAid() + " UMIDL: " + messageSummaryBean.getUmidl() + " UMIDH: " + messageSummaryBean.getUmidh());

					e.printStackTrace();
				}
			}
		}
		if (!calculatedMeasuresList.isEmpty()) {
			insertCalculatedMeasures(calculatedMeasuresList);
		}

	}

	private void setTextFieldCalculatedMeasures(MessageSummaryDTO messageSummaryBean, CalculatedMeasuresBean calculatedMeasuresBean) {

		Map<Integer, TextFieldBean> textFieldsList = messageSummaryBean.getFieldsOptionsValueList();
		String currencyPair = null;

		if (textFieldsList != null && !textFieldsList.isEmpty()) {

			String corridor = messageSummaryBean.getSenderBIC().substring(4, 6) + " to " + messageSummaryBean.getReceicerBIC().substring(4, 6);
			calculatedMeasuresBean.setCorridor(corridor);

			if (textFieldsList.containsKey(33) && textFieldsList.get(33).getOptionsValuesMap().containsKey("B")) {
				String optionValue = getOptionValue(textFieldsList, 33, "B");
				currencyPair = optionValue.substring(0, 3);
			}

			if (currencyPair != null && textFieldsList.containsKey(32) && textFieldsList.get(32).getOptionsValuesMap().containsKey("A")) {

				String optionValue = getOptionValue(textFieldsList, 32, "A");
				currencyPair = currencyPair + " " + optionValue.substring(6, 9);
				calculatedMeasuresBean.setCurrencyPair(currencyPair);
			}

			initialOrdering(messageSummaryBean, textFieldsList, calculatedMeasuresBean);
			ultimateBeneficiary(messageSummaryBean, textFieldsList, calculatedMeasuresBean);
			setFieldMT103(messageSummaryBean, textFieldsList, calculatedMeasuresBean);
			assignedRole(messageSummaryBean, textFieldsList, calculatedMeasuresBean);

		}
	}

	public void initialOrdering(MessageSummaryDTO messageSummaryBean, Map<Integer, TextFieldBean> textFieldsList, CalculatedMeasuresBean calculatedMeasuresBean) {

		// Initial Ordering BIC
		if (textFieldsList.containsKey(52)) {

			if (textFieldsList.get(52).getOptionsValuesMap().containsKey("A")) {
				String optionValue = getOptionValue(textFieldsList, 52, "A");
				calculatedMeasuresBean.setInitialOrderingBIC(optionValue.substring(0, 8));
				calculatedMeasuresBean.setInitialOrderingOption(Constants.FIELD_OPT_A);

				calculatedMeasuresBean.setInitialOrderingCountery(
						commonLkupService.getCorrespondentsInformationBIC8().containsKey(optionValue.substring(0, 8)) ? commonLkupService.getCorrespondentsInformationBIC8().get(optionValue.substring(0, 8)).getCorrCountryName()
								: Constants.NOT_AVAILABLE);

				calculatedMeasuresBean
						.setInitialOrderingRegion(commonLkupService.getGeoLocations().containsKey(optionValue.substring(4, 6)) ? commonLkupService.getGeoLocations().get(optionValue.substring(4, 6)).getRegion() : Constants.NOT_AVAILABLE);

			} else {
				calculatedMeasuresBean.setInitialOrderingOption(Constants.FIELD_OPT_FREE_FORMAT);
			}

		} else {
			calculatedMeasuresBean.setInitialOrderingBIC(messageSummaryBean.getSenderBIC());
			String key = messageSummaryBean.getSenderBIC();

			calculatedMeasuresBean.setInitialOrderingCountery(commonLkupService.getCorrespondentsInformation().containsKey(key) ? commonLkupService.getCorrespondentsInformation().get(key).getCorrCountryName() : Constants.NOT_AVAILABLE);

			calculatedMeasuresBean.setInitialOrderingRegion(commonLkupService.getGeoLocations().containsKey(key.substring(4, 6)) ? commonLkupService.getGeoLocations().get(key.substring(4, 6)).getRegion() : Constants.NOT_AVAILABLE);

			calculatedMeasuresBean.setInitialOrderingOption(Constants.FIELD_OPT_EMPTY);
		}

	}

	public void ultimateBeneficiary(MessageSummaryDTO messageSummaryBean, Map<Integer, TextFieldBean> textFieldsList, CalculatedMeasuresBean calculatedMeasuresBean) {

		// Ultimate Beneficiary
		if (textFieldsList.containsKey(57) && !messageSummaryBean.getMesgType().equals("202") && textFieldsList.get(57).getOptionsValuesMap().containsKey("A")) {

			String optionValue = getOptionValue(textFieldsList, 57, "A");
			calculatedMeasuresBean.setUltimateBeneficiaryBIC(optionValue.substring(0, 8));

			calculatedMeasuresBean.setUltimateBeneficiaryCountry(
					commonLkupService.getCorrespondentsInformationBIC8().containsKey(optionValue.substring(0, 8)) ? commonLkupService.getCorrespondentsInformationBIC8().get(optionValue.substring(0, 8)).getCorrCountryName() : Constants.NOT_AVAILABLE);

			calculatedMeasuresBean
					.setUltimateBeneficiaryRegion(commonLkupService.getGeoLocations().containsKey(optionValue.substring(4, 6)) ? commonLkupService.getGeoLocations().get(optionValue.substring(4, 6)).getRegion() : Constants.NOT_AVAILABLE);

		} else if (textFieldsList.containsKey(58) && messageSummaryBean.getMesgType().equals("202") && textFieldsList.get(58).getOptionsValuesMap().containsKey("A")) {

			String optionValue = getOptionValue(textFieldsList, 58, "A");
			if (optionValue != null && !optionValue.isEmpty()) {
				calculatedMeasuresBean.setUltimateBeneficiaryBIC(optionValue.substring(0, 8));
			}

			calculatedMeasuresBean.setUltimateBeneficiaryCountry(
					commonLkupService.getCorrespondentsInformationBIC8().containsKey(optionValue.substring(0, 8)) ? commonLkupService.getCorrespondentsInformationBIC8().get(optionValue.substring(0, 8)).getCorrCountryName() : Constants.NOT_AVAILABLE);
			calculatedMeasuresBean
					.setUltimateBeneficiaryRegion(commonLkupService.getGeoLocations().containsKey(optionValue.substring(4, 6)) ? commonLkupService.getGeoLocations().get(optionValue.substring(4, 6)).getRegion() : Constants.NOT_AVAILABLE);
		} else

		{
			calculatedMeasuresBean.setUltimateBeneficiaryBIC(messageSummaryBean.getReceicerBIC());

			String key = messageSummaryBean.getReceicerBIC();

			calculatedMeasuresBean.setUltimateBeneficiaryCountry(commonLkupService.getCorrespondentsInformation().containsKey(key) ? commonLkupService.getCorrespondentsInformation().get(key).getCorrCountryName() : Constants.NOT_AVAILABLE);

			calculatedMeasuresBean.setUltimateBeneficiaryRegion(commonLkupService.getGeoLocations().containsKey(key.substring(4, 6)) ? commonLkupService.getGeoLocations().get(key.substring(4, 6)).getRegion() : Constants.NOT_AVAILABLE);

		}

	}

	public void setFieldMT103(MessageSummaryDTO messageSummaryBean, Map<Integer, TextFieldBean> textFieldsList, CalculatedMeasuresBean calculatedMeasuresBean) {

		if (messageSummaryBean.getMesgType().equals("103")) {

			if (textFieldsList.containsKey(53) && textFieldsList.get(53).getOptionsValuesMap().containsKey("A")) {

				String optionValue = getOptionValue(textFieldsList, 53, "A");

				calculatedMeasuresBean.setSenderCorrBIC(optionValue.substring(0, 8));
				calculatedMeasuresBean.setCoverPaymentFlag((optionValue != null && !optionValue.isEmpty()) ? 1 : 0);
			}

			if (textFieldsList.containsKey(33) && textFieldsList.get(33).getOptionsValuesMap().containsKey("B")) {

				String optionValue = getOptionValue(textFieldsList, 33, "B");
				calculatedMeasuresBean.setInstructedCurrency(optionValue.substring(0, 3));
			}

			if (textFieldsList.containsKey(23) && textFieldsList.get(23).getOptionsValuesMap().containsKey("E")) {
				calculatedMeasuresBean.setInstructionCode(get23EInstructionCode(textFieldsList, 23, "E"));
			}

			if (textFieldsList.containsKey(56)) {

				if (textFieldsList.get(56).getOptionsValuesMap().containsKey("A")) {
					String optionValue = getOptionValue(textFieldsList, 56, "A");

					String corrInstitutionName = commonLkupService.getCorrespondentsInformationBIC8().containsKey(optionValue.substring(0, 8))
							? commonLkupService.getCorrespondentsInformationBIC8().get(optionValue.substring(0, 8)).getCorrInstitutionName()
							: Constants.NOT_AVAILABLE;

					calculatedMeasuresBean.setIntermediaryInstitution(corrInstitutionName);
					calculatedMeasuresBean.setIntermediaryInstitutionOption(Constants.FIELD_OPT_A);

				} else {
					calculatedMeasuresBean.setIntermediaryInstitutionOption(Constants.FIELD_OPT_FREE_FORMAT);
				}
			} else {
				calculatedMeasuresBean.setIntermediaryInstitutionOption(Constants.FIELD_OPT_EMPTY);
			}

			if (textFieldsList.containsKey(54)) {

				if (textFieldsList.get(54).getOptionsValuesMap().containsKey("A")) {

					String optionValue = getOptionValue(textFieldsList, 54, "A");
					calculatedMeasuresBean.setReceiverCorrespondentBIC8(optionValue.substring(0, 8));
					calculatedMeasuresBean.setReceiverCorrespondentOption(Constants.FIELD_OPT_A);
				} else {
					calculatedMeasuresBean.setReceiverCorrespondentOption(Constants.FIELD_OPT_FREE_FORMAT);
				}
			} else {
				calculatedMeasuresBean.setReceiverCorrespondentOption(Constants.FIELD_OPT_EMPTY);
			}

			if (textFieldsList.containsKey(70) && textFieldsList.get(70).getListOfvalue() != null && !textFieldsList.get(70).getListOfvalue().isEmpty()) {

				String value = textFieldsList.get(70).getListOfvalue().get(0);
				String remittanceInfo = value;
				int lastIndext = value.substring(1).indexOf('/');

				if (lastIndext != -1 && EnumUtils.isValidEnum(Field70Options.class, value.substring(1, lastIndext + 1))) {
					remittanceInfo = Field70Options.valueOf(value.substring(1, lastIndext + 1)).getFieldValue();
				}
				calculatedMeasuresBean.setRemittanceInformation(remittanceInfo);

			}

			if (textFieldsList.containsKey(72) && textFieldsList.get(72).getListOfvalue() != null && !textFieldsList.get(72).getListOfvalue().isEmpty()) {

				String value = textFieldsList.get(72).getListOfvalue().get(0);
				String senderToReceiverInfo = value;
				int lastIndext = value.substring(1).indexOf('/');

				if (lastIndext != -1 && EnumUtils.isValidEnum(Field72Options.class, value.substring(1, lastIndext + 1))) {
					senderToReceiverInfo = Field72Options.valueOf(value.substring(1, lastIndext + 1)).getFieldValue();
				}

				calculatedMeasuresBean.setSenderToReceiverInformation(senderToReceiverInfo);
			}

		}

	}

	@Transactional
	public void insertCalculatedMeasures(List<CalculatedMeasuresBean> calculatedMeasureslist) {

		LOGGER.info("Start Inserting: " + (calculatedMeasureslist.size()) + " messages details in CAL_PAYM_MEASURES table");
		String insertStatemnt = "INSERT INTO CAL_PAYM_MEASURES VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
					CalculatedMeasuresBean calculatedMeasuresBean = calculatedMeasureslist.get(index);

					preparedStatement.setLong(1, calculatedMeasuresBean.getAid());
					preparedStatement.setLong(2, calculatedMeasuresBean.getUmidl());
					preparedStatement.setLong(3, calculatedMeasuresBean.getUmidh());
					preparedStatement.setString(4, calculatedMeasuresBean.getInitialOrderingBIC());
					preparedStatement.setString(5, calculatedMeasuresBean.getInitialOrderingCountery());
					preparedStatement.setString(6, calculatedMeasuresBean.getInitialOrderingRegion());
					preparedStatement.setString(7, calculatedMeasuresBean.getUltimateBeneficiaryBIC());
					preparedStatement.setString(8, calculatedMeasuresBean.getUltimateBeneficiaryCountry());
					preparedStatement.setString(9, calculatedMeasuresBean.getUltimateBeneficiaryRegion());
					preparedStatement.setInt(10, (calculatedMeasuresBean.getCoverPaymentFlag() != null) ? calculatedMeasuresBean.getCoverPaymentFlag() : 0);
					preparedStatement.setString(11, calculatedMeasuresBean.getCurrencyPair());
					preparedStatement.setString(12, calculatedMeasuresBean.getInitialOrderingOption());
					preparedStatement.setString(13, calculatedMeasuresBean.getInstructedCurrency());
					preparedStatement.setString(14, calculatedMeasuresBean.getInstructionCode());
					preparedStatement.setString(15, calculatedMeasuresBean.getIntermediaryInstitution());
					preparedStatement.setString(16, calculatedMeasuresBean.getIntermediaryInstitutionOption());
					preparedStatement.setString(17, calculatedMeasuresBean.getReceiverCorrespondentBIC8());
					preparedStatement.setString(18, calculatedMeasuresBean.getReceiverCorrespondentOption());
					preparedStatement.setString(19, calculatedMeasuresBean.getRemittanceInformation());
					preparedStatement.setString(20, calculatedMeasuresBean.getSenderToReceiverInformation());
					preparedStatement.setString(21, calculatedMeasuresBean.getMyRole());
					preparedStatement.setString(22, calculatedMeasuresBean.getCounterPartRole());
					preparedStatement.setDate(23, calculatedMeasuresBean.getMesgCreateDateTime());
					preparedStatement.setString(24, calculatedMeasuresBean.getSenderCorrBIC());
					preparedStatement.setString(25, calculatedMeasuresBean.getCorridor());

				}

				@Override
				public int getBatchSize() {
					return calculatedMeasureslist.size();
				}
			});
		} catch (Exception e) {
			if (calculatedMeasureslist.size() == 1) {
				CalculatedMeasuresBean calculatedMeasuresBean = calculatedMeasureslist.get(0);
				auditDAO.insertIntoErrorld("Error",
						"Error Inserting rows to CAL_PAYM_MEASURES table for the following id:" + "AID: " + calculatedMeasuresBean.getAid() + " UMIDL: " + calculatedMeasuresBean.getUmidl() + " UMIDH: " + calculatedMeasuresBean.getUmidh());
				LOGGER.error("Message ID: AID: " + calculatedMeasuresBean.getAid() + " UMIDL: " + calculatedMeasuresBean.getUmidl() + "UMIDH: " + calculatedMeasuresBean.getUmidh());
			} else {
				auditDAO.insertIntoErrorld("Error", "Error Inserting into CAL_PAYM_MEASURES table ");
				LOGGER.error("Error Inserting into CAL_PAYM_MEASURES table");
				LOGGER.error("Try using bulk size of 1 to know the id of the message caused the failure");

			}
			LOGGER.error(e);
			return;
		}

		LOGGER.debug((calculatedMeasureslist.size()) + " calculatedMeasureslist Inserted");
		auditDAO.insertIntoErrorld("Success", "Done Inserting " + (calculatedMeasureslist.size()) + " rows to CAL_PAYM_MEASURES Table");
		updateMessageSummaryStatus(calculatedMeasureslist);

	}

	public void updateMessageSummaryStatus(List<CalculatedMeasuresBean> calculatedMeasureslist) {

		LOGGER.trace("Start Updating Fetch Status to 1 for MESG_SUMMARY table " + calculatedMeasureslist.size() + "  Messages");

		String insertStatemnt = "Update mesg_summary set CALCULATED_STATUS = 1 where aid = ? and mesg_s_umidl = ?  and  mesg_s_umidh = ?";
		try {
			jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
					CalculatedMeasuresBean calculatedMeasuresBean = calculatedMeasureslist.get(index);
					preparedStatement.setLong(1, calculatedMeasuresBean.getAid());
					preparedStatement.setLong(2, calculatedMeasuresBean.getUmidl());
					preparedStatement.setLong(3, calculatedMeasuresBean.getUmidh());
				}

				@Override
				public int getBatchSize() {
					return calculatedMeasureslist.size();
				}
			});

			LOGGER.trace("Done Updating " + calculatedMeasureslist.size() + " messages 'Fetch_Status' in MESG_SUMMARY table");

		} catch (Exception e) {
			LOGGER.error(e);
			if (calculatedMeasureslist.size() == 1) {
				CalculatedMeasuresBean calculatedMeasuresBean = calculatedMeasureslist.get(0);
				auditDAO.insertIntoErrorld("Error", "Error Updating messages 'Fetch_Status' in MESG_SUMMARY table for the following id:" + "AID: " + calculatedMeasuresBean.getAid() + " UMIDL: " + calculatedMeasuresBean.getUmidl() + " UMIDH: "
						+ calculatedMeasuresBean.getUmidh());

				LOGGER.error("Message ID: AID: " + calculatedMeasuresBean.getAid() + " UMIDL: " + calculatedMeasuresBean.getUmidl() + "UMIDH: " + calculatedMeasuresBean.getUmidh());
			} else {
				auditDAO.insertIntoErrorld("Error", "Error Updating messages 'Fetch_Status' in MESG_SUMMARY table ");
				LOGGER.error("Error Updating Messages status in MESG_SUMMARY table ");
			}

		}

	}

	public void updateFaildToParseMessagesStatus(CalculatedMeasuresBean calculatedMeasuresBean) {

		LOGGER.trace("Updating Fetch Status to 2 in MESG_SUMMARY table for not parsed message");

		String updateStatemnt = "Update mesg_summary set CALCULATED_STATUS = 2 where aid = ? and mesg_s_umidl = ?  and  mesg_s_umidh = ?";
		try {
			jdbcTemplate.update(updateStatemnt, new Object[] { calculatedMeasuresBean.getAid(), calculatedMeasuresBean.getUmidl(), calculatedMeasuresBean.getUmidh() });

		} catch (Exception e) {

			auditDAO.insertIntoErrorld("Error", "Error Updating messages 'Fetch_Status' in MESG_SUMMARY table ");
			LOGGER.error("Error Updating Messages status in MESG_SUMMARY table ");
		}

	}

	private void assignedRole(MessageSummaryDTO messageSummaryBean, Map<Integer, TextFieldBean> textFieldsList, CalculatedMeasuresBean calculatedMeasuresBean) {

		if (messageSummaryBean.getMesgSubFormat().equalsIgnoreCase("INPUT")) { // First check outgoing messages
			getMyRoleForInputMessaegs(calculatedMeasuresBean, textFieldsList, messageSummaryBean);
			getCounterPartRoleForInputMessaegs(calculatedMeasuresBean, messageSummaryBean.getReceicerBIC(), textFieldsList, messageSummaryBean);

		} else {
			getMyRoleForOutputMessaegs(calculatedMeasuresBean, textFieldsList, messageSummaryBean);
			getCounterPartRoleForOutoutMessaegs(calculatedMeasuresBean, messageSummaryBean.getSenderBIC(), textFieldsList);
		}

	}

	private void getMyRoleForOutputMessaegs(CalculatedMeasuresBean calculatedMeasuresBean, Map<Integer, TextFieldBean> textFieldsList, MessageSummaryDTO messageSummaryBean) {

		boolean containsKey = false;
		if (messageSummaryBean.getMesgType().equals("103")) {
			containsKey = textFieldsList.containsKey(57);
		} else {
			containsKey = textFieldsList.containsKey(58);
		}

		if (!containsKey) {
			// MyRole >> Benifashart

			calculatedMeasuresBean.setMyRole(Role.BENEFICIARY.getOption());
		} else {
			String reciver = messageSummaryBean.getReceicerBIC().substring(0, 7);

			if (messageSummaryBean.getMesgType().equals("103")) {
				String feild57A = "";
				if (textFieldsList.get(57).getOptionsValuesMap().containsKey("A")) {
					feild57A = getOptionValue(textFieldsList, 57, "A").substring(0, 7);
				}
				if (feild57A.isEmpty()) {
					// myRole >> Indmedit
					calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
				} else {
					if (reciver.equals(feild57A)) {
						// MyRole >> Benifashary
						calculatedMeasuresBean.setMyRole(Role.BENEFICIARY.getOption());
					} else {
						// myRole >> Indmedit
						calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
					}
				}
			} else {
				String feild58A = "";
				if (textFieldsList.get(58).getOptionsValuesMap().containsKey("A")) {
					feild58A = getOptionValue(textFieldsList, 58, "A").substring(0, 7);
				}
				if (feild58A.isEmpty()) {
					calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
				} else {
					if (reciver.equals(feild58A)) {
						// MyRole >> Benifashary
						calculatedMeasuresBean.setMyRole(Role.BENEFICIARY.getOption());
					} else {
						// myRole >> Indmedit
						calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
					}
				}
			}
		}

	}

	private void getMyRoleForInputMessaegs(CalculatedMeasuresBean calculatedMeasuresBean, Map<Integer, TextFieldBean> textFieldsList, MessageSummaryDTO messageSummaryBean) {
		boolean containsKey = textFieldsList.containsKey(52);
		if (!containsKey) {
			// My Role Orgin
			calculatedMeasuresBean.setMyRole(Role.ORIGINATOR.getOption());

		} else {
			String sender = messageSummaryBean.getSenderBIC().substring(0, 7);
			String feild52A = "";
			if (textFieldsList.get(52).getOptionsValuesMap().containsKey("A")) {
				feild52A = getOptionValue(textFieldsList, 52, "A").substring(0, 7);
			}
			if (feild52A.isEmpty()) {
				// myRole >> Indmedit
				calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
			} else {
				if (sender.equals(feild52A)) {
					// MyRole >> Org

					calculatedMeasuresBean.setMyRole(Role.ORIGINATOR.getOption());
				} else {
					// myRole >> Indmedit
					calculatedMeasuresBean.setMyRole(Role.INTERMEDIARY.getOption());
				}
			}
		}
	}

	private void getCounterPartRoleForInputMessaegs(CalculatedMeasuresBean calculatedMeasuresBean, String reciver, Map<Integer, TextFieldBean> textFieldsList, MessageSummaryDTO messageSummaryBean) {
		boolean containsKey = false;
		if (messageSummaryBean.getMesgType().equals("103")) {
			containsKey = textFieldsList.containsKey(57);
		} else {
			containsKey = textFieldsList.containsKey(58);
		}

		if (!containsKey) {
			// cp=Benifashiry
			calculatedMeasuresBean.setCounterPartRole(Role.BENEFICIARY.getOption());

		} else {
			if (messageSummaryBean.getMesgType().equals("103")) {
				if (textFieldsList.get(57).getOptionsValuesMap().containsKey("A")) {
					String optionValue = getOptionValue(textFieldsList, 57, "A");
					if (optionValue.equalsIgnoreCase(reciver)) {
						// Cp=Benifashiry

						calculatedMeasuresBean.setCounterPartRole(Role.BENEFICIARY.getOption());
					} else {
						// Cp=Indemid

						calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
					}
				} else {
					// Cp=Indemid

					calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
				}

			} else {
				if (textFieldsList.get(58).getOptionsValuesMap().containsKey("A")) {
					String optionValue = getOptionValue(textFieldsList, 58, "A");
					if (optionValue.equalsIgnoreCase(reciver)) {
						// Cp=Benifashiry
						calculatedMeasuresBean.setCounterPartRole(Role.BENEFICIARY.getOption());

					} else {
						// Cp=Indemid
						calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
					}
				} else {
					// Cp=Indemid
					calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
				}

			}
		}

	}

	private void getCounterPartRoleForOutoutMessaegs(CalculatedMeasuresBean calculatedMeasuresBean, String sender, Map<Integer, TextFieldBean> textFieldsList) {

		boolean containsKey = textFieldsList.containsKey(52);

		if (!containsKey) {
			// cp=Org
			calculatedMeasuresBean.setCounterPartRole(Role.ORIGINATOR.getOption());
		} else {
			if (textFieldsList.get(52).getOptionsValuesMap().containsKey("A")) {
				String optionValue = getOptionValue(textFieldsList, 52, "A");
				if (optionValue.equalsIgnoreCase(sender)) {
					// Cp=Org
					calculatedMeasuresBean.setCounterPartRole(Role.ORIGINATOR.getOption());
				} else {
					// Cp=Indemid
					calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
				}
			} else {
				// Cp=Indemid
				calculatedMeasuresBean.setCounterPartRole(Role.INTERMEDIARY.getOption());
			}

		}

	}

	public String getOptionValue(Map<Integer, TextFieldBean> textFieldsList, Integer key, String option) {
		String optionValue = null;
		int index = textFieldsList.get(key).getOptionsValuesMap().get(option).get(0).lastIndexOf("\r\n");
		if (index > 0) {
			optionValue = textFieldsList.get(key).getOptionsValuesMap().get(option).get(0).substring(index).trim();
		} else {
			optionValue = textFieldsList.get(key).getOptionsValuesMap().get(option).get(0).trim();
		}

		return optionValue;
	}

	public String get23EInstructionCode(Map<Integer, TextFieldBean> textFieldsList, Integer key, String option) {
		// 23E is a repetitive field.
		// so we need to join all instrcutionCode and spilt it by ,
		StringJoiner value = new StringJoiner(",");
		List<String> listOfValues = textFieldsList.get(key).getOptionsValuesMap().get(option);
		for (String optionValue : listOfValues) {
			int index = optionValue.indexOf('/');
			if (index != -1) {
				value.add(optionValue.substring(0, optionValue.indexOf('/')).trim());
			} else {
				value.add(optionValue);
			}
		}

		return value.toString();
	}

}