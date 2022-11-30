package tgbot.Http;

import tgbot.Coordinates;
import tgbot.Exceptions.HttpException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    private final String USER_AGENT = "Mozilla/5.0";

    // HTTP GET request
    public String sendGet(String url) throws HttpException {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            throw new HttpException(e.getClass().getName());
        }
    }

    public String sendPost(String url, Coordinates firstCoordinates, Coordinates secondCoordinates) throws HttpException {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = """
                    {
                       "points": [
                           {
                               "type": "walking",
                               "x": {0},
                               "y": {1}
                           },
                           {
                               "type": "walking",
                               "x": {2},
                               "y": {3}
                           }
                       ],
                       "type": "pedestrian",
                       "output": "full"
                    }
                    """;
            urlParameters = urlParameters.replace("{0}", firstCoordinates.getLon() + "");
            urlParameters = urlParameters.replace("{1}", firstCoordinates.getLat() + "");
            urlParameters = urlParameters.replace("{2}", secondCoordinates.getLon() + "");
            urlParameters = urlParameters.replace("{3}", secondCoordinates.getLat() + "");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            throw new HttpException(e.getClass().getName());
        }
    }
}