package dk.sds.samlh.model.provideridentifier;

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
import dk.sds.samlh.xsd.provideridentifier.ObjectFactory;

public class ProviderIdentifier implements ClaimModel {
	private String xsiType;
	private String root;
	private String extension;
	private String assigningAuthorityName;
	private Boolean displayable;

	public void validate() throws ValidationException {
		if (this.getXsiType() == null) {
			throw new ValidationException("Type cannot be null.");
		}
		else if (this.getRoot() == null) {
			throw new ValidationException("Root attribute is mandatory.");
		}
		else if (this.getExtension() == null) {
			throw new ValidationException("Extension attribute is mandatory.");
		}
	}

	public static ProviderIdentifier parse(Element element, Validate validate) throws ValidationException, JAXBException {
		String str = null;

		try {
			str = ModelUtil.dom2String(element.getOwnerDocument());
		}
		catch (Exception ex) {
			throw new ValidationException("Cannot parse Element", ex);
		}

		return parse(str, validate);
	}

	public static ProviderIdentifier parse(String object, Validate validate) throws JAXBException, ValidationException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarsheller = context.createUnmarshaller();
		dk.sds.samlh.xsd.provideridentifier.ProviderIdentifier piType = (dk.sds.samlh.xsd.provideridentifier.ProviderIdentifier) unmarsheller.unmarshal(ModelUtil.getSecureSource(object));

		ProviderIdentifier result = new ProviderIdentifier();
		result.root = piType.getRoot();
		result.extension = piType.getExtension();
		result.assigningAuthorityName = piType.getAssigningAuthorityName();
		result.displayable = piType.getDisplayable();
		result.xsiType = piType.getXsiType();

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
		object.setRoot(this.getRoot());
		object.setExtension(this.getExtension());
		object.setAssigningAuthorityName(this.getAssigningAuthorityName());
		object.setDisplayable(this.getDisplayable());
		object.setXsiType(this.getXsiType());

		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.PROVIDER_IDENTIFIER;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.ELEMENT;
	}

	public String getXsiType() {
		return xsiType;
	}

	public void setXsiType(String xsiType) {
		this.xsiType = xsiType;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getAssigningAuthorityName() {
		return assigningAuthorityName;
	}

	public void setAssigningAuthorityName(String assigningAuthorityName) {
		this.assigningAuthorityName = assigningAuthorityName;
	}

	public Boolean getDisplayable() {
		return displayable;
	}

	public void setDisplayable(Boolean displayable) {
		this.displayable = displayable;
	}
}
