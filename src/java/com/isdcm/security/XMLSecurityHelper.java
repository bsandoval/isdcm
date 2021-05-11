/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.security;

import java.io.File;
import java.io.FileOutputStream;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.BasicConfigurator;

import org.w3c.dom.Document;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;

/**
 *
 * @author fiblabs
 */
public class XMLSecurityHelper {
    
    static org.slf4j.Logger log = 
        org.slf4j.LoggerFactory.getLogger(
            XMLSecurityHelper.class.getName());
    
    static {
        BasicConfigurator.configure();
        org.apache.xml.security.Init.init();
    }
    
    /**
     * Encrypts a xml file given a path and key. 
     * Writes the encrypted data in: build/encryptedInfo.xml
     * @param inputPath
     * @param outputPath
     * @param key
     * @throws XMLEncryptionException
     * @throws Exception 
     */
    public static void encrypt(String inputPath, String outputPath, String key) throws XMLEncryptionException, Exception {
        Document document = loadDocument(inputPath);
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        SecretKey symmetricKey = new SecretKeySpec(key.getBytes(), "AES");
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);
        boolean encryptContentsOnly = false;
        xmlCipher.doFinal(document, document.getDocumentElement(), encryptContentsOnly);
        //outputDocToFile(document, "build/encryptedInfo.xml");
        outputDocToFile(document, outputPath);
    }
    
    /**
     * Decrypt a xml file given a path and key. 
     * Writes the decrypted data in: build/decryptedInfo.xml
     * @param inputPath
     * @param outputPath
     * @param key
     * @throws XMLEncryptionException
     * @throws Exception 
     */
    public static void decrypt(String inputPath, String outputPath, String key) throws XMLEncryptionException, Exception {
        Document node = loadDocument(inputPath);
        XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        SecretKey symmetricKey = new SecretKeySpec(key.getBytes(), "AES");
        xmlCipher.init(XMLCipher.DECRYPT_MODE, symmetricKey);
        Document document = xmlCipher.doFinal(node, node.getDocumentElement());
        outputDocToFile(document, outputPath);
    }
    
    /**
     * Loads a document given a path
     * @param filePath
     * @return
     * @throws Exception 
     */
    public static Document loadDocument(String filePath) throws Exception {
        File encryptionFile = new File(filePath);
        javax.xml.parsers.DocumentBuilderFactory dbf =
            javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(encryptionFile);
        System.out.println(
            "Document loaded from " + encryptionFile.toURI().toURL().toString()
        );
        return document;
    }
    
    /**
     * Writes the output document file
     * @param doc
     * @param fileName
     * @throws Exception 
     */
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
