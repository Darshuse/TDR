package com.eastnets.enGpiLoader.DAO;

 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eastnets.domain.admin.LoaderConnection;
import com.eastnets.domain.viewer.DataSource;
import com.eastnets.domain.viewer.NotifierMessage;

 

public class LoaderOracleDAOImp extends LoaderDAOImp {


	@Override
	public void insertConfirmatioAttempt(NotifierMessage notifierMessage) {
		String insertString = null;
		if(notifierMessage.getDataSource().equals(DataSource.SAA)){
			insertString = "INSERT INTO ldGpiNotifersHistory (aid,mesg_s_umidl,mesg_s_umidh,confirmAttempts,Confirma_DATE) VALUES (?,?,?,?,?)"; 
			jdbcTemplate.update(insertString,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh(),1,new Date() );
			
		}else{
			insertString = "INSERT INTO ldGpiNotifersMQHistory (mq_mesg_id,confirmAttempts,Confirma_DATE) VALUES (?,?,?)"; 
			jdbcTemplate.update(insertString,notifierMessage.getMesgID(),1,new Date() );
		}

	}
	@Override
	public void updateConfirmatioAttempt(NotifierMessage notifierMessage) {
		if(notifierMessage.getDataSource().equals(DataSource.SAA)){
			String updateString = "UPDATE ldGpiNotifersHistory SET confirmAttempts = ? , Confirma_DATE = ?   WHERE aid = ? AND mesg_s_umidl = ? AND mesg_s_umidh = ?";
			jdbcTemplate.update(updateString, notifierMessage.getConfirmAtempt()+1,new Date() ,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh());
			
		}else{
			String updateString = "UPDATE ldGpiNotifersMQHistory SET confirmAttempts = ? , Confirma_DATE = ?   WHERE mq_mesg_id = ?";
			jdbcTemplate.update(updateString, notifierMessage.getConfirmAtempt()+1,new Date() ,notifierMessage.getMesgID());
		}

	}


	@Override
	public void insertMailAttempt(NotifierMessage notifierMessage) {
		String insertString = null;
		if(notifierMessage.getDataSource().equals(DataSource.SAA)){
			insertString = "INSERT INTO ldGpiNotifersHistory (aid,mesg_s_umidl,mesg_s_umidh,mailAttempts,Mail_DATE) VALUES (?,?,?,?,?)"; 
			jdbcTemplate.update(insertString,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh(),1,new Date());
			
		}else{
			insertString = "INSERT INTO ldGpiNotifersMQHistory (mq_mesg_id,mailAttempts,Mail_DATE) VALUES (?,?,?)"; 
			jdbcTemplate.update(insertString,notifierMessage.getMesgID(),1,new Date());
		}

	}
	@Override
	public void updateMailAttempt(NotifierMessage notifierMessage) {
		if(notifierMessage.getDataSource().equals(DataSource.SAA)){
			String updateString = "UPDATE ldGpiNotifersHistory SET mailAttempts = ? , Mail_DATE= ?   WHERE aid = ? AND mesg_s_umidl = ? AND mesg_s_umidh = ?";
			jdbcTemplate.update(updateString, notifierMessage.getMailAtempt()+1,new Date() ,notifierMessage.getAid(),notifierMessage.getMesgUmidl(),notifierMessage.getMesgUmidh());
			
		}else{
			String updateString = "UPDATE ldGpiNotifersMQHistory SET mailAttempts = ? , Mail_DATE= ?   WHERE mq_mesg_id = ?";
			jdbcTemplate.update(updateString, notifierMessage.getMailAtempt()+1,new Date() ,notifierMessage.getMesgID());
		}

	}

	
}
 