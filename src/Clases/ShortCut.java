/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import Ventanas.FormaPago;
import Ventanas.Ventas;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author fullmotors
 */
public class ShortCut {
    

    static final HashMap<KeyStroke, Action> actionMap = new HashMap<>();

    public static void setup() {
        KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        
        KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK);
        
        actionMap.put(key1, new AbstractAction("action1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Ventas().setVisible(true);
            }
        });
        actionMap.put(key2, new AbstractAction("action2") {
            @Override
            public void actionPerformed(ActionEvent e) {new FormaPago(new Ventas(), true).setVisible(true);
        if (FormaPago.m) {
            int n = JOptionPane.showConfirmDialog(null, "¿Desea imprimir Factura?", "Venta Exitosa", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                new Imprimir().imprimir1();
            }
            Ventas.limpiar();
            Ventas.nroVenta();
        }
            }
        });
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        kfm.addKeyEventDispatcher((KeyEvent e) -> {
            KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
            if (actionMap.containsKey(keyStroke)) {
                final Action a = actionMap.get(keyStroke);
                final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        a.actionPerformed(ae);
                    }
                });
                return true;
            }
            return false;
        });
    }
    
    public static Action accion(Action a){
        return a;
    }

}
