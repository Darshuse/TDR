package com.eastnets.textbreak.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.eastnets.resultbean.TextBreakResultBean;
import com.eastnets.textbreak.dao.beans.LdErrors;

@Service
public class TextBreakSqlDaoImpl extends TextBreakDaoImpl {

	@Override
	public void insertLdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2) {
		LdErrors ldErrors = new LdErrors(errName, errorDate, errorEvel, errorModule, errorMesage1, errorMessage2);
		String insertStatment = "INSERT INTO dbo.ldErrors(ErrExeName,Errtime,Errlevel,Errmodule,ErrMsg1,ErrMsg2) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(insertStatment, ldErrors.getErrName(), LocalDateTime.now(), ldErrors.getErrorEvel(), ldErrors.getErrorModule(), ldErrors.getErrorMesage1(), ldErrors.getErrorMessage2());

	}

	@Override
	public List<TextBreakResultBean> getAllMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(messageNumber);
		if (onLineDecoomposed) {
			parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		}
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(toDate));
		parameters.add(mesgFrmtName);
		parameters.add(aid);

		String selectQuery = " SELECT Top (?) *  " + " FROM rmesg m INNER JOIN rtext t ON m.mesg_s_umidh = t.text_s_umidh  AND m.mesg_s_umidl = t.text_s_umidl AND m.aid = t.aid  "
				+ (isPart ? " AND m.mesg_crea_date_time = t.x_crea_date_time_mesg " : "   ") + "" + " WHERE  " + (onLineDecoomposed ? " m.LAST_UPDATE >=  ?  AND   " : " ") + " m.mesg_crea_date_time BETWEEN ?  AND ? " + "   AND m.mesg_frmt_name = ? "
				+ "  AND t.text_data_block IS NOT NULL " + "    AND t.decomposed = 0 " + (textBreakConfig.isExcludeSystemMessages() ? " AND m.mesg_type NOT LIKE '0%' " : " AND m.mesg_type NOT IN (  '05',  '05 ' ) ") + "     AND m.aid = ? "
				+ " ORDER BY     m.mesg_crea_date_time ASC  ";

		List<TextBreakResultBean> messages = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<TextBreakResultBean>() {
			@Override
			public TextBreakResultBean mapRow(ResultSet rs, int arg1) throws SQLException {
				TextBreakResultBean message = new TextBreakResultBean();
				message.setAid(rs.getInt("aid"));
				message.setMesgCreaDateTime(rs.getTimestamp("mesg_crea_date_time"));
				message.setUmidh(rs.getInt("mesg_s_umidh"));
				message.setUmidl(rs.getInt("mesg_s_umidl"));
				message.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));
				message.setMesgType(rs.getString("mesg_type"));
				message.setTextDataBlock(rs.getString("text_data_block"));
				return message;
			}
		});
		return messages;
	}

	@Override
	public List<TextBreakResultBean> getConfiguredMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(messageNumber);
		if (onLineDecoomposed) {
			parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		}
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(toDate));
		parameters.add(mesgFrmtName);
		parameters.add(aid);

		String selectQuery = " SELECT Top (?) *  " + " FROM rmesg m INNER JOIN rtext t ON m.mesg_s_umidh = t.text_s_umidh  AND m.mesg_s_umidl = t.text_s_umidl AND m.aid = t.aid  "
				+ (isPart ? " AND m.mesg_crea_date_time = t.x_crea_date_time_mesg " : "   ") + " " + "  , ldParseMsgType f , LDGLOBALSETTINGS  e  "
				+ " WHERE   (((e.parse_textblock = 2 ) and  (f.MESG_TYPE = m.MESG_TYPE )) or e.parse_textblock = 1 )  and  " + (onLineDecoomposed ? " m.LAST_UPDATE >=  ?  AND   " : " ") + " m.mesg_crea_date_time BETWEEN ?  AND ? "
				+ "   AND m.mesg_frmt_name = ? " + "  AND t.text_data_block IS NOT NULL " + "    AND t.decomposed = 0 " + (textBreakConfig.isExcludeSystemMessages() ? " AND m.mesg_type NOT LIKE '0%' " : " AND m.mesg_type NOT IN (  '05',  '05 ' ) ")
				+ "     AND m.aid = ? " + " ORDER BY     m.mesg_crea_date_time ASC  ";

		List<TextBreakResultBean> messages = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<TextBreakResultBean>() {
			@Override
			public TextBreakResultBean mapRow(ResultSet rs, int arg1) throws SQLException {
				TextBreakResultBean message = new TextBreakResultBean();
				message.setAid(rs.getInt("aid"));
				message.setMesgCreaDateTime(rs.getTimestamp("mesg_crea_date_time"));
				message.setUmidh(rs.getInt("mesg_s_umidh"));
				message.setUmidl(rs.getInt("mesg_s_umidl"));
				message.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));
				message.setMesgType(rs.getString("mesg_type"));
				message.setTextDataBlock(rs.getString("text_data_block"));
				return message;
			}
		});
		return messages;
	}

	@Override
	public List<TextBreakResultBean> getRecoveryMessages(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, Integer messageNumber) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(messageNumber);
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(toDate));
		parameters.add(mesgFrmtName);
		parameters.add(aid);

		String selectQuery = " SELECT Top (?) *  " + " FROM rmesg m INNER JOIN rtext t ON m.mesg_s_umidh = t.text_s_umidh  AND m.mesg_s_umidl = t.text_s_umidl AND m.aid = t.aid  "
				+ (isPart ? " AND m.mesg_crea_date_time = t.x_crea_date_time_mesg " : "   ") + "" + " WHERE  " + " m.mesg_crea_date_time BETWEEN ?  AND ? " + "   AND m.mesg_frmt_name = ? " + "  AND t.text_data_block IS NOT NULL "
				+ "    AND t.decomposed in (2,3) " + (textBreakConfig.isExcludeSystemMessages() ? " AND m.mesg_type NOT LIKE '0%' " : " AND m.mesg_type NOT IN (  '05',  '05 ' ) ") + "     AND m.aid = ? " + " ORDER BY     m.mesg_crea_date_time ASC  ";

		List<TextBreakResultBean> messages = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<TextBreakResultBean>() {
			@Override
			public TextBreakResultBean mapRow(ResultSet rs, int arg1) throws SQLException {
				TextBreakResultBean message = new TextBreakResultBean();
				message.setAid(rs.getInt("aid"));
				message.setMesgCreaDateTime(rs.getTimestamp("mesg_crea_date_time"));
				message.setUmidh(rs.getInt("mesg_s_umidh"));
				message.setUmidl(rs.getInt("mesg_s_umidl"));
				message.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));
				message.setMesgType(rs.getString("mesg_type"));
				message.setTextDataBlock(rs.getString("text_data_block"));
				return message;
			}
		});
		return messages;
	}

	@Override
	public List<TextBreakResultBean> getSelectedOnlineTextBreakMessage(java.util.Date fromDate, java.util.Date toDate, String mesgFrmtName, int aid, boolean isPart, boolean onLineDecoomposed, Integer messageNumber) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(messageNumber);
		parameters.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(fromDate));
		parameters.add(mesgFrmtName);
		parameters.add(aid);

		String selectQuery = " SELECT Top (?) *  " + " FROM rmesg m INNER JOIN rtext t ON m.mesg_s_umidh = t.text_s_umidh  AND m.mesg_s_umidl = t.text_s_umidl AND m.aid = t.aid  "
				+ (isPart ? " AND m.mesg_crea_date_time = t.x_crea_date_time_mesg " : "   ") + " " + "   INNER JOIN   ldparsemsgtype   f ON m.mesg_type = f.mesg_type , LDGLOBALSETTINGS  e  "
				+ " WHERE   (((e.parse_textblock = 2 ) and  (f.MESG_TYPE = m.MESG_TYPE )) or e.parse_textblock = 1 )  and  " + (onLineDecoomposed ? " m.LAST_UPDATE >=  ?  AND   " : " ") + "  " + "    m.mesg_frmt_name = ? "
				+ "  AND t.text_data_block IS NOT NULL " + "    AND t.decomposed = 0 " + (textBreakConfig.isExcludeSystemMessages() ? " AND m.mesg_type NOT LIKE '0%' " : " AND m.mesg_type NOT IN (  '05',  '05 ' ) ") + "     AND m.aid = ? "
				+ " ORDER BY     m.mesg_crea_date_time ASC  ";

		List<TextBreakResultBean> messages = jdbcTemplate.query(selectQuery, parameters.toArray(), new RowMapper<TextBreakResultBean>() {
			@Override
			public TextBreakResultBean mapRow(ResultSet rs, int arg1) throws SQLException {
				TextBreakResultBean message = new TextBreakResultBean();
				message.setAid(rs.getInt("aid"));
				message.setMesgCreaDateTime(rs.getTimestamp("mesg_crea_date_time"));
				message.setUmidh(rs.getInt("mesg_s_umidh"));
				message.setUmidl(rs.getInt("mesg_s_umidl"));
				message.setMesgSyntaxTableVer(rs.getString("mesg_syntax_table_ver"));
				message.setMesgType(rs.getString("mesg_type"));
				message.setTextDataBlock(rs.getString("text_data_block"));
				return message;
			}
		});
		return messages;
	}

}
