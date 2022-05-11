/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.uploader.dialogs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.eastnets.reporting.licensing.beans.BicCode;
import com.eastnets.reporting.licensing.beans.TrafficBand;
import com.eastnets.reporting.licensing.util.BeanFactory;

public class BICCodeDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3300584743046017460L;
	public BicCode bic;
	private boolean isOK = false;
	private List<BicCode> bics = new ArrayList<BicCode>();

	public boolean isOK() {
		return this.isOK;
	}

	/**
	 * Creates new form BICCodeDialog
	 */
	public BICCodeDialog(java.awt.Frame parent, BicCode bic, List<BicCode> bics) {
		super(parent, ((bic == null) ? "New" : "Edit") + " BIC Code", true);
		this.bic = bic;
		this.bics = bics;
		initComponents();

		setLocationRelativeTo(parent);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Uploaded Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabelBICCode = new javax.swing.JLabel();
		jTextFieldBICCode = new javax.swing.JTextField();
		jLabelBand = new javax.swing.JLabel();
		jComboBoxBand = new javax.swing.JComboBox();
		jButtonCancel = new javax.swing.JButton();
		jButtonOK = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabelBICCode.setText("BIC Code:");

		jTextFieldBICCode.setText((this.bic == null) ? "" : this.bic.getBicCode());
		jTextFieldBICCode.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

		jLabelBand.setText("Band:");

		jComboBoxBand.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-1: < 250", "0: < 500", "1: < 1,000", "2: < 2,000", "3: < 5,000", "4: < 20,000", "5: < 50,000", "6: < 100,000", "7: < 250,000", "8: < 500,000" }));
		jComboBoxBand.setSelectedIndex((this.bic == null) ? 0 : this.bic.getBand().ordinal());

		jButtonCancel.setText("Cancel");
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCancelActionPerformed(evt);
			}
		});

		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonOKActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelBICCode).addComponent(jLabelBand))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextFieldBICCode).addComponent(jComboBoxBand, 0, 104, Short.MAX_VALUE)))
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jButtonOK).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonCancel)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelBICCode)
								.addComponent(jTextFieldBICCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelBand).addComponent(jComboBoxBand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonCancel).addComponent(jButtonOK))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOKActionPerformed
		try {
			boolean edit = bic != null;

			TrafficBand band = TrafficBand.values()[this.jComboBoxBand.getSelectedIndex()];
			bic = BeanFactory.getInstance().getNewBICCode(this.jTextFieldBICCode.getText(), band, band.getBandVolume());

			if (this.bics.contains(bic) && !edit) {
				throw new Exception("could not instantiate a bic code is duplicate");
			}

			this.isOK = true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.toString(), "Invalid BIC code", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.setVisible(false);
	}// GEN-LAST:event_jButtonOKActionPerformed

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCancelActionPerformed
		this.setVisible(false);
	}// GEN-LAST:event_jButtonCancelActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main_(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(BICCodeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(BICCodeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(BICCodeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(BICCodeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				BICCodeDialog dialog = new BICCodeDialog(new javax.swing.JFrame(), null, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonOK;
	private javax.swing.JComboBox jComboBoxBand;
	private javax.swing.JLabel jLabelBICCode;
	private javax.swing.JLabel jLabelBand;
	private javax.swing.JTextField jTextFieldBICCode;
	// End of variables declaration//GEN-END:variables
}
