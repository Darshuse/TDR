package com.eastnets.textbreak.dao.beans;

import java.time.LocalDateTime;

public class LdErrors {

	private Integer id;

	private String errName;

	private LocalDateTime errorDate;

	private String errorEvel;

	private String errorModule;

	private String errorMesage1;

	private String errorMessage2;

	public LdErrors(String errName, LocalDateTime errorDate, String errorEvel, String errorModule, String errorMesage1, String errorMessage2) {

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
		return "LdErrors [id=" + id + ", xName=" + errName + ", errorDate=" + errorDate + ", errorEvel=" + errorEvel + ", errorModule=" + errorModule + ", errorMesage1=" + errorMesage1 + ", errorMessage2=" + errorMessage2 + "]";
	}

}
