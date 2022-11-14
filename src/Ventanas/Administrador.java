package Ventanas;

import Clases.Fondo;
import Clases.Imagenes;
import Clases.Conexion;
import Clases.Fechas;
import Clases.FormatoPesos;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Clases.ImagenBoton;
import Clases.TotalVentas;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harol
 */
public final class Administrador extends javax.swing.JFrame {

    public static boolean m;

    public Administrador() {
        Fondo fondo = new Fondo("FondoMenu.jpg");
        this.setContentPane(fondo);
        this.setUndecorated(true);
        initComponents();
        m = true;
        new ImagenBoton("cerrar.png", JBotonCerrar, JBotonCerrar.getBounds().width, JBotonCerrar.getBounds().height);
        new ImagenBoton("Minimizar.png", Minimizar, Minimizar.getBounds().width, Minimizar.getBounds().height);
        JBotonCerrar.setContentAreaFilled(false);
        Minimizar.setContentAreaFilled(false);
        setLocationRelativeTo(null);
        setTitle("Menu");
        Shape p = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 30, 30);
        this.setShape(p);

        new Imagenes("Cerrar_Sesion.png", jLabelCerrarSesion);
        new Imagenes("ventas.png", jLabelventas);
        new Imagenes("Factura.png", jLabelfacturas);
        new Imagenes("Inventario.png", jLabelinventario);
        new Imagenes("Reportes.png", jLabelreportes);
        new Imagenes("Administrador.png", jLabelAdministrador);
        new Imagenes("icons8_agregar_usuario.png", jLabelAgregarUsuario);

