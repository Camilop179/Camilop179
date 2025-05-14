package Clases;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fondo extends JPanel {

    private Image fondo;
    private String d;

    public Fondo(String d) {
        this.d = d;
    }

    @Override
    public void paint(Graphics g) {
        fondo = new ImageIcon(getClass().getResource("/imagenes/" + d)).getImage();
        try {
            if (fondo != null) {
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                setOpaque(false);
                super.paint(g);
            } else {
                System.out.println("Clases.Fondo.paint()");
            }
        } catch (Exception e) {
        }

    }

}
