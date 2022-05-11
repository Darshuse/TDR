package com.eastnets.message.summary.service;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastnets.commonLkup.service.CommonLkupService;
import com.eastnets.message.summary.Bean.CorrespondentBean;
import com.eastnets.message.summary.Bean.ExchangeRateBean;
import com.eastnets.message.summary.Bean.GeoLocationBean;
import com.eastnets.message.summary.Bean.MessageSummaryDTO;
import com.eastnets.message.summary.configuration.GlobalConfiguration;
import com.eastnets.message.summary.enumDesc.CustomerRouteOption;
import com.eastnets.message.summary.enumDesc.GeoRouteOption;
import com.eastnets.message.summary.enumDesc.Quarterly;
import com.eastnets.message.summary.enumDesc.SwiftBics;

@Service
public class MessageSummaryService {

	private static final Logger LOGGER = LogManager.getLogger(MessageSummaryService.class);

	@Autowired
	public GlobalConfiguration globalConfiguration;

	@Autowired
	public MessageReaderService messageReaderService;

	@Autowired
	public MessageWriterService messageWriterService;

	@Autowired
	public CommonLkupService commonLkupService;

	public void startMigrationService() {
		List<MessageSummaryDTO> messages = null;

		long numberOfMessages = 1;

		while (numberOfMessages > 0) {

			LOGGER.debug("Fetching Messages from DB ");
			messages = messageReaderService.getMessages();

			if (messages == null) {
				LOGGER.error("NULL List - Stopping The application");
				LOGGER.debug("Check Traces in WebClient - Monitoring module and logs of this tool");
				return;
			} else if (messages.isEmpty()) {
				LOGGER.info(
						"No Messages were Selected - Working again after the given 'analyticsParserScheduler' period");
				numberOfMessages = 0;
				continue;
			} else if (messages.size() == 1) {
				LOGGER.debug("Processing message id: " + "AID: " + messages.get(0).getAid() + " UMIDL: "
						+ messages.get(0).getUmidl() + " UMIDH: " + messages.get(0).getUmidh());
			}
			numberOfMessages = messages.size();
			// Process Messages List

			LOGGER.debug("Number of Messages to be processed: " + numberOfMessages);
			processMessages(messages);

			LOGGER.debug("Start Inserting and updating " + numberOfMessages + " Messages");
			messageWriterService.writeMessages(messages);
		}
	}

