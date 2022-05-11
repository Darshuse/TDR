package com.eastnets.domain.viewer;

import java.util.List;

public class DaynamicViewerParam {

	private String loggedInUser;
	private ViewerSearchParam params;
	private String listFilter;
	private Integer listMax;
	private Integer timeZoneOffset;
	private List<FieldSearchInfo> fieldSearch;
	private Boolean addColums;
	private Boolean showInternalMessages;
	private Integer textDecompostionType;
	private Boolean caseSensitive = false;
	private Boolean includeSysMessages;
	private Boolean enableUnicodeSearch;
	private Boolean enableGpiSearch;
	private String decimalAmountFormat;
	private String thousandAmountFormat;
	private String amountValidationMsg = "";
	private Long groupId;
	private int pageNumber;
	private boolean webService;

	public DaynamicViewerParam() {
		// TODO Auto-generated constructor stub
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public ViewerSearchParam getParams() {
		return params;
	}

	public void setParams(ViewerSearchParam params) {
		this.params = params;
	}

	public String getListFilter() {
		return listFilter;
	}

	public void setListFilter(String listFilter) {
		this.listFilter = listFilter;
	}

	public int getListMax() {
		return listMax;
	}

	public void setListMax(int listMax) {
		this.listMax = listMax;
	}

	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public List<FieldSearchInfo> getFieldSearch() {
		return fieldSearch;
	}

	public void setFieldSearch(List<FieldSearchInfo> fieldSearch) {
		this.fieldSearch = fieldSearch;
	}

	public boolean isAddColums() {
		return addColums;
	}

	public void setAddColums(boolean addColums) {
		this.addColums = addColums;
	}

	public boolean isShowInternalMessages() {
		return showInternalMessages;
	}

	public void setShowInternalMessages(boolean showInternalMessages) {
		this.showInternalMessages = showInternalMessages;
	}

	public int getTextDecompostionType() {
		return textDecompostionType;
	}

	public void setTextDecompostionType(int textDecompostionType) {
		this.textDecompostionType = textDecompostionType;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isIncludeSysMessages() {
		return includeSysMessages;
	}

	public void setIncludeSysMessages(boolean includeSysMessages) {
		this.includeSysMessages = includeSysMessages;
	}

	public boolean isEnableUnicodeSearch() {
		return enableUnicodeSearch;
	}

	public void setEnableUnicodeSearch(boolean enableUnicodeSearch) {
		this.enableUnicodeSearch = enableUnicodeSearch;
	}

	public boolean isEnableGpiSearch() {
		return enableGpiSearch;
	}

	public void setEnableGpiSearch(boolean enableGpiSearch) {
		this.enableGpiSearch = enableGpiSearch;
	}

	public String getDecimalAmountFormat() {
		return decimalAmountFormat;
	}

	public void setDecimalAmountFormat(String decimalAmountFormat) {
		this.decimalAmountFormat = decimalAmountFormat;
	}

	public String getThousandAmountFormat() {
		return thousandAmountFormat;
	}

	public void setThousandAmountFormat(String thousandAmountFormat) {
		this.thousandAmountFormat = thousandAmountFormat;
	}

	public String getAmountValidationMsg() {
		return amountValidationMsg;
	}

	public void setAmountValidationMsg(String amountValidationMsg) {
		this.amountValidationMsg = amountValidationMsg;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isWebService() {
		return webService;
	}

	public void setWebService(boolean webService) {
		this.webService = webService;
	}

}
