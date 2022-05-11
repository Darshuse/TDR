package com.eastnets.resilience.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastnets.resilience.DatabaseQueries;
import com.eastnets.resilience.EnXmlDumpConfig;

public class DatabaseQueriesOrcl extends DatabaseQueries {
	private final String tempTableCreate = "CREATE GLOBAL TEMPORARY TABLE " + getTableName() + " ( t_aid NUMBER(3), t_umidl NUMBER(10), t_umidh NUMBER(10) ) ON COMMIT PRESERVE ROWS ";
	//private final String tempTableCreate = "CREATE TABLE  " + getTableName() + " ( t_aid NUMBER(3), t_umidl NUMBER(10), t_umidh NUMBER(10) )";
	private final String clearTempTable= "TRUNCATE TABLE " + getTableName() ;
	
	public DatabaseQueriesOrcl( Connection connection ){
		super( connection );
	}

	@Override
	public String getTableName(){
		return "XmlDumpTempSearchResult";
	}

	@Override
	public String getShema(){
		return "SIDE";
	}

	@Override
	public void initTempTable( EnXmlDumpConfig config ) throws SQLException {
		try{
			connection.createStatement().execute( tempTableCreate );
		}catch( SQLException e){
			if ( !e.getMessage().contains("ORA-00955")){
				throw e;
			}
		}
		connection.createStatement().execute(clearTempTable);
		connection.commit();
		String selectStatement= tempTableInsert +  getWhereStatement( config.getAid(), config.getFromDate(), config.getToDate(), config.isSwiftNet(), config.isAcked() ) ;
		connection.createStatement().execute( selectStatement );
		connection.commit();
	}

	@Override
	public String formatDate(  Date date )  {
		if ( date == null ){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "TO_DATE('" + sdf.format(date) + "', 'YYYY-MM-DD HH24:MI:SS')";		
	}

}
