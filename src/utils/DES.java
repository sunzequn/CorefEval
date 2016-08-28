package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.sun.org.apache.regexp.internal.recompile;

//import cn.edu.nju.ws.sview.rdf.ResourceFactory;

public class DES {
	private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    /**
     * Construct a new object which can be utilized to encrypt
     * and decrypt strings using the specified key
     * with a DES encryption algorithm.
     *
     * @param key The secret key used in the crypto operations.
     * @throws Exception If an error occurs.
     *
     */
    public DES(String password) throws Exception {
    	DESKeySpec key = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey skey = keyFactory.generateSecret(key);
        encryptCipher = Cipher.getInstance("DES");
        decryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, skey);
        decryptCipher.init(Cipher.DECRYPT_MODE, skey);
    }
//    public static DES getInstance() throws Exception{
//    	String password = "this is a des key for sview system";
//		DES des = new DES(password);
//		return des;
//    }

    /**
     * Encrypt a string using DES encryption, and return the encrypted
     * string as a base64 encoded string.
     * @param unencryptedString The string to encrypt.
     * @return String The DES encrypted and base 64 encoded string.
     * @throws Exception If an error occurs.
     */
    public String encryptBase64 (String unencryptedString) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] unencryptedByteArray = unencryptedString.getBytes("UTF-8");

        // Encrypt
        byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);

        // Encode bytes to base64 to get a string
        byte [] encodedBytes = Base64.encodeBase64(encryptedBytes);

        return new String(encodedBytes);
    }

    /**
     * Decrypt a base64 encoded, DES encrypted string and return
     * the unencrypted string.
     * @param encryptedString The base64 encoded string to decrypt.
     * @return String The decrypted string.
     * @throws Exception If an error occurs.
     */
    public String decryptBase64 (String encryptedString) throws Exception {
        // Encode bytes to base64 to get a string
        byte [] decodedBytes = Base64.decodeBase64(encryptedString.getBytes());

        // Decrypt
        byte[] unencryptedByteArray = decryptCipher.doFinal(decodedBytes);

        // Decode using utf-8
        return new String(unencryptedByteArray, "UTF-8");
    } 
    
    public static String getDES(String uri) throws Exception{
    	uri = URLDecoder.decode(uri, "UTF-8");  
    	String default_des_key="this is a des key for sview system.";
    	String etype="";
    	DES des = new DES(default_des_key);
    	String encrypted = des.encryptBase64(etype + uri);
		encrypted = URLEncoder.encode(URLEncoder.encode(encrypted, "utf-8"), "utf-8");
		return encrypted;
//		String href = "http://ws.nju.edu.cn/sview/views/eview.jsp?lang=en&query=" + encrypted;
//    	return href;
    }
    
    
    public static void main(String[] args) throws Exception{
    	String uri="http%3A%2F%2Fdbpedia.org%2Fresource%2FSemantic_Web";
		System.out.print(getDES(uri));
    }
}
