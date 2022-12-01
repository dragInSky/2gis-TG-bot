package tgbot;

import tgbot.Exceptions.HttpException;
import tgbot.Exceptions.MapApiException;
import tgbot.Exceptions.ParseException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private Coordinates userGeolocation = null;
    private final MapApiProcess mapApiProcess = new MapApiProcess();
    private String command;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (mapApiProcess.getRepeatCommand()) {
                commandProcess(update.getMessage(), command, text);
            } else {
                command = text;
                commandProcess(update.getMessage(), text, "");
            }
        } else { //значит нажата кнопка
            if (update.getMessage().hasLocation()) { //если были запрошены геоданные
                Location location = update.getMessage().getLocation();
                userGeolocation = new Coordinates(location);
            }
        }
    }

    private void commandProcess(Message msg, String message, String addr) {
        switch (message) {
            case "/start" -> sendMessage(msg,
                    """
                    2gis бот вас приветствует, € могу:
                    - по двум заданным пользователем адресам строить маршрут и выводить о нем информацию;
                    - выводить на карте место по адресу;
                    - выводить по адресу инфорацию об организаци€х.
                    /help - information about bot features
                    """);
            case "/help" ->  sendMessage(msg,
                    """
                    —писок моих команд:
                    /map - display place on map
                    /info - show information about address
                    /route - display information about route
                    """);
            case "/map" -> mapDisplayProcess(msg, msg.getChatId().toString(), addr);
            case "/info" -> addrInfoProcess(msg, addr);
            case "/route" -> routeProcess(msg, addr);
            default ->  sendMessage(msg,"¬ведите\\отправьте команду!");
        }
    }

    private void mapDisplayProcess(Message msg, String id, String address) {
        try {
            String data = mapApiProcess.mapDisplay(getBotToken(), id, address);
            if (data != null) {
                sendMessage(msg, data);
            }
        } catch (HttpException | MapApiException | ParseException e) {
            sendMessage(msg, e.getMessage());
        }
    }

    private void addrInfoProcess(Message msg, String address) {
        try {
            String info = mapApiProcess.addrInfo(address);
            sendMessage(msg, info);
        } catch (HttpException | MapApiException | ParseException e) {
            sendMessage(msg, e.getMessage());
        }
    }

    private void routeProcess(Message msg, String addr) {
        try {
            String route = mapApiProcess.createRouteWithAddress(addr);
            sendMessage(msg, route);
            if (mapApiProcess.getMiddlePointOnMap()) {
                mapApiProcess.coordinatesMapDisplay(getBotToken(), msg.getChatId().toString());
            }
        } catch (HttpException | MapApiException | ParseException e) {
            mapApiProcess.resetValues();
            sendMessage(msg, e.getMessage());
        }
    }

    private void sendMessage(Message msg, String data) {
        String chatId = msg.getChatId().toString();
        SendMessage message = new SendMessage(chatId, data);
        new Button().setUpGeolocation(message);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Coordinates getUserGeolocation() {
        return userGeolocation;
    }

    @Override
    public String getBotUsername() {
        return "maps_test_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TG_TOKEN");
    }
}