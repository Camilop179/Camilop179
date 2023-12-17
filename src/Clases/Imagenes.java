/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Imagenes {

   
    public Imagenes(String d,JLabel j,int w,int h){
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/imagenes/"+d));
        Icon icono = new  ImageIcon(imagen1.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
        j.setIcon(icono);
    }
}

