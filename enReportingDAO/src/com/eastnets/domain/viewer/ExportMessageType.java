package com.eastnets.domain.viewer;

public enum ExportMessageType {
	
	ALL_MESSAGES(1),
	CURRENT_MESSAGE_IN_PAGE(2),
	SELECTED_MESSAGE(3);
	private   int type;
	
	private ExportMessageType(int type) {
		 this.type = type;
	}

	public int getType() {
		return type;
	} 
 
	

}
