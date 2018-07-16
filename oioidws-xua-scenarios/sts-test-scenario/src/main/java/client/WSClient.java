package client;

import javax.xml.ws.soap.SOAPFaultException;

import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;

public class WSClient {
	public static enum TESTCASE {
		SERVER_ERROR,
		EXPIRED_TOKEN,
		BAD_CERTIFICATE,
		BAD_TOKENTYPE,
	}
	public static TESTCASE testcase;

    public static void main (String[] args) {
    	// This is a hack to support the self-signed SSL certificate used on the WSP
    	// in a real production setting, the service would be protected by a trusted SSL certificate
    	// and setting a custom truststore like this would not be needed
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

        try {
        	System.out.println("*** Running SERVER_ERROR testcase");
        	
	    	testcase = TESTCASE.SERVER_ERROR;
	    	HelloWorldService service = new HelloWorldService();
	    	HelloWorldPortType port = service.getHelloWorldPort();    	
	    	hello(port, "John");
	    	
	    	System.out.println("*** Unexpected non-error in test-case SERVER_ERROR");
        }
        catch (SOAPFaultException ex) {
        	if (!ex.getFault().getFaultString().equals("Internal Server Error")) {
        		System.out.println("*** Unexpected error in test-case SERVER_ERROR: " + ex.getFault().getFaultString());
        	}
        }

        try {
        	// the WSC fetches a token from the STS, which the STS signs
        	// with an unexpected certificate. The WSC then uses the token
        	// to call the WSP, which rejects it, because it does not trust the cert
        	System.out.println("*** Running BAD_CERTIFICATE testcase");
        	
	    	testcase = TESTCASE.BAD_CERTIFICATE;
	    	HelloWorldService service = new HelloWorldService();
	    	HelloWorldPortType port = service.getHelloWorldPort();    	
	    	hello(port, "John");
	    	
	    	System.out.println("*** Unexpected non-error in test-case BAD_CERTIFICATE");
        }
        catch (SOAPFaultException ex) {
        	if (!ex.getFault().getFaultString().equals("A security error was encountered when verifying the message")) {
        		System.out.println("*** Unexpected error in test-case BAD_CERTIFICATE: " + ex.getFault().getFaultString());
        	}
        }

        try {
        	// the WSC attempts to fetch a HOK token, but gets a BEARER token back,
        	// the WSC then attempt to use this BEARER token to call the WSP, which
        	// rejects this, as it expects a HOK token
        	System.out.println("*** Running BAD_TOKENTYPE testcase");
        	
	    	testcase = TESTCASE.BAD_TOKENTYPE;
	    	HelloWorldService service = new HelloWorldService();
	    	HelloWorldPortType port = service.getHelloWorldPort();    	
	    	hello(port, "John");
	    	
	    	System.out.println("*** Unexpected non-error in test-case BAD_TOKENTYPE");
        }
        catch (SOAPFaultException ex) {
        	if (!ex.getFault().getFaultString().equals("A security error was encountered when verifying the message")) {
        		System.out.println("*** Unexpected error in test-case BAD_TOKENTYPE: " + ex.getFault().getFaultString());
        	}
        }

        try {
        	// in this test-case the STS will issue tokens that expires shortly after being issued
        	System.out.println("*** Running EXPIRED_TOKEN testcase");

	    	testcase = TESTCASE.EXPIRED_TOKEN;
	    	HelloWorldService service = new HelloWorldService();
	    	HelloWorldPortType port = service.getHelloWorldPort();    	
	    	hello(port, "John");
	    	
	    	System.out.println("*** Unexpected non-error in test-case EXPIRED_TOKEN");
        }
        catch (SOAPFaultException ex) {
        	if (!ex.getFault().getFaultString().equals("A security error was encountered when verifying the message")) {
        		System.out.println("*** Unexpected error in test-case EXPIRED_TOKEN: " + ex.getFault().getFaultString());
        	}
        }
    }
    
    public static void hello(HelloWorldPortType port, String name) {
        String resp = port.helloWorld(name);

        System.out.println(resp);
    }
}
