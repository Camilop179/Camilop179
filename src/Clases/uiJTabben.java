/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author fullmotors
 */
public class uiJTabben extends BasicTabbedPaneUI {

    @Override
    public void paint(Graphics g, JComponent c) {
        Image imagen1 = new ImageIcon(getClass().getResource("/imagenes/Fondo.jpg")).getImage();
        g.drawImage(imagen1, 0, 0, c.getWidth(), c.getHeight(), c);
        c.setOpaque(false);
        super.paint(g, c); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {

        super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
        g.setColor(null);
        switch (tabPlacement) {
            case SwingConstants.TOP -> g.fillRect(x + 1, y + 1, w - 1, h - 1);
            case SwingConstants.BOTTOM -> g.fillRect(x, y, w - 1, h - 1);
            case SwingConstants.LEFT -> g.fillRect(x + 1, y + 1, w - 1, h - 2);
            case SwingConstants.RIGHT -> g.fillRect(x, y + 1, w - 1, h - 2);
        }
    }

}
