/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import java.sql.*;
import javax.swing.JOptionPane;
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
            JOptionPane.showMessageDialog(null, "Error al Actulizar Producto: "+e);
            Errores.Errores("Error al disminuir Producto: " +e);
        }
    }
    public static void aumentar(int cant,double precioC,String codigo){
        int cantidad;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select cantidad,precio_venta from producto where codigo =?");
            pr.setString(1, codigo);
            ResultSet rs = pr.executeQuery();
            while(rs.next()){
                double preciov = rs.getDouble(2);
                cantidad=rs.getInt(1);
                cantidad += cant;
                
                PreparedStatement pr2 = cn.prepareStatement("update producto set cantidad=?,total_cost=?,precio_compra=?,utilidad=?,porcentaje_utilidad=? where codigo =?");
                pr2.setInt(1, cantidad);
                pr2.setDouble(2, cantidad*precioC);
                pr2.setDouble(3, precioC);
                pr2.setDouble(4, preciov-precioC);
                pr2.setDouble(5, Math.round((preciov/precioC-1)*100));
                pr2.setString(6, codigo);
                pr2.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e);
            
            Errores.Errores("Error al actualizar Aumento de Producto: " +e);
        }
    }
    
}
