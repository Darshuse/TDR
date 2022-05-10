import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.syntax.Message;

public class SyntaxGui {
	
	static JTextField serverTextField= new JTextField(10);
	static JTextField userTextField= new JTextField(10);
	static JTextField passwordTextField= new JPasswordField(10);

	static JTextField mesgTypeTextField= new JTextField(10);
	
	static ButtonGroup dbtypeGroup = new ButtonGroup();
	static ButtonGroup versionGroup = new ButtonGroup();
    
	static Preferences prefs;
	
  public static void main(String[] args) {
    JFrame frame = new JFrame("Syntax");
    prefs = Preferences.userRoot().node(frame.getClass().getName());
    
    JPanel entreePanelM = new JPanel();
    entreePanelM.setLayout(new FlowLayout());
    JPanel entreePanel = new JPanel();
        
    //Oracle or mssql
    JRadioButton radioButton= new JRadioButton("Oracle");
    radioButton.setActionCommand("Oracle");
    radioButton.setSelected(true);
    entreePanel.add(radioButton);
    dbtypeGroup.add(radioButton);
    
    radioButton = new JRadioButton("Sql Server");
    radioButton.setActionCommand("Sql Server");
    entreePanel.add(radioButton);
    dbtypeGroup.add(radioButton);
    
    //syntax version 
    JPanel versionPanel = new JPanel();
    JRadioButton radioButton2;
    
    versionPanel.add(radioButton2 = new JRadioButton("1805"));
    radioButton2.setActionCommand("1805");
    radioButton2.setSelected(true);
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1705"));
    radioButton2.setActionCommand("1705");
    radioButton2.setSelected(true);
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1605"));
    radioButton2.setActionCommand("1605");
    radioButton2.setSelected(true);
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1505"));
    radioButton2.setActionCommand("1505");
    //radioButton2.setSelected(true);
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1405"));
    radioButton2.setActionCommand("1405");
    versionGroup.add(radioButton2);

    versionPanel.add(radioButton2 = new JRadioButton("1305"));
    radioButton2.setActionCommand("1305");
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1205"));
    radioButton2.setActionCommand("1205");
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1105"));
    radioButton2.setActionCommand("1105");
    versionGroup.add(radioButton2);
    
    versionPanel.add(radioButton2 = new JRadioButton("1005"));
    radioButton2.setActionCommand("1005");
    versionGroup.add(radioButton2);
    
    versionPanel.add( new JLabel("Message Type") );
    versionPanel.add(mesgTypeTextField);

    		 
    //server data 
    Box serverPanel = Box.createHorizontalBox();
    serverPanel.add(new JLabel("Server"));
    serverPanel.add(serverTextField);
    serverPanel.add(new JLabel("User"));
    serverPanel.add(userTextField);
    serverPanel.add(new JLabel("Password"));
    serverPanel.add(passwordTextField);
    
    entreePanelM.add(entreePanel);
    entreePanelM.add(versionPanel);
    
    Box topArea = Box.createVerticalBox();
    topArea.add(entreePanelM);
    topArea.add( serverPanel );
    
    
    //text and expanded text
    final JTextArea unexpandedTextArea = new JTextArea(35, 63);
    JScrollPane unexpandedScrollPane = new JScrollPane(unexpandedTextArea); 
    unexpandedTextArea.setFont(new Font("Monospaced", Font.BOLD,12));
    
    final JTextArea expandedTextArea = new JTextArea(35, 100);
    expandedTextArea.setEditable(false);
    JScrollPane expandedScrollPane = new JScrollPane(expandedTextArea); 
    expandedTextArea.setFont(new Font("Monospaced", Font.BOLD,12));
    
    
    JPanel textPanel = new JPanel();

    Box unexpandedTextPanel = Box.createVerticalBox();
    Box expandedTextPanel = Box.createVerticalBox();
    unexpandedTextPanel.add (new JLabel("UnExpanded Text") );
    unexpandedTextPanel.add( unexpandedScrollPane );
    expandedTextPanel.add (new JLabel("Expanded Text") );
    expandedTextPanel.add( expandedScrollPane );
    
    
    //textPanel.add( entreePanelM );
    textPanel.add( unexpandedTextPanel );
    textPanel.add( expandedTextPanel );
   
    

    //actions 
    JPanel expandPanel = new JPanel();
    JButton expandButton = new JButton("Expand");
    expandPanel.add(expandButton);

    
    //the window, and its layout 
    frame.setLayout( new BorderLayout());
    
    frame.add(BorderLayout.NORTH, topArea);
    frame.add(BorderLayout.CENTER, textPanel);    
    frame.add(BorderLayout.SOUTH,  expandPanel);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 750);
    frame.setResizable(false);
    frame.setVisible(true);
    
    //load old data
    serverTextField.setText(prefs.get("Server", ""));
	userTextField.setText(prefs.get("User", ""));
	passwordTextField.setText(prefs.get("Password", ""));
    
    //action
    expandButton.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent ae) {
        
        //get data from gui
        boolean dbTypeOracle = dbtypeGroup.getSelection().getActionCommand().equalsIgnoreCase("Oracle");
        String version = versionGroup.getSelection().getActionCommand();
        String server = serverTextField.getText().trim();
        String user = userTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        String mesgType = mesgTypeTextField.getText().trim();
        

        //save preferences
        prefs.put("Server", server);
        prefs.put("User", user);
        prefs.put("Password", password);
        
        //start expanding
        Connection conn= null;
        if ( dbTypeOracle ){
        	try {
				Class.forName("oracle.jdbc.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@" + server, user, password);
        	} catch (Exception e) {
        		expandedTextArea.setText( "Database Error:" + e.getMessage() );
        		return;
			} 
        }
        else{
        	try {
	        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    		conn = DriverManager.getConnection("jdbc:sqlserver://" + server + ";DatabaseName=sidedb;user=" + user + ";Password=" + password);
        	} catch (Exception e) {
        		expandedTextArea.setText( "Database Error:" + e.getMessage() );
        		return;
			} 
        }
        if ( conn == null ){
        	expandedTextArea.setText( "Database Connection null" );
    		return;
        }
        
        try{
        	Message messageParser = Syntax.getParser(version, mesgType, conn);
        	String text= unexpandedTextArea.getText().trim();
        	text= text.replace("\r\n", "\n");
        	text= text.replace("\r", "");
        	text= "\r\n" + text.replace("\n", "\r\n");
        	ParsedMessage list = messageParser.parseMessageText( text );
        	expandedTextArea.setText( list.getExpandedMessage(new Timestamp(System.currentTimeMillis()), null , null,null) );
        }
        catch( Exception e ){
        	expandedTextArea.setText( "Database Error:" + e.getMessage() );
    		return;
        }
      }
    });
    
  }
}