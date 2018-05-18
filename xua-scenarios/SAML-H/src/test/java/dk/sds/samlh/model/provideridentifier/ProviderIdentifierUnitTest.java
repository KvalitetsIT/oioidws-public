package dk.sds.samlh.model.provideridentifier;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.xml.sax.SAXException;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.provideridentifier.ProviderIdentifier;

public class ProviderIdentifierUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		String xmlObject = "<id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"II\" root=\"1.2.208.176.1.1\" extension=\"8041000016000^Sydvestjysk Sygehus\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\"/>";
		ProviderIdentifier pi = ProviderIdentifier.parse(xmlObject, Validate.YES);
		assertEquals("Sundhedsvæsenets Organisationsregister (SOR)", pi.getAssigningAuthorityName());
		assertEquals(null, pi.getDisplayable());
		assertEquals("8041000016000^Sydvestjysk Sygehus", pi.getExtension());
		assertEquals("1.2.208.176.1.1", pi.getRoot());
		assertEquals("II", pi.getXsiType());
	}

	@Test
	public void testGenerateMethod() throws JAXBException, ValidationException, SAXException, IOException {
		String xmlObject = "<id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"II\" root=\"1.2.208.176.1.1\" extension=\"8041000016000^Sydvestjysk Sygehus\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\"/>";
		ProviderIdentifier org = ProviderIdentifier.parse(xmlObject, Validate.YES);
		ProviderIdentifier pi = ProviderIdentifier.parse(org.generate(Validate.NO), Validate.YES);
		assertEquals(org.getAssigningAuthorityName(), pi.getAssigningAuthorityName());
		assertEquals(org.getDisplayable(), pi.getDisplayable());
		assertEquals(org.getExtension(), pi.getExtension());
		assertEquals(org.getRoot(), pi.getRoot());
		assertEquals(org.getXsiType(), pi.getXsiType());
	}

	@Test
	public void testValidateXSI() throws JAXBException, ValidationException {
		String xmlObject = "<id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"SX\" root=\"1.2.208.176.1.1\" extension=\"8041000016000^Sydvestjysk Sygehus\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\"/>";
		try {
			ProviderIdentifier.parse(xmlObject, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Type must be set to II.", e.getMessage());
		}
	}

	@Test
	public void testValidateMandatoryElementMissing() throws JAXBException, ValidationException {
		String xmlObject = "<id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"II\" extension=\"8041000016000^Sydvestjysk Sygehus\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\"/>";
		try {
			ProviderIdentifier.parse(xmlObject, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Root attribute is mandatory.", e.getMessage());
		}

		String xmlObject2 = "<id xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"II\" root=\"1.2.208.176.1.1\" assigningAuthorityName=\"Sundhedsvæsenets Organisationsregister (SOR)\"/>";
		try {
			ProviderIdentifier.parse(xmlObject2, Validate.YES);
		}
		catch (ValidationException e) {
			assertEquals("Extension attribute is mandatory.", e.getMessage());
		}
	}
}
