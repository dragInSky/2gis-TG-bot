package org.bot;

import org.apache.http.HttpException;
import org.apache.http.ParseException;

import java.text.MessageFormat;
import java.util.Objects;

public class HttpProcess {
    private static String firstAddr = "", secondAddr = "";
    private static Coordinates firstCoordinates = null, secondCoordinates = null;
    private static int duration;
    private static boolean repeatCommand = false;
    //private static boolean button = false;
    private final HttpRequest httpRequest = new HttpRequest();

    public boolean getRepeatCommand(){
        return repeatCommand;
    }

    public void resetValues() {
        repeatCommand = false;
        firstAddr = "";
        secondAddr = "";
    }

    //public static boolean getButton(){return button;}

    public Coordinates addressToCoordinates(String addr) throws HttpException, AddressException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (response == null) {
            throw new HttpException("Unknown error");
        }
        if (isWrongGetRequest(response)) {
            throw new AddressException("¬веден некорректный адрес: " + addr);
        }
        return findCoordinates(response);
    }

    public String createRouteWithAddress(String addr) throws AddressException, HttpException {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());

        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите первый адрес";
        }
        else if (Objects.equals(firstAddr, "")) {
            firstAddr = addr;
            firstCoordinates = addressToCoordinates(firstAddr);
            return "¬ведите второй адрес";
        }
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            secondCoordinates = addressToCoordinates(secondAddr);
        }

        if (Objects.equals(firstAddr, secondAddr)) {
            throw new AddressException("¬ведите разные адреса!");
        }
        resetValues();

        String response = httpRequest.sendPost(url, firstCoordinates, secondCoordinates);
        if (response == null) {
            throw new HttpException("Unknown error");
        }

        String status = findStatus(response);
        if (!status.equals("OK")) {
            throw new HttpException(status);
        }

        duration = findDuration(response);

        //штука дл€ поиска средней точки
        //Coordinates middleCoordinate = new CoordinatesProcessor(response).coordinatesProcess();

        return findRouteInformation(response); //+ "\nMiddle point of route: " + middleCoordinate.toString(); - вывод средней точки
    }

//    public String createRouteWithCoordinates(Coordinates coordinates) { //штука дл€ поиска средней точки
//        String url = MessageFormat.format(
//                "https://routing.api.2gis.com/carrouting/6.0.0/global?key={0}",
//                get2GisPostKey());
//
//        String[] firstAddrInCoordinate = addressToCoordinates(firstAddr).split(" ");
//        String response = httpRequest.sendPost(url, firstAddrInCoordinate, coordinates.toString().split(" "));
//        if (response == null) {
//            return "Unknown error";
//        }
//        return findRouteInformation(response);
//    }

    public String mapDisplay(String token, String id, String addr) throws AddressException, HttpException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите адрес";
        }
        else {
            repeatCommand = false;
        }

        Coordinates coordinates = addressToCoordinates(addr);
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, coordinates.getLat(), coordinates.getLon());
        if (httpRequest.sendGet(url) == null) {
            throw new HttpException("Unknown error");
        }

        return "";
    }

    public String addrInfo(String addr) throws AddressException, HttpException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "¬ведите адрес";
        }
        else {
            repeatCommand = false;
        }

        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(addr), get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (response == null) {
            throw new HttpException("Unknown error");
        }
        if (isWrongGetRequest(response)) {
            throw new AddressException("¬веден некорректный адрес: " + addr);
        }

        return findCompanies(response);
    }

    private String buildingId(String addr) throws HttpException, AddressException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=building&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (response == null) {
            throw new HttpException("Unknown error");
        }
        if (isWrongGetRequest(response)) {
            throw new AddressException("¬веден некорректный адрес: " + addr);
        }
        return findBuildingId(response);
    }

    private boolean isWrongGetRequest(String response) {
        try {
            return !response.contains("code\":200");
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private String findStatus(String response) {
        try {
            int firstIdx = response.indexOf("status") + "status".length() + 3;
            int lastIdx = response.indexOf(",", firstIdx) - 1;
            return response.substring(firstIdx, lastIdx);
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private String findCompanies(String response) {
        try {
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
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private String findBuildingId(String response) {
        try {
            int firstIdx = response.indexOf("id") + 5;
            int lastIdx = response.indexOf(",", firstIdx) - 1;
            return response.substring(firstIdx, lastIdx);
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private Coordinates findCoordinates(String response) {
        try {
            int latFirstIdx = response.indexOf("lat") + "lat".length() + 2;
            int latLastIdx = response.indexOf(",", latFirstIdx);

            int lonFirstIdx = response.indexOf("lon") + "lat".length() + 2;
            int lonLastIdx = response.indexOf("}", lonFirstIdx);

            return new Coordinates(
                    response.substring(latFirstIdx, latLastIdx), response.substring(lonFirstIdx, lonLastIdx)
            );
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private String findRouteInformation(String response) {
        try {
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
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    private int findDuration(String response) {
        try {
            int start = response.indexOf("total_distance");
            int finish = response.indexOf("type", start);
            String route = response.substring(start - 1, finish - 2);
            return Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
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
