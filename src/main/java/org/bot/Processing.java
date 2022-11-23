package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Processing {
    public static HttpRequest http = new HttpRequest();

    public String addressToCoordinates(String addr) { //метод для конвертации адреса к координатам
        return http.sendGetGeo(addr);
    }
    private String coordinatesToAddress(Location location) { //перегрузка для конвертации Location к адресу
        return coordinatesToAddress(new Coordinates(location));
    }
    private String coordinatesToAddress(Coordinates coordinates) { //перегрузка для конвертации Coordinates к адресу
        return http.sendGetGeo(coordinates);
    }

    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
        http.mapDisplay(token, id, coordinates);
    }
}