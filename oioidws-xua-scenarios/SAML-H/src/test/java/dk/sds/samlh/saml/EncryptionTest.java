package dk.sds.samlh.saml;

import static org.junit.Assert.assertEquals;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import dk.sds.samlh.encryption.SAMLHEncrypter;
import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.resourceid.ResourceId;
import dk.sds.samlh.model.role.Role;

public class EncryptionTest {

	@Test
	public void testElementEncryption() throws Exception {

		// generate some keys for encryption
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		KeyPair kp = kpg.generateKeyPair();
		PublicKey publicKey = kp.getPublic();
		PrivateKey privateKey = kp.getPrivate();
		
		// data before encryption
		Element attributeStatement = generateAttributeStatement();
		String originalData = ModelUtil.dom2String(attributeStatement.getOwnerDocument());
		
		// encrypt the two attributes
		Element resourceIdElement = getElementByExpression(attributeStatement.getOwnerDocument(), "//saml:Attribute[@Name=\"" + AttributeNameConstants.RESOURCE_ID + "\"]", 1);
		SAMLHEncrypter.encryptContent(resourceIdElement, publicKey, true);
		SAMLHEncrypter.encrypt(new QName("urn:hl7-org:v3", "Role"), attributeStatement.getOwnerDocument(), publicKey, true);

		// string value after encryption
		String encryptedData = ModelUtil.dom2String(attributeStatement.getOwnerDocument());
		
		// decrypt ResourceId attribute
		Element encryptedResourceIdElement = getElementByExpression(attributeStatement.getOwnerDocument(), "//saml:Attribute[@Name=\"" + SAMLHEncrypter.ENCRYPTED_PREFIX + AttributeNameConstants.RESOURCE_ID + "\"]", 2);
		SAMLHEncrypter.decrypt(encryptedResourceIdElement, privateKey, true);

		// decrypt Role attribute
		Element encryptedRoleElement = getElementByExpression(attributeStatement.getOwnerDocument(), "//saml:Attribute[@Name=\"" + SAMLHEncrypter.ENCRYPTED_PREFIX + AttributeNameConstants.ROLE + "\"]", 2);
		SAMLHEncrypter.decrypt(encryptedRoleElement, privateKey, true);

		// string value after decryption
		String decryptedData = ModelUtil.dom2String(attributeStatement.getOwnerDocument());

		// manual inspection for the curious
		System.out.println("original  : " + originalData);
		System.out.println("encrypted : " + encryptedData);
		System.out.println("decrypted : " + decryptedData);
		
		assertEquals(originalData, decryptedData);
	}
	
	private Element generateAttributeStatement() throws Exception  {
		Document doc = ModelUtil.createDocument();
		Element attributeStatementElement = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeStatement");
		doc.appendChild(attributeStatementElement);
		
		Element attribute = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Attribute");
		attribute.setAttributeNS(null, "Name", AttributeNameConstants.ROLE);
		attribute.setAttributeNS(null, "NameFormat", "urn:oasis:names:tc:SAML:2.0:attrname-format:basic");
		Element attributeValue = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeValue");
		Element claim = generateElementAttribute();
		Node importedNode = doc.importNode(claim, true);
		attributeValue.appendChild(importedNode);
		attribute.appendChild(attributeValue);
		attributeStatementElement.appendChild(attribute);

		Element attribute2 = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:Attribute");
		attribute2.setAttributeNS(null, "Name", AttributeNameConstants.RESOURCE_ID);
		attribute2.setAttributeNS(null, "NameFormat", "urn:oasis:names:tc:SAML:2.0:attrname-format:basic");
		Element attributeValue2 = doc.createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "saml:AttributeValue");
        Text textNode = doc.createTextNode(generateStringAttribute());
        attributeValue2.appendChild(textNode);
		attribute2.appendChild(attributeValue2);
		attributeStatementElement.appendChild(attribute2);

		return attributeStatementElement;
	}
	
	private Element generateElementAttribute() throws Exception {
		Role role = new Role();
		role.setCode("7170");
		role.setCodeSystem("1.2.208.176.1.3");
		role.setCodeSystemName("Autorisationsregister");
		role.setDisplayName("Læge");
		role.setXsiType("CE");

		return role.generateElement(Validate.YES);
	}
	
	private String generateStringAttribute() throws Exception {
		ResourceId resourceId = new ResourceId();
		resourceId.setOid("1.2.208.176.1.2");
		resourceId.setPatientId("2512484916");
		
		return resourceId.generate(Validate.YES);
	}
	
	// this is not a very pretty methods, but it works for testing purposes - for production
	// it is recommended to build a better version :)
	private Element getElementByExpression(Document doc, String expression, int childLevel) throws Exception {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
	    NamespaceContext context = new NamespaceContextMap(
	    	"saml", "urn:oasis:names:tc:SAML:2.0:assertion"
	    );
		xpath.setNamespaceContext(context);
		XPathExpression expr = xpath.compile(expression);
		NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		if (nl.getLength() == 0) {
			throw new Exception("Element not found");
		}
		
		Node node = nl.item(0);
		if (!(node instanceof Element)) {
			throw new Exception("Not an element");
		}
		
		Element element = (Element) node;
		while (childLevel > 0) {
			childLevel--;
			
			if (element.getFirstChild() == null || !(element.getFirstChild() instanceof Element)) {
				throw new Exception("Child is not an element");
			}
			
			element = (Element) element.getFirstChild();
		}
		
		return element;
	}
}
