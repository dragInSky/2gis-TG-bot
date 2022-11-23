package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Processing {
    private final HttpRequest request = new HttpRequest();

    private String addressToCoordinates(String addr) { //����� ��� ����������� ������ � �����������
        return request.sendGetGeo(addr);
    }
    private String coordinatesToAddress(Location location) { //���������� ��� ����������� Location � ������
        return coordinatesToAddress(new Coordinates(location));
    }
    private String coordinatesToAddress(Coordinates coordinates) { //���������� ��� ����������� Coordinates � ������
        return request.sendGetGeo(coordinates);
    }

    public void mapDisplay(String token, String id, String addr) {
        String coordinates = addressToCoordinates(addr);
        request.mapDisplay(token, id, coordinates);
    }
}