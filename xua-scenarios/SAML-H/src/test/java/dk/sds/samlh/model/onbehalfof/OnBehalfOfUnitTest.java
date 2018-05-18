package dk.sds.samlh.model.onbehalfof;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.onbehalfof.OnBehalfOf;
import dk.sds.samlh.model.onbehalfof.OnBehalfOf.Legislation;

public class OnBehalfOfUnitTest {

	@Test
	public void testParseMethod() throws JAXBException, ValidationException {
		// For user authorizations the legislations

		String oboString = "urn:dk:healthcare:saml:actThroughDelegationByAuthorizedHealthcareProfessional:userAuthorization:AuthorizationCode:141KY:EducationCode:1501";
		OnBehalfOf obo = OnBehalfOf.parse(oboString, Validate.YES);
		assertEquals("141KY", obo.getAuthorizationCode());
		assertEquals("1501", obo.getEducationCode());
		assertEquals(Legislation.actThroughDelegationByAuthorizedHealthcareProfessional, obo.getLegislation());
		assertEquals(null, obo.getCpr());
		assertEquals(null, obo.getEcpr());
		assertEquals(null, obo.getPseudonym());
		assertEquals(oboString, obo.generate(Validate.NO));
		
		String oboString0 = "urn:dk:healthcare:saml:actAsTechnicalAssistantForAuthorizedHealthcareProfessional:userAuthorization:AuthorizationCode:141KY:EducationCode:1501";
		OnBehalfOf obo0 = OnBehalfOf.parse(oboString0, Validate.YES);
		assertEquals("141KY", obo0.getAuthorizationCode());
		assertEquals("1501", obo0.getEducationCode());
		assertEquals(Legislation.actAsTechnicalAssistantForAuthorizedHealthcareProfessional, obo0.getLegislation());
		assertEquals(null, obo0.getCpr());
		assertEquals(null, obo0.getEcpr());
		assertEquals(null, obo0.getPseudonym());
		assertEquals(oboString0, obo0.generate(Validate.NO));
		
		//END
		
		//Test for actThroughProcurationBy
		
		String obo1String = "urn:dk:healthcare:saml:actThroughProcurationBy:cprNumberIdentifier:0101017777";
		OnBehalfOf obo1 = OnBehalfOf.parse(obo1String, Validate.YES);
		assertEquals(null, obo1.getAuthorizationCode());
		assertEquals(null, obo1.getEducationCode());
		assertEquals(Legislation.actThroughProcurationBy, obo1.getLegislation());
		assertEquals("0101017777", obo1.getCpr());
		assertEquals(null, obo1.getEcpr());
		assertEquals(null, obo1.getPseudonym());
		assertEquals(obo1String, obo1.generate(Validate.NO));
		
		String obo2String = "urn:dk:healthcare:saml:attribute:actThroughProcurationBy:ECprNumberIdentifier:010101777E";
		OnBehalfOf obo2 = OnBehalfOf.parse(obo2String, Validate.YES);
		assertEquals(null, obo2.getAuthorizationCode());
		assertEquals(null, obo2.getEducationCode());
		assertEquals(Legislation.actThroughProcurationBy, obo2.getLegislation());
		assertEquals(null, obo2.getCpr());
		assertEquals("010101777E", obo2.getEcpr());
		assertEquals(null, obo2.getPseudonym());
		assertEquals(obo2String, obo2.generate(Validate.NO));
		
		String obo3String = "urn:dk:healthcare:saml:attribute:actThroughProcurationBy:PersistentPseudonym:Test";
		OnBehalfOf obo3 = OnBehalfOf.parse(obo3String, Validate.YES);
		assertEquals(null, obo3.getAuthorizationCode());
		assertEquals(null, obo3.getEducationCode());
		assertEquals(Legislation.actThroughProcurationBy, obo3.getLegislation());
		assertEquals(null, obo3.getCpr());
		assertEquals(null, obo3.getEcpr());
		assertEquals("Test", obo3.getPseudonym());
		assertEquals(obo3String, obo3.generate(Validate.NO));
		//END
		
		//Test for actThroughCustodyOver
		
		String obo4String = "urn:dk:healthcare:saml:actThroughCustodyOver:cprNumberIdentifier:0101017777";
		OnBehalfOf obo4 = OnBehalfOf.parse(obo4String, Validate.YES);
		assertEquals(null, obo4.getAuthorizationCode());
		assertEquals(null, obo4.getEducationCode());
		assertEquals(Legislation.actThroughCustodyOver, obo4.getLegislation());
		assertEquals("0101017777", obo4.getCpr());
		assertEquals(null, obo4.getEcpr());
		assertEquals(null, obo4.getPseudonym());
		assertEquals(obo4String, obo4.generate(Validate.NO));
		
		String obo5String = "urn:dk:healthcare:saml:attribute:actThroughCustodyOver:ECprNumberIdentifier:010101777E";
		OnBehalfOf obo5 = OnBehalfOf.parse(obo5String, Validate.YES);
		assertEquals(null, obo5.getAuthorizationCode());
		assertEquals(null, obo5.getEducationCode());
		assertEquals(Legislation.actThroughCustodyOver, obo5.getLegislation());
		assertEquals(null, obo5.getCpr());
		assertEquals("010101777E", obo5.getEcpr());
		assertEquals(null, obo5.getPseudonym());
		assertEquals(obo5String, obo5.generate(Validate.NO));
		
		String obo6String = "urn:dk:healthcare:saml:attribute:actThroughCustodyOver:PersistentPseudonym:Test";
		OnBehalfOf obo6 = OnBehalfOf.parse(obo6String, Validate.YES);
		assertEquals(null, obo6.getAuthorizationCode());
		assertEquals(null, obo6.getEducationCode());
		assertEquals(Legislation.actThroughCustodyOver, obo6.getLegislation());
		assertEquals(null, obo6.getCpr());
		assertEquals(null, obo6.getEcpr());
		assertEquals("Test", obo6.getPseudonym());
		assertEquals(obo6String, obo6.generate(Validate.NO));
		//END
		
		//Test for actThroughDelegationByRegisteredPharmacist
		
		String obo7String = "urn:dk:healthcare:saml:actThroughDelegationByRegisteredPharmacist:cprNumberIdentifier:0101017777";
		OnBehalfOf obo7 = OnBehalfOf.parse(obo7String, Validate.YES);
		assertEquals(null, obo7.getAuthorizationCode());
		assertEquals(null, obo7.getEducationCode());
		assertEquals(Legislation.actThroughDelegationByRegisteredPharmacist, obo7.getLegislation());
		assertEquals("0101017777", obo7.getCpr());
		assertEquals(null, obo7.getEcpr());
		assertEquals(null, obo7.getPseudonym());
		assertEquals(obo7String, obo7.generate(Validate.NO));
		
		String obo8String = "urn:dk:healthcare:saml:attribute:actThroughDelegationByRegisteredPharmacist:ECprNumberIdentifier:010101777E";
		OnBehalfOf obo8 = OnBehalfOf.parse(obo8String, Validate.YES);
		assertEquals(null, obo8.getAuthorizationCode());
		assertEquals(null, obo8.getEducationCode());
		assertEquals(Legislation.actThroughDelegationByRegisteredPharmacist, obo8.getLegislation());
		assertEquals(null, obo8.getCpr());
		assertEquals("010101777E", obo8.getEcpr());
		assertEquals(null, obo8.getPseudonym());
		assertEquals(obo8String, obo8.generate(Validate.NO));
		
		String obo9String = "urn:dk:healthcare:saml:attribute:actThroughDelegationByRegisteredPharmacist:PersistentPseudonym:Test";
		OnBehalfOf obo9 = OnBehalfOf.parse(obo9String, Validate.YES);
		assertEquals(null, obo9.getAuthorizationCode());
		assertEquals(null, obo9.getEducationCode());
		assertEquals(Legislation.actThroughDelegationByRegisteredPharmacist, obo9.getLegislation());
		assertEquals(null, obo9.getCpr());
		assertEquals(null, obo9.getEcpr());
		assertEquals("Test", obo9.getPseudonym());
		assertEquals(obo9String, obo9.generate(Validate.NO));
	}
}
