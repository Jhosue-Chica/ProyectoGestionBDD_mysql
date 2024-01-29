
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class InterfazConsultaContratos extends javax.swing.JFrame {

    private JComboBox<String> cboEstadoContrato;
    private JTable tblContratos;
    private EditableTableModel tableModel; // Cambio en el tipo de modelo
    private JTextArea txtDetallesEmpleado;
    private JButton btnRegresar;

    /**
     * Creates new form InterfazConsultaContratos
     */
    public InterfazConsultaContratos() {
        setTitle("Consulta de Contratos");
                setSize(1100, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear componentes
        JLabel lblEstadoContrato = new JLabel("Seleccionar Estado del Contrato:");
        cboEstadoContrato = new JComboBox<>();
        cboEstadoContrato.addItem("Activo");
        cboEstadoContrato.addItem("Inactivo");

        JButton btnConsultar = new JButton("Consultar Contratos");
        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarContratos();
            }
        });

        // Botón modificar estado contrato
        JButton btnActualizarEstado = new JButton("Actualizar Estado del Contrato");
        btnActualizarEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEstadoContrato();
            }
        });

        // Crear botón de regresar
        btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar();
            }
        });

        // Obtener la resolución de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Calcular la posición para centrar la ventana
        int xPos = (screenSize.width - getWidth()) / 2;
        int yPos = (screenSize.height - getHeight()) / 2;

        // Establecer la posición de la ventana
        setLocation(xPos, yPos);

        tableModel = new EditableTableModel();
        tblContratos = new JTable(tableModel);
        tblContratos.setRowHeight(20);

        // Asegúrate de agregar la columna "id_estado_contrato" como editable
        int idEstadoContratoColumnIndex = 2;  // Cambia esto según la posición real de la columna
        tableModel.addEditableColumn(idEstadoContratoColumnIndex);

        txtDetallesEmpleado = new JTextArea();
        txtDetallesEmpleado.setEditable(false);
        JScrollPane scrollPaneEmpleado = new JScrollPane(txtDetallesEmpleado);
        scrollPaneEmpleado.setPreferredSize(new Dimension(300, 400));

        tblContratos.setPreferredScrollableViewportSize(new Dimension(700, 400));
        tblContratos.setFillsViewportHeight(true);

        JScrollPane scrollPaneContratos = new JScrollPane(tblContratos);
        scrollPaneContratos.setPreferredSize(new Dimension(700, 400));

        // Configurar el diseño del JFrame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblEstadoContrato, gbc);

        gbc.gridx = 1;
        panel.add(cboEstadoContrato, gbc);

        gbc.gridx = 2;
        panel.add(btnConsultar, gbc);

        gbc.gridx = 3;
        panel.add(btnActualizarEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(scrollPaneContratos, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(scrollPaneEmpleado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = -50;
        panel.add(btnRegresar, gbc);

        add(panel);

        setVisible(true);
    }

    private void actualizarEstadoContrato() {
        int selectedRow = tblContratos.getSelectedRow();

        if (selectedRow != -1) {
            int idContrato = (int) tblContratos.getValueAt(selectedRow, 0);

            // Obtén el nuevo estado directamente desde la tabla
            Object nuevoEstadoObject = tblContratos.getValueAt(selectedRow, tblContratos.getColumn("id_estado_contrato").getModelIndex());

            if (nuevoEstadoObject != null) {
                try {
                    int nuevoEstado = Integer.parseInt(nuevoEstadoObject.toString());

                    // Actualiza el estado del contrato en la base de datos
                    actualizarEstadoContratoEnBD(idContrato, nuevoEstado);

                    // Refresca la tabla
                    consultarContratos();
                    System.out.println("Nuevo Estado: " + nuevoEstado);
                    System.out.println("ID Contrato: " + idContrato);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Error al convertir el nuevo estado del contrato a un entero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El nuevo estado del contrato es nulo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un contrato antes de actualizar el estado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarEstadoContratoEnBD(int idContrato, int nuevoEstado) {
        try (Connection conexion = Conexion.obtenerConexion()) {
            // Llamada al procedimiento almacenado sp_ActualizarEstadoContrato
            try (CallableStatement callableStatement = conexion.prepareCall("{call sp_ActualizarEstadoContrato(?, ?)}")) {
                callableStatement.setInt(1, idContrato);
                callableStatement.setInt(2, nuevoEstado);
    
                boolean resultado = callableStatement.execute();
    
                if (resultado) {
                    try (ResultSet resultSet = callableStatement.getResultSet()) {
                        if (resultSet.next()) {
                            int filasActualizadas = resultSet.getInt("filasActualizadas");
                            String mensaje = resultSet.getString("mensaje");
    
                            if (filasActualizadas > 0) {
                                System.out.println("Estado del contrato actualizado correctamente. " + mensaje);
                            } else {
                                System.out.println("No se pudo actualizar el estado del contrato. " + mensaje);
                            }
                        }
                    }
                } else {
                    System.out.println("La llamada al procedimiento no devolvió un resultado.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    // Método para verificar si un estado de contrato existe en la vista vw_EstadoContratoExiste
    private boolean estadoContratoExiste(int idEstadoContrato, Connection conexion) throws SQLException {
        String sql = "SELECT id_estado_contrato FROM vw_EstadoContratoExiste WHERE id_estado_contrato = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setInt(1, idEstadoContrato);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Retorna true si el estado existe
            }
        }
    }


    private void consultarContratos() {
        // Lógica para consultar todos los detalles de los contratos según el estado seleccionado
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
    
        String estadoSeleccionado = cboEstadoContrato.getSelectedItem().toString();
        int idEstadoContrato = (estadoSeleccionado.equals("Activo")) ? 1 : 2;
        System.out.println("ID Estado Contrato: " + idEstadoContrato);
    
        try (Connection conexion = Conexion.obtenerConexion()) {
            String vistaContratos = (idEstadoContrato == 1) ? "ContratosActivos" : "ContratosInactivos";
            String sql = "SELECT * FROM " + vistaContratos;
    
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
    
                    // Agregar columnas al modelo de la tabla
                    for (int i = 1; i <= columnCount; i++) {
                        tableModel.addColumn(metaData.getColumnName(i));
                    }
    
                    // Ajustar el ancho de las columnas
                    for (int i = 0; i < tblContratos.getColumnCount(); i++) {
                        TableColumn column = tblContratos.getColumnModel().getColumn(i);
                        if (metaData.getColumnName(i + 1).equals("id_estado_contrato")) {
                            // Ajusta el ancho de la columna id_estado_contrato
                            column.setPreferredWidth(150);  // Ajusta el tamaño según tus necesidades
                        } else {
                            // Ajusta el ancho de las demás columnas
                            column.setPreferredWidth(120);  // Ajusta el tamaño según tus necesidades
                        }
                    }
    
                    // Agregar filas al modelo de la tabla
                    while (resultSet.next()) {
                        Object[] rowData = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            rowData[i - 1] = resultSet.getObject(i);
                        }
                        tableModel.addRow(rowData);
                    }
                }
            }
    
            // Mostrar detalles del empleado cuando se selecciona un contrato
            tblContratos.getSelectionModel().addListSelectionListener(e -> mostrarDetallesEmpleado());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    private void mostrarDetallesEmpleado() {
        int selectedRow = tblContratos.getSelectedRow();
    
        if (selectedRow != -1) {
            int idEmpleado = (int) tblContratos.getValueAt(selectedRow, 1);
    
            try (Connection conexion = Conexion.obtenerConexion()) {
                String sql = "SELECT * FROM vw_DetallesEmpleado WHERE id_empleado = ?";
                try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                    preparedStatement.setInt(1, idEmpleado);
    
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int id = resultSet.getInt("id_empleado");
                            String nombre = resultSet.getString("nombre");
                            String apellidos = resultSet.getString("apellidos");
                            String direccion = resultSet.getString("direccion");
                            String telefono = resultSet.getString("telefono");
                            Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");

                            String detallesEmpleado = String.format(
                                    "ID Empleado: %d\nNombre: %s\nApellidos: %s\nDirección: %s\nTeléfono: %s\nFecha Nacimiento: %s",
                                    id, nombre, apellidos, direccion, telefono, fechaNacimiento);

                            txtDetallesEmpleado.setText(detallesEmpleado);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void regresar() {
        this.dispose();
    }

    public class EditableTableModel extends DefaultTableModel {

        private final Set<Integer> editableColumns = new HashSet<>();

        public void addEditableColumn(int columnIndex) {
            editableColumns.add(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return editableColumns.contains(column);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        SwingUtilities.invokeLater(() -> new InterfazConsultaContratos());

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
