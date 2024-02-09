
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterfazUbicacion extends javax.swing.JFrame {

    private JTable tabla;
    private JButton botonRegresar;
    private JButton botonActualizar;
    private UbicacionTableModel modeloTabla;

    public InterfazUbicacion() {
        super("Lista de Ubicaciones");

        try {
            // Crear un modelo de tabla personalizado
            modeloTabla = new UbicacionTableModel();
            tabla = new JTable(modeloTabla);

            // Configurar el ancho de la columna "Dirección"
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(3).setPreferredWidth(150); // Ajusta el valor según tus necesidades

            // Configurar el diseño de la interfaz
            setLayout(new BorderLayout());
            add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Botones
            botonActualizar = new JButton("Actualizar Datos");
            botonActualizar.addActionListener(this::mostrarVentanaActualizar);
            botonRegresar = new JButton("Regresar");
            botonRegresar.addActionListener(e -> {
            MenuGADs menuGAD = new MenuGADs();
            menuGAD.setVisible(true);
            dispose();
            
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
            cargarDatosUbicacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosUbicacion() {
        // Limpiar el modelo actual
        modeloTabla.clear();

        try {
            Connection conexion = Conexion.obtenerConexion();
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UBICACION");

            // Agregar filas a la tabla desde el resultado de la consulta
            while (resultSet.next()) {
                int idUbicacion = resultSet.getInt("id_ubicacion");
                String nombre = resultSet.getString("nombre");
                boolean estado = resultSet.getBoolean("estado");
                int idPadre = resultSet.getInt("id_padre");
                String tipoUbicacion = resultSet.getString("tipo_ubicacion");
                modeloTabla.addUbicacion(new Ubicacion(idUbicacion, nombre, estado, idPadre, tipoUbicacion));
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
            Ubicacion ubicacionSeleccionada = modeloTabla.getUbicacion(filaSeleccionada);

            // Crear una nueva interfaz para actualizar datos
            SwingUtilities.invokeLater(() -> new VentanaActualizarUbicacion(ubicacionSeleccionada, this));
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una ubicación para actualizar sus datos.");
        }
    }

// Método para actualizar la tabla en tiempo real
    public void actualizarTabla() {
        cargarDatosUbicacion();
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
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazUbicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazUbicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazUbicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazUbicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazUbicacion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

class Ubicacion {

    private int idUbicacion;
    private String nombre;
    private boolean estado;
    private int idPadre;
    private String tipoUbicacion;

    public Ubicacion(int idUbicacion, String nombre, boolean estado, int idPadre, String tipoUbicacion) {
        this.idUbicacion = idUbicacion;
        this.nombre = nombre;
        this.estado = estado;
        this.idPadre = idPadre;
        this.tipoUbicacion = tipoUbicacion;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public String getTipoUbicacion() {
        return tipoUbicacion;
    }

}

class UbicacionTableModel extends AbstractTableModel {

    private List<Ubicacion> ubicaciones;
    private String[] columnNames = {"ID Ubicación", "Nombre", "Estado", "ID Padre", "Tipo de Ubicación"};

    public UbicacionTableModel() {
        this.ubicaciones = new ArrayList<>();
    }

    public void addUbicacion(Ubicacion ubicacion) {
        ubicaciones.add(ubicacion);
        fireTableRowsInserted(ubicaciones.size() - 1, ubicaciones.size() - 1);
    }

    public void removeUbicacion(int rowIndex) {
        ubicaciones.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Ubicacion getUbicacion(int rowIndex) {
        return ubicaciones.get(rowIndex);
    }

    public void clear() {
        ubicaciones.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return ubicaciones.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ubicacion ubicacion = ubicaciones.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ubicacion.getIdUbicacion();
            case 1:
                return ubicacion.getNombre();
            case 2:
                return ubicacion.isEstado();
            case 3:
                return ubicacion.getIdPadre();
            case 4:
                return ubicacion.getTipoUbicacion();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

class VentanaActualizarUbicacion extends JFrame {

    private JTextField campoNombre;
    private JTextField campoEstado;
    private JTextField campoIdPadre;
    private JTextField campoTipoUbicacion;
    private JButton botonGuardar;
    private Ubicacion ubicacion;
    private InterfazUbicacion ventanaPrincipal;

    public VentanaActualizarUbicacion(Ubicacion ubicacion, InterfazUbicacion ventanaPrincipal) {
        super("Actualizar Datos de la Ubicación");

        this.ubicacion = ubicacion;
        this.ventanaPrincipal = ventanaPrincipal;

        // Configurar el diseño de la interfaz
        setLayout(new GridLayout(6, 2));

        add(new JLabel("ID Ubicación:"));
        add(new JLabel(String.valueOf(ubicacion.getIdUbicacion())));

        add(new JLabel("Nombre:"));
        campoNombre = new JTextField(ubicacion.getNombre());
        add(campoNombre);

        add(new JLabel("Estado:"));
        campoEstado = new JTextField(String.valueOf(ubicacion.isEstado()));
        add(campoEstado);

        add(new JLabel("ID Padre:"));
        campoIdPadre = new JTextField(String.valueOf(ubicacion.getIdPadre()));
        add(campoIdPadre);

        add(new JLabel("Tipo de Ubicación:"));
        campoTipoUbicacion = new JTextField(ubicacion.getTipoUbicacion());
        add(campoTipoUbicacion);

        botonGuardar = new JButton("Guardar Cambios");
        botonGuardar.addActionListener(e -> guardarCambios());
        add(botonGuardar);

        // Configurar la ventana
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana al presionar el botón de cerrar
        setLocationRelativeTo(ventanaPrincipal); // Centrar la ventana con respecto a la ventana principal
        setVisible(true);
    }

    private void guardarCambios() {
        try (Connection conexion = Conexion.obtenerConexion()) {
            // Llamada al procedimiento almacenado
            try (CallableStatement callableStatement = conexion.prepareCall("{call sp_actualizar_ubicacion(?, ?, ?, ?, ?, ?)}")) {
                // Establecer los parámetros del procedimiento almacenado
                callableStatement.setString(1, campoNombre.getText());
                callableStatement.setBoolean(2, Boolean.parseBoolean(campoEstado.getText()));
                callableStatement.setInt(3, Integer.parseInt(campoIdPadre.getText()));
                callableStatement.setString(4, campoTipoUbicacion.getText());
                callableStatement.setInt(5, ubicacion.getIdUbicacion());
                callableStatement.setString(6, ObtenerIP.obtenerIPPublica()); // Ejemplo de dirección IP
        
                // Ejecutar la actualización
                int filasActualizadas = callableStatement.executeUpdate();
        
                // Comprobar si se actualizaron filas y mostrar un mensaje apropiado
                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar los cambios. Inténtalo nuevamente.");

                } else {
                    
                    JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
                    ventanaPrincipal.actualizarTabla(); // Actualizar la tabla en la ventana principal
                    dispose(); // Cerrar la ventana actual
                }
            }
        } catch (SQLException | NumberFormatException e) {
            // Manejar excepciones SQL y de formato de número
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios. Verifica los datos ingresados.");
            e.printStackTrace(); // Puedes manejar las excepciones de manera más específica según tus necesidades.
        }
    }
    
    
    
}
