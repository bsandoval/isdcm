/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdcm.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author fiblabs
 */
public class VideoSecurityHelper {
    //private static final String SECRET_KEY = "t0L64tGfVBU11zuhvM7ksA==";
    
    public static void encryptVideo(String inputPath, String outputPath, String key) 
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        FileInputStream fis = new FileInputStream(new File(inputPath));
        File outfile = new File(outputPath);
        if(!outfile.exists()) outfile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(outfile)) {
            Cipher encipher = Cipher.getInstance("AES");
            
            // decode the base64 encoded string
            byte[] decodedKey = Base64.getDecoder().decode(key);
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            
            encipher.init(Cipher.ENCRYPT_MODE, originalKey);
            CipherInputStream cis = new CipherInputStream(fis, encipher);
            
            int read;
            while((read = cis.read())!=-1){
                fos.write((char)read);
                fos.flush();
            }
        }
    }
    
    public static void decryptVideo(String inputPath, String outputPath, String key) 
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        File outfile = new File(inputPath);
        File decfile = new File(outputPath);
        
        if(!decfile.exists()) decfile.createNewFile();
        FileInputStream encfis = new FileInputStream(outfile);
        FileOutputStream decfos = new FileOutputStream(decfile);
        
        Cipher decipher = Cipher.getInstance("AES");
        
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(key);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        
        decipher.init(Cipher.DECRYPT_MODE, originalKey);
        
        try (CipherOutputStream cos = new CipherOutputStream(decfos,decipher)) {
            int read;
            while((read=encfis.read())!=-1)
            {
                cos.write(read);
                cos.flush();
            }
        }
    }    
}
