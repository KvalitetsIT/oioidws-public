package backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:cxf.xml" })
public class CxfConfigurationAccess {

}
