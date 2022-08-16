/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
        public static Connection cn = null;
    
        static String ip ;
        static String baseDatos;
        static String usuario;
        static String contraseña;
    public static void parametros(String dir,String bd,String user, String contra){
        ip = dir;
        baseDatos = bd;
        usuario = user;
        contraseña = contra;
    }
    
    public static Connection Conexion(){  
        try {
            cn =DriverManager.getConnection("jdbc:mysql://"+ip+":3306/"+baseDatos,usuario,contraseña); 
        } catch (SQLException e) {
            System.out.println("error al conectar " +e );
            Errores.Errores("No se puedo conectar Base de Datos: " +e);
        } 
        return cn;
    }

    

}
