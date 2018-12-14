package dk.sds.samlh.model.oiobpp;

import java.util.List;

import dk.sds.samlh.model.oiobpp.PrivilegeList.PriviligeType;

public class PrivilegeGroup {
	private PriviligeType priviligeType;
	private String scopeValue; // non-null for everything except UserAuthorization
	private String scopeEducationCode; // non-null for UserAuthorization
	private String scopeAuthorizationCode; // non-null for UserAuthorization
	private String yderNumberIdentifier; // non-null for YderNumberIdentifier
	private String regionCode; // might be non-null for YderNumberIdentifier
	private List<String> privileges;
	
	public PriviligeType getPriviligeType() {
		return priviligeType;
	}
	
	public void setPriviligeType(PriviligeType priviligeType) {
		this.priviligeType = priviligeType;
	}
	
	public String getScopeValue() {
		return scopeValue;
	}
	
	public void setScopeValue(String scopeValue) {
		this.scopeValue = scopeValue;
	}
	
	public String getScopeEducationCode() {
		return scopeEducationCode;
	}
	
	public void setScopeEducationCode(String scopeEducationCode) {
		this.scopeEducationCode = scopeEducationCode;
	}
	
	public String getScopeAuthorizationCode() {
		return scopeAuthorizationCode;
	}
	
	public void setScopeAuthorizationCode(String scopeAuthorizationCode) {
		this.scopeAuthorizationCode = scopeAuthorizationCode;
	}
	
	public List<String> getPrivileges() {
		return privileges;
	}
	
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}

	public String getRegionCode() {
		return regionCode;
	}
	
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getYderNumberIdentifier() {
		return yderNumberIdentifier;
	}
	
	public void setYderNumberIdentifier(String yderNumberIdentifier) {
		this.yderNumberIdentifier = yderNumberIdentifier;
	}
}
