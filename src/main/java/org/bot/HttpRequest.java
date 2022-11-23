package org.bot;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;


public class HttpRequest {

    private String firstAddr = "";

    private String secondAddr = "";
    private final String USER_AGENT = "Mozilla/5.0";

    public String sendGetGeo(String addr) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        return sendGet(url);
    }

    public String sendPostRoute(String addr) {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        if (addr == "") {
            TelegramBot.repeatCommand = true;
            return "¬ведите первый адрес";
        }

        else if (firstAddr == "") {
            firstAddr = addr;
            return "¬ведите второй адрес";
        }

        else if (secondAddr == "") {
            secondAddr = addr;
            TelegramBot.repeatCommand = false;
        }

        return sendPost(url, firstAddr, secondAddr);
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

            Processing.http = new HttpRequest();

            return coordinates[0].substring(coordinates[0].indexOf(":") + 1) +
                    " " + coordinates[1].substring(coordinates[1].indexOf(":") + 1);
        } catch (Exception e) {
            return null;
        }
    }

    private String sendPost(String url, String firstAddr, String secondAddr) {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            Processing processing = new Processing();

            String[] firstAddrInCoordinate = processing.coordinates(firstAddr).split(" ");
            String[] secondAddrInCoordinate = processing.coordinates(secondAddr).split(" ");


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
                       ]
                    }
                    """;
            urlParameters = urlParameters.replace("{0}", firstAddrInCoordinate[0]);
            urlParameters = urlParameters.replace("{1}", firstAddrInCoordinate[1]);
            urlParameters = urlParameters.replace("{2}", secondAddrInCoordinate[0]);
            urlParameters = urlParameters.replace("{3}", secondAddrInCoordinate[1]);



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
}