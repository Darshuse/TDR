package com.eastnets.commonLkup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.commonLkup.dao.CommonLkupDAO;
import com.eastnets.message.summary.Bean.CorrespondentBean;
import com.eastnets.message.summary.Bean.ExchangeRateBean;
import com.eastnets.message.summary.Bean.GeoLocationBean;
import com.eastnets.message.summary.Bean.MsgCategoryBean;
import com.eastnets.message.summary.Bean.StxMessageBean;
import com.eastnets.message.summary.configuration.GlobalConfiguration;

@Service
public class CommonLkupService {
	private static final Logger LOGGER = LogManager.getLogger(CommonLkupService.class);

	@Autowired
	CommonLkupDAO commonLkupDAO;

	@Autowired
	public GlobalConfiguration globalConfiguration;

	private boolean correspondentsCached = false;
	private boolean regionsCached = false;
	private boolean fieldDescCached = false;
	private boolean msgCategoryCached = false;
	private boolean exchangeRateCached = false;

	private Map<String, CorrespondentBean> correspondentsInformation = new HashMap<>();
	private Map<String, CorrespondentBean> correspondentsInformationBIC8 = new HashMap<>();
	private Map<String, GeoLocationBean> geoLocationBeans = new HashMap<>();

	private List<StxMessageBean> stxMessageBeans = new ArrayList<>();
	private Map<String, String> mesgCategorys = new HashMap<>();
	private Map<String, ExchangeRateBean> exsancgeRatesMap = new HashMap<>();

	public void cacheLookUpTables() {

		if (!correspondentsCached) {
			LOGGER.debug("Caching RCORR Information");
			cacheCorrespondentsInformation();
			correspondentsCached = true;
		}
		if (!regionsCached) {
			LOGGER.debug("Caching BI_CT_REGIONS Information");
			cacheRegionsInformation();
			regionsCached = true;
		}
		if (!fieldDescCached) {
			LOGGER.debug("Caching STX MESSGAES Information");
			cachefieldDescInformation();
			fieldDescCached = true;

		}

		if (!msgCategoryCached) {
			LOGGER.debug("Caching Msg Category Information");
			cacheMsgCategoryInformation();
			msgCategoryCached = true;

		}

		if (!exchangeRateCached) {
			LOGGER.debug("Caching exchangeRate Information");
			cacheExsancgeRatesInformation();
			exchangeRateCached = true;
		}

	}

	public String getBaseCurrency() {
		return commonLkupDAO.getBaseCurrency();
	}

	private void cacheRegionsInformation() {

		// Generate a Map Out of the list of regions
		List<GeoLocationBean> geoLocationBeans = commonLkupDAO.cacheRegionsInformation();

		if (geoLocationBeans != null && !geoLocationBeans.isEmpty()) {
			setGeoLocations(geoLocationBeans.stream().collect(Collectors.toMap(GeoLocationBean::getCountryCode,
					geoLocation -> geoLocation, (oldValue, newValue) -> newValue)));
		}
	}

	private void cachefieldDescInformation() {
		List<StxMessageBean> stxMessageBeans = commonLkupDAO.getFieldDescList();
		setStxMessages(stxMessageBeans);
	}

	private void cacheMsgCategoryInformation() {
		List<MsgCategoryBean> categories = commonLkupDAO.getMsgcategory();
		Map<String, String> categoriesMap = categories.stream()
				.collect(Collectors.toMap(MsgCategoryBean::getCategory, MsgCategoryBean::getDesc));
		setMesgCategorys(categoriesMap);
	}

	private void cacheExsancgeRatesInformation() {
		List<ExchangeRateBean> exsancgeRates = commonLkupDAO.getExsancgeRates();
		Map<String, ExchangeRateBean> exsancgeRatesMap = exsancgeRates.stream()
				.collect(Collectors.toMap(ExchangeRateBean::getKey, Function.identity()));
		setExsancgeRatesMap(exsancgeRatesMap);
	}

	private void cacheCorrespondentsInformation() {

		// Generate a Map Out of the list of correspondents
		List<CorrespondentBean> correspondentsInformation = commonLkupDAO.cacheCorrespondentsInformation();
		if (correspondentsInformation != null && !correspondentsInformation.isEmpty()) {
			setCorrespondentsInformation(
					correspondentsInformation.stream().collect(Collectors.toMap(CorrespondentBean::getCorrBIC11,
							correspondent -> correspondent, (oldValue, newValue) -> newValue)));
		}
	}

	public GlobalConfiguration getGlobalConfiguration() {
		return globalConfiguration;
	}

	public void setGlobalConfiguration(GlobalConfiguration globalConfiguration) {
		this.globalConfiguration = globalConfiguration;
	}

	public Map<String, CorrespondentBean> getCorrespondentsInformation() {
		return correspondentsInformation;
	}

	public void setCorrespondentsInformation(Map<String, CorrespondentBean> correspondentsInformation) {
		this.correspondentsInformation = correspondentsInformation;
	}

	public Map<String, CorrespondentBean> getCorrespondentsInformationBIC8() {
		return correspondentsInformationBIC8;
	}

	public void setCorrespondentsInformationBIC8(Map<String, CorrespondentBean> correspondentsInformationBIC8) {
		this.correspondentsInformationBIC8 = correspondentsInformationBIC8;
	}

	public Map<String, GeoLocationBean> getGeoLocations() {
		return geoLocationBeans;
	}

	public void setGeoLocations(Map<String, GeoLocationBean> geoLocationBeans) {
		this.geoLocationBeans = geoLocationBeans;
	}

	public List<StxMessageBean> getStxMessages() {
		return stxMessageBeans;
	}

	public void setStxMessages(List<StxMessageBean> stxMessageBeans) {
		this.stxMessageBeans = stxMessageBeans;
	}

	public Map<String, String> getMesgCategorys() {
		return mesgCategorys;
	}

	public void setMesgCategorys(Map<String, String> mesgCategorys) {
		this.mesgCategorys = mesgCategorys;
	}

	public Map<String, ExchangeRateBean> getExsancgeRatesMap() {
		return exsancgeRatesMap;
	}

	public void setExsancgeRatesMap(Map<String, ExchangeRateBean> exsancgeRatesMap) {
		this.exsancgeRatesMap = exsancgeRatesMap;
	}

}
