package dk.sds.samlh.model.role;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Element;

import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.xsd.role.ObjectFactory;

public class Role implements ClaimModel {
	private String code;
	private String codeSystem;
	private String codeSystemName;
	private String displayName;
	private String xsiType;
	
	public void validate() throws ValidationException {
		if (this.getXsiType() == null || !this.getXsiType().equals("CE")) {
			throw new ValidationException("Type must be set to CE.");
		}
		else if (this.getCode() == null) {
			throw new ValidationException("Code attribute is mandatory.");
		}
		else if (this.getCodeSystem() == null) {
			throw new ValidationException("CodeSystem attribute is mandatory.");
		}
	}

	public static Role parse(Element element, Validate validate) throws ValidationException, JAXBException {
		String str = null;

		try {
			str = ModelUtil.dom2String(element.getOwnerDocument());
		}
		catch (Exception ex) {
			throw new ValidationException("Cannot parse Element", ex);
		}

		return parse(str, validate);
	}

	public static Role parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		dk.sds.samlh.xsd.role.Role roleType = (dk.sds.samlh.xsd.role.Role) unmarsheller.unmarshal(ModelUtil.getSecureSource(object));

		Role result = new Role();
		result.code = roleType.getCode();
		result.codeSystem = roleType.getCodeSystem();
		result.xsiType = roleType.getXsiType();
		result.codeSystemName = roleType.getCodeSystemName();
		result.displayName = roleType.getDisplayName();

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

		dk.sds.samlh.xsd.role.Role object = objectFactory.createRole();
		object.setCode(this.getCode());
		object.setCodeSystem(this.getCodeSystem());
		object.setXsiType(this.getXsiType());
		object.setCodeSystemName(this.getCodeSystemName());
		object.setDisplayName(this.getDisplayName());

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.ROLE;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.ELEMENT;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCodeSystemName() {
		return codeSystemName;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getXsiType() {
		return xsiType;
	}

	public void setXsiType(String xsiType) {
		this.xsiType = xsiType;
	}
}
