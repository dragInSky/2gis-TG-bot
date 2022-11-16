package org.bot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class Processing {
    public Response processMessage(String message) {
        return switch (message) {
            case "/help" ->  new Response("/help - info about bot" +
                    "\n/location - place current location on map");
            case "/start" ->  new Response("Bot grats you!" +
                    "\n/help - info about bot");
            case "/location" -> new Response("", true);
            default ->  new Response("Bot can reply only on commands(");
        };
    }

    public void request(String token, String id, Double latitude, Double longitude) {
        try {
            final String urlAdress = MessageFormat.format(
                    "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}.6680&longitude={3}",
                    token, id, latitude, longitude); // url куда нужно совершить запрос
            URL url = new URL(urlAdress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode(); // совершаем запрос
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}