package org.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage response = new SendMessage(); // Create a SendMessage object with mandatory fields
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText(proccessMessage(update.getMessage().getText()));

            try {
                execute(response); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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

    private String proccessMessage(String message) {
        message = message.charAt(0) != '/' ? message : message.substring(1);
        String data;
        try {
            data = Commands.valueOf(message).getData();
        } catch (IllegalArgumentException e) {
            data = Commands.wrong.getData();
        }
        return data;
    }
}