package client.callback;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.ws.security.trust.claims.ClaimsCallback;

import client.session.SessionContext;
import client.session.SessionContextHolder;
import dk.sds.samlh.model.Validate;

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

	private static Object createClaims() {
		SessionContext context = SessionContextHolder.get();
		
		if (context != null) {
			try {
				if (context.getResourceId() != null) {
					return context.getResourceId().generateClaim(Validate.YES);
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}			
		}		
		
		return null;
	}
}