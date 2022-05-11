package com.eastnets.dao;


public class Queries {

	
	public static final String GET_MESSAGES_ORACLE_QUERY = 
    " SELECT m.AID,m.MESG_S_UMIDL,m.MESG_S_UMIDH,m.MESG_CREA_DATE_TIME,p.APPE_SEQ_NBR , m.X_RECEIVER_X1, m.FIELD_CODE, " +
    " m.FIELD_OPTION,m.VALUE " + 
	" FROM rsmesgtextfield m, rappe p " + 
	" WHERE m.aid = p.aid AND M.MESG_S_UMIDH = P.APPE_S_UMIDH AND M.MESG_S_UMIDL = P.APPE_S_UMIDL " +
	" ANd m.mesg_type='103' AND p.appe_iapp_name = 'SWIFT' AND p.x_appe_last = 1 AND m.mesg_sub_format='INPUT' " + 
	" AND p.appe_type = 'APPE_EMISSION' AND p.appe_network_delivery_status = 'DLV_ACKED' AND p.appe_inst_num = 0 " + 
	" AND m.MESG_CREA_DATE_TIME  between to_date ( ? ,'yyyy/MM/dd HH24:mi:ss') and to_date ( ? ,'yyyy/MM/dd HH24:mi:ss') " + 
	" AND m.X_OWN_LT = ? " + 
	" order by  m.MESG_CREA_DATE_TIME";

}
