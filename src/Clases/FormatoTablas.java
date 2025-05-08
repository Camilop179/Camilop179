/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author fullmotors
 */
public class FormatoTablas extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setForeground((isSelected) ? new Color(0,0,0) : new Color(0,0,0));
        if (row % 2 == 0) {
            this.setBackground((isSelected) ? new Color(90,90,90) : new Color(102,102,102));
        }else{
            
            this.setBackground((isSelected) ? new Color(90,90,90) : Color.WHITE);
        }
        
        if(column ==2){
            this.setFont(Font.getFont("Segoe UI 12 Bold"));
        }
        else if (column == 3) {
            double precio = Double.parseDouble(value.toString().replace(",", ""));
            setText(FormatoPesos.formato(precio));
        } else if (column == 6) {
            double precio = Double.parseDouble(value.toString().replace(",", ""));
            setText(FormatoPesos.formato(precio));
        }


        return this;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
