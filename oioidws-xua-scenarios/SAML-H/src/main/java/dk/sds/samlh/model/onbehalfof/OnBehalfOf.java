package dk.sds.samlh.model.onbehalfof;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnBehalfOf implements ClaimModel {
	private static final String ATTRIBUTE_NAME = "dk:healthcare:saml:attribute:OnBehalfOf";
	
	private static final String userAuthorizationPattern = "urn:dk:healthcare:saml:(.+):userAuthorization:AuthorizationCode:(.+):EducationCode:(.+)";
	private static final String cprPattern = "urn:dk:healthcare:saml:(.+):cprNumberIdentifier:(.+)";
	private static final String ecprPattern = "urn:dk:healthcare:saml:attribute:(.+):ECprNumberIdentifier:(.+)";
	private static final String pseudonymPattern = "urn:dk:healthcare:saml:attribute:(.+):PersistentPseudonym:(.+)";

	private static final String cprPrefix = "urn:dk:healthcare:saml:";
	private static final String ecprPseudonymPrefix = "urn:dk:healthcare:saml:attribute:";
	private static final String byAuthorizedHealthcareProfessionalPrefix = "urn:dk:healthcare:saml:";

	public enum Legislation {
		actThroughProcurationBy,
		actThroughCustodyOver,
		actThroughDelegationByRegisteredPharmacist,
		actThroughDelegationByAuthorizedHealthcareProfessional,
		actAsTechnicalAssistantForAuthorizedHealthcareProfessional
	}

	private Legislation legislation;
	private String cpr;
	private String ecpr;
	private String pseudonym;
	private String authorizationCode;
	private String educationCode;

	private void validate() throws ValidationException {
		if (legislation == null) {
			throw new ValidationException("Legislation cannot be null");
		}
		
		if (cpr != null) {
			if (legislation != Legislation.actThroughProcurationBy && legislation != Legislation.actThroughCustodyOver && legislation != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for CPR value: " + legislation);
			}
			else if (ecpr != null || pseudonym != null || authorizationCode != null || educationCode != null) {
				throw new ValidationException("ecpr, pseudonym, authorizationCode and educationCode must be null when cpr is non-null");
			}
		}
		else if (ecpr != null) {
			if (legislation != Legislation.actThroughProcurationBy && legislation != Legislation.actThroughCustodyOver && legislation != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for ECPR value: " + legislation);
			}
			else if (cpr != null || pseudonym != null || authorizationCode != null || educationCode != null) {
				throw new ValidationException("cpr, pseudonym, authorizationCode and educationCode must be null when ecpr is non-null");
			}
		}
		else if (pseudonym != null) {
			if (legislation != Legislation.actThroughProcurationBy && legislation != Legislation.actThroughCustodyOver && legislation != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for PSEUDONYM value: " + legislation);
			}
			else if (ecpr != null || cpr != null || authorizationCode != null || educationCode != null) {
				throw new ValidationException("cpr, ecpr, authorizationCode and educationCode must be null when pseudonym is non-null");
			}
		}
		
		if ((authorizationCode != null && educationCode == null) ||
		    (educationCode != null && authorizationCode == null)) {
			throw new ValidationException("Both educationCode and authorizationCode must be non-null at the same time");
		}
		else if (authorizationCode != null && educationCode != null) {
			if (legislation != Legislation.actAsTechnicalAssistantForAuthorizedHealthcareProfessional && legislation != Legislation.actThroughDelegationByAuthorizedHealthcareProfessional) {
				throw new ValidationException("Invalid legislation: " + legislation);
			}
			else if (authorizationCode.length() != 5) {
				throw new ValidationException("AuthorizationCode must be 5 characters in length");
			}
			else if (educationCode.length() != 4) {
				throw new ValidationException("EducationCode must be 4 characters in length");
			}
			else if (ecpr != null || cpr != null || pseudonym != null) {
				throw new ValidationException("cpr, ecpr and pseudonym must be null when authorizationCode and educationCode are non-null");
			}
		}
	}

	public static OnBehalfOf parse(String object, Validate validate) throws JAXBException, ValidationException {
		Legislation legislation = null;
		String cpr = null;
		String ecpr = null;
		String pseudonym = null;
		String authorizationCode = null;
		String educationCode = null;
		Matcher matcher;

		if (object.matches(userAuthorizationPattern)) {
			matcher = Pattern.compile(userAuthorizationPattern).matcher(object);

			while (matcher.find()) {
				String group1 = matcher.group(1);
				legislation = (group1 != null) ? Legislation.valueOf(group1) : null;
				authorizationCode = matcher.group(2);
				educationCode = matcher.group(3);
			}
		}
		else if (object.matches(cprPattern)) {
			matcher = Pattern.compile(cprPattern).matcher(object);
			
			while (matcher.find()) {
				String group1 = matcher.group(1);
				legislation = (group1 != null) ? Legislation.valueOf(group1) : null;
				cpr = matcher.group(2);
			}
		}
		else if (object.matches(ecprPattern)) {
			matcher = Pattern.compile(ecprPattern).matcher(object);
			
			while (matcher.find()) {
				String group1 = matcher.group(1);
				legislation = (group1 != null) ? Legislation.valueOf(group1) : null;
				ecpr = matcher.group(2);
			}
		}
		else if (object.matches(pseudonymPattern)) {
			matcher = Pattern.compile(pseudonymPattern).matcher(object);

			while (matcher.find()) {
				String group1 = matcher.group(1);
				legislation = (group1 != null) ? Legislation.valueOf(group1) : null;
				pseudonym = matcher.group(2);
			}
		}

		OnBehalfOf result = OnBehalfOf.builder().authorizationCode(authorizationCode).cpr(cpr).ecpr(ecpr).educationCode(educationCode).legislation(legislation).pseudonym(pseudonym).build();

		if (validate.equals(Validate.YES)) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException, JAXBException {
		String result = "";

		if (cpr != null) {
			result += cprPrefix + legislation + ":cprNumberIdentifier:" + cpr;
		}
		else if (ecpr != null || pseudonym != null) {
			result += ecprPseudonymPrefix + legislation + (ecpr != null ? ":ECprNumberIdentifier:" + ecpr : ":PersistentPseudonym:" + pseudonym);
		}
		else if (authorizationCode != null || educationCode != null) {
			result += byAuthorizedHealthcareProfessionalPrefix + legislation + ":userAuthorization:AuthorizationCode:" + authorizationCode + ":EducationCode:" + educationCode;
		}

		if (validate.equals(Validate.YES)) {
			validate();
		}

		return result;
	}

	@Override
	public String getAttributeName() {
		return ATTRIBUTE_NAME;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.TEXT;
	}
}