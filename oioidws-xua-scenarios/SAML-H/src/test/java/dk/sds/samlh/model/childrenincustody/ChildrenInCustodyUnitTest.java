package dk.sds.samlh.model.childrenincustody;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class ChildrenInCustodyUnitTest {

	@Test
	public void testParseWorks() throws ValidationException {
		String stringCPR = "urn:dk:gov:saml:cprNumberIdentifier:1105771345";
		String stringECPR = "urn:dk:healthcare:saml:ECprNumberIdentifier:151298049F";

		ChildrenInCustody cic = ChildrenInCustody.parse(stringCPR, Validate.YES);
		assertEquals("urn:dk:gov:saml:cprNumberIdentifier", cic.getCprType().getValue());
		assertEquals("1105771345", cic.getValue());

		ChildrenInCustody cic2 = ChildrenInCustody.parse(stringECPR, Validate.YES);
		assertEquals("urn:dk:healthcare:saml:ECprNumberIdentifier", cic2.getCprType().getValue());
		assertEquals("151298049F", cic2.getValue());
	}

	@Test(expected = ValidationException.class)
	public void testInvalidDate1() throws ValidationException {
		String stringCPR = "urn:dk:gov:saml:cprNumberIdentifier:aabbcc1345";

		ChildrenInCustody.parse(stringCPR, Validate.YES);
	}
	
	@Test(expected = ValidationException.class)
	public void testInvalidDate2() throws ValidationException {
		String stringECPR = "urn:dk:healthcare:saml:ECprNumberIdentifier:153298049F";

		ChildrenInCustody.parse(stringECPR, Validate.YES);
	}

	@Test(expected = ValidationException.class)
	public void testInvalidInput() throws ValidationException {
		String stringCPR = "this is obviously not the right format";
		ChildrenInCustody.parse(stringCPR, Validate.YES);
	}

	@Test
	public void testGenerateMethod() throws ValidationException {
		String stringCPR = "urn:dk:gov:saml:cprNumberIdentifier:1105771345";
		String stringECPR = "urn:dk:healthcare:saml:ECprNumberIdentifier:151298049F";

		ChildrenInCustody cic = ChildrenInCustody.parse(stringCPR, Validate.YES);
		assertEquals(stringCPR, cic.generate(Validate.NO));

		ChildrenInCustody cic2 = ChildrenInCustody.parse(stringECPR, Validate.YES);
		assertEquals(stringECPR, cic2.generate(Validate.NO));
	}
}
