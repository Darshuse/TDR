/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.generator.renderers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 *
 * @author Mhattab
 */
public class DateTableCellEditorRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2989575149730971635L;
	private JDateChooser dateChooser;

    public DateTableCellEditorRenderer() {
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setOpaque(true);
        //*
        JTextFieldDateEditor jTextFieldDateEditor = (JTextFieldDateEditor) dateChooser.getComponent(1);
        jTextFieldDateEditor.setEditable(false);
        jTextFieldDateEditor.getDocument().addDocumentListener(new DocumentListener() {

         @Override
         public void removeUpdate(DocumentEvent arg0) {
          DateTableCellEditorRenderer.this.fireEditingStopped();    
         }

         @Override
         public void insertUpdate(DocumentEvent arg0) {
          DateTableCellEditorRenderer.this.fireEditingStopped();
         }

         @Override
         public void changedUpdate(DocumentEvent arg0) {
          DateTableCellEditorRenderer.this.fireEditingStopped();    
         }});
         //*/
    }

    @Override
    public Object getCellEditorValue() {
        return this.dateChooser.getDate();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.dateChooser.setDate((Date) value);
        return this.dateChooser;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.dateChooser.setDate((Date) value);
        return this.dateChooser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        stopCellEditing();
    }
}