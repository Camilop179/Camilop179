/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author harol
 */
public class ImagenBoton {
    public ImagenBoton(String imagen,JButton b,int w,int h){
        Image imagen1 = new ImageIcon(getClass().getResource("/imagenes/"+imagen)).getImage();
        Icon icono = new  ImageIcon(imagen1.getScaledInstance(w, h, Image.SCALE_DEFAULT));
        b.setIcon(icono);
    }
}
