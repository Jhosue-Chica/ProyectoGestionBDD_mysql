
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MenuInt extends javax.swing.JFrame {

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
        buttonPanel.add(salirButton, buttonConstraints);

// ActionListener para el botón "Gestionar Trabajadores"
        button1.addActionListener(e -> {
            // Crear una instancia de MenuEmpleados y mostrarla
            SwingUtilities.invokeLater(() -> {
                MenuEmp menuGAD = new MenuEmp();
                menuGAD.setVisible(true);
            });
        });

// ActionListener para el botón "Gestionar GADs"
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

        // ActionListener para el botón "Salir"
        salirButton.addActionListener(e -> {
            // Cerrar el JFrame actual al hacer clic en el botón
            SwingUtilities.getWindowAncestor(salirButton).dispose();
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
            java.util.logging.Logger.getLogger(MenuInt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuInt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuInt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuInt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menú e Interfaz");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            centerFrameOnScreen(frame);

            // Crear un panel para los botones
            JPanel buttonPanel = createButtonPanel();

            // Establecer una imagen de fondo
            ImageIcon backgroundImage = new ImageIcon("ruta/imagen.jpg");
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setLayout(new BorderLayout());
            backgroundLabel.add(buttonPanel, BorderLayout.CENTER);
            // Hacer visible el marco
            frame.setVisible(true);

        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
