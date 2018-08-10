package dk.sds.sts.config;

import java.util.ArrayList;

import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.sts.token.provider.AttributeStatementProvider;
import org.apache.cxf.sts.token.provider.DefaultConditionsProvider;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dk.sds.sts.dao.WebServiceConsumerDao;
import dk.sds.sts.providers.CustomSAMLTokenProvider;
import dk.sds.sts.providers.STSProvider;
import dk.sds.sts.test.TestHeaderInterceptor;

@Configuration
public class STSConfig {

	@Autowired
	private ApplicationContext applicationContext;
		
	@Value(value="${sts.timetolive}")
	private long timeToLive;
	
	@Value("${sts.entityid}")
	private String entityId;
	
	@Value("${sts.debug}")
	private boolean debugEnabled;
	
	@Value("${sts.testheader}")
	private boolean testHeaderEnabled;
	
	@Bean
	public SAMLTokenProvider samlTokenProvider(AttributeStatementProvider provider, WebServiceConsumerDao wscDao) {
		ArrayList<AttributeStatementProvider> attributeStatementProviders = new ArrayList<>();
		attributeStatementProviders.add(provider);
		
		SAMLTokenProvider samlTokenProvider = new CustomSAMLTokenProvider(wscDao, entityId);
		samlTokenProvider.setAttributeStatementProviders(attributeStatementProviders);
		((DefaultConditionsProvider) samlTokenProvider.getConditionsProvider()).setLifetime(timeToLive);

		return samlTokenProvider;
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new CXFServlet(), "/service/*");
	}

	@Bean
	public EndpointImpl stsService(STSProvider implementor) throws Exception {
		Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);

		EndpointImpl endpoint = new EndpointImpl(bus, implementor);
		endpoint.publish("/sts");

		if (debugEnabled) {
			endpoint.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
			endpoint.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		}
		
		if (testHeaderEnabled) {
			TestHeaderInterceptor interceptor = new TestHeaderInterceptor();
			endpoint.getServer().getEndpoint().getInInterceptors().add(interceptor);
		}

		return endpoint;
	}
}
