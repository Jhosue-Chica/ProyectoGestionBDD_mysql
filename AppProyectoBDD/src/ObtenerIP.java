import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ObtenerIP {

    public static String obtenerIPPublica() {
        try {
            URL url = new URL("https://httpbin.org/ip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }

            reader.close();
            connection.disconnect();

            // Parsear la respuesta JSON para obtener la dirección IP pública
            String ipAddress = content.toString().split("\"")[3];
            return ipAddress;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Manejar el error según sea necesario en tu aplicación
        }
    }
}
