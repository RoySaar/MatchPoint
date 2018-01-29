package saar.roy.matchpoint;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Eidan on 1/19/2018.
 */

class Court {
    private double latitude;
    private double longitude;
    //private LatLng position;
    private String name;
    private String description;
    private double price;

    public Court(String description, String name, double latitude, double longitude,double price) {
        this.name = name;
        this.description = description;
        //this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
    }

    public Court() {
    }

    // public double getLatitude(){ return this.latitude; }

    // public double getLongitude(){ return this.longitude; }

    public LatLng getPosition() { return new LatLng(latitude,longitude); }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() { return price; }

    MarkerOptions toMarkerOptions() {
        return new MarkerOptions()
                .position(getPosition())
                .title(getName())
                .snippet(getDescription() + " - " + getPrice() + "₪");
    }

}