package com.eastnets.beans;


public class MessageKey {

	private String aid;
	private String mesgUmidl;
	private String mesgUmidh;
	
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	
	
	public String getMesgUmidl() {
		return mesgUmidl;
	}
	public void setMesgUmidl(String mesgUmidl) {
		this.mesgUmidl = mesgUmidl;
	}
	
	
	public String getMesgUmidh() {
		return mesgUmidh;
	}
	public void setMesgUmidh(String mesgUmidh) {
		this.mesgUmidh = mesgUmidh;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		MessageKey key2 = (MessageKey)obj;
		
		if (  this.aid != null && this.aid.equals(key2.aid)
		   && this.mesgUmidh != null && this.mesgUmidh.equals(key2.getMesgUmidh())
		   && this.mesgUmidl != null && this.mesgUmidl.equals(key2.getMesgUmidl()) ) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {    
		return this.aid.hashCode() + this.mesgUmidh.hashCode() + this.mesgUmidl.hashCode();  
	}
	
}
