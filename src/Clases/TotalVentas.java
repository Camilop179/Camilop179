/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import static Ventanas.Administrador.jLabelVentaSemana;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author harol
 */
public class TotalVentas {

    public static String VentaDia() {
        String ventaDia = "";
        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        Date fecha_actual = Fechas.fechaActualDate();

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha = ?");
            pre.setDate(1, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                do {
                    venta_dia += rs.getDouble(1);

                    ventaDia = "$" + formatea.format(venta_dia);
                } while (rs.next());
            } else {
                ventaDia = "$0";
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
        return ventaDia;
    }

    public static String VentaMes() {
        String ventaMes = "$0";
        ZoneId defaultZoneId = ZoneId.systemDefault();
        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        Date fecha_actual = Fechas.fechaActualDate();
        GregorianCalendar semana = new GregorianCalendar();
        semana.setTime(fecha_actual);
        Month mes = LocalDate.now().getMonth();
        LocalDate fecha_semana = LocalDate.of(LocalDate.now().getYear(), mes, 1);
        java.util.Date fecha_sem = Date.from(fecha_semana.atStartOfDay(defaultZoneId).toInstant());
        java.sql.Date bd_fecha = new java.sql.Date(fecha_sem.getTime());

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha BETWEEN ? and ?");
            pre.setDate(1, bd_fecha);
            pre.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                do {
                    venta_dia += rs.getDouble(1);

                    ventaMes = "$" + formatea.format(venta_dia);
                } while (rs.next());
            } else {
                ventaMes = "$0";
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
        return ventaMes;
    }

    public static String VentaSemana() {
        String fechaSemana = "$0";
        Date fecha_actual = Fechas.fechaActualDate();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha_actual);
        LocalDate fecha_semana;
        int i = gc.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                fecha_semana = LocalDate.now().minusDays(6);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;
            case 2:
                fecha_semana = LocalDate.now().minusDays(0);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;
            case 3:
                fecha_semana = LocalDate.now().minusDays(1);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
            case 4:
                fecha_semana = LocalDate.now().minusDays(2);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;
            case 5:
                fecha_semana = LocalDate.now().minusDays(3);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;
            case 6:
                fecha_semana = LocalDate.now().minusDays(4);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;
            case 7:
                fecha_semana = LocalDate.now().minusDays(5);
                fechaSemana = dia_semana(fecha_actual, fecha_semana);
                break;

        }
        return fechaSemana;
    }

    public static String dia_semana(Date fecha_actual, LocalDate fecha_semana) {
        String dia_semana = "$0";
        ZoneId defaultZoneId = ZoneId.systemDefault();
        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        java.util.Date fecha_sem = Date.from(fecha_semana.atStartOfDay(defaultZoneId).toInstant());
        java.sql.Date bd_fecha = new java.sql.Date(fecha_sem.getTime());

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha BETWEEN ? and ?");
            pre.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            pre.setDate(1, bd_fecha);
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                venta_dia += rs.getDouble(1);
                dia_semana = "$" + formatea.format(venta_dia);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
        return dia_semana;
    }

}
