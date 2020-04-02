/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoes;

import AES.AES;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author Carlos
 */
public class Seguridad {
    
    public Seguridad(){
        
    }
     
    public static String getSHA512(String input){

	String toReturn = null;
	try {
	    MessageDigest digest = MessageDigest.getInstance("SHA-512");
	    digest.reset();
	    digest.update(input.getBytes("utf8"));
	    toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return toReturn;
    }
    
    public static File cifrarFicheroAES(File ficheroSinCifrar, String nombreFichero, String key){
        File ficheroCifrado = new File(nombreFichero);
        AES    en      = new AES(key);
        try {
            /*
            * write out encrypted file
            */
            en.WriteEncryptedFile(ficheroSinCifrar,ficheroCifrado);
            return ficheroCifrado;
        } catch (IOException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static File descifrarFicheroAES(File ficheroCifrado, String nombreFicheroDescifrado, String key){
        File ficheroDescifrado = new File(nombreFicheroDescifrado);
        AES    en      = new AES(key);
        try {
            /*
            * write out encrypted file
            */
            en.ReadEncryptedFile(ficheroCifrado,ficheroDescifrado);
            return ficheroDescifrado;
        } catch (IOException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
