package dk.sds.sts.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.rt.security.claims.Claim;
import org.apache.cxf.sts.token.provider.AttributeStatementProvider;
import org.apache.cxf.sts.token.provider.TokenProviderParameters;
import org.apache.wss4j.common.saml.bean.AttributeBean;
import org.apache.wss4j.common.saml.bean.AttributeStatementBean;
import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.childrenincustody.ChildrenInCustody;
import dk.sds.samlh.model.childrenincustody.ChildrenInCustody.CprType;
import dk.sds.samlh.model.oiobpp.PrivilegeGroup;
import dk.sds.samlh.model.oiobpp.PrivilegeList;
import dk.sds.samlh.model.onbehalfof.OnBehalfOf;
import dk.sds.samlh.model.onbehalfof.OnBehalfOf.Legislation;
import dk.sds.samlh.model.provideridentifier.ProviderIdentifier;
import dk.sds.samlh.model.purposeofuse.PurposeOfUse;
import dk.sds.samlh.model.purposeofuse.PurposeOfUse.Code;
import dk.sds.samlh.model.resourceid.ResourceId;
import dk.sds.samlh.model.role.Role;
import dk.sds.samlh.model.userauthorization.UserAuthorization;
import dk.sds.samlh.model.userauthorization.UserAuthorizationList;
import lombok.extern.log4j.Log4j;
import net.shibboleth.utilities.java.support.xml.DOMTypeSupport;

@Log4j
@Component
public class CustomAttributeStatementProvider implements AttributeStatementProvider {
	private static final String BASIC_FORMAT = "urn:oasis:names:tc:SAML:2.0:attrname-format:basic";
	
