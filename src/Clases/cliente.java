/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.*;

/**
 *
 * @author USER
 */
public class cliente {

    public cliente() {
    }
    
    public double saldoCliente(String cedula){
        double saldo = 0;
        try (Connection cn = Conexion.Conexion()){
            PreparedStatement ps = cn.prepareStatement("Select Saldo  from clientes WHERE Cedula = ?");
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {                
                saldo = rs.getDouble(1);
            }
            cn.close();
        } catch (Exception e) {
        }
        return saldo;
    }
    
}
