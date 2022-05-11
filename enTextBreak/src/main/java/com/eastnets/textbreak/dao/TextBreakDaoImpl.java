package com.eastnets.textbreak.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.eastnets.resultbean.TextBreakResultBean;
import com.eastnets.textbreak.bean.SourceData;
import com.eastnets.textbreak.bean.TextBreakConfig;
import com.eastnets.textbreak.dao.beans.User;
import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;
import com.eastnets.textbreak.enums.DecomposeStatus;
import com.eastnets.textbreak.procedure.CheckRoleProcedure;

@Service
public class TextBreakDaoImpl implements TeacBreakDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TextBreakConfig textBreakConfig;

	@Override
	public void fillTextField(List<TextField> textFields, Date xCreationDateTime) {
		String insertStatemnt = "";
		if (!textBreakConfig.getPartitioned()) {
			insertStatemnt = "INSERT INTO rtextfield (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,FIELD_CNT,FIELD_CODE,FIELD_CODE_ID,FIELD_OPTION,VALUE,VALUE_MEMO,SEQUENCE_ID,GROUP_IDX) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt = "INSERT INTO rtextfield (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,FIELD_CNT,FIELD_CODE,FIELD_CODE_ID,FIELD_OPTION,VALUE,VALUE_MEMO,SEQUENCE_ID,GROUP_IDX,X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		}

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				TextField textField = textFields.get(index);
				preparedStatement.setLong(1, textField.getId().getAid());
				preparedStatement.setLong(2, textField.getId().getTextSUmidl());
				preparedStatement.setLong(3, textField.getId().getTextSUmidh());
				preparedStatement.setLong(4, textField.getFieldCnt());
				preparedStatement.setLong(5, textField.getId().getFieldCode());
				preparedStatement.setLong(6, textField.getId().getFieldCodeId());
				preparedStatement.setString(7, textField.getFieldOption());
				preparedStatement.setString(8, textField.getValue());
				preparedStatement.setString(9, textField.getValueMemo());
				preparedStatement.setString(10, textField.getId().getSequenceId());
				preparedStatement.setLong(11, textField.getId().getGroupIdx());
				if (textBreakConfig.getPartitioned()) {
					preparedStatement.setTimestamp(12, new java.sql.Timestamp(textField.getMesgCreaDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return textFields.size();
			}
		});
	}

	@Override
	public void fillTextLoop(List<TextFieldLoop> textFieldLoopList, Date xCreationDateTime) {
		String insertStatemnt = "";
		if (!textBreakConfig.getPartitioned()) {
			insertStatemnt = "INSERT INTO rtextfieldloop (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,SEQUENCE_ID,GROUP_ID,GROUP_CNT,GROUP_IDX,PARENT_GROUP_IDX) VALUES (?,?,?,?,?,?,?,?)";
		} else {
			insertStatemnt = "INSERT INTO rtextfieldloop (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,SEQUENCE_ID,GROUP_ID,GROUP_CNT,GROUP_IDX,PARENT_GROUP_IDX,X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?,?,?)";

		}
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				TextFieldLoop textFieldLoop = textFieldLoopList.get(index);
				preparedStatement.setLong(1, textFieldLoop.getId().getAid());
				preparedStatement.setLong(2, textFieldLoop.getId().getTextSUmidl());
				preparedStatement.setLong(3, textFieldLoop.getId().getTextSUmidh());
				preparedStatement.setString(4, textFieldLoop.getId().getSequenceId());
				preparedStatement.setLong(5, textFieldLoop.getGroupId());
				preparedStatement.setLong(6, textFieldLoop.getGroupCnt());
				preparedStatement.setLong(7, textFieldLoop.getId().getGroupIdx());
				preparedStatement.setLong(8, textFieldLoop.getParentGroupIGdx());
				if (textBreakConfig.getPartitioned()) {
					preparedStatement.setTimestamp(9, new java.sql.Timestamp(textFieldLoop.getMesgCreaDateTime().getTime()));
				}
			}

			@Override
			public int getBatchSize() {
				return textFieldLoopList.size();
			}
		});
	}

	@Override
	public void fillSystemTextField(List<SystemTextField> systemTextFields, Date xCreationDateTime) {
		String insertStatemnt = "";
		if (!textBreakConfig.getPartitioned()) {
			insertStatemnt = "INSERT INTO rSystemTextField (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,FIELD_CNT,FIELD_CODE,VALUE) VALUES (?,?,?,?,?,?)";
		} else {

			insertStatemnt = "INSERT INTO rSystemTextField (AID,TEXT_S_UMIDL,TEXT_S_UMIDH,FIELD_CNT,FIELD_CODE,VALUE,X_CREA_DATE_TIME_MESG) VALUES (?,?,?,?,?,?,?)";
		}
		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				SystemTextField systemTextField = systemTextFields.get(index);
				preparedStatement.setLong(1, systemTextField.getId().getAid());
				preparedStatement.setLong(2, systemTextField.getId().getTextSUmidl());
				preparedStatement.setLong(3, systemTextField.getId().getTextSUmidh());
				preparedStatement.setLong(4, systemTextField.getId().getFieldCnt());
				preparedStatement.setLong(5, systemTextField.getFieldCode());
				preparedStatement.setString(6, systemTextField.getValue());
				if (textBreakConfig.getPartitioned()) {
					preparedStatement.setTimestamp(7, new java.sql.Timestamp(systemTextField.getMesgCreaDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return systemTextFields.size();
			}
		});

	}

	@Override
	public void updateParsinStatus(SourceData sourceData, Integer decopsedStatus) {
		Boolean partitioned = textBreakConfig.getPartitioned();
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(decopsedStatus);
		parameters.add(sourceData.getAid());
		parameters.add(sourceData.getMesgUmidl());
		parameters.add(sourceData.getMesgUmidh());

		String insertStatemnt = "update rtext set decomposed = ? where  aid = ? and text_s_umidl= ? and text_s_umidh = ? ";

		if (partitioned) {
			insertStatemnt = insertStatemnt + " and  x_crea_date_time_mesg = ?";
			parameters.add(sourceData.getMesgCreaDateTime());
		}

		jdbcTemplate.update(insertStatemnt, parameters.toArray());

	}

	@Override
	public User getUserProfileId(String username) {

		StringBuilder selectUsersQuery = new StringBuilder();
		selectUsersQuery.append("SELECT");
		selectUsersQuery.append(" sUser.UserName AS UserName");
		selectUsersQuery.append(" , sUser.FullUserName AS FullUserName");
		selectUsersQuery.append(" , sUser.GroupID AS GroupID");
		selectUsersQuery.append(" FROM sUser ");

		selectUsersQuery.append(" WHERE ");

		selectUsersQuery.append(" sUser.UserName NOT IN ('LSA_USER' , 'RSA_USER' ) ");
		selectUsersQuery.append("AND UPPER(sUser.UserName) =  upper(?)  ");
		selectUsersQuery.append("  ORDER BY UserName");

		List<Object> parameters = new ArrayList<>();
		parameters.add(username);

		List<User> query = jdbcTemplate.query(selectUsersQuery.toString(), parameters.toArray(), new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rownum) throws SQLException {
				User user = new User();
				user.setGroupId(rs.getLong("GroupID"));
				user.setUsername(rs.getString("UserName"));
				return user;
			}
		});
		return (query != null && !query.isEmpty()) ? query.get(0) : new User();

	}

	@Override
	public List<String> getProfileRoles(Long profileID) {
		String selectQuery = "SELECT role_name FROM sGrantedRoles where group_id = " + profileID;
		List<String> roles = jdbcTemplate.query(selectQuery, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("role_name").toUpperCase();
			}
		});

		return roles;
	}

	@Override
	public void updateFinMessageStatus(List<TextField> textFields, Integer decopsedStatus) {
		Boolean partitioned = textBreakConfig.getPartitioned();
		boolean sucssesedDecomposedStatus = (DecomposeStatus.DECOMPOSED.getStatus() == decopsedStatus) ? true : false;
		String decomposedOnlySql = "";
		if (sucssesedDecomposedStatus && textBreakConfig.isDecomposedOnly()) {
			decomposedOnlySql = " , TEXT_DATA_BLOCK =null ";
		}

		String insertStatemnt = "update rtext set " + "decomposed = ? " + decomposedOnlySql + " where  aid = ? and text_s_umidl= ? and text_s_umidh = ? ";

		if (partitioned) {
			insertStatemnt = insertStatemnt + " and  x_crea_date_time_mesg = ?";
		}

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				TextField textField = textFields.get(index);
				preparedStatement.setInt(1, decopsedStatus);
				preparedStatement.setLong(2, textField.getId().getAid());
				preparedStatement.setLong(3, textField.getId().getTextSUmidl());
				preparedStatement.setLong(4, textField.getId().getTextSUmidh());
				if (partitioned) {
					preparedStatement.setDate(5, new java.sql.Date(textField.getMesgCreaDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return textFields.size();
			}
		});

	}

	@Override
	public void updateSysMessageStatus(List<SystemTextField> systemTextFields, Integer decopsedStatus) {
		Boolean partitioned = textBreakConfig.getPartitioned();

		boolean sucssesedDecomposedStatus = (DecomposeStatus.DECOMPOSED.getStatus() == decopsedStatus) ? true : false;
		String decomposedOnlySql = "";
		if (textBreakConfig.isRemoveFromRtext() && sucssesedDecomposedStatus && textBreakConfig.isDecomposedOnly()) {
			decomposedOnlySql = " , TEXT_DATA_BLOCK =null ";
		}

		String insertStatemnt = "update rtext set decomposed = ?  " + decomposedOnlySql + " where  aid = ? and text_s_umidl= ? and text_s_umidh = ? ";

		if (partitioned) {
			insertStatemnt = insertStatemnt + " and  x_crea_date_time_mesg = ?";
		}

		jdbcTemplate.batchUpdate(insertStatemnt, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
				SystemTextField systemTextField = systemTextFields.get(index);
				preparedStatement.setInt(1, decopsedStatus);
				preparedStatement.setLong(2, systemTextField.getId().getAid());
				preparedStatement.setLong(3, systemTextField.getId().getTextSUmidl());
				preparedStatement.setLong(4, systemTextField.getId().getTextSUmidh());
				if (partitioned) {
					preparedStatement.setDate(5, new java.sql.Date(systemTextField.getMesgCreaDateTime().getTime()));
				}

			}

			@Override
			public int getBatchSize() {
				return systemTextFields.size();
			}
		});

	}

	@Override
	public void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean sCheckRoals() {
		CheckRoleProcedure checkRoleProcedure = new CheckRoleProcedure(jdbcTemplate);
		Integer status = checkRoleProcedure.execute("SIDE_LOADER");
		if (status != 1)
			return false;

		return true;
	}

	@Override
	public List<TextBreakResultBean> getAllMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TextBreakResultBean> getConfiguredMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TextBreakResultBean> getRecoveryMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, Integer messageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public TextBreakConfig getTextBreakConfig() {
		return textBreakConfig;
	}

	public void setTextBreakConfig(TextBreakConfig textBreakConfig) {
		this.textBreakConfig = textBreakConfig;
	}

	@Override
	public List<TextBreakResultBean> getSelectedOnlineTextBreakMessage(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
