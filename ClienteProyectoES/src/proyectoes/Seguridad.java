/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoes;

import AES.AES;
import RSA.RSA;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import static proyectoes.Login.IP;
import static proyectoes.Login.PORT;
import static proyectoes.MenuUsuario.USUARIO;
import static proyectoes.MenuUsuario.USER_AES_KEY;
import static proyectoes.MenuUsuario.USUARIO;

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
    
    public static void generarClavesRSA(){
        RSA rsa = new RSA();
        rsa.generarClaves();
    }
    
    
    public static void cifrarClavePrivadaRSA(String usuario, String AES_KEY){
        File privateKey = new File("private.key"); 
        cifrarFicheroAES(privateKey, "privateKey_" + usuario, AES_KEY);
        privateKey.delete();//Eliminamos la clave privada
    }
    
    public static void descifrarClavePrivadaRSA(String usuario, String AES_KEY){
        File privateKey = new File("privateKey_" + usuario); 
        descifrarFicheroAES(privateKey, "private.key", AES_KEY);
        privateKey.delete();
    }
    
    public static void descargarClavesRSA(String usuario, String AES_KEY){
        descargarClavePublicaRSA(MenuUsuario.USER_ID);
        descargarClavePrivadaRSA(MenuUsuario.USER_ID, AES_KEY, "private.key");
    }
    
    public static String cifrarConRSA(String AES_KEY){
        try{
            return RSA.encrypt(AES_KEY);
        }catch(Exception ex){
            System.out.println(ex.toString());
            return "";
        }
    }
    
    public static String descifrarConRSA(String criptograma){
        try{
            return RSA.decrypt(criptograma);
        }catch(Exception ex){
            System.out.println(ex.toString());
            return "";
        }
    }
    
    public static void descargarClavePublicaRSA(String usuarioId){
        OkHttpClient client = Seguridad.getUnsafeOkHttpClient();
        String url = "https://"+IP+":"+PORT+"/obtenerClavePublica?usuarioId="+usuarioId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try{
            Response response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            try {
                JSONObject json_response=new JSONObject(new String(responseBody));
                JSONObject data = (JSONObject) json_response.get("data");
                String filename = (String) json_response.get("filename");
                JSONArray bytearray_json = (JSONArray) data.get("data");
                byte[] bytes = new byte[bytearray_json.length()];
                for (int i =0; i < bytearray_json.length(); i++ ) {
                    bytes[i] = (byte)(int)bytearray_json.get(i);
                }
                
                //Obtenemos la clave privada cifrada con AES
                File TEMP = new File("public.key");
                OutputStream os = new FileOutputStream(TEMP);
                os.write(bytes);
                os.close();
                
                //Borramos fichero cifrado y el ZIP
                TEMP.deleteOnExit();
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    private static void descargarClavePrivadaRSA(String usuarioId, String AES_KEY, String KEYNAME){
        OkHttpClient client = Seguridad.getUnsafeOkHttpClient();
        String url = "https://"+IP+":"+PORT+"/obtenerClavePrivada?usuarioId="+usuarioId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try{
            Response response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            try {
                JSONObject json_response=new JSONObject(new String(responseBody));
                JSONObject data = (JSONObject) json_response.get("data");
                String filename = (String) json_response.get("filename");
                JSONArray bytearray_json = (JSONArray) data.get("data");
                byte[] bytes = new byte[bytearray_json.length()];
                for (int i =0; i < bytearray_json.length(); i++ ) {
                    bytes[i] = (byte)(int)bytearray_json.get(i);
                }
                
                //Obtenemos la clave privada cifrada con AES
                File cifradoTEMP = new File(filename);
                OutputStream os = new FileOutputStream(cifradoTEMP);
                os.write(bytes);
                os.close();
                
                //Desciframos el fichero
                Seguridad.descifrarFicheroAES(cifradoTEMP, KEYNAME, AES_KEY);
                
                //Borramos fichero cifrado y el ZIP
                cifradoTEMP.delete();
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
          // Create a trust manager that does not validate certificate chains
          final TrustManager[] trustAllCerts = new TrustManager[] {
              new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new java.security.cert.X509Certificate[]{};
                }
              }
          };
          // Install the all-trusting trust manager
          final SSLContext sslContext = SSLContext.getInstance("SSL");
          sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
          // Create an ssl socket factory with our all-trusting manager
          final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

          OkHttpClient.Builder builder = new OkHttpClient.Builder();
          builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
          builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
              return true;
            }
          });

          OkHttpClient okHttpClient = builder.build();
          return okHttpClient;
        } catch (Exception e) {
          //throw new RuntimeException(e);
        }
        return null;
    }
    
}
