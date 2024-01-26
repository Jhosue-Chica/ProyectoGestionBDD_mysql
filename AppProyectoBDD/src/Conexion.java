import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto";
    private static final String USER = "root";
    private static final String PASSWORD = "270902";

    public static void main(String[] args) {
        obtenerConexion();
        //SwingUtilities.invokeLater(InterfazEmpleado::new);
        System.out.println("Conexion activada si: ");
    }

    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el controlador de MySQL
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
            return null;
        }
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
}
