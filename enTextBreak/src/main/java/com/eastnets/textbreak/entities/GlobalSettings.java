package com.eastnets.textbreak.entities;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LDGLOBALSETTINGS")
@Cacheable(value = false)
public class GlobalSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GlobalSettingsKey id;

	@Column(name = "OFFLINEDECOMPOSITION")
	private long offlineDecomposition;

	@Column(name = "OFFLINEWATCHDOG")
	private long offlineWatchdog;

	@Column(name = "parse_textblock")
	private long parseTextblock;

	public long getParseTextblock() {
		return parseTextblock;
	}

	public void setParseTextblock(long parseTextblock) {
		this.parseTextblock = parseTextblock;
	}

	public long getOfflineDecomposition() {
		return offlineDecomposition;
	}

	public void setOfflineDecomposition(long offlineDecomposition) {
		this.offlineDecomposition = offlineDecomposition;
	}

	public GlobalSettingsKey getId() {
		return id;
	}

	public void setId(GlobalSettingsKey id) {
		this.id = id;
	}

	public long getOfflineWatchdog() {
		return offlineWatchdog;
	}

	public void setOfflineWatchdog(long offlineWatchdog) {
		this.offlineWatchdog = offlineWatchdog;
	}

}
