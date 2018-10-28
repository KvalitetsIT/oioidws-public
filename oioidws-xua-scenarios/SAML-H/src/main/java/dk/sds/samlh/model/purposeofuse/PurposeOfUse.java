package dk.sds.samlh.model.purposeofuse;

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
import dk.sds.samlh.xsd.purposeofuse.ObjectFactory;

public class PurposeOfUse implements ClaimModel {	
	private Code code;
	private String codeSystem;
	private String codeSystemName;
	private String displayName;
	private String xsiType;
	
	public enum Code {
		TREATMENT, EMERGENCY, REQUEST		
	}

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
	
	public static PurposeOfUse parse(Element element, Validate validate) throws ValidationException, JAXBException {
		String str = null;

		try {
			str = ModelUtil.dom2String(element.getOwnerDocument());
		}
		catch (Exception ex) {
			throw new ValidationException("Cannot parse Element", ex);
		}

		return parse(str, validate);
	}

	public static PurposeOfUse parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		dk.sds.samlh.xsd.purposeofuse.PurposeOfUse pouType = (dk.sds.samlh.xsd.purposeofuse.PurposeOfUse) unmarsheller.unmarshal(ModelUtil.getSecureSource(object));

		PurposeOfUse result = new PurposeOfUse();
		result.code = (pouType.getCode() != null ? Code.valueOf(pouType.getCode()) : null);
		result.codeSystem = pouType.getCodeSystem();
		result.xsiType = pouType.getXsiType();
		result.codeSystemName = pouType.getCodeSystemName();
		result.displayName = pouType.getDisplayName();

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

		dk.sds.samlh.xsd.purposeofuse.PurposeOfUse object = objectFactory.createPurposeOfUse();
		object.setCode(this.getCode().toString());
		object.setCodeSystem(this.getCodeSystem());
		object.setXsiType(this.getXsiType());
		object.setCodeSystemName(this.getCodeSystemName());
		object.setDisplayName(this.getDisplayName());

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.PURPOSE_OF_USE;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.ELEMENT;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
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
