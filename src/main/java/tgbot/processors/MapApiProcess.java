package tgbot.processors;

import tgbot.BotException;
import tgbot.structs.Coordinates;
import tgbot.SearchCategories;
import java.text.MessageFormat;
import java.util.Objects;

public class MapApiProcess {
    private static final int RADIUS_OF_SEARCH = 350;
    private String firstAddr = "", secondAddr = "", type = "", place = "",
            city = "Екатеринбург", middlePointPlaceAddress;
    private Coordinates firstCoordinates = null, secondCoordinates = null;
    private boolean repeatCommand = false, middlePointOnMap = false, button = false,
            buttonDel = false, routeList = false, placeList = false, delLast = true;
    private final HttpRequest httpRequest;
    private final Parser parser;

    MapApiProcess(Parser parser, HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.parser = parser;
    }

    public boolean getRepeatCommand(){
        return repeatCommand;
    }
    public boolean getMiddlePointOnMap(){
        return middlePointOnMap;
    }
    public String getCity(){
        return city;
    }
    public void setCity(String newCity) {
        city = newCity;
        button = false;
    }
    public boolean getButton() {
        return button;
    }
    public void setButton(boolean value) {
        button = value;
    }
    public boolean getButtonDel() {
        return buttonDel;
    }
    public void setButtonDel(boolean value) {
        buttonDel = value;
    }
    public boolean getRouteList(){
        return routeList;
    }
    public boolean getPlaceList(){
        return placeList;
    }
    public  boolean getDelLast(){
        return delLast;
    }

    public void resetValues() {
        repeatCommand = false;
        button = false;
        buttonDel = false;
        routeList = false;
        placeList = false;
        delLast = true;
        firstCoordinates = null;
        secondCoordinates = null;
        firstAddr = "";
        secondAddr = "";
        type = "";
        place = "";
    }

    private Coordinates addressToCoordinates(String addr) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (parser.findCityOnlyAddress(response) || !Objects.equals(parser.findCode(response), "200")) {
            throw new BotException("Введен некорректный адрес: " + addr);
        }
        return parser.findCoordinates(response);
    }

    public boolean notExistingCity(String city) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&key={1}",
                city, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        return !Objects.equals(parser.findCode(response), "200") || !parser.findCityOnlyAddress(response);
    }

    public String cityInPoint(Coordinates geolocation) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?lon={0}&lat={1}&" +
                        "fields=items.adm_div,items.address&type=adm_div.city&key={2}",
                geolocation.getLon() + "", geolocation.getLat() + "", get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (!Objects.equals(parser.findCode(response), "200")) {
            throw new BotException("По этому адресу нет города");
        }
        return parser.findCity(response);
    }

    public String createRouteWithAddress(Coordinates geolocation) throws BotException {
        buttonDel = true;
        button = false;
        firstCoordinates = geolocation;
        return "Введите второй адрес:";
    }

    public String createRouteWithAddress(String text) throws BotException {
        if (Objects.equals(text, "")) {
            repeatCommand = true;
            buttonDel = false;
            button = true;
            delLast = false;
            return "Введите первый адрес:";
        } else if (firstCoordinates == null) {
            buttonDel = true;
            button = false;
            firstAddr = city + ", " + text;
            firstCoordinates = addressToCoordinates(firstAddr);
            return "Введите второй адрес:";
        } else if (Objects.equals(secondAddr, "")) {
            secondAddr = city + ", " + text;
            routeList = true;
            buttonDel = false;
            secondCoordinates = addressToCoordinates(secondAddr);
            return "Выберите тип маршрута:";
        }

        if (Objects.equals(firstAddr, secondAddr)) {
            throw new BotException("Введите разные адреса!");
        }


        if (!buttonDel) {
            if (!text.equals("Пешком") & !text.equals("На машине") & !text.equals("Велосипед")) {
                return "Пожалуйста, выберите из списка";
            }
            type = text;
            buttonDel = true;
            routeList = false;
            placeList = true;
            return "Выберете место:";
        }

        if (type.equals("Пешком"))
            type = "pedestrian";
        else if (type.equals("На машине"))
            type = "jam";
        else
            type = "bicycle";

        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());
        String response = httpRequest.sendPost(url, firstCoordinates, secondCoordinates, type);

        if (Objects.equals(response, "")) { //не совсем понятно, когда это условие срабатывает
            System.out.println("response = \"\";");
            throw new BotException("Данный маршрут не может быть построен!");
        }
        String status = parser.findStatus(response);
        if (!status.equals("OK")) {
            System.out.println(status);
            throw new BotException("Данный маршрут не может быть построен!");
        }

        if (!text.equals("Кафе") & !text.equals("Парк") & !text.equals("Бар")) {
            return "Пожалуйста, выберите из списка";
        }

        place = text;

        SearchCategories search;
        if (place.equals("Кафе"))
            search = SearchCategories.CAFE;
        else if (place.equals("Парк"))
            search = SearchCategories.PARK;
        else
            search = SearchCategories.BAR;

        Coordinates middlePoint = new CoordinatesProcess(response, firstCoordinates).middleDistancePoint();
        resetValues();

        return parser.findRouteInformation(response) + "\n" + radiusSearch(middlePoint, search);
    }

    public String radiusSearch(Coordinates middlePoint, SearchCategories search) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=branch&point={1}%2C{2}&radius={3}&key={4}",
                search.getSearch(), middlePoint.getLon() + "", middlePoint.getLat() + "",
                RADIUS_OF_SEARCH, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (!Objects.equals(parser.findCode(response), "200")) {
            switch (search.getSearch()) {
                case "парк отдыха" -> {
                    return "Поблизости нет парков(";
                }
                case "бар" -> {
                    return "Поблизости нет баров(";
                }
                case "кафе" -> {
                    return "Поблизости нет кафе(";
                }
            }
        }
        middlePointOnMap = true;
        middlePointPlaceAddress = city + ", " + parser.findPlaceAddress(response);
        return "Место встречи: " + middlePointPlaceAddress +
                "\n— " + parser.findPlaceInfo(response);
    }

    public String mapDisplay(String token, String chatId, String addr) throws BotException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите адрес:";
        } else {
            repeatCommand = false;
        }

        Coordinates coordinates = addressToCoordinates(city + ", " + addr);
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, chatId, coordinates.getLat() + "", coordinates.getLon() + "");
        httpRequest.sendGet(url);

        return null;
    }

    public void coordinatesMapDisplay(String token, String chatId) throws BotException {
        middlePointOnMap = false;
        Coordinates coordinates = addressToCoordinates(middlePointPlaceAddress);
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, chatId, coordinates.getLat() + "", coordinates.getLon() + "");
        httpRequest.sendGet(url);
    }

    public String addrInfo(String addr) throws BotException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите адрес:";
        } else {
            repeatCommand = false;
        }
        addressToCoordinates(city + ", " + addr); //используется для проверки корректности адреса

        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(city + ", " + addr), get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (!Objects.equals(parser.findCode(response), "200")) {
            throw new BotException("По этому адресу нет организаций");
        }

        return "Список организаций:\n" + parser.findCompanies(response);
    }

    private String buildingId(String addr) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=building&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        if (!Objects.equals(parser.findCode(response), "200")) {
            throw new BotException("Введен некорректный адрес: " + addr);
        }

        return parser.findBuildingId(response);
    }

    public String get2GisPostKey() {
        return System.getenv("GIS_POST_KEY");
    }

    public String get2GisGetKey() {
        return System.getenv("GIS_GET_KEY");
    }
}
