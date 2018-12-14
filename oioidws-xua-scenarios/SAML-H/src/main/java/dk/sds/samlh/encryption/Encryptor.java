package dk.sds.samlh.encryption;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.xml.namespace.QName;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Encryptor {
	private static final String ENCRYPTED_KEY_LOCALNAME = "EncryptedKey";
	private static final String CIPHER_DATA_LOCALNAME = "CipherData";
    private static final String XMLNS = "http://www.w3.org/2000/xmlns/";
    private static final String XMLENC_NS = "http://www.w3.org/2001/04/xmlenc#";
    private static final String XMLSIG_NS = "http://www.w3.org/2000/09/xmldsig#";
    private static final String DS_KEY_INFO = "ds:KeyInfo";

	public static EncryptedKey encryptKey(Document document, SecretKey keyToBeEncrypted, PublicKey keyUsedToEncryptSecretKey) throws XMLEncryptionException {
		XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_OAEP);
		keyCipher.init(XMLCipher.WRAP_MODE, keyUsedToEncryptSecretKey);

		return keyCipher.encryptKey(document, keyToBeEncrypted);
	}

	public static Element getElement(Document doc, QName elementQName) {
        NodeList nl = doc.getElementsByTagNameNS(elementQName.getNamespaceURI(), elementQName.getLocalPart());
        if (nl.getLength() == 0) {
            nl = doc.getElementsByTagNameNS("*", elementQName.getLocalPart());
            if (nl.getLength() == 0)
                nl = doc.getElementsByTagName(elementQName.getPrefix() + ":" + elementQName.getLocalPart());
            if (nl.getLength() == 0)
                return null;
        }
        return (Element) nl.item(0);
    }
	
	public static Element encryptElement(Document document, Element element, PublicKey publicKey, SecretKey secretKey, boolean encryptContentOnly) throws Exception {
        if (element == null || document == null) {
        	throw new Exception("element or document is null");
        }
 
        EncryptedKey encryptedKey = encryptKey(document, secretKey, publicKey);
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_256);
        cipher.init(XMLCipher.ENCRYPT_MODE, secretKey);
 
        Document encryptedDoc = cipher.doFinal(document, element, encryptContentOnly); 
        Element encryptedKeyElement = cipher.martial(document, encryptedKey);
 
        Element sigElement = encryptedDoc.createElementNS(XMLSIG_NS, DS_KEY_INFO);
        sigElement.setAttributeNS(XMLNS, "xmlns:ds", XMLSIG_NS);
        sigElement.appendChild(encryptedKeyElement);
 
        NodeList nodeList = encryptedDoc.getElementsByTagNameNS(XMLENC_NS, CIPHER_DATA_LOCALNAME);
        if (nodeList == null || nodeList.getLength() == 0) {
        	throw new Exception("nodeList null or empty");
        }

        Element cipherDataElement = (Element) nodeList.item(0);
        Node cipherParent = cipherDataElement.getParentNode();
        cipherParent.insertBefore(sigElement, cipherDataElement);
        
        return (Element) cipherParent;
    }
	
	public static Element decryptElement(Document document, Element element, PrivateKey privateKey) throws Exception { 
		NodeList nodeList = element.getElementsByTagNameNS(XMLENC_NS, ENCRYPTED_KEY_LOCALNAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new Exception("Encrypted Key not found");
		}

		Element encKeyElement = (Element) nodeList.item(0);
 
		XMLCipher cipher = XMLCipher.getInstance();
		cipher.init(XMLCipher.DECRYPT_MODE, null);
		EncryptedData encryptedData = cipher.loadEncryptedData(document, element);
		EncryptedKey encryptedKey = cipher.loadEncryptedKey(document, encKeyElement);
 
        Document decryptedDoc = null;
        if (encryptedData != null && encryptedKey != null) {
			String encAlgoURL = encryptedData.getEncryptionMethod().getAlgorithm();
			XMLCipher keyCipher = XMLCipher.getInstance();
			keyCipher.init(XMLCipher.UNWRAP_MODE, privateKey);
			Key encryptionKey = keyCipher.decryptKey(encryptedKey, encAlgoURL);
			cipher = XMLCipher.getInstance();
			cipher.init(XMLCipher.DECRYPT_MODE, encryptionKey);

			decryptedDoc = cipher.doFinal(document, element);
        }
 
        if (decryptedDoc == null) {
        	throw new Exception("Decryption failed");
        }
 
        /*
        Element decryptedRoot = decryptedDoc.getDocumentElement();
        Element dataElement = getNextElementNode(decryptedRoot.getFirstChild());
        if (dataElement == null)
            throw logger.nullValueError("Data Element after encryption is null");
 
        decryptedRoot.removeChild(dataElement);
        decryptedDoc.replaceChild(dataElement, decryptedRoot);
         */

        return decryptedDoc.getDocumentElement();
    }
}
