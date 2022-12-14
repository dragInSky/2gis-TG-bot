package tgbot.processors;

import tgbot.BotException;
import tgbot.Structs.Coordinates;
import tgbot.SearchCategories;
import tgbot.Structs.User;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

public class MapApiProcess {

    MapApiProcess() {
        repeatCommand = false;
        middlePointOnMap = false;
        button = false;
        buttonDel = false;
        firstCoordinates = null;
        secondCoordinates = null;
        firstAddr = "";
        secondAddr = "";
        city = "Екатеринбург, ";
    }

    private static final int RADIUS_OF_SEARCH = 400;
    private static String firstAddr = "", secondAddr = "", middlePointPlaceAddress;
    private String city = "Екатеринбург, ";
    private static Coordinates firstCoordinates = null, secondCoordinates = null;
    private static boolean repeatCommand = false, middlePointOnMap = false, button = false, buttonDel = false;
    //private static int duration;
    private final HttpRequest httpRequest = new HttpRequest();
    private final Parser parser = new Parser();

    public boolean getRepeatCommand(){
        return repeatCommand;
    }
    public boolean getMiddlePointOnMap(){
        return middlePointOnMap;
    }


    public String getFirstAddr() {
        return firstAddr;
    }

    public String getSecondAddr() {
        return secondAddr;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String newCity){
        city = newCity;
    }
    public boolean getButton() {
        return button;
    }
    public boolean getButtonDel() {
        return buttonDel;
    }
    //public int getDuration() { return duration; }

    public void resetValues() {
        repeatCommand = false;
        button = false;
        buttonDel = false;
        firstCoordinates = null;
        secondCoordinates = null;
        firstAddr = "";
        secondAddr = "";
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

    public String createRouteWithAddress(String chatId, Coordinates geolocation) throws BotException {
        //managerOfThreadData.get(chatId).setButtonDel(true);
        //managerOfThreadData.get(chatId).setButton(false);
        //managerOfThreadData.get(chatId).setFirstCoordinates(geolocation);
        buttonDel = true;
        button = false;
        firstCoordinates = geolocation;
        return "Введите второй адрес";
    }

    public String createRouteWithAddress(String chatId,String addr, SearchCategories search) throws BotException {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());

        if (Objects.equals(addr, "")) {
            //managerOfThreadData.get(chatId).setRepeatCommand(true);
            //managerOfThreadData.get(chatId).setButton(true);
            repeatCommand = true;
            button = true;
            return "Введите первый адрес";
        }
        /*else if (managerOfThreadData.get(chatId).getFirstCoordinates() == null){
            managerOfThreadData.get(chatId).setButtonDel(true);
            managerOfThreadData.get(chatId).setButton(false);
            managerOfThreadData.get(chatId).setFirstAddr(addr);
            managerOfThreadData.get(chatId).setFirstCoordinates(addressToCoordinates(addr));*/
        else if (firstCoordinates == null) {
            buttonDel = true;
            button = false;
            firstAddr = addr;
            firstCoordinates = addressToCoordinates(firstAddr);
            return "Введите второй адрес";
        }

        /*else if (Objects.equals(managerOfThreadData.get(chatId).getSecondAddr(), "")){
            managerOfThreadData.get(chatId).setSecondAddr(addr);
            managerOfThreadData.get(chatId).setSecondCoordinates(addressToCoordinates(addr));
        }*/
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            secondCoordinates = addressToCoordinates(secondAddr);
        }

        /*if (Objects.equals(firstAddr, secondAddr)) {
            throw new BotException("Введите разные адреса!");
        }*/

        /*String response = httpRequest.sendPost(url, managerOfThreadData.get(chatId).getFirstCoordinates(),
                managerOfThreadData.get(chatId).getSecondCoordinates());*/


        String response = httpRequest.sendPost(url, firstCoordinates, secondCoordinates);

        if (Objects.equals(response, "")) { //не совсем понятно, когда это условие срабатывает
            System.out.println("response = \"\";");
            throw new BotException("Данный маршрут не может быть построен!");
        }
        String status = parser.findStatus(response);
        if (!status.equals("OK")) {
            System.out.println(status);
            throw new BotException("Данный маршрут не может быть построен!");
        }

        //duration = parser.findDuration(response);

        /*Coordinates middlePoint = new CoordinatesProcess(response,
                managerOfThreadData.get(chatId).getFirstCoordinates(),
                managerOfThreadData.get(chatId).getSecondCoordinates()).
                coordinatesProcess();*/

        Coordinates middlePoint = new CoordinatesProcess(response, firstCoordinates, secondCoordinates).
                coordinatesProcess();
        middlePointOnMap = true;

        String middlePointPlace = radiusSearch(middlePoint, search);

        //managerOfThreadData.get(chatId).resetValues();
        resetValues();
        return parser.findRouteInformation(response) + "\nmiddle point(debug): " + middlePoint + "\n" + middlePointPlace;
    }

    public String radiusSearch(Coordinates middlePoint, SearchCategories search) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=branch&point={1}%2C{2}&radius={3}&key={4}",
                search.getSearch(), middlePoint.getLon() + "", middlePoint.getLat() + "",
                RADIUS_OF_SEARCH, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        middlePointPlaceAddress = city + parser.findPlaceAddress(response);
        return "Место встречи: " + middlePointPlaceAddress +
                "\n— " + parser.findPlaceInfo(response);
    }

    public String mapDisplay(String token, String chatId, String addr) throws BotException {
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
                token, chatId, coordinates.getLat() + "", coordinates.getLon() + "");
        httpRequest.sendGet(url);

        return null;
    }

    public void coordinatesMapDisplay(String token, String id) throws BotException {
        middlePointOnMap = false;
        Coordinates coordinates = addressToCoordinates(middlePointPlaceAddress);
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, coordinates.getLat() + "", coordinates.getLon() + "");
        httpRequest.sendGet(url);
    }

    public String addrInfo(String addr) throws BotException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "Введите адрес";
        }
        else {
            repeatCommand = false;
        }
        addressToCoordinates(addr);

        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(addr), get2GisGetKey());
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
        return System.getenv("2GIS_POST_KEY");
    }

    public String get2GisGetKey() {
        return System.getenv("2GIS_GET_KEY");
    }
}
