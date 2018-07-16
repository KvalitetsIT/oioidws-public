package dk.sds.samlh.model.resourceid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class ResourceIdUnitTest {

	@Test
	public void testParse() throws ValidationException {
		String resourceIdString = "2512484916^^^&amp;1.2.208.176.1.2&amp;ISO";

		ResourceId.parse(resourceIdString, Validate.YES);
	}

	@Test(expected = ValidationException.class)
	public void testParseFail() throws ValidationException {
		String resourceIdString = "7fdg78fdsg8dfg78fg8ds@D@d2##87fd37f";
		ResourceId.parse(resourceIdString, Validate.YES);
	}

	@Test(expected = ValidationException.class)
	public void testParseFailMissingPatientIdElement() throws ValidationException {
		String resourceIdString = "^^^&1.2.208.176.1.2&ISO";
		ResourceId.parse(resourceIdString, Validate.YES);
	}

	@Test(expected = ValidationException.class)
	public void testParseFailMissingOIDElement() throws ValidationException {
		String resourceIdString = "2512484916^^^&&ISO";
		ResourceId.parse(resourceIdString, Validate.YES);
	}

	@Test
	public void testGenerate() throws ValidationException {
		String resourceIdString = "2512484916^^^&1.2.208.176.1.2&ISO";

		ResourceId resource = ResourceId.parse(resourceIdString, Validate.YES);
		assertEquals(resourceIdString, resource.generate(Validate.NO));
	}
}
