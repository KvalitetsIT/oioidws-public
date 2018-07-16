package client.saml;

import javax.xml.bind.JAXBException;

import org.opensaml.xml.io.MarshallingException;

import dk.itst.oiosaml.sp.UserAssertion;
import dk.itst.oiosaml.sp.UserAssertionHolder;
import dk.itst.oiosaml.sp.UserAttribute;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.userauthorization.UserAuthorizationList;

public class UserAuthorizationFetcher {	
	public static String fetch() throws MarshallingException, ValidationException, JAXBException {

		// pull the attribute from the users assertion
		UserAssertion userAssertion = UserAssertionHolder.get();
		UserAttribute attribute = userAssertion.getAttribute("dk:healthcare:saml:attribute:UserAuthorizations");
		
		// OIOSAML deals with string-values, so we use UserAuthorizationList to ensure it
		// has the right format, and is valid, then convert back to String for the UI presentation
		if (attribute != null) {
			String value = attribute.getValue();
			
			UserAuthorizationList validatedUserAuthorization = UserAuthorizationList.parse(value, Validate.YES);
			return validatedUserAuthorization.generate(Validate.YES);
		}
		
		return null;
	}
}
