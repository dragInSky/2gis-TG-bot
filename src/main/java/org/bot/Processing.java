package org.bot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class Processing {
    public String coordinates(String addr) {
        HttpRequest request = new HttpRequest();
        return request.sendGetGeo(addr);
    }

    public void request(String token, String id, String addr) {
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