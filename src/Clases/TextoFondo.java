/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.JTextField;

/**
 *
 * @author harol
 */
public class TextoFondo {

    String mensaje;
    JTextField textField;
    private static Color color;

    public TextoFondo(String mensaje, JTextField jt) {
        this.mensaje = mensaje;
        this.textField = jt;
        colorMensaje(mensaje, textField);
        gainFucos(textField, mensaje);
    }

    private static void colorMensaje(String mensaje, JTextField textField) {
        color = textField.getForeground();
        textField.setForeground(new Color(191, 201, 202));
        textField.setText(mensaje);
    }

    public static void gainFucos(JTextField textField, String mensaje) {
        textField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {

                String contenido = textField.getText();
                if (!contenido.equals("") && contenido.equals(mensaje)) {
                    textField.setText("");
                    textField.setForeground(color);
                    textField.setBackground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String contenido = textField.getText();

                if (contenido.equals("")) {
                    colorMensaje(mensaje, textField);
                }
            }
        });
    }

}
