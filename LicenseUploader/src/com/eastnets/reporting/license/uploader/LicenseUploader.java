package com.eastnets.reporting.license.uploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.eastnets.config.ConfigBean;
import com.eastnets.config.DBType;
import com.eastnets.reporting.license.uploader.dialogs.UserParamsDialog;
import com.eastnets.reporting.license.uploader.frames.MainFrame;

/**
 * 
 * @author EastNets
 * @since dJan 10, 2014
 * 
 */
public class LicenseUploader {

	public final static String DEFAULT_LIC_PARAMS_FILE = "LicParams.xml";

	public static String licParamsFile = DEFAULT_LIC_PARAMS_FILE; 

	public static final String VERSION = "2.0.0";

	private static HashMap<String, String> mapUserParams = new HashMap<String, String>();

	private static String usageString = null;

	static {
		StringBuilder sbUsage = new StringBuilder("License Uploader\n");
		sbUsage.append("Copyright 1999-2014 EastNets\n" );
		sbUsage.append("Usage : \n");
		sbUsage.append(  "  -cnfg\t\t: license application configuraton file\n");
		sbUsage.append(  "  -U\t\t: user name for database connection\n");
		sbUsage.append(  "  -P\t\t: password for databasae connection\n");
		sbUsage.append(  "  -IP\t\t: The IP address or server name for database connection, default is localhost.\n");
		sbUsage.append(  "  -dbtype\t: oracle or mssql database, default is oracle\n");
		sbUsage.append(  "  -port\t\t: database port, default 1521 for oracle and 1433 for mssql\n");
		sbUsage.append(  "  -dbname\t: database name\n");
		sbUsage.append(  "  -instanceName\t: instance name\n");
		usageString = sbUsage.toString();
	}
		
