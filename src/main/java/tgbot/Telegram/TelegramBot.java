package tgbot.Telegram;

import tgbot.Coordinates;
import tgbot.Exceptions.AddressException;
import tgbot.Exceptions.HttpException;
import tgbot.Http.HttpProcess;
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
            String text = update.getMessage().getText();
            if (httpProcess.getRepeatCommand()) {
                commandProcess(update.getMessage(), command, text);
            } else {
                command = text;
                commandProcess(update.getMessage(), text, "");
            }
        } else { //������ ������ ������
            if (update.getMessage().hasLocation()) { //���� ���� ��������� ���������
                Location location = update.getMessage().getLocation();
                userGeolocation = new Coordinates(location);
            }
        }
    }

    private void commandProcess(Message msg, String message, String addr) {
        switch (message) {
            case "/start" -> sendMessage(msg,
                    """
                    Bot grats you!
                    /help - information about bot features
                    /map - display place on map
                    /info - show information about place
                    /route - display information about route
                    """);
            case "/help" ->  sendMessage(msg,
                    """
                    /help - information about bot features
                    /map - display place on map
                    /info - show information about address
                    /route - display information about route
                    """);
            case "/map" -> mapDisplayProcess(msg, msg.getChatId().toString(), addr);
            case "/info" -> addrInfoProcess(msg, addr);
            case "/route" -> routeProcess(msg, addr);
            default ->  sendMessage(msg,"Bot can reply only on commands");
        }
    }

    private void mapDisplayProcess(Message msg, String id, String address) {
        try {
            String data = httpProcess.mapDisplay(getBotToken(), id, address);
            if (data != null) {
                sendMessage(msg, data);
            }
        } catch (HttpException | AddressException e) {
            sendMessage(msg, e.getMessage());
        }
    }

    private void addrInfoProcess(Message msg, String address) {
        try {
            String info = httpProcess.addrInfo(address);
            sendMessage(msg, info);
        } catch (HttpException | AddressException e) {
            sendMessage(msg, e.getMessage());
        }
    }

    private void routeProcess(Message msg, String addr) {
        try {
            String route = httpProcess.createRouteWithAddress(addr);
            sendMessage(msg, route);
        } catch (HttpException | AddressException e) {
            httpProcess.resetValues();
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