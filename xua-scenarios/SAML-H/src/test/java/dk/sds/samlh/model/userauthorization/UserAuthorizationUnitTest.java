package dk.sds.samlh.model.userauthorization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class UserAuthorizationUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		String xmlString = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>341KY</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>7AD6T</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>5433</uap:EducationCode>\n" + 
				"        <uap:EducationType>Tandlæge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";
		
		UserAuthorizationList userAuth = UserAuthorizationList.parse(xmlString, Validate.YES);
		UserAuthorization userAuth0 = userAuth.getUserAuthorizations().get(0);
		UserAuthorization userAuth1 = userAuth.getUserAuthorizations().get(1);
		assertEquals("341KY", userAuth0.getAuthorizationCode());
		assertEquals("7170", userAuth0.getEducationCode());
		assertEquals("Læge", userAuth0.getEducationType());
		assertEquals("7AD6T", userAuth1.getAuthorizationCode());
		assertEquals("5433", userAuth1.getEducationCode());
		assertEquals("Tandlæge", userAuth1.getEducationType());
	}
	
	@Test
	public void testGenerateMethod() throws JAXBException, ValidationException {
		String xmlString = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>341KY</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>7AD6T</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>5433</uap:EducationCode>\n" + 
				"        <uap:EducationType>Tandlæge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";

		UserAuthorizationList userAuth = UserAuthorizationList.parse(xmlString, Validate.YES);

		String serialized = userAuth.generate(Validate.NO);
		UserAuthorizationList userAuth2 = UserAuthorizationList.parse(serialized, Validate.YES);

		assertEquals(userAuth.getUserAuthorizations().size(), userAuth2.getUserAuthorizations().size());
	}
	
	@Test
	public void testValidateAuthorizationUnique() throws JAXBException, ValidationException {
		String xmlStringValid = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>341KY</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>7AD6T</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>5433</uap:EducationCode>\n" + 
				"        <uap:EducationType>Tandlæge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";

		UserAuthorizationList.parse(xmlStringValid, Validate.YES);
		
		String xmlStringInvalid= 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>AAAA1</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>AAAA1</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";
		
		try {
			UserAuthorizationList.parse(xmlStringInvalid, Validate.YES);
			
			assertTrue("Should have thrown an exception", false);
		}
		catch (ValidationException e) {
			assertTrue(e.getMessage().contains("Non-unique AuthorizationCodes found"));
		}
	}
	
	@Test
	public void testValidateAuthorizationCode() throws JAXBException, ValidationException {
		String xmlStringInvalid= 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>x</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";
		
		try {
			UserAuthorizationList.parse(xmlStringInvalid, Validate.YES);
			
			assertTrue("Should have thrown an exception", false);
		}
		catch (ValidationException e) {
			assertEquals("AuthorizationCode must consist of a 5-digit code, which is a mixture of letters and numbers.", e.getMessage());
		}
	}
	
	@Test
	public void testValidateEducationCode() throws JAXBException, ValidationException {
		String xmlStringInvalid= 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>341KY</uap:AuthorizationCode>\n" + 
				"        <uap:EducationType>Læge</uap:EducationType>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";
		
		try {
			UserAuthorizationList.parse(xmlStringInvalid, Validate.YES);
			
			assertTrue("Should have thrown an exception", false);
		}
		catch (ValidationException e) {
			assertTrue(e.getMessage().equals("EducationCode must not be null"));
		}
	}
	
	@Test
	public void testValidateEducationType() throws JAXBException, ValidationException {
		String xmlStringInvalid= 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<uap:UserAuthorizationList xmlns:uap=\"urn:dk:healthcare:saml:user_authorization_profile:1.0\">\n" + 
				"    <uap:UserAuthorization>\n" + 
				"        <uap:AuthorizationCode>341KY</uap:AuthorizationCode>\n" + 
				"        <uap:EducationCode>7170</uap:EducationCode>\n" + 
				"    </uap:UserAuthorization>\n" + 
				"</uap:UserAuthorizationList>";
		
		try {
			UserAuthorizationList.parse(xmlStringInvalid, Validate.YES);
			
			assertTrue("Should have thrown an exception", false);
		}
		catch (ValidationException e) {
			assertTrue(e.getMessage().equals("EducationType must not be null"));
		}
	}
}
