/**
 * Copyright (c) 2012 EastNets
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EastNets ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with EastNets. 
 */

package com.eastnets.service.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.eastnets.dao.dashboard.DashboardDAO;
import com.eastnets.domain.dashboard.ChartRecord;
import com.eastnets.domain.dashboard.MultiSeriesChartRecord;
import com.eastnets.service.ServiceBaseImp;
import com.eastnets.service.dashboard.chartsproperties.ColumnChartProperties;

/**
 * Dashboard Service Implementation
 * @author EastNets
 * @since July 22, 2012
 */
/**
 * @author MAlTaweel
 *
 */
public class DashboardServiceImp extends ServiceBaseImp implements DashboardService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7748000957343970438L;
	private DashboardDAO dashboardDAO;
	private ColumnChartProperties messagesPerCountryChartProperties;
	private ColumnChartProperties messagesPerCurrencyChartProperties;
	private ColumnChartProperties amountPerCategoryChartProperties;
	private ColumnChartProperties sentReceivedMessagesPerLogicalTerminalChartProperties;
	private ColumnChartProperties messagesCountPerCategoryChartProperties;
	private ColumnChartProperties notificationsChartProperties;
	private ColumnChartProperties loaderNewMessagesChartProperties;
	private ColumnChartProperties loaderNewEventsChartProperties;
	private ColumnChartProperties loaderUpdatedMessagesChartProperties;


	private static LinkedHashMap<Integer, String> colors;

	static {
		colors = new LinkedHashMap<Integer, String>();
		colors.put(0, "AFD8F8");
		colors.put(1, "8BBA00");
		colors.put(2, "F6BD0F");
		colors.put(3, "0000FF");
		colors.put(4, "D64646");
		colors.put(5, "008E8E");
		colors.put(6, "B3AA00");
		colors.put(7, "8E468E");
		colors.put(8, "A186BE");
		colors.put(9, "588526");
		colors.put(10, "D119D1");
		colors.put(11, "008ED6");
		colors.put(12, "669900");
		colors.put(13, "9D080D");
		colors.put(14, "FF0000");
		colors.put(15, "666666");
		colors.put(16, "FF8E46");
		colors.put(17, "003366");
		colors.put(18, "F984A1");
		colors.put(19, "0099CC");
		colors.put(20, "7C7CB4");
		colors.put(21, "999966");
		colors.put(22, "FF9933");
		colors.put(23, "1941A5");
		colors.put(24, "CCCCFF");
		colors.put(25, "669966");

}

	@Override
	public String buildMessagesPerCountryChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID) {
		List<ChartRecord> records = dashboardDAO.getMessagesCountPerCountry(dateFrom, dateTo, loggedUserGroupID);

		StringBuilder reportInfo = getGraphNode(messagesPerCountryChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	@Override
	public String buildMessagesPerCurrencyChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID) {
		List<ChartRecord> records = dashboardDAO.getMessagesCountPerCurrency(dateFrom, dateTo, loggedUserGroupID);

		StringBuilder reportInfo = getGraphNode(messagesPerCurrencyChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	@Override
	public String buildAmountPerCategoryChartXml(String loggedInUser, Date dateFrom, Date dateTo, String currency, long loggedUserGroupID) {
		List<ChartRecord> records = dashboardDAO.getAmountPerCategory(dateFrom, dateTo, currency, loggedUserGroupID);

		StringBuilder reportInfo = getGraphNode(amountPerCategoryChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	@Override
	public String buildSendRecievedMessagesPerLogicalTerminalChartXml(String loggedInUser, Date dateFrom, Date dateTo, long loggedUserGroupID) {
		
		List<MultiSeriesChartRecord> messageCountPerCategoryList = dashboardDAO.getSentReceivedMessagesCountPerLogicalTerminal(dateFrom, dateTo, loggedUserGroupID);
		
		Set<String> chartCategories = new LinkedHashSet<String>();

		for (MultiSeriesChartRecord record : messageCountPerCategoryList) {
			String categoryName = record.getChartCategoryName();			
			chartCategories.add(categoryName);
		}
		
		StringBuilder reportInfo = new StringBuilder();
		reportInfo.append(getGraphNode(sentReceivedMessagesPerLogicalTerminalChartProperties, isRotateXAxisValues(chartCategories)));
		reportInfo.append(getCategoriesNode(chartCategories));
		
		//Prepere the data
		LinkedHashMap<String, Map<String, String>> dataValues = new LinkedHashMap<String, Map<String,String>>();
		
		for(MultiSeriesChartRecord MSChartrecord : messageCountPerCategoryList) {
			ChartRecord chartRecord = MSChartrecord.getChartRecord();
			String messageFormat = chartRecord.getName();
			if(messageFormat.equalsIgnoreCase("INPUT")) {
				messageFormat = "Sent";
			} else if(messageFormat.equalsIgnoreCase("OUTPUT")) {
				messageFormat = "Received";
			}
			
			if(!dataValues.containsKey(MSChartrecord.getChartCategoryName())) {
				Map<String, String> categoriesValues = new HashMap<String, String>();
				categoriesValues.put(messageFormat, chartRecord.getValue());
				dataValues.put(MSChartrecord.getChartCategoryName(), categoriesValues);
			} else {
				Map<String, String> map = dataValues.get(MSChartrecord.getChartCategoryName());
				map.put(messageFormat, chartRecord.getValue());
				}
			}
	
			
		reportInfo.append(getDatasetNodes(dataValues));
		
		reportInfo.append("</graph>");
		return reportInfo.toString();
		
	}

	@Override
	public Map<String,List<MultiSeriesChartRecord>> getMessagesCountPerValueDate(String loggedInUser,	Date dateFrom, Date dateTo, long loggedUserGroupID){
		
		List<MultiSeriesChartRecord> messageCountPerCategoryList = dashboardDAO.getMessageCountPerCategory(dateFrom, dateTo, loggedUserGroupID);
		
		Map<String,List<MultiSeriesChartRecord>> retVal = new TreeMap<String, List<MultiSeriesChartRecord>>();
		
		for (MultiSeriesChartRecord record : messageCountPerCategoryList) {
			
			String categoryName = record.getChartCategoryName();			
			List<MultiSeriesChartRecord> list = retVal.get(categoryName);
			if(list == null) {
				list = new ArrayList<MultiSeriesChartRecord>();
			}
			list.add(record);
			retVal.put(categoryName, list);
		}
		
		return retVal;
	}
	
	@Override
	public String buildMessageCountPerCategoryChartXml(String loggedInUser, String category, List<MultiSeriesChartRecord> MultiSeriesChartRecordList)
	{
		LinkedHashMap<String, Map<String, String>> dataValues = new LinkedHashMap<String, Map<String,String>>();
		Map<String, String> categoriesValues = new HashMap<String, String>();
		dataValues.put(category, categoriesValues);
		List<ChartRecord> chartRecordsList = new ArrayList<ChartRecord>();
		for (MultiSeriesChartRecord MSChartrecord : MultiSeriesChartRecordList) {
			
			ChartRecord chartRecord = MSChartrecord.getChartRecord();
			chartRecordsList.add(chartRecord);
			String name = chartRecord.getName();
			String value = chartRecord.getValue();
			categoriesValues.put(name, value);
		}
		
		StringBuilder reportInfo = getGraphNode(messagesCountPerCategoryChartProperties, isRotateXAxisValues(chartRecordsList));
		reportInfo.append(getRecordsNodes(chartRecordsList, true));
		
		reportInfo.append("</graph>");
		return reportInfo.toString();
	}
	
	@Override
	public String buildMessageCountPerCategoryChartXml(String loggedInUser,	Date dateFrom, Date dateTo, long loggedUserGroupID) {
		List<MultiSeriesChartRecord> messageCountPerCategoryList = dashboardDAO.getMessageCountPerCategory(dateFrom, dateTo, loggedUserGroupID);
		
		Set<String> chartCategories = new LinkedHashSet<String>();
		for (MultiSeriesChartRecord record : messageCountPerCategoryList) {
			String categoryName = record.getChartCategoryName();			
			chartCategories.add(categoryName);
		}
		
		StringBuilder reportInfo = new StringBuilder();
		reportInfo.append(getGraphNode(messagesCountPerCategoryChartProperties, isRotateXAxisValues(messageCountPerCategoryList)));
		reportInfo.append(getCategoriesNode(chartCategories));
		
		//Prepare the data
		LinkedHashMap<String, Map<String, String>> dataValues = new LinkedHashMap<String, Map<String,String>>();
		
		for(MultiSeriesChartRecord MSChartrecord : messageCountPerCategoryList) {
			ChartRecord chartRecord = MSChartrecord.getChartRecord();
			if(dataValues.containsKey(MSChartrecord.getChartCategoryName())) {
				Map<String, String> map = dataValues.get(MSChartrecord.getChartCategoryName());
				map.put(chartRecord.getName(), chartRecord.getValue());
			} else {
				Map<String, String> categoriesValues = new HashMap<String, String>();
				categoriesValues.put(chartRecord.getName(), chartRecord.getValue());
				dataValues.put(MSChartrecord.getChartCategoryName(), categoriesValues);
			}
		}
	
			
		reportInfo.append(getDatasetNodes(dataValues));
		
		reportInfo.append("</graph>");
		return reportInfo.toString();
		
	}
	
	@Override
	public String buildNotificationsChartXml(String loggedInUser, Date dateFrom, Date dateTo, String tableName) {
		
		List<ChartRecord> records = null;
		
		if(tableName.equals("Message")) {
			records = dashboardDAO.getMessageNotifications(dateFrom, dateTo);
		} else if (tableName.equals("Event") ){
			records = dashboardDAO.getEventNotifications(dateFrom, dateTo);
		}
		//records = fillRecordGaps(records);
		
		StringBuilder reportInfo = getGraphNode(notificationsChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records, false));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	@Override
	public String buildLoaderNewMessagesChartXml(String loggedInUser, Date date, String aid) {
		
		List<ChartRecord> records = fillRecordGaps(dashboardDAO.getLoaderNewMessages(date, aid));
		
		
		StringBuilder reportInfo = getGraphNode(loaderNewMessagesChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records, false));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	
	/**
	 * return new list, where new records are added for hours that don't contains records. The new values have zero values.
	 * @param records
	 * @return
	 */
	private List<ChartRecord> fillRecordGaps ( List<ChartRecord> records)
	{
		
		List<ChartRecord> recordsGraph=new ArrayList<ChartRecord>();
		int lastHour=0;
		int currHour=0;
		for (int i = 0; i < records.size(); i++) {
			
			String name = records.get(i).getName();
			if( i==0)//first record
			{
				recordsGraph.add(records.get(i));
				lastHour=Integer.parseInt(name);
				continue;
			}
			currHour=Integer.parseInt(name);
			
			for(int k=lastHour+1; k<currHour;k++)
			{   
				ChartRecord gapRecord = new ChartRecord();
				gapRecord.setName(String.valueOf(k));
				gapRecord.setValue(String.valueOf(0));
				recordsGraph.add(gapRecord);
			}
			recordsGraph.add(records.get(i));
			lastHour=Integer.parseInt(name);
		}
		return recordsGraph;
	}
	
	@Override
	public String buildLoaderNewEventsChartXml(String loggedInUser, Date date, String aid) {
		
		List<ChartRecord> records = fillRecordGaps(dashboardDAO.getLoaderNewEvents(date, aid));

		StringBuilder reportInfo = getGraphNode(loaderNewEventsChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records, false));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}
	
	@Override
	public String buildLoaderUpdateMessagesChartXml(String loggedInUser, Date date, String aid) {
		
		List<ChartRecord> records = fillRecordGaps(dashboardDAO.getLoaderUpdatedMessages(date, aid));

		StringBuilder reportInfo = getGraphNode(loaderUpdatedMessagesChartProperties, isRotateXAxisValues(records));
		reportInfo.append(getRecordsNodes(records, false));
		// close the graph node
		reportInfo.append("</graph>");

		return reportInfo.toString();
	}

	private String getDatasetNodes(LinkedHashMap<String, Map<String, String>> dataValues) {
		Set<String> datasetNames = new HashSet<String>();
		
		for(String key : dataValues.keySet()) {
			Map<String, String> valuesMap = dataValues.get(key);
			for(String datasetName : valuesMap.keySet()) {
				datasetNames.add(datasetName);
			}
		}
		
		List<String> sortedDatasetNames = new ArrayList<String>(datasetNames);
        Collections.sort(sortedDatasetNames);
		
		StringBuilder reportInfo = new StringBuilder();
		int colorIndex = 0;
		for (String datasetName : sortedDatasetNames) {
			if(colorIndex == colors.size() -1) {
				colorIndex = 0;
			}
			
			reportInfo.append("<dataset ");
			reportInfo.append("seriesname=");
			reportInfo.append("'");
			reportInfo.append(datasetName);
			reportInfo.append("' ");
			reportInfo.append("color=");
			reportInfo.append("'");
			reportInfo.append(colors.get(colorIndex));
			reportInfo.append("'");
			reportInfo.append(">");
			
			for(String key : dataValues.keySet()) {
				
				Map<String, String> valuesMap = dataValues.get(key);
				
				reportInfo.append("<set ");
				reportInfo.append("value=");
				reportInfo.append("'");
				
				if(!valuesMap.keySet().contains(datasetName)) {
					reportInfo.append("0");
				} else {
					reportInfo.append(valuesMap.get(datasetName));
				} 
				
				reportInfo.append("'");
				reportInfo.append("/>");
			}
			
			colorIndex++;
			reportInfo.append("</dataset>");
		}
		
		return reportInfo.toString();
		
	}
	
	@Override
	public String buildEmptyGraph() {
		String emptyXMLGraph = "<graph caption=''  xAxisName='Empty Graph' yAxisName='Empty Graph' showNames='1' showValues='0'></graph>";
		return emptyXMLGraph;
	}

	private Object getCategoriesNode(Set<String> keySet) {
		StringBuilder node = new StringBuilder();
		
		node.append("<categories>");
		for(String category : keySet) {
			node.append("<category name='");
			node.append(category);
			node.append("'/> ");
		}
		
		node.append("</categories>");
		
		return node;
	}

	private StringBuilder getRecordsNodes(List<ChartRecord> records) {
		return getRecordsNodes(records, true);
	}
	
	private StringBuilder getRecordsNodes(List<ChartRecord> records, boolean enableMultiColors) {
		StringBuilder reportInfo = new StringBuilder();
		boolean allValuesAreZeros = true;
		for(ChartRecord record : records) {
			if(record.getValue() != null && !record.getValue().equals("") && !record.getValue().equals("0") ) {
				allValuesAreZeros = false;
				break;
			}
		}
		
		if(allValuesAreZeros) {
			return reportInfo;
		}
		
//		if(records.size() == 1) {
//			ChartRecord record = new ChartRecord();
//			record.setName("0");
//			record.setValue("0");
//			records.add(0, record);
//		}

		int colorIndex = 0;
		for (int i = 0; i < records.size(); i++) {
			ChartRecord record = records.get(i);
			reportInfo.append("<set ");
			reportInfo.append("name=\"");
			reportInfo.append(record.getName());
			if(record.getName2() != null){
				reportInfo.append(", ");
				reportInfo.append(record.getName2());
			}
			reportInfo.append("\" ");

			reportInfo.append("value='");
			reportInfo.append(record.getValue());
			reportInfo.append("' ");

			if (colorIndex == colors.size() - 1) {
				colorIndex = 0;
			}
			
			reportInfo.append("color='");
			if(enableMultiColors) {
				reportInfo.append(colors.get(colorIndex));
			} else {
				reportInfo.append("9AC6DB");
			}
			reportInfo.append("' ");
			reportInfo.append("/>\n");

			colorIndex++;
		}

		return reportInfo;
	}
	
	private StringBuilder getGraphNode(ColumnChartProperties chartProperties, boolean rotateXAxisValues) {
		StringBuilder reportInfo = new StringBuilder();
		reportInfo.append("<graph");
		
		reportInfo.append(" caption='");
		reportInfo.append(chartProperties.getReportName());
		reportInfo.append("' ");

		reportInfo.append(" xAxisName='");
		reportInfo.append(chartProperties.getxAxisName());
		reportInfo.append("'");

		reportInfo.append(" yAxisName='");
		reportInfo.append(chartProperties.getyAxisName());
		reportInfo.append("'");

		reportInfo.append(" showNames='");
		reportInfo.append(chartProperties.getShowNames());
		reportInfo.append("'");
		
		reportInfo.append(" showValues='");
		reportInfo.append(chartProperties.getShowValues());
		reportInfo.append("'");

		if(rotateXAxisValues) {
			reportInfo.append(" rotateNames='");
			reportInfo.append(chartProperties.getRotateNames());
			reportInfo.append("'");		
		}
	
		reportInfo.append(">");
		reportInfo.append("\n");
		
		return reportInfo;
	}
	
	private boolean isRotateXAxisValues(Collection<?> collection) {
		if(collection == null) {
			return false;
		}
		
		return collection.size() >= 6;
	}

	public DashboardDAO getDashboardDAO() {
		return dashboardDAO;
	}

	public void setDashboardDAO(DashboardDAO dashboardDAO) {
		this.dashboardDAO = dashboardDAO;
	}

	public ColumnChartProperties getMessagesPerCountryChartProperties() {
		return messagesPerCountryChartProperties;
	}

	public void setMessagesPerCountryChartProperties(
			ColumnChartProperties messagesPerCountryChartProperties) {
		this.messagesPerCountryChartProperties = messagesPerCountryChartProperties;
	}

	public ColumnChartProperties getMessagesPerCurrencyChartProperties() {
		return messagesPerCurrencyChartProperties;
	}

	public void setMessagesPerCurrencyChartProperties(
			ColumnChartProperties messagesPerCurrencyChartProperties) {
		this.messagesPerCurrencyChartProperties = messagesPerCurrencyChartProperties;
	}

	public ColumnChartProperties getAmountPerCategoryChartProperties() {
		return amountPerCategoryChartProperties;
	}

	public void setAmountPerCategoryChartProperties(
			ColumnChartProperties amountPerCategoryChartProperties) {
		this.amountPerCategoryChartProperties = amountPerCategoryChartProperties;
	}

	public ColumnChartProperties getSentReceivedMessagesPerLogicalTerminalChartProperties() {
		return sentReceivedMessagesPerLogicalTerminalChartProperties;
	}

	public void setSentReceivedMessagesPerLogicalTerminalChartProperties(
			ColumnChartProperties sentReceivedMessagesPerLogicalTerminalChartProperties) {
		this.sentReceivedMessagesPerLogicalTerminalChartProperties = sentReceivedMessagesPerLogicalTerminalChartProperties;
	}

	public ColumnChartProperties getMessagesCountPerCategoryChartProperties() {
		return messagesCountPerCategoryChartProperties;
	}

	public void setMessagesCountPerCategoryChartProperties(
			ColumnChartProperties messagesCountPerCategoryChartProperties) {
		this.messagesCountPerCategoryChartProperties = messagesCountPerCategoryChartProperties;
	}

	public ColumnChartProperties getNotificationsChartProperties() {
		return notificationsChartProperties;
	}

	public void setNotificationsChartProperties(
			ColumnChartProperties notificationsChartProperties) {
		this.notificationsChartProperties = notificationsChartProperties;
	}

	public ColumnChartProperties getLoaderNewMessagesChartProperties() {
		return loaderNewMessagesChartProperties;
	}

	public void setLoaderNewMessagesChartProperties(
			ColumnChartProperties loaderNewMessagesChartProperties) {
		this.loaderNewMessagesChartProperties = loaderNewMessagesChartProperties;
	}

	public ColumnChartProperties getLoaderNewEventsChartProperties() {
		return loaderNewEventsChartProperties;
	}

	public void setLoaderNewEventsChartProperties(
			ColumnChartProperties loaderNewEventsChartProperties) {
		this.loaderNewEventsChartProperties = loaderNewEventsChartProperties;
	}

	public ColumnChartProperties getLoaderUpdatedMessagesChartProperties() {
		return loaderUpdatedMessagesChartProperties;
	}

	public void setLoaderUpdatedMessagesChartProperties(
			ColumnChartProperties loaderUpdatedMessagesChartProperties) {
		this.loaderUpdatedMessagesChartProperties = loaderUpdatedMessagesChartProperties;
	}
}