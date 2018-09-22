package backend.controller;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.security.TokenHolder;
import backend.sts.XUASTSClient;

@Controller
public class HelloController {

	@Autowired
	private XUASTSClient stsClient;
	
	@ResponseBody
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public ResponseEntity<HelloResponse> hello(@RequestBody HelloRequest request) throws Exception {
		HelloWorldService service = new HelloWorldService();
		HelloWorldPortType port = service.getHelloWorldPort();

		TokenHolder.setToken(new String(Base64.decodeBase64(request.getToken()), Charset.forName("UTF-8")));

		// perform a call to the STS to exchange the self-signed token into a "bootstrap"-token
		SecurityToken securityToken = stsClient.requestSecurityToken("http://localhost:8080/service/hello");
		TokenHolder.setToken(securityToken.getToken());

		// then perform a webservice call, which implicitly performs an ActAs call to the STS to get a token for this endpoint
		String serviceResponse = port.helloWorld(request.getName());
		HelloResponse response = new HelloResponse();
		response.setResponse(serviceResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
