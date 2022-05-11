/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.uploader.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eastnets.reporting.licensing.beans.BicCode;

public class BICsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5857430631567714360L;
	protected final List<BicCode> bics;

	public BICsTableModel(List<BicCode> bics) {
		this.bics = bics;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new BICsTableModel(this.bics);
	}

	@Override
	public int getRowCount() {
		return this.bics.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BicCode bic = this.bics.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return bic.getBicCode();
		case 1:
			return bic.getBand().ordinal() - 1;
		case 2:
			return bic.getBandVolume();
		default:
			return "unimlemented";
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "BIC code";
		case 1:
			return "Band";
		case 2:
			return "Volume";
		default:
			return "unimplemented";
		}
	}
}
