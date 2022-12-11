package tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private final Button button = new Button();
    private final Process process = new Process();

    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println("Starting: " + update.getMessage().getChatId());
        String chatId = update.getMessage().getChatId().toString(), text = "";
        Coordinates userGeolocation = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            text = update.getMessage().getText();
        } else if (update.getMessage().hasLocation()) {
            Location location = update.getMessage().getLocation();
            userGeolocation = new Coordinates(location);
        }
        Struct messageData = process.processing(chatId, text, userGeolocation, getBotToken());
        if (messageData != null) {
            sendMessage(messageData);
        }
//        try {
//            Thread.sleep(10_000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Finishing: " + update.getMessage().getChatId());
    }

    public void sendMessage(Struct messageData) {
        SendMessage message = new SendMessage(messageData.getChatId(), messageData.getData());

        if (process.mapApiProcess.getButton()) {
            button.setUpGeolocation(message);
        }
        if (process.mapApiProcess.getButtonDel()) {
            button.removeKeyboard(message);
        }

        try {
            execute(message);
            if (messageData.isFlag() && process.mapApiProcess.getMiddlePointOnMap()) {
                process.mapApiProcess.coordinatesMapDisplay(getBotToken(), messageData.getChatId());
            }
        } catch (TelegramApiException | BotException e) {
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