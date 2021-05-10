/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author fiblabs
 */
public class VideoSecurityHelper {
    private static final String SECRET_KEY = "t0L64tGfVBU11zuhvM7ksA==";
    
    public static void encryptVideo() 
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\micky\\Documents\\NetBeansProjects\\isdcm\\web\\WEB-INF\\video/inputVideo.mp4"));
        File outfile = new File("C:\\Users\\micky\\Documents\\NetBeansProjects\\isdcm\\web\\WEB-INF\\video/encVideo.mp4");
        if(!outfile.exists()) outfile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outfile);
        
        Cipher encipher = Cipher.getInstance("AES");
        /*
        SecretKey symmetricKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        SecretKey skey = KeyGenerator.getInstance("AES").generateKey();
        
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecretKey skey = kgen.generateKey();
        
        System.out.println(skey);
        System.out.println("ENCODED: " + skey.getEncoded());
        System.out.println("FORMAT: " + skey.getFormat());
        byte[] raw = skey.getEncoded();
        System.out.println("RAW FORMAT: " + raw.toString());
        */
        
       
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        
        encipher.init(Cipher.ENCRYPT_MODE, originalKey);
        CipherInputStream cis = new CipherInputStream(fis, encipher);

        int read;            
        while((read = cis.read())!=-1){
            fos.write((char)read);
            fos.flush();
        }   
        fos.close();
    }
    
    public static void decryptVideo() 
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        File outfile = new File("C:\\Users\\micky\\Documents\\NetBeansProjects\\isdcm\\web\\WEB-INF\\video/inputVideo.mp4");
        File decfile = new File("C:\\Users\\micky\\Documents\\NetBeansProjects\\isdcm\\web\\WEB-INF\\video/decVideo.mp4");
        if(!decfile.exists()) decfile.createNewFile();
        FileInputStream encfis = new FileInputStream(outfile);
        FileOutputStream decfos = new FileOutputStream(decfile);
        
        Cipher decipher = Cipher.getInstance("AES");
        /*
        SecretKey symmetricKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        */
        
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        
        decipher.init(Cipher.DECRYPT_MODE, originalKey);
        
        CipherOutputStream cos = new CipherOutputStream(decfos,decipher);
        
        int read;
        while((read=encfis.read())!=-1)
        {
            cos.write(read);
            cos.flush();
        }
        cos.close(); 
    }    
}
