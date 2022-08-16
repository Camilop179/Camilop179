/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 *
 * @author harol
 */
public class TablaFondo extends JTable{
    private Image fondo;
    private String d;
    public TablaFondo(String d){
        this.d = d;
    }
        @Override
        public void paint(Graphics g){
        fondo = new ImageIcon(getClass().getResource("/imagenes/"+d)).getImage();
        g.drawImage(fondo, 0, 0,getWidth(), getHeight(), this);
        setOpaque(false);
        super.paint(g);
    }
}
