package dk.sds.sts.providers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.namespace.QName;
import org.apache.cxf.rt.security.claims.Claim;
import org.apache.cxf.sts.claims.ClaimsParser;
import org.apache.cxf.ws.security.sts.provider.STSException;
import org.w3c.dom.Element;

import lombok.extern.log4j.Log4j;

@Log4j
public class AuthClaimsParser implements ClaimsParser {
	public static final String CLAIMS_DIALECT_AUTHCLAIMS = "http://docs.oasis-open.org/wsfed/authorization/200706/authclaims";
	public static final QName CLAIM_TYPE_NAME = new QName("http://docs.oasis-open.org/wsfed/authorization/200706", "ClaimType");
	public static final QName CLAIM_VALUE_NAME = new QName("http://docs.oasis-open.org/wsfed/authorization/200706", "ClaimValue");

	public String getSupportedDialect() {
		return CLAIMS_DIALECT_AUTHCLAIMS;
	}

	@Override
	public Claim parse(Element claim) {
		String claimLocalName = claim.getLocalName();
		String claimNS = claim.getNamespaceURI();
		QName claimName = new QName(claimNS, claimLocalName);

		if (CLAIM_TYPE_NAME.equals(claimName)) {
			if ("ClaimType".equals(claimLocalName)) {
				String claimTypeUri = claim.getAttribute("Uri");
				String claimTypeOptional = claim.getAttribute("Optional");

				Claim requestClaim = new Claim();
				requestClaim.setOptional(Boolean.parseBoolean(claimTypeOptional));
				requestClaim.addValue(claim.getTextContent());

				try {
					requestClaim.setClaimType(new URI(claimTypeUri));
				}
				catch (URISyntaxException ex) {
					throw new STSException("Cannot create URI from the given ClaimType attribute value " + claimTypeUri, ex);
				}

				return requestClaim;
			}

			log.warn("Found unknown element: " + claimLocalName + " " + claimNS);

			return null;
		}

		return null;
	}
}
