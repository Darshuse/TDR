package com.eastnets.dao.bicloader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.support.DataAccessUtils;

import com.eastnets.dao.DAOBaseImp;

public abstract class BICLoaderDAOImp extends DAOBaseImp implements BICLoaderDAO {

	private static final long serialVersionUID = 1L;
	protected SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Override
	public boolean saveGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		int insertCounter = 0;
		boolean returnValue = false;
		Date currentDate = new Date();
		String currentDateString;
		String sql = "INSERT INTO rGPIDirectory ( " + "RECORD_KEY," + "MODIFICATION_FLAG," + "BDP_RECORD_KEY,"
				+ "PLATFORM," + "SERVICE_ID," + "SERVICE_NAME," + "PARTICIPANT_ID," + "ATTRIBUTE_1," + "ATTRIBUTE_2,"
				+ "ATTRIBUTE_3," + "ATTRIBUTE_4," + "ATTRIBUTE_5," + "ATTRIBUTE_6," + "ATTRIBUTE_7," + "ATTRIBUTE_8,"
				+ "ATTRIBUTE_9," + "ATTRIBUTE_10," + "ATTRIBUTE_11," + "ATTRIBUTE_12," + "ATTRIBUTE_13,"
				+ "ATTRIBUTE_14," + "ATTRIBUTE_15," + "ATTRIBUTE_16," + "ATTRIBUTE_17," + "ATTRIBUTE_18,"
				+ "ATTRIBUTE_19," + "ATTRIBUTE_20," + "ACTIVATION_DATE," + "FIELD_A," + "FIELD_B," + "FIELD_C,"
				+ "INSERTION_DATETIME)" + "VALUES "
				+ "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?, ? )";

		for (Map<String, String> content : contents) {
			try {
				currentDate.setTime(currentDate.getTime() + 100);
				currentDateString = sdf.format(currentDate);
				jdbcTemplate.update(sql, content.get("RECORD KEY"), content.get("MODIFICATION FLAG"),
						content.get("BDP RECORD KEY"), content.get("PLATFORM"),
						content.get("SERVICE ID") != null ? Integer.parseInt(content.get("SERVICE ID")) : null,
						content.get("SERVICE NAME"), content.get("PARTICIPANT ID"), content.get("ATTRIBUTE 1"),
						content.get("ATTRIBUTE 2"), content.get("ATTRIBUTE 3"), content.get("ATTRIBUTE 4"),
						content.get("ATTRIBUTE 5"), content.get("ATTRIBUTE 6"), content.get("ATTRIBUTE 7"),
						content.get("ATTRIBUTE 8"), content.get("ATTRIBUTE 9"), content.get("ATTRIBUTE 10"),
						content.get("ATTRIBUTE 11") != null ? Integer.parseInt(content.get("ATTRIBUTE 11")) : null,
						content.get("ATTRIBUTE 12") != null ? Integer.parseInt(content.get("ATTRIBUTE 12")) : null,
						content.get("ATTRIBUTE 13"), content.get("ATTRIBUTE 14"), content.get("ATTRIBUTE 15"),
						content.get("ATTRIBUTE 16"), content.get("ATTRIBUTE 17"), content.get("ATTRIBUTE 18"),
						content.get("ATTRIBUTE 19"), content.get("ATTRIBUTE 20"),
						content.get("ACTIVATION DATE") != null ? formatter.parse(content.get("ACTIVATION DATE")) : null,
						content.get("FIELD A"), content.get("FIELD B"), content.get("FIELD C"), currentDateString);

				insertCounter++;
				System.out.println("<saveGPICorr> : BIC <" + content.get("PARTICIPANT ID") + "> stored successfully");
				returnValue = true;

			} catch (Exception e) {
				updateldBicsHistory(bicFile, "rGPIDirectory");
				insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while inserting to rGPIDirectory", "");
				throw new Exception(e.getMessage());
			}
		}

		updateldBicsHistory(bicFile, "rGPIDirectory");

