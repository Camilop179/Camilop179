/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author harol
 */
public class Errores {

   
    public static void Errores(String error){
        String ruta = "Errores.txt";
        try {
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            try ( PrintWriter conte = new PrintWriter(ruta)) {
                conte.println(error);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
