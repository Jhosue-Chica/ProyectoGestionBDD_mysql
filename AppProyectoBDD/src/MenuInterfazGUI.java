import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInterfazGUI {

    public void initUI() {
        JFrame frame = new JFrame("Menú e Interfaz");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerFrameOnScreen(frame);

        // Crear un panel principal para la interfaz
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un panel para los botones
        JPanel buttonPanel = createButtonPanel();

        // Crear una lista desplegable para los usuarios
        String[] usuarios = {"Usuario DBA", "Funcionario", "Reportes"}; // usuarios
        JComboBox<String> usuariosComboBox = new JComboBox<>(usuarios);
        usuariosComboBox.setPreferredSize(new Dimension(10, 50)); // Establece el tamaño preferido
        usuariosComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioSeleccionado = (String) usuariosComboBox.getSelectedItem();
                // Determinar qué función de conexión llamar dependiendo del usuario seleccionado
                switch (usuarioSeleccionado) {
                    case "Usuario DBA":
                        Conexion conexionDBA = new Conexion(1);
                       
                        break;
                    case "Funcionario":
                        Conexion conexionFuncionario = new Conexion(2);
                       
                        break;
                    case "Reportes":
                        Conexion conexionReportes = new Conexion(3);
                        
                        break;
                }
            }
        });
        // Agregar el panel de botones y la lista desplegable al panel principal
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(usuariosComboBox, BorderLayout.AFTER_LAST_LINE);



        // Establecer una imagen de fondo
        ImageIcon backgroundImage = new ImageIcon("ruta/imagen.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(mainPanel, BorderLayout.CENTER);

        frame.setContentPane(backgroundLabel);

        // Hacer visible el marco
        frame.setVisible(true);
    }

    // Método para centrar la ventana en la pantalla
    private static void centerFrameOnScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    // Método para crear y configurar el panel de botones
    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());

        // Configurar fuente y tamaño para los botones
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // Crear y configurar los botones
        JButton button1 = createButton("Gestionar Trabajadores", buttonFont);
        JButton button2 = createButton("Gestionar GADs", buttonFont);
        JButton button3 = createButton("Gestionar Contratos", buttonFont);
        JButton salirButton = createButton("Salir", buttonFont);

        // Crear un panel para el título
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("Menu Principal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        // Agregar el panel del título al panel de botones
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        buttonPanel.add(titlePanel, titleConstraints);

        // Agregar los botones al panel con restricciones y mayores márgenes
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.insets = new Insets(10, 10, 10, 10); // Ajusta estos valores según tu preferencia

        buttonPanel.add(button1, buttonConstraints);

        buttonConstraints.gridy = 2;
        buttonPanel.add(button2, buttonConstraints);

        buttonConstraints.gridy = 3;
        buttonPanel.add(button3, buttonConstraints);

        buttonConstraints.gridy = 4;
        buttonPanel.add(salirButton, buttonConstraints);

        // ActionListener para el botón "Gestionar Trabajadores"
        button1.addActionListener(e -> {
            // Crear una instancia de MenuEmpleados y mostrarla
            SwingUtilities.invokeLater(() -> {
                // Cerrar el JFrame actual al hacer clic en el botón
                SwingUtilities.getWindowAncestor(button1).dispose();
                MenuEmp menuGAD = new MenuEmp();
                menuGAD.setVisible(true);
            });
        });

        // ActionListener para el botón "Gestionar GADs"
        button2.addActionListener(e -> {
            // Crear una instancia de MenuGAD y mostrarla
            SwingUtilities.invokeLater(() -> {
                MenuGADs menuGAD = new MenuGADs();
                menuGAD.setVisible(true);
            });

            // Cerrar el JFrame actual al hacer clic en el botón
            SwingUtilities.getWindowAncestor(button2).dispose();
        });

        // ActionListener para el botón "Gestionar Contratos"
        button3.addActionListener(e -> {
            // Crear una instancia de MenuGAD y mostrarla
            SwingUtilities.invokeLater(() -> {
                MenuCon menuGAD = new MenuCon();
                menuGAD.setVisible(true);
            });

            // Cerrar el JFrame actual al hacer clic en el botón
            SwingUtilities.getWindowAncestor(button3).dispose();
        });

        // ActionListener para el botón "Salir"
        salirButton.addActionListener(e -> {
            // Cerrar el JFrame actual al hacer clic en el botón
            System.exit(0); // Detiene la ejecución del programa con estado de terminación exitoso (0)
        });

        return buttonPanel;
    }

    // Método para crear y configurar un botón
    private static JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(font);

        // Establecer color de fondo y borde para los botones
        Color buttonColor = new Color(255, 255, 255, 200);
        button.setBackground(buttonColor);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return button;
    }
}
