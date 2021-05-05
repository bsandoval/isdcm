/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.security;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;

/**
 *
 * @author fiblabs
 */
public class XMLSecurityHelper {
    
    static {
        org.apache.xml.security.Init.init();
    }
    
    public static void encrypt(Document document, String key) throws XMLEncryptionException, Exception {
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        SecretKey symmetricKey = new SecretKeySpec(key.getBytes(), "AES");
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);
        boolean encryptContentsOnly = false;
        xmlCipher.doFinal(document, document.getDocumentElement(), encryptContentsOnly);
        outputDocToFile(document, "build/encryptedInfo.xml");
    }
    
    public static void decrypt(Document node, String key) throws XMLEncryptionException, Exception {
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        SecretKey symmetricKey = new SecretKeySpec(key.getBytes(), "AES");
        xmlCipher.init(XMLCipher.DECRYPT_MODE, symmetricKey);
        Document document = xmlCipher.doFinal(node, node.getDocumentElement());
        outputDocToFile(document, "build/decryptedInfo.xml");
    }
    
    private static void outputDocToFile(Document doc, String fileName) throws Exception {
        File encryptionFile = new File(fileName);
        FileOutputStream f = new FileOutputStream(encryptionFile);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(f);
        transformer.transform(source, result);
        f.close();
        System.out.println(
            "Wrote document containing encrypted/decrypted data to " + encryptionFile.toURI().toURL().toString()
        );
    }
}
