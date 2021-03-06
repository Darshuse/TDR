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
 * <p>Java class for RoutingStep.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoutingStep"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Verify"/&gt;
 *     &lt;enumeration value="Authorise"/&gt;
 *     &lt;enumeration value="Modify"/&gt;
 *     &lt;enumeration value="ReadyToSend"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RoutingStep")
@XmlEnum
public enum RoutingStep {

    @XmlEnumValue("Verify")
    VERIFY("Verify"),
    @XmlEnumValue("Authorise")
    AUTHORISE("Authorise"),
    @XmlEnumValue("Modify")
    MODIFY("Modify"),
    @XmlEnumValue("ReadyToSend")
    READY_TO_SEND("ReadyToSend");
    private final String value;

    RoutingStep(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RoutingStep fromValue(String v) {
        for (RoutingStep c: RoutingStep.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
