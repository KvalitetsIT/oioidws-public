package client.callback;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.trust.claims.ClaimsCallback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClaimsCallbackHandler implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof ClaimsCallback) {
				ClaimsCallback callback = (ClaimsCallback) callbacks[i];
				callback.setClaims(createClaims());
			}
			else {
				throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
			}
		}
	}

	private static Element createClaims() {
		Document doc = DOMUtils.createDocument();
		Element claimsElement = doc.createElementNS("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "wst:Claims");
		claimsElement.setAttributeNS(null, "Dialect", "http://docs.oasis-open.org/wsfed/authorization/200706/authclaims");

		Element claimType = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "auth:ClaimType");
		claimType.setAttributeNS(null, "Uri", "urn:oasis:names:tc:xacml:2.0:resource:resource-id");
		claimsElement.appendChild(claimType);

		Element claimValue = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "auth:Value");
		claimValue.setTextContent("2512484916^^^&1.2.208.176.1.2&ISO");
		claimType.appendChild(claimValue);

		return claimsElement;
	}

}