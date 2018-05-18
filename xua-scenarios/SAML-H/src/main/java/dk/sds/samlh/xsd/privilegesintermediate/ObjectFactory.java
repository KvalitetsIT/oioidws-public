//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.13 at 03:08:54 PM CEST 
//


package dk.sds.samlh.xsd.privilegesintermediate;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.sts.xsd.privilegesintermediate package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PrivilegeList_QNAME = new QName("http://itst.dk/oiosaml/basic_privilege_profile", "PrivilegeList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.sts.xsd.privilegesintermediate
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PrivilegeListType }
     * 
     */
    public PrivilegeListType createPrivilegeListType() {
        return new PrivilegeListType();
    }

    /**
     * Create an instance of {@link PrivilegeGroupType }
     * 
     */
    public PrivilegeGroupType createPrivilegeGroupType() {
        return new PrivilegeGroupType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrivilegeListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://itst.dk/oiosaml/basic_privilege_profile", name = "PrivilegeList")
    public JAXBElement<PrivilegeListType> createPrivilegeList(PrivilegeListType value) {
        return new JAXBElement<PrivilegeListType>(_PrivilegeList_QNAME, PrivilegeListType.class, null, value);
    }

}