	private void processMessages(List<MessageSummaryDTO> messages) {

		String baseCur = commonLkupService.getBaseCurrency();

		try {

			LOGGER.trace("Setting BICs information");

			if (messages != null && !messages.isEmpty()) {

				for (MessageSummaryDTO message : messages) {

					String messageDirection = message.getMesgSubFormat();

					if (messageDirection != null && !messageDirection.isEmpty()) {
						CorrespondentBean senderInformation = commonLkupService.getCorrespondentsInformation()
								.get(message.getSenderBIC());

						GeoLocationBean senderGeoLocation = commonLkupService.getGeoLocations()
								.get(message.getSenderBIC().substring(4, 6));

						CorrespondentBean receiverInformation = commonLkupService.getCorrespondentsInformation()
								.get(message.getReceicerBIC());
						// TODO
						GeoLocationBean receiverGeoLocation = commonLkupService.getGeoLocations()
								.get(message.getReceicerBIC().substring(4, 6));

						if (messageDirection.equalsIgnoreCase("INPUT")) {

							if (senderInformation != null) {

								message.setOwnerInformation(senderInformation);

							} else {

								LOGGER.trace("Sender BIC Information '" + message.getSenderBIC()
										+ "' is not available in RCORR Table");
							}

							if (senderGeoLocation != null) {

								message.setOwnerGeoLocation(senderGeoLocation.getRegion());

							} else {
								LOGGER.trace(
										"Sender BIC '" + message.getSenderBIC() + "' GEO Location is not available");
								if (message.getSenderBIC().contains("BE")) {
									System.out.println("qwfawfasfas");
								}
							}

							if (receiverInformation != null) {

								message.setCounterPartInformation(receiverInformation);
							} else {
								LOGGER.trace("Receiver BIC Information '" + message.getReceicerBIC()
										+ "' is not available in RCORR Table");
							}

							if (receiverGeoLocation != null) {

								message.setCounterPartGeoLocation(receiverGeoLocation.getRegion());

							} else {
								LOGGER.trace(
										"Receiver BIC '" + message.getSenderBIC() + "' GEO Location is not available");
								if (message.getReceicerBIC().contains("BE")) {
									System.out.println("qwfawfasfas");
								}
							}

						} else {

							if (receiverInformation != null) {

								message.setOwnerInformation(receiverInformation);
							} else {
								LOGGER.trace("Receiver BIC Information '" + message.getReceicerBIC()
										+ "' is not available in RCORR Table");
							}

							if (receiverGeoLocation != null) {

								message.setOwnerGeoLocation(receiverGeoLocation.getRegion());

							} else {
								LOGGER.trace(
										"Receiver BIC '" + message.getSenderBIC() + "' GEO Location is not available");
							}

							if (senderInformation != null) {

								message.setCounterPartInformation(senderInformation);
							} else {
								LOGGER.trace("Sender BIC Information '" + message.getSenderBIC()
										+ "' is not available in RCORR Table");
							}

							if (senderGeoLocation != null) {

								message.setCounterPartGeoLocation(senderGeoLocation.getRegion());

							} else {
								LOGGER.trace(
										"Sender BIC '" + message.getSenderBIC() + "' GEO Location is not available");
							}
						}
					}

					// sBusinessArea
					commonLkupService.getStxMessages().stream()
							.filter(bean -> bean.getStxMesgVersion().equals(message.getMesgStxVersion()))
							.forEach(stxVersionBean -> {
								if (stxVersionBean.getMesgType().equals(message.getMesgType())) {
									message.setBusinessArea(stxVersionBean.getFieldDescription());
								}
							});
					// smsgcategory table -id - description
					message.setCategoryDescription(message.getxCategory() + "-"
							+ commonLkupService.getMesgCategorys().get(message.getxCategory()));
					// Customer Route
					message.setCustomerRoute(getCustomerRouteValue(message.getSenderBIC(), message.getReceicerBIC()));
					// Geo Route
					message.setGeoRoute(getGeoRouteValue(message.getSenderBIC(), message.getReceicerBIC()));
					// day,month,year
					extractDate(message);
					// QuarterlyPeriod
					message.setQuarterlyPeriod(getQuarterlyPeriod(message) + message.getYear());
					// Period
					message.setPeriod(getPeriod(message).toUpperCase() + message.getYear());
					// Bukets
					message.setStanderValueBucket(getSVB(message, baseCur));
					message.setGpiFlag((message.getMesgSLA() != null
							&& (message.getMesgSLA() == "001" || message.getMesgSLA() == "002")) ? 1 : 0);

				}
			}
		} catch (Exception e) {
			LOGGER.trace("Error Reading information of Correspondents and Regions");
			e.printStackTrace();
		}
	}

	private String getSVB(MessageSummaryDTO message, String baseCur) {
		try {
			String bucket = "";
			Double amount = message.getxFinAmount();

			String cur = message.getxFinCcy();

			Double calBaseAmount = calBaseAmount(amount, cur, baseCur);

			if (calBaseAmount == null) {
				calBaseAmount = amount;
			}

			message.setBaseAmount(calBaseAmount);
			if (calBaseAmount >= 0 && calBaseAmount < 500) {
				bucket = "0 to < 500";
			} else if (calBaseAmount >= 500 && calBaseAmount < 2500) {
				bucket = "500 to < 2500";
			} else if (calBaseAmount >= 2500 && calBaseAmount < 10000) {
				bucket = "2500 to < 10K";
			} else if (calBaseAmount >= 10000 && calBaseAmount < 25000) {
				bucket = "10K to < 25K";
			} else if (calBaseAmount >= 25000 && calBaseAmount < 50000) {
				bucket = "25K to < 50K";
			} else if (calBaseAmount >= 50000 && calBaseAmount < 100000) {
				bucket = "50K to < 100K";
			} else if (calBaseAmount >= 100000 && calBaseAmount < 500000) {
				bucket = "100K to < 500K";
			} else if (calBaseAmount >= 500000 && calBaseAmount < 1000000) {
				bucket = "500K to < 1M";
			} else if (calBaseAmount >= 1000000 && calBaseAmount < 10000000) {
				bucket = "1M to < 10M";
			} else if (calBaseAmount >= 10000000 && calBaseAmount < 50000000) {
				bucket = "10M to < 50M";
			} else if (calBaseAmount >= 50000000) {
				bucket = "50M and greater";
			}
			return bucket;
		} catch (Exception e) {

			return null;
		}
	}

