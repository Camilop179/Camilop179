/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.event.KeyEvent;

public class Validaciones {

    public static boolean validarString(KeyEvent key) {
        boolean b = true;
        char keyc = key.getKeyChar();
        if (Character.isDigit(keyc) || keyc == 8 || keyc == 10 || keyc == 46) {
            b = false;
        }
        return b;
    }

    public static boolean validarEnter(KeyEvent key) {
        boolean b = true;
        char keyc = key.getKeyChar();
        if (keyc == 10) {
            b = false;
        }
        return b;
    }

    public static boolean validarSuprimir(KeyEvent key) {
        boolean b = true;
        char keyc = key.getKeyChar();
        if (keyc == 127) {
            b = false;
        }
        return b;
    }
}
