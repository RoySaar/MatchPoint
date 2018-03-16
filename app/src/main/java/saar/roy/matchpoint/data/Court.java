package saar.roy.matchpoint.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import saar.roy.matchpoint.R;

/**
 * Created by Eidan on 1/19/2018.
 */

public class Court {
    private GeoPoint position;
    private String name;
    private String description;
    private double price;

    public Court() {
    }

    public GeoPoint getPosition() {
        return position;
    }

    public LatLng getPositionAsLatLng() { return new LatLng(position.getLatitude(),position.getLongitude());}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() { return price; }

    public MarkerOptions toMarkerOptions(Bitmap smallMarker) {
        return new MarkerOptions()
                .position(getPositionAsLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title(getName())
                .snippet(getDescription() + " - " + getPrice() + "₪");
    }

}