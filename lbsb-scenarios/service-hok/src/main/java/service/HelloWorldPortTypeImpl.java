package service;

import javax.jws.WebService;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.MissingName;

import service.bpp.PrivilegeGroupType;
import service.bpp.PrivilegeListType;
import service.saml.AssertionHolder;

@WebService(targetNamespace = "http://www.example.org/contract/HelloWorld",
			portName = "HelloWorldPort",
			serviceName = "HelloWorldService",
			endpointInterface = "org.example.contract.helloworld.HelloWorldPortType")
@EndpointProperties(value = {
		@EndpointProperty(key = "ws-security.asymmetric.signature.algorithm", value = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256")
	})
public class HelloWorldPortTypeImpl implements HelloWorldPortType {

	@Override
	public String helloWorld(String name) throws MissingName {
		if (name == null || name.length() == 0) {
			throw new MissingName("No name supplied");
		}
		
		// get the Privileges from the presented token
		PrivilegeListType privilegeListType = AssertionHolder.get();

		// print the privileges
		if (privilegeListType != null) {
			for (PrivilegeGroupType privilegeGroup : privilegeListType.getPrivilegeGroup()) {
				System.out.println("scope: " + privilegeGroup.getScope());

				for (String privilegeString : privilegeGroup.getPrivilege()) {
					System.out.println("privilege: " + privilegeString);
				}
			}
		}

		return "Hello " + name;
	}
}
