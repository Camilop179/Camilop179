package Ventanas;

import Clases.Conexion;
import Clases.Imagenes;
import Clases.TablaFondo;
import Clases.Fondo;
import Clases.FormatoPesos;
import Clases.Imprimir;
import Clases.Validaciones;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author harol
 */
public final class Catalogo extends javax.swing.JFrame {

    static String sql;
    public static int vent = 0;
    static int column = 0;
    static boolean importadora = false;

    public Catalogo() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        jCheckBoxCodigo.setContentAreaFilled(false);
        jCheckBoxCodigo_Barra.setContentAreaFilled(false);
        jCheckBoxNombre.setContentAreaFilled(false);
        setLocationRelativeTo(null);

        new Imagenes("agregar-producto.png", jLabelNP, 70, 60);
        jTextFieldBusqueda.requestFocus();
        inventario();
        total();
        llenarTipo();
        cerra();
        Table.setDefaultRenderer(Object.class, dtc);
    }

    void llenarTipo() {
        jComboBox1.addItem("");
        try (Connection cnn = Conexion.Conexion()) {
            PreparedStatement pre = cnn.prepareStatement("select Tipo from tipo");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                jComboBox1.addItem(rs.getString(1));
            }
            cnn.close();
        } catch (SQLException e) {

        }
    }

    public void cerra() {

        try {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    Ventas.m = false;
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Sacar el Total en Productos
     */
    public static void total() {
        int t = 0;
        for (int i = 0; i < Table.getRowCount(); i++) {
            t += Double.parseDouble(Table.getValueAt(i, 5).toString());
        }

        jTextFieldTotal.setText("$" + FormatoPesos.formato(t));
    }

    /**
     * Consulatar Base de Datos para los Productos
     */
    public static void inventario() {
        if (Ventas.m) {
            sql = "SELECT * FROM(select p.idProducto,p.codigo,p.codigo_barras,p.producto,p.precio_venta,p.cantidad,p.tipo,"
                    + "p.seccion,p.marca,p.proveedor,u.nombre,p.fecha_ingreso,p.minimo,p.subInventario from producto p "
                    + "left join usuarios u on p.idUsuario = u.idusuarios";
            column = 14;
        } else {
            sql = "SELECT * FROM(select p.idProducto,p.codigo,p.codigo_barras,p.producto,p.precio_compra"
                    + ",total_cost,p.precio_venta,p.cantidad,p.utilidad,p.porcentaje_utilidad,p.tipo,p.seccion"
                    + ",p.marca,p.proveedor,u.nombre,p.fecha_ingreso,p.minimo,p.subInventario from producto p left join usuarios u on p.idUsuario = u.idusuarios";
            column = 18;
        }
        DefaultTableModel tabla = tabla(column);

        String[] datos = new String[column];
        try (Connection cnn = Conexion.Conexion()) {
            PreparedStatement pre = cnn.prepareStatement(sql + ") p");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < column; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                if (Ventas.m) {
                    datos[4] = FormatoPesos.formato(rs.getDouble(5));
                }
                tabla.addRow(datos);
            }
            cnn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    DefaultTableCellRenderer dtc = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (table.getColumnCount() == 18) {
                if (column == 4) {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
                else if (column == 5) {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
                else if (column == 6) {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
            } else {
                if (column == 4) {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
            }
            return this;
        }

    };

    public static DefaultTableModel tabla(int column) {
        DefaultTableModel tabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.addColumn("id");
        tabla.addColumn("Codigo");
        tabla.addColumn("Codigo Barra");
        tabla.addColumn("Producto");
        if (!Ventas.m) {
            tabla.addColumn("Precio Costo");
            tabla.addColumn("Total");
        }
        tabla.addColumn("Precio Venta");
        tabla.addColumn("Cantidad");
        if (!Ventas.m) {
            tabla.addColumn("Utilidad");
            tabla.addColumn("Utilida %");
        }
        tabla.addColumn("Tipo");
        tabla.addColumn("Seccion");
        tabla.addColumn("Marca");
        tabla.addColumn("Proveedor");
        tabla.addColumn("Usuario");
        tabla.addColumn("Fecha Entrada");
        tabla.addColumn("Minimo");
        tabla.addColumn("Sub Inventario");

        Table.setModel(tabla);

        TableColumnModel columnModel = Table.getColumnModel();

        for (int i = 0; i < column; i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(400);
        if (!Ventas.m) {
            columnModel.getColumn(14).setPreferredWidth(150);
            columnModel.getColumn(15).setPreferredWidth(150);
        }

        for (int i = 0; i < column; i++) {
            columnModel.getColumn(i).setResizable(false);
        }

        return tabla;
    }

    public static void buscar() {
        boolean[] c = new boolean[]{jCheckBoxCodigo.isSelected(), jCheckBoxCodigo_Barra.isSelected(), jCheckBoxNombre.isSelected()};
        int x = 0;
        boolean n = false;
        String code = "";
        for (int i = 0; i < 3; i++) {
            if (c[i]) {
                if (x > 0) {
                    code += " or ";
                }
                x++;
                switch (i) {
                    case 0 ->
                        code += "p.codigo like ?";
                    case 1 ->
                        code += "p.codigo_barras like ?";
                    case 2 ->
                        code += "p.producto like ?";
                }
            }
        }
        if (jComboBox1.getSelectedIndex() > 0) {
            code = sql + " where " + code + " ) P WHERE P.TIPO =?";
            n = true;
            if (importadora) {
                code += " and subInventario = 'Importadora'";
            }
        } else {
            code = sql + " where " + code + ")p";
            if (importadora) {
                code += " where subInventario = 'Importadora'";
            }
        }

        DefaultTableModel tabla = tabla(column);
        String[] datos = new String[column];
        try (Connection cnn = Conexion.Conexion()) {
            PreparedStatement pre = cnn.prepareStatement(code);
            for (int i = 1; i <= x; i++) {
                pre.setString(i, '%' + jTextFieldBusqueda.getText().trim() + '%');
            }

            if (n) {
                x++;
                pre.setString(x, jComboBox1.getSelectedItem().toString());
            }

            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < column; i++) {
                    datos[i] = rs.getString(i + 1);
                }

                if (Ventas.m) {
                    datos[4] = FormatoPesos.formato(rs.getDouble(5));
                }

                tabla.addRow(datos);
            }
            cnn.close();
        } catch (SQLException e) {
            System.err.println("Error buscar producto: " + e);
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
        Table = new TablaFondo("FondoMenu.jpg");
        jCheckBoxNombre = new javax.swing.JCheckBox();
        jCheckBoxCodigo_Barra = new javax.swing.JCheckBox();
        jCheckBoxCodigo = new javax.swing.JCheckBox();
        jLabelNP = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldBusqueda = new javax.swing.JTextField();
        jTextFieldTotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(1900, 1800));

        jScrollPane1.setBackground(null);
        jScrollPane1.setBorder(null);
        jScrollPane1.setForeground(new java.awt.Color(0, 153, 153));
        jScrollPane1.setOpaque(false);

        Table.setAutoCreateRowSorter(true);
        Table.setBackground(new java.awt.Color(75, 75, 75));
        Table.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 204)));
        Table.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Table.setForeground(new java.awt.Color(0, 0, 0));
        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Table.setAutoscrolls(false);
        Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Table.setGridColor(new java.awt.Color(204, 204, 204));
        Table.setInheritsPopupMenu(true);
        Table.setOpaque(false);
        Table.setRowHeight(30);
        Table.setSelectionBackground(new java.awt.Color(0, 102, 102));
        Table.setSelectionForeground(new java.awt.Color(0, 153, 153));
        Table.getTableHeader().setReorderingAllowed(false);
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        jCheckBoxNombre.setBackground(null);
        jCheckBoxNombre.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxNombre.setSelected(true);
        jCheckBoxNombre.setText("Nombre Producto");
        jCheckBoxNombre.setBorder(null);
        jCheckBoxNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxNombreActionPerformed(evt);
            }
        });

        jCheckBoxCodigo_Barra.setBackground(null);
        jCheckBoxCodigo_Barra.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxCodigo_Barra.setSelected(true);
        jCheckBoxCodigo_Barra.setText("Codigo De Barras");
        jCheckBoxCodigo_Barra.setBorder(null);
        jCheckBoxCodigo_Barra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCodigo_BarraActionPerformed(evt);
            }
        });

        jCheckBoxCodigo.setBackground(null);
        jCheckBoxCodigo.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxCodigo.setSelected(true);
        jCheckBoxCodigo.setText("Codigo");
        jCheckBoxCodigo.setBorder(null);
        jCheckBoxCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCodigoActionPerformed(evt);
            }
        });

        jLabelNP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.blue, new java.awt.Color(51, 51, 51), new java.awt.Color(0, 0, 204), new java.awt.Color(204, 204, 204)));
        jLabelNP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelNP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelNPMouseClicked(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Creado por Corporacion Portillo ADMP ®©™ 2022 V1.0");
        jLabel1.setAlignmentX(253);
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Exclusivo para CaliDrogas El Tambo");
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jTextFieldBusqueda.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldBusquedaKeyReleased(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total:");

        jButton1.setText("MANO DE OBRA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tipo:");

        jButton2.setText("IMPORTADORA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxCodigo_Barra, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldBusqueda)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBoxNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton1)
                                            .addComponent(jCheckBoxCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton2)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)
                        .addComponent(jLabelNP, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(367, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(0, 367, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(184, 184, 184)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelNP, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxCodigo_Barra)
                            .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabelNPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelNPMouseClicked
        if (Administrador.m) {
            new Producto().setVisible(true);
        }
    }//GEN-LAST:event_jLabelNPMouseClicked

    private void jTextFieldBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBusquedaKeyReleased
        buscar();
        total();
    }//GEN-LAST:event_jTextFieldBusquedaKeyReleased

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        String cod;
        int i = Table.getSelectedRow();

        if (evt.getClickCount() == 2) {
            if (Ventas.m) {
                cod = Table.getValueAt(i, 1).toString();
                Ventas.jTextFieldCodigo.setText(cod);
                this.dispose();
                Ventas.producto();
                Ventas.m = false;
            } else if (Compras.n) {
                cod = Table.getValueAt(i, 1).toString();
                Compras.jTextFieldCodigo.setText(cod);
                this.dispose();
                Compras.producto();
                Compras.n = false;
            } else if (CotizacionT.m) {
                cod = Table.getValueAt(i, 1).toString();
                CotizacionT.jTextFieldCodigo.setText(cod);
                this.dispose();
                CotizacionT.producto();
                CotizacionT.m = false;
            } else if (OrdenDeTrabajo.m) {
                cod = Table.getValueAt(i, 1).toString();
                OrdenDeTrabajo.jTextFieldCodigo.setText(cod);
                this.dispose();
                OrdenDeTrabajo.producto();
                OrdenDeTrabajo.m = false;
            } else if (Administrador.m) {
                vent = 1;
                int id = Integer.parseInt(Table.getValueAt(i, 0).toString());
                new Producto().setVisible(true);
                Producto.modificar(id);
            }
        }


    }//GEN-LAST:event_TableMouseClicked

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        if (Administrador.m) {
            if (!Validaciones.validarSuprimir(evt)) {
                try {
                    int k = JOptionPane.showConfirmDialog(null, "Desea eliminar Producto?", "Eliminar Producto", JOptionPane.YES_NO_OPTION);
                    if (k == 0) {
                        int i = Table.getSelectedRow();
                        Connection cn = Conexion.Conexion();
                        PreparedStatement pr = cn.prepareStatement("Delete from producto where (idProducto = ?)");
                        pr.setInt(1, Integer.parseInt(Table.getValueAt(i, 0).toString()));
                        pr.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Producto eliminado");
                        inventario();
                    }
                } catch (HeadlessException | NumberFormatException | SQLException e) {
                }
            }
        }
    }//GEN-LAST:event_TableKeyPressed

    private void jCheckBoxCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCodigoActionPerformed
        buscar();
    }//GEN-LAST:event_jCheckBoxCodigoActionPerformed

    private void jCheckBoxNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxNombreActionPerformed
        buscar();
    }//GEN-LAST:event_jCheckBoxNombreActionPerformed

    private void jCheckBoxCodigo_BarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCodigo_BarraActionPerformed
        buscar();
    }//GEN-LAST:event_jCheckBoxCodigo_BarraActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        new Servicios().setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (importadora) {
            importadora = false;
        } else {
            importadora = true;
        }

        buscar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Object[] opc = new Object[]{"Ticket", "Carta", "NO"};
        int i = JOptionPane.showOptionDialog(null, "Venta Exitosa\n¿Desea imprimir factura?", "Venta Exitosa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);

        if (i == 0) {
            new Imprimir().catalogo();
        } else if (i == 1) {
            new Imprimir().catalogo();
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable Table;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private static javax.swing.JCheckBox jCheckBoxCodigo;
    private static javax.swing.JCheckBox jCheckBoxCodigo_Barra;
    private static javax.swing.JCheckBox jCheckBoxNombre;
    private static javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelNP;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextField jTextFieldBusqueda;
    private static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables

}
