
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VerGAD extends javax.swing.JFrame {

    private JTable tabla;
    private JButton botonRegresar;
    private JButton botonActualizar;
    private GADTableModel modeloTabla;

    public VerGAD() {
        super("Lista de GAD");

        try {
            // Crear un modelo de tabla personalizado
            modeloTabla = new GADTableModel();
            tabla = new JTable(modeloTabla);

            // Configurar el diseño de la interfaz
            setLayout(new BorderLayout());
            add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Botones
            botonActualizar = new JButton("Actualizar Datos");
            botonActualizar.addActionListener(this::mostrarVentanaActualizar);
            botonRegresar = new JButton("Regresar");
            botonRegresar.addActionListener(e -> {
            dispose(); // Cierra la ventana actual
            SwingUtilities.invokeLater(() -> {
                MenuGADs menuGAD = new MenuGADs();
                menuGAD.setVisible(true);
            });
            });

            JPanel panelBotones = new JPanel();
            panelBotones.add(botonActualizar);
            panelBotones.add(botonRegresar);

            add(panelBotones, BorderLayout.SOUTH);

            // Configurar la ventana
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // Centrar la ventana
            setVisible(true);

            // Cargar datos al iniciar la ventana
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        // Limpiar el modelo actual
        modeloTabla.clear();

        try {
            Connection conexion = Conexion.obtenerConexion();
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM GAD");

            // Agregar filas a la tabla desde el resultado de la consulta
            while (resultSet.next()) {
                int idGAD = resultSet.getInt("id_gad");
                String nombre = resultSet.getString("nombre");
                int idTipo = resultSet.getInt("id_tipo");
                int idUbicacion = resultSet.getInt("id_ubicacion");
                Date fechaCreacion = resultSet.getDate("fecha_creacion");

                modeloTabla.addGAD(new GAD(idGAD, nombre, idTipo, idUbicacion, fechaCreacion));
            }

            // Cerrar la conexión después de usarla
            resultSet.close();
            statement.close();
            conexion.close();

            // Notificar al modelo que los datos han cambiado
            modeloTabla.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarVentanaActualizar(ActionEvent event) {
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada != -1) {
            // Obtener los datos de la fila seleccionada
            GAD gadSeleccionado = modeloTabla.getGAD(filaSeleccionada);

            // Crear una nueva interfaz para actualizar datos
            SwingUtilities.invokeLater(() -> new VentanaActualizarGAD(gadSeleccionado, this));
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un GAD para actualizar sus datos.");
        }
    }

    // Método para actualizar la tabla en tiempo real
    public void actualizarTabla() {
        cargarDatos();
    }
}

class GAD {

    private int idGAD;
    private String nombre;
    private int idTipo;
    private int idUbicacion;
    private Date fechaCreacion;

    public GAD(int idGAD, String nombre, int idTipo, int idUbicacion, Date fechaCreacion) {
        this.idGAD = idGAD;
        this.nombre = nombre;
        this.idTipo = idTipo;
        this.idUbicacion = idUbicacion;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdGAD() {
        return idGAD;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
}

class GADTableModel extends AbstractTableModel {

    private List<GAD> GADs;
    private String[] columnNames = {"ID GAD", "Nombre", "ID Tipo", "ID Ubicación", "Fecha de Creación"};

    public GADTableModel() {
        this.GADs = new ArrayList<>();
    }

    public void addGAD(GAD gad) {
        GADs.add(gad);
        fireTableRowsInserted(GADs.size() - 1, GADs.size() - 1);
    }

    public void removeGAD(int rowIndex) {
        GADs.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public GAD getGAD(int rowIndex) {
        return GADs.get(rowIndex);
    }

    public void clear() {
        GADs.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return GADs.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GAD gad = GADs.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return gad.getIdGAD();
            case 1:
                return gad.getNombre();
            case 2:
                return gad.getIdTipo();
            case 3:
                return gad.getIdUbicacion();
            case 4:
                return gad.getFechaCreacion();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

class VentanaActualizarGAD extends JFrame {

    private JTextField campoNombre;
    private JTextField campoIdTipo;
    private JTextField campoIdUbicacion;
    private JTextField campoFechaCreacion;
    private JButton botonGuardar;
    private GAD gad;
    private VerGAD ventanaPrincipal;

    public VentanaActualizarGAD(GAD gadSeleccionado, VerGAD verGAD) {
        super("Actualizar Datos del GAD");

        this.gad = gadSeleccionado;
        this.ventanaPrincipal = verGAD;

        // Configurar el diseño de la interfaz
        setLayout(new GridLayout(7, 2));

        // Etiqueta y valor del ID GAD
        add(new JLabel("ID GAD:"));
        add(new JLabel(String.valueOf(gad.getIdGAD())));

        // Campos para editar nombre, ID Tipo y ID Ubicación
        add(new JLabel("Nombre:"));
        campoNombre = new JTextField(gad.getNombre());
        add(campoNombre);

        add(new JLabel("ID Tipo:"));
        campoIdTipo = new JTextField(String.valueOf(gad.getIdTipo()));
        add(campoIdTipo);

        add(new JLabel("ID Ubicación:"));
        campoIdUbicacion = new JTextField(String.valueOf(gad.getIdUbicacion()));
        add(campoIdUbicacion);

        // Campo para la fecha de creación con formato específico
        add(new JLabel("Fecha de Creación (YYYY-MM-DD):"));
        campoFechaCreacion = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(gad.getFechaCreacion()));
        add(campoFechaCreacion);

        // Botón para guardar los cambios
        botonGuardar = new JButton("Guardar Cambios");
        botonGuardar.addActionListener(e -> guardarCambios());
        add(botonGuardar);

        // Configurar la ventana
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal); // Centrar la ventana con respecto a la ventana principal
        setVisible(true);
    }

    private void guardarCambios() {
        try {
            // Establecer la conexión a la base de datos
            try (Connection conexion = Conexion.obtenerConexion()) {
                // Llamada al procedimiento almacenado
                try (CallableStatement callableStatement = conexion.prepareCall("{call sp_actualizar_gad(?, ?, ?, ?, ?, ?)}")) {
                    // Establecer los parámetros del procedimiento almacenado
                    callableStatement.setString(1, campoNombre.getText());
                    callableStatement.setInt(2, Integer.parseInt(campoIdTipo.getText()));
                    callableStatement.setInt(3, Integer.parseInt(campoIdUbicacion.getText()));
                    callableStatement.setDate(4, Date.valueOf(campoFechaCreacion.getText()));
                    callableStatement.setInt(5, gad.getIdGAD());
                    callableStatement.setString(6, ObtenerIP.obtenerIPPublica());
                    
                    
    
                    // Ejecutar el procedimiento almacenado
                    int filasActualizadas = callableStatement.executeUpdate();
    
                    if (filasActualizadas > 0) {
                        JOptionPane.showMessageDialog(this, "No se pudo guardar los cambios. Inténtalo nuevamente.");

                    } else {
                        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
                        // Actualizar la tabla en la ventana principal
                        ventanaPrincipal.actualizarTabla();
                        dispose(); // Cierra la ventana después de guardar los cambios
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VerGAD::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

