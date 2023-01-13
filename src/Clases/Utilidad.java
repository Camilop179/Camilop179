/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

/**
 *
 * @author harol
 */
public class Utilidad {
    
    public static double utilidad(String codigo){
        double utilida=0;
         try {
                Connection cnn = Conexion.Conexion();
                PreparedStatement pre = cnn.prepareStatement("select utilidad from producto where codigo = ? or codigo_barras =?");
                pre.setString(1, codigo);
                pre.setString(2, codigo);
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
                PreparedStatement pre = cnn.prepareStatement("select precio_compra from producto where codigo = ? or codigo_barras=?");
                pre.setString(1, codigo);
                pre.setString(2, codigo);
                ResultSet rs = pre.executeQuery();
                while (rs.next()) {
                        utilida = rs.getDouble(1);
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        return utilida;
    }
    public static int utilidadMes() {
        int utilidad = 0;
        try ( Connection cn = Conexion.Conexion()) {
            LocalDate fecha = LocalDate.now();
            Month mes = fecha.getMonth();
            LocalDate fecha1 = LocalDate.of(fecha.getYear(), mes.getValue(), 1);
            PreparedStatement pr1 = cn.prepareStatement("select utilidad from ventas where fecha between ? and ?");
            pr1.setDate(1, new java.sql.Date(java.util.Date.from(fecha1.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
            pr1.setDate(2, new java.sql.Date(Fechas.fechaActualDate().getTime()));
            ResultSet rs1 = pr1.executeQuery();
            while (rs1.next()) {
                utilidad += rs1.getDouble(1);
            }
            cn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return utilidad;
    }
    
}
