/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Ventanas;

import Clases.Fondo;
import Clases.Validaciones;
import Clases.uiJtextField;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.RoundRectangle2D;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class Abono extends javax.swing.JDialog {

    /**
     * Creates new form Abono
     */
    public Abono(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        Fondo fondo = new Fondo("");
        this.setContentPane(fondo);
        this.setUndecorated(true);
        initComponents();
        
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
        jTextField1 = new uiJtextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField(){
            private int arcw = 20;
            private int arch = 20;

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(getForeground());
                g.drawRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintBorder(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g;
                Paint oldPaint = g2.getPaint();

                RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight()+1, arcw, arch);
                g2.clip(r2d);

                g2.setPaint(new GradientPaint(0.0f, 0.0f, getBackground(),
                    0.0f, getHeight(), getBackground()));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setPaint(new GradientPaint(0.0f, 0.0f, Color.BLACK,
                0.0f, getHeight(), Color.BLACK));
        g2.drawRoundRect(0, 0, getWidth(), getHeight(), arcw, arch);

        g2.setPaint(oldPaint);
        super.paintComponent(g);
    }
    };

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setBackground(new java.awt.Color(5, 0, 80));

    jLabel1.setText("Concepto");

    jLabel2.setText("Valor");

    jButton1.setText("Agregar");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¤#,##0"))));
    jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            jFormattedTextField1KeyReleased(evt);
        }
        public void keyTyped(java.awt.event.KeyEvent evt) {
            jFormattedTextField1KeyTyped(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(43, 43, 43)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel2)
                .addComponent(jLabel1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField1)))
            .addContainerGap(48, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap(103, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addComponent(jButton1)
            .addGap(17, 17, 17))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String concepto = jTextField1.getText();
        Double valor = Double.parseDouble(jFormattedTextField1.getValue().toString());
        DefaultTableModel dtm = (DefaultTableModel) Comprobante.jTable1.getModel();
        Object[] obg = new Object[2];
        obg[0]=concepto;
        obg[1]=valor;
        dtm.addRow(obg);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        if(!Validaciones.validarString(evt)&&!"".equals(jFormattedTextField1.getText())){
            double value = Double.parseDouble(jFormattedTextField1.getText().replaceAll("[\\D]", ""));
            jFormattedTextField1.setValue(value);
            jFormattedTextField1.setCaretPosition(jFormattedTextField1.getText().length());
        }
    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyTyped
        if (Validaciones.validarString(evt)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField1KeyTyped

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
