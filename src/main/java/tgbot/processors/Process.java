package tgbot.processors;

import tgbot.BotException;
import tgbot.Structs.Coordinates;
import tgbot.SearchCategories;
import tgbot.Structs.MessageContainer;
//import tgbot.Structs.User;

//import java.util.Map;
import java.util.Objects;

public class Process {


    public final MapApiProcess mapApiProcess = new MapApiProcess();
    private String command;

    private boolean cityCommand = false;

    public MessageContainer processing(String chatId, String text, Coordinates userGeolocation, String botToken) {
        if (userGeolocation != null) {
            return routeProcess(chatId, userGeolocation);
        }
        if (cityCommand) {
            cityCommand = false;
            mapApiProcess.setCity(text + ", ");
            return new MessageContainer(chatId, "�� �������� ����� �� " + text + "\n/help - ������ ���� ������");
        } else if (mapApiProcess.getRepeatCommand()) {
            return commandProcess(chatId, command, text, botToken);
        } else {
            command = text;
            return commandProcess(chatId, text, "", botToken);
        }
    }

    private MessageContainer commandProcess(String chatId, String text, String addr, String botToken) {
        if (!Objects.equals(addr, "")) {
            addr = mapApiProcess.getCity() + addr;
        }
        switch (text) {
            case "/start" -> {
                return new MessageContainer(chatId, """
                ��� ������������ 2gis ���, � ����:
                - �������� ����� ������� ��� ���� �����;
                - �������� �� ����� ����� �� ������;
                - �������� �� ������ ���������� �� ������������.
                
                /changecity - �������� ����� (������ ������������).
                """);
            }
            case "/help" -> {
                return new MessageContainer(chatId,
                """
                ������ ���� ������:
                /changecity - �������� �����
                /map - ������� �� ����� ����� �� ������
                /info - ������� �� ������ ���������� �� ������������
                /route - ����� ����� ������� ��� ���� �����
                """);
            }
            case "/changecity" -> { return changeCityProcess(chatId); }
            case "/map" -> { return mapDisplayProcess(chatId, addr, botToken); }
            case "/info" -> { return addrInfoProcess(chatId, addr); }
            case "/route" -> {
                return routeProcess(chatId, addr); }
            default -> { return new MessageContainer(chatId,"�������\\��������� �������!"); }
        }
    }

    private MessageContainer changeCityProcess(String chatId) {
        cityCommand = true;
        return new MessageContainer(chatId, "������� �����, � ������� �� ����������");
    }

    private MessageContainer mapDisplayProcess(String chatId, String address, String botToken) {
        try {
            String data = mapApiProcess.mapDisplay(botToken, chatId, address);
            if (data != null) {
                return new MessageContainer(chatId, data);
            }
            return null;
        } catch (BotException e) {
            return new MessageContainer(chatId, e.getMessage());
        }
    }

    private MessageContainer addrInfoProcess(String chatId, String address) {
        try {
            String info = mapApiProcess.addrInfo(address);
            return new MessageContainer(chatId, info);
        } catch (BotException e) {
            return new MessageContainer(chatId, e.getMessage());
        }
    }

    private MessageContainer routeProcess(String chatId, Coordinates geolocation) {
        try {
            String route = mapApiProcess.createRouteWithAddress(/*chatId,*/ geolocation);
            return new MessageContainer(chatId, route);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new MessageContainer(chatId, e.getMessage());
        }
    }

    private MessageContainer routeProcess(String chatId, String addr) {
        try {
            String route = mapApiProcess.createRouteWithAddress(/*chatId,*/ addr, SearchCategories.CAFE);
            return new MessageContainer(chatId, route, true);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new MessageContainer(chatId, e.getMessage());
        }
    }
}