/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Ventanas.Cotizacion;
import Ventanas.Ventas;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author fullmotors
 */
public final class Imprimir {

    public Imprimir() {

    }
public void catalogo() {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/catalogo.jasper");
        try {
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
            jv.setTitle("Reporte Ventas");
        } catch (JRException e) {
            System.out.println(e);
        }
    }
    public void imprimir1() {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/report1.jasper");
        try {
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            URL url = getClass().getResource("/imagenes/logo.jpg");
            parametro.put("NroVentas", Integer.valueOf(Ventas.jLabelNoVenta.getText()));
            parametro.put("url", url);
            jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
            jv.setTitle("Reporte Ventas");
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    double total = 0;
    double descuento = 0;

    

    public void imprimir2() {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/Factura_Carta.jasper");
        try {
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            URL url = getClass().getResource("/imagenes/logo.jpg");
            URL url2 = getClass().getResource("/imagenes/uma.jpg");
            
            parametro.put("NroVentas", Integer.valueOf(Ventas.jLabelNoVenta.getText()));
            parametro.put("Imagen1", url);
            parametro.put("Imagen2", url2);
            parametro.put("Dataset1.NroVentas", Integer.valueOf(Ventas.jLabelNoVenta.getText()));
            jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
            jv.setTitle("Reporte Ventas");
        } catch (JRException e) {
            System.out.println(e);
        }
    }
    public void imprimirNomina(String nro) {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/Nomina.jasper");
        try {
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            URL url = getClass().getResource("/imagenes/logo.jpg");
            URL url2 = getClass().getResource("/imagenes/uma.jpg");
            parametro.put("NroNom", nro);
            parametro.put("Imagen1", url);
            parametro.put("Imagen2", url2);
            jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
            jv.setTitle("Reporte Ventas");
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    public void imprimir3() {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/Factura_Carta2.jasper");
        try {
            double iva = total * 0.1596638655462185;
            double subtotal = total - descuento - iva;
            double anteIva = subtotal + descuento;
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            URL url = getClass().getResource("/imagenes/logo.jpg");
            URL url2 = getClass().getResource("/imagenes/uma.jpg");
            parametro.put("NroVentas", Integer.valueOf(Cotizacion.jLabelNoVenta.getText()));
            parametro.put("Imagen1", url);
            parametro.put("Imagen2", url2);
            parametro.put("Subtotal", new FormatoPesos().decimales(subtotal));
            parametro.put("SinIva", new FormatoPesos().decimales(anteIva));
            parametro.put("iva", new FormatoPesos().decimales(iva));
            jr = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
            jv.setTitle("Reporte Ventas");
        } catch (JRException e) {
            System.out.println(e);
        }
    }
}
