package Ventanas;

import Clases.Conexion;
import Clases.Fechas;
import Clases.Fondo;
import Clases.FormatoPesos;
import Clases.Imagenes;
import Clases.Validaciones;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author harol
 */
public final class Producto extends javax.swing.JFrame {

    public static int idp;

    public Producto() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();

        setLocationRelativeTo(null);
        setResizable(false);
        new Imagenes("disco-flexible.png", jLabelListo, 56, 56);
        jLabelFecha.setText(Fechas.fechaActual());
        jTextFieldCantidad.setText("0");
        llenarTipo();
        llenarProveedor();
        llenarMarca();
        llenarSeccion();
        llenarInventario();

        this.repaint();
        cerra();
    }

    public static void modificar(int id) {
        try {
            idp=id;
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select * from producto where idProducto = ?");
            pr.setInt(1, id);
            ResultSet rs;
            rs = pr.executeQuery();
            while (rs.next()) {
                DecimalFormat dm = new DecimalFormat("######");
                jTextFieldCodigo.setText(rs.getString(2));
                jTextFieldCodigoB.setText(rs.getString(3));
                jTextFieldProducto.setText(rs.getString(4));
                jTextFieldCantidad.setText(rs.getString(5));
                jTextFieldPrecio_C.setText(dm.format(rs.getDouble(6)));
                jTextFieldPrecio_V.setText(dm.format(rs.getDouble(8)));
                jTextFieldUtilidad.setText(dm.format(rs.getDouble(9)));
                jTextFieldUtilidad_Por.setText(rs.getString(10));
                jComboBoxProveedor.setSelectedItem(rs.getString(14));
                jComboBoxMarca.setSelectedItem(rs.getString(15));
                jComboBoxTipo_Producto.setSelectedItem(rs.getString(16));
                jComboBoxSeccion.setSelectedItem(rs.getString(17));
                jTextFieldCantidadMin.setText(rs.getString(13));
                
                jComboBoxMarca1.setSelectedItem(rs.getString(18));
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void llenarTipo() {
        llenarCombo(jComboBoxTipo_Producto, "tipo", "Tipo");
    }

    public static void llenarSeccion() {
        String col = "Seccion";
        llenarCombo(jComboBoxSeccion, "seccion", col);
    }

    public static void llenarProveedor() {
        String col = "Nombre";
        llenarCombo(jComboBoxProveedor, col, "proveedor");
    }

    public static void llenarMarca() {
        String col = "Marca";
        llenarCombo(jComboBoxMarca, col, "marca");
    }
public static void llenarInventario() {
        String col = "nombre";
        llenarCombo(jComboBoxMarca1, col, "subinventario");
    }
    public static void llenarCombo(JComboBox jCom, String columna, String tabla) {
        try {
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select " + columna + " from " + tabla);
            ResultSet rs = pr.executeQuery();
            jCom.addItem("Selecciona");
            while (rs.next()) {
                String llenar;
                llenar = rs.getString(1);
                jCom.addItem(llenar);
            }
            jCom.addItem("Agregar");
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void actualizar(int id) {
        String codigo = jTextFieldCodigo.getText();
        String codigo_B = jTextFieldCodigoB.getText();
        String product = jTextFieldProducto.getText();
        Double precio_C = Double.valueOf(jTextFieldPrecio_C.getText());
        Double precio_V = Double.valueOf(jTextFieldPrecio_V.getText());
        int cantidad = Integer.parseInt(jTextFieldCantidad.getText());
        Double utilidad = Double.valueOf(jTextFieldUtilidad.getText());
        Double utilidad_Por = Double.valueOf(jTextFieldUtilidad_Por.getText());
        String tipo = jComboBoxTipo_Producto.getSelectedItem().toString();
        String seccion = jComboBoxSeccion.getSelectedItem().toString();
        String prov = jComboBoxProveedor.getSelectedItem().toString();
        String marca = jComboBoxMarca.getSelectedItem().toString();
        String subInventario = jComboBoxMarca1.getSelectedItem().toString();
        java.sql.Date fecha_i_bd = new java.sql.Date(Fechas.fechaActualDate().getTime());
        if (!("".equals(codigo + codigo_B + product + tipo + seccion) && precio_C + precio_V == 0)) {
            try (Connection cn = Conexion.Conexion()) {
                PreparedStatement pre = cn.prepareStatement("Update producto set codigo = ?, codigo_barras=?, producto=?, precio_compra=?, precio_venta=?, "
                        + "cantidad=?, utilidad=?, porcentaje_utilidad=?, tipo=?, seccion=?, marca=?, proveedor=?, idUsuario=?, fecha_ingreso=?,"
                        + "minimo=?,total_cost=?,subInventario=? where idProducto=?");

                pre.setString(1, codigo);
                pre.setString(2, codigo_B);
                pre.setString(3, product);
                pre.setDouble(4, precio_C);
                pre.setDouble(5, precio_V);
                pre.setInt(6, cantidad);
                pre.setDouble(7, utilidad);
                pre.setDouble(8, utilidad_Por);
                pre.setString(9, tipo);
                pre.setString(10, seccion);
                pre.setString(11, marca);
                pre.setString(12, prov);
                pre.setInt(13, Login.idUsuario);
                pre.setDate(14, fecha_i_bd);
                pre.setString(15, jTextFieldCantidadMin.getText().trim());
                pre.setDouble(16, (precio_C * cantidad));
                pre.setString(17, subInventario);
                pre.setInt(18, id);

                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Actualizacion exitoso");
            } catch (SQLException e) {
                System.err.println("Error al actualizar el producto " + e);
                JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo.");
            }
        }
    }

    public void agregar() {
        String codigo = jTextFieldCodigo.getText();

        String codigo_B = jTextFieldCodigoB.getText();

        String product = jTextFieldProducto.getText();

        Double precio_C = Double.valueOf(jTextFieldPrecio_C.getText());

        Double precio_V = Double.valueOf(jTextFieldPrecio_V.getText());

        int cantidad = Integer.parseInt(jTextFieldCantidad.getText());

        Double utilidad = Double.valueOf(jTextFieldUtilidad.getText());

        Double utilidad_Por = Double.valueOf(jTextFieldUtilidad_Por.getText());

        String tipo = jComboBoxTipo_Producto.getSelectedItem().toString();

        java.sql.Date fecha_i_bd = new java.sql.Date(Fechas.fechaActualDate().getTime());
        String seccion = jComboBoxSeccion.getSelectedItem().toString();

        String prov = jComboBoxProveedor.getSelectedItem().toString();
        String marca = jComboBoxMarca.getSelectedItem().toString();
        String subInvetario = jComboBoxMarca1.getSelectedItem().toString();

        if (!("".equals(codigo + codigo_B + product + tipo + seccion) && precio_C + precio_V == 0)) {
            try (Connection cn = Conexion.Conexion()) {

                PreparedStatement pre = cn.prepareStatement("INSERT INTO producto (idProducto,codigo,codigo_barras,producto,cantidad,precio_compra,"
                        + "precio_venta,utilidad,porcentaje_utilidad,"
                        + "idUsuario,fecha_ingreso,minimo,proveedor,marca,tipo,seccion,total_cost,subInventario) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pre.setInt(1, 0);
                pre.setString(2, codigo);
                pre.setString(3, codigo_B);
                pre.setString(4, product);
                pre.setInt(5, cantidad);
                pre.setDouble(6, precio_C);
                pre.setDouble(7, precio_V);
                pre.setDouble(8, utilidad);
                pre.setDouble(9, utilidad_Por);

                pre.setInt(10, Login.idUsuario);
                pre.setDate(11, fecha_i_bd);
                pre.setString(12, jTextFieldCantidadMin.getText().trim());
                pre.setString(13, prov);
                pre.setString(14, marca);
                pre.setString(15, tipo);
                pre.setString(16, seccion);
                pre.setDouble(17, precio_C * cantidad);
                pre.setString(18, subInvetario);

                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registro exitoso");
                jTextFieldCantidad.setText("0");
                jTextFieldCodigo.setText("");
                jTextFieldCodigoB.setText("");
                jTextFieldPrecio_C.setText("0");
                jTextFieldPrecio_V.setText("0");
                jTextFieldProducto.setText("");
                jTextFieldUtilidad.setText("0");
                jTextFieldUtilidad_Por.setText("0");
                jTextFieldCantidadMin.setText("0");
                jTextFieldProducto.requestFocus();
                jComboBoxMarca.setSelectedIndex(0);
                jComboBoxMarca1.setSelectedIndex(0);
                jComboBoxProveedor.setSelectedIndex(0);
                jComboBoxSeccion.setSelectedIndex(0);
                jComboBoxTipo_Producto.setSelectedIndex(0);
                Catalogo.buscar();
                Catalogo.total();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al ingresar el producto " + e);
                JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo. \n" + e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Llene Todos Los Campos");
        }
    }

    public void cerra() {
        try {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Catalogo.vent = 0;
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void utilidad() {
        if (!(jTextFieldPrecio_C.getText().equals("") || jTextFieldPrecio_V.getText().equals(""))) {
            double precio_v = Double.parseDouble(jTextFieldPrecio_V.getText());
            double preico_c = Double.parseDouble(jTextFieldPrecio_C.getText());
            double util = precio_v - preico_c;
            double utilpo = (1 - (preico_c / precio_v)) * 100;
            jTextFieldUtilidad.setText(String.valueOf((double) Math.round(util)));
            jTextFieldUtilidad_Por.setText(String.valueOf((double) Math.round(utilpo * 100) / 100));
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
        jTextFieldProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCodigoB = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPrecio_C = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldUtilidad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldUtilidad_Por = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxTipo_Producto = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxSeccion = new javax.swing.JComboBox<>();
        jLabelListo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldPrecio_V = new javax.swing.JTextField();
        jLabelFecha = new javax.swing.JLabel();
        jComboBoxMarca = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldCantidadMin = new javax.swing.JTextField();
        jComboBoxMarca1 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Codigo:");

        jTextFieldProducto.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldProducto.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldProductoKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Codigo de Barra:");

        jTextFieldCodigoB.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldCodigoB.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Producto:");

        jTextFieldCodigo.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldCodigo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Precio Compra:");

        jTextFieldPrecio_C.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldPrecio_C.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldPrecio_C.setText("0");
        jTextFieldPrecio_C.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPrecio_CKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPrecio_CKeyTyped(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Precio Venta:");

        jTextFieldCantidad.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldCantidad.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Utilidad:");

        jTextFieldUtilidad.setEditable(false);
        jTextFieldUtilidad.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldUtilidad.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Utilidad %:");

        jTextFieldUtilidad_Por.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldUtilidad_Por.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldUtilidad_Por.setText("0");
        jTextFieldUtilidad_Por.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldUtilidad_PorKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldUtilidad_PorKeyTyped(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Tipo de Producto:");

        jComboBoxTipo_Producto.setBackground(new java.awt.Color(0, 153, 153));
        jComboBoxTipo_Producto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));
        jComboBoxTipo_Producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipo_ProductoActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Seccion:");

        jComboBoxSeccion.setBackground(new java.awt.Color(0, 153, 153));
        jComboBoxSeccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        jComboBoxSeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSeccionActionPerformed(evt);
            }
        });

        jLabelListo.setText("jLabel12");
        jLabelListo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelListoMouseClicked(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Guardar");

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Cantida:");

        jTextFieldPrecio_V.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldPrecio_V.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldPrecio_V.setText("0");
        jTextFieldPrecio_V.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPrecio_VKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPrecio_VKeyTyped(evt);
            }
        });

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFecha.setText("jLabel13");

        jComboBoxMarca.setBackground(new java.awt.Color(0, 153, 153));
        jComboBoxMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBoxMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMarcaActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Proveedor:");

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Marca:");

        jComboBoxProveedor.setBackground(new java.awt.Color(0, 153, 153));
        jComboBoxProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBoxProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProveedorActionPerformed(evt);
            }
        });

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Cantidad Min:");

        jTextFieldCantidadMin.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldCantidadMin.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jComboBoxMarca1.setBackground(new java.awt.Color(0, 153, 153));
        jComboBoxMarca1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jComboBoxMarca1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMarca1ActionPerformed(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Inventario");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelListo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldPrecio_C, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(48, 48, 48)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel5)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldUtilidad_Por, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel11))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldCantidadMin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPrecio_V)
                                    .addComponent(jComboBoxTipo_Producto, 0, 206, Short.MAX_VALUE)
                                    .addComponent(jComboBoxSeccion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldCodigoB)
                                    .addComponent(jComboBoxMarca1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jTextFieldProducto)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9)
                        .addGap(78, 78, 78)
                        .addComponent(jLabel15)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelFecha)
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCodigoB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPrecio_V, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jComboBoxTipo_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldPrecio_C, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxSeccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldUtilidad_Por, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel9))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldCantidadMin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(jComboBoxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBoxMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelListo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabelListoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelListoMouseClicked
        if (Catalogo.vent == 0 && !productoNegativo.m) {
            agregar();
        } else if (productoNegativo.m) {
            actualizar(idp);
            dispose();
            Administrador.pn.tablaProducto();
        } else {
            actualizar(idp);
            Catalogo.vent = 0;
            dispose();
            Catalogo.buscar();
            Catalogo.total();
        }
    }//GEN-LAST:event_jLabelListoMouseClicked

    private void jTextFieldPrecio_CKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_CKeyReleased
        utilidad();
    }//GEN-LAST:event_jTextFieldPrecio_CKeyReleased

    private void jTextFieldPrecio_VKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_VKeyTyped
        if (Validaciones.validarString(evt)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPrecio_VKeyTyped

    private void jTextFieldPrecio_VKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_VKeyReleased
        utilidad();
    }//GEN-LAST:event_jTextFieldPrecio_VKeyReleased

    private void jTextFieldPrecio_CKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_CKeyTyped
        if (Validaciones.validarString(evt)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPrecio_CKeyTyped

    private void jTextFieldUtilidad_PorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldUtilidad_PorKeyTyped
        if (Validaciones.validarString(evt)) {
            getToolkit().beep();
            evt.consume();
        } else {
        }
    }//GEN-LAST:event_jTextFieldUtilidad_PorKeyTyped

    private void jTextFieldUtilidad_PorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldUtilidad_PorKeyReleased

        if (!jTextFieldPrecio_C.getText().equals("") && !jTextFieldPrecio_V.getText().equals("") && !jTextFieldUtilidad_Por.getText().equals("")) {
            double precio_c = Double.parseDouble(jTextFieldPrecio_C.getText());
            double util = 1 - (Double.parseDouble(jTextFieldUtilidad_Por.getText()) / 100);
            double precio_v = precio_c / util;
            jTextFieldPrecio_V.setText(String.valueOf((double) Math.round(precio_v)));
            jTextFieldUtilidad.setText(String.valueOf((double) Math.round(precio_v - precio_c)));
        }

    }//GEN-LAST:event_jTextFieldUtilidad_PorKeyReleased

    private void jComboBoxTipo_ProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipo_ProductoActionPerformed
        String index = jComboBoxTipo_Producto.getSelectedItem().toString();

        if (index.equals("Agregar")) {
            jComboBoxTipo_Producto.removeAllItems();
            new Tipo().setVisible(true);
        }
    }//GEN-LAST:event_jComboBoxTipo_ProductoActionPerformed

    private void jComboBoxSeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSeccionActionPerformed
        String index = jComboBoxSeccion.getSelectedItem().toString();

        if (index.equals("Agregar")) {
            jComboBoxSeccion.removeAllItems();
            new Seccion().setVisible(true);
        }

    }//GEN-LAST:event_jComboBoxSeccionActionPerformed

    private void jComboBoxMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMarcaActionPerformed
        String index = jComboBoxMarca.getSelectedItem().toString();

        if (index.equals("Agregar")) {
            jComboBoxMarca.removeAllItems();
            Marca j = new Marca();
            j.setVisible(true);
        }
    }//GEN-LAST:event_jComboBoxMarcaActionPerformed

    private void jComboBoxProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProveedorActionPerformed
        String index = jComboBoxProveedor.getSelectedItem().toString();

        if (index.equals("Agregar")) {
            jComboBoxProveedor.removeAllItems();
            AgregarProveedor j = new AgregarProveedor(this, true);
            j.setVisible(true);
        }
    }//GEN-LAST:event_jComboBoxProveedorActionPerformed

    private void jTextFieldProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductoKeyReleased
        if (!Validaciones.validarEnter(evt)) {
            jTextFieldCodigo.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldProductoKeyReleased

    private void jComboBoxMarca1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMarca1ActionPerformed
        String index = jComboBoxMarca1.getSelectedItem().toString();

        if (index.equals("Agregar")) {
            jComboBoxMarca1.removeAllItems();
            SubInventario j = new SubInventario();
            j.setVisible(true);
        }
    }//GEN-LAST:event_jComboBoxMarca1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JComboBox<String> jComboBoxMarca;
    private static javax.swing.JComboBox<String> jComboBoxMarca1;
    private static javax.swing.JComboBox<String> jComboBoxProveedor;
    public static javax.swing.JComboBox<String> jComboBoxSeccion;
    public static javax.swing.JComboBox<String> jComboBoxTipo_Producto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFecha;
    private static javax.swing.JLabel jLabelListo;
    private static javax.swing.JTextField jTextFieldCantidad;
    private static javax.swing.JTextField jTextFieldCantidadMin;
    private static javax.swing.JTextField jTextFieldCodigo;
    private static javax.swing.JTextField jTextFieldCodigoB;
    private static javax.swing.JTextField jTextFieldPrecio_C;
    private static javax.swing.JTextField jTextFieldPrecio_V;
    private static javax.swing.JTextField jTextFieldProducto;
    private static javax.swing.JTextField jTextFieldUtilidad;
    private static javax.swing.JTextField jTextFieldUtilidad_Por;
    // End of variables declaration//GEN-END:variables
}