	private static String[] getUserParamsGUI() throws Exception
	{
		if(0 == mapUserParams.size()) {
			displayUsage("", true);
		}
		UserParamsDialog userParams = new UserParamsDialog(null, true, mapUserParams, usageString);
		userParams.setVisible(true);
		if(userParams.isOK()) {
			ArrayList<String> arrArgs = new ArrayList<String>();
			mapUserParams.put("-cnfg", userParams.Product_License_configurations_file());
			mapUserParams.put("-U", userParams.database_user_name());
			mapUserParams.put("-P", userParams.Password());
			mapUserParams.put("-IP", userParams.database_IP_host());
			mapUserParams.put("-dbtype", userParams.Database_provider());
			mapUserParams.put("-port", userParams.database_port());
			mapUserParams.put("-dbname", userParams.database_name());
			mapUserParams.put("-instanceName", userParams.database_instance_name());
			for(Entry<String, String> entry : mapUserParams.entrySet()) {
				if(null != entry.getValue() && !entry.getValue().isEmpty()) {
					arrArgs.add(entry.getKey());
					arrArgs.add(entry.getValue());
				}
			}
			return arrArgs.toArray(new String[0]);
		}
		else {
			System.exit(0);
			return null;
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		final MainFrame theMainFrame = new MainFrame();;
		try{
			while(0 == args.length || !loadParameters(args) || !checkDBConnection() || !theMainFrame.CmdParamsInit()) {
				// this while may exit if cancel is chosen in the user params dialog which leads to exit
				// or everything goes alright ;)
				args = getUserParamsGUI();
			}
			
			/* Set the Nimbus look and feel */
			// <editor-fold defaultstate="collapsed"
			// desc=" Look and feel setting code (optional) ">
			/*
			 * If Nimbus (introduced in Java SE 6) is not available, stay with the
			 * default look and feel. For details see
			 * http://download.oracle.com/javase
			 * /tutorial/uiswing/lookandfeel/plaf.html
			 */
			// final ApplicationContext context = new
			// ClassPathXmlApplicationContext("LicensingBeans.xml");

			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				theMainFrame.setVisible(true);
			}
		});
	}
	
	private static boolean checkDBConnection() throws Exception {
		ConfigBean cnfgBean = LicenseUploaderBase.getAppConfigBean();
		ArrayList<String> arrMissingParams = new ArrayList<String>();
		if( cnfgBean.getServerName() == null || cnfgBean.getServerName().length() <= 0)
			arrMissingParams.add("-IP");
		if( cnfgBean.getUsername() == null || cnfgBean.getUsername().length() <= 0 )
			arrMissingParams.add("-U");
		if( cnfgBean.getPassword() == null || cnfgBean.getPassword().length() <= 0 )
			arrMissingParams.add("-P");
		if( cnfgBean.getPortNumber() == null || cnfgBean.getPortNumber().length() <= 0 )
			arrMissingParams.add("-port");
		if( cnfgBean.getDatabaseName() == null || cnfgBean.getDatabaseName().length() <= 0)
			arrMissingParams.add("-dbname");
		if(arrMissingParams.size() > 0) {
			displayUsage(StringUtils.join(arrMissingParams, ','), true);
			return false;
		}
		else {
			return true;
		}
	}

	private static boolean loadParameters(String[] args) throws Exception {
		try {
			mapUserParams.clear();
			ConfigBean configBean = LicenseUploaderBase.getAppConfigBean();
			
			//default config values
			licParamsFile = DEFAULT_LIC_PARAMS_FILE;
			// for database connection
			configBean.setDatabaseType(DBType.ORACLE) ;
			configBean.setPortNumber("1521");
			configBean.setServerName("localhost");
			configBean.setUsername(null);
			configBean.setPassword(null);
			configBean.setDatabaseName(null);
			configBean.setInstanceName(null);
			
			boolean bPortGiven = false;

			for (int i=0; i<args.length;i++){
				String value = args[i];
				if("-cnfg".equals(value)){
					if(i++ < args.length){
						LicenseUploader.licParamsFile = args[i];
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-U".equals(value)){
					if(i++ < args.length){
						configBean.setUsername(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-P".equals(value)){
					if(i++ < args.length){
						configBean.setPasswordText(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-IP".equals(value)){
					if(i++ < args.length){
						configBean.setServerName(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-dbname".equals(value)){
					if(i++ < args.length){
						configBean.setDatabaseName(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-dbtype".equals(value)){
					if(i++ < args.length){
						if(args[i].toLowerCase().equals("mssql")) {
							configBean.setDatabaseType(DBType.MSSQL);
							if(!bPortGiven) {
								configBean.setPortNumber("1433");
							}
						} else if(args[i].toLowerCase().equals("oracle")) {
							configBean.setDatabaseType(DBType.ORACLE);
							if(!bPortGiven) {
								configBean.setPortNumber("1521");
							}
						} else {
							displayUsage(args[i], true);
							return false;
						}
						mapUserParams.put(value, args[i]);
						/*commented the below code and replaced with the above implemenatation, this is to solve the case:
						 * -port option given before the -dbtype option, with different port number.
						configBean.setDatabaseType(args[i].toLowerCase().equals("msql") ? DBType.MSSQL : DBType.ORACLE);
						if(args[i].toLowerCase().equals("msql") && configBean.getPortNumber().equals("1521"))
							configBean.setPortNumber("1433");
						*/
					} else {
						displayUsage(value, true);
						return false;
					}
				}else if("-port".equals(value)){
					bPortGiven = true;
					if(i++ < args.length){
						configBean.setPortNumber(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else if("-instanceName".equals(value)){
					if(i++ < args.length){
						configBean.setInstanceName(args[i]);
						mapUserParams.put(value, args[i]);
					}else{
						displayUsage(value, true);
						return false;
					}
				}else{
					displayUsage(value, true);
					return false;
				}
			}
		} catch(Exception ex) {
			displayUsage("", true);
			return false;
		}
		return true;
	}

	private static void displayUsage(String value, boolean bGUI) throws Exception {
		String outString = String.format("%s: missing or invalid paramter\n%s", value, usageString);
		System.out.println(outString);
		
		if(bGUI) {
			JOptionPane.showMessageDialog(null, outString, "EastNets Licensing Tool", JOptionPane.ERROR_MESSAGE, null);
		}
	}
}

