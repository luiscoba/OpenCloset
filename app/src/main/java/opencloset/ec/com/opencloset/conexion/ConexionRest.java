package opencloset.ec.com.opencloset.conexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import opencloset.ec.com.opencloset.excepcion.ConexionHostExcepcion;

public class ConexionRest {
    private String urlStr;

    public ConexionRest(String url) {
        this.urlStr = url;
    }

    public String invocarWS(String requestMethod,String contentType, String accept, String tramaJson) throws ConexionHostExcepcion {

        String r2 = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(requestMethod);

            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept",accept);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(tramaJson);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String respuesta = "";

            while ((respuesta = br.readLine()) != null) {
                r2 = r2 + respuesta;
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al enviar: " + tramaJson);
            throw new ConexionHostExcepcion("Error de conexión al servidor");
        }
        return r2;
    }

    public String invocarWS(String requestMethod,String contentType, String accept) throws ConexionHostExcepcion {

        String r2 = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(requestMethod);

            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept",accept);

/*            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write();
            writer.close();
*/
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(reader);
            String respuesta = "";

            while ((respuesta = br.readLine()) != null) {
                r2 = r2 + respuesta;
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al enviar sin trama " );
            throw new ConexionHostExcepcion("Error de conexión al servidor");
        }
        return r2;
    }
}
