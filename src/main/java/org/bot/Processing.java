package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Processing {
    private final HttpRequest request = new HttpRequest();
    public static HttpRequest http = new HttpRequest();
    public String processMessage(String message, String addr) {
        return switch (message) {
            case "/start" ->  """
                    Bot grats you!
                    /help - information about bot features
                    /route - display information about route
                    """;
            case "/help" ->  """
                    /help - information about bot features
                    /route - display information about route
                    """;
            case "/route" -> http.sendPostRoute(addr);
            default ->  "Bot can reply only on commands";
        };
    }

        public String processMessage(String message){
            return switch (message) {
                case "/start" -> """
                        Bot grats you!
                        /help - information about bot features
                        /route - display information about route
                        """;
                case "/help" -> """
                        /help - information about bot features
                        /route - display information about route
                        """;
                case "/route" -> http.sendPostRoute("");
                default -> "Bot can reply only on commands";
            };
        }


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