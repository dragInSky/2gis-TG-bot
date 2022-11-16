package org.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        Proccessing proccessing = new Proccessing();
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            String id = update.getMessage().getChatId().toString();
            message.setChatId(id);
            String text = update.getMessage().getText();
            Response response = proccessing.proccessMessage(text);
            message.setText(response.getData());

            try {
                if (response.getFlag()) {
                    proccessing.request(getBotToken(), id, 0d, 0d);
                } else {
                    execute(message); // Call method to send the message
                }
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
}