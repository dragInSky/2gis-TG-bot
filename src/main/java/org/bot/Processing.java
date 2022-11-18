package org.bot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class Processing {
    public Response processMessage(String message) {
        return switch (message) {
            case "/start" ->  new Response("Bot grats you!" +
                    "\nYou can write address, bot will display it on map" +
                    "\n/help - info about bot");
            case "/help" ->  new Response("You can write address, bot will display it on map" +
                    "\n/help - info about bot" +
                    "\n/map - place current location on map");
            case "/map" -> new Response("Enter address, you want to display on map:");
            default ->  new Response("", true);
        };
    }

    public String coordinates(String addr) throws Exception {
        HttpGetRequest request = new HttpGetRequest();
        return request.sendGetGeo(addr);
    }

    public void request(String token, String id, String addr) throws Exception {
        String data = coordinates(addr);
        try {
            String[] coordinates = data.split(" ");
            final String urlAdress = MessageFormat.format(
                    "https://api.telegram.org/bot{0}/sendlocation?chat_id={1}&latitude={2}&longitude={3}",
                    token, id, coordinates[0], coordinates[1]);
            URL url = new URL(urlAdress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
            con.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}