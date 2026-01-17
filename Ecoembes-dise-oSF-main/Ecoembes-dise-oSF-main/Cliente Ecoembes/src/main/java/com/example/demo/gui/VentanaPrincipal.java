package com.example.demo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.example.demo.dto.ContenedorDTO;
import com.example.demo.facade.AsignacionController;
import com.example.demo.facade.ContenedorController;

public class VentanaPrincipal extends JFrame {

    private ContenedorController contenedorController;
    private AsignacionController asignacionController;
    private JTable tablaContenedores;
    private DefaultTableModel modeloTabla;
    private JTextField txtUbicacion;
    private JComboBox<ContenedorDTO.NivelLlenado> comboNivel;
    private JRadioButton rbUbicacion, rbNivel;

    public VentanaPrincipal(String token) {
        this.contenedorController = new ContenedorController();
        this.asignacionController = new AsignacionController();
        initComponents();
        cargarContenedores("Bilbao"); // Carga inicial
    }

    private void initComponents() {
        setTitle("Ecoembes - Panel Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra superior de búsqueda mejorada
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rbUbicacion = new JRadioButton("Ubicación:", true);
        rbNivel = new JRadioButton("Estado:");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbUbicacion);
        bg.add(rbNivel);

        txtUbicacion = new JTextField("Bilbao", 10);
        comboNivel = new JComboBox<>(ContenedorDTO.NivelLlenado.values());
        comboNivel.setEnabled(false);

        panelBusqueda.add(rbUbicacion);
        panelBusqueda.add(txtUbicacion);
        panelBusqueda.add(rbNivel);
        panelBusqueda.add(comboNivel);

        JButton btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de contenedores
        String[] columnas = { "ID", "Ubicación", "CP", "Llenado", "Planta", "Envases", "Última Act." };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaContenedores = new JTable(modeloTabla);
        add(new JScrollPane(tablaContenedores), BorderLayout.CENTER);

        // Panel inferior de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo Contenedor");
        JButton btnAsignar = new JButton("Asignar a Planta");
        JButton btnRefrescar = new JButton("Refrescar");

        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnAsignar);
        panelAcciones.add(btnRefrescar);
        add(panelAcciones, BorderLayout.SOUTH);

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

        btnNuevo.addActionListener(e -> {
            DialogoCrearContenedor diag = new DialogoCrearContenedor(this);
            diag.setVisible(true);
            if (diag.isVerificado()) {
                try {
                    contenedorController.crearContenedor(diag.getContenedor());
                    JOptionPane.showMessageDialog(this, "Contenedor creado con éxito");
                    cargarContenedores(diag.getContenedor().getUbicacion());
                } catch (Exception ex) {

                }
            }
        });

        btnAsignar.addActionListener(e -> {
            int selectedRow = tablaContenedores.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un contenedor de la tabla");
                return;
            }
            String contenedorId = (String) modeloTabla.getValueAt(selectedRow, 0);

            DialogoAsignarPlanta diag = new DialogoAsignarPlanta(this);
            diag.setVisible(true);
            if (diag.isVerificado()) {
                try {
                    com.example.demo.dto.AsignacionDTO dto = new com.example.demo.dto.AsignacionDTO(
                            diag.getPlantaId(),
                            Collections.singletonList(contenedorId),
                            java.time.LocalDate.now(),
                            "admin@ecoembes.es" // Usuario actual
                    );
                    asignacionController.asignarAPlanta(dto);
                    JOptionPane.showMessageDialog(this,
                            "Contenedor asignado con éxito a la planta " + diag.getPlantaId());
                } catch (Exception ex) {

                }
            }
        });
    }

    private void realizarBusqueda() {
        if (rbUbicacion.isSelected()) {
            cargarContenedores(txtUbicacion.getText());
        } else {
            cargarContenedoresPorNivel((ContenedorDTO.NivelLlenado) comboNivel.getSelectedItem());
        }
    }

    private void cargarContenedores(String ubicacion) {
        if (ubicacion == null || ubicacion.isEmpty())
            return;
        try {
            List<ContenedorDTO> contenedores = contenedorController.buscarPorUbicacion(ubicacion);
            mostrarEnTabla(contenedores);
        } catch (Exception ex) {

        }
    }

    private void cargarContenedoresPorNivel(ContenedorDTO.NivelLlenado nivel) {
        try {
            // Usamos un rango amplio por defecto (desde el inicio de los tiempos hasta
            // ahora)
            LocalDateTime inicio = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.now();
            List<ContenedorDTO> contenedores = contenedorController.buscarPorNivelYFecha(nivel, inicio, fin);
            mostrarEnTabla(contenedores);
        } catch (Exception ex) {

        }
    }

    private void mostrarEnTabla(List<ContenedorDTO> contenedores) {
        modeloTabla.setRowCount(0);
        for (ContenedorDTO c : contenedores) {
            Object[] fila = {
                    c.getId(),
                    c.getUbicacion(),
                    c.getCodigoPostal(),
                    c.getNivelLlenado(),
                    c.getNombrePlanta() != null ? c.getNombrePlanta() : "---",
                    c.getNumEnvases(),
                    c.getUltimaActualizacion()
            };
            modeloTabla.addRow(fila);
        }
    }
}