		System.out.println("Number of Inserted Records " + insertCounter);
		return returnValue;
	}

	@Override
	public boolean updateGPICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		int updateCounter = 0;
		boolean returnValue = false;
		Date currentDate = new Date();
		String currentDateString;
		StringBuilder updateGPIDirectory = new StringBuilder();

		updateGPIDirectory.append("UPDATE rGPIDirectory SET ");
		updateGPIDirectory.append("MODIFICATION_FLAG = ? , ");
		updateGPIDirectory.append("RECORD_KEY = ? , ");
		updateGPIDirectory.append("BDP_RECORD_KEY = ? , ");
		updateGPIDirectory.append("PLATFORM = ? , ");
		updateGPIDirectory.append("SERVICE_ID = ? , ");
		updateGPIDirectory.append("SERVICE_NAME = ? , ");
		updateGPIDirectory.append("PARTICIPANT_ID = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_1 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_2 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_3 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_4 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_5 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_6 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_7 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_8 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_9 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_10 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_11 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_12 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_13 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_14 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_15= ? , ");
		updateGPIDirectory.append("ATTRIBUTE_16 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_17 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_18 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_19 = ? , ");
		updateGPIDirectory.append("ATTRIBUTE_20 = ? , ");
		updateGPIDirectory.append("ACTIVATION_DATE = ? , ");
		updateGPIDirectory.append("FIELD_A = ? , ");
		updateGPIDirectory.append("FIELD_B = ? , ");
		updateGPIDirectory.append("FIELD_C = ? , ");
		updateGPIDirectory.append("MODIFICATION_DATETIME = ? ");
		updateGPIDirectory.append("Where RECORD_KEY = ? and PARTICIPANT_ID = ? ");

		for (Map<String, String> content : contents) {
			try {
				currentDate.setTime(currentDate.getTime() + 100);
				currentDateString = sdf.format(currentDate);
				int updated = jdbcTemplate.update(updateGPIDirectory.toString(), content.get("MODIFICATION FLAG"),
						content.get("RECORD KEY"), content.get("BDP RECORD KEY"), content.get("PLATFORM"),
						content.get("SERVICE ID") != null ? Integer.parseInt(content.get("SERVICE ID")) : null,
						content.get("SERVICE NAME"), content.get("PARTICIPANT ID"), content.get("ATTRIBUTE 1"),
						content.get("ATTRIBUTE 2"), content.get("ATTRIBUTE 3"), content.get("ATTRIBUTE 4"),
						content.get("ATTRIBUTE 5"), content.get("ATTRIBUTE 6"), content.get("ATTRIBUTE 7"),
						content.get("ATTRIBUTE 8"), content.get("ATTRIBUTE 9"), content.get("ATTRIBUTE 10"),
						content.get("ATTRIBUTE 11") != null ? Integer.parseInt(content.get("ATTRIBUTE 11")) : null,
						content.get("ATTRIBUTE 12") != null ? Integer.parseInt(content.get("ATTRIBUTE 12")) : null,
						content.get("ATTRIBUTE 13"), content.get("ATTRIBUTE 14"), content.get("ATTRIBUTE 15"),
						content.get("ATTRIBUTE 16"), content.get("ATTRIBUTE 17"), content.get("ATTRIBUTE 18"),
						content.get("ATTRIBUTE 19"), content.get("ATTRIBUTE 20"),
						content.get("ACTIVATION DATE") != null ? formatter.parse(content.get("ACTIVATION DATE")) : null,
						content.get("FIELD A"), content.get("FIELD B"), content.get("FIELD C"), currentDateString,
						content.get("RECORD KEY"), content.get("PARTICIPANT ID"));

				if (updated > 0) {
					updateCounter++;
					System.out.println(
							"<updateGPICorr> : BIC <" + content.get("PARTICIPANT ID") + "> Updated successfully");
					returnValue = true;
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader",
							content.get("PARTICIPANT ID") + "> FAILED TO Update", "");

					System.out
							.println("<updateGPICorr> : BIC <" + content.get("PARTICIPANT ID") + "> FAILED TO Update");
					returnValue = true;
				}

			} catch (Exception e) {
				insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rGPIDirectory", "");
				updateldBicsHistory(bicFile, "rGPIDirectory");
				throw new Exception(e.getMessage());
			}
		}

		updateldBicsHistory(bicFile, "rGPIDirectory");
		System.out.println("Number of Updated Records " + updateCounter);
		return returnValue;

	}

	@Override
	public boolean saveFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		String sql = "INSERT INTO rCorr ( " + "corr_type," + "corr_X1," + "corr_nature," + "corr_BIC_can_be_updated,"
				+ "corr_inheritance," + "corr_language," + "corr_institution_name," + "corr_branch_info,"
				+ "corr_location," + "corr_city_name," + "corr_physical_address,	" + "corr_ctry_code,"
				+ "corr_ctry_name," + "corr_subtype," + "corr_pob_number," + "corr_pob_location,"
				+ "corr_pob_ctry_code," + "corr_pob_ctry_name," + "corr_status," + "corr_crea_oper_nickname,"
				+ "corr_crea_date_time," + "corr_mod_oper_nickname," + "corr_mod_date_time," + "corr_token ) "
				+ "VALUES " + "(?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)";

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC CODE") == null || content.get("BIC CODE").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC CODE");
				
				if (content.get("BRANCH CODE") != null && !content.get("BRANCH CODE").isEmpty()) {
					bicCode += content.get("BRANCH CODE");
				}
				
				jdbcTemplate.update(sql, "CORR_TYPE_INSTITUTION", bicCode, " ", 1, 0,
						"SWA_LANG_ENGLISH", content.get("INSTITUTION NAME"), content.get("BRANCH INFORMATION"),
						content.get("LOCATION"), content.get("CITY HEADING"),
						content.get("PHYSICAL ADDRESS 1") + " " + content.get("PHYSICAL ADDRESS 2") + " "
								+ content.get("PHYSICAL ADDRESS 3") + " " + content.get("PHYSICAL ADDRESS 4"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATION"), content.get("POB NUMBER"),
						content.get("POB LOCATION"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("POB COUNTRY NAME"), "CORR_ACTIVE", "side", new Date(), "side", new Date(), 0);

				System.out.println("<saveFICorr> : BIC <" + content.get("BIC CODE") + "> stored successfully");

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.IX_RCORR") || e.getMessage().contains("IX_rCorr")) {
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					updateldBicsHistory(bicFile, "rCorr");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "rCorr");

		return true;
	}

	@Override
	public boolean updateFICorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		int updateCounter = 0;

		StringBuilder update = new StringBuilder();

		update.append("UPDATE RCORR SET ");
		update.append("CORR_TYPE = ? , ");
		update.append("CORR_X1 = ? , ");
		update.append("CORR_NATURE = ? , ");
		update.append("CORR_BIC_CAN_BE_UPDATED = ? , ");
		update.append("CORR_INHERITANCE = ? , ");
		update.append("CORR_LANGUAGE = ? , ");
		update.append("CORR_INSTITUTION_NAME = ? , ");
		update.append("CORR_BRANCH_INFO = ? , ");
		update.append("CORR_LOCATION = ? , ");
		update.append("CORR_CITY_NAME = ? , ");
		update.append("CORR_PHYSICAL_ADDRESS = ? , ");
		update.append("CORR_CTRY_CODE = ? , ");
		update.append("CORR_CTRY_NAME = ? , ");
		update.append("CORR_SUBTYPE = ? , ");
		update.append("CORR_POB_NUMBER = ? , ");
		update.append("CORR_POB_LOCATION = ? , ");
		update.append("CORR_POB_CTRY_CODE = ? , ");
		update.append("CORR_POB_CTRY_NAME = ? , ");
		update.append("CORR_STATUS = ? , ");
		update.append("CORR_CREA_OPER_NICKNAME = ? , ");
		update.append("CORR_MOD_OPER_NICKNAME= ? , ");
		update.append("CORR_MOD_DATE_TIME = ? , ");
		update.append("CORR_TOKEN = ? ");
		update.append("WHERE CORR_X1 like ? ");

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC CODE") == null || content.get("BIC CODE").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC CODE");
				
				if (content.get("BRANCH CODE") != null && !content.get("BRANCH CODE").isEmpty()) {
					bicCode += content.get("BRANCH CODE");
				}
				
				
				int updated = jdbcTemplate.update(update.toString(), "CORR_TYPE_INSTITUTION", bicCode,
						" ", 1, 0, "SWA_LANG_ENGLISH", content.get("INSTITUTION NAME"),
						content.get("BRANCH INFORMATION"), content.get("LOCATION"), content.get("CITY HEADING"),
						content.get("PHYSICAL ADDRESS 1") + " " + content.get("PHYSICAL ADDRESS 2") + " "
								+ content.get("PHYSICAL ADDRESS 3") + " " + content.get("PHYSICAL ADDRESS 4"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATION"), content.get("POB NUMBER"),
						content.get("POB LOCATION"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("POB COUNTRY NAME"), "CORR_ACTIVE", "side", "side", new Date(), 0,
						"%" + content.get("BIC CODE").trim().toUpperCase() + "%");

				if (updated > 0) {
					updateCounter++;
					System.out.println("<updateFICorr> : BIC <" + content.get("BIC CODE") + "> Updated successfully");
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader",
							content.get("BIC CODE") + "> FAILED TO Update FICorr", "");

					System.out
							.println("<updateFICorr> : BIC <" + content.get("BIC CODE") + "> FAILED TO Update FICorr");
				}

			} catch (Exception e) {
				if ((e.getMessage().contains("SIDE.IX_RCORR")) || (e.getMessage().contains("IX_rCorr"))) {

				} else {
					updateldBicsHistory(bicFile, "rCorr");
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					throw new Exception(e.getMessage());
				}
			}
		}

		updateldBicsHistory(bicFile, "rCorr");

		System.out.println("Number of Updated Records " + updateCounter);
		return true;
	}

	@Override
	public boolean saveCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		String sql = "INSERT INTO CT ( " + "COUNTRYCODE," + "COUNTRYNAME," + "UPDATEDATE," + "DELETED )" + "VALUES "
				+ "(?,?,?,?)";

		for (Map<String, String> content : contents) {
			try {
				jdbcTemplate.update(sql, content.get("COUNTRY CODE"), content.get("COUNTRY NAME"), new Date(), 0);

				System.out.println(
						"<saveCTCorr> : Country code <" + content.get("COUNTRY CODE") + "> stored successfully");

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.PK_CT") || e.getMessage().contains("PK_CT")) {
					System.out.println(
							"CT <" + content.get("COUNTRY CODE") + "> With the same info Already added before");
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating CT", "");
					updateldBicsHistory(bicFile, "CT");
					throw new Exception(e.getMessage());
				}
			}
		}

		updateldBicsHistory(bicFile, "CT");

		return true;
	}

	@Override
	public boolean updateCTCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		int updateCounter = 0;
		StringBuilder update = new StringBuilder();

		update.append("UPDATE CT SET ");
		update.append("COUNTRYCODE = ? , ");
		update.append("COUNTRYNAME = ? , ");
		update.append("UPDATEDATE = ? , ");
		update.append("DELETED = ? ");
		update.append("WHERE COUNTRYCODE = ?");

		for (Map<String, String> content : contents) {
			try {
				int updated = jdbcTemplate.update(update.toString(), content.get("COUNTRY CODE"),
						content.get("COUNTRY NAME"), new Date(), 0, content.get("COUNTRY CODE"));

				if (updated > 0) {
					updateCounter++;
					System.out.println(
							"<updateCTCorr> : Country Code <" + content.get("COUNTRY CODE") + "> Updated successfully");
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader",
							content.get("COUNTRY CODE") + "> FAILED TO Update CT", "");

					System.out.println("<updateCTCorr> : Country Code <" + content.get("COUNTRY CODE")
							+ "> FAILED TO Update CTCorr");
				}

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.PK_CT") || e.getMessage().contains("PK_CT")) {
					System.out.println(
							"CT <" + content.get("COUNTRY CODE") + "> With the same info Already added before");
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating CT", "");
					updateldBicsHistory(bicFile, "CT");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "CT");

		System.out.println("Number of Updated Records " + updateCounter);
		return true;
	}

	@Override
	public boolean saveCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		String sql = "INSERT INTO CU ( " + "CURRENCYCODE," + "CURRENCYNAME," + "NUMBEROFDIGITS," + "COUNTRYCODE,"
				+ "UPDATEDATE," + "DELETED )" + "VALUES " + "(?,?,?,?,?,?)";

		for (Map<String, String> content : contents) {
			try {
				jdbcTemplate.update(sql, content.get("CURRENCY CODE"), content.get("CURRENCY NAME"),
						content.get("FRACTIONAL DIGIT"), content.get("COUNTRY CODE") != null
								? content.get("COUNTRY CODE") : content.get("CURRENCY CODE").substring(0, 2),
						new Date(), 0);

				System.out.println(
						"<saveCUCorr> : Currency code <" + content.get("CURRENCY CODE") + "> stored successfully");

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.PK_CU") || e.getMessage().contains("PK_CU")) {
					System.out.println(
							"CU <" + content.get("CURRENCY CODE") + "> With the same info Already added before");
				} else {
					e.printStackTrace();
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating CU", "");
					updateldBicsHistory(bicFile, "CU");
				}
			}
		}
		updateldBicsHistory(bicFile, "CU");

		return true;
	}

	@Override
	public boolean updateCUCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		int updateCounter = 0;

		StringBuilder update = new StringBuilder();

		update.append("UPDATE CU SET ");
		update.append("CURRENCYCODE = ? , ");
		update.append("CURRENCYNAME = ? , ");
		update.append("NUMBEROFDIGITS = ? , ");
		update.append("COUNTRYCODE = ? , ");
		update.append("UPDATEDATE = ? , ");
		update.append("DELETED = ? ");
		update.append("WHERE CURRENCYCODE = ?");

		for (Map<String, String> content : contents) {
			try {
				int updated = jdbcTemplate.update(update.toString(), content.get("CURRENCY CODE"),
						content.get("CURRENCY NAME"), content.get("FRACTIONAL DIGIT"),
						content.get("COUNTRY CODE") != null ? content.get("COUNTRY CODE")
								: content.get("CURRENCY CODE").substring(0, 2),
						new Date(), 0, content.get("CURRENCY CODE"));

				if (updated > 0) {
					updateCounter++;
					System.out.println("<updateCUCorr> : Currency Code <" + content.get("CURRENCY CODE")
							+ "> Updated successfully");
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader",
							content.get("CURRENCY CODE") + "> FAILED TO Update CU", "");

					System.out.println("<updateCUCorr> : Currency Code <" + content.get("CURRENCY CODE")
							+ "> FAILED TO Update CUCorr");
				}

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.PK_CU") || e.getMessage().contains("PK_CU")) {
					System.out.println(
							"CU <" + content.get("CURRENCY CODE") + "> With the same info Already added before");
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating CU", "");
					updateldBicsHistory(bicFile, "CU");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "CU");

		System.out.println("Number of Updated Records " + updateCounter);
		return true;
	}

	@Override
	public boolean deleteFICorr() throws Exception {

		boolean returnValue = false;
		try {
			String sql = "DELETE FROM rCorr ";
			jdbcTemplate.update(sql);
			returnValue = true;

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean deleteCTCorr() throws Exception {
		System.out.println("Inside delete CT CORR Method");
		boolean returnValue = false;
		try {
			String sql = "DELETE FROM CT ";
			jdbcTemplate.update(sql);
			returnValue = true;
			System.out.println("Delete CT return type : " + returnValue);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean deleteCUCorr() throws Exception {

		boolean returnValue = false;
		try {
			String sql = "DELETE FROM CU ";
			jdbcTemplate.update(sql);
			returnValue = true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean deleteGPICorr() throws Exception {

		boolean returnValue = false;
		try {
			String sql = "DELETE FROM rGPIDirectory ";
			jdbcTemplate.update(sql);
			returnValue = true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return returnValue;
	}

	@Override
	public boolean saveBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		String sql = "INSERT INTO rCorr ( " + "corr_type," + "corr_X1," + "corr_nature," + "corr_BIC_can_be_updated,"
				+ "corr_inheritance," + "corr_language," + "corr_institution_name," + "corr_branch_info,"
				+ "corr_location," + "corr_city_name," + "corr_physical_address, " + "corr_ctry_code,"
				+ "corr_ctry_name," + "corr_subtype," + "corr_pob_number," + "corr_pob_ctry_code," + "corr_status,"
				+ "corr_crea_oper_nickname," + "corr_crea_date_time," + "corr_mod_oper_nickname,"
				+ "corr_mod_date_time ," + " corr_token ) " + "VALUES "
				+ "(?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)";

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC CODE") == null || content.get("BIC CODE").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC CODE");
				
				if (content.get("BRANCH CODE") != null && !content.get("BRANCH CODE").isEmpty()) {
					bicCode += content.get("BRANCH CODE");
				}
				
				jdbcTemplate.update(sql, "CORR_TYPE_INSTITUTION", bicCode , "CORR_EXTERNAL", 1, 0,
						"SWA_LANG_ENGLISH", content.get("INSTITUTION NAME"), content.get("BRANCH INFORMATION"),
						content.get("LOCATION CODE"), content.get("RE CITY"), content.get("RE STREET ADDRESS 1"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATOR"), content.get("POB NUMBER"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null, "CORR_ACTIVE",
						"side", new Date(), "side", new Date(), 0);

				System.out.println("<saveBICDueCorr> : BIC <" + content.get("BIC CODE") + "> stored successfully");

			} catch (Exception e) {
				if ((e.getMessage().contains("SIDE.IX_RCORR")) || (e.getMessage().contains("IX_rCorr"))) {
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					updateldBicsHistory(bicFile, "rCorr");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "rCorr");

		return true;
	}

	@Override
	public boolean updateBICDueCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		int updateCounter = 0;

		StringBuilder update = new StringBuilder();

		update.append("UPDATE RCORR SET ");
		update.append("CORR_TYPE = ? , ");
		update.append("CORR_X1 = ? , ");
		update.append("CORR_NATURE = ? , ");
		update.append("CORR_BIC_CAN_BE_UPDATED = ? , ");
		update.append("CORR_INHERITANCE = ? , ");
		update.append("CORR_LANGUAGE = ? , ");
		update.append("CORR_INSTITUTION_NAME = ? , ");
		update.append("CORR_BRANCH_INFO = ? , ");
		update.append("CORR_LOCATION = ? , ");
		update.append("CORR_CITY_NAME = ? , ");
		update.append("CORR_PHYSICAL_ADDRESS = ? , ");
		update.append("CORR_CTRY_CODE = ? , ");
		update.append("CORR_CTRY_NAME = ? , ");
		update.append("CORR_SUBTYPE = ? , ");
		update.append("CORR_POB_NUMBER = ? , ");
		update.append("CORR_POB_CTRY_CODE = ? , ");
		update.append("CORR_STATUS = ? , ");
		update.append("CORR_CREA_OPER_NICKNAME = ? , ");
		update.append("CORR_MOD_OPER_NICKNAME = ? , ");
		update.append("CORR_MOD_DATE_TIME = ? , ");
		update.append("CORR_TOKEN= ? ");
		update.append("WHERE CORR_X1 like ?");

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC CODE") == null || content.get("BIC CODE").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC CODE");
				
				if (content.get("BRANCH CODE") != null && !content.get("BRANCH CODE").isEmpty()) {
					bicCode += content.get("BRANCH CODE");
				}
				int updated = jdbcTemplate.update(update.toString(), "CORR_TYPE_INSTITUTION",
						bicCode.trim(), "CORR_EXTERNAL", 1, 0, "SWA_LANG_ENGLISH",
						content.get("INSTITUTION NAME"), content.get("BRANCH INFORMATION"),
						content.get("LOCATION CODE"), content.get("RE CITY"), content.get("RE STREET ADDRESS 1"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATOR"), content.get("POB NUMBER"),
						content.get("BIC CODE") != null ? content.get("BIC CODE").substring(4, 6) : null, "CORR_ACTIVE",
						"side", "side", new Date(), 0, "%" + content.get("BIC CODE").trim().toUpperCase() + "%");
				if (updated > 0) {
					updateCounter++;
					System.out
							.println("<updateBICDueCorr> : BIC <" + content.get("BIC CODE") + "> Updated successfully");
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader", content.get("BIC CODE") + "> FAILED TO Update",
							"");

					System.out.println("<updateBICDueCorr> : BIC <" + content.get("BIC CODE") + "> FAILED TO Update");
				}

			} catch (Exception e) {
				if (e.getMessage().contains("SIDE.IX_RCORR") || e.getMessage().contains("IX_rCorr")) {
					System.out.println(
							"Another entry of <" + content.get("BIC CODE") + "> with the same info already exist ");
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					updateldBicsHistory(bicFile, "rCorr");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "rCorr");

		System.out.println("Number of Updated Records " + updateCounter);
		return true;

	}

	@Override
	public boolean saveBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {

		String sql = "INSERT INTO rCorr ( " + "corr_type," + "corr_X1," + "corr_nature," + "corr_BIC_can_be_updated,"
				+ "corr_inheritance," + "corr_language," + "corr_institution_name," + "corr_branch_info,"
				+ "corr_city_name," + "corr_physical_address,	" + "corr_ctry_code," + "corr_ctry_name,"
				+ "corr_subtype," + "corr_pob_number," + "corr_pob_ctry_code," + "corr_status,"
				+ "corr_crea_oper_nickname," + "corr_crea_date_time," + "corr_mod_oper_nickname,"
				+ "corr_mod_date_time," + "corr_token ) " + "VALUES "
				+ "(?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)";

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC8") == null || content.get("BIC8").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC8");
				
				if(content.get("BRANCH BIC") != null && !content.get("BRANCH BIC").isEmpty()){
					bicCode += content.get("BRANCH BIC");
				}
				
				
				jdbcTemplate.update(sql, "CORR_TYPE_INSTITUTION", bicCode, "CORR_EXTERNAL", 1, 0,
						"SWA_LANG_ENGLISH", content.get("INSTITUTION NAME"), content.get("BRANCH INFORMATION"),
						content.get("CITY"),
						content.get("STREET ADDRESS 1") + " " + content.get("STREET ADDRESS 2") + " "
								+ content.get("STREET ADDRESS 3") + " " + content.get("STREET ADDRESS 4"),
						content.get("BIC8") != null ? content.get("BIC8").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATOR"), content.get("POB NUMBER"),
						content.get("BIC8") != null ? content.get("BIC8").substring(4, 6) : null, "CORR_ACTIVE", "side",
						new Date(), "side", new Date(), 0);

				System.out.println("<saveBankDirectoryCorr> : BIC <" + content.get("BIC8") + "> stored successfully");

			} catch (Exception e) {
				if ((e.getMessage().contains("SIDE.IX_RCORR")) || (e.getMessage().contains("IX_rCorr"))) {
				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					updateldBicsHistory(bicFile, "rCorr");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "rCorr");

		return true;
	}

	@Override
	public boolean updateBankDirectoryCorr(List<Map<String, String>> contents, BicFile bicFile) throws Exception {
		int updateCounter = 0;
		StringBuilder update = new StringBuilder();

		update.append("UPDATE rCorr SET ");
		update.append("corr_type = ? , ");
		update.append("corr_X1 = ? , ");
		update.append("corr_nature = ? , ");
		update.append("corr_BIC_can_be_updated = ? , ");
		update.append("corr_inheritance = ? , ");
		update.append("corr_language = ? , ");
		update.append("corr_institution_name = ? , ");
		update.append("corr_branch_info = ? , ");
		update.append("corr_city_name = ? , ");
		update.append("corr_physical_address = ? , ");
		update.append("corr_ctry_code = ? , ");
		update.append("corr_ctry_name = ? , ");
		update.append("corr_subtype = ? , ");
		update.append("corr_pob_number = ? , ");
		update.append("corr_pob_ctry_code = ? , ");
		update.append("corr_status = ? , ");
		update.append("corr_crea_oper_nickname = ? , ");
		update.append("corr_mod_oper_nickname = ? , ");
		update.append("corr_mod_date_time = ? , ");
		update.append("corr_token= ? ");
		update.append("WHERE corr_X1 like ? ");

		for (Map<String, String> content : contents) {
			try {
				if(content.get("BIC8") == null || content.get("BIC8").length() < 8){
					continue;
				}
				
				String bicCode = content.get("BIC8");
				
				if(content.get("BRANCH BIC") != null && !content.get("BRANCH BIC").isEmpty()){
					bicCode += content.get("BRANCH BIC");
				}
				
				
				int updated = jdbcTemplate.update(update.toString(), "CORR_TYPE_INSTITUTION", bicCode,
						"CORR_EXTERNAL", 1, 0, "SWA_LANG_ENGLISH", content.get("INSTITUTION NAME"),
						content.get("BRANCH INFORMATION"), content.get("CITY"),
						content.get("STREET ADDRESS 1") + " " + content.get("STREET ADDRESS 2") + " "
								+ content.get("STREET ADDRESS 3") + " " + content.get("STREET ADDRESS 4"),
						content.get("BIC8") != null ? content.get("BIC8").substring(4, 6) : null,
						content.get("COUNTRY NAME"), content.get("SUBTYPE INDICATOR"), content.get("POB NUMBER"),
						content.get("BIC8") != null ? content.get("BIC8").substring(4, 6) : null, "CORR_ACTIVE", "side",
						"side", new Date(), 0, "%" + content.get("BIC8").trim().toUpperCase() + "%");

				if (updated > 0) {
					updateCounter++;
					System.out.println(
							"<updateBankDirectoryCorr> : BIC <" + content.get("BIC8") + "> Updated successfully");
				}

				if (updated <= 0) {
					insertIntoErrorld("BICLoader", "error", "BICLoader", content.get("BIC8") + "> FAILED TO Update",
							"");

					System.out
							.println("<updateBankDirectoryCorr> : BIC <" + content.get("BIC8") + "> FAILED TO Update");
				}

			} catch (Exception e) {
				if ((e.getMessage().contains("SIDE.IX_RCORR")) || (e.getMessage().contains("IX_rCorr"))) {
					System.out.println(
							"Another entry of <" + content.get("BIC8") + "> with the same info already exist ");

				} else {
					insertIntoErrorld("BICLoader", "Failed", "BICLoader", "Error while Updating rCorr", "");
					updateldBicsHistory(bicFile, "rCorr");
					throw new Exception(e.getMessage());
				}
			}
		}
		updateldBicsHistory(bicFile, "rCorr");

		System.out.println("Number of Updated Records " + updateCounter);
		return true;

	}

	@Override
	public boolean checkFileToBeProcessed(String filename) {

		String sql = "Select MODIFICATION_DATE from ldBicsFilesHistory where FILE_NAME like ? ";
		String modificationDate = DataAccessUtils
				.singleResult(jdbcTemplate.queryForList(sql, new Object[] { filename }, String.class));

		return (modificationDate != null && !modificationDate.isEmpty());
	}

	@Override
	public void updateldBicsHistory(BicFile bicFile, String tablename) {

		String sql = "Select MODIFICATION_DATE from ldBicsFilesHistory where FILE_NAME like ?";
		String modificationDate = DataAccessUtils
				.singleResult(jdbcTemplate.queryForList(sql, new Object[] { bicFile.getFilename() }, String.class));

		Date currentDate = new Date();
		String currentDateString = sdf.format(currentDate);

		if (modificationDate != null && !modificationDate.isEmpty()) {

			String updateSql = "UPDATE ldBicsFilesHistory set MODIFICATION_DATE = ? , FILE_TYPE = ? , FULL_DELTA = ? , DELETE_OLD_OPTION = ? , AFFECTED_TABLE = ? where FILE_NAME like ?";
			int updated = jdbcTemplate.update(updateSql, currentDateString, bicFile.getFileType(),
					bicFile.isFullFile() ? "FULL" : "DELTA", bicFile.isDeleteOldOption() ? "ON" : "OFF", tablename,
					bicFile.getFilename());
			if (updated > 0)
				System.out.println("Modification date for " + bicFile.getFilename() + " in ldBicsFilesHistory updated");
		} else {

			String insertSql = "INSERT INTO ldBicsFilesHistory values (?,?,?,?,?,?)";
			int updated = jdbcTemplate.update(insertSql, bicFile.getFilename(), currentDateString,
					bicFile.getFileType(), bicFile.isFullFile() ? "FULL" : "DELTA",
					bicFile.isDeleteOldOption() ? "ON" : "OFF", tablename);
			if (updated > 0) {
				System.out.println(bicFile.getFilename() + " ADDED TO ldBicsFilesHistory TABLE");
			}

		}

	}

}
