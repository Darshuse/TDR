package com.eastnets.dao.jrnl;

import java.util.List;
import com.eastnets.dao.DAO;
import com.eastnets.domain.jrnl.DetailsEntity;
import com.eastnets.domain.jrnl.JournalSearchProcedureParameters;
import com.eastnets.domain.jrnl.JournalShowDetailsProcedureParameters;


/**
 * Journal DAO Interface 
 * @author EastNets
 * @since December 24, 2012
 */
public interface JrnlDAO extends DAO {

	/**
	 * Search for journals by providing search procedure with the required input parameters
	 * @param inParams
	 * @return Search result
	 */
	public Object search(JournalSearchProcedureParameters inParams) throws Exception ;
	
	public DetailsEntity showDetails(JournalShowDetailsProcedureParameters inParams) throws Exception ;
	
	public List<String> fillLookupEventClass() throws Exception ;
	
	public List<String> fillLookupEventSeverity() throws Exception ;
	
	public List<String> fillLookupApplicationService() throws Exception ;
	
	public List<String> fillLookupComponent() throws Exception ;
	
	public List<String> fillLookupEventType() throws Exception ;
	
	public List<String> fillLookupHostname() throws Exception ;
	
	public List<String> fillLookupAlarmType() throws Exception ;
	
}
