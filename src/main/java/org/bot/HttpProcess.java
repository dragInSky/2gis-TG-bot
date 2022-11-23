package org.bot;

import java.text.MessageFormat;
import java.util.Objects;

public class HttpProcess {
    private String firstAddr = "";
    private String secondAddr = "";
    private int duration;
    private CoordinatesProcessor coordinatesProcessor;
    private final HttpRequest httpRequest = new HttpRequest();

    public String addressToCoordinates(String addr) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        return findCoordinates(httpRequest.sendGet(url));
    }

    public String coordinatesToAddress(Coordinates coordinates) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?{0}&fields=items.point&key={1}",
                coordinates.toString(), get2GisGetKey());
        return findAddress(httpRequest.sendGet(url));
    }

    public String createRouteWithAddress(String addr) {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        if (Objects.equals(addr, "")) {
            TelegramBot.repeatCommand = true;
            return "¬ведите первый адрес";
        }
        else if (Objects.equals(firstAddr, "")) {
            firstAddr = addr;
            return "¬ведите второй адрес";
        }
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            TelegramBot.repeatCommand = false;
        }

        String response = httpRequest.sendPost(url, firstAddr, secondAddr);
        coordinatesProcessor = new CoordinatesProcessor(response);

        String route = findInformation(response);
        duration = Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));

        return route;
    }

    public String createRouteWithCoordinates(Coordinates coordinates) {
        String addr = coordinatesToAddress(coordinates);
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());

        String response = httpRequest.sendPost(url, firstAddr, secondAddr);
        System.out.println(response);

        return findInformation(httpRequest.sendPost(url, firstAddr, addr));
    }

    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
        String[] splittedCoordinates = coordinates.split(" ");
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, splittedCoordinates[0], splittedCoordinates[1]);
        httpRequest.sendGet(url);
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

    private String findInformation(String response)
    {
        int start = response.indexOf("total_distance");
        int finish = response.indexOf("type", start);

        return response.substring(start - 1, finish - 2);
    }

    public String get2GisPostKey() {
        return System.getenv("2GIS_POST_KEY");
    }

    public String get2GisGetKey() {
        return System.getenv("2GIS_GET_KEY");
    }

    public int getDuration() {
        return duration;
    }
}
