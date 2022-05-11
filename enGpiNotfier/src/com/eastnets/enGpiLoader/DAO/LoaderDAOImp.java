package com.eastnets.enGpiLoader.DAO;



import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.eastnets.config.DBType;
import com.eastnets.dao.DAOBaseImp;
import com.eastnets.dao.common.DBPortabilityHandler;
import com.eastnets.domain.admin.ApprovalStatus;
import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.admin.Profile;
import com.eastnets.domain.admin.User;
import com.eastnets.domain.monitoring.UpdatedMessage;
import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;
import com.eastnets.enGpiLoader.Beans.GbiHistoryBean;
import com.eastnets.enGpiLoader.utility.GpiDirectory;
import com.eastnets.enGpiLoader.utility.GpiNotifersHistory;
import com.eastnets.enGpiLoader.utility.MessageKey;
import com.eastnets.enGpiLoader.utility.XMLNotifierConfigHelper; 

public abstract class LoaderDAOImp extends DAOBaseImp implements LoaderDAO {




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
	public Map<String, GbiHistoryBean> getHistoryMap( DataSource dataSource,XMLNotifierConfigHelper   notifierConfigHelper) { 
		String queryString ="";
		Map<String, GbiHistoryBean> historyMap=new HashMap<>();
		if(dataSource.equals(DataSource.MQ)){
			queryString = "select * from ldGpiNotifersMQHistory where (mailAttempts < ?  or  mailAttempts is null ) or (confirmAttempts < ?  or confirmAttempts is null )";	
		}else if(dataSource.equals(DataSource.EXT_DB)){
			queryString = "select * from ldGpiNotifersHistory where mq_mesg_id = ?";	
		}  
		List<GbiHistoryBean> historyMesgList = jdbcTemplate.query(queryString, new Object[] {notifierConfigHelper.getMailAttempts(),notifierConfigHelper.getConfirmAttempts()},
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
						gbiHistoryBean.setMesgSeq(rs.getString("mq_mesg_id"));

						return gbiHistoryBean;
					}

				});

		for(GbiHistoryBean gpiHistoryBean:historyMesgList){
			historyMap.put(gpiHistoryBean.getMesgSeq(), gpiHistoryBean);
		}
		
		return historyMap;
	}



}
