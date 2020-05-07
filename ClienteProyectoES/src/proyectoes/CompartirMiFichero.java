/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import static proyectoes.MenuUsuario.FRAME_listarFicheros;
import static proyectoes.MenuUsuario.USER_ID;

/**
 *
 * @author Carlos
 */
public class CompartirMiFichero extends javax.swing.JFrame {
    
    private int ficheroId;
    private String[] PERMITIDOS_listaUsuariosNombre = new String[0], PERMITIDOS_listaUsuariosId = new String[0];

    public String[] getPERMITIDOS_listaUsuariosId() {
        return PERMITIDOS_listaUsuariosId;
    }

    public String[] getPERMITIDOS_listaUsuariosNombre() {
        return PERMITIDOS_listaUsuariosNombre;
    }
    private String[] DENEGADOS_listaUsuariosNombre, DENEGADOS_listaUsuariosId;
    /**
     * Creates new form Compartir
     */
    public CompartirMiFichero(int ficheroId) {
        initComponents();
        this.ficheroId = ficheroId;
        getListaUsuariosDenegadosGET();
        getListaUsuariosPermtidosGET();
        jListNoPermitidos.setListData(DENEGADOS_listaUsuariosNombre);
        jListPermitidos.setListData(PERMITIDOS_listaUsuariosNombre);
    }
    
    public CompartirMiFichero(String ficheroId) {
        initComponents();
    }
    
    public void getListaUsuariosDenegadosGET(){
        OkHttpClient client = Seguridad.getUnsafeOkHttpClient();
        String url = "https://localhost:5000/noCompartidosFichero?ficheroId=" + ficheroId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try{
            Response response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            try {
                String user_id, nombre;
                JSONObject json_response = new JSONObject(new String(responseBody));
                JSONArray array_json = (JSONArray) json_response.get("result");
                
                //Inicializamos los array con el tamaño del json devuelto menos nuestro usuario
                DENEGADOS_listaUsuariosNombre = new String[array_json.length()-1];
                DENEGADOS_listaUsuariosId = new String[array_json.length()-1];
                
                //Rellenamos los arrays
                for (int i=0, j=0; i < array_json.length(); i++) {
                    nombre = (String) array_json.getJSONObject(i).get("usuario");
                    user_id = String.valueOf(array_json.getJSONObject(i).getInt("compartido"));
                    //No contamos nuestro propio USUARIO
                    if(Integer.parseInt(USER_ID)!=Integer.parseInt(user_id)){
                        DENEGADOS_listaUsuariosNombre[j] = nombre;
                        DENEGADOS_listaUsuariosId[j] = user_id;
                        j++;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void getListaUsuariosPermtidosGET(){
        OkHttpClient client = Seguridad.getUnsafeOkHttpClient();
        String url = "https://localhost:5000/compartidosFichero?ficheroId=" + ficheroId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try{
            Response response = client.newCall(request).execute();
            byte[] responseBody = response.body().bytes();
            try {
                String fichero_id, nombre;
                JSONObject json_response = new JSONObject(new String(responseBody));
                JSONArray array_json = (JSONArray) json_response.get("result");
                
                //Inicializamos los array con el tamaño del json devuelto
                PERMITIDOS_listaUsuariosNombre = new String[array_json.length()];
                PERMITIDOS_listaUsuariosId = new String[array_json.length()];
                
                //Rellenamos los arrays
                for (int i=0; i < array_json.length(); i++) {
                    nombre = (String) array_json.getJSONObject(i).get("usuario");
                    fichero_id = String.valueOf(array_json.getJSONObject(i).getInt("compartido"));
                    PERMITIDOS_listaUsuariosNombre[i] = nombre;
                    PERMITIDOS_listaUsuariosId[i] = fichero_id;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonAñadir = new javax.swing.JButton();
        jButtonBack = new javax.swing.JButton();
        jButtonQuitar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListNoPermitidos = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListPermitidos = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCambiarPermisos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jPanel2.setBackground(new java.awt.Color(255, 153, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("El fichero es compartido con");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jButtonAñadir.setLabel("Añadir");
        jButtonAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAñadirActionPerformed(evt);
            }
        });

        jButtonBack.setText("Volver");
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        jButtonQuitar.setLabel("Quitar");
        jButtonQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jListNoPermitidos);

        jScrollPane4.setViewportView(jListPermitidos);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Usuarios permitidos");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Usuarios NO permitidos");

        jButtonCambiarPermisos.setText("Cambiar permisos");
        jButtonCambiarPermisos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCambiarPermisosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonAñadir, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButtonCambiarPermisos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAñadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonQuitar)
                        .addGap(118, 118, 118))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)))
                .addComponent(jButtonCambiarPermisos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBack)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        // TODO add your handling code here:
        //CAMBIAR PERMISOS DE TODO
        this.hide();
        FRAME_listarFicheros.setVisible(true);
    }//GEN-LAST:event_jButtonBackActionPerformed
    
    
    public static String[] removeElement(String[] original, int element){
        String[] n = new String[original.length - 1];
        System.arraycopy(original, 0, n, 0, element );
        System.arraycopy(original, element+1, n, element, original.length - element-1);
        return n;
    }
    
