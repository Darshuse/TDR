package com.eastnets.enGpiLoader.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map; 
import com.eastnets.enGpiLoader.Beans.DBViewBean;
import com.eastnets.enGpiLoader.utility.DataSourceParser;

public class ExternalDAO {
	
	private Connection conn;
	public ExternalDAO(Connection conn) {
		this.conn=conn; 
	}
 
	public Map<String,DBViewBean> getPendingExternalPendingMsg(DataSourceParser dataSourceParser) throws SQLException {
		final Map<String,DBViewBean> mesgMap=new HashMap<String,DBViewBean>();  
		PreparedStatement stat = conn.prepareStatement("select * from viewTest");
		ResultSet rs = stat.executeQuery();
		while (rs.next()) {
			DBViewBean  dbViewBean =new DBViewBean();
			dbViewBean.setSeq(rs.getString("seq"));
			dbViewBean.setMesgCreaDate(new Date(rs.getTimestamp("creation_DATE").getTime()));
			dbViewBean.setMesgText(rs.getString("mesg_Text")); 
			mesgMap.put(dbViewBean.getSeq(), dbViewBean);
		}
		return mesgMap;
	}
	
	
	
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
