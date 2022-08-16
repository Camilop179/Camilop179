/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ventanas;

import Clases.Conexion;
import Clases.Fechas;
import Clases.Fondo;
import Clases.ImagenBoton;
import Clases.Imagenes;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

/**
 *
 * @author harol
 */
public class Vendedor extends javax.swing.JFrame {

    /**
     * Creates new form Vendedor
     */
    public Vendedor() {
        Fondo fondo = new Fondo("Fondo_Dialogos.jpg");
        this.setContentPane(fondo);
        this.setUndecorated(true);
        initComponents();
        new ImagenBoton("cerrar.png", JBotonCerrar, JBotonCerrar.getBounds().width, JBotonCerrar.getBounds().height);
        new ImagenBoton("Minimizar.png", Minimizar, Minimizar.getBounds().width, Minimizar.getBounds().height);
        JBotonCerrar.setContentAreaFilled(false);
        Minimizar.setContentAreaFilled(false);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setTitle("Menu");
        Shape p = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 30, 30);
        this.setShape(p);
        
        new Imagenes("ventas.png", jLabelventas);
        new Imagenes("Inventario.png", jLabelinventario);
        VentaDia();
        VentaSemana();
        VentaMes();
    }
    
    public static void VentaDia() {

        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        Date fecha_actual = Fechas.fechaActualDate();

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha = ?");
            pre.setDate(1, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                do {
                    venta_dia += rs.getDouble(1);

                    jLabelVentasHoy.setText("$" + formatea.format(venta_dia));
                } while (rs.next());
            } else {
                jLabelVentasHoy.setText("$0");
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void VentaMes() {

        ZoneId defaultZoneId = ZoneId.systemDefault();
        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        Date fecha_actual = Fechas.fechaActualDate();
        GregorianCalendar semana = new GregorianCalendar();
        semana.setTime(fecha_actual);
        Month mes = LocalDate.now().getMonth();
        LocalDate fecha_semana = LocalDate.of(LocalDate.now().getYear(), mes, 1);
        java.util.Date fecha_sem = Date.from(fecha_semana.atStartOfDay(defaultZoneId).toInstant());
        java.sql.Date bd_fecha = new java.sql.Date(fecha_sem.getTime());

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha BETWEEN ? and ?");
            pre.setDate(1, bd_fecha);
            pre.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                do {
                    venta_dia += rs.getDouble(1);

                    VentaMEs.setText("$" + formatea.format(venta_dia));
                } while (rs.next());
            } else {
                VentaMEs.setText("$0");
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void VentaSemana() {

        Date fecha_actual = Fechas.fechaActualDate();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha_actual);
        LocalDate fecha_semana;
        int i = gc.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                fecha_semana = LocalDate.now().minusDays(6);
                dia_semana(fecha_actual, fecha_semana);
                break;
            case 2:
                fecha_semana = LocalDate.now().minusDays(0);
                dia_semana(fecha_actual, fecha_semana);
                break;
            case 3:
                fecha_semana = LocalDate.now().minusDays(1);
                dia_semana(fecha_actual, fecha_semana);
            case 4:
                fecha_semana = LocalDate.now().minusDays(2);
                dia_semana(fecha_actual, fecha_semana);
                break;
            case 5:
                fecha_semana = LocalDate.now().minusDays(3);
                dia_semana(fecha_actual, fecha_semana);
                break;
            case 6:
                fecha_semana = LocalDate.now().minusDays(4);
                dia_semana(fecha_actual, fecha_semana);
                break;
            case 7:
                fecha_semana = LocalDate.now().minusDays(5);
                dia_semana(fecha_actual, fecha_semana);
                break;

        }

    }

    public static void dia_semana(Date fecha_actual, LocalDate fecha_semana) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        DecimalFormat formatea = new DecimalFormat("###,###");
        double venta_dia = 0;
        java.util.Date fecha_sem = Date.from(fecha_semana.atStartOfDay(defaultZoneId).toInstant());
        java.sql.Date bd_fecha = new java.sql.Date(fecha_sem.getTime());

        try {
            Connection cnn = Conexion.Conexion();
            PreparedStatement pre = cnn.prepareStatement("select precio_Total from ventas Where fecha BETWEEN ? and ?");
            pre.setDate(2, new java.sql.Date(fecha_actual.getTime()));
            pre.setDate(1, bd_fecha);
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                do {
                    venta_dia += rs.getDouble(1);

                    jLabelVentaSemana.setText("$" + formatea.format(venta_dia));
                } while (rs.next());
            } else {
                jLabelVentaSemana.setText("$0");
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

        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelVentasHoy = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelVentaSemana = new javax.swing.JLabel();
        VentaMEs = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelventas = new javax.swing.JLabel();
        jLabelinventario = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        JBotonCerrar = new javax.swing.JButton();
        Minimizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        jLabel6.setBackground(new java.awt.Color(0, 153, 153));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Ventas");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 0, 153)));

        jLabelVentasHoy.setBackground(new java.awt.Color(255, 255, 255));
        jLabelVentasHoy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVentasHoy.setText("jLabel4");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("                   VENTA HOY:");

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("              VENTA SEMANA:");

        jLabelVentaSemana.setBackground(new java.awt.Color(255, 255, 255));
        jLabelVentaSemana.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVentaSemana.setText("jLabel4");

        VentaMEs.setBackground(new java.awt.Color(255, 255, 255));
        VentaMEs.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        VentaMEs.setText("jLabel4");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("                      VENTA MES:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(VentaMEs))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelVentasHoy, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVentaSemana))))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelVentasHoy))
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelVentaSemana))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(VentaMEs))
                .addGap(20, 20, 20))
        );

        jLabelventas.setBackground(new java.awt.Color(153, 153, 153));
        jLabelventas.setText("jLabel1");
        jLabelventas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelventas.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelventas.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabelinventario.setBackground(new java.awt.Color(153, 153, 153));
        jLabelinventario.setText("jLabel1");
        jLabelinventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelinventario.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelinventario.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setPreferredSize(new java.awt.Dimension(60, 60));

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("Inventario");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        JBotonCerrar.setBorder(null);
        JBotonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonCerrarActionPerformed(evt);
            }
        });

        Minimizar.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        Minimizar.setBorder(null);
        Minimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(JBotonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelinventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBotonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelinventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(74, 74, 74))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        new Ventas().setVisible(true);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        new Catalogo().setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void JBotonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBotonCerrarActionPerformed
        Object[] opc = new Object[]{"SI", "NO"};
        int i = JOptionPane.showOptionDialog(null, "¿Desea salir?", "salir", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
        if (i == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_JBotonCerrarActionPerformed

    private void MinimizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizarActionPerformed
        this.setState(ICONIFIED);
    }//GEN-LAST:event_MinimizarActionPerformed

    int xm;
    int ym;
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        xm = evt.getX();
        ym = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xm, y - ym);
    }//GEN-LAST:event_formMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBotonCerrar;
    private javax.swing.JButton Minimizar;
    private static javax.swing.JLabel VentaMEs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public static javax.swing.JLabel jLabelVentaSemana;
    private static javax.swing.JLabel jLabelVentasHoy;
    private javax.swing.JLabel jLabelinventario;
    private javax.swing.JLabel jLabelventas;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
