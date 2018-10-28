package dk.sds.samlh.model.childrenincustody;

import dk.sds.samlh.model.AttributeNameConstants;
import dk.sds.samlh.model.ClaimModel;
import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.ValidationException;

public class ChildrenInCustody implements ClaimModel {
	public enum CprType {
		CPR("urn:dk:gov:saml:cprNumberIdentifier"), ECPR("urn:dk:healthcare:saml:attribute:ECprNumberIdentifier");

		private String value;

		private CprType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static CprType fromValue(String value) {
			for (CprType type : CprType.values()) {
				if (type.getValue().equals(value)) {
					return type;
				}
			}

			return null;
		}
	}

	private CprType cprType;
	private String value;

	public void validate() throws ValidationException {
		if (getCprType() == null) {
			throw new ValidationException("CprType not set");
		}

		if (getValue() == null || getValue().length() != 10) {
			throw new ValidationException("Value must have 10 characters");
		}

		switch (this.getCprType()) {
			case CPR:
				try {
					int day = Integer.parseInt(getValue().substring(0, 2));
					int month = Integer.parseInt(getValue().substring(2, 4));
					int year = Integer.parseInt(getValue().substring(4, 6));

					int end = Integer.parseInt(getValue().substring(6, 9));
					if (day < 0 || day > 31 || month < 0 || month > 12 || year < 0 || year > 99 || end < 0 || end > 9999) {
						throw new ValidationException("Not a legal date");
					}
				}
				catch (Exception ex) {
					throw new ValidationException("CPR value is invalid", ex);
				}
				break;
			case ECPR:
				try {
					int day = Integer.parseInt(getValue().substring(0, 2));
					int month = Integer.parseInt(getValue().substring(2, 4));
					int year = Integer.parseInt(getValue().substring(4, 6));

					if (!getValue().substring(6, 9).matches("^[a-zA-Z0-9]*$")) {
						throw new ValidationException("E-CPR control value is invalid.");
					}
					else if (day < 0 || day > 31 || month < 0 || month > 12 || year < 0 || year > 99) {
						throw new ValidationException("E-CPR value not a valid date.");
					}
				}
				catch (Exception ex) {
					throw new ValidationException("E-CPR value is invalid.", ex);
				}

				break;
		}
	}

	public static ChildrenInCustody parse(String object, Validate validate) throws ValidationException {
		ChildrenInCustody result = null;

		long offset = object.lastIndexOf(":");
		if (offset == -1) {
			throw new ValidationException("No ':' present in string!");
		}

		try {
			String value = object.substring(object.lastIndexOf(":") + 1);
			String cprTypeValue = object.substring(0, object.lastIndexOf(":"));
			CprType cprType = (cprTypeValue != null) ? CprType.fromValue(cprTypeValue) : null;

			result = new ChildrenInCustody();
			result.setCprType(cprType);
			result.setValue(value);
		}
		catch (Exception e) {
			throw new ValidationException("Unable to parse string", e);
		}

		if (validate.equals(Validate.YES)) {
			result.validate();
		}

		return result;
	}

	public String generate(Validate validate) throws ValidationException {
		if (validate.equals(Validate.YES)) {
			validate();
		}

		return getCprType().getValue() + ':' + getValue();
	}

	@Override
	public String getAttributeName() {
		return AttributeNameConstants.CHILDREN_IN_CUSTODY;
	}

	@Override
	public ClaimType getClaimType() {
		return ClaimType.TEXT;
	}

	public CprType getCprType() {
		return cprType;
	}

	public void setCprType(CprType cprType) {
		this.cprType = cprType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
