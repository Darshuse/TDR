package com.eastnets.domain.viewer;

import java.io.Serializable;

public class TableColumnsHeader implements Comparable<TableColumnsHeader>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8707332647947274687L;
	private Integer id;
	private String columnName;
	private boolean checked;
	private Integer rowOrder;
	private boolean seletedByDefault;
	private boolean havebeinReOrdered;

	/**
	 * column Width is in pixel
	 */
	private Integer columnWidth;

	public TableColumnsHeader(Integer id, String columnName, boolean checked, Integer rowOrder, boolean seletedByDefault, Integer columnWidth) {
		this.id = id;
		this.columnName = columnName;
		this.checked = checked;
		this.rowOrder = rowOrder;
		this.seletedByDefault = seletedByDefault;
		this.columnWidth = columnWidth;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(Integer columnWidth) {
		this.columnWidth = columnWidth;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public boolean isSeletedByDefault() {
		return seletedByDefault;
	}

	public void setSeletedByDefault(boolean seletedByDefault) {
		this.seletedByDefault = seletedByDefault;
	}

	@Override
	public int compareTo(TableColumnsHeader o) {
		return getRowOrder().compareTo(o.getRowOrder());

	}

	public boolean isHavebeinReOrdered() {
		return havebeinReOrdered;
	}

	public void setHavebeinReOrdered(boolean havebeinReOrdered) {
		this.havebeinReOrdered = havebeinReOrdered;
	}

}
