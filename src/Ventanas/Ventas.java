package Ventanas;

import Clases.ActualizarCantidad;
import Clases.Conexion;
import Clases.Errores;
import Clases.Fechas;
import Clases.Fondo;
import Clases.FormatoPesos;
import Clases.FormatoTablas;
import Clases.ImagenBoton;
import Clases.Imagenes;
import Clases.Imprimir;
import Clases.ShortCut;
import Clases.Validaciones;
import Clases.Utilidad;
import static Ventanas.Administrador.jProgressBar1;
import java.awt.Color;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author harol
 */
public final class Ventas extends javax.swing.JFrame {

    public static boolean m = false;
    public static boolean n;
    FormatoTablas ft = new FormatoTablas();
    static ArrayList idEmp = new ArrayList();
    static ArrayList utilidaTotal = new ArrayList();
    static ArrayList Servicio = new ArrayList();

    public Ventas() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        idEmp.add(0);
        n = true;
        this.setContentPane(fondo);
        initComponents();
        llenarEmpleado();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        new ImagenBoton("vender.png", jButtonVender, 43, 43);
        jButtonBuscando.setContentAreaFilled(false);
        ImageIcon imagen1 = new ImageIcon("src/imagenes/carrito-de-compras.png");
        new Imagenes("buscando.png", jLabelBuscar);
        new Imagenes("Adelante.png", jLabelRegresar1);
        new Imagenes("Atras.png", jLabelRegresar);
        new Imagenes("imprimir.png", jLabelImprimir);
        new ImagenBoton("buscando.png", jButtonBuscando, 38, 38);
        jLabelFecha.setText(Fechas.fechaActual());
        jLabelSaldo.setText("0");
        jTextFieldTotal1.setText("0");
        this.setLocationRelativeTo(null);
        if (Reportes.m == 1) {
            jLabelImprimir.setVisible(true);
            jLabelNoVenta.setText("" + Reportes.nro);
        } else {
            jLabelImprimir.setVisible(false);
            nroVenta();
        }
        jScrollPane2.getViewport().setBackground(new Color(51, 153, 255));
        tamañoColumna();
        jTableVenta.setDefaultRenderer(Object.class, ft);
        reportes();
        cerra();
        eventotabla();

    }

    public void eventotabla() {
        jTableVenta.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int columna = e.getColumn();
                int row = e.getLastRow();
                if (columna == 2) {
                    cambiarCant(row);
                    total();
                } else if (columna == 4) {
                    cambiarCant(row);
                    total();
                } else if (columna == 3) {
                    descuento(row);
                }
            }
        });
    }

    public void descuento(int row) {
        double por = Double.parseDouble(jTableVenta.getValueAt(row, 3).toString());
        double precio = Double.parseDouble(jTableVenta.getValueAt(row, 2).toString());
        por = 1 - por / 100;
        String total = String.valueOf((int) precio * por);
        jTableVenta.setValueAt(total, row, 5);
        total();
    }

    public void cerra() {
        try {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                    Object[] opc = new Object[]{"SI", "NO"};
                    int i = JOptionPane.showOptionDialog(null, "Desea Cancelar Venta?", "Salir de Ventas", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
                    if (i == 0) {
                        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        m = false;
                        Reportes.m = 0;
                        utilidaTotal.clear();
                        Servicio.clear();
                        n = false;
                        limpiar();
                    } else {
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error al cerrar venta: " + e);
        }
    }

    public void reportes() {
        if (Reportes.m == 1) {
            String NroVentas = jLabelNoVenta.getText();
            buscarDetalle(NroVentas);
            buscarVentas(NroVentas);
        }
    }

    public static void buscarDetalle(String NroVenta) {
        limpiar();
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        String[] datos = new String[6];
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr2 = cn.prepareStatement("select codigo,producto,precioUnitario,Descuento,cantidad,PrecioTotal from detallesventa where nro_venta = ?");
            pr2.setString(1, NroVenta);
            ResultSet rs2 = pr2.executeQuery();
            while (rs2.next()) {
                for (int i = 0; i < datos.length; i++) {
                    datos[i] = rs2.getString(i + 1);
                }
                tabla.addRow(datos);

            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void buscarVentas(String NroVenta) {
        try {
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select v.*,c.saldo,c.celular from ventas v left join clientes c on cedula_cliente=c.cedula  where nroVentas = ?");
            pr.setString(1, NroVenta);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                jLabelFecha.setText(rs.getString(7));
                jTextFieldTotal.setText(rs.getString(8));
                jTextFieldCedula.setText(rs.getString(4));
                jLabelTelefono.setText(rs.getString(19));
                jLabelSaldo.setText(rs.getString(18));
                jTextFieldNombre.setText(rs.getString(3));
                jTextFieldPlaca.setText(rs.getString(13));
                jTextFieldMoto.setText(rs.getString(12));
                jTextFieldColor.setText(rs.getString(14));
                jTextArea1.setText(rs.getString(16));
                jComboBox1.setSelectedIndex(rs.getInt(18));
            }

            jTextFieldCodigo.setEditable(false);
            jTextFieldCedula.setEditable(false);
            jTextFieldNombre.setEditable(false);
            jTextFieldTotal.setEditable(false);
            cn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public final void tamañoColumna() {
        DefaultTableModel tabla = new DefaultTableModel() {
            boolean[] m = new boolean[]{
                false, true, true, true, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return m[columnIndex];
            }
        };
        tabla.addColumn("Codigo");
        tabla.addColumn("Producto");
        tabla.addColumn("Precio Unidad");
        tabla.addColumn("Descuento");
        tabla.addColumn("Cantidad");
        tabla.addColumn("Total");

        jTableVenta.setEditingColumn(-1);

        jTableVenta.setModel(tabla);
        TableColumnModel columnModel = jTableVenta.getColumnModel();
        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);
        columnModel.getColumn(5).setResizable(false);

        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(50);
        columnModel.getColumn(5).setPreferredWidth(100);

    }

    public static int nroVenta() {
        int nro_venta = 0;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroVentas) from ventas");
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                nro_venta = rs.getInt(1);
                nro_venta++;
                jLabelNoVenta.setText("" + nro_venta);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return nro_venta;
    }

    public void vender() {
        new FormaPago(this, true).setVisible(true);
        if (FormaPago.m) {
            Object[] opc = new Object[]{"Ticket", "Carta", "NO"};
            int i = JOptionPane.showOptionDialog(null, "¿Desea imprimir factura?", "salir", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
            if (i == 0) {
                new Imprimir().imprimir1();
            } else if (i == 1) {
                new Imprimir().imprimir2();
            }
            limpiar();
            nroVenta();
            Administrador.ventas();
        }
    }

    public static void total() {
        double t = 0;
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            t += Double.parseDouble(jTableVenta.getValueAt(i, 5).toString());
        }
        jTextFieldTotal.setText(FormatoPesos.formato(t));
    }

    public static void servicio() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();

        try {
            String codigo = jTextFieldCodigo.getText().trim();
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select codigo,Concepto,Valor from servicio where codigo = ?");
            pre.setString(1, codigo);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int i = tabla(rs.getString(1));
                if (i >= 0) {
                    int cant = Integer.parseInt(jTableVenta.getValueAt(i, 4).toString());
                    cant++;
                    jTableVenta.setValueAt(cant, i, 4);
                    total();
                } else {
                    String[] datos = new String[6];
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = String.valueOf(rs.getInt(3));;
                    datos[3] = "0";
                    datos[4] = "1";
                    datos[5] = String.valueOf(rs.getInt(3));
                    tabla.addRow(datos);
                    Object obg = rs.getDouble(3);
                    utilidaTotal.add(obg);
                    total();
                    Servicio.add(true);
                }
                jTextFieldCodigo.setText("");
                m = false;
            } else {
                m = true;
                new Servicios().setVisible(true);
            }
            cnn.close();
        } catch (SQLException e) {
            System.err.println(e);
            Errores.Errores("Error al Agregar Producto: " + e);
        }
    }

    public static void producto() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();

        try {
            String codigo = jTextFieldCodigo.getText().trim();
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select codigo,producto,precio_venta,precio_compra from producto where codigo = ? or codigo_barras = ?");
            pre.setString(1, codigo);
            pre.setString(2, codigo);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int i = tabla(rs.getString(1));
                if (i >= 0) {
                    int cant = Integer.parseInt(jTableVenta.getValueAt(i, 4).toString());
                    int precio = Integer.parseInt(jTableVenta.getValueAt(i, 2).toString());
                    cant++;
                    int totalV = precio * cant;
                    jTableVenta.setValueAt(cant, i, 4);
                    jTableVenta.setValueAt(totalV, i, 5);
                    utilidaTotal.set(i, (precio - rs.getDouble(5)) * cant);
                    total();
                } else {
                    String[] datos = new String[6];
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = String.valueOf(rs.getInt(3));
                    datos[3] = "0";
                    datos[4] = "1";
                    datos[5] = String.valueOf(rs.getInt(3));
                    tabla.addRow(datos);
                    Object obg = rs.getDouble(3) - rs.getDouble(4);
                    utilidaTotal.add(obg);
                    Servicio.add(false);
                    total();
                }
                jTextFieldCodigo.setText("");
                m = false;
            } else {
                m = true;
                new Catalogo().setVisible(true);
            }
            cnn.close();
        } catch (SQLException e) {
            System.err.println(e);
            Errores.Errores("Error al Agregar Producto: " + e);
        }
    }

    public void buscarcl() {
        if (!jTextFieldCedula.getText().equals("")) {
            try (Connection cn = Conexion.Conexion()) {
                String cedula = jTextFieldCedula.getText();
                PreparedStatement pr = cn.prepareStatement("select * from clientes where cedula = ?");
                pr.setString(1, cedula);
                ResultSet rs = pr.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString(3);

                    jTextFieldNombre.setText(nombre);
                    jLabelTelefono.setText(rs.getString(4));
                    jLabelSaldo.setText(rs.getString(5));
                    jTextFieldNombre.requestFocus();

                } else {

                    int i = JOptionPane.showConfirmDialog(null, "No se encuentra cliente", "¿desea ingresar el cliente?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(i);
                    if (i == 0) {

                        IngresarClientes cliente = new IngresarClientes(this, true);
                        IngresarClientes.jTextFieldCedula.setText(cedula);
                        cliente.setVisible(true);
                        buscarcl();
                    }
                }
                cn.close();
            } catch (SQLException e) {
                System.out.println(e);
                Errores.Errores("Error al Buscar CLiente: " + e);
            }
        }
    }

    public static void limpiar() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            tabla.removeRow(i);
            i--;
            jTextFieldCedula.setText("");
            jTextFieldNombre.setText("");
            jTextFieldTotal.setText("0");
            jTextFieldMoto.setText("");
            jTextFieldPlaca.setText("");
            jTextFieldColor.setText("");
            jTextArea1.setText("");
            jLabelFecha.setText("");
            jLabelTelefono.setText("");

            utilidaTotal.clear();
            Servicio.clear();

        }
    }

    public static void detalleVenta() {
        try {

            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO detallesventa (iddetallesVenta,nro_venta,codigo,producto,precioUnitario,cantidad,utilidad,precioTotal,Servicio,Descuento) values(?,?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                pr.setInt(1, 0);
                pr.setInt(2, Integer.parseInt(jLabelNoVenta.getText()));
                pr.setString(3, jTableVenta.getValueAt(i, 0).toString());
                pr.setString(4, jTableVenta.getValueAt(i, 1).toString());
                pr.setDouble(5, Double.parseDouble(jTableVenta.getValueAt(i, 2).toString()));
                pr.setInt(6, Integer.parseInt(jTableVenta.getValueAt(i, 4).toString()));
                pr.setDouble(7, (double) utilidaTotal.get(i));
                pr.setDouble(8, Double.parseDouble(jTableVenta.getValueAt(i, 5).toString()));
                pr.setBoolean(9, (boolean) Servicio.get(i));
                pr.setDouble(10, Double.parseDouble( jTableVenta.getValueAt(i, 3).toString()));
                pr.executeUpdate();
                String codigo = jTableVenta.getValueAt(i, 0).toString();
                int cantidad = Integer.parseInt(jTableVenta.getValueAt(i, 4).toString());
                ActualizarCantidad.restar(cantidad, codigo);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir detalles venta: " + e);
            Errores.Errores("Error al Subir Detalles de venta: " + e);
        }
    }

    public static void venta(String FormaPago, double cambio, double efectivo, double saldo) {
        try {
            double utilidad = 0;
            for (int i = 0; i < utilidaTotal.size(); i++) {
                utilidad += Double.parseDouble(utilidaTotal.get(i).toString());
            }
            double t = 0;
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                t += Double.parseDouble(jTableVenta.getValueAt(i, 3).toString());
            }
            int nro = nroVenta();
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO ventas (idventas,nroVentas,cliente,cedula_cliente,idUsuario,utilidad,fecha,precio_Total,Efectivo,Cambio,FormaPago,moto,placa,color,Saldo,comentario,hora,idEmpleado,Descuento) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pr.setInt(1, 0);
            pr.setInt(2, nro);
            pr.setString(3, jTextFieldNombre.getText());
            if (jTextFieldCedula.getText().equals("")) {
                pr.setString(4, "0");
            } else {
                pr.setString(4, jTextFieldCedula.getText().trim());
            }
            pr.setInt(5, Login.idUsuario);
            pr.setDouble(6, utilidad);
            pr.setDate(7, new java.sql.Date(Fechas.fechaActualDate().getTime()));
            pr.setDouble(8, Double.parseDouble(jTextFieldTotal.getText().replace(",", "")));
            pr.setDouble(9, efectivo);
            pr.setDouble(10, cambio);
            pr.setString(11, FormaPago);
            pr.setString(12, jTextFieldMoto.getText().trim());
            pr.setString(13, jTextFieldPlaca.getText().trim());
            pr.setString(14, jTextFieldColor.getText().trim());
            pr.setDouble(15, saldo);
            pr.setString(16, jTextArea1.getText());
            pr.setTime(17, new Time(Fechas.fechaActualDate().getTime()));
            pr.setInt(18, (int) idEmp.get(jComboBox1.getSelectedIndex()));
            pr.setDouble(19, t);
            pr.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir Venta: " + e);
            Errores.Errores("Error al subir venta: " + e);
        }
    }

    public void eliminarProducto() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        int row = jTableVenta.getSelectedRow();
        utilidaTotal.remove(row);
        Servicio.remove(row);
        tabla.removeRow(jTableVenta.getSelectedRow());
        total();
    }

    public void cambiarCant(int row) {
        String codigo = jTableVenta.getValueAt(row, 0).toString();
        int cant = Integer.parseInt(jTableVenta.getValueAt(row, 4).toString());
        double precio = Double.parseDouble(jTableVenta.getValueAt(row, 2).toString());
        double total1 = cant * precio;
        double util = (precio - Utilidad.costo(codigo)) * cant;
        utilidaTotal.set(row, util);
        jTableVenta.setValueAt(total1, row, 5);
        System.out.println(utilidaTotal);

    }

    public static int tabla(String codigo) {
        int l = -1;
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            if (jTableVenta.getValueAt(i, 0).toString().equals(codigo)) {
                l = i;
            }
        }
        return l;
    }

    public static void llenarEmpleado() {
        jComboBox1.removeAll();
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("select * from empleados");
            ResultSet rs = pr.executeQuery();
            jComboBox1.addItem("Selecciona");
            while (rs.next()) {
                idEmp.add(rs.getInt(1));
                jComboBox1.addItem(rs.getString(3));
            }
            jComboBox1.addItem("Agregar");
            cn.close();
        } catch (SQLException e) {
            System.out.println(e);
            Errores.Errores("Error al Buscar CLiente: " + e);
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

        jLabel1 = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCedula = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCodigo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldTotal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonVender = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabelNoVenta = new javax.swing.JLabel();
        jLabelBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVenta = new javax.swing.JTable();
        jLabelRegresar = new javax.swing.JLabel();
        jLabelRegresar1 = new javax.swing.JLabel();
        jLabelImprimir = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldMoto = new javax.swing.JTextField();
        jTextFieldColor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldPlaca = new javax.swing.JTextField();
        jButtonBuscando = new javax.swing.JButton();
        jLabelTelefono = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelSaldo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldTotal1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setSize(new java.awt.Dimension(1000, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Cedula:");

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Fecha:");

        jTextFieldCedula.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCedulaKeyPressed(evt);
            }
        });

        jTextFieldNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nombre:");

        jTextFieldCodigo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Productos:");

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldTotal.setToolTipText("");
        jTextFieldTotal.setActionCommand("<Not Set>");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total:");

        jButtonVender.setBackground(new java.awt.Color(0, 102, 0));
        jButtonVender.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButtonVender.setText("Vender");
        jButtonVender.setBorderPainted(false);
        jButtonVender.setPreferredSize(new java.awt.Dimension(85, 33));
        jButtonVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVenderActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nro. Venta:");

        jLabelNoVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNoVenta.setForeground(new java.awt.Color(255, 255, 255));

        jLabelBuscar.setText("d");
        jLabelBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelBuscarMouseClicked(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Creado por Corporacion Portillo CORPORT ADMP ®©™ 2023 V2.0");
        jLabel2.setAlignmentX(253);
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Exclusivo para Fullmotors de Corport El Tambo");
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jScrollPane2.setBackground(new java.awt.Color(0, 51, 51));
        jScrollPane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 102, 255), new java.awt.Color(0, 0, 255), new java.awt.Color(0, 102, 255), new java.awt.Color(0, 102, 255)));
        jScrollPane2.setForeground(new java.awt.Color(51, 153, 255));
        jScrollPane2.setOpaque(false);

        jTableVenta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 102, 255), new java.awt.Color(0, 51, 255), new java.awt.Color(0, 102, 255), new java.awt.Color(0, 51, 204)));
        jTableVenta.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jTableVenta.setForeground(new java.awt.Color(51, 153, 255));
        jTableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableVenta.setGridColor(new java.awt.Color(51, 153, 255));
        jTableVenta.setOpaque(false);
        jTableVenta.setRowHeight(40);
        jTableVenta.setRowMargin(2);
        jTableVenta.setSelectionBackground(new java.awt.Color(0, 102, 255));
        jTableVenta.setSelectionForeground(new java.awt.Color(0, 0, 204));
        jTableVenta.getTableHeader().setReorderingAllowed(false);
        jTableVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableVentaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableVenta);

        jLabelRegresar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jLabelRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegresarMouseClicked(evt);
            }
        });

        jLabelRegresar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jLabelRegresar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegresar1MouseClicked(evt);
            }
        });

        jLabelImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 153, 204), new java.awt.Color(102, 102, 102), null, null));
        jLabelImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelImprimirMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Moto:");

        jTextFieldMoto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldMoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMotoKeyPressed(evt);
            }
        });

        jTextFieldColor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldColor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldColorKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Color:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Placa:");

        jTextFieldPlaca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPlacaKeyPressed(evt);
            }
        });

        jButtonBuscando.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jButtonBuscando.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscandoActionPerformed(evt);
            }
        });

        jLabelTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTelefono.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTelefono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Saldo: ");

        jLabelSaldo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelSaldo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaldo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Comentario:");

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel14.setText("Tecnico");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Descuento:");

        jTextFieldTotal1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldTotal1.setToolTipText("");
        jTextFieldTotal1.setActionCommand("<Not Set>");
        jTextFieldTotal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldTotal1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldTotal1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldTotal1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(79, 79, 79)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelNoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscando, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelRegresar1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabelRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBuscando, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabelNoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)))
                .addGap(83, 83, 83)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed

        if (jTableVenta.getRowCount() != 0) {
            vender();
            Administrador.jProgressBar1.setValue(Utilidad.utilidadMes());
            int porsentaje = (jProgressBar1.getValue() / jProgressBar1.getMaximum()) * 100;
            jProgressBar1.setString("%" + porsentaje);
            Administrador.utilidadPor();
        } else {
            JOptionPane.showMessageDialog(this, "No hay Productos Para Venta");
        }
    }//GEN-LAST:event_jButtonVenderActionPerformed

    private void jTextFieldCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCedulaKeyPressed

        if (!Validaciones.validarEnter(evt)) {
            if (jTextFieldCedula.getText().equals("")) {
                new BuscarClientes(this, true).setVisible(true);
                buscarcl();
            } else {
                buscarcl();
            }
        }
    }//GEN-LAST:event_jTextFieldCedulaKeyPressed

    private void jTextFieldNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyPressed
        if (!Validaciones.validarEnter(evt)) {

            jTextFieldMoto.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyPressed

    private void jLabelRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseClicked
        int nr = Integer.parseInt(jLabelNoVenta.getText());
        jLabelImprimir.setVisible(true);
        if (nr > 1) {
            limpiar();
            nr--;
            jLabelNoVenta.setText("" + nr);
            buscarVentas(String.valueOf(nr));
            buscarDetalle(String.valueOf(nr));
            jButtonVender.setVisible(false);
        }
    }//GEN-LAST:event_jLabelRegresarMouseClicked

    private void jLabelRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresar1MouseClicked
        int nro = Integer.parseInt(jLabelNoVenta.getText());

        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroVentas) from ventas");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {

                int nur = rs.getInt(1);

                if (nur <= nro) {

                    jLabelImprimir.setVisible(false);
                    jTextFieldCedula.setEditable(true);
                    jTextFieldCodigo.setEditable(true);

                    jButtonVender.setVisible(true);
                    limpiar();
                    nroVenta();
                } else {
                    nro++;
                    jLabelNoVenta.setText("" + nro);
                    limpiar();
                    buscarDetalle(String.valueOf(nro));
                    buscarVentas(String.valueOf(nro));
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_jLabelRegresar1MouseClicked

    private void jLabelImprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImprimirMouseClicked
        Object[] opc = new Object[]{"Ticket", "Carta", "NO"};
        int i = JOptionPane.showOptionDialog(null, "Venta Exitosa\n¿Desea imprimir factura?", "Venta Exitosa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);

        if (i == 0) {
            new Imprimir().imprimir1();
        } else if (i == 1) {
            new Imprimir().imprimir2();
        }
    }//GEN-LAST:event_jLabelImprimirMouseClicked

    private void jTextFieldCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoKeyPressed
        String objeto = jLabel9.getText();

        if (!Validaciones.validarEnter(evt)) {
            if ("Servicio:".equals(objeto)) {
                servicio();
            } else {
                producto();
            }
        } else if ((evt.getKeyCode() == KeyEvent.VK_R) && (evt.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
            if ("Servicio:".equals(objeto)) {
                jLabel9.setText("Productos:");
            } else {
                jLabel9.setText("Servicio:");
            }
        }
    }//GEN-LAST:event_jTextFieldCodigoKeyPressed

    private void jLabelBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelBuscarMouseClicked
        m = true;
        if (jLabel9.getText().equals("Productos:")) {
            new Catalogo().setVisible(true);
        } else {
            new Servicios().setVisible(true);
        }

    }//GEN-LAST:event_jLabelBuscarMouseClicked

    private void jTableVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableVentaKeyPressed
        if (!Validaciones.validarSuprimir(evt)) {
            eliminarProducto();
        }
    }//GEN-LAST:event_jTableVentaKeyPressed

    private void jTextFieldMotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMotoKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldColor.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldMotoKeyPressed

    private void jTextFieldColorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldColorKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldPlaca.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldColorKeyPressed

    private void jTextFieldPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPlacaKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldCodigo.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldPlacaKeyPressed

    private void jButtonBuscandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscandoActionPerformed
        new Buscar_Venta(this, true).setVisible(true);
    }//GEN-LAST:event_jButtonBuscandoActionPerformed

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
        if (jTextArea1.getText().length() > 100) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String Item = jComboBox1.getSelectedItem().toString();
        if (Item.equals("Agregar")) {
            IngresarEmpleado.m = true;
            new IngresarEmpleado(this, true).setVisible(true);
            jComboBox1.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextFieldTotal1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTotal1KeyPressed
        if (!Validaciones.validarEnter(evt)) {
            double porDes = Double.parseDouble(jTextFieldTotal1.getText().trim());
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                jTableVenta.setValueAt(porDes, i, 3);
            }
        }
    }//GEN-LAST:event_jTextFieldTotal1KeyPressed

    private void jTextFieldTotal1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTotal1KeyTyped
        if (Validaciones.validarString(evt)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldTotal1KeyTyped

    private void jTextFieldTotal1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldTotal1KeyReleased

    }//GEN-LAST:event_jTextFieldTotal1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscando;
    private javax.swing.JButton jButtonVender;
    private static javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBuscar;
    private static javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelImprimir;
    public static javax.swing.JLabel jLabelNoVenta;
    private javax.swing.JLabel jLabelRegresar;
    private javax.swing.JLabel jLabelRegresar1;
    protected static javax.swing.JLabel jLabelSaldo;
    private static javax.swing.JLabel jLabelTelefono;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTableVenta;
    private static javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextField jTextFieldCedula;
    public static javax.swing.JTextField jTextFieldCodigo;
    public static javax.swing.JTextField jTextFieldColor;
    public static javax.swing.JTextField jTextFieldMoto;
    private static javax.swing.JTextField jTextFieldNombre;
    public static javax.swing.JTextField jTextFieldPlaca;
    public static javax.swing.JTextField jTextFieldTotal;
    public static javax.swing.JTextField jTextFieldTotal1;
    // End of variables declaration//GEN-END:variables
}
