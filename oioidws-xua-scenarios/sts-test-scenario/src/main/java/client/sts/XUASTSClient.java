package client.sts;

import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.Bus;
import org.apache.cxf.message.Message;
import org.apache.cxf.staxutils.W3CDOMStreamWriter;
import org.apache.cxf.ws.security.trust.STSClient;

import client.WSClient;
import client.WSClient.TESTCASE;

public class XUASTSClient extends STSClient {

	public XUASTSClient(Bus b) {
		super(b);
	}

	@Override
	protected void writeElementsForRSTPublicKey(W3CDOMStreamWriter writer, X509Certificate cert) throws Exception {
		writer.writeStartElement("wst", "UseKey", namespace);
		writer.writeStartElement("wsse", "BinarySecurityToken", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		writer.writeAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
		writer.writeAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
		writer.getCurrentNode().setTextContent(new String(Base64.encodeBase64(cert.getEncoded()), Charset.forName("UTF-8")));

		writer.writeEndElement();
		writer.writeEndElement();

		if (WSClient.testcase.equals(TESTCASE.BAD_CERTIFICATE)) {
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("testheader", Arrays.asList(TESTCASE.BAD_CERTIFICATE.toString()));
			client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		}
		else if (WSClient.testcase.equals(TESTCASE.BAD_TOKENTYPE)) {
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("testheader", Arrays.asList(TESTCASE.BAD_TOKENTYPE.toString()));
			client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		}
		else if (WSClient.testcase.equals(TESTCASE.EXPIRED_TOKEN)) {
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("testheader", Arrays.asList(TESTCASE.EXPIRED_TOKEN.toString()));
			client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		}
		else if (WSClient.testcase.equals(TESTCASE.SERVER_ERROR)) {
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("testheader", Arrays.asList(TESTCASE.SERVER_ERROR.toString()));
			client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		}
	}
}
