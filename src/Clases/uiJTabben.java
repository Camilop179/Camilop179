/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Graphics;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author fullmotors
 */
public class uiJTabben extends BasicTabbedPaneUI {

    

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {

        super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
        g.setColor(null);
        switch (tabPlacement) {
            case SwingConstants.TOP -> g.fillRect(x + 1, y + 1, w + 1, h - 1);
            case SwingConstants.BOTTOM -> g.fillRect(x, y, w + 1, h - 1);
            case SwingConstants.LEFT -> g.fillRect(x + 1, y + 1, w + 1, h - 2);
            case SwingConstants.RIGHT -> g.fillRect(x, y + 1, w + 1, h - 2);
        }
    }

}