	private Double calBaseAmount(Double amount, String cur, String baseCur) {
		ExchangeRateBean exchangeRateBean = commonLkupService.getExsancgeRatesMap().get(baseCur + cur);
		if (exchangeRateBean == null) {
			return null;
		}
		return amount * exchangeRateBean.getCurRate();
	}

	private String getPeriod(MessageSummaryDTO message) {
		return getShortMonth(message.getMonthNumber() - 1);
	}

	private String getQuarterlyPeriod(MessageSummaryDTO message) {
		for (Quarterly quarterly : Quarterly.values()) {
			if (quarterly.getM1() == message.getMonthNumber() || quarterly.getM2() == message.getMonthNumber()
					|| quarterly.getM3() == message.getMonthNumber()) {
				return quarterly.getQuarter();
			}
		}
		return "";
	}

	private void extractDate(MessageSummaryDTO message) {
		Date date = message.getMesgCreateDateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		message.setDay(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		message.setMonth(getMonthForInt(calendar.get(Calendar.MONTH)));
		message.setMonthNumber(calendar.get(Calendar.MONTH) + 1);
		message.setYear(Integer.toString(calendar.get(Calendar.YEAR)));
	}

	private String getCustomerRouteValue(String sender, String reciver) {
		String senderBarnch = sender.substring(sender.length() - 3);
		String reciverBarnch = reciver.substring(reciver.length() - 3);
		if (!checkIfSwiftBic(sender, reciver).isEmpty()) {
			return CustomerRouteOption.SWIFT.getOption();
		}
		if (senderBarnch.equalsIgnoreCase(reciverBarnch)) {
			return CustomerRouteOption.INTRA.getOption();
		} else if (!senderBarnch.equalsIgnoreCase(reciverBarnch)) {
			return CustomerRouteOption.INTER.getOption();
		}
		return "";
	}

	static String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	static String getShortMonth(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getShortMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	private String getGeoRouteValue(String sender, String reciver) {
		String senderCountry = sender.substring(4, 6);
		String reciverCountry = reciver.substring(4, 6);

		if (!checkIfSwiftBic(sender, reciver).isEmpty()) {
			return CustomerRouteOption.SWIFT.getOption();
		}

		if (senderCountry.equalsIgnoreCase(reciverCountry)) {
			return GeoRouteOption.Domestic.getOption();
		} else if (!reciverCountry.equalsIgnoreCase(senderCountry)) {
			return GeoRouteOption.International.getOption();
		}
		return CustomerRouteOption.SWIFT.getOption();
	}

	private String checkIfSwiftBic(String sender, String reciver) {
		String swiftBic = "";
		try {
			SwiftBics.valueOf(sender).getBic();
			swiftBic = CustomerRouteOption.SWIFT.getOption();
		} catch (Exception e) {
			swiftBic = "";
		}
		if (!swiftBic.isEmpty())
			return swiftBic;

		try {
			SwiftBics.valueOf(reciver).getBic();
			swiftBic = CustomerRouteOption.SWIFT.getOption();
		} catch (Exception e) {
			swiftBic = "";
		}

		return swiftBic;
	}

}
