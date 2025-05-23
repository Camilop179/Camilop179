/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Ventanas;

import Clases.Conexion;
import Clases.Errores;
import Clases.Fechas;
import Clases.FormatoPesos;
import Clases.FormatoTablas;
import Clases.ImagenBoton;
import Clases.Imagenes;
import Clases.Imprimir;
import Clases.Utilidad;
import Clases.Validaciones;
import java.sql.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author fullmotors
 */
public class CotizacionT extends javax.swing.JPanel {

    /**
     * Creates new form CotizacionT
     */
    public static boolean m = false;
    public static boolean n;
    FormatoTablas ft = new FormatoTablas();
    static ArrayList idEmp = new ArrayList();
    static ArrayList utilidaTotal = new ArrayList();
    static ArrayList Servicio = new ArrayList();

    public CotizacionT() {
        idEmp.add(0);
        n = true;
        initComponents();
        llenarEmpleado();
       
        jLabelFecha.setText(Fechas.fechaActual());
        if (Reportes.m == 1) {
            jLabelImprimir.setVisible(true);
            jLabelNoCot.setText("" + Reportes.nro);
        } else {
            jLabelImprimir.setVisible(false);
            nroVenta();
        }
        jScrollPane2.getViewport().setBackground(new Color(51, 153, 255));
        tamañoColumna();
        jTableCotizacion.setDefaultRenderer(Object.class, ft);
        reportes();
        eventotabla();
        imagenes();
    }
    
    public void imagenes(){
        new ImagenBoton("vender.png", jButtonVender,45,45);
        jButtonBuscando.setContentAreaFilled(false);
        new Imagenes("buscando.png", jLabelBuscar,45,45);
        new Imagenes("ADELANTE.png", jLabelRegresar1,45,45);
        new Imagenes("ATRAS.png", jLabelRegresar,45,45);
        new Imagenes("imprimir.png", jLabelImprimir,45,45);
        new ImagenBoton("buscando.png", jButtonBuscando,45,45);
    }
    
