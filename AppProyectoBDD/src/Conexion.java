
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://10.41.1.128:23306/ProyectoU1";
    private static final String USER_DBA = "root"; // DBA
    private static final String USER_FUNCIONARIO = "funcionario"; 
    private static final String USER_REPORTES = "reportes";
    private static final String PASSWORD_DBA = "270902"; // 12345
    private static final String PASSWORD_FUNCIONARIO = "1234";
    private static final String PASSWORD_REPORTES = "123";

    public static void main(String[] args) {
        obtenerConexion();
        //SwingUtilities.invokeLater(InterfazEmpleado::new);
        System.out.println("Conexion activada: ");
    }
    // Conexion usuario DBA
    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el controlador de MySQL
            return DriverManager.getConnection(URL, USER_DBA, PASSWORD_DBA);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
            return null;
        }
    }

    // Conexion usuario FUNCIONARIO
    public static Connection obtenerConexion_FUNCINARIO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el controlador de MySQL
            return DriverManager.getConnection(URL, USER_FUNCIONARIO, PASSWORD_FUNCIONARIO);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
            return null;
        }
    }


    // Conexion usuario REPORTES
    public static Connection obtenerConexion_REPORTES() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el controlador de MySQL
            return DriverManager.getConnection(URL, USER_REPORTES, PASSWORD_REPORTES);
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
