package com.eastnets.enGpiNotifer.DAO;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.eastnets.dao.DAOBaseImp;
import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiNotifer.Beans.GbiHistoryBean;
import com.eastnets.enGpiNotifer.utility.GpiDirectory;
import com.eastnets.enGpiNotifer.utility.XMLNotifierConfigHelper; 
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
public abstract class LoaderDAOImp extends DAOBaseImp implements LoaderDAO {

	private NamedParameterJdbcTemplate namedJdbcTemplate; 


	@Override
	public Map<String, GpiDirectory> getGpiDirectory() {
		Map<String, GpiDirectory> gpiDirectoryMap=new HashMap<String, GpiDirectory>();
		String gpiDirectoryQuery = "SELECT * FROM rGPIDirectory";
		List<GpiDirectory> gpiDirectories = (List<GpiDirectory>) jdbcTemplate.query(gpiDirectoryQuery, new RowMapper<GpiDirectory>() {
			public GpiDirectory mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				GpiDirectory gpiDirectory = new GpiDirectory(); 
				gpiDirectory.setCutOfTime(resultSet.getString("ATTRIBUTE_5"));
				gpiDirectory.setCur(resultSet.getString("ATTRIBUTE_4"));
				gpiDirectory.setBic(resultSet.getString("PARTICIPANT_ID"));
				return gpiDirectory;
			}
		});


		for(GpiDirectory  directory:gpiDirectories){ 
			gpiDirectoryMap.put(directory.getBic()+directory.getCur(), directory); 
		}

		return gpiDirectoryMap;
	} 

	@Override
	public void getGpiNotifersHistory(NotifierMessage notifierMessage) {
		// TODO Auto-generated method stub

	}


	@Override
	public void insertIntoLdGpiNotifersHistory(NotifierMessage notifierMessage) {
		String insertString = null;
		insertString = "INSERT INTO ldGpiNotifersHistory (aid,mesg_s_umidl,mesg_s_umidh,confirmAttempts,mailAttempts) VALUES (?,?,?,?,?)"; 
		jdbcTemplate.update(insertString,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh(),1,1 );

	}


	@Override
	public void updateLdGpiNotifersHistory(NotifierMessage notifierMessage) {
		String updateString = "UPDATE ldGpiNotifersHistory SET confirmAttempts = ? , mailAttempts = ? WHERE aid = ? AND mesg_s_umidl = ? AND mesg_s_umidh = ?";
		jdbcTemplate.update(updateString, notifierMessage.getConfirmAtempt()+1,notifierMessage.getMailAtempt()+1,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh());

	}



	@Override
	public GbiHistoryBean getmessageFromHistory(String id, DataSource dataSource) {
		String queryString ="";
		if(dataSource.equals(DataSource.MQ)){
			queryString = "select * from ldGpiNotifersHistory where mq_mesg_id = ?";	
		}else if(dataSource.equals(DataSource.EXT_DB)){
			queryString = "select * from ldGpiNotifersHistory where mq_mesg_id = ?";	
		} 

		GbiHistoryBean  gbiHistoryBean = (GbiHistoryBean) jdbcTemplate.queryForObject(queryString,
				new Object[] {id}, 
				new RowMapper<GbiHistoryBean>() {
			public GbiHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {

				GbiHistoryBean  gbiHistoryBean = new GbiHistoryBean();
				if (rs.getTimestamp("Mail_DATE") != null) {
					gbiHistoryBean.setMailAttemptsDate(new java.sql.Date(rs.getTimestamp("Mail_DATE").getTime()));
				}

				if (rs.getTimestamp("Confirma_DATE") != null) {
					gbiHistoryBean
					.setConfirmAttemptsDate(new java.sql.Date(rs.getTimestamp("Confirma_DATE").getTime()));
				}

				gbiHistoryBean.setMailAtempt(rs.getInt("mailAttempts"));
				gbiHistoryBean.setConfirmAtempt(rs.getInt("confirmAttempts"));

				return gbiHistoryBean;
			}
		});
		return gbiHistoryBean;
	}

	@Override
	public Map<String, GbiHistoryBean> getHistoryMap( DataSource dataSource,XMLNotifierConfigHelper   notifierConfigHelper,Set<String> mesgKeySet) { 
		String queryString ="";

		if(mesgKeySet.isEmpty()){
			return null;
		}
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", mesgKeySet); 
		if(dataSource.equals(DataSource.MQ)){
			queryString = "select * from ldGpiNotifersMQHistory WHERE mq_mesg_id IN (:ids)";	
		}else if(dataSource.equals(DataSource.EXT_DB)){
			queryString = "select * from ldGpiNotifersDBViewHistory WHERE dbView_mesg_id IN (:ids)";	
		} 
		List<GbiHistoryBean> historyMesgList = namedJdbcTemplate.query(queryString,parameters,
				new RowMapper<GbiHistoryBean>() {
			@Override
			public GbiHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				GbiHistoryBean  gbiHistoryBean = new GbiHistoryBean();
				if (rs.getTimestamp("Mail_DATE") != null) {
					gbiHistoryBean.setMailAttemptsDate(new java.sql.Date(rs.getTimestamp("Mail_DATE").getTime()));
				}

				if (rs.getTimestamp("Confirma_DATE") != null) {
					gbiHistoryBean
					.setConfirmAttemptsDate(new java.sql.Date(rs.getTimestamp("Confirma_DATE").getTime()));
				}  
				gbiHistoryBean.setMailAtempt(rs.getInt("mailAttempts"));
				gbiHistoryBean.setConfirmAtempt(rs.getInt("confirmAttempts"));
				if(dataSource.equals(DataSource.MQ)){ 
					gbiHistoryBean.setMesgSeq(rs.getString("mq_mesg_id"));
				}else if(dataSource.equals(DataSource.EXT_DB)){
					gbiHistoryBean.setMesgSeq(rs.getString("dbView_mesg_id"));
				}  

				return gbiHistoryBean;
			}

		});

		Map<String, GbiHistoryBean> historyMap=historyMesgList.stream().collect(Collectors.toMap(GbiHistoryBean :: getMesgSeq , gbiHistoryBean -> gbiHistoryBean)); 
		return historyMap;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}



}
