package dk.sds.samlh.model.oiobpp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class PrivilegeListUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KY:EducationCode:1234\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KL:EducationCode:5678\">\n" +
				"      <Privilege>urn:dk:some_domain:myPrivilege1C</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1D</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 2);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeAuthorizationCode().equals("341KY"));
		assertTrue(firstGroup.getScopeEducationCode().equals("1234"));
	}

	@Test
	public void testParseMethodOnBase64Value() throws JAXBException, ValidationException {
		String xmlString = "PG5zMjpQcml2aWxlZ2VMaXN0IHhtbG5zOm5zMj0iaHR0cDovL2l0c3QuZGsvb2lvc2FtbC9iYXNpY19wcml2aWxlZ2VfcHJvZmlsZSI+PFByaXZpbGVnZUdyb3VwIFNjb3BlPSJ1cm46ZGs6aGVhbHRoY2FyZTpzYW1sOnVzZXJBdXRob3JpemF0aW9uOkF1dGhvcml6YXRpb25Db2RlOjM0MUtZOkVkdWNhdGlvbkNvZGU6MTIzNCI+PFByaXZpbGVnZT51cm46ZGs6c29tZV9kb21haW46bXlQcml2aWxlZ2UxQTwvUHJpdmlsZWdlPjxQcml2aWxlZ2U+dXJuOmRrOnNvbWVfZG9tYWluOm15UHJpdmlsZWdlMUI8L1ByaXZpbGVnZT48L1ByaXZpbGVnZUdyb3VwPjxQcml2aWxlZ2VHcm91cCBTY29wZT0idXJuOmRrOmhlYWx0aGNhcmU6c2FtbDp1c2VyQXV0aG9yaXphdGlvbjpBdXRob3JpemF0aW9uQ29kZTozNDFLTDpFZHVjYXRpb25Db2RlOjU2NzgiPjxQcml2aWxlZ2U+dXJuOmRrOnNvbWVfZG9tYWluOm15UHJpdmlsZWdlMUM8L1ByaXZpbGVnZT48UHJpdmlsZWdlPnVybjpkazpzb21lX2RvbWFpbjpteVByaXZpbGVnZTFEPC9Qcml2aWxlZ2U+PC9Qcml2aWxlZ2VHcm91cD48L25zMjpQcml2aWxlZ2VMaXN0Pg==";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);
		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size()==2);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeAuthorizationCode().equals("341KY"));
		assertTrue(firstGroup.getScopeEducationCode().equals("1234"));
	}
	
	@Test
	public void testParsePharmacist() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:RegisteredPharmacistCPR:0101011118\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" +
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 1);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeValue().equals("0101011118"));
	}
	
	@Test
	public void testParseCprIdentifier() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:gov:saml:cprNumberIdentifier:0101011118\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" +
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 1);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeValue().equals("0101011118"));
	}
	
	@Test
	public void testParseCvrIdentifier() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:gov:saml:cvrNumberIdentifier:12345678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" +
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 1);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeValue().equals("12345678"));
	}
	
	@Test
	public void testParseSeIdentifier() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:gov:saml:seNumberIdentifier:12345678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" +
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 1);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeValue().equals("12345678"));
	}
	
	@Test
	public void testParsePNumberIdentifier() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:gov:saml:productionUnitIdentifier:1234567890\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" +
				"</bpp:PrivilegeList>";
		
		PrivilegeList pl = PrivilegeList.parse(xmlString, Validate.YES);

		assertNotNull(pl);
		assertNotNull(pl.getPrivilegeGroups());
		assertTrue(pl.getPrivilegeGroups().size() == 1);

		PrivilegeGroup firstGroup = pl.getPrivilegeGroups().get(0);
		assertTrue(firstGroup.getScopeValue().equals("1234567890"));
	}
	
	@Test
	public void testParseAndGenerateMethod() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KY:EducationCode:1234\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KL:EducationCode:5678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1C</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1D</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"</bpp:PrivilegeList>";
		
		PrivilegeList firstObject = PrivilegeList.parse(xmlString, Validate.YES);
		PrivilegeList secondObject = PrivilegeList.parse(firstObject.generate(Validate.YES), Validate.YES);
		assertTrue(secondObject.getPrivilegeGroups().size() == firstObject.getPrivilegeGroups().size());

		for (int i = 0; i < firstObject.getPrivilegeGroups().size(); i++) {
			PrivilegeGroup firstGroup = firstObject.getPrivilegeGroups().get(i);
			PrivilegeGroup secondGroup = secondObject.getPrivilegeGroups().get(i);
			assertTrue(firstGroup.getScopeEducationCode().equals(secondGroup.getScopeEducationCode()));
			assertTrue(firstGroup.getScopeAuthorizationCode().equals(secondGroup.getScopeAuthorizationCode()));
			assertTrue(firstGroup.getPrivileges().size() == secondGroup.getPrivileges().size());
			
			for (int j = 0; j < firstGroup.getPrivileges().size(); j++) {
				String firstPrivilege = firstGroup.getPrivileges().get(j);
				String secondPrivilege = secondGroup.getPrivileges().get(j);
				assertEquals(firstPrivilege, secondPrivilege);
			}
		}
	}
	
	@Test
	public void testMissingPrivileges() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KY:EducationCode:1234\">\n" + 
				"   </PrivilegeGroup>\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KL:EducationCode:5678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1C</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1D</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"</bpp:PrivilegeList>";
		try {
			PrivilegeList.parse(xmlString, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Found PrivilegeGroup element that doesn't contain any Privilege elements.", e.getMessage());
		}
	}
	
	@Test
	public void testInvalidScopeValue() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:gov:saml:cvrNumberIdentifier:12345678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1A</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"</bpp:PrivilegeList>";
		try {
			PrivilegeList.parse(xmlString, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Invalid scope: urn:dk:gov:saml:cvrNumberIdentifier:12345678", e.getMessage());
		}
	}

	@Test
	public void testInvalidPrivilege() throws JAXBException, ValidationException {
		String xmlString =
				"<bpp:PrivilegeList xmlns:bpp=\"http://itst.dk/oiosaml/basic_privilege_profile\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KY:EducationCode:1234\">\n" + 
				"      <Privilege>http://www.google.com</Privilege>\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1B</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"   <PrivilegeGroup Scope=\"urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:341KL:EducationCode:5678\">\n" + 
				"      <Privilege>urn:dk:some_domain:myPrivilege1C</Privilege>\n" + 
				"      <Privilege>sometext....</Privilege>\n" + 
				"   </PrivilegeGroup>\n" + 
				"</bpp:PrivilegeList>";
		
		try {
			PrivilegeList.parse(xmlString, Validate.YES);
		}
		catch (ValidationException e) {
			assertTrue(e.getMessage().contains("Found invalid Privilege:"));
		}
	}
}
