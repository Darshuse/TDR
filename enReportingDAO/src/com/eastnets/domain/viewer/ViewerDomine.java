package com.eastnets.domain.viewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap; 

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils; 

import com.eastnets.dao.common.Constants;
import com.eastnets.domain.AdvancedDate;
import com.eastnets.domain.viewer.AddressBook;
import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.DetailsHistory;
import com.eastnets.domain.viewer.EntryNode;
import com.eastnets.domain.viewer.FieldSearchInfo;
import com.eastnets.domain.viewer.GpiAgent;
import com.eastnets.domain.viewer.GpiType;
import com.eastnets.domain.viewer.Identifier;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.MessageNote;
import com.eastnets.domain.viewer.MessageSearchTemplate;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.domain.viewer.TableColumnsHeader;
import com.eastnets.domain.viewer.ViewerReportSettings;
import com.eastnets.domain.viewer.ViewerSearchParam;
import com.eastnets.domain.xml.XMLConditionMetadata; 
import com.eastnets.utils.ApplicationUtils;
public class ViewerDomine {
	 

		private static final long serialVersionUID = 6236769424828194712L;

		public enum ViewerSortCategory{
			alliance_instance,inputOutput, correspondent, messageType, referance,format, userGroup, status,instRpName, amount, currency, valueDate, sender, reciever, creationDateTime,ntStatus, mailInfo, receptionInfo, identifier
		}
		

		private List<TableColumnsHeader> listOfAvialableColumns = new ArrayList<TableColumnsHeader>();
		{
			defaultAvialableColumns();
		}
		
		private List<TableColumnsHeader> defaultSelectedTableColmunHeaderList;
		
		private List<TableColumnsHeader> selectedColumnWithoutNote;
		private List<TableColumnsHeader> selectedTableColmunHeaderList;
		/*private boolean columnSelectionApplySuccessfuly;
		private LinkedList<Boolean> columnVisible = new LinkedList<Boolean>();
		private LinkedList<Boolean> tempColumnVisible = new LinkedList<Boolean>();
		private LinkedHashMap<String,String> selectedColumnsMap = new LinkedHashMap <String, String>();*/
		
		private List<SearchResultEntity> entities = new ArrayList<SearchResultEntity>();
		private List<SearchResultEntity> selectedEntities = new ArrayList<SearchResultEntity>();
		private SearchLookups viewerSearchLookups = new SearchLookups();
		private ViewerSearchParam param = new ViewerSearchParam();
		private MessageDetails messageDetails = null;
		private InstanceExtDetails currentMessageInstance;
		private InterventionExtDetails currentInstanceIntv;
		private AppendixExtDetails 	currentInstanceAppe;
		private MessageNote messageNote;
		
		private Date fromDate = new Date();
		private Date toDate = new Date();
		private String format;
		private String direction;
		private String sender;
		private String receiver;
		private String nature;
		private String networkName;
		private String fromToNetwork;
		private String status;
		private boolean withPDE=true;
		private boolean renderNoteColumn;
		
		private boolean umidTypeFieldEnabled;
		private boolean stayOnSameTab;
		private boolean senderInstitutionEnabled;
		private boolean senderDepartmentEnabled;
		private boolean senderIndividualEnabled;
		
		private boolean rcvInstitutionEnabled;
		private boolean rcvDepartmentEnabled;
		private boolean rcvIndividualEnabled;	
		
		private boolean contentNauterReferenceFieldsEnabled;
		private boolean contentNauterAmountFieldsEnabled;
		
		private Integer messageCount= 0;
		
		private String activeMessagePanel= "";
		private String activeInstancePanel= "";
		private String reportText;
		
		private boolean messageTextExpanded;
		private String messageText;
		private boolean enableUnicodeSearch;
		//email-print-exportJRE settings 
		private Integer messagesToPrint =3;//1= All Messages, 2= Selected Messages
		private Integer messagesReportType =1;//1= Print, 2=Mail, 3=CSV, 4=JRE
		private boolean includeHistory = false;
		private boolean expandText = true;
		private boolean printExpandText = true;
		 
		
		private String  mailSubject;
		private String  mailTo;	
		
		
		private Integer umidSearchType= 1;
		private String umidSearchUMID;
		private String umidSearchUMIDL;
		private String umidSearchUMIDH;
		
		private List<String> authorizedReportingActions; 
		
		private String fieldTag;
		private String condition;
		private String conditionValue;
		
		private boolean fieldSearchItemListInvalid= true; 
		private List<FieldSearchInfo> fieldSearchList;

		private Integer selectedFieldSearchIndex;
		
		private int listMax = 25;
		private Integer viewerMaxMsgsPerPage;
		private int timeZoneOffset;
		
		private boolean resultListCheckAll;
		private boolean resultListCheckAllColumns;
		private List<MessageSearchTemplate> msgSearchTemplate;
		private String selectedMsgTemplate;
		private String newSearchTemplate;
		private String filterByTemplateName;
		private String selectedMsgTemplateValue;
		private boolean searchCriteriaApplySuccessfuly;
		
		private boolean viewCriteriaTable= false;
		private String reportTextHeader= "";
		
		private Map<String, String> ccyValuesMap= new TreeMap<String, String>();
		
		private ViewerReportSettings reportSettings = new ViewerReportSettings();
		private List<EntryNode> displayFields = new ArrayList<EntryNode>();
		
		private String latestInstalledSyntaxVer;
		private int reportType;//0:pdf - 1:doc - 2:xls 
		private int reportPaperDirection= 1;//1 = Portrait, 2= Landscape	
		


		//this was separated from the field messagesToPrint because it overrides the value
		private Integer reportMessagesToPrint =3;//1= All Messages, 2= All Messages in current page , 3= Selected Messages
		
		private boolean expandedTree = true;
		
		
		private AddressBook addressBook ;
		private String filterByUserName;
		
		private boolean selectAllAddresses;
		private AddressBook currentAddressBook ;
		private boolean mailToAddressBook;
		
		private boolean hasGenerateReportLicense;
		private int maxFetchSize ;
		private List<String> mailToList ;
		private int selectedXMLIndex;
		
		private Identifier mxIdentifer;
		
		private String mXKeyword1;
		private String mXKeyword2;
		private String mXKeyword3;
		private Integer withinPeriod;
		private XMLConditionMetadata currentConditionMetadata;
		private XMLConditionMetadata selectedCondition;
		private String sortBy;
		
		private int msgViewerTableTotalWidth;
		
		
		
