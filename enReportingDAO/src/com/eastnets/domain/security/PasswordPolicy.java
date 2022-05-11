package com.eastnets.domain.security;

import java.io.Serializable;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;

import com.eastnets.domain.admin.User;

public class PasswordPolicy implements Serializable, Cloneable,Diffable<PasswordPolicy>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5307971522958483280L;

	private int 	minLength;
	private int 	maxLength;

	private boolean	canContainUsername;
	private boolean	canContainReversedUsername;

	private boolean	canContainUsernamePart;//3 characters of more
	private int		namePartLength = 3;

	private boolean	canContainSpaces;

	private boolean	mustContainUpperCase;
	private boolean	mustContainLowerCase;
	private boolean	mustContainNumber;
	private boolean	mustContainSpecialChar;

	private boolean	mustStartWithAlpha;

	private int passwordExpirationInDays;
	private int passwordExpirationWarningInDays;
	private int passwordHistoryLenght;
	private int numberOfAttempts;


	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isCanContainUsername() {
		return canContainUsername;
	}
	public void setCanContainUsername(boolean canContainUsername) {
		this.canContainUsername = canContainUsername;
	}
	public boolean isCanContainReversedUsername() {
		return canContainReversedUsername;
	}
	public void setCanContainReversedUsername(boolean canContainReversedUsername) {
		this.canContainReversedUsername = canContainReversedUsername;
	}
	public boolean isCanContainUsernamePart() {
		return canContainUsernamePart;
	}
	public void setCanContainUsernamePart(boolean canContainUsernamePart) {
		this.canContainUsernamePart = canContainUsernamePart;
	}
	public boolean isCanContainSpaces() {
		return canContainSpaces;
	}
	public void setCanContainSpaces(boolean canContainSpaces) {
		this.canContainSpaces = canContainSpaces;
	}
	public boolean isMustContainUpperCase() {
		return mustContainUpperCase;
	}
	public void setMustContainUpperCase(boolean mustContainUpperCase) {
		this.mustContainUpperCase = mustContainUpperCase;
	}
	public boolean isMustContainLowerCase() {
		return mustContainLowerCase;
	}
	public void setMustContainLowerCase(boolean mustContainLowerCase) {
		this.mustContainLowerCase = mustContainLowerCase;
	}
	public boolean isMustContainNumber() {
		return mustContainNumber;
	}
	public void setMustContainNumber(boolean mustContainNumber) {
		this.mustContainNumber = mustContainNumber;
	}
	public boolean isMustContainSpecialChar() {
		return mustContainSpecialChar;
	}
	public void setMustContainSpecialChar(boolean mustContainSpecialChar) {
		this.mustContainSpecialChar = mustContainSpecialChar;
	}
	public boolean isMustStartWithAlpha() {
		return mustStartWithAlpha;
	}
	public void setMustStartWithAlpha(boolean mustStartWithAlpha) {
		this.mustStartWithAlpha = mustStartWithAlpha;
	}
	public int getPasswordExpirationInDays() {
		return passwordExpirationInDays;
	}
	public void setPasswordExpirationInDays(int passwordExpirationInDays) {
		this.passwordExpirationInDays = passwordExpirationInDays;
	}
	public int getPasswordExpirationWarningInDays() {
		return passwordExpirationWarningInDays;
	}
	public void setPasswordExpirationWarningInDays(int passwordExpirationWarningInDays) {
		this.passwordExpirationWarningInDays = passwordExpirationWarningInDays;
	}
	public int getPasswordHistoryLenght() {
		return passwordHistoryLenght;
	}
	public void setPasswordHistoryLenght(int passwordHistoryLenght) {
		this.passwordHistoryLenght = passwordHistoryLenght;
	}
	public int getNamePartLength() {
		return namePartLength;
	}
	public void setNamePartLength(int namePartLength) {
		this.namePartLength = namePartLength;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public int getNumberOfAttempts() {
		return numberOfAttempts;
	}
	public void setNumberOfAttempts(int numberOfAttempts) {
		this.numberOfAttempts = numberOfAttempts;
	}

	/*
	 * 	private int 	minLength;
	private int 	maxLength; 
	private int		namePartLength = 3; 
	private int passwordExpirationInDays;
	private int passwordExpirationWarningInDays;
	private int passwordHistoryLenght;
	private int numberOfAttempts;




	private boolean	mustContainSpecialChar;
	 * 
	 * */


	@Override
	public DiffResult diff(PasswordPolicy passwordPolicy) { 
		DiffBuilder compare = new DiffBuilder(this, passwordPolicy, null,false);
		compare.append("Must Contain Upper Case",this.isMustContainUpperCase(),passwordPolicy.isMustContainUpperCase());
		compare.append("Must Contain Lower Case",this.isMustContainLowerCase(),passwordPolicy.isMustContainLowerCase());

		compare.append("Can Contain Username",this.isCanContainUsername(),passwordPolicy.isCanContainUsername());
		compare.append("Can Contain Reversed Username",this.isCanContainReversedUsername(),passwordPolicy.isCanContainReversedUsername());
		compare.append("Can Contain User name Part",this.isCanContainUsernamePart(),passwordPolicy.isCanContainUsernamePart());
		compare.append("Can Contain Spaces",this.isCanContainSpaces(),passwordPolicy.isCanContainSpaces());
		compare.append("Must Start With Alpha",this.isMustStartWithAlpha(),passwordPolicy.isMustStartWithAlpha());
		compare.append("Must Contain Number",this.isMustContainNumber(),passwordPolicy.isMustContainNumber());
		compare.append("Must Contain Special Char",this.isMustContainSpecialChar(),passwordPolicy.isMustContainSpecialChar());  
		compare.append("Min Length",this.getMinLength(),passwordPolicy.getMinLength()); 
		compare.append("Max Length",this.getMaxLength(),passwordPolicy.getMaxLength()); 
		compare.append("Name Part Length",this.getNamePartLength(),passwordPolicy.getNamePartLength()); 
		compare.append("Password Expiration In Days",this.getPasswordExpirationInDays(),passwordPolicy.getPasswordExpirationInDays()); 
		compare.append("Password Expiration Warning In Days",this.getPasswordExpirationWarningInDays(),passwordPolicy.getPasswordExpirationWarningInDays()); 
		compare.append("Password History Lenght",this.getPasswordHistoryLenght(),passwordPolicy.getPasswordHistoryLenght()); 
		compare.append("Number Of Attempts",this.getNumberOfAttempts(),passwordPolicy.getNumberOfAttempts());  
		
		return compare.build();

	}

}
