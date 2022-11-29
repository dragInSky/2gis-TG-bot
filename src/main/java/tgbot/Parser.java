package tgbot;

import org.apache.http.ParseException;

public class Parser {
    public boolean findBadRequest(String response) {
        try {
            return !response.contains("code\":200");
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public int findDuration(String response) {
        try {
            int start = response.indexOf("total_distance");
            int finish = response.indexOf("type", start);
            String route = response.substring(start - 1, finish - 2);
            return Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public String findStatus(String response) {
        try {
            int firstIdx = response.indexOf("status") + "status".length() + 3;
            int lastIdx = response.indexOf(",", firstIdx) - 1;
            return response.substring(firstIdx, lastIdx);
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public String findBuildingId(String response) {
        try {
            int firstIdx = response.indexOf("id") + 5;
            int lastIdx = response.indexOf(",", firstIdx) - 1;
            return response.substring(firstIdx, lastIdx);
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public String findCompanies(String response) {
        try {
            StringBuilder result = new StringBuilder();
            int idx = 0;
            while (true) {
                int firstIdx = response.indexOf("\"name", idx);
                int lastIdx = response.indexOf("\",", firstIdx + "\"name".length() + 3);
                if (firstIdx == -1 || lastIdx == -1) {
                    break;
                }
                firstIdx += 8;

                result.append(response, firstIdx, lastIdx);
                result.append('\n');
                idx = lastIdx;
            }
            return result.toString();
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public String findRouteInformation(String response) {
        try {
            int unitFirstIdx = response.indexOf("unit") + "unit".length() + 3;
            int unitLastIdx = response.indexOf(",", unitFirstIdx) - 1;
            String unit = response.substring(unitFirstIdx, unitLastIdx);

            int distFirstIdx = response.indexOf("value") + "value".length() + 3;
            int distLastIdx = response.indexOf("}", distFirstIdx) - 1;
            String dist = response.substring(distFirstIdx, distLastIdx);

            int durFirstIdx = response.indexOf("ui_total_duration") + "ui_total_duration".length() + 3;
            int durLastIdx = response.indexOf(",", durFirstIdx) - 1;
            String dur = response.substring(durFirstIdx, durLastIdx);

            return "Расстояние маршрута: " + dist + " " + unit + "\nДлительность маршрута: " + dur;
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }

    public Coordinates findCoordinates(String response) {
        try {
            int latFirstIdx = response.indexOf("lat") + "lat".length() + 2;
            int latLastIdx = response.indexOf(",", latFirstIdx);

            int lonFirstIdx = response.indexOf("lon") + "lon".length() + 2;
            int lonLastIdx = response.indexOf("}", lonFirstIdx);

            return new Coordinates(
                    response.substring(latFirstIdx, latLastIdx), response.substring(lonFirstIdx, lonLastIdx)
            );
        } catch (Exception e) {
            throw new ParseException("Parse error");
        }
    }
}