	@Override
	public AttributeStatementBean getStatement(TokenProviderParameters providerParameters) {
		List<AttributeBean> attributeList = new ArrayList<>();

		if (providerParameters.getRequestedPrimaryClaims() != null && providerParameters.getRequestedPrimaryClaims().size() > 0) {
			for (Claim claim : providerParameters.getRequestedPrimaryClaims()) {
				if (claim.getClaimType().getSchemeSpecificPart().equals("oasis:names:tc:xacml:2.0:resource:resource-id")) {
					if (claim.getValues().size() == 0) {
						log.error("Claim of type: " + claim.getClaimType().getSchemeSpecificPart() + " found but it has no value.");
						break;
					}
					
					String resourceClaim = (String) claim.getValues().get(0);
					ResourceId resourceId = null;
					try {
						// attempt to parse the incoming claim using the SAML-H library
						resourceId = ResourceId.parse(resourceClaim, Validate.YES);
						
						// then create the response, and use the SAML-H library to serialize the object back to a string
						attributeList.add(getAttribute("dk:healthcare:saml:attribute:EvidenceForPatientInCare", "urn:oasis:names:tc:SAML:2.0:attrname-format:basic", "urn:dk:healthcare:brs:B"));
						attributeList.add(getAttribute("oasis:names:tc:xacml:2.0:resource:resource-id",
													   BASIC_FORMAT,
													   resourceId.generate(Validate.YES)));
					}
					catch (ValidationException ex) {
						log.error("Bad claim: " + resourceClaim, ex);
					}
				}
			}
		}

		// set AssuranceLevel to 3
		attributeList.add(getAttribute("dk:gov:saml:attribute:AssuranceLevel", BASIC_FORMAT, "3"));

		XSAnyBuilder builder = new XSAnyBuilder();
		
		// send ChildrenInCustody claim
		try {
			ChildrenInCustody childrenInCustody = ChildrenInCustody.builder()
					.cprType(CprType.CPR)
					.value("1101116171")
					.build();

			attributeList.add(getAttribute(ChildrenInCustody.ATTRIBUTE_NAME, BASIC_FORMAT, childrenInCustody.generate(Validate.YES)));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate childrenInCustody claim!", ex);
		}
		
		// send OIO-BPP claim
		try {
			PrivilegeList oiobpp = new PrivilegeList();
			oiobpp.getPrivilegeGroups().add(PrivilegeGroup.builder()
					.scopeAuthorizationCode("341KY")
					.scopeEducationCode("7170")
					.privileges(Collections.singletonList("http://some.role.dk"))
					.build());

			attributeList.add(getAttribute(PrivilegeList.ATTRIBUTE_NAME, BASIC_FORMAT, oiobpp.generate(Validate.YES)));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate PrivilegeList claim!", ex);
		}
		
		// send OnBehalfOf claim
		try {
			OnBehalfOf onBehalfOf = OnBehalfOf.builder()
					.cpr("1101116171")
					.legislation(Legislation.actThroughCustodyOver)
					.build();

			attributeList.add(getAttribute(OnBehalfOf.ATTRIBUTE_NAME, BASIC_FORMAT, onBehalfOf.generate(Validate.YES)));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate OnBehalfOf claim!", ex);
		}
		
		// send ProviderIdentifier claim
		try {
			ProviderIdentifier providerIdentifier = ProviderIdentifier.builder()
					.assigningAuthorityName("Sundhedsvæsenets Organisationsregister (SOR)")
					.displayable(false)
					.extension("8041000016000^Sydvestjysk Sygehus")
					.root("1.2.208.176.1.1")
					.xsiType("II")
					.build();
			
			Element element = providerIdentifier.generateElement(Validate.YES);

			XSAny claim = createXSAny(builder, element);
			
			XSAny attributeValue = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
			attributeValue.getUnknownXMLObjects().add(claim);

			attributeList.add(getAttribute(ProviderIdentifier.ATTRIBUTE_NAME, BASIC_FORMAT, attributeValue));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate ProviderIdentifier claim!", ex);
		}
		
		// send PurposeOfUse claim
		try {
			PurposeOfUse purposeOfUse = PurposeOfUse.builder()
					.code(Code.TREATMENT)
					.codeSystem("urn:oasis:names:tc:xspa:1.0")
					.xsiType("CE")
					.build();

			Element element = purposeOfUse.generateElement(Validate.YES);

			XSAny claim = createXSAny(builder, element);
			
			XSAny attributeValue = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
			attributeValue.getUnknownXMLObjects().add(claim);

			attributeList.add(getAttribute(PurposeOfUse.ATTRIBUTE_NAME, BASIC_FORMAT, attributeValue));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate PurposeOfUse claim!", ex);
		}

		// send Role claim
		try {
			Role role = Role.builder()
					.code("7170")
					.codeSystem("1.2.208.176.1.3")
					.codeSystemName("Autorisationsregister")
					.displayName("Læge")
					.xsiType("CE")
					.build();

			Element element = role.generateElement(Validate.YES);

			XSAny claim = createXSAny(builder, element);
			
			XSAny attributeValue = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
			attributeValue.getUnknownXMLObjects().add(claim);

			attributeList.add(getAttribute(Role.ATTRIBUTE_NAME, BASIC_FORMAT, attributeValue));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate Role claim!", ex);
		}
		
		// send UserAuthorization claim
		try {
			UserAuthorizationList auth = new UserAuthorizationList();
			auth.getUserAuthorizations().add(UserAuthorization.builder()
					.authorizationCode("341KY")
					.educationCode("7170")
					.educationType("Læge")
					.build());

			Element element = auth.generateElement(Validate.YES);

			XSAny claim = createXSAny(builder, element);
			
			XSAny attributeValue = builder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME);
			attributeValue.getUnknownXMLObjects().add(claim);

			attributeList.add(getAttribute(UserAuthorizationList.ATTRIBUTE_NAME, BASIC_FORMAT, attributeValue));
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to generate UserAuthorization claim!", ex);
		}

		AttributeStatementBean attrBean = new AttributeStatementBean();
		attrBean.setSamlAttributes(attributeList);

		return attrBean;
	}

	private AttributeBean getAttribute(String name, String format, Object value) {
		List<Object> attributeValues = new ArrayList<>();
		attributeValues.add(value);

		AttributeBean attributeBean = new AttributeBean();
		attributeBean.setQualifiedName(name);
		attributeBean.setNameFormat(format);
		attributeBean.setAttributeValues(attributeValues);

		return attributeBean;
	}
	
	private XSAny createXSAny(XSAnyBuilder builder, Node element) {
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
