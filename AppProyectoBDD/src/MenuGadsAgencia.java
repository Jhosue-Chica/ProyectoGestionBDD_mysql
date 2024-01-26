import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGadsAgencia {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Menu Gads");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal con layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel para los botones con layout GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio a los lados

        // Botones
        JButton btnModificar = createStyledButton("Gestionar Gads");
        JButton btnAgregar = createStyledButton("Gestionar Agencias");
        JButton btnDescargar = createStyledButton("Consultas fecuentes");
        JButton btnRegresar = createStyledButton("Regresar");

        // Agrega ActionListener a cada botón para manejar eventos
        btnModificar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Acción: Modificar Informacion de Empleados");
        });

        btnAgregar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Acción: Agregar Empleados");
        });

        btnDescargar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Acción: Descargar Lista de Empleados");
        });

        btnRegresar.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Acción: Regresar");
        });

        // Agrega botones al panel con GridBagLayout
        buttonPanel.add(btnModificar, gbc);
        gbc.gridy++;
        buttonPanel.add(btnAgregar, gbc);
        gbc.gridy++;
        buttonPanel.add(btnDescargar, gbc);
        gbc.gridy++;
        buttonPanel.add(btnRegresar, gbc);

        // Agrega el panel de botones al panel principal
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Agrega el panel principal al frame
        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(350, 50));
        return button;
    }
}
