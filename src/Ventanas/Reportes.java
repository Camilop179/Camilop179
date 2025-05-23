/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ventanas;

import Clases.Caja;
import Clases.Conexion;
import Clases.Fechas;
import Clases.Fondo;
import Clases.FormatoPesos;
import Clases.ImagenBoton;
import Clases.TextoFondo;
import Clases.TotalVentas;
import Clases.Validaciones;
import Clases.uiJTabben;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author harolD
 */
public final class Reportes extends javax.swing.JFrame {

    public static int totalNomina = 0;
    public static int m = 0;
    public static boolean b = true;
    public static String nro;
    static DefaultTableModel tabla = new DefaultTableModel();
    static DefaultTableModel dm = new DefaultTableModel();
    DecimalFormat dm1 = new DecimalFormat("$###,###");

    DefaultTableModel df;
    GregorianCalendar gc = new GregorianCalendar();
    Date fecha_actual = Fechas.fechaActualDate();
    ZoneId defaultZoneId = ZoneId.systemDefault();

    TextoFondo tx;

    public Reportes() {
        Fondo fondo = new Fondo("FondoMenu.jpg");

        this.setContentPane(fondo);

        initComponents();

        jscroll();

        new ImagenBoton(
                "buscando.png", jButtonBuscar, jButtonBuscar.getWidth() - 5, jButtonBuscar.getHeight() - 5);
        new ImagenBoton(
                "buscando.png", jButtonBuscar1, jButtonBuscar1.getWidth() - 5, jButtonBuscar1.getHeight() - 5);
        new ImagenBoton(
                "proximo.png", jButton3, jButton3.getWidth() - 20, jButton3.getHeight() - 5);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        jButtonBuscar.setContentAreaFilled(
                false);
        jButtonBuscar1.setContentAreaFilled(
                false);
        jFormattedTextFieldFetcha.setText(Fechas.fechaActual());
        jDateChooser_fechaI.setDate(fecha_actual);
        int dia = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        jDateChooser_fechaF.setDate(fechaMes(dia));
        jScrollPane4.getViewport()
                .setBackground(new Color(51, 153, 255));
        jScrollPane3.getViewport()
                .setBackground(new Color(51, 153, 255));
        jTabbedPane2.setUI(
                new uiJTabben());
        jTabbedPane3.setUI(
                new uiJTabben());
        jTabbedPane1.setUI(
                new uiJTabben());
        jTabbedPane4.setUI(new uiJTabben());
        cerra();

        nroNomina();

        fecha();

        compra();

        venta();

        total(jTable2, jTextFieldTotalVenta,
                5);
        total(jTable1, jTextField2,
                4);
        b = false;

        tabla(jTableDevengado);

        tabla(jTableDeduccion);

        jTextFieldTotalInv1.setEditable(
                false);
        resumenEgresos();

        administracion();

        creditos();
        jTabbedPane2.add("Mano Obra", new manoDeObre());

        Date fecha1 = fechaMes(1);
        Date fecha2 = Fechas.fechaActualDate();
        rent(fecha1, fecha2);
        jDateChooser3.setDate(fecha1);
        jDateChooser4.setDate(fecha2);
        try {
            graficar();
            graficarBarra();
        } catch (ParseException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void creditos() {
        limpiar(jTable4, (DefaultTableModel) jTable4.getModel());
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select * from ventas where FormaPago='Credito'");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                String[] datos = new String[5];
                datos[0] = rs.getString(2);
                datos[1] = rs.getString(4);
                datos[2] = rs.getString(9);
                datos[3] = rs.getString(8);
                datos[4] = rs.getString(16);
                DefaultTableModel df = (DefaultTableModel) jTable4.getModel();
                df.addRow(datos);
                jTable4.setModel(df);
            }
            total(jTable4, jTextField10, 2);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void egreso(String concepto, double Valor, int tipo) {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("insert into egresos(Concepto,Valor,idUsuario,Fecha,Hora,tipo) values(?,?,?,?,?,?)");
            ps.setString(1, concepto);
            ps.setDouble(2, Valor);
            ps.setInt(3, Login.idUsuario);
            ps.setDate(4, new java.sql.Date(Fechas.fechaActualDate().getTime()));
            ps.setTime(5, new java.sql.Time(Fechas.fechaActualDate().getTime()));
            ps.setInt(6, tipo);
            ps.execute();

            DefaultTableModel tabal = (DefaultTableModel) jTable3.getModel();
            String[] datos = new String[4];
            datos[0] = concepto;
            datos[1] = String.valueOf(Valor);
            datos[2] = String.valueOf(Double.parseDouble(Administrador.jLabelCaja.getText().replace(",", "")) - Valor);
            datos[3] = String.valueOf(new Time(Fechas.fechaActualDate().getTime()));
            tabal.addRow(datos);
            jTable3.setModel(tabal);
            resta(Valor, concepto);
            totalEgresos();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    void resta(double valor, String concepto) {
        try (Connection cn = Conexion.Conexion()) {
            double total = new Caja().cajaTotal() - valor;

            PreparedStatement ps = cn.prepareStatement("insert into caja(Concepto,Valor,Total,Fecha,Hora) values(?,?,?,?,?)");
            ps.setString(1, concepto);
            ps.setDouble(2, -valor);
            ps.setDouble(3, total);
            ps.setDate(4, new java.sql.Date(Fechas.fechaActualDate().getTime()));
            ps.setTime(5, new Time(Fechas.fechaActualDate().getTime()));
            ps.execute();
            cn.close();
            Administrador.jLabelCaja.setText(FormatoPesos.formato(total));
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al restar caja: " + e);
            System.out.println(e);
        }
    }

    void EgresoNomina() {
        try {
            double total = 0;
            Connection cn = Conexion.Conexion();
            PreparedStatement ps = cn.prepareStatement("select Total from nomina where fecha2 between ? and ?");
            ps.setDate(1, new java.sql.Date(fechaMes(1).getTime()));
            ps.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getDouble(1) != 0) {
                    total += rs.getDouble(1);
                }
            }
            jTextField18.setText(FormatoPesos.formato(total));
        } catch (SQLException e) {
            System.err.println("Error en Egreso Nomina: " + e);
        }
    }

    public void resumenEgresos() {
        tx = new TextoFondo("Valor", jTextField3);
        tx = new TextoFondo("Valor", jTextField4);
        tx = new TextoFondo("Valor", jTextField5);
        tx = new TextoFondo("Valor", jTextField7);
        tx = new TextoFondo("Valor", jTextField15);
        tx = new TextoFondo("Valor", jTextField17);
        tx = new TextoFondo("Concepto", jTextField8);
        tx = new TextoFondo("Concepto", jTextField22);
        tx = new TextoFondo("Concepto", jTextField9);
        tx = new TextoFondo("Concepto", jTextField11);
        tx = new TextoFondo("Concepto", jTextField16);
        new ImagenBoton("cheque.png", jButton6, jButton6.getWidth() - 3, jButton6.getHeight() - 3);
        new ImagenBoton("cheque.png", jButton7, jButton7.getWidth() - 3, jButton7.getHeight() - 3);
        new ImagenBoton("cheque.png", jButton8, jButton8.getWidth() - 3, jButton8.getHeight() - 3);
        new ImagenBoton("cheque.png", jButton9, jButton9.getWidth() - 3, jButton9.getHeight() - 3);
        new ImagenBoton("cheque.png", jButton10, jButton10.getWidth() - 3, jButton10.getHeight() - 3);
        new ImagenBoton("cheque.png", jButton11, jButton11.getWidth() - 3, jButton11.getHeight() - 3);
        jButton3.setBackground(new Color(41, 128, 185));
        jButton3.setBorder(null);
        jButton6.setContentAreaFilled(false);
        jButton7.setContentAreaFilled(false);
        jButton8.setContentAreaFilled(false);
        jButton9.setContentAreaFilled(false);
        jButton10.setContentAreaFilled(false);
        jButton11.setContentAreaFilled(false);

        EgresoNomina();
        totalEgresos();
    }

    void rent(Date fechaIn, Date fechaFinal) {
        DefaultTableModel df = (DefaultTableModel) jTable5.getModel();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        while (!(formato.format(fechaIn)).equals(formato.format(fechaFinal))) {
            Object[] obg = new Object[2];
            obg[0] = formato.format(fechaIn);
            obg[1] = FormatoPesos.formato(rentabilidadDia(fechaIn));
            df.addRow(obg);
            fechaIn = fechaDia(fechaIn);
        }
    }

    Date fechaDia(Date fecha) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(fecha);
        cale.add(Calendar.DAY_OF_YEAR, 1);
        return cale.getTime();
    }

