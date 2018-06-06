package client.sts;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.ws.security.tokenstore.SecurityToken;

import client.session.SessionContext;
import client.session.SessionContextHolder;
import dk.sds.samlh.model.resourceid.ResourceId;

// Apache CXF has a build-in caching mechanism, which will work fine for most ordinary
// cases, where a token is issued for a specific endpoint only.
//
// In the case where the token carries additional information (e.g. claims about
// the patient being treated), the caching mechanism in CXF will not work, as it
// will re-use any existing token, ignoring the change of patient-context (as this
// change is not known to CXF).
//
// This is a very simple caching mechanism, which can be used a template for building
// a service-specific caching mechanism (it is assumed that the claims for each service
// may vary, so a TokenCache implementation is needed for each individual service, or
// more complex logic needs to be added to take the service-endpoint into consideration)
//
// In the example below, the claim "ResourceId" is used as a factor for deciding cache
// hit/misses, and a token is stored for each ResourceId.
//
// Note that no cleanup is implemented - it is recommended that a scheduled task
// runs through the cached tokens at regular intervals, to remove tokens from
// the map that has expired.
public class TokenCache {
	private static Map<String, SecurityToken> tokenMap = new HashMap<>();

	public static SecurityToken get() {
		SessionContext sessionContext = SessionContextHolder.get();
		
		if (sessionContext != null) {
			// if multiple claims are used to decide hit/miss in the tokencache,
			// compute a key from all the claims to use as a lookup key in the map
			ResourceId resourceId = sessionContext.getResourceId();
			
			if (resourceId != null) {
				String patientId = resourceId.getPatientId();
				
				if (patientId != null) {
					SecurityToken token = tokenMap.get(patientId);
					System.out.println("token in cache: " + token);
					
					return token;
				}
			}
		}
		
		return null;
	}

	public static void set(SecurityToken token) {
		SessionContext sessionContext = SessionContextHolder.get();
		
		if (sessionContext != null) {
			ResourceId resourceId = sessionContext.getResourceId();
			
			if (resourceId != null) {
				String patientId = resourceId.getPatientId();
				
				if (patientId != null) {
					tokenMap.put(patientId, token);
				}
			}
		}
	}
}
