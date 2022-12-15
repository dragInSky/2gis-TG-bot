package tgbot.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.BotException;
import tgbot.processors.HttpRequest;
import tgbot.processors.Parser;
import tgbot.structs.Coordinates;
//import tgbot.Structs.User;
import tgbot.processors.Process;
import tgbot.structs.MessageContainer;
import java.util.HashMap;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {
    //Менеджер потоков следит, чтобы каждому потоку выделялся свой обработчик команд
    private final Map<String, Process> managerOfThreads = new HashMap<>();
    private final Map<String, String> userCities;
    //Менеджер команд у пользователя
    //private final Map<String, String> managerOfThreadProcess = new HashMap<>();
    //Менеджер полей у пользователя
    //private final Map<String, User> managerOfThreadData = new HashMap<>();
    private final Button button = new Button();

    //private final ButtonMessage buttonMessage = new ButtonMessage();
    private Process process;
    private final HttpRequest httpRequest = new HttpRequest();
    private final Parser parser = new Parser();

    TelegramBot(Map<String, String> userCities) {
        this.userCities = userCities;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println("Starting: " + update.getMessage().getChatId());
        String chatId = update.getMessage().getChatId().toString();
        String text = "";
        Coordinates userGeolocation = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            text = update.getMessage().getText();
        } else if (update.getMessage().hasLocation()) {
            Location location = update.getMessage().getLocation();
            userGeolocation = new Coordinates(location);
        }
        mainLogic(chatId, text, userGeolocation);
//        try {
//            Thread.sleep(10_000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("Finishing: " + update.getMessage().getChatId());
    }

    private void mainLogic(String chatId, String text, Coordinates userGeolocation) {
        if (!managerOfThreads.containsKey(chatId)) {
            managerOfThreads.put(String.valueOf(chatId), new Process(parser, httpRequest));
            //managerOfThreadData.put(String.valueOf(chatId), new User());
        }

        process = managerOfThreads.get(chatId);

        if (userCities.containsKey(chatId)) {
            process.mapApiProcess.setCity(userCities.get(chatId));
        }

        MessageContainer messageData = process.processing(chatId, text, userGeolocation, getBotToken(), userCities);
        if (messageData != null) {
            sendMessage(messageData);
        }
    }

    public void sendMessage(MessageContainer messageData) {
        SendMessage message = new SendMessage(messageData.getChatId(), messageData.getData());

        if (process.mapApiProcess.getButton()) {
            button.setUpGeolocation(message);
        }
        if (process.mapApiProcess.getButtonDel() | process.mapApiProcess.getDelLast()) {
            button.removeKeyboard(message);
        }
        if (process.mapApiProcess.getRouteList()) {
            button.route(message);
        }
        if (process.mapApiProcess.getPlaceList()) {
            button.place(message);
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