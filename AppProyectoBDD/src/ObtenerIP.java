
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ObtenerIP {

    public static String obtenerIPPublica() {
        try {
            // Obtenemos la dirección IP de la máquina local
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress(); // Devolvemos la dirección IP en formato String
        } catch (UnknownHostException e) {
            // Manejamos la excepción en caso de que no se pueda obtener la dirección IP
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String ip = obtenerIPPublica();
        System.out.println("La dirección IP pública es: " + ip);
    }
}