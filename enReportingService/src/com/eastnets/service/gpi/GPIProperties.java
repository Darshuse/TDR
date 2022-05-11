package com.eastnets.service.gpi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.GpiAgent;
import com.eastnets.domain.viewer.GpiType;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.ViewerSearchParam;

public class GPIProperties {

	private MessageDetails messageDetails = null;

	private String uniqueEndToEndTransactionReference;
	private int timeZoneOffset;
	private ViewerSearchParam param = new ViewerSearchParam();
	private List<Boolean> gpiHistoryColumVisabilty;
	private List<DetailsHistory> detailsHistoriesList;
	private Map<String, String> bicArrivedDate = new HashMap<>();
	private Map<String, String> arrivedCoveDate = new HashMap<>();
	private List<String> bicList = new ArrayList<>();
	private List<String> forwordingBicList = new ArrayList<>();
	private boolean nonTraceableBeneficiaryBank = false;
	private GpiAgent beneficiaryBank;
	private boolean rejectPayment;
	private String totalDurationMesgCov;
	long totalHours;
	long totalMin;
	long totalSec;
	long totalCovHours;
	long totalCovMin;
	long totalCovSec;
	private String totalDeductsStr;
	private String allDeductType;
	private String deductCur;
	private Double totalDeducts;
	private String amountAfterDeduct;
	private String creditedAmount;
	private boolean initialDraw = false;
	private boolean updateFromIncoming = false;
	private String totalDuration;
	private boolean forwardToBeneficiaryCustomer = false;
	private boolean forwardToNonGpiAgent = false;
	private boolean showDetailsCreditedAmount;
	boolean findNonTracabel = false;
	private String credetedCCy;
	private String nonGpiBic;
	private boolean hasCovPayment = false;
	private boolean hasgSRPRequest = false;
	private String gSRPAgentResponse;
	private String gSRPTrackerResponse;
	private String gSRPRequestReason;
	private boolean fromGpiDeatils = false;
	private List<String> nonGpiCoveAgent = new ArrayList<>();

	private String satausCovImage;
	private String statusCovStyle;
	private String statusMessageCov;

	private String statusMessageGpi;

	private GpiType gpiType;

	private boolean hasIntermediaryGpiAgent = true;

	private String satausImage;

	private String statusStyle;

	private Map<String, Integer> currenciesISO;
	private boolean showCreditedAmountBeforeConvert;
	private boolean show71FCharges;

	void resetGpiValue() {
		setDetailsHistoriesList(new ArrayList<DetailsHistory>());
		bicList = new ArrayList<>();
		forwordingBicList = new ArrayList<>();
		setBicArrivedDate(new HashMap<String, String>());
		setArrivedCoveDate(new HashMap<String, String>());
		setBeneficiaryBank(null);
		setRejectPayment(false);
		setTotalDuration("");
		setTotalHours(0);
		setTotalMin(0);
		setTotalSec(0);
		setTotalDurationMesgCov("");
		setTotalCovHours(0);
		setTotalCovMin(0);
		setTotalCovSec(0);
		setAmountAfterDeduct("");
		setTotalDeducts(0.0);
		setCreditedAmount("");
		setTotalDeductsStr("");
		setAllDeductType("");
		nonTraceableBeneficiaryBank = false;
		forwardToBeneficiaryCustomer = false;
		showDetailsCreditedAmount = false;
		forwardToNonGpiAgent = false;
		findNonTracabel = false;
		setInitialDraw(false);
		setUpdateFromIncoming(false);
		nonGpiBic = "";
		setHasCovPayment(false);
		setHasgSRPRequest(false);
		setgSRPAgentResponse(null);
		setgSRPRequestReason(null);
		setgSRPTrackerResponse(null);
		nonGpiCoveAgent = new ArrayList<>();
		fromGpiDeatils = false;
		setCredetedCCy("");
		setDeductCur("");
	}

	public String getCredetedCCy() {
		if (showCreditedAmountBeforeConvert) {
			return credetedCCy;
		}
		return messageDetails.getInstructedCurGpi();
	}

	public void setCredetedCCy(String credetedCCy) {
		this.credetedCCy = credetedCCy;
	}

	public boolean isShowCreditedAmountBeforeConvert() {
		return showCreditedAmountBeforeConvert;
	}

	public void setShowCreditedAmountBeforeConvert(boolean showCreditedAmountBeforeConvert) {
		this.showCreditedAmountBeforeConvert = showCreditedAmountBeforeConvert;
	}

	public String getUniqueEndToEndTransactionReference() {
		return uniqueEndToEndTransactionReference;
	}

	public void setUniqueEndToEndTransactionReference(String uniqueEndToEndTransactionReference) {
		this.uniqueEndToEndTransactionReference = uniqueEndToEndTransactionReference;
	}

	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {

		if (timeZoneOffset > -23 && timeZoneOffset < 23) {
			this.timeZoneOffset = timeZoneOffset;
		}
	}

