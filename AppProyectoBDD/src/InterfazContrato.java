
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.mysql.cj.jdbc.CallableStatement;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.util.Date;

public class InterfazContrato extends javax.swing.JFrame {




    private JLabel lblSiguienteIdContrato, lblNombreApellido, lblNombreEmpleado;
    private JTextField txtIdEmpleado, txtFechaInicio, txtFechaFin, txtSueldo;
    private JComboBox<String> cboIdEstadoContrato, cboIdAgencia;
    private JButton btnAgregar;

    public InterfazContrato() {

        setTitle("Agregar Contrato");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear etiquetas y campos de texto
        JLabel lblIdEmpleado = new JLabel("ID Empleado:");
        txtIdEmpleado = new JTextField(10);

        // Añadir un oyente de foco al campo de texto de ID Empleado
        txtIdEmpleado.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                buscarEmpleado();
            }
        });

        // Nueva etiqueta para mostrar "Nombre y Apellido del Empleado:"
        JLabel lblNombreYApellido = new JLabel("Nombre y Apellido:");

        // Etiqueta para mostrar el nombre y apellido del empleado
        lblNombreEmpleado = new JLabel();

        JLabel lblIdEstadoContrato = new JLabel("ID Estado Contrato:");
        cboIdEstadoContrato = new JComboBox<>();
        cboIdEstadoContrato.setEnabled(false); // Deshabilitar por defecto
        mostrarOpcionesEstadoContrato(); // Mostrar opciones al crear la interfaz

        JLabel lblFechaInicio = new JLabel("Fecha Inicio:");
        txtFechaInicio = new JTextField(10);
        configurarCampoFechaInicio();

        JLabel lblFechaFin = new JLabel("Fecha Fin:");
        txtFechaFin = new JTextField(10);
        configurarCampoFechaFin();

        // Mostrar el próximo ID de contrato
        lblSiguienteIdContrato = new JLabel("ID de Contrato: ");
        JLabel lblValorSiguienteIdContrato = new JLabel(String.valueOf(obtenerSiguienteIdContrato()));

        // Mostrar nombre y apellido del empleado
        lblNombreApellido = new JLabel();

        // JComboBox para seleccionar la ID de la agencia
        cboIdAgencia = new JComboBox<>();
        cboIdAgencia.setEnabled(false);
        mostrarOpcionesIdAgencia();

        JLabel lblSueldo = new JLabel("Sueldo:");
        txtSueldo = new JTextField(12);
        configurarCampoSueldo();

        // Botón para agregar contrato
        btnAgregar = new JButton("Agregar Contrato");
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarContrato();
            }
        });

        // Botón para regresar
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Puedes agregar lógica para regresar a la pantalla anterior
                // En este ejemplo, simplemente cerramos la ventana
                dispose();
            }
        });

        // Obtener la resolución de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Calcular la posición para centrar la ventana
        int xPos = (screenSize.width - getWidth()) / 2;
        int yPos = (screenSize.height - getHeight()) / 2;

        // Establecer la posición de la ventana
        setLocation(xPos, yPos);

        // Crear un panel y agregar componentes
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblSiguienteIdContrato, gbc);

        // Agregar el nuevo JLabel al panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblSiguienteIdContrato, gbc);

        gbc.gridx = 1;
        panel.add(lblValorSiguienteIdContrato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblIdEmpleado, gbc);

        gbc.gridx = 1;
        panel.add(txtIdEmpleado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblNombreYApellido, gbc);

        gbc.gridx = 1;
        panel.add(lblNombreEmpleado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblIdEstadoContrato, gbc);

        gbc.gridx = 1;
        panel.add(cboIdEstadoContrato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblFechaInicio, gbc);

        gbc.gridx = 1;
        panel.add(txtFechaInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblFechaFin, gbc);

        gbc.gridx = 1;
        panel.add(txtFechaFin, gbc);

        // Nueva fila para "ID Agencia"
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("ID Agencia:"), gbc);

        gbc.gridx = 1;
        panel.add(cboIdAgencia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblSueldo, gbc);

        gbc.gridx = 1;
        panel.add(txtSueldo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(btnRegresar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(btnAgregar, gbc);

        // Agregar el panel a la ventana
        add(panel);

        setVisible(true);

    }

    private void configurarCampoFechaFin() {
        // Establecer un formato gris dentro del campo
        txtFechaFin.setForeground(Color.GRAY);
        txtFechaFin.setText("YYYY-MM-DD");

        txtFechaFin.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtFechaFin.getForeground().equals(Color.GRAY)) {
                    txtFechaFin.setText("YYYY-MM-DD");
                    txtFechaFin.setForeground(Color.BLACK);
                }
                txtFechaFin.setCaretPosition(0); // Posicionar el cursor al principio
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtFechaFin.getText().trim().isEmpty()) {
                    txtFechaFin.setForeground(Color.GRAY);
                    txtFechaFin.setText("YYYY-MM-DD");
                }
            }

        });

        // Desactivar la tecla "Suprimir" en txtFechaFin
        ((AbstractDocument) txtFechaFin.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                // No permitir eliminar caracteres
            }
        });

        // Agregar un KeyListener para aceptar solo números y guiones
        txtFechaFin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_MINUS || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                } else {
                    handleKeyTyped(c);
                    e.consume();
                }
            }

            private void handleKeyTyped(char c) {
                String text = txtFechaFin.getText();
                int caretPosition = txtFechaFin.getCaretPosition();
                if (c == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                } else if (Character.isDigit(c)) {
                    handleDigit(c, caretPosition);
                } else if (c == KeyEvent.VK_MINUS) {
                    handleMinus(caretPosition);
                }
            }

            private void handleDigit(char digit, int caretPosition) {
                StringBuilder newText = new StringBuilder(txtFechaFin.getText());

                if (caretPosition < 10) {
                    if (caretPosition == 4 || caretPosition == 7) {
                        caretPosition++; // Saltar el guión
                    }

                    newText.setCharAt(caretPosition, digit);
                    txtFechaFin.setText(newText.toString());
                    txtFechaFin.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleMinus(int caretPosition) {
                if (caretPosition == 4 || caretPosition == 7) {
                    txtFechaFin.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleBackspace() {
                int caretPosition = txtFechaFin.getCaretPosition();
                if (caretPosition > 0 && caretPosition != 5 && caretPosition != 8) {
                    txtFechaFin.setCaretPosition(caretPosition - 1);
                }
            }
        });
    }

    private void configurarCampoSueldo() {
        // Agregar un KeyListener para aceptar solo números, comas y puntos
        txtSueldo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_COMMA || c == KeyEvent.VK_PERIOD || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                } else {
                    handleKeyTyped(c);
                    e.consume();
                }
            }

            private void handleKeyTyped(char c) {
                String text = txtSueldo.getText();
                if (c == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                } else if (Character.isDigit(c)) {
                    handleDigit(c);
                } else if (c == KeyEvent.VK_COMMA) {
                    handleComma();
                } else if (c == KeyEvent.VK_PERIOD) {
                    handlePeriod();
                }
            }

            private void handleDigit(char digit) {
                txtSueldo.setText(txtSueldo.getText() + digit);
            }

            private void handleComma() {
                txtSueldo.setText(txtSueldo.getText() + ".");
            }

            private void handlePeriod() {
                txtSueldo.setText(txtSueldo.getText() + ".");
            }

            private void handleBackspace() {
                String text = txtSueldo.getText();
                if (!text.isEmpty()) {
                    txtSueldo.setText(text.substring(0, text.length() - 1));
                }
            }
        });
    }

    // Fecha fin restricciones
    private void configurarCampoFechaInicio() {
        // Establecer un formato gris dentro del campo
        txtFechaInicio.setForeground(Color.GRAY);
        txtFechaInicio.setText("YYYY-MM-DD");

        txtFechaInicio.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtFechaInicio.getForeground().equals(Color.GRAY)) {
                    txtFechaInicio.setText("YYYY-MM-DD");
                    txtFechaInicio.setForeground(Color.BLACK);
                }
                txtFechaInicio.setCaretPosition(0); // Posicionar el cursor al principio
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtFechaInicio.getText().trim().isEmpty()) {
                    txtFechaInicio.setForeground(Color.GRAY);
                    txtFechaInicio.setText("YYYY-MM-DD");
                }
            }
        });

        // Desactivar la tecla "Suprimir" en txtFechaInicio
        ((AbstractDocument) txtFechaInicio.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                // No permitir eliminar caracteres
            }
        });

        // Agregar un KeyListener para aceptar solo números y guiones
        txtFechaInicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_MINUS || c == KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                } else {
                    handleKeyTyped(c);
                    e.consume();
                }
            }

            private void handleKeyTyped(char c) {
                String text = txtFechaInicio.getText();
                int caretPosition = txtFechaInicio.getCaretPosition();
                if (c == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                } else if (Character.isDigit(c)) {
                    handleDigit(c, caretPosition);
                } else if (c == KeyEvent.VK_MINUS) {
                    handleMinus(caretPosition);
                }
            }

            private void handleDigit(char digit, int caretPosition) {
                StringBuilder newText = new StringBuilder(txtFechaInicio.getText());

                if (caretPosition < 10) {
                    if (caretPosition == 4 || caretPosition == 7) {
                        caretPosition++; // Saltar el guión
                    }

                    newText.setCharAt(caretPosition, digit);
                    txtFechaInicio.setText(newText.toString());
                    txtFechaInicio.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleMinus(int caretPosition) {
                if (caretPosition == 4 || caretPosition == 7) {
                    txtFechaInicio.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleBackspace() {
                int caretPosition = txtFechaInicio.getCaretPosition();
                if (caretPosition > 0 && caretPosition != 5 && caretPosition != 8) {
                    txtFechaInicio.setCaretPosition(caretPosition - 1);
                }
            }
        });
    }

    private void buscarEmpleado() {
        // Lógica para buscar y mostrar el nombre y apellido del empleado
        // Utiliza el lblNombreEmpleado para mostrar la información
        try {
            Connection conexion = Conexion.obtenerConexion();

            String sql = "SELECT nombre, apellidos FROM EMPLEADO WHERE id_empleado = ?";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                int idEmpleado = Integer.parseInt(txtIdEmpleado.getText());
                preparedStatement.setInt(1, idEmpleado);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String nombre = resultSet.getString("nombre");
                        String apellido = resultSet.getString("apellidos");
                        lblNombreEmpleado.setText(nombre + " " + apellido);
                    } else {
                        // Limpiar la etiqueta si no se encuentra el empleado
                        lblNombreEmpleado.setText("");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejar la excepción según tus necesidades
        }
    }

    private void mostrarOpcionesEstadoContrato() {
        // Establecer automáticamente el estado del contrato como "activo"
        cboIdEstadoContrato.addItem("1 - Activo");
        cboIdEstadoContrato.setSelectedIndex(0); // Seleccionar automáticamente el estado "activo"

    }

    private void mostrarOpcionesIdAgencia() {
        // Lógica para obtener las opciones de la tabla AGENCIA
        // y mostrarlas en el JComboBox cboIdAgencia
        // Puedes utilizar consultas SQL y la conexión a la base de datos
        try {
            Connection conexion = Conexion.obtenerConexion();

            String sql = "SELECT id_agencia, nombre FROM AGENCIAS";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
                cboIdAgencia.removeAllItems(); // Limpiar items existentes
                while (resultSet.next()) {
                    int idAgencia = resultSet.getInt("id_agencia");
                    String nombreAgencia = resultSet.getString("nombre");
                    cboIdAgencia.addItem(idAgencia + " - " + nombreAgencia);
                }
                cboIdAgencia.setEnabled(true); // Habilitar el JComboBox
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejar la excepción según tus necesidades
        }
    }

    private void agregarContrato() {
        try {
            // Obtener datos de los campos de texto y ComboBox
            int idEmpleado = Integer.parseInt(txtIdEmpleado.getText());
            int idEstadoContrato = Integer.parseInt(cboIdEstadoContrato.getSelectedItem().toString().split(" - ")[0]);
            int idAgencia = Integer.parseInt(cboIdAgencia.getSelectedItem().toString().split(" - ")[0]);
    
            // Convertir las fechas a objetos Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicio = dateFormat.parse(txtFechaInicio.getText());
            Date fechaFin = dateFormat.parse(txtFechaFin.getText());
    
            // Convertir el sueldo a un valor decimal
            double sueldo = Double.parseDouble(txtSueldo.getText());
    
            try (Connection conexion = Conexion.obtenerConexion()) {
                // Llamar al procedimiento almacenado
                String sql = "{CALL sp_insertar_contrato(?, ?, ?, ?, ?, ?, ?)}";
    
                try (CallableStatement callableStatement = (CallableStatement) conexion.prepareCall(sql)) {
                    // Establecer los valores de los parámetros
                    callableStatement.setInt(1, idEmpleado);
                    callableStatement.setInt(2, idEstadoContrato);
                    callableStatement.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                    callableStatement.setDate(4, new java.sql.Date(fechaFin.getTime()));
                    callableStatement.setInt(5, idAgencia);
                    callableStatement.setDouble(6, sueldo);
                    callableStatement.setString(7, ObtenerIP.obtenerIPPublica()); // Ejemplo de dirección IP
    
                    // Ejecutar el procedimiento almacenado
                    boolean resultado = callableStatement.execute();
    
                    
    
                    // Muestra un mensaje indicando que se ha agregado el contrato
                    JOptionPane.showMessageDialog(this, "Contrato agregado correctamente.");
    
                    // Actualizar el próximo ID de contrato después de agregar
                    lblSiguienteIdContrato.setText("Siguiente ID de Contrato: " + obtenerProximoIdContrato());
    
                    // Limpiar los campos después de agregar el contrato
                    limpiarCamposContrato();
                }
            }
        } catch (SQLException | ParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar contrato. Verifica los datos ingresados.");
            ex.printStackTrace(); // Puedes manejar las excepciones de manera más específica según tus necesidades.
        }
    }
    
    
    private int obtenerProximoIdContrato() {
        try (Connection conexion = Conexion.obtenerConexion()) {
            String sql = "SELECT MAX(id_contrato) FROM CONTRATOS";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) + 1;
                    } else {
                        return 1; // Si no hay contratos, empezar desde 1
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejar la excepción según tus necesidades
            return -1; // o lanzar una excepción personalizada
        }
    }

    private void limpiarCamposContrato() {
        // Limpiar los campos después de agregar un contrato
        txtIdEmpleado.setText("");
        txtFechaInicio.setText("YYYY-MM-DD");
        txtFechaInicio.setForeground(Color.GRAY);
        txtFechaFin.setText("YYYY-MM-DD");
        txtFechaFin.setForeground(Color.GRAY);
        cboIdEstadoContrato.setSelectedIndex(0);
        cboIdAgencia.setSelectedIndex(0);
        txtSueldo.setText("");
    }

    private int obtenerSiguienteIdContrato() {
        try (Connection conexion = Conexion.obtenerConexion()) {
            String sql = "SELECT MAX(id_contrato) FROM CONTRATOS";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) + 1;
                    } else {
                        return 1; // Si no hay contratos, empezar desde 1
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejar la excepción según tus necesidades
            return -1; // o lanzar una excepción personalizada
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
        SwingUtilities.invokeLater(InterfazContrato::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
