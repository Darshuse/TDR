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
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eastnets.dao.filepayload.procedure.InsertSepaMessage;
import com.eastnets.domain.filepayload.FilePayload;

/**
 * File Payload DAO Implementation
 * 
 * @author EastNets
 * @since September 5, 2013
 */
public class FilePayloadOracleDAOImp extends FilePayloadDAOImp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5609928643655005661L;
	private InsertSepaMessage insertSepaMsg;

	public InsertSepaMessage getInsertSepaMsg() {
		return insertSepaMsg;
	}

	public void setInsertSepaMsg(InsertSepaMessage insertSepaMsg) {
		this.insertSepaMsg = insertSepaMsg;
	}

	@Override
	public List<FilePayload> getFilesToRetrieve(int aid, int maxTryCount, int delayMinutes, int chunkCount) {
		String query = "select * from ( select AID, FILE_S_UMIDL, FILE_S_UMIDH, MESG_CREA_DATE_TIME, MESG_FILE_LOGICAL_NAME, MESG_FILE_SIZE, MESG_FILE_DIGEST_ALGO, MESG_FILE_DIGEST_VALUE" + " from  rFile" + " where aid = " + aid
				+ " and requested = 0" + " and ( payload is null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) =  null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload) =  0 )" + " and ( payload_text is null or "
				+ getDbPortabilityHandler().getDataLengthFn() + "(payload_text) =  null or " + getDbPortabilityHandler().getDataLengthFn() + "(payload_text) =  0 )" + " and trials_count < " + maxTryCount + " order by last_try asc"
				+ " ) where rownum <= " + chunkCount;
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
		String query = "Update rFile set trials_count= trials_count + 1 , last_try = sysdate, requested = 1" + " where ( aid, file_s_umidl, file_s_umidh, mesg_crea_date_time ) in" + " ( ";
		boolean firstPayload = true;
		for (FilePayload payload : filePayloads) {
			if (!firstPayload) {
				query += ", ";
			}
			query += String.format("( %d, %d, %d, %s )", payload.getAid(), payload.getSumidl(), payload.getSumidh(), getDbPortabilityHandler().getFormattedDateWithNoBinding(payload.getCreationDateTime(), true));
			firstPayload = false;
		}
		query += ")";
		jdbcTemplate.execute(query);

	}

	@Override
	public void updateFilePayload(final FilePayload filePayload, final InputStream inputStream) throws Exception {

		String where = " where aid =" + filePayload.getAid() + " and file_s_umidl= " + filePayload.getSumidl() + " and file_s_umidh= " + filePayload.getSumidh() + " and mesg_crea_date_time= "
				+ getDbPortabilityHandler().getFormattedDateWithNoBinding(filePayload.getCreationDateTime(), true);

		jdbcTemplate.execute("update rFile set payload = EMPTY_BLOB() " + where);// create an empty blob
		boolean result = jdbcTemplate.query("select payload from  rFile " + where + " FOR UPDATE ", new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws DataAccessException {
				try {
					if (rs.next()) {
						Blob blob = rs.getBlob("payload");
						if (blob == null) {
							return false;
						}

						OutputStream stream = blob.setBinaryStream(0);
						if (stream == null) {
							return false;
						}

						int len = 10240;
						byte[] bytes = new byte[len];
						try {
							while ((len = inputStream.read(bytes)) > -1) {
								stream.write(bytes, 0, len);
							}
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						}
						if (stream != null) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
								return false;
							}
						}
						return true;
					}
					return false;

				} catch (Exception e) {
					System.out.println("err: " + e.getMessage());
				}
				return false;
			}
		});
		if (!result) {
			throw new Exception(String.format("Unable to update payload( %d, %d, %d )", filePayload.getAid(), filePayload.getSumidl(), filePayload.getSumidh()));
		}
		jdbcTemplate.getDataSource().getConnection().commit();
	}

	@Override
	public void updateFilePayloadText(final FilePayload filePayload, final InputStream inputStream) throws Exception {

		String where = " where aid =" + filePayload.getAid() + " and file_s_umidl= " + filePayload.getSumidl() + " and file_s_umidh= " + filePayload.getSumidh() + " and mesg_crea_date_time= "
				+ getDbPortabilityHandler().getFormattedDateWithNoBinding(filePayload.getCreationDateTime(), true);

		jdbcTemplate.execute("update rFile set payload_text = EMPTY_CLOB() " + where);// create an empty clob
		boolean result = jdbcTemplate.query("select payload_text from  rFile " + where + " FOR UPDATE ", new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws DataAccessException {
				try {
					if (rs.next()) {
						Clob clob = rs.getClob("payload_text");
						if (clob == null) {
							return false;
						}

						OutputStream stream = clob.setAsciiStream(0);
						if (stream == null) {
							return false;
						}

						int len = 10240;
						byte[] bytes = new byte[len];
						try {
							while ((len = inputStream.read(bytes)) > -1) {
								stream.write(bytes, 0, len);
							}
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						}

						return true;
					}
					return false;

				} catch (Exception e) {
					System.out.println("err: " + e.getMessage());
				}
				return false;
			}
		});
		if (!result) {
			throw new Exception(String.format("Unable to update payload( %d, %d, %d )", filePayload.getAid(), filePayload.getSumidl(), filePayload.getSumidh()));
		}
		jdbcTemplate.getDataSource().getConnection().commit();
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// work arround to insert into RMXText table
	@Override
	public int updateSEPAText(final FilePayload filePayload, String mesgText, String mesgId, int mesgOrder, int mesgSize, int blkFlag, int numMesgs) {
		int result = 0;
		try {
			result = getInsertSepaMsg().execute(filePayload, mesgText, mesgId, mesgOrder, mesgSize, blkFlag);
			if (mesgOrder == numMesgs && result == 1) {
				jdbcTemplate.getDataSource().getConnection().commit();
			} else if (result != 1) {
				jdbcTemplate.getDataSource().getConnection().rollback();
			}

			return result;

		} catch (Exception e) {
			System.out.println(getDateStr() + " " + e.getMessage());
			return result;
		}

	}

	// -------------------------------------------------------------------------------------------------------------------------

}
