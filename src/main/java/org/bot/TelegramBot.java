package org.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private Coordinates coordinates;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandProcess(update);
        } else { //значит нажата кнопка
            buttonProcess(update);
        }
    }

    private void commandProcess(Update update) {
        String messageText = update.getMessage().getText();
        switch (messageText) {
            case "/start" -> sendMessage(update,
                                """
                                Bot grats you!
                                /help - information about bot features
                                /route - display information about route
                                """);
            case "/help" ->  sendMessage(update,
                            """
                            /help - information about bot features
                            /route - display information about route
                            """);
            //case "/map" -> sendMessage(update,"Enter address, you want to display on map:");
            case "/route" -> sendMessage(update, new HttpRequest().sendPostRoute());
            default ->  sendMessage(update,"Bot can reply only on commands");
        }
    }

    private void buttonProcess(Update update) {
        sendMessage(update, "");
    }

    private void sendMessage(Update update, String text) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage message = new SendMessage(chatId, text);
        new Button().setUpGeolocation(message);

        if (update.getMessage().hasLocation()) { //если была запрошена геолокация
            Location location = update.getMessage().getLocation();
            coordinates = new Coordinates(location);
            message.setText(coordinates.toString());
        } else {
            message.setText(text);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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