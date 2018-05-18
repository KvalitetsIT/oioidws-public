package frontend.util;

import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.UUID;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.AuthnContextBuilder;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.security.BasicSecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.signature.impl.SignatureBuilder;
import org.opensaml.xml.signature.impl.X509CertificateBuilder;
import org.opensaml.xml.signature.impl.X509DataBuilder;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

import dk.itst.oiosaml.sp.util.AttributeUtil;
import frontend.ui.UI;

public class TokenBuilder {
	
	public TokenBuilder() throws Exception {
		DefaultBootstrap.bootstrap();
		
		BasicSecurityConfiguration config = (BasicSecurityConfiguration) Configuration.getGlobalSecurityConfiguration();
		config.setSignatureReferenceDigestMethod(SignatureConstants.ALGO_ID_DIGEST_SHA256);
	}

	public String getToken(UI ui, KeyStore ks, String alias, String password) {
		try {
			Issuer issuer = new IssuerBuilder().buildObject();
			issuer.setValue("CN=Henrik Jensen"); // self-issued

			NameID nameID = new NameIDBuilder().buildObject();
			nameID.setValue("CN=Henrik Jensen");
			nameID.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

			SubjectConfirmation confirmation = new SubjectConfirmationBuilder().buildObject();
			confirmation.setMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");
			
			Subject subject = new SubjectBuilder().buildObject();
			subject.setNameID(nameID);
			subject.getSubjectConfirmations().add(confirmation);

			Audience audience = new AudienceBuilder().buildObject();
			audience.setAudienceURI("http://sts.sundhedsdatastyrelsen.dk/");
			
			AudienceRestriction audienceRestriction = new AudienceRestrictionBuilder().buildObject();
			audienceRestriction.getAudiences().add(audience);

			Conditions conditions = new ConditionsBuilder().buildObject();
			conditions.setNotBefore(new DateTime().minus(5 * 1000));
			conditions.setNotOnOrAfter(new DateTime().plus(60 * 60 * 1000));
			conditions.getAudienceRestrictions().add(audienceRestriction);

			Attribute attribute = new AttributeBuilder().buildObject();
			attribute.setName("dk:gov:saml:attribute:AssuranceLevel");
			attribute.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:basic");
			attribute.getAttributeValues().add(AttributeUtil.createAttributeValue("2"));

			AttributeStatement attributeStatement = new AttributeStatementBuilder().buildObject();
			attributeStatement.getAttributes().add(attribute);
			
			AuthnContextClassRef authnContextClassRef = new AuthnContextClassRefBuilder().buildObject();
			authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:X509");
			
			AuthnContext authnContext = new AuthnContextBuilder().buildObject();
			authnContext.setAuthnContextClassRef(authnContextClassRef);
			
			AuthnStatement authnStatement = new AuthnStatementBuilder().buildObject();
			authnStatement.setAuthnInstant(new DateTime());
			authnStatement.setAuthnContext(authnContext);

			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
			Credential signingCredential = SecurityHelper.getSimpleCredential(certificate, privateKey);
			
	        org.opensaml.xml.signature.X509Certificate cert = new X509CertificateBuilder().buildObject();
	        cert.setValue(Base64.getEncoder().encodeToString(certificate.getEncoded()));
	        X509Data x509Data = new X509DataBuilder().buildObject();
			x509Data.getX509Certificates().add(cert);

	        KeyInfo keyInfo = new KeyInfoBuilder().buildObject();
			keyInfo.getX509Datas().add(x509Data);

	        Signature signature = new SignatureBuilder().buildObject();
			signature.setSigningCredential(signingCredential);
	        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
			signature.setKeyInfo(keyInfo);

			Assertion assertion = new AssertionBuilder().buildObject();			
			assertion.setID(UUID.randomUUID().toString());
			assertion.setIssueInstant(new DateTime());
			assertion.setIssuer(issuer);
			assertion.setSubject(subject);
			assertion.setConditions(conditions);
			assertion.getAttributeStatements().add(attributeStatement);
			assertion.getAuthnStatements().add(authnStatement);
			assertion.setSignature(signature);
	        
	        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(assertion);
	        marshaller.marshall(assertion);

	        Signer.signObject(signature);

			logToken(ui, assertion);
	
			Element plaintextElement = marshaller.marshall(assertion);
	        String assertionString = XMLHelper.nodeToString(plaintextElement);
	        return Base64.getEncoder().encodeToString(assertionString.getBytes(Charset.forName("UTF-8")));
		}
		catch (Exception ex) {
			ui.writeLog("Failed to generate self-signed token", ex);
		}
		
		return null;
	}
	
	private void logToken(UI ui, Assertion assertion) throws Exception {
        AssertionMarshaller marshaller = new AssertionMarshaller();
        Element plaintextElement = marshaller.marshall(assertion);
        String assertionString = XMLHelper.prettyPrintXML(plaintextElement);

        ui.writeLog("**** self-signed assertion ****");
        ui.writeLog(assertionString);
        ui.writeLog("**** self-signed assertion ****");
	}
}
