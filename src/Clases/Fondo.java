package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fondo extends JPanel {

    private String d;

    public Fondo(String d) {
        this.d = d;
    }

    @Override
    public void setBackground(Color bg) {
        bg = new Color(5, 0, 80);
        super.setBackground(bg); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
