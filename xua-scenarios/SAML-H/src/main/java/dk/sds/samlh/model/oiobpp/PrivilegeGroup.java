package dk.sds.samlh.model.oiobpp;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrivilegeGroup {
	private String scopeEducationCode;
	private String scopeAuthorizationCode;
	private List<String> privileges;
}
