package saar.roy.matchpoint;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eidan on 1/19/2018.
 */

class Court {
    private double latitude;
    private double longitude;
    private String name;
    private String description;

    public Court(String name, String description, LatLng latLng) {
        this.name = name;
        this.description = description;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public LatLng getPosition() {
        return new LatLng(latitude,longitude);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
