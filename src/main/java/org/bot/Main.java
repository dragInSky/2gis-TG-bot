package org.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

//        Transport transport = new Transport();
//
//        String data = Commands.start.getData();
//        transport.write(data);
//
//        String userInput = null;
//        while (!Objects.equals(userInput, "kill")) {
//            userInput = transport.read();
//            try {
//                data = Commands.valueOf(userInput).getData();
//                transport.write(data);
//            } catch (IllegalArgumentException e) {
//                transport.write(Commands.wrong.getData());
//            }
//        }
//
//        data = Commands.finish.getData();
//        transport.write(data);
    }
}