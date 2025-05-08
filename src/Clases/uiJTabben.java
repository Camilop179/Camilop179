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

        g.setColor(null);
        
        switch (tabPlacement) {
            case SwingConstants.TOP ->
                g.fillRoundRect(x + 1, y + 1, w + 3, h + 2, w, h);
            case SwingConstants.BOTTOM ->
                g.fillRoundRect(x, y, w + 3, h + 2, w, h);
            case SwingConstants.LEFT ->
                g.fillRoundRect(x + 1, y + 1, w + 3, h + 2, w, h);
            case SwingConstants.RIGHT ->
                g.fillRoundRect(x, y + 1, w + 3, h + 2, w, h);
        }
        super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
    }

}
