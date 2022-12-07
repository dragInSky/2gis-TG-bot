package tgbot;


import java.text.MessageFormat;
import java.util.Objects;

public class MapApiProcess {
    private static String firstAddr = "", secondAddr = "";
    private static Coordinates firstCoordinates = null, secondCoordinates = null, middlePoint;
    private static boolean repeatCommand = false, middlePointOnMap = false;

    //private static int duration;
    private static boolean button = false;

    private static boolean buttonDel = false;
    private final HttpRequest httpRequest = new HttpRequest();
    private final Parser parser = new Parser();

    public boolean getRepeatCommand(){
        return repeatCommand;
    }
    public boolean getMiddlePointOnMap(){
        return middlePointOnMap;
    }


    public boolean getButton() { return button; }

    public boolean getButtonDel() { return buttonDel; }


    //public int getDuration() { return duration; }

    public void resetValues() {
        repeatCommand = false;
        firstAddr = "";
        secondAddr = "";
        button = false;
        buttonDel = false;
    }

    private Coordinates addressToCoordinates(String addr) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        String code = parser.findCode(response);
        if (!Objects.equals(code, "200")) {
            throw new BotException("������ ������������ �����: " + addr);
        }
        return parser.findCoordinates(response);
    }

    public String coordinatesToAddress(Coordinates point) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?lat={0}&lon={1}&fields=items.point&key={2}",
                point.getLat() + "", point.getLon() + "", get2GisGetKey());
        String response = httpRequest.sendGet(url);
        String code = parser.findCode(response);
        if (!Objects.equals(code, "200")) {
            throw new BotException(code);
        }
        return parser.findAddress(response);
    }

    public String createRouteWithAddress(String addr) throws BotException {
        String url = MessageFormat.format(
                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
                get2GisPostKey());

        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            button = true;
            return "������� ������ �����";
        }
        else if (Objects.equals(firstAddr, "")) {
            firstAddr = addr;
            buttonDel = true;
            button = false;
            firstCoordinates = addressToCoordinates(firstAddr);
            return "������� ������ �����";
        }
        else if (Objects.equals(secondAddr, "")) {
            secondAddr = addr;
            secondCoordinates = addressToCoordinates(secondAddr);
        }

        if (Objects.equals(firstAddr, secondAddr)) {
            throw new BotException("������� ������ ������!");
        }
        resetValues();

        String response = httpRequest.sendPost(url, firstCoordinates, secondCoordinates);
        if (Objects.equals(response, "")) { //�� ������ �������, ����� ��� ������� �����������
            throw new BotException("������� �� ����� ���� ��������!");
        }
        String status = parser.findStatus(response);
        if (!status.equals("OK")) {
            throw new BotException("������: " + status);
        }

        //����� ��� ������ ������� �����
        //duration = parser.findDuration(response);
        middlePoint = new CoordinatesProcessor(response, firstCoordinates, secondCoordinates).
                coordinatesProcessEconom();
        middlePointOnMap = true;

        return parser.findRouteInformation(response) + "\n������� ����� ��������: " + middlePoint +
                " (" + coordinatesToAddress(middlePoint) + ")"; // ����� ������� �����
    }

//    public String createRouteWithCoordinates(Coordinates coordinates) throws HttpException { //����� ��� ������ ������� �����
//        String url = MessageFormat.format(
//                "https://routing.api.2gis.com/carrouting/6.0.1/global?key={0}",
//                get2GisPostKey());
//        String response = httpRequest.sendPost(url, firstCoordinates, coordinates);
//        if (response == null) {
//            return "����������� ������";
//        }
//        return response;
//    }

    public String mapDisplay(String token, String id, String addr) throws BotException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "������� �����";
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

    public void coordinatesMapDisplay(String token, String id) throws BotException {
        middlePointOnMap = false;
        String url = MessageFormat.format(
                "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                token, id, middlePoint.getLat() + "", middlePoint.getLon() + "");
        httpRequest.sendGet(url);
    }

    public String addrInfo(String addr) throws BotException {
        if (Objects.equals(addr, "")) {
            repeatCommand = true;
            return "������� �����";
        }
        else {
            repeatCommand = false;
        }

        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?building_id={0}&key={1}",
                buildingId(addr), get2GisGetKey());
        String response = httpRequest.sendGet(url);
        String code = parser.findCode(response);
        if (!Objects.equals(code, "200")) {
            throw new BotException(code);
        }

        return "������ �����������: " +
                "\n" + parser.findCompanies(response);
    }

    /*private String detailAddrInfo(String addr) throws HttpException, MapApiException, ParseException {
        String url = MessageFormat.format(
            "https://catalog.api.2gis.com/3.0/items?q={0}&fields=items.address," +
                    "items.adm_div,items.floors,items.point,items.links,items.structure_info.apartments_count," +
                    "items.structure_info.material,items.structure_info.porch_count&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        String code = parser.findCode(response);
        if (!Objects.equals(code, "200")) {
            throw new MapApiException(code);
        }

        return parser.findBuildingName(response);
    }*/

    private String buildingId(String addr) throws BotException {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items?q={0}&type=building&key={1}",
                addr, get2GisGetKey());
        String response = httpRequest.sendGet(url);
        String code = parser.findCode(response);
        if (!Objects.equals(code, "200")) {
            throw new BotException("������ ������������ �����: " + addr);
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
