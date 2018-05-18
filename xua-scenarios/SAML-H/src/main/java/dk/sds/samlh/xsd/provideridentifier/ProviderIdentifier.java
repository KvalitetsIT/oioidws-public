package dk.sds.samlh.xsd.provideridentifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "id", namespace = "urn:hl7-org:v3")
public class ProviderIdentifier {

	@XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
	private String xsiType;

	@XmlAttribute(name = "root")
	private String root;

	@XmlAttribute(name = "extension")
	private String extension;

	@XmlAttribute(name = "assigningAuthorityName")
	private String assigningAuthorityName;

	@XmlAttribute(name = "displayable")
	private Boolean displayable;

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
