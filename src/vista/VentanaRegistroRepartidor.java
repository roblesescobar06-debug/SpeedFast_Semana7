package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Formulario para registrar repartidores en la base de datos
 * y mostrar los repartidores almacenados en una JTable.
 */
public class VentanaRegistroRepartidor extends JFrame {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    private JTextField txtNombre;
    private DefaultTableModel modeloTabla;

    public VentanaRegistroRepartidor() {
        setTitle("SpeedFast - Registrar repartidor");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----- Título -----
        JLabel lblTitulo = new JLabel("Registro de repartidores", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        // ----- Formulario -----
        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        txtNombre = new JTextField(20);
        JButton btnGuardar = new JButton("Guardar");
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(btnGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // ----- Tabla -----
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tablaRepartidores = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaRepartidores);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // ----- Botones -----
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnCerrar);

        btnGuardar.addActionListener(e -> guardarRepartidor());
        btnCerrar.addActionListener(e -> dispose());

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarRepartidores();
    }

    private void guardarRepartidor() {
        String nombre = txtNombre.getText().trim();

        if (nombre.length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "El nombre debe tener al menos 3 caracteres.",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (repartidorDAO.guardar(new Repartidor(0, nombre))) {
            JOptionPane.showMessageDialog(this,
                    "Repartidor " + nombre + " guardado correctamente en la base de datos.",
                    "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);
            txtNombre.setText("");
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el repartidor.\nRevisa la consola para ver el detalle.",
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRepartidores() {
        modeloTabla.setRowCount(0);
        for (Repartidor r : repartidorDAO.listarTodos()) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }
}
