package dk.sds.samlh.model.oiobpp;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.util.UrnUriValidator;
import dk.sds.samlh.xsd.privilegesintermediate.ObjectFactory;
import dk.sds.samlh.xsd.privilegesintermediate.PrivilegeGroupType;
import dk.sds.samlh.xsd.privilegesintermediate.PrivilegeListType;

public class PrivilegeList implements ClaimModel {
	public enum PriviligeType { UserAuthorization, RegisteredPharmacist, CprNumberIdentifier, SeNumberIdentifier, CvrNumberIdentifier, ProductionNumberIdentifier };
	private static Map<String, PriviligeType> prefixMap = new HashMap<>();
	
	static {
		prefixMap.put("urn:dk:healthcare:saml:userAuthorization:", PriviligeType.UserAuthorization);
		prefixMap.put("urn:dk:healthcare:saml:RegisteredPharmacistCPR:", PriviligeType.RegisteredPharmacist);
		prefixMap.put("urn:dk:gov:saml:cprNumberIdentifier:", PriviligeType.CprNumberIdentifier);
		prefixMap.put("urn:dk:gov:saml:seNumberIdentifier:", PriviligeType.SeNumberIdentifier);
		prefixMap.put("urn:dk:gov:saml:cvrNumberIdentifier:", PriviligeType.CvrNumberIdentifier);
		prefixMap.put("urn:dk:gov:saml:productionUnitIdentifier:", PriviligeType.ProductionNumberIdentifier);
	}

	private List<PrivilegeGroup> privilegeGroups = new ArrayList<>();
	
