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
import Clases.Validaciones;
import Clases.Utilidad;
import Clases.comprobarCant;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author harol
 */
public final class Administrador extends javax.swing.JFrame {

    public static boolean m;
    static String porString;
    public static productoNegativo pn;

    public static comprobarCant compCant = new comprobarCant();

    public Administrador() {
        Fondo fondo = new Fondo("a.jpg");
        this.setContentPane(fondo);
        this.setUndecorated(true);
        initComponents();
        m = true;

        new ImagenBoton("cerrar.png", JBotonCerrar);
        new ImagenBoton("Minimizar.png", Minimizar);
        new Imagenes("Cerrar_Sesion.png", jLabelCerrarSesion);
        new Imagenes("CAJA.png", jLabel11, 40, jLabel11.getHeight());
        new Imagenes("Administrador.png", jLabelAdministrador);
        new Imagenes("icons8_agregar_usuario.png", jLabelAgregarUsuario);

        JBotonCerrar.setContentAreaFilled(false);
        Minimizar.setContentAreaFilled(false);
        setLocationRelativeTo(null);
        setTitle("Menu");
        this.setSize(910, 580);
        Shape p = new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 30, 30);
        this.setShape(p);
        jLabel16.setText("<html>Bienvenido <br>" + Login.nombre);

        new Imagenes("ventas_1.png", jLabelventas);
        new Imagenes("ABONOS.png", jLabelComprobante);
        new Imagenes("Factura.png", jLabelfacturas);
        new Imagenes("INVENTARIO_1.png", jLabelinventario);
        new Imagenes("REPORTES_1.png", jLabelreportes);
        new Imagenes("NOMINA.png", jLabelNomina);
        new Imagenes("mecanico.png", jLabelTrabajo);

        jProgressBar1.setStringPainted(true);

