
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String URL = "jdbc:mysql://192.168.1.4:23306/ProyectoU1";
    static final String USER_DBA = "DBA";
    static final String USER_FUNCIONARIO = "funcionario";
    private static final String USER_REPORTES = "reportes";
    private static final String PASSWORD_DBA = "12345";
    private static final String PASSWORD_FUNCIONARIO = "1234";
    private static final String PASSWORD_REPORTES = "123";
    private static final String USER_SYSTEM = "grupogad";
    private static final String PASSWORD_SYSTEM = "gad123";

    private static String usuario_actual;

    public Conexion(Integer usuario) {
        switch (usuario) {
            case 1:
                usuario_actual = USER_DBA;
                break;
            case 2:
                usuario_actual = USER_FUNCIONARIO;
                break;
            case 3:
                usuario_actual = USER_REPORTES;
                break;
            case 4:
                usuario_actual = usuario_actual;
                break;
            case 5:
                usuario_actual = USER_SYSTEM;
                break;
            default:
                usuario_actual = usuario_actual;
                break;
        }
    }

    public static String getusuario_actual() {
        return usuario_actual;
    }

    public static void main(String[] args) {
        Conexion conexionDBA = new Conexion(2);

        Connection conexionDBAObj = conexionDBA.obtenerConexion();

        if (conexionDBAObj != null) {
            System.out.println("Conexiones activadas: ");
        } else {
            System.out.println("No se pudieron activar todas las conexiones.");
        }
    }

    public static String obtenerUsuarioActualDetallado() {
        String usuarioActual = null;
        ManejadorErroresBD manejador = new ManejadorErroresBD();
        try (Connection connection = obtenerConexion();
                Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_USER() AS usuario_actual");
            if (resultSet.next()) {
                usuarioActual = resultSet.getString("usuario_actual");
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el usuario actual: " + ex.getMessage());
            String descripcionError = ex.getMessage();
            manejador.guardarError(descripcionError);
        }

        return usuarioActual;
    }

    public static Connection obtenerConexion() {
        String usuario;
        String password;

        switch (usuario_actual) {
            case USER_DBA:
                usuario = USER_DBA;
                password = PASSWORD_DBA;
                break;
            case USER_FUNCIONARIO:
                usuario = USER_FUNCIONARIO;
                password = PASSWORD_FUNCIONARIO;
                break;
            case USER_REPORTES:
                usuario = USER_REPORTES;
                password = PASSWORD_REPORTES;
                break;
            case USER_SYSTEM:
                usuario = USER_SYSTEM;
                password = PASSWORD_SYSTEM;
                break;

            default:
                usuario = "";
                password = "";
                break;
        }

        ManejadorErroresBD manejador = new ManejadorErroresBD();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, usuario, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
            String descripcionError = ex.getMessage();
            manejador.guardarError(descripcionError);

            return null;
        }
    }

    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            ManejadorErroresBD manejador = new ManejadorErroresBD();
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.getMessage());
                String descripcionError = ex.getMessage();
                manejador.guardarError(descripcionError);
            }
        }
    }
}