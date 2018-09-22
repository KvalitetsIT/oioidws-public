package service;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.example.contract.helloworld.HelloWorldPortType;

import dk.sds.samlh.model.Validate;
import dk.sds.samlh.model.oiobpp.PrivilegeGroup;
import dk.sds.samlh.model.oiobpp.PrivilegeList;
import dk.sds.samlh.model.userauthorization.UserAuthorizationList;
import service.saml.AssertionHolder;
import service.saml.UserAuthorizationHolder;

@WebService(targetNamespace = "http://www.example.org/contract/HelloWorld",
			portName = "HelloWorldPort",
			serviceName = "HelloWorldService",
			endpointInterface = "org.example.contract.helloworld.HelloWorldPortType")
@EndpointProperties(value = {
		@EndpointProperty(key = "ws-security.asymmetric.signature.algorithm", value = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256")
})
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class HelloWorldPortTypeImpl implements HelloWorldPortType {

	static {
		System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
	}

	@Override
	public String helloWorld(String name) {
		UserAuthorizationList auth = UserAuthorizationHolder.get();
		try {
			System.out.println(auth.generate(Validate.YES));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// get the Privileges from the presented token
		PrivilegeList privilegeList = AssertionHolder.get();

		// print the privileges
		if (privilegeList != null) {
			for (PrivilegeGroup privilegeGroup : privilegeList.getPrivilegeGroups()) {
				System.out.println("scopeAuthorizationCode: " + privilegeGroup.getScopeAuthorizationCode());
				System.out.println("scopeEducationCode: " + privilegeGroup.getScopeEducationCode());

				for (String privilegeString : privilegeGroup.getPrivileges()) {
					System.out.println("privilege: " + privilegeString);
				}
			}
		}

		return "Hello " + name;
	}
}
