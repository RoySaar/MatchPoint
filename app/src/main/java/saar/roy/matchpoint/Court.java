package saar.roy.matchpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.Locale;

/**
 * Created by Eidan on 1/19/2018.
 */

class Court {
    private GeoPoint position;
    private String name;
    private String description;
    private double price;

    public Court(String description, String name, GeoPoint position,double price) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.price = price;
    }

    public Court() {
    }

    public GeoPoint getPosition() { return position; }

    public LatLng getPositionAsLatLng() { return new LatLng(position.getLatitude(),position.getLatitude());}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() { return price; }

    MarkerOptions toMarkerOptions(Context context) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getDrawable(R.drawable.marker_icon);
        Bitmap b = bitmapDrawable.getBitmap();
        int ICON_HEIGHT = 210;
        int ICON_WIDTH = 110;
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, ICON_WIDTH, ICON_HEIGHT, false);
        return new MarkerOptions()
                .position(getPositionAsLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title(getName())
                .snippet(getDescription() + " - " + getPrice() + "₪");
    }

}