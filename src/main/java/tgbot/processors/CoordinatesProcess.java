package tgbot.processors;

import tgbot.structs.Coordinates;
import java.util.ArrayList;

public class CoordinatesProcess {
    private final ArrayList<Coordinates> coordinatesArray;
    private final Coordinates m_firstCoordinates;

    CoordinatesProcess(String route, Coordinates firstCoordinates) {
        m_firstCoordinates = firstCoordinates;
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
                    Coordinates coordinates =
                            new Coordinates(Double.parseDouble(strArr[i + 1]), Double.parseDouble(strArr[i]));
                    if (!coordinatesArray.contains(coordinates)) {
                        coordinatesArray.add(coordinates);
                    }
                } catch (NumberFormatException ignored) {}
            }
            idx = endIdx;
        }
    }

    public Coordinates middleDistancePoint() {
        double allDistance = 0;
        ArrayList<Double> distances = new ArrayList<>();
        Coordinates prevCoord = m_firstCoordinates;
        for (int i = 5; i < coordinatesArray.size(); i++) {
            distances.add(Math.sqrt(Math.pow(coordinatesArray.get(i).getLat() - prevCoord.getLat(), 2) +
                    (Math.pow(coordinatesArray.get(i).getLon() - prevCoord.getLon(), 2))));
            allDistance += distances.get(i - 5);
            prevCoord = coordinatesArray.get(i);
        }
        allDistance /= 2;
        for (int i = 5; i < coordinatesArray.size(); i++) {
            allDistance -= distances.get(i - 5);
            if (allDistance < 0) {
                return coordinatesArray.get(i);
            }
        }
        return m_firstCoordinates;
    }
}
