package dk.sds.sts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class Application {
	
	public static void main(String[] args) {
		System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");

		SpringApplication.run(Application.class, args);
	}
}
