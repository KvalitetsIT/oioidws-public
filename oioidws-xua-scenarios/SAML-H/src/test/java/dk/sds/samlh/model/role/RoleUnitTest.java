package dk.sds.samlh.model.role;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.role.Role;

public class RoleUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		String xmlString2 = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";

		Role role = Role.parse(xmlString2, Validate.YES);
		assertEquals("7170", role.getCode());
		assertEquals("1.2.208.176.1.3", role.getCodeSystem());
		assertEquals("Autorisationsregister", role.getCodeSystemName());
		assertEquals("Læge", role.getDisplayName());
		assertEquals("CE", role.getXsiType());
		
		String xmlStringWithoutOptionalAttributes = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\"/>";
		Role role2 = Role.parse(xmlStringWithoutOptionalAttributes, Validate.YES);
		assertEquals("7170", role2.getCode());
		assertEquals("1.2.208.176.1.3", role2.getCodeSystem());
		assertEquals(null, role2.getCodeSystemName());
		assertEquals(null, role2.getDisplayName());
		assertEquals("CE", role2.getXsiType());
	}
	
	@Test(expected=JAXBException.class)
	public void testParseMethodFailsMissingNS() throws JAXBException, ValidationException {
		String xmlString = "<Role xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		Role.parse(xmlString, Validate.YES);
	}

	@Test(expected=JAXBException.class)
	public void testParseMethodFailsMissingXSINamespace() throws JAXBException, ValidationException {
		String xmlString = "<Role xmlns=\"urn:hl7-org:v3\" xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		Role.parse(xmlString, Validate.YES);
	}

	@Test
	public void testGenerateMethod() throws JAXBException, ValidationException {
		String xmlString = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		Role role = Role.parse(xmlString, Validate.YES);
		Role role2 = Role.parse(role.generate(Validate.NO), Validate.YES);
		
		assertEquals("7170", role2.getCode());
		assertEquals("1.2.208.176.1.3", role2.getCodeSystem());
		assertEquals("Autorisationsregister", role2.getCodeSystemName());
		assertEquals("Læge", role2.getDisplayName());
		assertEquals("CE", role2.getXsiType());
	}
	
	@Test
	public void testValidateMethod() throws JAXBException, ValidationException {
		String xmlStringWithoutCode = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";

		try {
			Role.parse(xmlStringWithoutCode, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Code attribute is mandatory.", e.getMessage());
		}
		
		String xmlStringWithoutCodeSystem = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"7170\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		try {
			Role.parse(xmlStringWithoutCodeSystem, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("CodeSystem attribute is mandatory.", e.getMessage());
		}
		
		String xmlStringWrongCodeSystem = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"7170\" codeSystem=\"1.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		try {
			Role.parse(xmlStringWrongCodeSystem, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("CodeSystem must be set to [1.2.208.176.1.3]", e.getMessage());
		}
		
		String xmlStringXSIType = "<Role xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CZE\" code=\"7170\" codeSystem=\"1.2.208.176.1.3\" codeSystemName=\"Autorisationsregister\" displayName=\"Læge\"/>";
		try {
			Role.parse(xmlStringXSIType, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Type must be set to CE.", e.getMessage());
		}
	}
}
