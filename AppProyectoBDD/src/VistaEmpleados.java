
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VistaEmpleados extends javax.swing.JFrame {

    private Connection connection;

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnContratos, btnGadUbicacion, btnEmpleadosActivos, btnAgenciasContacto, btnRegresar;

    public VistaEmpleados() {
        
        super("Interfaz de Base de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        // Inicializar botones
        btnContratos = new JButton("Ver Contratos");
        btnGadUbicacion = new JButton("Ver GAD con Ubicación");
        btnEmpleadosActivos = new JButton("Ver Empleados Activos");
        btnAgenciasContacto = new JButton("Ver Agencias con Contacto");
        btnRegresar = new JButton("Regresar");


        // Configurar tabla
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Agregar componentes al contenedor
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnContratos);
        buttonPanel.add(btnGadUbicacion);
        buttonPanel.add(btnEmpleadosActivos);
        buttonPanel.add(btnAgenciasContacto);
        buttonPanel.add(btnRegresar);

        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        // Manejadores de eventos
        btnContratos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showData("SELECT * FROM vw_detalle_contrato");
            }
        });

        btnGadUbicacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showData("SELECT * FROM vw_detalle_gad_ubicacion");
            }
        });

        btnEmpleadosActivos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showData("SELECT * FROM vw_empleados_activos");
            }
        });

        btnAgenciasContacto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showData("SELECT * FROM vw_agencias_contacto");
            }
        });

        btnRegresar.addActionListener(e -> {
            dispose(); // Cierra la ventana actual
            MenuGADs menuGAD = new MenuGADs();
            menuGAD.setVisible(true);
        });
        // Conectar a la base de datos y crear vistas si no existen
        connectToDatabase();
        
    }

    private void connectToDatabase() {
        try {
            connection = Conexion.obtenerConexion();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createViews() {
        try (Statement statement = connection.createStatement()) {
            // Verificar la existencia de la vista antes de intentar crearla
            if (!viewExists("vw_detalle_contrato")) {
                statement.executeUpdate("CREATE VIEW vw_detalle_contrato AS "
                        + "SELECT c.id_contrato, e.nombre AS nombre_empleado, e.apellidos AS apellidos_empleado, "
                        + "a.nombre AS nombre_agencia, ec.nombre AS estado_contrato, c.fecha_inicio, c.fecha_fin, c.sueldo "
                        + "FROM CONTRATOS c JOIN EMPLEADO e ON c.id_empleado = e.id_empleado "
                        + "JOIN ESTADO_CONTRATO ec ON c.id_estado_contrato = ec.id_estado_contrato "
                        + "JOIN AGENCIAS a ON c.id_agencia = a.id_agencia;");
            }

            // Verificar y crear la vista vw_detalle_gad_ubicacion
            if (!viewExists("vw_detalle_gad_ubicacion")) {
                statement.executeUpdate("CREATE VIEW vw_detalle_gad_ubicacion AS "
                        + "SELECT g.id_gad, g.nombre AS nombre_gad, t.nombre AS tipo_gad, "
                        + "u.nombre AS nombre_ubicacion, u.estado, u.tipo_ubicacion "
                        + "FROM GAD g JOIN UBICACION u ON g.id_ubicacion = u.id_ubicacion "
                        + "JOIN TIPO t ON g.id_tipo = t.id_tipo;");
            }

            // Verificar y crear la vista vw_empleados_activos
            if (!viewExists("vw_empleados_activos")) {
                statement.executeUpdate("CREATE VIEW vw_empleados_activos AS "
                        + "SELECT e.id_empleado, e.nombre, e.apellidos, c.fecha_inicio AS fecha_inicio_contrato, "
                        + "c.fecha_fin AS fecha_fin_contrato, ec.nombre AS estado_contrato "
                        + "FROM EMPLEADO e JOIN CONTRATOS c ON e.id_empleado = c.id_empleado "
                        + "JOIN ESTADO_CONTRATO ec ON c.id_estado_contrato = ec.id_estado_contrato "
                        + "WHERE c.fecha_fin IS NULL;");
            }

            // Verificar y crear la vista vw_agencias_contacto
            if (!viewExists("vw_agencias_contacto")) {
                statement.executeUpdate("CREATE VIEW vw_agencias_contacto AS "
                        + "SELECT a.id_agencia, a.nombre AS nombre_agencia, a.telefono, "
                        + "g.nombre AS nombre_gad, t.nombre AS tipo_gad "
                        + "FROM AGENCIAS a JOIN GAD g ON a.id_gad = g.id_gad JOIN TIPO t ON g.id_tipo = t.id_tipo;");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear vistas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean viewExists(String viewName) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getTables(null, null, viewName, null)) {
            return resultSet.next();
        }
    }

    private void showData(String query) {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            // Limpiar la tabla antes de mostrar nuevos datos
            tableModel.setRowCount(0);

            // Obtener metadatos para establecer nombres de columnas
            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }
            tableModel.setColumnIdentifiers(columnNames);

            // Mostrar los datos en la tabla
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al ejecutar la consulta", "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(VistaEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        SwingUtilities.invokeLater(() -> {
            VistaEmpleados databaseGUI = new VistaEmpleados();
            databaseGUI.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
