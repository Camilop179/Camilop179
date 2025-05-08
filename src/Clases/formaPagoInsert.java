/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Ventanas.Ventas;
import javax.swing.JTable;
import java.sql.*;

/**
 *
 * @author USER
 */
public class formaPagoInsert {
   
    public void insetFormaPago(int nroVentas,String forma,double valor){
        try(Connection cn = Conexion.Conexion()){
            PreparedStatement ps = cn.prepareStatement("insert into formapago(id,nroVentas,forma,valor) values (?,?,?,?)");
            ps.setInt(1, 0);
            ps.setInt(2, nroVentas);
            ps.setString(3, forma);
            ps.setDouble(4, valor);
            ps.execute();
            cn.close();
        }catch(SQLException ex){
            System.err.println(ex);
        }
    }
}
