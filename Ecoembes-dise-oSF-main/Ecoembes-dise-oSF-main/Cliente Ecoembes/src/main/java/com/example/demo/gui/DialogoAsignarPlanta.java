package com.example.demo.gui;

import javax.swing.*;
import java.awt.*;

public class DialogoAsignarPlanta extends JDialog {

    private JTextField txtPlantaId;
    private Long plantaIdSeleccionada;
    private boolean verificado = false;

    public DialogoAsignarPlanta(Frame parent) {
        super(parent, "Asignar a Planta", true);
        initComponents();
    }

    private void initComponents() {
        setSize(300, 150);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(1, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelCampos.add(new JLabel("ID Planta (Long):"));
        txtPlantaId = new JTextField();
        panelCampos.add(txtPlantaId);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAceptar.addActionListener(e -> {
            try {
                plantaIdSeleccionada = Long.parseLong(txtPlantaId.getText());
                verificado = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de planta debe ser un número", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public boolean isVerificado() {
        return verificado;
    }

    public Long getPlantaId() {
        return plantaIdSeleccionada;
    }
}
