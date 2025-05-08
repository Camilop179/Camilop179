/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USER
 */
public class uiPassword extends JPasswordField{
    
    private int arcw = 20;
    private int arch = 20;

    public uiPassword() {
        setOpaque(false);
        setBorder(new EmptyBorder(0, 5, 0, 2));
        setPreferredSize(new Dimension(100, 20));
    }

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
    
}
