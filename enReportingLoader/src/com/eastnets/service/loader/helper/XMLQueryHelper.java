package com.eastnets.service.loader.helper;

public class XMLQueryHelper {
	private String tableName;
	private String seqNumb;
	private String txtCol;
	private String historyCol;
	private String mesgType;
	private String where;
	private String queryName;
	private String unitName;
	private String creationDate;
	private String creationOperator;

	public XMLQueryHelper() {
	}

	public XMLQueryHelper(String queryName, String tableName, String seqNumb, String txtCol,String historyCol, String mesgType, String where, String unitName, String creationDate,
			String creationOperator) {
		this.queryName = queryName;
		this.tableName = tableName;
		this.seqNumb = seqNumb;
		this.txtCol = txtCol;
		this.historyCol=historyCol;
		this.mesgType = mesgType;
		this.where = where;
		this.unitName = unitName;
		this.creationDate = creationDate;
		this.creationOperator = creationOperator;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSeqNumb() {
		return seqNumb;
	}

	public void setSeqNumb(String seqNumb) {
		this.seqNumb = seqNumb;
	}

	public String getTxtCol() {
		return txtCol;
	}

	public void setTxtCol(String txtCol) {
		this.txtCol = txtCol;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationOperator() {
		return creationOperator;
	}

	public void setCreationOperator(String creationOperator) {
		this.creationOperator = creationOperator;
	}

	@Override
	public String toString() {
		return "XMLQueryHelper [tableName=" + tableName + ", seqNumb=" + seqNumb + ", txtCol=" + txtCol + ", mesgType=" + mesgType + ", where=" + where + "]";
	}

	public String getHistoryCol() {
		return historyCol;
	}

	public void setHistoryCol(String historyCol) {
		this.historyCol = historyCol;
	}

}
