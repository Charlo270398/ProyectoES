/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
    String SOURCE_FOLDER;
    private ArrayList <String> fileList;
    
    public Fichero(){
        
    }
    
    public Fichero(File fichero){
        this.fichero = fichero;
        if(fichero.isDirectory()){
            SOURCE_FOLDER = fichero.getAbsolutePath();
            fileList = new ArrayList();
        }
    }
    
    private static void zipSingleFile(File file, String zipFileName) {
        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            
            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            System.out.println(file.getCanonicalPath()+" is zipped to "+zipFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
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
    
    public void subirFicheroPOST(String usuario) throws IOException{
        //COMPRESION A ZIP
        if(fichero.isDirectory()){
            //SI EL ES CARPETA
            generateFileList(new File(SOURCE_FOLDER));
            zipIt("TEMPORAL.zip");//Comprimimos el fichero/carpeta
        }else{
            //SI ES UN FICHERO SIMPLE
            zipSingleFile(fichero, "TEMPORAL.zip");
        }
        
        //ENVIO DEL FICHERO
        File temporal = new File("TEMPORAL.zip"); //Cargamos el zip temporal para subirlo
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:5000/subirFichero";
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("usuario", usuario)
                .addFormDataPart("file1", fichero.getName() + ".zip", RequestBody.create(MediaType.parse("zip"), temporal))
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
                temporal.delete();//Borramos el zip temporal
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
     public void borrarFichero(String usuario, String ficheroId, String ficheroNombre) throws FileNotFoundException, IOException{
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:5000/borrarFichero";
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("usuario", usuario)
                .addFormDataPart("ficheroId", ficheroId)
                .addFormDataPart("ficheroNombre", ficheroNombre)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            JSONObject json_response = new JSONObject(new String(responseBody));
            String result = json_response.getString("result");

            //Resultado de la peticióm
            if(!result.equals("OK")){

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
