
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.mysql.cj.jdbc.CallableStatement;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DocumentFilter;

public class InterfazEmpleado extends javax.swing.JFrame {

    private static int idEmpleadoActual = 1; // Inicializar el contador de ID

    private JTextField txtNombre, txtApellidos, txtDireccion, txtTelefono, txtFechaNacimiento;

    public InterfazEmpleado() {
        setTitle("Añadir Datos de Empleado");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar el aspecto visual con Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear etiquetas y campos de texto
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);

        JLabel lblApellidos = new JLabel("Apellidos:");
        txtApellidos = new JTextField(20);

        JLabel lblDireccion = new JLabel("Dirección:");
        txtDireccion = new JTextField(30);

        JLabel lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField(9); // Limitar el campo a 9 caracteres
        ((AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new LimitDocumentFilter(9));

        JLabel lblFechaNacimiento = new JLabel("Fecha de Nacimiento:");
        txtFechaNacimiento = new JTextField(12);
        configurarCampoFechaNacimiento();

        // Botón para agregar empleado
        JButton btnAgregar = new JButton("Agregar Empleado");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarEmpleado();
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
        panel.add(lblNombre, gbc);

        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblApellidos, gbc);

        gbc.gridx = 1;
        panel.add(txtApellidos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblDireccion, gbc);

        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblTelefono, gbc);

        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblFechaNacimiento, gbc);

        gbc.gridx = 1;
        panel.add(txtFechaNacimiento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(btnRegresar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1; // Restaurar el valor a 1
        panel.add(btnAgregar, gbc);

        // Agregar el panel a la ventana
        add(panel);

        setVisible(true);
    }

    private void configurarCampoFechaNacimiento() {
        // Establecer un formato gris dentro del campo
        txtFechaNacimiento.setForeground(Color.GRAY);
        txtFechaNacimiento.setText("YYYY-MM-DD");

        txtFechaNacimiento.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtFechaNacimiento.getForeground().equals(Color.GRAY)) {
                    txtFechaNacimiento.setText("YYYY-MM-DD");
                    txtFechaNacimiento.setForeground(Color.BLACK);
                }
                txtFechaNacimiento.setCaretPosition(0); // Posicionar el cursor al principio
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtFechaNacimiento.getText().trim().isEmpty()) {
                    txtFechaNacimiento.setForeground(Color.GRAY);
                    txtFechaNacimiento.setText("YYYY-MM-DD");
                }
            }
        });
        // Configuración del KeyListener para aceptar solo números
        txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                } else {
                    handleKeyTyped(c);
                    e.consume();
                }
            }

            private void handleKeyTyped(char c) {
                String text = txtTelefono.getText();
                int caretPosition = txtTelefono.getCaretPosition();

                if (caretPosition < text.length()) {
                    handleDigit(c, caretPosition);
                } else if (text.length() < 9) {
                    txtTelefono.setText(txtTelefono.getText() + c);
                    txtTelefono.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleDigit(char digit, int caretPosition) {
                if (caretPosition < 9) {
                    StringBuilder newText = new StringBuilder(txtTelefono.getText());
                    newText.setCharAt(caretPosition, digit);
                    txtTelefono.setText(newText.toString());
                    txtTelefono.setCaretPosition(caretPosition + 1);
                }
            }
        });

        // Agregar un KeyListener para aceptar solo números y guiones
        txtFechaNacimiento.addKeyListener(new KeyAdapter() {
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
                String text = txtFechaNacimiento.getText();
                int caretPosition = txtFechaNacimiento.getCaretPosition();
                if (c == KeyEvent.VK_BACK_SPACE) {
                    handleBackspace();
                } else if (Character.isDigit(c)) {
                    handleDigit(c, caretPosition);
                } else if (c == KeyEvent.VK_MINUS) {
                    handleMinus(caretPosition);
                }
            }

            private void handleDigit(char digit, int caretPosition) {
                StringBuilder newText = new StringBuilder(txtFechaNacimiento.getText());

                if (caretPosition < 10) {
                    if (caretPosition == 4 || caretPosition == 7) {
                        caretPosition++; // Saltar el guión
                    }

                    newText.setCharAt(caretPosition, digit);
                    txtFechaNacimiento.setText(newText.toString());
                    txtFechaNacimiento.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleMinus(int caretPosition) {
                if (caretPosition == 4 || caretPosition == 7) {
                    txtFechaNacimiento.setCaretPosition(caretPosition + 1);
                }
            }

            private void handleBackspace() {
                int caretPosition = txtFechaNacimiento.getCaretPosition();
                if (caretPosition > 0 && caretPosition != 5 && caretPosition != 8) {
                    txtFechaNacimiento.setCaretPosition(caretPosition - 1);
                }
            }
        });
    }

    // Conectar a la base de datos
    String url = "jdbc:mysql://10.41.1.128:23306/ProyectoU1";
    String usuario = "root";
    String contrasena = "admin";

    private void agregarEmpleado() {

        try {
            // Obtener datos de los campos de texto
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            // Convertir la fecha de nacimiento a un objeto Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNacimiento = dateFormat.parse(txtFechaNacimiento.getText());

            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                // Obtener el próximo ID de empleado
                int nuevoIdEmpleado = obtenerProximoIdEmpleado();

                if (nuevoIdEmpleado == -1) {
                    // Manejar el error según tus necesidades
                    JOptionPane.showMessageDialog(this, "Error al obtener el próximo ID de empleado.");
                    return;
                }


                    try (CallableStatement callableStatement = (CallableStatement) conexion.prepareCall("{call sp_insertar_empleado(?, ?, ?, ?, ?, ?)}")) {
                        // Establecer los valores de los parámetros
                        callableStatement.setInt(1, nuevoIdEmpleado);
                        callableStatement.setString(2, nombre);
                        callableStatement.setString(3, apellidos);
                        callableStatement.setString(4, direccion);
                        callableStatement.setString(5, telefono);
                        callableStatement.setDate(6, new java.sql.Date(fechaNacimiento.getTime()));
                        
                        // Ejecutar el procedimiento almacenado
                        callableStatement.execute();

                    // Muestra un mensaje indicando que se ha agregado el empleado
                    JOptionPane.showMessageDialog(this, "Empleado agregado correctamente");

                    // Limpiar los campos después de agregar el empleado
                    limpiarCampos();
                }
            }
        } catch (SQLException | ParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar empleado. Verifica los datos ingresados.");
            ex.printStackTrace(); // Puedes manejar las excepciones de manera más específica según tus necesidades.
        }
    }

    private int obtenerProximoIdEmpleado() {

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String sql = "SELECT MAX(id_empleado) FROM EMPLEADO";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) + 1;
                    } else {
                        return 1; // Si no hay empleados, empezar desde 1
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejar la excepción según tus necesidades
            return -1; // o lanzar una excepción personalizada
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtFechaNacimiento.setForeground(Color.GRAY);
        txtFechaNacimiento.setText("YYYY-MM-DD");

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
        SwingUtilities.invokeLater(InterfazEmpleado::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

class LimitDocumentFilter extends DocumentFilter {

    private int limit;

    public LimitDocumentFilter(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit can not be <= 0");
        }
        this.limit = limit;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int currentLength = fb.getDocument().getLength();
        int overLimit = (currentLength + text.length()) - limit - length;
        if (overLimit > 0) {
            text = text.substring(0, text.length() - overLimit);
        }
        if (text.length() > 0) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
