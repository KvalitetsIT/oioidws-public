package dk.sds.samlh.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public interface ClaimModel {
	enum ClaimType {
		TEXT, ELEMENT
	};

	String generate(Validate validate) throws ValidationException, JAXBException;
	String getAttributeName();
	ClaimType getClaimType();

	default Element generateElement(Validate validate) throws ValidationException, JAXBException, SAXException, IOException, ParserConfigurationException {
		if (!getClaimType().equals(ClaimType.ELEMENT)) {
			throw new ValidationException("Object is not an Element type!");
		}
		
		String res = generate(validate);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8")));

		DocumentBuilderFactory b = DocumentBuilderFactory.newInstance();
		b.setNamespaceAware(true);
		DocumentBuilder db = b.newDocumentBuilder();

		Document doc = db.parse(bais);
		
		return doc.getDocumentElement();
	}
	
	default Element generateClaim(Validate validate) throws ParserConfigurationException, DOMException, ValidationException, JAXBException, SAXException, IOException {
		Document doc = ModelUtil.createDocument();
		Element claimsElement = doc.createElementNS("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "wst:Claims");
		claimsElement.setAttributeNS(null, "Dialect", "http://docs.oasis-open.org/wsfed/authorization/200706/authclaims");
		doc.appendChild(claimsElement);
		
		Element claimType = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "auth:ClaimType");
		claimType.setAttributeNS(null, "Uri", getAttributeName());
		claimsElement.appendChild(claimType);

		Element claimValue = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "auth:Value");

		if (getClaimType().equals(ClaimType.ELEMENT)) {
			Element element = generateElement(validate);
			Element importedElement = (Element) doc.importNode(element, true);

			claimValue.appendChild(importedElement);
		}
		else {
			claimValue.setTextContent(generate(validate));
		}

		claimType.appendChild(claimValue);

		return claimsElement;
	}
}
