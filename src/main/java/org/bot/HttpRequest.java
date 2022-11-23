package org.bot;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;


public class HttpRequest {
    private String firstAddr = "";
    private String secondAddr = "";
    private int distance, duration;
    private CoordinatesProcessor coordinatesProcessor;
    private final String USER_AGENT = "Mozilla/5.0";

    public String addressToCoordinates(String addr) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        return findCoordinates(sendGet(url));
    }

    public String coordinatesToAddress(Coordinates coordinates) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?{0}&fields=items.point&key={1}",
                coordinates.toString(), get2GisGetKey());
        return findAddress(sendGet(url));
    }

    public String createRouteWithAddress(String addr) {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        if (addr == "") {
            TelegramBot.repeatCommand = true;
            return "Введите первый адрес";
        }
        else if (firstAddr == "") {
            firstAddr = addr;
            return "Введите второй адрес";
        }
        else if (secondAddr == "") {
            secondAddr = addr;
            TelegramBot.repeatCommand = false;
        }

        return sendPost(url, firstAddr, secondAddr);
    }

    public String createRouteWithCoordinates(Coordinates coordinates) {
        String addr = coordinatesToAddress(coordinates);
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        return sendPost2(url, firstAddr, addr);
    }

    //этот метод должен сделать sendGet запрос, без возвращаемого значения
    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
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

            String[] firstAddrInCoordinate = addressToCoordinates(firstAddr).split(" ");
            String[] secondAddrInCoordinate = addressToCoordinates(secondAddr).split(" ");


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
            String route = findInformation(response);
            coordinatesProcessor = new CoordinatesProcessor(response.toString());

            distance = Integer.parseInt(route.substring(route.indexOf(':') + 1, route.indexOf(',')));
            duration = Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));

            return route + "\n" + coordinatesProcessor.coordinatesProcess().toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String sendPost2(String url, String firstAddr, String secondAddr) {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String[] firstAddrInCoordinate = addressToCoordinates(firstAddr).split(" ");
            String[] secondAddrInCoordinate = addressToCoordinates(secondAddr).split(" ");

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

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    private String findCoordinates(String response) {
        int firstIdx = response.indexOf("lat");
        int lastIdx = response.indexOf("purpose_name") - 3;
        String substr = response.substring(firstIdx, lastIdx);
        String[] coordinates = substr.split(",");

        return coordinates[0].substring(coordinates[0].indexOf(":") + 1) +
                " " + coordinates[1].substring(coordinates[1].indexOf(":") + 1);
    }

    private String findAddress(String response) {
        int firstIdx = response.indexOf("full_name") + "full_name".length() + 4;
        int lastIdx = response.indexOf("id") - 3;

        return response.substring(firstIdx, lastIdx);
    }

    private String findInformation(StringBuilder response)
    {
        int start = response.indexOf("total_distance");
        int finish = response.indexOf("type", start);

        return response.substring(start - 1, finish - 2);
    }
}