package tgbot;

import tgbot.Exceptions.HttpException;
import tgbot.Exceptions.MapApiException;
import tgbot.Exceptions.ParseException;

import java.text.MessageFormat;
import java.util.Objects;

public class MapApiProcess {
    private static String firstAddr = "", secondAddr = "";
    private static Coordinates firstCoordinates = null, secondCoordinates = null;
    private static boolean repeatCommand = false;
    private static int duration;
    //private static boolean button = false;
    private final HttpRequest httpRequest = new HttpRequest();
    private final Parser parser = new Parser();

    public boolean getRepeatCommand(){
        return repeatCommand;
    }
    //public static boolean getButton() { return button; }
    public int getDuration() { return duration; }

    public void resetValues() {
        repeatCommand = false;
        firstAddr = "";
        secondAddr = "";
    }

    private Coordinates addressToCoordinates(String addr) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (parser.findCode(response) != 200) {
            throw new MapApiException("Введен некорректный адрес: " + addr);
        }
        return parser.findCoordinates(response);
    }

    private String coordinatesToAddress(Coordinates point) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?lat={0}&lon={1}&fields=items.point&key={2}",
                point.getLat() + "", point.getLon() + "", get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (parser.findCode(response) != 200) {
            throw new MapApiException("Невозможно найти адрес средней точки: " + point);
        }
        return parser.findAddress(response);
    }

    public String createRouteWithAddress(String addr) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());

        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите первый адрес";
        }
        else if (Objects.equals(firstAddr, "")) {
            firstAddr = addr;
            firstCoordinates = addressToCoordinates(firstAddr);
            return "Введите второй адрес";
        }
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            secondCoordinates = addressToCoordinates(secondAddr);
        }

        if (Objects.equals(firstAddr, secondAddr)) {
            throw new MapApiException("Введите разные адреса!");
        }
        resetValues();

        String response = httpRequest.sendPost(url, firstCoordinates, secondCoordinates);
        if (Objects.equals(response, "")) { //не совсем понятно, когда это условие срабатывает
            throw new MapApiException("Маршрут не может быть построен!");
        }
        String status = parser.findStatus(response);
        if (!status.equals("OK")) {
            throw new MapApiException("Ошибка: " + status);
        }

        duration = parser.findDuration(response);
        //штука для поиска средней точки
        Coordinates middleCoordinate = new CoordinatesProcessor(response, firstCoordinates, secondCoordinates).
                coordinatesProcessEconom();
        //new CoordinatesProcessor(response, firstCoordinates, secondCoordinates);

        //return parser.findRouteInformation(response);
        return parser.findRouteInformation(response) + "\nСредняя точка маршрута: " + middleCoordinate +
                " (" + coordinatesToAddress(middleCoordinate) + ")"; //- вывод средней точки
    }

    public String createRouteWithCoordinates(Coordinates coordinates) throws HttpException { //штука для поиска средней точки
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());
        String response = httpRequest.sendPost(url, firstCoordinates, coordinates);
        if (response == null) {
            return "Unknown error";
        }
        return response;
    }

    public String mapDisplay(String token, String id, String addr) throws HttpException, MapApiException, ParseException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите адрес";
        }
        else {
            repeatCommand = false;
        }

        Coordinates coordinates = addressToCoordinates(addr);
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, coordinates.getLat() + "", coordinates.getLon() + "");
        httpRequest.sendGet(url);

        return null;
    }

    public String addrInfo(String addr) throws HttpException, MapApiException, ParseException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите адрес";
        }
        else {
            repeatCommand = false;
        }

        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(addr), get2GisGetKey());
        String response = httpRequest.sendGet(url);
        int code = parser.findCode(response);
        if (code != 200) {
            throw new MapApiException("Ошбика: " + code);
        }

        return "Название места: " +
                "\n - " + detailAddrInfo(addr) +
                "\nСписок организаций: " +
                "\n" + parser.findCompanies(response);
    }

    private String detailAddrInfo(String addr) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
            "https://catalog.api.2gis.com/3.0/items?q={0}&fields=items.address," +
                    "items.adm_div,items.floors,items.point,items.links,items.structure_info.apartments_count," +
                    "items.structure_info.material,items.structure_info.porch_count&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        int code = parser.findCode(response);
        if (code != 200) {
            throw new MapApiException("Ошбика: " + code);
        }

        return parser.findBuildingName(response);
    }

    private String buildingId(String addr) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=building&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (parser.findCode(response) != 200) {
            throw new MapApiException("Введен некорректный адрес: " + addr);
        }

        return parser.findBuildingId(response);
    }

    public String get2GisPostKey() {
        return System.getenv("2GIS_POST_KEY");
    }

    public String get2GisGetKey() {
        return System.getenv("2GIS_GET_KEY");
    }
}
