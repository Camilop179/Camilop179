package Ventanas;

import Clases.Conexion;
import Clases.Errores;
import Clases.Fechas;
import Clases.Fondo;
import Clases.FormatoPesos;
import Clases.FormatoTablas;
import Clases.ImagenBoton;
import Clases.Imagenes;
import Clases.Imprimir;
import Clases.Validaciones;
import Clases.Utilidad;
import Clases.uiJtextField;
import static Ventanas.Administrador.jProgressBar1;
import java.awt.Color;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author harol
 */
public final class OrdenDeTrabajo extends javax.swing.JFrame {

    public static boolean m = false;
    public static boolean n;
    FormatoTablas ft = new FormatoTablas();
    static ArrayList idEmp = new ArrayList();
    static ArrayList utilidaTotal = new ArrayList();
    static ArrayList Servicio = new ArrayList();
    static ArrayList precioCosto = new ArrayList();
    static int idOrden = 0;
    static ArrayList idDetalle = new ArrayList();

    public OrdenDeTrabajo() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        idEmp.add(0);
        n = true;
        this.setContentPane(fondo);
        initComponents();
        
        setTitle("Orden de Trabajo");
        llenarEmpleado();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImagenBoton imagenBoton = new ImagenBoton("vender.png", jButtonVender, 43, 43);
        jButtonBuscando.setContentAreaFilled(true);
        Imagenes imagenes = new Imagenes("buscando.png", jLabelBuscar, 45, 40);
        Imagenes imagenes1 = new Imagenes("ADELANTE.png", jLabelAdelante, jLabelAdelante.getWidth() - 2, jLabelAdelante.getHeight());
        Imagenes imagenes2 = new Imagenes("ATRAS.png", jLabelRegresar, jLabelRegresar.getWidth() - 2, jLabelRegresar.getHeight());
        Imagenes imagenes3 = new Imagenes("imprimir.png", jLabelImprimir, 45, 40);
        Imagenes imagenes4 = new Imagenes("logo fondo.png", jLabel16, jLabel16.getWidth() + 10, jLabel16.getHeight() + 10);
        ImagenBoton imagenBoton1 = new ImagenBoton("buscando.png", jButtonBuscando, 38, 38);
        Imagenes imagenes5 = new Imagenes("disco-flexible.png", jLabelEditar, jLabelEditar.getWidth() + 10, jLabelEditar.getHeight() + 10);
        ImagenBoton imagenBoton2 = new ImagenBoton("ventas.png", botonEnviar, 38, 38);
        jLabelFecha.setText(Fechas.fechaActual());
        jLabelSaldo.setText("0");
        jTextFieldDescuento.setText("0");
        jLabelEditar.setVisible(false);
        this.setLocationRelativeTo(null);
        if (Reportes.m == 1) {
            jLabelImprimir.setVisible(true);
            jLabelNoVenta.setText("" + Reportes.nro);
        } else {
            jLabelImprimir.setVisible(false);
            nroVenta();
        }
        jScrollPane2.getViewport().setBackground(new Color(75, 75, 75));
        tamañoColumna();
        jTableVenta.setDefaultRenderer(Object.class, ft);
        jTextFieldMoto.setEditable(false);
        jTextFieldColor.setEditable(false);
        reportes();
        cerra();
        eventotabla();

    }

    public void eventotabla() {
        jTableVenta.getModel().addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int columna = e.getColumn();
                int row = e.getLastRow();
                switch (columna) {
                    case 3 -> {
                        cambiarCant(row);
                        total();
                    }
                    case 5 -> {
                        cambiarCant(row);
                        total();
                    }
                    case 4 ->
                        descuento(row);
                    default -> {
                    }
                }
            }
        });
    }

    public void descuento(int row) {
        double por = Double.parseDouble(jTableVenta.getValueAt(row, 4).toString());
        double costo = Utilidad.costo(jTableVenta.getValueAt(row, 1).toString()) * Double.parseDouble(jTableVenta.getValueAt(row, 5).toString());
        double cant = Double.parseDouble(jTableVenta.getValueAt(row, 5).toString());
        por = 1 - por / 100;
        double precio = Double.parseDouble(jTableVenta.getValueAt(row, 3).toString()) * por * cant;
        jTableVenta.setValueAt(precio, row, 6);
        utilidaTotal.set(row, precio - costo);
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
            buscarOrden(NroVentas);
        }
    }

    public static void buscarDetalle(String NroVenta) {
        limpiar();
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        String[] datos = new String[7];
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr2 = cn.prepareStatement("select id,nro,codigo,producto,precioUnitario,Descuento,cantidad,PrecioTotal,utilidad,servicio from detalles_orden where nro_Orden = ?");
            pr2.setString(1, NroVenta);
            ResultSet rs2 = pr2.executeQuery();
            while (rs2.next()) {
                double costo=0;
                costo = Utilidad.costo(rs2.getString(3));
                idDetalle.add(rs2.getInt(1));
                datos[0] = rs2.getString(2);
                datos[1] = rs2.getString(3);
                datos[2] = rs2.getString(4);
                datos[3] = rs2.getString(5);
                datos[4] = rs2.getString(6);
                datos[5] = rs2.getString(7);
                datos[6] = rs2.getString(8);
                utilidaTotal.add(rs2.getDouble(9));
                Servicio.add(rs2.getBoolean(10));
                precioCosto.add(costo);
                tabla.addRow(datos);
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void buscarOrden(String NroVenta) {
        try {
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select v.*,c.saldo,c.celular from orden_trabajo v left join clientes c on cedula_cliente=c.cedula  where nroOrden = ?");
            pr.setString(1, NroVenta);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                idOrden = rs.getInt(1);
                jTextFieldNombre.setText(rs.getString(3));
                jTextFieldCedula.setText(rs.getString(4));
                jLabelFecha.setText(rs.getString(8));
                jTextFieldTotal.setText(rs.getString(9));
                jTextFieldMoto.setText(rs.getString(10));
                jTextFieldPlaca.setText(rs.getString(11));
                jTextFieldColor.setText(rs.getString(12));
                jTextArea1.setText(rs.getString(13));
                jComboBox1.setSelectedIndex(rs.getInt(15));
                jLabelSaldo.setText(rs.getString(18));
                jLabelTelefono.setText(rs.getString(19));
            }

            cn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public final void tamañoColumna() {
        DefaultTableModel tabla = new DefaultTableModel() {
            boolean[] m = new boolean[]{
                false, false, true, true, true, true, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return m[columnIndex];
            }
        };
        tabla.addColumn("Nro");
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

        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(400);
        columnModel.getColumn(3).setPreferredWidth(70);
        columnModel.getColumn(4).setPreferredWidth(20);
        columnModel.getColumn(5).setPreferredWidth(20);
        columnModel.getColumn(6).setPreferredWidth(100);

    }

    public static int nroVenta() {
        int nro_venta = 0;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroOrden) from orden_trabajo");
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

    public void guarda() {

        guardar();
        detalleOrden();
        limpiar();
        nroVenta();

    }

    public static void total() {
        double t = 0;
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            t += Double.parseDouble(jTableVenta.getValueAt(i, 6).toString());
        }
        jTextFieldTotal.setText(FormatoPesos.formato(t));
    }

    public static void servicio() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();

        try (Connection cnn = Conexion.Conexion()) {
            String codigo = jTextFieldCodigo.getText().trim();

            PreparedStatement pre = cnn.prepareStatement("select codigo,Concepto,Valor from servicio where codigo = ?");
            pre.setString(1, codigo);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int i = tabla(rs.getString(1));
                if (i >= 0) {
                    int cant = Integer.parseInt(jTableVenta.getValueAt(i, 5).toString());
                    cant++;
                    jTableVenta.setValueAt(cant, i, 5);
                    total();
                    precioCosto.add(0);
                } else {
                    Object[] datos = new Object[7];
                    datos[0] = jTableVenta.getRowCount() + 1;
                    datos[1] = rs.getString(1);
                    datos[2] = rs.getString(2);
                    datos[3] = rs.getString(3);
                    datos[4] = 0;
                    datos[5] = 1;
                    datos[6] = rs.getString(3);
                    tabla.addRow(datos);
                    Object obg = rs.getDouble(3);
                    utilidaTotal.add(obg);
                    precioCosto.add(0);
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
                    int cant = Integer.parseInt(jTableVenta.getValueAt(i, 5).toString());
                    cant++;
                    jTableVenta.setValueAt(cant, i, 5);
                    precioCosto.set(i, rs.getDouble(5) * cant);
                    total();
                } else {
                    Object[] datos = new Object[7];
                    datos[0] = jTableVenta.getRowCount() + 1;
                    datos[1] = rs.getString(1);
                    datos[2] = rs.getString(2);
                    datos[3] = String.valueOf(rs.getInt(3));
                    datos[4] = "0";
                    datos[5] = "1";
                    datos[6] = String.valueOf(rs.getInt(3));
                    tabla.addRow(datos);
                    Object obg = rs.getDouble(3) - rs.getDouble(4);
                    utilidaTotal.add(obg);
                    precioCosto.add(rs.getDouble(4));
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
    static double saldo = 0;

    public void buscarcl() {
        if (!jTextFieldCedula.getText().equals("")) {
            try (Connection cn = Conexion.Conexion()) {
                String cedula = jTextFieldCedula.getText();
                PreparedStatement pr = cn.prepareStatement("select c.*,v.placa from clientes c left join ventas v on v.cedula_cliente=c.cedula where cedula = ?");
                pr.setString(1, cedula);
                ResultSet rs = pr.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString(3);
                    saldo = rs.getDouble(5);
                    jTextFieldNombre.setText(nombre);
                    jLabelTelefono.setText(rs.getString(4));
                    jLabelSaldo.setText(FormatoPesos.formato(saldo));
                    jTextFieldPlaca.setText(rs.getString(6));
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
        for (int i = jTableVenta.getRowCount(); i > 0; i--) {
            tabla.removeRow(i - 1);
            jTextFieldCedula.setText("");
            jTextFieldNombre.setText("");
            jTextFieldTotal.setText("0");
            jTextFieldMoto.setText("");
            jTextFieldPlaca.setText("");
            jTextFieldColor.setText("");
            jTextArea1.setText("");
            jLabelTelefono.setText("");
            saldo = 0;
            utilidaTotal.clear();
            precioCosto.clear();
            Servicio.clear();

        }
    }

    public static void detalleOrden() {
        try {

            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO detalles_orden (id,nro_Orden,codigo,producto,precioUnitario,cantidad,utilidad,precioTotal,Servicio,Descuento,nro) values(?,?,?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                pr.setInt(1, 0);
                pr.setInt(2, Integer.parseInt(jLabelNoVenta.getText()));
                pr.setString(3, jTableVenta.getValueAt(i, 1).toString());
                pr.setString(4, jTableVenta.getValueAt(i, 2).toString());
                pr.setDouble(5, Double.parseDouble(jTableVenta.getValueAt(i, 3).toString()));
                pr.setInt(6, Integer.parseInt(jTableVenta.getValueAt(i, 5).toString()));
                pr.setDouble(7, (double) utilidaTotal.get(i));
                pr.setDouble(8, Double.parseDouble(jTableVenta.getValueAt(i, 6).toString()));
                pr.setBoolean(9, (boolean) Servicio.get(i));
                pr.setDouble(10, Double.parseDouble(jTableVenta.getValueAt(i, 4).toString()));
                pr.setDouble(11, Double.parseDouble(jTableVenta.getValueAt(i, 0).toString()));

                pr.execute();
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir detalles orden: " + e);
            Errores.Errores("Error al Subir Detalles de venta: " + e);
        }
    }

    public static void guardar() {
        try {
            double utilidad = 0;
            double costo = 0;
            for (int i = 0; i < utilidaTotal.size(); i++) {
                utilidad += Double.parseDouble(utilidaTotal.get(i).toString());
            }
            for (int i = 0; i < precioCosto.size(); i++) {
                costo += Double.parseDouble(precioCosto.get(i).toString());
            }
            double t = 0;
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                t += Double.parseDouble(jTableVenta.getValueAt(i, 3).toString());
            }
            int nro = nroVenta();
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO orden_trabajo (id,nroOrden,cliente,cedula_cliente,idUsuario,utilidad,fecha,precio_Total,moto,placa,color,comentario,hora,idEmpleado,Descuento,precioCosto) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pr.setInt(1, 0);
            pr.setInt(2, nro);
            pr.setString(3, jTextFieldNombre.getText());
            pr.setString(4, jTextFieldCedula.getText().trim());
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
            pr.setDouble(15, t);
            pr.setDouble(16, costo);
            pr.execute();
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
        precioCosto.remove(row);
        Servicio.remove(row);
        tabla.removeRow(jTableVenta.getSelectedRow());
        total();
    }

    public void cambiarCant(int row) {
        String codigo = jTableVenta.getValueAt(row, 1).toString();
        int cant = Integer.parseInt(jTableVenta.getValueAt(row, 5).toString());
        double precio = Double.parseDouble(jTableVenta.getValueAt(row, 3).toString());
        double total1 = cant * precio;
        double por = 1 - Double.parseDouble(jTableVenta.getValueAt(row, 4).toString()) / 100;
        double util = (precio - Utilidad.costo(codigo)) * cant;
        utilidaTotal.set(row, util);
        precioCosto.set(row, Utilidad.costo(codigo) * cant);
        jTableVenta.setValueAt(total1 * por, row, 6);

    }

    public static int tabla(String codigo) {
        int l = -1;
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            if (jTableVenta.getValueAt(i, 1).toString().equals(codigo)) {
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
        jTextFieldCedula = new uiJtextField();
        jTextFieldNombre = new uiJtextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCodigo = new uiJtextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldTotal = new uiJtextField();
        jLabel13 = new javax.swing.JLabel();
        jButtonVender = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabelNoVenta = new javax.swing.JLabel();
        jLabelBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelRegresar = new javax.swing.JLabel();
        jLabelAdelante = new javax.swing.JLabel();
        jLabelImprimir = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldMoto = new uiJtextField();
        jTextFieldColor = new uiJtextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldPlaca = new uiJtextField();
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
        jTextFieldDescuento = new uiJtextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVenta = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabelEditar = new javax.swing.JLabel();
        botonEnviar = new javax.swing.JButton();

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
        jButtonVender.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButtonVender.setText("Guardar");
        jButtonVender.setBorderPainted(false);
        jButtonVender.setPreferredSize(new java.awt.Dimension(85, 33));
        jButtonVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVenderActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nro. Orden:");

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

        jLabelRegresar.setBackground(new java.awt.Color(51, 51, 51));
        jLabelRegresar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jLabelRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelRegresar.setOpaque(true);
        jLabelRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegresarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelRegresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelRegresarMouseExited(evt);
            }
        });

        jLabelAdelante.setBackground(new java.awt.Color(51, 51, 51));
        jLabelAdelante.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jLabelAdelante.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelAdelante.setOpaque(true);
        jLabelAdelante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelAdelanteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelAdelanteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelAdelanteMouseExited(evt);
            }
        });

        jLabelImprimir.setBackground(new java.awt.Color(51, 51, 51));
        jLabelImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 153, 204), new java.awt.Color(102, 102, 102), null, null));
        jLabelImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelImprimir.setOpaque(true);
        jLabelImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelImprimirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelImprimirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelImprimirMouseExited(evt);
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

        jButtonBuscando.setBackground(new java.awt.Color(51, 51, 51));
        jButtonBuscando.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        jButtonBuscando.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscando.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonBuscandoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonBuscandoMouseExited(evt);
            }
        });
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

        jTextFieldDescuento.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldDescuento.setToolTipText("");
        jTextFieldDescuento.setActionCommand("<Not Set>");
        jTextFieldDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDescuentoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDescuentoKeyTyped(evt);
            }
        });

        jScrollPane2.setForeground(new java.awt.Color(102, 102, 102));
        jScrollPane2.setOpaque(false);

        jTableVenta.setBackground(new java.awt.Color(75, 75, 75));
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
        jTableVenta.setOpaque(false);
        jTableVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableVentaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableVenta);

        jLabelEditar.setBackground(new java.awt.Color(51, 51, 51));
        jLabelEditar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 153, 204), new java.awt.Color(102, 102, 102), null, null));
        jLabelEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelEditar.setOpaque(true);
        jLabelEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelEditarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelEditarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelEditarMouseExited(evt);
            }
        });

        botonEnviar.setBackground(new java.awt.Color(51, 51, 51));
        botonEnviar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102), new java.awt.Color(0, 102, 102), new java.awt.Color(0, 102, 102)));
        botonEnviar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonEnviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonEnviarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonEnviarMouseExited(evt);
            }
        });
        botonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEnviarActionPerformed(evt);
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(175, Short.MAX_VALUE)
                        .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel10)
                                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel8)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(31, 31, 31))
                                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(79, 79, 79)
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabelNoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabelTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel12)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(65, 65, 65)))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelAdelante, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscando, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(203, 203, 203)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButtonBuscando, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                        .addComponent(jLabelAdelante, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelRegresar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonEnviar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                        .addGap(12, 12, 12)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)))
                .addGap(93, 93, 93)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    static public boolean tipoventa = false;
    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed

        if (jTableVenta.getRowCount() != 0) {
            guarda();
            Administrador.jProgressBar1.setValue(Utilidad.utilidadMes());
            if (jProgressBar1.getMaximum() != 0) {
                int porsentaje = (jProgressBar1.getValue() / jProgressBar1.getMaximum()) * 100;
                jProgressBar1.setString("%" + porsentaje);
                Administrador.utilidadPor();
            }
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

            jTextFieldPlaca.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyPressed

    private void jLabelRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseClicked
        int nr = Integer.parseInt(jLabelNoVenta.getText());
        jLabelImprimir.setVisible(true);
        jLabelEditar.setVisible(true);
        if (nr > 1) {
            limpiar();
            nr--;
            jLabelNoVenta.setText("" + nr);
            buscarOrden(String.valueOf(nr));
            buscarDetalle(String.valueOf(nr));
            jButtonVender.setVisible(false);
            editar = false;
        }
    }//GEN-LAST:event_jLabelRegresarMouseClicked

    private void jLabelAdelanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAdelanteMouseClicked
        int nro = Integer.parseInt(jLabelNoVenta.getText());

        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(nroOrden) from orden_trabajo");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {

                int nur = rs.getInt(1);

                if (nro >= nur) {

                    jLabelImprimir.setVisible(false);
                    jLabelEditar.setVisible(false);

                    jButtonVender.setVisible(true);
                    jLabelFecha.setText(Fechas.fechaActual());
                    limpiar();
                    nroVenta();
                    editar = true;

                } else {
                    nro++;
                    jLabelNoVenta.setText("" + nro);
                    limpiar();
                    buscarDetalle(String.valueOf(nro));
                    buscarOrden(String.valueOf(nro));
                }
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_jLabelAdelanteMouseClicked

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
                Moto moto = new Moto(this, true);
                moto.setPlaca(jTextFieldPlaca.getText());
                moto.setVisible(true);
            }

        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(this, e);
        }
    }
    private void jButtonBuscandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscandoActionPerformed
        new Buscar_Orden(this, true).setVisible(true);
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

    private void jTextFieldDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescuentoKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            double porDes = Double.parseDouble(jTextFieldDescuento.getText().trim());
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                jTableVenta.setValueAt(porDes, i, 3);

            }
        }
    }//GEN-LAST:event_jTextFieldDescuentoKeyPressed

    private void jTextFieldDescuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescuentoKeyTyped
        if (Validaciones.validarString(evt)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldDescuentoKeyTyped

    private void jLabelRegresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseExited
        jLabelRegresar.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelRegresarMouseExited

    private void jLabelAdelanteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAdelanteMouseExited
        jLabelAdelante.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelAdelanteMouseExited

    private void jLabelRegresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseEntered
        jLabelRegresar.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelRegresarMouseEntered

    private void jLabelAdelanteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAdelanteMouseEntered
        jLabelAdelante.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelAdelanteMouseEntered

    private void jButtonBuscandoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBuscandoMouseEntered
        jButtonBuscando.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jButtonBuscandoMouseEntered

    private void jButtonBuscandoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBuscandoMouseExited
        jButtonBuscando.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jButtonBuscandoMouseExited

    private void jLabelImprimirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImprimirMouseExited
        jLabelImprimir.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelImprimirMouseExited

    private void jLabelImprimirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelImprimirMouseEntered
        jLabelImprimir.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabelImprimirMouseEntered

    private void jLabelEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelEditarMouseClicked
        if (!editar) {
            modificarOrden();
            
        }
    }//GEN-LAST:event_jLabelEditarMouseClicked

    private void jLabelEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelEditarMouseEntered
        jLabelEditar.setBackground(new Color(115, 115, 115));
    }//GEN-LAST:event_jLabelEditarMouseEntered

    private void jLabelEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelEditarMouseExited
        jLabelEditar.setBackground(new Color(51, 51, 51));
    }//GEN-LAST:event_jLabelEditarMouseExited

    private void jTableVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableVentaKeyPressed
        DefaultTableModel df = (DefaultTableModel) jTableVenta.getModel();
        if (!Validaciones.validarSuprimir(evt)) {
            int i = jTableVenta.getSelectedRow();
            df.removeRow(i);
            total();
            utilidaTotal.remove(i);
            precioCosto.remove(i);
            Servicio.remove(i);
        }
    }//GEN-LAST:event_jTableVentaKeyPressed

    private void botonEnviarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEnviarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEnviarMouseEntered

    private void botonEnviarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEnviarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_botonEnviarMouseExited

    private void botonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEnviarActionPerformed
        enviarOrden();
        dispose();
    }//GEN-LAST:event_botonEnviarActionPerformed
    void enviarOrden(){
        Ventas ventas = new Ventas();
        ventas.setVisible(true);
        ventas.recibirOrden(jLabelNoVenta.getText());
        ventas.buscarcl();
    }
    boolean editar = true;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonEnviar;
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
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAdelante;
    private javax.swing.JLabel jLabelBuscar;
    private javax.swing.JLabel jLabelEditar;
    private static javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelImprimir;
    public static javax.swing.JLabel jLabelNoVenta;
    private javax.swing.JLabel jLabelRegresar;
    protected static javax.swing.JLabel jLabelSaldo;
    private static javax.swing.JLabel jLabelTelefono;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTableVenta;
    private static javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextField jTextFieldCedula;
    public static javax.swing.JTextField jTextFieldCodigo;
    public static javax.swing.JTextField jTextFieldColor;
    public static javax.swing.JTextField jTextFieldDescuento;
    public static javax.swing.JTextField jTextFieldMoto;
    private static javax.swing.JTextField jTextFieldNombre;
    public static javax.swing.JTextField jTextFieldPlaca;
    public static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables

    private void modificarOrden() {
        double costo = 0;
        double utilidad = 0;
        for (int i = 0; i < precioCosto.size(); i++) {
            costo = +Double.parseDouble(precioCosto.get(i).toString());
            utilidad = +Double.parseDouble(utilidaTotal.get(i).toString());
        }
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr = cn.prepareStatement("UPDATE orden_trabajo SET "
                    + "cliente = ?, cedula_cliente=?, utilidad=?,precioCosto=?,precio_Total=?,moto=?,placa=?,color=?,idEmpleado=?"
                    + " WHERE (nroOrden=? and id=?)");
            pr.setString(1, jTextFieldNombre.getText());
            pr.setString(2, jTextFieldCedula.getText());
            pr.setDouble(3, utilidad);
            pr.setDouble(4, costo);
            pr.setDouble(5, Double.parseDouble(jTextFieldTotal.getText().replace(",", "")));
            pr.setString(6, jTextFieldMoto.getText());
            pr.setString(7, jTextFieldPlaca.getText());
            pr.setString(8, jTextFieldColor.getText());
            pr.setInt(9, jComboBox1.getSelectedIndex());

            pr.setInt(10, Integer.parseInt(jLabelNoVenta.getText()));
            pr.setInt(11, idOrden);
            pr.executeUpdate();

            JOptionPane.showMessageDialog(this, "Guardado Extitoso");
        } catch (SQLException ex) {
            System.err.println(ex);
        }
            eliminarDetalle();
            detalleOrden();
    }

    
    void eliminarDetalle(){
        try (Connection cn = Conexion.Conexion()){
            PreparedStatement pr = cn.prepareStatement("DELETE FROM detalles_orden WHERE (nro_Orden = ?)");
            pr.setString(1, jLabelNoVenta.getText());
            pr.execute();
        } catch (Exception e) {
        }
    }
}
