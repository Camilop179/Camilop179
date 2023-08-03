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
 * @author fullmotors
 */
public class Caja {

    public Caja() {
    }
    
    public double cajaTotal(){
        double total = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps;
            ps = cn.prepareStatement("SELECT total FROM caja  where id=(select max(id) from caja)");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total=rs.getDouble(1);
            }
            cn.close();
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e);
        }
        return total;
    }
    
}