        cerra();
        caja();
        ptm();
        progesoUtilidad();
        invisible();
        repaint();
    }

    public static void uiProgresos(String s) {

        jProgressBar1.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int ancho = c.getWidth();
                int alto = c.getHeight();
                double por = jProgressBar1.getPercentComplete();
                if (por < 1) {
                    g2.setColor(Color.white);
                    RoundRectangle2D r3 = new RoundRectangle2D.Double(4, 2, c.getWidth() - 5, alto - 7, alto, alto);
                    g2.fill(r3);

                    ancho = (int) (ancho * por);
                    g2.setColor(Color.RED);
                } else {
                    por = por - 1;
                    g2.setColor(Color.RED);
                    RoundRectangle2D r3 = new RoundRectangle2D.Double(4, 2, c.getWidth() - 5, alto - 7, alto, alto);
                    g2.fill(r3);

                    ancho = (int) (ancho * por);
                    g2.setColor(Color.GREEN);
                }
                RoundRectangle2D R = new RoundRectangle2D.Double(4, 2, ancho - 6, alto - 7, alto, alto);
                g2.fill(R);

                g2.setColor(new Color(51, 51, 51));
                g2.setFont(new Font("Purisa", Font.PLAIN, 18));
                g2.drawString(s, 10, 20);
                g2.setColor(Color.BLACK);
                RoundRectangle2D r2 = new RoundRectangle2D.Double(2, 2, ancho - 5, alto - 7, alto, alto);
                Stroke grosor = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
                g2.setStroke(grosor);
                g2.draw(r2);
            }

        });
        jProgressBar1.setBackground(new Color(0, 0, 0, 0));
        jProgressBar1.setOpaque(true);
    }

    public void progesoUtilidad() {
        uiProgresos(porString);
        try (Connection cn = Conexion.Conexion()) {
            int utilidad = 0;
            LocalDate fecha = LocalDate.now();
            int mesNum = fecha.getMonthValue();
            int añoNum;
            if (mesNum == 1) {
                mesNum = 12;
                añoNum = fecha.getYear() - 1;
            } else {
                mesNum--;
                añoNum = fecha.getYear();
            }
            Calendar dia = Calendar.getInstance();
            dia.set(Calendar.MONTH, dia.get(Calendar.MONTH) - 1);
            LocalDate fecha1 = LocalDate.of(añoNum, mesNum, 1);
            LocalDate fecha2 = LocalDate.now();
            fecha2 = fecha2.minusMonths(1);
            fecha2 = fecha2.with(TemporalAdjusters.lastDayOfMonth());
            PreparedStatement pr1 = cn.prepareStatement("select utilidad from ventas where fecha between ? and ?");
            pr1.setDate(1, new java.sql.Date(java.util.Date.from(fecha1.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
            pr1.setDate(2, new java.sql.Date(java.util.Date.from(fecha2.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
            ResultSet rs1 = pr1.executeQuery();
            while (rs1.next()) {
                utilidad += rs1.getDouble(1);
            }
            if (utilidad != 0) {
                jProgressBar1.setMaximum(utilidad);
            } else {
                jProgressBar1.setMaximum(1);
            }
            jProgressBar1.setValue(Utilidad.utilidadMes());
            utilidadPor();
            cn.close();
        } catch (SQLException ex) {
            System.err.println("Error mes" + ex);
        }
    }

    public static void utilidadPor() {
        double porsentaje;
        porsentaje = (Double.valueOf(Utilidad.utilidadMes()) / Double.valueOf(jProgressBar1.getMaximum())) * 100;

        porString = "%" + new DecimalFormat("###,###.##").format(porsentaje);
        uiProgresos(porString);
    }

    public void caja() {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement ps = cn.prepareStatement("select total from caja where fecha = current_date() and id =(Select max(id) from caja)");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getDouble(1) < 0) {
                    jLabelCaja.setText(FormatoPesos.formato(rs.getDouble(1)));
                } else {
                    jLabelCaja.setText(" "+FormatoPesos.formato(rs.getDouble(1)));
                }

            } else {
                new Caja_Dia(this, true).setVisible(true);
                caja();
            }
            cn.close();
        } catch (SQLException ex) {
            System.out.println(" ");
        }
    }

    public void ptm() {
        try (Connection cn = Conexion.Conexion()) {
            PreparedStatement pr1 = cn.prepareStatement("select max(id) from ptm");
            ResultSet rs1 = pr1.executeQuery();
            while (rs1.next()) {
                PreparedStatement ps = cn.prepareStatement("select total from ptm where fecha =? and id =?");
                ps.setDate(1, new java.sql.Date(Fechas.fechaActualDate().getTime()));
                ps.setInt(2, rs1.getInt(1));
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                   if (rs.getDouble(1) < 0) {
                    jLabelPtm.setText(FormatoPesos.formato(rs.getDouble(1)));
                } else {
                    jLabelPtm.setText(" "+FormatoPesos.formato(rs.getDouble(1)));
                }
                }
            }
            cn.close();

        } catch (SQLException ex) {
            Logger.getLogger(Administrador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
        JBotonCerrar = new javax.swing.JButton();
        Minimizar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelCaja = new javax.swing.JLabel();
        jLabelPtm = new javax.swing.JLabel();
        jTextFieldPtm = new Clases.uiJtextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabelNomina = new javax.swing.JLabel();
        jLabelComprobante = new javax.swing.JLabel();
        jLabelTrabajos = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabelTrabajo = new javax.swing.JLabel();

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
        jLabelinventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelinventario.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelinventario.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelinventario.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelinventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 80, 60));

        jLabelreportes.setBackground(new java.awt.Color(153, 153, 153));
        jLabelreportes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelreportes.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelreportes.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelreportes.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelreportes.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelreportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 80, 60));

        jLabelfacturas.setBackground(new java.awt.Color(153, 153, 153));
        jLabelfacturas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelfacturas.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelfacturas.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelfacturas.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelfacturas.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelfacturas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 80, 60));

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
        getContentPane().add(jLabelAgregarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 430, 30, 30));

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
        getContentPane().add(jLabelCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 380, 30, 30));

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
        getContentPane().add(jLabelbarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 330, 110, 220));

        jLabelAdministrador.setText("          0");
        jLabelAdministrador.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabelAdministradorMouseMoved(evt);
            }
        });
        getContentPane().add(jLabelAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 480, 70, 60));

        jLabelventas.setBackground(new java.awt.Color(153, 153, 153));
        jLabelventas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelventas.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelventas.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelventas.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 80, 60));

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventario");
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, 140, -1));

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Compras");
        jLabel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 140, -1));

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Reportes");
        jLabel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 140, -1));

        jLabel6.setBackground(new java.awt.Color(51, 51, 51));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Ventas");
        jLabel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 140, -1));

        JBotonCerrar.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        JBotonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBotonCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(JBotonCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(875, 19, 25, 25));

        Minimizar.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        Minimizar.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        Minimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimizarActionPerformed(evt);
            }
        });
        getContentPane().add(Minimizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(844, 19, 25, 25));

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("CUENTAS BANCARIAS:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 530, 140, 40));

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("caja");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 460, 140, 40));

        jLabelCaja.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelCaja.setForeground(new java.awt.Color(204, 204, 204));
        jLabelCaja.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelCaja.setText("jLabel5");
        jLabelCaja.setToolTipText("");
        jLabelCaja.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabelCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 450, 270, 50));

        jLabelPtm.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelPtm.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPtm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelPtm.setText("jLabel5");
        getContentPane().add(jLabelPtm, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 520, 270, 50));

        jTextFieldPtm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldPtmKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPtmKeyTyped(evt);
            }
        });
        getContentPane().add(jTextFieldPtm, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 460, 190, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Retirar", "Consignar" }));
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, -1, -1));

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 60, 30));

        jLabelNomina.setBackground(new java.awt.Color(153, 153, 153));
        jLabelNomina.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelNomina.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelNomina.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelNomina.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelNomina.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelNomina, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, 80, 60));

        jLabelComprobante.setBackground(new java.awt.Color(153, 153, 153));
        jLabelComprobante.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelComprobante.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelComprobante.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelComprobante.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelComprobante.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelComprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 80, 60));

        jLabelTrabajos.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTrabajos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelTrabajos.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTrabajos.setText("Trabajos");
        jLabelTrabajos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelTrabajos.setOpaque(true);
        jLabelTrabajos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelTrabajosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelTrabajosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelTrabajosMouseExited(evt);
            }
        });
        getContentPane().add(jLabelTrabajos, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 140, -1));

        jLabel10.setBackground(new java.awt.Color(51, 51, 51));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nomina");
        jLabel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel10.setOpaque(true);
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 370, 140, -1));

        jLabel5.setBackground(new java.awt.Color(51, 51, 51));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Abonos");
        jLabel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 140, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("jLabel16");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 220, 70));

        jProgressBar1.setBackground(null);
        jProgressBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jProgressBar1.setForeground(new java.awt.Color(0, 0, 0));
        jProgressBar1.setMaximum(0);
        jProgressBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jProgressBar1.setBorderPainted(false);
        jProgressBar1.setStringPainted(true);
        getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 400, 32));

        jLabelTrabajo.setBackground(new java.awt.Color(153, 153, 153));
        jLabelTrabajo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabelTrabajo.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jLabelTrabajo.setMaximumSize(new java.awt.Dimension(35, 35));
        jLabelTrabajo.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabelTrabajo.setPreferredSize(new java.awt.Dimension(60, 60));
        getContentPane().add(jLabelTrabajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 80, 60));

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
        if (evt.getClickCount() == 1) {
            new Catalogo().setVisible(true);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if (evt.getClickCount() == 1) {
            new Compras().setVisible(true);
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        if (evt.getClickCount() == 1) {
            if (Reportes.m == 0) {
                new Reportes().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "la ventana ya esta abierta");
            }
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

        if (!jTextFieldPtm.getText().equals("")) {
            double valor = Double.parseDouble(jTextFieldPtm.getText().trim().replace(",", ""));
            if (jComboBox1.getSelectedItem() == "Consignar") {
                CambioCaja(-valor, "RECARGA BANCO");
                Cambioptm(valor, "RECARGA BANCO");
                jTextFieldPtm.setText("0");
            } else {
                CambioCaja(valor, "RETIRO BANCO");
                Cambioptm(-valor, "RETIRO BANCO");
                jTextFieldPtm.setText("0");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldPtmKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPtmKeyReleased
        if (!jTextFieldPtm.getText().equals("") && !Validaciones.validarString(evt)) {
            jTextFieldPtm.setText(FormatoPesos.formato(Double.valueOf(jTextFieldPtm.getText().replace(",", ""))));
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPtmKeyReleased

    private void jTextFieldPtmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPtmKeyTyped
        if (Validaciones.validarString(evt)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPtmKeyTyped

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        new Comprobante().setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked
    public static Nomina nomina = new Nomina();
    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        nomina.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jLabel1.setBackground(new Color(115, 115, 115));
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        jLabel6.setBackground(new Color(115, 115, 115));
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setBackground(new Color(115, 115, 115));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        jLabel5.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setBackground(new Color(115, 115, 115));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        jLabel6.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        jLabel5.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setBackground(new Color(51, 51, 51));// TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabelTrabajosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelTrabajosMouseClicked
        new OrdenDeTrabajo().setVisible(true);
    }//GEN-LAST:event_jLabelTrabajosMouseClicked

    private void jLabelTrabajosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelTrabajosMouseEntered
        jLabelTrabajos.setBackground(new Color(115, 115, 115));
    }//GEN-LAST:event_jLabelTrabajosMouseEntered

    private void jLabelTrabajosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelTrabajosMouseExited
        jLabelTrabajos.setBackground(new Color(51, 51, 51));
    }//GEN-LAST:event_jLabelTrabajosMouseExited

    void Cambioptm(double valor, String concepto) {
        try (Connection cn = Conexion.Conexion()) {
            double total = Double.parseDouble(jLabelPtm.getText().trim().replace(",", "")) + (valor);
            PreparedStatement ps = cn.prepareStatement("insert into ptm (id,Concepto,Valor,Total,Hora,Fecha) values(?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setString(2, concepto);
            ps.setDouble(3, valor);
            ps.setDouble(4, total);
            ps.setTime(5, new Time(Fechas.fechaActualDate().getTime()));
            ps.setDate(6, new Date(Fechas.fechaActualDate().getTime()));
            ps.execute();
            cn.close();
            if (total<0) {
                jLabelPtm.setText(FormatoPesos.formato(total));
            }else{
                 jLabelPtm.setText(" "+FormatoPesos.formato(total));
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    void CambioCaja(double valor, String concepto) {
        try (Connection cn = Conexion.Conexion()) {
            double total = Double.parseDouble(jLabelCaja.getText().trim().replace(",", "")) + (valor);
            PreparedStatement ps = cn.prepareStatement("insert into caja (id,Concepto,Valor,Total,Hora,Fecha) values(?,?,?,?,?,?)");
            ps.setInt(1, 0);
            ps.setString(2, concepto);
            ps.setDouble(3, valor);
            ps.setDouble(4, total);
            ps.setTime(5, new Time(Fechas.fechaActualDate().getTime()));
            ps.setDate(6, new Date(Fechas.fechaActualDate().getTime()));
            ps.execute();
            cn.close();
            if (total < 0) {
                jLabelCaja.setText(FormatoPesos.formato(total));
            } else {
                jLabelCaja.setText(" " + FormatoPesos.formato(total));
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBotonCerrar;
    private javax.swing.JButton Minimizar;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelAdministrador;
    private javax.swing.JLabel jLabelAgregarUsuario;
    protected static javax.swing.JLabel jLabelCaja;
    private javax.swing.JLabel jLabelCerrarSesion;
    private javax.swing.JLabel jLabelComprobante;
    private javax.swing.JLabel jLabelNomina;
    public static javax.swing.JLabel jLabelPtm;
    private javax.swing.JLabel jLabelTrabajo;
    private javax.swing.JLabel jLabelTrabajos;
    private javax.swing.JLabel jLabelbarra;
    private javax.swing.JLabel jLabelfacturas;
    private javax.swing.JLabel jLabelinventario;
    private javax.swing.JLabel jLabelreportes;
    private javax.swing.JLabel jLabelventas;
    public static javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jTextFieldPtm;
    // End of variables declaration//GEN-END:variables
}
