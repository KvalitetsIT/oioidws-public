package dk.sds.samlh.xsd.purposeofuse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PurposeOfUse", namespace = "urn:hl7-org:v3")
public class PurposeOfUse {

	@XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
	private String xsiType;

	@XmlAttribute(name = "code")
	private String code;

	@XmlAttribute(name = "codeSystem")
	private String codeSystem;

	@XmlAttribute(name = "codeSystemName")
	private String codeSystemName;

	@XmlAttribute(name = "displayName")
	private String displayName;

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getXsiType() {
		return xsiType;
	}

	public void setXsiType(String xsiType) {
		this.xsiType = xsiType;
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
}
