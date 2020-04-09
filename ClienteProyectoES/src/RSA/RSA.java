package RSA; 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
    

public class RSA {
    
    public static void generarClaves(){
        try {
            // Get an instance of the RSA key generator
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            // Generate the keys — might take sometime on slow computers
            KeyPair myPair = kpg.generateKeyPair();
            
            byte[] publickey = myPair.getPublic().getEncoded();
            FileOutputStream pubkeyfos = new FileOutputStream("public.key");
            pubkeyfos.write(publickey);
            pubkeyfos.close();
            
            byte[] privatekey = myPair.getPrivate().getEncoded();
            FileOutputStream privkeyfos = new FileOutputStream("private.key");
            privkeyfos.write(privatekey);
            privkeyfos.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
     
   public static String encrypt(String textoPlano) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException, InvalidKeySpecException
    {
        //get the public key
        //PublicKey pk=pair.getPublic(); 

        FileInputStream keyfis = new FileInputStream("public.key");
        byte[] encKey = new byte[keyfis.available()];  
        keyfis.read(encKey);
        keyfis.close();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pk = keyFactory.generatePublic(pubKeySpec);

        //Initialize the cipher for encryption. Use the public key.
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, pk);

        //Perform the encryption using doFinal
        byte[] encByte = rsaCipher.doFinal(textoPlano.getBytes());

        // converts to base64 for easier display.
        byte[] base64Cipher = Base64.getEncoder().encode(encByte);

        return new String(base64Cipher);
    }//end encrypt

    public static String decrypt(String criptograma) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, InvalidKeySpecException
    {
        //get the public key
        //PrivateKey pvk=pair.getPrivate(); 

        FileInputStream keyfis = new FileInputStream("private.key");
        byte[] encKey = new byte[keyfis.available()];  
        keyfis.read(encKey);
        keyfis.close();
        PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey pvk = keyFactory.generatePrivate(pubKeySpec);
        //Create a Cipher object
        //Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");

        //Initialize the cipher for encryption. Use the public key.
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, pvk);

        //Perform the encryption using doFinal
        byte[] decByte = rsaCipher.doFinal(Base64.getDecoder().decode(criptograma));

        return new String(decByte);
        
        

    }//end decrypt
     public static void main(String args[]) {
         //PARA HACER PRUEBAS
        try {
            generarClaves();
            String cifrado = encrypt("HOLI");
            System.out.println(cifrado);
            System.out.println(decrypt(cifrado));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}