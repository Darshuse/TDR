package com.eastnets.enGpiLoader.bulder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eastnets.enGpiLoader.utility.DataSourceParser;
import com.eastnets.enGpiLoader.utility.Queue;
import com.eastnets.enGpiLoader.utility.XMLNotifierConfigHelper;

/**
 * @author MKassab
 * 
 * */
public class GpiQueryBulder { 
	private static final Logger LOGGER = Logger.getLogger(GpiQueryBulder.class); 
	public static String buildQuery(DataSourceParser dataSourceParser,boolean enabelDebug){ 

			LOGGER.debug("Start building query ... ");  
		String sql="";  
		XMLNotifierConfigHelper xmlNotifierConfigHelper = dataSourceParser.getxMLNotifierConfigHelper();  
		String mesgDirQuery=getMesgDirectionIfExsist(xmlNotifierConfigHelper.getMesgDirection());
		if(dataSourceParser.getXmlDataSource().getDbType().equalsIgnoreCase("mssql")){
			sql="select  ldGpHis.Confirma_DATE,ldGpHis.Mail_DATE,ldGpHis.mailAttempts,ldGpHis.confirmAttempts,mesg.x_fin_ccy,mesg.mesg_sender_swift_address,mesg.mesg_sub_format,mesg.mesg_sender_X1, m.aid,m.mesg_s_umidh,m.mesg_s_umidl,inst.inst_rp_name ,mesg_crea_date_time from ldLiveList m "
					+ "JOIN rMesg mesg  on m.aid = mesg.aid  AND m.mesg_s_umidl = mesg.mesg_s_umidl and m.mesg_s_umidh = mesg.mesg_s_umidh "
					+ "JOIN rInst inst on m.aid = inst.aid  AND m.mesg_s_umidl = inst.inst_s_umidl and m.mesg_s_umidh = inst.inst_s_umidh and inst_num=0 "
					+ "LEFT OUTER  JOIN ldGpiNotifersHistory ldGpHis  on m.aid = ldGpHis.aid  AND m.mesg_s_umidl = ldGpHis.mesg_s_umidl and m.mesg_s_umidh = ldGpHis.mesg_s_umidh "
					+ "where "+mesgDirQuery
					+ " mesg_type in ('103','202','205')   and ((ldGpHis.mailAttempts < ?  or ldGpHis.mailAttempts is null ) or (ldGpHis.confirmAttempts < ?  or ldGpHis.confirmAttempts is null ))   and  mesg_crea_date_time>=DATEADD(Day,datediff(day, 0,getdate()),?) and "+buildqueueQuery(xmlNotifierConfigHelper.getQueueMap());

		}else if(dataSourceParser.getXmlDataSource().getDbType().equalsIgnoreCase("oracle")) {
			sql="select  ldGpHis.Confirma_DATE,ldGpHis.Mail_DATE,ldGpHis.mailAttempts,ldGpHis.confirmAttempts,mesg.x_fin_ccy,mesg.mesg_sender_swift_address,mesg.mesg_sub_format,mesg.mesg_sender_X1, m.aid,m.mesg_s_umidh,m.mesg_s_umidl,inst.inst_rp_name ,mesg_crea_date_time from ldLiveList m "
					+ "JOIN rMesg mesg  on m.aid = mesg.aid  AND m.mesg_s_umidl = mesg.mesg_s_umidl and m.mesg_s_umidh = mesg.mesg_s_umidh "
					+ "JOIN rInst inst on m.aid = inst.aid  AND m.mesg_s_umidl = inst.inst_s_umidl and m.mesg_s_umidh = inst.inst_s_umidh and inst_num=0 "
					+ "LEFT OUTER  JOIN ldGpiNotifersHistory ldGpHis  on m.aid = ldGpHis.aid  AND m.mesg_s_umidl = ldGpHis.mesg_s_umidl and m.mesg_s_umidh = ldGpHis.mesg_s_umidh "
					+ "where"+mesgDirQuery
					+ " mesg_type in ('103','202','205')   and ((ldGpHis.mailAttempts < ?  or ldGpHis.mailAttempts is null ) or (ldGpHis.confirmAttempts < ?  or ldGpHis.confirmAttempts is null ))   and  (mesg_crea_date_time >= sysdate - ?) and "+buildqueueQuery(xmlNotifierConfigHelper.getQueueMap());	
		}
		return sql; 
	}
	
	
	private static String getMesgDirectionIfExsist(String direction ){
	return (direction != null && !direction.isEmpty()) ? "  UPPER(mesg_sub_format) like '"+direction.toUpperCase()+"' and ":"";	
	}
	
	private static String buildqueueQuery(Map<String, Queue> QueuesMap){ 
		int itemCount=1;
		int queueSize=QueuesMap.keySet().size();
		String allSelectedQueue="";
		String queryForqueue="inst.inst_rp_name IN (";
		for(String queueName:QueuesMap.keySet()){
			if(queueSize == itemCount){
				queueName="'"+queueName+"'"; 
			}else {
				queueName="'"+queueName+"'"+",";	
			} 
			allSelectedQueue=allSelectedQueue+queueName;
			itemCount++;  
		}
		queryForqueue=queryForqueue+allSelectedQueue+")"; 	
		return queryForqueue;
	}

	
/*	public static void main(String[] args) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
	}*/
	
	 public static void main(String[] argv) throws ParseException {

	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	        String dateInString = "7-Jun-2013";

	        try {

	            Date date = formatter.parse(dateInString);
	            System.out.println(date);
	            System.out.println(formatter.format(date));

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	    }

}
