package com.eastnets.service.viewer.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.eastnets.dao.viewer.ViewerDAO;
import com.eastnets.domain.Pair;
import com.eastnets.domain.TreeNode;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.ViewerSearchParam;

/**
 * ReportSummary class is used to generate financial summary for messages
 * @author SAlababneh
 */
public class ReportSummary {
	private List<SearchResultEntity> messages;
	private Date maxValueDate ;
	private Date minValueDate ;
	private ViewerSearchParam params ;
	
	//private int[] messagesWithFinInfo = {103, 110, 191, 200, 202, 210, 300, 320, 400, 410, 412, 420, 430, 456, 541, 543, 545, 547, 548, 566, 578, 700, 707, 710, 732, 734, 740, 742, 752, 754, 756, 790, 791, 900, 910, 940, 942, 950};
	//private List<String> messagesWithFinancialInfo= new ArrayList<String>( ); 

	public ReportSummary(List<SearchResultEntity> messages, ViewerDAO viewerDAO , ViewerSearchParam params){
		this.messages= messages;
		this.params = params ;
	}
	
	/**
	 * @return root node of a tree, the tree looks like 
	 * 
	 *                                |-- CURRENCY1 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT ) 
	 *        |--SENDER:RECEIVER 1 ---|-- CURRENCY2 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT )
	 *        |                       |-- CURRENCY3 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT )
	 *        |					      ......
	 * root --|
	 *        |
	 *        |                       |-- CURRENCY1 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT )
	 *        |--SENDER:RECEIVER 1 ---|-- CURRENCY2 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT )
	 *        |                       |-- CURRENCY3 .value= Pair( TOTAL_AMOUNT, AMOUNT_COUNT )
	 *        					      ......
	 *        ......
	 * @throws Exception 
	 */
	
	@SuppressWarnings("unchecked")
	public TreeNode<String> getReportSummary() throws Exception{
		TreeNode<String> root = new TreeNode<String>("root");
		
		if ( messages == null ){
			return root;
		}
		calculateTheDays();
		if(maxValueDate == null )
		 maxValueDate = messages.get(0).getxFinValueDate();
		if(minValueDate == null)
		 minValueDate = messages.get(0).getxFinValueDate();
		for ( SearchResultEntity message : messages ){
			if (   message.getxFinCcy() == null || message.getxFinCcy().trim().isEmpty() 
				|| message.getxFinAmount()== null || message.getxFinAmount().doubleValue() == 0){
					continue;
			}
			
			String senderReceiver= message.getMesgSenderX1() + ":" + message.getInstReceiverX1();
			TreeNode<String> senderReceiverNode= root.getChild(senderReceiver);
			
			if( senderReceiverNode == null ){
				senderReceiverNode=root.addChild(senderReceiver);
			}
			
			String currency= message.getxFinCcy();
			TreeNode<String> currencyNode= senderReceiverNode.getChild(currency);
			if( currencyNode == null ){
				currencyNode=senderReceiverNode.addChild(currency);	
			}
			
			Pair<BigDecimal, Integer> amount = null;
			if ( currencyNode.getValue() != null && Pair.class.isInstance(currencyNode.getValue())){
				amount= (Pair<BigDecimal, Integer>)currencyNode.getValue();
			}
			
			if (amount == null){
				amount = new Pair<BigDecimal, Integer>(new BigDecimal(0), 0);
				currencyNode.setValue(amount);
			}
			amount.setKey(amount.getKey().add(message.getxFinAmount()));
			amount.setValue(amount.getValue() + 1 );			

			if(params.getContentValueDateTo() == null && message.getxFinValueDate() != null ){
				if(maxValueDate==null || message.getxFinValueDate().after(maxValueDate)){
					maxValueDate = message.getxFinValueDate();
				}	
			}
			if(params.getContentValueDateFrom() == null && message.getxFinValueDate() != null){
				if(minValueDate==null || message.getxFinValueDate().before(minValueDate)){
					minValueDate = message.getxFinValueDate();
				}	
			}
		}
		
		return root;
	}

