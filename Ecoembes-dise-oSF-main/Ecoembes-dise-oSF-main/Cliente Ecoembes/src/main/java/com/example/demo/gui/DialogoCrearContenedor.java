package com.example.demo.gui;

import com.example.demo.dto.ContenedorDTO;
import javax.swing.*;
import java.awt.*;

public class DialogoCrearContenedor extends JDialog {

    private JTextField txtId;
    private JTextField txtUbicacion;
    private JTextField txtCP;
    private JTextField txtCapacidad;
    private ContenedorDTO contenedorCreado;
    private boolean verificado = false;

    public DialogoCrearContenedor(Frame parent) {
        super(parent, "Nuevo Contenedor", true);
        initComponents();
    }

    private void initComponents() {
        setSize(300, 250);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelCampos.add(new JLabel("ID (C-XXX):"));
        txtId = new JTextField();
        panelCampos.add(txtId);

        panelCampos.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        panelCampos.add(txtUbicacion);

        panelCampos.add(new JLabel("Código Postal:"));
        txtCP = new JTextField();
        panelCampos.add(txtCP);

        panelCampos.add(new JLabel("Capacidad:"));
        txtCapacidad = new JTextField("1000");
        panelCampos.add(txtCapacidad);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            try {
                String id = txtId.getText();
                String ubi = txtUbicacion.getText();
                String cp = txtCP.getText();
                float cap = Float.parseFloat(txtCapacidad.getText());

                if (id.isEmpty() || ubi.isEmpty() || cp.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                contenedorCreado = new ContenedorDTO(id, ubi, cp, cap);
                verificado = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public boolean isVerificado() {
        return verificado;
    }

    public ContenedorDTO getContenedor() {
        return contenedorCreado;
    }
}
