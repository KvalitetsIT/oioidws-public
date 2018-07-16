package dk.sds.sts.providers;

import java.security.Principal;

import org.apache.cxf.sts.request.TokenRequirements;
import org.apache.cxf.sts.token.provider.DefaultSubjectProvider;
import org.apache.cxf.sts.token.provider.SubjectProviderParameters;
import org.apache.cxf.sts.token.provider.TokenProviderParameters;
import org.apache.cxf.ws.security.sts.provider.STSException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.common.saml.bean.SubjectBean;
import org.apache.wss4j.common.saml.builder.SAML2Constants;
import org.w3c.dom.Element;

public class CustomSubjectProvider extends DefaultSubjectProvider {

	@Override
	protected SubjectBean createSubjectBean(Principal principal, SubjectProviderParameters subjectProviderParameters) {
		TokenProviderParameters providerParameters = subjectProviderParameters.getProviderParameters();
		TokenRequirements tokenRequirements = providerParameters.getTokenRequirements();

		// check for ActAs case
		try {
			if (tokenRequirements != null && tokenRequirements.getActAs() != null) {
				SamlAssertionWrapper actAsToken = new SamlAssertionWrapper((Element) providerParameters.getTokenRequirements().getActAs().getToken());

				String tokenType = tokenRequirements.getTokenType();
				String keyType = providerParameters.getKeyRequirements().getKeyType();
				String confirmationMethod = getSubjectConfirmationMethod(tokenType, keyType);

				CustomSubjectBean subjectBean = new CustomSubjectBean(actAsToken.getSubjectName(), null /*"http://cxf.apache.org/sts"*/, confirmationMethod);
				subjectBean.setSubjectNameIDFormat(SAML2Constants.NAMEID_FORMAT_X509_SUBJECT_NAME);

				subjectBean.setSubjectConfirmationNameIDFormat(SAML2Constants.NAMEID_FORMAT_X509_SUBJECT_NAME);
				subjectBean.setSubjectConfirmationNameIDValue(principal.getName());
	
				return subjectBean;
			}
		}
		catch (Exception ex) {
			throw new STSException("Failed top parse ActAs token", ex);
		}
		
		// default to standard behavior if not ActAs case
		return super.createSubjectBean(principal, subjectProviderParameters);
	}
}