		//GPI Flag
		private  HashMap<String,String> bicArrivedDate=new HashMap<String,String>();  
		private ArrayList<DetailsHistory> detailsHistoriesList=new ArrayList<DetailsHistory>();
		private ArrayList<String> bicList=new ArrayList<String>(); 
		private String amountAfterDeduct; 
		private boolean showDetailsCreditedAmount;
		private GpiType gpiType;
		private String creditedAmount;
		private String initialDraw; 
		private String totalDuration;
		private String nonGpiBic;
		private String totalDeductsStr;
		private String deductCur;
		Double totalDeducts;
		long   allSpentTime;
		long   totalHours;
		long   totalMin;
		long   totalSec; 
		private boolean showGPITracker; 
		boolean findNonTracabel=false;   
		private boolean rejectPayment; 
		private boolean forwardToBeneficiaryCustomer=false; 
		private boolean forwardToNonGpiAgent=false;   
		private boolean hasIntermediaryGpiAgent=true;
	 
		private boolean nonTraceableBinfashryBank=false;  
		private GpiAgent benifasharyBank; 
		private boolean  inithialDrow=false; 
		private boolean mesgWithUter=true;
		private String satausImage;
		private String statusStyle; 
		private String statusMessageGpi;
		private boolean resoneCodeEnabled=false;
		private boolean enabelGpiSeacrh;
		
		void resetGpiValue(){ 
			detailsHistoriesList=new ArrayList<DetailsHistory>();
			bicList=new ArrayList<String>();
			setBicArrivedDate(new HashMap<String, String>());
			setBenifasharyBank(null);
			setRejectPayment(false);
			setTotalDuration("");
			setTotalHours(0);
			setTotalMin(0);
			setTotalSec(0); 
			setAmountAfterDeduct("");
			setTotalDeducts(0.0);
			setCreditedAmount("");
			setTotalDeductsStr("");
			nonTraceableBinfashryBank=false;
			forwardToBeneficiaryCustomer=false;
			showDetailsCreditedAmount=false; 
			forwardToNonGpiAgent=false;
			findNonTracabel=false;
			inithialDrow=false; 
			nonGpiBic="";
		}
		
 
		
		public String getReportTextHeader() {
			return reportTextHeader;
		}
		public void setReportTextHeader(String reportTextHeader) {
			this.reportTextHeader = reportTextHeader;
		}
		public List<String> getReportTextSplitted() {
			return reportTextSplitted;
		}
		public void setReportTextSplitted(List<String> reportTextSplitted) {
			this.reportTextSplitted = reportTextSplitted;
		}

		private List<String> reportTextSplitted= new ArrayList<String>();
		
		
		public boolean isSearchCriteriaApplySuccessfuly() {
			return searchCriteriaApplySuccessfuly;
		}
		public void setSearchCriteriaApplySuccessfuly(
				boolean searchCriteriaApplySuccessfuly) {
			this.searchCriteriaApplySuccessfuly = searchCriteriaApplySuccessfuly;
		}

		public String getSelectedMsgTemplateValue() {
			return selectedMsgTemplateValue;
		}

		public void setSelectedMsgTemplateValue(String selectedMsgTemplateValue) {
			this.selectedMsgTemplateValue = selectedMsgTemplateValue;
		}

		public String getFilterByTemplateName() {
			return filterByTemplateName;
		}

		public void setFilterByTemplateName(String filterByTemplateName) {
			this.filterByTemplateName = filterByTemplateName;
		}

		public List<MessageSearchTemplate> getMsgSearchTemplate() { 
			return msgSearchTemplate;
		}

		public void setMsgSearchTemplate(List<MessageSearchTemplate> msgSearchTemplate) {
			this.msgSearchTemplate = msgSearchTemplate;
		}

		public String getSelectedMsgTemplate() {
			return selectedMsgTemplate;
		}

		public void setSelectedMsgTemplate(String selectedMsgTemplate) {
			this.selectedMsgTemplate = selectedMsgTemplate;
		}

		public String getNewSearchTemplate() {
			return newSearchTemplate;
		}

		public void setNewSearchTemplate(String newSearchTemplate) {
			this.newSearchTemplate = newSearchTemplate;
		}

 
		
		private ViewerSortCategory sortCategory;
		
		public boolean isMessageTextExpanded() {
			return messageTextExpanded;
		}

		public void setMessageTextExpanded(boolean messageTextExpanded) {
			this.messageTextExpanded = messageTextExpanded;
		}

