
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {

    public static void main(String[] args) throws Exception {
        
        Conexion conexion = new Conexion(1);
        // Configurar el aspecto visual con Nimbus
        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception e) {

            e.printStackTrace();
        }

            // Crear una instancia de MenuInterfazGUI
            MenuInterfazGUI menuInterfaz = new MenuInterfazGUI();
            
            // Llamar al método main de MenuInterfazGUI
            menuInterfaz.initUI();

    }
}
