package com.eastnets.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LDERRORS")
public class LdErrors implements Serializable {

	private static final long serialVersionUID = -9157142905784749237L;

	@Column(name = "ERRID")
	@Id
	@SequenceGenerator(name = "LDERRORS_ID_GENERATOR", sequenceName = "LDERRORS_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LDERRORS_ID_GENERATOR")
	private Integer id;

	@Column(name = "ERREXENAME")
	private String errName;

	@Column(name = "ERRTIME")
	private LocalDateTime errorDate;

	@Column(name = "ERRLEVEL")
	private String errorEvel;

	@Column(name = "ERRMODULE")
	private String errorModule;

	@Column(name = "ERRMSG1")
	private String errorMesage1;

	@Column(name = "ERRMSG2")
	private String errorMessage2;

	public LdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1,
			String errorMessage2) {

		this.errName = errName;
		this.errorDate = errorDate;
		this.errorEvel = errorEvel;
		this.errorModule = errorModule;
		this.errorMesage1 = errorMesage1;
		this.errorMessage2 = errorMessage2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getErrName() {
		return errName;
	}

	public void setErrName(String errName) {
		this.errName = errName;
	}

	public LocalDateTime getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(LocalDateTime errorDate) {
		this.errorDate = errorDate;
	}

	public String getErrorEvel() {
		return errorEvel;
	}

	public void setErrorEvel(String errorEvel) {
		this.errorEvel = errorEvel;
	}

	public String getErrorModule() {
		return errorModule;
	}

	public void setErrorModule(String errorModule) {
		this.errorModule = errorModule;
	}

	public String getErrorMesage1() {
		return errorMesage1;
	}

	public void setErrorMesage1(String errorMesage1) {
		this.errorMesage1 = errorMesage1;
	}

	public String getErrorMessage2() {
		return errorMessage2;
	}

	public void setErrorMessage2(String errorMessage2) {
		this.errorMessage2 = errorMessage2;
	}

	@Override
	public String toString() {
		return "LdErrors [id=" + id + ", xName=" + errName + ", errorDate=" + errorDate + ", errorEvel=" + errorEvel
				+ ", errorModule=" + errorModule + ", errorMesage1=" + errorMesage1 + ", errorMessage2=" + errorMessage2
				+ "]";
	}

}
