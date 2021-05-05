/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.security;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author fiblabs
 */
public class XMLEncryptionMainTest {
    private static final String SECRET_KEY = "31d9bd527f0861c1355459929e11b90b";
    
    public static void main(String args[]) throws Exception {
        String xmlPath = "web/WEB-INF/xml/didlFilm1.xml";
        Document xmlDocument = loadDocument(xmlPath);
        XMLSecurityHelper.encrypt(xmlDocument, SECRET_KEY); //encrypted into "build/encryptedInfo.xml"
        
        String encryptedPath = "build/encryptedInfo.xml";
        Document encryptedDocument = loadDocument(encryptedPath);
        System.out.println("Encrypted document: ");
        printDocument(encryptedDocument);
        XMLSecurityHelper.decrypt(xmlDocument, SECRET_KEY); //encrypted into "build/decryptedInfo.xml"
        
        String decryptedPath = "build/decryptedInfo.xml";
        Document decryptedDocument = loadDocument(decryptedPath);
        System.out.println("Decrypted document: ");
        printDocument(decryptedDocument);
    }
    
    private static Document loadDocument(String fileName) throws Exception {
        File encryptionFile = new File(fileName);
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
    
    private static void printDocument(Document doc) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();
        System.out.println(xmlString);
    }
}
