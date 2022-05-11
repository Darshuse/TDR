/**
 * 
 */
package com.eastnets.reporting.license.generator;

import com.eastnets.reporting.license.generator.frames.MainFrame;

/**
 * 
 * @author EastNets
 * @since dDec 11, 2012
 * 
 */
public class LicenseGenerator {

	public static String licParamsFile = "LicParams.xml";

	public static final String VERSION = "2.2.3";

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		if (args.length > 0) {
			LicenseGenerator.licParamsFile = args[0];
		}

		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see http://download.oracle.com/javase /tutorial/uiswing/lookandfeel/plaf.html
		 */
		// final ApplicationContext context = new
		// ClassPathXmlApplicationContext("LicensingBeans.xml");
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});

		/*
		 * LicensePdfGenerator licensePdfGenerator = new LicensePdfGenerator(); try { licensePdfGenerator.generateLicensePdf(); } catch (ParserConfigurationException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } catch (SAXException e) { // TODO Auto-generated catch block e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

}
