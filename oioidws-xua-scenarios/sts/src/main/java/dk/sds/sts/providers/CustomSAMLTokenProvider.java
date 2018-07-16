package dk.sds.sts.providers;

import java.util.List;

import org.apache.cxf.sts.service.EncryptionProperties;
import org.apache.cxf.sts.token.provider.DefaultSubjectProvider;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.provider.TokenProviderParameters;
import org.apache.cxf.sts.token.provider.TokenProviderResponse;
import org.apache.cxf.ws.security.sts.provider.STSException;
import org.apache.log4j.Logger;

import dk.sds.sts.dao.WebServiceConsumerDao;
import dk.sds.sts.dao.model.WebServiceConsumer;
import dk.sds.sts.dao.model.WebServiceProvider;

public class CustomSAMLTokenProvider extends SAMLTokenProvider {
	private static final Logger logger = Logger.getLogger(CustomSAMLTokenProvider.class);
	private WebServiceConsumerDao wscDao;
	private boolean encryptionEnabled;

	public CustomSAMLTokenProvider(WebServiceConsumerDao wscDao, String entityId, boolean encryptionEnabled) {
		this.wscDao = wscDao;
		this.encryptionEnabled = encryptionEnabled;
		
		((DefaultSubjectProvider) this.getSubjectProvider()).setSubjectNameQualifier(entityId);
		
		setSubjectProvider(new CustomSubjectProvider());
	}
	
	@Override
	public TokenProviderResponse createToken(TokenProviderParameters tokenParameters) {
		String audience = tokenParameters.getAppliesToAddress();
		String principal = tokenParameters.getPrincipal().toString();

		WebServiceConsumer client = wscDao.getBySubject(principal);
		if (client == null) {
			String error = "Client certificate is not trusted: " + principal;

			logger.warn(error);
            throw new STSException(error, STSException.REQUEST_FAILED);
		}
		
		// verify that the given principal is allowed to access the given audience
		boolean isAllowed = false;
		List<WebServiceProvider> wsps = client.getWebServiceProviders();
		
		WebServiceProvider provider = null;
		for (WebServiceProvider wsp : wsps) {
			if (wsp.getEntityId().equals(audience)) {
				provider = wsp;
				isAllowed = true;
			}
		}
		
		if (isAllowed == false) {
			String error = "WSC not authorized to call WSP (" + audience + ")";

			logger.error(error);
            throw new STSException(error, STSException.REQUEST_FAILED);
		}

		if (encryptionEnabled) {			
			try {
				tokenParameters.setEncryptToken(true);
				EncryptionProperties encryptionProperties = new EncryptionProperties();
				encryptionProperties.setEncryptionName(provider.getSubject());
				tokenParameters.setEncryptionProperties(encryptionProperties);
				tokenParameters.getStsProperties().setEncryptionCrypto(new CustomMerlin(null, this.getClass().getClassLoader(), null));
			}
			catch (Exception ex) {
				throw new STSException("Failed to create a token", ex);
			}
		}

		return super.createToken(tokenParameters);
	}
}
