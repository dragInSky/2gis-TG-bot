package tgbot.processors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tgbot.BotException;
import tgbot.Structs.Coordinates;

import java.util.Objects;

public class Parser {
    public String findCode(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject meta = json.getJSONObject("meta");
            if (meta.getInt("code") == 200) {
                return "200";
            }
            JSONObject error = meta.getJSONObject("error");
            return error.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public int findDuration(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONArray("result").getJSONObject(0);
            return result.getInt("total_duration");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findStatus(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            return json.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findBuildingId(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            if (items.has("id")) {
                return items.getString("id");
            }
            throw new BotException("По этому адресу нет здания");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findCompanies(String response) throws BotException {
        try {
            StringBuilder companies = new StringBuilder();
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONArray items = result.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject company = items.getJSONObject(i);
                if (company.has("name")) {
                    companies.append(" - ").append(company.getString("name")).append("\n");
                }
            }

            if (!companies.isEmpty()) {
                return companies.toString();
            }
            throw new BotException("По этому адресу нет организаций");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findRouteInformation(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONArray("result").getJSONObject(0);
            JSONObject distance = result.getJSONObject("ui_total_distance");
            return "Расстояние маршрута: " + distance.getString("value") + " " + distance.getString("unit") +
                    "\nДлительность маршрута: " + result.getString("ui_total_duration");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public Coordinates findCoordinates(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            JSONObject point = items.getJSONObject("point");
            return new Coordinates(point.getDouble("lat"), point.getDouble("lon"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findAddress(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            return items.getString("full_name");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findPlaceAddress(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            return items.getString("address_name");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public String findPlaceInfo(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            String res = "";
            if (items.has("ads")) {
                JSONObject ads = items.getJSONObject("ads");
                res = "Описание: " + ads.getString("text") +
                        "\n" + ads.getString("article").
                        replace("<br />", " ").
                        replace("•&nbsp;", "");
            }
            return items.getString("name") + "\n" + res;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }

    public boolean findCityOnlyAddress(String response) throws BotException {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject result = json.getJSONObject("result");
            JSONObject items = result.getJSONArray("items").getJSONObject(0);
            return Objects.equals(items.getString("type"), "adm_div");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new BotException("Ошибка на стороне разработчика!");
        }
    }
}
