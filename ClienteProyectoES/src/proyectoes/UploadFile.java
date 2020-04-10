/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import static proyectoes.Login.FRAME_menuUsuario;

/**
 *
 * @author Carlos
 */
public class UploadFile extends javax.swing.JFrame {

    /**
     * Creates new form MenuUsuario
     */
    public UploadFile() {
        initComponents();
        FRAME_CompartirFichero = new CompartirFichero();
    }
    
    File selectedFile = null;
    CompartirFichero FRAME_CompartirFichero = null;
    
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
        jButtonSeleccionarArchivo = new javax.swing.JButton();
        jLabelFichero = new javax.swing.JLabel();
        jButtonCompartirArchivo = new javax.swing.JButton();
        jButtonAñadirArchivo = new javax.swing.JButton();
        jButtonBack = new javax.swing.JButton();
        jLabelPolitica = new javax.swing.JLabel();
        jCheckBoxDiaria = new javax.swing.JCheckBox();
        jCheckBoxSemanal = new javax.swing.JCheckBox();
        jCheckBoxMensual = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jPanel2.setBackground(new java.awt.Color(255, 153, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Añadir archivos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jButtonSeleccionarArchivo.setText("Seleccionar archivo");
        jButtonSeleccionarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeleccionarArchivoActionPerformed(evt);
            }
        });

        jLabelFichero.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFichero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFichero.setText("Fichero sin seleccionar");

        jButtonCompartirArchivo.setText("Compartir");
        jButtonCompartirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCompartirArchivoActionPerformed(evt);
            }
        });

        jButtonAñadirArchivo.setText("Añadir");
        jButtonAñadirArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAñadirArchivoActionPerformed(evt);
            }
        });

        jButtonBack.setText("Volver");
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        jLabelPolitica.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelPolitica.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPolitica.setText("Política de copias");

        jCheckBoxDiaria.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxDiaria.setText("Diaria");

        jCheckBoxSemanal.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxSemanal.setText("Semanal");

        jCheckBoxMensual.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxMensual.setText("Mensual");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAñadirArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonCompartirArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSeleccionarArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelFichero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPolitica, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBoxDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxSemanal, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBoxMensual, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSeleccionarArchivo)
                    .addComponent(jLabelFichero))
                .addGap(18, 18, 18)
                .addComponent(jButtonCompartirArchivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jLabelPolitica)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxDiaria)
                    .addComponent(jCheckBoxSemanal)
                    .addComponent(jCheckBoxMensual))
                .addGap(30, 30, 30)
                .addComponent(jButtonAñadirArchivo)
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

    private void jButtonSeleccionarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeleccionarArchivoActionPerformed
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                //System.out.print(jfc.getCurrentDirectory());
                selectedFile = jfc.getSelectedFile();
                jLabelFichero.setText(selectedFile.getAbsolutePath());
            }
    }//GEN-LAST:event_jButtonSeleccionarArchivoActionPerformed

    private void jButtonAñadirArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAñadirArchivoActionPerformed
        if(selectedFile != null){
            try {
                Fichero fichero = new Fichero (selectedFile, FRAME_CompartirFichero.getPERMITIDOS_listaUsuariosNombre());
                fichero.setCopia_diaria(jCheckBoxDiaria.isSelected());
                fichero.setCopia_semanal(jCheckBoxSemanal.isSelected());
                fichero.setCopia_mensual(jCheckBoxMensual.isSelected());
                try {
                    fichero.subirFicheroPOST(MenuUsuario.USUARIO);
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println(ex.toString());
                }
                //Reiniciamos los valores
                selectedFile = null;
                jLabelFichero.setText("Fichero sin seleccionar");
                jCheckBoxDiaria.setSelected(false);
                jCheckBoxSemanal.setSelected(false);
                jCheckBoxMensual.setSelected(false);
                this.hide();
                FRAME_menuUsuario.setVisible(true);
            } catch (IOException ex) {
                System.out.println("ERROR SUBIENDO FICHERO: " + ex.toString());
            }
        }
    }//GEN-LAST:event_jButtonAñadirArchivoActionPerformed

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        // TODO add your handling code here:
        selectedFile = null;
        jLabelFichero.setText("Fichero sin seleccionar");
        this.hide();
        FRAME_menuUsuario.setVisible(true); 
    }//GEN-LAST:event_jButtonBackActionPerformed

    private void jButtonCompartirArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompartirArchivoActionPerformed
        // TODO add your handling code here:
        FRAME_CompartirFichero.setVisible(true);
        this.hide();
    }//GEN-LAST:event_jButtonCompartirArchivoActionPerformed

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
            java.util.logging.Logger.getLogger(UploadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UploadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UploadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UploadFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UploadFile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAñadirArchivo;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonCompartirArchivo;
    private javax.swing.JButton jButtonSeleccionarArchivo;
    private javax.swing.JCheckBox jCheckBoxDiaria;
    private javax.swing.JCheckBox jCheckBoxMensual;
    private javax.swing.JCheckBox jCheckBoxSemanal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelFichero;
    private javax.swing.JLabel jLabelPolitica;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
