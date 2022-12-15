package tgbot.structs;

public class User {
    private static final int RADIUS_OF_SEARCH = 400;
    private String firstAddr = "", secondAddr = "", middlePointPlaceAddress, map = "";
    private String city = "������������, ";

    //private String command;
    private Coordinates firstCoordinates = null, secondCoordinates = null;
    private boolean repeatCommand = false, middlePointOnMap = false, button = false, buttonDel = false;
    //private static int duration;

    public boolean getButton() {
        return button;
    }

    public boolean getButtonDel() {
        return buttonDel;
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

    /*public String getCommand(){
        return command;
    }*/

    public String getMap(){
        return map;
    }

    public String getFirstAddr(){
        return firstAddr;
    }

    public String getSecondAddr(){
        return secondAddr;
    }

    public String getMiddlePointPlaceAddress(){
        return middlePointPlaceAddress;
    }

    public Coordinates getFirstCoordinates(){
        return firstCoordinates;
    }

    public Coordinates getSecondCoordinates(){
        return secondCoordinates;
    }

    public void setCity(String newCity){
        city = newCity;
    }

    public void setMap(String newAddr){
        map = newAddr;
    }

   /* public void setCommand(String newCommand){
        command = newCommand;
    }*/

    public void setFirstAddr(String newAddr){
        firstAddr = newAddr;
    }

    public void setSecondAddr(String newAddr){
        secondAddr = newAddr;
    }

    public void setMiddlePointPlaceAddress(String newMiddlePoint){
        middlePointPlaceAddress = newMiddlePoint;
    }

    public void setFirstCoordinates(Coordinates newCoordinate){
        firstCoordinates = newCoordinate;
    }

    public void setSecondCoordinates(Coordinates newCoordinate){
        secondCoordinates = newCoordinate;
    }

    public void setRepeatCommand(boolean newBoolean){
        repeatCommand = newBoolean;
    }

    public void setMiddlePointOnMap(boolean newBoolean){
        middlePointOnMap = newBoolean;
    }

    public void setButton(boolean newBoolean){
        button = newBoolean;
    }

    public void setButtonDel(boolean newBoolean){
        buttonDel = newBoolean;
    }

    public void resetValues() {
        repeatCommand = false;
        button = false;
        buttonDel = false;
        firstCoordinates = null;
        secondCoordinates = null;
        firstAddr = "";
        secondAddr = "";
        map = "";
    }
}
