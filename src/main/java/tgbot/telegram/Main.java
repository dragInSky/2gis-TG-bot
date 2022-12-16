package tgbot.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        final Map<String, String> userCities = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("out/artifacts/consoleBot_jar/cities"))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    userCities.put(line.split(" : ")[0], line.split(" : ")[1]);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot(userCities));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}