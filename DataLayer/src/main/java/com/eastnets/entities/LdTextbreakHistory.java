package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LDTEXTBREAKHISTORY")
@Cacheable(value = false)
public class LdTextbreakHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7926208863626925876L;

	@EmbeddedId
	private LdTextbreakHistoryPK id;

	@Lob
	@Column(name = "MESG_TEXT")
	private String mesgText;



	public Mesg getMesg() {
		return mesg;
	}

	public void setMesg(Mesg mesg) {
		this.mesg = mesg;
	}

	@OneToOne
	@JoinColumns({ @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false),
		@JoinColumn(name = "MESG_S_UMIDH", referencedColumnName = "MESG_S_UMIDH", insertable = false, updatable = false),
		@JoinColumn(name = "MESG_S_UMIDL", referencedColumnName = "MESG_S_UMIDL", insertable = false, updatable = false) })
	private Mesg mesg;


	public LdTextbreakHistoryPK getId() {
		return id;
	}

	public void setId(LdTextbreakHistoryPK id) {
		this.id = id;
	}

	public String getMesgText() {
		return mesgText;
	}

	public void setMesgText(String mesgText) {
		this.mesgText = mesgText;
	}




}
