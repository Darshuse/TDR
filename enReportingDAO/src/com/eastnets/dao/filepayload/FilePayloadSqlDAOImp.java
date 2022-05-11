/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.dao.filepayload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;



import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.filepayload.procedure.InsertSepaMessage;
import com.eastnets.domain.filepayload.FilePayload;

/**
 * File Payload DAO Implementation
 * 
 * @author EastNets
 * @since September 5, 2013
 */
public class FilePayloadSqlDAOImp extends FilePayloadDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3056927215615288989L;
	private InsertSepaMessage insertSepaMsg;
	
	
	
	
	public InsertSepaMessage getInsertSepaMsg() {
		return insertSepaMsg;
	}

	public void setInsertSepaMsg(InsertSepaMessage insertSepaMsg) {
		this.insertSepaMsg = insertSepaMsg;
	}

	@Override
	public List<FilePayload> getFilesToRetrieve(int aid, int maxTryCount, int delayMinutes, int chunkCount) {
		String query = "select * from ( select TOP(" 
				+ chunkCount 
				+ ") AID, FILE_S_UMIDL, FILE_S_UMIDH, MESG_CREA_DATE_TIME, MESG_FILE_LOGICAL_NAME, MESG_FILE_SIZE, MESG_FILE_DIGEST_ALGO, MESG_FILE_DIGEST_VALUE"
				+ " from  rFile"
				+ " where aid = "
				+ aid
				+ " and requested = 0"
				+ " and ( payload is null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) =  null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) =  0 )"
				+ " and ( payload_text is null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload_text) =  null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload_text) =  0 )"
				+ " and trials_count < " + maxTryCount + " order by last_try asc"
				+ " ) aa" ;
		;

		List<FilePayload> payloads = jdbcTemplate.query(query, new RowMapper<FilePayload>() {
			@Override
			public FilePayload mapRow(ResultSet rs, int rowNum) throws SQLException {
				FilePayload payload = new FilePayload();
				payload.setAid(rs.getInt("AID"));
				payload.setSumidl(rs.getInt("FILE_S_UMIDL"));
				payload.setSumidh(rs.getInt("FILE_S_UMIDH"));
				payload.setCreationDateTime(rs.getTimestamp("MESG_CREA_DATE_TIME"));

				payload.setFileName(rs.getString("MESG_FILE_LOGICAL_NAME"));
				payload.setFileSize(rs.getInt("MESG_FILE_SIZE"));
				payload.setFileDigest(rs.getString("MESG_FILE_DIGEST_VALUE"));
				payload.setFileDigestAlgo(rs.getString("MESG_FILE_DIGEST_ALGO"));

				return payload;
			}
		});
		return payloads;
	}

	@Override
	public void updateTrials(List<FilePayload> filePayloads) throws DataAccessException {
		String query = 	"Update rFile set trials_count= trials_count + 1 , last_try = GETDATE(), requested = 1" +
				" where ( " +
				"convert( nvarchar(10), aid ) + ' ' +" +
				"convert( nvarchar(10), file_s_umidl ) + ' ' +" +
				"convert( nvarchar(10), file_s_umidh ) + ' ' +" +
				"convert( nvarchar(50), mesg_crea_date_time, 120)" +
				" ) in" + " ( ";
		boolean firstPayload = true;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (FilePayload payload : filePayloads) {
			if (!firstPayload) {
				query += ", ";
			}
			query += String.format("'%d %d %d %s'", payload.getAid(), payload.getSumidl(), payload.getSumidh(),  
					formatter.format(payload.getCreationDateTime()));
			firstPayload = false;
		}
		query += ")";
		jdbcTemplate.execute(query);

	}

	@Override
	public void updateFilePayload(final FilePayload filePayload, final InputStream inputStream) throws Exception {

		String query = "update rFile set payload=? where aid=? and file_s_umidl=? and file_s_umidh=? and mesg_crea_date_time=? ";		
		jdbcTemplate.update(query, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				int byteCount;
				try {
					byteCount = inputStream.available();
					byte[] bytes = new byte[byteCount];
					inputStream.read(bytes, 0, byteCount);

					int idx = 0;
					preparedStatement.setBytes(++idx, bytes);
					preparedStatement.setInt(++idx, filePayload.getAid());
					preparedStatement.setInt(++idx, filePayload.getSumidl());
					preparedStatement.setInt(++idx, filePayload.getSumidh());
					preparedStatement.setTimestamp(++idx, filePayload.getCreationDateTime());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void updateFilePayloadText(final FilePayload filePayload, final InputStream inputStream) throws Exception {

		String query = "update rFile set payload_text=? where aid=? and file_s_umidl=? and file_s_umidh=? and mesg_crea_date_time=? ";		
		jdbcTemplate.update(query, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				try {
					//this was not made as we have done ion the updateFilePayload because inputStream.available() always gives 1 for streams comming from archives   
					StringBuilder sb = new StringBuilder();
					int len = 10240;
					byte[] bytes = new byte[len];
					try {
						while ((len = inputStream.read(bytes)) > -1) {
							sb.append(new String(bytes, 0, len));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					//bytes.

					int idx = 0;
					preparedStatement.setString(++idx, sb.toString());
					preparedStatement.setInt(++idx, filePayload.getAid());
					preparedStatement.setInt(++idx, filePayload.getSumidl());
					preparedStatement.setInt(++idx, filePayload.getSumidh());
					preparedStatement.setTimestamp(++idx, filePayload.getCreationDateTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	//-------------------------------------------------------------------------------------------------------------------------
	//  work arround to insert into RMXText table
	@Override
	public int updateSEPAText( final FilePayload filePayload, String mesgText , String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs) {
		// TODO Auto-generated method stub
		int result = 0;
		try{
			result = getInsertSepaMsg().execute(filePayload, mesgText, mesgId,mesgOrder, mesgSize,blkFlag);
			if (mesgOrder == numMesgs && result == 1){
				jdbcTemplate.getDataSource().getConnection().commit();
			}else{
				jdbcTemplate.getDataSource().getConnection().rollback();
			}
			return result;

		}catch(Exception e){
			System.out.println(getDateStr() + " " + e.getMessage());
			return result;
		}

	}
	
	//-------------------------------------------------------------------------------------------------------------------------

	
}
