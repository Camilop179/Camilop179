/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.*;
/**
 *
 * @author harol
 */
public class ActualizarCantidad {
    public static void restar(int cant,String codigo){
        int cantidad;
        
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select cantidad,precio_compra from producto where codigo =?");
            pr.setString(1, codigo);
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                cantidad=rs.getInt(1);
                cantidad-=cant;
                
                
                PreparedStatement pr2 = cn.prepareStatement("update producto set cantidad=?,total_cost=? where codigo =?");
                pr2.setInt(1, cantidad);
                pr2.setDouble(2, cantidad*rs.getDouble(2));
                pr2.setString(3, codigo);
                pr2.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public static void aumentar(int cant,String codigo){
        int cantidad;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select cantidad,precio_compra from producto where codigo =?");
            pr.setString(1, codigo);
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                cantidad=rs.getInt(1);
                cantidad += cant;
                
                PreparedStatement pr2 = cn.prepareStatement("update producto set cantidad=?,total_cost=? where codigo =?");
                pr2.setInt(1, cantidad);
                pr2.setDouble(2, cantidad*rs.getDouble(2));
                pr2.setString(3, codigo);
                pr2.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
}
