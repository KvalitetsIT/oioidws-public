package dk.sds.samlh.encryption;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.namespace.QName;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dk.sds.samlh.model.ModelUtil;

public class SAMLHEncrypter {
	public static final String NAME_ATTRIBUTE = "Name";
	public static final String ENCRYPTED_PREFIX = "dk:healthcare:ENCRYPTED:";

	static {
        org.apache.xml.security.Init.init();
    }

    public static void encrypt(QName elementQName, Document document, PublicKey key, boolean fixAttributeName) throws Exception {    	
		encrypt(ModelUtil.getElement(document, elementQName), key, fixAttributeName);
    }
    
    public static void decrypt(QName elementQName, Document document, PrivateKey key, boolean fixAttributeName) throws Exception {
		decrypt(ModelUtil.getElement(document, elementQName), key, fixAttributeName);
    }

	public static void encryptContent(Element element, PublicKey key, boolean fixAttributeName) throws Exception {
 		if (fixAttributeName) {
 			addEncryptedPrefix(element, 1);
 		}

 		XMLCipher xmlCipher = initCipher(key, element.getOwnerDocument());
        xmlCipher.doFinal(element.getOwnerDocument(), element, true);
	}

    public static void encrypt(Element element, PublicKey key, boolean fixAttributeName) throws Exception {
		if (fixAttributeName) {
			addEncryptedPrefix(element, 2);
		}

 		XMLCipher xmlCipher = initCipher(key, element.getOwnerDocument());
        xmlCipher.doFinal(element.getOwnerDocument(), element);
    }

    public static void decrypt(Element element, PrivateKey key, boolean fixAttributeName) throws Exception {
		if (fixAttributeName) {
			removeEncryptedPrefix(element, 2);
		}

        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(XMLCipher.DECRYPT_MODE, null);
        xmlCipher.setKEK(key);
        xmlCipher.doFinal(element.getOwnerDocument(), element);
    }

    private static void removeEncryptedPrefix(Element element, int parentLevel) throws Exception {
    	Element parent = element;
    	while (parentLevel > 0) {
    		parentLevel--;
    		if (parent.getParentNode() == null || !(parent.getParentNode() instanceof Element)) {
    			throw new Exception("Parent node is null or not an element!");    			
    		}
    		
    		parent = (Element) parent.getParentNode();
    	}
	
		String attributeValue = parent.getAttribute(NAME_ATTRIBUTE);
		if (attributeValue.startsWith(ENCRYPTED_PREFIX)) {
			attributeValue = attributeValue.substring(ENCRYPTED_PREFIX.length());
			parent.setAttribute(NAME_ATTRIBUTE, attributeValue);
		}
    }

    private static void addEncryptedPrefix(Element element, int parentLevel) throws Exception {
    	Element parent = element;
    	while (parentLevel > 0) {
    		parentLevel--;
    		if (parent.getParentNode() == null || !(parent.getParentNode() instanceof Element)) {
    			throw new Exception("Parent node is null or not an element!");    			
    		}
    		
    		parent = (Element) parent.getParentNode();
    	}
    	
		String attributeValue = parent.getAttribute(NAME_ATTRIBUTE);
		parent.setAttribute(NAME_ATTRIBUTE, ENCRYPTED_PREFIX + attributeValue);
    }
    
    private static XMLCipher initCipher(PublicKey key, Document doc) throws Exception {
        Key symmetricKey = generateDataEncryptionKey();
        Key keyEncryptingKey = key;

        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_OAEP);
        keyCipher.init(XMLCipher.WRAP_MODE, keyEncryptingKey);
        EncryptedKey encryptedKey = keyCipher.encryptKey(doc, symmetricKey);

        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128_GCM);
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);

        EncryptedData encryptedData = xmlCipher.getEncryptedData();
        KeyInfo keyInfo = new KeyInfo(doc);
        keyInfo.add(encryptedKey);
        encryptedData.setKeyInfo(keyInfo);

        return xmlCipher;
    }

    private static SecretKey generateDataEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);

        return keyGenerator.generateKey();
    }
}