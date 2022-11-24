package org.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private String command;
    @Override
    public void onUpdateReceived(Update update) {
        Processing processing = new Processing();
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            String id = update.getMessage().getChatId().toString();
            message.setChatId(id);
            if (!HttpRequest.setRepeatCommand())
            {
                String text = update.getMessage().getText();
                command = text;
                String textToSend = processing.processMessage(text);
                message.setText(textToSend);
            }
            else
            {
                String text = update.getMessage().getText();
                String textToSend = processing.processMessage(command, text);
                message.setText(textToSend);
            }

            try {
                execute(message); // Call method to send the message
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