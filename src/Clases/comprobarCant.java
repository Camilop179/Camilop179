/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author pc
 */
public class comprobarCant {

    ArrayList<Object> lista = new ArrayList<>();
    ResultSet rs;

    public comprobarCant() {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select idProducto,codigo,producto,cantidad,minimo from producto");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt(4) <= rs.getInt(5)) {
                    Object[] obj = new Object[4];
                    obj[0] = rs.getString(1);
                    obj[1] = rs.getString(2);
                    obj[2] = rs.getString(3);
                    obj[3] = rs.getString(4);
                    lista.add(obj);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public ResultSet getRs() {
        return rs;
    }

    public ArrayList getLista() {
        return lista;
    }
}
