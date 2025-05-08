package Ventanas;

import Clases.Conexion;
import Clases.Fechas;
import Clases.Fondo;
import Clases.Imagenes;
import Clases.Validaciones;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author harol
 */
public final class Servicio extends javax.swing.JFrame {

    public static int idp;

    public Servicio() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();

        setLocationRelativeTo(null);
        setResizable(false);
        new Imagenes("disco-flexible.png", jLabelListo, 37, 37);
        jLabelFecha.setText(Fechas.fechaActual());

        this.repaint();
        cerra();
    }

    public static void modificar(int id) {
        try {
            Connection cn;
            cn = Conexion.Conexion();
            PreparedStatement pr;
            pr = cn.prepareStatement("select * from servicio where id = ?");
            pr.setInt(1, id);
            ResultSet rs;
            rs = pr.executeQuery();
            while (rs.next()) {
                jTextFieldCodigo.setText(rs.getString(2));
                jTextFieldProducto.setText(rs.getString(3));
                jTextFieldPrecio_C.setText(rs.getString(4));
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void actualizar(int id) {
        String codigo = jTextFieldCodigo.getText();
        String product = jTextFieldProducto.getText();
        Double precio_C = Double.parseDouble(jTextFieldPrecio_C.getText());
        java.sql.Date fecha_i_bd = new java.sql.Date(Fechas.fechaActualDate().getTime());
        if (!("".equals(codigo + product) && precio_C == 0)) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pre = cn.prepareStatement("Update servicio set codigo = ?, Concepto=?, Valor=?,idUsuario=?, FechaModificar=? where id=?");
                pre.setString(1, codigo);
                pre.setString(2, product);
                pre.setDouble(3, precio_C);
                pre.setInt(4, Login.idUsuario);
                pre.setDate(5, fecha_i_bd);
                pre.setInt(6, id);

                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Actualizacion exitoso");
                jTextFieldCodigo.setText("");
                jTextFieldPrecio_C.setText("");
                jTextFieldProducto.setText("");
                jTextFieldProducto.requestFocus();
                Catalogo.inventario();
                Catalogo.total();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al ingresar el producto " + e);
                JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo.");
            }
        }
    }

    public void agregar() {
        String codigo = jTextFieldCodigo.getText();

        String product = jTextFieldProducto.getText();

        Double precio_C = Double.parseDouble(jTextFieldPrecio_C.getText());

        java.sql.Date fecha_i_bd = new java.sql.Date(Fechas.fechaActualDate().getTime());

        if (!("".equals(codigo + product) && precio_C == 0)) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pre = cn.prepareStatement("INSERT INTO servicio (id,codigo,Concepto,Valor,idUsuario,FechaModificar) values(?,?,?,?,?,?)");
                pre.setInt(1, 0);
                pre.setString(2, codigo);
                pre.setString(3, product);
                pre.setDouble(4, precio_C);
                pre.setInt(5, Login.idUsuario);
                pre.setDate(6, fecha_i_bd);

                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registro exitoso");
                jTextFieldCodigo.setText("");
                jTextFieldPrecio_C.setText("0");
                jTextFieldProducto.setText("");
                jTextFieldProducto.requestFocus();
                Servicios.buscar();
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
                }
            });
        } catch (Exception e) {
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

        jLabel1 = new javax.swing.JLabel();
        jTextFieldProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPrecio_C = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabelListo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Codigo:");

        jTextFieldProducto.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldProducto.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Producto:");

        jTextFieldCodigo.setBackground(new java.awt.Color(0, 153, 153));
        jTextFieldCodigo.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTextFieldCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoActionPerformed(evt);
            }
        });

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

        jLabelListo.setText("jLabel12");
        jLabelListo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelListoMouseClicked(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Guardar");

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFecha.setText("jLabel13");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPrecio_C, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelListo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelFecha)
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldPrecio_C, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(100, 100, 100)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelListo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCodigoActionPerformed

    private void jLabelListoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelListoMouseClicked

        if (idp == 0) {
            agregar();
        }else{
            actualizar(idp);
        }

    }//GEN-LAST:event_jLabelListoMouseClicked

    private void jTextFieldPrecio_CKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_CKeyReleased

    }//GEN-LAST:event_jTextFieldPrecio_CKeyReleased

    private void jTextFieldPrecio_CKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecio_CKeyTyped
        if (Validaciones.validarString(evt)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPrecio_CKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFecha;
    private static javax.swing.JLabel jLabelListo;
    private static javax.swing.JTextField jTextFieldCodigo;
    private static javax.swing.JTextField jTextFieldPrecio_C;
    private static javax.swing.JTextField jTextFieldProducto;
    // End of variables declaration//GEN-END:variables
}
