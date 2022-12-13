package tgbot.Structs;

import tgbot.processors.HttpRequest;
import tgbot.processors.Parser;

public class User {
    private static final int RADIUS_OF_SEARCH = 400;
    private static String firstAddr = "", secondAddr = "", middlePointPlaceAddress;
    private String city = "Екатеринбург, ";
    private static Coordinates firstCoordinates = null, secondCoordinates = null;
    private static boolean repeatCommand = false, middlePointOnMap = false, button = false, buttonDel = false;
    //private static int duration;
    private final HttpRequest httpRequest = new HttpRequest();
    private final Parser parser = new Parser();
}
