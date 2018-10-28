package dk.sds.samlh.model.resourceid;

import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class ResourceId implements ClaimModel {
	private String patientId;
	private String oid;

	private void validate() throws ValidationException {
		if (getPatientId() == null || getPatientId().isEmpty()) {
			throw new ValidationException("PatientId is mandatory.");
		}
		else if (getOid() == null || getOid().isEmpty()) {
			throw new ValidationException("OId is mandatory.");
		}
	}

	public static ResourceId parse(String object, Validate validate) throws ValidationException {
		object = object.replace("&amp;", "&");
		String[] fragments = object.split("&");
		
		if (fragments.length < 2) {
			throw new ValidationException("not a legal input string:" + object);
		}
		
		ResourceId result = new ResourceId();
		result.patientId = fragments[0].replace("^^^", "");
		result.oid = fragments[1];
		
		if (validate == Validate.YES) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException {
		if (validate == Validate.YES) {
			validate();
		}

		return getPatientId() + "^^^&" + getOid() + "&ISO";
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.RESOURCE_ID;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.TEXT;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
}
