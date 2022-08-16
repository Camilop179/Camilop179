/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Imagenes extends JPanel{

   
    public Imagenes(String d,JLabel j){
        ImageIcon imagen1 = new ImageIcon(getClass().getResource("/imagenes/"+d));
        Icon icono = new  ImageIcon(imagen1.getImage().getScaledInstance(j.getWidth(), j.getHeight(), Image.SCALE_DEFAULT));
        j.setIcon(icono);
    }
}

