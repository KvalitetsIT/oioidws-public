package dk.sds.samlh.model.onbehalfof;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class OnBehalfOf implements ClaimModel {
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
		if (getLegislation() == null) {
			throw new ValidationException("Legislation cannot be null");
		}
		
		if (getCpr() != null) {
			if (getLegislation() != Legislation.actThroughProcurationBy && getLegislation() != Legislation.actThroughCustodyOver && getLegislation() != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for CPR value: " + getLegislation());
			}
			else if (getEcpr() != null || getPseudonym() != null || getAuthorizationCode() != null || getEducationCode() != null) {
				throw new ValidationException("ecpr, pseudonym, authorizationCode and educationCode must be null when cpr is non-null");
			}
		}
		else if (getEcpr() != null) {
			if (getLegislation() != Legislation.actThroughProcurationBy && getLegislation() != Legislation.actThroughCustodyOver && getLegislation() != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for ECPR value: " + getLegislation());
			}
			else if (getCpr() != null || getPseudonym() != null || getAuthorizationCode() != null || getEducationCode() != null) {
				throw new ValidationException("cpr, pseudonym, authorizationCode and educationCode must be null when ecpr is non-null");
			}
		}
		else if (getPseudonym() != null) {
			if (getLegislation() != Legislation.actThroughProcurationBy && getLegislation() != Legislation.actThroughCustodyOver && getLegislation() != Legislation.actThroughDelegationByRegisteredPharmacist) {
				throw new ValidationException("Legislation value not allowed for PSEUDONYM value: " + getLegislation());
			}
			else if (getEcpr() != null || getCpr() != null || getAuthorizationCode() != null || getEducationCode() != null) {
				throw new ValidationException("cpr, ecpr, authorizationCode and educationCode must be null when pseudonym is non-null");
			}
		}
		
		if ((getAuthorizationCode() != null && getEducationCode() == null) ||
		    (getEducationCode() != null && getAuthorizationCode() == null)) {
			throw new ValidationException("Both educationCode and authorizationCode must be non-null at the same time");
		}
		else if (getAuthorizationCode() != null && getEducationCode() != null) {
			if (getLegislation() != Legislation.actAsTechnicalAssistantForAuthorizedHealthcareProfessional && getLegislation() != Legislation.actThroughDelegationByAuthorizedHealthcareProfessional) {
				throw new ValidationException("Invalid legislation: " + getLegislation());
			}
			else if (getAuthorizationCode().length() != 5) {
				throw new ValidationException("AuthorizationCode must be 5 characters in length");
			}
			else if (getEducationCode().length() != 4) {
				throw new ValidationException("EducationCode must be 4 characters in length");
			}
			else if (getEcpr() != null || getCpr() != null || getPseudonym() != null) {
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

		OnBehalfOf result = new OnBehalfOf();
		result.authorizationCode = authorizationCode;
		result.cpr = cpr;
		result.ecpr = ecpr;
		result.educationCode = educationCode;
		result.legislation = legislation;
		result.pseudonym = pseudonym;

		if (validate.equals(Validate.YES)) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException, JAXBException {
		String result = "";

		if (getCpr() != null) {
			result += cprPrefix + getLegislation() + ":cprNumberIdentifier:" + getCpr();
		}
		else if (getEcpr() != null || getPseudonym() != null) {
			result += ecprPseudonymPrefix + getLegislation() + (getEcpr() != null ? ":ECprNumberIdentifier:" + getEcpr() : ":PersistentPseudonym:" + getPseudonym());
		}
		else if (getAuthorizationCode() != null || getEducationCode() != null) {
			result += byAuthorizedHealthcareProfessionalPrefix + getLegislation() + ":userAuthorization:AuthorizationCode:" + getAuthorizationCode() + ":EducationCode:" + getEducationCode();
		}

		if (validate.equals(Validate.YES)) {
			validate();
		}

		return result;
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.ON_BEHALF_OF;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.TEXT;
	}

	public Legislation getLegislation() {
		return legislation;
	}

	public void setLegislation(Legislation legislation) {
		this.legislation = legislation;
	}

	public String getCpr() {
		return cpr;
	}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public String getEcpr() {
		return ecpr;
	}

	public void setEcpr(String ecpr) {
		this.ecpr = ecpr;
	}

	public String getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getEducationCode() {
		return educationCode;
	}

	public void setEducationCode(String educationCode) {
		this.educationCode = educationCode;
	}
}