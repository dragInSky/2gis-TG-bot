package org.bot;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

public class HttpRequest {
    private final String USER_AGENT = "Mozilla/5.0";

    public String sendGetGeo(String addr) { //метод для конвертации адреса к координатам
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        return sendGet(url);
    }

    public String sendGetGeo(Coordinates coordinates) { //перегрузка для конвертации Coordinates к адресу
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                coordinates.toString(), get2GisGetKey());
        return sendGet(url);
    }

    public String sendPostRoute() {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        return sendPost(url);
    }

    public void mapDisplay(String token, String id, String coordinates) {
        String[] splittedCoordinates = coordinates.split(" ");
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, splittedCoordinates[0], splittedCoordinates[1]);
        sendGet(url);
    }

    // HTTP GET request
    private String sendGet(String url) {
        try {
            URL obj = new URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

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

            int firstIdx = response.indexOf("lat");
            int lastIdx = response.indexOf("purpose_name") - 3;
            String substr = response.substring(firstIdx, lastIdx);
            String[] coordinates = substr.split(",");

            return coordinates[0].substring(coordinates[0].indexOf(":") + 1) +
                    " " + coordinates[1].substring(coordinates[1].indexOf(":") + 1);
        } catch (Exception e) {
            return null;
        }
    }

    private String sendPost(String url) {
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
                               "x": 82.93057,
                               "y": 54.943207
                           },
                           {
                               "type": "walking",
                               "x": 82.945039,
                               "y": 55.033879
                           }
                       ]
                    }
                    """;

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
            return findInformation(response);
        } catch (Exception e) {
            return null;
        }
    }

    public String get2GisPostKey() {
        return System.getenv("2GIS_POST_KEY");
    }

    public String get2GisGetKey() {
        return System.getenv("2GIS_GET_KEY");
    }

    private String findInformation(StringBuilder response)
    {
        int start = response.indexOf("total_distance");
        int finish = response.indexOf("type", start);

        return response.substring(start - 1, finish - 2);
    }

    private ArrayList<Coordinates> coordinatesArray(String route) {
        System.out.println(route);
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        int idx = 0;
        while (true) {
            int startIdx = route.indexOf("LINESTRING(", idx);
            int endIdx = route.indexOf(")", startIdx);
            if (startIdx == -1 || endIdx == -1) {
                break;
            }
            String substr = route.substring(startIdx, endIdx);
            String[] strArr = substr.split("[ ,]");
            for (int i = 0; i + 1 < strArr.length; i += 2) {
                try {
                    coordinates.add(new Coordinates(Double.parseDouble(strArr[i + 1]), Double.parseDouble(strArr[i])));
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }

        return coordinates;
    }
}