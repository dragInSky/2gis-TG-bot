package tgbot;

import java.util.Objects;

public class Process {
    public final MapApiProcess mapApiProcess = new MapApiProcess();
    private String command;
    private boolean cityCommand = false;

    public Struct processing(String chatId, String text, Coordinates userGeolocation, String botToken) {
        if (userGeolocation != null) {
            return routeProcess(chatId, userGeolocation);
        }
        if (cityCommand) {
            cityCommand = false;
            mapApiProcess.setCity(text + ", ");
            return new Struct(chatId, "Вы изменили город на " + text + "\n/help - список моих команд");
        } else if (mapApiProcess.getRepeatCommand()) {
            return commandProcess(chatId, command, text, botToken);
        } else {
            command = text;
            return commandProcess(chatId, text, "", botToken);
        }
    }

    private Struct commandProcess(String chatId, String text, String addr, String botToken) {
        if (!Objects.equals(addr, "")) {
            addr = mapApiProcess.getCity() + addr;
        }
        switch (text) {
            case "/start" -> { return startCommandProcess(chatId); }
            case "/help" -> {
                return new Struct(chatId,
                        """
                        Список моих команд:
                        /changecity - поменять город
                        /map - вывести на карте место по адресу
                        /info - вывести по адресу информацию об организациях
                        /route - найти место встречи для двух людей
                        """);
            }
            case "/changecity" -> { return changeCityProcess(chatId); }
            case "/map" -> { return mapDisplayProcess(chatId, addr, botToken); }
            case "/info" -> { return addrInfoProcess(chatId, addr); }
            case "/route" -> { return routeProcess(chatId, addr); }
            default -> { return new Struct(chatId,"Введите\\отправьте команду!"); }
        }
    }

    private Struct startCommandProcess(String chatId) {
        return new Struct(chatId, """
                Вас приветствует 2gis бот, я могу:
                - находить место встречи для двух людей;
                - выводить на карте место по адресу;
                - выводить по адресу информацию об организациях.
                
                /changecity - поменять город (сейчас Екатеринбург).
                """);
    }

    private Struct changeCityProcess(String chatId) {
        cityCommand = true;
        return new Struct(chatId, "Введите город, в котором вы находитесь");
    }

    private Struct mapDisplayProcess(String chatId, String address, String botToken) {
        try {
            String data = mapApiProcess.mapDisplay(botToken, chatId, address);
            if (data != null) {
                return new Struct(chatId, data);
            }
            return null;
        } catch (BotException e) {
            return new Struct(chatId, e.getMessage());
        }
    }

    private Struct addrInfoProcess(String chatId, String address) {
        try {
            String info = mapApiProcess.addrInfo(address);
            return new Struct(chatId, info);
        } catch (BotException e) {
            return new Struct(chatId, e.getMessage());
        }
    }

    private Struct routeProcess(String chatId, Coordinates geolocation) {
        try {
            String route = mapApiProcess.createRouteWithAddress(geolocation);
            return new Struct(chatId, route);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new Struct(chatId, e.getMessage());
        }
    }

    private Struct routeProcess(String chatId, String addr) {
        try {
            String route = mapApiProcess.createRouteWithAddress(addr, SearchCategories.CAFE);
            return new Struct(chatId, route, true);
        } catch (BotException e) {
            mapApiProcess.resetValues();
            return new Struct(chatId, e.getMessage());
        }
    }
}