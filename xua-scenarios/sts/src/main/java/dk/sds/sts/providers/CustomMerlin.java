package dk.sds.sts.providers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Properties;

import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.common.crypto.PasswordEncryptor;
import org.apache.wss4j.common.ext.WSSecurityException;

import dk.sds.sts.dao.WebServiceConsumerDao;
import dk.sds.sts.dao.WebServiceProviderDao;
import dk.sds.sts.dao.model.WebServiceProvider;
import dk.sds.sts.util.SpringContext;

public class CustomMerlin extends Merlin {

	public CustomMerlin() {
		super();
	}

	public CustomMerlin(boolean loadCACerts, String cacertsPasswd) {
		super(loadCACerts, cacertsPasswd);
	}

	public CustomMerlin(Properties properties, ClassLoader loader, PasswordEncryptor passwordEncryptor) throws WSSecurityException, IOException {
		super(properties, loader, passwordEncryptor);
	}
	
	@Override
	public void loadProperties(Properties properties, ClassLoader loader, PasswordEncryptor passwordEncryptor) throws WSSecurityException, IOException {
		super.loadProperties(properties, loader, passwordEncryptor);

		// TODO: trust to the IdP should be loaded in some other manner
		// TODO: trust to the MOCES certificates should be loaded in some other manner

		WebServiceConsumerDao wscDao = (WebServiceConsumerDao) SpringContext.getApplicationContext().getBean(WebServiceConsumerDao.class);
		WebServiceProviderDao wspDao = (WebServiceProviderDao) SpringContext.getApplicationContext().getBean(WebServiceProviderDao.class);

		this.loadCACerts = false;
		this.truststore = null;

		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);

			for (dk.sds.sts.dao.model.WebServiceConsumer wsc : wscDao.findAll()) {
				Certificate cert = certificateFactory.generateCertificate(new ByteArrayInputStream(wsc.getCertificate()));

				ks.setCertificateEntry(wsc.getSubject(), cert);
			}

			for (WebServiceProvider wsp : wspDao.findAll()) {
				Certificate cert = certificateFactory.generateCertificate(new ByteArrayInputStream(wsp.getCertificate()));

				ks.setCertificateEntry(wsp.getSubject(), cert);				
			}

			this.truststore = ks;
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}
}
