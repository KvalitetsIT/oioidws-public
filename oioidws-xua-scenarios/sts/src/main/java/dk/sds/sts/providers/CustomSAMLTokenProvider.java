package dk.sds.sts.providers;

import java.util.List;

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

	public CustomSAMLTokenProvider(WebServiceConsumerDao wscDao, String entityId) {
		this.wscDao = wscDao;
		
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
		
		for (WebServiceProvider wsp : wsps) {
			if (wsp.getEntityId().equals(audience)) {
				isAllowed = true;
			}
		}
		
		if (isAllowed == false) {
			String error = "WSC not authorized to call WSP (" + audience + ")";

			logger.error(error);
            throw new STSException(error, STSException.REQUEST_FAILED);
		}

		return super.createToken(tokenParameters);
	}
}