		public String getMessageText() {
			if (StringUtils.isEmpty(messageText)){
				messageText= messageDetails.getMesgUnExpandedTextWithBlock5();
			}			
			return messageText;
		}

		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}
		
		
		public boolean isUmidTypeFieldEnabled() {
			umidTypeFieldEnabled = param.getUmidFormat() != null && param.getUmidFormat().equalsIgnoreCase("swift");
			return umidTypeFieldEnabled;
		}
		
		public boolean isSenderInstitutionEnabled() {
			senderInstitutionEnabled = !((param.getContentSender() ==null) ||(param.getContentSender().equalsIgnoreCase("Any")));
			return senderInstitutionEnabled;
		}

		public boolean isSenderDepartmentEnabled() {
			senderDepartmentEnabled = (param.getContentSender() !=null) && ((param.getContentSender().equalsIgnoreCase("Department")) || (param.getContentSender().equalsIgnoreCase("Individual")));
			return senderDepartmentEnabled;
		}

		public boolean isSenderIndividualEnabled() {
			senderIndividualEnabled = (param.getContentSender() !=null) && ((param.getContentSender().equalsIgnoreCase("Individual")));
			return senderIndividualEnabled;
		}

		public boolean isRcvInstitutionEnabled() {
			rcvInstitutionEnabled = !((param.getContentReceiver() ==null) ||(param.getContentReceiver().equalsIgnoreCase("Any")));
			return rcvInstitutionEnabled;
		}

		public boolean isRcvDepartmentEnabled() {
			rcvDepartmentEnabled = ((param.getContentReceiver() !=null)) && ((param.getContentReceiver().equalsIgnoreCase("Department")) || (param.getContentReceiver().equalsIgnoreCase("Individual")));
			return rcvDepartmentEnabled;
		}

		public boolean isRcvIndividualEnabled() {
			rcvIndividualEnabled = (param.getContentReceiver() !=null) && ((param.getContentReceiver().equalsIgnoreCase("Individual")));
			return rcvIndividualEnabled;
		}

		public boolean isContentNauterReferenceFieldsEnabled() {
			contentNauterReferenceFieldsEnabled = (param.getContentNature()!=null 
					&& (param.getContentNature().equalsIgnoreCase("Financial") || param.getContentNature().equalsIgnoreCase("Text")) 
					|| (param.getUmidFormat() != null &&( param.getUmidFormat().equalsIgnoreCase("MX") || param.getUmidFormat().equalsIgnoreCase("FILE") )));
			return contentNauterReferenceFieldsEnabled;
		}
		
		public boolean isContentNauterAmountFieldsEnabled() {
			contentNauterAmountFieldsEnabled = param.getContentNature()!=null && (param.getContentNature().equalsIgnoreCase("Financial"));
			return contentNauterAmountFieldsEnabled;
		}	

		public Date getFromDate() {
			return fromDate;
		}
		public void setFromDate(Date fromDate) {
			this.fromDate = fromDate;
		}
		public Date getToDate() {
			return toDate;
		}
		public void setToDate(Date toDate) {
			this.toDate = toDate;
		}
		public String getFormat() {
			return format;
		}
		public void setFormat(String format) {
			this.format = format;
		}
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getReceiver() {
			return receiver;
		}
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		public String getNature() {
			return nature;
		}
		public void setNature(String nature) {
			this.nature = nature;
		}
		public String getNetworkName() {
			return networkName;
		}
		public void setNetworkName(String networkName) {
			this.networkName = networkName;
		}
		public String getFromToNetwork() {
			return fromToNetwork;
		}
		public void setFromToNetwork(String fromToNetwork) {
			this.fromToNetwork = fromToNetwork;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public List<SearchResultEntity> getEntities() {
			return entities;
		}
		public void setEntities(List<SearchResultEntity> entities) {
			this.entities = entities;
		}
		
		public ViewerSearchParam getParam() {
			if ( param != null && ( param.getCreationDate() == null || param.getCreationDate().getDateFrom() == null || param.getCreationDate().getDateTo() == null ) )
			{
				SimpleDateFormat formatReadere = new SimpleDateFormat("yyyy/MM/dd" );
				SimpleDateFormat formatWriter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
				String dateStr= formatReadere.format(Calendar.getInstance().getTime());
				if ( param.getCreationDate().getDateFrom() == null  )
				{
					if ( param.getCreationDate().getType() == AdvancedDate.TYPE_DATE ){
						String fromDateStr= dateStr +  " 00:00:00";
						try{
							param.getCreationDate().setDate(formatWriter.parse(fromDateStr));
						}catch(Exception e){}
					}
					else if ( param.getCreationDate().getType() == AdvancedDate.TYPE_DAYS ){
						param.getCreationDate().setDays(0);
					}
					else if ( param.getCreationDate().getType() == AdvancedDate.TYPE_WEEKS ){
						param.getCreationDate().setWeeks(0);
					}
					else if ( param.getCreationDate().getType() == AdvancedDate.TYPE_MONTHS ){
						param.getCreationDate().setMonths(0);
					}
				}
				if ( param.getCreationDate().getType() == AdvancedDate.TYPE_DATE && param.getCreationDate().getDateTo() == null )
				{
					String toDateStr= dateStr +  " 23:59:59";
					try{
						param.getCreationDate().setSecondDate(formatWriter.parse(toDateStr));
					}catch(Exception e){}
				}
			}
			return param;
		}

		public void setParam(ViewerSearchParam param) {
			this.param = param;
		}
		public SearchLookups getViewerSearchLookups() {
			return viewerSearchLookups;
		}
		public void setViewerSearchLookups(SearchLookups viewerSearchLookups) {
			this.viewerSearchLookups = viewerSearchLookups;
		}

		public void setUmidType(String umidType) {
			param.setUmidType( umidType );
		}

		public String getUmidType() {
			if ( !StringUtils.equalsIgnoreCase(param.getUmidFormat(), "swift") )
			{
				param.setUmidType("");
			}		
			return param.getUmidType();
		}

		public void setContentSenderInstitution(String contentSenderInstitution) {
			if(contentSenderInstitution != null)
				param.setContentSenderInstitution( contentSenderInstitution.toUpperCase() );
		}

		public String getContentSenderInstitution() {
			if ( ! senderInstitutionEnabled )
			{
				param.setContentSenderInstitution("");
			}		
			return param.getContentSenderInstitution();
		}

		public void setContentReceiverInstitution(String contentReceiverInstitution) {
			if(contentReceiverInstitution != null)
				param.setContentReceiverInstitution(contentReceiverInstitution.toUpperCase());
		}

		public String getContentReceiverInstitution() {
			if ( !rcvInstitutionEnabled )
			{
				param.setContentReceiverInstitution("");
			}		
			return param.getContentReceiverInstitution();
		}

		public void setContentSenderDepartment(String contentSenderDepartment) {
			param.setContentSenderDepartment(contentSenderDepartment);
		}

		public String getContentSenderDepartment() {
			if ( !senderDepartmentEnabled )
			{
				param.setContentSenderDepartment("");
			}		
			return param.getContentSenderDepartment();
		}

		/*private String contentReceiverDepartment;

		public void setContentReceiverDepartment(String contentReceiverDepartment) {
			this.contentReceiverDepartment = contentReceiverDepartment;
		}

		public String getContentReceiverDepartment() {
			return contentReceiverDepartment;
		}*/
		public void set(String contentReceiverDepartment) {
			param.setContentReceiverDepartment(contentReceiverDepartment);
		}

		public String getContentReceiverDepartment() {
			if ( !rcvDepartmentEnabled )
			{
				param.setContentReceiverDepartment("");
			}		
			return param.getContentReceiverDepartment();
		}

		public void setContentSenderLastName(String contentSenderLastName) {
			param.setContentSenderLastName(contentSenderLastName);
		}

		public String getContentSenderLastName() {
			if ( !senderIndividualEnabled )
			{
				param.setContentSenderLastName("");
			}		
			return param.getContentSenderLastName();
		}

		public void setContentReceiverLastName(String contentReceiverLastName) {
			param.setContentReceiverLastName(contentReceiverLastName);
		}

		public String getContentReceiverLastName() {
			if ( !rcvIndividualEnabled )
			{
				param.setContentReceiverLastName("");
			}		
			return param.getContentReceiverLastName();
		}

		public void setContentSenderFirstName(String contentSenderFirstName) {
			param.setContentSenderFirstName(contentSenderFirstName);
		}

		public String getContentSenderFirstName() {
			if ( !senderIndividualEnabled )
			{
				param.setContentSenderFirstName("");
			}		
			return param.getContentSenderFirstName();
		}

		public void setContentReceiverFirstName(String contentReceiverFirstName) {
			param.setContentReceiverFirstName(contentReceiverFirstName);
		}

		public String getContentReceiverFirstName() {
			if ( !rcvIndividualEnabled )
			{
				param.setContentReceiverFirstName("");
			}		
			return param.getContentReceiverFirstName();
		}
		
		public void setContentTransactionReference( String contentTransactionReference ) {
			param.setContentTransactionReference(contentTransactionReference);
		}

		public String getContentTransactionReference() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentTransactionReference("");
			}		
			return param.getContentTransactionReference();
		}

		public void setContentRelatedRefference(String contentRelatedRefference) {
			param.setContentRelatedRefference(contentRelatedRefference);
		}

		public String getContentRelatedRefference() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentRelatedRefference("");
			}		
			return param.getContentRelatedRefference();
		}

		public void setContentAmountFrom(String contentAmountFrom) {
			param.setContentAmountFrom(contentAmountFrom);
		}

		public String getContentAmountFrom() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentAmountFrom("");
			}		
			return param.getContentAmountFrom();
		}

		public void setContentAmountTo(String contentAmountTo) {
			param.setContentAmountTo(contentAmountTo);
		}

		public String getContentAmountTo() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentAmountTo("");
			}		
			return param.getContentAmountTo();
		}

		public void setContentAmountCurrency(String contentAmountCurrency) {
			param.setContentAmountCurrency(contentAmountCurrency);
		}

		public String getContentAmountCurrency() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentAmountCurrency("");
			}		
			return param.getContentAmountCurrency();
		}

		public void setContentValueDateFrom(Date contentValueDateFrom) {
			param.setContentValueDateFrom(contentValueDateFrom);
		}

		public Date getContentValueDateFrom() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentValueDateFrom(null);
			}		
			return param.getContentValueDateFrom();
		}

		public void setContentValueDateTo(Date contentValueDateTo) {
			param.setContentValueDateTo(contentValueDateTo);
		}

		public Date getContentValueDateTo() {
			if ( !isContentNauterReferenceFieldsEnabled() )
			{
				param.setContentValueDateTo(null);
			}		
			return param.getContentValueDateTo();
		}

		public void setMessageCount(Integer messageCount) {
			this.messageCount = messageCount;
		}

		public Integer getMessageCount() {
			return messageCount;
		}

		public void setMessageDetails(MessageDetails messageDetails) {
			this.messageDetails= messageDetails;
		}

		public MessageDetails getMessageDetails() {
			return messageDetails;
		}

		public void setCurrentMessageInstance(InstanceExtDetails currentMessageInstance) {
			this.currentMessageInstance = currentMessageInstance;
		}

		public InstanceExtDetails getCurrentMessageInstance() {
			return currentMessageInstance;
		}

		public void setCurrentInstanceIntv(InterventionExtDetails currentInstanceIntv) {
			this.currentInstanceIntv = currentInstanceIntv;
		}

		public InterventionExtDetails getCurrentInstanceIntv() {
			return currentInstanceIntv;
		}

		public void setCurrentInstanceAppe(AppendixExtDetails currentInstanceAppe) {
			this.currentInstanceAppe = currentInstanceAppe;
		}

		public AppendixExtDetails getCurrentInstanceAppe() {
			return currentInstanceAppe;
		}

		public void setActiveMessagePanel(String activeMessagePanel) {
			this.activeMessagePanel = activeMessagePanel;
		}

		public String getActiveMessagePanel() {
			return activeMessagePanel;
		}

		public void setActiveInstancePanel(String activeInstancePanel) {
			this.activeInstancePanel = activeInstancePanel;
		}

		public String getActiveInstancePanel() {
			return activeInstancePanel;
		}


		public void setReportText(String reportText) {
			String[] messages = StringUtils.splitByWholeSeparator(reportText, "\r\n\r\n<^$^>");
			reportTextHeader= "";
			reportTextSplitted= new ArrayList<String>();
			
			if ( messages.length > 0 ){
				reportTextHeader = messages[0];
			}
			
			for (int i = 1 ; i < messages.length ; ++i ){
				reportTextSplitted.add( messages[i] );
			}
			
			this.reportText = reportText.replace("<^$^>", "");
		}

		public String getReportText() {
			return reportText;
		}
		
		public Integer getMessagesToPrint() {
			if (messagesToPrint == null) return new Integer(3);
			return messagesToPrint;
		}

		public void setMessagesToPrint(Integer messagesToPrint) {
			this.messagesToPrint = messagesToPrint;
		}

		public boolean isIncludeHistory() {
			return includeHistory;
		}

		public void setIncludeHistory(boolean includeHistory) {
			this.includeHistory = includeHistory;
		}

		public boolean isExpandText() {
			return expandText;
		}

		public void setExpandText(boolean expandText) {
			this.expandText = expandText;
		}

		public void setMailSubject(String mailSubject) {
			this.mailSubject = mailSubject;
		}

		public String getMailSubject() {
			return mailSubject;
		}

		public void setMailTo(String mailTo) {
			this.mailTo = mailTo;
		}

		public String getMailTo() {
			return mailTo;
		}

		public void setMessagesReportType(Integer messagesReportType) {
			this.messagesReportType = messagesReportType;
		}
		
		public Integer getMessagesReportType() {
			//value validation
			if( messagesReportType < 1 ) messagesReportType = 1;
			if ( !isCanPrintMessages() && messagesReportType == 1 ) messagesReportType= 2;
			if ( !isCanMailTo() && messagesReportType == 2 ) messagesReportType= 3;
			
			return messagesReportType;
		}
		
		public boolean isHasHistory() {
			return getMessagesReportType()  < 3;
		}

		public boolean isHasExpand() {
			return getMessagesReportType()  < 3;
		}

		public boolean isPrintVisible() {
			return getMessagesReportType()  == 1;
		}
		public boolean isMailVisible() {
			return getMessagesReportType()  == 2;
		}

		public boolean isCsvVisible() {
			return getMessagesReportType()  == 3;
		}
		
		public boolean isJreVisible() {
			return getMessagesReportType()  == 4;
		}
		
		public boolean isXmlVisible(){
			return getMessagesReportType() == 5;
		}

		public void setWithPDE(boolean withPDE) {
			this.withPDE = withPDE;
		}

		public boolean isWithPDE() {
			return withPDE;
		}

		public String getUmidSearchUMID() {
			return umidSearchUMID;
		}

		public void setUmidSearchUMID(String umidSearchUMID) {
			this.umidSearchUMID = umidSearchUMID;
		}

		public String getUmidSearchUMIDL() {
			return umidSearchUMIDL;
		}

		public void setUmidSearchUMIDL(String umidSearchUMIDL) {
			this.umidSearchUMIDL = umidSearchUMIDL;
		}

		public String getUmidSearchUMIDH() {
			return umidSearchUMIDH;
		}

		public void setUmidSearchUMIDH(String umidSearchUMIDH) {
			this.umidSearchUMIDH = umidSearchUMIDH;
		}

		public Integer getUmidSearchType() {
			return umidSearchType;
		}

		public void setUmidSearchType(Integer umidSearchType) {
			this.umidSearchType = umidSearchType;
		}

		public List<String> getAuthorizedReportingActions() {
			return authorizedReportingActions;
		}

		public void setAuthorizedReportingActions(
				List<String> authorizedReportingActions) {
			this.authorizedReportingActions = authorizedReportingActions;
		}
			
		public boolean isCanPrintMessages(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_PRINT_MESSAGES );
		}
		
		public boolean isCanExportMessages(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_EXPORT_MESSAGES );
		}
		
		public boolean isCanMailTo(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_MAIL_TO );
		}
		
		public boolean isCanCountMessages(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_COUNT_MESSAGES );
		}
		
		public boolean isCanSearchBySUMID(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_SEARCH_BY_SUMID );
		}

		public boolean isCanAddMessageNotes() {
			return authorizedReportingActions.contains( Constants.VIEWER_MESSAGE_NOTES);
		}
		
		public boolean isCanGenerateReports(){
			return authorizedReportingActions.contains( Constants.VIEWER_ACTION_GENERATE_REPORTS );
		}
		public boolean isCanAddMessageGpi() {
			return authorizedReportingActions.contains( Constants.VIEWER_MESSAGE_GPI);
		}
		
		
		public void   setAppeAuthValue(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}
		
		public String getAppeAuthValue(){
			return getCurrentInstanceAppe().getAppeAuthValue();
		}	

		public void   setAppePacValue(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}
		
		public String getAppePacValue(){
			return getCurrentInstanceAppe().getAppePacValue();
		}

		public void   setAppePkiPac2Value(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}
		 
		public String getAppePkiPac2Value(){
			return getCurrentInstanceAppe().getAppePkiPac2Value();
		}
		
		public void   setAppeAckNackText(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}
	 
		public String getAppeAckNackText(){
			return getCurrentInstanceAppe().getAppeAckNackText();
		}
		
		public void   setMesgFileHeaderInfo(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}

		public String getMesgFileHeaderInfo(){
			return ApplicationUtils.convertClob2String(getMessageDetails().getMesgFile().getMesgFileHeaderInfo());
		}
		
		public void   setIntvText(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}

		public String getIntvText(){
			return ApplicationUtils.convertClob2String(getCurrentInstanceIntv().getIntvText());
		}
		
		public void   setAuthenticationPKISign(String val){
			throw new NotImplementedException("this should not be called as it should be readonly");
		}

		public String getAuthenticationPKISign(){
			
			String strAppePkiAuthValue=  getCurrentInstanceAppe().getAppePkiAuthResult();
			if( strAppePkiAuthValue == null || ! "AUTH_SUCCESS".equalsIgnoreCase(strAppePkiAuthValue.trim())  ){
				return "";
			}
			return "Yes";
		}

		public ViewerSortCategory getSortCategory() {
			return sortCategory;
		}

		public void setSortCategory(ViewerSortCategory sortCategory) {
			this.sortCategory = sortCategory;
		}

 

		public String getFieldTag() {
			return fieldTag;
		}

		public void setFieldTag(String fieldTag) {
			this.fieldTag = fieldTag;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		public String getConditionValue() {
			return conditionValue;
		}

		public void setConditionValue(String conditionValue) {
			this.conditionValue = conditionValue;
		}
		
		public List<FieldSearchInfo> getFieldSearchList() {
			return fieldSearchList;
		}

		public void setFieldSearchList(List<FieldSearchInfo> fieldSearchList) {
			fieldSearchItemListInvalid= true;//next time generate the fieldSearchItemList
			this.fieldSearchList = fieldSearchList;
		}

 

		/**
		 * @return the selectedFieldSearchIndex
		 */
		public Integer getSelectedFieldSearchIndex() {
			return selectedFieldSearchIndex;
		}

		/**
		 * @param selectedFieldSearchIndex the selectedFieldSearchIndex to set
		 */
		public void setSelectedFieldSearchIndex(Integer selectedFieldSearchIndex) {
			this.selectedFieldSearchIndex = selectedFieldSearchIndex;
		}
		
		public int getListMax(){
			return listMax;
		}

		public void setListMax(int listMax){
			if ( listMax < 25 || listMax > 999999999){
				this.listMax= 25;
				System.err.println("Viewer search result list maximum items value is not valid.");
				return;
			}
			this.listMax= listMax;
			
		}
		
		public Integer getViewerMaxMsgsPerPage() {
			return viewerMaxMsgsPerPage;
		}
		
		public void setViewerMaxMsgsPerPage(Integer viewerMaxMsgsPerPage) {
			this.viewerMaxMsgsPerPage = viewerMaxMsgsPerPage;
		}
		
		public int getTimeZoneOffset(){
			return timeZoneOffset;
		}

		public void setTimeZoneOffset(int timeZoneOffset){
			if ( timeZoneOffset < -23 || timeZoneOffset > 23){
				return;
			}else{
				this.timeZoneOffset= timeZoneOffset;
			}
			
			
		}

		public boolean isResultListCheckAll() {
			return resultListCheckAll;
		}

		public void setResultListCheckAll(boolean resultListCheckAll) {
			this.resultListCheckAll = resultListCheckAll;
		}
		public boolean isViewCriteriaTable() {
			return viewCriteriaTable;
		}
		public void setViewCriteriaTable(boolean viewCriteriaTable) {
			this.viewCriteriaTable = viewCriteriaTable;
		}
		public Map<String, String> getCcyValuesMap() {
			return ccyValuesMap;
		}

		public void setCcyValuesMap(Set<String> ccyValuesList) {

			this.ccyValuesMap.clear();
			this.ccyValuesMap.put("","");
			
			if ( ccyValuesList == null || ccyValuesList.isEmpty())
			{
				return;
			}

			for (String ccy : ccyValuesList) {
				this.ccyValuesMap.put(ccy,ccy);
			}
		}
		public ViewerReportSettings getReportSettings() {
			return reportSettings;
		}
		public void setReportSettings(ViewerReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}
		public List<EntryNode> getDisplayFields() {
			return displayFields;
		}
		public void setDisplayFields(List<EntryNode> displayFields) {
			this.displayFields.clear();
			for (EntryNode display : displayFields ){
				this.displayFields.add( display );
			}
		}
		public String getLatestInstalledSyntaxVer() {
			return latestInstalledSyntaxVer;
		}
		public void setLatestInstalledSyntaxVer(String latestInstalledSyntaxVer) {
			this.latestInstalledSyntaxVer = latestInstalledSyntaxVer;
		}
		public int getReportType() {
			return reportType;
		}
		public void setReportType(int reportType) {
			this.reportType = reportType;
		}
		public int getReportPaperDirection() {
			return reportPaperDirection;
		}
		public void setReportPaperDirection(int reportPaperDirection) {
			this.reportPaperDirection = reportPaperDirection;
		}
		public Integer getReportMessagesToPrint() {
			return reportMessagesToPrint;
		}
		public void setReportMessagesToPrint(Integer reportMessagesToPrint) {
			this.reportMessagesToPrint = reportMessagesToPrint;
		}
		
		/*public LinkedList<Boolean> getColumnVisible() {
			return columnVisible;
		}

		public void setColumnVisible(LinkedList<Boolean> columnVisible) {
			this.columnVisible = columnVisible;
		}*/


		





	public void defaultAvialableColumns() {
			
			int index = 0 ;
			
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "alliance_instance", false, 25, false,75));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgSubFormat", false, 25, true,35));   
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "correspondent", false, 25, true,100));   
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgReference", false, 25, true,110));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgFrmtName", false, 25, true,60));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgIdentifier", false, 25, true,90));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgStatus", false, 25, true,75));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "xFinAmountFormatted", true, 25, true,100));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "xFinCcy", false, 25, true,45));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "xFinValueDateFormatted", false, 25, true,80)); 
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgSenderX1", false, 25, true,85));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "instReceiverX1", false, 25, true,85));   
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgCreaDateTimeStr", false, 25, true,120));   
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "instRpName", false, 25, false,100));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "emiNetworkDeliveryStatusFormatted", false, 25, true,100));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "emiIAppNameFormatted", false, 100, true,130));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "recIAppNameFormatted", false, 25,true,130));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "slaId", false, 25, false,45));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "uetr", false, 25, false,170));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgRelatedReference", false, 25, false,100)); 
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mesgUserReferenceText", false, 25, false,110));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mXKeyword1", false, 25, false,77));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mXKeyword2", false, 25, false,77));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "mXKeyword3", false, 25, false,77));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "serviceName", false, 25, false,80)); 
			
			//For GPI Column 
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "orderingCustomer", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "orderingInstitution", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "beneficiaryCustomer", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "accountWithInstitution", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "detailsOfcharges", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "deductsFormatted", false, 25, false, 80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "statusCode", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "reasonCodes", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "statusOriginatorBIC", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "forwardedToAgent", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "transactionStatus", false, 25, false,80));
//			listOfAvialableColumns.add(new TableColumnsHeader(index++, "NAKCode", false, 25, false,80));  
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "gpiCur", false, 25, false,80));
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "date_time_suffix", false, 25, false,80));
			
			
			listOfAvialableColumns.add(new TableColumnsHeader(index++, "note", false, 25, false,50)); 
			  
			
		
			

			
		}



		
	public List<TableColumnsHeader> getListOfAvialableColumns() {
		return listOfAvialableColumns;
	}

	public void setListOfAvialableColumns(
			List<TableColumnsHeader> listOfAvialableColumns) {
		this.listOfAvialableColumns = listOfAvialableColumns;
	}

	/*public void fillTableColmunHeader(){
		
		
		
		int index = 0;
		for(String headerName : listOfAvialableColumns){
			
			TableColumnsHeader header = new TableColumnsHeader();
			
			header.setId(index);
			header.setRowOrder(listOfAvialableColumns.size());
			header.setColumnName(headerName);
			if(index == 1 || index == 8 || index == 17){
				header.setColumnWidth(20);
			}else{
				header.setColumnWidth(100);
			}
			
			header.setChecked(false);
			tableColmunHeaderList.add(header);
			index++ ;
		}
		
	}*/
		
		public void reorderTableColumnsHeader(List<String> list){
			// set columns Orders
			int itemIndex = 0;
			/*
			 * default value = 0 + width of first two columns in the table 
			 * */
			this.msgViewerTableTotalWidth = 190;
			listOfAvialableColumns.clear();
			selectedTableColmunHeaderList.clear();
			defaultAvialableColumns();
			for(String item : list){
				
				listOfAvialableColumns.get(Integer.valueOf(item)).setRowOrder(Integer.valueOf(itemIndex)) ;
				
				TableColumnsHeader header = getColumnByRowOrder(Integer.valueOf(itemIndex));
				if(header.getColumnName().equalsIgnoreCase("note")){
					setRenderNoteColumn(true);  } 
					selectedTableColmunHeaderList.add(header);	
				 
				this.msgViewerTableTotalWidth+= header.getColumnWidth();
				itemIndex++ ;
			}
			
			Collections.sort(listOfAvialableColumns);
			//Collections.sort(selectedTableColmunHeaderList);
		}

	 public TableColumnsHeader getColumnByRowOrder(int order){
		 
		 TableColumnsHeader orderedColumnsHeader = null;
		 for (TableColumnsHeader columnHeader : listOfAvialableColumns) {
			
			 if(columnHeader.getRowOrder()== order){
				 orderedColumnsHeader = columnHeader;
				 break;
			 }
				 
		}
		 
		 
		 return orderedColumnsHeader;
	 }
		
		
		/*public void setSelectedColumns() {
			selectedColumnsMap.clear();
			for(int i = 0 ; i < columnVisible.size() ; i++){
				if(columnVisible.get(i)==true){
					selectedColumnsMap.put(""+i, listOfAvialableColumns.get(i));
			}
			}
			
		}*/

		/*public void setTempVisibleColumns(List<String> list) {
			for(int i = 0 ; i < tempColumnVisible.size() ; i++){
					if(list.contains(""+i))
						tempColumnVisible.set(i, true);
					else
						tempColumnVisible.set(i, false);
			}
			}*/

		/*public void setVisibleColumns(List<String> list) {

			for(int i = 0 ; i < columnVisible.size() ; i++){	
					if(list.contains(""+i))
						columnVisible.set(i, true);
					else
						columnVisible.set(i, false);
			}
		}*/
		
	/*	public boolean isNoItemSelectedInTemp() {
			for (boolean flag : tempColumnVisible) {
				if (flag == true) {
					return false;
				}
			}
			return true;
		}

		public LinkedList<Boolean> getTempColumnVisible() {
			return tempColumnVisible;
		}

		public void setTempColumnVisible(LinkedList<Boolean> tempColumnVisible) {
			this.tempColumnVisible = tempColumnVisible;
		}
		public boolean isColumnSelectionApplySuccessfuly() {
			return columnSelectionApplySuccessfuly;
	}
		public void setColumnSelectionApplySuccessfuly(
				boolean columnselectionaApplySuccessfuly) {
			this.columnSelectionApplySuccessfuly = columnselectionaApplySuccessfuly;
		}*/
		public boolean isPrintExpandText() {
			return printExpandText;
	}
		public void setPrintExpandText(boolean printExpandText) {
			this.printExpandText = printExpandText;
		}
		public boolean isResultListCheckAllColumns() {
			return resultListCheckAllColumns;
	}
		public void setResultListCheckAllColumns(boolean resultListCheckAllColumns) {
			this.resultListCheckAllColumns = resultListCheckAllColumns;
		}

		/*public LinkedHashMap<String,String> getSelectedColumnsMap() {
			return selectedColumnsMap;
	}
		public void setSelectedColumnsMap(LinkedHashMap<String,String> selectedColumnsMap) {
			this.selectedColumnsMap = selectedColumnsMap;
		}*/
		
		public MessageNote getMessageNote() {
			return messageNote;
		}
		
		public void setMessageNote(MessageNote messageNote) {
			this.messageNote = messageNote;
		}
		public boolean isExpandedTree() {
			return expandedTree;
		}
		public void setExpandedTree(boolean expandedTree) {
			this.expandedTree = expandedTree;
		}
		public AddressBook getAddressBook() {
			return addressBook;
		}
		public void setAddressBook(AddressBook addressBook) {
			this.addressBook = addressBook;
		}
		public boolean isSelectAllAddresses() {
			return selectAllAddresses;
		}
		public void setSelectAllAddresses(boolean selectAllAddresses) {
			this.selectAllAddresses = selectAllAddresses;
		}
		public String getFilterByUserName() {
			return filterByUserName;
		}
		public void setFilterByUserName(String filterByUserName) {
			this.filterByUserName = filterByUserName;
		}
		public AddressBook getCurrentAddressBook() {
			return currentAddressBook;
		}
		public void setCurrentAddressBook(AddressBook currentAddressBook) {
			this.currentAddressBook = currentAddressBook;
		}
		
		public boolean isMailToAddressBook() {
			return mailToAddressBook;
		}
		public void setMailToAddressBook(boolean mailToAddressBook) {
			this.mailToAddressBook = mailToAddressBook;
		}
		public boolean isHasGenerateReportLicense() {
			return hasGenerateReportLicense;
		}
		public void setHasGenerateReportLicense(boolean hasGenerateReportLicense) {
			this.hasGenerateReportLicense = hasGenerateReportLicense;
		}
		
		public int getMaxFetchSize() {
			return maxFetchSize;
		}
		public void setMaxFetchSize(int maxFetchSize) {
			this.maxFetchSize = maxFetchSize;
		}
		public boolean isEnableUnicodeSearch() {
			return enableUnicodeSearch;
		}
		public void setEnableUnicodeSearch(boolean enableUnicodeSearch) {
			this.enableUnicodeSearch = enableUnicodeSearch;
		}
		
		
		public List<String> getMailToList() {
			return mailToList;
		}

		public void setMailToList(List<String> mailToList) {
			this.mailToList = mailToList;
		}


		public List<TableColumnsHeader> getSelectedTableColmunHeaderList() {
			return selectedTableColmunHeaderList;
		}
		public void setSelectedTableColmunHeaderList(
				List<TableColumnsHeader> selectedTableColmunHeaderList) {
			this.selectedTableColmunHeaderList = selectedTableColmunHeaderList;
		}
		public List<SearchResultEntity> getSelectedEntities() {
			return selectedEntities;
		}
		public void setSelectedEntities(List<SearchResultEntity> selectedEntities) {
			this.selectedEntities = selectedEntities;
		}

		public int getSelectedXMLIndex() {
			return selectedXMLIndex;
		}

		public void setSelectedXMLIndex(int selectedXMLIndex) {
			this.selectedXMLIndex = selectedXMLIndex;
		}
		
		public String getmXKeyword1() {
			return mXKeyword1;
		}

		public void setmXKeyword1(String mXKeyword1) {
			this.mXKeyword1 = mXKeyword1;
		}

		public String getmXKeyword2() {
			return mXKeyword2;
		}

		public void setmXKeyword2(String mXKeyword2) {
			this.mXKeyword2 = mXKeyword2;
		}

		public String getmXKeyword3() {
			return mXKeyword3;
		}

		public void setmXKeyword3(String mXKeyword3) {
			this.mXKeyword3 = mXKeyword3;
		}

		public Identifier getMxIdentifer() {
			return mxIdentifer;
		}

		public void setMxIdentifer(Identifier mxIdentifer) {
			this.mxIdentifer = mxIdentifer;
		}

		public XMLConditionMetadata getCurrentConditionMetadata() {
			return currentConditionMetadata;
		}

		public void setCurrentConditionMetadata(XMLConditionMetadata currentConditionMetadata) {
			this.currentConditionMetadata = currentConditionMetadata;
		}

		public XMLConditionMetadata getSelectedCondition() {
			return selectedCondition;
		}

		public void setSelectedCondition(XMLConditionMetadata selectedCondition) {
			this.selectedCondition = selectedCondition;
		}

		public Integer getWithinPeriod() {
			return withinPeriod;
		}

		public void setWithinPeriod(Integer withinPeriod) {
			if(withinPeriod== null){
				this.withinPeriod = 7 ;
			}else{
				this.withinPeriod = withinPeriod;
			}
		}


		public List<TableColumnsHeader> getDefaultSelectedTableColmunHeaderList() {
			return defaultSelectedTableColmunHeaderList;
		}

		public void setDefaultSelectedTableColmunHeaderList(
				List<TableColumnsHeader> defaultSelectedTableColmunHeaderList) {
			this.defaultSelectedTableColmunHeaderList = defaultSelectedTableColmunHeaderList;
		}

		public int getMsgViewerTableTotalWidth() {
			return msgViewerTableTotalWidth;
		}

		public void setMsgViewerTableTotalWidth(int msgViewerTableTotalWidth) {
			this.msgViewerTableTotalWidth = msgViewerTableTotalWidth;
		}

		public String getSortBy() {
			return sortBy;
		}

		public void setSortBy(String sortBy) {
			this.sortBy = sortBy;
		}

	 

		public boolean isRenderNoteColumn() {
			return renderNoteColumn;
		}

		public void setRenderNoteColumn(boolean renderNoteColumn) {
			this.renderNoteColumn = renderNoteColumn;
		}

		public List<TableColumnsHeader> getSelectedColumnWithoutNote() {
			return selectedColumnWithoutNote;
		}

		public void setSelectedColumnWithoutNote(List<TableColumnsHeader> selectedColumnWithoutNote) {
			this.selectedColumnWithoutNote = selectedColumnWithoutNote;
		}
		
		public HashMap<String,String> getBicArrivedDate() {
			return bicArrivedDate;
		}

		public void setBicArrivedDate(HashMap<String,String> bicArrivedDate) {
			this.bicArrivedDate = bicArrivedDate;
		}

		
		public ArrayList<DetailsHistory> getDetailsHistoriesList() {
			return detailsHistoriesList;
		}

		public void setDetailsHistoriesList(ArrayList<DetailsHistory> detailsHistoriesList) {
			this.detailsHistoriesList = detailsHistoriesList;
		}

		public ArrayList<String> getBicList() {
			return bicList;
		}

		public void setBicList(ArrayList<String> bicList) {
			this.bicList = bicList;
		}

		public String getCreditedAmount() {
			return creditedAmount;
		}

		public void setCreditedAmount(String creditedAmount) {
			this.creditedAmount = creditedAmount;
		}

		public String getInitialDraw() {
			return initialDraw;
		}

		public void setInitialDraw(String initialDraw) {
			this.initialDraw = initialDraw;
		}

		public String getNonGpiBic() {
			return nonGpiBic;
		}

		public void setNonGpiBic(String nonGpiBic) {
			this.nonGpiBic = nonGpiBic;
		}

		public Double getTotalDeducts() {
			return totalDeducts;
		}

		public void setTotalDeducts(Double totalDeducts) {
			this.totalDeducts = totalDeducts;
		}

		public long getAllSpentTime() {
			return allSpentTime;
		}

		public void setAllSpentTime(long allSpentTime) {
			this.allSpentTime = allSpentTime;
		}

		public long getTotalHours() {
			return totalHours;
		}

		public void setTotalHours(long totalHours) {
			this.totalHours = totalHours;
		}

		public long getTotalMin() {
			return totalMin;
		}

		public void setTotalMin(long totalMin) {
			this.totalMin = totalMin;
		}

		public long getTotalSec() {
			return totalSec;
		}

		public void setTotalSec(long totalSec) {
			this.totalSec = totalSec;
		}

		public boolean isFindNonTracabel() {
			return findNonTracabel;
		}

		public void setFindNonTracabel(boolean findNonTracabel) {
			this.findNonTracabel = findNonTracabel;
		}

		public boolean isHasIntermediaryGpiAgent() {
			return hasIntermediaryGpiAgent;
		}

		public void setHasIntermediaryGpiAgent(boolean hasIntermediaryGpiAgent) {
			this.hasIntermediaryGpiAgent = hasIntermediaryGpiAgent;
		}

		public boolean isRejectPayment() {
			return rejectPayment;
		}

		public void setRejectPayment(boolean rejectPayment) {
			this.rejectPayment = rejectPayment;
		}

		public boolean isForwardToBeneficiaryCustomer() {
			return forwardToBeneficiaryCustomer;
		}

		public void setForwardToBeneficiaryCustomer(boolean forwardToBeneficiaryCustomer) {
			this.forwardToBeneficiaryCustomer = forwardToBeneficiaryCustomer;
		}

		public boolean isForwardToNonGpiAgent() {
			return forwardToNonGpiAgent;
		}

		public void setForwardToNonGpiAgent(boolean forwardToNonGpiAgent) {
			this.forwardToNonGpiAgent = forwardToNonGpiAgent;
		}

		public boolean isShowGPITracker() {
			return showGPITracker;
		}

		public void setShowGPITracker(boolean showGPITracker) {
			this.showGPITracker = showGPITracker;
		}

		public boolean isNonTraceableBinfashryBank() {
			return nonTraceableBinfashryBank;
		}

		public void setNonTraceableBinfashryBank(boolean nonTraceableBinfashryBank) {
			this.nonTraceableBinfashryBank = nonTraceableBinfashryBank;
		}

		public GpiAgent getBenifasharyBank() {
			return benifasharyBank;
		}

		public void setBenifasharyBank(GpiAgent benifasharyBank) {
			this.benifasharyBank = benifasharyBank;
		}

		public boolean isInithialDrow() {
			return inithialDrow;
		}

		public void setInithialDrow(boolean inithialDrow) {
			this.inithialDrow = inithialDrow;
		}

		public String getAmountAfterDeduct() {
			return amountAfterDeduct;
		}

		public void setAmountAfterDeduct(String amountAfterDeduct) {
			this.amountAfterDeduct = amountAfterDeduct;
		}

		public void setSenderInstitutionEnabled(boolean senderInstitutionEnabled) {
			this.senderInstitutionEnabled = senderInstitutionEnabled;
		}

		public void setSenderDepartmentEnabled(boolean senderDepartmentEnabled) {
			this.senderDepartmentEnabled = senderDepartmentEnabled;
		}

		public void setSenderIndividualEnabled(boolean senderIndividualEnabled) {
			this.senderIndividualEnabled = senderIndividualEnabled;
		}



		public boolean isShowDetailsCreditedAmount() {
			return showDetailsCreditedAmount;
		}



		public void setShowDetailsCreditedAmount(boolean showDetailsCreditedAmount) {
			this.showDetailsCreditedAmount = showDetailsCreditedAmount;
		}



		public GpiType getGpiType() {
			return gpiType;
		}



		public void setGpiType(GpiType gpiType) {
			this.gpiType = gpiType;
		}



		public String getTotalDeductsStr() {
			return totalDeductsStr;
		}



		public void setTotalDeductsStr(String totalDeductsStr) {
			this.totalDeductsStr = totalDeductsStr;
		}



		public boolean isMesgWithUter() {
			return mesgWithUter;
		}



		public void setMesgWithUter(boolean mesgWithUter) {
			this.mesgWithUter = mesgWithUter;
		}



		public boolean isConfirmationMesg() {
			if(messageDetails != null){
			return messageDetails.getMesgType().equals("103") ? true : false ;	
			}
			return true;
		} 
		
		public String getTotalDuration() {
			return totalDuration;
		}

		public void setTotalDuration(String totalDuration) {
			this.totalDuration = totalDuration;
		}

		public String getSatausImage() {
			return satausImage;
		}

		public void setSatausImage(String satausImage) {
			this.satausImage = satausImage;
		}

		public String getStatusStyle() {
			return statusStyle;
		}

		public void setStatusStyle(String statusStyle) {
			this.statusStyle = statusStyle;
		}

		public String getStatusMessageGpi() {
			return statusMessageGpi;
		}

		public void setStatusMessageGpi(String statusMessageGpi) {
			this.statusMessageGpi = statusMessageGpi;
		}

		public boolean isResoneCodeEnabled() {  
			if(param.getStatusCode() != null ){ 
				resoneCodeEnabled =((param.getStatusCode().equalsIgnoreCase("ACSP")));
			}
			return resoneCodeEnabled;
		}

		public void setResoneCodeEnabled(boolean resoneCodeEnabled) {
			this.resoneCodeEnabled = resoneCodeEnabled;
		}


		public boolean isEnabelGpiSeacrh() {
			return enabelGpiSeacrh;
		}


		public void setEnabelGpiSeacrh(boolean enabelGpiSeacrh) {
			this.enabelGpiSeacrh = enabelGpiSeacrh;
		}


		public String getDeductCur() {
			return deductCur;
		}


		public void setDeductCur(String deductCur) {
			this.deductCur = deductCur;
		}


		public boolean isStayOnSameTab() {
			return stayOnSameTab;
		}


		public void setStayOnSameTab(boolean stayOnSameTab) {
			this.stayOnSameTab = stayOnSameTab;
		}

		
		
	 

}
