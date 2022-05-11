package com.eastnets.resilience.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastnets.resilience.DatabaseQueries;
import com.eastnets.resilience.EnXmlDumpConfig;

public class DatabaseQueriesMsSql extends DatabaseQueries {
	
	private final String tempTableCreate = "CREATE TABLE " + getTableName() + " ( t_aid tinyint, t_umidl int, t_umidh int )";

	public DatabaseQueriesMsSql(Connection connection ){
		super( connection );
	}
	
	@Override
	public String getTableName() {
		return "#TempSearchResult";
	}

	@Override
	public String getShema() {
		return "dbo";
	}
	
	
	@Override
	public String formatDate(  Date date )  {
		if ( date == null ){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "CONVERT(datetime, '" + sdf.format(date) + "', 120)";		
	}

	@Override
	public void initTempTable( EnXmlDumpConfig config ) throws SQLException {
		connection.createStatement().execute( tempTableCreate );
		String selectStatement= tempTableInsert +  getWhereStatement( config.getAid(), config.getFromDate(), config.getToDate(), config.isSwiftNet(), config.isAcked() ) ;
		connection.createStatement().execute( selectStatement );
	}

}
