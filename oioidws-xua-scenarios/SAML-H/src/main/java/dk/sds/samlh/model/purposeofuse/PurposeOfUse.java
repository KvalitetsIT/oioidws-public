package dk.sds.samlh.model.purposeofuse;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Element;

import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.ModelUtil;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.xsd.purposeofuse.ObjectFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PurposeOfUse implements ClaimModel {
	private static final String ATTRIBUTE_NAME = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
	
	private Code code;
	private String codeSystem;
	private String codeSystemName;
	private String displayName;
	private String xsiType;
	
	public enum Code {
		TREATMENT, EMERGENCY, REQUEST		
	}

	public void validate() throws ValidationException {
		if (this.xsiType == null || !this.xsiType.equals("CE")) {
			throw new ValidationException("Type must be set to CE.");
		}
		else if (this.code == null) {
			throw new ValidationException("Code attribute is mandatory.");
		}
		else if (this.codeSystem == null) {
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

		PurposeOfUse result = PurposeOfUse.builder()
				.code(pouType.getCode() != null ? Code.valueOf(pouType.getCode()) : null)
				.codeSystem(pouType.getCodeSystem())
				.xsiType(pouType.getXsiType())
				.codeSystemName(pouType.getCodeSystemName())
				.displayName(pouType.getDisplayName())
				.build();

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
		object.setCode(this.code.toString());
		object.setCodeSystem(this.codeSystem);
		object.setXsiType(this.xsiType);
		object.setCodeSystemName(this.codeSystemName);
		object.setDisplayName(this.displayName);

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
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
