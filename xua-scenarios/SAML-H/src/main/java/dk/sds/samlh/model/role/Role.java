package dk.sds.samlh.model.role;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.XmlObjectModel;
import dk.sds.samlh.xsd.role.ObjectFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Role extends XmlObjectModel {
	public static final String ATTRIBUTE_NAME = "urn:oasis:names:tc:xacml:2.0:subject:role";

	private static ArrayList<String> allowedCodes = new ArrayList<String>();

	private String code;
	private String codeSystem;
	private String codeSystemName;
	private String displayName;
	private String xsiType;
	
	static {
		allowedCodes.add("4498");
		allowedCodes.add("5015");
		allowedCodes.add("5151");
		allowedCodes.add("5152");
		allowedCodes.add("5153");
		allowedCodes.add("5155");
		allowedCodes.add("5158");
		allowedCodes.add("5159");
		allowedCodes.add("5166");
		allowedCodes.add("5175");
		allowedCodes.add("5176");
		allowedCodes.add("5176");
		allowedCodes.add("5265");
		allowedCodes.add("5431");
		allowedCodes.add("5432");
		allowedCodes.add("5433");
		allowedCodes.add("5451");
		allowedCodes.add("7170");
		allowedCodes.add("9495");
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
		else if (!this.codeSystem.equals("1.2.208.176.1.3")) {
			throw new ValidationException("CodeSystem must be set to [1.2.208.176.1.3]");
		}
		else if (!allowedCodes.contains(this.code)) {
			throw new ValidationException("Code: Only the following values SHOULD be used: [" + String.join(", ", allowedCodes) + "]");
		}
	}

	public static Role parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		dk.sds.samlh.xsd.role.Role roleType = (dk.sds.samlh.xsd.role.Role) unmarsheller.unmarshal(new ByteArrayInputStream(object.getBytes(Charset.forName("UTF-8"))));

		Role result = Role.builder()
			.code(roleType.getCode())
			.codeSystem(roleType.getCodeSystem())
			.xsiType(roleType.getXsiType())
			.codeSystemName(roleType.getCodeSystemName())
			.displayName(roleType.getDisplayName())
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

		dk.sds.samlh.xsd.role.Role object = objectFactory.createRole();
		object.setCode(this.code);
		object.setCodeSystem(this.codeSystem);
		object.setXsiType(this.xsiType);
		object.setCodeSystemName(this.codeSystemName);
		object.setDisplayName(this.displayName);

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}
}
