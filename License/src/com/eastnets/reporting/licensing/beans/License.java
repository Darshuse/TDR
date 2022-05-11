/**
 * 
 */
package com.eastnets.reporting.licensing.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.eastnets.encdec.AESEncryptDecrypt;
import com.eastnets.reporting.licensing.util.BeanFactory;


/**
 * 
 * @author EastNets
 * @since dNov 26, 2012
 * 
 */
public abstract class License implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3591257512055332350L;
	private LicenseMisc licenseMisc;
	private LinkedList<Product> products = new LinkedList<Product>();
	private LinkedList<Product> traffics = new LinkedList<Product>();
	private List<BicCode> bicCodes = new ArrayList<BicCode>();
	
	private static final String LICENCE_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String LICENCE_DATE_PREFIX = "@#$EaSTnEtS&8^*&%";//just to make it harder 
	private static final String LICENCE_DATE_POSTFIX = "#LIcenSe:aDeFFFgEkjfE%4%^#@@4@234$%Ydrgf34t";//just to make it harder 
	
	private Date licenseDate ;//we store the date in the license file it self so that relicense will not work for older/same license(s)
	
	private String[] licenseKeys;

	public License(LicenseMisc licenseMisc) {
		this.licenseMisc = licenseMisc;
	}

	/**
	 * @return the licenseKeys
	 */
	public String[] getLicenseKeys() {
		return licenseKeys;
	}

	/**
	 * @param licenseKeys the licenseKeys to set
	 */
	public void setLicenseKeys(String[] licenseKeys) {
		this.licenseKeys = licenseKeys;
	}

	/**
	 * @return the products
	 */
	public LinkedList<Product> getProducts() {
		return products;
	}

	/**
	 * @return the licensedProducts
	 */
	public LinkedList<Product> getLicensedProducts() {
		LinkedList<Product> list2Retutn = new LinkedList<Product>();
		for(Product product : this.products) {
			if(product.isLicensed()) {
				list2Retutn.add(product);
			}
		}
		return list2Retutn;
	}

	/**
	 * @return the notLicensedProducts
	 */
	public LinkedList<Product> getNotLicensedProducts() {
		LinkedList<Product> list2Retutn = new LinkedList<Product>();
		for(Product product : this.products) {
			if(!product.isLicensed()) {
				list2Retutn.add(product);
			}
		}
		return list2Retutn;
	}

	/**
	 * @return the traffics
	 */
	public LinkedList<Product> getTraffics() {
		return traffics;
	}

	/**
	 * @return the licensedTraffics
	 */
	public LinkedList<Product> getLicensedTraffics() {
		LinkedList<Product> list2Retutn = new LinkedList<Product>();
		list2Retutn.add(BeanFactory.getInstance().getNewProduct("00", "FIN Messages", null, true));
		for(Product traffic : this.traffics) {
			if(traffic.isLicensed()) {
				list2Retutn.add(traffic);
			}
		}
		return list2Retutn;
	}

	/**
	 * @return the notLicensedTraffics
	 */
	public LinkedList<Product> getNotLicensedTraffics() {
		LinkedList<Product> list2Retutn = new LinkedList<Product>();
		for(Product traffic : this.traffics) {
			if(!traffic.isLicensed()) {
				list2Retutn.add(traffic);
			}
		}
		return list2Retutn;
	}

	/**
	 * @return the bicCodes
	 */
	public List<BicCode> getBicCodes() {
		return bicCodes;
	}

	/**
	 * @return the licenseMisc
	 */
	public LicenseMisc getLicenseMisc() {
		return licenseMisc;
	}

	public Date getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(Date licenseDate) {
		this.licenseDate = licenseDate;
	}

	public String getLicenseDateEncrypted( boolean simple ) throws Exception {
		//LICENCE_DATE_FORMAT
		//this.licenseDate ;
		  
        SimpleDateFormat sdf = new SimpleDateFormat( License.LICENCE_DATE_FORMAT);
        String licenseDateStr=  sdf.format( licenseDate );
        if ( !simple ){
        	licenseDateStr = LICENCE_DATE_PREFIX + licenseDateStr + LICENCE_DATE_POSTFIX;//add more data for complexity 
        }
		return encrypt( licenseDateStr );
	}

	public static Date getDateFromEncryptedString(String licenseDateEncrypted, boolean simple) throws Exception {
		String licenseDateStr=  decrypt( licenseDateEncrypted );
		if( licenseDateStr == null || licenseDateStr.trim().isEmpty() ){
			return null;
		}
		if ( ! simple ){
			//string should start with the prefix and end with the postfix
			if ( !licenseDateStr.startsWith(LICENCE_DATE_PREFIX) || !licenseDateStr.endsWith(LICENCE_DATE_POSTFIX  ) ){
				return null;
			}
			licenseDateStr= licenseDateStr.substring(LICENCE_DATE_PREFIX.length() , licenseDateStr.length() -  LICENCE_DATE_POSTFIX.length() );//remove the prefix and the postfix 
		}
		Date licenseDate = null; 
		//fill the date 
		SimpleDateFormat sdf = new SimpleDateFormat( License.LICENCE_DATE_FORMAT );
		try{
			licenseDate=  sdf.parse( licenseDateStr );
		}catch( Exception e){
			return null;
		}
		return licenseDate;
	}


	public static String encrypt(String licenseDate2) throws Exception {
			return AESEncryptDecrypt.encrypt(licenseDate2);
	}
	
	public static String decrypt(String licenseDateEncrypted) throws Exception {
		return AESEncryptDecrypt.decrypt(licenseDateEncrypted);
	}
}