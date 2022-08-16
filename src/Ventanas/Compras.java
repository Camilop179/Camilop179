/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ventanas;

import Clases.ActualizarCantidad;
import Clases.Conexion;
import Clases.Fechas;
import Clases.Fondo;
import Clases.ImagenBoton;
import Clases.Imagenes;
import Clases.Utilidad;
import Clases.Validaciones;
import java.sql.*;
import java.text.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author harol
 */
public class Compras extends javax.swing.JFrame {
    double total = 0;
    double utilidaTotal = 0;
    double utilidad = 0;
    static boolean n = false;

    DefaultTableModel tabla = new DefaultTableModel();

    /**
     * Creates new form Comprs
     */
    public Compras() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();
        new ImagenBoton("vender.png", jButtonVender, 45, 45);
        new ImagenBoton("carrito-de-compras.png", jButtonAgregar, 45, 45);

        new Imagenes("Adelante.png", jLabelRegresar1);
        new Imagenes("Atras.png", jLabelRegresar);
        jTextFieldNombre.setEditable(false);
        jLabelFecha.setText(Fechas.fechaActual());
        jDateChooser_fechav.setDate(Fechas.fechaActualDate());
        jTextFieldCantidad.setText("0");
        this.setLocationRelativeTo(null);
        tamañoColumna();
        if (Reportes.m == 1) {
            jLabelNoCompra.setText("" + Reportes.nro);
        } else {
            nroCompra();
        }
        reportes();
        

    }

    public void reportes() {
        if (Reportes.m == 1) {
            buscarDetalle();
            buscarCompra();
            
        }
    }

    public void buscarDetalle() {
        String[] datos = new String[5];
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr2 = cn.prepareStatement("select codigo,producto,precio,cantidad,total from detallescompra where nro_compra = " + jLabelNoCompra.getText());
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

    public void buscarCompra() {
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select * from compra where numero_factura = " + jLabelNoCompra.getText());
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                jLabelFecha.setText(rs.getString(3));
                jDateChooser_fechav.setDateFormatString(rs.getString(4));
                jTextFieldTotal.setText(rs.getString(5));
                jComboBox1.setSelectedItem(rs.getString(6));
                jTextFieldNit.setText(rs.getString(7));
                jTextFieldNombre.setText(rs.getString(8));
            }

            jTextFieldCambio.setEditable(false);
            jTextFieldAsesorr.setEditable(false);
            jTextFieldCantidad.setEditable(false);
            jTextFieldCodigo.setEditable(false);
            jTextFieldEfectivo.setEditable(false);
            jTextFieldNit.setEditable(false);
            jTextFieldNombre.setEditable(false);
            jTextFieldPrecio.setEditable(false);
            jTextFieldProducto.setEditable(false);
            jTextFieldTotal.setEditable(false);
            jComboBox1.setEditable(false);
            cn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void tamañoColumna() {
        tabla.addColumn("Codigo");
        tabla.addColumn("Producto");
        tabla.addColumn("Precio Unidad");
        tabla.addColumn("Cantidad");
        tabla.addColumn("Total");

        new Imagenes("buscando.png", jLabelBuscar);

        jTableCompra.setModel(tabla);
        TableColumnModel columnModel = jTableCompra.getColumnModel();
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

    public void nroCompra() {

        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(numero_factura) from compra");
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                int nro = rs.getInt(1);
                nro++;
                jLabelNoCompra.setText(""+nro);
            } else {
                jLabelNoCompra.setText(""+1);
            }
        } catch (SQLException e) {
            System.err.println(e);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCompra = new javax.swing.JTable();
        jTextFieldCodigo = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCambio = new javax.swing.JTextField();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabelFecha = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelBuscar = new javax.swing.JLabel();
        jButtonAgregar = new javax.swing.JButton();
        jTextFieldTotal = new javax.swing.JTextField();
        jTextFieldPrecio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldEfectivo = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jTextFieldNit = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldProducto = new javax.swing.JTextField();
        jLabelNoCompra = new javax.swing.JLabel();
        jButtonVender = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser_fechav = new com.toedter.calendar.JDateChooser();
        jLabelRegresar = new javax.swing.JLabel();
        jLabelRegresar1 = new javax.swing.JLabel();
        jTextFieldAsesorr = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setForeground(new java.awt.Color(0, 153, 153));
        jScrollPane1.setName(""); // NOI18N

        jTableCompra.setAutoCreateRowSorter(true);
        jTableCompra.setBackground(new java.awt.Color(0, 204, 204));
        jTableCompra.setForeground(new java.awt.Color(255, 255, 255));
        jTableCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Producto", "Cantidad", "Precio U", "Precio T"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCompra.setAutoscrolls(false);
        jTableCompra.setCellSelectionEnabled(true);
        jTableCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTableCompra.setEditingColumn(0);
        jTableCompra.setOpaque(false);
        jTableCompra.getTableHeader().setReorderingAllowed(false);
        jTableCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableCompraKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCompra);

        jTextFieldCodigo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoActionPerformed(evt);
            }
        });
        jTextFieldCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoKeyPressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Efectivo:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Creado por Corporacion Portillo ADMP ®©™ 2022 V1.0");
        jLabel2.setAlignmentX(253);
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Codigo:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Cambio:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Fecha:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nit:");

        jTextFieldCambio.setEditable(false);
        jTextFieldCambio.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldCambio.setToolTipText("0");

        jTextFieldCantidad.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCantidadKeyPressed(evt);
            }
        });

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nombre:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total:");

        jLabelBuscar.setText("jLabel16");

        jButtonAgregar.setBackground(new java.awt.Color(102, 153, 255));
        jButtonAgregar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setBorder(null);
        jButtonAgregar.setBorderPainted(false);
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldTotal.setToolTipText("");
        jTextFieldTotal.setActionCommand("<Not Set>");

        jTextFieldPrecio.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPrecioKeyPressed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Cantidad");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Precio:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Producto:");

        jTextFieldEfectivo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldEfectivo.setToolTipText("");
        jTextFieldEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldEfectivoKeyReleased(evt);
            }
        });

        jTextFieldNombre.setEditable(false);
        jTextFieldNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreActionPerformed(evt);
            }
        });
        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyPressed(evt);
            }
        });

        jTextFieldNit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldNit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNitKeyPressed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Exclusivo para CaliDrogas El Tambo");
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jTextFieldProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextFieldProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldProductoKeyPressed(evt);
            }
        });

        jLabelNoCompra.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNoCompra.setForeground(new java.awt.Color(255, 255, 255));

        jButtonVender.setBackground(new java.awt.Color(0, 102, 0));
        jButtonVender.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButtonVender.setText("Comprar");
        jButtonVender.setBorderPainted(false);
        jButtonVender.setPreferredSize(new java.awt.Dimension(85, 33));
        jButtonVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVenderActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nro. Compra:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Asesor:");

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Forma De Pago:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Credito" }));

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Feche Vencimiento:");

        jDateChooser_fechav.setDateFormatString("dd/MM/yyyy\n");

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

        jTextFieldAsesorr.setEditable(false);
        jTextFieldAsesorr.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldAsesorr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldAsesorrKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(66, 66, 66)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel15)
                                            .addComponent(jLabel8)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel5)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTextFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jDateChooser_fechav, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTextFieldAsesorr, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextFieldNit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabelNoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addComponent(jLabelRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(117, 117, 117)
                                        .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabelNoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldNit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldAsesorr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addComponent(jButtonAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 51, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser_fechav, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(jButtonVender, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(32, 32, 32))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableCompraKeyPressed
        if (!Validaciones.validarSuprimir(evt)) {

            eliminarProducto();
        }
    }//GEN-LAST:event_jTableCompraKeyPressed

    private void jTextFieldCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoActionPerformed

    }//GEN-LAST:event_jTextFieldCodigoActionPerformed

    private void jTextFieldCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoKeyPressed
        if (!Validaciones.validarEnter(evt)) {

            try {
                String codigo = jTextFieldCodigo.getText().trim();
                Connection cnn = Conexion.Conexion();
                PreparedStatement pre = cnn.prepareStatement("select codigo,producto,precio_venta from producto where codigo = ? or codigo_barras = ?");
                pre.setString(1, codigo);
                pre.setString(2, codigo);
                ResultSet rs = pre.executeQuery();
                if (rs.next()) {
                    jTextFieldCodigo.setText(rs.getString(1));
                    jTextFieldProducto.setText(rs.getString(2));
                    jTextFieldPrecio.setText("" + rs.getDouble(3));
                    jTextFieldProducto.requestFocus();

                }else{
                    
                }

            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }//GEN-LAST:event_jTextFieldCodigoKeyPressed

    private void jTextFieldCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCantidadKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            int cant = Integer.parseInt(jTextFieldCantidad.getText());
            if (!jTextFieldCodigo.getText().equals("") && !jTextFieldCantidad.getText().equals("")) {
                agregarProducto();
                jTextFieldCantidad.setText("");
                jTextFieldPrecio.setText("");
                jTextFieldCodigo.setText("");
                jTextFieldProducto.setText("");
                jTextFieldCantidad.setText("0");
            } else {
                JOptionPane.showMessageDialog(null, "Ingres un Porducto");
            }
            jTextFieldNit.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldCantidadKeyPressed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        int cant = Integer.parseInt(jTextFieldCantidad.getText());
        if (!jTextFieldCodigo.getText().equals("") && !jTextFieldCantidad.getText().equals("") && cant > 0) {
            agregarProducto();
            jTextFieldCantidad.setText("");
            jTextFieldPrecio.setText("");
            jTextFieldCodigo.setText("");
            jTextFieldProducto.setText("");
            jTextFieldCantidad.setText("0");
        } else {
            JOptionPane.showMessageDialog(null, "Ingres un Porducto");
        }
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jTextFieldPrecioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecioKeyPressed
        if (!Validaciones.validarEnter(evt)) {

            jTextFieldCantidad.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldPrecioKeyPressed

    private void jTextFieldEfectivoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEfectivoKeyReleased
        if (!Validaciones.validarString(evt)) {

            jTextFieldCambio.setText("" + (Double.parseDouble(jTextFieldEfectivo.getText()) - Double.parseDouble(jTextFieldTotal.getText())));
        }
    }//GEN-LAST:event_jTextFieldEfectivoKeyReleased

    private void jTextFieldNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldAsesorr.setEditable(true);
            jTextFieldAsesorr.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyPressed

    private void jTextFieldNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNitKeyPressed

        if (!Validaciones.validarEnter(evt)) {
            buscarProveedor();
            jTextFieldNombre.setEditable(true);
            jTextFieldNombre.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldNitKeyPressed

    private void jTextFieldProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductoKeyPressed
        if (!Validaciones.validarEnter(evt)) {

            jTextFieldPrecio.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldProductoKeyPressed

    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed
        compra();
        detalleCompra();
        JOptionPane.showMessageDialog(null, "Compra exitosa");
        utilidaTotal = 0;
        total = 0;
        limpiar();
    }//GEN-LAST:event_jButtonVenderActionPerformed

    private void jTextFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreActionPerformed
       
    }//GEN-LAST:event_jTextFieldNombreActionPerformed

    private void jLabelRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresarMouseClicked
        int nr = Integer.parseInt(jLabelNoCompra.getText());
        if (nr > 1) {
            limpiar();
            nr--;
            if (nr > 0 && nr <= 9) {
                jLabelNoCompra.setText("000000" + nr);
            } else if (nr > 9 && nr <= 99) {
                jLabelNoCompra.setText("00000" + nr);
                jLabelNoCompra.setText("0000" + nr);
            } else if (nr > 999 && nr <= 9999) {
                jLabelNoCompra.setText("000" + nr);
            } else if (nr > 9999 && nr <= 99999) {
                jLabelNoCompra.setText("00" + nr);
            } else if (nr > 99999 && nr <= 999999) {
                jLabelNoCompra.setText("0" + nr);
            } else if (nr > 999999) {
                jLabelNoCompra.setText("" + nr);
            }
            buscarCompra();
            buscarDetalle();
        }
    }//GEN-LAST:event_jLabelRegresarMouseClicked

    private void jLabelRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegresar1MouseClicked
        int nro = Integer.parseInt(jLabelNoCompra.getText());
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select max(numero_factura) from compra");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int nur = rs.getInt(1);
                if (nur < nro) {
                    nroCompra();
                } else {
                    limpiar();
                    if (nro > 0 && nro <= 9) {
                        nro++;
                        jLabelNoCompra.setText("000000" + nro);
                    } else if (nro > 9 && nro <= 99) {
                        nro++;
                        jLabelNoCompra.setText("00000" + nro);
                    } else if (nro > 99 && nro <= 999) {
                        nro++;
                        jLabelNoCompra.setText("0000" + nro);
                    } else if (nro > 999 && nro <= 9999) {
                        nro++;
                        jLabelNoCompra.setText("000" + nro);
                    } else if (nro > 9999 && nro <= 99999) {
                        nro++;
                        jLabelNoCompra.setText("00" + nro);
                    } else if (nro > 99999 && nro <= 999999) {
                        nro++;
                        jLabelNoCompra.setText("0" + nro);
                    } else if (nro > 999999) {
                        nro++;
                        jLabelNoCompra.setText("" + nro);
                    }
                    buscarCompra();
                    buscarDetalle();
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_jLabelRegresar1MouseClicked

    private void jTextFieldAsesorrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAsesorrKeyPressed
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldCodigo.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldAsesorrKeyPressed
    public static void  buscarProveedor() {
        if (!jTextFieldNit.getText().equals("")) {
            try {

                String nit = jTextFieldNit.getText();
                Connection cn = Conexion.Conexion();
                PreparedStatement pr = cn.prepareStatement("select Nombre,Asesor from proveedor where Nit = " + nit + "");
                ResultSet rs = pr.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString(1);
                    String asesor = rs.getString(2);
                    jTextFieldNombre.setText(nombre);
                    jTextFieldAsesorr.setText(asesor);

                    jTextFieldNombre.requestFocus();

                } 
                cn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }else{
            n=true;
            new Proveedor().setVisible(true);
        }
    }

    public void limpiar() {
        for (int i = 0; i < jTableCompra.getRowCount(); i++) {
            tabla.removeRow(i);
        }

        jTextFieldNit.setText("");
        jTextFieldNombre.setText("");
        jTextFieldTotal.setText("0");
        jTextFieldEfectivo.setText("");
        jTextFieldCambio.setText("");
    }

    public void detalleCompra() {
        try {

            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO detallescompra  (id,nro_compra,codigo,producto,precio,cantidad,total) values(?,?,?,?,?,?,?)");
            for (int i = 0; i < jTableCompra.getRowCount(); i++) {
                pr.setInt(1, 0);
                pr.setString(2, jLabelNoCompra.getText());
                pr.setString(3, jTableCompra.getValueAt(i, 0).toString());
                pr.setString(4, jTableCompra.getValueAt(i, 1).toString());
                pr.setDouble(5, Double.parseDouble(jTableCompra.getValueAt(i, 2).toString()));
                pr.setInt(6, Integer.parseInt(jTableCompra.getValueAt(i, 3).toString()));
                pr.setDouble(7, Double.parseDouble(jTableCompra.getValueAt(i, 4).toString()));
                String codigo = jTableCompra.getValueAt(i, 0).toString();
                int cantidad = Integer.parseInt(jTableCompra.getValueAt(i, 3).toString());
                ActualizarCantidad.aumentar(cantidad, codigo);
                pr.executeUpdate();
            }
            nroCompra();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void compra() {
        try {
            String fecha_i = Fechas.fechaActual();
            String fecha_v = ((JTextField) jDateChooser_fechav.getDateEditor().getUiComponent()).getText();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fecha_i_d = formato.parse(fecha_i);
            java.util.Date fecha_v_d = formato.parse(fecha_v);
            java.sql.Date fecho_i_bd = new java.sql.Date(fecha_i_d.getTime());

            java.sql.Date fecho_V_bd = new java.sql.Date(fecha_v_d.getTime());

            int i = jComboBox1.getSelectedIndex();
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("INSERT INTO compra (idcompra,numero_factura,fecha_factura,fecha_vencimiento,precio_factura,forma_pago"
                    + ",Nit,proveedor,estado) values(?,?,?,?,?,?,?,?,?)");
            pr.setInt(1, 0);
            pr.setString(2, jLabelNoCompra.getText());
            pr.setDate(3, fecho_i_bd);
            pr.setDate(4, fecho_V_bd);
            pr.setDouble(5, Double.parseDouble(jTextFieldTotal.getText()));
            pr.setString(6, jComboBox1.getSelectedItem().toString());
            pr.setString(7, jTextFieldNit.getText());
            pr.setString(8, jTextFieldNombre.getText());
            if (i == 0) {
                pr.setString(9, "Cancelado");
                pr.executeUpdate();
            } else {
                pr.setString(9, "Pendiente");

                pr.executeUpdate();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Conectar a la Base de Datos \n "+e);
            System.err.println(e);
        }
    }

    public void eliminarProducto() {
        int row = jTableCompra.getSelectedRow();
        double totald = Double.parseDouble(jTableCompra.getValueAt(row, 4).toString());
        total -= totald;
        jTextFieldTotal.setText("" + total);
        double util = Utilidad.utilidad(jTableCompra.getValueAt(row, 0).toString());
        utilidaTotal -= (util * Integer.parseInt(jTableCompra.getValueAt(row, 3).toString()));
        tabla.removeRow(jTableCompra.getSelectedRow());
    }

    public void agregarProducto() {
        Double precio = Double.parseDouble(jTextFieldPrecio.getText());
        int cantidad = Integer.parseInt(jTextFieldCantidad.getText());
        double total1 = precio * cantidad;
        String[] datos = new String[5];
        datos[0] = jTextFieldCodigo.getText();
        datos[1] = jTextFieldProducto.getText();
        datos[2] = jTextFieldPrecio.getText();
        datos[3] = jTextFieldCantidad.getText();
        datos[4] = "" + total1;
        tabla.addRow(datos);
        total += total1;
        utilidaTotal += (Utilidad.utilidad(jTextFieldCodigo.getText()) * cantidad);
        jTextFieldTotal.setText("" + total);

    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonVender;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser_fechav;
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
    private javax.swing.JLabel jLabelBuscar;
    private javax.swing.JLabel jLabelFecha;
    public static javax.swing.JLabel jLabelNoCompra;
    private javax.swing.JLabel jLabelRegresar;
    private javax.swing.JLabel jLabelRegresar1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTableCompra;
    public static javax.swing.JTextField jTextFieldAsesorr;
    private javax.swing.JTextField jTextFieldCambio;
    private javax.swing.JTextField jTextFieldCantidad;
    private javax.swing.JTextField jTextFieldCodigo;
    private javax.swing.JTextField jTextFieldEfectivo;
    public static javax.swing.JTextField jTextFieldNit;
    public static javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldPrecio;
    private javax.swing.JTextField jTextFieldProducto;
    public static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
