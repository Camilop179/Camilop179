/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author harol
 */
public class Fechas {
    public static Date fecha_a;
    public static String fechaActual(){
        Date fecha_a = new Date();
        SimpleDateFormat formatdma = new SimpleDateFormat("dd/MM/YYYY");
        String fecha_actual = formatdma.format(fecha_a);
        return fecha_actual;
    }
     public static String fechaString(Date fecha){
        SimpleDateFormat formatdma = new SimpleDateFormat("dd/MM/YYYY");
        String fechaS = formatdma.format(fecha);
        return fechaS;
    }
     public static Date fechaActualDate(){
         Date fecha_a = new Date();
        return fecha_a;
    }
    public static LocalTime horaActual(){
        LocalTime hora = LocalTime.now();
        return hora;
    }   
}
