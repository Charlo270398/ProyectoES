/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Carlos
 */
public class Fichero {
    File fichero = null;
    
    public Fichero(){
        
    }
    
    public Fichero(File fichero){
        this.fichero = fichero;
    }
    
    public void getFicheroGET(String ficheroId){
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:5000/obtenerFichero?ficheroId="+ficheroId;
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
                File file = new File(filename);
                OutputStream os = new FileOutputStream(file);
                os.write(bytes);
                os.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void subirFicheroPOST(String usuario) throws FileNotFoundException, IOException{
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:5000/subirFichero";
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("usuario", usuario)
                .addFormDataPart("file1", fichero.getName(), RequestBody.create(MediaType.parse("file"), fichero))
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            JSONObject json_response = new JSONObject(new String(responseBody));
            String result = json_response.getString("result");

            //Resultado de la peticióm
            if(result.equals("OK")){
                System.out.println(result);
            }else{
                String error = json_response.getString("error");
                System.out.println(error);
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (JSONException ex) { 
            System.out.println(ex.toString());
        } 
    }
}
