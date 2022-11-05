package Ventanas;

import Clases.ActualizarCantidad;
import Clases.Conexion;
import Clases.Errores;
import Clases.Fechas;
import Clases.Fondo;
import Clases.ImagenBoton;
import Clases.Imagenes;
import Clases.Validaciones;
import Clases.Utilidad;
import java.awt.Color;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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

    static int total = 0;
    public static boolean m = false;
    static ArrayList utilidaTotal = new ArrayList();

    public Ventas() {

        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        setFocusable(true);
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        new ImagenBoton("vender.png", jButtonVender, 45, 45);
        ImageIcon imagen1 = new ImageIcon("src/imagenes/carrito-de-compras.png");
        new Imagenes("buscando.png", jLabelBuscar);
        new Imagenes("Adelante.png", jLabelRegresar1);
        new Imagenes("Atras.png", jLabelRegresar);
        new Imagenes("imprimir.png", jLabelImprimir);
        new ImagenBoton("buscando.png", jButtonBuscando, 38 , 38);
        Icon icono = new ImageIcon(imagen1.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        jLabelFecha.setText(Fechas.fechaActual());
        this.setLocationRelativeTo(null);
        m = true;
        if (Reportes.m == 1) {
            jLabelImprimir.setVisible(true);
            jLabelNoVenta.setText("" + Reportes.nro);
        } else {
            jLabelImprimir.setVisible(false);
            nroVenta();
        }
        jScrollPane2.getViewport().setBackground(new Color(51, 153, 255));
        tamañoColumna();

        reportes();
        cerra();

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
                    } else {
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            });
        } catch (Exception e) {
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
        String[] datos = new String[5];
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr2 = cn.prepareStatement("select codigo,producto,precioUnitario,cantidad,PrecioTotal from detallesventa where nro_venta = ?");
            pr2.setString(1, NroVenta);
            ResultSet rs2 = pr2.executeQuery();
            while (rs2.next()) {
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs2.getString(i + 1);
                }
                tabla.addRow(datos);

            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void buscarVentas(String NroVenta) {
        try {
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select * from ventas where nroVentas = ?");
            pr.setString(1, NroVenta);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                jLabelFecha.setText(rs.getString(7));
                jTextFieldTotal.setText(rs.getString(8));
                jTextFieldCedula.setText(rs.getString(4));
                jTextFieldNombre.setText(rs.getString(3));
                jTextFieldPlaca.setText(rs.getString(13));
                jTextFieldMoto.setText(rs.getString(12));
                jTextFieldColor.setText(rs.getString(14));
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

        jTableVenta.setEditingColumn(-1);

        jTableVenta.setModel(tabla);
        TableColumnModel columnModel = jTableVenta.getColumnModel();
        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);

        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(300);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);

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
        } catch (SQLException e) {
            System.err.println(e);
        }
        return nro_venta;
    }

    public void imprimir1() {
        JasperReport jr;
        URL file = this.getClass().getResource("/Clases/report1.jasper");
        try {
            Connection cn = Conexion.Conexion();
            Map parametro = new HashMap();
            URL url = getClass().getResource("/imagenes/LOGO.jpg");
            parametro.put("NroVentas", Integer.parseInt(jLabelNoVenta.getText()));
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
      public void vender() {
        new FormaPago(this, true).setVisible(true);
        if (FormaPago.m) {
            int n = JOptionPane.showConfirmDialog(null, "¿Desea imprimir Factura?", "Venta Exitosa", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                imprimir1();
            }
            jTextFieldCedula.setText("");
            jTextFieldNombre.setText("");
            jTextFieldTotal.setText("0");
            limpiar();
            utilidaTotal.clear();
            nroVenta();
            Administrador.ventas();
        }
    }

    public static void total() {
        DecimalFormatSymbols sm = new DecimalFormatSymbols();
        sm.setDecimalSeparator('.');
        sm.setGroupingSeparator(',');
        DecimalFormat dm = new DecimalFormat("###,###",sm);
        int t = 0;
        for (int i = 0; i < jTableVenta.getRowCount(); i++) {
            t += Integer.parseInt(jTableVenta.getValueAt(i, 4).toString().replace(",", ""));
        }
        jTextFieldTotal.setText(dm.format(t));
    }

    public static void producto() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        DecimalFormatSymbols sm = new DecimalFormatSymbols();
        sm.setDecimalSeparator('.');
        sm.setGroupingSeparator(',');
        DecimalFormat dm = new DecimalFormat("###,###",sm);
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
                    int cant = Integer.parseInt(jTableVenta.getValueAt(i, 3).toString());
                    int precio = Integer.parseInt(jTableVenta.getValueAt(i, 2).toString().replaceAll("[\\D]", ""));
                    cant++;
                    int totalV = precio * cant;
                    jTableVenta.setValueAt(cant, i, 3);
                    jTableVenta.setValueAt(dm.format(totalV), i, 4);
                    utilidaTotal.set(i, (precio-rs.getDouble(4))* cant);
                    System.out.println(utilidaTotal);
                    total();
                } else {
                    String[] datos = new String[5];
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = dm.format(rs.getInt(3));
                    datos[3] = "1";
                    datos[4] = dm.format(rs.getInt(3));
                    tabla.addRow(datos);
                    Object obg = rs.getDouble(3)-rs.getDouble(4);
                    utilidaTotal.add(obg);
                    System.out.println(utilidaTotal);
                    total();
                }
                jTextFieldCodigo.setText("");
            } else {
                m = true;
                new Catalogo().setVisible(true);
            }

        } catch (SQLException e) {
            System.err.println(e);
            Errores.Errores("Error al Agregar Producto: " + e);
        }
    }

    public void buscarcl() {
        if (!jTextFieldCedula.getText().equals("")) {
            try ( Connection cn = Conexion.Conexion()) {

                String cedula = jTextFieldCedula.getText();
                PreparedStatement pr = cn.prepareStatement("select nombres from clientes where cedula = ?");
                pr.setString(1, cedula);
                ResultSet rs = pr.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString(1);
                    jTextFieldNombre.setText(nombre);

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

        }
    }

    public static void detalleVenta() {
        try {

            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO detallesventa (iddetallesVenta,nro_venta,codigo,producto,precioUnitario,cantidad,utilidad,precioTotal) values(?,?,?,?,?,?,?,?)");
            for (int i = 0; i < jTableVenta.getRowCount(); i++) {
                pr.setInt(1, 0);
                pr.setInt(2, Integer.parseInt(jLabelNoVenta.getText()));
                pr.setString(3, jTableVenta.getValueAt(i, 0).toString());
                pr.setString(4, jTableVenta.getValueAt(i, 1).toString());
                pr.setDouble(5, Double.parseDouble(jTableVenta.getValueAt(i, 2).toString().replace(",", "")));
                pr.setInt(6, Integer.parseInt(jTableVenta.getValueAt(i, 3).toString()));
                pr.setDouble(7, (double) utilidaTotal.get(i));
                pr.setDouble(8, Double.parseDouble(jTableVenta.getValueAt(i, 4).toString().replace(",", "")));
                pr.executeUpdate();
                String codigo = jTableVenta.getValueAt(i, 0).toString();
                int cantidad = Integer.parseInt(jTableVenta.getValueAt(i, 3).toString());
                ActualizarCantidad.restar(cantidad, codigo);
            }

        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir detalles venta: "+e);
            Errores.Errores("Error al Subir Detalles de venta: " + e);
        }
    }

    public static void venta(String FormaPago, double cambio, double efectivo) {
        try {
            double utilidad = 0;
            for (int i = 0; i < utilidaTotal.size(); i++) {
                utilidad += Double.parseDouble(utilidaTotal.get(i).toString());
            }
            int nro = nroVenta();
            java.sql.Date fecho_i_bd = new java.sql.Date(Fechas.fechaActualDate().getTime());
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO ventas (idventas,nroVentas,cliente,cedula_cliente,idUsuario,utilidad,fecha,precio_Total,Efectivo,Cambio,FormaPago,moto,placa,color) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pr.setInt(1, 0);
            pr.setInt(2, nro);
            pr.setString(3, jTextFieldNombre.getText());
            if (jTextFieldCedula.getText().equals("")) {
                pr.setInt(4, 0);
            } else {
                pr.setInt(4, Integer.parseInt(jTextFieldCedula.getText().trim()));
            }
            pr.setInt(5, Login.idUsuario);
            pr.setDouble(6, utilidad);
            pr.setDate(7, fecho_i_bd);
            pr.setDouble(8, Double.parseDouble(jTextFieldTotal.getText().replace(",", "")));
            pr.setDouble(9, efectivo);
            pr.setDouble(10, cambio);
            pr.setString(11, FormaPago);
            pr.setString(12, jTextFieldMoto.getText().trim());
            pr.setString(13, jTextFieldPlaca.getText().trim());
            pr.setString(14, jTextFieldColor.getText().trim());

            pr.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al subir Venta: "+e);
            Errores.Errores("Error al subir venta: " + e);
        }
    }

    public void eliminarProducto() {
        DefaultTableModel tabla = (DefaultTableModel) jTableVenta.getModel();
        int row = jTableVenta.getSelectedRow();
        utilidaTotal.remove(row);
        tabla.removeRow(jTableVenta.getSelectedRow());
        total();
    }

    public void cambiarCant() {
        DecimalFormatSymbols sm = new DecimalFormatSymbols();
        sm.setDecimalSeparator('.');
        sm.setGroupingSeparator(',');
        DecimalFormat dm = new DecimalFormat("###,###",sm);
        int row = jTableVenta.getSelectedRow();
        String codigo = jTableVenta.getValueAt(row, 0).toString();
        int cant = Integer.parseInt(jTableVenta.getValueAt(row, 3).toString());
        int precio = Integer.parseInt(jTableVenta.getValueAt(row, 2).toString().replace(",", ""));
        int total1 = cant * precio;
        double util = (precio - Utilidad.costo(codigo))*cant;
        utilidaTotal.set(row, util);
        jTableVenta.setValueAt(dm.format(total1), row, 4);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(1000, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Cedula:");

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
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
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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

        jLabelBuscar.setText("jLabel16");
        jLabelBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelBuscarMouseClicked(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Creado por Corporacion Portillo ADMP ®©™ 2022 V1.0");
        jLabel2.setAlignmentX(253);
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Exclusivo para CaliDrogas El Tambo");
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableVentaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableVenta);

        jLabelRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegresarMouseClicked(evt);
            }
        });

        jLabelRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegresar1MouseClicked(evt);
            }
        });

        jLabelImprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelImprimirMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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

        jButtonBuscando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscandoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel5)
                                .addGap(16, 16, 16)
                                .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(250, 250, 250))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(187, 187, 187)
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelNoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(31, 31, 31)
                                                    .addComponent(jLabel6)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                    .addGap(83, 83, 83)
                                                    .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(71, 71, 71)
                                        .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBuscando, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelRegresar1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabelRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBuscando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addContainerGap())
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
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldMoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(177, 177, 177)
                        .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 108, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed
        System.out.println(jTableVenta.getRowCount());
        if (jTableVenta.getRowCount() != 0) {
            vender();
        } else {
            JOptionPane.showMessageDialog(this, "No hay Productos Para Venta");
        }
    }//GEN-LAST:event_jButtonVenderActionPerformed

    private void jTextFieldCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCedulaKeyPressed

        if (!Validaciones.validarEnter(evt)) {
            buscarcl();
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
        int i = JOptionPane.showConfirmDialog(null, "Imprimir factura", "¿Desea imprimir Factura?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (i == 0) {
            imprimir1();
        }
    }//GEN-LAST:event_jLabelImprimirMouseClicked

    private void jTextFieldCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            producto();
        } else if ((evt.getKeyCode() == 71) && (evt.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
            vender();
        }
    }//GEN-LAST:event_jTextFieldCodigoKeyPressed

    private void jLabelBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelBuscarMouseClicked
        m = true;
        new Catalogo().setVisible(true);

    }//GEN-LAST:event_jLabelBuscarMouseClicked

    private void jTableVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableVentaKeyPressed
        if (!Validaciones.validarSuprimir(evt)) {
            eliminarProducto();
        }
    }//GEN-LAST:event_jTableVentaKeyPressed

    private void jTableVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableVentaKeyReleased
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("###,###",dfs);
        
        int fila = jTableVenta.getSelectedRow();
        int precio = Integer.parseInt(jTableVenta.getValueAt(fila,2).toString().replace(",", ""));
        jTableVenta.setValueAt(df.format(precio), fila, 2);
        if (!Validaciones.validarEnter(evt)) {
            cambiarCant();
            total();
        }
    }//GEN-LAST:event_jTableVentaKeyReleased

    private void jTextFieldMotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMotoKeyPressed
        if(!Validaciones.validarEnter(evt)){
            jTextFieldColor.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldMotoKeyPressed

    private void jTextFieldColorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldColorKeyPressed
        if(!Validaciones.validarEnter(evt)){
            jTextFieldPlaca.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldColorKeyPressed

    private void jTextFieldPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPlacaKeyPressed
        if(!Validaciones.validarEnter(evt)){
            jTextFieldCodigo.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldPlacaKeyPressed

    private void jButtonBuscandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscandoActionPerformed
        new Buscar_Venta(this, true).setVisible(true);
    }//GEN-LAST:event_jButtonBuscandoActionPerformed

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscando;
    private javax.swing.JButton jButtonVender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTableVenta;
    public static javax.swing.JTextField jTextFieldCedula;
    public static javax.swing.JTextField jTextFieldCodigo;
    public static javax.swing.JTextField jTextFieldColor;
    public static javax.swing.JTextField jTextFieldMoto;
    private static javax.swing.JTextField jTextFieldNombre;
    public static javax.swing.JTextField jTextFieldPlaca;
    public static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
