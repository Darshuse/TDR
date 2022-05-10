/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */
package com.eastnets.resilience.xmldump.db.dao.impl;

import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.eastnets.resilience.textparser.bean.ParsedField;
import com.eastnets.resilience.textparser.bean.ParsedLoop;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.xmldump.GlobalConfiguration;
import com.eastnets.resilience.xmldump.db.DatabaseType;
import com.eastnets.resilience.xmldump.db.dao.ParsedTextDAO;
import com.eastnets.resilience.xmldump.logging.StatisticsObserver;
import com.eastnets.resilience.xmldump.utils.GlobalSettings;
import com.eastnets.resilience.xsd.messaging.Message;

/**
 * Parsed message text importer
 * 
 * @author EHakawati
 * 
 */
public class ParsedTextDAOImpl extends ParsedTextDAO {

	// the edge between value and value_memo
	private static final int MEMO_SIZE = 1750;

	private Connection connection;

	/**
	 * Constructor
	 * 
	 * @param Connection
	 * @param restoreSet
	 * @throws SQLException
	 */
	public ParsedTextDAOImpl(Connection conn, int restoreSet) throws SQLException {
		// register observer
		addObserver(new StatisticsObserver());
		setRestoreSet(restoreSet);
		this.connection = conn;
	}

	/**
	 * Add parsed text fields
	 * 
	 * @param Message
	 * @param ParsedMessage
	 * @throws SQLException
	 */
	@Override
	public void addTextFields(Message message, ParsedMessage parsedMessage) throws SQLException {

		// initialize prepared statements (value and value memo)
		CallableStatement processTextField;
		CallableStatement processTextFieldMemo;

		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			processTextField = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTFIELD_PARTITIONED") + " }");
			processTextFieldMemo = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTFIELD_MEMO_PARTITIONED") + " }");
		} else {
			processTextField = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTFIELD") + " }");
			processTextFieldMemo = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTFIELD_MEMO") + " }");
		}

		List<ParsedField> parsedFields = parsedMessage.getParsedFields();

		int fieldCount = 1;
		for (ParsedField field : parsedFields) {

			if (field.getMaxTextSize() >= MEMO_SIZE) {
				processTextFieldMemo(processTextFieldMemo, message, field, fieldCount);
			} else {
				processTextField(processTextField, message, field, fieldCount);
			}

			fieldCount++;
			setChanged();
			notifyObservers(StatisticsObserver.Type.TEXTFIELD);

		}

		// resetTextBlock(message);

		processTextField.close();
		processTextFieldMemo.close();
	}

	/**
	 * Execute ldProcessTextField
	 * 
	 * @param processTextField
	 * @param message
	 * @param field
	 * @param fieldCount
	 * @throws SQLException
	 */

	private void processTextField(CallableStatement processTextField, Message message, ParsedField field, int fieldCount)
			throws SQLException {

		int aid = GlobalConfiguration.getInstance().getAllianceId();
		int umidl = message.getUmidL();
		int umidh = message.getUmidH();

		int index = 1;

		processTextField.clearParameters();

		// v_request_type
		processTextField.setInt(index++, 0);// Dummy value, not used by this
											// procedure.
		// v_mesg_type
		processTextField.setString(index++, message.getType());
		// v_aid
		processTextField.setInt(index++, aid);
		// v_text_s_umidl
		processTextField.setInt(index++, umidl);
		// v_text_s_umidh
		processTextField.setInt(index++, umidh);
		// v_field_cnt
		processTextField.setInt(index++, fieldCount++);
		// v_field_code

		if (field.getFieldCode() == null) {
			processTextField.setString(index++, "0");
		} else if (field.getFieldCode().length() == 3) {
			processTextField.setString(index++, field.getFieldCode().substring(0, 2));
		} else {
			processTextField.setString(index++, field.getFieldCode());
		}

		// v_field_code_id
		processTextField.setInt(index++, field.getCodeId());
		// v_field_option
		processTextField.setString(index++, field.getFieldOption());
		// v_sequence_id
		processTextField.setString(index++, field.getSequence() == null ? "0" : field.getSequence());
		// v_group_id -- not used
		processTextField.setInt(index++, 0);
		// v_group_cnt -- not used
		processTextField.setInt(index++, 0);
		// v_group_idx
		processTextField.setInt(index++, field.getGroupIdx());
		// v_parent_group_id -- not used
		processTextField.setInt(index++, 0);
		// v_parent_group_cnt -- not used
		processTextField.setInt(index++, 0);
		// v_parent_group_idx -- not used
		processTextField.setInt(index++, 0);
		// v_value
		processTextField.setString(index++, field.getValue());
		// RETSTATUS
		int outputIndex = index;
		processTextField.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

		if (GlobalConfiguration.getInstance().isPartitioned()) {
			// v_x_crea_date_time_mesg
			this.setTimestamp(processTextField, ++index, message.getCreationDate());
		}

		processTextField.executeUpdate();

	}

	/**
	 * Execute ldProcessTextFieldMemo
	 * 
	 * @param processTextFieldMemo
	 * @param message
	 * @param field
	 * @param fieldCount
	 * @throws SQLException
	 */
	private void processTextFieldMemo(CallableStatement processTextFieldMemo, Message message, ParsedField field,
			int fieldCount) throws SQLException {

		int aid = GlobalConfiguration.getInstance().getAllianceId();
		int umidl = message.getUmidL();
		int umidh = message.getUmidH();

		int index = 1;

		processTextFieldMemo.clearParameters();

		// v_request_type
		processTextFieldMemo.setInt(index++, 0);// Dummy value, not used by
												// this
		// procedure.
		// v_mesg_type
		processTextFieldMemo.setString(index++, message.getType());
		// v_aid
		processTextFieldMemo.setInt(index++, aid);
		// v_text_s_umidl
		processTextFieldMemo.setInt(index++, umidl);
		// v_text_s_umidh
		processTextFieldMemo.setInt(index++, umidh);
		// v_field_cnt
		processTextFieldMemo.setInt(index++, fieldCount++);
		// v_field_code

		if (field.getFieldCode() == null) {
			processTextFieldMemo.setString(index++, "0");
		} else if (field.getFieldCode().length() == 3) {
			processTextFieldMemo.setString(index++, field.getFieldCode().substring(0, 2));
		} else {
			processTextFieldMemo.setString(index++, field.getFieldCode());
		}

		// v_field_code_id
		processTextFieldMemo.setInt(index++, field.getCodeId());
		// v_field_option
		processTextFieldMemo.setString(index++, field.getFieldOption());
		// v_sequence_id
		processTextFieldMemo.setString(index++, field.getSequence() == null ? "0" : field.getSequence());
		// v_group_id -- not used
		processTextFieldMemo.setInt(index++, 0);
		// v_group_cnt -- not used
		processTextFieldMemo.setInt(index++, 0);
		// v_group_idx
		processTextFieldMemo.setInt(index++, field.getGroupIdx());
		// v_parent_group_id -- not used
		processTextFieldMemo.setInt(index++, 0);
		// v_parent_group_cnt -- not used
		processTextFieldMemo.setInt(index++, 0);
		// v_parent_group_idx -- not used
		processTextFieldMemo.setInt(index++, 0);

		// TODO : check If applicable in mssql
		// v_value
		int valueIndex = index++;
		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.MSSQL) {
			processTextFieldMemo.setString(valueIndex, field.getValue());
		} else {
			processTextFieldMemo.registerOutParameter(valueIndex, java.sql.Types.CLOB);
		}

		// RETSTATUS
		int outputIndex = index;
		processTextFieldMemo.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

		if (GlobalConfiguration.getInstance().isPartitioned()) {
			// v_x_crea_date_time_mesg
			this.setTimestamp(processTextFieldMemo, ++index, message.getCreationDate());
		}

		processTextFieldMemo.executeUpdate();

		if (GlobalConfiguration.getInstance().getDatabaseType() == DatabaseType.ORACLE && field.getValue() != null) {
			Clob clob = processTextFieldMemo.getClob(valueIndex);
			Writer writer = clob.setCharacterStream(0);
			try {
				writer.write(field.getValue());
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				// -- Do nothing
			}

		}
	}

	/**
	 * Execute ldProcessTextLoop
	 * 
	 * @param message
	 * @param parsedMessage
	 * @throws SQLException
	 */
	@Override
	public void addTextLoops(Message message, ParsedMessage parsedMessage) throws SQLException {

		CallableStatement processTextFieldLoop;

		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			processTextFieldLoop = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTLOOP_PARTITIONED") + " }");
		} else {
			processTextFieldLoop = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_TEXTLOOP") + " }");
		}

		int aid = GlobalConfiguration.getInstance().getAllianceId();
		int umidl = message.getUmidL();
		int umidh = message.getUmidH();

		List<ParsedLoop> parsedLoops = parsedMessage.getParsedLoopsSet();

		for (ParsedLoop loop : parsedLoops) {

			int index = 1;
			// 1- v_request_type -- not needed
			processTextFieldLoop.setInt(index++, 0);
			// v_aid
			processTextFieldLoop.setInt(index++, aid);
			// v_text_s_umidl
			processTextFieldLoop.setInt(index++, umidl);
			// v_text_s_umidh
			processTextFieldLoop.setInt(index++, umidh);
			// v_sequence_id
			processTextFieldLoop.setString(index++, loop.getSequence() == null ? "0" : loop.getSequence());
			// v_group_id
			processTextFieldLoop.setString(index++, loop.getGroupId());
			// v_group_cnt
			processTextFieldLoop.setInt(index++, loop.getGroupCnt());
			// v_group_idx
			processTextFieldLoop.setInt(index++, loop.getGroupIdx());
			// v_parent_group_id -- not used
			processTextFieldLoop.setInt(index++, 0);
			// v_parent_group_cnt -- not used
			processTextFieldLoop.setInt(index++, 0);
			// v_parent_group_idx -- not used
			processTextFieldLoop.setInt(index++, loop.getParentGroupIdx());
			// RETSTATUS
			int outputIndex = index;
			processTextFieldLoop.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

			if (GlobalConfiguration.getInstance().isPartitioned()) {
				// v_x_crea_date_time_mesg
				this.setTimestamp(processTextFieldLoop, ++index, message.getCreationDate());
			}
			processTextFieldLoop.executeUpdate();
			processTextFieldLoop.clearParameters();
			setChanged();
			notifyObservers(StatisticsObserver.Type.TEXTLOOP);

		}
		processTextFieldLoop.close();
	}

	/**
	 * Execute ldProcessSystemText
	 * 
	 * @param message
	 * @param systemTexts
	 * @throws SQLException
	 */
	@Override
	public void addSystemText(Message message, List<String[]> systemTexts) throws SQLException {
		CallableStatement systemText;

		// Load the right query (partitioning have 1 extra input)
		if (GlobalConfiguration.getInstance().isPartitioned()) {
			systemText = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_SYSTEM_TTEXT_PARTITIONED") + " }");
		} else {
			systemText = connection.prepareCall("{ call "
					+ ResourceBundle.getBundle("queries" + GlobalSettings.db_postfix ).getString("LD_PROCESS_SYSTEM_TTEXT") + " }");
		}

		int aid = GlobalConfiguration.getInstance().getAllianceId();
		int umidl = message.getUmidL();
		int umidh = message.getUmidH();

		int fieldCnt = 0;
		for (String[] line : systemTexts) {

			systemText.clearParameters();

			int index = 1;
			// v_request_type -- not needed
			systemText.setInt(index++, 0);
			// v_aid
			systemText.setInt(index++, aid);
			// v_text_s_umidl
			systemText.setInt(index++, umidl);
			// v_text_s_umidh
			systemText.setInt(index++, umidh);
			// v_field_cnt
			systemText.setInt(index++, fieldCnt++);

			// v_field_code
			systemText.setString(index++, line[0]);
			// v_value
			systemText.setString(index++, line[1]);

			// RETSTATUS
			int outputIndex = index;
			systemText.registerOutParameter(outputIndex, java.sql.Types.INTEGER);

			if (GlobalConfiguration.getInstance().isPartitioned()) {
				// v_x_crea_date_time_mesg
				this.setTimestamp(systemText, ++index, message.getCreationDate());
			}
			systemText.executeUpdate();

		}
		systemText.close();

		setChanged();
		notifyObservers(StatisticsObserver.Type.TEXTSYSTEM);
	}

}
