package tgbot.processors;

import tgbot.BotException;
import tgbot.structs.Coordinates;
import tgbot.structs.MessageContainer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Process {
    private final MapApiProcess mapApiProcess;
    private String command;
    private boolean cityCommand = false;

    public MapApiProcess getMapApiProcess() {
        return mapApiProcess;
    }

    public Process(Parser parser, HttpRequest httpRequest) {
        mapApiProcess = new MapApiProcess(parser, httpRequest);
    }

    public MessageContainer processing(String chatId, String text, Coordinates userGeolocation,
                                       String botToken, Map<String, String> userCities) {
        if (cityCommand) {
            return afterChangingCity(chatId, text, userGeolocation, userCities);
        } else if (userGeolocation != null) {
            return routeProcess(chatId, userGeolocation);
        } else if (mapApiProcess.getRepeatCommand()) {
            return commandProcess(chatId, command, text, botToken);
        } else {
            command = text;
            return commandProcess(chatId, text, "", botToken);
        }
    }

    private MessageContainer afterChangingCity(String chatId, String text, Coordinates userGeolocation,
                                               Map<String, String> userCities) {
        cityCommand = false;
        try {
            if (userGeolocation != null) {
                text = mapApiProcess.cityInPoint(userGeolocation); //�������� ����� �����������
            } else if (mapApiProcess.notExistingCity(text)) {
                return new MessageContainer(chatId, "������� ������������ �������� ������: " + text);
            }
        } catch (BotException e) {
            return new MessageContainer(chatId, e.getMessage());
        }

        if (!userCities.containsKey(chatId)) {
            try (BufferedWriter bufferedWriter =
                         new BufferedWriter(new FileWriter("out/artifacts/consoleBot_jar/cities", true))) {
                String fileContent = chatId + " : " + text + "\n";
                bufferedWriter.write(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Path path = Paths.get("out/artifacts/consoleBot_jar/cities");
            try {
                String content = Files.readString(path);
                int startIdx = content.indexOf(chatId + " : ") + (chatId + " : ").length();
                int endIdx = content.indexOf("\n", startIdx);
                content = content.replace(content.substring(startIdx, endIdx), text);
                Files.writeString(path, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userCities.put(String.valueOf(chatId), text);

        mapApiProcess.setCity(text);
        mapApiProcess.setButtonDel(true);

        return new MessageContainer(chatId, "�� �������� ����� �� " + text + "\n/help - ������ ���� ������");
    }

    private MessageContainer commandProcess(String chatId, String text, String addr, String botToken) {
        switch (text) {
            case "/start" -> {
                return new MessageContainer(chatId,
                        """
                        ��� ������������ 2gis ���, � ����:
                        - �������� ����� ������� ��� ���� �����;
                        - �������� �� ����� ����� �� ������;
                        - �������� �� ������ ���������� �� ������������.
                        """ +
                        "/changecity - �������� ����� (������ " + mapApiProcess.getCity() + ")." +
                        "\n/help - ������ ���� ������.");
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
            case "/changecity" -> {
                return changeCityProcess(chatId);
            }
            case "/map" -> {
                return mapDisplayProcess(chatId, addr, botToken);
            }
            case "/info" -> {
                return addrInfoProcess(chatId, addr);
            }
            case "/route" -> {
                return routeProcess(chatId, addr);
            }
            default -> {
                return new MessageContainer(chatId, "�������\\��������� �������!");
            }
        }
    }

    private MessageContainer changeCityProcess(String chatId) {
        mapApiProcess.setButton(true);
        cityCommand = true;
        return new MessageContainer(chatId, "������� �����, � ������� �� ���������� " +
                "(������ " + mapApiProcess.getCity() + ")");
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
            String route = mapApiProcess.createRouteWithAddress(geolocation);
            return new MessageContainer(chatId, route);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new MessageContainer(chatId, e.getMessage());
        }
    }

    private MessageContainer routeProcess(String chatId, String text) {
        try {
            String route = mapApiProcess.createRouteWithAddress(text);
            return new MessageContainer(chatId, route, true);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new MessageContainer(chatId, e.getMessage());
        }
    }
}