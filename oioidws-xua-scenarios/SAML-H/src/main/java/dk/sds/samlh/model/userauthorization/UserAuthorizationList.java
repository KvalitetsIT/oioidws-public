package dk.sds.samlh.model.userauthorization;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Element;

import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.xsd.userauthorizations.ObjectFactory;
import dk.sds.samlh.xsd.userauthorizations.UserAuthorizationListType;
import dk.sds.samlh.xsd.userauthorizations.UserAuthorizationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthorizationList implements ClaimModel {
	private static final String ATTRIBUTE_NAME = "dk:healthcare:saml:attribute:UserAuthorizations";
	
	private List<UserAuthorization> userAuthorizations = new ArrayList<>();
		
	public void validate() throws ValidationException {
		ArrayList<String> authorizationCodes = new ArrayList<>();

		for (UserAuthorization userAuth : userAuthorizations) {
			String authorizationCode = userAuth.getAuthorizationCode();
			String eduCode = userAuth.getEducationCode();
			String eduType = userAuth.getEducationType();
			authorizationCodes.add(authorizationCode);

			if (authorizationCode == null || !authorizationCode.matches("^[a-zA-Z0-9]{5}$")) {
				throw new ValidationException("AuthorizationCode must consist of a 5-digit code, which is a mixture of letters and numbers.");
			}
			else if (eduCode == null) {
				throw new ValidationException("EducationCode must not be null");
			}
			else if (eduType == null) {
				throw new ValidationException("EducationType must not be null");
			}
		}

		HashSet<String> authCodesSet = new HashSet<>(authorizationCodes);
		if (authorizationCodes.size() > authCodesSet.size()) {
			throw new ValidationException("Non-unique AuthorizationCodes found [" + String.join(",", findDuplicates(authorizationCodes)) + "]");
		}
	}

	public static UserAuthorizationList parse(Element element, Validate validate) throws ValidationException, JAXBException {
		String str = null;

		try {
			str = ModelUtil.dom2String(element.getOwnerDocument());
		}
		catch (Exception ex) {
			throw new ValidationException("Cannot parse Element", ex);
		}

		return parse(str, validate);
	}

	@SuppressWarnings("unchecked")
	public static UserAuthorizationList parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		JAXBElement<dk.sds.samlh.xsd.userauthorizations.UserAuthorizationListType> userAuthType = (JAXBElement<dk.sds.samlh.xsd.userauthorizations.UserAuthorizationListType>) unmarsheller.unmarshal(ModelUtil.getSecureSource(object));

		UserAuthorizationList result = new UserAuthorizationList();
		for (UserAuthorizationType userAuthorization : userAuthType.getValue().getUserAuthorization()) {
			UserAuthorization userAuth = UserAuthorization.builder()
				.authorizationCode(userAuthorization.getAuthorizationCode())
				.educationCode(userAuthorization.getEducationCode())
				.educationType(userAuthorization.getEducationType())
				.build();
			
			result.userAuthorizations.add(userAuth);
		}

		if (validate.equals(Validate.YES)) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException, JAXBException {
		if (validate.equals(Validate.YES)) {
			validate();
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		StringWriter writer = new StringWriter();
		ObjectFactory objectFactory = new ObjectFactory();

		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);

		UserAuthorizationListType authListType = objectFactory.createUserAuthorizationListType();
		for (UserAuthorization userAuthorization : userAuthorizations) {
			UserAuthorizationType userAuth = objectFactory.createUserAuthorizationType();
			userAuth.setAuthorizationCode(userAuthorization.getAuthorizationCode());
			userAuth.setEducationCode(userAuthorization.getEducationCode());
			userAuth.setEducationType(userAuthorization.getEducationType());
			authListType.getUserAuthorization().add(userAuth);
		}

		JAXBElement<UserAuthorizationListType> object = objectFactory.createUserAuthorizationList(authListType);

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}
	
	private <T> Set<T> findDuplicates(Collection<T> list) {
		Set<T> duplicates = new LinkedHashSet<T>();
		Set<T> uniques = new HashSet<T>();

		for (T t : list) {
			if (!uniques.add(t)) {
				duplicates.add(t);
			}
		}

		return duplicates;
	}

	@Override
	public String getAttributeName() {
		return ATTRIBUTE_NAME;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.ELEMENT;
	}
}
