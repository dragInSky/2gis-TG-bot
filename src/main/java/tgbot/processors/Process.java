package tgbot.processors;

import tgbot.BotException;
import tgbot.structs.Coordinates;
import tgbot.structs.MessageContainer;
//import tgbot.Structs.User;
//import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class Process {
    public final MapApiProcess mapApiProcess;
    private String command;
    private boolean cityCommand = false;

    public Process(Parser parser, HttpRequest httpRequest) {
        mapApiProcess = new MapApiProcess(parser, httpRequest);
    }

    public MessageContainer processing(String chatId, String text, Coordinates userGeolocation,
                                       String botToken, Map<String, String> userCities) {
        if (cityCommand) {
            cityCommand = false;
            try {
                if (userGeolocation != null) {
                    text = mapApiProcess.cityInPoint(userGeolocation); //Передали город геолокацией
                } else if (mapApiProcess.notExistingCity(text)) {
                    return new MessageContainer(chatId, "Введено некорректное название города: " + text);
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
            return new MessageContainer(chatId, "Вы изменили город на " + text + "\n/help - список моих команд");
        } else if (userGeolocation != null) {
            return routeProcess(chatId, userGeolocation);
        } else if (mapApiProcess.getRepeatCommand()) {
            return commandProcess(chatId, command, text, botToken);
        } else {
            command = text;
            return commandProcess(chatId, text, "", botToken);
        }
    }

    private MessageContainer commandProcess(String chatId, String text, String addr, String botToken) {
        if (!Objects.equals(addr, "")) {
            addr = mapApiProcess.getCity() + ", " + addr;
        }
        switch (text) {
            case "/start" -> {
                return new MessageContainer(chatId,
                        """
                                Вас приветствует 2gis бот, я могу:
                                - находить место встречи для двух людей;
                                - выводить на карте место по адресу;
                                - выводить по адресу информацию об организациях.
                                """ +
                                "/changecity - поменять город (сейчас " + mapApiProcess.getCity() + ")." +
                                "\n/help - список моих команд.");
            }
            case "/help" -> {
                return new MessageContainer(chatId,
                        """
                                Список моих команд:
                                /changecity - поменять город
                                /map - вывести на карте место по адресу
                                /info - вывести по адресу информацию об организациях
                                /route - найти место встречи для двух людей
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
                return new MessageContainer(chatId, "Введите\\отправьте команду!");
            }
        }
    }

    private MessageContainer changeCityProcess(String chatId) {
        mapApiProcess.setButton(true);
        cityCommand = true;
        return new MessageContainer(chatId, "Введите город, в котором вы находитесь " +
                "(сейчас " + mapApiProcess.getCity() + ")");
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

    private MessageContainer routeProcess(String chatId, String text) {
        try {
            String route = mapApiProcess.createRouteWithAddress(/*chatId,*/ text);
            return new MessageContainer(chatId, route, true);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new MessageContainer(chatId, e.getMessage());
        }
    }
}