	public void calculateTheDays(){
		if(params.getContentValueDateFrom() != null){
			minValueDate = params.getContentValueDateFrom();
		}if(params.getContentValueDateTo() != null){
			maxValueDate = params.getContentValueDateTo();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getReportSummaryString() throws Exception {
		String summary= "";
		
		TreeNode<String> reportSummaryTree= getReportSummary();
		boolean isThereValueDate=false;
		Long interval=1l;
		Double day= 1.0  ;
		Double week= 1.0  ;
		Double month= 1.0  ;
		Double year= 1.0  ;
		if(maxValueDate!= null && minValueDate !=null){
			isThereValueDate=true;
			interval = maxValueDate.getTime() - minValueDate.getTime();
			
			if(interval==0){
				day = 1.0 ;
				week = 1.0 ;
				month = 1.0 ; 
				year = 1.0;
			}else{
			 day = (double) ((interval/(1000*60*60))/24)+1;
			 week = day / 7 ;
			 month = day / 30 ;
			 year = day / 365 ;
			}
		}
		
		for (TreeNode<String> senderReceiver : reportSummaryTree.getChildren() ){
			String senderReceiverStr = senderReceiver.getData();
			for ( TreeNode<String> currency : senderReceiver.getChildren() ){
				String currencyStr = currency.getData();
				BigDecimal  amount = ((Pair<BigDecimal, Integer>)currency.getValue()).getKey();
				Integer  count = ((Pair<BigDecimal, Integer>)currency.getValue()).getValue();

				String[] bics = senderReceiverStr.split(":");
				
				DecimalFormat df = new DecimalFormat("#.##");
				summary += addRow(bics[0], bics[1], currencyStr, df.format(amount).toString(), count.toString(), df.format( amount.doubleValue() / count ),df.format( amount.doubleValue() / day )+" | "+df.format( count / day )
						,df.format( amount.doubleValue() / week )+" | "+df.format( count / week ),df.format( amount.doubleValue() / month )+" | "+df.format( count / month ),
						df.format( amount.doubleValue() / year )+" | "+df.format( count / year ), "\n" );
			}
		}
		if ( summary.endsWith("\n") ){
			summary= summary.substring(0, summary.length() - 1 );
		}
		return summary;
	}
	
	public Date getMaxValueDate() {
		return maxValueDate;
	}

	public void setMaxValueDate(Date maxValueDate) {
		this.maxValueDate = maxValueDate;
	}

	public Date getMinValueDate() {
		return minValueDate;
	}

	public void setMinValueDate(Date minValueDate) {
		this.minValueDate = minValueDate;
	}

	public String getReportSummaryHeader() {
		return addRow("Sender", "Receiver", "Currency", "Total Amount", "Messages", "Avg Amount", "Avg(Val|Vol)/Day","Avg(Val|Vol)/Week","Avg(Val|Vol)/Month","Avg(Val|Vol)/Year","");
	}
	
	private String addRow(String sender, String receiver, String currency, String totalAmount, String totalMessages, String averageAmount, String dailyAverage , String weeklyAverage , String monthlyAverage , String yearlyAverage , String separator) {
		return addItem( sender, 12 ) + addItem( receiver, 12 ) + addItem( currency, 9 ) + addItem( totalAmount, 14 ) + addItem( totalMessages, 9 )+ addItem(averageAmount,18) + addItem(dailyAverage,18) + 
				addItem(weeklyAverage,18) + addItem(monthlyAverage,20) + yearlyAverage+separator;
	}

	private String addItem(String item, int width ) {
		int length = width - item.length() ;
		if ( length < 1 ){
			System.out.println("Formatting \"" + item + "\" exceeded the column width (" + width  + ") ");
			length= 1;
		}
		return String.format("%s%" + length + "s" , item, "");
	}
}
