/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ventanas;

import Clases.Hash;
import Clases.Conexion;
import Clases.Fondo;
import Clases.uiJTabben;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author harol
 */
public class Usuario extends javax.swing.JFrame {
    int i;
    static int g;

    public Usuario() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        initComponents();

        setLocationRelativeTo(null);
        g = 1;
        usuariotabla();
        cerra();
        jTabbedPane1.setUI(new uiJTabben());
        jButton2.setVisible(false);
        jButton3.setVisible(false);
//        jPanel1.setUI(new BasicPanelUI() {
//            @Override
//            public void paint(Graphics g, JComponent c) {
//                g.setColor(null);
//                g.fillRect(c.getX(), c.getY(), c.getWidth(), c.getHeight());
//                super.paint(g, c); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
//
//            }
//
//        });

    }

    private void cerra() {
        try {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    g = 0;
                }
            });
        } catch (Exception e) {
        }
    }

    public static void usuariotabla() {
        String[] datos = new String[8];
        DefaultTableModel tabla = new DefaultTableModel();

        tabla.addColumn("Id");
        tabla.addColumn("Usuario");
        tabla.addColumn("Contraseña");
        tabla.addColumn("Nombre");
        tabla.addColumn("Cargo");
        tabla.addColumn("Cedula");
        tabla.addColumn("Celular");
        tabla.addColumn("Correo");

        jTableUsuarios.setModel(tabla);
        TableColumnModel columnModel = jTableUsuarios.getColumnModel();
        columnModel.getColumn(0).setResizable(false);
        columnModel.getColumn(1).setResizable(false);
        columnModel.getColumn(2).setResizable(false);
        columnModel.getColumn(3).setResizable(false);
        columnModel.getColumn(4).setResizable(false);
        columnModel.getColumn(5).setResizable(false);
        columnModel.getColumn(6).setResizable(false);

        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(150);
        try {
            Connection cnn = Conexion.Conexion();

            PreparedStatement pre = cnn.prepareStatement("select * from usuarios");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < 8; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                tabla.addRow(datos);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public boolean confirmarcontraseña(String contraseña) {

        boolean b = false;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pre = cn.prepareStatement("select contraseña from usuarios where contraseña = ?");
            pre.setString(1, contraseña);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                b = true;
            }
        } catch (SQLException e) {
        }
        return b;
    }

    public void modificar() {
        if (validacionUsuario() || jTableUsuarios.getValueAt(i, 1).toString().equals(jTextFieldUsuario.getText())) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pre = cn.prepareStatement("update usuarios set usuario = ?,contraseña=?,nombre =?,cargo = ?,cedula = ?,celular =?,correo =? where idusuarios = ?");
                pre.setString(1, jTextFieldUsuario.getText());
                if (!confirmarcontraseña(jTextFieldContraseña.getText())) {
                    pre.setString(2, Hash.hash24(jTextFieldContraseña.getText()));
                } else {
                    pre.setString(2, jTextFieldContraseña.getText());
                }
                pre.setString(3, jTextFieldNombre.getText());
                pre.setString(4, jComboBoxCargo.getSelectedItem().toString());
                pre.setString(5, jTextFieldCedula.getText());
                pre.setString(6, jTextFieldCelular.getText());
                pre.setString(7, jTextFieldCorrea.getText());
                pre.setInt(8, Integer.parseInt(jTableUsuarios.getValueAt(i, 0).toString()));
                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Actualizacion exitosa");

            } catch (HeadlessException | SQLException e) {
                System.err.println("Error al ingresar el producto " + e);
                JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "el usuario ya existe, por favor digite oto usuario difrente");
        }
    }

    public void agregar() {
        if (validacionUsuario() && jTextFieldContraseña.getText().length() < 13 && jTextFieldContraseña.getText().length() > 7) {
            try {
                Connection cn = Conexion.Conexion();
                PreparedStatement pre = cn.prepareStatement("INSERT INTO usuarios (idusuarios,usuario,contraseña,nombre,cargo,"
                        + "cedula,celular,correo) values(?,?,?,?,?,?,?,?)");

                pre.setInt(1, 0);
                pre.setString(2, jTextFieldUsuario.getText());
                pre.setString(3, Hash.hash24(jTextFieldContraseña.getText()));
                pre.setString(4, jTextFieldNombre.getText());
                pre.setString(5, jComboBoxCargo.getSelectedItem().toString());
                pre.setString(6, jTextFieldCedula.getText());
                pre.setString(7, jTextFieldCelular.getText());
                pre.setString(8, jTextFieldCorrea.getText());

                pre.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registro exitoso");
            } catch (HeadlessException | SQLException e) {
                System.err.println("Error al ingresar el producto " + e);
                JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo.");
            }
        } else {
            if (!validacionUsuario()) {
                JOptionPane.showMessageDialog(null, "el usuario ya existe, por favor digite oto usuario difrente");
            } else {
                JOptionPane.showMessageDialog(null, "la contraseña debe contener mas de 8 y menos de 12 caracteres");
            }
        }
    }

    public void eliminar() {
        int i = jTableUsuarios.getSelectedRow();
        int id = Integer.parseInt(jTableUsuarios.getValueAt(i, 0).toString());
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("delete from usuarios where idusuarios = " + id);
            pr.executeUpdate();
            usuariotabla();
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    public boolean validacionUsuario() {

        boolean b = true;
        try {
            Connection cn = Conexion.Conexion();
            PreparedStatement pr = cn.prepareStatement("select usuario from usuarios");
            ResultSet rs = pr.executeQuery();
            String user;
            while (rs.next()) {
                user = rs.getString(1);
                if (user.equals(jTextFieldUsuario.getText())) {
                    b = false;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al ingresar el producto " + e);
            JOptionPane.showMessageDialog(null, "¡Error al ingresar el producto!. Contacte al soporte Corporacion Portillo.");
        }

        return b;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new Fondo("Fondo.jpg");
        jLabel8 = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextFieldCorrea = new javax.swing.JTextField();
        jTextFieldCelular = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBoxCargo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldCedula = new javax.swing.JTextField();
        jTextFieldContraseña = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableUsuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAutoRequestFocus(false);
        setResizable(false);

        jTabbedPane1.setBackground(null);
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setOpaque(true);

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cargo:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Celular:");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Creado por Corporacion Portillo ADMP ®©™ 2022 V1.0");
        jLabel9.setAlignmentX(253);
        jLabel9.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Cedula:");

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Usuario:");

        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBoxCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Administrador", "Vendedor" }));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Correo:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Nombre:");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Exclusivo para FullMotors");
        jLabel10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 255, 255)));

        jTextFieldCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCedulaActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Contraseña:");

        jTableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableUsuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableUsuarios);
        if (jTableUsuarios.getColumnModel().getColumnCount() > 0) {
            jTableUsuarios.getColumnModel().getColumn(0).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(1).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(2).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(3).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(4).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(5).setResizable(false);
            jTableUsuarios.getColumnModel().getColumn(7).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(69, 69, 69)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldCedula)
                            .addComponent(jTextFieldUsuario)
                            .addComponent(jTextFieldContraseña)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCorrea)
                            .addComponent(jTextFieldCelular)
                            .addComponent(jComboBoxCargo, 0, 266, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(195, 195, 195))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldCorrea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Usuarios", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCedulaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String usuario = jTextFieldUsuario.getText();
        String contraseña = jTextFieldContraseña.getText();
        String nombre = jTextFieldNombre.getText();
        String cedula = jTextFieldCedula.getText();
        String celular = jTextFieldCelular.getText();
        String correo = jTextFieldCorrea.getText();
        int i = jComboBoxCargo.getSelectedIndex();
        System.out.println("" + i);

        if (!(usuario.equals("") && contraseña.equals("") && nombre.equals("") && cedula.equals("") && celular.equals("") && correo.equals("")) && jComboBoxCargo.getSelectedIndex() != 0) {
            agregar();
            usuariotabla();
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese los datos");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        eliminar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUsuariosMouseClicked
        i = jTableUsuarios.getSelectedRow();
        jButton2.setVisible(true);
        jButton3.setVisible(true);
        String usuario = jTableUsuarios.getValueAt(i, 1).toString();
        String contraseña = jTableUsuarios.getValueAt(i, 2).toString();
        String nombre = jTableUsuarios.getValueAt(i, 3).toString();
        String cargo = jTableUsuarios.getValueAt(i, 4).toString();
        String cedula = jTableUsuarios.getValueAt(i, 5).toString();
        String celular = jTableUsuarios.getValueAt(i, 6).toString();
        String correo = jTableUsuarios.getValueAt(i, 7).toString();

        jTextFieldCedula.setText(cedula);
        jTextFieldCelular.setText(celular);
        jTextFieldContraseña.setText(contraseña);
        jTextFieldCorrea.setText(correo);
        jTextFieldNombre.setText(nombre);
        jTextFieldUsuario.setText(usuario);
        jComboBoxCargo.setSelectedItem(cargo);
    }//GEN-LAST:event_jTableUsuariosMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if ((jTextFieldContraseña.getText().length() > 7 && jTextFieldContraseña.getText().length() < 13) || confirmarcontraseña(jTextFieldContraseña.getText())) {
            modificar();
            usuariotabla();
        } else {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener entre 8 a 12 caracteres!!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBoxCargo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private static javax.swing.JTable jTableUsuarios;
    private javax.swing.JTextField jTextFieldCedula;
    private javax.swing.JTextField jTextFieldCelular;
    private javax.swing.JTextField jTextFieldContraseña;
    private javax.swing.JTextField jTextFieldCorrea;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
