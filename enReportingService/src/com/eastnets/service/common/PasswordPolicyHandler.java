package com.eastnets.service.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.eastnets.common.exception.WebClientException;
import com.eastnets.dao.security.SecurityDAO;
import com.eastnets.domain.security.PasswordPolicy;

public class PasswordPolicyHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2539832708685142622L;

	private SecurityDAO securityDAO;
	private PasswordPolicy policy = null;

	
	//needed for random password generation 
	private int generatedStrlength = 8;
	

	public SecurityDAO getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAO securityDAO) {
		this.securityDAO = securityDAO;
	}
	
	public PasswordPolicy getPolicy() {
		if ( policy == null ){
			policy =  securityDAO.getPasswordPolicy();
			if ( policy == null  ){
				throw new WebClientException("Error : could not get password policy from database.");
			}
		}
		
		return policy;
	}

	public void setPolicy(PasswordPolicy policy) {
		this.policy = policy;
	}
	
	public PasswordChangeStatus validatePassword(String username, String password ){
		if ( username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty() ){
			return PasswordChangeStatus.EMPTY;
		}
		
		String regex = "\\A[a-z|A-Z|0-9| |\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\-\\\\\\|\\?\\/\\.\\,\\<\\>\\;\\'\\[\\]\\{\\}\\\"]*\\z";
		if (! password.matches(regex) ){
			return PasswordChangeStatus.INVALID_CHAR;
		}
		
		//fill the policy if its empty
		policy= getPolicy();
		
		String lowerPassword = password.toLowerCase();
		
		if ( password.length() < policy.getMinLength() ){
			return PasswordChangeStatus.SHORT;
		}
				
		if ( password.length() > policy.getMaxLength() ){
			return PasswordChangeStatus.LONG;
		}
			
		if ( !policy.isCanContainUsername() )
		{
			if ( lowerPassword.contains(username.toLowerCase()) ){
				return PasswordChangeStatus.CONTAIN_USERNAME;
			}
		}
		
		if ( !policy.isCanContainReversedUsername() )
		{
			if ( lowerPassword.contains(StringUtils.reverse(username).toLowerCase()) ){
				return PasswordChangeStatus.CONTAIN_REVERSE_USERNAME;
			}
		}
		
		if ( !policy.isCanContainSpaces() && containsSpace( password ) ){
			return PasswordChangeStatus.CONTAIN_SPACE;
		}
		
		if ( policy.isMustContainUpperCase() && !containsUpperCaseLetter( password )  ){
			return PasswordChangeStatus.NOT_CONTAIN_UPPER;
		}
		if ( policy.isMustContainLowerCase() && !containsLowerCaseLetter(password)){
			return PasswordChangeStatus.NOT_CONTAIN_LOWER;
		}
		if ( policy.isMustContainNumber() && !containsNumber( password )){
			return PasswordChangeStatus.NOT_CONTAIN_NUMBER;
		}
		
		if ( policy.isMustContainSpecialChar() && !containsSpecialChar(password) ){
			return PasswordChangeStatus.NOT_CONTAIN_SPERCIAL_CHAR;
		}
		if ( policy.isMustStartWithAlpha() && !startsWithAlpha(password)  ){
			return PasswordChangeStatus.NOT_START_ALPHA;
		}
		
		if(!policy.isCanContainUsernamePart() && !policy.isCanContainUsername()){
			
			if ( policy.getNamePartLength() > 2)
			{
				int partLength = policy.getNamePartLength();
				if ( partLength > username.length() ){
					partLength= username.length();
				}
				for ( int i = partLength ; i <= username.length() ; ++i )
				{
					String part = username.substring(i-partLength, i).toLowerCase();
					if ( lowerPassword.contains(part) ){
						return PasswordChangeStatus.CONTAIN_USERNAME_PART;
					}
				}
			}
		}
		

		return PasswordChangeStatus.SUCCESS;
	}

	public String getRandomString() {
		String PASSWORDCHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		
		StringBuilder generatedStr = new StringBuilder(generatedStrlength);
		for (int i = 0; i < generatedStrlength; i++){
			generatedStr.append(PASSWORDCHARS.charAt(rnd.nextInt(PASSWORDCHARS.length())));
		}
		
		return generatedStr.toString();
	}

	public String getNewPolicyPassword() {
		String set1= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String set2= "abcdefghijklmnopqrstuvwxyz";
		String set3 = "0123456789";
		String set4 = "~!@#$%^&*=-.";//not the entire allowed set, but these are the most common
		
		String entireSet = set1 + set2 + set3 + set4;
		
		Random rnd = new Random();
		
		policy= getPolicy();
		
		int passwordLength = Math.max(policy.getMinLength(), 8 );
			
		
		StringBuilder generatedStr = new StringBuilder(passwordLength);
		for (int i = 0; i < passwordLength; i++){
			generatedStr.append(entireSet.charAt(rnd.nextInt(entireSet.length())));
		}
				
		List<Integer> replacedChars = new ArrayList<Integer>(); 
		if ( policy.isMustStartWithAlpha() && !startsWithAlpha( generatedStr.toString() ) ){
			generatedStr.insert(0, (set1 + set2).charAt(rnd.nextInt((set1 + set2).length())));
			replacedChars.add(0);
		}
		
		if ( policy.isMustContainUpperCase() && !containsUpperCaseLetter( generatedStr.toString() )  ){
			int index = 0;
			int loopCount = 0;
			do
			{
				if ( loopCount++ > 10 ) break;
				index= rnd.nextInt(generatedStr.length());
			}while ( !replacedChars.contains(index)  );

			generatedStr.insert(index, set1.charAt(rnd.nextInt(set1.length())));
		}
		if ( policy.isMustContainLowerCase() && !containsLowerCaseLetter(generatedStr.toString())){
			int index = 0;
			int loopCount = 0;
			do
			{
				if ( loopCount++ > 10 ) break;
				index= rnd.nextInt(generatedStr.length());
			}while ( !replacedChars.contains(index)  );

			generatedStr.insert(index, set2.charAt(rnd.nextInt(set2.length())));
		}
		if ( policy.isMustContainNumber() && !containsNumber( generatedStr.toString() )){
			int index = 0;
			int loopCount = 0;
			do
			{
				if ( loopCount++ > 10 ) break;
				index= rnd.nextInt(generatedStr.length());
			}while ( !replacedChars.contains(index)  );

			generatedStr.insert(index, set3.charAt(rnd.nextInt(set3.length())));
		}
		
		if ( policy.isMustContainSpecialChar() && !containsSpecialChar( generatedStr.toString() ) ){
			int index = 0;
			int loopCount = 0;
			do
			{
				if ( loopCount++ > 10 ) break;
				index= rnd.nextInt(generatedStr.length());
			}while ( !replacedChars.contains(index)  );

			generatedStr.insert(index, set3.charAt(rnd.nextInt(set3.length())));
		}
		
		if (generatedStr.length() > policy.getMaxLength()){
			return generatedStr.substring(0, policy.getMaxLength());
		}
		return generatedStr.toString();
	}

	private boolean startsWithAlpha(String str) {
		return str.matches("\\A[a-z|A-Z](.|\\n)*");
	}
	
	private boolean containsSpace(String str) {
		return str.matches("(.|\\n)*[ ](.|\\n)*");
	}
	
	private boolean containsUpperCaseLetter(String password) {
		return password.matches(".*[A-Z].*");
	}

	private boolean containsLowerCaseLetter(String password) {
		return password.matches(".*[a-z].*");
	}

	private boolean containsNumber(String password) {
		return password.matches(".*[0-9].*");
	}

	/**
	 * allowed special chars are `~!@#$%^&*()_+=-\|?/.,<>;'[]{}"
	 * @param str
	 * @return
	 */
	private boolean containsSpecialChar(String str) {
		return str.matches(".*[\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\-\\\\\\|\\?\\/\\.\\,\\<\\>\\;\\'\\[\\]\\{\\}\\\"].*");
	}

	public void updatePasswordPolicy(String username,PasswordPolicy passwordPolicy,PasswordPolicy oldPassword) {
		if ( passwordPolicy == null ){
			throw new WebClientException( "Null password policy passed to PasswordPolicyHandler::passwordPolicy()" );
		}
		if ( policy.getMaxLength() < policy.getMinLength() ){
			policy.setMaxLength( policy.getMinLength() );
		}
		if ( policy.getMaxLength() < 8 ){
			policy.setMaxLength( 8 );
		}
		
		securityDAO.updatePasswordPolicy(passwordPolicy);
		policy = passwordPolicy;
	}

	public void resetPolicy() {
		policy = null;
	}
}
