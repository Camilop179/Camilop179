/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import static Ventanas.Compras.jTextFieldSaldo;
import static Ventanas.Compras.jTextFieldTotal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class proveedor {
    private int id;
    public proveedor(int id) {
        this.id=id;
    }

    double saldo() {
        double saldo=0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("Select saldo from proveedor where idproveedor = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                saldo =rs.getDouble(1);
            }
            cn.close();
        } catch (Exception e) {
        }
        return saldo;
    }

    public void actualizarSaldo(double precio) {
        double saldo = saldo() + precio;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("update proveedor set saldo=? where idproveedor=?");
            ps.setDouble(1, saldo);
            ps.setInt(2, id);
            ps.execute();
            cn.close();
        } catch (Exception e) {
        }
    }
}
