package dk.sds.samlh.model.userauthorization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAuthorization {
	private String authorizationCode;
	private String educationCode;
	private String educationType;
}
