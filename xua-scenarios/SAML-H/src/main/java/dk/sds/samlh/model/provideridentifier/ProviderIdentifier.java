package dk.sds.samlh.model.provideridentifier;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import dk.sds.samlh.model.XmlObjectModel;
import dk.sds.samlh.xsd.provideridentifier.ObjectFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProviderIdentifier extends XmlObjectModel {
	public static final String ATTRIBUTE_NAME = "urn:ihe:iti:xua:2017:subject:provider-identifier";

	private String xsiType;
	private String root;
	private String extension;
	private String assigningAuthorityName;
	private Boolean displayable;

	public void validate() throws ValidationException {
		if (this.xsiType == null || !this.xsiType.equals("II")) {
			throw new ValidationException("Type must be set to II.");
		}
		else if (this.root == null) {
			throw new ValidationException("Root attribute is mandatory.");
		}
		else if (this.extension == null) {
			throw new ValidationException("Extension attribute is mandatory.");
		}
	}

	public static ProviderIdentifier parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		dk.sds.samlh.xsd.provideridentifier.ProviderIdentifier piType = (dk.sds.samlh.xsd.provideridentifier.ProviderIdentifier) unmarsheller.unmarshal(new ByteArrayInputStream(object.getBytes(Charset.forName("UTF-8"))));

		ProviderIdentifier result = ProviderIdentifier.builder()
			.root(piType.getRoot())
			.extension(piType.getExtension())
			.assigningAuthorityName(piType.getAssigningAuthorityName())
			.displayable(piType.getDisplayable())
			.xsiType(piType.getXsiType())
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

		dk.sds.samlh.xsd.provideridentifier.ProviderIdentifier object = objectFactory.createProviderIdentifier();
		object.setRoot(this.root);
		object.setExtension(this.extension);
		object.setAssigningAuthorityName(this.assigningAuthorityName);
		object.setDisplayable(this.displayable);
		object.setXsiType(this.xsiType);

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}
}
