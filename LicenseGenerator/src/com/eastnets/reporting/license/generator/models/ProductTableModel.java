/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.generator.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eastnets.reporting.licensing.beans.Product;

/**
 *
 * @author Mhattab
 */
public class ProductTableModel extends AbstractTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4035338986039968949L;
	private final List<Product> products;
    private final String[] headers;

    public ProductTableModel(List<Product> products, String... headers) {
        this.products = products;
        this.headers = headers;
    }

    @Override
    public ProductTableModel clone() {
        return new ProductTableModel(this.products, this.headers);
    }

    @Override
    public int getRowCount() {
        return this.products.size();
    }

    @Override
    public int getColumnCount() {
        return this.headers.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = this.products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getID();
            case 1:
                return product.getDescription();
            case 2:
                return (product.getExpirationDate() == null) ? null : new java.util.Date(product.getExpirationDate().getTime());
            default:
                return "unimlemented";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 2) {
            super.setValueAt(aValue, rowIndex, columnIndex);
        } else {
            Product product = this.products.get(rowIndex);
            product.setExpirationDate((aValue == null) ? null : new java.sql.Date(((java.util.Date) aValue).getTime()));
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public String getColumnName(int column) {
        return this.headers[column];
    }

    public List<Product> getSelectedProducts(int... selectedRows) {
        List<Product> selectedProducts = new ArrayList<Product>();
        for (int row : selectedRows) {
            selectedProducts.add(this.products.get(row));
        }
        return selectedProducts;
    }

    public Product getProductAt(int index) {
        return this.products.get(index);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) {
            return java.util.Date.class;
        } else {
            return String.class;
        }
    }

	/**
	 * @return the products
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @return the headers
	 */
	public String[] getHeaders() {
		return headers;
	}
}
