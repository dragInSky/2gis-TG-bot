package tgbot;

//import tgbot.Exceptions.HttpException;
//import tgbot.Exceptions.ParseException;

import java.util.ArrayList;

public class CoordinatesProcessor {
    private final ArrayList<Coordinates> coordinatesArray;
    private final Coordinates m_firstCoordinates;
    private final double m_distance;

    CoordinatesProcessor(String route, Coordinates firstCoordinates, Coordinates secondCoordinates) {
        m_firstCoordinates = firstCoordinates;
        m_distance = Math.sqrt(Math.pow(firstCoordinates.getLat() - secondCoordinates.getLat(), 2) +
                        (Math.pow(firstCoordinates.getLon() - secondCoordinates.getLon(), 2)));
        coordinatesArray = new ArrayList<>();
        int idx = 0;
        int secondRouteIdx = route.indexOf("ui_total_distance");
        while (true) {
            int startIdx = route.indexOf("selection\":\"LINESTRING(", idx);
            int endIdx = route.indexOf(")", startIdx);
            if (startIdx == -1 || endIdx == -1 || endIdx >= secondRouteIdx) {
                break;
            }
            startIdx += "selection\":\"LINESTRING(".length();
            String substr = route.substring(startIdx, endIdx);
            String[] strArr = substr.split(",\s|\s");
            for (int i = 0; i + 1 < strArr.length; i += 2) {
                try {
                    Coordinates coordinates = new Coordinates(
                            Double.parseDouble(strArr[i + 1]), Double.parseDouble(strArr[i])
                    );
                    if (!coordinatesArray.contains(coordinates)) {
                        coordinatesArray.add(coordinates);
                    }
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }
    }

    public Coordinates coordinatesProcessEconom() {
        return coordinatesArray.get(middleDistancePoint());
    }

//    public Coordinates coordinatesProcess() throws HttpException, ParseException {
//        MapApiProcess mapApiProcess = new MapApiProcess();
//        int duration, minDur = Integer.MAX_VALUE;
//        Coordinates middleCoordinate = null;
//        Boolean bFlag = null;
//        for (int i = middleDistancePoint(), breaker = 0; breaker < 10 &&
//                i >= 0 && i < coordinatesArray.size(); breaker++) {
//            System.out.println(i);
//            String route = mapApiProcess.createRouteWithCoordinates(coordinatesArray.get(i));
//            try {
//                duration = new Parser().findDuration(route);
//            }
//            catch (NumberFormatException e) {
//                continue;
//            }
//            if (Math.abs(duration - mapApiProcess.getDuration() / 2) < minDur) {
//                minDur = Math.abs(duration - mapApiProcess.getDuration() / 2);
//                middleCoordinate = coordinatesArray.get(i);
//            }
//            if (bFlag != null && bFlag != (duration > mapApiProcess.getDuration() / 2)) {
//                break;
//            }
//            bFlag = duration > mapApiProcess.getDuration() / 2;
//            if (bFlag) {
//                i--;
//            } else {
//                i++;
//            }
//        }
//
//        return middleCoordinate;
//    }

    public int middleDistancePoint() {
        double distance, minDist = Double.MAX_VALUE;
        int idx = 0;
        Boolean bFlag = null;
        for (int i = 5; i < coordinatesArray.size(); i++) {
            distance = Math.sqrt(Math.pow(coordinatesArray.get(i).getLat() - m_firstCoordinates.getLat(), 2) +
                    (Math.pow(coordinatesArray.get(i).getLon() - m_firstCoordinates.getLon(), 2)));
            if (Math.abs(distance - m_distance / 2) < minDist) {
                minDist = Math.abs(distance - m_distance / 2);
                idx = i;
            }
            if (bFlag != null && bFlag != (distance > m_distance / 2)) {
                break;
            }
            bFlag = distance > m_distance / 2;
        }
        return idx;
    }
}
