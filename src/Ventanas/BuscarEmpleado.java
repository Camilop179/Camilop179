/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Ventanas;

import Clases.Conexion;
import Clases.Fondo;
import Clases.ImagenBoton;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fullmotors
 */
public final class BuscarEmpleado extends javax.swing.JDialog {

    /**
     * Creates new form NewJDialog
     */
    public BuscarEmpleado(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        Fondo fondo = new Fondo("Dialogos.jpg");
        this.setContentPane(fondo);
        this.setUndecorated(true);
        initComponents();
        this.setLocationRelativeTo(null);
        Shape p = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 30, 30);
        this.setShape(p);
        new ImagenBoton("cerrar.png", JBotonCerrar);
        JBotonCerrar.setContentAreaFilled(false);
        personal();
    }

    public void personal() {
        DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
        try (Connection cn = Conexion.Conexion()) {
            Object[] obg = new Object[3];
            PreparedStatement ps = cn.prepareStatement("select cedula,nombre,telefono from empleados");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < 3; i++) {
                    obg[i] = rs.getString(i + 1);
                }
                df.addRow(obg);
            }
            cn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        JBotonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setText("CC");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setBackground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Nombre");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CC", "Nombre", "Telefono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        JBotonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButton1))
                .addContainerGap(87, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JBotonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JBotonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    int xy;
    int ym;
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        xy = evt.getX();
        ym = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xy, y - ym); // TODO add your handling code here:
    }//GEN-LAST:event_formMouseDragged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void JBotonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBotonCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_JBotonCerrarActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        String buscar = jTextField1.getText().trim();
        if (jRadioButton1.isSelected() && jRadioButton2.isSelected()) {
            buscar("select cedula,nombre,telefonp from empleado where nombre like ? or cedula like ?", buscar, 2);
        } else if (jRadioButton1.isSelected()) {
            buscar("select cedula,nombre,telefono from empleados where cedula like ?", buscar, 1);
        } else if (jRadioButton2.isSelected()) {
            buscar("select cedula,nombre,telefono from emplados where nombre like ?", buscar, 1);
        }

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int i = jTable1.getSelectedRow();
        String cedula = jTable1.getValueAt(i, 0).toString();
        Nomina.jTextFieldCedula.setText(cedula);
        dispose();
    }//GEN-LAST:event_jTable1MouseClicked

    public void buscar(String sql, String buscar, int parametros) {
        DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
        limpiar();
        try (Connection cn = Conexion.Conexion()) {
            Object[] obg = new Object[3];
            PreparedStatement ps = cn.prepareStatement(sql);
            for (int i = 0; i < parametros; i++) {
                ps.setString(i + 1, "%" + buscar + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < 3; i++) {
                    obg[i] = rs.getString(i + 1);
                }
                df.addRow(obg);
            }
            cn.close();
            jTable1.setModel(df);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void limpiar() {
        DefaultTableModel tabla = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            tabla.removeRow(i);
            i--;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBotonCerrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
