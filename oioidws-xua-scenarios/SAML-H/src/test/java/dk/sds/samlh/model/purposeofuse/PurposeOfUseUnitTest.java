package dk.sds.samlh.model.purposeofuse;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.purposeofuse.PurposeOfUse;

public class PurposeOfUseUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		String xmlString = "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"TREATMENT\" codeSystem=\"urn:oasis:names:tc:xspa:1.0\"/>";

		PurposeOfUse test = PurposeOfUse.parse(xmlString, Validate.YES);

		assertEquals("TREATMENT", test.getCode().toString());
		assertEquals("urn:oasis:names:tc:xspa:1.0", test.getCodeSystem());
		assertEquals(null, test.getCodeSystemName());
	}
	
	@Test(expected=JAXBException.class)
	public void testParseFail() throws JAXBException, ValidationException {
		String xmlString = "<Purpose xmlns=\"urn:hl7-org:v3\" code=\"TREATMENT\" codeSystem=\"urn:oasis:names:tc:xspa:1.0\"/>";
		PurposeOfUse.parse(xmlString, Validate.YES);
	}

	@Test
	public void testGenerate() throws JAXBException, ValidationException {
		String xmlString = "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"TREATMENT\" codeSystem=\"urn:oasis:names:tc:xspa:1.0\"/>";

		PurposeOfUse test = PurposeOfUse.parse(xmlString, Validate.YES);
		PurposeOfUse gen = PurposeOfUse.parse(test.generate(Validate.NO), Validate.YES);
		
		assertEquals(gen.getCode(), test.getCode());
		assertEquals(gen.getCodeSystem(), test.getCodeSystem());
		assertEquals(gen.getCodeSystemName(), test.getCodeSystemName());
		assertEquals(gen.getDisplayName(), test.getDisplayName());
		assertEquals(gen.getXsiType(), test.getXsiType());
	}
	
	@Test
	public void testValidateMandatory() throws JAXBException, ValidationException {
		String xmlString = "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" codeSystem=\"urn:oasis:names:tc:xspa:1.0\"/>";

		try {
			PurposeOfUse.parse(xmlString, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Code attribute is mandatory.", e.getMessage());
		}
		
		String xmlString2 = "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"CE\" code=\"TREATMENT\" />";
		try {
			PurposeOfUse.parse(xmlString2, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("CodeSystem attribute is mandatory.", e.getMessage());
		}
	}
	
	@Test
	public void testValidateXsiType() throws JAXBException, ValidationException {
		String xmlString = "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"BE\" code=\"TREATMENT\" codeSystem=\"urn:oasis:names:tc:xspa:1.0\"/>";

		try {
			PurposeOfUse.parse(xmlString, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Type must be set to CE.", e.getMessage());
		}
	}
}
