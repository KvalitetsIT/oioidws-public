package dk.sds.sts.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:sts.properties")
public class StaticKeystoreProperties {
	private static StaticKeystoreProperties instance;

	@Value(value = "${org.apache.ws.security.crypto.merlin.keystore.password}")
	private String password;

	public StaticKeystoreProperties() {
		StaticKeystoreProperties.instance = this;
	}

	public static String getPassword() {
		return instance.password;
	}
}
