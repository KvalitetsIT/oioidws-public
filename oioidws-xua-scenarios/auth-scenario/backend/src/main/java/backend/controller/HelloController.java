package backend.controller;

import java.nio.charset.Charset;
import java.util.Base64;

import org.example.contract.helloworld.HelloWorldPortType;
import org.example.contract.helloworld.HelloWorldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.security.TokenHolder;

@Controller
public class HelloController {

	@ResponseBody
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public ResponseEntity<HelloResponse> hello(@RequestBody HelloRequest request) {
		HelloWorldService service = new HelloWorldService();
		HelloWorldPortType port = service.getHelloWorldPort();

		TokenHolder.setToken(new String(Base64.getDecoder().decode(request.getToken()), Charset.forName("UTF-8")));

		String serviceResponse = port.helloWorld(request.getName());
		HelloResponse response = new HelloResponse();
		response.setResponse(serviceResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
