package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Processing {
    public static HttpRequest http = new HttpRequest();

    public String addressToCoordinates(String addr) { //����� ��� ����������� ������ � �����������
        return http.sendGetGeo(addr);
    }
    private String coordinatesToAddress(Location location) { //���������� ��� ����������� Location � ������
        return coordinatesToAddress(new Coordinates(location));
    }
    private String coordinatesToAddress(Coordinates coordinates) { //���������� ��� ����������� Coordinates � ������
        return http.sendGetGeo(coordinates);
    }

    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
        http.mapDisplay(token, id, coordinates);
    }
}