    public void totalEgresos() {
        double total = 0;
        double servcios = 0;
        double aseo = 0;
        double servicioT = 0;
        double comida = 0;
        double aliss = 0;
        double varios = 0;
        java.sql.Date fecha_i = new java.sql.Date(fechaMes(1).getTime());
        java.sql.Date fecha_f = new java.sql.Date(Fechas.fechaActualDate().getTime());
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select * from egresos where fecha between ? and ? ");
            pr.setDate(1, fecha_i);
            pr.setDate(2, fecha_f);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {

                switch (rs.getInt(7)) {
                    case 1 -> {
                        servcios += rs.getDouble(3);
                    }
                    case 2 -> {
                        aseo += rs.getDouble(3);
                    }
                    case 3 -> {
                        servicioT += rs.getDouble(3);
                    }
                    case 4 -> {
                        comida += rs.getDouble(3);
                    }
                    case 5 -> {
                        aliss += rs.getDouble(3);
                    }
                    case 6 -> {
                        varios += rs.getDouble(3);
                    }
                    default ->
                        throw new AssertionError();
                }
                total += rs.getDouble(3);

            }

            jLabelSP.setText("$" + FormatoPesos.formato(servcios));
            jLabelAseo.setText("$" + FormatoPesos.formato(aseo));
            jLabelServicio.setText("$" + FormatoPesos.formato(servicioT));
            jLabelAlisson.setText("$" + FormatoPesos.formato(aliss));
            jLabelComida.setText("$" + FormatoPesos.formato(comida));
            jLabelTotalEgresos.setText("$" + FormatoPesos.formato(total));
            jLabelVarios.setText("$" + FormatoPesos.formato(varios));
        } catch (SQLException ex) {
            System.err.println("Error en Total Egresos" + ex);
        }
        totaldia();
    }

    public void totaldia() {

        double totaldia = 0;
        java.sql.Date fecha = new java.sql.Date(Fechas.fechaActualDate().getTime());
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select * from egresos where fecha = ? ");
            pr.setDate(1, fecha);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                totaldia += rs.getDouble(3);
            }
        } catch (SQLException e) {

        }
        jLabelTotalEgresosDia.setText("$" + FormatoPesos.formato(totaldia));
    }

    public Date fechaMes(int dia) {
        Date fecha;
        LocalDate fechaLocal = LocalDate.now();
        LocalDate fechaF = LocalDate.of(fechaLocal.getYear(), fechaLocal.getMonth(), dia);
        fecha = Date.from(fechaF.atStartOfDay(defaultZoneId).toInstant());
        return fecha;
    }

    public double entraInventario() {
        double total = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select precio_factura from compra where fecha_factura = ? and estado=?");
            pr.setDate(1, new java.sql.Date(fecha_actual.getTime()));
            pr.setString(2, "Cancelado");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                total += rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error Rentabilidad: " + e);
        }
        return total;
    }

    public double[] rentabilidad(Date fecha_inicial, Date fecha_actual) {
        double[] total = new double[2];
        total[0] = 0;
        total[1] = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select utilidad,precioCosto from ventas where fecha BETWEEN ? and ?");
            pr.setDate(1, new java.sql.Date(fecha_inicial.getTime()));
            pr.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                total[0] += rs.getDouble(1);
                total[1] += rs.getDouble(2);
            }
        } catch (SQLException e) {
            System.err.println("Error Rentabilidad: " + e);
        }
        return total;
    }

    public void rentabilidadTotal() {
        double totalR = 0;
        double total = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select utilidad,precio_Total from ventas");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                totalR += rs.getDouble(1);
                total += rs.getDouble(2);
            }
            jTextFieldRentabilidadVentas.setText(dm1.format(totalR));
            jTextFieldTotalVentas1.setText(dm1.format(total));
        } catch (SQLException e) {
            System.out.println("Error al sacar rentabilidad " + e);
        }

    }

    public void administracion() {
        rentabilidadTotal();
        int dia1 = Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);
        double rentDia = rentabilidad(fecha_actual, fecha_actual)[0];
        double estimadoDia = 0;
        jTextFieldRentabilidadDia.setText(FormatoPesos.formato(rentDia - 50000));
        if (rentDia - 50000 > estimadoDia) {
            jTextFieldRentabilidadDia.setBackground(Color.GREEN);
        } else {
            jTextFieldRentabilidadDia.setBackground(Color.RED);
        }
        jTextFieldRentabilidadMes.setText(FormatoPesos.formato(rentabilidad(fechaMes(dia1), fecha_actual)[0]));
        jTextFieldEntraInventario.setText(FormatoPesos.formato(entraInventario()));
        jTextFieldCosto.setText(FormatoPesos.formato(rentabilidad(fecha_actual, fecha_actual)[1]));
        jTextFieldVentaDia.setText(TotalVentas.VentaDia());
        jTextFieldVentaMes.setText(TotalVentas.VentaMes());
        double totalIn = 0;
        double rentEs = 0;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select utilidad,total_cost,cantidad from producto");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentEs += (rs.getDouble(1) * rs.getInt(3));
                totalIn += rs.getDouble(2);
            }
            jTextFieldRentEs.setText(dm1.format(rentEs));
            jTextFieldTotalInv1.setText(dm1.format(totalIn));
            Caja caja = new Caja();
            jTextFieldCapitalTotal.setText(dm1.format(totalIn+caja.cajaTotal()+caja.cuentasTotal()));
            jTextFieldResto.setText(FormatoPesos.formato(totalIn + rentabilidad(fecha_actual, fecha_actual)[1] - entraInventario()));
        } catch (SQLException e) {
            System.err.println("Error en Administracion: " + e);
        }
        egresosReg();
    }

    double rentabilidadDia(Date fecha) {
        double util = -50000;
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select sum(utilidad) from ventas where fecha=?");
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                util += rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return util;
    }

    void egresosReg() {
        try {
            String[] datos = new String[4];
            DefaultTableModel tablaEgresos;
            tablaEgresos = (DefaultTableModel) jTable3.getModel();
            Connection cn = Conexion.Conexion();
            PreparedStatement ps = cn.prepareStatement("Select e.Concepto,e.Valor,c.Total,e.hora from egresos e left join caja c "
                    + "on e.hora=c.Hora where e.fecha=current_date()");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < 4; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                tablaEgresos.addRow(datos);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    }

    public void tabla(JTable j) {
        df = new DefaultTableModel();
        df.addColumn("Cedula");
        df.addColumn("Nombre");
        df.addColumn("Novedad");
        df.addColumn("Valor");

        j.setModel(df);

        TableColumnModel tcm = j.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(800);
        tcm.getColumn(2).setPreferredWidth(600);
        tcm.getColumn(3).setPreferredWidth(298);

        tcm.getColumn(0).setResizable(false);
        tcm.getColumn(1).setResizable(false);
        tcm.getColumn(2).setResizable(false);
        tcm.getColumn(3).setResizable(false);

    }

    public void fecha() {
        jDateChooser1.setDate(fecha_actual);
        jDateChooser2.setDate(fecha_actual);
    }

    public void cerra() {
        try {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    m = 0;
                    System.out.println(m);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void venta() {

        String[] datos = new String[8];
        if (b == true) {
            dm.addColumn("id");
            dm.addColumn("Factura");
            dm.addColumn("Cliente");
            dm.addColumn("Cedula");
            dm.addColumn("Usuario");
            dm.addColumn("Utilidad");
            dm.addColumn("Fecha Venta");
            dm.addColumn("Total");
        }

        jTable2.setModel(dm);

        TableColumnModel columnModel = jTable2.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(100);

        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);
        columnModel.getColumn(5).setResizable(false);
        columnModel.getColumn(6).setResizable(false);
        columnModel.getColumn(7).setResizable(false);
        if (b == true) {
            try {
                Connection cnn = Conexion.Conexion();

                PreparedStatement pre = cnn.prepareStatement("SELECT * FROM ventas WHERE fecha BETWEEN  ? and ?");
                pre.setDate(1, new java.sql.Date(jDateChooser1.getDate().getTime()));
                pre.setDate(2, new java.sql.Date(jDateChooser2.getDate().getTime()));
                ResultSet rs = pre.executeQuery();

                while (rs.next()) {
                    for (int i = 0; i < 8; i++) {
                        datos[i] = rs.getString(i + 1);
                    }
                    dm.addRow(datos);
                }

            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public static void compra() {
        String[] datos = new String[9];
        if (b == true) {
            tabla.addColumn("id");
            tabla.addColumn("Factura");
            tabla.addColumn("Fecha Realizada");
            tabla.addColumn("Fecha Vencimiento");
            tabla.addColumn("Precio");
            tabla.addColumn("Forma de Pago");
            tabla.addColumn("Nit");
            tabla.addColumn("Proveedor");
            tabla.addColumn("Estado");
        }
        jTable1.setModel(tabla);

        TableColumnModel columnModel = jTable1.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(100);

        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);
        columnModel.getColumn(5).setResizable(false);
        columnModel.getColumn(6).setResizable(false);
        columnModel.getColumn(7).setResizable(false);
        if (b == true) {
            try {
                Connection cnn = Conexion.Conexion();

                PreparedStatement pre = cnn.prepareStatement("SELECT * FROM compra");
                ResultSet rs = pre.executeQuery();

                while (rs.next()) {
                    for (int i = 0; i < 9; i++) {
                        datos[i] = rs.getString(i + 1);
                    }
                    tabla.addRow(datos);
                }

            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    /**
     *
     * @return
     */
    public int totalNomina() {
        totalNomina = 0;
        for (int i = 0; i < jTableDevengado.getRowCount(); i++) {
            double valor = Double.parseDouble(jTableDevengado.getValueAt(i, 3).toString());
            totalNomina += (int) valor;
        }
        return totalNomina;
    }

    public void borrar() {
        df = (DefaultTableModel) jTableDevengado.getModel();
        int i = jTableDevengado.getSelectedRow();
        df.removeRow(i);
        jTextTotalNomina.setValue(totalNomina());
    }

    public void nroNomina() {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select max(NroNomina) from nomina");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int i = rs.getInt(1);
                if (i == 0) {
                    jLabelNroNomina.setText("1");
                } else {
                    i++;
                    jLabelNroNomina.setText("" + i);
                }
            }

            jTextTotalNomina.setValue(totalNomina);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void abrirCMD(String disco_duro, String ruta) {
        try {
            ProcessBuilder p = new ProcessBuilder();
            p.command("cmd.exe", disco_duro, ruta);
            p.start();
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void limpiar(JTable x, DefaultTableModel y) {
        for (int i = 0; i < x.getRowCount(); i++) {
            y.removeRow(i);
            i--;
        }
    }

    public void total(JTable tabla, JTextField total, int columna) {
        double t = 0;
        double n;
        for (int i = 0; i < tabla.getRowCount(); i++) {
            n = Double.parseDouble(tabla.getValueAt(i, columna).toString().replace(",", ""));
            t += n;
        }
        total.setText(dm1.format(t));
    }

    public void buscarT(String columna, int n) {
        String[] datos = new String[9];
        switch (n) {
            case 1 -> {
                try {
                    Connection cnn = Conexion.Conexion();

                    PreparedStatement pre = cnn.prepareStatement("select * from compra where " + columna + " like '%" + jTextFieldBuscar.getText().trim() + "%'");
                    ResultSet rs = pre.executeQuery();

                    while (rs.next()) {
                        for (int i = 0; i < 9; i++) {
                            datos[i] = rs.getString(i + 1);
                        }
                        tabla.addRow(datos);
                    }

                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
            case 2 -> {
                try {
                    Connection cnn = Conexion.Conexion();

                    PreparedStatement pre = cnn.prepareStatement("SELECT * FROM ventas WHERE fecha BETWEEN  ? and ?");
                    pre.setDate(1, new java.sql.Date(jDateChooser1.getDate().getTime()));
                    pre.setDate(2, new java.sql.Date(jDateChooser2.getDate().getTime()));
                    ResultSet rs = pre.executeQuery();
                    System.out.println(new java.sql.Date(jDateChooser1.getDate().getTime()));
                    while (rs.next()) {
                        for (int i = 0; i < 8; i++) {
                            datos[i] = rs.getString(i + 1);
                        }
                        dm.addRow(datos);
                    }

                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextFieldBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel4 = new Fondo("FondoMenu.jpg");
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTotalVenta = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new Fondo("FondoMenu.jpg");
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabelSP = new javax.swing.JLabel();
        jLabelAseo = new javax.swing.JLabel();
        jLabelComida = new javax.swing.JLabel();
        jLabelAlisson = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabelTotalEgresos = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabelVarios = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabelServicio = new javax.swing.JLabel();
        jLabelTotal1 = new javax.swing.JLabel();
        jLabelTotalEgresosDia = new javax.swing.JLabel();
        jPanel3 = new Fondo("FondoMenu.jpg");
        jLabel39 = new javax.swing.JLabel();
        jFormattedTextFieldFetcha = new javax.swing.JFormattedTextField();
        jLabel40 = new javax.swing.JLabel();
        jDateChooser_fechaI = new com.toedter.calendar.JDateChooser();
        jLabel41 = new javax.swing.JLabel();
        jDateChooser_fechaF = new com.toedter.calendar.JDateChooser();
        jLabel42 = new javax.swing.JLabel();
        jTextFieldCCN = new javax.swing.JTextField();
        jLabelNombreN = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabelNroNomina = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDevengado = new javax.swing.JTable();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldNovedad = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextFieldValor = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDeduccion = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        jTextFieldNovedad1 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTextFieldValor1 = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jTextFieldEmpleado = new javax.swing.JTextField();
        jLabelNombreE = new javax.swing.JLabel();
        jButtonBuscar1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jTextTotalNomina = new javax.swing.JFormattedTextField();
        jPanel7 = new Fondo("FondoMenu.jpg");
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldCedulaCredito = new javax.swing.JTextField();
        jLabelNombre = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTextField10 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldCosto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldRentabilidadVentas = new javax.swing.JTextField();
        jTextFieldTotalInv1 = new javax.swing.JTextField();
        jTextFieldVentaMes = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldTotalVentas1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldRentabilidadMes = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldVentaDia = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldRentabilidadDia = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldRentEs = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldCapitalTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldResto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldRentabilidadDiaAnt = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextFieldEntraInventario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane2.setBackground(null);
        jTabbedPane2.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane2.setToolTipText("");
        jTabbedPane2.setAutoscrolls(true);
        jTabbedPane2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jPanel5.setOpaque(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fecha Realizada", "Fecha Vencida", "Proveedor", "Nro Factura" }));

        jTextFieldBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldBuscarKeyReleased(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(0, 102, 255));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nro Factura", "Fecha Realizada", "Fecha Vencida", "Precio", "Forma de Pago", "Proveedor", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Total:");

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        jTabbedPane2.addTab("Facturas Compras", jPanel5);

        jPanel4.setOpaque(false);

        jScrollPane2.setBackground(null);
        jScrollPane2.setOpaque(false);

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nro Factura", "Fecha Realizada", "Fecha Vencida", "Precio", "Forma de Pago", "Proveedor", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total:");

        jTextFieldTotalVenta.setEditable(false);

        jDateChooser1.setDateFormatString("y-MM-d");

        jDateChooser2.setDateFormatString("y-MM-d");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("-");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Generar Reporte");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1454, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(17, 17, 17))))
        );

        jTabbedPane2.addTab("Facturas Ventas", jPanel4);

        jPanel2.setOpaque(false);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Gastos");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Nomina Empleados");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Registros");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Servicios Publicos ");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Elementos Aseo");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Servicios Tecnicos ");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Comida");

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField5.setText("jTextField5");
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Energia", "Agua", "Internet" }));

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.setText("jTextField3");
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jTextField22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField22.setText("jTextField1");

        jTextField18.setEditable(false);
        jTextField18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField4.setText("jTextField4");
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField8.setText("jTextField3");

        jTextField9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Valor", "Total Caja", "Hora"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(300);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
        }

        jButton6.setText("jButton6");
        jButton6.setAlignmentX(0.5F);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("jButton6");
        jButton7.setAlignmentX(0.5F);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("jButton6");
        jButton8.setAlignmentX(0.5F);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("jButton6");
        jButton9.setAlignmentX(0.5F);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Alisson");

        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jTextField15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField15KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });

        jButton10.setText("jButton6");
        jButton10.setAlignmentX(0.5F);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Servicios Publicos");

        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Aseo");

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Comida");

        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Alisson");

        jLabelSP.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSP.setText("jLabel22");

        jLabelAseo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAseo.setText("jLabel23");

        jLabelComida.setForeground(new java.awt.Color(255, 255, 255));
        jLabelComida.setText("jLabel24");

        jLabelAlisson.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAlisson.setText("jLabel30");

        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Varios");

        jLabelTotalEgresos.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotalEgresos.setText("jLabel23");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Varios");

        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
        });

        jTextField17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField17KeyTyped(evt);
            }
        });

        jButton11.setText("jButton6");
        jButton11.setAlignmentX(0.5F);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabelVarios.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVarios.setText("jLabel23");

        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTotal.setText("Total:");

        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Servicio Tecnico");

        jLabelServicio.setForeground(new java.awt.Color(255, 255, 255));
        jLabelServicio.setText("jLabel24");

        jLabelTotal1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTotal1.setText("Total Dia:");

        jLabelTotalEgresosDia.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotalEgresosDia.setText("jLabel23");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField18)
                                    .addComponent(jTextField9)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField3)
                                        .addComponent(jTextField4)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(861, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelSP)
                                            .addComponent(jLabelServicio)
                                            .addComponent(jLabelAseo))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabelComida)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabelTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelAlisson)
                                    .addComponent(jLabelVarios)
                                    .addComponent(jLabelTotalEgresos)
                                    .addComponent(jLabelTotalEgresosDia))
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton10)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton11)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabelSP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabelAseo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabelServicio)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAlisson)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabelVarios))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotalEgresos)
                            .addComponent(jLabelTotal))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelTotalEgresosDia)
                        .addComponent(jLabelTotal1))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabelComida)))
                .addGap(50, 50, 50))
        );

        jButton10.setContentAreaFilled(false);
        jButton10.setContentAreaFilled(false);

        jTabbedPane2.addTab("Resumen Egresos", jPanel2);

        jPanel3.setOpaque(false);

        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Fecha:");

        jFormattedTextFieldFetcha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));

        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Fecha Inicial:");

        jDateChooser_fechaI.setDateFormatString("dd/MM/yyyy\n");
        jDateChooser_fechaI.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Fecha Final:");

        jDateChooser_fechaF.setDateFormatString("dd/MM/yyyy\n");
        jDateChooser_fechaF.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Elaborado Por:");

        jTextFieldCCN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCCNKeyPressed(evt);
            }
        });

        jLabelNombreN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelNombreN.setForeground(new java.awt.Color(255, 255, 255));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("NOM");

        jLabelNroNomina.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelNroNomina.setForeground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        jScrollPane4.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane4.setOpaque(false);

        jTableDevengado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jTableDevengado.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableDevengado.getTableHeader().setReorderingAllowed(false);
        jTableDevengado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableDevengadoKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTableDevengado);

        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Novedad:");

        jTextFieldNovedad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNovedadKeyPressed(evt);
            }
        });

        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Valor:");

        jTextFieldValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldValorKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1456, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jTextFieldNovedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46)
                    .addComponent(jTextFieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Devengado", jPanel6);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        jTableDeduccion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableDeduccion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableDeduccion.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTableDeduccion);

        jLabel48.setBackground(new java.awt.Color(255, 255, 255));
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Novedad:");

        jTextFieldNovedad1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNovedad1KeyPressed(evt);
            }
        });

        jLabel49.setBackground(new java.awt.Color(255, 255, 255));
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Valor:");

        jTextFieldValor1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldValor1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1456, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNovedad1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldValor1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jTextFieldNovedad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(jTextFieldValor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Deduccion", jPanel8);

        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Empleado:");

        jTextFieldEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldEmpleadoKeyPressed(evt);
            }
        });

        jLabelNombreE.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelNombreE.setForeground(new java.awt.Color(255, 255, 255));

        jButtonBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscar1ActionPerformed(evt);
            }
        });

        jButton4.setText("Guardar");

        jLabel47.setText("Total:");

        jTextTotalNomina.setEditable(false);
        jTextTotalNomina.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$#,###"))));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCCN, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextFieldFetcha, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNombreE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButtonBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNombreN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(22, 22, 22)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser_fechaI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser_fechaF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelNroNomina, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextTotalNomina, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jLabelNroNomina, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser_fechaF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jDateChooser_fechaI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel40)))
                        .addGap(104, 104, 104)
                        .addComponent(jTabbedPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(jTextTotalNomina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(jButton4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(jFormattedTextFieldFetcha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldCCN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelNombreN, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelNombreE, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Nomina", jPanel3);

        jPanel7.setOpaque(false);

        jTabbedPane3.setBackground(null);
        jTabbedPane3.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel10.setBackground(null);
        jPanel10.setForeground(null);
        jPanel10.setOpaque(false);

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("CC:");

        jTextFieldCedulaCredito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCedulaCreditoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCedulaCreditoKeyReleased(evt);
            }
        });

        jLabelNombre.setForeground(new java.awt.Color(255, 255, 255));

        jLabelTelefono.setForeground(new java.awt.Color(255, 255, 255));

        jTable4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nro Factura", "CC", "Valor", "Fecha", "Saldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setRowHeight(26);
        jTable4.setRowMargin(3);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            jTable4.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable4.getColumnModel().getColumn(1).setResizable(false);
            jTable4.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTable4.getColumnModel().getColumn(2).setResizable(false);
            jTable4.getColumnModel().getColumn(2).setPreferredWidth(400);
            jTable4.getColumnModel().getColumn(3).setResizable(false);
            jTable4.getColumnModel().getColumn(3).setPreferredWidth(250);
            jTable4.getColumnModel().getColumn(4).setResizable(false);
            jTable4.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField10.setText("$0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextFieldCedulaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabelNombre)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabelTelefono))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldCedulaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNombre)
                    .addComponent(jLabelTelefono))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Credito Cliente", jPanel10);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Mis Creditos", jPanel9);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Creditos", jPanel7);

        jPanel11.setOpaque(false);

        jScrollPane7.setOpaque(false);

        jTable5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Rentabilidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setRowHeight(40);
        jTable5.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(jTable5);
        if (jTable5.getColumnModel().getColumnCount() > 0) {
            jTable5.getColumnModel().getColumn(0).setResizable(false);
            jTable5.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTable5.getColumnModel().getColumn(1).setResizable(false);
            jTable5.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jDateChooser3.setDateFormatString("yyyy-MM-dd");

        jDateChooser4.setDateFormatString("yyyy-MM-dd");

        jLabel24.setText("-");

        jButton12.setText("Buscar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTabbedPane4.setForeground(new java.awt.Color(255, 255, 255));

        jPanel12.setOpaque(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 922, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Flujo de Riqueza", jPanel12);

        jPanel13.setOpaque(false);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 922, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Flujo de Fondos", jPanel13);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12))
                    .addComponent(jScrollPane7))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jButton12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Rentabilidad", jPanel11);

        jPanel1.setOpaque(false);

        jTextFieldCosto.setEditable(false);
        jTextFieldCosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldCosto.setText("jTextField1");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Capital Inventario:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Salida Inventario:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total ventas:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Rentabilidad Estimadas Inventario:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Rentabilidad de ventas:");

        jTextFieldRentabilidadVentas.setEditable(false);
        jTextFieldRentabilidadVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldRentabilidadVentas.setText("jTextField1");

        jTextFieldTotalInv1.setEditable(false);
        jTextFieldTotalInv1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldTotalInv1.setText("jTextField1");

        jTextFieldVentaMes.setEditable(false);
        jTextFieldVentaMes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldVentaMes.setText("jTextField1");

        jTextField6.setEditable(false);
        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextField6.setText("jTextField1");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Ventas Ultimo mes:");

        jTextFieldTotalVentas1.setEditable(false);
        jTextFieldTotalVentas1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldTotalVentas1.setText("jTextField1");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Rentabilidad mes");

        jTextFieldRentabilidadMes.setEditable(false);
        jTextFieldRentabilidadMes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldRentabilidadMes.setText("jTextField1");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Ventas Ultimo Dia :");

        jTextFieldVentaDia.setEditable(false);
        jTextFieldVentaDia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldVentaDia.setText("jTextField1");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Rentabilidad Dia:");

        jTextFieldRentabilidadDia.setEditable(false);
        jTextFieldRentabilidadDia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldRentabilidadDia.setText("jTextField1");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Inventario Inicio Dia:");

        jTextFieldRentEs.setEditable(false);
        jTextFieldRentEs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldRentEs.setText("jTextField1");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Capital total:");

        jTextFieldCapitalTotal.setEditable(false);
        jTextFieldCapitalTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldCapitalTotal.setText("jTextField1");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Capital efectivo");

        jTextFieldResto.setEditable(false);
        jTextFieldResto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldResto.setText("jTextField1");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Rentabilidad Dia Anterior");

        jTextFieldRentabilidadDiaAnt.setEditable(false);
        jTextFieldRentabilidadDiaAnt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldRentabilidadDiaAnt.setText("jTextField1");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Entra Inventario:");

        jTextFieldEntraInventario.setEditable(false);
        jTextFieldEntraInventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextFieldEntraInventario.setText("jTextField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(373, 373, 373)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldRentEs, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldTotalVentas1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldRentabilidadDia, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldRentabilidadVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jTextFieldVentaDia, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVentaMes, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldResto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldEntraInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldCapitalTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldRentabilidadMes, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldRentabilidadDiaAnt, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldTotalInv1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCapitalTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRentEs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldResto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldEntraInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTotalInv1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRentabilidadVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTotalVentas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVentaDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRentabilidadDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldVentaMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRentabilidadMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRentabilidadDiaAnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(531, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Administración", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscar1ActionPerformed

    }//GEN-LAST:event_jButtonBuscar1ActionPerformed

    private void jTextFieldEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEmpleadoKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pr = cn.prepareStatement("Select idusuarios,nombre from usuarios where cedula = ?");
                pr.setString(1, jTextFieldEmpleado.getText().trim());
                ResultSet rs = pr.executeQuery();
                while (rs.next()) {
                    jLabelNombreE.setText(rs.getString(2));
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_jTextFieldEmpleadoKeyPressed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jTextFieldValor1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldValor1KeyPressed
        df = (DefaultTableModel) jTableDeduccion.getModel();
        if (!Validaciones.validarEnter(evt)) {
            String[] datos = new String[4];
            datos[0] = jTextFieldEmpleado.getText().trim();
            datos[1] = jLabelNombreE.getText().trim();
            datos[2] = jTextFieldNovedad1.getText().trim();
            datos[3] = jTextFieldValor1.getText().trim();
            df.addRow(datos);

        }
    }//GEN-LAST:event_jTextFieldValor1KeyPressed

    private void jTextFieldNovedad1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNovedad1KeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldValor1.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNovedad1KeyPressed

    private void jTextFieldValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldValorKeyPressed
        df = (DefaultTableModel) jTableDevengado.getModel();
        if (!Validaciones.validarEnter(evt)) {
            String[] datos = new String[4];
            datos[0] = jTextFieldEmpleado.getText().trim();
            datos[1] = jLabelNombreE.getText().trim();
            datos[2] = jTextFieldNovedad.getText().trim();
            datos[3] = jTextFieldValor.getText().trim();
            df.addRow(datos);
            jTextTotalNomina.setValue(totalNomina());
        }
    }//GEN-LAST:event_jTextFieldValorKeyPressed

    private void jTextFieldNovedadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNovedadKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldValor.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNovedadKeyPressed

    private void jTableDevengadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableDevengadoKeyPressed
        if (!Validaciones.validarSuprimir(evt)) {
            borrar();
        }
    }//GEN-LAST:event_jTableDevengadoKeyPressed

    private void jTextFieldCCNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCCNKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pr = cn.prepareStatement("Select idusuarios,nombre from usuarios where cedula = ?");
                pr.setString(1, jTextFieldCCN.getText().trim());
                ResultSet rs = pr.executeQuery();
                while (rs.next()) {
                    jLabelNombreN.setText(rs.getString(2));
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_jTextFieldCCNKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int i = jTable2.getRowCount();
        if (i > 0) {
            JasperReport jr = null;
            String file = "src/Clases/reporte.jasper";
            try {
                Connection cn = Conexion.Conexion();
                Map parametro = new HashMap();
                parametro.put("Fecha_Inicial", jDateChooser1.getDate());
                parametro.put("FechaFinal", jDateChooser2.getDate());
                parametro.put("Total", jTextFieldTotalVenta.getText());
                jr = (JasperReport) JRLoader.loadObjectFromFile(file);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
                JasperViewer jv = new JasperViewer(jp, false);
                jv.setVisible(true);
                jv.setTitle("Reporte Ventas");
            } catch (JRException e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        limpiar(jTable2, dm);
        buscarT("fecha_venta", 2);
        total(jTable2, jTextFieldTotalVenta, 5);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int fila = jTable2.rowAtPoint(evt.getPoint());
        if (fila > -1) {
            m = 1;
            nro = jTable2.getValueAt(fila, 1).toString();

            new Ventas().setVisible(true);
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int fila = jTable1.rowAtPoint(evt.getPoint());
        if (fila > -1) {
            m=1;
            nro = jTable1.getValueAt(fila, 1).toString();
            
            new Compras().setVisible(true);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextFieldBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBuscarKeyReleased
        limpiar(jTable1, tabla);
        int box = jComboBox1.getSelectedIndex();
        switch (box) {
            case 0 ->
                buscarT("fecha_factura", 1);
            case 1 ->
                buscarT("fecha_vencimiento", 1);
            case 2 ->
                buscarT("proveedor", 1);
            case 3 ->
                buscarT("numero_factura", 1);
        }
        total(jTable1, jTextField2, 4);
    }//GEN-LAST:event_jTextFieldBuscarKeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        numero(evt);

    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        numero(evt);
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        numero(evt);
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        numero(evt);
    }//GEN-LAST:event_jTextField7KeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane2.setSelectedIndex(3);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String Concepto = jComboBox4.getSelectedItem().toString();
        if (!Concepto.equals("Seleccionar") && !jTextField3.getText().equals("Valor")) {
            double valor = Double.parseDouble(jTextField3.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 1);
            jTextField3.setText("");
            jComboBox4.setSelectedIndex(0);
            jTextField3.requestFocus();
            jComboBox4.requestFocus();
        } else if (jTextField3.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField3.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Selecione una Opcion", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jComboBox4.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        formato(jTextField3);
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        formato(jTextField4);
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        formato(jTextField5);
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        formato(jTextField7);
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String Concepto = jTextField8.getText();
        if (!Concepto.equals("Concepto") && !jTextField4.getText().equals("Valor")) {

            double valor = Double.parseDouble(jTextField4.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 2);

            jTextField8.setText("");
            jTextField4.setText("");

            jTextField4.requestFocus();
            jTextField8.requestFocus();
        } else if (jTextField4.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField4.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Campo Vacio", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jTextField8.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String Concepto = jTextField22.getText();
        if (!Concepto.equals("Concepto") && !jTextField5.getText().equals("Valor")) {

            double valor = Double.parseDouble(jTextField5.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 3);
            jTextField22.setText("");
            jTextField5.setText("");

            jTextField5.requestFocus();
            jTextField22.requestFocus();
        } else if (jTextField5.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField5.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Campo Vacio", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jTextField22.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String Concepto = jTextField9.getText();
        if (!Concepto.equals("Concepto") && !jTextField7.getText().equals("Valor")) {
            double valor = Double.parseDouble(jTextField7.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 4);
            jTextField9.setText("");
            jTextField7.setText("");
            jTextField7.requestFocus();
            jTextField9.requestFocus();
        } else if (jTextField7.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField7.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Campo vacio", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jTextField9.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextFieldCedulaCreditoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCedulaCreditoKeyPressed


    }//GEN-LAST:event_jTextFieldCedulaCreditoKeyPressed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        if (evt.getClickCount() == 2) {

        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTextFieldCedulaCreditoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCedulaCreditoKeyReleased
        limpiar(jTable4, (DefaultTableModel) jTable4.getModel());
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select c.nombres,c.celular,c.saldo,v.nroVentas,v.precio_Total,v.fecha,v.saldo,v.cedula_cliente from clientes c join ventas v on "
                    + "c.cedula=v.cedula_cliente where v.FormaPago='Credito' and c.cedula like ?");
            pr.setString(1, "%" + jTextFieldCedulaCredito.getText() + "%");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                jLabelNombre.setText(rs.getString(1));
                jLabelTelefono.setText(rs.getString(2));
                String[] datos = new String[5];
                datos[0] = rs.getString(4);
                datos[1] = rs.getString(8);
                datos[2] = rs.getString(5);
                datos[3] = rs.getString(6);
                datos[4] = rs.getString(7);
                DefaultTableModel df = (DefaultTableModel) jTable4.getModel();
                df.addRow(datos);
                jTable4.setModel(df);
            }
            total(jTable4, jTextField10, 2);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_jTextFieldCedulaCreditoKeyReleased

    private void jTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyReleased
        formato(jTextField15);
    }//GEN-LAST:event_jTextField15KeyReleased

    private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
        numero(evt);
    }//GEN-LAST:event_jTextField15KeyTyped

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String Concepto = jTextField11.getText();
        if (!Concepto.equals("Concepto") && !jTextField15.getText().equals("Valor")) {
            double valor = Double.parseDouble(jTextField15.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 5);
            jTextField11.setText("");
            jTextField15.setText("");
            jTextField11.requestFocus();
            jTextField15.requestFocus();
        } else if (jTextField11.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField11.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Campo vacio", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jTextField15.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        formato(jTextField15);
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        formato(jTextField15);
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
        formato(jTextField15);
    }//GEN-LAST:event_jTextField17KeyReleased

    private void jTextField17KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyTyped
        numero(evt);
    }//GEN-LAST:event_jTextField17KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String Concepto = jTextField16.getText();
        if (!Concepto.equals("Concepto") && !jTextField17.getText().equals("Valor")) {
            double valor = Double.parseDouble(jTextField17.getText().trim().replace(",", ""));
            egreso(Concepto, valor, 6);
            jTextField16.setText("");
            jTextField17.setText("");
            jTextField16.requestFocus();
            jTextField17.requestFocus();
        } else if (jTextField16.getText().equals("Valor")) {
            JOptionPane.showMessageDialog(this, "No ingreso un precio");
            jTextField16.setBackground(Color.red);
        } else {
            JOptionPane.showMessageDialog(null, "Campo vacio", "No se Selecciono un Servicio", JOptionPane.ERROR_MESSAGE);
            jTextField17.setBackground(Color.red);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        limpiar(jTable5, (DefaultTableModel) jTable5.getModel());

        Date fecha1 = jDateChooser3.getDate();
        Date fecha2 = fechaDia(jDateChooser4.getDate());
        rent(fecha1, fecha2);
        jPanel12.removeAll();
        jPanel13.removeAll();
        try {
            graficar();
            graficarBarra();
        } catch (ParseException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    public void graficar() throws ParseException {
        double max = 0;
        double min = 0;
        XYSeries series = new XYSeries("Rentabilidad");
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        double valor = 0;
        XYSeriesCollection dataset = new XYSeriesCollection();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < jTable5.getRowCount(); i++) {
            valor += Double.parseDouble(jTable5.getValueAt(i, 1).toString().replace(",", ""));
            series.add(new Day(sdf.parse(jTable5.getValueAt(i, 0).toString())).getMiddleMillisecond(), valor);
            datos.addValue(Double.parseDouble(jTable5.getValueAt(i, 1).toString().replace(",", "")), jTable5.getValueAt(i, 0).toString(), "Renta");
            if (valor > max) {
                max = valor;
            }
            if (min > valor) {
                min = valor;
            }
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart("Rentabilidad", // Título
                "Tiempo (x)", // Etiqueta Coordenada X
                "Valor", dataset, // Datos
                PlotOrientation.VERTICAL,
                true, // Muestra la leyenda de los productos (Producto A)
                true,
                false
        );

        chart.removeLegend();

        XYDifferenceRenderer renderer = new XYDifferenceRenderer();
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRenderer(renderer);
        StandardChartTheme chartTheme = (StandardChartTheme) StandardChartTheme.createDarknessTheme();
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setBackgroundPaint(Color.BLACK);
////        renderer.setUseFillPaint(true);
////        renderer.setSeriesShapesFilled(0, true);
////        renderer.setSeriesShapesVisible(0, false);
//        renderer.setSeriesPaint(0, Color.GRAY);
//        renderer.setBaseItemLabelPaint(Color.WHITE);

        DateAxis xAxis = new DateAxis("Fecha");
        xAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        plot.setDomainAxis(xAxis);

        chartTheme.apply(chart);
        // Mostramos la grafica en pantalla
        ChartPanel frame = new ChartPanel(chart);
        frame.setMouseWheelEnabled(true);
        jPanel12.setLayout(
                new java.awt.BorderLayout());
        jPanel12.add(frame);

        jPanel12.validate();
    }

    public void graficarBarra() throws ParseException {
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        double valor = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < jTable5.getRowCount(); i++) {
            valor = Double.parseDouble(jTable5.getValueAt(i, 1).toString().replace(",", ""));
            datos.addValue(Double.parseDouble(jTable5.getValueAt(i, 1).toString().replace(",", "")), jTable5.getValueAt(i, 0).toString(), "Renta");

        }

        JFreeChart chart = ChartFactory.createBarChart("Rentabilidad", // Título
                "Tiempo (x)", // Etiqueta Coordenada X
                "Valor", datos, // Datos
                PlotOrientation.VERTICAL,
                true, // Muestra la leyenda de los productos (Producto A)
                true,
                false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRenderer(new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                double v = (double) plot.getDataset().getValue(row, column);
                if (v < 0) {
                    return Color.red;
                } else {
                    return Color.GREEN;
                }
            }

        });

        chart.removeLegend();

        chart.setBackgroundPaint(Color.white);
        StandardChartTheme chartTheme = (StandardChartTheme) StandardChartTheme.createDarknessTheme();
        chartTheme.apply(chart);

        // Mostramos la grafica en pantalla
        ChartPanel frame = new ChartPanel(chart);

        jPanel13.setLayout(
                new java.awt.BorderLayout());
        jPanel13.add(frame);

        jPanel13.validate();
    }

//validar si se tipea un numero
    void numero(KeyEvent evt) {
        if (Validaciones.validarString(evt)) {
            evt.consume();
        }
    }

    void jscroll() {
        jScrollPane1.setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;

                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;

                Rectangle vsbR = new Rectangle();
                vsbR.width = 12;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
        jScrollPane1.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar) c;
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                } else if (isDragging) {
                    color = new Color(0, 0, 255);
                } else if (isThumbRollover()) {
                    color = new Color(0, 102, 255);
                } else {
                    color = new Color(51, 153, 255);
                }
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.BLACK);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
    }

    void formato(JTextField jt) {
        if (!jt.getText().equals("Valor") && !jt.getText().equals("")) {
            jt.setText(FormatoPesos.formato(Double.parseDouble(jt.getText().trim().replace(",", "").trim())));
        }
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonBuscar1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox4;
    private static com.toedter.calendar.JDateChooser jDateChooser1;
    private static com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser_fechaF;
    private com.toedter.calendar.JDateChooser jDateChooser_fechaI;
    private javax.swing.JFormattedTextField jFormattedTextFieldFetcha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAlisson;
    private javax.swing.JLabel jLabelAseo;
    private javax.swing.JLabel jLabelComida;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelNombreE;
    private javax.swing.JLabel jLabelNombreN;
    private javax.swing.JLabel jLabelNroNomina;
    private javax.swing.JLabel jLabelSP;
    private javax.swing.JLabel jLabelServicio;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotal1;
    private javax.swing.JLabel jLabelTotalEgresos;
    private javax.swing.JLabel jLabelTotalEgresosDia;
    private javax.swing.JLabel jLabelVarios;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private static javax.swing.JTable jTableDeduccion;
    private javax.swing.JTable jTableDevengado;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextFieldBuscar;
    private javax.swing.JTextField jTextFieldCCN;
    private javax.swing.JTextField jTextFieldCapitalTotal;
    private javax.swing.JTextField jTextFieldCedulaCredito;
    private javax.swing.JTextField jTextFieldCosto;
    private javax.swing.JTextField jTextFieldEmpleado;
    private javax.swing.JTextField jTextFieldEntraInventario;
    private javax.swing.JTextField jTextFieldNovedad;
    private javax.swing.JTextField jTextFieldNovedad1;
    private javax.swing.JTextField jTextFieldRentEs;
    private javax.swing.JTextField jTextFieldRentabilidadDia;
    private javax.swing.JTextField jTextFieldRentabilidadDiaAnt;
    private javax.swing.JTextField jTextFieldRentabilidadMes;
    private javax.swing.JTextField jTextFieldRentabilidadVentas;
    private javax.swing.JTextField jTextFieldResto;
    private javax.swing.JTextField jTextFieldTotalInv1;
    private javax.swing.JTextField jTextFieldTotalVenta;
    private javax.swing.JTextField jTextFieldTotalVentas1;
    private javax.swing.JTextField jTextFieldValor;
    private javax.swing.JTextField jTextFieldValor1;
    private javax.swing.JTextField jTextFieldVentaDia;
    private javax.swing.JTextField jTextFieldVentaMes;
    private javax.swing.JFormattedTextField jTextTotalNomina;
    // End of variables declaration//GEN-END:variables
}
