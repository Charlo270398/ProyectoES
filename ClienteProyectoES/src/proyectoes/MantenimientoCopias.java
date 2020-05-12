/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import static proyectoes.Login.IP;
import static proyectoes.Login.PORT;
import static proyectoes.MenuUsuario.USUARIO;

/**
 *
 * @author Carlos
 */
public class MantenimientoCopias extends Thread{
    
    String[] listaFicherosNombre, listaFicherosId;
    Boolean[] listaFicherosDiaria, listaFicherosSemanal, listaFicherosMensual;
    Date[] listaFicherosFecha;
    
    public void sincronizarCarpeta(){
        getListaFicherosGET();
        for(int i=0; i<listaFicherosNombre.length; i++){
            Fichero fichero = new Fichero();
            Date date = new Date();
            fichero.getFicheroGET(listaFicherosId[i]);
        }
    }
    
    public void run(){
        SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date;  
        getListaFicherosGET();
        while(true){
            //Aquí comprobamos si volvemos a realizar la copia
            for(int i=0; i<listaFicherosNombre.length; i++){
                date = new Date();
                long diff = date.getTime() - listaFicherosFecha[i].getTime();
                if(listaFicherosDiaria[i]){
                    if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 1){
                        try {
                            System.out.println(USUARIO + "_MIS_FICHEROS/" + listaFicherosNombre[i]);
                            File file = new File(USUARIO + "_MIS_FICHEROS/" + listaFicherosNombre[i]);
                            System.out.println(file.getAbsolutePath());
                            Fichero fichero = new Fichero(file);
                            fichero.subirFicheroMantenimientoCopiasPOST(dateformatter.format(date)+"--"+listaFicherosNombre[i], listaFicherosId[i]);
                            getListaFicherosGET();
                            i=listaFicherosNombre.length;
                        } catch (Exception ex) {
                            System.out.println("ERROR COPIAS:" + ex.toString());
                        }
                    }
                }else{
                    if(listaFicherosSemanal[i]){
                        if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 7){
                            try {
                                File file = new File(USUARIO + "_MIS_FICHEROS/" + listaFicherosNombre[i]);
                                Fichero fichero = new Fichero(file);
                                fichero.subirFicheroMantenimientoCopiasPOST(dateformatter.format(date)+"--"+listaFicherosNombre[i], listaFicherosId[i]);
                                getListaFicherosGET();
                                i=listaFicherosNombre.length;
                            } catch (Exception ex) {
                                System.out.println("ERROR COPIAS:" + ex.toString());
                            }
                        }
                    }else{
                        if(listaFicherosMensual[i]){
                            if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 30){
                                try {
                                    File file = new File(USUARIO + "_MIS_FICHEROS/" + listaFicherosNombre[i]);
                                    Fichero fichero = new Fichero(file);
                                    fichero.subirFicheroMantenimientoCopiasPOST(dateformatter.format(date)+"--"+listaFicherosNombre[i], listaFicherosId[i]);
                                    getListaFicherosGET();
                                    i=listaFicherosNombre.length;
                                } catch (Exception ex) {
                                    System.out.println("ERROR COPIAS:" + ex.toString());
                                }   
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void getListaFicherosGET(){
        OkHttpClient client = Seguridad.getUnsafeOkHttpClient();
        String url = "https://"+IP+":"+PORT+"/obtenerListaFicheros";
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", MenuUsuario.USER_ID)
                .addFormDataPart("userToken", MenuUsuario.USER_TOKEN)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            try {
                String fichero_id, nombre, fichero_fechasubida;
                Boolean fichero_diaria, fichero_semanal, fichero_mensual;
                Date fichero_fecha;
                JSONObject json_response = new JSONObject(new String(responseBody));
                JSONArray array_json = (JSONArray) json_response.get("result");
                
                //Inicializamos los array con el tamaño del json devuelto
                listaFicherosNombre = new String[array_json.length()];
                listaFicherosId = new String[array_json.length()];
                listaFicherosDiaria = new Boolean[array_json.length()];
                listaFicherosSemanal = new Boolean[array_json.length()];
                listaFicherosMensual = new Boolean[array_json.length()];
                listaFicherosFecha = new Date[array_json.length()];
                
                //Rellenamos los arrays
                for (int i=0; i < array_json.length(); i++) {
                    nombre = (String) array_json.getJSONObject(i).get("nombre");
                    fichero_id = String.valueOf(array_json.getJSONObject(i).getInt("fichero_id"));
                    fichero_fechasubida = array_json.getJSONObject(i).getString("fecha_modificacion");
                    fichero_diaria = array_json.getJSONObject(i).getBoolean("copia_diaria");
                    fichero_semanal = array_json.getJSONObject(i).getBoolean("copia_semanal");
                    fichero_mensual = array_json.getJSONObject(i).getBoolean("copia_mensual");
                    listaFicherosNombre[i] = nombre;
                    listaFicherosId[i] = fichero_id;
                    listaFicherosDiaria[i] = fichero_diaria;
                    listaFicherosSemanal[i] = fichero_semanal;
                    listaFicherosMensual[i] = fichero_mensual;
                    SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    fichero_fecha = dateformatter.parse(fichero_fechasubida);  
                    listaFicherosFecha[i] = fichero_fecha;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
