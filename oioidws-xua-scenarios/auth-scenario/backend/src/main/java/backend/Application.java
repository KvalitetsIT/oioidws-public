package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {		
    	// This is a hack to support the self-signed SSL certificate used on the WSP
    	// in a real production setting, the service would be protected by a trusted SSL certificate
    	// and setting a custom truststore like this would not be needed
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
		System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
		
		SpringApplication.run(Application.class, args);
	}
}
