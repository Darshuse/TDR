//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.23 at 04:31:47 PM EET 
//


package com.eastnets.mx.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SWIFTNetSecurityInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SWIFTNetSecurityInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IsNRRequested" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="SignerDN" type="{urn:swift:saa:xsd:saa.2.0}DN" minOccurs="0"/&gt;
 *         &lt;element name="NRType" type="{urn:swift:saa:xsd:saa.2.0}NRType" minOccurs="0"/&gt;
 *         &lt;element name="NRWarning" type="{urn:swift:saa:xsd:saa.2.0}NRWarning" minOccurs="0"/&gt;
 *         &lt;element name="SignatureResult" type="{urn:swift:saa:xsd:saa.2.0}SignatureResult" minOccurs="0"/&gt;
 *         &lt;element name="SignatureValue" type="{urn:swift:saa:xsd:saa.2.0}SwAny" minOccurs="0"/&gt;
 *         &lt;element name="ResponseNRType" type="{urn:swift:saa:xsd:saa.2.0}NRType" minOccurs="0"/&gt;
 *         &lt;element name="ResponseNRWarning" type="{urn:swift:saa:xsd:saa.2.0}NRWarning" minOccurs="0"/&gt;
 *         &lt;element name="ResponseSignatureResult" type="{urn:swift:saa:xsd:saa.2.0}SignatureResult" minOccurs="0"/&gt;
 *         &lt;element name="ResponseSignatureValue" type="{urn:swift:saa:xsd:saa.2.0}SwAny" minOccurs="0"/&gt;
 *         &lt;element name="FileDigestAlgorithm" type="{urn:swift:saa:xsd:saa.2.0}FileDigestAlgorithm" minOccurs="0"/&gt;
 *         &lt;element name="FileDigestValue" type="{urn:swift:saa:xsd:saa.2.0}FileDigestValue" minOccurs="0"/&gt;
 *         &lt;element name="DigestList" type="{urn:swift:saa:xsd:saa.2.0}DigestList" minOccurs="0"/&gt;
 *         &lt;element name="ThirdPartySignerDN" type="{urn:swift:saa:xsd:saa.2.0}DN" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SWIFTNetSecurityInfo", propOrder = {
    "isNRRequested",
    "signerDN",
    "nrType",
    "nrWarning",
    "signatureResult",
    "signatureValue",
    "responseNRType",
    "responseNRWarning",
    "responseSignatureResult",
    "responseSignatureValue",
    "fileDigestAlgorithm",
    "fileDigestValue",
    "digestList",
    "thirdPartySignerDN"
})
public class SWIFTNetSecurityInfo {

    @XmlElement(name = "IsNRRequested")
    protected Boolean isNRRequested;
    @XmlElement(name = "SignerDN")
    protected String signerDN;
    @XmlElement(name = "NRType")
    @XmlSchemaType(name = "string")
    protected NRType nrType;
    @XmlElement(name = "NRWarning")
    protected String nrWarning;
    @XmlElement(name = "SignatureResult")
    @XmlSchemaType(name = "string")
    protected SignatureResult signatureResult;
    @XmlElement(name = "SignatureValue")
    protected SwAny signatureValue;
    @XmlElement(name = "ResponseNRType")
    @XmlSchemaType(name = "string")
    protected NRType responseNRType;
    @XmlElement(name = "ResponseNRWarning")
    protected String responseNRWarning;
    @XmlElement(name = "ResponseSignatureResult")
    @XmlSchemaType(name = "string")
    protected SignatureResult responseSignatureResult;
    @XmlElement(name = "ResponseSignatureValue")
    protected SwAny responseSignatureValue;
    @XmlElement(name = "FileDigestAlgorithm")
    @XmlSchemaType(name = "string")
    protected FileDigestAlgorithm fileDigestAlgorithm;
    @XmlElement(name = "FileDigestValue")
    protected String fileDigestValue;
    @XmlElement(name = "DigestList")
    protected DigestList digestList;
    @XmlElement(name = "ThirdPartySignerDN")
    protected String thirdPartySignerDN;

