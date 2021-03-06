//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.23 at 04:31:47 PM EET 
//


package com.eastnets.mx.mapping;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageContext.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageContext"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Original"/&gt;
 *     &lt;enumeration value="Copy"/&gt;
 *     &lt;enumeration value="Report"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MessageContext")
@XmlEnum
public enum MessageContext {

    @XmlEnumValue("Original")
    ORIGINAL("Original"),
    @XmlEnumValue("Copy")
    COPY("Copy"),
    @XmlEnumValue("Report")
    REPORT("Report");
    private final String value;

    MessageContext(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageContext fromValue(String v) {
        for (MessageContext c: MessageContext.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