	public ViewerSearchParam getParam() {
		return param;
	}

	public void setParam(ViewerSearchParam param) {
		this.param = param;
	}

	public List<Boolean> getGpiHistoryColumVisabilty() {
		return gpiHistoryColumVisabilty;
	}

	public void setGpiHistoryColumVisabilty(List<Boolean> gpiHistoryColumVisabilty) {
		this.gpiHistoryColumVisabilty = gpiHistoryColumVisabilty;
	}

	public List<DetailsHistory> getDetailsHistoriesList() {
		return detailsHistoriesList;
	}

	public void setDetailsHistoriesList(List<DetailsHistory> detailsHistoriesList) {
		this.detailsHistoriesList = detailsHistoriesList;
	}

	public Map<String, String> getBicArrivedDate() {
		return bicArrivedDate;
	}

	public void setBicArrivedDate(Map<String, String> bicArrivedDate) {
		this.bicArrivedDate = bicArrivedDate;
	}

	public Map<String, String> getArrivedCoveDate() {
		return arrivedCoveDate;
	}

	public void setArrivedCoveDate(Map<String, String> arrivedCoveDate) {
		this.arrivedCoveDate = arrivedCoveDate;
	}

	public GpiAgent getBeneficiaryBank() {
		return beneficiaryBank;
	}

	public void setBeneficiaryBank(GpiAgent beneficiaryBank) {
		this.beneficiaryBank = beneficiaryBank;
	}

	public boolean isRejectPayment() {
		return rejectPayment;
	}

	public void setRejectPayment(boolean rejectPayment) {
		this.rejectPayment = rejectPayment;
	}

	public String getTotalDurationMesgCov() {
		return totalDurationMesgCov;
	}

	public void setTotalDurationMesgCov(String totalDurationMesgCov) {
		this.totalDurationMesgCov = totalDurationMesgCov;
	}

	public long getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(long totalHours) {
		this.totalHours = totalHours;
	}

	public long getTotalMin() {
		return totalMin;
	}

	public void setTotalMin(long totalMin) {
		this.totalMin = totalMin;
	}

	public long getTotalSec() {
		return totalSec;
	}

	public void setTotalSec(long totalSec) {
		this.totalSec = totalSec;
	}

	public long getTotalCovHours() {
		return totalCovHours;
	}

	public void setTotalCovHours(long totalCovHours) {
		this.totalCovHours = totalCovHours;
	}

	public long getTotalCovMin() {
		return totalCovMin;
	}

	public void setTotalCovMin(long totalCovMin) {
		this.totalCovMin = totalCovMin;
	}

	public long getTotalCovSec() {
		return totalCovSec;
	}

	public void setTotalCovSec(long totalCovSec) {
		this.totalCovSec = totalCovSec;
	}

	public String getTotalDeductsStr() {
		return totalDeductsStr;
	}

	public void setTotalDeductsStr(String totalDeductsStr) {
		this.totalDeductsStr = totalDeductsStr;
	}

	public String getDeductCur() {
		return deductCur;
	}

	public void setDeductCur(String deductCur) {
		this.deductCur = deductCur;
	}

	public Double getTotalDeducts() {
		return totalDeducts;
	}

	public void setTotalDeducts(Double totalDeducts) {
		this.totalDeducts = totalDeducts;
	}

	public String getAmountAfterDeduct() {
		return amountAfterDeduct;
	}

	public void setAmountAfterDeduct(String amountAfterDeduct) {
		this.amountAfterDeduct = amountAfterDeduct;
	}

	public String getCreditedAmount() {
		return creditedAmount;
	}

