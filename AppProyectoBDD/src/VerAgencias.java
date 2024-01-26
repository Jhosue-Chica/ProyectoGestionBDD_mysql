import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VerAgencias extends javax.swing.JFrame {

    private JTable tabla;
    private JButton botonRegresar;
    private JButton botonActualizar;
    private AgenciaTableModel modeloTabla;

    public VerAgencias() {
        super("Lista de Agencias");

        try {
            modeloTabla = new AgenciaTableModel();
            tabla = new JTable(modeloTabla);

            setLayout(new BorderLayout());
            add(new JScrollPane(tabla), BorderLayout.CENTER);

            botonActualizar = new JButton("Actualizar Datos");
            botonActualizar.addActionListener(this::mostrarVentanaActualizar);
            botonRegresar = new JButton("Regresar");
            botonRegresar.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                MenuGADs menuGAD = new MenuGADs();
                menuGAD.setVisible(true);
            });
            });

            JPanel panelBotones = new JPanel();
            panelBotones.add(botonActualizar);
            panelBotones.add(botonRegresar);

            add(panelBotones, BorderLayout.SOUTH);

            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);

            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        modeloTabla.clear();

        try {
            Connection conexion = Conexion.obtenerConexion();
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM AGENCIAS");

            while (resultSet.next()) {
                int idAgencia = resultSet.getInt("id_agencia");
                String nombre = resultSet.getString("nombre");
                int idGAD = resultSet.getInt("id_gad");
                Date fechaCreacion = resultSet.getDate("fecha_creacion");
                String telefono = resultSet.getString("telefono");

                modeloTabla.addAgencia(new Agencia(idAgencia, nombre, idGAD, fechaCreacion, telefono));
            }

            resultSet.close();
            statement.close();
            conexion.close();

            modeloTabla.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarVentanaActualizar(ActionEvent event) {
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada != -1) {
            Agencia agenciaSeleccionada = modeloTabla.getAgencia(filaSeleccionada);
            SwingUtilities.invokeLater(() -> new VentanaActualizarAgencia(agenciaSeleccionada, this));
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una Agencia para actualizar sus datos.");
        }
    }

    public void actualizarTabla() {
        cargarDatos();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VerAgencias::new);
    }
}

class Agencia {
    private int idAgencia;
    private String nombre;
    private int idGAD;
    private Date fechaCreacion;
    private String telefono;

    public Agencia(int idAgencia, String nombre, int idGAD, Date fechaCreacion, String telefono) {
        this.idAgencia = idAgencia;
        this.nombre = nombre;
        this.idGAD = idGAD;
        this.fechaCreacion = fechaCreacion;
        this.telefono = telefono;
    }

    public int getIdAgencia() {
        return idAgencia;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdGAD() {
        return idGAD;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public String getTelefono() {
        return telefono;
    }
}

class AgenciaTableModel extends AbstractTableModel {
    private List<Agencia> agencias;
    private String[] columnNames = {"ID Agencia", "Nombre", "ID GAD", "Fecha de Creación", "Teléfono"};

    public AgenciaTableModel() {
        this.agencias = new ArrayList<>();
    }

    public void addAgencia(Agencia agencia) {
        agencias.add(agencia);
        fireTableRowsInserted(agencias.size() - 1, agencias.size() - 1);
    }

    public void removeAgencia(int rowIndex) {
        agencias.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Agencia getAgencia(int rowIndex) {
        return agencias.get(rowIndex);
    }

    public void clear() {
        agencias.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return agencias.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agencia agencia = agencias.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return agencia.getIdAgencia();
            case 1:
                return agencia.getNombre();
            case 2:
                return agencia.getIdGAD();
            case 3:
                return agencia.getFechaCreacion();
            case 4:
                return agencia.getTelefono();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

class VentanaActualizarAgencia extends JFrame {
    private JTextField campoNombre;
    private JTextField campoIdGAD;
    private JTextField campoFechaCreacion;
    private JTextField campoTelefono;
    private JButton botonGuardar;
    private Agencia agencia;
    private VerAgencias ventanaPrincipal;

    public VentanaActualizarAgencia(Agencia agenciaSeleccionada, VerAgencias verAgencias) {
        super("Actualizar Datos de la Agencia");

        this.agencia = agenciaSeleccionada;
        this.ventanaPrincipal = verAgencias;

        setLayout(new GridLayout(7, 2));

        add(new JLabel("ID Agencia:"));
        add(new JLabel(String.valueOf(agencia.getIdAgencia())));

        add(new JLabel("Nombre:"));
        campoNombre = new JTextField(agencia.getNombre());
        add(campoNombre);

        add(new JLabel("ID GAD:"));
        campoIdGAD = new JTextField(String.valueOf(agencia.getIdGAD()));
        add(campoIdGAD);

        add(new JLabel("Fecha de Creación (YYYY-MM-DD):"));
        campoFechaCreacion = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(agencia.getFechaCreacion()));
        add(campoFechaCreacion);

        add(new JLabel("Teléfono:"));
        campoTelefono = new JTextField(agencia.getTelefono());
        add(campoTelefono);

        botonGuardar = new JButton("Guardar Cambios");
        botonGuardar.addActionListener(e -> guardarCambios());
        add(botonGuardar);

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ventanaPrincipal);
        setVisible(true);
    }

    private void guardarCambios() {
        try {
            String url = "jdbc:sqlserver://DESKTOP-GSCMPF5\\MSSQLSERVER_DEV;database=PROYECTO FINAL";
            String usuario = "sa";
            String contrasena = "270902";

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                String consulta = "UPDATE AGENCIAS SET nombre = ?, id_gad = ?, fecha_creacion = ?, telefono = ? WHERE id_agencia = ?";
                try (PreparedStatement preparedStatement = conexion.prepareStatement(consulta)) {
                    preparedStatement.setString(1, campoNombre.getText());
                    preparedStatement.setInt(2, Integer.parseInt(campoIdGAD.getText()));
                    preparedStatement.setDate(3, Date.valueOf(campoFechaCreacion.getText()));
                    preparedStatement.setString(4, campoTelefono.getText());
                    preparedStatement.setInt(5, agencia.getIdAgencia());

                    int filasActualizadas = preparedStatement.executeUpdate();

                    if (filasActualizadas > 0) {
                        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
                        ventanaPrincipal.actualizarTabla();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo guardar los cambios. Inténtalo nuevamente.");
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
            java.util.logging.Logger.getLogger(VerAgencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerAgencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerAgencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerAgencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        SwingUtilities.invokeLater(VerAgencias::new);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
