package com.eastnets.domain.loader;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.eastnets.domain.loader.Inst;

@Entity
@Table(name = "RINST")
@NamedQuery(name = "InstPart.findAll", query = "SELECT i FROM InstPart i")
@Cacheable(value = false)
public class InstPart extends Inst implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1685006577716837490L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "X_CREA_DATE_TIME_MESG",nullable=true)
	private Date mesgCreaDateTime;

 
	public Date getMesgCreaDateTime() {
		return mesgCreaDateTime;
	}
	public void setMesgCreaDateTime(Date mesgCreaDateTime) {
		this.mesgCreaDateTime = mesgCreaDateTime;
	}
	@Override
	public String toString() {
		 return "Hol";
	}
}
