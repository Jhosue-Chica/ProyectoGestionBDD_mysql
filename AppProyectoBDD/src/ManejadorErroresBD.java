import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class ManejadorErroresBD {
    Conexion conexion1 = new Conexion(4);
    private Conexion conexion;

    public ManejadorErroresBD() {
        this.conexion = conexion1;
    }

    public void guardarError(String descripcionError) {
        if (conexion1.getusuario_actual() != "DBA" && conexion1.getusuario_actual() != "reportes"
                && conexion1.getusuario_actual() != "grupogad" && conexion1.getusuario_actual() != "funcionario") {
            conexion = new Conexion(5);
        }

        Connection conn = conexion.obtenerConexion();
        if (conn != null) {
            String ipOrigen = ObtenerIP.obtenerIPPublica(); // Obtener la IP pública
            String usuarioActual = Conexion.obtenerUsuarioActualDetallado(); // Obtener el usuario actual
            try {
                // Llamar al procedimiento almacenado para guardar el error
                String call = "{call registrar_error(?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(call);
                statement.setString(1, descripcionError);
                statement.setString(2, ipOrigen);
                statement.setString(3, usuarioActual);
                statement.execute();
            } catch (SQLException e) {
                // Si ocurre un error al guardar en la base de datos, imprimirlo en la consola
                // e.printStackTrace();
                Conexion conexionAdmin = new Conexion(5);
                Connection conn1 = conexionAdmin.obtenerConexion();
                try {
                    String call1 = "{call registrar_error(?, ?, ?)}";
                    CallableStatement statement1 = conn1.prepareCall(call1);
                    String descripcionError1 = e.getMessage();
                    statement1.setString(1, descripcionError1);
                    statement1.setString(2, ipOrigen);
                    statement1.setString(3, usuarioActual);
                    statement1.execute();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                conexion.cerrarConexion(conn);
            }
        }
    }

    public static void main(String[] args) {
        Conexion conexion = new Conexion(1);
        ManejadorErroresBD manejador = new ManejadorErroresBD();
        Connection conn = conexion.obtenerConexion();
        try {
            // Código que puede lanzar una excepción
                String query = "INSERT INTO ESTADO_CONTRATO (id_estado_contrato, nombre) VALUES (?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, "1");
                statement.setString(2, "EEC-1");
                statement.executeUpdate();
    } catch (SQLException e) {
            // En caso de error, guardar el error en la tabla errores_log
            String descripcionError = e.getMessage();
            manejador.guardarError(descripcionError);
        }
    }
}