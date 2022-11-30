package tgbot;

import org.json.JSONArray;
import org.json.JSONObject;
import tgbot.Exceptions.ParseException;

public class Parser {
    public int findCode(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject meta = json.getJSONObject("meta");
            return meta.getInt("code");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public int findDuration(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            return result.getInt("total_duration");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public String findStatus(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            return json.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public String findBuildingId(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            return items.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public String findCompanies(String response) throws ParseException {
        try {
            StringBuilder companies = new StringBuilder();
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONArray items = result.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject company = items.getJSONObject(i);
                companies.append(" - ").append(company.getString("name")).append("\n");
            }

            return companies.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public String findRouteInformation(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONArray("result").getJSONObject(0);
            JSONObject distance = result.getJSONObject("ui_total_distance");
            return "Расстояние маршрута: " + distance.getString("value") + " " + distance.getString("unit") +
                    "\nДлительность маршрута: " + result.getString("ui_total_duration");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public Coordinates findCoordinates(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            JSONObject point = items.getJSONObject("point");
            return new Coordinates(point.getDouble("lat"), point.getDouble("lon"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }

    public String findBuildingName(String response) throws ParseException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            return items.getString("building_name");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("Ошибка на стороне разработчика!");
        }
    }
}
