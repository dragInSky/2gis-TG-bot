package tgbot.structs;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Coordinates {
    private final double latitude;
    private final double longitude;
    public Coordinates(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }
    public Coordinates(Location location) {
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coordinates secondCoordinates = (Coordinates) obj;
        return this.latitude == secondCoordinates.latitude && this.longitude == secondCoordinates.longitude;
    }
    @Override
    public int hashCode() {
        return Double.hashCode(latitude) + Double.hashCode(longitude);
    }
    @Override
    public String toString() {
        return latitude + " " + longitude;
    }
}
