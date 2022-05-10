package com.eastnets.enGpiNotifer.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.eastnets.enGpiNotifer.Beans.DBViewBean;
import com.eastnets.enGpiNotifer.utility.DataSourceParser;
import com.eastnets.enGpiNotifer.utility.XMLDataSouceHelper;

public class ExternalDAO {

	private Connection conn;

	public ExternalDAO(Connection conn) {
		this.conn = conn;
	}

	public Map<String, DBViewBean> getPendingExternalPendingMsg(String dbType, String querey, DataSourceParser dataSourceParser) throws SQLException {
		XMLDataSouceHelper dataSouceHelper = dataSourceParser.getXmlDataSource();
		String mesgTextColumnName = dataSouceHelper.getMsgTextColumn();
		String mesgIDColumnName = dataSouceHelper.getMsgIDCloumn();
		String mesgCreatDataColumnName = dataSouceHelper.getMsgCreationDataColumn();
		// String msgQueueNameColumn=dataSouceHelper.getMsgQueueNameColumn();
		final Map<String, DBViewBean> mesgMap = new HashMap<String, DBViewBean>();
		PreparedStatement stat = conn.prepareStatement(querey);
		ResultSet rs = stat.executeQuery();
		while (rs.next()) {
			DBViewBean dbViewBean = new DBViewBean();
			dbViewBean.setSeq(rs.getBigDecimal(mesgIDColumnName));
			dbViewBean.setMesgCreaDate(new Date(rs.getTimestamp(mesgCreatDataColumnName).getTime()));
			dbViewBean.setMesgText(rs.getString(mesgTextColumnName));
			// dbViewBean.setQueueName(rs.getString(msgQueueNameColumn));
			mesgMap.put(dbViewBean.getSeq().toString(), dbViewBean);
		}
		conn.close();
		return mesgMap;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
