package dk.sds.sts.providers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.rt.security.claims.Claim;
import org.apache.cxf.rt.security.claims.ClaimCollection;
import org.apache.cxf.sts.claims.ClaimsHandler;
import org.apache.cxf.sts.claims.ClaimsParameters;
import org.apache.cxf.sts.claims.ProcessedClaim;
import org.apache.cxf.sts.claims.ProcessedClaimCollection;

public class AuthClaimsHandler implements ClaimsHandler {
	private static List<URI> knownURIs = new ArrayList<>();
	private static final URI AUTH_CLAIM = URI.create("http://docs.oasis-open.org/wsfed/authorization/200706/authclaims");

	static {
		knownURIs.add(AUTH_CLAIM);
	}

	public List<URI> getSupportedClaimTypes() {
		return knownURIs;
	}

	public ProcessedClaimCollection retrieveClaimValues(ClaimCollection claims, ClaimsParameters parameters) {
		if (claims != null && !claims.isEmpty()) {
			ProcessedClaimCollection claimCollection = new ProcessedClaimCollection();
			
			for (Claim requestClaim : claims) {
				ProcessedClaim claim = new ProcessedClaim();
				claim.setClaimType(requestClaim.getClaimType());
				claimCollection.add(claim);
			}
			
			return claimCollection;
		}

		return null;
	}
}