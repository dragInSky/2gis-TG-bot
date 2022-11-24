package org.bot;

import java.util.ArrayList;

public class CoordinatesProcessor {
    private final ArrayList<Coordinates> coordinatesArray;

    CoordinatesProcessor(String route) {
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
                            new Coordinates(Double.parseDouble(strArr[i]), Double.parseDouble(strArr[i + 1])));
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }

        for (Coordinates coord : coordinatesArray) {
            System.out.println(coord.toString());
        }
    }

    public Coordinates coordinatesProcess() {
        HttpProcess httpProcess = new HttpProcess();
        int duration, minDur = Integer.MAX_VALUE;
        Coordinates middleCoordinate = null;
        Boolean bFlag = null;
        for (int i = coordinatesArray.size() / 2; i >= 0 && i < coordinatesArray.size();) {
            String route = httpProcess.createRouteWithCoordinates(coordinatesArray.get(i));
            try {
                duration = Integer.parseInt(route.substring(route.lastIndexOf(':') + 1));
            }
            catch (NumberFormatException e) {
                continue;
            }
            if (Math.abs(duration - httpProcess.getDuration() / 2) < minDur) {
                minDur = Math.abs(duration - httpProcess.getDuration() / 2);
                middleCoordinate = coordinatesArray.get(i);
            }
            if (bFlag != null && bFlag != (duration > httpProcess.getDuration() / 2)) {
                break;
            }
            bFlag = duration > httpProcess.getDuration() / 2;
            if (bFlag) {
                i--;
            } else {
                i++;
            }
        }

        return middleCoordinate;
    }
}
