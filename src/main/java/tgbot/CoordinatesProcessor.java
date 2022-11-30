package tgbot;

import tgbot.Exceptions.HttpException;
import tgbot.Exceptions.MapApiException;
import tgbot.Exceptions.ParseException;

import java.util.ArrayList;

public class CoordinatesProcessor {
    private final ArrayList<Coordinates> coordinatesArray;

    CoordinatesProcessor(String route) {
        System.out.println(route);
        coordinatesArray = new ArrayList<>();
        int idx = 0;
        while (true) {
            int startIdx = route.indexOf("LINESTRING(", idx);
            int endIdx = route.indexOf(")", startIdx);
            if (startIdx == -1 || endIdx == -1) {
                break;
            }
            startIdx += "LINESTRING(".length();
            String substr = route.substring(startIdx, endIdx);
            String[] strArr = substr.split(",\s|\s");
            for (int i = 0; i + 1 < strArr.length; i += 2) {
                try {
                    coordinatesArray.add(
                            new Coordinates(Double.parseDouble(strArr[i + 1]), Double.parseDouble(strArr[i])));
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }

        for (Coordinates coord : coordinatesArray) {
            System.out.println(coord.toString());
        }
    }

    public Coordinates coordinatesProcess() throws HttpException, ParseException {
        MapApiProcess mapApiProcess = new MapApiProcess();
        int duration, minDur = Integer.MAX_VALUE;
        Coordinates middleCoordinate = null;
        Boolean bFlag = null;
        for (int i = coordinatesArray.size() / 2; i >= 0 && i < coordinatesArray.size();) {
            System.out.println(i);
            String route = mapApiProcess.createRouteWithCoordinates(coordinatesArray.get(i));
            try {
                duration = new Parser().findDuration(route);
            }
            catch (NumberFormatException e) {
                continue;
            }
            if (Math.abs(duration - mapApiProcess.getDuration() / 2) < minDur) {
                minDur = Math.abs(duration - mapApiProcess.getDuration() / 2);
                middleCoordinate = coordinatesArray.get(i);
            }
            if (bFlag != null && bFlag != (duration > mapApiProcess.getDuration() / 2)) {
                break;
            }
            bFlag = duration > mapApiProcess.getDuration() / 2;
            if (bFlag) {
                i--;
            } else {
                i++;
            }
        }

        return middleCoordinate;
    }
}
