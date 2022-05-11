/**
 * 
 */
package com.eastnets.source;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.eastnets.encdec.AESEncryptDecrypt;

/**
 * 
 * @author EastNets
 * @since dNov 7, 2012
 * 
 */
public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7226705571069868140L;
	private ButtonGroup buttonGroup;
    private JButton encryptButton;
    private JButton decryptButton;
    private JLabel passwordLable;
    private JLabel encryptionLable;
    private JPanel mainPanel;
    private JRadioButton encryptRadioButton;
    private JRadioButton decryptRadioButton;
    private JTextField passwordTextField;
    private JTextField encryptionTextField;
	private JLabel errorLable;
	
	private final String decriptingError = "Could not decrypt the given value";
	
	/**
	 * 
	 */
	public MainFrame() {
		this.setTitle("Password Encryption");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initComponents();
		preparInitialValues();
        initLayout();
	}

	private void initLayout() {
		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(encryptionLable, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordTextField)
                            .addComponent(encryptionTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(encryptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decryptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorLable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(decryptRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(encryptRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(289, 289, 289))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(encryptRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decryptRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLable)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(encryptionLable)
                    .addComponent(encryptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(encryptButton)
                    .addComponent(decryptButton)
                    .addComponent(errorLable))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        
        // to center the application on the screen
        this.setLocationRelativeTo(null);
	}
	
	/**
	 * 
	 * @author EastNets
	 * @since Nov 7, 2012
	 */
	private void preparInitialValues() {
		encryptRadioButton.setSelected(true);
		decryptButton.setEnabled(false);
		encryptionTextField.setEditable(false);
		errorLable.setVisible(false);
	}

	/**
	 * 
	 * @author EastNets
	 * @since Nov 7, 2012
	 */
	private void initComponents() {
		mainPanel = new JPanel();
		
		buttonGroup = new ButtonGroup();
		
		encryptRadioButton = new JRadioButton("Encrypt");
		encryptRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				encryptionRadioButtonActionListener(evt);
			}
		});
	    
        decryptRadioButton = new JRadioButton("Decrypt");
        decryptRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                decryptionRadioButtonActionListener(evt);
            }
        });
        
        buttonGroup.add(encryptRadioButton);
        buttonGroup.add(decryptRadioButton);
        
        passwordLable = new JLabel("Password");
        passwordTextField = new JTextField();
        
        encryptionLable = new JLabel("Encryption");
        
        encryptionTextField = new JTextField();
        
        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                encryptButtonActionListener(evt);
            }
        });
        
        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                decryptButtonActionListener(evt);
            }
        });

		encryptRadioButton.setVisible(false);
		decryptRadioButton.setVisible(false);
		decryptButton.setVisible(false);
		
        errorLable = new JLabel();
        errorLable.setForeground(new Color(255, 0, 0));
		errorLable.setText(decriptingError);
        
		
	}

	private void decryptButtonActionListener(ActionEvent evt) {
		errorLable.setVisible(false);
		try {
			this.passwordTextField.setText(AESEncryptDecrypt.decrypt(this.encryptionTextField.getText()));
		} catch (Exception e) {
			errorLable.setVisible(true);
			errorLable.setText(decriptingError);
		}
	}
	
	private void encryptButtonActionListener(ActionEvent evt) {
		errorLable.setVisible(false);
		try {
			this.encryptionTextField.setText(AESEncryptDecrypt.encrypt(this.passwordTextField.getText()));
		} catch (Exception e) {
			errorLable.setVisible(true);
			errorLable.setText(decriptingError);
		}
	}

	private void decryptionRadioButtonActionListener(ActionEvent evt) {
		errorLable.setVisible(false);
		encryptButton.setEnabled(false);
		decryptButton.setEnabled(true);
		passwordTextField.setEditable(false);
		passwordTextField.setText("");
		encryptionTextField.setEditable(true);
	}

	private void encryptionRadioButtonActionListener(ActionEvent evt) {
		errorLable.setVisible(false);
		encryptButton.setEnabled(true);
		decryptButton.setEnabled(false);
		passwordTextField.setEditable(true);
		encryptionTextField.setEditable(false);
		encryptionTextField.setText("");
	}
	
	public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
        	JOptionPane.showMessageDialog(null, ex.getMessage());
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
        	JOptionPane.showMessageDialog(null, ex.getMessage());
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        	JOptionPane.showMessageDialog(null, ex.getMessage());
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	JOptionPane.showMessageDialog(null, ex.getMessage());
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
