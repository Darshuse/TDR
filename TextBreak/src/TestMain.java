import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.eastnets.resilience.textparser.Syntax;
import com.eastnets.resilience.textparser.bean.ParsedMessage;
import com.eastnets.resilience.textparser.exception.RequiredNotFound;
import com.eastnets.resilience.textparser.exception.SyntaxNotFound;
import com.eastnets.resilience.textparser.exception.UnrecognizedBlockException;
import com.eastnets.resilience.textparser.syntax.Message;


/**
 * 
 * @author EHakawati
 * 
 */

public class TestMain {

	/**
	 * Test Main
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws SyntaxNotFound
	 * @throws RequiredNotFound
	 * @throws UnrecognizedBlockException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, SyntaxNotFound,
			RequiredNotFound, UnrecognizedBlockException {

		
		
//		StringUtils.getFinToRegexFormat("D 3d");
		
//		ResourceBundle bundle = ResourceBundle.getBundle("fin_to_regex");
//		for(String str : bundle.keySet() ) {
//			System.out.println( str  + "=" + StringUtils.getFinToRegexFormat(str));
//			System.out.println(str);
//		}
	
		long time = System.currentTimeMillis();

		Class.forName("oracle.jdbc.OracleDriver");

		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.50.74:1525/ORCL", "side", "max");
		
		/*Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.59.132:1433;DatabaseName=sidedb;user=side;Password=edis");*/
		//jdbc:sqlserver://localhost:1433;DatabaseName=YourDBName;user=UserName;Password=YourPassword

		//Message messageParser = Syntax.getParser("1205", "542", conn);
		//Message messageParser = Syntax.getParser("1205", "103", conn);
		//Message messageParser = Syntax.getParser("1305", "300", conn);
		Message messageParser = Syntax.getParser("1205", "360", conn);

		System.out.println(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
		ParsedMessage list = null;
		try {			
			/*list = messageParser.parseMessageText( "\r\n"
					+ ":15A:\r\n"
					+ ":20:Sender-Ref\r\n"
					+ ":22A:DUPL\r\n"
					+ ":22C:PTSAMM0012PTSAMM\r\n"
					+ ":82A:PTSALUIIXXX\r\n"
					+ ":87A:PTSALUIIXXX\r\n"
					+ ":15B:\r\n"
					+ ":30T:20110307\r\n"
					+ ":30V:20110301\r\n"
					+ ":36:12,0\r\n"
					+ ":32B:EGP50,000\r\n"
					+ ":57A:PTSALUIIXXX\r\n"
					+ ":33B:EGP30,000\r\n"
					+ ":57A:PTSALUIIXXX\r\n"
					+ ":15E:\r\n"
					+ ":81J:/NAME/Sameer\r\n"
					+ ":96J:/NAME/welcome\r\n"
					+ "/CLRC/home\r\n"
					+ ":22S:N/die\r\n"
					+ ":22S:N/die\r\n"
					+ ":22S:N/die\r\n"
					+ ":22S:N/my darling\r\n"
					+ ":22T:not a single\r\n"
					+ ":22U:1234"
					
					
					
					
					);*/
			list = messageParser.parseMessageText( "\r\n" +
					":15O:\r\n" +
					":22L:vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\r\n" +
					":91A:/lkjkjj\r\n" +
					"PTSABEMMXXX\r\n" +
					":22M:cccccc/ccc\r\n" +
					":22N:11111111111111111111111111111111\r\n" +
					":22P:lllllll/ll\r\n" +
					":22R:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					":96D:/qqqqqqqqq\r\n" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					":22S:P/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					":22T:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n" +
					":17E:Y\r\n" +
					":22U:IRSOIS\r\n" +
					":17H:P\r\n" +
					":17P:U" +
					":22V:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					":98D:20130808235959,592/12" +
					":17W:Y" +
					":77A:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
					"xxxxxxxxxxxxxxxxxxxxxxxxx"

					);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		//list.getParsedLoops();
		if(list != null) {
			//messageParser.debugObject(list.getParsedFields());
			System.out.println(list.getExpandedMessage(new Timestamp(System.currentTimeMillis()), null , null,null));
		}
		//messageParser.debugObject(list.getParsedFields());
		
		

		System.out.println(System.currentTimeMillis() - time);
	}

}
