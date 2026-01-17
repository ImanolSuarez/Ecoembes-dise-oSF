package com.example.demo.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.example.demo.facade.AutenticacionController;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AutenticacionController authController;

    public VentanaLogin() {
        authController = new AutenticacionController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Ecoembes - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelCentral.add(new JLabel("Email:"));
        txtEmail = new JTextField("admin@ecoembes.es");
        panelCentral.add(txtEmail);

        panelCentral.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField("123");
        panelCentral.add(txtPassword);

        btnLogin = new JButton("Iniciar Sesión");
        panelCentral.add(new JLabel("")); // Espaciador
        panelCentral.add(btnLogin);

        add(panelCentral, BorderLayout.CENTER);

        btnLogin.addActionListener(this::handleLogin);
    }

    private void handleLogin(ActionEvent e) {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        try {
            String token = authController.login(email, password);
            if (token != null) {
                com.example.demo.ServiceProxy.ServiceProxy.getInstance().setToken(token);
                JOptionPane.showMessageDialog(this, "Login exitoso. Token: " + token);
                // Abrir ventana principal (Pendiente)
                new VentanaPrincipal(token).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
