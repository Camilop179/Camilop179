/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author fullmotors
 */
public class FormatoTablasNomina extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setForeground((isSelected) ? new Color(0, 0, 204) : new Color(51, 153, 255));
        if (row % 2 == 0) {
            this.setBackground((isSelected) ? new Color(0, 102, 255) : new Color(234,234,234));
        }else{
            
            this.setBackground((isSelected) ? new Color(0, 102, 255) : Color.WHITE);
        }

        switch (column) {
            case 3 ->                 {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
            case 4 ->                 {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
            case 5 ->                 {
                    double precio = Double.parseDouble(value.toString().replace(",", ""));
                    setText(FormatoPesos.formato(precio));
                }
            default -> {
            }
        }

        setFont(table.getFont());

        return this;
    }

    @Override
    public Color getBackground() {
        return super.getBackground(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    

}
