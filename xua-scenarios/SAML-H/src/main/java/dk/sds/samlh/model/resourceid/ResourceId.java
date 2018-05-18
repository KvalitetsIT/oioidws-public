package dk.sds.samlh.model.resourceid;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResourceId {
	public static final String ATTRIBUTE_NAME = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";

	private String patientId;
	private String oid;

	private void validate() throws ValidationException {
		if (patientId == null || patientId.isEmpty()) {
			throw new ValidationException("PatientId is mandatory.");
		}
		else if (oid == null || oid.isEmpty()) {
			throw new ValidationException("OId is mandatory.");
		}
	}

	public static ResourceId parse(String object, Validate validate) throws ValidationException {
		object = object.replace("&amp;", "&");
		String[] fragments = object.split("&");
		
		if (fragments.length < 2) {
			throw new ValidationException("not a legal input string:" + object);
		}
		
		ResourceId result = ResourceId.builder()
				.patientId(fragments[0].replace("^^^", ""))
				.oid(fragments[1])
				.build();
		
		if (validate == Validate.YES) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException {
		if (validate == Validate.YES) {
			validate();
		}

		return patientId + "^^^&" + oid + "&ISO";
	}
}
