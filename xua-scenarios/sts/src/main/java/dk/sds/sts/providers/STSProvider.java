package dk.sds.sts.providers;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.ws.WebServiceProvider;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.apache.cxf.sts.StaticSTSProperties;
import org.apache.cxf.sts.operation.TokenIssueOperation;
import org.apache.cxf.sts.service.ServiceMBean;
import org.apache.cxf.sts.service.StaticService;
import org.apache.cxf.sts.token.delegation.HOKDelegationHandler;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.validator.SAMLTokenValidator;
import org.apache.cxf.ws.security.sts.provider.SecurityTokenServiceProvider;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dk.sds.sts.callback.PasswordCallbackHandler;
import dk.sds.sts.dao.WebServiceProviderDao;

@WebServiceProvider(serviceName = "SecurityTokenService",
   portName = "STS_Port",
   targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/",
   wsdlLocation = "/sts.wsdl")
@EndpointProperties(value = {
   @EndpointProperty(key = "ws-security.signature.properties", value = "sts.properties"),
   @EndpointProperty(key = "ws-security.callback-handler", value = "dk.sds.sts.callback.PasswordCallbackHandler"),
   @EndpointProperty(key = "ws-security.asymmetric.signature.algorithm", value = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256")
})
@Component
public class STSProvider extends SecurityTokenServiceProvider {

	@Value("${sts.entityid}")
	private String stsEntityId;
	
	@Autowired
	private WebServiceProviderDao wspDao;
	
	@Autowired
	private SAMLTokenProvider samlTokenProvider;
	
	public STSProvider() throws Exception {
		super();
	}
	
	@PostConstruct
	public void init() throws InitializationException {
		StaticSTSProperties props = new StaticSTSProperties();
		props.setSignatureCryptoProperties("sts.properties");
		props.setCallbackHandlerClass(PasswordCallbackHandler.class.getCanonicalName());
		props.setIssuer(stsEntityId);

		List<String> endpoints = new ArrayList<>();
		for (dk.sds.sts.dao.model.WebServiceProvider wsp : wspDao.findAll()) {
			endpoints.add(wsp.getEntityId());
		}

		StaticService service = new StaticService();
		service.setEndpoints(endpoints);

		List<ServiceMBean> services = new ArrayList<ServiceMBean>();
		services.add(service);

		TokenIssueOperation issueOperation = new TokenIssueOperation();
		issueOperation.getTokenProviders().add(samlTokenProvider);
		issueOperation.setServices(services);
		issueOperation.setStsProperties(props);
		issueOperation.getTokenValidators().add(new SAMLTokenValidator());
		issueOperation.getDelegationHandlers().add(new HOKDelegationHandler());
		issueOperation.getClaimsManager().setClaimHandlers(Collections.singletonList(new AuthClaimsHandler()));
		issueOperation.getClaimsManager().setClaimParsers(Collections.singletonList(new AuthClaimsParser()));
		
		this.setIssueOperation(issueOperation);

		// initialize OpenSAML XMLObjectProvider
		InitializationService.initialize();
	}
}