	public void setCreditedAmount(String creditedAmount) {
		this.creditedAmount = creditedAmount;
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public List<String> getBicList() {
		return bicList;
	}

	public void setBicList(List<String> bicList) {
		this.bicList = bicList;
	}

	public boolean isForwardToBeneficiaryCustomer() {
		return forwardToBeneficiaryCustomer;
	}

	public void setForwardToBeneficiaryCustomer(boolean forwardToBeneficiaryCustomer) {
		this.forwardToBeneficiaryCustomer = forwardToBeneficiaryCustomer;
	}

	public boolean isForwardToNonGpiAgent() {
		return forwardToNonGpiAgent;
	}

	public void setForwardToNonGpiAgent(boolean forwardToNonGpiAgent) {
		this.forwardToNonGpiAgent = forwardToNonGpiAgent;
	}

	public boolean isShowDetailsCreditedAmount() {
		return showDetailsCreditedAmount;
	}

	public void setShowDetailsCreditedAmount(boolean showDetailsCreditedAmount) {
		this.showDetailsCreditedAmount = showDetailsCreditedAmount;
	}

	public boolean isFindNonTracabel() {
		return findNonTracabel;
	}

	public void setFindNonTracabel(boolean findNonTracabel) {
		this.findNonTracabel = findNonTracabel;
	}

	public String getNonGpiBic() {
		return nonGpiBic;
	}

	public void setNonGpiBic(String nonGpiBic) {
		this.nonGpiBic = nonGpiBic;
	}

	public boolean isHasCovPayment() {
		return hasCovPayment;
	}

	public void setHasCovPayment(boolean hasCovPayment) {
		this.hasCovPayment = hasCovPayment;
	}

	public boolean isHasgSRPRequest() {
		return hasgSRPRequest;
	}

	public void setHasgSRPRequest(boolean hasgSRPRequest) {
		this.hasgSRPRequest = hasgSRPRequest;
	}

	public String getgSRPAgentResponse() {
		return gSRPAgentResponse;
	}

	public void setgSRPAgentResponse(String gSRPAgentResponse) {
		this.gSRPAgentResponse = gSRPAgentResponse;
	}

	public String getgSRPTrackerResponse() {
		return gSRPTrackerResponse;
	}

	public void setgSRPTrackerResponse(String gSRPTrackerResponse) {
		this.gSRPTrackerResponse = gSRPTrackerResponse;
	}

	public String getgSRPRequestReason() {
		return gSRPRequestReason;
	}

	public void setgSRPRequestReason(String gSRPRequestReason) {
		this.gSRPRequestReason = gSRPRequestReason;
	}

	public boolean isFromGpiDeatils() {
		return fromGpiDeatils;
	}

	public void setFromGpiDeatils(boolean fromGpiDeatils) {
		this.fromGpiDeatils = fromGpiDeatils;
	}

	public List<String> getNonGpiCoveAgent() {
		return nonGpiCoveAgent;
	}

	public void setNonGpiCoveAgent(List<String> nonGpiCoveAgent) {
		this.nonGpiCoveAgent = nonGpiCoveAgent;
	}

	public String getSatausCovImage() {
		return satausCovImage;
	}

	public void setSatausCovImage(String satausCovImage) {
		this.satausCovImage = satausCovImage;
	}

	public String getStatusCovStyle() {
		return statusCovStyle;
	}

	public void setStatusCovStyle(String statusCovStyle) {
		this.statusCovStyle = statusCovStyle;
	}

	public String getStatusMessageCov() {
		return statusMessageCov;
	}

	public void setStatusMessageCov(String statusMessageCov) {
		this.statusMessageCov = statusMessageCov;
	}

	public String getStatusMessageGpi() {
		return statusMessageGpi;
	}

	public void setStatusMessageGpi(String statusMessageGpi) {
		this.statusMessageGpi = statusMessageGpi;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(MessageDetails messageDetails) {
		this.messageDetails = messageDetails;
	}

	public GpiType getGpiType() {
		return gpiType;
	}

	public void setGpiType(GpiType gpiType) {
		this.gpiType = gpiType;
	}

	public boolean isHasIntermediaryGpiAgent() {
		return hasIntermediaryGpiAgent;
	}

	public void setHasIntermediaryGpiAgent(boolean hasIntermediaryGpiAgent) {
		this.hasIntermediaryGpiAgent = hasIntermediaryGpiAgent;
	}

	public String getSatausImage() {
		return satausImage;
	}

	public void setSatausImage(String satausImage) {
		this.satausImage = satausImage;
	}

	public boolean isInitialDraw() {
		return initialDraw;
	}

	public void setInitialDraw(boolean initialDraw) {
		this.initialDraw = initialDraw;
	}

	public boolean isNonTraceableBeneficiaryBank() {
		return nonTraceableBeneficiaryBank;
	}

	public void setNonTraceableBeneficiaryBank(boolean nonTraceableBeneficiaryBank) {
		this.nonTraceableBeneficiaryBank = nonTraceableBeneficiaryBank;
	}

	public String getStatusStyle() {
		return statusStyle;
	}

	public void setStatusStyle(String statusStyle) {
		this.statusStyle = statusStyle;
	}

	public Map<String, Integer> getCurrenciesISO() {
		return currenciesISO;
	}

	public void setCurrenciesISO(Map<String, Integer> currenciesISO) {
		this.currenciesISO = currenciesISO;
	}

	public boolean isUpdateFromIncoming() {
		return updateFromIncoming;
	}

	public void setUpdateFromIncoming(boolean updateFromIncoming) {
		this.updateFromIncoming = updateFromIncoming;
	}

	public boolean isShow71FCharges() {
		return show71FCharges;
	}

	public void setShow71FCharges(boolean show71fCharges) {
		show71FCharges = show71fCharges;
	}

	public String getAllDeductType() {
		return allDeductType;
	}

	public void setAllDeductType(String allDeductType) {
		this.allDeductType = allDeductType;
	}

	public List<String> getForwordingBicList() {
		return forwordingBicList;
	}

	public void setForwordingBicList(List<String> forwordingBicList) {
		this.forwordingBicList = forwordingBicList;
	}

}
