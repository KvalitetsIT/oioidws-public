package backend.sts;

import java.nio.charset.Charset;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.Bus;
import org.apache.cxf.staxutils.W3CDOMStreamWriter;
import org.apache.cxf.ws.security.trust.STSClient;
import org.w3c.dom.Element;

import backend.security.TokenHolder;

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
	}	

	@Override
	public Element getActAsToken() throws Exception {
		return getDelegationSecurityToken(TokenHolder.getToken());
	}
}
