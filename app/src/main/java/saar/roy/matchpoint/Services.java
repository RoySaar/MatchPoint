package saar.roy.matchpoint;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eidan on 1/19/2018.
 */

class Services {
    public List<Court> getCourts() {
        List<Court> courts = new ArrayList<>();
        courts.add(new Court("Court1","Good Court",new LatLng(32.1698115, 34.826633)));
        courts.add(new Court("Court2","Bad Court",new LatLng(32.16641, 34.82591)));
        return courts;
    }
}
