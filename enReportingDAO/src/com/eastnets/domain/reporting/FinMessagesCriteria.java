/**
 * 
 */
package com.eastnets.domain.reporting;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author EastNets
 * @since dNov 11, 2012
 *  
 */

public class FinMessagesCriteria implements Serializable, ReportingSearchCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4598773977825533309L;

	private static Map<String, Class<?>> fieldsMap;
	
	private Date creationDate = new Date();
	private String sender = "";
	private String receiver = "";
	private String name = "";
	private boolean newCriteria = false;
	
	static {
		fieldsMap = new HashMap<String, Class<?>>();
		final Field[] fields = ReportingSearchCriteria.class.getFields();
		for(Field field : fields) {
			if(!field.getName().equalsIgnoreCase("fieldsMap")) {
				fieldsMap.put(field.getName(), field.getType());
			}
		}
	}

	@Override
	public Map<String, Class<?>> getFieldsMap() {
		return fieldsMap;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the newCriteria
	 */
	public boolean isNewCriteria() {
		return newCriteria;
	}

	/**
	 * @param newCriteria the newCriteria to set
	 */
	public void setNewCriteria(boolean newCriteria) {
		this.newCriteria = newCriteria;
	}
}
