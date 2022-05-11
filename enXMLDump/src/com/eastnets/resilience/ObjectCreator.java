package com.eastnets.resilience;

import java.sql.Connection;

import com.eastnets.resilience.EnXmlDumpConfig.DBType;
import com.eastnets.resilience.oracle.DatabaseQueriesOrcl;
import com.eastnets.resilience.sqlserver.DatabaseQueriesMsSql;

public class ObjectCreator {

	/**
	 * @param conn: needed for initialization purposes
	 * @param dbType
	 * @return
	 */
	public static DatabaseQueries getQueryProvider(Connection conn, DBType dbType) {
		if ( dbType == DBType.ORACLE ){
			return new DatabaseQueriesOrcl(conn);
		}else if ( dbType == DBType.MSQL ){
			return new DatabaseQueriesMsSql(conn);
		}
		return null;
	}

}
