package com.example.demo.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;

/**
 * Interfaz gráfica unificada del cliente Ecoembes.
 * Combina login y panel principal en una sola ventana con CardLayout.
 */
public class SwingClientGUI extends JFrame {

    private final SwingClientController controller;

    // Login components
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    // Main panel components
    private JTable tablaContenedores;
    private DefaultTableModel modeloTabla;
    private JTextField txtUbicacion;
    private JComboBox<Contenedor.NivelLlenado> comboNivel;
    private JRadioButton rbUbicacion, rbNivel;

    // Layout
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public SwingClientGUI() {
        this.controller = new SwingClientController();
        initComponents();
    }

    public SwingClientGUI(SwingClientController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Ecoembes - Sistema de Gestión de Contenedores");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createLoginPanel(), "LOGIN");
        contentPanel.add(createMainPanel(), "MAIN");

        add(contentPanel);
        cardLayout.show(contentPanel, "LOGIN");
    }

    // ==================== LOGIN PANEL ====================

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitle = new JLabel("Ecoembes - Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField("admin@ecoembes.es", 20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField("123", 20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Botón Login
        JButton btnLogin = new JButton("Iniciar Sesión");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> handleLogin());

        return panel;
    }

    private void handleLogin() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        try {
            String token = controller.login(email, password);
            if (token != null) {
                JOptionPane.showMessageDialog(this, "Login exitoso!");
                cardLayout.show(contentPanel, "MAIN");
                cargarContenedores("Bilbao");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== MAIN PANEL ====================

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rbUbicacion = new JRadioButton("Ubicación:", true);
        rbNivel = new JRadioButton("Estado:");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbUbicacion);
        bg.add(rbNivel);

        txtUbicacion = new JTextField("Bilbao", 10);
        comboNivel = new JComboBox<>(Contenedor.NivelLlenado.values());
        comboNivel.setEnabled(false);

        panelBusqueda.add(rbUbicacion);
        panelBusqueda.add(txtUbicacion);
        panelBusqueda.add(rbNivel);
        panelBusqueda.add(comboNivel);

        JButton btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);

        // Botón logout
        JButton btnLogout = new JButton("Cerrar Sesión");
        panelBusqueda.add(Box.createHorizontalStrut(20));
        panelBusqueda.add(btnLogout);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de contenedores
        String[] columnas = {"ID", "Ubicación", "CP", "Llenado", "Planta", "Envases", "Última Act."};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaContenedores = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaContenedores), BorderLayout.CENTER);

        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo Contenedor");
        JButton btnAsignar = new JButton("Asignar a Planta");
        JButton btnRefrescar = new JButton("Refrescar");

        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnAsignar);
        panelAcciones.add(btnRefrescar);
        panel.add(panelAcciones, BorderLayout.SOUTH);

        // Eventos
        rbUbicacion.addActionListener(e -> {
            txtUbicacion.setEnabled(true);
            comboNivel.setEnabled(false);
        });

        rbNivel.addActionListener(e -> {
            txtUbicacion.setEnabled(false);
            comboNivel.setEnabled(true);
        });

        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnRefrescar.addActionListener(e -> realizarBusqueda());

        btnLogout.addActionListener(e -> {
            controller.logout();
            cardLayout.show(contentPanel, "LOGIN");
            modeloTabla.setRowCount(0);
        });

        btnNuevo.addActionListener(e -> mostrarDialogoCrearContenedor());
        btnAsignar.addActionListener(e -> mostrarDialogoAsignarPlanta());

        return panel;
    }

    private void realizarBusqueda() {
        if (rbUbicacion.isSelected()) {
            cargarContenedores(txtUbicacion.getText());
        } else {
            cargarContenedoresPorNivel((Contenedor.NivelLlenado) comboNivel.getSelectedItem());
        }
    }

    private void cargarContenedores(String ubicacion) {
        if (ubicacion == null || ubicacion.isEmpty()) return;
        try {
            List<Contenedor> contenedores = controller.buscarContenedoresPorUbicacion(ubicacion);
            mostrarEnTabla(contenedores);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarContenedoresPorNivel(Contenedor.NivelLlenado nivel) {
        try {
            LocalDateTime inicio = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.now();
            List<Contenedor> contenedores = controller.buscarContenedoresPorNivelYFecha(nivel, inicio, fin);
            mostrarEnTabla(contenedores);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarEnTabla(List<Contenedor> contenedores) {
        modeloTabla.setRowCount(0);
        for (Contenedor c : contenedores) {
            Object[] fila = {
                    c.id(),
                    c.ubicacion(),
                    c.codigoPostal(),
                    c.nivelLlenado(),
                    c.nombrePlanta() != null ? c.nombrePlanta() : "---",
                    c.numEnvases(),
                    c.ultimaActualizacion()
            };
            modeloTabla.addRow(fila);
        }
    }

    // ==================== DIALOGOS ====================

    private void mostrarDialogoCrearContenedor() {
        JDialog dialog = new JDialog(this, "Nuevo Contenedor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtUbic = new JTextField();
        JTextField txtCp = new JTextField();
        JTextField txtCapacidad = new JTextField("1000");

        dialog.add(new JLabel("ID:"));
        dialog.add(txtId);
        dialog.add(new JLabel("Ubicación:"));
        dialog.add(txtUbic);
        dialog.add(new JLabel("Código Postal:"));
        dialog.add(txtCp);
        dialog.add(new JLabel("Capacidad Total:"));
        dialog.add(txtCapacidad);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        dialog.add(btnCancelar);
        dialog.add(btnGuardar);

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnGuardar.addActionListener(e -> {
            try {
                Contenedor nuevo = new Contenedor(
                        txtId.getText(),
                        txtUbic.getText(),
                        txtCp.getText(),
                        Float.parseFloat(txtCapacidad.getText())
                );
                controller.crearContenedor(nuevo);
                JOptionPane.showMessageDialog(dialog, "Contenedor creado con éxito");
                dialog.dispose();
                cargarContenedores(txtUbic.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void mostrarDialogoAsignarPlanta() {
        int selectedRow = tablaContenedores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un contenedor de la tabla");
            return;
        }

        String contenedorId = (String) modeloTabla.getValueAt(selectedRow, 0);

        String input = JOptionPane.showInputDialog(this, "ID de la Planta:", "Asignar a Planta", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) return;

        try {
            Long plantaId = Long.parseLong(input);
            Asignacion asignacion = new Asignacion(
                    plantaId,
                    Collections.singletonList(contenedorId),
                    LocalDate.now(),
                    controller.getCurrentUserEmail()
            );
            controller.asignarContenedoresAPlanta(asignacion);
            JOptionPane.showMessageDialog(this, "Contenedor asignado con éxito a la planta " + plantaId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID de planta inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SwingClientGUI().setVisible(true);
        });
    }
}
