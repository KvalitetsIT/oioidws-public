package dk.sds.samlh.model.oiobpp;

import java.util.List;

import dk.sds.samlh.model.oiobpp.PrivilegeList.PriviligeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrivilegeGroup {
	private PriviligeType priviligeType;
	private String scopeValue; // non-null for everything except UserAuthorization
	private String scopeEducationCode; // non-null for UserAuthorization
	private String scopeAuthorizationCode; // non-null for UserAuthorization
	private List<String> privileges;
}
