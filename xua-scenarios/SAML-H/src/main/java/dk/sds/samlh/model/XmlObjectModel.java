package dk.sds.samlh.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public abstract class XmlObjectModel {
	public abstract String generate(Validate validate) throws ValidationException, JAXBException;

	public Element generateElement(Validate validate) throws ValidationException, JAXBException, SAXException, IOException, ParserConfigurationException {
		String res = generate(validate);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8")));

		DocumentBuilderFactory b = DocumentBuilderFactory.newInstance();
		b.setNamespaceAware(true);
		DocumentBuilder db = b.newDocumentBuilder();

		Document doc = db.parse(bais);
		
		return doc.getDocumentElement();
	}
}
