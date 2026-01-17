package com.example.demo;

import com.example.demo.gui.VentanaLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClienteEcoembesApplication {

    public static void main(String[] args) {
        // Configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar la aplicación en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
        });
    }
}
