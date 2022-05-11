/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastnets.reporting.license.uploader.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Mhattab
 */
public class UserParamsDialog extends javax.swing.JDialog {

    /**
     *
     */
    private static final long serialVersionUID = -1307197868733895525L;

    private String usage = null;

    /**
     * Creates new form UserParamsDialog
     */
    public UserParamsDialog(java.awt.Frame parent, boolean modal, HashMap<String, String> mapUserParams, String usageString) {
        super(parent, modal);
        initComponents();
        this.usage = usageString;
        if(null != mapUserParams) {
        	try {
        		String licParamsFileName = mapUserParams.get("-cnfg");
        		if(licParamsFileName != null && licParamsFileName.length() > 0) {
        			cmb_Product_License_configurations_file.setSelectedItem(licParamsFileName);
        			if(!licParamsFileName.equals(cmb_Product_License_configurations_file.getSelectedItem())) {
        				cmb_Product_License_configurations_file.addItem(licParamsFileName);
            			cmb_Product_License_configurations_file.setSelectedItem(licParamsFileName);
        			}
        		}
        		else {
        			cmb_Product_License_configurations_file.setSelectedIndex(0);
        		}
        	} catch(Exception e){};
            try {
        		cmb_Database_provider.setSelectedItem(mapUserParams.get("-dbtype"));
        	} catch(Exception e){};
        	try {
            	edt_Password.setText(mapUserParams.get("-P"));
        	} catch(Exception e){};
        	try {
        		edt_database_IP_host.setText(mapUserParams.get("-IP"));
        	} catch(Exception e){};
    		try {
        		edt_database_instance_name.setText(mapUserParams.get("-instanceName"));
        	} catch(Exception e){};
    		try {
        		edt_database_name.setText(mapUserParams.get("-dbname"));
        	} catch(Exception e){};
    		try {
        		edt_database_port.setText(mapUserParams.get("-port"));
        	} catch(Exception e){};
    		try {
        		edt_database_user_name.setText(mapUserParams.get("-U"));
        	} catch(Exception e){};
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        
//        edt_database_port.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                char vChar = e.getKeyChar();
//                if (!Character.isDigit(vChar) || KeyEvent.VK_BACK_SPACE == vChar || KeyEvent.VK_DELETE == vChar || edt_database_port.getText().length() >= 10) {
//                        e.consume();
//                    }
//                }
//            });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        lbl_Product_License_configurations_file = new javax.swing.JLabel();
        cmb_Product_License_configurations_file = new javax.swing.JComboBox();
        lbl_database_user_name = new javax.swing.JLabel();
        edt_database_user_name = new javax.swing.JTextField();
        lbl_Password = new javax.swing.JLabel();
        lbl_Database_provider = new javax.swing.JLabel();
        cmb_Database_provider = new javax.swing.JComboBox();
        lbl_database_IP_host = new javax.swing.JLabel();
        edt_database_IP_host = new javax.swing.JTextField();
        lbl_database_port = new javax.swing.JLabel();
        edt_database_port = new javax.swing.JTextField();
        lbl_database_name = new javax.swing.JLabel();
        edt_database_name = new javax.swing.JTextField();
        lbl_database_instance_name = new javax.swing.JLabel();
        edt_database_instance_name = new javax.swing.JTextField();
        btn_Cancel = new javax.swing.JButton();
        btn_OK = new javax.swing.JButton();
        btn_Usage = new javax.swing.JButton();
        edt_Password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EastNets Licensing Tool; user parameters");
        setModal(true);
        setResizable(false);

        lbl_Product_License_configurations_file.setDisplayedMnemonic('p');
        lbl_Product_License_configurations_file.setLabelFor(cmb_Product_License_configurations_file);
        lbl_Product_License_configurations_file.setText("Product/License configurations file:");
        lbl_Product_License_configurations_file.setToolTipText("-cnfg");

        cmb_Product_License_configurations_file.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Reporting*", "DDA*" }));
        cmb_Product_License_configurations_file.setSelectedIndex(0);
        cmb_Product_License_configurations_file.setToolTipText("default is Reporting, or supply a full path in the command line.");
        cmb_Product_License_configurations_file.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lbl_database_user_name.setDisplayedMnemonic('t');
        lbl_database_user_name.setLabelFor(edt_database_user_name);
        lbl_database_user_name.setText("database user name:");
        lbl_database_user_name.setToolTipText("-U");

        edt_database_user_name.setToolTipText("max 50 characters. Mandatory");
        edt_database_user_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
            }
        });

        lbl_Password.setDisplayedMnemonic('s');
        lbl_Password.setLabelFor(edt_Password);
        lbl_Password.setText("Password:");
        lbl_Password.setToolTipText("-P");

        lbl_Database_provider.setDisplayedMnemonic('d');
        lbl_Database_provider.setLabelFor(cmb_Database_provider);
        lbl_Database_provider.setText("Database provider:");
        lbl_Database_provider.setToolTipText("-dbtype");

        cmb_Database_provider.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "oracle", "mssql" }));
        cmb_Database_provider.setSelectedIndex(0);
        cmb_Database_provider.setToolTipText("default is oracle");

        lbl_database_IP_host.setDisplayedMnemonic('i');
        lbl_database_IP_host.setLabelFor(edt_database_IP_host);
        lbl_database_IP_host.setText("database IP/host:");
        lbl_database_IP_host.setToolTipText("-IP");

        edt_database_IP_host.setToolTipText("max 50 characters, keep empty for default: localhost");
        edt_database_IP_host.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
            }
        });

        lbl_database_port.setDisplayedMnemonic('a');
        lbl_database_port.setLabelFor(edt_database_port);
        lbl_database_port.setText("database port:");
        lbl_database_port.setToolTipText("-port");

        edt_database_port.setToolTipText("max 10 integers, keep empty for default: 1521 for oracle and 1433 for mssql");
        edt_database_port.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
                OnKeyTyped_Port(evt);
            }
        });

        lbl_database_name.setDisplayedMnemonic('b');
        lbl_database_name.setLabelFor(edt_database_name);
        lbl_database_name.setText("database name:");
        lbl_database_name.setToolTipText("-dbname");

        edt_database_name.setToolTipText("max 50 characters");
        edt_database_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
            }
        });

        lbl_database_instance_name.setDisplayedMnemonic('n');
        lbl_database_instance_name.setLabelFor(edt_database_instance_name);
        lbl_database_instance_name.setText("database instance name:");
        lbl_database_instance_name.setToolTipText("-instanceName");

        edt_database_instance_name.setToolTipText("max 50 characters");
        edt_database_instance_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
            }
        });

        btn_Cancel.setMnemonic('c');
        btn_Cancel.setText("Cancel");
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        btn_OK.setMnemonic('o');
        btn_OK.setText("OK");
        btn_OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OKActionPerformed(evt);
            }
        });

        btn_Usage.setMnemonic('u');
        btn_Usage.setText("Usage");
        btn_Usage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UsageActionPerformed(evt);
            }
        });

        edt_Password.setToolTipText("max 50 characters. Mandatory");
        edt_Password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Product_License_configurations_file)
                            .addComponent(lbl_Database_provider)
                            .addComponent(lbl_database_IP_host)
                            .addComponent(lbl_database_port)
                            .addComponent(lbl_database_user_name)
                            .addComponent(lbl_Password)
                            .addComponent(lbl_database_name)
                            .addComponent(lbl_database_instance_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmb_Product_License_configurations_file, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_Database_provider, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_database_IP_host, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_database_port, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_database_user_name, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_database_name, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_database_instance_name, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_Usage)
                        .addGap(128, 128, 128)
                        .addComponent(btn_OK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Product_License_configurations_file)
                    .addComponent(cmb_Product_License_configurations_file, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Database_provider)
                    .addComponent(cmb_Database_provider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_database_IP_host)
                    .addComponent(edt_database_IP_host, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_database_port)
                    .addComponent(edt_database_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_database_user_name)
                    .addComponent(edt_database_user_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Password)
                    .addComponent(edt_Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_database_name)
                    .addComponent(edt_database_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_database_instance_name)
                    .addComponent(edt_database_instance_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancel)
                    .addComponent(btn_OK)
                    .addComponent(btn_Usage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_UsageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UsageActionPerformed
        JOptionPane.showMessageDialog(this, this.usage, "EastNets Licensing Tool", JOptionPane.INFORMATION_MESSAGE, null);
    }//GEN-LAST:event_btn_UsageActionPerformed

    private boolean isOK = false;

    public boolean isOK() {
        return isOK;
    }

    private void btn_OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OKActionPerformed
        isOK = true;
        setVisible(false);
    }//GEN-LAST:event_btn_OKActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btn_CancelActionPerformed

    private void OnKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OnKeyTyped
        if (((JTextField)evt.getComponent()).getText().length() >= Integer.parseInt(((JTextField)evt.getComponent()).getToolTipText().substring(4, 6))) {
                evt.consume();
            }
    }//GEN-LAST:event_OnKeyTyped

    private void OnKeyTyped_Port(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OnKeyTyped_Port
        char vChar = evt.getKeyChar();
        if (!Character.isDigit(vChar) || KeyEvent.VK_BACK_SPACE == vChar || KeyEvent.VK_DELETE == vChar) {
                evt.consume();
            }
    }//GEN-LAST:event_OnKeyTyped_Port

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(UserParamsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(UserParamsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(UserParamsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(UserParamsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                UserParamsDialog dialog = new UserParamsDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    public String Product_License_configurations_file() {
        return (String)cmb_Product_License_configurations_file.getSelectedItem();
    }

    public String Database_provider() {
        return (String)cmb_Database_provider.getSelectedItem();
    }

    public String database_IP_host() {
        return edt_database_IP_host.getText();
    }

    public String database_port() {
        return edt_database_port.getText();
    }

    public String database_user_name() {
        return edt_database_user_name.getText();
    }

    public String Password() {
        return String.valueOf(edt_Password.getPassword());
    }

    public String database_name() {
        return edt_database_name.getText();
    }

    public String database_instance_name() {
        return edt_database_instance_name.getText();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_OK;
    private javax.swing.JButton btn_Usage;
	private javax.swing.JComboBox cmb_Database_provider;
    private javax.swing.JComboBox cmb_Product_License_configurations_file;
    private javax.swing.JPasswordField edt_Password;
    private javax.swing.JTextField edt_database_IP_host;
    private javax.swing.JTextField edt_database_instance_name;
    private javax.swing.JTextField edt_database_name;
    private javax.swing.JTextField edt_database_port;
    private javax.swing.JTextField edt_database_user_name;
    private javax.swing.JLabel lbl_Database_provider;
    private javax.swing.JLabel lbl_Password;
    private javax.swing.JLabel lbl_Product_License_configurations_file;
    private javax.swing.JLabel lbl_database_IP_host;
    private javax.swing.JLabel lbl_database_instance_name;
    private javax.swing.JLabel lbl_database_name;
    private javax.swing.JLabel lbl_database_port;
    private javax.swing.JLabel lbl_database_user_name;
    // End of variables declaration//GEN-END:variables
}
