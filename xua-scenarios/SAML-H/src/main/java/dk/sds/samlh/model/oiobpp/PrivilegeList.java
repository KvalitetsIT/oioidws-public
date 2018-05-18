package dk.sds.samlh.model.oiobpp;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.util.UrnUriValidator;
import dk.sds.samlh.xsd.privilegesintermediate.ObjectFactory;
import dk.sds.samlh.xsd.privilegesintermediate.PrivilegeGroupType;
import dk.sds.samlh.xsd.privilegesintermediate.PrivilegeListType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivilegeList {
	private static final String SCOPE_PREFIX = "urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:";
	private static final String SCOPE_INFIX = ":EducationCode:";
	public static final String ATTRIBUTE_NAME = "dk:gov:saml:attribute:Privileges_intermediate";

	private List<PrivilegeGroup> privilegeGroups = new ArrayList<>();

	public void validate() throws ValidationException {
		if (privilegeGroups.size() == 0) {
			throw new ValidationException("PrivilegeList should contain at least one PrivilegeGroup element.");
		}

		for (PrivilegeGroup privilegeGroup : privilegeGroups) {
			if (privilegeGroup.getPrivileges().size() == 0) {
				throw new ValidationException("Found PrivilegeGroup element that doesn't contain any Privilege elements.");
			}
			else if (privilegeGroup.getScopeAuthorizationCode() == null || privilegeGroup.getScopeAuthorizationCode().length() != 5) {
				throw new ValidationException("authorizationCode cannot be null, and must be 5 characters in length");
			}
			else if (privilegeGroup.getScopeEducationCode() == null || privilegeGroup.getScopeEducationCode().length() != 4) {
				throw new ValidationException("educationCode cannot be null, and must be 4 characters in length");
			}
			
			for (String privilege : privilegeGroup.getPrivileges()) {
				if(!UrnUriValidator.validateURI(privilege) && !UrnUriValidator.validateURN(privilege)) {
					throw new ValidationException("Found invalid Privilege: "+ privilege);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static PrivilegeList parse(String object, Validate validate) throws ValidationException, JAXBException {
		// we accept both base64 encoded input, and "raw" xml-strings
		try {
			object = new String(Base64.getDecoder().decode(object.getBytes(Charset.forName("UTF-8"))));
		}
		catch (Exception ex) {
			; // ignore
		}

		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		JAXBElement<PrivilegeListType> privilegeList = (JAXBElement<PrivilegeListType>) unmarsheller.unmarshal(new ByteArrayInputStream(object.getBytes(Charset.forName("UTF-8"))));

		PrivilegeListType priviligeListType = privilegeList.getValue();

		PrivilegeList result = new PrivilegeList();
		for (PrivilegeGroupType privilegeGroupType : priviligeListType.getPrivilegeGroup()) {
			PrivilegeGroup group = buildPriviligeGroupFromScope(privilegeGroupType.getScope());

			if (group == null) {
				throw new ValidationException("Invalid scope: " + privilegeGroupType.getScope());
			}
			
			group.setPrivileges(privilegeGroupType.getPrivilege());
			result.getPrivilegeGroups().add(group);
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
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		PrivilegeListType privlageList = new PrivilegeListType();
		for (PrivilegeGroup privilegeGroup : this.getPrivilegeGroups()) {
			PrivilegeGroupType privilegeGroupType = new PrivilegeGroupType();
			privilegeGroupType.setScope(SCOPE_PREFIX + privilegeGroup.getScopeAuthorizationCode() + SCOPE_INFIX + privilegeGroup.getScopeEducationCode());
			privilegeGroupType.getPrivilege().addAll(privilegeGroup.getPrivileges());
			privlageList.getPrivilegeGroup().add(privilegeGroupType);
		}

		JAXBElement<PrivilegeListType> object = objectFactory.createPrivilegeList(privlageList);
		jaxbMarshaller.marshal(object, writer);
		String result = writer.toString();
		
		return Base64.getEncoder().encodeToString(result.getBytes(Charset.forName("UTF-8")));
	}
	
	private static PrivilegeGroup buildPriviligeGroupFromScope(String scope) throws ValidationException {
		String tokens[] = scope.split(":");
		if (tokens.length != 9 ||
			!"urn".equals(tokens[0]) ||
			!"dk".equals(tokens[1]) ||
			!"healthcare".equals(tokens[2]) ||
			!"saml".equals(tokens[3]) ||
			!"userAuthorization".equals(tokens[4]) ||
			!"AuthorizationCode".equals(tokens[5]) ||
			!"EducationCode".equals(tokens[7])) {

			return null;
		}
		
		return PrivilegeGroup.builder().scopeAuthorizationCode(tokens[6]).scopeEducationCode(tokens[8]).build();
	}
}