    private void jButtonAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAñadirActionPerformed
        // TODO add your handling code here:
        if(jListNoPermitidos.getSelectedIndex() != -1){
            //AÑADIR A PERMITIDOS
            PERMITIDOS_listaUsuariosId = Arrays.copyOf(PERMITIDOS_listaUsuariosId, PERMITIDOS_listaUsuariosId.length + 1);
            PERMITIDOS_listaUsuariosNombre = Arrays.copyOf(PERMITIDOS_listaUsuariosNombre, PERMITIDOS_listaUsuariosNombre.length + 1);
            PERMITIDOS_listaUsuariosId[PERMITIDOS_listaUsuariosId.length-1] = DENEGADOS_listaUsuariosId[jListNoPermitidos.getSelectedIndex()];
            PERMITIDOS_listaUsuariosNombre[PERMITIDOS_listaUsuariosNombre.length-1] = DENEGADOS_listaUsuariosNombre[jListNoPermitidos.getSelectedIndex()];
            jListPermitidos.setListData(PERMITIDOS_listaUsuariosNombre);
            //QUITAR DE DENEGADOS
            DENEGADOS_listaUsuariosId = removeElement(DENEGADOS_listaUsuariosId, jListNoPermitidos.getSelectedIndex());
            DENEGADOS_listaUsuariosNombre = removeElement(DENEGADOS_listaUsuariosNombre, jListNoPermitidos.getSelectedIndex());
            jListNoPermitidos.setListData(DENEGADOS_listaUsuariosNombre);
        }
    }//GEN-LAST:event_jButtonAñadirActionPerformed

    private void jButtonQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarActionPerformed
        // TODO add your handling code here:
        if(jListPermitidos.getSelectedIndex() != -1){
            //AÑADIR A DENEGADOS
            DENEGADOS_listaUsuariosId = Arrays.copyOf(DENEGADOS_listaUsuariosId, DENEGADOS_listaUsuariosId.length + 1);
            DENEGADOS_listaUsuariosNombre = Arrays.copyOf(DENEGADOS_listaUsuariosNombre, DENEGADOS_listaUsuariosNombre.length + 1);
            DENEGADOS_listaUsuariosId[DENEGADOS_listaUsuariosId.length-1] = PERMITIDOS_listaUsuariosId[jListPermitidos.getSelectedIndex()];
            DENEGADOS_listaUsuariosNombre[DENEGADOS_listaUsuariosNombre.length-1] = PERMITIDOS_listaUsuariosNombre[jListPermitidos.getSelectedIndex()];
            jListNoPermitidos.setListData(DENEGADOS_listaUsuariosNombre);
            //QUITAR DE PERMITIDOS
            PERMITIDOS_listaUsuariosId = removeElement(PERMITIDOS_listaUsuariosId, jListPermitidos.getSelectedIndex());
            PERMITIDOS_listaUsuariosNombre = removeElement(PERMITIDOS_listaUsuariosNombre, jListPermitidos.getSelectedIndex());
            jListPermitidos.setListData(PERMITIDOS_listaUsuariosNombre);
        }
    }//GEN-LAST:event_jButtonQuitarActionPerformed

    private void jButtonCambiarPermisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCambiarPermisosActionPerformed
        try {
            // TODO add your handling code here:
            cambiarPermisos();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }//GEN-LAST:event_jButtonCambiarPermisosActionPerformed
    
    private void cambiarPermisos() throws IOException{
        Fichero fichero = new Fichero();
        //Obtenemos la clave AES del fichero y la desciframos con nuestra clave privada
        String CLAVE_AES_DESCIFRADA = fichero.getClaveFicheroGET(String.valueOf(ficheroId));
        boolean OK = true;
        
        //Denegamos acceso a todos los que hayamos indicado
        for(int i=0; i<DENEGADOS_listaUsuariosId.length; i++){
            OK = fichero.cancelarComparticionFichero(String.valueOf(DENEGADOS_listaUsuariosId[i]), String.valueOf(ficheroId));
            if(!OK){
                System.out.println("Error quitando permisos para el fichero con ID: " + String.valueOf(ficheroId));
            }
        }
        
        //Compartimos con todos los que hayamos indicado
        OK = true;
        for(int i=0; i<PERMITIDOS_listaUsuariosId.length; i++){
            OK = fichero.compartirFicheroUsuario(String.valueOf(PERMITIDOS_listaUsuariosId[i]), String.valueOf(ficheroId), CLAVE_AES_DESCIFRADA);
            if(!OK){
                System.out.println("Error añadiendo permisos para el fichero con ID: " + String.valueOf(ficheroId));
            }
        }
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CompartirMiFichero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CompartirMiFichero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CompartirMiFichero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CompartirMiFichero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAñadir;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonCambiarPermisos;
    private javax.swing.JButton jButtonQuitar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jListNoPermitidos;
    private javax.swing.JList<String> jListPermitidos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
