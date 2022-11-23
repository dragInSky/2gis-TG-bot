package org.bot;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Coordinates {
    private final double latitude;
    private final double longitude;

    Coordinates(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }
    Coordinates(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public double getLat() {
        return latitude;
    }
    public double getLon() {
        return longitude;
    }

    @Override
    public String toString() {
        return "lat=" + latitude + "&lon=" + longitude;
    }
}
