package org.bot;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

public class HttpProcess {
    private static String firstAddr = "";
    private static String secondAddr = "";
    private static int duration;
    private static boolean repeatCommand = false;

    //private static boolean button = false;
    private final HttpRequest httpRequest = new HttpRequest();

    public static boolean getRepeatCommand(){
        return repeatCommand;
    }

    //public static boolean getButton(){return button;}

    public String addressToCoordinates(String addr) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (response == null) {
            return "error";
        }
        if (wrongRequest(response)) {
            return "¬веден некорректный адрес: " + addr;
        }
        return findCoordinates(response);
    }

    public String createRouteWithAddress(String addr) {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите первый адрес";
        }
        else if (Objects.equals(firstAddr, "")) {
            firstAddr = addr;
            return "¬ведите второй адрес";
        }
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            repeatCommand = false;
        }

        if (Objects.equals(firstAddr, secondAddr)) {
            firstAddr = "";
            secondAddr = "";
            return "¬ведите разные адреса!";
        }

        String firstCoordinates = addressToCoordinates(firstAddr);
        if (firstCoordinates.equals("error")) {
            return "error";
        }
        if (firstCoordinates.equals("¬веден некорректный адрес: " + addr)) {
            return "¬веден некорректный адрес: " + addr;
        }

        String secondCoordinates = addressToCoordinates(secondAddr);
        if (secondCoordinates.equals("error")) {
            return "error";
        }
        if (secondCoordinates.equals("¬веден некорректный адрес: " + addr)) {
            return "¬веден некорректный адрес: " + addr;
        }

        String[] firstAddrInCoordinate = firstCoordinates.split(" ");
        String[] secondAddrInCoordinate = secondCoordinates.split(" ");
        System.out.println(url);
        String response = httpRequest.sendPost(url, firstAddrInCoordinate, secondAddrInCoordinate);
        System.out.println(Arrays.toString(firstAddrInCoordinate));
        System.out.println(Arrays.toString(secondAddrInCoordinate));
        if (response == null) {
            return "error";
        }
        if (wrongRequest(response)) {
            return "¬веден некорректный адрес: " + addr;
        }
        String route = findRouteInformation(response);

        duration = Integer.parseInt(route.substring(route.lastIndexOf(':') + 2, route.lastIndexOf(' ')));
        firstAddr = "";
        secondAddr = "";
        //штука дл€ поиска средней точки
        //Coordinates middleCoordinate = new CoordinatesProcessor(response).coordinatesProcess();

        return route; //+ "\nMiddle point of route: " + middleCoordinate.toString(); - вывод средней точки
    }

    public String createRouteWithCoordinates(Coordinates coordinates) { //штука дл€ поиска средней точки
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
                get2GisPostKey());

        String[] firstAddrInCoordinate = addressToCoordinates(firstAddr).split(" ");
        String response = httpRequest.sendPost(url, firstAddrInCoordinate, coordinates.toString().split(" "));
        if (response == null) {
            return "error";
        }
        return findRouteInformation(response);
    }

    public String mapDisplay(String token, String id, String addr) {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите адрес";
        }
        else{
            repeatCommand = false;
        }

        String coordinates = addressToCoordinates(addr);
        if (coordinates.equals("error")) {
            return "error";
        }
        if (coordinates.equals("¬веден некорректный адрес: " + addr)) {
            return "¬веден некорректный адрес: " + addr;
        }
        String[] splittedCoordinates = coordinates.split(" ");
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, splittedCoordinates[0], splittedCoordinates[1]);
        String response = httpRequest.sendGet(url);
        if (response == null) {
            return "error";
        }
        return "";
    }

    public String addrInfo(String addr) {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите адрес";
        }
        else{
            repeatCommand = false;
        }
        String buildId = buildingId(addr);
        if (buildId.equals("error")) {
            return "error";
        }
        if (buildId.equals("¬веден некорректный адрес: " + addr)) {
            return "¬веден некорректный адрес: " + addr;
        }
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(addr), get2GisGetKey());

        String response = httpRequest.sendGet(url);
        if (response == null) {
            return "error";
        }
        if (wrongRequest(response)) {
            return "¬веден некорректный адрес: " + addr;
        }
        return findCompanies(response);
    }

    private String buildingId(String addr) {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=building&key={1}",
                addr, get2GisGetKey());

        String response = httpRequest.sendGet(url);
        if (response == null) {
            return "error";
        }
        if (wrongRequest(response)) {
            return "¬веден некорректный адрес: " + addr;
        }
        return findBuildingId(response);
    }

    private boolean wrongRequest(String response) {
        return response.contains("code\":400") || response.contains("code\":404");
    }

    private String findCompanies(String response) {
        StringBuilder result = new StringBuilder();
        int idx = 0;
        while (true) {
            int firstIdx = response.indexOf("\"name", idx);
            int lastIdx = response.indexOf("type", firstIdx);
            if (firstIdx == -1 || lastIdx == -1) {
                break;
            }
            firstIdx += 8;
            lastIdx -= 3;

            result.append(response, firstIdx, lastIdx);
            result.append('\n');
            idx = lastIdx;
        }
        return result.toString();
    }

    private String findBuildingId(String response) {
        int firstIdx = response.indexOf("id") + 5;
        int lastIdx = response.indexOf(",", firstIdx) - 1;
        return response.substring(firstIdx, lastIdx);
    }

    private String findCoordinates(String response) {
        int firstIdx = response.indexOf("lat");
        int lastIdx = response.indexOf("purpose_name") - 3;
        String substr = response.substring(firstIdx, lastIdx);
        String[] coordinates = substr.split(",");

        return coordinates[0].substring(coordinates[0].indexOf(":") + 1) +
                " " + coordinates[1].substring(coordinates[1].indexOf(":") + 1);
    }

    private String findRouteInformation(String response)
    {
        int unitFirstIdx = response.indexOf("unit") + "unit".length() + 3;
        int unitLastIdx = response.indexOf(",", unitFirstIdx) - 1;
        String unit = response.substring(unitFirstIdx, unitLastIdx);

        int distFirstIdx = response.indexOf("value") + "value".length() + 3;
        int distLastIdx = response.indexOf("}", distFirstIdx) - 1;
        String dist = response.substring(distFirstIdx, distLastIdx);

        int durFirstIdx = response.indexOf("ui_total_duration") + "ui_total_duration".length() + 3;
        int durLastIdx = response.indexOf(",", durFirstIdx) - 1;
        String dur = response.substring(durFirstIdx, durLastIdx);

        return "–ассто€ние маршрута: " + dist + " " + unit + "\nƒлительность маршрута: " + dur;
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
