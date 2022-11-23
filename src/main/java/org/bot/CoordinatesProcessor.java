package org.bot;

import java.util.ArrayList;

public class CoordinatesProcessor {
    private ArrayList<Coordinates> coordinatesArray;

    CoordinatesProcessor(String route) {
        coordinatesArray = new ArrayList<>();
        int idx = 0;
        while (true) {
            int startIdx = route.indexOf("LINESTRING(", idx);
            int endIdx = route.indexOf(")", startIdx);
            if (startIdx == -1 || endIdx == -1) {
                break;
            }
            String substr = route.substring(startIdx, endIdx);
            String[] strArr = substr.split("[ ,]");
            for (int i = 0; i + 1 < strArr.length; i += 2) {
                try {
                    coordinatesArray.add(new Coordinates(Double.parseDouble(strArr[i + 1]), Double.parseDouble(strArr[i])));
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }
    }

    public Coordinates coordinatesProcess() {
        HttpRequest httpRequest = new HttpRequest();
        int duration, minDur = Integer.MAX_VALUE;
        Coordinates averageCoordinate = null;
        for (Coordinates coordinate : coordinatesArray) {
            String route = httpRequest.createRouteWithCoordinates(coordinate);
            System.out.println(route);
            try {
                duration = Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));
            }
            catch (NumberFormatException e) {
                continue;
            }
            if (Math.abs(duration - httpRequest.getDuration() / 2) < minDur) {
                minDur = Math.abs(duration - httpRequest.getDuration() / 2);
                averageCoordinate = coordinate;
            }
        }

        return averageCoordinate;
    }
}