        invisible();
        cerra();
        ventas();
        caja();
        ptm();
    }

    public void caja() {
        try ( Connection cn = Conexion.Conexion()) {

            PreparedStatement pr1 = cn.prepareStatement("select max(id) from caja");
            ResultSet rs1 = pr1.executeQuery();
            while (rs1.next()) {
                PreparedStatement ps = cn.prepareStatement("select total from caja where fecha =? and id =?");
                ps.setDate(1, new java.sql.Date(Fechas.fechaActualDate().getTime()));
                ps.setInt(2, rs1.getInt(1));
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    if (rs.getInt(1) != 0) {
                        jLabelCaja.setText(FormatoPesos.formato(rs.getDouble(1)));
                    } else {
                        new Caja_Dia(this, true).setVisible(true);
                    }
                }
            }

            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ptm() {
        try ( Connection cn = Conexion.Conexion()) {
            PreparedStatement pr1 = cn.prepareStatement("select max(id) from ptm");
            ResultSet rs1 = pr1.executeQuery();
            while (rs1.next()) {
                PreparedStatement ps = cn.prepareStatement("select total from ptm where fecha =? and id =?");
                ps.setDate(1, new java.sql.Date(Fechas.fechaActualDate().getTime()));
                ps.setInt(2, rs1.getInt(1));
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    jLabelPtm.setText(FormatoPesos.formato(rs.getDouble(1)));
                }
            }

            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ventas() {
        jLabelVentasHoy.setText(TotalVentas.VentaDia());
        jLabelVentaSemana.setText(TotalVentas.VentaSemana());
        VentaMEs.setText(TotalVentas.VentaMes());
    }

    public void cerra() {

        try {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Object[] opc = new Object[]{"SI", "NO"};
                    int i = JOptionPane.showOptionDialog(null, "¿Desea salir?", "salir", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
                    System.out.println(i);
                    if (i == 0) {
                        setDefaultCloseOperation(EXIT_ON_CLOSE);
                    } else {
                        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void visible() {
        jLabelbarra.setVisible(true);
        jLabelCerrarSesion.setVisible(true);
        jLabelAgregarUsuario.setVisible(true);
    }

    /**
     *
     */
    public void invisible() {
        jLabelbarra.setVisible(false);
        jLabelCerrarSesion.setVisible(false);
        jLabelAgregarUsuario.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelinventario = new javax.swing.JLabel();
        jLabelreportes = new javax.swing.JLabel();
        jLabelfacturas = new javax.swing.JLabel();
        jLabelAgregarUsuario = new javax.swing.JLabel();
        jLabelCerrarSesion = new javax.swing.JLabel();
        jLabelbarra = new javax.swing.JLabel();
        jLabelAdministrador = new javax.swing.JLabel();
        jLabelventas = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelVentasHoy = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelVentaSemana = new javax.swing.JLabel();
        VentaMEs = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JBotonCerrar = new javax.swing.JButton();
        Minimizar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabelCaja = new javax.swing.JLabel();
        jLabelPtm = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldPtm = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Administrador");
        setResizable(false);
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelinventario.setBackground(new java.awt.Color(153, 153, 153));
        jLabelinventario.setText("jLabel1");
        jLabelinventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelinventario.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelinventario.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelinventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 176, -1, -1));

        jLabelreportes.setBackground(new java.awt.Color(153, 153, 153));
        jLabelreportes.setText("jLabel1");
        jLabelreportes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelreportes.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelreportes.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelreportes.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelreportes.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelreportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 386, -1, -1));

        jLabelfacturas.setBackground(new java.awt.Color(153, 153, 153));
        jLabelfacturas.setText("jLabel1");
        jLabelfacturas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelfacturas.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelfacturas.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelfacturas.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelfacturas.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelfacturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 286, -1, -1));

        jLabelAgregarUsuario.setText("Agregar usuario");
        jLabelAgregarUsuario.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelAgregarUsuarioMouseMoved(evt);
            }
        });
        jLabelAgregarUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelAgregarUsuarioMouseClicked(evt);
            }
        });
        getContentPane().add(jLabelAgregarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(839, 388, 30, 30));

        jLabelCerrarSesion.setText("cerrar ");
        jLabelCerrarSesion.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelCerrarSesionMouseMoved(evt);
            }
        });
        jLabelCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelCerrarSesionMouseClicked(evt);
            }
        });
        getContentPane().add(jLabelCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(839, 348, 30, 30));

        jLabelbarra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelbarraMouseMoved(evt);
            }
        });
        jLabelbarra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelbarraMouseExited(evt);
            }
        });
        getContentPane().add(jLabelbarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(796, 308, 110, 220));

        jLabelAdministrador.setText("          0");
        jLabelAdministrador.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelAdministradorMouseMoved(evt);
            }
        });
        getContentPane().add(jLabelAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(819, 448, 70, 60));

        jLabelventas.setBackground(new java.awt.Color(153, 153, 153));
        jLabelventas.setText("jLabel1");
        jLabelventas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelventas.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelventas.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 76, -1, -1));

        jLabel1.setBackground(new java.awt.Color(204, 0, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventario");
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 186, 90, -1));

        jLabel2.setBackground(new java.awt.Color(204, 0, 51));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Compras");
        jLabel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jLabel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel2KeyPressed(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 296, 80, -1));

        jLabel3.setBackground(new java.awt.Color(204, 0, 51));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Reportes");
        jLabel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 396, 80, -1));

        jLabel6.setBackground(new java.awt.Color(204, 0, 51));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Ventas");
        jLabel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 86, 70, -1));

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
                .addContainerGap(230, Short.MAX_VALUE))
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

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 123, -1, -1));

        JBotonCerrar.setBorder(null);
        JBotonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(JBotonCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 19, 25, 25));

        Minimizar.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        Minimizar.setBorder(null);
        Minimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizarActionPerformed(evt);
            }
        });
        getContentPane().add(Minimizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 19, 25, 25));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CAJA");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 390, -1, -1));

        jLabelCaja.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCaja.setForeground(new java.awt.Color(204, 204, 204));
        jLabelCaja.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCaja.setText("jLabel5");
        jLabelCaja.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabelCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, 240, 50));

        jLabelPtm.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPtm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPtm.setText("jLabel5");
        getContentPane().add(jLabelPtm, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 480, 190, 30));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("PTM");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 460, -1, -1));
        getContentPane().add(jTextFieldPtm, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 190, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Retirar", "Consignar" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 390, -1, -1));

        jButton2.setText("jButton1");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 430, 30, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked

        if (!Ventas.m) {
            new Ventas().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "La ventana ya esta Abierta");
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        new Catalogo().setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2KeyPressed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        new Compras().setVisible(true);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        if (Reportes.m == 0) {
            new Reportes().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "la ventana ya esta abierta");
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabelAdministradorMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAdministradorMouseMoved
        visible();
    }//GEN-LAST:event_jLabelAdministradorMouseMoved

    private void jLabelCerrarSesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelCerrarSesionMouseClicked
        int i = JOptionPane.showConfirmDialog(null, "¿Esta seguro de Salir?", "Cerrar Sesion", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            m = false;
            this.dispose();
            new Login().setVisible(true);
        }
    }//GEN-LAST:event_jLabelCerrarSesionMouseClicked

    private void jLabelbarraMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelbarraMouseMoved
        visible();
    }//GEN-LAST:event_jLabelbarraMouseMoved

    private void jLabelbarraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelbarraMouseExited
        invisible();
    }//GEN-LAST:event_jLabelbarraMouseExited

    private void jLabelCerrarSesionMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelCerrarSesionMouseMoved
        visible();
    }//GEN-LAST:event_jLabelCerrarSesionMouseMoved

    private void jLabelAgregarUsuarioMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAgregarUsuarioMouseMoved
        visible();
    }//GEN-LAST:event_jLabelAgregarUsuarioMouseMoved

    private void jLabelAgregarUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAgregarUsuarioMouseClicked
        if (Usuario.g == 0) {
            new Usuario().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "la ventana ya esta abierta");
        }
    }//GEN-LAST:event_jLabelAgregarUsuarioMouseClicked

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

    private void JBotonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBotonCerrarActionPerformed
        Object[] opc = new Object[]{"SI", "NO"};
        int i = JOptionPane.showOptionDialog(null, "¿Desea salir?", "salir", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
        if (i == 0) {
            m = false;
            System.exit(0);
        }
    }//GEN-LAST:event_JBotonCerrarActionPerformed

    private void MinimizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimizarActionPerformed
        this.setState(ICONIFIED);
    }//GEN-LAST:event_MinimizarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        double valor = Double.parseDouble(jTextFieldPtm.getText().trim().replace(",", ""));
        if (jComboBox1.getSelectedItem() == "Retirar") {
            CambioCaja(-valor, "Retiro Ptm");
            Cambioptm(valor, "Retiro Ptm");
            jTextFieldPtm.setText("0");
        } else {
            CambioCaja(valor, "Recarga Ptm");
            Cambioptm(-valor, "Recatga Ptm");
            jTextFieldPtm.setText("0");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    void Cambioptm(double valor, String concepto) {
        try ( Connection cn = Conexion.Conexion()) {
            double total = Double.parseDouble(jLabelPtm.getText().trim().replace(",", "")) + (valor);
            PreparedStatement ps = cn.prepareStatement("insert into ptm (id,Concepto,Valor,Total,Hora,Fecha) values(?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setString(2, concepto);
            ps.setDouble(3, valor);
            ps.setDouble(4, total);
            ps.setTime(5, new Time(Fechas.fechaActualDate().getTime()));
            ps.setDate(6, new Date(Fechas.fechaActualDate().getTime()));
            ps.execute();
            jLabelPtm.setText(String.valueOf(total));
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    void CambioCaja(double valor, String concepto) {
        try ( Connection cn = Conexion.Conexion()) {
            double total = Double.parseDouble(jLabelCaja.getText().trim().replace(",", "")) + (valor);
            PreparedStatement ps = cn.prepareStatement("insert into caja (id,Concepto,Valor,Total,Hora,Fecha) values(?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setString(2, concepto);
            ps.setDouble(3, valor);
            ps.setDouble(4, total);
            ps.setTime(5, new Time(Fechas.fechaActualDate().getTime()));
            ps.setDate(6, new Date(Fechas.fechaActualDate().getTime()));
            ps.execute();
            jLabelCaja.setText(String.valueOf(total));
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBotonCerrar;
    private javax.swing.JButton Minimizar;
    private static javax.swing.JLabel VentaMEs;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelAdministrador;
    private javax.swing.JLabel jLabelAgregarUsuario;
    protected static javax.swing.JLabel jLabelCaja;
    private javax.swing.JLabel jLabelCerrarSesion;
    private javax.swing.JLabel jLabelPtm;
    public static javax.swing.JLabel jLabelVentaSemana;
    private static javax.swing.JLabel jLabelVentasHoy;
    private javax.swing.JLabel jLabelbarra;
    private javax.swing.JLabel jLabelfacturas;
    private javax.swing.JLabel jLabelinventario;
    private javax.swing.JLabel jLabelreportes;
    private javax.swing.JLabel jLabelventas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldPtm;
    // End of variables declaration//GEN-END:variables
}
