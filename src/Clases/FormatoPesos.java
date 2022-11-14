/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author harol
 */
public class FormatoPesos {
    public static String formato(double valor){
        
        DecimalFormatSymbols sm = new DecimalFormatSymbols();
        sm.setDecimalSeparator('.');
        sm.setGroupingSeparator(',');
        DecimalFormat dm = new DecimalFormat("###,###",sm);
        return dm.format(valor);
    }
}
