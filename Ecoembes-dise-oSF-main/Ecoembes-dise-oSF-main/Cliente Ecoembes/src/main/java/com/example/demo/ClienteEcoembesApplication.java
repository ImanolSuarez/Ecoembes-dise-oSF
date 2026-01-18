package com.example.demo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.example.demo.swing.SwingClientGUI;

public class ClienteEcoembesApplication {

    public static void main(String[] args) {
        // Configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar la aplicación en el Event Dispatch Thread de Swing
        EventQueue.invokeLater(() -> {
            try {
                SwingClientGUI gui = new SwingClientGUI();
                gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gui.setVisible(true);
                gui.toFront();
                gui.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
