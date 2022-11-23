package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Processing {
    private final HttpRequest request = new HttpRequest();

    private String addressToCoordinates(String addr) { //метод для конвертации адреса к координатам
        return request.sendGetGeo(addr);
    }
    private String coordinatesToAddress(Location location) { //перегрузка для конвертации Location к адресу
        return coordinatesToAddress(new Coordinates(location));
    }
    private String coordinatesToAddress(Coordinates coordinates) { //перегрузка для конвертации Coordinates к адресу
        return request.sendGetGeo(coordinates);
    }

    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
        request.mapDisplay(token, id, coordinates);
    }
}