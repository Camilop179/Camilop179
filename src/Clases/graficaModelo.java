/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author USER
 */
public class graficaModelo extends XYSplineRenderer {

    XYDataset data;

    public graficaModelo(XYDataset data, int n) {
        this.data = data;
    }

    
    @Override
    public Paint getItemPaint(int row, int column) {

        double v = (double) data.getYValue(row, column);
        if (v < 0) {
            return Color.red;
        } else {
            return Color.green;
        }

        }

}
