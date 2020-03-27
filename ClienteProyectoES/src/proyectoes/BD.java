/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoes;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 *
 * @author Carlos
 */
public class BD {
    String cadenaConexion, user, password;
    Connection conexion = null;
    public BD(){
        cadenaConexion = "jdbc:mysql://localhost:3306/test";
        user = "root";
        password = "";
    }
    
    public void conectarBD(){
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            conexion = DriverManager.getConnection(cadenaConexion,user,password); //Cadena conexión, usuario, pass
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean loginUsuario(String usuario, String password){
        try {
            Statement stmt = conexion.createStatement();
            String sql = "Select * from tbLogin where Username='" + usuario + "' and Password='" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return true;
            }else {
		return false;
	}
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
