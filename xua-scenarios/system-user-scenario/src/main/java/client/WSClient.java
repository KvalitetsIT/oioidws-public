package client;

import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;

import client.session.SessionContext;
import client.session.SessionContextHolder;
import client.sts.TokenCache;
import dk.sds.samlh.model.resourceid.ResourceId;

public class WSClient {
    public static void main (String[] args) {
    	setSSLTrust();

    	// perform two calls in the context of one specific patient
    	// 1st call will fetch a token
    	// 2nd call will use the cached token
    	setPatientContext("2512484916");
    	System.out.println(hello("John"));
    	System.out.println(hello("Jane"));
    	
    	// perform another call in the context of another patient,
    	// resulting in a new token being fetched
    	setPatientContext("0405771187");
    	System.out.println(hello("James"));

    	// finally perform another call in the context of the
    	// first patient, reusing the first token
    	setPatientContext("2512484916");
    	System.out.println(hello("Jimmy"));
    }

	// This is a hack to support the self-signed SSL certificate used on the WSP
	// in a real production setting, the service would be protected by a trusted SSL certificate
	// and setting a custom truststore like this would not be needed
    private static void setSSLTrust() {
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
	}

	public static String hello(String name) {
		// create a new instance of the Service/Port classes for each call,
		// so any cached tokens from CXF is stripped
    	HelloWorldService service = new HelloWorldService();
    	HelloWorldPortType port = service.getHelloWorldPort();

    	// hook in any cached token (if it exist)
    	SecurityToken token = TokenCache.get();
    	if (token != null) {
	    	ClientImpl client = (ClientImpl) ClientProxy.getClient(port);    	
	    	client.getEndpoint().put(SecurityConstants.TOKEN, token);
    	}

    	// call service
        String resp = port.helloWorld(name);
        
        // extract token from the client and put in cache
    	ClientImpl client = (ClientImpl) ClientProxy.getClient(port);    	
    	token = (SecurityToken) client.getEndpoint().get(SecurityConstants.TOKEN);
    	if (token != null) {
    		TokenCache.set(token);
    	}
        
        return resp;
    }
    
    private static void setPatientContext(String cpr) {
    	ResourceId patientContext = ResourceId.builder()
    			.oid("1.2.208.176.1.2")
    			.patientId(cpr)
    			.build();

    	SessionContext context = new SessionContext();
    	context.setResourceId(patientContext);
    	SessionContextHolder.set(context);
    }
}
