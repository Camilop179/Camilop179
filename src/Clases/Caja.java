/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import static Ventanas.Comprobante.jLabelNro;
import Ventanas.Login;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

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
    public double cuentasTotal(){
        double total = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps;
            ps = cn.prepareStatement("SELECT total FROM ptm  where id=(select max(id) from ptm)");
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
    public void sumarCaja(String concepto, double precio ) {

        try {
            double valor = cajaTotal();
            double total = valor + precio;
            Connection cn = Conexion.Conexion();
            PreparedStatement ps = cn.prepareStatement("insert into caja (id,concepto,valor,total,fecha,hora) values (?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setString(2, concepto);
            ps.setDouble(3, precio);
            ps.setDouble(4, total);
            ps.setDate(5, new Date(Fechas.fechaActualDate().getTime()));
            ps.setTime(6, new Time(Fechas.fechaActualDate().getTime()));
            ps.executeUpdate();
            cn.close();
            Login.adm.caja();
        } catch (NumberFormatException | SQLException e) {
            System.err.println("Error al Sumar Caja: " + e);
        }
    }
    
}