    /**
     * Gets the value of the isNRRequested property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNRRequested() {
        return isNRRequested;
    }

    /**
     * Sets the value of the isNRRequested property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNRRequested(Boolean value) {
        this.isNRRequested = value;
    }

    /**
     * Gets the value of the signerDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignerDN() {
        return signerDN;
    }

    /**
     * Sets the value of the signerDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignerDN(String value) {
        this.signerDN = value;
    }

    /**
     * Gets the value of the nrType property.
     * 
     * @return
     *     possible object is
     *     {@link NRType }
     *     
     */
    public NRType getNRType() {
        return nrType;
    }

    /**
     * Sets the value of the nrType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NRType }
     *     
     */
    public void setNRType(NRType value) {
        this.nrType = value;
    }

    /**
     * Gets the value of the nrWarning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRWarning() {
        return nrWarning;
    }

    /**
     * Sets the value of the nrWarning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRWarning(String value) {
        this.nrWarning = value;
    }

    /**
     * Gets the value of the signatureResult property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureResult }
     *     
     */
    public SignatureResult getSignatureResult() {
        return signatureResult;
    }

    /**
     * Sets the value of the signatureResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureResult }
     *     
     */
    public void setSignatureResult(SignatureResult value) {
        this.signatureResult = value;
    }

    /**
     * Gets the value of the signatureValue property.
     * 
     * @return
     *     possible object is
     *     {@link SwAny }
     *     
     */
    public SwAny getSignatureValue() {
        return signatureValue;
    }

    /**
     * Sets the value of the signatureValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link SwAny }
     *     
     */
    public void setSignatureValue(SwAny value) {
        this.signatureValue = value;
    }

    /**
     * Gets the value of the responseNRType property.
     * 
     * @return
     *     possible object is
     *     {@link NRType }
     *     
     */
    public NRType getResponseNRType() {
        return responseNRType;
    }

    /**
     * Sets the value of the responseNRType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NRType }
     *     
     */
    public void setResponseNRType(NRType value) {
        this.responseNRType = value;
    }

    /**
     * Gets the value of the responseNRWarning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseNRWarning() {
        return responseNRWarning;
    }

    /**
     * Sets the value of the responseNRWarning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseNRWarning(String value) {
        this.responseNRWarning = value;
    }

    /**
     * Gets the value of the responseSignatureResult property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureResult }
     *     
     */
    public SignatureResult getResponseSignatureResult() {
        return responseSignatureResult;
    }

    /**
     * Sets the value of the responseSignatureResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureResult }
     *     
     */
    public void setResponseSignatureResult(SignatureResult value) {
        this.responseSignatureResult = value;
    }

    /**
     * Gets the value of the responseSignatureValue property.
     * 
     * @return
     *     possible object is
     *     {@link SwAny }
     *     
     */
    public SwAny getResponseSignatureValue() {
        return responseSignatureValue;
    }

    /**
     * Sets the value of the responseSignatureValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link SwAny }
     *     
     */
    public void setResponseSignatureValue(SwAny value) {
        this.responseSignatureValue = value;
    }

    /**
     * Gets the value of the fileDigestAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link FileDigestAlgorithm }
     *     
     */
    public FileDigestAlgorithm getFileDigestAlgorithm() {
        return fileDigestAlgorithm;
    }

    /**
     * Sets the value of the fileDigestAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileDigestAlgorithm }
     *     
     */
    public void setFileDigestAlgorithm(FileDigestAlgorithm value) {
        this.fileDigestAlgorithm = value;
    }

    /**
     * Gets the value of the fileDigestValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileDigestValue() {
        return fileDigestValue;
    }

    /**
     * Sets the value of the fileDigestValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileDigestValue(String value) {
        this.fileDigestValue = value;
    }

    /**
     * Gets the value of the digestList property.
     * 
     * @return
     *     possible object is
     *     {@link DigestList }
     *     
     */
    public DigestList getDigestList() {
        return digestList;
    }

    /**
     * Sets the value of the digestList property.
     * 
     * @param value
     *     allowed object is
     *     {@link DigestList }
     *     
     */
    public void setDigestList(DigestList value) {
        this.digestList = value;
    }

    /**
     * Gets the value of the thirdPartySignerDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThirdPartySignerDN() {
        return thirdPartySignerDN;
    }

    /**
     * Sets the value of the thirdPartySignerDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThirdPartySignerDN(String value) {
        this.thirdPartySignerDN = value;
    }

}
