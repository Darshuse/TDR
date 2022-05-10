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
package com.eastnets.resilience.textparser.db.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.db.bean.FullField;
import com.eastnets.resilience.textparser.db.dao.FullFieldsDAO;
import com.eastnets.resilience.textparser.util.GlobalSettings;

/**
 * FullFieldsDAO generic implementation valid for both oracle and mssql
 * 
 * @author EHakawati
 * 
 */
public class FullFieldsDAOImpl implements FullFieldsDAO {

	@Override
	public List<FullField> getAllFields(int versionIdx, int typeIdx) throws SQLException {
		String sql = "SELECT " + " IDX,CODE,tag,patt_id,ENTRY_OPTION,SEQUENCE_ID FROM stxentryfield " + " WHERE (type_idx = ?)  " + "ORDER BY field_cnt";
		PreparedStatement st = Syntax.getDBSession().prepareStatement(sql);

		// bind variables
		st.setInt(1, typeIdx);

		// execute
		ResultSet rs = st.executeQuery();

		List<FullField> daosList = new ArrayList<FullField>();
		// get entries
		while (rs.next()) {

			FullField field = new FullField();
			// field.setFieldIdx(rs.getInt("field_idx"));
			field.setFieldTag(rs.getString("tag").replaceFirst("F", ""));
			field.setFieldPatternId(rs.getString("patt_id"));
			field.setFieldCode(rs.getInt("CODE"));
			field.setFieldOptionOptionChoice(rs.getString("ENTRY_OPTION"));
			field.setSequenceEntryId(rs.getString("SEQUENCE_ID"));
			field.setFieldIdx(rs.getInt("IDX"));
			// field.setFieldExpansion(rs.getString("field_expansion"));
			daosList.add(field);

		}

		st.close();
		rs.close();
		return daosList;
	}

	/**
	 * get the a list of SyntaxAllFieldsView
	 * 
	 * @param vesrion
	 * @param mesgType
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<FullField> getSyntaxAllFieldsViews(int versionIdx, int typeIdx) throws SQLException {

		PreparedStatement st = Syntax.getDBSession()
				.prepareStatement("SELECT " + "field_idx, field_cnt, field_entry_id, field_tag, field_patt_id, " + "field_expansion, field_is_optional, loop_id, loop_entry_id, "
						+ "loop_min_occ, loop_max_occ, parent_loop_entry_id, parent_loop_min_occ, " + "parent_loop_max_occ, sequ_entry_id, sequ_is_optional, sequ_expansion, "
						+ "fiopt_option_choice, fiopt_option_expansion, fiopt_field_tag, fiopt_patt_id, " + "fiopt_is_optional, altrn_entry_id, altrn_is_optional, altrn_option_choice, "
						+ "pats_prompt, pats_min_char, pats_max_char, pats_nb_rows, pats_row_separator, " + "pats_type, pats_default_value, pats_existence_condition, pats_is_visible, "
						+ "pats_is_optional, pats_is_editable, pats_is_verifiable, patt_id " + "FROM " + GlobalSettings.getSchemaNameWithDot() + "stxGetAllFieldsView " + "WHERE (version_idx = ? and type_idx = ?) " + "ORDER BY field_cnt, pats_id");

		// bind variables
		st.setInt(1, versionIdx);
		st.setInt(2, typeIdx);

		// execute
		ResultSet rs = st.executeQuery();

		List<FullField> daosList = new LinkedList<FullField>();
		// get entries
		while (rs.next()) {

			FullField field = new FullField();
			field.setFieldIdx(rs.getInt("field_idx"));
			field.setFieldCnt(rs.getInt("field_cnt"));
			field.setFieldEntryId(rs.getString("field_entry_id"));
			field.setFieldTag(rs.getString("field_tag"));
			field.setFieldPatternId(rs.getString("field_patt_id"));
			field.setFieldExpansion(rs.getString("field_expansion"));
			field.setFieldIsOptional(rs.getBoolean("field_is_optional"));
			field.setLoopId(rs.getInt("loop_id"));
			field.setLoopEntryId(rs.getString("loop_entry_id"));
			field.setLoopMinOcc(rs.getInt("loop_min_occ"));
			field.setLoopMaxOcc(rs.getInt("loop_max_occ"));
			String parentId = rs.getString("parent_loop_entry_id");
			if ((parentId == null || parentId.equals("")) && rs.getInt("loop_id") >= 10) {
				if (rs.getString("loop_entry_id") != null && !"".equals(rs.getString("loop_entry_id"))) {
					int dotIndex = rs.getString("loop_entry_id").indexOf(".");
					parentId = rs.getString("loop_entry_id").substring(dotIndex + 1);
				}
			}
			field.setParentLoopEntryId(parentId);
			field.setParentLoopMinOcc(rs.getInt("parent_loop_min_occ"));
			field.setParentLoopMaxOcc(rs.getInt("parent_loop_max_occ"));
			field.setSequenceEntryId(rs.getString("sequ_entry_id"));
			field.setSequenceIsOptional(rs.getBoolean("sequ_is_optional"));
			field.setSequenceExpansion(rs.getString("sequ_expansion"));
			field.setFieldOptionOptionChoice(rs.getString("fiopt_option_choice"));
			field.setFieldOptionExpansion(rs.getString("fiopt_option_expansion"));
			field.setFieldOptionFieldTag(rs.getString("fiopt_field_tag"));
			field.setFieldOptionPatternId(rs.getString("fiopt_patt_id"));
			field.setFieldOptionIsOptional(rs.getBoolean("fiopt_is_optional"));
			field.setAlternativeEntryId(rs.getString("altrn_entry_id"));
			field.setAlternativeIsOptional(rs.getBoolean("altrn_is_optional"));
			field.setAlternativeOptionChoice(rs.getString("fiopt_option_choice"));
			field.setPatternPrompt(rs.getString("pats_prompt"));
			field.setPatternMinChar(rs.getInt("pats_min_char"));
			field.setPatternMaxChar(rs.getInt("pats_max_char"));
			field.setPatternNbNows(rs.getInt("pats_nb_rows"));
			field.setPatternRowSeparator(rs.getString("pats_row_separator"));
			field.setPatternType(rs.getString("pats_type"));
			field.setPatternDefaultValue(rs.getString("pats_default_value"));
			field.setPatternExistenceCondition(rs.getString("pats_existence_condition"));
			field.setPatternIsVisible(rs.getBoolean("pats_is_visible"));
			field.setPatternIsOptional(rs.getBoolean("pats_is_optional"));
			field.setPatternIsEditable(rs.getBoolean("pats_is_editable"));
			field.setPatternIsVerifiable(rs.getBoolean("pats_is_verifiable"));
			field.setPatternId(rs.getString("patt_id"));

			daosList.add(field);

		}

		st.close();
		rs.close();
		return daosList;
	}
}
