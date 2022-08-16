package Ventanas;

import Clases.Conexion;
import Clases.Imagenes;
import Clases.TablaFondo;
import Clases.Fondo;
import Clases.Validaciones;
import java.awt.HeadlessException;
import java.math.BigInteger;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author harol
 */
public final class Catalogo extends javax.swing.JFrame {

    public static int m = 0;

    public Catalogo() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        jCheckBoxCodigo.setContentAreaFilled(false);
        jCheckBoxCodigo_Barra.setContentAreaFilled(false);
        jCheckBoxNombre.setContentAreaFilled(false);
        setLocationRelativeTo(null);

        new Imagenes("Agregar.gif", jLabelNP);

        inventario();
        total();
    }

    /**
     * Sacar el Total en Productos
     */
    public static void total() {
        int t = 0;
        for (int i = 0; i < Table.getRowCount(); i++) {
            t += Double.parseDouble(Table.getValueAt(i,5).toString());
        }

        DecimalFormat dm = new DecimalFormat("###,###");

        jTextFieldTotal.setText("$" + dm.format(t));
    }

    /**
     * Consulatar Base de Datos para los Productos
     */
    public static void inventario() {
        DefaultTableModel tabla = tabla();

        String[] datos = new String[17];
        try {
            Connection cnn = Conexion.Conexion();

            PreparedStatement pre = cnn.prepareStatement("select p.idProducto,p.codigo,p.codigo_barras,p.producto,p.precio_compra"
                    + ",total_cost,p.precio_venta,p.cantidad,p.utilidad,p.porcentaje_utilidad,p.tipo,p.seccion"
                    + ",p.marca,p.proveedor,u.nombre,p.fecha_ingreso,p.fecha_vencimiento from producto p left join usuarios u on p.idUsuario = u.idusuarios");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < 17; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                tabla.addRow(datos);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
        

    }
    public static void total_bd(){
        for(int i=0;i<Table.getRowCount();i++){
            double cost = Double.parseDouble(Table.getValueAt(i, 4).toString());
            int cant = Integer.parseInt(Table.getValueAt(i, 7).toString());
            
            double total = cost*cant;
            
            try (Connection cn = Conexion.Conexion()){
                PreparedStatement ps = cn.prepareStatement("update producto set total_cost=? where idProducto=?");
                ps.setDouble(1, total);
                ps.setInt(2, Integer.parseInt(Table.getValueAt(i, 0).toString()));
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public static DefaultTableModel tabla() {
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
        tabla.addColumn("Precio Costo");
        tabla.addColumn("Total");
        tabla.addColumn("Precio Venta");
        tabla.addColumn("Cantidad");
        tabla.addColumn("Utilidad");
        tabla.addColumn("Utilida %");
        tabla.addColumn("Tipo");
        tabla.addColumn("Seccion");
        tabla.addColumn("Marca");
        tabla.addColumn("Proveedor");
        tabla.addColumn("Usuario");
        tabla.addColumn("Fecha Entrada");
        tabla.addColumn("Fecha Vencimiento");

        Table.setModel(tabla);

        TableColumnModel columnModel = Table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(300);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(100);
        columnModel.getColumn(8).setPreferredWidth(100);
        columnModel.getColumn(9).setPreferredWidth(100);
        columnModel.getColumn(10).setPreferredWidth(100);
        columnModel.getColumn(11).setPreferredWidth(100);
        columnModel.getColumn(12).setPreferredWidth(100);
        columnModel.getColumn(13).setPreferredWidth(150);
        columnModel.getColumn(14).setPreferredWidth(150);
        columnModel.getColumn(15).setPreferredWidth(150);

        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);
        columnModel.getColumn(5).setResizable(false);
        columnModel.getColumn(6).setResizable(false);
        columnModel.getColumn(7).setResizable(false);
        columnModel.getColumn(8).setResizable(false);
        columnModel.getColumn(9).setResizable(false);
        columnModel.getColumn(10).setResizable(false);
        columnModel.getColumn(11).setResizable(false);
        columnModel.getColumn(12).setResizable(false);
        columnModel.getColumn(13).setResizable(false);
        columnModel.getColumn(14).setResizable(false);
        columnModel.getColumn(15).setResizable(false);

        return tabla;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(1900, 1800));

        jScrollPane1.setBackground(null);
        jScrollPane1.setBorder(null);
        jScrollPane1.setForeground(new java.awt.Color(0, 153, 153));
        jScrollPane1.setOpaque(false);

        Table.setAutoCreateRowSorter(true);
        Table.setBackground(new java.awt.Color(0, 204, 204));
        Table.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 204, 204)));
        Table.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Table.setForeground(new java.awt.Color(0, 51, 51));
        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Table.setAutoscrolls(false);
        Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Table.setGridColor(new java.awt.Color(0, 153, 153));
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
        jCheckBoxNombre.setText("Nombre Producto");
        jCheckBoxNombre.setBorder(null);
        jCheckBoxNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxNombreActionPerformed(evt);
            }
        });

        jCheckBoxCodigo_Barra.setBackground(null);
        jCheckBoxCodigo_Barra.setText("Codigo De Barras");
        jCheckBoxCodigo_Barra.setBorder(null);
        jCheckBoxCodigo_Barra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCodigo_BarraActionPerformed(evt);
            }
        });

        jCheckBoxCodigo.setBackground(null);
        jCheckBoxCodigo.setText("Codigo");
        jCheckBoxCodigo.setBorder(null);
        jCheckBoxCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCodigoActionPerformed(evt);
            }
        });

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

        jLabel3.setText("Total:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBoxCodigo_Barra, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBoxNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelNP, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(367, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(0, 367, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(148, 148, 148)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelNP, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxCodigo_Barra)
                            .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
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
        
        total_bd();
        boolean codigo = jCheckBoxCodigo.isSelected();
        boolean codigoBarra = jCheckBoxCodigo_Barra.isSelected();
        boolean nombre = jCheckBoxNombre.isSelected();
        String codigoS = "codigo";
        String nombreS = "producto";
        String codigoBarr = "codigo_barras";
        if (codigo == true) {
            buscar(codigoS);
        }
        if (codigoBarra == true) {
            buscar(codigoBarr);
        }
        if (nombre == true) {
            buscar(nombreS);
        }
    }//GEN-LAST:event_jTextFieldBusquedaKeyReleased

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        String cod;
        int i = Table.getSelectedRow();
        if (Administrador.m) {
            if (evt.getClickCount() == 2) {
                if (Ventas.m == true) {
                    cod = Table.getValueAt(i, 1).toString();
                    Ventas.jTextFieldCodigo.setText(cod);
                    this.dispose();
                    Ventas.producto();
                } else {
                    m = 1;
                    int id = Integer.parseInt(Table.getValueAt(i, 0).toString());
                    new Producto().setVisible(true);
                    Producto.modificar(id);
                    Producto.idp = id;
                }
            }
        } else if(Ventas.m) {
            cod = Table.getValueAt(i, 1).toString();
            Ventas.jTextFieldCodigo.setText(cod);
            this.dispose();
            Ventas.producto();
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
        jCheckBoxNombre.setSelected(false);
        jCheckBoxCodigo_Barra.setSelected(false);
    }//GEN-LAST:event_jCheckBoxCodigoActionPerformed

    private void jCheckBoxNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxNombreActionPerformed
        jCheckBoxCodigo.setSelected(false);
        jCheckBoxCodigo_Barra.setSelected(false);

    }//GEN-LAST:event_jCheckBoxNombreActionPerformed

    private void jCheckBoxCodigo_BarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCodigo_BarraActionPerformed
        jCheckBoxCodigo.setSelected(false);
        jCheckBoxNombre.setSelected(false);
    }//GEN-LAST:event_jCheckBoxCodigo_BarraActionPerformed
    public void buscar(String columna) {
        DefaultTableModel tabla = tabla();
        String[] datos = new String[16];
        try {
            Connection cnn = Conexion.Conexion();

            PreparedStatement pre = cnn.prepareStatement("select p.idProducto,p.codigo,p.codigo_barras,p.producto,p.precio_compra"
                    + ",p.precio_venta,p.cantidad,p.utilidad,p.porcentaje_utilidad,p.tipo,p.seccion"
                    + ",p.marca,p.proveedor,u.nombre,p.fecha_ingreso,p.fecha_vencimiento from producto p left join usuarios u on u.idusuarios = p.idUsuario where p." + columna + " like '%" + jTextFieldBusqueda.getText().trim() + "%'");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < 16; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                tabla.addRow(datos);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable Table;
    private javax.swing.JCheckBox jCheckBoxCodigo;
    private javax.swing.JCheckBox jCheckBoxCodigo_Barra;
    private javax.swing.JCheckBox jCheckBoxNombre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelNP;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldBusqueda;
    private static javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables

}