	public void validate() throws ValidationException {
		if (getPrivilegeGroups().size() == 0) {
			throw new ValidationException("PrivilegeList should contain at least one PrivilegeGroup element.");
		}

		for (PrivilegeGroup privilegeGroup : getPrivilegeGroups()) {
			if (privilegeGroup.getPrivileges().size() == 0) {
				throw new ValidationException("Found PrivilegeGroup element that doesn't contain any Privilege elements.");
			}
			
			switch (privilegeGroup.getPriviligeType()) {
				case UserAuthorization:
					if (privilegeGroup.getScopeAuthorizationCode() == null || privilegeGroup.getScopeAuthorizationCode().length() != 5) {
						throw new ValidationException("authorizationCode cannot be null, and must be 5 characters in length");
					}
					else if (privilegeGroup.getScopeEducationCode() == null || privilegeGroup.getScopeEducationCode().length() != 4) {
						throw new ValidationException("educationCode cannot be null, and must be 4 characters in length");
					}
					break;
				case CprNumberIdentifier:
				case ProductionNumberIdentifier:
				case RegisteredPharmacist:
					if (privilegeGroup.getScopeValue() == null || !privilegeGroup.getScopeValue().matches("[0-9]+") || privilegeGroup.getScopeValue().length() != 10) {
						throw new ValidationException("ScopeValue: " + privilegeGroup.getScopeValue() + " is not numerical or its length is different than 10.");
					}
					break;
				case CvrNumberIdentifier:
				case SeNumberIdentifier:
					if (privilegeGroup.getScopeValue() == null || !privilegeGroup.getScopeValue().matches("[0-9]+") || privilegeGroup.getScopeValue().length() != 8) {
						throw new ValidationException("ScopeValue: " + privilegeGroup.getScopeValue() + " is not numerical or its length is different than 8.");
					}
					break;
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
		JAXBElement<PrivilegeListType> privilegeList = (JAXBElement<PrivilegeListType>) unmarsheller.unmarshal(ModelUtil.getSecureSource(object));

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

		PrivilegeListType privilegeList = new PrivilegeListType();
		for (PrivilegeGroup privilegeGroup : this.getPrivilegeGroups()) {
			PrivilegeGroupType privilegeGroupType = new PrivilegeGroupType();
			privilegeGroupType.getPrivilege().addAll(privilegeGroup.getPrivileges());
			
			switch (privilegeGroup.getPriviligeType()) {
				case CprNumberIdentifier:
				case CvrNumberIdentifier:
				case ProductionNumberIdentifier:
				case RegisteredPharmacist:
				case SeNumberIdentifier:
					privilegeGroupType.setScope(getPrefix(privilegeGroup.getPriviligeType()) + privilegeGroup.getScopeValue());
					break;
				case UserAuthorization:
					privilegeGroupType.setScope("urn:dk:healthcare:saml:userAuthorization:AuthorizationCode:" + privilegeGroup.getScopeAuthorizationCode() + ":EducationCode:" + privilegeGroup.getScopeEducationCode());
					break;
			}
			
			privilegeList.getPrivilegeGroup().add(privilegeGroupType);
		}

		JAXBElement<PrivilegeListType> object = objectFactory.createPrivilegeList(privilegeList);
		jaxbMarshaller.marshal(object, writer);
		String result = writer.toString();
		
		return Base64.getEncoder().encodeToString(result.getBytes(Charset.forName("UTF-8")));
	}
	
	private static PrivilegeGroup buildPriviligeGroupFromScope(String scope) throws ValidationException {
		for (String prefix : prefixMap.keySet()) {
			if (scope.startsWith(prefix)) {
				switch (prefixMap.get(prefix)) {
					case CprNumberIdentifier: {
						String cpr = scope.substring(prefix.length());

						PrivilegeGroup res = new PrivilegeGroup();
						res.setPriviligeType(PriviligeType.CprNumberIdentifier);
						res.setScopeValue(cpr);

						return res;
					}
					case CvrNumberIdentifier: {
						String cvr = scope.substring(prefix.length());

						PrivilegeGroup res = new PrivilegeGroup();
						res.setPriviligeType(PriviligeType.CvrNumberIdentifier);
						res.setScopeValue(cvr);

						return res;
					}
					case ProductionNumberIdentifier: {
						String pNumber = scope.substring(prefix.length());

						PrivilegeGroup res = new PrivilegeGroup();
						res.setPriviligeType(PriviligeType.ProductionNumberIdentifier);
						res.setScopeValue(pNumber);
						
						return res;
					}
					case RegisteredPharmacist: {
						String pharmacist = scope.substring(prefix.length());

						PrivilegeGroup res = new PrivilegeGroup();
						res.setPriviligeType(PriviligeType.RegisteredPharmacist);
						res.setScopeValue(pharmacist);
						
						return res;
					}
					case SeNumberIdentifier: {
						String se = scope.substring(prefix.length());

						PrivilegeGroup res = new PrivilegeGroup();
						res.setPriviligeType(PriviligeType.SeNumberIdentifier);
						res.setScopeValue(se);
						
						return res;
					}
					case UserAuthorization: {
						String scopeValue = scope.substring(prefix.length());
						
						String[] tokens = scopeValue.split(":");
						if (tokens.length == 4 && "AuthorizationCode".equals(tokens[0]) && "EducationCode".equals(tokens[2])) {
							
							PrivilegeGroup res = new PrivilegeGroup();
							res.setPriviligeType(PriviligeType.UserAuthorization);
							res.setScopeAuthorizationCode(tokens[1]);
							res.setScopeEducationCode(tokens[3]);
							
							return res;
						}

						break;
					}
				}
			}
		}
		
		throw new ValidationException("Unknown scope: " + scope);
	}

	private String getPrefix(PriviligeType type) throws ValidationException {
		for (String prefix : prefixMap.keySet()) {
			if (prefixMap.get(prefix).equals(type)) {
				return prefix;
			}
		}
		
		// we do not actually get here unless there is a programming error in this SAML-H
		// framework, so this is just in that special case ;)
		throw new ValidationException("Unknown priviligeType: " + type);
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.PRIVILEGES_INTERMEDIATE;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.TEXT;
	}

	public List<PrivilegeGroup> getPrivilegeGroups() {
		return privilegeGroups;
	}

	public void setPrivilegeGroups(List<PrivilegeGroup> privilegeGroups) {
		this.privilegeGroups = privilegeGroups;
	}
}
