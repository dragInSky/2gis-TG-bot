package org.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private Coordinates userGeolocation = null;
    private final HttpProcess httpProcess = new HttpProcess();
    private String command;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandProcess(update.getMessage());
        } else { //значит нажата кнопка
            buttonProcess(update.getMessage());
        }
    }

    private void commandProcess(Message msg) {
        String text = msg.getText();
        if (!HttpProcess.setRepeatCommand()) {
            command = text;
            commandProcess(msg, text);
        }
        else {
            commandProcess(msg, command, text);
        }
    }

    private void commandProcess(Message msg, String message) {
        switch (message) {
            case "/start" -> sendMessage(msg,
                                """
                                Bot grats you!
                                /help - information about bot features
                                /map - display place on map
                                /route - display information about route
                                """);
            case "/help" ->  sendMessage(msg,
                            """
                            /help - information about bot features
                            /map - display place on map
                            /route - display information about route
                            """);
            //вторым параметром идет адрес дл€ вывода, как его получить - ?
            case "/map" -> mapDisplayProcess(msg.getChatId().toString(),"“урнегева 4, ≈катеринбург");
            case "/route" -> routeProcess(msg, "");
            default ->  sendMessage(msg,"Bot can reply only on commands");
        }
    }

    private void commandProcess(Message msg, String message, String addr) {
        switch (message) {
            case "/start" -> sendMessage(msg,
                    """
                    Bot grats you!
                    /help - information about bot features
                    /map - display place on map
                    /route - display information about route
                    """);
            case "/help" ->  sendMessage(msg,
                    """
                    /help - information about bot features
                    /map - display place on map
                    /route - display information about route
                    """);
            //вторым параметром идет адрес дл€ вывода, как его получить - ?
            case "/map" -> mapDisplayProcess(msg.getChatId().toString(),"“урнегева 4, ≈катеринбург");
            case "/route" -> routeProcess(msg, addr);
            default ->  sendMessage(msg,"Bot can reply only on commands");
        }
    }

    private void mapDisplayProcess(String id, String address) {
        httpProcess.mapDisplay(getBotToken(), id, address);
    }

    private void routeProcess(Message msg, String addr) {
        sendMessage(msg, httpProcess.createRouteWithAddress(addr));
    }

    private void buttonProcess(Message msg) {
        sendMessage(msg, "");
    }

    private void sendMessage(Message msg, String data) {
        String chatId = msg.getChatId().toString();
        SendMessage message = new SendMessage(chatId, data);
        new Button().setUpGeolocation(message);

        if (msg.hasLocation()) { //если была запрошена геолокаци€
            Location location = msg.getLocation();
            userGeolocation = new Coordinates(location);
            message.setText(userGeolocation.toString());
        }

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