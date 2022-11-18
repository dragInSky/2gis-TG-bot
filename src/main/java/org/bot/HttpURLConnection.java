package org.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;

public class HttpURLConnection {
    public static void main(String[] args) throws Exception {
        System.out.println(new HttpURLConnection().sendGetGeo("Москва, Садовническая, 25"));
    }

    public String sendGetGeo(String addr) throws Exception {
        String url = MessageFormat.format(
                "https://catalog.api.2gis.com/3.0/items/geocode?q={0}&fields=items.point&key={1}",
                addr, get2GisKey());
        return sendGet(url);
    }

    // HTTP GET request
    public String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        int firstIdx = response.indexOf("lat");
        int lastIdx = response.indexOf("purpose_name") - 3;
        String substr = response.substring(firstIdx, lastIdx);
        String[] coordinates = substr.split(",");

        return coordinates[0].substring(coordinates[0].indexOf(":") + 1) +
                " " + coordinates[1].substring(coordinates[1].indexOf(":") + 1);
    }

    public String get2GisKey() {
        return System.getenv("2GIS_KEY");
    }
}