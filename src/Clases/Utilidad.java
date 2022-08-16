/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author harol
 */
public class Utilidad {
    
    public static double utilidad(String codigo){
        double utilida=0;
         try {
                Connection cnn = Conexion.Conexion();
                PreparedStatement pre = cnn.prepareStatement("select utilidad from producto where codigo = ?");
                pre.setString(1, codigo);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                        utilida = rs.getDouble(1);
                }

            } catch (SQLException e) {
                System.err.println(e);
            }
        return utilida;
         
    }
    public static double costo(String codigo){
        double utilida=0;
         try {
                Connection cnn = Conexion.Conexion();
                PreparedStatement pre = cnn.prepareStatement("select precio_compra from producto where codigo = ?");
                pre.setString(1, codigo);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                        utilida = rs.getDouble(1);
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        return utilida;
    }
    
}
