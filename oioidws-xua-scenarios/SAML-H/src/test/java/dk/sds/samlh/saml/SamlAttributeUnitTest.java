package dk.sds.samlh.saml;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallerFactory;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.impl.AttributeBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.provideridentifier.ProviderIdentifier;
import dk.sds.samlh.model.userauthorization.UserAuthorization;
import dk.sds.samlh.model.userauthorization.UserAuthorizationList;
import net.shibboleth.utilities.java.support.xml.DOMTypeSupport;

public class SamlAttributeUnitTest {

	@Test
	public void testClaimGeneration() throws Exception {
		UserAuthorizationList auth = new UserAuthorizationList();
		auth.getUserAuthorizations().add(UserAuthorization.builder()
				.authorizationCode("341KY")
				.educationCode("7170")
				.educationType("Læge")
				.build());
		
		Element claim = auth.generateClaim(Validate.YES);

		String str = ModelUtil.dom2String(claim.getOwnerDocument());

		// compare with expected output
		assertEquals("<wst:Claims Dialect=\"http://docs.oasis-open.org/wsfed/authorization/200706/authclaims\" xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\"><auth:ClaimType Uri=\"dk:healthcare:saml:attribute:UserAuthorizations\" xmlns:auth=\"http://docs.oasis-open.org/wsfed/authorization/200706\"><auth:Value><UserAuthorizationList xmlns=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\"><UserAuthorization><AuthorizationCode>341KY</AuthorizationCode><EducationCode>7170</EducationCode><EducationType>Læge</EducationType></UserAuthorization></UserAuthorizationList></auth:Value></auth:ClaimType></wst:Claims>", str);
	}

	@Test
	public void testElementToAttributeConversion() throws Exception {		
		ProviderIdentifier providerIdentifier = ProviderIdentifier.builder()
				.assigningAuthorityName("Sundhedsvæsenets Organisationsregister (SOR)")
				.displayable(false)
				.extension("8041000016000^Sydvestjysk Sygehus")
				.root("1.2.208.176.1.1")
				.xsiType("II")
				.build();
		
		Element element = providerIdentifier.generateElement(Validate.YES);

		// initialize OpenSAML XMLObjectProvider
		InitializationService.initialize();
		
		// build our claim value
		XSAnyBuilder builder = new XSAnyBuilder();
		XSAny claim = createXSAny(builder, element);		
		XSAny attributeValue = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
		attributeValue.getUnknownXMLObjects().add(claim);

		// wrap in Attribute
		AttributeBuilder attributeBuilder = new AttributeBuilder();
		Attribute attr = attributeBuilder.buildObject();
		attr.setName(providerIdentifier.getAttributeName());
		attr.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:basic");
		attr.getAttributeValues().add(attributeValue);

		// marshal and print
		Element dom = marshallObject(attr);		
		String str = ModelUtil.dom2String(dom.getOwnerDocument());
				
		// compare with expected output
		assertEquals("<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:ihe:iti:xua:2017:subject:provider-identifier\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml2:AttributeValue><id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\" displayable=\"false\" extension=\"8041000016000^Sydvestjysk Sygehus\" root=\"1.2.208.176.1.1\" xsi:type=\"II\"/></saml2:AttributeValue></saml2:Attribute>", str);
	}

	private static Element marshallObject(XMLObject object) throws MarshallingException {
		if (object.getDOM() == null) {
			MarshallerFactory marshallerFactory = XMLObjectProviderRegistrySupport.getMarshallerFactory();

			Marshaller m = marshallerFactory.getMarshaller(object);
			if (m == null) {
				throw new IllegalArgumentException("No unmarshaller for " + object);
			}

			return m.marshall(object);
		}

		return object.getDOM();
	}

	// utility method for converting a DOM element to a XSAny instance
	private static XSAny createXSAny(XSAnyBuilder builder, Node element) {
        XSAny claim = builder.buildObject(element.getNamespaceURI(), element.getLocalName(), null, DOMTypeSupport.getXSIType((Element) element));

		// copy attributes
		if (element.hasAttributes()) {
			NamedNodeMap attributes = element.getAttributes();

			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);

				// namespace and type are dealt with through the constructor
				if ("xmlns".equals(attribute.getPrefix()) || "xmlns".equals(attribute.getNodeName())) {
					continue;
				}
				else if ("http://www.w3.org/2001/XMLSchema-instance".equals(attribute.getNamespaceURI()) && "type".equals(attribute.getLocalName())) {
					continue;
				}

				QName qname = new QName(attribute.getNamespaceURI(), attribute.getLocalName());
				claim.getUnknownAttributes().put(qname, attribute.getNodeValue());
			}
		}
		
		// copy children
		if (element.hasChildNodes())
		{
			NodeList childNodes = element.getChildNodes();

			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);

				if (child.getNodeType() == Node.TEXT_NODE) {
					claim.setTextContent(child.getTextContent());
				}
				else if (child.getNodeType() == Node.ELEMENT_NODE) {
					XSAny childClaim = createXSAny(builder, child);
					claim.getUnknownXMLObjects().add(childClaim);				
				}
			}
		}

		return claim;
	}
}
