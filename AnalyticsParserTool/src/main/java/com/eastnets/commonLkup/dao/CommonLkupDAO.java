package com.eastnets.commonLkup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.eastnets.commonLkup.service.CommonLkupService;
import com.eastnets.message.summary.Bean.CorrespondentBean;
import com.eastnets.message.summary.Bean.ExchangeRateBean;
import com.eastnets.message.summary.Bean.GeoLocationBean;
import com.eastnets.message.summary.Bean.MsgCategoryBean;
import com.eastnets.message.summary.Bean.StxMessageBean;

@Repository
public class CommonLkupDAO {
	private static final Logger LOGGER = LogManager.getLogger(CommonLkupDAO.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public CommonLkupService commonLkupService;

	public List<StxMessageBean> getFieldDescList() {
		List<StxMessageBean> listOfStxMessage = new ArrayList<>();

		LOGGER.trace("Caching StxMessageBean from STXMessges");
		try {
			String query = "SELECT  type,description,version FROM STXMESSAGE m INNER JOIN STXVERSION  v ON   v.idx=m.version_idx";
			Object[] param = null;
			listOfStxMessage = jdbcTemplate.query(query, param, new RowMapper<StxMessageBean>() {
				public StxMessageBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					StxMessageBean stxMessageBean = new StxMessageBean();
					stxMessageBean.setFieldDescription(rs.getString("description"));
					stxMessageBean.setMesgType(rs.getString("type"));
					stxMessageBean.setStxMesgVersion(rs.getString("version"));

					return stxMessageBean;
				}
			});

			if (listOfStxMessage == null || listOfStxMessage.isEmpty()) {
				listOfStxMessage = new ArrayList<>();
				LOGGER.trace("No data returned from STXMessges table");
			}
			return listOfStxMessage;

		} catch (Exception e) {
			LOGGER.error("Error in caching STXMessges : " + e);
			return new ArrayList<>();
		}

	}