    @Override
    public void paint(Graphics g) {
        Image fondo = new ImageIcon(getClass().getResource("/imagenes/FondoMenu.jpg")).getImage();
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        } else {
            System.out.println("Clases.Fondo.paint()");
        }
    }
    
    public void eventotabla() {
        jTableCotizacion.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int columna = e.getColumn();
                int row = e.getLastRow();
                if (columna == 2) {
                    cambiarCant(row);
                    total();
                } else if (columna == 3) {
                    cambiarCant(row);
                    total();
                }
            }
        });
    }


    public void reportes() {
        if (Reportes.m == 1) {
            String NroVentas = jLabelNoCot.getText();
            buscarDetalle(NroVentas);
            buscarVentas(NroVentas);
        }
    }

    public static void buscarDetalle(String NroVenta) {
        limpiar();
        DefaultTableModel tabla = (DefaultTableModel) jTableCotizacion.getModel();
        String[] datos = new String[5];
        try (Connection cn = Conexion.Conexion()){
            
            PreparedStatement pr2 = cn.prepareStatement(
                    "select codigo,producto,precioUnitario,cantidad,PrecioTotal from detallescotizacion where nro_Cotizacion = ?");
            pr2.setString(1, NroVenta);
            ResultSet rs2 = pr2.executeQuery();
            while (rs2.next()) {
                for (int i = 0; i < 5; i++) {
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
            pr = cn.prepareStatement("select v.*,c.saldo,c.celular from cotizacion v left join clientes c on cedula_cliente=c.cedula  where nroCotizacion = ?");
            pr.setString(1, NroVenta);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                jLabelFecha.setText(rs.getString(7));
                jTextFieldTotal.setText(rs.getString(8));
                jTextFieldCedula.setText(rs.getString(4));
                jLabelTelefono.setText(rs.getString(17));
                jTextFieldNombre.setText(rs.getString(3));
                jTextFieldPlaca.setText(rs.getString(10));
                jTextFieldMoto.setText(rs.getString(9));
                jTextFieldColor.setText(rs.getString(11));
                jTextArea1.setText(rs.getString(12));
                jComboBox1.setSelectedIndex(rs.getInt(14));
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
        tabla.addColumn("Cantidad");
        tabla.addColumn("Total");

        jTableCotizacion.setEditingColumn(-1);

        jTableCotizacion.setModel(tabla);
        TableColumnModel columnModel = jTableCotizacion.getColumnModel();
        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);

        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(100);

    }

    public static int nroVenta() {
        int nro_venta = 0;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroCotizacion) from cotizacion");
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                nro_venta = rs.getInt(1);
                nro_venta++;
                jLabelNoCot.setText("" + nro_venta);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return nro_venta;
    }

    public void vender() {
        int i = JOptionPane.showConfirmDialog(this, "Desea realizar Cotizacion");
        if (i == 0) {
            venta();
            detalleVenta();
        }
    }

    public static void total() {
        double t = 0;
        for (int i = 0; i < jTableCotizacion.getRowCount(); i++) {
            t += Double.parseDouble(jTableCotizacion.getValueAt(i, 4).toString());
        }
        jTextFieldTotal.setText(FormatoPesos.formato(t));
    }

    public static void servicio() {
        DefaultTableModel tabla = (DefaultTableModel) jTableCotizacion.getModel();

        try {
            String codigo = jTextFieldCodigo.getText().trim();
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select codigo,Concepto,Valor from servicio where codigo = ?");
            pre.setString(1, codigo);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int i = tabla(rs.getString(1));
                if (i >= 0) {
                    int cant = Integer.parseInt(jTableCotizacion.getValueAt(i, 3).toString());
                    cant++;
                    jTableCotizacion.setValueAt(cant, i, 3);
                    total();
                } else {
                    String[] datos = new String[5];
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = String.valueOf(rs.getInt(3));
                    datos[3] = "1";
                    datos[4] = String.valueOf(rs.getInt(3));
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
        DefaultTableModel tabla = (DefaultTableModel) jTableCotizacion.getModel();

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
                    int cant = Integer.parseInt(jTableCotizacion.getValueAt(i, 3).toString());
                    int precio = Integer.parseInt(jTableCotizacion.getValueAt(i, 2).toString());
                    cant++;
                    int totalV = precio * cant;
                    jTableCotizacion.setValueAt(cant, i, 3);
                    jTableCotizacion.setValueAt(totalV, i, 4);
                    utilidaTotal.set(i, (precio - rs.getDouble(4)) * cant);
                    total();
                } else {
                    String[] datos = new String[5];
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = String.valueOf(rs.getInt(3));
                    datos[3] = "1";
                    datos[4] = String.valueOf(rs.getInt(3));
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

                        IngresarClientes cliente = new IngresarClientes(new Comprobante(), true);
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
        DefaultTableModel tabla = (DefaultTableModel) jTableCotizacion.getModel();
        for (int i = 0; i < jTableCotizacion.getRowCount(); i++) {
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
        try (Connection cn = Conexion.Conexion()) {

            PreparedStatement pr = cn.prepareStatement("INSERT INTO detallescotizacion (id,nro_Cotizacion,codigo,producto,precioUnitario,cantidad,utilidad,precioTotal,Servicio) values(?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < jTableCotizacion.getRowCount(); i++) {
                pr.setInt(1, 0);
                pr.setInt(2, Integer.parseInt(jLabelNoCot.getText()));
                pr.setString(3, jTableCotizacion.getValueAt(i, 0).toString());
                pr.setString(4, jTableCotizacion.getValueAt(i, 1).toString());
                pr.setDouble(5, Double.parseDouble(jTableCotizacion.getValueAt(i, 2).toString()));
                pr.setInt(6, Integer.parseInt(jTableCotizacion.getValueAt(i, 3).toString()));
                pr.setDouble(7, (double) utilidaTotal.get(i));
                pr.setDouble(8, Double.parseDouble(jTableCotizacion.getValueAt(i, 4).toString()));
                pr.setBoolean(9, (boolean) Servicio.get(i));
                pr.executeUpdate();
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir detalles venta: " + e);
            Errores.Errores("Error al Subir Detalles de venta: " + e);
        }
    }

    public static void venta() {
        try {
            double utilidad = 0;
            for (int i = 0; i < utilidaTotal.size(); i++) {
                utilidad += Double.parseDouble(utilidaTotal.get(i).toString());
            }
            int nro = nroVenta();
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO cotizacion (id,nroCotizacion,cliente,cedula_cliente,idUsuario,utilidad,fecha,precio_Total,moto,placa,color,comentario,hora,idEmpleado,Descuento) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            pr.setString(9, jTextFieldMoto.getText().trim());
            pr.setString(10, jTextFieldPlaca.getText().trim());
            pr.setString(11, jTextFieldColor.getText().trim());
            pr.setString(12, jTextArea1.getText());
            pr.setTime(13, new Time(Fechas.fechaActualDate().getTime()));
            pr.setInt(14, (int) idEmp.get(jComboBox1.getSelectedIndex()));
            pr.setDouble(15, 0);
            pr.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir Venta: " + e);
            Errores.Errores("Error al subir venta: " + e);
        }
    }

    public void eliminarProducto() {
        DefaultTableModel tabla = (DefaultTableModel) jTableCotizacion.getModel();
        int row = jTableCotizacion.getSelectedRow();
        utilidaTotal.remove(row);
        Servicio.remove(row);
        tabla.removeRow(jTableCotizacion.getSelectedRow());
        total();
    }

    public void cambiarCant(int row) {
        String codigo = jTableCotizacion.getValueAt(row, 0).toString();
        int cant = Integer.parseInt(jTableCotizacion.getValueAt(row, 3).toString());
        double precio = Double.parseDouble(jTableCotizacion.getValueAt(row, 2).toString());
        double total1 = cant * precio;
        double util = (precio - Utilidad.costo(codigo)) * cant;
        utilidaTotal.set(row, util);
        jTableCotizacion.setValueAt(total1, row, 4);
        System.out.println(utilidaTotal);

    }

    public static int tabla(String codigo) {
        int l = -1;
        for (int i = 0; i < jTableCotizacion.getRowCount(); i++) {
            if (jTableCotizacion.getValueAt(i, 0).toString().equals(codigo)) {
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
        jLabelNoCot = new javax.swing.JLabel();
        jLabelBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCotizacion = new javax.swing.JTable();
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

        jLabelNoCot.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNoCot.setForeground(new java.awt.Color(255, 255, 255));

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

        jTableCotizacion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 102, 255), new java.awt.Color(0, 51, 255), new java.awt.Color(0, 102, 255), new java.awt.Color(0, 51, 204)));
        jTableCotizacion.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jTableCotizacion.setForeground(new java.awt.Color(51, 153, 255));
        jTableCotizacion.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableCotizacion.setGridColor(new java.awt.Color(51, 153, 255));
        jTableCotizacion.setOpaque(false);
        jTableCotizacion.setRowHeight(40);
        jTableCotizacion.setRowMargin(2);
        jTableCotizacion.setSelectionBackground(new java.awt.Color(0, 102, 255));
        jTableCotizacion.setSelectionForeground(new java.awt.Color(0, 0, 204));
        jTableCotizacion.getTableHeader().setReorderingAllowed(false);
        jTableCotizacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableCotizacionKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableCotizacion);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
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
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addGap(68, 68, 68)
                                .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscando, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(79, 79, 79)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelNoCot, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)))
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
                            .addComponent(jLabelNoCot, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
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
                    .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(83, 83, 83)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCedulaKeyPressed

        if (!Validaciones.validarEnter(evt)) {
            if (jTextFieldCedula.getText().equals("")) {
                new BuscarClientes(new Comprobante(), true).setVisible(true);
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

    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed

        if (jTableCotizacion.getRowCount() != 0) {
            vender();
            limpiar();
            nroVenta();
        } else {
            JOptionPane.showMessageDialog(this, "No hay Productos Para Venta");
        }
    }//GEN-LAST:event_jButtonVenderActionPerformed

    private void jLabelBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelBuscarMouseClicked
        m = true;
        if (jLabel9.getText().equals("Productos:")) {
            new Catalogo().setVisible(true);
        } else {
            new Servicios().setVisible(true);
        }
    }//GEN-LAST:event_jLabelBuscarMouseClicked

    private void jTableCotizacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableCotizacionKeyPressed
        if (!Validaciones.validarSuprimir(evt)) {
            eliminarProducto();
        }
    }//GEN-LAST:event_jTableCotizacionKeyPressed

    private void jLabelRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseClicked
        int nr = Integer.parseInt(jLabelNoCot.getText());
        jLabelImprimir.setVisible(true);
        if (nr > 1) {
            limpiar();
            nr--;
            jLabelNoCot.setText("" + nr);
            buscarVentas(String.valueOf(nr));
            buscarDetalle(String.valueOf(nr));
            jButtonVender.setVisible(false);
        }
    }//GEN-LAST:event_jLabelRegresarMouseClicked

    private void jLabelRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresar1MouseClicked
        int nro = Integer.parseInt(jLabelNoCot.getText());

        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroCotizacion) from cotizacion");
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
                    jLabelNoCot.setText("" + nro);
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
//            new Imprimir().imprimir1();
        } else if (i == 1) {
            new Imprimir().imprimir3();
        }
    }//GEN-LAST:event_jLabelImprimirMouseClicked

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
            BuscarMoto();
        }
    }//GEN-LAST:event_jTextFieldPlacaKeyPressed

    private void jButtonBuscandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscandoActionPerformed
        new Buscar_Venta(new Comprobante(), true).setVisible(true);
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
            new IngresarEmpleado(new Comprobante(), true).setVisible(true);
            jComboBox1.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public void BuscarMoto() {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select * from motos where placa = ?");
            ps.setString(1, jTextFieldPlaca.getText());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jTextFieldColor.setText(rs.getString(4));
                jTextFieldMoto.setText(rs.getString(3));

                jTextFieldColor.setEditable(true);
                jTextFieldMoto.setEditable(true);

                jTextFieldColor.requestFocus();
                cn.close();
            } else {
                Moto moto = new Moto(null, true);
                moto.setPlaca(jTextFieldPlaca.getText());
                moto.setVisible(true);
            }

        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, e);
        }
    }

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JLabel jLabelBuscar;
    private static javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelImprimir;
    public static javax.swing.JLabel jLabelNoCot;
    private javax.swing.JLabel jLabelRegresar;
    private javax.swing.JLabel jLabelRegresar1;
    protected static javax.swing.JLabel jLabelSaldo;
    private static javax.swing.JLabel jLabelTelefono;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTableCotizacion;
    private static javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextField jTextFieldCedula;
    public static javax.swing.JTextField jTextFieldCodigo;
    public static javax.swing.JTextField jTextFieldColor;
    public static javax.swing.JTextField jTextFieldMoto;
    private static javax.swing.JTextField jTextFieldNombre;
    public static javax.swing.JTextField jTextFieldPlaca;
    public static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