	public List<MsgCategoryBean> getMsgcategory() {
		List<MsgCategoryBean> listOfMsgCategory = new ArrayList<>();
		LOGGER.trace("Caching Catogary from getMsgcategory");
		try {
			String query = "SELECT  * from smsgcategory";
			listOfMsgCategory = jdbcTemplate.query(query, new RowMapper<MsgCategoryBean>() {
				public MsgCategoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					MsgCategoryBean msgCategoryBean = new MsgCategoryBean();
					msgCategoryBean.setCategory(rs.getString("CATEGORY"));
					msgCategoryBean.setDesc(rs.getString("DESCRIPTION"));
					return msgCategoryBean;
				}
			});
			if (listOfMsgCategory == null || listOfMsgCategory.isEmpty()) {
				listOfMsgCategory = new ArrayList<>();
				LOGGER.trace("No data returned from smsgcategory table");
			}
			return listOfMsgCategory;

		} catch (Exception e) {
			LOGGER.error("Error in caching getMsgcategory : " + e);
			return new ArrayList<>();
		}

	}

	public List<ExchangeRateBean> getExsancgeRates() {

		LOGGER.trace("Caching SEXCHANGERATES from SEXCHANGERATES");
		List<ExchangeRateBean> listofExchangeRate = new ArrayList<>();
		try {
			String query = "SELECT *  FROM SEXCHANGERATES";
			listofExchangeRate = jdbcTemplate.query(query, new RowMapper<ExchangeRateBean>() {
				public ExchangeRateBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					ExchangeRateBean exchangeRateBean = new ExchangeRateBean();
					exchangeRateBean.setBaseCur(rs.getString("BASE_CURRENCY"));
					exchangeRateBean.setCurCode(rs.getString("CURRENCY_CODE"));
					exchangeRateBean.setCurRate(rs.getDouble("CURRENCY_RATE"));

					return exchangeRateBean;
				}
			});

			if (listofExchangeRate == null || listofExchangeRate.isEmpty()) {
				listofExchangeRate = new ArrayList<>();
				LOGGER.trace("No data returned from SEXCHANGERATES table");
			}
			return listofExchangeRate;

		} catch (Exception e) {
			LOGGER.error("Error in caching SEXCHANGERATES : " + e);
			return new ArrayList<>();
		}

	}

	public String getBaseCurrency() {

		LOGGER.trace("Get Base Currency From ld Globel Settings");
		try {
			String query = "SELECT BASE_CUR FROM LDGLOBALSETTINGS";
			return jdbcTemplate.queryForObject(query, String.class);

		} catch (Exception e) {
			LOGGER.error("Error whene get BaseCur : " + e);
			return null;
		}

	}

	public List<CorrespondentBean> cacheCorrespondentsInformation() {

		Map<String, CorrespondentBean> correspondentsInformationBIC8 = new HashMap<>();
		List<CorrespondentBean> listOfcorrespondent = new ArrayList<>();
		LOGGER.trace("Caching Correspondents from RCORR");
		try {
			String query = "SELECT DISTINCT C.CORR_X1, C.CORR_INSTITUTION_NAME, C.CORR_BRANCH_INFO, C.CORR_CTRY_CODE, C.CORR_CTRY_NAME FROM RCORR C ORDER BY CORR_X1 ";

			Object[] param = null;
			listOfcorrespondent = jdbcTemplate.query(query, param, new RowMapper<CorrespondentBean>() {
				public CorrespondentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					CorrespondentBean correspondentBean = new CorrespondentBean();
					String bic11 = rs.getString("CORR_X1");
					if (bic11 != null && !bic11.isEmpty()) {
						String bic8 = bic11.substring(0, 8);
						correspondentBean.setCorrBIC11(bic11);
						correspondentBean.setCorrBIC8(bic8);
						correspondentBean.setCorrBranchCode(bic11.substring(8, 11));
						correspondentBean.setCorrInstitutionName(rs.getString("CORR_INSTITUTION_NAME"));
						correspondentBean.setCorrBranchInfo(rs.getString("CORR_BRANCH_INFO"));
						correspondentBean.setCorrCountryName(rs.getString("CORR_CTRY_NAME"));
						correspondentBean.setCorrCountryCode(rs.getString("CORR_CTRY_CODE"));

						if (!correspondentsInformationBIC8.containsKey(bic8)) {
							correspondentsInformationBIC8.put(bic8, correspondentBean);
							commonLkupService.setCorrespondentsInformationBIC8(correspondentsInformationBIC8);
						}

						return correspondentBean;
					}
					return null;
				}
			});

			if (listOfcorrespondent == null || listOfcorrespondent.isEmpty()) {
				listOfcorrespondent = new ArrayList<>();
				LOGGER.trace("No data returned from RCORR table");
			}

			return listOfcorrespondent;

		} catch (Exception e) {
			LOGGER.error("Error in caching correspondents : " + e);
			return new ArrayList<>();
		}
	}

	public List<GeoLocationBean> cacheRegionsInformation() {

		List<GeoLocationBean> listOfGeoLocation = new ArrayList<>();
		LOGGER.trace("Caching Regions from BI_CT_REGIONS");
		try {
			String query = "SELECT DISTINCT R.COUNTRYCODE, R.REGION, R.CURRENCYCODE FROM BI_CT_REGIONS R  ORDER BY COUNTRYCODE ";
			Object[] param = null;
			listOfGeoLocation = jdbcTemplate.query(query, param, new RowMapper<GeoLocationBean>() {
				public GeoLocationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					GeoLocationBean geo = new GeoLocationBean();
					geo.setCountryCode(rs.getString("COUNTRYCODE"));
					geo.setRegion(rs.getString("REGION"));
					geo.setCurrencyCode(rs.getString("CURRENCYCODE"));
					return geo;
				}
			});

			if (listOfGeoLocation == null || listOfGeoLocation.isEmpty()) {
				listOfGeoLocation = new ArrayList<>();
				LOGGER.trace("No data returned from BI_CT_REGIONS table");
			}
			return listOfGeoLocation;

		} catch (Exception e) {
			LOGGER.error("Error in caching regions : " + e);
			return new ArrayList<>();
		}
	}

}
